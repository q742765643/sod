package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.DataTableDao;
import com.piesat.dm.dao.DatabaseDao;
import com.piesat.dm.dao.ShardingDao;
import com.piesat.dm.entity.DataTableEntity;
import com.piesat.dm.entity.DatabaseEntity;
import com.piesat.dm.entity.ShardingEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.dto.SampleData;
import com.piesat.dm.rpc.mapper.DataTableMapper;
import com.piesat.dm.rpc.mapper.DatabaseMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月22日 16:34:17
 */
@Service
public class DataTableServiceImpl extends BaseService<DataTableEntity> implements DataTableService {
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DataTableMapper dataTableMapper;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DatabaseSqlService databaseSqlService;
    @Autowired
    private DatabaseInfo databaseInfo;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Override
    public BaseDao<DataTableEntity> getBaseDao() {
        return dataTableDao;
    }

    @Override
    public DataTableDto saveDto(DataTableDto dataTableDto) {
        DataTableEntity dataTableEntity = this.dataTableMapper.toEntity(dataTableDto);
        dataTableEntity = this.save(dataTableEntity);
        return this.dataTableMapper.toDto(dataTableEntity);
    }

    @Override
    public List<DataTableDto> all() {
        List<DataTableEntity> all = this.getAll();
        return this.dataTableMapper.toDto(all);
    }

    @Override
    public DataTableDto getDotById(String id) {
        DataTableEntity dataTableEntity = this.getById(id);
        return this.dataTableMapper.toDto(dataTableEntity);
    }

    @Override
    public List<DataTableDto> getByDatabaseIdAndClassId(String databaseId, String dataClassId) {
        List<DataTableEntity> tableEntities = this.dataTableDao.getByDatabaseIdAndClassId(databaseId, dataClassId);
        return this.dataTableMapper.toDto(tableEntities);
    }

    @Override
    public List<Map<String, Object>> getByDatabaseId(String databaseId) {
        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(this.dataTableDao.getByDatabaseId(databaseId));
        String sql = "select A.* ,B.storage_type from T_SOD_DATA_TABLE A,T_SOD_DATA_LOGIC B where A.class_logic_id=B.id and B.database_id ='"+databaseId+"'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public List<DataTableDto> getByClassLogicId(String classLogic) {
        List<DataTableEntity> tableEntities = this.dataTableDao.findByClassLogicId(classLogic);
        return this.dataTableMapper.toDto(tableEntities);
    }

    @Override
    public int updateById(DataTableDto dataTableDto) {
        return dataTableDao.updateById(dataTableDto.getTableName(), dataTableDto.getId());
    }
    public Map<String, String> getSql(String tableId, String databaseId) {
        List<ShardingEntity> shardingEntities = this.shardingDao.findByTableId(tableId);
        DataTableDto dataTableDto = this.getDotById(tableId);
        Optional<DatabaseEntity> databaseEntity = this.databaseDao.findById(databaseId);
        DatabaseEntity database = databaseEntity.get();
        String databaseType = database.getDatabaseDefine().getDatabaseType();
        Map<String, String> map = new HashMap<>();
        if (this.databaseInfo.getXugu().equals(databaseType)){
//            this.databaseSqlService.getXuGuCreateSql()
        }else if (this.databaseInfo.getGbase8a().equals(databaseType)){

        }else if (this.databaseInfo.getCassandra().equals(databaseType)){

        }

        return map;
    }

    @Override
    public ResultT getSampleData(SampleData sampleData) throws Exception {
        DatabaseEntity databaseEntity = this.databaseDao.findById(sampleData.getDatabaseId()).get();
        DatabaseDto databaseDto = databaseMapper.toDto(databaseEntity);
        DatabaseDcl database = DatabaseUtil.getDatabase(databaseDto, databaseInfo);
        return database.queryData(databaseDto.getSchemaName(), sampleData.getTableName(), sampleData.getColumn(), 10);
    }

    @Override
    public List<Map<String, Object>> getByDatabaseIdAndTableName(String databaseId, String tableName) {
       return mybatisQueryMapper.getByDatabaseIdAndTableName(databaseId,tableName);
    }
}
