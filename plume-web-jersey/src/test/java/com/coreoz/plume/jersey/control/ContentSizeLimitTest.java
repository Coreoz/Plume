package com.coreoz.plume.jersey.control;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.jersey.WebJerseyTestModule;
import com.coreoz.plume.jersey.security.control.ContentControlFeature.ContentSizeLimitInterceptor.ContentSizeLimitInputStream;

@RunWith(GuiceTestRunner.class)
@GuiceModules(WebJerseyTestModule.class)
public class ContentSizeLimitTest {

    private static final int LIMIT = 10;
    private ByteArrayInputStream byteArrayInputStream;
    private ContentSizeLimitInputStream contentSizeLimitInputStream;

    @After
    public void tearDown() throws IOException {
        if (contentSizeLimitInputStream != null) {
            contentSizeLimitInputStream.close();
        }
    }

    @Test
    public void test_read_within_limit() throws IOException {
        byte[] data = "12345".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(data);
        contentSizeLimitInputStream = new ContentSizeLimitInputStream(byteArrayInputStream, data.length, LIMIT);

        byte[] buffer = new byte[data.length];
        int bytesRead = contentSizeLimitInputStream.read(buffer);

        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void test_read_beyond_limit() {
        byte[] data = "12345678901".getBytes(); // 11 bytes, 1 byte over the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        contentSizeLimitInputStream = new ContentSizeLimitInputStream(byteArrayInputStream, data.length, LIMIT);

        byte[] buffer = new byte[data.length];
        assertThrows(WebApplicationException.class, () -> {
            contentSizeLimitInputStream.read(buffer);
        });
    }

    @Test
    public void test_read_exactly_at_limit() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        contentSizeLimitInputStream = new ContentSizeLimitInputStream(byteArrayInputStream, data.length, LIMIT);

        byte[] buffer = new byte[data.length];
        int bytesRead = contentSizeLimitInputStream.read(buffer);

        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void test_read_from_empty_stream() throws IOException {
        byte[] data = "".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(data);
        contentSizeLimitInputStream = new ContentSizeLimitInputStream(byteArrayInputStream, data.length, LIMIT);

        byte[] buffer = new byte[10];
        int bytesRead = contentSizeLimitInputStream.read(buffer);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void test_incorrect_content_length() {
        byte[] data = "12345".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(data);
        contentSizeLimitInputStream = new ContentSizeLimitInputStream(byteArrayInputStream, 3, LIMIT);

        byte[] buffer = new byte[data.length];

        assertThrows(WebApplicationException.class, () -> {
            contentSizeLimitInputStream.read(buffer);
        });
    }

}
