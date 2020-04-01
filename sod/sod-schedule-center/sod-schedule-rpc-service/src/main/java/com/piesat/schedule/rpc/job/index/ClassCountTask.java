package com.piesat.schedule.rpc.job.index;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.piesat.schedule.dao.index.ClassCountDao;
import com.piesat.schedule.entity.index.ClassCountEntity;
import com.piesat.schedule.mapper.index.IndexCountTaskMapper;

import lombok.extern.slf4j.Slf4j;

/** 数据分类统计
*@description
*@author wlg
*@date 2020年3月30日下午4:26:54
*
*/
@Component
@Slf4j
public class ClassCountTask extends TimerTask{
	
	@Autowired
	private IndexCountTaskMapper indexCountTaskMapper;
	
	@Autowired
	private ClassCountDao classCountDao;

	@Override
	public void run() {
		log.info("定时统计资料数量");
		this.doCountTask();
		
	}
	/**
	 *  执行统计
	 * @description 
	 * @author wlg
	 * @date 2020年3月30日下午4:30:42
	 */
	@Transactional
	private void doCountTask() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			String date = sdf.format(c.getTime());
			Integer num = indexCountTaskMapper.countData(date);
			if(null != num ) {
	            String year = date.split("[-]")[0];
	            String month = date.split("[-]")[1];
	            
	            ClassCountEntity cce = new ClassCountEntity();
	            cce.setYear(year);
	            cce.setMonth(month);
	            cce.setNum(num);
	            Map<String,Object> param = new HashMap<>();
	            param.put("year", year);
	            param.put("month", month);
	            
	            indexCountTaskMapper.delOldClassCount(param);
	            
	            classCountDao.saveNotNull(cce);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
