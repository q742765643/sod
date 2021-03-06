package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.TableForeignKeyDao;
import com.piesat.dm.entity.datatable.TableForeignKeyEntity;
import com.piesat.dm.rpc.api.datatable.TableForeignKeyService;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
import com.piesat.dm.rpc.mapper.datatable.TableForeignKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:07:13
 */
@Service
public class TableForeignKeyServiceImpl extends BaseService<TableForeignKeyEntity> implements TableForeignKeyService {
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
        TableForeignKeyEntity = this.saveNotNull(TableForeignKeyEntity);
        return this.tableForeignKeyMapper.toDto(TableForeignKeyEntity);
    }

    @Override
    public List<TableForeignKeyDto> all() {
        List<TableForeignKeyEntity> all = this.getAll();
        return this.tableForeignKeyMapper.toDto(all);
    }

    @Override
    public List<Map<String, Object>> findByTableId(String tableId) {
        return this.tableForeignKeyDao.findList(tableId);
    }

    @Override
    public List<TableForeignKeyDto> findBySubOrTableId(String tableId) {
        List<TableForeignKeyEntity> tableForeignKey = this.tableForeignKeyDao.findByTableIdOrSubTableId(tableId, tableId);
        return this.tableForeignKeyMapper.toDto(tableForeignKey);
    }

    @Override
    public TableForeignKeyDto getDotById(String id) {
        TableForeignKeyEntity TableForeignKeyEntity = this.getById(id);
        return this.tableForeignKeyMapper.toDto(TableForeignKeyEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIdIn(List<String> ids) {
        return this.tableForeignKeyDao.deleteByIdIn(ids);
    }


}
