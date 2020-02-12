package com.piesat.schedule.client.vo;

import lombok.Data;

import javax.persistence.SecondaryTable;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 18:10
 **/
@Data
public class ReplaceVo {
    private String databaseId;
    private String dataClassId;
    private String ddataId;
    private long backupTime;
    private String msg;
    private Set timeSet;
}

