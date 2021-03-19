package com.piesat.dm.web.controller.dataapply;

import com.piesat.dm.rpc.api.dataapply.DataResourceApplyService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataResourceApplyDto;
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
 * @author yaya
 * @description portal 资源申请接口
 * @date 2020/3/30 13:12
 */
@RestController
@RequestMapping("/dm/resourceApply")
@Api(tags = "portal资源申请接口")
public class DataResourceApplyController {

    @Autowired
    private DataResourceApplyService dataResourceApplyService;



    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DataResourceApplyDto dataResourceApplyDto) {
        try {
            DataResourceApplyDto save = this.dataResourceApplyService.saveDto(dataResourceApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

    @GetMapping("/findAuthorityList")
    @RequiresPermissions("dm:resourceApply:findAuthorityList")
    @ApiOperation(value = "查询资料被申请权限", notes = "查询资料被申请权限")
    public ResultT<List<Map<String, Object>>> findAuthorityList(String tableId) {
        ResultT<List<Map<String, Object>>> resultT = new ResultT<>();
        List<Map<String, Object>> authorityList = this.dataResourceApplyService.findAuthorityList(tableId);
        resultT.setData(authorityList);
        return resultT;
    }

}
