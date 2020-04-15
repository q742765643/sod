package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.entity.datatable.CmccElementEntity;
import com.piesat.dm.entity.datatable.DatumTableEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.mapper.datatable.TableColumnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DataTableService dataTableService;
    @Autowired
    private TableColumnMapper tableColumnMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Override
    public BaseDao<TableColumnEntity> getBaseDao() {
        return tableColumnDao;
    }

    @Override
    public TableColumnDto saveDto(TableColumnDto tableColumnDto) {
        TableColumnEntity tableColumnEntity = this.tableColumnMapper.toEntity(tableColumnDto);
        tableColumnEntity = this.save(tableColumnEntity);
        if (tableColumnDto.getUpdateDatabase()){
            DataTableDto datatable = dataTableService.getDotById(tableColumnDto.getTableId());
        }
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
    @Transactional(readOnly = false)
    public int deleteByIdIn(List<String> ids) {
        return this.tableColumnDao.deleteByIdIn(ids);
    }

    @Override
    public TableColumnDto updateDto(TableColumnDto tableColumnDto) {
        TableColumnEntity tableColumnEntity = tableColumnMapper.toEntity(tableColumnDto);
        tableColumnEntity = this.saveNotNull(tableColumnEntity);
        return tableColumnMapper.toDto(tableColumnEntity);
    }

    @Override
    public List<TableColumnDto> getPrimaryKey(String tableId) {
        List<TableColumnEntity> tableColumnEntitys = this.tableColumnDao.findByTableIdAndIsPrimaryKeyTrue(tableId);
        return this.tableColumnMapper.toDto(tableColumnEntitys);
    }

    @Override
    public TableColumnDto getDotById(String id) {
        TableColumnEntity tableColumnEntity = this.getById(id);
        return this.tableColumnMapper.toDto(tableColumnEntity);
    }

}
