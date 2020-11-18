package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.TypicalAppDao;
import com.piesat.portal.entity.TypicalAppEntity;
import com.piesat.portal.rpc.api.TypicalAppService;
import com.piesat.portal.rpc.dto.TypicalAppDto;
import com.piesat.portal.rpc.mapstruct.TypicalAppMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("typicalAppService")
public class TypicalAppServiceImpl extends BaseService<TypicalAppEntity> implements TypicalAppService {

    @Autowired
    private TypicalAppDao typicalAppDao;

    @Autowired
    private TypicalAppMapstruct typicalAppMapstruct;


    @Override
    public BaseDao<TypicalAppEntity> getBaseDao() {
        return typicalAppDao;
    }


    @Override
    public PageBean selectPageList(PageForm<TypicalAppDto> pageForm) {
        TypicalAppDto typicalAppDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(typicalAppDto.getClassCode())){
            specificationBuilder.add("classCode", SpecificationOperator.Operator.eq.name(),typicalAppDto.getClassCode());
        }
        if(StringUtils.isNotEmpty(typicalAppDto.getIsshow())){
            specificationBuilder.add("isshow", SpecificationOperator.Operator.eq.name(),typicalAppDto.getIsshow());
        }
        if(StringUtils.isNotEmpty(typicalAppDto.getAppName())){
            specificationBuilder.add("appName", SpecificationOperator.Operator.likeAll.name(),typicalAppDto.getAppName());
        }
        if(StringUtils.isNotEmpty(typicalAppDto.getOrgName())){
            specificationBuilder.add("orgName", SpecificationOperator.Operator.likeAll.name(),typicalAppDto.getOrgName());
        }
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<TypicalAppEntity> typicalAppEntities = (List<TypicalAppEntity>) pageBean.getPageData();
        List<TypicalAppDto> typicalAppDtos = typicalAppMapstruct.toDto(typicalAppEntities);
        pageBean.setPageData(typicalAppDtos);
        return pageBean;
    }

    @Override
    public TypicalAppDto getDotById(String id) {
        TypicalAppEntity typicalAppEntity = this.getById(id);
        return this.typicalAppMapstruct.toDto(typicalAppEntity);
    }

    @Override
    public TypicalAppDto saveDto(TypicalAppDto typicalAppDto) {
        TypicalAppEntity typicalAppEntity = typicalAppMapstruct.toEntity(typicalAppDto);
        typicalAppEntity = this.saveNotNull(typicalAppEntity);
        return typicalAppMapstruct.toDto(typicalAppEntity);
    }

    @Override
    public TypicalAppDto updateDto(TypicalAppDto typicalAppDto) {
        TypicalAppEntity typicalAppEntity = typicalAppMapstruct.toEntity(typicalAppDto);
        typicalAppEntity = this.saveNotNull(typicalAppEntity);
        return typicalAppMapstruct.toDto(typicalAppEntity);
    }
}
