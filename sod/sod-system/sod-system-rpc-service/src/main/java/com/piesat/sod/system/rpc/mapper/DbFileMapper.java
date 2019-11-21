package com.piesat.sod.system.rpc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.DbFileEntity;
import com.piesat.sod.system.rpc.dto.DbFileDto;

/** 
*@description
*@author wlg
*@date 2019年11月20日下午1:53:18
*
*/
@Component("dbFileMapper")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DbFileMapper extends BaseMapper<DbFileDto,DbFileEntity>{

}
