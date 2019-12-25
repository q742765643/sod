package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableForeignKeyDao;
import com.piesat.dm.entity.TableForeignKeyEntity;
import com.piesat.dm.rpc.api.TableForeignKeyService;
import com.piesat.dm.rpc.dto.TableForeignKeyDto;
import com.piesat.dm.rpc.mapper.TableForeignKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:07:13
 */
@Service
public class TableForeignKeyServiceImpl  extends BaseService<TableForeignKeyEntity> implements TableForeignKeyService {
    @Autowired
    private TableForeignKeyDao tableForeignKeyDao;
    @Autowired
    private TableForeignKeyMapper tableForeignKeyMapper;

    @Override
    public BaseDao<TableForeignKeyEntity> getBaseDao() {
        return tableForeignKeyDao;
    }

    @Override
    public TableForeignKeyDto saveDto(TableForeignKeyDto TableForeignKeyDto) {
        TableForeignKeyEntity TableForeignKeyEntity = this.tableForeignKeyMapper.toEntity(TableForeignKeyDto);
        TableForeignKeyEntity = this.save(TableForeignKeyEntity);
        return this.tableForeignKeyMapper.toDto(TableForeignKeyEntity);
    }

    @Override
    public List<TableForeignKeyDto> all() {
        List<TableForeignKeyEntity> all = this.getAll();
        return this.tableForeignKeyMapper.toDto(all);
    }

    @Override
    public TableForeignKeyDto getDotById(String id) {
        TableForeignKeyEntity TableForeignKeyEntity = this.getById(id);
        return this.tableForeignKeyMapper.toDto(TableForeignKeyEntity);
    }
}
