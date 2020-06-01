package com.piesat.dm.web.controller.datatable;

import com.piesat.dm.rpc.api.datatable.TableSqlService;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:27:50
 */
@Api(tags = "表sql")
@RequestMapping("/dm/tablesql")
@RestController
public class TableSqlController {
    @Autowired
    private TableSqlService tableSqlService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:tablesql:add")
    @Log(title = "表sql（新增）", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody TableSqlDto tableSqlDto) {
        try {
            TableSqlDto save = this.tableSqlService.saveDto(tableSqlDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:tablesql:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            TableSqlDto tableSqlDto = this.tableSqlService.getDotById(id);
            return ResultT.success(tableSqlDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }
}
