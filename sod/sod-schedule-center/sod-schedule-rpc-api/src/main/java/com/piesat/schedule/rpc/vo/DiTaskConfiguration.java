package com.piesat.schedule.rpc.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @program: DataStoragePlatform
 * @描述
 * @创建人 zzj
 * @创建时间 2019/4/11 9:24
 */
public class DiTaskConfiguration {
    private String taskId;
    private String system;
    private String taskName;
    private String taskDuty;
    private String taskCron;
    private int overtime;
    private int taskMaxTime;
    private int offset;
    private int alarmtime;
    private int isAlarm;
    private String dataType;
    @JSONField(name= "SEND_PHYS")
    private String sendPhys;
    private int handleProcess;
    private int src;
    @JSONField(name= "PROD_SYS")
    private String prodSys;



    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDuty() {
        return taskDuty;
    }

    public void setTaskDuty(String taskDuty) {
        this.taskDuty = taskDuty;
    }

    public String getTaskCron() {
        return taskCron;
    }

    public void setTaskCron(String taskCron) {
        this.taskCron = taskCron;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getTaskMaxTime() {
        return taskMaxTime;
    }

    public void setTaskMaxTime(int taskMaxTime) {
        this.taskMaxTime = taskMaxTime;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(int alarmtime) {
        this.alarmtime = alarmtime;
    }

    public int getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(int isAlarm) {
        this.isAlarm = isAlarm;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    public int getHandleProcess() {
        return handleProcess;
    }

    public void setHandleProcess(int handleProcess) {
        this.handleProcess = handleProcess;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public String getSendPhys() {
        return sendPhys;
    }

    public void setSendPhys(String sendPhys) {
        this.sendPhys = sendPhys;
    }

    public String getProdSys() {
        return prodSys;
    }

    public void setProdSys(String prodSys) {
        this.prodSys = prodSys;
    }
}
