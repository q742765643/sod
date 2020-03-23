package com.piesat.sod.system.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.piesat.sod.system.entity.DbFileEntity;

/** 文件表 Mybatis
*@description
*@author wlg
*@date 2020年1月14日下午5:11:47
*
*/
@Component
public interface DbFileMapper {

	/**
	 *  文件表分页查询
	 * @description 
	 * @author wlg
	 * @date 2020年3月19日下午3:48:55
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	List<DbFileEntity> selectList(DbFileEntity entity) throws Exception;
}
