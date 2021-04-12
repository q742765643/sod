package com.piesat.dm.web.controller.datatable;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.StorageConfigParam;
import com.piesat.dm.rpc.api.datatable.DataTableApplyService;
import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description: 表申请
 * @date 2021年 03月09日 14:46:49
 */
@Api(tags = "表申请")
@RequestMapping("/dm/tableapply")
@RestController
public class DataTableApplyController {
    @Autowired
    private DataTableApplyService dataTableApplyService;

    @ApiOperation(value = "新增")
    @RequiresPermissions("dm:tableapply:add")
    @Log(title = "表申请", businessType = BusinessType.INSERT)
    @PostMapping(value = "/save")
    public ResultT save(@RequestBody DataTableApplyDto dataTableApplyDto) {
        try {
            DataTableApplyDto save = this.dataTableApplyService.saveDto(dataTableApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id查询")
    @RequiresPermissions("dm:tableapply:get")
    @GetMapping(value = "/get")
    public ResultT get(String id) {
        try {
            DataTableApplyDto dataTableApplyDto = this.dataTableApplyService.getDotById(id);
            return ResultT.success(dataTableApplyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除")
    @RequiresPermissions("dm:tableapply:del")
    @Log(title = "表申请", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/del")
    public ResultT del(String id) {
        try {
            this.dataTableApplyService.delete(id);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有")
    @RequiresPermissions("dm:tableapply:all")
    @GetMapping(value = "/all")
    public ResultT all() {
        try {
            List<DataTableApplyDto> all = this.dataTableApplyService.all();
            return ResultT.success(all);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(DataTableApplyDto dta,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dta", dta);
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String, Object>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = dataTableApplyService.selectPageList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @ApiOperation(value = "审核")
    @RequiresPermissions("dm:tableapply:review")
    @Log(title = "表申请审核", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/review")
    public ResultT review(@RequestBody DataTableApplyDto dataTableApplyDto) {
        try {
            return this.dataTableApplyService.review(dataTableApplyDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
