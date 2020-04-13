package com.piesat.sso.client.interceptor;

import com.piesat.common.interceptor.HthtInterceptor;
import com.piesat.sso.client.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/15 13:13
 */
@Slf4j
public class SsoHthtInterceptor extends HthtInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String appId = request.getHeader("appId");
        String token =request.getHeader("authorization");
        String oldAppId="";
        /*if(!StringUtils.isEmpty(token)){
            oldAppId=token;
            String sign=request.getHeader("sign");
            SignAuthUtil.checkSign(sign,oldAppId);
        }
        if(!StringUtils.isEmpty(appId)){
            String sign=request.getHeader("sign");
            oldAppId=appId;
            SignAuthUtil.checkSign(sign,oldAppId);

        }*/
        if(null!=request.getContentType()){
            if (StringUtils.substringMatch(request.getContentType(), 0, MediaType.APPLICATION_JSON_VALUE)) {
                return true;
            }
        }
        SignUtil.signParam(request,response,o);
        //DecryptUtil.decrypt(request,response,o);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }


}
