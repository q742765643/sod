package com.piesat.sod.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.DictionaryEntity;

/** 字典管理
*@description
*@author wlg
*@date 2020年1月14日下午4:56:21
*
*/
@Repository
public interface DictionaryDao extends BaseDao<DictionaryEntity>{
	/**
	 *  根据menu获取数据
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午8:48:06
	 * @param flag
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	List<DictionaryEntity> findByFlagAndMenuIn(String flag,List<Integer> menus) throws Exception;
	
	/**
	 *  根据id删除
	 * @description 
	 * @author wlg
	 * @date 2020年1月15日上午11:26:54
	 * @param ids
	 * @throws Exception
	 */
	@Query(value="delete from DictionaryEntity where id in :ids")
	@Modifying
	@Transactional
	void deleteByIds(@Param("ids") List<String> ids) throws Exception;

}
