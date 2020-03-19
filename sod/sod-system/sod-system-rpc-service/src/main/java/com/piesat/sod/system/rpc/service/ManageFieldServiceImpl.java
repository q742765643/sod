package com.piesat.sod.system.rpc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.sod.system.dao.ManageFieldDao;
import com.piesat.sod.system.dao.ManageFieldGroupDao;
import com.piesat.sod.system.dao.ManageGroupDao;
import com.piesat.sod.system.entity.ManageFieldEntity;
import com.piesat.sod.system.entity.ManageFieldGroupEntity;
import com.piesat.sod.system.entity.ManageGroupEntity;
import com.piesat.sod.system.mapper.ManageFieldMapper;
import com.piesat.sod.system.rpc.api.ManageFieldService;
import com.piesat.sod.system.rpc.dto.ManageFieldDto;
import com.piesat.sod.system.rpc.dto.ManageGroupDto;
import com.piesat.sod.system.rpc.mapstruct.ManageFieldMapstruct;
import com.piesat.sod.system.rpc.mapstruct.ManageGroupMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/** 管理字段service
*@description
*@author wlg
*@date 2020年1月16日下午5:19:38
*
*/
@Service("manageFieldService")
public class ManageFieldServiceImpl extends BaseService<ManageFieldEntity> implements ManageFieldService{
	
	@Autowired
	private ManageFieldDao manageFieldDao;
	
	@Autowired
	private ManageGroupDao manageGroupDao;
	
	@Autowired
	private ManageFieldGroupDao manageFieldGroupDao;
	
	@Autowired
	private ManageGroupMapstruct manageGroupMapstruct;
	
	@Autowired
	private ManageFieldMapper manageFieldMapper;
	@Autowired
	private ManageFieldMapstruct manageFieldMapstruct;

	@Override
	public BaseDao<ManageFieldEntity> getBaseDao() {
		return manageFieldDao;
	}

	/**
	 *  获取所有管理字段分组
	 * @description 
	 * @author wlg
	 * @date 2020-01-16 17:27
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ManageGroupDto> findAllManageGroup() throws Exception {
		List<ManageGroupEntity> entityList = manageGroupDao.findAll();
		List<ManageGroupDto> result = manageGroupMapstruct.toDto(entityList);
		return result;
	}

	/**
	 *  新增管理字段分组
	 * @description 
	 * @author wlg
	 * @date 2020-01-16 17:57
	 * @param manageGroupDto
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addManageGroup(ManageGroupDto manageGroupDto) throws Exception {
		manageGroupDao.save(manageGroupMapstruct.toEntity(manageGroupDto));
		
	}

	/**
	 *  删除管理字段分组
	 * @description 
	 * @author wlg
	 * @date 2020-01-16 17:57
	 * @param groupId
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void delManageGroup(String groupId) throws Exception {
		manageGroupDao.deleteById(groupId);

		manageFieldGroupDao.delByGroupId(groupId);

		
	}

	/**
	 *  获取分页数据
	 * @description 
	 * @author wlg
	 * @date 2020-01-17 17:09
	 * @param manageFieldDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageBean findPageData(PageForm<ManageFieldDto> pageForm) throws Exception {
		ManageFieldEntity mfe = manageFieldMapstruct.toEntity(pageForm.getT());
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		//获取结果集
		if(!StringUtil.isEmpty(mfe.getDbEleCode())) mfe.setDbEleCode("%"+mfe.getDbEleCode()+"%");
		
		List<ManageFieldEntity> data = manageFieldMapper.findByConditions(mfe);
		PageInfo<ManageFieldEntity> pageInfo = new PageInfo<>(data);
		
		List<ManageFieldDto> dtoData = manageFieldMapstruct.toDto(pageInfo.getList());
		PageBean pageBean = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		
		return pageBean;
	}

	/**
	 *  新增管理字段
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 10:07
	 * @param manageFieldDto
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addManageField(ManageFieldDto manageFieldDto) throws Exception {
		ManageFieldEntity mfe = manageFieldMapstruct.toEntity(manageFieldDto);
		mfe = manageFieldDao.save(mfe);
		
		//保存与分组关联表
		String[] groupIds = manageFieldDto.getGroupId().split(",");
		for(String groupId:groupIds) {
			ManageFieldGroupEntity mfge = new ManageFieldGroupEntity();
			mfge.setFieldId(mfe.getId());
			mfge.setGroupId(groupId);
			manageFieldGroupDao.save(mfge);
		}
		
	}

	/**
	 *  主键查询管理字段分组
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 10:36
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ManageGroupDto findManageGroupByPk(String groupId) throws Exception {
		
		ManageGroupEntity mge = manageGroupDao.findById(groupId).orElse(null);
		ManageGroupDto mgd = manageGroupMapstruct.toDto(mge);
		return mgd;
	}

	/**
	 *  主键查询管理字段
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 10:48
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public ManageFieldDto findManageFieldByPk(String id) throws Exception {
		ManageFieldEntity mfe = manageFieldDao.findById(id).orElse(null);
		ManageFieldDto mfd = manageFieldMapstruct.toDto(mfe);
		return mfd;
	}

	/**
	 *  编辑管理字段
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 11:23
	 * @param manageFieldDto
	 * @throws Exception
	 */
	@Override
	public void editManageField(ManageFieldDto manageFieldDto) throws Exception {
		ManageFieldEntity mfe = manageFieldMapstruct.toEntity(manageFieldDto);
		manageFieldDao.save(mfe);
	}

	/**
	 *  编辑管理字段分组
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 11:31
	 * @param manageGroupDto
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void editManageGroup(ManageGroupDto manageGroupDto) throws Exception {
		ManageGroupEntity mge = manageGroupMapstruct.toEntity(manageGroupDto);
		manageGroupDao.save(mge);
	}

	/**
	 *  批量删除管理字段
	 * @description 
	 * @author wlg
	 * @date 2020-02-06 11:38
	 * @param ids
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void delManageField(String ids) throws Exception {
		String[] idArr = ids.split(",");
		
		for(String id:idArr) {
			manageFieldDao.deleteById(id);
			
			manageFieldGroupDao.delByFieldId(id);
		}
		
	}

	/**
	 *  管理字段导出
	 * @description 
	 * @author wlg
	 * @date 2020-03-19 16:34
	 * @param dto
	 */
	@Override
	public void exportExcel(ManageFieldDto dto) {
		List<ManageFieldEntity> data = findList(dto);
		ExcelUtil<ManageFieldEntity> util = new ExcelUtil(ManageFieldEntity.class);
		util.exportExcel(data,"管理字段管理");
		
	}
	/**
	 *  获取结果集
	 * @description 
	 * @author wlg
	 * @date 2020年3月19日下午4:52:29
	 * @param dto
	 * @return
	 */
	private List<ManageFieldEntity> findList(ManageFieldDto dto){
		try {
			ManageFieldEntity mfe = manageFieldMapstruct.toEntity(dto);
			//获取结果集
			if(!StringUtil.isEmpty(mfe.getDbEleCode())) mfe.setDbEleCode("%"+mfe.getDbEleCode()+"%");
			List<ManageFieldEntity> data = manageFieldMapper.findByConditions(mfe);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
