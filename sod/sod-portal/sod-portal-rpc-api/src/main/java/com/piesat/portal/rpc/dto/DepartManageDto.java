package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DepartManageDto extends BaseDto {

    String deptunicode;

    /**
     * 部门编码
     */
    String deptcode;

    /**
     * 部门名称
     */
    String deptname;

    /**
     * 类型 : 机构01 , 部门02
     */
    String depttype;

    /**
     * 上级组织机构编码
     */
    String parentCode;

    /**
     * 上级组织机构名称
     */
    String parentName;

    /**
     * 机构状态 : 01停用 02在用
     */
    String status;

    /**
     * 序号
     */
    private Integer serialNumber;

    /**
     * 部门级别
     */
    String deptLevel;

    /**
     * 部门描述职责
     */
    String deptDscr;

    @ApiModelProperty(hidden = true)
    private List<DepartManageDto> children=new ArrayList<>();
}
