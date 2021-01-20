package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.entity.datatable.TablePartEntity;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.dm.rpc.mapper.datatable.ShardingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 12月09日 14:07:13
 */
@Service
public class ShardingServiceImpl extends BaseService<TablePartEntity> implements ShardingService {
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private ShardingMapper shardingMapper;

    @Override
    public BaseDao<TablePartEntity> getBaseDao() {
        return shardingDao;
    }

    @Override
    public TablePartDto saveDto(TablePartDto tablePartDto) {
        TablePartEntity tablePartEntity = this.shardingMapper.toEntity(tablePartDto);
        tablePartEntity = this.saveNotNull(tablePartEntity);
        return this.shardingMapper.toDto(tablePartEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TablePartDto> saveDto(List<TablePartDto> tablePartDto) {
        List<TablePartEntity> tablePartEntity = this.shardingMapper.toEntity(tablePartDto);
        tablePartEntity = this.saveNotNull(tablePartEntity);
        return this.shardingMapper.toDto(tablePartEntity);
    }

    @Override
    public List<TablePartDto> all() {
        List<TablePartEntity> all = this.getAll();
        return this.shardingMapper.toDto(all);
    }

    @Override
    public TablePartDto getDotById(String id) {
        TablePartEntity tablePartEntity = this.getById(id);
        return this.shardingMapper.toDto(tablePartEntity);
    }

    @Override
    public TablePartDto getDotByTableId(String id) {
        TablePartEntity all = this.shardingDao.findById(id).orElse(null);
        return this.shardingMapper.toDto(all);
    }
}
