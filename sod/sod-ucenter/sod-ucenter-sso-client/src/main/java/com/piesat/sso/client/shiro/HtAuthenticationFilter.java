package com.piesat.sso.client.shiro;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.ip.AddressUtils;
import com.piesat.common.utils.ip.IpUtils;
import com.piesat.sso.client.enums.OperatorType;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.ucenter.rpc.api.monitor.LoginInfoService;
import com.piesat.ucenter.rpc.dto.monitor.LoginInfoDto;
import com.piesat.util.ResultT;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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
            ResultT<Map<String,Object>> resultT=new ResultT<>();
            HttpServletRequest req = (HttpServletRequest) request;
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(appId, "");
                token.setLoginType("1");
                token.setRequest(req);
                token.setOperatorType(OperatorType.INTERFACE.ordinal());
                subject.login(token);
                redisUtil.set(THRID_LOGIN_APP_ID+appId,subject.getSession().getId(),18000);
                this.recordLogininfor(req,appId,resultT);
                return true;
            } catch (Exception e) {
                resultT.setErrorMessage("接口登陆失败");
                this.recordLogininfor(req,appId,resultT);
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

    public void recordLogininfor(HttpServletRequest request,String userName,ResultT<Map<String,Object>> resultT){
        try {
            ExecutorService executorService= (ExecutorService) SpringUtil.getBean("executorService");
            LoginInfoService loginInfoService =SpringUtil.getBean(LoginInfoService.class);
            executorService.execute(
                    ()->{
                        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                        final String ip = IpUtils.getIpAddr(request);
                        String address = AddressUtils.getRealAddressByIP(ip);
                        String os = userAgent.getOperatingSystem().getName();
                        // 获取客户端浏览器
                        String browser = userAgent.getBrowser().getName();
                        LoginInfoDto logininfor=new LoginInfoDto();
                        logininfor.setUserName(userName);
                        logininfor.setIpaddr(ip);
                        logininfor.setLoginLocation(address);
                        logininfor.setBrowser(browser);
                        logininfor.setOs(os);
                        logininfor.setMsg(resultT.getMsg());
                        if(resultT.isSuccess()){
                            logininfor.setStatus("0");
                        }else{
                            logininfor.setStatus("1");
                        }
                        logininfor.setLoginTime(new Date());
                        loginInfoService.insertLogininfor(logininfor);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
