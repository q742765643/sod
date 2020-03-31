package com.piesat.schedule.mapper.index;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/** 首页定时统计任务
*@description
*@author wlg
*@date 2020年3月30日下午3:59:14
*
*/
@Component
public interface IndexCountTaskMapper {

	/**
	 *  统计资料数量
	 * @description 
	 * @author wlg
	 * @date 2020年3月30日下午4:06:26
	 * @param date
	 * @return
	 * @throws Exception
	 */
	Integer countData(String date) throws Exception;
	/**
	 *  删除旧的统计数据
	 * @description 
	 * @author wlg
	 * @date 2020年3月30日下午4:07:39
	 * @param param
	 * @throws Exception
	 */
	void delOldClassCount(Map<String,Object> param) throws Exception;
}
