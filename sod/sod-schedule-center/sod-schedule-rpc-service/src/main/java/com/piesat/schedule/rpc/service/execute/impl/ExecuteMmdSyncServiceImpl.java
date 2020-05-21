package com.piesat.schedule.rpc.service.execute.impl;

import java.util.List;

import com.piesat.schedule.rpc.thread.ScheduleThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piesat.schedule.dao.mmd.ComMetadataSyncCfgDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.api.mmd.ComMetadataSyncService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.vo.Server;
import com.piesat.util.ResultT;

import lombok.extern.slf4j.Slf4j;

/**公共元数据同步任务调度
*@description
*@author wlg
*@date 2020年2月18日下午5:35:35
*
*/
@Service("executeMmdSyncService")
@Slf4j
public class ExecuteMmdSyncServiceImpl extends ExecuteBaseService implements ExecuteService{
	
	@Autowired
	private ComMetadataSyncCfgDao comMetadataSyncCfgDao;
	
	@Autowired
	private ComMetadataSyncService comMetadataSyncService;

	@Override
	public JobInfoEntity getById(String id) {
		return comMetadataSyncCfgDao.findById(id).orElse(null);
	}

	@Override
	public void checkExecutorBlockStrategyEnum(List<Server> servers, JobInfoEntity jobInfoEntity) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *  公共元数据自动同步调用
	 * @description 
	 * @author wlg
	 * @date 2020-02-18 17:57
	 * @param jobInfoEntity
	 * @param resultT
	 */
	public void executeBusiness(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
		log.info(">>>>>>>>>>执行公共元数据自动同步");
		sendLocalPool.execute(
				()->{
					try {
						String id = jobInfoEntity.getId();

						comMetadataSyncService.syncDataNow(id, null, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		);

		
	}

}
