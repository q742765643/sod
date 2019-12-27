package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.DataLogicDao;
import com.piesat.dm.dao.DataTableDao;
import com.piesat.dm.dao.ShardingDao;
import com.piesat.dm.entity.*;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.mapper.DataLogicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月22日 16:32:48
 */
@Service
public class DataLogicServiceImpl extends BaseService<DataLogicEntity> implements DataLogicService {
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private DataLogicMapper dataLogicMapper;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ShardingDao shardingDao;

    @Override
    public BaseDao<DataLogicEntity> getBaseDao() {
        return dataLogicDao;
    }

    @Override
    public DataLogicDto saveDto(DataLogicDto dataLogicDto) {
        dataLogicDto.setCreateTime(new Date());
        DataLogicEntity dataLogicEntity = this.dataLogicMapper.toEntity(dataLogicDto);
        dataLogicEntity = this.dataLogicDao.save(dataLogicEntity);
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }

    @Override
    public List<DataLogicDto> all() {
        List<DataLogicEntity> all = this.getAll();
        return this.dataLogicMapper.toDto(all);
    }

    @Override
    public List<Map<String, Object>> getByDataClassIds(List<String> ids) {

        return null;
    }

    @Override
    public List<Map<String, Object>> getByDatabaseId(String databaseId) {
        String sql = "SELECT A.*,B.D_DATA_ID,B.CLASS_NAME,B.META_DATA_NAME FROM T_SOD_DATA_LOGIC A LEFT JOIN T_SOD_DATA_CLASS B ON A.DATA_CLASS_ID = B.DATA_CLASS_ID AND A.DATABASE_ID = '" + databaseId + "'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public DataLogicDto getDotById(String id) {
        DataLogicEntity dataLogicEntity = this.getById(id);
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }


    public void importData() {
        String sql = "select * from DMIN_DATA_CLASS_LOGIC a,DMIN_DATA_DL_PHYSICS b where a.CLASS_LOGIC_ID = b.DL_ID";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        for (Map<String, Object> m : list) {
            DataLogicEntity dc = new DataLogicEntity();
            String class_logic_id = toString(m.get("CLASS_LOGIC_ID"));
            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            dc.setDataClassId(data_class_id);
            String logic_id = toString(m.get("LOGIC_ID"));
            dc.setLogicFlag(logic_id);
            String storage_type = toString(m.get("STORAGE_TYPE"));
            dc.setStorageType(storage_type);
            String database_id = toString(m.get("DATABASE_ID"));
            dc.setDatabaseId(database_id);
            dc.setCreateTime(new Date());
            DataLogicEntity save = save(dc);
            importData(class_logic_id,save);
        }
    }

