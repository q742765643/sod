package com.piesat.sod.system.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.piesat.sod.system.entity.ManageFieldEntity;

/** 管理字段mapper
*@description
*@author wlg
*@date 2020年1月17日下午4:03:24
*
*/
@Component
public interface ManageFieldMapper {

	/**
	 *  根据
	 * @description
	 * @author wlg
	 * @date 2020年1月17日下午4:05:41
	 * @param manageFieldEntity
	 * @return
	 * @throws Exception
	 */
	List<ManageFieldEntity> findByConditions(ManageFieldEntity manageFieldEntity) throws Exception;

	/**
	 * 获取数据总数
	 * @param mfe
	 * @return
	 */
	Map<String, Object> findTotal(ManageFieldEntity mfe);
}
