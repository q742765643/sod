package com.piesat.sod.system.rpc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.SqlTemplateDao;
import com.piesat.sod.system.entity.SqlTemplateEntity;
import com.piesat.sod.system.rpc.api.SqlTemplateService;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import com.piesat.sod.system.rpc.mapstruct.SqlTemplateMapstruct;

@Service
public class SqlTemplateServiceImpl extends BaseService<SqlTemplateEntity> implements SqlTemplateService{
	
	@Autowired
	private SqlTemplateDao sqlTemplateDao;
	
	@Autowired
	private SqlTemplateMapstruct sqlTemplateMapstruct;

	@Override
	public BaseDao<SqlTemplateEntity> getBaseDao() {
		return sqlTemplateDao;
	}

	/**
	 *  新增sql模板
	 */
	@Override
	public void add(SqlTemplateDto sqlTemplateDto) throws Exception {
		SqlTemplateEntity ste = sqlTemplateMapstruct.toEntity(sqlTemplateDto);
		sqlTemplateDao.save(ste);
	}
	/**
	 *  删除sql模板
	 */
	@Override
	public void del(String id) throws Exception{
		sqlTemplateDao.deleteById(id);
	}

}
