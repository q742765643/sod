package com.piesat.sod.system.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/** 首页
*@description
*@author wlg
*@date 2020年2月19日下午3:26:55
*
*/
@Component
public interface IndexMapper {
	/**
	 *  获取数据库访问账户已审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:42:40
	 * @return
	 * @throws Exception
	 */
	Integer findUpChecked() throws Exception;
	/**
	 *  获取数据库访问账户未审核数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:43:01
	 * @return
	 * @throws Exception
	 */
	Integer findUpUncheck() throws Exception;
	
	/**
	 *  获取云数据库已审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:50:57
	 * @return
	 * @throws Exception
	 */
	Integer findCloudBChecked() throws Exception;
	/**
	 *  获取云数据库未审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:51:11
	 * @return
	 * @throws Exception
	 */
	Integer findCloudBUnchecked() throws Exception;
	/**
	 *  获取数据注册已审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:06:04
	 * @return
	 * @throws Exception
	 */
	Integer findNewDataChecked() throws Exception;
	/**
	 *  获取数据注册未审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:06:21
	 * @return
	 * @throws Exception
	 */
	Integer findNewDataUncheck() throws Exception;
	/**
	 *  获取资料权限已审数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:24:46
	 * @return
	 * @throws Exception
	 */
	Integer findDataAuthorChecked() throws Exception;
	/**
	 *  获取资料权限待审数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:25:08
	 * @return
	 * @throws Exception
	 */
	Integer findDataAuthorUncheck() throws Exception;
	
	/**
	 *  获取专题库已审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:33:56
	 * @return
	 * @throws Exception
	 */
	Integer findSpecialDbChecked() throws Exception;
	/**
	 *  获取专题库未审核数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午4:35:01
	 * @return
	 * @throws Exception
	 */
	Integer findSpecialDbUncheck() throws Exception;
	/**
	 *  获取近12个月的资料统计数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:20:58
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findDataMonthCount() throws Exception;
	/**
	 *  获取文件列表
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:40:05
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findFileList() throws Exception;
	/**
	 * 根据资料分类获取统计数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午10:14:05
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findDataCount() throws Exception;
	/**
	 *  获取逻辑库资料统计数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午10:38:01
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicCountData() throws Exception;
	/**
	 *  获取专题库信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午11:00:58
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findSpecialDbList() throws Exception;
	/**
	 *  获取数据用途信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午11:26:34
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicInfo() throws Exception;
	/**
	 *  获取数据用途使用量
	 * @description 
	 * @author wlg
	 * @date 2020年4月3日上午10:22:11
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicCapacity() throws Exception;
	/**
	 *  获取数据用途资料统计
	 * @description 
	 * @author wlg
	 * @date 2020年4月3日上午10:23:20
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicDataCount() throws Exception;
	/**
	 *  获取数据用途物理库
	 * @description 
	 * @author wlg
	 * @date 2020年4月3日上午10:23:41
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLoigcDB() throws Exception;

}
