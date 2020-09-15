package com.piesat.schedule.client.util;

import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 15:37
 **/
@Slf4j
public class FileUtil {

    public static void mkdirs(String path, ResultT<String> resultT){
        try {
            if(!new File(path).exists()){
                new File(path).mkdirs();
            }
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_5_ERROR.getKey());
            EiSendUtil.fileException(path,resultT);
        }
    }

    public static void createFile(String path,ResultT<String> resultT){
        try {
            if(!new File(path).exists()){
                new File(path).createNewFile();
            }
        } catch (IOException e) {
            resultT.setErrorMessage(OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_5_ERROR.getKey());
            EiSendUtil.fileException(path,resultT);
        }
    }

    public static void delFile(File file,ResultT<String> resultT) {

        try {
            delFiles(file);
            log.info("删除文件{}成功",file.getPath());
        } catch (Exception e) {
            resultT.setErrorMessage("删除文件{}失败{}",file.getPath(),OwnException.get(e));
            log.error("删除文件{}失败{}",file.getPath(),OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_3_ERROR.getKey());
            EiSendUtil.fileException(file.getPath(),resultT);
        }
    }
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
    public static void delFileList(List<String> fileList,ResultT<String> resultT){
        for(String f:fileList){
           File file=new File(f);
            try {
                if(file.exists()){
                    file.delete();
                }
                log.info("删除文件{}成功",file.getPath());
            } catch (Exception e) {
                resultT.setErrorMessage("删除文件{}失败{}",file.getPath(),OwnException.get(e));
                log.error("删除文件{}失败{}",file.getPath(),OwnException.get(e));
                resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_3_ERROR.getKey());
                EiSendUtil.fileException(file.getPath(),resultT);
            }
        }

    }

    public static void copyFile(String srcFile,String tagertFile,ResultT<String> resultT){
        try {
            if(!new File(tagertFile).getParentFile().exists()){
                mkdirs(new File(tagertFile).getParentFile().getPath(),resultT);
            }
            FileUtils.copyFile(new File(srcFile), new File(tagertFile));
            log.info("移动文件{}到{}成功",srcFile,tagertFile);

        } catch (IOException e) {
            resultT.setErrorMessage("移动文件{}到{}失败,{}",srcFile,tagertFile,OwnException.get(e));
            log.error("移动文件{}到{}失败,{}",srcFile,tagertFile,OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_4_ERROR.getKey());
            EiSendUtil.fileException(srcFile,resultT);
        }
    }
    public static void copyMoveFile(String srcFile,String tagertFile,ResultT<String> resultT){
        try {
            if(!new File(tagertFile).getParentFile().exists()){
                mkdirs(new File(tagertFile).getParentFile().getPath(),resultT);
            }
            if(new File(srcFile).exists()){
                FileUtils.copyFile(new File(srcFile), new File(tagertFile));
            }else{
                resultT.setSuccessMessage(srcFile+"不存在");
            }
            log.info("移动文件{}到{}成功",srcFile,tagertFile);

        } catch (IOException e) {
            resultT.setErrorMessage("移动文件{}到{}失败,{}",srcFile,tagertFile,OwnException.get(e));
            log.error("移动文件{}到{}失败,{}",srcFile,tagertFile,OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_4_ERROR.getKey());
            EiSendUtil.fileException(srcFile,resultT);
        }
    }

    public static void  delete(String path,ResultT<String> resultT){
        boolean isdelete=false;
        File file=new File(path);
        if(!file.exists()){
            return;
        }
        try {
            isdelete=file.delete();
        } catch (Exception e) {
            log.error(OwnException.get(e));
        }
        if(!isdelete){
            String dpcUser="";
            String dpcPass="";
            String cmd="echo \""+dpcPass+"\" |su -l "+dpcUser+" -c \"rm -rf "+path+"\"";
            String[] commands = new String[]{"/bin/sh", "-c", cmd};
            Runtime r = Runtime.getRuntime();
            try {
                Process proc = r.exec(commands);
                int exitVal = proc.waitFor();
                if (exitVal == 0) {
                    isdelete = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!isdelete){
            resultT.setErrorMessage("删除文件失败{}",path);
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_3_ERROR.getKey());
            EiSendUtil.fileException(path,resultT);
        }



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

