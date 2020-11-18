package com.piesat.common.utils;

import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 15:37
 **/
@Slf4j
public class FileUtil {

    public static boolean delFiles(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(null!=files){
                for (File f : files) {
                    delFiles(f);
                }
            }
        }
        return file.delete();
    }

    public static void checkFile(String path,ResultT<String> resultT){
        File file=new File(path);
        if(!file.exists()){
            resultT.setErrorMessage("{}不存在",path);
            log.error("{}不存在",path);
        }

    }
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}

