package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.DataTableApplyDao;
import com.piesat.dm.entity.datatable.DataTableApplyEntity;
import com.piesat.dm.rpc.api.datatable.DataTableApplyService;
import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
import com.piesat.dm.rpc.mapper.datatable.DataTableApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表申请
 *
 * @author cwh
 * @date 2020年 07月31日 16:48:06
 */
@Service
public class DataTableApplyServiceImpl extends BaseService<DataTableApplyEntity> implements DataTableApplyService {
    @Autowired
    private DataTableApplyDao dataTableApplyDao;
    @Autowired
    private DataTableApplyMapper dataTableApplyMapper;

    @Override
    public BaseDao<DataTableApplyEntity> getBaseDao() {
        return this.dataTableApplyDao;
    }

    @Override
    public DataTableApplyDto saveDto(DataTableApplyDto dataTableApplyDto) {
        DataTableApplyEntity dataTableApplyEntity = this.dataTableApplyMapper.toEntity(dataTableApplyDto);
        DataTableApplyEntity save = this.save(dataTableApplyEntity);
        return this.dataTableApplyMapper.toDto(save);
    }

    @Override
    public DataTableApplyDto getDotById(String id) {
        DataTableApplyEntity byId = this.getById(id);
        return this.dataTableApplyMapper.toDto(byId);
    }

    @Override
    public List<DataTableApplyDto> all() {
        List<DataTableApplyEntity> all = this.getAll();
        return this.dataTableApplyMapper.toDto(all);
    }

}
