package com.piesat.schedule.rpc.mapstruct.mmd;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncCfgDto;
import org.springframework.stereotype.Service;

/** 公共元数据同步配置
*@description
*@author wlg
*@date 2020年2月6日下午5:12:26
*
*/
@Service
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComMetadataSyncCfgMapstruct extends BaseMapper<ComMetadataSyncCfgDto,ComMetadataSyncCfgEntity>{

}
