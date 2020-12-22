package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DbUserAlterLogDao;
import com.piesat.dm.entity.database.DbUserAlterLogEntity;
import com.piesat.dm.rpc.api.database.DbUserAlterLogService;
import com.piesat.dm.rpc.dto.database.DbUserAlterLogDto;
import com.piesat.dm.rpc.mapper.database.DbUserAlterLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cwh
 * @date 2020年 12月16日 15:22:31
 */
@Service
public class DbUserAlterLogServiceImpl extends BaseService<DbUserAlterLogEntity> implements DbUserAlterLogService {

    @Autowired
    private DbUserAlterLogDao dbUserAlterLogDao;
    @Autowired
    private DbUserAlterLogMapper dbUserAlterLogMapper;

    @Override
    public BaseDao<DbUserAlterLogEntity> getBaseDao() {
        return this.dbUserAlterLogDao;
    }

    @Override
    public DbUserAlterLogDto saveDto(DbUserAlterLogDto dbUserAlterLogDto) {
        DbUserAlterLogEntity dbUserAlterLogEntity = this.dbUserAlterLogMapper.toEntity(dbUserAlterLogDto);
        dbUserAlterLogEntity = this.saveNotNull(dbUserAlterLogEntity);
        return this.dbUserAlterLogMapper.toDto(dbUserAlterLogEntity);
    }

    @Override
    public DbUserAlterLogDto getDotById(String id) {
        DbUserAlterLogEntity byId = this.getById(id);
        return this.dbUserAlterLogMapper.toDto(byId);
    }

    @Override
    public List<DbUserAlterLogDto> all() {
        List<DbUserAlterLogEntity> all = this.getAll();
        return this.dbUserAlterLogMapper.toDto(all);
    }

    @Override
    public void deleteByAuthorizeId(String id) {
        this.dbUserAlterLogDao.deleteByAuthorizeId(id);
    }
}
