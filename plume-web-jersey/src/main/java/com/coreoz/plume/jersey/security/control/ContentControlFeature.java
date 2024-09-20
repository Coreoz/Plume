package com.coreoz.plume.jersey.security.control;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContentControlFeature implements DynamicFeature {

    public static final int DEFAULT_MAX_SIZE = 500 * 1024; // 500 KB
    private final Integer maxSize;

    public ContentControlFeature(int maxSize) {
        this.maxSize = maxSize;
    }

    public ContentControlFeature() {
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    public Integer getContentSizeLimit() {
        if (maxSize == null) {
            return DEFAULT_MAX_SIZE;
        }
        return maxSize;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        addContentSizeFilter(resourceInfo.getResourceMethod(), context);
    }

    private void addContentSizeFilter(AnnotatedElement annotatedElement, FeatureContext methodResourcecontext) {
        ContentSizeLimit contentSizeLimit = annotatedElement.getAnnotation(ContentSizeLimit.class);
        methodResourcecontext.register(new ContentSizeLimitInterceptor(
                contentSizeLimit != null ? contentSizeLimit.value() : maxSize
            ));
    }

    public static class ContentSizeLimitInterceptor implements ReaderInterceptor {

        private final int maxSize;

        public ContentSizeLimitInterceptor(int maxSize) {
            this.maxSize = maxSize;
        }

        // https://stackoverflow.com/questions/24516444/best-way-to-make-jersey-2-x-refuse-requests-with-incorrect-content-length
        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {
            Integer headerContentLength;
            try {
                headerContentLength = Integer.parseInt(context.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
            } catch (NumberFormatException e) {
                headerContentLength = maxSize; // default value for GET or chunked body
            }
            if (headerContentLength > maxSize) {
                throw new WebApplicationException(
                    Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE)
                            .entity("Content size limit exceeded.")
                            .build()
                );
            }

            final InputStream contextInputStream = context.getInputStream();
            context.setInputStream(new SizeLimitingInputStream(contextInputStream, headerContentLength));

            return context.proceed();
        }

        public static final class SizeLimitingInputStream extends InputStream {
            private long length = 0;
            private int mark = 0;

            private final int maxSize;

            private final InputStream delegateInputStream;

            public SizeLimitingInputStream(InputStream delegateInputStream, int maxSize) {
                this.delegateInputStream = delegateInputStream;
                this.maxSize = maxSize;
            }

            @Override
            public int read() throws IOException {
                final int read = delegateInputStream.read();
                readAndCheck(read != -1 ? 1 : 0);
                return read;
            }

            @Override
            public int read(final byte[] b) throws IOException {
                final int read = delegateInputStream.read(b);
                readAndCheck(read != -1 ? read : 0);
                return read;
            }

            @Override
            public int read(final byte[] b, final int off, final int len) throws IOException {
                final int read = delegateInputStream.read(b, off, len);
                readAndCheck(read != -1 ? read : 0);
                return read;
            }

            @Override
            public long skip(final long n) throws IOException {
                final long skip = delegateInputStream.skip(n);
                readAndCheck(skip != -1 ? skip : 0);
                return skip;
            }

            @Override
            public int available() throws IOException {
                return delegateInputStream.available();
            }

            @Override
            public void close() throws IOException {
                delegateInputStream.close();
            }

            @Override
            public synchronized void mark(final int readlimit) {
                mark += readlimit;
                delegateInputStream.mark(readlimit);
            }

            @Override
            public synchronized void reset() throws IOException {
                this.length = 0;
                readAndCheck(mark);
                delegateInputStream.reset();
            }

            @Override
            public boolean markSupported() {
                return delegateInputStream.markSupported();
            }

            private void readAndCheck(final long read) {
                this.length += read;

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
