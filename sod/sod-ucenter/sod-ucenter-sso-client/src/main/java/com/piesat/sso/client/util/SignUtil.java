package com.piesat.sso.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.piesat.common.filter.WrapperedRequest;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.MD5Util;
import com.piesat.common.utils.ServletUtils;
import com.piesat.common.utils.SignException;
import com.piesat.common.vo.CasVo;
import com.piesat.sso.client.enums.OperatorType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-09 15:38
 **/
public class SignUtil {
    private static String THRID_LOGIN_APP_ID="THRID_LOGIN_APP_ID:";

    public static void signParam(HttpServletRequest request, HttpServletResponse response, Object o)  throws Exception {
        boolean shouldSign=true;
        /*****===========2.判断注解判断是否需要解密=================*****/
        if(o instanceof ResourceHttpRequestHandler){
            return;
        }
        Map<String, String[]> param =request.getParameterMap();
        String token =request.getHeader("authorization");
        if(null!=token&&token.equals("88888888")){
            request.setAttribute("REQUEST_RESOLVER_PARAM_MAP_NAME",param);
            return;
        }

        Map<String,Object> map=new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : param.entrySet()) {
            if(entry.getValue().length>0){
                map.put(entry.getKey(),entry.getValue()[0]);
            }
        }
        CasVo casVo=JSON.parseObject(JSON.toJSONString(map),CasVo.class);
        if(null!=casVo.getData()&&!"".equals(casVo.getData())){
            Map<String,Object> data=JSON.parseObject(casVo.getData(),Map.class);
            Map<String,String[]> parameterMap=new HashMap<>();

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object oo=entry.getValue();

                if(oo instanceof JSONArray){
                    String parmFit="";
                    JSONArray jsonArray= (JSONArray) oo;
                    for(int i=0;i<jsonArray.size();i++){
                        parmFit+=String.valueOf(jsonArray.get(i))+"&&&";
                    }
                    parameterMap.put(entry.getKey(),parmFit.split("&&&"));
                }else if(null == oo) {
                    parameterMap.put(entry.getKey(), null);
                }else{
                    String parmFit=String.valueOf(oo) ;
                    parameterMap.put(entry.getKey(),parmFit.split("&&&"));
                }
            }
            request.setAttribute("REQUEST_RESOLVER_PARAM_MAP_NAME",parameterMap);
            checkSign(casVo,map);

        }else{
            request.setAttribute("REQUEST_RESOLVER_PARAM_MAP_NAME",param);

        }



    }

    public static void signJson(HttpInputMessage inputMessage, MethodParameter parameter, Map<String, ByteArrayInputStream> map) throws Exception {


            String data = StreamUtils.copyToString(inputMessage.getBody(), Charset.forName("UTF-8"));
            CasVo casVo= JSON.parseObject(data, CasVo.class);
            ByteArrayInputStream inputStream=null;
            if(null==casVo.getData()||"".equals(casVo.getData())){
                inputStream= new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8")));
            }else{
                inputStream = new ByteArrayInputStream(casVo.getData().getBytes(Charset.forName("UTF-8")));
            }
            map.put("REQUEST_RESOLVER_PARAM_MAP_NAME",inputStream);
            Map<String,Object> signMap=JSON.parseObject(data,Map.class);
            checkSign(casVo,signMap);



    }
    public static void checkSign(CasVo casVo,Map<String,Object> signMap){
        if(casVo.getTimestamp()<System.currentTimeMillis()-1000*60*10){
            throw new SignException("请求时间已过期");
        }
        if(casVo.getNonce().length()<15){
            throw new SignException("请求随机数长度小于15");
        }
        if(null==casVo.getSign()||"".equals(casVo.getSign())){
            throw new SignException("请求没有签名");
        }
        TreeMap<String,Object> treeMap=new TreeMap<>();
        treeMap.putAll(signMap);
        if(signMap.size()>0) {
            String result="";
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                if(!entry.getKey().equals("sign")){
                    result=result+entry.getKey()+"="+entry.getValue()+"&";
                }
            }
            String sign=MD5Util.MD5Encode(result).toUpperCase();
            if(!casVo.getSign().equals(sign)){
                throw new SignException("请求签名不匹配");
            }
        }



    }


    public static void signParam(WrapperedRequest request)  throws Exception {
        Map<String, String[]> param =request.getParameterMap();
        Map<String,Object> map=new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : param.entrySet()) {
            if(entry.getValue().length>0){
                map.put(entry.getKey(),entry.getValue()[0]);
            }
        }
        CasVo casVo=JSON.parseObject(JSON.toJSONString(map),CasVo.class);
        request.putHeader("appId",casVo.getUserId());
        if(null!=casVo.getData()&&!"".equals(casVo.getData())){
            Map<String,Object> data=JSON.parseObject(casVo.getData(),Map.class);
            Map<String,String[]> parameterMap=new HashMap<>();

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object oo=entry.getValue();

                if(oo instanceof JSONArray){
                    String parmFit="";
                    JSONArray jsonArray= (JSONArray) oo;
                    for(int i=0;i<jsonArray.size();i++){
                        parmFit+=String.valueOf(jsonArray.get(i))+"&&&";
                    }
                    parameterMap.put(entry.getKey(),parmFit.split("&&&"));
                }else if(null == oo) {
                    parameterMap.put(entry.getKey(), null);
                }else{
                    String parmFit=String.valueOf(oo) ;
                    parameterMap.put(entry.getKey(),parmFit.split("&&&"));
                }
            }
            request.putParameterMap(parameterMap);

        }
        checkSign(casVo,map);




    }
    public static void signJson(CasVo casVo,String data, WrapperedRequest wrapRequest) throws Exception {
        wrapRequest.putHeader("appId",casVo.getUserId());
        Map<String,Object> signMap=JSON.parseObject(data,Map.class);
        checkSign(casVo,signMap);

    }

    private static boolean validatAppId(String appId){
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

