package com.piesat.dm.web.controller.special;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.constant.FileTypesConstant;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.special.DatabaseSpecialAccessDao;
import com.piesat.dm.entity.special.DatabaseSpecialAccessEntity;
import com.piesat.dm.rpc.api.special.DatabaseSpecialAuthorityService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.api.special.DatabaseSpecialService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.special.*;
import com.piesat.dm.rpc.service.special.DatabaseSpecialTreeServiceImpl;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 专题库管理
 *
 * @author wulei
 * @Description
 * @ClassName DatabaseSpecialController
 * @date 2020/2/12 15:56
 */
@Api(tags = "专题库管理")
@RequestMapping("/dm/databaseSpecial")
@RestController
public class DatabaseSpecialController {

    @Autowired
    private DatabaseSpecialService databaseSpecialService;
    @Autowired
    private DatabaseSpecialReadWriteService databaseSpecialReadWriteService;
    @Autowired
    private DatabaseSpecialAuthorityService databaseSpecialAuthorityService;
    @Autowired
    private DatabaseSpecialTreeServiceImpl databaseSpecialTreeService;

    @Value("${serverfile.special}")
    private String fileAddress;

    @ApiOperation(value = "分页查询")
    @RequiresPermissions("dm:databaseSpecial:page")
    @GetMapping(value = "/page")
    public ResultT<PageBean<DatabaseSpecialDto>> getPage(DatabaseSpecialDto databaseSpecialDto,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean<DatabaseSpecialDto>> resultT = new ResultT<>();
        PageForm<DatabaseSpecialDto> pageForm = new PageForm<>(pageNum, pageSize, databaseSpecialDto);
        PageBean pageBean = databaseSpecialService.getPage(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库列表")
//    @RequiresPermissions("dm:databaseSpecial:specialList")
    @GetMapping(value = "/specialList")
    public ResultT<PageBean> list(DatabaseSpecialDto databaseSpecialDto, int pageNum, int pageSize) {
        try {
            ResultT<PageBean> resultT = new ResultT<>();
            PageForm<DatabaseSpecialDto> pageForm = new PageForm<>(pageNum, pageSize, databaseSpecialDto);
            PageBean pageBean = databaseSpecialService.selectPageList(pageForm);
            resultT.setData(pageBean);
            return resultT;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:databaseSpecial:getById")
    @GetMapping(value = "/getById")
    public ResultT get(String id) {
        try {
            DatabaseSpecialDto databaseSpecialDto = this.databaseSpecialService.getDotById(id);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据用户id查询")
    //@RequiresPermissions("dm:databaseSpecial:findByUserId")
    @GetMapping(value = "/findByUserId")
    public ResultT findByUserId(String userId) {
        try {
            List<DatabaseSpecialDto> byUserId = this.databaseSpecialService.findByUserId(userId);
            return ResultT.success(byUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取专题库资料列表")
//    @RequiresPermissions("dm:databaseSpecial:getSpecialDataList")
    @GetMapping(value = "/getSpecialDataList")
    public ResultT getSpecialDataList(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            List<DatabaseSpecialReadWriteDto> dataList = this.databaseSpecialReadWriteService.getDotList(databaseSpecialReadWriteDto);
            return ResultT.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:databaseSpecial:del")
    @DeleteMapping(value = "/delete")
    public ResultT delete(String id) {
        try {
            this.databaseSpecialService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id和用戶id删除专题库访问申请表中记录（撤销引用）")
    @GetMapping(value = "/deleteAccessBySdbIdAndUserId")
    public ResultT deleteAccessBySdbIdAndUserId(String sdbId, String userId) {
        try {
            this.databaseSpecialService.deleteAccessBySdbIdAndUserId(sdbId, userId);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增/编辑(portal调用，form表单类型)")
    //@RequiresPermissions("dm:databaseSpecial:update")
    @PostMapping(value = "/addOrUpdate")
    public ResultT addOrUpdate(HttpServletRequest request, @RequestParam(value = "APPLY_FILE_PATH", required = false) MultipartFile applyMaterial) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            File newFile = null;
            if (applyMaterial != null) {
                String originalFileName1 = applyMaterial.getOriginalFilename();//旧的文件名(用户上传的文件名称)
                String fileSuffix = FilenameUtils.getExtension(originalFileName1);
                boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileSuffix));
                if (!b) {
                    return ResultT.failed("文件格式不支持！");
                }
                if (StringUtils.isNotNullString(originalFileName1)) {
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
            Map<String, String> map = new LinkedHashMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (entry.getValue().length > 0) {
                    map.put(entry.getKey(), entry.getValue()[0]);
                }
            }
            DatabaseSpecialDto databaseSpecialDto = databaseSpecialService.addOrUpdate(map, newFile == null ? "" : newFile.getPath());
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id修改专题库使用状态")
    //@RequiresPermissions("dm:databaseSpecial:updateUseStatusById")
    @Log(title = "专题库管理", businessType = BusinessType.UPDATE)
    @GetMapping(value = "/updateUseStatusById")
    public ResultT updateUseStatusById(String sdbId, String useStatus) {
        try {
            DatabaseSpecialDto save = this.databaseSpecialService.updateUseStatusById(sdbId, useStatus);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据获取专题库对应数据库权限")
//    @RequiresPermissions("dm:databaseSpecial:getAuthorityBySdbId")
    @GetMapping(value = "/getAuthorityBySdbId")
    public ResultT getAuthorityBySdbId(String sdbId) {
        try {
            List<DatabaseSpecialAuthorityDto> authorityList = this.databaseSpecialAuthorityService.getAuthorityBySdbId(sdbId);
            return ResultT.success(authorityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库授权")
//    @RequiresPermissions("dm:databaseSpecial:empowerDatabaseSpecial")
    @PostMapping(value = "/empowerDatabaseSpecial")
    public ResultT empowerDatabaseSpecial(@RequestBody SpecialParamVO specialParamVO) {
        try {
            //授权
            DatabaseDto databaseDto = new DatabaseDto();
            databaseDto.setUserId(specialParamVO.getUserId());
            DatabaseDefineDto databaseDefineDto = new DatabaseDefineDto();
            databaseDefineDto.setDatabaseInstance(specialParamVO.getSimpleName());
            databaseDto.setDatabaseDefine(databaseDefineDto);
            databaseDto.setTdbId(specialParamVO.getSdbId());
            databaseDto.setDatabaseSpecialAuthorityList(specialParamVO.getDatabaseSpecialAuthorityList());
            databaseDto.setSchemaName(specialParamVO.getSimpleName());
            databaseDto.setDatabaseName(specialParamVO.getSdbName());
            this.databaseSpecialService.empowerDatabaseSpecial(databaseDto);
            //修改授权状态
            DatabaseSpecialDto databaseSpecialDto = databaseSpecialService.getDotById(specialParamVO.getSdbId());
            databaseSpecialDto.setExamineStatus("2");
            databaseSpecialDto.setExamineTime(new Date());
            databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改审核状态")
//    @RequiresPermissions("dm:databaseSpecial:updateExamineStatus")
    @GetMapping(value = "/updateExamineStatus")
    public ResultT updateExamineStatus(String sdbId, String examineStatus) {
        try {
            //修改授权状态
            DatabaseSpecialDto databaseSpecialDto = databaseSpecialService.getDotById(sdbId);
            databaseSpecialDto.setExamineStatus(examineStatus);
            databaseSpecialDto.setExamineTime(new Date());
            databaseSpecialService.saveDto(databaseSpecialDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @Data
    private static class SpecialParamVO {
        //专题库ID
        String sdbId;
        //专题库名称
        String sdbName;
        //专题库简称
        String simpleName;
        //数据库ID
        String databaseId;
        //账户ID
        String userId;
        //审核状态
        String examineStatus;
        //权限列表
        List<DatabaseSpecialAuthorityDto> DatabaseSpecialAuthorityList;
    }

    @ApiOperation(value = "资料授权-单独")
//    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataOne")
    public ResultT empowerDataOne(@RequestBody DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        try {
            this.databaseSpecialService.empowerDataOne(databaseSpecialReadWriteDto);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "资料授权-批量")
//    @RequiresPermissions("dm:databaseSpecial:empowerDataOne")
    @PostMapping(value = "/empowerDataBatch")
    public ResultT empowerDataBatch(@RequestBody String listString) {
        try {
            List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList = JSONObject.parseArray(listString,
                    DatabaseSpecialReadWriteDto.class);
            this.databaseSpecialService.empowerDataBatch(databaseSpecialReadWriteDtoList);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树和资料信息")
    //@RequiresPermissions("dm:databaseSpecial:getDataTreeBySdbId")
    @GetMapping(value = "/getDataTreeBySdbId")
    public ResultT getDataTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getDataTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID获取对应树信息")
    //@RequiresPermissions("dm:databaseSpecial:getTreeBySdbId")
    @GetMapping(value = "/getTreeBySdbId")
    public ResultT getTreeBySdbId(String sdbId) {
        try {
            Map<String, Object> treeBySdbId = this.databaseSpecialService.getTreeBySdbId(sdbId);
            return ResultT.success(treeBySdbId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据审核状态查询")
//    @RequiresPermissions("dm:databaseSpecial:getByExamineStatus")
    @GetMapping(value = "/getByExamineStatus")
    public ResultT getByExamineStatus(String examineStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByExamineStatus(examineStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询其他用户创建并且没有被该用户引用的所有专题库信息")
    @RequiresPermissions("dm:databaseSpecial:getAllOtherRecordByUserId")
    @GetMapping(value = "/getAllOtherRecordByUserId")
    public ResultT getAllOtherRecordByUserId(String userId, String useStatus) {
        try {
            List<Map<String, Object>> databaseSpecialDtos = this.databaseSpecialService.getAllOtherRecordByUserId(userId, useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库id查询专题库申请资料（去除该用户申请的资料授权）")
    @RequiresPermissions("dm:databaseSpecial:getRecordSpecialByTdbId")
    @GetMapping(value = "/getRecordSpecialByTdbId")
    public ResultT getRecordSpecialByTdbId(String sdbId, String userId) {
        try {
            List<Map<String, Object>> recordSpecial = this.databaseSpecialReadWriteService.getRecordSpecialByTdbId(sdbId, userId);
            return ResultT.success(recordSpecial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据用户id和使用状态查询")
    //@RequiresPermissions("dm:databaseSpecial:getByUserIdAndUseStatus")
    @GetMapping(value = "/getByUserIdAndUseStatus")
    public ResultT getByUserIdAndUseStatus(String userId, String useStatus) {
        try {
            List<Map<String, Object>> databaseSpecialList = this.databaseSpecialService.getByUserIdAndUseStatus(userId, useStatus);
            return ResultT.success(databaseSpecialList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据使用状态查询专题库")
    @GetMapping(value = "/getByUseStatus")
    public ResultT getByUseStatus(String useStatus) {
        try {
            List<DatabaseSpecialDto> databaseSpecialDtos = this.databaseSpecialService.getByUseStatus(useStatus);
            return ResultT.success(databaseSpecialDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "插入同一专题库下的多条记录")
    //@RequiresPermissions("dm:databaseSpecial:saveMultilRecord")
    @PostMapping(value = "/saveMultilRecord")
    @Log(title = "插入同一专题库下的多条记录", businessType = BusinessType.INSERT)
    public ResultT saveMultilRecord(@RequestBody DatabaseSpecialDto databaseSpecialDto) {
        try {
            databaseSpecialDto = this.databaseSpecialService.saveMultilRecord(databaseSpecialDto);
            return ResultT.success(databaseSpecialDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id和专题库id查询专题库访问申请记录（引用申请）")
    //@RequiresPermissions("dm:databaseSpecial:getSpecialAccess")
    @GetMapping(value = "/getSpecialAccess")
    public ResultT getSpecialAccess(String tdbId, String userId) {
        try {
            DatabaseSpecialAccessDto databaseSpecialAccessDto = this.databaseSpecialService.getSpecialAccess(tdbId, userId);
            return ResultT.success(databaseSpecialAccessDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "专题库引用申请")
    @PostMapping(value = "/specialAccessApply")
    public ResultT specialAccessApply(@RequestBody DatabaseSpecialAccessDto databaseSpecialAccessDto) {
        try {
            databaseSpecialAccessDto = this.databaseSpecialService.specialAccessApply(databaseSpecialAccessDto);
            return ResultT.success(databaseSpecialAccessDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "专题库引用授权")
    @PostMapping(value = "/specialAccessAutho")
    public ResultT specialAccessAutho(@RequestBody DatabaseSpecialAccessDto databaseSpecialAccessDto) {
        try {
            databaseSpecialAccessDto = this.databaseSpecialService.specialAccessAutho(databaseSpecialAccessDto);
            return ResultT.success(databaseSpecialAccessDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id和专题库状态修改")
    @RequiresPermissions("dm:databaseSpecial:updateBySql")
    @GetMapping(value = "/updateBySql")
    public ResultT updateBySql(HttpServletRequest request) {
        try {
            String tdbId = request.getParameter("tdbId");
            String userId = request.getParameter("userId");
            String cause = request.getParameter("cause");
            String examineStatus = request.getParameter("examineStatus");
            Map<String, Object> map = this.databaseSpecialService.updateBySql(tdbId, userId, cause, examineStatus);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "下根据专题库ID号获取对应专题库信息和该专题库中对应数据信息")
    //@RequiresPermissions("dm:databaseSpecial:getRecordByTdbId")
    @GetMapping(value = "/getRecordByTdbId")
    public ResultT getRecordByTdbId(String tdbId, String typeId, String status) {
        try {
            Map<String, Object> map = this.databaseSpecialService.getRecordByTdbId(tdbId, typeId, status);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID题号获取对应专库信息和对应数据信息")
    //@RequiresPermissions("dm:databaseSpecial:getOneRecordByTdbId")
    @GetMapping(value = "/getOneRecordByTdbId")
    public ResultT getOneRecordByTdbId(String tdbId, String typeId, String status) {
        try {
            Map<String, Object> map = this.databaseSpecialService.getOneRecordByTdbId(tdbId, typeId, status);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "修改资料申请状态")
    @RequiresPermissions("dm:databaseSpecial:changeDataStatus")
    @GetMapping(value = "/changeDataStatus")
    public ResultT changeDataStatus(String tdbId, String physical, String data_class_id) {
        try {
            Map<String, Object> map = this.databaseSpecialReadWriteService.changeDataStatus(tdbId, physical, data_class_id);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "专题库资料清单删除")
    //@RequiresPermissions("dm:databaseSpecial:deleteRecords")
    @PostMapping(value = "/deleteRecords")
    public ResultT deleteRecords(@RequestBody List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtos) {
        try {
            this.databaseSpecialReadWriteService.deleteRecords(databaseSpecialReadWriteDtos);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "保存专题库资料树")
    @RequiresPermissions("dm:databaseSpecial:saveTreeData")
    @PostMapping(value = "/saveTreeData")
    public  ResultT saveTreeData(@RequestBody DatabaseSpecialTreeDto databaseSpecialTreeDto){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.saveTreeData(databaseSpecialTreeDto);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID号和分类ID修改对应分类名称、上级分类、排序")
    @RequiresPermissions("dm:databaseSpecial:updateOneRecordByTdbId")
    @PostMapping(value = "/updateOneRecordByTdbId")
    public  ResultT updateOneRecordByTdbId(@RequestBody DatabaseSpecialTreeDto databaseSpecialTreeDto){
        try {
            Map<String,Object> map =  this.databaseSpecialTreeService.updateOneRecordByTdbId(databaseSpecialTreeDto);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID号和分类ID删除对应树信息")
    @RequiresPermissions("dm:databaseSpecial:deleteRecordByTdbId")
    @GetMapping(value = "/deleteRecordByTdbId")
    public ResultT deleteRecordByTdbId(String tdbId, String typeId) {
        try {
            Map<String, Object> map = this.databaseSpecialTreeService.deleteRecordByTdbId(tdbId, typeId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据专题库ID和存储编码来修改分类ID")
    //@RequiresPermissions("dm:databaseSpecial:updateTypeIdByTdbId")
    @GetMapping(value = "/updateTypeIdByTdbId")
    public ResultT updateTypeIdByTdbId(String tdbId, String dataClassId, String typeId) {
        try {
            Map<String, Object> map = this.databaseSpecialTreeService.updateTypeIdByTdbId(tdbId, dataClassId, typeId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "获取平台中所有专题库")
    @RequiresPermissions("dm:databaseSpecial:getAllSpecial")
    @GetMapping(value = "/getAllSpecial")
    public ResultT getAllSpecial(String userId) {
        try {
            Map<String, Object> map = this.databaseSpecialService.getAllSpecial(userId);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "提交专题库引用申请并将一条记录插入数据表")
    @RequiresPermissions("dm:databaseSpecial:saveOneRecord")
    @GetMapping(value = "/saveOneRecord")
    public ResultT saveOneRecord(HttpServletRequest request) {
        try {
            Map<String, Object> map = this.databaseSpecialService.saveOneRecord(request);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "专题库导出")
    @RequiresPermissions("dm:databaseSpecial:exportTable")
    @GetMapping("/exportTable")
    public void exportExcel(DatabaseSpecialDto databaseSpecialDto) {
        databaseSpecialService.exportExcel(databaseSpecialDto);
    }

}
