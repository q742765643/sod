package com.piesat.schedule.rpc.dto;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务ID")
    private String jobId;
    @ApiModelProperty("执行地址")
    private String executorAddress;
    @ApiModelProperty("触发时间")
    private long triggerTime;
    @ApiModelProperty("触发状态")
    private Integer triggerCode;
    @ApiModelProperty("实际执行时间")
    private Date handleTime;
    @ApiModelProperty("执行状态")
    private String handleCode;
    @ApiModelProperty("执行过程")
    private String handleMsg;
    @ApiModelProperty("执行耗时 秒")
    private long elapsedTime;
}

