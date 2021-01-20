package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.dao.ReviewLogDao;
import com.piesat.dm.dao.dataclass.DataClassApplyDao;
import com.piesat.dm.entity.dataclass.DataClassApplyEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.dataclass.DataClassApplyService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableColumnService;
import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassApplyDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.dm.rpc.mapper.dataclass.DataClassApplyMapper;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private TableIndexService tableIndexService;
    @Autowired
    private ShardingService shardingService;
    @Autowired
    private ReviewLogService reviewLogService;


    @Override
    public BaseDao<DataClassApplyEntity> getBaseDao() {
        return dataClassApplyDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataClassApplyDto saveDto(DataClassApplyDto dataClassApplyDto) {
        List<TableColumnDto> tableColumns = dataClassApplyDto.getTableColumns();
        List<TableIndexDto> tableIndexList = dataClassApplyDto.getTableIndexList();
        TablePartDto tablePart = dataClassApplyDto.getTablePart();
        DataClassApplyEntity save = this.save(this.dataClassApplyMapper.toEntity(dataClassApplyDto));
        DataClassApplyDto dataClassApply = this.dataClassApplyMapper.toDto(save);
        Optional.ofNullable(tableColumns).ifPresent(e -> {
            e.forEach(e1 -> {
                e1.setTableId(save.getId());
            });
            List<TableColumnDto> tableColumnDtos = this.tableColumnService.saveDtoList(e);
            dataClassApply.setTableColumns(tableColumnDtos);
        });
        Optional.ofNullable(tableIndexList).ifPresent(e -> {
            e.forEach(e1 -> {
                e1.setTableId(save.getId());
                this.tableIndexService.saveDto(e1);
            });
        });
        Optional.ofNullable(tablePart).ifPresent(e -> {
            this.shardingService.saveDto(e);
        });
        ReviewLogDto rl = new ReviewLogDto();
        rl.setBindId(save.getId());
        rl.setUserId(save.getUserId());
        rl.setStatusInfo(ConstantsMsg.STATUS1);
        this.reviewLogService.saveDto(rl);
        return dataClassApply;
    }

    @Override
    public DataClassApplyDto getDotById(String id) {
        DataClassApplyDto dataClassApplyDto = this.dataClassApplyMapper.toDto(this.getById(id));
        List<TableColumnDto> tableColumn = this.tableColumnService.findByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTableColumns(tableColumn);
        List<TableIndexDto> tableIndex = this.tableIndexService.findByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTableIndexList(tableIndex);
        TablePartDto tablePartDto = this.shardingService.getDotByTableId(dataClassApplyDto.getId());
        dataClassApplyDto.setTablePart(tablePartDto);
        return dataClassApplyDto;
    }

    @Override
    public List<DataClassApplyDto> all() {
        return this.dataClassApplyMapper.toDto(this.getAll());
    }

}
