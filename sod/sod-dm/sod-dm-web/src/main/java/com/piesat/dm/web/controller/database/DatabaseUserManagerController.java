package com.piesat.dm.web.controller.database;

import com.piesat.common.constant.FileTypesConstant;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.AESUtil;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.Doc2PDF;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.database.DatabaseUserService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.DatabaseUserDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.SecurityUtils;
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
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.*;

/**
 * 数据库访问账户管理
 *
 * @author wulei
 * @Description
 * @ClassName DatabaseUserManagerController
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
    @GrpcHthtClient
    private UserService userService;
    @Value("${serverfile.user}")
    private String fileAddress;
    @Value("${fileUpload.httpPath}")
    private String httpPath;

    @Value("${businessParameters.databaseUserDefaultPassword}")
    private String defaultPassword;

    @ApiOperation(value = "分页获取数据列表")
    @RequiresPermissions("dm:databaseUser:all")
    @GetMapping("/all")
    public ResultT<PageBean> list(DatabaseUserDto databaseUserDto, int pageNum, int pageSize) {
        try {
            ResultT<PageBean> resultT = new ResultT<>();
            PageForm<DatabaseUserDto> pageForm = new PageForm<>(pageNum, pageSize, databaseUserDto);
            PageBean pageBean = databaseUserService.selectPageList(pageForm);
            resultT.setData(pageBean);
            return resultT;
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

    @ApiOperation(value = "根据用户id查询")
    @RequiresPermissions("dm:databaseUser:getByUserId")
    @GetMapping(value = "/getByUserId")
    public ResultT getByUserId(String userId) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.getDotByUserId(userId);
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
            List<Map<String, Object>> allDatabaseDto = this.databaseService.getDatabaseList("1,3");
            return ResultT.success(allDatabaseDto);
        } catch (Exception e) {
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
            this.databaseUserService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "申请文件上传接口")
    @RequiresPermissions("dm:databaseUser:upload")
    @PostMapping(value = "/api/databaseUser/upload")
    public ResultT uploadFile(MultipartHttpServletRequest request) {
        try {
            // 获取文件存储路径 , 如果没有 , 创建
            if (!Paths.get(fileAddress).toFile().exists()) {
                Paths.get(fileAddress).toFile().mkdirs();
            }
            List<MultipartFile> fileList = request.getFiles("filename");
            MultipartFile mf = fileList.get(0);
            //文件路径
            String filePath = fileAddress + "/" + mf.getOriginalFilename();
            String fileSuffix = FilenameUtils.getExtension(mf.getOriginalFilename());
            boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileSuffix));
            if (!b) {
                return ResultT.failed("文件格式不支持！");
            }
            //上传文件到服务器指定路径
            FileCopyUtils.copy(mf.getBytes(), new File(filePath));
            //转换PDF
            String pdfName = mf.getOriginalFilename().substring(0, mf.getOriginalFilename().lastIndexOf(".")) + ".pdf";
            String pdfPath = fileAddress + "/" + pdfName;
            if(!fileSuffix.equalsIgnoreCase("pdf")){
                Doc2PDF.doc2pdf(filePath, pdfPath);
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("filePath", filePath);
            resultMap.put("pdfPath", httpPath + "/user/" + pdfName);
            return ResultT.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "申请文件下载接口")
    @RequiresPermissions("api:databaseUser:download")
    @GetMapping(value = "/download")
    public void download(String filepath, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(filepath);
        String fileName = file.getName();

        if (file.exists()) {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
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
            if (databaseUserDto != null) {
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
            if (databaseUserDto != null) {
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
    public ResultT findByUserIdAndExamineStatus(String userId, String examineStatus) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.findByUserIdAndExamineStatus(userId, examineStatus);
            if (databaseUserDto != null) {
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
            //默认待审核
            databaseUserDto.setExamineStatus("0");
            DatabaseUserDto save = this.databaseUserService.saveDto(databaseUserDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取业务注册数据库用户")
    @RequiresPermissions("dm:databaseUser:getBizDatabaseUser")
    @GetMapping(value = "/getBizDatabaseUser")
    public ResultT getBizDatabaseUser(String userId, String databaseUpId) {
        DatabaseUserDto byUserId = this.databaseUserService.findByUserIdAndDatabaseUpId(userId, databaseUpId);
        return ResultT.success(byUserId);
    }


    @ApiOperation(value = "新增用户是否在数据库已经存在")
    @RequiresPermissions("dm:databaseUser:databaseUserExi")
    @GetMapping(value = "/databaseUserExi")
    public Boolean databaseUserExi(DatabaseUserDto databaseUserDto) {
        return this.databaseUserService.databaseUserExi(databaseUserDto);
    }

    @ApiOperation(value = "新增Bzi")
    @RequiresPermissions("dm:databaseUser:addBzi")
    @PostMapping(value = "/addBzi")
    public ResultT saveBzi(@RequestBody DatabaseUserDto databaseUserDto) {
        try {
            //默认待审核
            databaseUserDto.setExamineStatus("0");
            DatabaseUserDto save = null;
            UserDto userDto = this.userService.selectUserByUserName(databaseUserDto.getUserId());
            String pwd = AESUtil.aesDecrypt(userDto.getPassword()).trim();
            databaseUserDto.setDatabaseUpPassword(pwd);
            DatabaseUserDto byUserId = this.databaseUserService.findByUserIdAndDatabaseUpId(databaseUserDto.getUserId(), databaseUserDto.getDatabaseUpId());
            if (byUserId != null) {
                byUserId.setApplyDatabaseId(databaseUserDto.getApplyDatabaseId());
                byUserId.setDatabaseUpPassword(databaseUserDto.getDatabaseUpPassword());
                byUserId.setRemarks(databaseUserDto.getRemarks());
                byUserId.setDatabaseUpIp(databaseUserDto.getDatabaseUpIp());
                byUserId.setExamineStatus("0");
                save = this.databaseUserService.saveDto(byUserId);
            } else {
                save = this.databaseUserService.saveDto(databaseUserDto);
            }
            //数据库授权
            ResultT b = this.databaseUserService.empower(save);
            if (b.getCode() == 200) {
                save.setExamineStatus("1");
                DatabaseUserDto update = this.databaseUserService.mergeDto(save);
                return ResultT.success(update);
            } else {
                DatabaseUserDto update = this.databaseUserService.mergeDto(save);
                return ResultT.failed(b.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "审核")
    @RequiresPermissions("dm:databaseUser:update")
    @PostMapping(value = "/update")
    public ResultT update(@RequestBody DatabaseUserDto databaseUserDto) {
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        try {
            //审核通过，给数据库授权
            if (databaseUserDto.getExamineStatus().equals("1")) {
                //数据库授权
                ResultT b = this.databaseUserService.empower(databaseUserDto);
                if (b.getCode() == 200) {
                    databaseUserDto.setExamineStatus("1");
                    databaseUserDto.setExamineTime(new Date());
                    databaseUserDto.setExaminer(loginUser.getUserName());
                    DatabaseUserDto update = this.databaseUserService.mergeDto(databaseUserDto);
                    return ResultT.success(update);
                } else {
                    DatabaseUserDto update = this.databaseUserService.mergeDto(databaseUserDto);
                    return ResultT.failed(b.getMsg());
                }
            }
            //审核不通过
            databaseUserDto.setExamineStatus("2");
            databaseUserDto.setExamineTime(new Date());
            databaseUserDto.setExaminer(loginUser.getUserName());
            DatabaseUserDto update = this.databaseUserService.mergeDto(databaseUserDto);
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

    @ApiOperation(value = "数据库访问账户申请")
    @RequiresPermissions("dm:databaseUser:applyDatabaseUser")
    @PostMapping(value = "applyDatabaseUser")
    public ResultT applyDatabaseUser(HttpServletRequest request) {
        try {
            DatabaseUserDto applyDatabaseUser = this.databaseUserService.applyDatabaseUser(request);
            return ResultT.success(applyDatabaseUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库访问账户修改")
    @RequiresPermissions("dm:databaseUser:updateDatabaseUser")
    @PostMapping(value = "updateDatabaseUser")
    public ResultT updateDatabaseUser(HttpServletRequest request) {
        try {
            DatabaseUserDto updateDatabaseUser = this.databaseUserService.applyDatabaseUser(request);
            return ResultT.success(updateDatabaseUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增/编辑(portal调用，form表单类型)")
    @RequiresPermissions("dm:databaseUser:addOrUpdateDatabaseUser")
    @PostMapping(value = "/addOrUpdateDatabaseUser")
    public ResultT addOrUpdateDatabaseUser(HttpServletRequest request, @RequestParam(value = "apply_material", required = false) MultipartFile applyMaterial) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            File newFile = null;
            if (applyMaterial != null) {
                String originalFileName1 = applyMaterial.getOriginalFilename();//旧的文件名(用户上传的文件名称)
                if (StringUtils.isNotNullString(originalFileName1)) {
                    //新的文件名
                    String newFileName1 = originalFileName1.substring(0, originalFileName1.lastIndexOf(".")) + "_" + DateUtils.parseDateToStr("YYYYMMDDHHMMSS", new Date()) + originalFileName1.substring(originalFileName1.lastIndexOf("."));
                    String filePath = fileAddress + File.separator + newFileName1;
                    newFile = new File(filePath);
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    //存入
                    applyMaterial.transferTo(newFile);
                    if (newFile.getName().endsWith(".pdf") || newFile.getName().endsWith(".PDF")) {
                        parameterMap.put("pdfPath", new String[]{httpPath + "/filePath/" + newFile.getName()});
                    } else {
                        //转换PDF
                        String pdfName = newFileName1.substring(0, newFileName1.lastIndexOf(".")) + ".pdf";
                        String pdfPath = fileAddress + "/" + pdfName;
                        Doc2PDF.doc2pdf(filePath, pdfPath);
                        parameterMap.put("pdfPath", new String[]{httpPath + "/user/" + pdfName});
                    }
                }
            }
            DatabaseUserDto databaseUserDto = databaseUserService.addOrUpdate(parameterMap, newFile == null ? "" : newFile.getPath());
            return ResultT.success(databaseUserDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "数据库访问账户申请材料是否存在")
    @RequiresPermissions("dm:databaseUser:fileIsExist")
    @PostMapping(value = "fileIsExist")
    public ResultT fileIsExist(HttpServletRequest request) {
        try {
            String fileNames = request.getParameter("apply_material");
            fileNames = URLDecoder.decode(fileNames, "UTF-8");
            String fileName = request.getSession().getServletContext().getRealPath("") + File.separator + "tupian"
                    + File.separator + fileNames;
            File testFile = new File(fileName);
            if (!testFile.exists()) {
                return ResultT.success("NO");
            }
            return ResultT.success("YES");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "判断用户是否存在数据库访问账户(已审核通过)")
    @RequiresPermissions("dm:databaseUser:existUser")
    @GetMapping(value = "/existUser")
    public ResultT existUser(String userId) {
        try {
            DatabaseUserDto databaseUserDto = this.databaseUserService.findByUserIdAndExamineStatus(userId, "1");
            Map<String, Object> map = new HashMap<>();
            if (null == databaseUserDto.getDatabaseUpId() || databaseUserDto.getDatabaseUpId().equals("")) {
                map.put("returnCode", 1);
                map.put("returnMessage", "当前用户没有UP账户");
                map.put("databaseUP_ID", "-");
                map.put("databaseUP_PASSWORD", "-");
                return ResultT.success(map);
            } else {
                map.put("returnCode", 0);
                map.put("returnMessage", "当前用户存在UP账户");
                map.put("databaseUP_ID", databaseUserDto.getDatabaseUpId());
                map.put("databaseUP_PASSWORD", databaseUserDto.getDatabaseUpPassword());
                map.put("examine_status", databaseUserDto.getExamineStatus());
                return ResultT.success(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "针对具体物理库撤销读写权限")
    @RequiresPermissions("api:databaseUser:dataAuthorityCancel")
    @PostMapping(value = "/api/databaseUser/dataAuthorityCancel")
    public ResultT dataAuthorityCancel(String user_id, String database_id, String data_class_id, String apply_authority, String mark) {
        // 针对具体库中表撤销读写权限
        Integer apply_authoritys = Integer.parseInt(apply_authority);
        Map<String, Object> map = databaseUserService.dataAuthorityCancel(user_id, database_id, data_class_id, apply_authoritys, mark);
        return ResultT.success(map);
    }

    @ApiOperation(value = "获取数据库访问账户默认密码（Portal调用）")
    @RequiresPermissions("dm:databaseUser:getDefaultPassword")
    @GetMapping(value = "/getDefaultPassword")
    public ResultT<String> getDefaultPassword() {
        try {
            ResultT<String> resultT = new ResultT<>();
            resultT.setData(defaultPassword);
            return resultT;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改密码（Portal调用）")
    @RequiresPermissions("dm:databaseUser:changePassword")
    @GetMapping(value = "/changePassword")
    public ResultT changePassword(String id, String oldPwd, String newPwd) {
        try {
            return databaseUserService.changePassword(id, oldPwd, newPwd);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
