package com.piesat.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.common.annotation.DecryptRequest;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.vo.HttpReq;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/14 13:53
 */
@ControllerAdvice
@RequiredArgsConstructor
public class CustomRequestBodyAdvice extends RequestBodyAdviceAdapter {
    private final ObjectMapper objectMapper;
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        boolean shouldEncrypt = true;
        DecryptRequest decryptRequest=parameter.getMethod().getAnnotation(DecryptRequest.class);
        if(decryptRequest!=null&&decryptRequest.value()==false){
            shouldEncrypt=false;
        }
        if(shouldEncrypt){
            //System.out.println(StreamUtils.copyToByteArray(inputMessage.getBody()));
            HttpReq httpReq= objectMapper.readValue(StreamUtils.copyToByteArray(inputMessage.getBody()), HttpReq.class);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(AESUtil.aesDecode(httpReq.getData()).getBytes(Charset.forName("UTF-8")));
            return new MappingJacksonInputMessage(inputStream, inputMessage.getHeaders());
        } else {
            return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        }
    }
}
