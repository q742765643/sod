package com.piesat.schedule.client.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

