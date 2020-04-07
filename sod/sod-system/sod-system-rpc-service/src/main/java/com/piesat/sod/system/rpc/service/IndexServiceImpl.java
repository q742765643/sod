package com.piesat.sod.system.rpc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piesat.sod.system.mapper.IndexMapper;
import com.piesat.sod.system.rpc.api.IndexService;

/** 获取首页数据
*@description
*@author wlg
*@date 2020年2月19日下午3:35:21
*
*/
@Service
public class IndexServiceImpl implements IndexService{
	
	@Autowired
	private IndexMapper indexMapper;

	/**
	 *  获取up账户审核与未审核信息
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 15:36
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findUpAccCount() throws Exception {
		Map<String,Object> result = new HashMap<>();
		Integer checked = indexMapper.findUpChecked();
		Integer uncheck = indexMapper.findUpUncheck();
		
		result.put("checked", checked);
		result.put("uncheck", uncheck);
		return result;
	}

	/**
	 *  获取云数据库已审核和未审核数量
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 15:49
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findCloudDBCount() throws Exception {
		Map<String,Object> result = new HashMap<>();
		Integer checked = indexMapper.findCloudBChecked();
		Integer uncheck = indexMapper.findCloudBUnchecked();
		result.put("checked", checked);
		result.put("uncheck", uncheck);
		return result;
	}

	/**
	 *  获取数据注册审核与未审核信息
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 16:04
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findNewDataCount() throws Exception {
		Map<String,Object> result = new HashMap<>();
		Integer checked = indexMapper.findNewDataChecked();
		Integer uncheck = indexMapper.findNewDataUncheck();
		
		result.put("checked", checked);
		result.put("uncheck", uncheck);
		return result;
	}

	/**
	 *  获取数据权限 已审核和未审核数量
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 16:16
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findDataAuthorCount() throws Exception {
		Map<String,Object> result = new HashMap<>();
		
		Integer checked = indexMapper.findDataAuthorChecked();
		Integer uncheck = indexMapper.findDataAuthorUncheck();
		
		result.put("checked", checked);
		result.put("uncheck", uncheck);
		return result;
	}

	/**
	 *  获取专题库已审核和待审核数量
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 16:32
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findSpecialDBCount() throws Exception {
		Map<String,Object> result = new HashMap<>();
		Integer checked = indexMapper.findSpecialDbChecked();
		Integer uncheck = indexMapper.findSpecialDbUncheck();
		
		result.put("checked", checked);
		result.put("uncheck", uncheck);
		return result;
	}

	/**
	 *  获取近12个月的资料统计
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 17:18
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> findDataMonthCount() throws Exception {
		List<Map<String,Object>> list = indexMapper.findDataMonthCount();
		
		if (list != null && list.size() > 0) {
            List<String> month = new ArrayList<>();
            List<String> num = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                Map<String, Object> map = list.get(i);
                month.add(map.get("MONTH").toString() + "月");
                num.add(map.get("NUM").toString());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("monthData", month);
            map.put("numData", num);
            return map;
		}
		return null;
	}
	

	/**
	 *  获取文件列表
	 * @description 
	 * @author wlg
	 * @date 2020-02-19 17:48
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findFileList() throws Exception {
		List<Map<String,Object>> fileList = indexMapper.findFileList();
		
		return fileList;
	}

	/**
	 *  根据资料分类统计资料数量
	 * @description 
	 * @author wlg
	 * @date 2020-02-20 10:13
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,Object>> findDataCount() throws Exception {
		List<Map<String,Object>> result = indexMapper.findDataCount();
		
		return result;
	}

	/**
	 *  获取逻辑库资料统计数量
	 * @description 
	 * @author wlg
	 * @date 2020-02-20 10:41
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findLogicCountData() throws Exception {
		List<Map<String,Object>> result = indexMapper.findLogicCountData();
		return result;
	}

	/**
	 *  获取专题库信息
	 * @description 
	 * @author wlg
	 * @date 2020-02-20 11:09
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findSpecialDbList() throws Exception {
		List<Map<String,Object>> result = indexMapper.findSpecialDbList();
		
		return result;
	}

	/**
	 *  获取逻辑库信息
	 * @description 
	 * @author wlg
	 * @date 2020-02-20 11:27
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findLogicInfo() throws Exception {
//		List<Map<String,Object>> result = indexMapper.findLogicInfo();
		List<Map<String,Object>> capacityCount = indexMapper.findLogicCapacity();
		List<Map<String,Object>> dataCount = indexMapper.findLogicDataCount();
		List<Map<String,Object>> dbCount = indexMapper.findLoigcDB();
		
		Map<String,Object> dbListMap = new HashMap<>();
		List<String> dbList = new ArrayList<>();
		for(Map<String,Object> m:dbCount) {
			dbList.add(m.get("LOGIC_FLAG")+"");
		}
		
		for(String dbLogic:dbList) {
			List<Map<String,Object>> dbInfo = new ArrayList<>();
			for(Map<String,Object> m:dbCount) {
				if(dbLogic.equals(m.get("LOGIC_FLAG"))) {
					Map<String,Object> db = new HashMap<>();
					db.put("DATABASE_DEFINE_ID", m.get("DATABASE_DEFINE_ID"));
					db.put("DATABASE_NAME", m.get("DATABASE_NAME"));
					dbInfo.add(db);
				}
			}
			dbListMap.put(dbLogic, dbInfo);
		}
		
		for(Map<String,Object> dataMap : dataCount) {
			
			for(Map<String,Object> capMap:capacityCount) {
				if(dataMap.get("LOGIC_FLAG").equals(capMap.get("DATABASE_LOGIC"))) {
					dataMap.put("TOTAL_CAPACITY", capMap.get("TOTAL_CAPACITY"));
					dataMap.put("USED_CAPACITY", capMap.get("TOTAL_CAPACITY"));
				}
			}
			
			for(Map.Entry<String, Object> entry:dbListMap.entrySet()) {
				if(dataMap.get("LOGIC_FLAG").equals(entry.getKey())) {
					dataMap.put("DB_INFO", entry.getValue());
				}
			}
		}
		return dataCount;
	}

}
