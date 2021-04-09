package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DataClassInfoDto.java
 * @Description TODO 资料信息
 * @createTime 2021年03月30日 11:05:00
 */
@Data
public class DataClassInfoDto extends BaseDto {

    private String dataClassId;

    /**
     * 数据名称（主要用于中间数据）
     * data_name
     */
    private String dataName;

    /**
     * 资料状态
     * status
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
     * 存储目录
     * dir
     */
    private String dir;


    /**
     * 用户id
     */
    private String userId;


    /**
     * 数据类型（0：业务数据，1：业务中间数据）
     * data_type
     */
    private Integer dataType;

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
