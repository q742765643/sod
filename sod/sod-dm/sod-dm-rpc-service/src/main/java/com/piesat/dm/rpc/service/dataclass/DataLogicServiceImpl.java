package com.piesat.dm.rpc.service.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.special.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.mapper.dataclass.DataLogicMapper;
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
    private StorageConfigurationService storageConfigurationService;
    @Autowired
    private DataLogicMapper dataLogicMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;

    @Override
    public BaseDao<DataLogicEntity> getBaseDao() {
        return dataLogicDao;
    }

    @Override
    public DataLogicDto saveDto(DataLogicDto dataLogicDto) {
        boolean isAdd = false;
        if (StringUtils.isEmpty(dataLogicDto.getId())) {
            isAdd = true;
        }
        dataLogicDto.setCreateTime(new Date());
        DataLogicEntity dataLogicEntity = this.dataLogicMapper.toEntity(dataLogicDto);
        dataLogicEntity = this.dataLogicDao.save(dataLogicEntity);
        if (isAdd) {
            StorageConfigurationDto storageConfigurationDto = new StorageConfigurationDto();
            storageConfigurationDto.setClassLogicId(dataLogicEntity.getId());
            storageConfigurationDto.setStorageDefineIdentifier(2);
            storageConfigurationDto.setSyncIdentifier(2);
            storageConfigurationDto.setCleanIdentifier(2);
            storageConfigurationDto.setMoveIdentifier(2);
            storageConfigurationDto.setBackupIdentifier(2);
            storageConfigurationDto.setArchivingIdentifier(2);
            storageConfigurationService.saveDto(storageConfigurationDto);
        }
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }

    @Override
    public List<DataLogicDto> saveList(List<DataLogicDto> dataLogicList) {
        List<DataLogicEntity> dataLogicEntities = this.dataLogicMapper.toEntity(dataLogicList);
        for (DataLogicEntity d : dataLogicEntities) {
            boolean isAdd = false;
            if (StringUtils.isEmpty(d.getId())) {
                isAdd = true;
                d.setCreateTime(new Date());
            }
            d = this.dataLogicDao.save(d);
            if (isAdd) {
                StorageConfigurationDto storageConfigurationDto = new StorageConfigurationDto();
                storageConfigurationDto.setClassLogicId(d.getId());
                storageConfigurationDto.setStorageDefineIdentifier(2);
                storageConfigurationDto.setSyncIdentifier(2);
                storageConfigurationDto.setCleanIdentifier(2);
                storageConfigurationDto.setMoveIdentifier(2);
                storageConfigurationDto.setBackupIdentifier(2);
                storageConfigurationDto.setArchivingIdentifier(2);
                storageConfigurationService.saveDto(storageConfigurationDto);
            }
        }
        return this.dataLogicMapper.toDto(dataLogicEntities);
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
        return this.mybatisQueryMapper.getDataLogicByDatabaseId(databaseId);
    }

    @Override
    public List<Map<String, Object>> getDistinctDatabaseAndLogic() {
        String sql = "select f.*,d.database_name from (select distinct logic_flag, database_id from T_SOD_DATA_LOGIC) f left join T_SOD_DATABASE d on f.database_id = d.id";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public void deleteByDataClassId(String dataClassId) {
        this.dataLogicDao.deleteByDataClassId(dataClassId);
    }

    @Override
    public List<DataLogicDto> findByDataClassIdAndLogicFlagAndStorageType(DataLogicDto dataLogicDto) {
        List<DataLogicEntity> byDataClassIdAndLogicFlagAndStorageType = dataLogicDao.findByDataClassIdAndLogicFlagAndStorageType(dataLogicDto.getDataClassId(), dataLogicDto.getLogicFlag(), dataLogicDto.getStorageType());
        return this.dataLogicMapper.toDto(byDataClassIdAndLogicFlagAndStorageType);
    }

    @Override
    public List<DataLogicDto> findByDataClassId(String dataClassId) {
        List<DataLogicEntity> byDataClassId = this.dataLogicDao.findByDataClassId(dataClassId);
        return this.dataLogicMapper.toDto(byDataClassId);
    }

    @Override
    public List<DataLogicDto> getDataLogic(String dataclassId, String databaseId, String tableName) {
        List<DataLogicEntity> dataLogic = this.mybatisQueryMapper.getDataLogic(dataclassId, databaseId, tableName);
        return this.dataLogicMapper.toDto(dataLogic);
    }

    @Override
    public DataLogicDto getDotById(String id) {
        DataLogicEntity dataLogicEntity = this.getById(id);
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }

    @Override
    public void deleteById(String id) {
        DataLogicDto dotById = this.getDotById(id);
        dataTableService.deleteByClassLogicId(dotById.getId());
        this.delete(id);
    }
    @Override
    public Map<String, Object> getTableByDBLogics(String tdbId, List<String> logics) {
        Map<String, Object> map = new HashMap();
        try {
            List<Map<String,Object>> dataList=mybatisQueryMapper.queryTableBylogics(logics);
            List<Map<String,Object>>  groupConcat = mybatisQueryMapper.getGroupConcat(logics);
            //如果是向砖题库中追加资料，过滤掉之前选择过的资料
            if(!StringUtils.isBlank(tdbId)){
                List<DatabaseSpecialReadWriteEntity> selectedList = databaseSpecialReadWriteDao.findBySdbId(tdbId);
                if(selectedList!=null&&selectedList.size()>0){
                    List<Map<String,Object>> delectedList = new ArrayList<Map<String,Object>>();
                    for(Map<String,Object> notSelect :dataList){
                        for(DatabaseSpecialReadWriteEntity databaseSpecial :selectedList){
                            if(notSelect.get("DATA_CLASS_ID").toString().equals(databaseSpecial.getDatabaseId().toString())){
                                delectedList.add(notSelect);
                            }
                        }
                    }
                    dataList.removeAll(delectedList);
                }
            }

            // 下面创建JSONArray对象，来存储查出的所有记录数据。
            JSONArray data = new JSONArray();
            HashSet<String> pp = new HashSet<String>();
            if(dataList != null && dataList.size()>0){
                for(int i=0;i<dataList.size();i++){
                    Map<String,Object> dataTable = dataList.get(i);
                    JSONObject pIdData = new JSONObject();
                    if(!pp.contains(dataTable.get("PID"))){
                        pIdData.put("id",dataTable.get("PID"));
                        pIdData.put("pId","-1");
                        pIdData.put("name",dataTable.get("className"));
                        pIdData.put("d_data_id",dataTable.get("PD_DATA_ID"));
                        pIdData.put("open","false");
                        data.add(pIdData);
                        pp.add(dataTable.get("PID").toString());
                    }
                    JSONObject oneData = new JSONObject();
                    oneData.put("id", dataTable.get("DATA_CLASS_ID"));
                    oneData.put("pId",dataTable.get("PID"));
                    oneData.put("name", dataTable.get("dataName"));
                    oneData.put("d_data_id", dataTable.get("D_DATA_ID"));
                    oneData.put("table_name", dataTable.get("TABLE_NAME"));
                    oneData.put("open","false");
                    oneData.put("db_table_type",dataTable.get("DB_TABLE_TYPE"));
                    oneData.put("storage_type",dataTable.get("STORAGE_TYPE"));
                    oneData.put("special_database_name", dataTable.get("SPECIAL_DATABASE_NAME"));
                    oneData.put("database_schema_name", dataTable.get("DATABASE_SCHEMA_NAME"));
                    //如果是键表-要素表中的要素表，要隐藏
                    if("E".equals(dataTable.get("DB_TABLE_TYPE")) && ((String)dataTable.get("STORAGE_TYPE")).contains("K")){
                        oneData.put("isHidden",true);
                    }
                    //前台临时存储使用
                    oneData.put("readOrWrite", "");
                    for(int j=0;j<groupConcat.size();j++){
                        Map<String,Object> group = groupConcat.get(j);
                        oneData.put("database_id", group.get("DATABASE_ID"));
                        if(dataTable.get("DATA_CLASS_ID").toString().equals(group.get("DATA_CLASS_ID")) && dataTable.get("D_DATA_ID").toString().equals(group.get("D_DATA_ID"))){
                            oneData.put("physicalDB", group.get("LOGIC"));
                            break;
                        }
                    }
                    data.add(oneData);
                }
            }

            map.put("data", data);
            map.put("returnCode", 0);
            map.put("returnMessage", "资料列表数据获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("returnCode", 1);
            map.put("returnMessage", "资料列表数据获取失败");
        }

        //下面返回值。
        return map;
    }
}
