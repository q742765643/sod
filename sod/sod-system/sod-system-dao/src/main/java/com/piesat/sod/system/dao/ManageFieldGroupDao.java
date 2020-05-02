package com.piesat.sod.system.dao;

import com.piesat.sod.system.entity.ManageGroupEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.ManageFieldGroupEntity;

import java.util.List;

/** 管理字段与分组关联表
*@description
*@author wlg
*@date 2020年2月6日上午10:17:58
*
*/
@Repository
public interface ManageFieldGroupDao extends BaseDao<ManageFieldGroupEntity>{

	/**
	 *  根据fieldId删除
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:43:26
	 * @param fieldId
	 * @throws Exception
	 */
	@Query(value="delete from ManageFieldGroupEntity where fieldId = :fieldId")
	@Modifying
	void delByFieldId(@Param("fieldId") String fieldId) throws Exception;

	/**
	 *  根据groupId删除
	 * @description
	 * @author wlg
	 * @date 2020年2月6日上午11:44:42
	 * @param groupId
	 * @throws Exception
	 */
	@Query(value="delete from ManageFieldGroupEntity where groupId = :groupId")
	@Modifying
	void delByGroupId(@Param("groupId") String groupId) throws Exception;

	List<ManageFieldGroupEntity> findByGroupId(String groupId);

    ManageFieldGroupEntity findByFieldId(String id);
}
