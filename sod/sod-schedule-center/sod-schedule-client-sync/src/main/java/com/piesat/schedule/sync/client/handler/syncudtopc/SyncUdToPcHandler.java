package com.piesat.schedule.sync.client.handler.syncudtopc;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcEntity;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcLogEntity;
import com.piesat.schedule.sync.client.Service.syncudtopc.SyncUdToPcLogService;
import com.piesat.schedule.sync.client.Service.syncudtopc.SyncUdToPcService;
import com.piesat.schedule.sync.client.constants.ComConstants;
import com.piesat.schedule.sync.client.constants.SqlConstants;
import com.piesat.schedule.sync.client.constants.TypeConstants;
import com.piesat.schedule.sync.client.handler.base.BaseHandler;
import com.piesat.schedule.sync.client.util.EiSendUtil;
import com.piesat.schedule.sync.client.util.SelectUtil;
import com.piesat.schedule.sync.client.util.SftpUtil;
import com.piesat.schedule.sync.client.vo.SftpConfig;
import com.piesat.schedule.sync.client.vo.SyncComVo;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 非结构化数据公有云同步
 *
 * @author cwh
 * @date 2020年 10月29日 14:12:00
 */
@Slf4j
@Service("syncUdToPcHandler")
public class SyncUdToPcHandler implements BaseHandler {
    @Autowired
    private SyncUdToPcLogService syncUdToPcLogService;
    @Autowired
    private SyncUdToPcService syncUdToPcService;

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        log.info("非结构化数据公有云同步调用成功");
        SyncComVo syncComVo = new SyncComVo();
        syncComVo.setStartTime(System.currentTimeMillis());
        SyncUdToPcEntity se = (SyncUdToPcEntity) jobInfoEntity;
        this.syncExecute(syncComVo, se, resultT);
    }

    public void syncExecute(SyncComVo syncComVo, SyncUdToPcEntity se, ResultT<String> resultT) {
        SyncUdToPcLogEntity syncLog = new SyncUdToPcLogEntity();
        syncLog.setBeginTime(se.getLastTime());
        Date endTime = null;
        if (se.getEndTime() != null) {
            //补做
            syncLog.setEndTime(se.getEndTime());
            resultT.setSuccessMessage("任务补做：");
        } else {
            long l = System.currentTimeMillis();
            endTime = new Date(l - se.getBufferTime() * 60 * 1000);
            se.setEndTime(endTime);
        }

        BeanUtils.copyProperties(se, syncLog);
        try {
            this.checkParam(se, resultT);
            if (!resultT.isSuccess()) {
                return;
            }
            this.querySites(se, syncLog, resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("同步过程失败,资料名称{},错误{}", se.getProfileName(), OwnException.get(e));
            log.error("同步过程失败,资料名称{},错误{}", se.getProfileName(), OwnException.get(e));
        } finally {
            syncComVo.setEndTime(System.currentTimeMillis());
            this.updateSyncLog(syncComVo, syncLog, resultT);
            if (endTime != null) {
                this.syncUdToPcService.updateLastTime(endTime, se.getId());
            }
        }

    }

    public void querySites(SyncUdToPcEntity se, SyncUdToPcLogEntity syncLog, ResultT<String> resultT) {
        String begin = DateUtils.getDateTimeStr(syncLog.getBeginTime());
        String end = DateUtils.getDateTimeStr(syncLog.getEndTime());
        try {
            List<String> whereList = new ArrayList<>();
            whereList.add(String.format(SqlConstants.WHERE_SQL, se.getTimeColumn(), ComConstants.EGT, begin));
            whereList.add(String.format(SqlConstants.WHERE_SQL, se.getTimeColumn(), ComConstants.LT, end));
            SelectUtil sel = new SelectUtil();
            List<String> columnList = new ArrayList<>();
            columnList.add(se.getSiteColumn());
            List<Map<String, Object>> res = sel.query(se.getParentId(), se.getTableName(), columnList, whereList);
            List<String> dirs = res.stream().map(e -> String.valueOf(e.get(se.getSiteColumn()))).collect(Collectors.toList());
            this.processJob(dirs, se, resultT);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage(OwnException.get(e));
        }
    }


    public void processJob(List<String> dirs, SyncUdToPcEntity se, ResultT<String> resultT) {

        String dateTimeNow = DateUtils.getDate();
        if (dirs.size() == 0) {
            resultT.setSuccessMessage("未查询到可发送数据！");
            return;
        }
        dirs.stream().forEach(d -> {
            File f = new File(d);
            String syncDir = se.getSaveDirectory();

            if (se.getTransferType().equalsIgnoreCase(TypeConstants.LOCAL)) {
                syncDir += File.separator + f.getName();
                try {
                    FileUtils.copyFile(f, new File(syncDir));
                } catch (IOException e) {
                    log.error("复制文件出错：由{}到{}，错误：{}", f.getPath(), syncDir, OwnException.get(e));
                    resultT.setErrorMessage("复制文件出错：由{}到{}，错误：{}", f.getPath(), syncDir, OwnException.get(e));
                }
            } else if (se.getTransferType().equalsIgnoreCase(TypeConstants.FTP)) {
                SftpConfig fc = new SftpConfig(se.getFtpIp(), se.getFtpPort(), se.getFtpUser(), se.getFtpPwd());
                fc.setRoot(syncDir);
                SftpUtil sftpUtil = new SftpUtil();
                sftpUtil.setStfpConfig(fc);
                try {
                    sftpUtil.createSftp();
                } catch (Exception e) {
                    log.error("SFTP连接到{}失败：发送文件到{}，错误：{}", fc.getHostname(), syncDir, OwnException.get(e));
                    resultT.setErrorMessage("SFTP连接到{}失败：发送文件到{}，错误：{}", f.getPath(), syncDir, OwnException.get(e));
                }
                String path = File.separator + dateTimeNow + File.separator + f.getName();
                sftpUtil.uploadFile(path, f, resultT);
                sftpUtil.disconnect();
            } else {
                log.error("转发类型缺配或不支持：{}", se.getTransferType());
                resultT.setErrorMessage("转发类型缺配或不支持：{}", se.getTransferType());
            }
            if (!resultT.isSuccess()) {
                return;
            }
        });
    }


    public static void main(String[] args) {
        SyncUdToPcEntity se = new SyncUdToPcEntity();
        se.setDirLevel(2);
        se.setFtpIp("10.40.17.38");
        se.setFtpPort(22);
        se.setFtpUser("sod");
        se.setFtpPwd("sod@2019");
        se.setSaveDirectory("/space/cmadaas/sod/");
        List<String> l = new ArrayList();
        l.add("D:\\项目\\存储管理\\SVN\\省级部署实施与测试\\方案模板\\0.4版\\附件4.数据库部署_实时应用库集群部署.docx");
        SyncUdToPcHandler sh = new SyncUdToPcHandler();
        ResultT<String> r = new ResultT<>();
        se.setTransferType("ftp");
        sh.processJob(l, se, r);
    }

    public void checkParam(SyncUdToPcEntity se, ResultT<String> resultT) {
        SelectUtil su = new SelectUtil();
        String timeColumn = se.getTimeColumn();
        String siteColumn = se.getSiteColumn();
        try {
            List<String> column = su.getColumn(se.getParentId(), se.getTableName());
            if (!column.contains(timeColumn)) {
                resultT.setErrorMessage("时间字段{}在物理表中不存在", timeColumn);
                return;
            } else if (!column.contains(siteColumn)) {
                resultT.setErrorMessage("路径字段{}在物理表中不存在", siteColumn);
                return;
            }

            se.getTriggerLastTime();
            se.getTriggerNextTime();
        } catch (SQLException e) {
            e.printStackTrace();
            resultT.setErrorMessage("查询表结构失败:{},错误信息:{}", se.getTableName(), OwnException.get(e));
        }
    }

    public void updateSyncLog(SyncComVo syncComVo, SyncUdToPcLogEntity syncUdToPcLogEntity, ResultT<String> resultT) {
        try {
            syncUdToPcLogEntity.setHandleCode("1");
            syncUdToPcLogEntity.setElapsedTime((syncComVo.getEndTime() - syncComVo.getStartTime()) / 1000);
            if (!resultT.isSuccess()) {
                syncUdToPcLogEntity.setHandleCode("2");
            }
            syncUdToPcLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            syncUdToPcLogService.saveNotNull(syncUdToPcLogEntity);
        } catch (Exception e) {
            resultT.setErrorMessage("修改日志出错{}", OwnException.get(e));
            log.error("修改日志出错{}", OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
    }

}
