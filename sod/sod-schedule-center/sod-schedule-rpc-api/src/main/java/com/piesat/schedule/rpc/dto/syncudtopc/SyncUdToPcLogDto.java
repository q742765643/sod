package com.piesat.schedule.rpc.dto.syncudtopc;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 10月28日 15:03:30
 */
@Data
public class SyncUdToPcLogDto extends JobInfoLogDto {
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String saveDirectory;
    private String databaseType;
    private String tableName;
    private String vTableName;
    private Date beginTime;
    private Date endTime;
    private int dirLevel;
    private long bufferTime;
    /**
     * 文件地址字段
     */
    private String siteColumn;
    /**
     * 时间字段
     */
    private String timeColumn;
    private String transferType;
    private Integer isEnd;
}
