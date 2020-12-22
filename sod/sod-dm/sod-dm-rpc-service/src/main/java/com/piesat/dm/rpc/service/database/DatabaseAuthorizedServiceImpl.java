package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DatabaseAuthorizedDao;
import com.piesat.dm.entity.database.DatabaseAuthorizedEntity;
import com.piesat.dm.rpc.api.database.DatabaseAuthorizedService;
import com.piesat.dm.rpc.dto.database.DatabaseAuthorizedDto;
import com.piesat.dm.rpc.mapper.database.DatabaseAuthorizedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月21日 14:17:06
 */
@Service
public class DatabaseAuthorizedServiceImpl extends BaseService<DatabaseAuthorizedEntity> implements DatabaseAuthorizedService {

    @Autowired
    private DatabaseAuthorizedDao databaseAuthorizedDao;

    @Autowired
    private DatabaseAuthorizedMapper databaseAuthorizedMapper;

    @Override
    public BaseDao<DatabaseAuthorizedEntity> getBaseDao() {
        return this.databaseAuthorizedDao;
    }

    @Override
    public DatabaseAuthorizedDto saveDto(DatabaseAuthorizedDto databaseAuthorizedDto) {
        DatabaseAuthorizedEntity save = this.save(this.databaseAuthorizedMapper.toEntity(databaseAuthorizedDto));
        return this.databaseAuthorizedMapper.toDto(save);
    }

    @Override
    public DatabaseAuthorizedDto getDotById(String id) {
        return this.databaseAuthorizedMapper.toDto(this.getById(id));
    }

    @Override
    public List<DatabaseAuthorizedDto> all() {
        return this.databaseAuthorizedMapper.toDto(this.getAll());
    }

    @Override
    public List<DatabaseAuthorizedDto> findByDatabaseUsername(String username) {
        return this.databaseAuthorizedMapper.toDto(this.databaseAuthorizedDao.findByDatabaseUsername(username));
    }

    @Override
    public void delete(String username, String databaseId) {
        this.databaseAuthorizedDao.deleteByDatabaseUsernameAndDatabaseId(username, databaseId);
    }

    @Override
    public DatabaseAuthorizedDto findByDatabaseUsernameAndDatabaseId(String username, String databaseId) {
        List<DatabaseAuthorizedEntity> r = this.databaseAuthorizedDao.findByDatabaseUsernameAndDatabaseId(username, databaseId);
        if (r != null && r.size() > 0) {
            return this.databaseAuthorizedMapper.toDto(r.get(0));
        } else {
            return null;
        }
    }
}
