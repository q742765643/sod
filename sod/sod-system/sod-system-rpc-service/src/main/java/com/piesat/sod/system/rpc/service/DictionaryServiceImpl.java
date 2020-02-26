package com.piesat.sod.system.rpc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
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
		if(!StringUtil.isEmpty(de.getKeyCol())) de.setKeyCol("%"+de.getKeyCol()+"%");
		List<DictionaryEntity> data = dictionaryMapper.selectList(de);
		PageInfo<DictionaryEntity> pageInfo = new PageInfo<>(data);
		
		List<DictionaryDto> dtoData = dictionaryMapstruct.toDto(pageInfo.getList());
		PageBean pageBean = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		
		return pageBean;
	}

	/**
	 *  获取字典分组
	 * @description 
	 * @author wlg
	 * @date 2020-01-15 08:50
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DictionaryDto> findMenu(String menu) throws Exception {
		List<Integer> menus = new ArrayList<>();
		menus.add(Integer.valueOf(menu));
		String flag = "T";
		List<DictionaryEntity> entityList = dictionaryDao.findByFlagAndMenuIn(flag, menus);
		List<DictionaryDto> result = dictionaryMapstruct.toDto(entityList);
		return result;
	}

	/**
	 *  新增字典分组
	 * @description 
	 * @author wlg
	 * @date 2020-01-15 09:47
	 * @param dictionaryDto
	 * @throws Exception
	 */
	@Override
	public void addType(DictionaryDto dictionaryDto) throws Exception {
		Integer type = dictionaryDto.getType();
		if(null == type) {
			type = dictionaryMapper.selectMaxType();
			if(null == type) type=0;
			dictionaryDto.setType(++type);
		}
		DictionaryEntity de = dictionaryMapstruct.toEntity(dictionaryDto);
		dictionaryDao.save(de);
	}

	/**
	 *  主键查询
	 * @description 
	 * @author wlg
	 * @date 2020-01-15 10:16
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public DictionaryDto findById(String id) throws Exception {
		DictionaryEntity de = dictionaryDao.findById(id).orElse(null);
		DictionaryDto result = dictionaryMapstruct.toDto(de);
		return result;
	}

	/**
	 *  修改字典
	 * @description 
	 * @author wlg
	 * @date 2020-01-15 10:48
	 * @param dictionaryDto
	 * @throws Exception
	 */
	@Override
	public void updateDictionary(DictionaryDto dictionaryDto) throws Exception {
		DictionaryEntity de = dictionaryMapstruct.toEntity(dictionaryDto);
		dictionaryDao.save(de);
	}

	/**
	 *  根据id批量删除
	 * @description 
	 * @author wlg
	 * @date 2020-01-15 11:41
	 * @param ids
	 * @throws Exception
	 */
	@Override
	public void deleteByIds(String ids) throws Exception {
		//去除逗号
		if(ids.lastIndexOf(',') == ids.length()-1) ids = ids.substring(0,ids.length()-1);
		
		String[] idArr = ids.split(",");
		List<String> idList = Arrays.asList(idArr);
		
		dictionaryDao.deleteByIds(idList);
	}

	/**
	 *  根据type 查询管理字段
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 17:54
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DictionaryDto> findByType(String type) throws Exception {
		DictionaryEntity de = new DictionaryEntity();
		de.setType(Integer.valueOf(type));
		
		List<DictionaryEntity> deList = dictionaryMapper.selectList(de);
		List<DictionaryDto> ddList = dictionaryMapstruct.toDto(deList);
		return ddList;
	}

	/**
	 *  获取数据库类型
	 * @description 
	 * @author wlg
	 * @date 2020-02-07 17:11
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<DictionaryDto> queryAllByTypeAndFlag() throws Exception {
		List<DictionaryEntity> deList = dictionaryMapper.queryAllByTypeAndFlag();
		List<DictionaryDto> ddList =  dictionaryMapstruct.toDto(deList);
		return ddList;
	}

}
