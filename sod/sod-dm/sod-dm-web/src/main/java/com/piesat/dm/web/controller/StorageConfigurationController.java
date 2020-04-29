package com.piesat.dm.web.controller;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.util.ExportTableUtil;
import com.piesat.dm.rpc.StorageConfigParam;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/11 18:13
 */
@RestController
@RequestMapping("/dm/storageConfiguration")
@Api(value = "存储结构概览", tags = {"存储结构概览"})
public class StorageConfigurationController {

    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private StorageConfigurationService storageConfigurationService;
    @Autowired
    private GrpcService storageConfigurationGrpcService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(StorageConfigParam scp,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, String> map = new HashMap<String, String>();

        if (StringUtils.isNotNullString(scp.getDatabaseName())) {
            map.put("database_name", scp.getDatabaseName());
        }
        if (StringUtils.isNotNullString(scp.getSpecialDatabaseName())) {
            map.put("special_database_name", scp.getSpecialDatabaseName());
        }
        if (StringUtils.isNotNullString(scp.getClassName())) {
            map.put("class_name", scp.getClassName());
        }
        if (StringUtils.isNotNullString(scp.getDataClassId())) {
            map.put("parent_id", scp.getDataClassId());
        }
        if (StringUtils.isNotNullString(scp.getDDataId())) {
            map.put("d_data_id", scp.getDDataId());
        }
        if (StringUtils.isNotNullString(scp.getLogicName())) {
            map.put("logic_name", scp.getLogicName());
        }
        if (StringUtils.isNotNullString(scp.getTableName())) {
            map.put("table_name", scp.getTableName());
        }
        if (StringUtils.isNotNullString(scp.getDataClassId())) {
            map.put("data_class_id", scp.getDataClassId());
        }
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String, String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = storageConfigurationService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    @GetMapping("/storageFieldList")
    @ApiOperation(value = "存储字段检索条件分页查询", notes = "存储字段检索条件分页查询")
    public ResultT<PageBean> storageFieldList(StorageConfigParam scp,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotNullString(scp.getClassName())) {
            map.put("class_name", scp.getClassName());
        }
        if (StringUtils.isNotNullString(scp.getLogicName())) {
            map.put("logic_name", scp.getLogicName());
        }
        if (StringUtils.isNotNullString(scp.getTableName())) {
            map.put("table_name", scp.getTableName());
        }
        if (StringUtils.isNotNullString(scp.getCElementCode())) {
            map.put("c_element_code", scp.getCElementCode());
        }
        if (StringUtils.isNotNullString(scp.getDbEleCode())) {
            map.put("db_ele_code", scp.getDbEleCode());
        }
        if (StringUtils.isNotNullString(scp.getUserEleCode())) {
            map.put("user_ele_code", scp.getUserEleCode());
        }
        if (StringUtils.isNotNullString(scp.getEleName())) {
            map.put("ele_name", scp.getEleName());
        }
        if (StringUtils.isNotNullString(scp.getType())) {
            map.put("type", scp.getType());
        }
        if (StringUtils.isNotNullString(scp.getAccuracy())) {
            map.put("accuracy", scp.getAccuracy());
        }

        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String, String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = storageConfigurationService.storageFieldList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }


    @GetMapping("/getDataClassSuper")
    @ApiOperation(value = "查询资料顶级分类", notes = "查询资料顶级分类")
    public ResultT<List<Map<String, Object>>> getDataClassSuper() {
        List<Map<String, Object>> dataTypeList = this.dataClassService.getDataTypeList();
        return ResultT.success(dataTypeList);
    }

