package com.piesat.sod.system.rpc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.ComMetadataSyncCfgDao;
import com.piesat.sod.system.dao.ComMetadataSyncRecordDao;
import com.piesat.sod.system.entity.ComMetadataSyncCfgEntity;
import com.piesat.sod.system.rpc.api.ComMetadataSyncService;
import com.piesat.sod.system.rpc.mapstruct.ComMetaDataSyncRecordMapstruct;
import com.piesat.sod.system.rpc.mapstruct.ComMetadataSyncCfgMapstruct;

/**
*@description
*@author wlg
*@date 2020年2月6日下午5:19:19
*
*/
@Service
public class ComMetadataSyncServiceImpl extends BaseService<ComMetadataSyncCfgEntity> implements ComMetadataSyncService{
	
	@Autowired
	private ComMetadataSyncCfgDao comMetadataSyncCfgDao;
	
	@Autowired
	private ComMetadataSyncRecordDao comMetadataSyncRecordDao;
	
	@Autowired
	private ComMetadataSyncCfgMapstruct comMetadataSyncCfgMapstruct;
	
	@Autowired
	private ComMetaDataSyncRecordMapstruct comMetaDataSyncRecordMapstruct;

	@Override
	public BaseDao<ComMetadataSyncCfgEntity> getBaseDao() {
		return comMetadataSyncCfgDao;
	}
	
	

}
