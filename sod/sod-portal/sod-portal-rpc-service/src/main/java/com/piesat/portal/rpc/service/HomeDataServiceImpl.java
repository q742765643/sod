package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.HomeDataDao;
import com.piesat.portal.entity.HomeDataEntity;
import com.piesat.portal.rpc.api.HomeDataService;
import com.piesat.portal.rpc.dto.HomeDataDto;
import com.piesat.portal.rpc.mapstruct.HomeDataMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("homeDataService")
public class HomeDataServiceImpl extends BaseService<HomeDataEntity> implements HomeDataService {

    @Autowired
    private HomeDataDao homeDataDao;

    @Autowired
    private HomeDataMapstruct homeDataMapstruct;

    @Override
    public BaseDao<HomeDataEntity> getBaseDao() {
        return homeDataDao;
    }

    @Override
    public PageBean selectPageList(PageForm<HomeDataDto> pageForm) {
        HomeDataDto homeDataDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(homeDataDto.getRemark())){
            specificationBuilder.add("remark", SpecificationOperator.Operator.likeAll.name(),homeDataDto.getRemark());
        }
        PageBean pageBean = this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<HomeDataEntity> homeDataEntities = (List<HomeDataEntity>) pageBean.getPageData();
        List<HomeDataDto> homeDataDtos = homeDataMapstruct.toDto(homeDataEntities);
        pageBean.setPageData(homeDataDtos);
        return pageBean;
    }

    @Override
    public HomeDataDto saveDto(HomeDataDto homeDataDto) {
        HomeDataEntity homeDataEntity = homeDataMapstruct.toEntity(homeDataDto);
        homeDataEntity = this.saveNotNull(homeDataEntity);
        return homeDataMapstruct.toDto(homeDataEntity);
    }

    @Override
    public HomeDataDto updateDto(HomeDataDto homeDataDto) {
        HomeDataEntity homeDataEntity = homeDataMapstruct.toEntity(homeDataDto);
        homeDataEntity = this.saveNotNull(homeDataEntity);
        return homeDataMapstruct.toDto(homeDataEntity);
    }

    @Override
    public HomeDataDto getDotById(String id) {
        HomeDataEntity homeDataEntity = this.getById(id);
        return this.homeDataMapstruct.toDto(homeDataEntity);
    }


}
