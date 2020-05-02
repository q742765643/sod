package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.ConsistencyCheckHistoryDao;
import com.piesat.dm.entity.ConsistencyCheckEntity;
import com.piesat.dm.entity.ConsistencyCheckHistoryEntity;
import com.piesat.dm.rpc.api.ConsistencyCheckHistoryService;
import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.dm.rpc.dto.ConsistencyCheckHistoryDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.mapper.ConsistencyCheckHistoryMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 16:13
 */
@Service
public class ConsistencyCheckHistoryServiceImpl extends BaseService<ConsistencyCheckHistoryEntity> implements ConsistencyCheckHistoryService {

    @Autowired
    private ConsistencyCheckHistoryDao consistencyCheckHistoryDao;

    @Autowired
    private ConsistencyCheckHistoryMapper consistencyCheckHistoryMapper;
    @Override
    public BaseDao<ConsistencyCheckHistoryEntity> getBaseDao() {
        return this.consistencyCheckHistoryDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ConsistencyCheckHistoryDto> pageForm) {
        ConsistencyCheckHistoryEntity consistencyCheckHistoryEntity = consistencyCheckHistoryMapper.toEntity(pageForm.getT());

        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        specificationBuilder.add("databaseId", SpecificationOperator.Operator.eq.name(),consistencyCheckHistoryEntity.getDatabaseId());
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);

        List<ConsistencyCheckHistoryEntity> consistencyCheckHistoryEntities= (List<ConsistencyCheckHistoryEntity>) pageBean.getPageData();

        List<ConsistencyCheckHistoryDto> consistencyCheckHistoryDtos = consistencyCheckHistoryMapper.toDto(consistencyCheckHistoryEntities);

        pageBean.setPageData(consistencyCheckHistoryDtos);
        return pageBean;
    }

    @Override
    public List<ConsistencyCheckHistoryDto> findHistoryByDatabaseIdAndFileName(String databaseId, String fileName) {
        List<ConsistencyCheckHistoryEntity> consistencyCheckHistoryEntities = consistencyCheckHistoryDao.findByDatabaseIdAndFileName(databaseId, fileName);
        return consistencyCheckHistoryMapper.toDto(consistencyCheckHistoryEntities);
    }

    @Override
    public ConsistencyCheckHistoryDto saveDto(ConsistencyCheckHistoryDto consistencyCheckHistoryDto) {
        ConsistencyCheckHistoryEntity consistencyCheckHistoryEntity = consistencyCheckHistoryMapper.toEntity(consistencyCheckHistoryDto);
        consistencyCheckHistoryEntity = this.saveNotNull(consistencyCheckHistoryEntity);
        return consistencyCheckHistoryMapper.toDto(consistencyCheckHistoryEntity);
    }

    @Override
    public void deleteById(String id) {
        this.delete(id);
    }
}
