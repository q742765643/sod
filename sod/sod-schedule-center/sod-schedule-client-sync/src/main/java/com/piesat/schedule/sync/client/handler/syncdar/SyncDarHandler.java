package com.piesat.schedule.sync.client.handler.syncdar;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.constant.HandleConstant;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.syncdar.SyncDarEntity;
import com.piesat.schedule.entity.syncdar.SyncDarLogEntity;
import com.piesat.schedule.sync.client.Service.syncdar.SyncDarLogService;
import com.piesat.schedule.sync.client.Service.syncdar.SyncDarService;
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
 * 非结构化数据交换系统同步
 *
 * @author cwh
 * @date 2020年 10月29日 14:12:00
 */
@Slf4j
@Service("syncdarHandler")
public class SyncDarHandler implements BaseHandler {

    @Autowired
    private SyncDarLogService syncDarLogService;
    @Autowired
    private SyncDarService syncDarService;
    @Value("${sync.temp-path}")
    private String syncTempPath;

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        log.info("非结构化数据交换系统同步调用成功");
        SyncComVo syncComVo = new SyncComVo();
        syncComVo.setStartTime(System.currentTimeMillis());
        SyncDarEntity se = (SyncDarEntity) jobInfoEntity;
        this.syncTempPath += File.separator + HandleConstant.SYNCDAR;
        this.syncExecute(syncComVo, se, resultT);
    }

    public void syncExecute(SyncComVo syncComVo, SyncDarEntity se, ResultT<String> resultT) {
        SyncDarLogEntity syncLog = new SyncDarLogEntity();
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
                this.syncDarService.updateLastTime(endTime, se.getId());
            }
        }

    }

    public void querySites(SyncDarEntity se, SyncDarLogEntity syncLog, ResultT<String> resultT, SyncComVo syncComVo) {
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


    public void processJob(SyncDarEntity se, ResultT<String> resultT, SyncComVo syncComVo) throws IOException {

        String dateTimeNow = DateUtils.getDate();
        String old_path = syncComVo.getConditions();
        String syncDir = se.getSaveDirectory();

        File f = new File(old_path);
        if (!f.exists()) {
            resultT.setSuccessMessage("没有可发送文件！");
            return;
        }

        if (!resultT.isSuccess()) {
            return;
        }
    }

    public void checkParam(SyncDarEntity se, ResultT<String> resultT) {
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

    public void updateSyncLog(SyncComVo syncComVo, SyncDarLogEntity SyncDarLogEntity, ResultT<String> resultT) {
        try {
            SyncDarLogEntity.setHandleCode("1");
            SyncDarLogEntity.setElapsedTime((syncComVo.getEndTime() - syncComVo.getStartTime()) / 1000);
            if (!resultT.isSuccess()) {
                SyncDarLogEntity.setHandleCode("2");
            }
            SyncDarLogEntity.setSaveDirectory(syncComVo.getConditions());
            SyncDarLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            syncDarLogService.saveNotNull(SyncDarLogEntity);
        } catch (Exception e) {
            resultT.setErrorMessage("修改日志出错{}", OwnException.get(e));
            log.error("修改日志出错{}", OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
    }
}
