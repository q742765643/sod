package com.piesat.common.utils.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.utils.RSAUtil;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.vo.SignVo;
import net.bytebuddy.implementation.bytecode.Throw;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-25 10:16
 **/
public class SignAuthUtil {
     public static boolean  compareSign(String sign,String oldAppId){
         String realSign= AESUtil.aesDecrypt(sign);
         SignVo signVo= JSON.parseObject(realSign,SignVo.class);
         if(!StringUtils.isNotNullString(signVo.getUuidHt())){
             return false;
         }
         if(System.currentTimeMillis()-signVo.getTimestampHt()>1000*3*60){
             System.out.println(System.currentTimeMillis()-signVo.getTimestampHt());
             return false;
         }
         String newAppId= RSAUtil.decrypt(signVo.getAppIdHt(),RSAUtil.keyMap.get(1));
         if(!newAppId.equals(oldAppId)){
             return false;
         }

         return true;

     }
     public static void checkSign(String sign,String oldAppId) throws Exception {
         boolean flag= false;
         try {
             flag = compareSign(sign,oldAppId);
         } catch (Exception e) {
             throw new Exception("请求不合法");
         }
         if(!flag){
             throw new Exception("请求不合法");
         }
     }

}

