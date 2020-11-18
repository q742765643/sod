package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.SdkManageDao;
import com.piesat.portal.entity.FileManageEntity;
import com.piesat.portal.entity.SdkManageEntity;
import com.piesat.portal.rpc.api.SdkManageService;
import com.piesat.portal.rpc.dto.FileManageDto;
import com.piesat.portal.rpc.dto.SdkManageDto;
import com.piesat.portal.rpc.mapstruct.SdkManageMapstruct;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sdkManageService")
public class SdkManageServiceImpl extends BaseService<SdkManageEntity> implements SdkManageService {

    @Autowired
    private SdkManageDao sdkManageDao;

    @Autowired
    private SdkManageMapstruct sdkManageMapstruct;

    @Override
    public BaseDao<SdkManageEntity> getBaseDao() {
        return sdkManageDao;
    }


    @Override
    public PageBean selectPageList(PageForm<SdkManageDto> pageForm) {
        SdkManageDto sdkManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(sdkManageDto.getSdkType())){
            specificationBuilder.add("sdkType", SpecificationOperator.Operator.eq.name(),sdkManageDto.getSdkType());
        }
        Sort sort=Sort.by(Sort.Direction.ASC,"serialNumber");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,sort);
        List<SdkManageEntity> sdkManageEntities = (List<SdkManageEntity>) pageBean.getPageData();
        List<SdkManageDto> sdkManageDtos = sdkManageMapstruct.toDto(sdkManageEntities);
        pageBean.setPageData(sdkManageDtos);
        return pageBean;
    }

    @Override
    public SdkManageDto getDotById(String id) {
        SdkManageEntity sdkManageEntity = this.getById(id);
        return this.sdkManageMapstruct.toDto(sdkManageEntity);
    }

    @Override
    public SdkManageDto saveDto(SdkManageDto sdkManageDto) {
        SdkManageEntity sdkManageEntity = sdkManageMapstruct.toEntity(sdkManageDto);
        sdkManageEntity = this.saveNotNull(sdkManageEntity);
        return sdkManageMapstruct.toDto(sdkManageEntity);
    }

    @Override
    public SdkManageDto updateDto(SdkManageDto sdkManageDto) {
        SdkManageEntity sdkManageEntity =  sdkManageMapstruct.toEntity(sdkManageDto);
        sdkManageEntity = this.saveNotNull(sdkManageEntity);
        return sdkManageMapstruct.toDto(sdkManageEntity);
    }
}
