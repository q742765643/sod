package com.piesat.schedule.rpc.dto.sync;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 17:03
 */
@Data
public class SyncTaskDto extends BaseDto {
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 同步类型
     * 1：定时任务同步
     * 2：数据触发同步
     * 3：数据库日志同步
     */
    private Integer syncType;

    /**
     * 数据来源标识 有CTS、DPL、MUSIC
     */
    private String dataSourceId;


    /**
     *  数据流向标识 有大数据平台、公共云、备份中心、文件目录
     */
    private String dataFlowDirectionId;

    /**
     *  源库ID 缓存库BFDB
     */
    private String sourceDatabaseId;
    private String sourceDatabaseName;

    /**
     * 目标库ID 服务库STDB、分析库HADB
     */
    private String targetDatabaseId;

    /**
     * 源和目标同步映射关系主键
     * 对应syncMapping表的id
     */
    private String sourceTable;

    /**
     * 源数据表时间戳对应字段名
     */
    private String sourceTableDatecolumn;

    /**
     * 每批次数据条数
     */
    private Integer  batchAmount;

    /**
     * 同步任务查询的开始时间
     */
    private Date beginTime;

    /**
     * 同步任务最后一次成功同步的时间
     */
    private Date lastSuccessTime;

    /**
     * 是否有V_BBB字段，键值表默认配置为没有 0表示没有，1表示有
     */
    private String hasModify;

    /**
     * 值表映射关系主键
     * 键值表同步时有此字段
     * 对应syncMapping表的id
     */
    private String slaveTables;

    /**
     * 外键信息
     * 键表与值表之间的外键信息，沿用xml的格式：<值表名称>外键列名1，外键列名2</值表名称> （键表和值表中当作外键关联的字段，字段名称必须相同）
     */
    private String linkKey;

    /**
     * 同步频次
     * 表示同步任务多长时间启动一次
     */
    private Integer syncPeriod;

    /**
     * DI发送标识
     */
    private String diOff;

    /**
     * 重复数据丢弃标识
     */
    private String discardOnDuplicate;

    /**
     * 同步任务执行主机IP
     */
    private String execIp;

    /**
     * 执行主机端口号
     */
    private Integer execPort;

    /**
     * 目标存储类型
     * 0数据库存储1文件存储
     */
    private Integer targetType;

    /**
     * 运行状态
     */
    private String runState;

    /**
     * 源表id
     */
    private String sourceTableId;

    /**
     * 目标表值表id
     */
    private String targetVTableId;

    /**
     * 源表值表id
     */
    private String sourceVTableId;


    /**
     * 目标表（键变/要素表） 与源表（键变/要素表）映射关系
     */
    private List<Map<String,Object>> targetRelation = new ArrayList<Map<String,Object>>();

    /**
     * 目标表（值表） 与源表（值表）映射关系
     */
    private Map<String,Object> slaveRelation = new HashMap<String,Object>();

    /**
     * 源表条件查询字段
     */
    private String sourceQueryCol;

    /**
     * 源表过滤字段
     */
    private String[] sourceTableFilter = new String[]{};

    /**
     * 源表过滤字段使用的操作符
     */
    private String[] columnOper = new String[]{};

    /**
     * 源表过滤字段对应的值
     */
    private String[]  sourceTableFilterText = new String[]{};


    private Integer checkInterval;

    private Integer timeLimit;

    private Integer biggestDifference;

    /**
     * DI过滤规则
     */
    private String queueName;

    /**
     * 主键拼接规则
     */
    private String primaryCom;

}
