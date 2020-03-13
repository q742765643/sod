package com.piesat.sod.system.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.sod.system.dao.ServiceCodeDao;
import com.piesat.sod.system.entity.ServiceCodeEntity;
import com.piesat.sod.system.rpc.api.ServiceCodeService;
import com.piesat.sod.system.rpc.dto.ServiceCodeDto;
import com.piesat.sod.system.rpc.mapstruct.ServiceCodeMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:15
 */
@Service
public class ServiceCodeServiceImpl extends BaseService<ServiceCodeEntity> implements ServiceCodeService {

    @Autowired
    private ServiceCodeDao serviceCodeDao;

    @Autowired
    private ServiceCodeMapstruct serviceCodeMapstruct;

    @Override
    public BaseDao<ServiceCodeEntity> getBaseDao() {
        return this.serviceCodeDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ServiceCodeDto> pageForm) {
        ServiceCodeEntity serviceCodeEntity=serviceCodeMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(serviceCodeEntity.getUserEleCode())){
            specificationBuilder.add("userEleCode", SpecificationOperator.Operator.likeAll.name(),serviceCodeEntity.getUserEleCode());
        }
        if(StringUtils.isNotNullString(serviceCodeEntity.getDbEleCode())){
            specificationBuilder.add("dbEleCode", SpecificationOperator.Operator.likeAll.name(),serviceCodeEntity.getDbEleCode());
        }
        if(StringUtils.isNotNullString(serviceCodeEntity.getEleName())){
            specificationBuilder.add("eleName", SpecificationOperator.Operator.likeAll.name(),serviceCodeEntity.getEleName());
        }

        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<ServiceCodeEntity> serviceCodeEntities= (List<ServiceCodeEntity>) pageBean.getPageData();
        List<ServiceCodeDto> serviceCodeDtos = serviceCodeMapstruct.toDto(serviceCodeEntities);
        pageBean.setPageData(serviceCodeDtos);
        return pageBean;
    }

    @Override
    public ServiceCodeDto saveDto(ServiceCodeDto serviceCodeDto) {
        ServiceCodeEntity serviceCodeEntity=serviceCodeMapstruct.toEntity(serviceCodeDto);
        serviceCodeEntity = this.saveNotNull(serviceCodeEntity);
        return serviceCodeMapstruct.toDto(serviceCodeEntity);
    }

    @Override
    public ServiceCodeDto updateDto(ServiceCodeDto serviceCodeDto) {
        ServiceCodeEntity serviceCodeEntity=serviceCodeMapstruct.toEntity(serviceCodeDto);
        serviceCodeEntity = this.saveNotNull(serviceCodeEntity);
        return serviceCodeMapstruct.toDto(serviceCodeEntity);
    }

    @Override
    public ServiceCodeDto getDotById(String id) {
        ServiceCodeEntity serviceCodeEntity = this.getById(id);
        return serviceCodeMapstruct.toDto(serviceCodeEntity);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public void deleteById(String id) {
        this.delete(id);
    }

    @Override
    public List<ServiceCodeDto> queryAll() {
        return this.serviceCodeMapstruct.toDto(this.getAll());
    }
}
