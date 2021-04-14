package com.piesat.dm.web.controller.dataclass;

import com.piesat.dm.rpc.api.dataclass.DataClassApplyService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
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
@Api(tags = "资料申请管理")
@RequestMapping("/dm/dataclassapply")
@RestController
public class DataClassApplyController {
    @Autowired
    private DataClassApplyService dataClassApplyService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:dataclassapply:add")
    @Log(title = "资料申请", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataClassApplyDto dataClassApplyDto) {
        return this.dataClassApplyService.saveDto(dataClassApplyDto);
    }



    @GetMapping("/list")
    @RequiresPermissions("dm:dataclassapply:list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DataClassApplyDto dataClassApplyDto, int pageNum, int pageSize) {
        PageForm<DataClassApplyDto> pageForm = new PageForm<>(pageNum, pageSize, dataClassApplyDto);
        return this.dataClassApplyService.list(pageForm);
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:dataclassapply:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataClassApplyDto dataClassApplyDto = this.dataClassApplyService.getDotById(id);
            return ResultT.success(dataClassApplyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:dataclassapply:del")
    @Log(title = "资料分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataClassApplyService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:dataclassapply:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataClassApplyDto> all = this.dataClassApplyService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "审核")
    @RequiresPermissions("dm:dataclassapply:review")
    @PostMapping(value = "/review")
    public ResultT review(@RequestBody DataClassApplyDto dca) {
        try {
            return this.dataClassApplyService.review(dca);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
