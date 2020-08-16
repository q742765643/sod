package com.piesat.dm.rpc.service.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.special.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.mapper.dataclass.DataLogicMapper;
import com.piesat.dm.rpc.util.DatabaseUtil;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DataClassDao dataClassDao;

    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatabaseInfo databaseInfo;


    @Override
    public BaseDao<DataLogicEntity> getBaseDao() {
        return dataLogicDao;
    }

    @Override
    @Transactional
    public DataLogicDto saveDto(DataLogicDto dataLogicDto) {
        boolean isAdd = false;
        if (StringUtils.isEmpty(dataLogicDto.getId())) {
            isAdd = true;
        }
        dataLogicDto.setCreateTime(new Date());
        DataLogicEntity dataLogicEntity = this.dataLogicMapper.toEntity(dataLogicDto);
        dataLogicEntity = this.dataLogicDao.saveNotNull(dataLogicEntity);
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
    @Transactional
    public List<DataLogicDto> saveList(List<DataLogicDto> dataLogicList) {
        List<DataLogicEntity> dataLogicEntities = this.dataLogicMapper.toEntity(dataLogicList);
        for (DataLogicEntity d : dataLogicEntities) {
            boolean isAdd = false;
            if (StringUtils.isEmpty(d.getId())) {
                isAdd = true;
                d.setCreateTime(new Date());
            }
            d = this.dataLogicDao.saveNotNull(d);
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
    @Transactional
    public void deleteById(String id) {
        DataLogicDto dotById = this.getDotById(id);
        dataTableService.deleteByClassLogicId(dotById.getId());
        this.delete(id);
        String dataClassId = dotById.getDataClassId();
        List<DataLogicDto> byDataClassId = this.findByDataClassId(dataClassId);
        if (byDataClassId==null||byDataClassId.size()==0){
            this.dataClassDao.deleteByDataClassId(dataClassId);
        }
    }

    @Override
    @Transactional
    public void onlyDeleteById(String id) {
        DataLogicDto dotById = this.getDotById(id);
        dataTableService.deleteByClassLogicId(dotById.getId());
        this.delete(id);
    }

    @Override
    public Map<String, Object> getTableByDBLogics(String tdbId, String logics) {
            Map<String, Object> map = new HashMap();

            //查物理库的基础库和专题库下的表信息
            List<Map<String,Object>> dataList=mybatisQueryMapper.queryTableBylogics(Arrays.asList(logics.split(",")));
            //查询物理库下专题库下的表信息
            List<Map<String,Object>>  groupConcat = mybatisQueryMapper.getGroupConcat(Arrays.asList(logics.split(",")));
            //获取物理库中得表名称
             Map<String, List<String>> databaseTables = getDatabaseTables(logics);

             //如果是向砖题库中追加资料，过滤掉之前选择过的资料
            if(!StringUtils.isBlank(tdbId)){
                //专题库下的资料
                List<DatabaseSpecialReadWriteEntity> selectedList = databaseSpecialReadWriteDao.findBySdbId(tdbId);

                //dataList剔除掉该专题库下的资料
                if(selectedList!=null&&selectedList.size()>0){
                    List<Map<String,Object>> delectedList = new ArrayList<Map<String,Object>>();
                    for(Map<String,Object> notSelect :dataList){
                        for(DatabaseSpecialReadWriteEntity databaseSpecial :selectedList){
                            if(notSelect.get("DATA_CLASS_ID").toString().equals(databaseSpecial.getDataClassId().toString())){
                                delectedList.add(notSelect);
                            }
                        }
                    }
                    dataList.removeAll(delectedList);
                }

                //资料申请访问权限下得资料，专题库id=业务用户id
                Map<String, Object> dataAuthorityMap = dataAuthorityApplyService.getDataAuthorityList(tdbId,"","","","","");
                //dataList剔除掉资料访问权限申请的资料
                if(dataAuthorityMap != null && dataAuthorityMap.get("DS") != null){
                    List<Map<String,Object>> dataAuthorityList = (List<Map<String,Object>>)dataAuthorityMap.get("DS");
                    List<Map<String,Object>> delectedList = new ArrayList<Map<String,Object>>();
                    for(Map<String,Object> notSelect :dataList){
                        for(Map<String, Object> dataAuthorityRecord :dataAuthorityList){
                            if(notSelect.get("DATA_CLASS_ID").toString().equals(dataAuthorityRecord.get("DATA_CLASS_ID").toString())){
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

                    //剔除掉物理库中不存在得资料
                    String database_define_id = dataTable.get("DATABASE_DEFINE_ID") == null ? null : (String)dataTable.get("DATABASE_DEFINE_ID") ;
                    String table_name = dataTable.get("TABLE_NAME") == null ? null : (String)dataTable.get("TABLE_NAME") ;
                    if(databaseTables.get(database_define_id) != null && !databaseTables.get(database_define_id).contains(table_name.toUpperCase())){
                        continue;
                    }

                    JSONObject pIdData = new JSONObject();
                    if(!pp.contains(dataTable.get("PID"))){
                        pIdData.put("id",dataTable.get("PID"));
                        pIdData.put("pId","-1");
                        pIdData.put("name",dataTable.get("CLASSNAME"));
                        pIdData.put("d_data_id",dataTable.get("PD_DATA_ID"));
                        pIdData.put("open","false");
                        data.add(pIdData);
                        pp.add(dataTable.get("PID").toString());
                    }
                    JSONObject oneData = new JSONObject();
                    oneData.put("id", dataTable.get("DATA_CLASS_ID"));
                    oneData.put("pId",dataTable.get("PID"));
                    oneData.put("name", dataTable.get("DATANAME"));
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
                    oneData.put("database_id", dataTable.get("DATABASE_ID"));
                    oneData.put("physicalDB", dataTable.get("DATABASE_DEFINE_ID"));
                    /*for(int j=0;j<groupConcat.size();j++){
                        Map<String,Object> group = groupConcat.get(j);
                        oneData.put("database_id", group.get("DATABASE_ID"));
                        if(dataTable.get("DATA_CLASS_ID").toString().equals(group.get("DATA_CLASS_ID")) && dataTable.get("D_DATA_ID").toString().equals(group.get("D_DATA_ID"))){
                            oneData.put("physicalDB", group.get("LOGIC"));
                            break;
                        }
                    }*/
                    data.add(oneData);
                }
            }

            map.put("data", data);

            //下面返回值。
            return map;
    }

    public Map<String,List<String>> getDatabaseTables(String logics){
        HashMap<String, List<String>> map = new HashMap<>();
        if(StringUtils.isNotEmpty(logics)){
            List<DatabaseDto> databaseDtos = databaseService.findByDatabaseClassifyAndDatabaseDefineIdIn("物理库", Arrays.asList(logics.split(",")));
            for(int i=0;i<databaseDtos.size();i++){
                DatabaseDcl databaseDcl = null;
                try {
                    databaseDcl = DatabaseUtil.getDatabase(databaseDtos.get(i), databaseInfo);
                }catch (Exception e){
                    if (e.getMessage().contains("用户不存在")){
                        if(databaseDcl != null) databaseDcl.closeConnect();
                        continue;
                    }
                }

                try{
                    ResultT resultT = databaseDcl.queryAllTableName(databaseDtos.get(i).getSchemaName());
                    if(resultT.isSuccess() && resultT.getData() != null){
                        map.put(databaseDtos.get(i).getDatabaseDefine().getId(),(List<String>)resultT.getData());
                    }
                    databaseDcl.closeConnect();
                }catch (Exception e){
                    continue;
                }finally {
                    if (databaseDcl!=null){
                        databaseDcl.closeConnect();
                    }
                }
            }

        }
        return map;
    }
}
