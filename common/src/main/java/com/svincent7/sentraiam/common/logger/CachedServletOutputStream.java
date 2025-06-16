package com.svincent7.sentraiam.common.logger;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachedServletOutputStream extends ServletOutputStream {

    private final OutputStream outputStream;
    private final ByteArrayOutputStream copy;

    public CachedServletOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }

    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }
}