    @GetMapping(value = "/exportTable",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "导出", notes = "导出")
    @ResponseBody
    public byte[] exportTable_1(StorageConfigurationDto storageConfigurationDto, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        if (storageConfigurationDto.getDatabaseDto() != null) {
            if (storageConfigurationDto.getDatabaseDto().getDatabaseDefine() != null) {
                if (StringUtils.isNotNullString(storageConfigurationDto.getDatabaseDto().getDatabaseDefine().getDatabaseName())) {
                    map.put("database_name", storageConfigurationDto.getDatabaseDto().getDatabaseDefine().getDatabaseName());
                }
            }
            if (StringUtils.isNotNullString(storageConfigurationDto.getDatabaseDto().getDatabaseName())) {
                map.put("special_database_name", storageConfigurationDto.getDatabaseDto().getDatabaseName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getClassName())) {
                map.put("class_name", storageConfigurationDto.getDataClassDto().getClassName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDataClassId())) {
                map.put("parent_id", storageConfigurationDto.getDataClassDto().getDataClassId());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDDataId())) {
                map.put("d_data_id", storageConfigurationDto.getDataClassDto().getDDataId());
            }
        }
        if (storageConfigurationDto.getLogicDefineDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getLogicDefineDto().getLogicName())) {
                map.put("logic_name", storageConfigurationDto.getLogicDefineDto().getLogicName());
            }
        }
        if (storageConfigurationDto.getDataTableDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataTableDto().getTableName())) {
                map.put("table_name", storageConfigurationDto.getDataTableDto().getTableName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDataClassId())) {
                map.put("data_class_id", storageConfigurationDto.getDataClassDto().getDataClassId());
            }
        }
        Map<String, Object> headAndData = storageConfigurationService.exportTable(map);
        return ExportTableUtil.exportTableNew(request, response, (List<String>) headAndData.get("headList"), (List<List<String>>) headAndData.get("lists"), "存储数据概览导出");
    }

    @GetMapping(value = "/exportTable_1")
    @ApiOperation(value = "导出", notes = "导出")
    public void exportTable(StorageConfigurationDto storageConfigurationDto, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        if (storageConfigurationDto.getDatabaseDto() != null) {
            if (storageConfigurationDto.getDatabaseDto().getDatabaseDefine() != null) {
                if (StringUtils.isNotNullString(storageConfigurationDto.getDatabaseDto().getDatabaseDefine().getDatabaseName())) {
                    map.put("database_name", storageConfigurationDto.getDatabaseDto().getDatabaseDefine().getDatabaseName());
                }
            }
            if (StringUtils.isNotNullString(storageConfigurationDto.getDatabaseDto().getDatabaseName())) {
                map.put("special_database_name", storageConfigurationDto.getDatabaseDto().getDatabaseName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getClassName())) {
                map.put("class_name", storageConfigurationDto.getDataClassDto().getClassName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDataClassId())) {
                map.put("parent_id", storageConfigurationDto.getDataClassDto().getDataClassId());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDDataId())) {
                map.put("d_data_id", storageConfigurationDto.getDataClassDto().getDDataId());
            }
        }
        if (storageConfigurationDto.getLogicDefineDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getLogicDefineDto().getLogicName())) {
                map.put("logic_name", storageConfigurationDto.getLogicDefineDto().getLogicName());
            }
        }
        if (storageConfigurationDto.getDataTableDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataTableDto().getTableName())) {
                map.put("table_name", storageConfigurationDto.getDataTableDto().getTableName());
            }
        }
        if (storageConfigurationDto.getDataClassDto() != null) {
            if (StringUtils.isNotNullString(storageConfigurationDto.getDataClassDto().getDataClassId())) {
                map.put("data_class_id", storageConfigurationDto.getDataClassDto().getDataClassId());
            }
        }
        Map<String, Object> headAndData = storageConfigurationService.exportTable(map);
        ExportTableUtil.exportTable(request, response, (List<String>) headAndData.get("headList"), (List<List<String>>) headAndData.get("lists"), "存储数据概览导出");
    }

    @PostMapping(value = "/updateColumnValue")
    @ApiOperation(value = "修改配置参数值", notes = "修改配置参数值")
    public ResultT<String> updateColumnValue(String id, String column, String value) {
        return storageConfigurationGrpcService.updateColumnValue(id, column, value);
    }

    @DeleteMapping(value = "/deleteColumnValue")
    @ApiOperation(value = "根据ID删除资料以及配置", notes = "根据ID删除资料以及配置")
    public ResultT<String> deleteById(String id) {
        if (StringUtils.isEmpty(id)){
            return ResultT.failed("配置不存在！");
        }
        return storageConfigurationGrpcService.deleteById(id);
    }
}
