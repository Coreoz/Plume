package com.coreoz.plume.jersey.security.control;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ContentControlFeature implements DynamicFeature {

    private static final Logger logger = LoggerFactory.getLogger(ContentControlFeature.class);

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        addContentSizeFilter(resourceInfo.getResourceMethod(), context);
    }

    private void addContentSizeFilter(AnnotatedElement annotatedElement, FeatureContext methodResourcecontext) {
        ContentSizeLimit contentSizeLimit = annotatedElement.getAnnotation(ContentSizeLimit.class);
        methodResourcecontext.register(new ContentSizeLimitInterceptor(
                contentSizeLimit != null ? contentSizeLimit.value() : ContentSizeLimitInterceptor.DEFAULT_MAX_SIZE
            ));
    }

    public static class ContentSizeLimitInterceptor implements ReaderInterceptor {

        private static final int DEFAULT_MAX_SIZE = 500;

        private final int maxSize;

        public ContentSizeLimitInterceptor(int maxSize) {
            this.maxSize = maxSize;
        }

        // https://stackoverflow.com/questions/24516444/best-way-to-make-jersey-2-x-refuse-requests-with-incorrect-content-length
        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {
            final InputStream contextInputStream = context.getInputStream();
            final String headerContentLength = context.getHeaders().getFirst("Content-Length");
            final Long declaredContentLength = headerContentLength == null ? -1 : Long.valueOf(headerContentLength);

            context.setInputStream(new ContentSizeLimitInputStream(contextInputStream, declaredContentLength, maxSize));

            final Object entity = context.proceed();
            context.setInputStream(contextInputStream);

            return entity;
        }

        public static final class ContentSizeLimitInputStream extends InputStream {
            private long length = 0;
            private int mark = 0;

            private final long declaredContentLength;
            private final int maxSize;

            private final InputStream contextInputStream;

            public ContentSizeLimitInputStream(InputStream contextInputStream, long declaredContentLength, int maxSize) {
                this.contextInputStream = contextInputStream;
                this.declaredContentLength = declaredContentLength;
                this.maxSize = maxSize;
            }

            @Override
            public int read() throws IOException {
                final int read = contextInputStream.read();
                readAndCheck(read != -1 ? 1 : 0);
                return read;
            }

            @Override
            public int read(final byte[] b) throws IOException {
                final int read = contextInputStream.read(b);
                readAndCheck(read != -1 ? read : 0);
                return read;
            }

            @Override
            public int read(final byte[] b, final int off, final int len) throws IOException {
                final int read = contextInputStream.read(b, off, len);
                readAndCheck(read != -1 ? read : 0);
                return read;
            }

            @Override
            public long skip(final long n) throws IOException {
                final long skip = contextInputStream.skip(n);
                readAndCheck(skip != -1 ? skip : 0);
                return skip;
            }

            @Override
            public int available() throws IOException {
                return contextInputStream.available();
            }

            @Override
            public void close() throws IOException {
                contextInputStream.close();
            }

            @Override
            public synchronized void mark(final int readlimit) {
                mark += readlimit;
                contextInputStream.mark(readlimit);
            }

            @Override
            public synchronized void reset() throws IOException {
                this.length = 0;
                readAndCheck(mark);
                contextInputStream.reset();
            }

            @Override
            public boolean markSupported() {
                return contextInputStream.markSupported();
            }

            private void readAndCheck(final long read) {
                this.length += read;

                if (this.length > declaredContentLength) {
                    try {
                        this.close();
                    } catch (IOException e) {
                        logger.error("Error while closing the input stream", e);
                    }
                    throw new WebApplicationException(
                        Response.status(Response.Status.LENGTH_REQUIRED)
                                .entity("Incorrect content-length provided.")
                                .build()
                    );
                }
                if (this.length > maxSize) {
                    try {
                        this.close();
                    } catch (IOException e) {
                        logger.error("Error while closing the input stream", e);
                    }
                    throw new WebApplicationException(
                        Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE)
                                .entity("Content size limit exceeded.")
                                .build()
                    );
                }
            }
        }
    }
}
