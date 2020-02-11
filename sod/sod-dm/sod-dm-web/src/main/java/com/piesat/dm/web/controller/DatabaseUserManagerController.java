package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.api.DatabaseUserService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据库访问账户管理
 * @Description 
 * @ClassName DatabaseUserManagerController
 * @author wulei
 * @date 2020/2/10 10:17
 */
@Api(tags = "数据库访问账户")
@RequestMapping("/dm/databaseUser")
@RestController
public class DatabaseUserManagerController {

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatabaseUserService databaseUserService;
    @Value("${serverfile.filePath}")
    private String fileAddress;

    @ApiOperation(value = "获取数据列表")
    @RequiresPermissions("dm:databaseUser:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DatabaseUserDto> all = this.databaseUserService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseUser:getById")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.getDotById(id);
            return ResultT.success(databaseUserDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库列表")
    @RequiresPermissions("dm:databaseUser:databaseList")
    @GetMapping(value = "/databaseList")
    public ResultT databaseList() {
        try {
            List<DatabaseDto> allDatabaseDto = this.databaseService.all();
            return ResultT.success(allDatabaseDto);
        }catch (Exception e){
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseUser:del")
    @Log(title = "数据库访问账户", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/delete")
    public ResultT delete(String id) {
        try {
            this.databaseUserService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value="申请文件上传接口",notes="申请文件上传接口")
    @RequiresPermissions("api:databaseUser:upload")
    @PostMapping(value="/api/databaseUser/upload")
    public ResultT uploadFile(MultipartHttpServletRequest request) {
        try {
            // 获取文件存储路径 , 如果没有 , 创建
            if(!Paths.get(fileAddress).toFile().exists()){
                Paths.get(fileAddress).toFile().mkdirs();
            }
            List<MultipartFile> fileList = request.getFiles("filename");
            Date now = new Date();
            MultipartFile mf = fileList.get(0);
            //文件路径
            String filePath = fileAddress + mf.getOriginalFilename();
            String fileSuffix = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf('.'));
            //上传文件到服务器指定路径
            FileCopyUtils.copy(mf.getBytes(), new File(filePath));
            return ResultT.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
