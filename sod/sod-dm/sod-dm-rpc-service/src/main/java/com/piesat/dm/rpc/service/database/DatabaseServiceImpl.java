package com.piesat.dm.rpc.service.database;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.mapper.database.DatabaseMapper;
import net.sf.saxon.expr.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Override
    public BaseDao<DatabaseEntity> getBaseDao() {
        return databaseDao;
    }

    @Override
    public DatabaseDto saveDto(DatabaseDto databaseDto) {
        DatabaseEntity databaseEntity = this.databaseMapper.toEntity(databaseDto);
        databaseEntity = this.saveNotNull(databaseEntity);
        return this.databaseMapper.toDto(databaseEntity);
    }

    @Override
    public List<DatabaseDto> all() {
        List<DatabaseEntity> all = this.getAll();
        return this.databaseMapper.toDto(all);
    }

    @Override
    public List<Map<String,Object>> getDatabaseName() {
       // String sql = "select t.id ID,concat(concat(d.database_name,'_'),t.database_name) DATABASE_NAME,d.database_type DATABASE_TYPE  from T_SOD_DATABASE t inner join T_SOD_DATABASE_DEFINE d on t.DATABASE_DEFINE_ID = d.id and d.user_display_control != '2'";
        List<Map<String, Object>> list = mybatisQueryMapper.getDatabaseName();
        return list;
    }

    @Override
    public List<Map<String, Object>> getByDatabaseDefineId(String id) {
        //String sql = "select *  from T_SOD_DATABASE t where t.DATABASE_DEFINE_ID = '"+id+"'";
        List<Map<String, Object>> list = mybatisQueryMapper.getByDatabaseDefineId(id);
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
    public List<DatabaseDto> findByDatabaseClassifyAndDatabaseDefineIdIn(String databaseClassify, List<String> databaseDefineIds) {
        List<DatabaseEntity> databaseEntityList = this.databaseDao.findByDatabaseClassifyAndDatabaseDefineIdIn(databaseClassify,databaseDefineIds);
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
        //闲时优化
        if(databaseEntities != null && databaseEntities.size()>0){
            for(int i=databaseEntities.size()-1;i>-1;i--){
                if(databaseEntities.get(i).getDatabaseDefine().getUserDisplayControl().intValue() != 1){
                    databaseEntities.remove(databaseEntities.get(i));
                }
            }
        }
        return this.databaseMapper.toDto(databaseEntities);
    }

    @Override
    public List<Map<String, Object>> findByUserIdAndDatabaseDefineId(String userId, String databaseDefineId) {
        //由于框架bug导致带union的sql报错，暂时分开写
        List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> byUserIdAndDatabaseDefineId1 = this.mybatisQueryMapper.findByUserIdAndDatabaseDefineId1(userId, databaseDefineId);
        if(byUserIdAndDatabaseDefineId1 != null){
            resultList.addAll(byUserIdAndDatabaseDefineId1);
        }
        List<Map<String, Object>> byUserIdAndDatabaseDefineId2 = this.mybatisQueryMapper.findByUserIdAndDatabaseDefineId2(userId, databaseDefineId);
        if(byUserIdAndDatabaseDefineId2 != null){
            resultList.addAll(byUserIdAndDatabaseDefineId2);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getDatabaseList(String ifDisplay) {
        String sql = "select t.id ID,concat(concat(d.database_name,'_'),t.database_name) DATABASE_NAME  from T_SOD_DATABASE t, T_SOD_DATABASE_DEFINE d WHERE t.DATABASE_DEFINE_ID = d.id AND d.user_display_control in("+ifDisplay+")";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public DatabaseDto getDotById(String id) {
        DatabaseEntity databaseEntity = this.getById(id);
        return this.databaseMapper.toDto(databaseEntity);
    }

}
