package com.piesat.schedule.client.util;

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
        try {
            String md5= DigestUtils.md5DigestAsHex( new FileInputStream(path));
            return md5;
        } catch (IOException e) {
            resultT.setErrorMessage("Md5加密异常");
            e.printStackTrace();
            return "";
        }
    }
}

