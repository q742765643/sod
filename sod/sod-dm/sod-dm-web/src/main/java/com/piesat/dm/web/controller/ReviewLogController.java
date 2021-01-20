package com.piesat.dm.web.controller;

import com.piesat.dm.entity.ReviewLogEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.service.ImportData;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审核日志
 *
 * @author cwh
 * @date 2020年 03月02日 11:29:06
 */
@Api(tags = "审核日志")
@RequestMapping("/dm/reviewlog")
@RestController
public class ReviewLogController {

    @Autowired
    private ReviewLogService reviewLogService;

    @ApiOperation(value = "根据绑定id查询审核日志")
    @RequiresPermissions("dm:product:enable")
    @GetMapping(value = "/enable")
    public ResultT findByBindId(String bindId) {
        try {
            ResultT r = new ResultT();
            r.setData(this.reviewLogService.findByBindId(bindId));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }


}
