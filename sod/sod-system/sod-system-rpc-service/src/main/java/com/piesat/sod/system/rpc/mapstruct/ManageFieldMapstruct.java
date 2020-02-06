package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ManageFieldEntity;
import com.piesat.sod.system.rpc.dto.ManageFieldDto;

/**管理字段 转换
*@description
*@author wlg
*@date 2020年1月16日下午5:16:45
*
*/
@Component("manageFieldMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManageFieldMapstruct extends BaseMapper<ManageFieldDto,ManageFieldEntity>{

}
