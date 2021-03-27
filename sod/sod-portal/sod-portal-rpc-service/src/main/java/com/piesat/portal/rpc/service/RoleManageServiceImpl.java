package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.RoleManageDao;
import com.piesat.portal.dao.RoleMenuManageDao;
import com.piesat.portal.entity.RoleManageEntity;
import com.piesat.portal.entity.RoleMenuManageEntity;
import com.piesat.portal.rpc.api.RoleManageService;
import com.piesat.portal.rpc.dto.RoleManageDto;
import com.piesat.portal.rpc.mapstruct.RoleManageMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("roleManageService")
public class RoleManageServiceImpl extends BaseService<RoleManageEntity> implements RoleManageService {

    @Autowired
    private RoleManageDao roleManageDao;

    @Autowired
    private RoleMenuManageDao roleMenuManageDao;

    @Autowired
    private RoleManageMapstruct roleManageMapstruct;

    @Override
    public BaseDao<RoleManageEntity> getBaseDao() {
        return roleManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<RoleManageDto> pageForm) {

        RoleManageDto roleManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(roleManageDto.getRoleName())){
            specificationBuilder.add("roleName", SpecificationOperator.Operator.likeAll.name(),roleManageDto.getRoleName());
        }
        if(StringUtils.isNotEmpty(roleManageDto.getStatus())){
            specificationBuilder.add("status", SpecificationOperator.Operator.eq.name(),roleManageDto.getStatus());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "roleSort");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<RoleManageEntity> roleManageEntities = (List<RoleManageEntity>) pageBean.getPageData();
        List<RoleManageDto> roleManageDtos = roleManageMapstruct.toDto(roleManageEntities);

        pageBean.setPageData(roleManageDtos);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleManageDto saveDto(RoleManageDto roleManageDto) {
        // 新增角色信息
        RoleManageEntity roleManageEntity = roleManageMapstruct.toEntity(roleManageDto);
        roleManageEntity = this.saveNotNull(roleManageEntity);
        insertRoleMenu(roleManageEntity);
        roleManageDto = roleManageMapstruct.toDto(roleManageEntity);
        return roleManageDto;
    }

    @Override
    public RoleManageDto getDotById(String id) {
        RoleManageEntity roleManageEntity = this.getById(id);
        RoleManageDto roleManageDto = this.roleManageMapstruct.toDto(roleManageEntity);
        return roleManageDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleManageDto updateDto(RoleManageDto roleManageDto) {
        // 修改角色信息
        RoleManageEntity roleManageEntity = roleManageMapstruct.toEntity(roleManageDto);
        roleManageEntity=this.saveNotNull(roleManageEntity);
        // 删除角色与菜单关联
        roleMenuManageDao.deleteByRoleId(roleManageEntity.getId());
        insertRoleMenu(roleManageEntity);
        roleManageDto = roleManageMapstruct.toDto(roleManageEntity);
        return roleManageDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(List<String> roleIds) {
        for(String roleId:roleIds){
            roleMenuManageDao.deleteByRoleId(roleId);
            roleManageDao.deleteById(roleId);
        }
    }

    @Override
    public RoleManageDto updateRoleStatus(RoleManageDto roleManageDto) {
        RoleManageEntity roleManageEntity = roleManageMapstruct.toEntity(roleManageDto);
        roleManageEntity = this.saveNotNull(roleManageEntity);
        return roleManageMapstruct.toDto(roleManageEntity);
    }

    @Override
    public List<RoleManageDto> findAllRole() {
        List<RoleManageEntity> roleManageEntities = this.getAll();
        return roleManageMapstruct.toDto(roleManageEntities);
    }

    public void insertRoleMenu(RoleManageEntity role)
    {
        int rows = 1;
        // 新增角色与菜单管理
        List<RoleMenuManageEntity> list = new ArrayList<>();
        for (String menuId : role.getMenuIds())
        {
            RoleMenuManageEntity rm = new RoleMenuManageEntity();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            roleMenuManageDao.saveNotNullAll(list);
        }
    }
}
