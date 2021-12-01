package com.hitejinro.snop.security.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

/**
 * Request의 Body Copy용
 * @author ykw
 *
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CachedBodyHttpServletRequest.class);

    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(this.cachedBody);
    }
    
    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    /**
     * Request의 Body Copy에 사용하는 클래스
     * @author ykw
     *
     */
    class CachedBodyServletInputStream extends ServletInputStream {
    
        private InputStream cachedBodyInputStream;
    
        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }
    
        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
        
        @Override
        public boolean isFinished() {
            return cachedBody.length == 0;
        }
        
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new RuntimeException("Not implemented");
        }
    }
}
