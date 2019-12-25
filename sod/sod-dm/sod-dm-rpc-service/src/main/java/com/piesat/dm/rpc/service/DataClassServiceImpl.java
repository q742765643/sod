package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataClassDao;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.entity.DataClassEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.mapper.DataClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:31:15
 */
@Service
public class DataClassServiceImpl extends BaseService<DataClassEntity> implements DataClassService {
    @Autowired
    private DataClassDao dataClassDao;
    @Autowired
    private DataClassMapper dataClassMapper;
    @Autowired
    private DatabaseDao databaseDao;

    @Override
    public BaseDao<DataClassEntity> getBaseDao() {
        return dataClassDao;
    }

    @Override
    public DataClassDto saveDto(DataClassDto dataClassDto) {
        DataClassEntity dataClassEntity = this.dataClassMapper.toEntity(dataClassDto);
        dataClassEntity = this.save(dataClassEntity);
        return this.dataClassMapper.toDto(dataClassEntity);
    }

    @Override
    public List<DataClassDto> all() {
        List<DataClassEntity> all = this.getAll();
        return this.dataClassMapper.toDto(all);
    }

    @Override
    public List<Map<String, Object>> getLogicClass() {
        String sql = "SELECT LOGIC_NAME \"name\",CONCAT('0-',LOGIC_FLAG) \"id\",'0' \"pId\",'true' \"isParent\",'0' \"metaDataId\" FROM  T_SOD_LOGIC_DEFINE " +
                "UNION " +
                "SELECT CLASS_NAME \"name\",CONCAT(DATA_CLASS_ID,CONCAT('-',LOGIC_FLAG)) \"id\",CONCAT(PARENT_ID,CONCAT('-',LOGIC_FLAG)) \"pId\"," +
                "'true' \"isParent\",'0' \"metaDataId\" FROM T_SOD_DATA_CLASS A INNER JOIN T_SOD_DATA_LOGIC B ON A.DATA_CLASS_ID = B.DATA_CLASS_ID WHERE TYPE = 1" +
                "UNION " +
                "SELECT CLASS_NAME \"name\",DATA_CLASS_ID \"id\",CONCAT(PARENT_ID,CONCAT('-',LOGIC_FLAG)) \"pId\"," +
                "'true' \"isParent\",'0' \"metaDataId\" FROM T_SOD_DATA_CLASS A INNER JOIN T_SOD_DATA_LOGIC B ON A.DATA_CLASS_ID = B.DATA_CLASS_ID WHERE TYPE = 2";
        List<Map<String, Object>> maps = this.queryByNativeSQL(sql);
        return maps;
    }

    @Override
    public List<Map<String, Object>> getDatabaseClass() {
        List<DatabaseEntity> databaseList = this.databaseDao.findAll();
        String sql =
                "SELECT * FROM (SELECT CONCAT(ID,'-P') id,'999' pId,DATABASE_NAME name,'' DATA_CLASS_ID,false isHidden,true isParent FROM " +
                        "T_SOD_DATABASE_DEFINE WHERE USER_DISPLAY_CONTROL = 1 ORDER BY ID)" +
                        "UNION " +
                        "SELECT * FROM (SELECT ID id,CONCAT(DATABASE_DEFINE_ID,'-P') pId," +
                        "CONCAT(CONCAT(CONCAT(DATABASE_NAME,'('),SCHEMA_NAME),')') name,'' DATA_CLASS_ID,false isHidden,true isParent FROM " +
                        "T_SOD_DATABASE WHERE STOP_USE = false ORDER BY ID)";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        for (DatabaseEntity db : databaseList) {
            if (!db.getStopUse()) {
                sql = "SELECT DISTINCT CASE TYPE WHEN 2 THEN DATA_CLASS_ID ELSE CONCAT(DATA_CLASS_ID, '"+db.getId()+"') END id,CLASS_NAME name," +
                        "CONCAT(CASE PARENT_ID WHEN 0 THEN '' ELSE PARENT_ID END, '"+db.getId()+"') pId," +
                        "DATA_CLASS_ID,CASE TYPE WHEN 2 THEN TRUE ELSE FALSE END isHidden, " +
                        "CASE TYPE WHEN 1 THEN TRUE ELSE FALSE END isParent " +
                        "FROM T_SOD_DATA_CLASS " +
                        "START WITH DATA_CLASS_ID IN (SELECT DISTINCT DATA_CLASS_ID FROM T_SOD_DATA_LOGIC WHERE DATABASE_ID = '"+db.getId()+"') " +
                        "CONNECT BY PRIOR PARENT_ID = DATA_CLASS_ID ORDER BY id ";
                List<Map<String, Object>> dataList = this.queryByNativeSQL(sql);
                list.addAll(dataList);
            }
        }
        return list;
    }

    @Override
    public DataClassDto getDotById(String id) {
        DataClassEntity dataClassEntity = this.getById(id);
        return this.dataClassMapper.toDto(dataClassEntity);
    }
}
