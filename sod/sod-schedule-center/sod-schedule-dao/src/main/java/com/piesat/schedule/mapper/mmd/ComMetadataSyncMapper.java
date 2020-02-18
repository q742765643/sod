package com.piesat.schedule.mapper.mmd;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;
import com.piesat.schedule.entity.mmd.ComMetadataSyncRecordEntity;
/**
 *  公共元数据同步接口
 * @description
 * @author wlg
 * @date 2020-02-17 14:41
 */
@Component
public interface ComMetadataSyncMapper {
    /**
     *  分页查询 公共元数据同步任务
     * @description 
     * @author wlg
     * @date 2020年2月17日下午3:19:36
     * @return
     * @throws Exception
     */
	List<ComMetadataSyncCfgEntity> selectList(ComMetadataSyncCfgEntity entity) throws Exception;
	/**
	 *  分页查询公共元数据同步记录
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午5:31:41
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	List<ComMetadataSyncRecordEntity> selectRecordList(ComMetadataSyncRecordEntity entity) throws Exception;
	/**
	 *  全量同步 ,清除表数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日下午1:33:36
	 * @param tableName
	 * @throws Exception
	 */
	void clearTable(String tableName) throws Exception;
	/**
	 *  插入一行数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日下午3:24:28
	 * @param row
	 * @param tableName
	 * @throws Exception
	 */
	void insert1Row(Map<String,Object> param) throws Exception;
	/**
	 *  更新一行数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日下午3:32:47
	 * @param param
	 * @throws Exception
	 */
	void update1Row(Map<String,Object> param) throws Exception;
	/**
	 *  删除一行数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日下午3:33:19
	 * @param param
	 * @throws Exception
	 */
	void del1Row(Map<String,Object> param) throws Exception;
	
}
