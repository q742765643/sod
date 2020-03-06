package com.piesat.schedule.client.api;

import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-04 11:18
 **/
@FeignClient(name = GrpcConstant.SCHEDULE_CLIENT_SERVER)
public interface UploadDownService {
    @PostMapping("/api/downloadFile")
    Response downloadFile(@RequestParam("path") String path);

    @RequestMapping(value = "/api/uploadFile",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultT<String> handleFileUpload(@RequestPart(value = "file") MultipartFile file, @RequestParam("path") String path);
}

