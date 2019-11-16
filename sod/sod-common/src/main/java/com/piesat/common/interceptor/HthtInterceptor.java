package com.piesat.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.common.annotation.DecryptRequest;
import com.piesat.common.filter.CustomEncryptHttpWrapper;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.vo.HttpReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/15 13:13
 */
@Slf4j
public class HthtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestBody = null;
        String contentType = request.getContentType();
        HandlerMethod h = (HandlerMethod)o;
        DecryptRequest decryptRequest=h.getMethod().getAnnotation(DecryptRequest.class);
        if(decryptRequest!=null&&decryptRequest.value()==false){
            return true;
        }
        if (StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        if (StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            requestBody = convertFormToString(request);
        }

        HttpReq httpReq= JSON.parseObject(requestBody,HttpReq.class);
        if(null!=httpReq.getData()){

        }
        //String data= AESUtil.aesDecode(httpReq.getData());
        String[] ss=new String[1];
        ss[0]="11111111";
        Map<String, String[]> parameterMap=new HashMap<>();
        parameterMap.put("age",ss);
        request.setAttribute("REQUEST_RESOLVER_PARAM_MAP_NAME",parameterMap);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        return StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
    }
    private String convertFormToString(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(8);
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            result.put(name, request.getParameter(name));
        }
        try {
            HttpReq httpReq= mapToBean(result,HttpReq.class);
            return httpReq.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static <T> T mapToBean(Map<String, Object> map,Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
}
