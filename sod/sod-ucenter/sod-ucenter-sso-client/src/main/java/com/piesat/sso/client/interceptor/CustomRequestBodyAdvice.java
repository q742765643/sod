package com.piesat.sso.client.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.common.utils.ServletUtils;
import com.piesat.sso.client.util.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/14 13:53
 */
//@ControllerAdvice
@RequiredArgsConstructor
public class CustomRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private final ObjectMapper objectMapper;
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
    @SneakyThrows
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        HttpServletRequest request = ServletUtils.getRequest();
        String token =request.getHeader("authorization");
        if(null!=token&&token.equals("88888888")){
            return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        }
        Map<String,ByteArrayInputStream> map=new HashMap<>();
        //boolean shouldDecrypt= DecryptUtil.decrypt(inputMessage,parameter,map);
        SignUtil.signJson(inputMessage,parameter,map);
       // if(shouldDecrypt){
        return new MappingJacksonInputMessage(map.get("REQUEST_RESOLVER_PARAM_MAP_NAME"), inputMessage.getHeaders());
       // } else {
            //return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        //}
    }
}
