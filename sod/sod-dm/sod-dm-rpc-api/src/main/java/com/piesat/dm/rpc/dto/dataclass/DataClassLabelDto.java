package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
public class DataClassLabelDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private String dataClassId;

    private String labelKey;

}
