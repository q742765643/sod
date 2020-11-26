package com.piesat.schedule.sync.client.handler.synctofile;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import com.piesat.schedule.entity.synctofile.SyncToFileLogEntity;
import com.piesat.schedule.sync.client.Service.synctofile.SyncToFileService;
import com.piesat.schedule.sync.client.constants.ComConstants;
import com.piesat.schedule.sync.client.constants.SqlConstants;
import com.piesat.schedule.sync.client.constants.TypeConstants;
import com.piesat.schedule.sync.client.handler.base.BaseHandler;
import com.piesat.schedule.sync.client.util.DataExport;
import com.piesat.schedule.sync.client.util.EiSendUtil;
import com.piesat.schedule.sync.client.util.SelectUtil;
import com.piesat.schedule.sync.client.util.SftpUtil;
import com.piesat.schedule.sync.client.vo.SftpConfig;
import com.piesat.schedule.sync.client.vo.SyncComVo;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import com.piesat.schedule.sync.client.Service.synctofile.SyncToFileLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 结构化数据转文件同步
 *
 * @author cwh
 * @date 2020年 10月29日 14:12:00
 */
@Slf4j
@Service("syncToFileHandler")
public class SyncToFileHandler implements BaseHandler {
    @Autowired
    private SyncToFileLogService syncToFileLogService;
    @Autowired
    private SyncToFileService syncToFileService;
    @Value("${sync.temp-path}")
    private String syncTempPath;

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        log.info("结构化数据转文件同步调用成功");
        SyncComVo syncComVo = new SyncComVo();
        syncComVo.setStartTime(System.currentTimeMillis());
        SyncToFileEntity se = (SyncToFileEntity) jobInfoEntity;
        this.syncExecute(syncComVo, se, resultT);
    }


    public void syncExecute(SyncComVo syncComVo, SyncToFileEntity se, ResultT<String> resultT) {
        SyncToFileLogEntity syncLog = new SyncToFileLogEntity();
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
            this.querySites(se, syncLog, resultT, syncComVo);
        } catch (Exception e) {
            resultT.setErrorMessage("同步过程失败,资料名称{},错误{}", se.getProfileName(), OwnException.get(e));
            log.error("同步过程失败,资料名称{},错误{}", se.getProfileName(), OwnException.get(e));
        } finally {
            syncComVo.setEndTime(System.currentTimeMillis());
            this.updateSyncLog(syncComVo, syncLog, resultT);
            if (endTime != null) {
                this.syncToFileService.updateLastTime(endTime, se.getId());
            }
        }

    }

    public void querySites(SyncToFileEntity se, SyncToFileLogEntity syncLog, ResultT<String> resultT, SyncComVo syncComVo) {
        String begin = DateUtils.getDateTimeStr(syncLog.getBeginTime());
        String end = DateUtils.getDateTimeStr(syncLog.getEndTime());
        try {
            List<String> whereList = new ArrayList<>();
            whereList.add(String.format(SqlConstants.WHERE_SQL, se.getTimeColumn(), ComConstants.EGT, begin));
            whereList.add(String.format(SqlConstants.WHERE_SQL, se.getTimeColumn(), ComConstants.LT, end));
            List<String> columnList = new ArrayList<>();
            columnList.add(ComConstants.ALL_COLUMN);
            DataExport de = new DataExport();
            de.export(se.getParentId(), se.getTableName(), columnList, whereList, syncTempPath, syncComVo, resultT);
            if (resultT.isSuccess()) {
                this.processJob(se, resultT, syncComVo);
            }
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage(OwnException.get(e));
        }
    }


    public void processJob(SyncToFileEntity se, ResultT<String> resultT, SyncComVo syncComVo) throws IOException {

        String dateTimeNow = DateUtils.getDate();
        String old_path = syncComVo.getConditions();
        String syncDir = se.getSaveDirectory();

        File f = new File(old_path);
        if (!f.exists()) {
            resultT.setSuccessMessage("没有可发送文件！");
            return;
        }
        if (se.getTransferType().equalsIgnoreCase(TypeConstants.LOCAL)) {
            syncDir += File.separator + f.getName();
            try {
                FileUtils.copyFile(f, new File(syncDir));
                resultT.setSuccessMessage("文件同步成功{}->{}", old_path, syncDir);
                f.delete();
            } catch (IOException e) {
                log.error("同步文件出错：由{}到{}，错误：{}", f.getPath(), syncDir, OwnException.get(e));
                resultT.setErrorMessage("同步文件出错：由{}到{}，错误：{}", f.getPath(), syncDir, OwnException.get(e));
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
            resultT.setSuccessMessage("文件ftp推送成功{}->{}", old_path, syncDir);
            f.delete();
        } else {
            log.error("转发类型缺配或不支持：{}", se.getTransferType());
            resultT.setErrorMessage("转发类型缺配或不支持：{}", se.getTransferType());
        }
        if (!resultT.isSuccess()) {
            return;
        }
    }

    public void checkParam(SyncToFileEntity se, ResultT<String> resultT) {
        SelectUtil su = new SelectUtil();
        String timeColumn = se.getTimeColumn();
        try {
            List<String> column = su.getColumn(se.getParentId(), se.getTableName());
            if (!column.contains(timeColumn)) {
                resultT.setErrorMessage("时间字段{}在物理表中不存在", timeColumn);
                return;
            }
            se.getTriggerLastTime();
            se.getTriggerNextTime();
        } catch (SQLException e) {
            e.printStackTrace();
            resultT.setErrorMessage("查询表结构失败:{},错误信息:{}", se.getTableName(), OwnException.get(e));
        }
    }

    public void updateSyncLog(SyncComVo syncComVo, SyncToFileLogEntity SyncToFileLogEntity, ResultT<String> resultT) {
        try {
            SyncToFileLogEntity.setHandleCode("1");
            SyncToFileLogEntity.setElapsedTime((syncComVo.getEndTime() - syncComVo.getStartTime()) / 1000);
            if (!resultT.isSuccess()) {
                SyncToFileLogEntity.setHandleCode("2");
            }
            SyncToFileLogEntity.setSaveDirectory(syncComVo.getConditions());
            SyncToFileLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            syncToFileLogService.saveNotNull(SyncToFileLogEntity);
        } catch (Exception e) {
            resultT.setErrorMessage("修改日志出错{}", OwnException.get(e));
            log.error("修改日志出错{}", OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
    }

}
