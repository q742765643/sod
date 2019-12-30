package com.piesat.schedule.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:07
 **/
@Data
public class JobInfoLogDto extends BaseDto{
    private String jobId;
    private String executorAddress;
    private long triggerTime;
    private Integer triggerCode;
    private Date handleTime;
    private Integer handleCode;
    private Integer handleMsg;
    private long elapsedTime;
}

