package com.piesat.schedule.rpc.dto.syncdar;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 10月28日 15:03:30
 */
@Data
public class SyncDarLogDto extends JobInfoLogDto {
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
    private long bufferTime;
    /**
     * 频率
     */
    private Integer frequency;
    /**
     * 单位
     */
    private String unit;
    private String transferType;
    private String fileName;
    private Integer isEnd;
}
