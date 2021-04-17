package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DataClassServiceCodeEntity.java
 * @Description TODO
 * @createTime 2021年04月15日 14:22:00
 */
@Data
public class DataClassServiceCodeDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    /**
     * 存储编码
     * data_class_id
     */
    private String dataClassId;

    /**
     * 字段id
     */
    private String tableColumnId;


    private String dbEleCode;

    /**
     * 中文名称
     * ele_name
     */
    private String eleName;


    /**
     * 服务名称
     * user_ele_code
     */
    private String userEleCode;

    /**
     * 英文单位
     * unit
     */
    private String unit;

    /**
     * 中文单位
     * unit_cn
     */
    private String unitCn;

    /**
     * 是否可显示
     * is_show
     */
    private Integer isShow;


    /**
     * 是否管理字段
     * is_manager
     */
    private Integer isManager;


}
