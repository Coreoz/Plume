package com.coreoz.plume.jersey.security.size;

import com.coreoz.plume.jersey.errors.WsError;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;

@Slf4j
public class ContentSizeLimitFeature implements DynamicFeature {
    public static final int DEFAULT_MAX_SIZE = 500 * 1024; // 500 KB
    static final String JSON_ENTITY_TOO_LARGE_ERROR = "{\"errorCode\":\""+WsError.CONTENT_SIZE_LIMIT_EXCEEDED.name()+"\",\"statusArguments\":[]}";
    private final Integer maxSize;

    public ContentSizeLimitFeature(int maxSize) {
        this.maxSize = maxSize;
    }

    public ContentSizeLimitFeature() {
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    public Integer getContentSizeLimit() {
        return maxSize;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        addContentSizeFilter(resourceInfo.getResourceMethod(), context);
    }

    private void addContentSizeFilter(AnnotatedElement annotatedElement, FeatureContext methodResourceContext) {
        ContentSizeLimit contentSizeLimit = annotatedElement.getAnnotation(ContentSizeLimit.class);
        methodResourceContext.register(new ContentSizeLimitInterceptor(
            contentSizeLimit != null ? contentSizeLimit.value() : maxSize
        ));
    }

    public static class ContentSizeLimitInterceptor implements ContainerRequestFilter {
        private final int maxSize;

        public ContentSizeLimitInterceptor(int maxSize) {
            this.maxSize = maxSize;
        }

        // https://stackoverflow.com/questions/24516444/best-way-to-make-jersey-2-x-refuse-requests-with-incorrect-content-length
        @Override
        public void filter(ContainerRequestContext context) {
            int headerContentLength = maxSize; // default value for GET or chunked body
            String contentLengthHeader = context.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH);
            if (contentLengthHeader != null) {
                try {
                    headerContentLength = Integer.parseInt(contentLengthHeader);
                } catch (NumberFormatException e) {
                    logger.warn("Wrong content length header received: {}", contentLengthHeader);
                }
            }

            if (headerContentLength > maxSize) {
                throw makeEntityTooLargeException();
            }

            final InputStream contextInputStream = context.getEntityStream();
            context.setEntityStream(new SizeLimitingInputStream(contextInputStream, headerContentLength));
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
                readAndCheck(skip);
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
                    throw makeEntityTooLargeException();
                }
            }
        }

        private static RuntimeException makeEntityTooLargeException() {
            return new ClientErrorException(Response
                .status(Response.Status.REQUEST_ENTITY_TOO_LARGE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                // We use a string response directly because Jersey does not accept an objet here (it would return a 500 error)
                .entity(JSON_ENTITY_TOO_LARGE_ERROR)
                .build()
            );
        }
    }
}
