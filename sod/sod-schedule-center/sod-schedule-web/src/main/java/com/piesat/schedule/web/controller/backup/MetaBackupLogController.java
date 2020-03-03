package com.piesat.schedule.web.controller.backup;

import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.MetaBackupLogService;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 11:01
 **/
@RestController
@Api(value="元数据数据备份日志接口",tags = {"元数据数据备份日志接口"})
@RequestMapping("/schedule/metaBackupLog")
public class MetaBackupLogController {

    @Autowired
    private MetaBackupLogService metaBackupLogService;

    @RequiresPermissions("schedule:metaBackupLog:list")
    @ApiOperation(value = "分页查询元数据备份任务日志", notes = "分页查询元数据备份任务日志")
    @GetMapping("/list")
    public ResultT<PageBean<MetaBackupLogDto>> list(MetaBackupLogDto metaBackupLogDto, int pageNum, int pageSize)
    {
        ResultT<PageBean<MetaBackupLogDto>> resultT=new ResultT<>();
        PageForm<MetaBackupLogDto> pageForm=new PageForm<>(pageNum,pageSize,metaBackupLogDto);
        PageBean pageBean=metaBackupLogService.selectMetaBackupLogList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackupLog:query")
    @ApiOperation(value = "根据ID查询元数据备份任务日志", notes = "根据ID查询元数据备份任务日志")
    @GetMapping(value = "/{metaBackupLogId}")
    public ResultT<MetaBackupLogDto> getInfo(@PathVariable String metaBackupLogId)
    {
        ResultT<MetaBackupLogDto> resultT=new ResultT<>();
        MetaBackupLogDto metaBackupLogDto= metaBackupLogService.findMetaBackupLogById(metaBackupLogId);
        resultT.setData(metaBackupLogDto);
        return resultT;
    }

    @RequiresPermissions("schedule:metaBackupLog:remove")
    @Log(title = "元数据备份任务日志管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除元数据备份任务日志", notes = "删除元数据备份任务日志")
    @DeleteMapping("/{metaBackupLogIds}")
    public  ResultT<String> remove(@PathVariable String[] metaBackupLogIds)
    {
        ResultT<String> resultT=new ResultT<>();
        metaBackupLogService.deleteMetaBackupLogByIds(metaBackupLogIds);
        return resultT;
    }
    @RequiresPermissions("schedule:metaBackupLog:downFile")
    @ApiOperation(value = "元数据备份文件下载", notes = "元数据备份文件下载")
    @GetMapping("/downFile")
    public String downFile(HttpServletResponse response, String path) {
        OutputStream os = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            byte[] bytes=metaBackupLogService.downFile(path);
            File file=new File(path);
            InputStream inputStream =new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(inputStream);
            os = response.getOutputStream();
            bos = new BufferedOutputStream(os);
            response.setContentType("application/octet-stream;charset=utf-8" );
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
            byte[] buffer = new byte[1024];
            int len = bis.read(buffer);
            while (len != -1) {
                bos.write(buffer, 0, len);
                len = bis.read(buffer);
            }

            bos.flush();
        } catch (IOException e) {
            return "备份失败,没有备份文件,下载出错";
        } finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

