package com.piesat.dm.web.controller.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.dm.rpc.api.dataapply.NewdataApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.datatable.*;
import com.piesat.dm.rpc.vo.TableInfoVo;
import com.piesat.dm.rpc.service.GrpcService;
import com.piesat.sod.system.rpc.api.ServiceCodeService;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月29日 09:56:25
 */
@Api(tags = "表信息管理")
@RequestMapping("/dm/dataTable")
@RestController
public class DataTableController {
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataLogicService dataLogicService;
    @Autowired
    private NewdataApplyService newdataApplyService;
    @Autowired
    private GrpcService grpcService;
    @GrpcHthtClient
    private ServiceCodeService serviceCodeService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataTable:add")
    @Log(title = "表信息管理(新增表信息)", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataTableInfoDto dataTableDto) {
        try {
            String id = dataTableDto.getId();
            if (StringUtils.isNotBlank(id)) {
                DataTableInfoDto dot = this.dataTableService.getDotById(id);
                dataTableDto.setColumns(dot.getColumns());
                dataTableDto.setTableIndexList(dot.getTableIndexList());
            }
            DataTableInfoDto save = this.dataTableService.saveDto(dataTableDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增申请表")
    @RequiresPermissions("dm:dataTable:addApply")
    @Log(title = "表信息管理（新增申请表信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addApply")
    public ResultT addApply(String classLogicIds, String applyId, String storageType, String databaseId) {
        try {
            NewdataApplyDto newdataApplyDto = this.newdataApplyService.getDotById(applyId);
            String[] ids = classLogicIds.split(",");
            List<DataTableInfoDto> list = new ArrayList<>();
            for (String id : ids) {
                DataClassLogicDto dataLogicDto = this.dataLogicService.getDotById(id);
                DataClassDto dataClassDto = this.dataClassService.findByDataClassId(dataLogicDto.getDataClassId());
                DataTableInfoDto dataTableDto = new DataTableInfoDto();
                dataTableDto.setDatabaseId(databaseId);
                dataTableDto.setStorageType(storageType);
                dataTableDto.setColumns(newdataApplyDto.toTableColumn());
                dataTableDto.setCreateTime(new Date());
                dataTableDto.setTableType("E");
                dataTableDto.setUserId(newdataApplyDto.getUserId());
                dataTableDto.setNameCn(dataClassDto.getClassName());
                dataTableDto.setTableName(newdataApplyDto.getTableName());
                DataTableInfoDto save = this.dataTableService.saveDto(dataTableDto);
                list.add(save);
            }
            return ResultT.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataTable:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataTableInfoDto dataTableDto = this.dataTableService.getDotById(id);
            ResultT r = new ResultT();
            if (dataTableDto != null) {
                this.dataTableService.contrastColumns(dataTableDto, r);
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataTable:del")
    @Log(title = "表信息管理（删除表信息）", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataTableService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有(禁用)")
    @RequiresPermissions("dm:dataTable:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
//            List<DataTableDto> all = this.dataTableService.all();
            return ResultT.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id和存储编码查询")
    @RequiresPermissions("dm:dataTable:dc")
    @GetMapping(value = "/dc")
    public ResultT getByDatabaseIdAndClassId(String databaseId, String dataClassId) {
        try {
            List<DataTableInfoDto> r = this.dataTableService.getByDatabaseIdAndClassId(databaseId, dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据数据用途id查询")
    @RequiresPermissions("dm:dataTable:gcl")
    @GetMapping(value = "/gcl")
    public ResultT getByClassLogicId(String classLogic) {
        try {
            List<DataTableInfoDto> r = this.dataTableService.getByClassLogicId(classLogic);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id查询")
    @RequiresPermissions("dm:dataTable:db")
    @GetMapping(value = "/db")
    public ResultT getByDatabaseId(String databaseId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getByDatabaseId(databaseId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据用户id查询资料及父级")
    //@RequiresPermissions("dm:dataTable:findByUserId")
    @GetMapping(value = "/findByUserId")
    public ResultT findByUserId(String userId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.findByUserId(userId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询样例数据")
    @RequiresPermissions("dm:dataTable:sample")
    @PostMapping(value = "/sample")
    public ResultT getSampleData(SampleData sampleData) {
        try {
            return this.dataTableService.getSampleData(sampleData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "粘贴表结构")
    @RequiresPermissions("dm:dataTable:paste")
    @Log(title = "表信息管理（粘贴表结构信息）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/paste")
    public ResultT paste(String copyId, String pasteId) {
        try {
            return this.dataTableService.paste(copyId, pasteId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询概览信息")
//    @RequiresPermissions("dm:dataTable:getOverview")
    @GetMapping(value = "/getOverview")
    public ResultT getOverview(String databaseId, String dataClassId) {
        try {
            return this.dataTableService.getOverview(databaseId, dataClassId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据类型查询字典信息")
    @RequiresPermissions("dm:dataTable:getDictByType")
    @GetMapping(value = "/getDictByType")
    public ResultT getDictByType(String dictType) {
        List<DictDataDto> dictByType = this.grpcService.getDictByType(dictType);
        return ResultT.success(dictByType);
    }

    @ApiOperation(value = "根据资料编码查询")
    //@RequiresPermissions("dm:dataTable:getByClassId")
    @GetMapping(value = "/getByClassId")
    public ResultT getByClassId(String dataClassId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getByClassId(dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据资料存储编码取得对应表的相关存储结构详细信息")
    //@RequiresPermissions("dm:dataTable:getMultiDataInfoByClassId")
    @GetMapping(value = "/getMultiDataInfoByClassId")
    public ResultT getMultiDataInfoByClassId(String dataClassId) {
        try {
            List<Map<String, Object>> r = this.dataTableService.getMultiDataInfoByClassId(dataClassId);
            return ResultT.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "创建表结构")
    @RequiresPermissions("dm:dataTable:createTable")
    @Log(title = "表信息管理（创建数据库表结构）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/createTable")
    public ResultT createTable(@RequestBody TableSqlDto tableSqlDto) {
        try {
            ResultT resultT = this.dataTableService.existTable(tableSqlDto);
            if (resultT.getCode() == 1 || resultT.getData().equals(true)) {
                return ResultT.failed("数据库已经存在表结构!");
            }
            return this.dataTableService.createTable(tableSqlDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "表结构是否存在")
    @RequiresPermissions("dm:dataTable:existTable")
    @GetMapping(value = "/existTable")
    public ResultT existTable(TableSqlDto tableSqlDto) {
        try {
            return this.dataTableService.existTable(tableSqlDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询相关的键表要素表")
    @RequiresPermissions("dm:dataTable:findTables")
    @GetMapping(value = "/findTables")
    public ResultT findTables(String tableId) {
        try {
            List<Map<String, Object>> tables = this.dataTableService.findTables(tableId);
            return ResultT.success(tables);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据tableId查询相关的表")
    @RequiresPermissions("dm:dataTable:getRelatedTables")
    @GetMapping(value = "/getRelatedTables")
    public ResultT getRelatedTables(String tableId) {
        try {
            List<Map<String, Object>> tables = this.dataTableService.getRelatedTables(tableId);
            return ResultT.success(tables);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据物理库id查询所有要素表")
    @RequiresPermissions("dm:dataTable:findETable")
    @GetMapping(value = "/findETable")
    public ResultT findETable(String databaseId) {
        try {
            List<Map<String, Object>> eTable = this.dataTableService.findETable(databaseId);
            return ResultT.success(eTable);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/getPageTableInfo")
    @ApiOperation(value = "条件分页查询")
    @RequiresPermissions("dm:dataTable:getPageTableInfo")
    public ResultT<List> getPageTableInfo(TableInfoVo tableInfoVo,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, Object> map = tableInfoVo.getMap();
        PageForm<Map<String, Object>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = this.dataTableService.getPageTableInfo(pageForm);
        return ResultT.success(pageBean.getPageData());
    }

    @ApiOperation(value = "查询表数据量")
    @RequiresPermissions("dm:dataTable:countTable")
    @GetMapping(value = "/countTable")
    public ResultT countTable(TableSqlDto tableSqlDto) {
        try {
            long l = this.dataTableService.countTable(tableSqlDto);
            return ResultT.success(l);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询表sql")
    @RequiresPermissions("dm:dataTable:getSql")
    @GetMapping(value = "/getSql")
    public ResultT getSql(String tableId) {
        try {
            return this.grpcService.getSql(tableId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据子表类型查询")
    @RequiresPermissions("dm:dataTable:findBySubType")
    @GetMapping(value = "/findBySubType")
    public ResultT findBySubType(String tableType, String storageType) {
        try {
            return ResultT.success(this.dataTableService.findBySubType(tableType, storageType));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据表名查询相关表信息")
    @RequiresPermissions("dm:dataTable:findTablesByTableName")
    @GetMapping(value = "/findTablesByTableName")
    public ResultT findTablesByTableName(String tableName) {
        try {
            return ResultT.success(this.dataTableService.findTablesByTableName(tableName));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "查询所有要素表")
    @RequiresPermissions("dm:dataTable:findAllETables")
    @GetMapping(value = "/findAllETables")
    public ResultT findAllETables() {
        try {
            return ResultT.success(this.dataTableService.findAllETables());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "逻辑删除（2）/恢复（0）")
    @RequiresPermissions("dm:dataTable:tombstone")
    @Log(title = "表信息管理", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/tombstone")
    public ResultT tombstone(String tableId, String delFlag) {
        try {
            this.dataTableService.tombstone(tableId, delFlag);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询回收站")
    @RequiresPermissions("dm:dataTable:getRecycle")
    @GetMapping(value = "/getRecycle")
    public ResultT getRecycle(TableInfoVo tableInfoVo ,@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            Map<String, Object> map = tableInfoVo.getMap();
            PageForm<Map<String, Object>> pageForm = new PageForm<>(pageNum, pageSize);
            pageForm.setT(map);
            return ResultT.success(this.dataTableService.getRecycle(pageForm));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
