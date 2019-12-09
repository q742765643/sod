package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableIndexDao;
import com.piesat.dm.entity.TableIndexEntity;
import com.piesat.dm.rpc.api.TableIndexService;
import com.piesat.dm.rpc.dto.TableIndexDto;
import com.piesat.dm.rpc.mapper.TableIndexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表索引
 *
 * @author cwh
 * @date 2019年 11月22日 16:40:38
 */
@Service
public class TableIndexServiceImpl extends BaseService<TableIndexEntity> implements TableIndexService {
    @Autowired
    private TableIndexDao tableIndexDao;
    @Autowired
    private TableIndexMapper tableIndexMapper;

    @Override
    public BaseDao<TableIndexEntity> getBaseDao() {
        return tableIndexDao;
    }

    @Override
    public TableIndexDto saveDto(TableIndexDto tableIndexDto) {
        TableIndexEntity tableIndexEntity = this.tableIndexMapper.toEntity(tableIndexDto);
        tableIndexEntity = this.save(tableIndexEntity);
        return this.tableIndexMapper.toDto(tableIndexEntity);
    }

    @Override
    public List<TableIndexDto> all() {
        List<TableIndexEntity> all = this.getAll();
        return this.tableIndexMapper.toDto(all);
    }

    @Override
    public TableIndexDto getDotById(String id) {
        TableIndexEntity tableIndexEntity = this.getById(id);
        return this.tableIndexMapper.toDto(tableIndexEntity);
    }
}
