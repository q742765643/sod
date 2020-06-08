package com.piesat.schedule.rpc.dto.sync;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/15 14:39
 */
@Data
public class SyncTaskLogDto extends BaseDto {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 日志级别
     */
    private Integer logLevel;

    /**
     * 日志时间
     */
    private Date logTime;

    /**
     * 任务状态
     */
    private Integer state;

    /**
     * 处理总记录数
     */
    private Integer counts;

    /**
     * 插入记录数
     */
    private Integer insertCount;

    /**
     * 更新记录数
     */
    private Integer updateCount;

    /**
     * 丢弃记录数
     */
    private Integer discardCount;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 耗时
     */
    private Long spendTime;

    /**
     * 错误信息
     */
    private String error;
}
