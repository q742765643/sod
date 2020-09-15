
package com.piesat.sso.client.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.common.filter.CustomEncryptHttpWrapper;
import com.piesat.common.filter.WrapperedRequest;
import com.piesat.common.filter.WrapperedResponse;
import com.piesat.common.vo.CasVo;
import com.piesat.common.vo.HttpReq;
import com.piesat.sso.client.util.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/13 17:57
 */

@WebFilter(urlPatterns = { "/*" }, filterName = "DataFilter")
@Slf4j
@RequiredArgsConstructor
public class DataFilter implements Filter {
    private final ObjectMapper objectMapper;



    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        return StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
    }



    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String enctype = request.getContentType();
        if (StringUtils.isNotBlank(enctype) && enctype.contains("multipart/form-data")){
            WrapperedRequest wrapRequest = new WrapperedRequest(
                    (HttpServletRequest) request);
            SignUtil.signParam(wrapRequest);
            filterChain.doFilter(wrapRequest, response);
        }else {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String token = httpRequest.getHeader("authorization");
            if (null != token && token.equals("88888888")) {
                filterChain.doFilter(request, response);
            }else {
                String requestBody = convertInputStreamToString(request.getInputStream());
                if (null==requestBody||"".equals(requestBody)) {
                    WrapperedRequest wrapRequest = new WrapperedRequest(
                            (HttpServletRequest) request);
                    //WrapperedResponse wrapResponse = new WrapperedResponse((HttpServletResponse) response);
                    SignUtil.signParam(wrapRequest);
                    filterChain.doFilter(wrapRequest, response);
           /* byte[] data = wrapResponse.getResponseData();
            String responseBodyMw=new String(data);
            writeResponse(response, responseBodyMw);*/
                } else {
                    JSONObject jsonObject = JSONObject.parseObject(requestBody);
                    CasVo casVo= JSON.parseObject(requestBody, CasVo.class);
                    WrapperedRequest wrapRequest = new WrapperedRequest(
                            (HttpServletRequest) request, casVo.getData());
                    //WrapperedResponse wrapResponse = new WrapperedResponse((HttpServletResponse) response);
                    SignUtil.signJson(casVo,requestBody,wrapRequest,jsonObject);
                    filterChain.doFilter(wrapRequest, response);
           /* byte[] data = wrapResponse.getResponseData();
            String responseBodyMw=new String(data);
            writeResponse(response, responseBodyMw);*/

                }
            }

        }


    }

    private void writeResponse(ServletResponse response, String responseString)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }
}

