package com.piesat.schedule.rpc.service.index;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piesat.schedule.rpc.api.index.IndexCountTaskService;
import com.piesat.schedule.rpc.job.index.ClassCountTask;

import lombok.extern.slf4j.Slf4j;

/** 首页数据统计
*@description
*@author wlg
*@date 2020年3月30日下午4:15:17
*
*/
@Service("indexCountTaskService")
@Slf4j
public class IndexTaskCountServiceImpl implements IndexCountTaskService{

	@Autowired
	private ClassCountTask classCountTask;

	/**
	 *  运行资料统计任务
	 * @description
	 * @author wlg
	 * @date 2020年3月30日下午4:58:18
	 */
	@Override
	public void runClassCountTask() {
		log.info("运行首页资料统计任务");
		Timer t = new Timer();
		//统计资料数量15分钟一次
		t.schedule(classCountTask, 5*1000,15*60*1000);
	}

}
