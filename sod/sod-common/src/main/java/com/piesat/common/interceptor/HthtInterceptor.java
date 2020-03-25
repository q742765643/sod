package com.piesat.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.common.annotation.DecryptRequest;
import com.piesat.common.filter.CustomEncryptHttpWrapper;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.utils.DecryptUtil;
import com.piesat.common.utils.sign.SignAuthUtil;
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
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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
        String appId = request.getHeader("appId");
        String token =request.getHeader("authorization");
        String oldAppId="";
        if(!StringUtils.isEmpty(token)){
            oldAppId=token;
            String sign=request.getHeader("sign");
            SignAuthUtil.checkSign(sign,oldAppId);
        }
        if(!StringUtils.isEmpty(appId)){
            String sign=request.getHeader("sign");
            oldAppId=appId;
            SignAuthUtil.checkSign(sign,oldAppId);

        }
        if(null!=request.getContentType()){
            if (StringUtils.substringMatch(request.getContentType(), 0, MediaType.APPLICATION_JSON_VALUE)) {
                return true;
            }
        }
        DecryptUtil.decrypt(request,response,o);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }


}
