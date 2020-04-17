package com.piesat.schedule.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.piesat.schedule.client.annotation.HtJson;
import com.piesat.schedule.client.vo.DiSendVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *@program: backup
 *@描述
 *@author  zzj
 *@创建时间  2019/5/7 13:28
 **/
@Component
public class DiSendUtil {
    private static final Logger logger = LoggerFactory.getLogger(DiSendUtil.class);
    private static String EIDIURL;
    private static String TRANSFERURL;

    @Value("${EIDI.URL}")
    public void setEidiUrl(String eidiUrl) {
        EIDIURL = eidiUrl;
    }
    @Value("${TRANSFER.URL}")
    public  void setTransferUrl(String transferUrl) {
        TRANSFERURL = transferUrl;
    }

    public static void send(long occurTime, String message, DiSendVo diSendVo) {
        diSendVo.setSystem("SOD");
        LinkedHashMap map = new LinkedHashMap();
        map.put("type", "OT.TASKINFO.DI");
        map.put("name", "定时任务运行详细信息");
        map.put("message", message);
        map.put("occur_time", occurTime);
        Map mapTags = new HashMap(10);
        mapTags.put("SYSTEM", "SOD");
        //map.put("tags", mapTags);
        LinkedHashMap mapSend = new LinkedHashMap();
        Field[] fields = diSendVo.getClass().getDeclaredFields();
        for (Field f : fields) {
            String name = "";
            if (f.isAnnotationPresent(HtJson.class)) {
                HtJson column = f.getAnnotation(HtJson.class);
                name = column.name();
            } else {
                name = f.getName();
            }
            mapSend.put(name,getFieldValueByName(f.getName(),diSendVo));

        }
        map.put("fields", mapSend);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>("["+JSONObject.toJSONString(map,SerializerFeature.WriteNullStringAsEmpty)+"]", headers);
        RestTemplate rst = new RestTemplate();

        logger.info("di-json信息:{}",JSON.toJSONString(map,SerializerFeature.WriteNullStringAsEmpty));
        boolean result=false;

        ResponseEntity<String> stringResponseEntity = null;
        try {
            stringResponseEntity = rst.postForEntity(TRANSFERURL, httpEntity, String.class);
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("di发送成功返回信息:{}",JSON.toJSONString(stringResponseEntity));
        if(!result){
            stringResponseEntity = rst.postForEntity(EIDIURL, httpEntity, String.class);
            logger.info("di发送返回信息:{}",JSON.toJSONString(stringResponseEntity));
        }else{
            logger.info("transferurl发送返回信息:{}",JSON.toJSONString(stringResponseEntity));
        }



    }

    public static Object getFieldValueByName(String name, DiSendVo diSendVo) {
        String firstletter = name.substring(0, 1).toUpperCase();
        String getter = "get" + firstletter + name.substring(1);
        Method method;
        Object value;
        try {
            method = diSendVo.getClass().getMethod(getter);
            value = method.invoke(diSendVo, new DiSendVo[]{});
            return value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
