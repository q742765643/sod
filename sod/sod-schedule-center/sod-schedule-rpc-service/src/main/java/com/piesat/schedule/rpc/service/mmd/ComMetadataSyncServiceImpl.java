package com.piesat.schedule.rpc.service.mmd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.mmd.ComMetadataSyncCfgDao;
import com.piesat.schedule.dao.mmd.ComMetadataSyncRecordDao;
import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;
import com.piesat.schedule.entity.mmd.ComMetadataSyncRecordEntity;
import com.piesat.schedule.mapper.mmd.ComMetadataSyncMapper;
import com.piesat.schedule.rpc.api.mmd.ComMetadataSyncService;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncCfgDto;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncRecordDto;
import com.piesat.schedule.rpc.mapstruct.mmd.ComMetaDataSyncRecordMapstruct;
import com.piesat.schedule.rpc.mapstruct.mmd.ComMetadataSyncCfgMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/** 公共元数据同步配置
*@description
*@author wlg
*@date 2020年2月6日下午5:19:19
*
*/
@Service
public class ComMetadataSyncServiceImpl extends BaseService<ComMetadataSyncCfgEntity> implements ComMetadataSyncService{
	
	@Autowired
	private ComMetadataSyncCfgDao comMetadataSyncCfgDao;
	
	@Autowired
	private ComMetadataSyncRecordDao comMetadataSyncRecordDao;
	
	@Autowired
	private ComMetadataSyncCfgMapstruct comMetadataSyncCfgMapstruct;
	
	@Autowired
	private ComMetaDataSyncRecordMapstruct comMetaDataSyncRecordMapstruct;
	
	@Autowired
	private ComMetadataSyncMapper comMetadataSyncMapper;

	@Override
	public BaseDao<ComMetadataSyncCfgEntity> getBaseDao() {
		return comMetadataSyncCfgDao;
	}

	/**
	 *  分页查询
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 15:48
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean findPageData(PageForm<ComMetadataSyncCfgDto> pageForm) throws Exception {
		ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(pageForm.getT());
		if(!StringUtil.isEmpty(ce.getTableName())) ce.setTableName("%"+ce.getTableName()+"%");
		if(!StringUtil.isEmpty(ce.getTaskName())) ce.setTaskName("%"+ce.getTaskName()+"%");
		
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		//获取结果集
		List<ComMetadataSyncCfgEntity> data = comMetadataSyncMapper.selectList(ce);
		PageInfo<ComMetadataSyncCfgEntity> pageInfo = new PageInfo<>(data);
		
		List<ComMetadataSyncCfgDto> dtoData = comMetadataSyncCfgMapstruct.toDto(pageInfo.getList());
		PageBean pageBean = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		return pageBean;
	}

	/**
	 *  添加同步配置
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 16:16
	 * @param cd
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addConfig(ComMetadataSyncCfgDto cd) throws Exception {
		ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(cd);
		comMetadataSyncCfgDao.save(ce);
		
	}

	/**
	 *  主键查询
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 16:35
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public ComMetadataSyncCfgDto findByPk(String id) throws Exception {
		ComMetadataSyncCfgEntity ce = comMetadataSyncCfgDao.findById(id).orElse(null);
		return comMetadataSyncCfgMapstruct.toDto(ce);
	}

	/**
	 *  编辑同步任务
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 17:04
	 * @param cd
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void editConfig(ComMetadataSyncCfgDto cd) throws Exception {
		ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(cd);
		comMetadataSyncCfgDao.save(ce);
	}

	/**
	 *  删除公共元数据同步任务
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 17:13
	 * @param ids
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void delConfig(String ids) throws Exception {
		String[] idArr = ids.split(",");
		for(String id:idArr) {
			comMetadataSyncCfgDao.deleteById(id);
		}
		
	}

	/**
	 *  获取公共元数据同步记录分页数据
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 17:51
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean findRecordPage(PageForm<ComMetadataSyncRecordDto> pageForm) throws Exception {
		ComMetadataSyncRecordEntity ce = comMetaDataSyncRecordMapstruct.toEntity(pageForm.getT());
		
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		//获取结果集
		List<ComMetadataSyncRecordEntity> data = comMetadataSyncMapper.selectRecordList(ce);
		PageInfo<ComMetadataSyncRecordEntity> pageInfo = new PageInfo<>(data);
		
		List<ComMetadataSyncRecordDto> dtoData = comMetaDataSyncRecordMapstruct.toDto(pageInfo.getList());
		PageBean pageBean = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		return pageBean;
	}

	/**
	 *  删除公共元数据同步记录
	 * @description 
	 * @author wlg
	 * @date 2020-02-17 17:52
	 * @param ids
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void delRecord(String ids) throws Exception {
		String[] idArr = ids.split(",");
		for(String id:idArr) {
			comMetadataSyncRecordDao.deleteById(id);
		}
	}
	
	

}
