package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.dm.dao.dataapply.NewdataTableColumnDao;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 17:20
 */
@Data
public class NewdataApplyDto extends BaseDto {

    /**
     * id
     */
    private String id;

    /**
     * 四级编码
     */
    private String dDataId;

    /**
     * 服务编码
     * MUSIC接口注册时提供
     */
    private String dataServiceId;

    /**
     * 存储编码
     */
    private String dataClassId;

    /**
     * 表英文名称
     */
    private String tableName;

    /**
     * 逻辑库ID
     * 如有多个逻辑库ID，用逗号分隔
     */
    private String logicId;

    /**
     * 数据库ID
     */
    private String databaseId;
    private List<DataLogicDto> dataLogicDtoList;

    /**
     * 数据采集频次
     */
    private String dataFreq;

    /**
     * 常用使用条件字段
     * 辅助管理员建索引，多个字段用逗号分隔
     */
    private String freuseField;

    /**
     * 服务权限
     * 1公开,2限制
     */
    private Integer isPublish;

    /**
     * 其他说明
     */
    private String memo;

    /**
     * 申请用户ID
     */
    private String userId;

    /**
     * 数据来源
     * 包括：MUSIC接口注册、Web用户注册、静态数据注册
     */
    private String dataOrigin;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     * 1-待审核 ,2-审核通过 ,3-审核不通过，4-删除申请中，5-已删除
     */
    private Integer examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 驳回原因
     */
    private String remark;

    /**
     * 存储构建状态
     *//*
    private Integer storageCreate;

    *//**
     * 同步配置状态
     *//*
    private Integer syncCreate;

    *//**
     * 迁移清除状态
     *//*
    private Integer moveCleanCreate;

    *//**
     * 备份恢复状态
     *//*
    private Integer backupCreate;

    *//**
     * 数据归档状态
     *//*
    private Integer filedCreate;*/

    /**
     * 资料属性
     */
    private String dataProp;

    private Integer version;

    private List<NewdataTableColumnDto> columnList;

    public LinkedHashSet<TableColumnDto> toTableColumn() {
        LinkedHashSet<TableColumnDto> list = new LinkedHashSet<>();
        for (NewdataTableColumnDto nc : this.columnList) {
            list.add(nc.toTableColumn());
        }
        return list;
    }
}
