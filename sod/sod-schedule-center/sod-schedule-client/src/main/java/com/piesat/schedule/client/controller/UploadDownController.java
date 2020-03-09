package com.piesat.schedule.client.controller;

import com.piesat.util.ResultT;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-04 11:13
 **/
@RestController
public class UploadDownController{
    @PostMapping("/api/downloadFile")
    public void downloadFile(HttpServletResponse response,String path){
        File file = new File(path);
        InputStream in = null;
        if(file.exists()){
            try {
                OutputStream out = response.getOutputStream();
                in = new FileInputStream(file);
                byte buffer[] = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) >= 0){
                    out.write(buffer,0,length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/api/uploadFile",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultT<String> handleFileUpload(@RequestPart(value = "file") MultipartFile file,String path) {
        ResultT<String> resultT=new ResultT<>();
        String fileName=file.getOriginalFilename();
        File filenew = new File(path+"/"+fileName);
        if (! filenew.getParentFile().exists()){
            filenew.getParentFile().mkdirs();
        }
        try {
            file.transferTo(filenew);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultT.setData(filenew.getPath().replace("\\","/"));

        return resultT;
    }

}

