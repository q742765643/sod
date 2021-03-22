package com.piesat.dm.rpc.dto.dataclass;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * 数据标签定义
 *
 * @author cwh
 * @date 2020年 07月29日 10:59:09
 */
@Data
public class DataClassLabelDefDto extends BaseDto {
    private static final long serialVersionUID = 1L;

    private String labelName;

    private Integer status;

    private String remark;

}
