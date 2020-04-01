package com.piesat.schedule.rpc.job.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.piesat.schedule.rpc.api.index.IndexCountTaskService;

import lombok.extern.slf4j.Slf4j;

/** 容器启动时运行首页定时任务
*@description
*@author wlg
*@date 2020年3月30日下午5:03:31
*
*/
@Component
@Slf4j
public class IndexTimerTask implements ApplicationRunner{
	@Autowired
	private IndexCountTaskService indexCountTaskService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("首页数据统计开始...");
		indexCountTaskService.runClassCountTask();
		
	}

}
