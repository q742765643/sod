package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ComMetadataSyncCfgEntity;
import com.piesat.sod.system.rpc.dto.ComMetadataSyncCfgDto;

/** 公共元数据同步配置
*@description
*@author wlg
*@date 2020年2月6日下午5:12:26
*
*/
@Component("comMetadataSyncCfgMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComMetadataSyncCfgMapstruct extends BaseMapper<ComMetadataSyncCfgDto,ComMetadataSyncCfgEntity>{

}
