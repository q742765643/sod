package com.piesat.sod.system.rpc.api;

import java.util.List;
import java.util.Map;

/** 首页数据返回
*@description
*@author wlg
*@date 2020年2月19日下午3:25:16
*
*/
public interface IndexService {
	
	/**
	 *  获取数据库访问账户的审核信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:31:04
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findUpAccCount() throws Exception;
	/***
	 *  获取云数据库的审核信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:32:34
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findCloudDBCount() throws Exception;
	/**
	 *  获取新增资料的审核信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:33:12
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findNewDataCount() throws Exception;
	/**
	 *  获取数据授权的审核信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:33:51
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findDataAuthorCount() throws Exception;
	/**
	 *  获取专题库审核信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午3:34:44
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findSpecialDBCount() throws Exception;
	/**
	 *  获取近12个月的资料统计
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:16:55
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findDataMonthCount() throws Exception;
	/**
	 *  获取文件列表
	 * @description 
	 * @author wlg
	 * @date 2020年2月19日下午5:47:32
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findFileList() throws Exception;
	/**
	 *  获取资料分类统计
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午9:28:43
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findDataCount() throws Exception;
	/**
	 *  获取逻辑库资料统计数量
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午10:37:13
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicCountData() throws Exception;
	/**
	 *  获取专题库信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午11:08:02
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findSpecialDbList() throws Exception;
	/**
	 *  获取逻辑库信息
	 * @description 
	 * @author wlg
	 * @date 2020年2月20日上午11:24:47
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findLogicInfo() throws Exception;

}
