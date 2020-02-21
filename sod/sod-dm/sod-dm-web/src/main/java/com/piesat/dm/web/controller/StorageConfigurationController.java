package com.piesat.dm.web.controller;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.util.ExportTableUtil;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResultT<PageBean> list(HttpServletRequest request,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotNullString(request.getParameter("database_name"))){
            map.put("database_name",request.getParameter("database_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("special_database_name"))){
            map.put("special_database_name",request.getParameter("special_database_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("class_name"))){
            map.put("class_name",request.getParameter("class_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("parent_id"))){
            map.put("parent_id",request.getParameter("parent_id"));
        }
        if(StringUtils.isNotNullString(request.getParameter("d_data_id"))){
            map.put("d_data_id",request.getParameter("d_data_id"));
        }
        if(StringUtils.isNotNullString(request.getParameter("logic_name"))){
            map.put("logic_name",request.getParameter("logic_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("queryTable"))){
            map.put("queryTable",request.getParameter("queryTable"));
        }
        //存储字段检索
        if(StringUtils.isNotNullString(request.getParameter("c_element_code"))){
            map.put("c_element_code",request.getParameter("c_element_code"));
        }
        if(StringUtils.isNotNullString(request.getParameter("db_ele_code"))){
            map.put("db_ele_code",request.getParameter("db_ele_code"));
        }
        if(StringUtils.isNotNullString(request.getParameter("user_ele_code"))){
            map.put("user_ele_code",request.getParameter("user_ele_code"));
        }
        if(StringUtils.isNotNullString(request.getParameter("ele_name"))){
            map.put("ele_name",request.getParameter("ele_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("type"))){
            map.put("type",request.getParameter("type"));
        }
        if(StringUtils.isNotNullString(request.getParameter("accuracy"))){
            map.put("accuracy",request.getParameter("accuracy"));
        }

        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String,String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = storageConfigurationService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @GetMapping("/getDataClassSuper")
    @ApiOperation(value = "查询资料顶级分类", notes = "查询资料顶级分类")
    public ResultT<List<Map<String,Object>>> getDataClassSuper(){
        List<Map<String,Object>> dataTypeList = this.dataClassService.getDataTypeList();
        return ResultT.success(dataTypeList);
    }

    @PostMapping(value = "/exportTable")
    @ApiOperation(value = "导出", notes = "导出")
    public void exportTable(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotNullString(request.getParameter("database_name"))){
            map.put("database_name",request.getParameter("database_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("special_database_name"))){
            map.put("special_database_name",request.getParameter("special_database_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("class_name"))){
            map.put("class_name",request.getParameter("class_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("parent_id"))){
            map.put("parent_id",request.getParameter("parent_id"));
        }
        if(StringUtils.isNotNullString(request.getParameter("d_data_id"))){
            map.put("d_data_id",request.getParameter("d_data_id"));
        }
        if(StringUtils.isNotNullString(request.getParameter("logic_name"))){
            map.put("logic_name",request.getParameter("logic_name"));
        }
        Map<String, Object> headAndData = storageConfigurationService.exportTable(map);
        ExportTableUtil.exportTable(request, response, (List<String>)headAndData.get("headList"),(List<List<String>>)headAndData.get("lists") , "存储数据概览导出");
    }

    @PostMapping(value = "/updateColumnValue")
    @ApiOperation(value = "修改配置参数值", notes = "修改配置参数值")
    public ResultT<String> updateColumnValue(String id, String column, String value){
        return storageConfigurationGrpcService.updateColumnValue(id, column, value);
    }

    @DeleteMapping(value = "/updateColumnValue")
    @ApiOperation(value = "根据ID删除资料以及配置", notes = "根据ID删除资料以及配置")
    public ResultT<String> deleteById(String id){
        return storageConfigurationGrpcService.deleteById(id);
    }
}
