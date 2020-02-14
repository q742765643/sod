package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.dm.dao.ConsistencyCheckDao;
import com.piesat.dm.entity.ConsistencyCheckEntity;
import com.piesat.dm.rpc.api.ConsistencyCheckService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.dm.rpc.mapper.ConsistencyCheckMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:43
 */
@Service
public class ConsistencyCheckServiceImpl extends BaseService<ConsistencyCheckEntity> implements ConsistencyCheckService {

    @Autowired
    private ConsistencyCheckDao consistencyCheckDao;
    @Autowired
    private ConsistencyCheckMapper consistencyCheckMapper;
    @Override
    public BaseDao<ConsistencyCheckEntity> getBaseDao() {
        return this.consistencyCheckDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ConsistencyCheckDto> pageForm) {
        ConsistencyCheckEntity consistencyCheckEntity=consistencyCheckMapper.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<ConsistencyCheckEntity> consistencyCheckEntities= (List<ConsistencyCheckEntity>) pageBean.getPageData();
        pageBean.setPageData(consistencyCheckMapper.toDto(consistencyCheckEntities));
        return pageBean;
    }
}
