package com.piesat.sod.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.SqlTemplateEntity;
/**
 *  sql 模板dao
 * @author adminis
 *
 */
@Repository
public interface SqlTemplateDao extends BaseDao<SqlTemplateEntity>{

	/**
	 *  根据databaseServer 查找
	 * @param databaseServer
	 * @return
	 * @throws Exception
	 */
	List<SqlTemplateEntity> findByDatabaseServer(String databaseServer) throws Exception;
}
