package com.piesat.sso.client.shiro;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.sso.client.enums.OperatorType;
import com.piesat.sso.client.util.RedisUtil;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/10 15:10
 */
public class HtAuthenticationFilter extends FormAuthenticationFilter {
    private Logger logger= LoggerFactory.getLogger(HtAuthenticationFilter.class);
    private static String THRID_LOGIN_APP_ID="THRID_LOGIN_APP_ID:";
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String appId = WebUtils.toHttp(request).getHeader("appId");
        if(null!=appId&&!"".equals(appId)){
            RedisUtil redisUtil=SpringUtil.getBean(RedisUtil.class);
            boolean check=this.validatAppId(appId);
            if(check){
                return check;
            }
            try {
                HttpServletRequest req = (HttpServletRequest) request;
                UsernamePasswordToken token = new UsernamePasswordToken(appId, "");
                token.setLoginType("1");
                token.setRequest(req);
                token.setOperatorType(OperatorType.INTERFACE.ordinal());
                subject.login(token);
                redisUtil.set(THRID_LOGIN_APP_ID+appId,subject.getSession().getId(),18000);
                return true;
            } catch (Exception e) {
                logger.error(OwnException.get(e));
                return false;
            }

        }else{
            return subject.isAuthenticated();
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return super.onAccessDenied(servletRequest,servletResponse);
    }

    private boolean validatAppId(String appId){
        RedisUtil redisUtil=SpringUtil.getBean(RedisUtil.class);
        boolean has=redisUtil.hasKey(THRID_LOGIN_APP_ID+appId);
        if(!has){
            return false;
        }
        String appSessionId= (String) redisUtil.get(THRID_LOGIN_APP_ID+appId);
        boolean checkSession=redisUtil.hasKey("shiro:session:"+appSessionId);
        if(!checkSession){
            return false;
        }
        return true;
    }
}
