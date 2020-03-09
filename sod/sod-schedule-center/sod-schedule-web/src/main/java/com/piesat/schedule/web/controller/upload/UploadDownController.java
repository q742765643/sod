package com.piesat.schedule.web.controller.upload;

import com.piesat.schedule.client.api.UploadDownService;
import com.piesat.util.ResultT;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-04 11:24
 **/
@RestController
@Api(value="文件上传下载接口",tags = {"文件上传下载接口"})
@RequestMapping("/api/schedule/uploadDown")
public class UploadDownController {
    @Autowired
    private UploadDownService uploadDownService;
    private static String encoding = "utf-8";


    @ApiOperation(value = "文件下载", notes = "文件下载")
    @GetMapping("/downFile")
    public void downFile(HttpServletResponse httpServletResponse,String path){
        InputStream inputStream = null;
        OutputStream outputStream=null;
        try {
            File file=new File(path);
            // feign文件下载
            Response response = uploadDownService.downloadFile(path);
            Response.Body body = response.body();
            inputStream = body.asInputStream();
            httpServletResponse.setContentType("application/octet-stream;charset=" + encoding);
            httpServletResponse.setCharacterEncoding(encoding);
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), encoding));
            outputStream = httpServletResponse.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @PostMapping(value = "/uploadFile")
    @ApiOperation(value = "文件上传", notes = "文件上传")
    public ResultT<String> handleFileUpload(@RequestPart(value = "file") MultipartFile file,String path) {
        return uploadDownService.handleFileUpload(file,path);
    }

}

