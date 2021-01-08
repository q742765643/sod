package com.piesat.dm.web.controller.dataapply;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.piesat.common.constant.FileTypesConstant;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyEntity;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyFeedbackService;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyLogService;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyService;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyDto;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyFeedbackDto;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyLogDto;
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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
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
import java.net.URI;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.*;
import java.lang.String;

import static com.piesat.util.page.CloudHttpUtil.*;

@RestController
@RequestMapping("/dm/yunDatabaseApply")
@Api(value = "中间件",tags = {"中间件"})
public class YunDatabaseApplyController {

    @Value("${cloudDeploy.url}")
    String CloudURL;
    @Value("${cloudDeploy.loginUrl}")
    String loginUrl;
    @Value("${cloudDeploy.cloudName}")
    String cloudName;
    @Value("${cloudDeploy.cloudPassword}")
    String cloudPassword;
    @Autowired
    private YunDatabaseApplyService yunDatabaseApplyService;

    @Autowired
    private YunDatabaseApplyLogService yunDatabaseApplyLogService;

    @Autowired
    private YunDatabaseApplyFeedbackService yunDatabaseApplyFeedbackService;

    @Value("${serverfile.yun}")
    private String fileAddress;

    @GrpcHthtClient
    private DictDataService dictDataService;

    @GrpcHthtClient
    private UserService userService;

