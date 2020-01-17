package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableColumnDao;
import com.piesat.dm.entity.TableColumnEntity;
import com.piesat.dm.rpc.api.TableColumnService;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.dm.rpc.mapper.TableColumnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:39:16
 */
@Service
public class TableColumnServiceImpl extends BaseService<TableColumnEntity> implements TableColumnService {
    @Autowired
    private TableColumnDao tableColumnDao;
    @Autowired
    private TableColumnMapper tableColumnMapper;

    @Override
    public BaseDao<TableColumnEntity> getBaseDao() {
        return tableColumnDao;
    }

    @Override
    public TableColumnDto saveDto(TableColumnDto tableColumnDto) {
        TableColumnEntity tableColumnEntity = this.tableColumnMapper.toEntity(tableColumnDto);
        tableColumnEntity = this.save(tableColumnEntity);
        return this.tableColumnMapper.toDto(tableColumnEntity);
    }

    @Override
    public List<TableColumnDto> saveDtoList(List<TableColumnDto> tableColumnDtoList) {
        List<TableColumnDto> l = new ArrayList<>();
        for (TableColumnDto t:tableColumnDtoList  ) {
            TableColumnEntity tableColumnEntity = this.tableColumnMapper.toEntity(t);
            tableColumnEntity = this.save(tableColumnEntity);
            l.add(this.tableColumnMapper.toDto(tableColumnEntity));
        }
        return l;
    }

    @Override
    public List<TableColumnDto> all() {
        List<TableColumnEntity> all = this.getAll();
        return this.tableColumnMapper.toDto(all);
    }

    @Override
    public List<TableColumnDto> findByTableId(String tableId) {
        List<TableColumnEntity> tableColumns = this.tableColumnDao.findByTableId(tableId);
        return tableColumnMapper.toDto(tableColumns);
    }

    @Override
    public int deleteByIdIn(List<String> ids) {
        return this.tableColumnDao.deleteByIdIn(ids);
    }

    @Override
    public TableColumnDto getDotById(String id) {
        TableColumnEntity tableColumnEntity = this.getById(id);
        return this.tableColumnMapper.toDto(tableColumnEntity);
    }
}
