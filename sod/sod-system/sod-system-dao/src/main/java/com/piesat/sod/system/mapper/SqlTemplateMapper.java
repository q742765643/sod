package com.piesat.sod.system.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.piesat.sod.system.entity.SqlTemplateEntity;

/**
 *  sql模板管理
 * @author adminis
 *
 */
@Component
public interface SqlTemplateMapper {

	/**
	 *  查询全部
	 * @return
	 * @throws Exception
	 */
	List<SqlTemplateEntity> findAll() throws Exception;
}
