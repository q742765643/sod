package com.piesat.dm.rpc.service.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.entity.datatable.ShardingEntity;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.dto.datatable.ShardingDto;
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
public class ShardingServiceImpl extends BaseService<ShardingEntity> implements ShardingService {
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private ShardingMapper shardingMapper;

    @Override
    public BaseDao<ShardingEntity> getBaseDao() {
        return shardingDao;
    }

    @Override
    public ShardingDto saveDto(ShardingDto shardingDto) {
        ShardingEntity shardingEntity = this.shardingMapper.toEntity(shardingDto);
        shardingEntity = this.saveNotNull(shardingEntity);
        return this.shardingMapper.toDto(shardingEntity);
    }

    @Override
    @Transactional
    public List<ShardingDto> saveDto(List<ShardingDto> shardingDto) {
        List<ShardingEntity> shardingEntity = this.shardingMapper.toEntity(shardingDto);
        shardingEntity = this.saveNotNull(shardingEntity);
        return this.shardingMapper.toDto(shardingEntity);
    }

    @Override
    public List<ShardingDto> all() {
        List<ShardingEntity> all = this.getAll();
        return this.shardingMapper.toDto(all);
    }

    @Override
    public ShardingDto getDotById(String id) {
        ShardingEntity shardingEntity = this.getById(id);
        return this.shardingMapper.toDto(shardingEntity);
    }

    @Override
    public List<ShardingDto> getDotByTableId(String id) {
        List<ShardingEntity> all = this.shardingDao.findByTableId(id);
        return this.shardingMapper.toDto(all);
    }
}
