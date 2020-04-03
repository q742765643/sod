package com.piesat.dm.web.controller;

import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品
 *
 * @author cwh
 * @date 2020年 03月02日 11:29:06
 */
@Api(tags = "产品配置")
@RequestMapping("/dm/product")
@RestController
public class ProductController {

    @Value("${product.enable}")
    private String enable;

    @ApiOperation(value = "产品配置启用")
    @RequiresPermissions("dm:product:enable")
    @GetMapping(value = "/enable")
    public ResultT enable() {
        try {
            ResultT r = new ResultT();
            r.isSuccess();
            r.setData(enable);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
