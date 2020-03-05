package com.piesat.schedule.client.api;

import com.piesat.util.constant.GrpcConstant;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}

