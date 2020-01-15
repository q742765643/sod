package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.codedom.CodeDOM;
import com.piesat.dm.dao.DataLogicDao;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.dao.DatabaseDefineDao;
import com.piesat.dm.entity.DatabaseDefineEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.mapper.DatabaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DataLogicDao dataLogicDao;

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
        String sql = "select t.id ID,concat(d.database_name,'_',t.database_name) DATABASE_NAME  from T_SOD_DATABASE t left join T_SOD_DATABASE_DEFINE d on t.DATABASE_DEFINE_ID = d.id";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public DatabaseDto getDotById(String id) {
        DatabaseEntity databaseEntity = this.getById(id);
        return this.databaseMapper.toDto(databaseEntity);
    }

    public void importData() {
        String sql = "select * from DMIN_DB_PHYSICS_DEFINE order by DATABASE_CLASSIFY desc";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String database_id = toString(m.get("DATABASE_ID"));
            String database_name = toString(m.get("DATABASE_NAME"));
            String serial_number = toString(m.get("SERIAL_NUMBER"));
            String database_instance = toString(m.get("DATABASE_INSTANCE"));
            String database_type = toString(m.get("DATABASE_TYPE"));
            String database_capacity = toString(m.get("DATABASE_CAPACITY"));
            String create_time = toString(m.get("CREATE_TIME"));
            String driver_class_name = toString(m.get("DRIVER_CLASS_NAME"));
            String display_control = toString(m.get("DISPLAY_CONTROL"));
            String mainbak_type = toString(m.get("MAINBAK_TYPE"));
            String check_conn = toString(m.get("CHECK_CONN"));
            String special_database_name = toString(m.get("SPECIAL_DATABASE_NAME"));
            String database_schema_name = toString(m.get("DATABASE_SCHEMA_NAME"));
            String parent_id = toString(m.get("PARENT_ID"));
            String database_classify = toString(m.get("DATABASE_CLASSIFY"));
            String user_display_control = toString(m.get("USER_DISPLAY_CONTROL"));
            String tdb_id = toString(m.get("TDB_ID"));
            DatabaseEntity de = new DatabaseEntity();
            DatabaseDefineEntity dd = new DatabaseDefineEntity();
            if (database_id.equals(parent_id)){
                sql = "select * from DMIN_DB_PHYSICS_CONNECTION where DATABASE_ID = '"+database_id+"'";
                Map map = CodeDOM.getList(sql).get(0);
                dd.setId(database_id);
                if (StringUtils.isNotEmpty(database_capacity))dd.setDatabaseCapacity(Integer.parseInt(database_capacity));
                dd.setDatabaseInstance(database_instance);
                dd.setDatabaseIp(toString(map.get("DATABASE_IP")));
                dd.setDatabasePort(toString(map.get("DATABASE_PORT")));
                dd.setDatabaseUrl(toString(map.get("DATABASE_URL")));
                dd.setUpUrl(toString(map.get("UP_URL")));
                dd.setDriverClassName(driver_class_name);
                dd.setDatabaseType(database_type);
                dd.setDatabaseName(database_name);
                dd.setCheckConn(Integer.parseInt(check_conn));
                dd.setMainBakType(Integer.parseInt(mainbak_type));
                dd.setUserDisplayControl(Integer.parseInt(user_display_control));
                dd.setSerialNumber(Integer.parseInt(serial_number));
                dd.setCreateTime(new Date());
                this.databaseDefineDao.save(dd);
            }
            DatabaseDefineEntity one = this.databaseDefineDao.getOne(parent_id);
            de.setTdbId(tdb_id);
            de.setDatabaseName(database_name);
            de.setDatabaseClassify(database_classify);
            de.setSchemaName(database_schema_name);
            de.setDatabaseName(special_database_name);
            de.setDatabaseDefine(one);
            de.setStopUse(false);
            de.setCreateTime(new Date());
            DatabaseEntity save = save(de);
            dataLogicDao.updateDatabaseId(save.getId(),database_id);
        }
    }

    public String toString(Object o){
        return o == null ? "":o.toString();
    }
}
