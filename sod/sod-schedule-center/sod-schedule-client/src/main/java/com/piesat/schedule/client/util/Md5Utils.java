package com.piesat.schedule.client.util;

import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;
import org.springframework.util.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 17:55
 **/
public class Md5Utils {
    /**
     * 获取文件的MD5值
     *
     * @param path
     *            目标文件
     * @return MD5字符串
     */
    public static String getFileMD5String(String path,ResultT<String> resultT) {
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(path);
            String md5= DigestUtils.md5DigestAsHex( fileInputStream);
            return md5;
        } catch (IOException e) {
            resultT.setErrorMessage("Md5加密异常{}", OwnException.get(e));
            return "";
        }finally {
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