    public void importData(String oldId, DataLogicEntity newId) {
        String sql = "select * from DMIN_DATA_ID_TABLE where CLASS_LOGIC_ID = '"+oldId+"'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        for (Map<String, Object> m : list) {
            DataTableEntity dt = new DataTableEntity();
            String table_id = toString(m.get("TABLE_ID"));
            String data_service_id = toString(m.get("DATA_SERVICE_ID"));
            dt.setDataServiceId(data_service_id);
            String table_name = toString(m.get("TABLE_NAME"));
            dt.setTableName(table_name);
            String db_table_type = toString(m.get("DB_TABLE_TYPE"));
            dt.setDbTableType(db_table_type);
            String table_desc = toString(m.get("TABLE_DESC"));
            dt.setTableDesc(table_desc);
            String data_service_name = toString(m.get("DATA_SERVICE_NAME"));
            dt.setDataServiceName(data_service_name);
            String name_cn = toString(m.get("NAME_CN"));
            dt.setNameCn(name_cn);
            String creator = toString(m.get("CREATOR"));
            dt.setCreator(creator);
            String user_id = toString(m.get("USER_ID"));
            dt.setUserId(user_id);
            String class_logic_id = toString(m.get("CLASS_LOGIC_ID"));
            dt.setClassLogicId(newId);
            dt.setCreateTime(new Date());
            dt = this.dataTableDao.save(dt);
            sql = "select * from DMIN_DATA_TABLE_FIELD where TABLE_ID ='" + table_id + "'";
            List<Map<String, Object>> columns = this.queryByNativeSQL(sql);
            Set<TableColumnEntity> columnArr = new HashSet<>();
            for (Map<String, Object> columnMap : columns) {
                TableColumnEntity tc = new TableColumnEntity();
                String db_ele_code = toString(columnMap.get("DB_ELE_CODE"));
                tc.setDbEleCode(db_ele_code);
                String c_element_code = toString(columnMap.get("C_ELEMENT_CODE"));
                tc.setCElementCode(c_element_code);
                String is_manager = toString(columnMap.get("IS_MANAGER")).toLowerCase();
                tc.setIsManager("true".equals(is_manager)?true:false);
                String user_ele_code = toString(columnMap.get("USER_ELE_CODE"));
                tc.setUserEleCode(user_ele_code);
                String name_cn1 = toString(columnMap.get("NAME_CN"));
                tc.setNameCn(name_cn1);
                String ele_name = toString(columnMap.get("ELE_NAME"));
                tc.setEleName(ele_name);
                String type = toString(columnMap.get("TYPE"));
                tc.setType(type);
                if (columnMap.get("ACCURACY")!=null){
                    String accuracy = columnMap.get("ACCURACY").toString();
                    tc.setAccuracy(accuracy);
                }

                String length = toString(columnMap.get("LENGTH"));
                if (StringUtils.isNotEmpty(length))tc.setLength(Integer.parseInt(length));
                String unit = toString(columnMap.get("UNIT"));
                tc.setUnit(unit);
                String unit_cn = toString(columnMap.get("UNIT_CN"));
                tc.setUnitCn(unit_cn);
                String is_null = toString(columnMap.get("IS_NULL")).toLowerCase();
                tc.setIsNull("true".equals(is_null)?true:false);
                String is_update = toString(columnMap.get("IS_UPDATE")).toLowerCase();
                tc.setIsUpdate("true".equals(is_update)?true:false);
                String is_show = toString(columnMap.get("IS_SHOW")).toLowerCase();
                tc.setIsShow("true".equals(is_show)?true:false);
                String is_premary_key = toString(columnMap.get("IS_PREMARY_KEY")).toLowerCase();
                tc.setIsPrimaryKey("true".equals(is_premary_key)?true:false);
                String is_kv_k = toString(columnMap.get("IS_KV_K")).toLowerCase();
                tc.setIsKvK("true".equals(is_kv_k)?true:false);
                String serial_number = toString(columnMap.get("SERIAL_NUMBER"));
                if (StringUtils.isNotEmpty(serial_number))tc.setSerialNumber(Integer.parseInt(serial_number));
                tc.setCreateTime(new Date());
                tc.setTableId(dt.getId());
                columnArr.add(tc);
            }
            dt.setColumns(columnArr);
            dt = this.dataTableDao.save(dt);
            sql = "select * from DMIN_DB_TABLE_INDEX where TABLE_ID ='" + table_id + "'";
            List<Map<String, Object>> indexs = this.queryByNativeSQL(sql);
            Set<TableIndexEntity> indexArr = new HashSet<>();
            for (Map<String, Object> indexMap:indexs) {
                TableIndexEntity ti = new TableIndexEntity();
                String index_type = toString(indexMap.get("INDEX_TYPE"));
                ti.setIndexType(index_type);
                String index_name = toString(indexMap.get("INDEX_NAME"));
                ti.setIndexName(index_name);
                String index_column = toString(indexMap.get("INDEX_COLUMN"));
                ti.setIndexColumn(index_column);
                ti.setCreateTime(new Date());
                ti.setTableId(dt.getId());
                indexArr.add(ti);

            }
            dt.setTableIndexList(indexArr);
            dt = this.dataTableDao.save(dt);

            sql = "select * from DMIN_SHARDING where TABLE_ID ='" + table_id + "'";
            List<Map<String, Object>> shardings = this.queryByNativeSQL(sql);
            for (Map<String, Object> sMap:shardings) {
            ShardingEntity se = new ShardingEntity();
                se.setTableId(dt.getId());
                String field = toString(sMap.get("FIELD"));
                se.setColumnName(field);
                String sharding_type = toString(sMap.get("SHARDING_TYPE")).toUpperCase();
                se.setShardingType("T".equals(sharding_type)?1:0);
                se.setCreateTime(new Date());
                this.shardingDao.save(se);
            }

        }
    }

    public String toString(Object o){
       return o == null ? "":o.toString();
    }
}
