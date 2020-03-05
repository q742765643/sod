package com.piesat.schedule.web.controller.recover;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.dao.recover.MetaRecoverLogDao;
import com.piesat.schedule.rpc.api.recover.MetaRecoverLogService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import com.piesat.sso.client.annotation.Log;
import com.piesat.sso.client.enums.BusinessType;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-03 10:47
 **/
@RestController
@RequestMapping(value="/metaRecoverLog")
@Api(value="元数据恢复接口",tags = {"元数据恢复接口"})
@Slf4j
public class MetaRecoverLogController {
    @Autowired
    private MetaRecoverLogService metaRecoverLogService;

    @RequiresPermissions("schedule:metaRecoverLog:list")
    @ApiOperation(value = "分页查询元数据恢复日志", notes = "分页查询元数据恢复日志")
    @GetMapping("/list")
    public ResultT<PageBean<MetaRecoverLogDto>> list(MetaRecoverLogDto metaRecoverLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<MetaRecoverLogDto>> resultT=new ResultT<>();
        PageForm<MetaRecoverLogDto> pageForm=new PageForm<>(pageNum,pageSize,metaRecoverLogDto);
        PageBean pageBean=metaRecoverLogService.selectMetaRecoverLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:metaRecoverLog:query")
    @ApiOperation(value = "根据ID查询元数据恢复日志", notes = "根据ID查询元数据恢复日志")
    @GetMapping(value = "/{metaRecoverLogId}")
    public ResultT<MetaRecoverLogDto> getInfo(@PathVariable String metaRecoverLogId)
    {
        ResultT<MetaRecoverLogDto> resultT=new ResultT<>();
        MetaRecoverLogDto metaRecoverLogDto= metaRecoverLogService.findMetaRecoverLogById(metaRecoverLogId);
        resultT.setData(metaRecoverLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaRecoverLog:remove")
    @Log(title = "元数据恢复日志管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除元数据恢复日志", notes = "删除元数据恢复日志")
    @DeleteMapping("/{metaRecoverLogIds}")
    public  ResultT<String> remove(@PathVariable String[] metaRecoverLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaRecoverLogService.deleteMetaRecoverLogByIds(metaRecoverLogIds);
        return resultT;
    }

    @RequiresPermissions("schedule:metaRecoverLog:recover")
    @ApiOperation(value = "添加元数据恢复", notes = "添加元数据恢复")
    @PostMapping("/recover")
    public  ResultT<String> recover(@RequestBody MetaRecoverLogDto metaRecoverLogDto)
    {
        ResultT<String> resultT=new ResultT<>();
        metaRecoverLogService.recover(metaRecoverLogDto);
        return resultT;
    }
    @RequiresPermissions("schedule:metaRecoverLog:getFileList")
    @ApiOperation(value = "查询文件树", notes = "查询文件树")
    @PostMapping("/getFileList")
    public  ResultT<List<TreeVo>> getFileList(String databaseId, String storageDirectory)
    {
        ResultT<List<TreeVo>> resultT=new ResultT<>();
        List<TreeVo>  treeVos=metaRecoverLogService.getFileList(databaseId,storageDirectory);
        resultT.setData(treeVos);
        return resultT;
    }
    @RequiresPermissions("schedule:metaRecoverLog:getFileChidren")
    @ApiOperation(value = "查询文件树子节点", notes = "查询文件树子节点")
    @PostMapping("/getFileChidren")
    public ResultT<List<TreeVo>> getFileChidren(String childrenPath){
        ResultT<List<TreeVo>> resultT=new ResultT<>();
        List<TreeVo>  treeVos=metaRecoverLogService.getFileChidren(childrenPath);
        resultT.setData(treeVos);
        return resultT;
    }
    @RequiresPermissions("schedule:metaRecoverLog:parsingPath")
    @ApiOperation(value = "解析恢复文件获取元数据树", notes = "解析恢复文件获取元数据树")
    @PostMapping("/parsingPath")
    public ResultT<Map<String,Object>> parsingPath(String path){
        ResultT<Map<String,Object>> resultT=new ResultT<>();
        Map<String,Object> treeVos=metaRecoverLogService.parsingPath(path);
        resultT.setData(treeVos);
        return resultT;
    }
}

