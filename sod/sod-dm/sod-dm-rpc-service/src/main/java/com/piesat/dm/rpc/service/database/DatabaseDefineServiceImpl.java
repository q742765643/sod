package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.mapper.database.DatabaseDefineMapper;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:59:29
 */
@Service
public class DatabaseDefineServiceImpl extends BaseService<DatabaseDefineEntity> implements DatabaseDefineService {
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDefineMapper databaseDefineMapper;

    @Override
    public BaseDao<DatabaseDefineEntity> getBaseDao() {
        return databaseDefineDao;
    }

    @Override
    public DatabaseDefineDto saveDto(DatabaseDefineDto databaseDefineDto) {
        DatabaseDefineEntity databaseDefineEntity = this.databaseDefineMapper.toEntity(databaseDefineDto);
        DatabaseDefineEntity save = this.save(databaseDefineEntity);
        return this.databaseDefineMapper.toDto(save);
    }

    @Override
    public List<DatabaseDefineDto> all() {
        List<DatabaseDefineEntity> all = this.getAll();
        return this.databaseDefineMapper.toDto(all);
    }

    @Override
    public List<DatabaseDefineDto> findByType(String databaseType) {
        return this.databaseDefineMapper.toDto(this.databaseDefineDao.findByDatabaseType(databaseType));
    }

    @Override
    public PageBean getPage(DatabaseDefineDto databaseDefineDto, int pageNum, int pageSize) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        if (StringUtils.isNotBlank(databaseDefineDto.getId())) {
            ssb.add("id", SpecificationOperator.Operator.likeAll.name(), databaseDefineDto.getId());
        }
        if (StringUtils.isNotBlank(databaseDefineDto.getDatabaseName())) {
            ssb.add("databaseName", SpecificationOperator.Operator.likeAll.name(), databaseDefineDto.getDatabaseName());
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        PageBean page = this.getPage(ssb.generateSpecification(), new PageForm(pageNum, pageSize), sort);
        List<DatabaseDefineEntity> pageData = (List<DatabaseDefineEntity>)page.getPageData();
        page.setPageData(this.databaseDefineMapper.toDto(pageData));
        return page;
    }

    @Override
    public DatabaseDefineDto getDotById(String id) {
        DatabaseDefineEntity databaseDefineEntity = this.getById(id);
        return this.databaseDefineMapper.toDto(databaseDefineEntity);
    }
}
