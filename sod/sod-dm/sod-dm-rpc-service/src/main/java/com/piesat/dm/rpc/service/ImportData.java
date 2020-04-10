package com.piesat.dm.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.dm.common.codedom.CodeDOM;
import com.piesat.dm.dao.dataapply.CloudDatabaseApplyDao;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.dataclass.DatumTypeInfoDao;
import com.piesat.dm.dao.dataclass.LogicDefineDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.ShardingDao;
import com.piesat.dm.entity.dataapply.CloudDatabaseApplyEntity;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.*;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.datatable.ShardingEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.dao.sync.SyncConfigDao;
import com.piesat.schedule.dao.sync.SyncFilterDao;
import com.piesat.schedule.dao.sync.SyncMappingDao;
import com.piesat.schedule.dao.sync.SyncTaskDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.sync.SyncConfigEntity;
import com.piesat.schedule.entity.sync.SyncFilterEntity;
import com.piesat.schedule.entity.sync.SyncMappingEntity;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import com.xugu.cloudjdbc.Clob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据导入
 *
 * @author cwh
 * @date 2020年 01月17日 16:12:33
 */
@Service
public class ImportData {
    @Autowired
    private DataClassDao dataClassDao;
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private ShardingDao shardingDao;
    @Autowired
    private DatumTypeInfoDao datumTypeInfoDao;
    @Autowired
    private LogicDefineDao logicDefineDao;
    @Autowired
    private DatabaseDefineDao databaseDefineDao;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DataTableService dataTableService;
    @GrpcHthtClient
    private SyncConfigDao syncConfigDao;
    @GrpcHthtClient
    private SyncMappingDao syncMappingDao;
    @GrpcHthtClient
    private SyncTaskDao syncTaskDao;
    @GrpcHthtClient
    private SyncFilterDao syncFilterDao;
    @GrpcHthtClient
    private BackupDao backupDao;
    @GrpcHthtClient
    private MoveDao moveDao;
    @GrpcHthtClient
    private ClearDao clearDao;
    @Autowired
    private CloudDatabaseApplyDao cloudDatabaseApplyDao;


    public void implAll(){
//        importDataClassData();
//        importDataClassLogicData();
//        importDatumData();
//        impLogicDefine();
//        importDatabaseData();

        //importSyncTask();
        //importCloudDatabase();
        //importBackUp();
        //importMove();
        importClear();
    }


