package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/11 18:13
 */
@RestController
@RequestMapping("/dm/storageConfiguration")
@Api(value = "存储结构概览", tags = {"存储结构概览"})
public class StorageConfigurationController {

    @Autowired
    private StorageConfigurationService storageConfigurationService;

    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询", notes = "条件分页查询")
    public ResultT<PageBean> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultT<PageBean> resultT=new ResultT<>();
       // PageBean pageBean = this.storageConfigurationService.selectPageList(newdataApplyDto, pageNum, pageSize);
       // resultT.setData(pageBean);

        return resultT;
    }
}
