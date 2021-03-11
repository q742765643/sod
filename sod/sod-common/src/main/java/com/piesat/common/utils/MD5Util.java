package com.piesat.common.utils;

import java.security.MessageDigest;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-09 16:34
 **/
public class MD5Util {
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)){
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            }
            else{
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
            }

        } catch (Exception exception) {
        }
        return resultString;
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        String sss = "data={\"dataId\":\"B.0025.0001.S001\"}&interfaceId=datumtypeinfo&nonce=3fa64952-0813-4f81-916c-b86467b425bd&pwd=123qweasdzxc&timestamp=1597986413056&userId=api_manager";
        String sign = MD5Encode(sss).toUpperCase();
        System.out.println(sign);
    }
}

