package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.DictionaryEntity;
import com.piesat.sod.system.rpc.dto.DictionaryDto;

/** 字典表
*@description
*@author wlg
*@date 2020年1月14日下午5:04:50
*
*/
@Component("dictionaryMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictionaryMapstruct extends BaseMapper<DictionaryDto,DictionaryEntity>{

}
