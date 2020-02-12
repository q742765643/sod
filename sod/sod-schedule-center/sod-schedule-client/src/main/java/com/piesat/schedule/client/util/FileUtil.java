package com.piesat.schedule.client.util;

import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;

import java.io.File;
import java.io.IOException;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 15:37
 **/
public class FileUtil {

    public static void mkdirs(String path, ResultT<String> resultT){
        try {
            if(!new File(path).exists()){
                new File(path).mkdirs();
            }
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }
    }

    public static void createFile(String path,ResultT<String> resultT){
        try {
            if(!new File(path).exists()){
                new File(path).createNewFile();
            }
        } catch (IOException e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
}

