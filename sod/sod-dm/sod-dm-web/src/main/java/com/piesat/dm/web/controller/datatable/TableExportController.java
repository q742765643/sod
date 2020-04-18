package com.piesat.dm.web.controller.datatable;

import com.alibaba.fastjson.JSONArray;
import com.piesat.dm.common.util.FreeMarkerUtil;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.api.datatable.TableExportService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表结构导出
 * @author wulei
 * @date 2020年2月5日 9:33:01
 */
@Api(tags = "表结构导出")
@RequestMapping("/dm/tableExport")
@RestController
public class TableExportController {
    @Autowired
    private LogicDefineService logicDefineService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private TableExportService tableExportService;
    @Autowired
    private DataClassService dataClassService;
    @Value("${fileUpload.httpPath}")
    private String outFilePath;

    @ApiOperation(value = "数据用途列表")
    @RequiresPermissions("dm:tableExport:logicDefineList")
    @GetMapping(value = "/logicDefineList")
    public ResultT logicDefineList() {
        try {
            List<LogicDefineDto> allLogicDefine = this.logicDefineService.getAllLogicDefine();
            LogicDefineDto logicDefine = new LogicDefineDto();
            logicDefine.setLogicFlag("");
            logicDefine.setLogicName("全部");
            allLogicDefine.add(0,logicDefine);
            return ResultT.success(allLogicDefine);
        }catch (Exception e){
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "数据库列表")
    @RequiresPermissions("dm:tableExport:databaseList")
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

    @ApiOperation(value = "资料分类树")
    @RequiresPermissions("dm:tableExport:dataTree")
    @GetMapping(value = "/dataTree")
    public ResultT dataTree(String use_id,String database_id) {
        try {
            JSONArray all = this.dataClassService.getSimpleTree();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "表结构导出-完整版")
    @RequiresPermissions("dm:tableExport:exportTable")
    @GetMapping(value = "/exportTable")
    public ResultT exportTableInfo(String use_id, String database_id, String data_class_ids,
                                HttpServletRequest request, HttpServletResponse response){
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //构建数据结构
            Map<String,Object> dataMap = tableExportService.getExportMap(database_id,data_class_ids);
            //制作模板
			//boolean createModel = exportTableService.createModel(request);
            //用逻辑库名称和物理库名称拼接文件名称
//            String logicName = tableExportService.getLogicName(use_id);
            String databaseName = tableExportService.getDatabaseName(database_id);
            String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String outFileName = databaseName+"_"+nowtime+".doc";
            //导出文件
            File file = new FreeMarkerUtil().createDoc(dataMap, outFilePath+"/"+outFileName,"template_simple.ftl",request);
            System.out.println(file.getAbsolutePath());
            Map<String,Object> map = new HashMap<>();
            map.put("filePath",file.getAbsolutePath());
            map.put("fileName",file.getName());
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        } finally{
            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is !=null){
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "表结构导出-简版")
    @RequiresPermissions("dm:tableExport:exportTableSimple")
    @GetMapping(value = "/exportTableSimple")
    public ResultT exportTableSimple(String use_id, String database_id, String data_class_ids,
                                   HttpServletRequest request, HttpServletResponse response){
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //构建数据结构
            Map<String,Object> dataMap = tableExportService.getExportMapSimple(database_id,data_class_ids);
            //制作模板
            //boolean createModel = exportTableService.createModel(request);
            //用逻辑库名称和物理库名称拼接文件名称
//            String logicName = tableExportService.getLogicName(use_id);
            String databaseName = tableExportService.getDatabaseName(database_id);
            String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String outFileName = databaseName+"_"+nowtime+".doc";
            //导出文件
            File file = new FreeMarkerUtil().createDoc(dataMap, outFilePath+"/"+outFileName,"template_simple.ftl",request);
            System.out.println(file.getAbsolutePath());
            Map<String,Object> map = new HashMap<>();
            map.put("filePath",file.getAbsolutePath());
            map.put("fileName",file.getName());
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        } finally{
            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is !=null){
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "SQL导出")
    @RequiresPermissions("dm:tableExport:exportSQL")
    @GetMapping(value = "/exportSQL")
    public ResultT exportSQL(String databaseId, String dataClassIds, Integer exportType) {
        try {
            Map<String,Object> map = tableExportService.exportSqlFile(databaseId,dataClassIds,exportType,outFilePath);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
