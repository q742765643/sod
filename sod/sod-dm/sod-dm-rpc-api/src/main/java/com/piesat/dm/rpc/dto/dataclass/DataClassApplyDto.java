package com.piesat.dm.rpc.dto.dataclass;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.piesat.dm.rpc.dto.datatable.*;
import com.piesat.util.BaseDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @author cwh
 * @program: sod
 * @description: 资料申请
 * @date 2021年 01月18日 18:30:51
 */
@Data
public class DataClassApplyDto extends BaseDto {

    private String dataClassId;

    /**
     * 数据名称（主要用于中间数据）
     * data_name
     */
    private String dataName;

    /**
     * 审核状态
     * 1-待审核 ,2-审核通过 ,3-审核不通过，4-删除申请中，5-已删除
     */
    private Integer status;

    /**
     * 数据等级
     * data_level
     */
    private Integer dataLevel;

    /**
     * 数据描述
     * data_desc
     */
    private String dataDesc;


    /**
     * 是否归档（0：否，1：是）
     * is_archive
     */
    private Integer isArchive;

    /**
     * 发布状态
     * published
     */
    private Integer published;

    /**
     * 数据类型（0：业务数据，1：业务中间数据）
     * data_type
     */
    private Integer dataType;

    /**
     * 存储目录
     * dir
     */
    private String dir;


    /**
     * 申请人
     */
    private String userId;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 审核信息
     */
    private String reviewNotes;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 申请备注
     */
    private String remark;


    /**
     * 资料表关系
     */
    private List<DataClassLogicDto> dataClassLogicList;

    /**
     * 资料标签关系
     */
    private List<DataClassLabelDto> dataClassLabelList;

    /**
     * 申请表信息
     */
    private List<DataTableApplyDto> dataTableApplyDtoList;

    /**
     * 辅助参数
     */
    private String tableName;


}
