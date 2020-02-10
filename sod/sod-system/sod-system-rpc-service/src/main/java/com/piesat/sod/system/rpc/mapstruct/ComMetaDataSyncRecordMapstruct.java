package com.piesat.sod.system.rpc.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.sod.system.entity.ComMetadataSyncRecordEntity;
import com.piesat.sod.system.rpc.dto.ComMetadataSyncRecordDto;

/** 公共元数据同步记录
*@description
*@author wlg
*@date 2020年2月6日下午5:14:18
*
*/
@Component("comMetadataSyncRecordMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComMetaDataSyncRecordMapstruct extends BaseMapper<ComMetadataSyncRecordDto,ComMetadataSyncRecordEntity>{

}
