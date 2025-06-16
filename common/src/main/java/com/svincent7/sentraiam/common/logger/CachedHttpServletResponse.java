package com.svincent7.sentraiam.common.logger;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@Setter
public class CachedHttpServletResponse extends HttpServletResponseWrapper {

    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private CachedServletOutputStream cachedServletOutputStream;

    public CachedHttpServletResponse(final HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            cachedServletOutputStream = new CachedServletOutputStream(outputStream);
        }

        return cachedServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            cachedServletOutputStream = new CachedServletOutputStream(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(cachedServletOutputStream,
                    getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            cachedServletOutputStream.flush();
        }
    }

    public byte[] getCached() {
        if (cachedServletOutputStream != null) {
            return cachedServletOutputStream.getCopy();
        } else {
            return new byte[0];
        }
    }

}
