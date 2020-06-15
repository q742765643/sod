package com.piesat.schedule.client.vo;


import com.piesat.schedule.client.annotation.HtJson;
import lombok.Data;

/**
 *@program: backup
 *@描述
 *@author  zzj
 *@创建时间  2019/5/8 11:00
 **/
@Data
public class EiSendVo {
    @HtJson(name="SYSTEM")
    private String system;
    @HtJson(name="GROUP_ID")
    private String groudId;
    @HtJson(name="ORG_TIME")
    private String orgTime;
    @HtJson(name="MSG_TYPE")
    private String msgType;
    @HtJson(name="COL_TYPE")
    private String colType;
    @HtJson(name="DATA_FROM")
    private String dataFrom;
    @HtJson(name="EVENT_TYPE")
    private String eventType;
    @HtJson(name="EVENT_LEVEL")
    private String eventLevel;
    @HtJson(name="EVENT_TITLE")
    private String eventTitle;
    @HtJson(name="KObject")
    private String kobject;
    @HtJson(name="KEvent")
    private String kevent;
    @HtJson(name="KResult")
    private String kresult;
    @HtJson(name="KIndex")
    private String kindex;
    @HtJson(name="KComment")
    private String kcomment;
    @HtJson(name="EVENT_TIME")
    private String eventTime;
    @HtJson(name="EVENT_SUGGEST")
    private String eventSuggest;
    @HtJson(name="EVENT_CONTROL")
    private String eventControl;
    @HtJson(name="EVENT_TRAG")
    private String eventTrag;
    @HtJson(name="EVENT_EXT1")
    private String eventExt1;
    @HtJson(name="EVENT_EXT2")
    private String eventExt2;


}
