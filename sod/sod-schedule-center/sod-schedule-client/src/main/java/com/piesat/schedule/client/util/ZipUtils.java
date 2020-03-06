package com.piesat.schedule.client.util;
import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 14:31
 **/
@Slf4j
public class ZipUtils {
    public static void doCompress(String srcFile, String zipFile,ResultT<String> resultT) {
        try {
            doCompress(new File(srcFile), new File(zipFile));
            resultT.setSuccessMessage("压缩文件{}成功",srcFile);
            log.info("压缩文件{}成功",srcFile);

        } catch (IOException e) {
            resultT.setErrorMessage("压缩文件失败{},{},",srcFile,OwnException.get(e));
            log.error("压缩文件失败{},{},",srcFile,OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_4_ERROR.getKey());
            EiSendUtil.fileException(srcFile,resultT);
        }
    }

    /**
     * 文件压缩
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();//记得关闭资源
        }
    }

    public static void doCompress(String filelName, ZipOutputStream out) throws IOException{
        doCompress(new File(filelName), out);
    }

    public static void doCompress(File file, ZipOutputStream out) throws IOException{
        doCompress(file, out, "");
    }

    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if ( inFile.isDirectory() ) {
            File[] files = inFile.listFiles();
            if (files!=null && files.length>0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, out, name);
                }
            }
        } else {
            //ZipUtils.doZip(inFile, out, dir);
            ZipUtils.doZip(inFile, out, "");

        }
    }

    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);

        int len = 0 ;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.closeEntry();
        fis.close();
    }

    public static void unZip(File srcFile, String destDirPath) throws RuntimeException {

        long start = System.currentTimeMillis();

        // 判断源文件是否存在

        if (!srcFile.exists()) {

            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");

        }

        // 开始解压

        ZipFile zipFile = null;

        try {

            zipFile = new ZipFile(srcFile);

            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();

                System.out.println("解压" + entry.getName());

                // 如果是文件夹，就创建个文件夹

                if (entry.isDirectory()) {

                    String dirPath = destDirPath + "/" + entry.getName();

                    File dir = new File(dirPath);

                    dir.mkdirs();

                } else {

                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去

                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    // 保证这个文件的父文件夹必须要存在

                    if(!targetFile.getParentFile().exists()){

                        targetFile.getParentFile().mkdirs();

                    }

                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中

                    InputStream is = zipFile.getInputStream(entry);

                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;

                    byte[] buf = new byte[1024];

                    while ((len = is.read(buf)) != -1) {

                        fos.write(buf, 0, len);

                    }

                    // 关流顺序，先打开的后关闭

                    fos.close();

                    is.close();

                }

            }

            long end = System.currentTimeMillis();

            System.out.println("解压完成，耗时：" + (end - start) +" ms");

        } catch (Exception e) {

            throw new RuntimeException("unzip error from ZipUtils", e);

        } finally {

            if(zipFile != null){

                try {

                    zipFile.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }
    public static void doZip(List<String> srcFile, String zipFile) throws IOException {
        File zip=new File(zipFile);
        if(zip.exists()){
            zip.delete();
        }
        ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipFile));
        byte[] buffer = new byte[1024];
        for(int i=0;i<srcFile.size();i++){
            File inFile=new File(srcFile.get(i));
            ZipEntry entry = new ZipEntry(inFile.getName());
            out.putNextEntry(entry);

            int len = 0 ;
            FileInputStream fis = new FileInputStream(inFile);
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
            fis.close();
        }
        out.closeEntry();
        out.close();

    }
    public static void writetxt(String path, String result, ResultT<String> resultT) {
        FileWriter fw = null;
        try {
            File f = new File(path);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(result);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }
    }

    public static void deleteDir(List<String> dir,String zipName) {
        if(!"".equals(zipName)){
            File zipFile=new File(zipName);
            if(zipFile.exists()){
                zipFile.delete();
            }
        }

        if(null==dir||dir.size()==0){
            return;
        }
        for(int i=0;i<dir.size();i++){
            deleteDir(dir.get(i));
        }
    }
    public static void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }


    public static void readFile(String path,StringBuilder stringBuilder,ResultT<String> resultT) {
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.indexOf("DROP TABLE IF")==-1){
                    stringBuilder.append(line).append("\r\n");
                }
            }
        } catch (IOException e) {
            resultT.setErrorMessage("读取文件失败:"+OwnException.get(e));
            e.printStackTrace();
        }
    }
    public static void writeFile(String path,StringBuilder stringBuilder,ResultT<String> resultT) {
        try {
            File writeName = new File(path); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(stringBuilder.toString()); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            resultT.setErrorMessage("写入文件失败:"+OwnException.get(e));
            e.printStackTrace();
        }
    }


    public static Map<String,String> readFile(String path, ResultT<String> resultT) {
        Map<String,String> map=new HashMap();
        String key="";
        String value="";
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("---table")){
                    key=line;
                    continue;
                }
                if(line.startsWith("---end table")){
                    map.put(key,value);
                    continue;
                }
                if(line.startsWith("---user")){
                    key=line;
                    continue;
                }
                if(line.startsWith("---end user")){
                    map.put(key,value);
                    continue;
                }
                if(line.startsWith("---data")){
                    key=line;
                    continue;
                }
                if(line.startsWith("---end data")){
                    map.put(key,value);
                    continue;
                }
                if(line.startsWith("---instance")){
                    key=line;
                    continue;
                }
                if(line.startsWith("---end instance")){
                    map.put(key,value);
                    continue;
                }
                value=line;

            }
        } catch (IOException e) {
            resultT.setErrorMessage("读取文件失败:"+OwnException.get(e));
            e.printStackTrace();
        }
        return map;
    }
    public static void readFileCansandra(String path,StringBuilder stringBuilder,ResultT<String> resultT) {
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            int i=0;
            while ((line = br.readLine()) != null) {
                if(i==1){
                    stringBuilder.append(line).append("\r\n");
                    break;
                }
                i++;

            }
        } catch (IOException e) {
            resultT.setErrorMessage("读取文件失败:"+OwnException.get(e));
            e.printStackTrace();
        }finally {
           FileUtil.delete(path,resultT);
        }
    }
    public static void readFileCansandraFile(String path,StringBuilder stringBuilder,ResultT<String> resultT) {
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\r\n");
            }
        } catch (IOException e) {
            resultT.setErrorMessage("读取文件失败:"+ OwnException.get(e));
            e.printStackTrace();
        }finally {
            File file=new File(path);
            file.delete();
        }
    }
    public static void main(String[] args) throws IOException {
        doCompress("/zzj/data/RADB/虚谷备份_20200306132300","/zzj/data/RADB/虚谷备份_202003061323001.zip",new ResultT<>());

    }
}

