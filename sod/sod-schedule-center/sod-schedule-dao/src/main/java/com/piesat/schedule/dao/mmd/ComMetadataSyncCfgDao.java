package com.piesat.schedule.dao.mmd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piesat.common.jpa.BaseDao;
import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;

/** 同步任务配置
*@description
*@author wlg
*@date 2020年2月6日下午4:42:10
*
*/
@Repository
public interface ComMetadataSyncCfgDao extends BaseDao<ComMetadataSyncCfgEntity>{

	/**
	 *  根据同步类型查找同步配置
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日下午4:06:10
	 * @param apiType
	 * @return
	 * @throws Exception
	 */
	List<ComMetadataSyncCfgEntity> findByApiType(String apiType) throws Exception;
}
