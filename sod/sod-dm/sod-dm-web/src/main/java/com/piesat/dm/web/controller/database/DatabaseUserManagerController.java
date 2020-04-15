package com.piesat.dm.web.controller.database;

import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
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

    @ApiOperation(value = "分页获取数据列表")
    @RequiresPermissions("dm:databaseUser:all")
    @GetMapping("/all")
    public ResultT<PageBean> list(DatabaseUserDto databaseUserDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<DatabaseUserDto> pageForm = new PageForm<>(pageNum, pageSize, databaseUserDto);
        PageBean pageBean = databaseUserService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
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

    @ApiOperation(value="申请文件上传接口")
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

    @ApiOperation(value="申请文件下载接口")
    @RequiresPermissions("api:databaseUser:download")
    @GetMapping(value="/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String fullPath = request.getParameter("filePath");
        File file = new File(fullPath);
        String fileName = file.getName();

        if(file.exists()){
            try {
                String userAgent = request.getHeader("User-Agent");
                if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                    //IE浏览器处理
                    fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                } else {
                    // 非IE浏览器的处理：
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
                // 以流的形式下载文件。
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                outputStream.write(buffer);
                outputStream.flush();
                outputStream.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            try {
                response.setContentType("text/html; charset=UTF-8"); //转码
                PrintWriter out = response.getWriter();
                out.flush();
                out.println("<script defer='defer' type='text/javascript'>");
                out.println("alert('文件不存在或已经被删除！');");
//                out.println("window.location='/AnnualStatistics/downloadList';");
                out.println("</script>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "判断UP账户是否已存在")
    @RequiresPermissions("dm:databaseUser:ifUPExist")
    @GetMapping(value = "/ifUPExist")
    public ResultT ifUPExist(String databaseUPId) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.getDotByUPID(databaseUPId);
            if(databaseUserDto!=null){
                return ResultT.success("YES");
            }
            return ResultT.success("NO");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "判断申请账户是否已存在(待审核或已审核通过)")
    @RequiresPermissions("dm:databaseUser:ifUserExist")
    @GetMapping(value = "/ifUserExist")
    public ResultT ifUserExist(String userId) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.getDotByUserId(userId);
            if(databaseUserDto!=null){
                return ResultT.success("YES");
            }
            return ResultT.success("NO");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id和审核状态查询")
    @RequiresPermissions("dm:databaseUser:findByUserIdAndExamineStatus")
    @GetMapping(value = "/findByUserIdAndExamineStatus")
    public ResultT findByUserIdAndExamineStatus(String userId,String examineStatus) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.findByUserIdAndExamineStatus(userId,examineStatus);
            if(databaseUserDto!=null){
                return ResultT.success(databaseUserDto);
            }
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:databaseUser:add")
    @PostMapping(value = "/add")
    public ResultT save(@RequestBody DatabaseUserDto databaseUserDto) {
        try {
            DatabaseUserDto save = this.databaseUserService.saveDto(databaseUserDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "审核")
    @RequiresPermissions("dm:databaseUser:update")
    @PostMapping(value = "/update")
    public ResultT update(@RequestBody DatabaseUserDto databaseUserDto) {
        try {
            //审核通过，给数据库授权
            if(databaseUserDto.getExamineStatus().equals("1")){
                //数据库授权
                boolean b = this.databaseUserService.empower(databaseUserDto);
                if(b){
                    DatabaseUserDto update = this.databaseUserService.saveDto(databaseUserDto);
                    return ResultT.success(update);
                }
                return ResultT.success("数据库授权失败，审核操作未完成");
            }
            //审核不通过
            DatabaseUserDto update = this.databaseUserService.saveDto(databaseUserDto);
            return ResultT.success(update);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "导出")
    @RequiresPermissions("dm:databaseUser:exportData")
    @GetMapping(value = "/exportData")
    public void exportData(String examineStatus) {
        this.databaseUserService.exportData(examineStatus);
    }
}
