package com.piesat.portal.web.controller;

import com.piesat.common.constant.FileTypesConstant;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.Doc2PDF;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.rpc.api.FileManageService;
import com.piesat.portal.rpc.dto.FileManageDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value="文件管理",tags= {"文件管理"})
@RequestMapping("/portal/fileManage")
@RestController
public class FileManageController {
    @Autowired
    private FileManageService fileManageService;

    @Value("${serverfile.portalfile}")
    private String fileAddress;


    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean<FileManageDto>> list(FileManageDto fileManageDto, int pageNum, int pageSize) {
        ResultT<PageBean<FileManageDto>> resultT = new ResultT<>();
        PageForm<FileManageDto> pageForm = new PageForm<>(pageNum, pageSize, fileManageDto);
        PageBean pageBean = fileManageService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "文件上传接口")
    @PostMapping(value = "/upload")
    public ResultT uploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取文件存储路径 , 如果没有 , 创建
            if (!Paths.get(fileAddress).toFile().exists()) {
                Paths.get(fileAddress).toFile().mkdirs();
            }
            List<MultipartFile> fileList = request.getFiles("fileName");
            MultipartFile mf = fileList.get(0);
            //文件路径
            String filePath = fileAddress + "/" + mf.getOriginalFilename();
            String fileSuffix = FilenameUtils.getExtension(mf.getOriginalFilename());
            boolean b = Arrays.stream(FileTypesConstant.PORTAL_FILE_ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileSuffix));
            if (!b) {
                response.setStatus(404);
                return ResultT.failed("文件格式不支持！");
            }
            //上传文件到服务器指定路径
            FileCopyUtils.copy(mf.getBytes(), new File(filePath));
            //转换PDF
            /*String pdfName = mf.getOriginalFilename().substring(0, mf.getOriginalFilename().lastIndexOf(".")) + ".pdf";
            String pdfPath = fileAddress + "/" + pdfName;
            if(!fileSuffix.equalsIgnoreCase("pdf")){
                Doc2PDF.doc2pdf(filePath, pdfPath);
            }*/
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("fileName", mf.getOriginalFilename());
            resultMap.put("filePath", filePath);
            resultMap.put("fileSuffix", fileSuffix);
            return ResultT.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    @PostMapping(value="/saveFileManage")
    @ApiOperation(value="新增文件管理（portal）",notes="新增文件管理（portal）")
    public ResultT saveFileManage(@RequestBody FileManageDto fileManageDto){
        try {
            FileManageDto save = this.fileManageService.saveDto(fileManageDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.fileManageService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            FileManageDto fileManageDto = this.fileManageService.getDotById(id);
            return ResultT.success(fileManageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑", notes = "编辑")
    public ResultT<FileManageDto> edit(@RequestBody FileManageDto fileManageDto)
    {
        ResultT<FileManageDto> resultT=new ResultT<>();
        fileManageDto= this.fileManageService.updateDto(fileManageDto);
        resultT.setData(fileManageDto);
        return resultT;
    }

}
