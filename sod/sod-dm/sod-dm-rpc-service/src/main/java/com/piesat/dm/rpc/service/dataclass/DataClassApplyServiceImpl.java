package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataclass.DataClassApplyDao;
import com.piesat.dm.entity.dataclass.DataClassApplyEntity;
import com.piesat.dm.rpc.api.dataclass.DataClassApplyService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassApplyServiceImpl extends BaseService<DataClassApplyEntity> implements DataClassApplyService {
    @Autowired
    private DataClassApplyDao dataClassApplyDao;
    @Autowired
    private DataClassApplyMapper dataClassApplyMapper;
    @Autowired
    private TableColumnService tableColumnService;


    @Override
    public BaseDao<DataClassApplyEntity> getBaseDao() {
        return dataClassApplyDao;
    }

    @Override
    public DataClassApplyDto saveDto(DataClassApplyDto dataClassApplyDto) {
        List<TableColumnDto> tableColumns = dataClassApplyDto.getTableColumns();
        DataClassApplyEntity save = this.save(this.dataClassApplyMapper.toEntity(dataClassApplyDto));
        tableColumns = tableColumns.stream()
                .map(e -> {
                    e.setTableId(save.getId());
                    return e; })
                .collect(Collectors.toList());
        this.tableColumnService.saveDtoList(tableColumns);
        return this.dataClassApplyMapper.toDto(save);
    }

    @Override
    public DataClassApplyDto getDotById(String id) {
        DataClassApplyDto dataClassApplyDto = this.dataClassApplyMapper.toDto(this.getById(id));
        List<TableColumnDto> tableColumn = this.tableColumnService.findByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTableColumns(tableColumn);
        return dataClassApplyDto;
    }

    @Override
    public List<DataClassApplyDto> all() {
        return this.dataClassApplyMapper.toDto(this.getAll());
    }
}
