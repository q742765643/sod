package com.piesat.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.utils.http.HttpClientUtil;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;


public class SM3Utils {

    /**
     * 计算SM3摘要值
     *
     * @param srcData 原文
     * @return 摘要值，对于SM3算法来说是32字节
     */
    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 验证摘要
     *
     * @param srcData 原文
     * @param sm3Hash 摘要值
     * @return 返回true标识验证成功，false标识验证失败
     */
    public static boolean verify(byte[] srcData, byte[] sm3Hash) {
        byte[] newHash = hash(srcData);
        if (Arrays.equals(newHash, sm3Hash)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算SM3 Mac值
     *
     * @param key     key值，可以是任意长度的字节数组
     * @param srcData 原文
     * @return Mac值，对于HMac-SM3来说是32字节
     */
    public static byte[] hmac(byte[] key, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    public static String Encode(String plaintext) {
        try {
            byte[] hash = SM3Utils.hash(plaintext.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(hash), "UTF-8");
            //return new String(Hex.encode(hash), "UTF-8");
        } catch (Exception e) {
            Logger.getLogger(SM4Utils.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static boolean verify(String plaintext, String ciphertext) {
        try {
            return SM3Utils.verify(plaintext.getBytes("UTF-8"), Base64.decodeBase64(ciphertext.getBytes("UTF-8")));
        } catch (Exception e) {
            Logger.getLogger(SM4Utils.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("client_id", "CMADAAS");
            jsonObject.put("uid", "7ABA187D488547EF9791E3E304820260");

            String message = "client_id=CMADAAS&uid=7ABA187D488547EF9791E3E304820260";
            String hash = SM3Utils.Encode(message);
            System.out.println("SM3 hash result:\n" + hash);

            jsonObject.put("signature", hash);
            String jsonStr = jsonObject.toString();
            System.out.println(jsonStr);

            String s1 = SM4Utils.encrypt(jsonStr,"f969ba3a158e48de8dc1f5585d387b4f");//没有用到
            System.out.println(s1);

            String url = "http://10.40.25.44:8080/userservice/getUserInfo/getUserByOAID";
            HashMap<String, String> headers= new HashMap<String, String>();
            headers.put("client_id","CMADAAS");
            String result = HttpClientUtil.doPost(url,s1,headers);
            System.out.println("结果："+result);
            String s2 = SM4Utils.decrypt(result,"f969ba3a158e48de8dc1f5585d387b4f");
            System.out.println(s2);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(111);
        }
    }
}
