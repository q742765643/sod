package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.service.backup.MetaBackService;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 16:07
 **/
@RestController
@Api(value="元数据备份接口",tags = {"元数据备份接口"})
@RequestMapping("/schedule/metabackup")
public class MetaBackController {
    @Autowired
    private MetaBackService metaBackService;

    @RequiresPermissions("schedule:metabackup:findMeta")
    @ApiOperation(value = "根据ID查询备份任务", notes = "根据ID查询备份任务")
    @GetMapping(value = "/findMeta")
    public ResultT findMeta(String parentId,String dataBaseType)
    {
        ResultT resultT=new ResultT<>();
        List<TreeVo> treeVos= metaBackService.findMeta(parentId,dataBaseType);
        resultT.setData(treeVos);
        return resultT;
    }
}

