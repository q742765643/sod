package com.piesat.schedule.rpc.mapstruct.mmd;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.mmd.ComMetadataSyncRecordEntity;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncRecordDto;
import org.springframework.stereotype.Service;

/** 公共元数据同步记录
*@description
*@author wlg
*@date 2020年2月6日下午5:14:18
*
*/
@Service
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComMetaDataSyncRecordMapstruct extends BaseMapper<ComMetadataSyncRecordDto,ComMetadataSyncRecordEntity>{

}
