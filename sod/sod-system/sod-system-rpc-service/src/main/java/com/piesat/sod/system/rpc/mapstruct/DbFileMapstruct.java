package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.DbFileEntity;
import com.piesat.sod.system.rpc.dto.DbFileDto;

/** 文档管理
*@description
*@author wlg
*@date 2020年1月14日下午3:01:16
*
*/
@Component("dbFileMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DbFileMapstruct extends BaseMapper<DbFileDto, DbFileEntity>{

}
