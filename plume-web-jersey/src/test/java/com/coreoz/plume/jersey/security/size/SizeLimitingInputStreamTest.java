package com.coreoz.plume.jersey.security.size;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;

import jakarta.ws.rs.WebApplicationException;

import java.io.ByteArrayInputStream;

import com.coreoz.plume.jersey.security.size.ContentSizeLimitFeature.ContentSizeLimitInterceptor.SizeLimitingInputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SizeLimitingInputStreamTest {

    private static final int LIMIT = 10;
    private ByteArrayInputStream byteArrayInputStream;
    private SizeLimitingInputStream sizeLimitingInputStream;

    @AfterEach
    public void tearDown() throws IOException {
        if (sizeLimitingInputStream != null) {
            sizeLimitingInputStream.close();
        }
    }

    @Test
    public void testRead_whenWithinLimit_shouldSuccess() throws IOException {
        byte[] data = "12345".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);

        byte[] buffer = new byte[data.length];
        int bytesRead = sizeLimitingInputStream.read(buffer);

        Assertions.assertThat(bytesRead).isEqualTo(data.length);
        Assertions.assertThat(buffer).isEqualTo(data);
    }

    @Test
    public void testRead_whenBeyondLimit_shouldThrow() {
        byte[] data = "12345678901".getBytes(); // 11 bytes, 1 byte over the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);

        byte[] buffer = new byte[data.length];
        assertThrows(WebApplicationException.class, () -> {
            sizeLimitingInputStream.read(buffer);
        });
    }

    @Test
    public void testRead_whenExactLimit_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);

        byte[] buffer = new byte[data.length];
        int bytesRead = sizeLimitingInputStream.read(buffer);

        Assertions.assertThat(bytesRead).isEqualTo(data.length);
        Assertions.assertThat(buffer).isEqualTo(data);
    }

    @Test
    public void testRead_whenEmpty_shouldSuccessWithoutReading() throws IOException {
        byte[] data = "".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);

        byte[] buffer = new byte[10];
        int bytesRead = sizeLimitingInputStream.read(buffer);

        Assertions.assertThat(bytesRead).isEqualTo(-1);
    }

    @Test
    public void testRead_withoutOffsetAndLength_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);
        int result = sizeLimitingInputStream.read(data, 0, 5);
        Assertions.assertThat(result).isEqualTo(5);
    }

    @Test
    public void testRead_withoutOffsetAndWithoutLength_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);
        int result = sizeLimitingInputStream.read();
        Assertions.assertThat(result).isEqualTo('1');
    }

    @Test
    public void testRead_withOffsetAndLength_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);
        int result = sizeLimitingInputStream.read(data, 3, 5);
        Assertions.assertThat(result).isEqualTo(5);
    }

    @Test
    public void testSkip_withinLength_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);
        long result = sizeLimitingInputStream.skip(3);
        Assertions.assertThat(result).isEqualTo(3);
    }

    @Test
    public void testSkip_beyondLength_shouldSuccess() throws IOException {
        byte[] data = "1234567890".getBytes(); // 10 bytes, exactly at the limit
        byteArrayInputStream = new ByteArrayInputStream(data);
        sizeLimitingInputStream = new SizeLimitingInputStream(byteArrayInputStream, LIMIT);
        long result = sizeLimitingInputStream.skip(11);
        Assertions.assertThat(result).isEqualTo(10);
    }
}
