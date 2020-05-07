package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.TableSqlDao;
import com.piesat.dm.entity.datatable.TableSqlEntity;
import com.piesat.dm.rpc.api.datatable.TableSqlService;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.dm.rpc.mapper.datatable.TableSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:08:01
 */
@Service
public class TableSqlServiceImpl extends BaseService<TableSqlEntity> implements TableSqlService {
    @Autowired
    private TableSqlDao tableSqlDao;
    @Autowired
    private TableSqlMapper tableSqlMapper;


    @Override
    public BaseDao<TableSqlEntity> getBaseDao() {
        return this.tableSqlDao;
    }

    @Override
    @Transactional
    public TableSqlDto saveDto(TableSqlDto tableSqlDto) {
        this.tableSqlDao.deleteByDatabaseIdAndTableName(tableSqlDto.getDatabaseId(),tableSqlDto.getTableName());
        TableSqlEntity tableSqlEntity = this.tableSqlMapper.toEntity(tableSqlDto);
        tableSqlEntity = this.saveNotNull(tableSqlEntity);
        return this.tableSqlMapper.toDto(tableSqlEntity);
    }

    @Override
    public TableSqlDto getDotById(String id) {
        TableSqlEntity tableSqlEntity = this.getById(id);
        return this.tableSqlMapper.toDto(tableSqlEntity);
    }

    @Override
    public List<TableSqlDto> all() {
        List<TableSqlEntity> all = this.getAll();
        return this.tableSqlMapper.toDto(all);
    }
}
