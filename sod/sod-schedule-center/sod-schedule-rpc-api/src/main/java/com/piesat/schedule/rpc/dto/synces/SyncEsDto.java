package com.piesat.schedule.rpc.dto.synces;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import lombok.Data;

import java.util.Date;

/**
 * @author cwh
 * @date 2020年 10月28日 15:03:30
 */
@Data
public class SyncEsDto extends JobInfoDto {
    private String databaseId;
    private String parentId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String saveDirectory;
    private String databaseType;
    private String tableName;
    private String vTableName;
    private Date lastTime;
    private Date startTime;
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
    private String ftpIp;
    private int ftpPort;
    private String ftpUser;
    private String ftpPwd;
}
