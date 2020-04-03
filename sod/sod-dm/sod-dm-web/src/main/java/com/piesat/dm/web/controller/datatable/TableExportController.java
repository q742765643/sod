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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            JSONArray all = this.dataClassService.getLogicClass();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "表结构导出-完整版")
    @RequiresPermissions("dm:tableExport:exportTable")
    @PostMapping(value = "/exportTable")
    public void exportTableInfo(String logic_id, String physical_id, String data_class_ids,
                                HttpServletRequest request, HttpServletResponse response){
        InputStream is = null;
        ServletOutputStream sos = null;
        try {
            //构建数据结构
            Map<String,Object> dataMap = tableExportService.getExportMap(physical_id,data_class_ids);
            //制作模板
			//boolean createModel = exportTableService.createModel(request);
            //导出文件
            File file = new FreeMarkerUtil().createDoc(dataMap, "table_struction.doc","template_simple.ftl",request);
            //下载文件
            is = new FileInputStream(file);
            //用逻辑库名称和物理库名称拼接文件名称
            String logicName = tableExportService.getLogicName(logic_id);
            String databaseName = tableExportService.getDatabaseName(physical_id);
            String nowtime = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String outFileName = logicName+databaseName+"_"+nowtime+".doc";
            //outFileName = URLEncoder.encode(outFileName,"UTF-8");
            outFileName = new String(outFileName.getBytes("UTF-8"),"iso-8859-1");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;filename="+outFileName);
            sos = response.getOutputStream();
            byte[] buffer = new byte[512];
            // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = is.read(buffer)) != -1) {
                sos.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(sos != null){
                try {
                    sos.close();
                    sos = null;
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
    public ResultT exportSQL() {
        try {
            JSONArray all = this.dataClassService.getLogicClass();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
