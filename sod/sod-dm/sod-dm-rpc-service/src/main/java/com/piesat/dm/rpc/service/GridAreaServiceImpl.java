package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.GridAreaDao;
import com.piesat.dm.entity.GridAreaEntity;
import com.piesat.dm.rpc.api.GridAreaService;
import com.piesat.dm.rpc.dto.GridAreaDto;
import com.piesat.dm.rpc.mapper.GridAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:36:52
 */
@Service
public class GridAreaServiceImpl extends BaseService<GridAreaEntity> implements GridAreaService {

    @Autowired
    private GridAreaDao gridAreaDao;
    @Autowired
    private GridAreaMapper gridAreaMapper;

    @Override
    public BaseDao<GridAreaEntity> getBaseDao() {
        return this.gridAreaDao;
    }
    @Override
    public GridAreaDto saveDto(GridAreaDto gridAreaDto) {
        GridAreaEntity gridAreaEntity = this.gridAreaMapper.toEntity(gridAreaDto);
        gridAreaEntity = this.save(gridAreaEntity);
        return this.gridAreaMapper.toDto(gridAreaEntity);
    }

    @Override
    public GridAreaDto getDotById(String id) {
        GridAreaEntity gridAreaEntity = this.getById(id);
        return this.gridAreaMapper.toDto(gridAreaEntity);
    }

    @Override
    public List<GridAreaDto> all() {
        List<GridAreaEntity> all = this.getAll();
        return this.gridAreaMapper.toDto(all);
    }

}
