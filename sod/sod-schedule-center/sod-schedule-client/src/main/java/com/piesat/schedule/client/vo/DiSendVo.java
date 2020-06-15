package com.piesat.schedule.client.vo;


import com.piesat.schedule.client.annotation.HtJson;

import java.util.Map;

/**
 * @program: backupandclear
 * @描述
 * @author  zzj
 * @创建时间 2019/4/10 16:39
 */
public class DiSendVo {
    /**任务编号*/
    @HtJson(name="TASK_ID")
    private String taskId;
    /**当前任务编号*/
    @HtJson(name="CURRENT_TASK_ID")
    private String currentTaskId ;
    /**任务名称*/
    @HtJson(name="TASK_NAME")
    private String taskName;
    /**业务系统*/
    @HtJson(name="SYSTEM")
    private String system;
    /**产品标识*/
    @HtJson(name="DATA_TYPE")
    private String dataType;
    /**计划开始时间*/
    @HtJson(name="START_TIME_S")
    private String startTimeS;
    /**实际开始时间*/
    @HtJson(name="START_TIME_A")
    private long startTimeA;
    /**实际结束时间*/
    @HtJson(name="END_TIME_A")
    private long endTimeA;
    /**任务状态*/
    @HtJson(name="TASK_STATE")
    private String taskState;
    /**任务执行详情*/
    @HtJson(name="TASK_DETAIL")
    private Map taskDetail;
    /**任务异常时间*/
    @HtJson(name="TASK_ERROR_TIME")
    private String taskErrorTime;
    /**任务异常状态说明*/
    @HtJson(name="TASK_ERROR_DETAIL")
    private String taskErrorDetail;
    /**任务异常原因说明*/
    @HtJson(name="TASK_ERROR_REASON")
    private String taskErrorReason;
    /**记录时间*/
    @HtJson(name="RECORD_TIME")
    private String recordTime;
    @HtJson(name="SEND_PHYS")
    private String sendPhys;
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getStartTimeS() {
        return startTimeS;
    }

    public void setStartTimeS(String startTimeS) {
        this.startTimeS = startTimeS;
    }

    public long getStartTimeA() {
        return startTimeA;
    }

    public void setStartTimeA(long startTimeA) {
        this.startTimeA = startTimeA;
    }

    public long getEndTimeA() {
        return endTimeA;
    }

    public void setEndTimeA(long endTimeA) {
        this.endTimeA = endTimeA;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public Map getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(Map taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getTaskErrorTime() {
        return taskErrorTime;
    }

    public void setTaskErrorTime(String taskErrorTime) {
        this.taskErrorTime = taskErrorTime;
    }

    public String getTaskErrorDetail() {
        return taskErrorDetail;
    }

    public void setTaskErrorDetail(String taskErrorDetail) {
        this.taskErrorDetail = taskErrorDetail;
    }

    public String getTaskErrorReason() {
        return taskErrorReason;
    }

    public void setTaskErrorReason(String taskErrorReason) {
        this.taskErrorReason = taskErrorReason;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getSendPhys() {
        return sendPhys;
    }

    public void setSendPhys(String sendPhys) {
        this.sendPhys = sendPhys;
    }
}
