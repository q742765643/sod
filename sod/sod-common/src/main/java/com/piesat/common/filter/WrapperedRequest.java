package com.piesat.common.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/13 18:00
 */
public class WrapperedRequest extends HttpServletRequestWrapper {

    private String requestBody = null;
    HttpServletRequest req = null;

    public WrapperedRequest(HttpServletRequest request) {
        super(request);
        this.req = request;
    }

    public WrapperedRequest(HttpServletRequest request, String requestBody) {
        super(request);
        this.requestBody = requestBody;
        this.req = request;
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(requestBody));
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private InputStream in = new ByteArrayInputStream(
                    requestBody.getBytes(req.getCharacterEncoding()));
            @Override
            public int read() throws IOException {
                return in.read();
            }
            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub

            }
        };
    }
}
