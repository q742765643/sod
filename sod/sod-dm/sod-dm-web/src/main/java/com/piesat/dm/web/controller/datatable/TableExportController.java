package com.piesat.dm.web.controller.datatable;

import com.alibaba.fastjson.JSONArray;
import com.piesat.dm.common.util.FreeMarkerUtil;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.LogicDefineService;
import com.piesat.dm.rpc.api.datatable.TableExportService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.dm.rpc.dto.datatable.ExportTableVO;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
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
import java.util.*;

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
    private DatabaseDefineService databaseDefineService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private TableExportService tableExportService;
    @Autowired
    private DataClassService dataClassService;
    @Value("${fileUpload.savaPath}")
    private String outFilePath;
    @Value("${serverfile.static.template.table-export-standard}")
    private String modelPath;

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
            List<DatabaseDefineDto> databaseDefineDtoList = this.databaseDefineService.getDatabaseDefineList();
            List<DatabaseDto> databaseDtoList = databaseService.all();
            List<DatabaseDto> resultList = new ArrayList<>();
            if(databaseDefineDtoList!=null&&databaseDtoList!=null){
                for(DatabaseDefineDto databaseDefine : databaseDefineDtoList){
                    for(DatabaseDto databaseDto : databaseDtoList){
                        if(databaseDto.getDatabaseDefine().getId().equals(databaseDefine.getId())){
                            resultList.add(databaseDto);
                        }
                    }
                }
            }
            return ResultT.success(resultList);
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
            JSONArray all = this.dataClassService.getSimpleTree(database_id);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "表结构导出-完整版")
    @RequiresPermissions("dm:tableExport:exportTable")
    @PostMapping(value = "/exportTable")
    public ResultT exportTableInfo(@RequestBody ExportTableVO exportTableVO,
                                HttpServletRequest request, HttpServletResponse response){
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //构建数据结构
            Map<String,Object> dataMap = tableExportService.getExportMap(exportTableVO.getDatabase_id(),exportTableVO.getData_class_ids());
            //制作模板
			//boolean createModel = exportTableService.createModel(request);
            //用逻辑库名称和物理库名称拼接文件名称
//            String logicName = tableExportService.getLogicName(use_id);
            String databaseName = tableExportService.getDatabaseName(exportTableVO.getDatabase_id());
            String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String outFileName = databaseName+"_"+nowtime+".doc";
            //导出文件
            File basepath = new File(outFilePath);
            if(!basepath.exists()){
                basepath.mkdirs();
            }
            File file = new FreeMarkerUtil().createDoc(dataMap, outFilePath+"/"+outFileName,modelPath,request);
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
    @PostMapping(value = "/exportTableSimple")
    public ResultT exportTableSimple(@RequestBody ExportTableVO exportTableVO,
                                   HttpServletRequest request, HttpServletResponse response){
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //构建数据结构
            Map<String,Object> dataMap = tableExportService.getExportMapSimple(exportTableVO.getDatabase_id(),
                    exportTableVO.getData_class_ids(),exportTableVO.getColumns(),
                    exportTableVO.getIndex(),exportTableVO.getShard());
            //制作模板
            //boolean createModel = exportTableService.createModel(request);
            //用逻辑库名称和物理库名称拼接文件名称
//            String logicName = tableExportService.getLogicName(use_id);
            String databaseName = tableExportService.getDatabaseName(exportTableVO.getDatabase_id());
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
    @PostMapping(value = "/exportSQL")
    public ResultT exportSQL(@RequestBody ExportTableVO exportTableVO) {
        try {
            Map<String,Object> map = tableExportService.exportSqlFile(exportTableVO.getDatabase_id(),
                    exportTableVO.getData_class_ids(),Integer.parseInt(exportTableVO.getExportType()),outFilePath);
            return ResultT.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