    public void importDataClassData() {
        String sql = "select * from DMIN_DATA_CLASS_TABLE";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            DataClassEntity dc = new DataClassEntity();
            dc.setClassName(m.get("CLASS_NAME").toString());
            dc.setDataClassId(m.get("DATA_CLASS_ID").toString());
            dc.setDDataId(m.get("D_DATA_ID").toString());
            dc.setFrequencyType(0);
            dc.setIfStopUse(false);
            dc.setIsAccess(1);
            dc.setIsAllLine(1);
            dc.setMetaDataName(m.get("META_DATA_NAME").toString());
            dc.setParentId(m.get("PARENT_CLASS_ID").toString());
            Object serial_no = m.get("SERIAL_NO");
            int n = 0;
            if (serial_no != null) {
                if (StringUtils.isNotEmpty(serial_no.toString())) {
                    n = Integer.parseInt(serial_no.toString());
                }
            }
            dc.setSerialNo(n);
            String meta_data_stor_type = m.get("META_DATA_STOR_TYPE").toString();
            if ("目录".equals(meta_data_stor_type)) dc.setType(1);
            else dc.setType(2);
            dc.setUseBaseInfo(1);
            dc.setDelFlag("0");
            dc.setCreateTime(new Date());
            dataClassDao.save(dc);
        }
    }

    public void importDataClassLogicData() {
        String sql = "select * from DMIN_DATA_CLASS_LOGIC a,DMIN_DATA_DL_PHYSICS b where a.CLASS_LOGIC_ID = b.DL_ID";
        List<Map> list = CodeDOM.getList(sql);
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
            DataLogicEntity save = dataLogicDao.save(dc);
            importTableData(class_logic_id, save);
        }
    }

    @Transactional
    public void importTableData(String oldId, DataLogicEntity newId) {
        String sql = "select * from DMIN_DATA_ID_TABLE where CLASS_LOGIC_ID = '" + oldId + "'";
        List<Map> list = CodeDOM.getList(sql);
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
            Optional<DataLogicEntity> byId = dataLogicDao.findById(newId.getId());
            dt.setClassLogic(byId.get());
            dt.setCreateTime(new Date());
            dt = this.dataTableDao.save(dt);
            sql = "select * from DMIN_DATA_TABLE_FIELD where TABLE_ID ='" + table_id + "'";
            List<Map> columns = CodeDOM.getList(sql);
            Set<TableColumnEntity> columnArr = new HashSet<>();
            for (Map<String, Object> columnMap : columns) {
                TableColumnEntity tc = new TableColumnEntity();
                String db_ele_code = toString(columnMap.get("DB_ELE_CODE"));
                tc.setDbEleCode(db_ele_code);
                String c_element_code = toString(columnMap.get("C_ELEMENT_CODE"));
                tc.setCElementCode(c_element_code);
                String is_manager = toString(columnMap.get("IS_MANAGER")).toLowerCase();
                tc.setIsManager("true".equals(is_manager) ? true : false);
                String user_ele_code = toString(columnMap.get("USER_ELE_CODE"));
                tc.setUserEleCode(user_ele_code);
                String name_cn1 = toString(columnMap.get("NAME_CN"));
                tc.setNameCn(name_cn1);
                String ele_name = toString(columnMap.get("ELE_NAME"));
                tc.setEleName(ele_name);
                String type = toString(columnMap.get("TYPE"));
                tc.setType(type);
                if (columnMap.get("ACCURACY") != null) {
                    String accuracy = columnMap.get("ACCURACY").toString();
                    tc.setAccuracy(accuracy);
                }

                String length = toString(columnMap.get("LENGTH"));
                if (com.piesat.common.utils.StringUtils.isNotEmpty(length)) tc.setLength(Integer.parseInt(length));
                String unit = toString(columnMap.get("UNIT"));
                tc.setUnit(unit);
                String unit_cn = toString(columnMap.get("UNIT_CN"));
                tc.setUnitCn(unit_cn);
                String is_null = toString(columnMap.get("IS_NULL")).toLowerCase();
                tc.setIsNull("true".equals(is_null) ? true : false);
                String is_update = toString(columnMap.get("IS_UPDATE")).toLowerCase();
                tc.setIsUpdate("true".equals(is_update) ? true : false);
                String is_show = toString(columnMap.get("IS_SHOW")).toLowerCase();
                tc.setIsShow("true".equals(is_show) ? true : false);
                String is_premary_key = toString(columnMap.get("IS_PREMARY_KEY")).toLowerCase();
                tc.setIsPrimaryKey("true".equals(is_premary_key) ? true : false);
                String is_kv_k = toString(columnMap.get("IS_KV_K")).toLowerCase();
                tc.setIsKvK("true".equals(is_kv_k) ? true : false);
                String serial_number = toString(columnMap.get("SERIAL_NUMBER"));
                if (com.piesat.common.utils.StringUtils.isNotEmpty(serial_number))
                    tc.setSerialNumber(Integer.parseInt(serial_number));
                tc.setCreateTime(new Date());
                tc.setTableId(dt.getId());
                columnArr.add(tc);
            }
            dt.setColumns(columnArr);
            dt = this.dataTableDao.save(dt);
            sql = "select * from DMIN_DB_TABLE_INDEX where TABLE_ID ='" + table_id + "'";
            List<Map> indexs = CodeDOM.getList(sql);
            Set<TableIndexEntity> indexArr = new HashSet<>();
            for (Map<String, Object> indexMap : indexs) {
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
            List<Map> shardings = CodeDOM.getList(sql);
            for (Map<String, Object> sMap : shardings) {
                ShardingEntity se = new ShardingEntity();
                se.setTableId(dt.getId());
                String field = toString(sMap.get("FIELD"));
                se.setColumnName(field);
                String sharding_type = toString(sMap.get("SHARDING_TYPE")).toUpperCase();
                se.setShardingType("T".equals(sharding_type) ? 1 : 0);
                se.setCreateTime(new Date());
                this.shardingDao.save(se);
            }

        }
    }

    public String toString(Object o) {
        return o == null ? "" : o.toString();
    }


    public void importDatumData() {
        String sql = "select * from tab_omin_cm_cc_datumtypeinfo ";
        List<Map> datum = CodeDOM.getList(sql);
        for (Map<String, Object> m : datum) {
            DatumTypeInfoEntity dte = new DatumTypeInfoEntity();
            dte.setCDatumCode(toString(m.get("C_DATUM_CODE")));
            dte.setCDatumtype(toString(m.get("C_DATUMTYPE")));
            dte.setCDatumLevel(toString(m.get("C_DATUM_LEVEL")));
            dte.setCFlowId(toString(m.get("C_FLOW_ID")));
            dte.setCDatumparentCode(toString(m.get("C_DATUMPARENT_CODE")));
            dte.setCCoremetarName(toString(m.get("C_COREMETAR_NAME")));
            dte.setCSysCode(toString(m.get("C_SYS_CODE")));
            dte.setCFlow(toString(m.get("C_FLOW")));
            dte.setCSysName(toString(m.get("C_SYS_NAME")));
            dte.setCOptType(toString(m.get("C_OPT_TYPE")));
            dte.setCStatus(toString(m.get("C_STATUS")));
            dte.setCCoremetaCode(toString(m.get("C_COREMETA_CODE")));
            dte.setCMsgHeader(toString(m.get("C_MSG_HEADER")));
            dte.setCDatatype(toString(m.get("C_DATATYPE")));
            dte.setCTraeeflag(toString(m.get("C_TRAEEFLAG")));
            dte.setCDatumProcessInfo(toString(m.get("C_DATUM_PROCESS_INFO")));
            dte.setCPri(toString(m.get("C_PRI")));
            dte.setCNetstationType(toString(m.get("C_NETSTATION_TYPE")));
            dte.setCIsKeyData(toString(m.get("C_IS_KEY_DATA")));
            dte.setCAccess(toString(m.get("C_ACCESS")));
            dte.setCciRootTypeCode(toString(m.get("CCI_ROOT_TYPE_CODE")));
            dte.setCSubmitDate((Date) (m.get("C_SUBMIT_DATE")));
            dte.setCSubmitUserid(toString(m.get("C_SUBMIT_USERID")));
            dte.setCSubmitUsername(toString(m.get("C_SUBMIT_USERNAME")));
            dte.setCSubmitOrgId(toString(m.get("C_SUBMIT_ORG_ID")));
            dte.setCSubmitOrgName(toString(m.get("C_SUBMIT_ORG_NAME")));
            dte.setCDataLevel(toString(m.get("C_DATA_LEVEL")));
            dte.setCPubdate((Date) (m.get("C_PUBDATE")));
            dte.setCDatumtypeinfopsId(toString(m.get("C_DATUMTYPEINFOPS_ID")));
            dte.setCRuleBankId(toString(m.get("C_RULE_BANK_ID")));
            dte.setCModifier(toString(m.get("C_MODIFIER")));
            dte.setCCreateDate((Date) (m.get("C_CREATE_DATE")));
            dte.setCUpdatedDate((Date) (m.get("C_UPDATED_DATE")));
            dte.setVersion(((BigDecimal)(m.get("VERSION"))).intValue());
            dte.setCBusinessFrequency(toString(m.get("C_BUSINESS_FREQUENCY")));
            dte.setCNettype(toString(m.get("C_NETTYPE")));
            dte.setCCoremetaId(toString(m.get("C_COREMETA_ID")));
            this.datumTypeInfoDao.save(dte);
        }
    }

    public void importSyncTask(){
        String sql = "select * from SYNC_TASK";
        List<Map> tasks = CodeDOM.getList(sql);
        for (Map<String, Object> m : tasks) {

            SyncTaskEntity syncTaskEntity = new SyncTaskEntity();

            String target_database_id = this.toString(m.get("TARGET_DATABASE_ID"));
            List<DatabaseEntity> databaseEntity = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", target_database_id);
            target_database_id = databaseEntity.get(0).getId();

            String source_table = this.toString(m.get("SOURCE_TABLE"));
            String sourceMappingIDs = "";
            String[] sourceTables = source_table.split(",");
            for(String sourceTable : sourceTables){
                String sourceMappingID = saveSync(sourceTable,target_database_id,"K");
                if(StringUtils.isNotEmpty(sourceMappingIDs)){
                    sourceMappingIDs = sourceMappingIDs + "," + sourceMappingID;
                }else{
                    sourceMappingIDs = sourceMappingID;
                }
            }

            String slave_tables = this.toString(m.get("SLAVE_TABLES"));
            String slaveMappingID  = "";
            if(StringUtils.isNotEmpty(slave_tables)){
                slaveMappingID = saveSync(slave_tables,target_database_id,"V");
            }

            /*String id = this.toString(m.get("TASK_ID"));
            if(StringUtils.isNotEmpty(id)){
                syncTaskEntity.setId(id);
            }*/

            String task_name = this.toString(m.get("TASK_NAME"));
            if(StringUtils.isNotEmpty(task_name)){
                syncTaskEntity.setTaskName(task_name);
            }

            Integer sync_type = Integer.valueOf(this.toString(m.get("SYNC_TYPE")));
            syncTaskEntity.setSyncType(sync_type);

            String data_source_id = this.toString(m.get("DATA_SOURCE_ID"));
            syncTaskEntity.setDataSourceId(data_source_id);

            String data_flow_direction_id = this.toString(m.get("DATA_FLOW_DIRECTION_ID"));
            syncTaskEntity.setDataFlowDirectionId(data_flow_direction_id);

            String source_database_id = this.toString(m.get("SOURCE_DATABASE_ID"));
            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", source_database_id);
            source_database_id = databaseEntity1.get(0).getId();
            syncTaskEntity.setSourceDatabaseId(source_database_id);

            //String target_database_id = this.toString(m.get("TARGET_DATABASE_ID"));
            syncTaskEntity.setTargetDatabaseId(target_database_id);

            syncTaskEntity.setSourceTable(sourceMappingIDs);

            String source_table_datecolumn = this.toString(m.get("SOURCE_TABLE_DATECOLUMN"));
            syncTaskEntity.setSourceTableDatecolumn(source_table_datecolumn);

            Integer batch_amount = Integer.valueOf(this.toString(m.get("BATCH_AMOUNT")));
            syncTaskEntity.setBatchAmount(batch_amount);

            String begin_time = this.toString(m.get("BEGIN_TIME"));
            if(StringUtils.isNotEmpty(begin_time)){
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, begin_time);
                syncTaskEntity.setBeginTime(date);
            }

            String last_success_time = this.toString(m.get("LAST_SUCCESS_TIME"));
            if(StringUtils.isNotEmpty(last_success_time)){
                Date Date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, last_success_time);
                syncTaskEntity.setBeginTime(Date);
            }

            String has_modify = this.toString(m.get("HAS_MODIFY"));
            syncTaskEntity.setHasModify(has_modify);

            if(StringUtils.isNotEmpty(slaveMappingID)){
                syncTaskEntity.setSlaveTables(slaveMappingID);
            }

            String link_key = "";
            Clob clob = (Clob)m.get("LINK_KEY");
            try {
                char[] mapperChar = new char[(int)clob.length()];
                int read = clob.getCharacterStream().read(mapperChar);
                if(read > 0){
                    link_key = String.valueOf(mapperChar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(StringUtils.isNotEmpty(link_key)){
                syncTaskEntity.setLinkKey(link_key);
            }

            Integer sync_period = Integer.valueOf(this.toString(m.get("SYNC_PERIOD")));
            syncTaskEntity.setSyncPeriod(sync_period);

            String di_off = this.toString(m.get("DI_OFF"));
            syncTaskEntity.setDiOff(di_off);

            String discard_on_duplicate = this.toString(m.get("DISCARD_ON_DUPLICATE"));
            syncTaskEntity.setDiscardOnDuplicate(discard_on_duplicate);

            String exec_ip = this.toString(m.get("EXEC_IP"));
            if(StringUtils.isNotEmpty(exec_ip)){
                syncTaskEntity.setExecIp(exec_ip);
            }

            Integer exec_port = Integer.valueOf(this.toString(m.get("EXEC_PORT")));
            syncTaskEntity.setExecPort(exec_port);

            String create_user = this.toString(m.get("CREATE_USER"));
            syncTaskEntity.setCreateBy(create_user);

            Integer target_type = Integer.valueOf(this.toString(m.get("TARGET_TYPE")));
            syncTaskEntity.setTargetType(target_type);



            //查找源表id
            List<Map<String, Object>> databaseIdAndTableName = dataTableService.getByDatabaseIdAndTableName(source_database_id, source_table_name);
            String source_table_Id = this.toString(databaseIdAndTableName.get(0).get("ID"));

            syncTaskEntity.setSourceTableId(source_table_Id);

            syncTaskEntity = syncTaskDao.saveNotNull(syncTaskEntity);

        }


    }
    String source_table_name = "";
    public String saveSync(String mappingId,String databaseId,String flag){
        String sql = "select * from SYNC_MAPPING where RECORD_ID='"+mappingId+"'";
        List<Map> mapping = CodeDOM.getList(sql);
        String sourceMappingID = "";

        for (Map<String, Object>  mapp: mapping) {

            if(flag.equals("K")){
                source_table_name = this.toString(mapp.get("SOURCE_TABLE_NAME"));
            }

            String source_table_id = this.toString(mapp.get("SOURCE_TABLE_ID"));
            String filerIDS = "";
            //源表有过滤字段
            if(StringUtils.isNotEmpty(source_table_id)){
                String[] filters = source_table_id.split(",");
                for(String filterId:filters){
                    sql = "select * from sync_filter where RECORD_ID='"+filterId+"'";
                    List<Map> filterMaps = CodeDOM.getList(sql);
                    if(filterMaps != null && filterMaps.size()>0){
                        for(Map<String, Object>  filter: filterMaps){
                            String column_name = this.toString(filter.get("COLUMN_NAME"));
                            String filter_values = this.toString(filter.get("FILTER_VALUES"));
                            String column_oper = this.toString(filter.get("COLUMN_OPER"));
                            if(StringUtils.isNotEmpty(column_name) && StringUtils.isNotEmpty(filter_values) && StringUtils.isNotEmpty(column_oper)){
                                SyncFilterEntity syncFilterEntity = new SyncFilterEntity();
                                syncFilterEntity.setColumnName(column_name);
                                syncFilterEntity.setFilterValues(filter_values);
                                syncFilterEntity.setColumnOper(column_oper);
                                SyncFilterEntity filterEntity = syncFilterDao.saveNotNull(syncFilterEntity);
                                if(StringUtils.isNotEmpty(filerIDS)){
                                    filerIDS = filerIDS + ","+ filterEntity.getId();
                                }
                            }
                        }
                    }
                }
            }

            //必须是有值得
            String source_table_name = this.toString(mapp.get("SOURCE_TABLE_NAME"));

            String target_table_name = this.toString(mapp.get("TARGET_TABLE_NAME"));

            //必须是有值得
            String target_table_id = this.toString(mapp.get("TARGET_TABLE_ID"));
            String configID = "";
            if(StringUtils.isNotEmpty(target_table_id)){
                sql = "select * from sync_config where RECORD_ID='"+target_table_id+"'";
                List<Map> configMaps = CodeDOM.getList(sql);//根据主键查询，只能查出一条
                if(configMaps != null && configMaps.size()>0){
                    String unique_keys = this.toString(configMaps.get(0).get("UNIQUE_KEYS"));
                    String ifpatitions = this.toString(configMaps.get(0).get("IFPATITIONS"));
                    String partition_keys = this.toString(configMaps.get(0).get("PARTITION_KEYS"));
                    /*String data_key = this.toString(configMaps.get(0).get("DATA_KEY"));*/
                    String is_kv = this.toString(configMaps.get(0).get("IS_KV"));
                    String d_data_id = this.toString(configMaps.get(0).get("D_DATA_ID"));
                    //查目标表id   根据存储编码，物理库id，表名
                    List<Map<String, Object>> databaseIdAndTableName = dataTableService.getByDatabaseIdAndTableName(databaseId, target_table_name);
                    String target_table_Id = this.toString(databaseIdAndTableName.get(0).get("ID"));
                    SyncConfigEntity syncConfigEntity = new SyncConfigEntity();
                    if(StringUtils.isNotEmpty(d_data_id)){
                        syncConfigEntity.setDDataId(d_data_id);
                    }
                    if(StringUtils.isNotEmpty(ifpatitions)){
                        syncConfigEntity.setIfpatitions(ifpatitions);
                    }
                    if(StringUtils.isNotEmpty(is_kv)){
                        syncConfigEntity.setIsKv(is_kv);
                    }
                    if(StringUtils.isNotEmpty(partition_keys)){
                        syncConfigEntity.setPartitionKeys(partition_keys);
                    }
                    if(StringUtils.isNotEmpty(unique_keys)){
                        syncConfigEntity.setUniqueKeys(unique_keys);
                    }
                    if(StringUtils.isNotEmpty(target_table_Id)){
                        syncConfigEntity.setTargetTableId(target_table_Id);
                    }
                    syncConfigEntity = syncConfigDao.saveNotNull(syncConfigEntity);
                    configID = this.toString(syncConfigEntity.getId());
                }
            }


            //mapp
            String mappss = "";
           Clob clob = (Clob)mapp.get("MAPPING");
            try {
                char[] mapperChar = new char[(int)clob.length()];
                int read = clob.getCharacterStream().read(mapperChar);
                if(read > 0){
                    mappss = String.valueOf(mapperChar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SyncMappingEntity syncMappingEntity = new SyncMappingEntity();
            syncMappingEntity.setMapping(mappss);
            if(StringUtils.isNotEmpty(filerIDS)){
                syncMappingEntity.setSourceTableId(filerIDS);
            }
            syncMappingEntity.setSourceTableName(source_table_name);
            syncMappingEntity.setTargetTableId(configID);
            syncMappingEntity.setTargetTableName(target_table_name);
            syncMappingEntity = syncMappingDao.saveNotNull(syncMappingEntity);
            if(StringUtils.isNotEmpty(sourceMappingID)){
                sourceMappingID = sourceMappingID + "," + syncMappingEntity.getId();
            }else{
                sourceMappingID = String.valueOf(syncMappingEntity.getId());
            }
        }

        return sourceMappingID;
    }




    public void impLogicDefine(){
        String sql = "select * from DMIN_DB_LOGIC_DEFINE";
        List<Map> logic = CodeDOM.getList(sql);
        for (Map<String, Object> m : logic) {
            sql = "select * from DMIN_DB_LOGIC_PHYSICS where LOGIC_ID = '"+m.get("LOGIC_ID")+"'";
            LogicDefineEntity dte = new LogicDefineEntity();
            dte.setLogicFlag(toString(m.get("LOGIC_ID")));
            dte.setLogicName(toString(m.get("LOGIC_NAME")));
            dte.setLogicDesc(toString(m.get("LOGIC_DESC")));
            dte.setSerialNumber(Integer.getInteger(toString(m.get("SERIAL_NUMBER"))));
            String storage_type = toString(m.get("STORAGE_TYPE"));
            String[] split = storage_type.split(",");
            Set<LogicStorageTypesEntity> ls = new HashSet<>();
            for (String s:split ) {
                LogicStorageTypesEntity lst = new LogicStorageTypesEntity();
                lst.setStorageType(s);
                lst.setCreateTime(new Date());
                ls.add(lst);
            }
            dte.setLogicStorageTypesEntityList(ls);
            Set<LogicDatabaseEntity> lll = new HashSet<>();
            List<Map> lde = CodeDOM.getList(sql);
            for (Map mm: lde ) {
                String database_id = toString(mm.get("DATABASE_ID"));
                LogicDatabaseEntity ld = new LogicDatabaseEntity();
                ld.setDatabaseId(database_id);
                ld.setCreateTime(new Date());
                lll.add(ld);
            }
            dte.setLogicDatabaseEntityList(lll);
            dte.setCreateTime(new Date());
            this.logicDefineDao.save(dte);
        }

    }

    /**
     * 必须在dataClassLogic同步完成后再同步此表
     */
    public void importDatabaseData() {
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
                if (com.piesat.common.utils.StringUtils.isNotEmpty(database_capacity))dd.setDatabaseCapacity(Integer.parseInt(database_capacity));
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
            DatabaseEntity save = this.databaseDao.save(de);
            dataLogicDao.updateDatabaseId(save.getId(),database_id);
        }
    }



    //云数据库申请
    public void  importCloudDatabase(){
        String sql = "select * from dmin_cldb_application";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {

            String application_time = toString(m.get("APPLICATION_TIME"));
            String user_id = toString(m.get("USER_ID"));
            String db_use = toString(m.get("DB_USE"));
            String examine_material = toString(m.get("EXAMINE_MATERIAL"));
            String application_system = toString(m.get("APPLICATION_SYSTEM"));
            String cpu_memory = toString(m.get("CPU_MEMORY"));
            String storage_space = toString(m.get("STORAGE_SPACE"));
            String new_cpu_memory = toString(m.get("NEW_CPU_MEMORY"));
            String new_storage_space = toString(m.get("NEW_STORAGE_SPACE"));
            String examine_status = toString(m.get("EXAMINE_STATUS"));
            String failure_reason = toString(m.get("FAILURE_REASON"));
            String examiner = toString(m.get("EXAMINER"));
            String examine_time = toString(m.get("EXAMINE_TIME"));
            String db_name = toString(m.get("DB_NAME"));
            String db_ip = toString(m.get("DB_IP"));
            String db_portnum = toString(m.get("DB_PORTNUM"));
            String db_username = toString(m.get("DB_USERNAME"));
            String db_password = toString(m.get("DB_PASSWORD"));
            String storage_logic = toString(m.get("STORAGE_LOGIC"));
            String mount_server = toString(m.get("MOUNT_SERVER"));
            String mount_directory = toString(m.get("MOUNT_DIRECTORY"));

            CloudDatabaseApplyEntity cloudDatabaseApplyEntity = new CloudDatabaseApplyEntity();
            cloudDatabaseApplyEntity.setDatabaseName(db_name);
            cloudDatabaseApplyEntity.setDatabaseUse(db_use);
            cloudDatabaseApplyEntity.setUserId(user_id);

            if(StringUtils.isNotEmpty(application_system)){
                cloudDatabaseApplyEntity.setApplicationSystem(application_system);
            }
            if(StringUtils.isNotEmpty(examine_material)){
                cloudDatabaseApplyEntity.setExamineMaterial(examine_material);
            }
            if(StringUtils.isNotEmpty(cpu_memory)){
                cloudDatabaseApplyEntity.setCpuMemory(cpu_memory);
            }
            if(StringUtils.isNotEmpty(storage_space)){
                cloudDatabaseApplyEntity.setStorageSpace(storage_space);
            }
            if(StringUtils.isNotEmpty(new_cpu_memory)){
                cloudDatabaseApplyEntity.setNewCpuMemory(new_cpu_memory);
            }

            cloudDatabaseApplyEntity.setNewStorageSpace(new_storage_space);
            cloudDatabaseApplyEntity.setExamineStatus(examine_status);
            cloudDatabaseApplyEntity.setFailure_reason(failure_reason);
            cloudDatabaseApplyEntity.setExaminer(examiner);

            if(StringUtils.isNotEmpty(examine_time)){
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                cloudDatabaseApplyEntity.setExamineTime(date);
            }

            cloudDatabaseApplyEntity.setDatabaseIp(db_ip);
            if(StringUtils.isNotEmpty(db_portnum)){
                try{
                    cloudDatabaseApplyEntity.setDatabasePort(Integer.valueOf(db_portnum));
                }catch (Exception e){
                    cloudDatabaseApplyEntity.setDatabasePort(8080);
                }
            }
            cloudDatabaseApplyEntity.setDatabaseUsername(db_username);
            cloudDatabaseApplyEntity.setDatabasePassword(db_password);
            cloudDatabaseApplyEntity.setStorageLogic(storage_logic);
            if(StringUtils.isNotEmpty(mount_server)){
                cloudDatabaseApplyEntity.setMountServer(mount_server);
            }
            if(StringUtils.isNotEmpty(mount_directory)){
                cloudDatabaseApplyEntity.setMountDirectory(mount_directory);
            }
            cloudDatabaseApplyDao.saveNotNull(cloudDatabaseApplyEntity);
        }
    }

    //数据备份
    public void importBackUp(){
        String sql = "select * from DMIN_DATA_BACKUP_TASK";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {

            String parent_id = toString(m.get("DATABASE_ID"));
            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", parent_id);
            String database_id = databaseEntity1.get(0).getId();

            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            String file_storagedir = toString(m.get("FILE_STORAGEDIR"));
            String exec_ip = toString(m.get("EXEC_IP"));
            String exec_port = toString(m.get("EXEC_PORT"));//int
            String backup_time_unit = toString(m.get("BACKUP_TIME_UNIT"));
            String first_time_period = toString(m.get("FIRST_TIME_PERIOD"));
            String last_time_period = toString(m.get("LAST_TIME_PERIOD"));
            String backup_type = toString(m.get("BACKUP_TYPE"));
            String backup_content = toString(m.get("BACKUP_CONTENT"));
            String cron = toString(m.get("CRON"));

            Integer cron_status = null;
            if(StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))){
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String alarm = toString(m.get("ALARM"));

            Integer time_out = null;
            if(StringUtils.isNotEmpty(toString(m.get("TIME_OUT")))){
                time_out = Integer.valueOf(toString(m.get("TIME_OUT")));
            }

            Integer retry_count = null;
            if(StringUtils.isNotEmpty(toString(m.get("RETRY_COUNT")))){
                retry_count = Integer.valueOf(toString(m.get("RETRY_COUNT")));
            }

            Integer between_retries =null;
            if(StringUtils.isNotEmpty(toString(m.get("BETWEEN_RETRIES")))){
                between_retries = Integer.valueOf(toString(m.get("BETWEEN_RETRIES")));
            }

            BackupEntity backupEntity = new BackupEntity();
            backupEntity.setConditions("D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-1d}' and D_DATETIME>='{yyyy-MM-dd,-2d}'");
            backupEntity.setDataClassId(data_class_id);
            backupEntity.setDatabaseId(database_id);
            backupEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            backupEntity.setDdataId(dataClassEntity.getDDataId());
            //backupEntity.setForeignKey();
            backupEntity.setParentId(parent_id);
            backupEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName()+"_"+databaseEntity1.get(0).getDatabaseName()+"_"+dataClassEntity.getClassName());
            backupEntity.setSecondConditions("D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-2d}' and D_DATETIME>='{yyyy-MM-dd,-3d}'");
            backupEntity.setStorageDirectory("/CMADAAS/EXCHANGE/SOD/BACKUP");


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if(dataTableEntities.size() == 1){
                backupEntity.setTableName(dataTableEntities.get(0).getTableName());
            }else{
                String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                backupEntity.setTableName(tableName);
            }
            //父表
            if(retry_count != null){
                backupEntity.setExecutorFailRetryCount(retry_count);
            }
            if(time_out != null){
                backupEntity.setExecutorTimeout(time_out);
            }
            if(StringUtils.isNotEmpty(alarm)){
                if(alarm.equals("on")){
                    backupEntity.setIsAlarm("1");
                }else{
                    backupEntity.setIsAlarm("0");
                }

            }
            if(StringUtils.isNotEmpty(cron)){
                backupEntity.setJobCron(cron);
            }
            if(between_retries != null){
                backupEntity.setRetryInterval(between_retries);
            }
            //backupEntity.setTriggerLastTime();//上次调度时间
            //backupEntity.setTriggerNextTime();//下次调度时间
            if(cron_status != null){
                backupEntity.setTriggerStatus(cron_status);
            }
            backupDao.saveNotNull(backupEntity);
        }
    }

    //数据迁移
    public void importMove() {
        String sql = "select * from omin_move_file_migratetactics where database_type=1";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String database_type = toString(m.get("DATABASE_TYPE"));

            String physics_database = toString(m.get("PHYSICS_DATABASE"));
            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", physics_database);
            String database_id = databaseEntity1.get(0).getId();

            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            String data_service_name = toString(m.get("DATA_SERVICE_NAME"));
            String storage_type = toString(m.get("STORAGE_TYPE"));
            String file_storagedir = toString(m.get("FILE_STORAGEDIR"));
            String data_overduetime = toString(m.get("DATA_OVERDUETIME"));
            String time_unit = toString(m.get("TIME_UNIT"));
            String exec_ip = toString(m.get("EXEC_IP"));
            String exec_port = toString(m.get("EXEC_PORT"));
            String cron = toString(m.get("CRON"));

            Integer cron_status = null;
            if(StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))){
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String source_directory = toString(m.get("SOURCE_DIRECTORY"));
            String alarm = toString(m.get("ALARM"));


            Integer timeout = null;
            if(StringUtils.isNotEmpty(toString(m.get("TIMEOUT")))){
                timeout = Integer.valueOf(toString(m.get("TIMEOUT")));
            }


            MoveEntity moveEntity = new MoveEntity();
            moveEntity.setClearConditions("D_FILE_SAVE_HIERARCHY='1' and D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-30d}'");
            moveEntity.setConditions("D_FILE_SAVE_HIERARCHY='0' and D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-30d}'");
            moveEntity.setDataClassId(data_class_id);
            moveEntity.setDatabaseId(database_id);

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            moveEntity.setDdataId(dataClassEntity.getDDataId());

            moveEntity.setIsClear("1");
            moveEntity.setMoveLimit(86400);
            moveEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName()+"_"+databaseEntity1.get(0).getDatabaseName()+"_"+dataClassEntity.getClassName());
            moveEntity.setSourceDirectory("/zzj/soure");


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if(dataTableEntities.size() == 1){
                moveEntity.setTableName(dataTableEntities.get(0).getTableName());
            }else{
                String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                moveEntity.setTableName(tableName);
            }
            moveEntity.setTargetDirectory("/zzj/target");
            moveEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());
            moveEntity.setParentId(physics_database);

            //父表
            if(timeout != null){
                moveEntity.setExecutorTimeout(timeout);
            }
            if(StringUtils.isNotEmpty(alarm)){
                if(alarm.equals("true")){
                    moveEntity.setIsAlarm("1");
                }else{
                    moveEntity.setIsAlarm("0");
                }

            }
            if(StringUtils.isNotEmpty(cron)){
                moveEntity.setJobCron(cron);
            }

            if(cron_status != null){
                moveEntity.setTriggerStatus(cron_status);
            }
            moveDao.saveNotNull(moveEntity);
        }

    }

    //数据清除
    public void importClear() {
        //表格型数据清除
        //omin_move_tbdata_cleantactics
        String sql = "select * from omin_move_tbdata_cleantactics";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {

            String physics_database = toString(m.get("PHYSICS_DATABASE"));
            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", physics_database);
            String database_id = databaseEntity1.get(0).getId();

            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            String data_service_name = toString(m.get("DATA_SERVICE_NAME"));
            String storage_type = toString(m.get("STORAGE_TYPE"));
            String data_overduetime = toString(m.get("DATA_OVERDUETIME"));
            String time_unit = toString(m.get("TIME_UNIT"));
            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));

            ClearEntity clearEntity = new ClearEntity();
            clearEntity.setClearLimit(86400);
            clearEntity.setConditions("default_time_to_live=2592000");
            clearEntity.setDataClassId(data_class_id);


            clearEntity.setDatabaseId(database_id);
            clearEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            clearEntity.setDdataId(dataClassEntity.getDDataId());

            clearEntity.setParentId(physics_database);
            clearEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName()+"_"+databaseEntity1.get(0).getDatabaseName()+"_"+dataClassEntity.getClassName());

            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if(dataTableEntities.size() == 1){
                clearEntity.setTableName(dataTableEntities.get(0).getTableName());
            }else{
                String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(dataTableEntities.size()>0 && !StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                clearEntity.setTableName(tableName);
            }

            clearEntity.setExecutorTimeout(2592000);//30天
            clearEntity.setIsAlarm("1");

            clearEntity.setJobCron("00 00 00 01 * * *");
            clearEntity.setTriggerStatus(0);

            clearDao.saveNotNull(clearEntity);
        }

        //结构化数据清除
        //omin_move_file_migratetactics
        sql = "select * from omin_move_file_migratetactics where database_type=2";
        List<Map> list1 = CodeDOM.getList(sql);
        for (Map<String, Object> m : list1) {
            String database_type = toString(m.get("DATABASE_TYPE"));

            String physics_database = toString(m.get("PHYSICS_DATABASE"));
            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", physics_database);
            String database_id = databaseEntity1.get(0).getId();

            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            String data_service_name = toString(m.get("DATA_SERVICE_NAME"));
            String storage_type = toString(m.get("STORAGE_TYPE"));
            String file_storagedir = toString(m.get("FILE_STORAGEDIR"));

            String data_overduetime = toString(m.get("DATA_OVERDUETIME"));
            String time_unit = toString(m.get("TIME_UNIT"));
            String exec_ip = toString(m.get("EXEC_IP"));
            String exec_port = toString(m.get("EXEC_PORT"));
            String cron = toString(m.get("CRON"));

            Integer cron_status = null;
            if(StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))){
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String source_directory = toString(m.get("SOURCE_DIRECTORY"));
            String alarm = toString(m.get("ALARM"));


            Integer timeout = null;
            if(StringUtils.isNotEmpty(toString(m.get("TIMEOUT")))){
                timeout = Integer.valueOf(toString(m.get("TIMEOUT")));
            }

            ClearEntity clearEntity = new ClearEntity();
            clearEntity.setClearLimit(Long.valueOf(file_storagedir));
            clearEntity.setConditions("D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-30d}'");
            clearEntity.setDataClassId(data_class_id);

            clearEntity.setDatabaseId(database_id);
            clearEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            clearEntity.setDdataId(dataClassEntity.getDDataId());

            clearEntity.setParentId(physics_database);
            clearEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName()+"_"+databaseEntity1.get(0).getDatabaseName()+"_"+dataClassEntity.getClassName());


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if(dataTableEntities.size() == 1){
                clearEntity.setTableName(dataTableEntities.get(0).getTableName());
            }else{
                String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                clearEntity.setTableName(tableName);
            }

            if(timeout != null){
                clearEntity.setExecutorTimeout(timeout);
            }
            if(StringUtils.isNotEmpty(alarm)){
                if(alarm.equals("true")){
                    clearEntity.setIsAlarm("1");
                }else{
                    clearEntity.setIsAlarm("0");
                }

            }
            if(StringUtils.isNotEmpty(cron)){
                clearEntity.setJobCron(cron);
            }

            if(cron_status!= null){
                clearEntity.setTriggerStatus(cron_status);
            }

            clearDao.saveNotNull(clearEntity);
        }

    }

}
