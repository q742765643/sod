package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.DynManageDao;
import com.piesat.portal.entity.DynManageEntity;
import com.piesat.portal.rpc.api.DynManageService;
import com.piesat.portal.rpc.dto.DynManageDto;
import com.piesat.portal.rpc.mapstruct.DynManageMapstruct;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dynManageService")
public class DynManageServiceImpl extends BaseService<DynManageEntity> implements DynManageService {
    @Autowired
    private DynManageDao dynManageDao;
    @Autowired
    private DynManageMapstruct dynManageMapstruct;
    @Override
    public BaseDao<DynManageEntity> getBaseDao() {
        return dynManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<DynManageDto> pageForm) {
        DynManageDto dynManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(dynManageDto.getTitle())){
            specificationBuilder.add("title", SpecificationOperator.Operator.likeAll.name(),dynManageDto.getTitle());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<DynManageEntity> DynManageEntities = (List<DynManageEntity>) pageBean.getPageData();
        List<DynManageDto> dynManageDtos = dynManageMapstruct.toDto(DynManageEntities);
        pageBean.setPageData(dynManageDtos);
        return pageBean;
    }

    @Override
    public DynManageDto saveDto(DynManageDto dynManageDto) {
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        DynManageEntity dynManageEntity = dynManageMapstruct.toEntity(dynManageDto);
        dynManageEntity.setCreateBy(loginUser.getNickName());
        dynManageEntity = this.saveNotNull(dynManageEntity);
        return dynManageMapstruct.toDto(dynManageEntity);
    }

    @Override
    public DynManageDto updateDto(DynManageDto dynManageDto) {
        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        DynManageEntity dynManageEntity=dynManageMapstruct.toEntity(dynManageDto);
        dynManageEntity.setCreateBy(loginUser.getNickName());
        dynManageEntity = this.saveNotNull(dynManageEntity);
        return dynManageMapstruct.toDto(dynManageEntity);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public DynManageDto getDotById(String id) {
        DynManageEntity dynManageEntity = this.getById(id);
        return this.dynManageMapstruct.toDto(dynManageEntity);
    }

}
