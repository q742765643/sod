package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ManageGroupEntity;
import com.piesat.sod.system.rpc.dto.ManageGroupDto;

/** 管理字段分组转换
*@description
*@author wlg
*@date 2020年1月16日下午5:17:12
*
*/
@Component("manageGroupMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManageGroupMapstruct extends BaseMapper<ManageGroupDto,ManageGroupEntity>{

}
