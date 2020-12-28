package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.entity.datatable.PartingEntity;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.dto.datatable.PartingDto;
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
public class ShardingServiceImpl extends BaseService<PartingEntity> implements ShardingService {
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private ShardingMapper shardingMapper;

    @Override
    public BaseDao<PartingEntity> getBaseDao() {
        return shardingDao;
    }

    @Override
    public PartingDto saveDto(PartingDto partingDto) {
        PartingEntity partingEntity = this.shardingMapper.toEntity(partingDto);
        partingEntity = this.saveNotNull(partingEntity);
        return this.shardingMapper.toDto(partingEntity);
    }

    @Override
    @Transactional
    public List<PartingDto> saveDto(List<PartingDto> partingDto) {
        List<PartingEntity> partingEntity = this.shardingMapper.toEntity(partingDto);
        partingEntity = this.saveNotNull(partingEntity);
        return this.shardingMapper.toDto(partingEntity);
    }

    @Override
    public List<PartingDto> all() {
        List<PartingEntity> all = this.getAll();
        return this.shardingMapper.toDto(all);
    }

    @Override
    public PartingDto getDotById(String id) {
        PartingEntity partingEntity = this.getById(id);
        return this.shardingMapper.toDto(partingEntity);
    }

    @Override
    public PartingDto getDotByTableId(String id) {
        PartingEntity all = this.shardingDao.findById(id).orElse(null);
        return this.shardingMapper.toDto(all);
    }
}
