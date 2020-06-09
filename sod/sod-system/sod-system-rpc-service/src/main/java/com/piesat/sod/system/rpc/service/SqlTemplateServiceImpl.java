package com.piesat.sod.system.rpc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.SqlTemplateDao;
import com.piesat.sod.system.entity.SqlTemplateEntity;
import com.piesat.sod.system.mapper.SqlTemplateMapper;
import com.piesat.sod.system.rpc.api.SqlTemplateService;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import com.piesat.sod.system.rpc.mapstruct.SqlTemplateMapstruct;
/**
 *  sql 模板管理
 * @author adminis
 *
 */
@Service
public class SqlTemplateServiceImpl extends BaseService<SqlTemplateEntity> implements SqlTemplateService{

	@Autowired
	private SqlTemplateDao sqlTemplateDao;

	@Autowired
	private SqlTemplateMapstruct sqlTemplateMapstruct;

	@Autowired
	private SqlTemplateMapper sqlTemplateMapper;

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
	@Transactional
	public void delByIds(String ids) throws Exception{
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			sqlTemplateDao.deleteById(id);
		}
	}

	/**
	 *  主键查询
	 */
	@Override
	public SqlTemplateDto findByPk(String id) throws Exception {
		SqlTemplateEntity ste = sqlTemplateDao.findById(id).orElse(null);
		return sqlTemplateMapstruct.toDto(ste);
	}

	/**
	 *  编辑
	 */
	@Override
	@Transactional
	public void edit(SqlTemplateDto sqlTemplateDto) throws Exception {
		SqlTemplateEntity ste = sqlTemplateMapstruct.toEntity(sqlTemplateDto);
		sqlTemplateDao.saveNotNull(ste);
	}

	/**
	 *  查询全部
	 */
	@Override
	public List<SqlTemplateDto> findAll() throws Exception {
		List<SqlTemplateEntity> entityList = sqlTemplateMapper.findAll();
		List<SqlTemplateDto> dtoList = sqlTemplateMapstruct.toDto(entityList);
		return dtoList;
	}

	/**
	 *  check
	 */
	@Override
	public List<SqlTemplateDto> checkSqlTemplate(String databaseServer) throws Exception {
		List<SqlTemplateEntity> entityList = sqlTemplateDao.findByDatabaseServer(databaseServer);
		List<SqlTemplateDto> dtoList = sqlTemplateMapstruct.toDto(entityList);
		return dtoList;
	}

}
