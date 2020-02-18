package com.piesat.schedule.rpc.api.mmd;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncCfgDto;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncRecordDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/** 公共元数据同步配置
*@description
*@author wlg
*@date 2020年2月6日下午5:08:57
*
*/
@GrpcHthtService(server = "comMetadata",serialization = SerializeType.PROTOSTUFF)
public interface ComMetadataSyncService {
	
	/**
	 *  分页查询
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午3:23:04
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	PageBean findPageData(PageForm<ComMetadataSyncCfgDto> pageForm) throws Exception;
	/**
	 * 添加同步配置
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午4:14:34
	 * @param cd
	 * @throws Exception
	 */
	void addConfig(ComMetadataSyncCfgDto cd) throws Exception;
	/**
	 *  主键查询
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午4:34:58
	 * @param id
	 * @return
	 * @throws Exception
	 */
	ComMetadataSyncCfgDto findByPk(String id) throws Exception;
	/**
	 * 编辑同步任务
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午5:03:31
	 * @param cd
	 * @throws Exception
	 */
	void editConfig(ComMetadataSyncCfgDto cd) throws Exception;
	/**
	 *  删除同步任务
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午5:12:21
	 * @param ids
	 * @throws Exception
	 */
	void delConfig(String ids) throws Exception;
	
	/**
	 *  获取公共元数据同步记录分页数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午5:46:04
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	PageBean findRecordPage(PageForm<ComMetadataSyncRecordDto> pageForm) throws Exception;
	/**
	 *  删除公共元数据同步记录
	 * @description 
	 * @author wlg
	 * @date 2020年2月17日下午5:46:56
	 * @param ids
	 * @throws Exception
	 */
	void delRecord(String ids) throws Exception;
	/**
	 * 立即同步公共元数据
	 * @description 
	 * @author wlg
	 * @date 2020年2月18日上午11:37:37
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	ResultT syncDataNow(String ids,String apiType,Integer oprType) throws Exception;
	

}
