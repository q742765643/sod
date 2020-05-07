package com.piesat.schedule.web.controller.recover;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.api.recover.MetaRecoverLogService;
import com.piesat.schedule.rpc.api.recover.RecoverLogService;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import com.piesat.schedule.rpc.dto.recover.RecoverLogDto;
import com.piesat.schedule.rpc.vo.PathVo;
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
 * @create: 2020-03-05 18:13
 **/
@RestController
@RequestMapping(value="/recoverLog")
@Api(value="结构化数据恢复接口",tags = {"结构化数据恢复接口"})
@Slf4j
public class RecoverLogController {
    @Autowired
    private RecoverLogService recoverLogService;

    @RequiresPermissions("schedule:recoverLog:list")
    @ApiOperation(value = "分页查询结构化数据恢复日志", notes = "分页查询结构化数据恢复日志")
    @GetMapping("/list")
    public ResultT<PageBean<RecoverLogDto>> list(RecoverLogDto recoverLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<RecoverLogDto>> resultT=new ResultT<>();
        PageForm<RecoverLogDto> pageForm=new PageForm<>(pageNum,pageSize,recoverLogDto);
        PageBean pageBean=recoverLogService.selectRecoverLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:recoverLog:query")
    @ApiOperation(value = "根据ID查询结构化数据恢复日志", notes = "根据ID查询结构化数据恢复日志")
    @GetMapping(value = "/{recoverLogId}")
    public ResultT<RecoverLogDto> getInfo(@PathVariable String recoverLogId)
    {
        ResultT<RecoverLogDto> resultT=new ResultT<>();
        RecoverLogDto recoverLogDto= recoverLogService.findRecoverLogById(recoverLogId);
        resultT.setData(recoverLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:recoverLog:remove")
    @Log(title = "结构化数据恢复日志管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除结构化数据恢复日志", notes = "删除结构化数据恢复日志")
    @DeleteMapping("/{recoverLogIds}")
    public  ResultT<String> remove(@PathVariable String[] recoverLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        recoverLogService.deleteRecoverLogByIds(recoverLogIds);
        return resultT;
    }

    @RequiresPermissions("schedule:recoverLog:getDataFileList")
    @ApiOperation(value = "查询结构化数据恢复文件树", notes = "查询结构化数据恢复文件树")
    @GetMapping("/getDataFileList")
    public ResultT<List<TreeVo>> getDataFileList(String databaseId, String dataClassId,String path){
        ResultT<List<TreeVo>> resultT=new ResultT<>();
        List<TreeVo> list=recoverLogService.getDataFileList(databaseId,dataClassId,path);
        resultT.setData(list);
        return resultT;
    }

    @RequiresPermissions("schedule:recoverLog:getFileChidren")
    @ApiOperation(value = "查询结构化数据恢复文件树子节点", notes = "查询结构化数据恢复文件树子节点")
    @GetMapping("/getFileChidren")
    public ResultT<List<TreeVo>> getFileChidren(String childrenPath){
        ResultT<List<TreeVo>> resultT=new ResultT<>();
        List<TreeVo> list=recoverLogService.getFileChidren(childrenPath);
        resultT.setData(list);
        return resultT;
    }

    @RequiresPermissions("schedule:recoverLog:recoverStructedData")
    @ApiOperation(value = "执行结构化数据恢复", notes = "执行结构化数据恢复")
    @PostMapping("/recoverStructedData")
    public ResultT<String> recoverStructedData(@RequestBody  RecoverLogDto recoverLogDto){
        ResultT<String> resultT=new ResultT<>();
        recoverLogService.recoverStructedData(recoverLogDto);
        return resultT;
    }
    @RequiresPermissions("schedule:recoverLog:md5Check")
    @ApiOperation(value = "结构化数据md5校验", notes = "结构化数据md5校验")
    @PostMapping("/md5Check")
    public ResultT<List<Map<String,Object>>> md5Check(@RequestBody PathVo pathVo){
        ResultT<List<Map<String,Object>>> resultT=new ResultT<>();
        List<Map<String,Object>> mapList=recoverLogService.md5Check(pathVo.getPaths());
        resultT.setData(mapList);
        return resultT;
    }

}

