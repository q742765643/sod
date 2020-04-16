package com.piesat.calculate.util;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-30 17:12
 **/
public class MapUtil {
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            JsonProperty jsonProperty=field.getAnnotation(JsonProperty.class);
            if(jsonProperty!=null){
                fieldName = jsonProperty.value();
            }
            if("fields".equals(fieldName)){
               objectToMapChirld(field.get(obj),fieldName,map);
            }else{
                Map<String, Object> value = new HashMap<>();
                if(field.getType().toString().toUpperCase().indexOf("STRING")!=-1){
                    value.put("type","text");
                }
                if(field.getType().toString().toUpperCase().indexOf("DATE")!=-1){
                    value.put("type","date");
                    value.put("format","yyyy-MM-dd HH:mm:ss:SSS||epoch_millis||date_optional_time");
                }
                if(field.getType().toString().toUpperCase().indexOf("FLOAT")!=-1){
                    value.put("type","float");
                }
                if(field.getType().toString().toUpperCase().indexOf("LONG")!=-1){
                    value.put("type","long");
                }
                if(field.getType().toString().toUpperCase().indexOf("INT")!=-1){
                    value.put("type","long");
                }
                map.put(fieldName,value);
            }


        }
        return map;
    }
    public static Map<String, Object> objectToMapChirld(Object obj,String qz, Map<String, Object> map) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            JsonProperty jsonProperty=field.getAnnotation(JsonProperty.class);
            if(jsonProperty!=null){
                fieldName = jsonProperty.value();
            }
            Map<String, Object> value = new HashMap<>();
            if(field.getType().toString().toUpperCase().indexOf("STRING")!=-1){
                value.put("type","text");
            }
            if(field.getType().toString().toUpperCase().indexOf("DATE")!=-1){
                value.put("type","date");
                value.put("format","yyyy-MM-dd HH:mm:ss:SSS||epoch_millis||date_optional_time");

                //value.put("format","yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time");
            }
            if(field.getType().toString().toUpperCase().indexOf("FLOAT")!=-1){
                value.put("type","float");
            }
            if(field.getType().toString().toUpperCase().indexOf("LONG")!=-1){
                value.put("type","long");
            }
            if(field.getType().toString().toUpperCase().indexOf("INT")!=-1){
                value.put("type","long");
            }
            map.put(qz+"."+fieldName, value);

        }
        return map;
    }
    public static Map<String, Object> objectToMapInsert(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            JsonProperty jsonProperty=field.getAnnotation(JsonProperty.class);
            if(jsonProperty!=null){
                fieldName = jsonProperty.value();
            }
            if("fields".equals(fieldName)){
                objectToMapChirldInsert(field.get(obj),fieldName,map);
            }else{
                Object value=field.get(obj);
                map.put(fieldName,value);
            }


        }
        return map;
    }
    public static Map<String, Object> objectToMapChirldInsert(Object obj,String qz, Map<String, Object> map) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            JsonProperty jsonProperty=field.getAnnotation(JsonProperty.class);
            if(jsonProperty!=null){
                fieldName = jsonProperty.value();
            }
            Object value=field.get(obj);
            if(value==null){
                value="";
            }
            map.put(qz+"."+fieldName, value);

        }
        return map;
    }

}

