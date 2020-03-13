package com.piesat.sod.system.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.sod.system.dao.ServiceCodeDefineDao;
import com.piesat.sod.system.entity.ServiceCodeDefineEntity;
import com.piesat.sod.system.rpc.api.ServiceCodeDefineService;
import com.piesat.sod.system.rpc.dto.ServiceCodeDefineDto;
import com.piesat.sod.system.rpc.mapstruct.ServiceCodeDefineMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 14:06
 */
@Service
public class ServiceCodeDefineServiceImpl extends BaseService<ServiceCodeDefineEntity> implements ServiceCodeDefineService {

    @Autowired
    private ServiceCodeDefineDao serviceCodeDefineDao;

    @Autowired
    private ServiceCodeDefineMapstruct serviceCodeDefineMapstruct;

    @Override
    public BaseDao<ServiceCodeDefineEntity> getBaseDao() {
        return this.serviceCodeDefineDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ServiceCodeDefineDto> pageForm) {
        ServiceCodeDefineEntity serviceCodeDefineEntity=serviceCodeDefineMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(serviceCodeDefineEntity.getUserFcstEle())){
            specificationBuilder.add("userFcstEle", SpecificationOperator.Operator.likeAll.name(),serviceCodeDefineEntity.getUserFcstEle());
        }
        if(StringUtils.isNotNullString(serviceCodeDefineEntity.getDbFcstEle())){
            specificationBuilder.add("dbFcstEle", SpecificationOperator.Operator.likeAll.name(),serviceCodeDefineEntity.getDbFcstEle());
        }

        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<ServiceCodeDefineEntity> ServiceCodeDefineEntities= (List<ServiceCodeDefineEntity>) pageBean.getPageData();
        List<ServiceCodeDefineDto> serviceCodeDefineDtos = serviceCodeDefineMapstruct.toDto(ServiceCodeDefineEntities);
        pageBean.setPageData(serviceCodeDefineDtos);
        return pageBean;
    }

    @Override
    public ServiceCodeDefineDto saveDto(ServiceCodeDefineDto serviceCodeDefineDto) {
        ServiceCodeDefineEntity serviceCodeDefineEntity=serviceCodeDefineMapstruct.toEntity(serviceCodeDefineDto);
        serviceCodeDefineEntity = this.saveNotNull(serviceCodeDefineEntity);
        return serviceCodeDefineMapstruct.toDto(serviceCodeDefineEntity);
    }

    @Override
    public ServiceCodeDefineDto updateDto(ServiceCodeDefineDto serviceCodeDefineDto) {
        ServiceCodeDefineEntity serviceCodeDefineEntity=serviceCodeDefineMapstruct.toEntity(serviceCodeDefineDto);
        serviceCodeDefineEntity = this.saveNotNull(serviceCodeDefineEntity);
        return serviceCodeDefineMapstruct.toDto(serviceCodeDefineEntity);
    }

    @Override
    public void deleteRecordByIds(List<String> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public List<ServiceCodeDefineDto> findByDbFcstEle(String dbFcstEle) {
        List<ServiceCodeDefineEntity> byDbFcstEle = this.serviceCodeDefineDao.findByDbFcstEle(dbFcstEle);
        return this.serviceCodeDefineMapstruct.toDto(byDbFcstEle);
    }

    @Override
    public ServiceCodeDefineDto findById(String id) {
        ServiceCodeDefineEntity serviceCodeDefineEntity = this.getById(id);
        return this.serviceCodeDefineMapstruct.toDto(serviceCodeDefineEntity);
    }
}
