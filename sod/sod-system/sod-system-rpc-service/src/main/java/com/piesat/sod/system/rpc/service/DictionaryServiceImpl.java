package com.piesat.sod.system.rpc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.DictionaryDao;
import com.piesat.sod.system.entity.DictionaryEntity;
import com.piesat.sod.system.mapper.DictionaryMapper;
import com.piesat.sod.system.rpc.api.DictionaryService;
import com.piesat.sod.system.rpc.dto.DictionaryDto;
import com.piesat.sod.system.rpc.mapstruct.DictionaryMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/** 字典表管理
*@description
*@author wlg
*@date 2020年1月14日下午5:06:17
*
*/
@Service
public class DictionaryServiceImpl extends BaseService<DictionaryEntity> implements DictionaryService{
	
	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Autowired
	private DictionaryMapstruct dictionaryMapstruct;
	
	@Autowired
	private DictionaryMapper dictionaryMapper;

	@Override
	public BaseDao<DictionaryEntity> getBaseDao() {
		return dictionaryDao;
	}

	/**
	 *  获取分页数据
	 * @description 
	 * @author wlg
	 * @date 2020-01-14 17:14
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean findPageData(PageForm<DictionaryDto> pageForm) throws Exception {
		DictionaryEntity de = dictionaryMapstruct.toEntity(pageForm.getT());
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		//获取结果集
		List<DictionaryEntity> data = dictionaryMapper.selectList(de);
		PageInfo<DictionaryEntity> pageInfo = new PageInfo<>(data);
		
		List<DictionaryDto> dtoData = dictionaryMapstruct.toDto(pageInfo.getList());
		PageBean pageBean = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		
		return pageBean;
	}

}
