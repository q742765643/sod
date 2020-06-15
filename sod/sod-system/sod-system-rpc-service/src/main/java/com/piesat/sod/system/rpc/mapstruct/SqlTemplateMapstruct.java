package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.SqlTemplateEntity;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;

/**
 *  sql模板管理
 * @author adminis
 *
 */
@Component("sqlTemplateMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SqlTemplateMapstruct extends BaseMapper<SqlTemplateDto,SqlTemplateEntity>{

}
