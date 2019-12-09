package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.LogicDefineDao;
import com.piesat.dm.entity.LogicDefineEntity;
import com.piesat.dm.rpc.api.LogicDefineService;
import com.piesat.dm.rpc.dto.LogicDefineDto;
import com.piesat.dm.rpc.mapper.LogicDefineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月22日 16:36:19
 */
@Service
public class LogicDefineServiceImpl extends BaseService<LogicDefineEntity> implements LogicDefineService {
    @Autowired
    private LogicDefineDao logicDefineDao;
    @Autowired
    private LogicDefineMapper logicDefineMapper;

    @Override
    public BaseDao<LogicDefineEntity> getBaseDao() {
        return logicDefineDao;
    }

    @Override
    public LogicDefineDto saveDto(LogicDefineDto logicDefineDto) {
        logicDefineDto.setCreateTime(new Date());
        LogicDefineEntity logicDefineEntity = this.logicDefineMapper.toEntity(logicDefineDto);
        logicDefineEntity = this.logicDefineDao.save(logicDefineEntity);
        return this.logicDefineMapper.toDto(logicDefineEntity);
    }

    @Override
    public List<LogicDefineDto> all() {
        List<LogicDefineEntity> all = this.getAll();
        return this.logicDefineMapper.toDto(all);
    }

    @Override
    public LogicDefineDto getDotById(String id) {
        LogicDefineEntity logicDefineEntity = this.getById(id);
        return this.logicDefineMapper.toDto(logicDefineEntity);
    }

    @Override
    public List<LogicDefineDto> getAllLogicDefine() {
        List<LogicDefineEntity> logicDefineDaoAll = this.logicDefineDao.findAll();
        return this.logicDefineMapper.toDto(logicDefineDaoAll);
    }
}
