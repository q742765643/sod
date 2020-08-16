package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.model.Column;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.datatable.TableColumnDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import com.piesat.dm.rpc.mapper.datatable.TableColumnMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private TableColumnMapper tableColumnMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Override
    public BaseDao<TableColumnEntity> getBaseDao() {
        return tableColumnDao;
    }

    @Override
    @Transactional
    public TableColumnDto saveDto(TableColumnDto tableColumnDto) throws Exception {
        String id = tableColumnDto.getId();

        TableColumnEntity tableColumnEntity = this.tableColumnMapper.toEntity(tableColumnDto);
        tableColumnEntity = this.saveNotNull(tableColumnEntity);
        if (tableColumnDto.getUpdateDatabase() != null && tableColumnDto.getUpdateDatabase()) {
            Column oldColumn = null;
            if (StringUtils.isNotEmpty(id)) {
                TableColumnEntity byId = this.getById(id);
                oldColumn.setDef(byId.getDefaultValue());
                oldColumn.setIsNull(byId.getIsNull());
                oldColumn.setName(byId.getEleName());
                oldColumn.setPrecision(byId.getLength() + "," + byId.getAccuracy());
                oldColumn.setType(byId.getType());
            }
            Column newColumn = null;
            newColumn.setDef(tableColumnEntity.getDefaultValue());
            newColumn.setIsNull(tableColumnEntity.getIsNull());
            newColumn.setName(tableColumnEntity.getEleName());
            newColumn.setPrecision(tableColumnEntity.getLength() + "," + tableColumnEntity.getAccuracy());
            newColumn.setType(tableColumnEntity.getType());
            DataTableDto datatable = dataTableService.getDotById(tableColumnDto.getTableId());
            String databaseId = datatable.getClassLogic().getDatabaseId();
            DatabaseEntity databaseEntity = this.databaseDao.findById(databaseId).get();
            DatabaseDto databaseDto = this.databaseMapper.toDto(databaseEntity);
            DatabaseDcl database = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
            try {
                database.updateColumn(databaseDto.getSchemaName(), datatable.getTableName(), oldColumn, newColumn);
                database.closeConnect();
            } catch (Exception e) {
            } finally {
                if (database != null) {
                    database.closeConnect();
                }
            }

        }
        return this.tableColumnMapper.toDto(tableColumnEntity);
    }

    @Override
    @Transactional
    public List<TableColumnDto> saveDtoList(List<TableColumnDto> tableColumnDtoList) {
        return tableColumnDtoList.stream().map(e -> {
            TableColumnEntity tableColumnEntity = this.tableColumnMapper.toEntity(e);
            if (StringUtils.isEmpty(tableColumnEntity.getId())) {
                tableColumnEntity.setVersion(0);
                tableColumnEntity.setCreateTime(new Date());
                tableColumnEntity.setId(null);
            }
            tableColumnEntity = this.saveNotNull(tableColumnEntity);
            return this.tableColumnMapper.toDto(tableColumnEntity);
        }).collect(Collectors.toList());
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
    public Integer findMaxNum(String tableId) {
        TableColumnEntity tableColumn = this.tableColumnDao.findFirstByTableIdOrderBySerialNumberDesc(tableId);
        return Optional.ofNullable(tableColumn).map(TableColumnEntity::getSerialNumber).orElse(0);
    }

    @Override
    public TableColumnDto getDotById(String id) {
        TableColumnEntity tableColumnEntity = this.getById(id);
        return this.tableColumnMapper.toDto(tableColumnEntity);
    }



}
