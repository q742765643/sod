package com.piesat.dm.web.controller.dataapply;

import com.piesat.common.constant.FileTypesConstant;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.dataapply.CloudDatabaseApplyService;
import com.piesat.dm.rpc.dto.dataapply.CloudDatabaseApplyDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:18
 */
@RestController
@RequestMapping("/dm/cloudDatabaseApply")
@Api(value = "云数据库审核", tags = {"云数据库审核"})
public class CloudDatabaseApplyController {

    @Autowired
    private CloudDatabaseApplyService cloudDatabaseApplyService;
    @GrpcHthtClient
    private UserService userService;

    @GrpcHthtClient
    private DictDataService dictDataService;

    @Value("${serverfile.cloud}")
    private String fileAddress;

    @GetMapping("/list")
    @RequiresPermissions("dm:cloudDatabaseApply:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(CloudDatabaseApplyDto cloudDatabaseApplyDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<CloudDatabaseApplyDto> pageForm = new PageForm<>(pageNum, pageSize, cloudDatabaseApplyDto);
        PageBean pageBean = cloudDatabaseApplyService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "申请文件上传接口")
    @PostMapping(value = "/upload")
    @RequiresPermissions("dm:cloudDatabaseApply:upload")
    public ResultT uploadFile(MultipartHttpServletRequest request) {
        try {
            // 获取文件存储路径 , 如果没有 , 创建
            if (!Paths.get(fileAddress).toFile().exists()) {
                Paths.get(fileAddress).toFile().mkdirs();
            }
            List<MultipartFile> fileList = request.getFiles("filename");
            MultipartFile mf = fileList.get(0);
            //文件路径
            String filePath = fileAddress + mf.getOriginalFilename();
            String fileSuffix = FilenameUtils.getExtension(mf.getOriginalFilename());
            boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileSuffix));
            if (!b) {
                return ResultT.failed("文件格式不支持！");
            }
            //上传文件到服务器指定路径
            FileCopyUtils.copy(mf.getBytes(), new File(filePath));
            return ResultT.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "申请文件下载接口")
    //@RequiresPermissions("dm:cloudDatabaseApply:download")
    @GetMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String apply_material) {
        String fullPath = fileAddress + File.separator + request.getParameter("apply_material");
        File file = new File(fullPath);
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

    @PostMapping(value = "/save")
    @RequiresPermissions("dm:cloudDatabaseApply:save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody CloudDatabaseApplyDto cloudDatabaseApplyDto) {
        try {
            cloudDatabaseApplyDto.setExamineStatus("02");
            cloudDatabaseApplyDto.setExamineTime(new Date());
            CloudDatabaseApplyDto save = this.cloudDatabaseApplyService.saveDto(cloudDatabaseApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @RequiresPermissions("dm:cloudDatabaseApply:edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<CloudDatabaseApplyDto> edit(@RequestBody CloudDatabaseApplyDto cloudDatabaseApplyDto) {
        ResultT<CloudDatabaseApplyDto> resultT = new ResultT<>();
        cloudDatabaseApplyDto.setExamineTime(new Date());
        cloudDatabaseApplyDto = this.cloudDatabaseApplyService.updateDto(cloudDatabaseApplyDto);
        resultT.setData(cloudDatabaseApplyDto);
        return resultT;
    }

    @ApiOperation(value = "新增/编辑(portal调用，form表单类型)")
    @RequiresPermissions("dm:cloudDatabaseApply:addOrUpdate")
    @PostMapping(value = "/addOrUpdate")
    public ResultT addOrUpdate(HttpServletRequest request, @RequestParam(value = "examineMaterial", required = false) MultipartFile applyMaterial) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            File newFile = null;
            if (applyMaterial != null) {
                String originalFileName1 = applyMaterial.getOriginalFilename();//旧的文件名(用户上传的文件名称)
                if (StringUtils.isNotNullString(originalFileName1)) {
                    //再次进行文件格式判断，防止绕过js上传非法格式文件
                    String extension = FilenameUtils.getExtension(originalFileName1);
                    boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(extension));
                   if (!b){
                        return ResultT.failed("文件格式错误");
                    }
                    //新的文件名
                    String newFileName1 = originalFileName1.substring(0, originalFileName1.lastIndexOf(".")) + "_" + DateUtils.parseDateToStr("YYYYMMDDHHMMSS", new Date()) + originalFileName1.substring(originalFileName1.lastIndexOf("."));
                    newFile = new File(fileAddress + File.separator + newFileName1);
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    //存入
                    applyMaterial.transferTo(newFile);
                }
            }
            CloudDatabaseApplyDto cloudDatabaseApplyDto = cloudDatabaseApplyService.addOrUpdate(parameterMap, newFile == null ? "" : newFile.getPath());
            return ResultT.success(cloudDatabaseApplyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/updateExamineStatus")
    @RequiresPermissions("dm:cloudDatabaseApply:updateExamineStatus")
    @ApiOperation(value = "根据申请id修改审核状态", notes = "根据申请id修改审核状态")
    public ResultT<CloudDatabaseApplyDto> updateExamineStatus(String id, String examineStatus) {
        ResultT<CloudDatabaseApplyDto> resultT = new ResultT<>();
        CloudDatabaseApplyDto cloudDatabaseApplyDto = this.cloudDatabaseApplyService.updateExamineStatus(id, examineStatus);
        resultT.setData(cloudDatabaseApplyDto);
        return resultT;
    }

    @GetMapping(value = "/getById")
    @RequiresPermissions("dm:cloudDatabaseApply:getById")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public ResultT<CloudDatabaseApplyDto> getById(String id) {
        ResultT<CloudDatabaseApplyDto> resultT = new ResultT<>();
        CloudDatabaseApplyDto cloudDatabaseApplyDto = this.cloudDatabaseApplyService.getDotById(id);
        resultT.setData(cloudDatabaseApplyDto);
        return resultT;
    }

    @DeleteMapping("/deleteById")
//    @RequiresPermissions("dm:cloudDatabaseApply:deleteById")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> remove(String id) {
        ResultT<String> resultT = new ResultT<>();
        this.cloudDatabaseApplyService.deleteById(id);
        return resultT;
    }

    @DeleteMapping("/deleteByIdPortal")
    @RequiresPermissions("dm:cloudDatabaseApply:deleteByIdPortal")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> deleteByIdPortal(@RequestBody CloudDatabaseApplyDto cloudDatabaseApplyDto) {
        ResultT<String> resultT = new ResultT<>();
        this.cloudDatabaseApplyService.deleteById(cloudDatabaseApplyDto.getId());
        return resultT;
    }

    @ApiOperation(value = "根据字典类型查询")
    @GetMapping("/getDictDataByType/{type}")
    @RequiresPermissions("dm:cloudDatabaseApply:getDictDataByType")
    public ResultT getDictDataByType(@PathVariable String type) {
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType(type);
        return ResultT.success(dictDataDtos);
    }

    @GetMapping(value = "/getByUserId")
    @RequiresPermissions("dm:cloudDatabaseApply:getByUserId")
    @ApiOperation(value = "根据用户id查询", notes = "根据用户id查询")
    public ResultT<List<CloudDatabaseApplyDto>> getByUserId(String userId) {
        ResultT<List<CloudDatabaseApplyDto>> resultT = new ResultT<>();
        List<CloudDatabaseApplyDto> cloudDatabaseApplyDtos = this.cloudDatabaseApplyService.getByUserId(userId);
        resultT.setData(cloudDatabaseApplyDtos);
        return resultT;
    }

    @GetMapping(value = "/getRecentTime")
    @RequiresPermissions("dm:cloudDatabaseApply:getRecentTime")
    @ApiOperation(value = "根据存储编码和cts编码查询进线时间", notes = "根据存储编码和cts编码查询进线时间")
    public ResultT getRecentTime(String classDataId, String ctsCode) {
        Map<String, Object> map = this.cloudDatabaseApplyService.getRecentTime(classDataId, ctsCode);
        return ResultT.success(map);
    }

    @GetMapping(value = "/getAllPortalUser")
    @ApiOperation(value = "获取所有业务用户", notes = "获取所有业务用户")
    public ResultT getAllPortalUser() {
        List<UserDto> userDtos = this.userService.findByUserType("11");
        return ResultT.success(userDtos);
    }

    @ApiOperation(value = "申请材料是否存在")
    @GetMapping(value = "fileIsExist")
    public ResultT fileIsExist(HttpServletRequest request, String apply_material) {
        try {
            //String fileName = fileAddress + File.separator + apply_material;
            String fileName = apply_material;
            File testFile = new File(fileName);
            if (testFile.exists()) {
                return ResultT.success();
            }
            return ResultT.failed();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