    @PostMapping(value = "/save")
    @RequiresPermissions("dm:yunDatabaseApply:save")
    @ApiOperation(value = "添加",notes = "添加")
    public ResultT save(@RequestBody YunDatabaseApplyDto yunDatabaseApplyDto) {
        try {
            yunDatabaseApplyDto.setExamineStatus("02");
            yunDatabaseApplyDto.setUpdateTime(new Date());
            yunDatabaseApplyDto.setExamineTime(new Date());
            YunDatabaseApplyDto save = this.yunDatabaseApplyService.saveDto(yunDatabaseApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "申请文件上传接口")
    @PostMapping(value = "/upload")
    @RequiresPermissions("dm:yunDatabaseApply:upload")
    public ResultT uploadFile(MultipartHttpServletRequest request) {
        try {
            // 获取文件存储路径 , 如果没有则创建
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
                out.println("</script>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "新增(portal调用，form表单类型)")
    @RequiresPermissions("dm:yunDatabaseApply:addorUpdate")
    @PostMapping(value = "/addorUpdate")
    public ResultT addorUpdate(HttpServletRequest request, @RequestParam(value = "examineMaterial", required = false) MultipartFile applyMaterial) {
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
            YunDatabaseApplyDto yunDatabaseApplyDto = yunDatabaseApplyService.addorUpdate(parameterMap, newFile == null ? "" : newFile.getPath());
            String logId = yunDatabaseApplyDto.getId();
            String examineMaterial = yunDatabaseApplyDto.getExamineMaterial();
            yunDatabaseApplyLogService.addLogEdit1(parameterMap,logId,examineMaterial);
            return ResultT.success(yunDatabaseApplyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "编辑(portal调用，form表单类型)")
    @RequiresPermissions("dm:yunDatabaseApply:addorUpdate2")
    @PostMapping(value = "/addorUpdate2")
    public ResultT addorUpdate2(HttpServletRequest request, @RequestParam(value = "examineMaterial", required = false) MultipartFile applyMaterial) {
//        ResultT<PageBean> resultT = new ResultT<>();
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String[] data = parameterMap.get("data");
//            YunDatabaseApplyEntity yunDatabaseApplyEntity = new YunDatabaseApplyEntity();
            JSONObject object = new JSONObject(data[0]);
            String id = (String) object.get("id");
            YunDatabaseApplyDto yunDatabaseApplyDto1 = yunDatabaseApplyService.getById1(id);
            File newFile = null;
            if (yunDatabaseApplyDto1 == null || yunDatabaseApplyDto1.getExamineStatus() == "04" || yunDatabaseApplyDto1.getExamineStatus() == "01") {
                return ResultT.failed("变更失败，原因为实例状态改变或实例已删除");
            } else {
                if (applyMaterial != null) {
                    String originalFileName1 = applyMaterial.getOriginalFilename();//旧的文件名(用户上传的文件名称)
                    if (StringUtils.isNotNullString(originalFileName1)) {
                        //再次进行文件格式判断，防止绕过js上传非法格式文件
                        String extension = FilenameUtils.getExtension(originalFileName1);
                        boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(extension));
                        if (!b) {
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
                YunDatabaseApplyDto yunDatabaseApplyDto = yunDatabaseApplyService.addorUpdate(parameterMap, newFile == null ? "" : newFile.getPath());
                String logId = yunDatabaseApplyDto.getId();
                String examineMaterial = yunDatabaseApplyDto.getExamineMaterial();
                yunDatabaseApplyLogService.addLogEdit1(parameterMap, logId, examineMaterial);
                return ResultT.success(yunDatabaseApplyDto);
            }
            } catch(Exception e){
                e.printStackTrace();
                return ResultT.failed(e.getMessage());
            }

    }

    @ApiOperation(value = "新增反馈信息")
    @RequiresPermissions("dm:yunDatabaseApply:addFeedback")
    @PostMapping(value = "/addFeedback")
    public ResultT addFeedback(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
//System.out.println("----------"+material);
            YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto = yunDatabaseApplyFeedbackService.addFeedback(parameterMap);
            return ResultT.success(yunDatabaseApplyFeedbackDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @GetMapping ("/listFeedback")
    @RequiresPermissions("dm:yunDatabaseApply:listFeedback")
    @ApiOperation(value = "反馈条件分页查询", notes = "反馈条件分页查询")
    public ResultT<PageBean> listFeedback(YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<YunDatabaseApplyFeedbackDto> pageForm = new PageForm<>(pageNum, pageSize, yunDatabaseApplyFeedbackDto);
        PageBean pageBean = yunDatabaseApplyFeedbackService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    @GetMapping(value = "/getByItserviceId")
    @RequiresPermissions("dm:yunDatabaseApply:getByItserviceId")
    @ApiOperation(value ="反馈实例ID查询",notes = "反馈查询")
    public ResultT<List<YunDatabaseApplyFeedbackDto>> getByItserviceId(String itserviceId, String feedbackStatus){
        ResultT<List<YunDatabaseApplyFeedbackDto>> resultT = new ResultT<>();
        List<YunDatabaseApplyFeedbackDto> yunDatabaseApplyFeedbackDtos = this.yunDatabaseApplyFeedbackService.getByItserviceId(itserviceId,"%"+feedbackStatus+"%");
        resultT.setData(yunDatabaseApplyFeedbackDtos);
        return resultT;
    }
    @GetMapping(value = "/getByFeedId")
    @RequiresPermissions("dm:yunDatabaseApply:getByFeedId")
    @ApiOperation(value ="反馈查询",notes = "反馈查询")
    public ResultT<YunDatabaseApplyFeedbackDto> getByFeedId(String id){
        ResultT<YunDatabaseApplyFeedbackDto> resultT = new ResultT<>();
        YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDtos = this.yunDatabaseApplyFeedbackService.getFeeById(id);
        resultT.setData(yunDatabaseApplyFeedbackDtos);
        return resultT;
    }
    @GetMapping("/updateFeedbackStatus")
    @RequiresPermissions("dm:yunDatabaseApply:updateFeedbackStatus")
    @ApiOperation(value = "根据id修改反馈状态", notes = "根据id修改反馈状态")
    public ResultT<YunDatabaseApplyFeedbackDto> updateFeedbackStatus(String id, String feedbackStatus) {
        ResultT<YunDatabaseApplyFeedbackDto> resultT = new ResultT<>();
        YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto = this.yunDatabaseApplyFeedbackService.updateFeedbackStatus(id, feedbackStatus);
        resultT.setData(yunDatabaseApplyFeedbackDto);
        return resultT;
    }
    @PutMapping("/editFeedback")
    @RequiresPermissions("dm:yunDatabaseApply:editFeedback")
    @ApiOperation(value = "编辑反馈信息", notes = "编辑反馈信息")
    public ResultT<YunDatabaseApplyFeedbackDto> editFeedback(@RequestBody YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto) {
        ResultT<YunDatabaseApplyFeedbackDto> resultT = new ResultT<>();
        yunDatabaseApplyFeedbackDto = this.yunDatabaseApplyFeedbackService.updateDto(yunDatabaseApplyFeedbackDto);
        resultT.setData(yunDatabaseApplyFeedbackDto);
        return resultT;
    }
    @DeleteMapping("/deleteByFeedbackId")
    @ApiOperation(value = "根据itserviceId删除反馈信息", notes = "根据itserviceId删除反馈信息")
    public ResultT<String> removeFeedback(String itserviceId, String feedbackStatus) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyFeedbackService.deleteByFeedbackId(itserviceId,"%"+feedbackStatus+"%");
        return resultT;
    }
    @DeleteMapping("/deleteByFeedId")
    @ApiOperation(value = "根据Id删除反馈信息", notes = "根据Id删除反馈信息")
    public ResultT<String> removeFeedId(String id) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyFeedbackService.deleteById(id);
        return resultT;
    }
    @DeleteMapping("/deleteFeedIdPortal")
    @RequiresPermissions("dm:yunDatabaseApply:deleteFeedIdPortal")
    @ApiOperation(value = "portal删除变更记录", notes = "portal删除变更记录")
    public ResultT<String> deleteFeedIdPortal(@RequestBody YunDatabaseApplyLogDto yunDatabaseApplyLogDto) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyFeedbackService.deleteById(yunDatabaseApplyLogDto.getId());
        return resultT;
    }
    @PostMapping(value = "/saveLog")
    @RequiresPermissions("dm:yunDatabaseApply:saveLog")
    @ApiOperation(value = "添加变更",notes = "添加变更")
    public ResultT save(@RequestBody YunDatabaseApplyLogDto yunDatabaseApplyLogDto) {
        try {
            if(yunDatabaseApplyLogDto.getViewStatus()==null){
                yunDatabaseApplyLogDto.setViewStatus("2");
            }
            yunDatabaseApplyLogDto.setExamineStatus("02");
            yunDatabaseApplyLogDto.setUpdateTime(new Date());
            YunDatabaseApplyLogDto save = this.yunDatabaseApplyLogService.saveLog(yunDatabaseApplyLogDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @ApiOperation(value = "新增变更记录")
    @RequiresPermissions("dm:yunDatabaseApply:addLogEdit")
    @PostMapping(value = "/addLogEdit")
    public ResultT addLogEdit(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            YunDatabaseApplyLogDto yunDatabaseApplyLogDto = yunDatabaseApplyLogService.addLogEdit(parameterMap);
            return ResultT.success(yunDatabaseApplyLogDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
    @GetMapping(value = "/getByLogId")
    @RequiresPermissions("dm:yunDatabaseApply:getByLogId")
    @ApiOperation(value ="变更查询",notes = "变更查询")
    public ResultT<List<YunDatabaseApplyLogDto>> getByLogId(String logId){
        ResultT<List<YunDatabaseApplyLogDto>> resultT = new ResultT<>();
        List<YunDatabaseApplyLogDto> yunDatabaseApplyLogDtos = this.yunDatabaseApplyLogService.getByLogId(logId);
        resultT.setData(yunDatabaseApplyLogDtos);
        return resultT;
    }
    @DeleteMapping("/deleteByLogIdPortal")
    @RequiresPermissions("dm:yunDatabaseApply:deleteByLogIdPortal")
    @ApiOperation(value = "portal删除变更记录", notes = "portal删除变更记录")
    public ResultT<String> deleteByLogIdPortal(@RequestBody YunDatabaseApplyLogDto yunDatabaseApplyLogDto) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyLogService.deleteByLogId(yunDatabaseApplyLogDto.getLogId());
        return resultT;
    }

    @DeleteMapping("/deleteByLogId")
    @ApiOperation(value = "根据id删除变更记录", notes = "根据id删除变更记录")
    public ResultT<String> removeLog(String logId) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyLogService.deleteByLogId(logId);
        return resultT;
    }

    @GetMapping(value = "/getRecentTime")
    @RequiresPermissions("dm:yunDatabaseApply:getRecentTime")
    @ApiOperation(value = "根据存储编码和cts编码查询进线时间", notes = "根据存储编码和cts编码查询进线时间")
    public ResultT getRecentTime(String classDataId, String ctsCode) {
        Map<String, Object> map = this.yunDatabaseApplyService.getRecentTime(classDataId, ctsCode);
        return ResultT.success(map);
    }

    @GetMapping ("/list")
    @RequiresPermissions("dm:yunDatabaseApply:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(YunDatabaseApplyDto yunDatabaseApplyDto, int pageNum, int pageSize) {
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<YunDatabaseApplyDto> pageForm = new PageForm<>(pageNum, pageSize, yunDatabaseApplyDto);
        PageBean pageBean = yunDatabaseApplyService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping(value = "/getByUserId")
    @RequiresPermissions("dm:yunDatabaseApply:getByUserId")
    @ApiOperation(value = "根据用户id查询", notes = "根据用户id查询")
    public ResultT<List<YunDatabaseApplyDto>> getByUserId(String userId) {
        ResultT<List<YunDatabaseApplyDto>> resultT = new ResultT<>();
        List<YunDatabaseApplyDto> yunDatabaseApplyDtos = this.yunDatabaseApplyService.getByUserId(userId);
        resultT.setData(yunDatabaseApplyDtos);
        return resultT;
    }




    @GetMapping(value = "/getByDNES")
    @RequiresPermissions("dm:yunDatabaseApply:getByDNES")
    @ApiOperation(value ="Portal查询",notes = "Portal查询")
    public ResultT<List<YunDatabaseApplyDto>> getByDNES(String userId,String storageLogic,String examineStatus,String displayname){
        ResultT<List<YunDatabaseApplyDto>> resultT = new ResultT<>();
        List<YunDatabaseApplyDto> yunDatabaseApplyDtos = this.yunDatabaseApplyService.getByDNES(userId,storageLogic,"%"+examineStatus+"%","%"+displayname+"%");
        resultT.setData(yunDatabaseApplyDtos);
        return resultT;
    }

    @GetMapping(value = "/getByUserIdStorageLogic")
    @RequiresPermissions("dm:yunDatabaseApply:getByUserIdStorageLogic")
    @ApiOperation(value = "根据用户id,类型查询", notes = "根据用户id，类型查询")
    public ResultT<List<YunDatabaseApplyDto>> getByUserIdStorageLogic(String userId,String storageLogic) {
        ResultT<List<YunDatabaseApplyDto>> resultT = new ResultT<>();
        List<YunDatabaseApplyDto> yunDatabaseApplyDtos = this.yunDatabaseApplyService.getByUserIdStorageLogic(userId,storageLogic);
        resultT.setData(yunDatabaseApplyDtos);
        return resultT;
    }

    @GetMapping(value = "/getById")
    @RequiresPermissions("dm:yunDatabaseApply:getById")
    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    public ResultT<YunDatabaseApplyDto> getById(String id) {
        ResultT<YunDatabaseApplyDto> resultT = new ResultT<>();
        try {
            YunDatabaseApplyDto yunDatabaseApplyDto = this.yunDatabaseApplyService.getDotById(id);
            resultT.setData(yunDatabaseApplyDto);
            return resultT;
        }catch (Exception e){
            resultT.setCode(202);
            resultT.setMsg("未找到该实例");
            return resultT;
        }

    }

    @DeleteMapping("/deleteByIdPortal")
    @RequiresPermissions("dm:yunDatabaseApply:deleteByIdPortal")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> deleteByIdPortal(@RequestBody YunDatabaseApplyDto yunDatabaseApplyDto) {
        ResultT<String> resultT = new ResultT<>();
        YunDatabaseApplyDto yunDatabaseApplyDtos = this.yunDatabaseApplyService.getDotById(yunDatabaseApplyDto.getId());
//        System.out.println(yunDatabaseApplyDtos.getItserviceId());
        if(yunDatabaseApplyDtos.getItserviceId() != null){
            resultT.setCode(400);
        }else{
            this.yunDatabaseApplyService.deleteById(yunDatabaseApplyDto.getId());
        }

        return resultT;
    }

    @ApiOperation(value = "申请材料是否存在")
    @GetMapping(value = "fileIsExist")
    public ResultT fileIsExist(HttpServletRequest request, String apply_material) {
        try {
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

    @ApiOperation(value = "根据字典类型查询")
    @GetMapping("/getDictDataByType/{type}")
    @RequiresPermissions("dm:yunDatabaseApply:getDictDataByType")
    public ResultT getDictDataByType(@PathVariable String type) {
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType(type);
        return ResultT.success(dictDataDtos);
    }

    @GetMapping(value = "/getAllPortalUser")
    @ApiOperation(value = "获取所有业务用户", notes = "获取所有业务用户")
    public ResultT getAllPortalUser() {
        List<UserDto> userDtos = this.userService.findByUserType("11");
        return ResultT.success(userDtos);
    }

    @DeleteMapping("/deleteById")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResultT<String> remove(String id) {
        ResultT<String> resultT = new ResultT<>();
        this.yunDatabaseApplyService.deleteById(id);
        return resultT;
    }

    @GetMapping("/updateExamineStatus")
    @RequiresPermissions("dm:yunDatabaseApply:updateExamineStatus")
    @ApiOperation(value = "根据申请id修改审核状态", notes = "根据申请id修改审核状态")
    public ResultT<YunDatabaseApplyDto> updateExamineStatus(String id, String examineStatus) {
        ResultT<YunDatabaseApplyDto> resultT = new ResultT<>();
        YunDatabaseApplyDto yunDatabaseApplyDto = this.yunDatabaseApplyService.updateExamineStatus(id, examineStatus);
        resultT.setData(yunDatabaseApplyDto);
        return resultT;
    }

    @PutMapping("/edit")
    @RequiresPermissions("dm:yunDatabaseApply:edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<YunDatabaseApplyDto> edit(@RequestBody YunDatabaseApplyDto yunDatabaseApplyDto) {
        ResultT<YunDatabaseApplyDto> resultT = new ResultT<>();
        try {
            YunDatabaseApplyDto yunDatabaseApplyDto1 = yunDatabaseApplyService.getById1(yunDatabaseApplyDto.getId());
            yunDatabaseApplyDto.setExamineTime(new Date());
            yunDatabaseApplyDto = this.yunDatabaseApplyService.updateDto(yunDatabaseApplyDto);
            resultT.setData(yunDatabaseApplyDto);
            return resultT;
        }catch (Exception e){
            resultT.setCode(202);
            return resultT;
        }
//        yunDatabaseApplyDto.getId();
    }

    @PostMapping("/getTemp")
    @RequiresPermissions("dm:yunDatabaseApply:getTemp")
    @ApiOperation(value = "模板信息", notes = "模板信息")
    public ResultT getTemp(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
System.out.println(requestData+"-------");
        JSONObject jsonObject = new JSONObject(requestData);
//        String id = jsonObject.getStr("id");//获取key为"_source"的值
        String type = jsonObject.getStr("type");
        String url = CloudURL+type+"/deploy/temp";
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/getNode")
    @RequiresPermissions("dm:yunDatabaseApply:getNode")
    @ApiOperation(value = "节点信息", notes = "节点信息")
    public ResultT getNode(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
//System.out.println(requestData);
        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为"_source"的值
        String type = jsonObject.getStr("type");
        String url = CloudURL+type+"/"+id+"/nodes";
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @GetMapping(value = "/getByIdPortal")
    @RequiresPermissions("dm:yunDatabaseApply:getByIdPortal")
    @ApiOperation(value = "节点信息查询", notes = "节点信息查询")
    public ResultT getByIdPortal(String type,String id) throws UnsupportedEncodingException{
        ResultT resultT = new ResultT<>();
        String url = CloudURL+type+"/"+id+"/nodes";
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/getContainers")
    @RequiresPermissions("dm:yunDatabaseApply:getContainers")
    @ApiOperation(value = "容器信息", notes = "容器信息")
    public ResultT getContainers(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();

        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为"_source"的值
        String type = jsonObject.getStr("type");
        String nodeId = jsonObject.getStr("nodeId");
        String url = CloudURL+type+"/"+id+"/nodes/"+nodeId+"/containers";
//        System.out.println(requestData);
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }

    @PostMapping("/getLink")
    @RequiresPermissions("dm:yunDatabaseApply:getLink")
    @ApiOperation(value = "链接信息", notes = "链接信息")
    public ResultT getLink(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为"_source"的值
        String type = jsonObject.getStr("type");
        String url = CloudURL+type+"/"+id+"/linkMessage";
//        System.out.println(requestData);
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }

    @PostMapping("/getMonitorDataNew")
    @RequiresPermissions("dm:yunDatabaseApply:getMonitorDataNew")
    @ApiOperation(value = "监控数据信息", notes = "监控数据信息")
    public ResultT getMonitorDataNew(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为"_source"的值
        String type = jsonObject.getStr("type");
        String nodeId = jsonObject.getStr("nodeId");
        String step = jsonObject.getStr("step");
        String startDate = jsonObject.getStr("startDate");
        String endDate = jsonObject.getStr("endDate");
        String zType = jsonObject.getStr("zType");
        String ZZ = URLEncoder.encode(zType);
        String url = CloudURL+type+"/"+id+"/nodes/"+nodeId+"/monitors?step="+step+"&type="+ZZ+"&startDate="+startDate+"&endDate="+endDate;
//        System.out.println("+++++++++"+zType);
//        System.out.println("+++++++++"+ZZ);
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/getJournal")
    @RequiresPermissions("dm:yunDatabaseApply:getJournal")
    @ApiOperation(value = "运行日志", notes = "运行日志")
    public ResultT getJournal(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为""的值
        String type = jsonObject.getStr("type");
        String nodeId = jsonObject.getStr("nodeId");
        String container = jsonObject.getStr("container");
        String url = CloudURL+type+"/"+id+"/nodes/"+nodeId+"/logs?rows=1000&name="+container;
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }

    @PostMapping("/getConfigNew")
    @RequiresPermissions("dm:yunDatabaseApply:getConfigNew")
    @ApiOperation(value = "监控配置", notes = "监控配置")
    public ResultT getConfigNew(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        System.out.println(requestData + "+++++++++");
        JSONObject jsonObject = new JSONObject(requestData);
        System.out.println(requestData);
        String id = jsonObject.getStr("id");//获取key为""的值
        String type = jsonObject.getStr("type");
        String url = CloudURL+type+"/"+id+"/monitors/configurations";
        resultT.setData(doGet(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }

    @PostMapping("/deleteNew")
    @RequiresPermissions("dm:yunDatabaseApply:deleteNew")
    @ApiOperation(value = "删除实例", notes = "删除实例")
    public ResultT deleteNew(@RequestBody String requestData) {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String id = jsonObject.getStr("id");//获取key为""的值
        String type = jsonObject.getStr("type");
        String url = CloudURL+type+"/"+id;
        resultT.setData(doDelete(url,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/deployNew")
    @RequiresPermissions("dm:yunDatabaseApply:deployNew")
    @ApiOperation(value = "部署实例", notes = "部署实例")
    public ResultT deployNew(@RequestBody String requestData) {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String type = jsonObject.getStr("storageLogic");
        String url = CloudURL+type;
        resultT.setData(doPost(url,requestData,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/editDeployNew")
    @RequiresPermissions("dm:yunDatabaseApply:editDeployNew")
    @ApiOperation(value = "修改实例", notes = "修改实例")
    public ResultT editDeployNew(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String type = jsonObject.getStr("storageLogic");
        String id = jsonObject.getStr("itserviceId");
        String url = CloudURL+type+"/"+id+"/configurations";
        resultT.setData(doPut(url,requestData,loginUrl,cloudName,cloudPassword));
        return resultT;
    }
    @PostMapping("/editConfigNew")
    @RequiresPermissions("dm:yunDatabaseApply:editConfigNew")
    @ApiOperation(value = "修改监控配置", notes = "修改监控配置")
    public ResultT editConfigNew(@RequestBody String requestData) throws UnsupportedEncodingException {
        ResultT resultT = new ResultT<>();
        JSONObject jsonObject = new JSONObject(requestData);
        String type = jsonObject.getStr("type");
        String id = jsonObject.getStr("id");
        String data = jsonObject.getStr("data");

//        System.out.println("修改数据"+data);
        String url = CloudURL+type+"/"+id+"/monitors/configurations";
        resultT.setData(doPut(url,data,loginUrl,cloudName,cloudPassword));
        return resultT;
    }

}
