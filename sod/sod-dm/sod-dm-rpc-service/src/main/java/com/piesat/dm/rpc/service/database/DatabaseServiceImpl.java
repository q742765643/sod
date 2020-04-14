package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月22日 16:27:41
 */
@Service
public class DatabaseServiceImpl extends BaseService<DatabaseEntity> implements DatabaseService {
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseMapper databaseMapper;

    @Override
    public BaseDao<DatabaseEntity> getBaseDao() {
        return databaseDao;
    }

    @Override
    public DatabaseDto saveDto(DatabaseDto databaseDto) {
        DatabaseEntity databaseEntity = this.databaseMapper.toEntity(databaseDto);
        databaseEntity = this.save(databaseEntity);
        return this.databaseMapper.toDto(databaseEntity);
    }

    @Override
    public List<DatabaseDto> all() {
        List<DatabaseEntity> all = this.getAll();
        return this.databaseMapper.toDto(all);
    }

    @Override
    public List<Map<String,Object>> getDatabaseName() {
        String sql = "select t.id ID,concat(concat(d.database_name,'_'),t.database_name) DATABASE_NAME  from T_SOD_DATABASE t left join T_SOD_DATABASE_DEFINE d on t.DATABASE_DEFINE_ID = d.id";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public List<Map<String, Object>> getByDatabaseDefineId(String id) {
        String sql = "select *  from T_SOD_DATABASE t where t.DATABASE_DEFINE_ID = '"+id+"'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public List<DatabaseDto> findByLevel(int level) {
        List<DatabaseEntity> all = this.databaseDao.findByLevel(level);
        return this.databaseMapper.toDto(all);
    }

    @Override
    public List<DatabaseDto> findByDatabaseClassifyAndIdIn(String databaseClassify,List<String> ids) {
        List<DatabaseEntity> databaseEntityList = this.databaseDao.findByDatabaseClassifyAndIdIn(databaseClassify,ids);
        return this.databaseMapper.toDto(databaseEntityList);
    }

    @Override
    public List<DatabaseDto> findByDatabaseDefineId(String id) {
        List<DatabaseEntity> databaseEntityList = this.databaseDao.findByDatabaseDefine_Id(id);
        return this.databaseMapper.toDto(databaseEntityList);
    }

    @Override
    public List<DatabaseDto> findByDatabaseClassify(String databaseClassify) {
        List<DatabaseEntity> databaseEntities = this.databaseDao.findByDatabaseClassify(databaseClassify);
        return this.databaseMapper.toDto(databaseEntities);
    }

    @Override
    public DatabaseDto getDotById(String id) {
        DatabaseEntity databaseEntity = this.getById(id);
        return this.databaseMapper.toDto(databaseEntity);
    }

}
