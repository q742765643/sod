package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassApplyService;
import com.piesat.dm.rpc.api.dataclass.DataClassServiceCodeService;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassServiceCodeDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassServiceCodeList;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月29日 09:53:19
 */
@Api(tags = "资料服务编码管理")
@RequestMapping("/dm/dataclassservicecode")
@RestController
public class DataClassServiceCodeController {
    @Autowired
    private DataClassServiceCodeService dataClassServiceCodeService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataclassservicecode:add")
    @Log(title = "资料服务编码管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassServiceCodeDto dataClassServiceCodeDto) {
        try {
            DataClassServiceCodeDto d = this.dataClassServiceCodeService.saveDto(dataClassServiceCodeDto);
            return ResultT.success(d);
        } catch (Exception e) {
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "新增List")
    @RequiresPermissions("dm:dataclassservicecode:savelist")
    @Log(title = "资料服务编码管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/saveList")
    public ResultT saveList(@RequestBody DataClassServiceCodeList dataClassServiceCodeDto) {
        try {
            DataClassServiceCodeList d = this.dataClassServiceCodeService.saveDtoList(dataClassServiceCodeDto);
            return ResultT.success(d);
        } catch (Exception e) {
            return ResultT.failed(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataclassservicecode:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassServiceCodeDto dataClassServiceCodeDto = this.dataClassServiceCodeService.getDotById(id);
            return ResultT.success(dataClassServiceCodeDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataclassservicecode:del")
    @Log(title = "资料服务编码管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassServiceCodeService.deleteById(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataclassservicecode:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassServiceCodeDto> all = this.dataClassServiceCodeService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询")
    @RequiresPermissions("dm:dataclassservicecode:getServiceCode")
    @GetMapping(value = "/getServiceCode")
    public ResultT getServiceCode(String tableId, String dataclassId) {
        try {
            List<DataClassServiceCodeDto> all = this.dataClassServiceCodeService.getServiceCode(tableId, dataclassId);
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
