package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassInfoService;
import com.piesat.dm.rpc.dto.dataclass.DataClassInfoDto;
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
import java.util.Map;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月29日 09:53:19
 */
@Api(tags = "资料信息管理")
@RequestMapping("/dm/dataclassinfo")
@RestController
public class DataClassInfoController {
    @Autowired
    private DataClassInfoService dataClassInfoService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataclassinfo:add")
    @Log(title = "资料信息管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassInfoDto dataClassInfoDto) {
        return this.dataClassInfoService.saveDto(dataClassInfoDto);
    }

    @ApiOperation(value = "新增申请")
    @RequiresPermissions("dm:dataclassinfo:apply")
    @Log(title = "资料信息管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/apply")
    public ResultT apply(@RequestBody DataClassInfoDto dataClassInfoDto) {
        return this.dataClassInfoService.apply(dataClassInfoDto);
    }


    @GetMapping("/list")
    @RequiresPermissions("dm:dataclassinfo:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DataClassInfoDto dataClassInfoDto, int pageNum, int pageSize) {
        PageForm<DataClassInfoDto> pageForm = new PageForm<>(pageNum, pageSize, dataClassInfoDto);
        return this.dataClassInfoService.list(pageForm);
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataclassinfo:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassInfoDto dataClassInfoDto = this.dataClassInfoService.getDotById(id);
            return ResultT.success(dataClassInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataclassinfo:del")
    @Log(title = "资料信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassInfoService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataclassinfo:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassInfoDto> all = this.dataClassInfoService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询最新资料信息")
    @RequiresPermissions("dm:dataclassinfo:newclassinfo")
    @GetMapping(value = "/getNewClassInfo")
    public ResultT getNewClassInfo(Integer n) {
        try {
            List<Map<String, Object>> newClassInfo = this.dataClassInfoService.getNewClassInfo(n);
            return ResultT.success(newClassInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
