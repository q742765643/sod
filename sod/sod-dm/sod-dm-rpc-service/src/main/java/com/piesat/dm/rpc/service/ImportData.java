package com.piesat.dm.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.UUID;
import com.piesat.dm.common.codedom.CodeDOM;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.dao.dataapply.*;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.dao.database.DatabaseUserDao;
import com.piesat.dm.dao.dataclass.DataClassDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.dataclass.DatumTypeInfoDao;
import com.piesat.dm.dao.dataclass.LogicDefineDao;
import com.piesat.dm.dao.datatable.*;
import com.piesat.dm.dao.special.*;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.entity.dataapply.*;
import com.piesat.dm.entity.database.DatabaseDefineEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.DatabaseUserEntity;
import com.piesat.dm.entity.dataclass.*;
import com.piesat.dm.entity.datatable.*;
import com.piesat.dm.entity.special.*;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.dao.backup.MetaBackupDao;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.dao.mmd.ComMetadataSyncCfgDao;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.dao.sync.SyncConfigDao;
import com.piesat.schedule.dao.sync.SyncFilterDao;
import com.piesat.schedule.dao.sync.SyncMappingDao;
import com.piesat.schedule.dao.sync.SyncTaskDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.entity.sync.SyncConfigEntity;
import com.piesat.schedule.entity.sync.SyncFilterEntity;
import com.piesat.schedule.entity.sync.SyncMappingEntity;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.mapstruct.clear.ClearMapstruct;
import com.piesat.schedule.rpc.mapstruct.move.MoveMapstruct;
import com.piesat.sod.system.dao.*;
import com.piesat.sod.system.entity.*;
import com.piesat.sod.system.rpc.api.ManageFieldService;
import com.piesat.ucenter.dao.dictionary.DefineDao;
import com.piesat.ucenter.dao.dictionary.LevelDao;
import com.piesat.ucenter.dao.system.PortalAuzDao;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.dictionary.DefineEntity;
import com.piesat.ucenter.entity.dictionary.LevelEntity;
import com.piesat.ucenter.entity.system.PortalAuzEntity;
import com.piesat.ucenter.entity.system.UserEntity;
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
    private DatabaseService databaseService;
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
    @GrpcHthtClient
    private ComMetadataSyncCfgDao comMetadataSyncCfgDao;
    @Autowired
    private CloudDatabaseApplyDao cloudDatabaseApplyDao;
    @Autowired
    private DatabaseUserDao databaseUserDao;
    @GrpcHthtClient
    private PortalAuzDao portalAuzDao;
    @Autowired
    private DatabaseSpecialDao databaseSpecialDao;
    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DatabaseSpecialTreeDao databaseSpecialTreeDao;
    @Autowired
    private DatabaseSpecialAuthorityDao databaseSpecialAuthorityDao;
    @Autowired
    private DatabaseSpecialAccessDao databaseSpecialAccessDao;
    @Autowired
    private NewdataApplyDao newdataApplyDao;
    @Autowired
    private NewdataTableColumnDao newdataTableColumnDao;
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private DataAuthorityApplyDao dataAuthorityApplyDao;
    @Autowired
    private DataAuthorityRecordDao dataAuthorityRecordDao;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private TableDataStatisticsDao tableDataStatisticsDao;
    @Autowired
    private MetaBackupDao metaBackupDao;
    @Autowired
    private ManageGroupDao manageGroupDao;
    @Autowired
    private ManageFieldDao manageFieldDao;
    @Autowired
    private ManageFieldService manageFieldService;
    @Autowired
    private ManageFieldGroupDao manageFieldGroupDao;
    @Autowired
    private ServiceCodeDao serviceCodeDao;
    @Autowired
    private ServiceCodeDefineDao serviceCodeDefineDao;
    @Autowired
    private DefineDao defineDao;
    @Autowired
    private GribParameterDefineDao gribParameterDefineDao;
    @Autowired
    private LevelDao levelDao;

    @Autowired
    private BackupService backupService;
    @Autowired
    private BackupMapstruct backupMapstruct;
    @Autowired
    private MoveMapstruct moveMapstruct;
    @Autowired
    private MoveService moveService;
    @Autowired
    private ClearMapstruct clearMapstruct;
    @Autowired
    private ClearService clearService;
    @Autowired
    private DataServerBaseInfoDao dataServerBaseInfoDao;
    @Autowired
    private GridDecodingDao gridDecodingDao;
    @Autowired
    private GridAreaDao gridAreaDao;
    @Autowired
    private DataServerConfigDao dataServerConfigDao;

    @GrpcHthtClient
    private UserDao userDao;


    public void implDataClass() {
        importDataClassData();
        importDataClassLogicData();
        importDatumData();
        impLogicDefine();
        importDatabaseData();
    }

    public void implDataOther() {

        /*importSyncTask();
        importCloudDatabase();
        importBackUp();
        importMove();
        importClear();
        importDatabaseUser();
        importPortalAuz();
        importSpecial();
        importNewData();
        importDataAuthority();
        importOnLineTime();
        importDsync();
        importMetadataBackUp();
        importManagerFiled();
        importEleData();
        importEleDataDefine();
        importGridAreaDefine();
        importGridEleDecodeDefine();
        importGridLayerLevel();


        importStorageConfig();

        //importPortalUser();


        importDataServiceBaseInfo();
        importGridDecoding();
        importAreaDefine();*/
        //importGridEEle();
        //executeBackUpMove();
        importBackUp();
        //executeBackUpMove();

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
//            dte.setVersion(((BigDecimal) (m.get("VERSION"))).intValue());
            dte.setCBusinessFrequency(toString(m.get("C_BUSINESS_FREQUENCY")));
            dte.setCNettype(toString(m.get("C_NETTYPE")));
            dte.setCCoremetaId(toString(m.get("C_COREMETA_ID")));
            this.datumTypeInfoDao.save(dte);
        }
    }

    public void importSyncTask() {
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
            for (String sourceTable : sourceTables) {
                String sourceMappingID = saveSync(sourceTable, target_database_id, "K");
                if (StringUtils.isNotEmpty(sourceMappingIDs)) {
                    sourceMappingIDs = sourceMappingIDs + "," + sourceMappingID;
                } else {
                    sourceMappingIDs = sourceMappingID;
                }
            }

            String slave_tables = this.toString(m.get("SLAVE_TABLES"));
            String slaveMappingID = "";
            if (StringUtils.isNotEmpty(slave_tables)) {
                slaveMappingID = saveSync(slave_tables, target_database_id, "V");
            }

            /*String id = this.toString(m.get("TASK_ID"));
            if(StringUtils.isNotEmpty(id)){
                syncTaskEntity.setId(id);
            }*/

            String task_name = this.toString(m.get("TASK_NAME"));
            if (StringUtils.isNotEmpty(task_name)) {
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
            if (StringUtils.isNotEmpty(begin_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, begin_time);
                syncTaskEntity.setBeginTime(date);
            }

            String last_success_time = this.toString(m.get("LAST_SUCCESS_TIME"));
            if (StringUtils.isNotEmpty(last_success_time)) {
                Date Date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, last_success_time);
                syncTaskEntity.setBeginTime(Date);
            }

            String has_modify = this.toString(m.get("HAS_MODIFY"));
            syncTaskEntity.setHasModify(has_modify);

            if (StringUtils.isNotEmpty(slaveMappingID)) {
                syncTaskEntity.setSlaveTables(slaveMappingID);
            }

            String link_key = "";
            Clob clob = (Clob) m.get("LINK_KEY");
            try {
                char[] mapperChar = new char[(int) clob.length()];
                int read = clob.getCharacterStream().read(mapperChar);
                if (read > 0) {
                    link_key = String.valueOf(mapperChar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotEmpty(link_key)) {
                syncTaskEntity.setLinkKey(link_key);
            }

            Integer sync_period = Integer.valueOf(this.toString(m.get("SYNC_PERIOD")));
            syncTaskEntity.setSyncPeriod(sync_period);

            String di_off = this.toString(m.get("DI_OFF"));
            syncTaskEntity.setDiOff(di_off);

            String discard_on_duplicate = this.toString(m.get("DISCARD_ON_DUPLICATE"));
            syncTaskEntity.setDiscardOnDuplicate(discard_on_duplicate);

            String exec_ip = this.toString(m.get("EXEC_IP"));
            if (StringUtils.isNotEmpty(exec_ip)) {
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

    public String saveSync(String mappingId, String databaseId, String flag) {
        String sql = "select * from SYNC_MAPPING where RECORD_ID='" + mappingId + "'";
        List<Map> mapping = CodeDOM.getList(sql);
        String sourceMappingID = "";

        for (Map<String, Object> mapp : mapping) {

            if (flag.equals("K")) {
                source_table_name = this.toString(mapp.get("SOURCE_TABLE_NAME"));
            }

            String source_table_id = this.toString(mapp.get("SOURCE_TABLE_ID"));
            String filerIDS = "";
            //源表有过滤字段
            if (StringUtils.isNotEmpty(source_table_id)) {
                String[] filters = source_table_id.split(",");
                for (String filterId : filters) {
                    sql = "select * from sync_filter where RECORD_ID='" + filterId + "'";
                    List<Map> filterMaps = CodeDOM.getList(sql);
                    if (filterMaps != null && filterMaps.size() > 0) {
                        for (Map<String, Object> filter : filterMaps) {
                            String column_name = this.toString(filter.get("COLUMN_NAME"));
                            String filter_values = this.toString(filter.get("FILTER_VALUES"));
                            String column_oper = this.toString(filter.get("COLUMN_OPER"));
                            if (StringUtils.isNotEmpty(column_name) && StringUtils.isNotEmpty(filter_values) && StringUtils.isNotEmpty(column_oper)) {
                                SyncFilterEntity syncFilterEntity = new SyncFilterEntity();
                                syncFilterEntity.setColumnName(column_name);
                                syncFilterEntity.setFilterValues(filter_values);
                                syncFilterEntity.setColumnOper(column_oper);
                                SyncFilterEntity filterEntity = syncFilterDao.saveNotNull(syncFilterEntity);
                                if (StringUtils.isNotEmpty(filerIDS)) {
                                    filerIDS = filerIDS + "," + filterEntity.getId();
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
            if (StringUtils.isNotEmpty(target_table_id)) {
                sql = "select * from sync_config where RECORD_ID='" + target_table_id + "'";
                List<Map> configMaps = CodeDOM.getList(sql);//根据主键查询，只能查出一条
                if (configMaps != null && configMaps.size() > 0) {
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
                    if (StringUtils.isNotEmpty(d_data_id)) {
                        syncConfigEntity.setDDataId(d_data_id);
                    }
                    if (StringUtils.isNotEmpty(ifpatitions)) {
                        syncConfigEntity.setIfpatitions(ifpatitions);
                    }
                    if (StringUtils.isNotEmpty(is_kv)) {
                        syncConfigEntity.setIsKv(is_kv);
                    }
                    if (StringUtils.isNotEmpty(partition_keys)) {
                        syncConfigEntity.setPartitionKeys(partition_keys);
                    }
                    if (StringUtils.isNotEmpty(unique_keys)) {
                        syncConfigEntity.setUniqueKeys(unique_keys);
                    }
                    if (StringUtils.isNotEmpty(target_table_Id)) {
                        syncConfigEntity.setTargetTableId(target_table_Id);
                    }
                    syncConfigEntity = syncConfigDao.saveNotNull(syncConfigEntity);
                    configID = this.toString(syncConfigEntity.getId());
                }
            }


            //mapp
            String mappss = "";
            Clob clob = (Clob) mapp.get("MAPPING");
            try {
                char[] mapperChar = new char[(int) clob.length()];
                int read = clob.getCharacterStream().read(mapperChar);
                if (read > 0) {
                    mappss = String.valueOf(mapperChar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SyncMappingEntity syncMappingEntity = new SyncMappingEntity();
            syncMappingEntity.setMapping(mappss);
            if (StringUtils.isNotEmpty(filerIDS)) {
                syncMappingEntity.setSourceTableId(filerIDS);
            }
            syncMappingEntity.setSourceTableName(source_table_name);
            syncMappingEntity.setTargetTableId(configID);
            syncMappingEntity.setTargetTableName(target_table_name);
            syncMappingEntity = syncMappingDao.saveNotNull(syncMappingEntity);
            if (StringUtils.isNotEmpty(sourceMappingID)) {
                sourceMappingID = sourceMappingID + "," + syncMappingEntity.getId();
            } else {
                sourceMappingID = String.valueOf(syncMappingEntity.getId());
            }
        }

        return sourceMappingID;
    }


    public void impLogicDefine() {
        String sql = "select * from DMIN_DB_LOGIC_DEFINE";
        List<Map> logic = CodeDOM.getList(sql);
        for (Map<String, Object> m : logic) {
            sql = "select * from DMIN_DB_LOGIC_PHYSICS where LOGIC_ID = '" + m.get("LOGIC_ID") + "'";
            LogicDefineEntity dte = new LogicDefineEntity();
            dte.setLogicFlag(toString(m.get("LOGIC_ID")));
            dte.setLogicName(toString(m.get("LOGIC_NAME")));
            dte.setLogicDesc(toString(m.get("LOGIC_DESC")));
            dte.setSerialNumber(Integer.getInteger(toString(m.get("SERIAL_NUMBER"))));
            String storage_type = toString(m.get("STORAGE_TYPE"));
            String[] split = storage_type.split(",");
            Set<LogicStorageTypesEntity> ls = new HashSet<>();
            for (String s : split) {
                LogicStorageTypesEntity lst = new LogicStorageTypesEntity();
                lst.setStorageType(s);
                lst.setCreateTime(new Date());
                ls.add(lst);
            }
            dte.setLogicStorageTypesEntityList(ls);
            Set<LogicDatabaseEntity> lll = new HashSet<>();
            List<Map> lde = CodeDOM.getList(sql);
            for (Map mm : lde) {
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
            if (database_id.equals(parent_id)) {
                sql = "select * from DMIN_DB_PHYSICS_CONNECTION where DATABASE_ID = '" + database_id + "'";
                Map map = CodeDOM.getList(sql).get(0);
                dd.setId(database_id);
                if (com.piesat.common.utils.StringUtils.isNotEmpty(database_capacity))
                    dd.setDatabaseCapacity(Integer.parseInt(database_capacity));
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
            dataLogicDao.updateDatabaseId(save.getId(), database_id);
        }
    }


    //云数据库申请
    public void importCloudDatabase() {
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

            if (StringUtils.isNotEmpty(application_system)) {
                cloudDatabaseApplyEntity.setApplicationSystem(application_system);
            }
            if (StringUtils.isNotEmpty(examine_material)) {
                cloudDatabaseApplyEntity.setExamineMaterial(examine_material);
            }
            if (StringUtils.isNotEmpty(cpu_memory)) {
                cloudDatabaseApplyEntity.setCpuMemory(cpu_memory);
            }
            if (StringUtils.isNotEmpty(storage_space)) {
                cloudDatabaseApplyEntity.setStorageSpace(storage_space);
            }
            if (StringUtils.isNotEmpty(new_cpu_memory)) {
                cloudDatabaseApplyEntity.setNewCpuMemory(new_cpu_memory);
            }

            cloudDatabaseApplyEntity.setNewStorageSpace(new_storage_space);
            cloudDatabaseApplyEntity.setExamineStatus(examine_status);
            cloudDatabaseApplyEntity.setFailure_reason(failure_reason);
            cloudDatabaseApplyEntity.setExaminer(examiner);

            if (StringUtils.isNotEmpty(examine_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                cloudDatabaseApplyEntity.setExamineTime(date);
            }

            cloudDatabaseApplyEntity.setDatabaseIp(db_ip);
            if (StringUtils.isNotEmpty(db_portnum)) {
                try {
                    cloudDatabaseApplyEntity.setDatabasePort(Integer.valueOf(db_portnum));
                } catch (Exception e) {
                    cloudDatabaseApplyEntity.setDatabasePort(8080);
                }
            }
            cloudDatabaseApplyEntity.setDatabaseUsername(db_username);
            cloudDatabaseApplyEntity.setDatabasePassword(db_password);
            cloudDatabaseApplyEntity.setStorageLogic(storage_logic);
            if (StringUtils.isNotEmpty(mount_server)) {
                cloudDatabaseApplyEntity.setMountServer(mount_server);
            }
            if (StringUtils.isNotEmpty(mount_directory)) {
                cloudDatabaseApplyEntity.setMountDirectory(mount_directory);
            }
            cloudDatabaseApplyDao.saveNotNull(cloudDatabaseApplyEntity);
        }
    }

    //数据备份
    public void importBackUp() {
        String sql = "select * from USR_SOD2.DMIN_DATA_BACKUP_TASK";
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
            if (StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))) {
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String alarm = toString(m.get("ALARM"));

            Integer time_out = null;
            if (StringUtils.isNotEmpty(toString(m.get("TIME_OUT")))) {
                time_out = Integer.valueOf(toString(m.get("TIME_OUT")));
            }

            Integer retry_count = null;
            if (StringUtils.isNotEmpty(toString(m.get("RETRY_COUNT")))) {
                retry_count = Integer.valueOf(toString(m.get("RETRY_COUNT")));
            }

            Integer between_retries = null;
            if (StringUtils.isNotEmpty(toString(m.get("BETWEEN_RETRIES")))) {
                between_retries = Integer.valueOf(toString(m.get("BETWEEN_RETRIES")));
            }

            BackupEntity backupEntity = new BackupEntity();
            backupEntity.setId(m.get("TASK_ID").toString());
            backupEntity.setConditions("D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-0d}' and D_DATETIME>='{yyyy-MM-dd,-1d}'");
            backupEntity.setDataClassId(data_class_id);
            backupEntity.setDatabaseId(database_id);
            backupEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            backupEntity.setDdataId(dataClassEntity.getDDataId());
            //backupEntity.setForeignKey();
            backupEntity.setParentId(parent_id);
            backupEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName() + "_" + databaseEntity1.get(0).getDatabaseName() + "_" + dataClassEntity.getClassName());
            backupEntity.setSecondConditions("D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-1d}' and D_DATETIME>='{yyyy-MM-dd,-2d}'");
            backupEntity.setStorageDirectory("/CMADAAS/EXCHANGE/SOD/BACKUP");


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if (dataTableEntities.size() == 1) {
                backupEntity.setTableName(dataTableEntities.get(0).getTableName());
            } else {
                String tableName = "";
                for (DataTableEntity dataTableEntity : dataTableEntities) {
                    if (dataTableEntity.getDbTableType().equals("K")) {
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
               /* if(StringUtils.isNotEmpty(tableName)){
                    for(DataTableEntity dataTableEntity: dataTableEntities){
                        if(dataTableEntity.getDbTableType().equals("E")){
                            String tableNameV = dataTableEntity.getTableName();
                            backupEntity.setVTableName(tableNameV);
                            break;
                        }
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                backupEntity.setTableName(tableName);*/
            }
            //父表
            if (retry_count != null) {
                backupEntity.setExecutorFailRetryCount(retry_count);
            }
            if (time_out != null) {
                backupEntity.setExecutorTimeout(time_out);
            }
            if (StringUtils.isNotEmpty(alarm)) {
                if (alarm.equals("on")) {
                    backupEntity.setIsAlarm("1");
                } else {
                    backupEntity.setIsAlarm("0");
                }

            }
            if (StringUtils.isNotEmpty(cron)) {
                backupEntity.setJobCron(cron);
            }
            if (between_retries != null) {
                backupEntity.setRetryInterval(between_retries);
            }
            //backupEntity.setTriggerLastTime();//上次调度时间
            //backupEntity.setTriggerNextTime();//下次调度时间
            if (cron_status != null) {
                backupEntity.setTriggerStatus(cron_status);
            }

            backupService.saveBackup( backupMapstruct.toDto(backupEntity));

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
            if (StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))) {
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String source_directory = toString(m.get("SOURCE_DIRECTORY"));
            String alarm = toString(m.get("ALARM"));


            Integer timeout = null;
            if (StringUtils.isNotEmpty(toString(m.get("TIMEOUT")))) {
                timeout = Integer.valueOf(toString(m.get("TIMEOUT")));
            }


            MoveEntity moveEntity = new MoveEntity();
            moveEntity.setClearConditions("D_FILE_SAVE_HIERARCHY='1' and D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-100y}'");
            moveEntity.setConditions("D_FILE_SAVE_HIERARCHY='0' and D_DATA_ID='{ddataId}' and D_DATETIME<'{yyyy-MM-dd,-100y}'");
            moveEntity.setDataClassId(data_class_id);
            moveEntity.setDatabaseId(database_id);

            //根据存储编码查询存储元数据
            DataClassEntity dataClassEntity = this.dataClassDao.findByDataClassId(data_class_id);
            moveEntity.setDdataId(dataClassEntity.getDDataId());

            moveEntity.setIsClear("1");
            moveEntity.setMoveLimit(86400);
            moveEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName() + "_" + databaseEntity1.get(0).getDatabaseName() + "_" + dataClassEntity.getClassName());
            moveEntity.setSourceDirectory("/CMADAAS/DATA");


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if (dataTableEntities.size() == 1) {
                moveEntity.setTableName(dataTableEntities.get(0).getTableName());
            } else {
               /* String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                moveEntity.setTableName(tableName);*/
            }
            moveEntity.setTargetDirectory("/CMADAAS/EXCHANGE/SOD/MOVE");
            moveEntity.setDatabaseType(databaseEntity1.get(0).getDatabaseDefine().getDatabaseType());
            moveEntity.setParentId(physics_database);

            //父表
            if (timeout != null) {
                moveEntity.setExecutorTimeout(timeout);
            }
            if (StringUtils.isNotEmpty(alarm)) {
                if (alarm.equals("true")) {
                    moveEntity.setIsAlarm("1");
                } else {
                    moveEntity.setIsAlarm("0");
                }

            }
            if (StringUtils.isNotEmpty(cron)) {
                moveEntity.setJobCron(cron);
            }

            if (cron_status != null) {
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
            clearEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName() + "_" + databaseEntity1.get(0).getDatabaseName() + "_" + dataClassEntity.getClassName());

            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if (dataTableEntities.size() == 1) {
                clearEntity.setTableName(dataTableEntities.get(0).getTableName());
            } else {
                /*String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(dataTableEntities.size()>0 && !StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                clearEntity.setTableName(tableName);*/
            }

            clearEntity.setExecutorTimeout(2592000);//30天
            clearEntity.setIsAlarm("1");

            clearEntity.setJobCron("32 40 7 1/1 * ?");
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
            if (StringUtils.isNotEmpty(toString(m.get("CRON_STATUS")))) {
                cron_status = Integer.valueOf(toString(m.get("CRON_STATUS")));
            }

            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String source_directory = toString(m.get("SOURCE_DIRECTORY"));
            String alarm = toString(m.get("ALARM"));


            Integer timeout = null;
            if (StringUtils.isNotEmpty(toString(m.get("TIMEOUT")))) {
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
            clearEntity.setProfileName(databaseEntity1.get(0).getDatabaseDefine().getDatabaseName() + "_" + databaseEntity1.get(0).getDatabaseName() + "_" + dataClassEntity.getClassName());


            List<DataTableEntity> dataTableEntities = dataTableDao.getByDatabaseIdAndClassId(database_id, data_class_id);
            if (dataTableEntities.size() == 1) {
                clearEntity.setTableName(dataTableEntities.get(0).getTableName());
            } else {
               /* String tableName = "";
                for(DataTableEntity dataTableEntity: dataTableEntities){
                    if(dataTableEntity.getDbTableType().equals("K")){
                        tableName = dataTableEntity.getTableName();
                        break;
                    }
                }
                if(!StringUtils.isNotEmpty(tableName)){
                    tableName = dataTableEntities.get(0).getTableName();
                }
                clearEntity.setTableName(tableName);*/
            }

            if (timeout != null) {
                clearEntity.setExecutorTimeout(timeout);
            }
            if (StringUtils.isNotEmpty(alarm)) {
                if (alarm.equals("true")) {
                    clearEntity.setIsAlarm("1");
                } else {
                    clearEntity.setIsAlarm("0");
                }

            }
            if (StringUtils.isNotEmpty(cron)) {
                clearEntity.setJobCron(cron);
            }

            if (cron_status != null) {
                clearEntity.setTriggerStatus(cron_status);
            }

            clearDao.saveNotNull(clearEntity);
        }

    }

    //数据库访问账户
    public void importDatabaseUser() {

        String sql = "select * from dmin_db_account";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String user_id = toString(m.get("USER_ID"));
            String databaseup_id = toString(m.get("DATABASEUP_ID"));
            String databaseup_password = toString(m.get("DATABASEUP_PASSWORD"));
            String serial_no = toString(m.get("SERIAL_NO"));
            String database_id = toString(m.get("DATABASE_ID"));
            String databaseup_desc = toString(m.get("DATABASEUP_DESC"));
            String apply_material = toString(m.get("APPLY_MATERIAL"));
            String create_time = toString(m.get("CREATE_TIME"));
            String examiner = toString(m.get("EXAMINER"));
            String examine_status = toString(m.get("EXAMINE_STATUS"));
            String examine_time = toString(m.get("EXAMINE_TIME"));
            String failure_reason = toString(m.get("FAILURE_REASON"));
            String databaseup_ip_segment = toString(m.get("DATABASEUP_IP_SEGMENT"));
            String databaseup_ip = toString(m.get("DATABASEUP_IP"));
            String apply_database_id = toString(m.get("APPLY_DATABASE_ID"));
            String remarks = toString(m.get("REMARKS"));
            String source = toString(m.get("SOURCE"));

            DatabaseUserEntity databaseUserEntity = new DatabaseUserEntity();

            if (StringUtils.isNotEmpty(apply_database_id)) {
                databaseUserEntity.setApplyDatabaseId(apply_database_id);
            }
            if (StringUtils.isNotEmpty(apply_material)) {
                databaseUserEntity.setApplyMaterial(apply_material);
            }
            if (StringUtils.isNotEmpty(databaseup_desc)) {
                databaseUserEntity.setDatabaseUpDesc(databaseup_desc);
            }
            if (StringUtils.isNotEmpty(databaseup_id)) {
                databaseUserEntity.setDatabaseUpId(databaseup_id);
            }
            if (StringUtils.isNotEmpty(databaseup_ip)) {
                databaseUserEntity.setDatabaseUpIp(databaseup_ip);
            }
            if (StringUtils.isNotEmpty(databaseup_ip_segment)) {
                databaseUserEntity.setDatabaseUpIpSegment(databaseup_ip_segment);
            }
            if (StringUtils.isNotEmpty(databaseup_password)) {
                databaseUserEntity.setDatabaseUpPassword(databaseup_password);
            }
            if (StringUtils.isNotEmpty(database_id)) {
                databaseUserEntity.setExamineDatabaseId(database_id);
            }
            if (StringUtils.isNotEmpty(examine_status)) {
                databaseUserEntity.setExamineStatus(examine_status);
            }
            if (StringUtils.isNotEmpty(examine_time)) {
                Date Date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                databaseUserEntity.setExamineTime(Date);
            }
            if (StringUtils.isNotEmpty(examiner)) {
                databaseUserEntity.setExaminer(examiner);
            }
            if (StringUtils.isNotEmpty(failure_reason)) {
                databaseUserEntity.setFailureReason(failure_reason);
            }
            if (StringUtils.isNotEmpty(user_id)) {
                databaseUserEntity.setUserId(user_id);
            }

            databaseUserDao.saveNotNull(databaseUserEntity);
        }
    }

    //用户角色审核
    public void importPortalAuz() {
        String sql = "select * from DMIN_PORTAL_AUZ";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String account = toString(m.get("ACCOUNT"));
            String employer = toString(m.get("EMPLOYER"));
            String username = toString(m.get("USERNAME"));
            String status = toString(m.get("STATUS"));
            String createtime = toString(m.get("CREATETIME"));
            String updatetime = toString(m.get("UPDATETIME"));

            PortalAuzEntity portalAuzEntity = new PortalAuzEntity();
            portalAuzEntity.setAccount(account);
            if (StringUtils.isNotEmpty(employer)) {
                portalAuzEntity.setPost(employer);
            }
            if (StringUtils.isNotEmpty(status)) {
                portalAuzEntity.setStatus(status);
            }
            if (StringUtils.isNotEmpty(username)) {
                portalAuzEntity.setUsername(username);
            }

            portalAuzDao.saveNotNull(portalAuzEntity);
        }
    }

    //专题库审核
    public void importSpecial() {
        String sql = "select * from dmin_special_db_createapply";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String tdb_id = toString(m.get("TDB_ID"));
            String tdb_name = toString(m.get("TDB_NAME"));
            String tdb_img = toString(m.get("TDB_IMG"));
            String unit_id = toString(m.get("UNIT_ID"));
            String user_id = toString(m.get("USER_ID"));
            String apply_time = toString(m.get("APPLY_TIME"));
            String uses = toString(m.get("USES"));
            String apply_file_path = toString(m.get("APPLY_FILE_PATH"));
            String examiner = toString(m.get("EXAMINER"));
            String examine_status = toString(m.get("EXAMINE_STATUS"));
            String examine_time = toString(m.get("EXAMINE_TIME"));
            String cause = toString(m.get("CAUSE"));
            String use_status = toString(m.get("USE_STATUS"));
            String database_id = toString(m.get("DATABASE_ID"));
            String database_schema_id = toString(m.get("DATABASE_SCHEMA_ID"));
            String database_id_use = toString(m.get("DATABASE_ID_USE"));
            String sort_no = toString(m.get("SORT_NO"));

            DatabaseSpecialEntity databaseSpecialEntity = new DatabaseSpecialEntity();
            if (StringUtils.isNotEmpty(apply_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, apply_time);
                databaseSpecialEntity.setCreateTime(date);
            }
            if (StringUtils.isNotEmpty(apply_file_path)) {
                databaseSpecialEntity.setApplyMaterial(apply_file_path);
            }

            databaseSpecialEntity.setDatabaseId(database_id);
            databaseSpecialEntity.setDatabaseSchema(database_schema_id);
            databaseSpecialEntity.setExamineStatus(examine_status);
            if (StringUtils.isNotEmpty(examine_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                databaseSpecialEntity.setExamineTime(date);
            }
            if (StringUtils.isNotEmpty(examiner)) {
                databaseSpecialEntity.setExaminer(examiner);
            }
            if (StringUtils.isNotEmpty(cause)) {
                databaseSpecialEntity.setFailureReason(cause);
            }
            databaseSpecialEntity.setSdbImg(tdb_img);
            databaseSpecialEntity.setSdbName(tdb_name);
            if (StringUtils.isNotEmpty(sort_no)) {
                databaseSpecialEntity.setSortNo(sort_no);
            }
            databaseSpecialEntity.setUseStatus(use_status);
            databaseSpecialEntity.setUserId(user_id);
            databaseSpecialEntity.setUses(uses);

            databaseSpecialEntity = databaseSpecialDao.saveNotNull(databaseSpecialEntity);

            //更新数据库中的专题库id
            List<DatabaseEntity> databaseEntities = databaseDao.findByTdbId(tdb_id);
            if (databaseEntities != null && databaseEntities.size() > 0) {
                for (DatabaseEntity databaseEntity : databaseEntities) {
                    databaseEntity.setTdbId(databaseSpecialEntity.getId());
                    databaseDao.saveNotNull(databaseEntity);
                }
            }

            sql = "select * from dmin_special_db_readwrite where tdb_id='" + tdb_id + "'";
            List<Map> readwriteList = CodeDOM.getList(sql);
            if (readwriteList != null && readwriteList.size() > 0) {
                for (Map<String, Object> rwMap : readwriteList) {
                    String data_class_id = toString(rwMap.get("DATA_CLASS_ID"));
                    String logic_id = toString(rwMap.get("LOGIC_ID"));
                    String apply_authority = toString(rwMap.get("APPLY_AUTHORITY"));
                    String real_authority = toString(rwMap.get("REAL_AUTHORITY"));
                    Integer authorize = Integer.valueOf(toString(rwMap.get("AUTHORIZE")));
                    String cause1 = toString(rwMap.get("CAUSE"));
                    String type_id = toString(rwMap.get("TYPE_ID"));
                    String apply_time1 = toString(rwMap.get("APPLY_TIME"));
                    Integer data_type = Integer.valueOf(toString(rwMap.get("DATA_TYPE")));

                    DatabaseSpecialReadWriteEntity readWriteEntity = new DatabaseSpecialReadWriteEntity();

                    if (StringUtils.isNotEmpty(apply_time1)) {
                        Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, apply_time1);
                        readWriteEntity.setCreateTime(date);
                    }
                    if (StringUtils.isNotEmpty(type_id)) {
                        readWriteEntity.setTypeId(type_id);
                    }
                    if (StringUtils.isNotEmpty(apply_authority)) {
                        readWriteEntity.setApplyAuthority(Integer.valueOf(apply_authority));
                    }
                    readWriteEntity.setDataClassId(data_class_id);
                    readWriteEntity.setDataType(data_type);

                    sql = "select * from DMIN_DB_PHYSICS_DEFINE where database_id='" + logic_id + "'";
                    List<Map> databaseList = CodeDOM.getList(sql);
                    if (databaseList != null) {
                        String database_classify = toString(databaseList.get(0).get("DATABASE_CLASSIFY"));
                        String special_database_name = toString(databaseList.get(0).get("SPECIAL_DATABASE_NAME"));
                        String database_schema_name = toString(databaseList.get(0).get("DATABASE_SCHEMA_NAME"));
                        String parent_id = toString(databaseList.get(0).get("PARENT_ID"));

                        if (data_type.intValue() == 2) {//引用资料
                            if (database_classify.equals("物理库")) {
                                List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", logic_id);
                                readWriteEntity.setDatabaseId(databaseEntity1.get(0).getId());
                            } else {
                                List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                                readWriteEntity.setDatabaseId(defineId.get(0).getId());
                            }
                        } else {//私有资料
                            List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                            readWriteEntity.setDatabaseId(defineId.get(0).getId());
                        }
                    }
                    if (StringUtils.isNotEmpty(real_authority)) {
                        readWriteEntity.setEmpowerAuthority(Integer.valueOf(real_authority));
                    }
                    readWriteEntity.setExamineStatus(authorize);
                    //readWriteEntity.setExamineTime();
                    readWriteEntity.setFailureReason(cause1);
                    readWriteEntity.setSdbId(databaseSpecialEntity.getId());

                    databaseSpecialReadWriteDao.saveNotNull(readWriteEntity);

                }
            }

            sql = "select * from dmin_special_db_tree where tdb_id='" + tdb_id + "'";
            List<Map> treeList = CodeDOM.getList(sql);
            if (treeList != null && treeList.size() > 0) {
                for (Map<String, Object> treeMap : treeList) {
                    String type_id = toString(treeMap.get("TYPE_ID"));
                    String type_name = toString(treeMap.get("TYPE_NAME"));
                    String parent_id = toString(treeMap.get("PARENT_ID"));
                    String sort = toString(treeMap.get("SORT"));
                    DatabaseSpecialTreeEntity treeEntity = new DatabaseSpecialTreeEntity();
                    treeEntity.setTypeId(type_id);
                    treeEntity.setTypeName(type_name);
                    treeEntity.setParentId(parent_id);
                    if (StringUtils.isNotEmpty(sort)) {
                        treeEntity.setSort(Integer.valueOf(sort));
                    }
                    treeEntity.setSdbId(databaseSpecialEntity.getId());
                    databaseSpecialTreeDao.saveNotNull(treeEntity);
                }
            }

            sql = "select * from dmin_special_db_authority where tdb_id='" + tdb_id + "'";
            List<Map> authorityList = CodeDOM.getList(sql);
            if (authorityList != null && authorityList.size() > 0) {
                for (Map<String, Object> authorityMap : authorityList) {
                    String create_table = toString(authorityMap.get("CREATE_TABLE"));
                    String delete_table = toString(authorityMap.get("DELETE_TABLE"));
                    String table_data_access = toString(authorityMap.get("TABLE_DATA_ACCESS"));
                    String database_id1 = toString(authorityMap.get("DATABASE_ID"));
                    DatabaseSpecialAuthorityEntity authorityEntity = new DatabaseSpecialAuthorityEntity();
                    if (StringUtils.isNotEmpty(create_table)) {
                        authorityEntity.setCreateTable(Integer.valueOf(create_table));
                    }
                    authorityEntity.setDatabaseId(database_id1);
                    if (StringUtils.isNotEmpty(delete_table)) {
                        authorityEntity.setDeleteTable(Integer.valueOf(delete_table));
                    }
                    authorityEntity.setSdbId(databaseSpecialEntity.getId());
                    if (StringUtils.isNotEmpty(table_data_access)) {
                        authorityEntity.setTableDataAccess(Integer.valueOf(table_data_access));
                    }
                    databaseSpecialAuthorityDao.saveNotNull(authorityEntity);
                }
            }

            sql = "select * from dmin_special_db_accessapply where tdb_id='" + tdb_id + "'";
            List<Map> accessList = CodeDOM.getList(sql);
            if (accessList != null && accessList.size() > 0) {
                for (Map<String, Object> accessMap : accessList) {
                    String access_authority = toString(accessMap.get("ACCESS_AUTHORITY"));
                    String user_id1 = toString(accessMap.get("USER_ID"));
                    String apply_time1 = toString(accessMap.get("APPLY_TIME"));
                    String uses1 = toString(accessMap.get("USES"));
                    String examiner1 = toString(accessMap.get("EXAMINER"));
                    String examine_status1 = toString(accessMap.get("EXAMINE_STATUS"));
                    String examine_time1 = toString(accessMap.get("EXAMINE_TIME"));
                    String cause1 = toString(accessMap.get("CAUSE"));
                    String use_status1 = toString(accessMap.get("USE_STATUS"));

                    DatabaseSpecialAccessEntity accessEntity = new DatabaseSpecialAccessEntity();
                    if (StringUtils.isNotEmpty(apply_time1)) {
                        Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, apply_time1);
                        accessEntity.setCreateTime(date);
                    }
                    if (StringUtils.isNotEmpty(access_authority)) {
                        accessEntity.setAccessAuthority(Integer.valueOf(access_authority));
                    }
                    if (StringUtils.isNotEmpty(examine_status1)) {
                        accessEntity.setExamineStatus(examine_status1);
                    }
                    if (StringUtils.isNotEmpty(examine_time1)) {
                        Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time1);
                        accessEntity.setExamineTime(date);
                    }
                    if (StringUtils.isNotEmpty(examiner1)) {
                        accessEntity.setExaminer(examiner1);
                    }
                    if (StringUtils.isNotEmpty(cause1)) {
                        accessEntity.setFailureReason(cause1);
                    }
                    accessEntity.setSdbId(databaseSpecialEntity.getId());
                    if (StringUtils.isNotEmpty(use_status1)) {
                        accessEntity.setUseStatus(use_status1);
                    }
                    accessEntity.setUserId(user_id1);
                    accessEntity.setUses(uses1);
                    databaseSpecialAccessDao.saveNotNull(accessEntity);

                }
            }


        }
    }

    //数据注册审核
    public void importNewData() {
        String sql = "select * from dmin_data_newdata_apply";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String apply_id = toString(m.get("APPLY_ID"));
            String d_data_id = toString(m.get("D_DATA_ID"));
            String data_class_id = toString(m.get("DATA_CLASS_ID"));
            String table_name = toString(m.get("TABLE_NAME"));
            String logic_id = toString(m.get("LOGIC_ID"));
            String datafreq = toString(m.get("DATAFREQ"));
            String freusefield = toString(m.get("FREUSEFIELD"));
            String ispublish = toString(m.get("ISPUBLISH"));
            String memo = toString(m.get("MEMO"));
            String uses = toString(m.get("USES"));
            String file_path = toString(m.get("FILE_PATH"));
            String user_id = toString(m.get("USER_ID"));
            String apply_time = toString(m.get("APPLY_TIME"));
            String examiner = toString(m.get("EXAMINER"));
            String examine_status = toString(m.get("EXAMINE_STATUS"));
            String examine_time = toString(m.get("EXAMINE_TIME"));
            String remark = toString(m.get("REMARK"));
            String storage_create = toString(m.get("STORAGE_CREATE"));
            String dataorigin = toString(m.get("DATAORIGIN"));
            String data_service_id = toString(m.get("DATA_SERVICE_ID"));
            String data_prop = toString(m.get("DATA_PROP"));
            String database_id = toString(m.get("DATABASE_ID"));//申请时是否有database_id

            NewdataApplyEntity newdataApplyEntity = new NewdataApplyEntity();
            if (StringUtils.isNotEmpty(database_id)) {
                String database_id_new = "";
                sql = "select * from DMIN_DB_PHYSICS_DEFINE where database_id='" + database_id + "'";
                List<Map> databaseList = CodeDOM.getList(sql);
                if (databaseList != null && databaseList.size() > 0) {
                    String database_classify = toString(databaseList.get(0).get("DATABASE_CLASSIFY"));
                    String special_database_name = toString(databaseList.get(0).get("SPECIAL_DATABASE_NAME"));
                    String database_schema_name = toString(databaseList.get(0).get("DATABASE_SCHEMA_NAME"));
                    String parent_id = toString(databaseList.get(0).get("PARENT_ID"));
                    if (database_classify.equals("物理库")) {
                        List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", database_id);
                        database_id_new = databaseEntity1.get(0).getId();
                    } else {
                        List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                        database_id_new = defineId.get(0).getId();
                    }
                }
                newdataApplyEntity.setDatabaseId(database_id_new);
            }
            newdataApplyEntity.setDDataId(d_data_id);
            if (StringUtils.isNotEmpty(data_class_id)) {
                newdataApplyEntity.setDataClassId(data_class_id);
            }
            newdataApplyEntity.setDataFreq(datafreq);
            newdataApplyEntity.setDataOrigin(dataorigin);
            if (StringUtils.isNotEmpty(data_prop)) {
                newdataApplyEntity.setDataProp(data_prop);
            }
            if (StringUtils.isNotEmpty(data_service_id)) {
                newdataApplyEntity.setDataServiceId(data_service_id);
            }
            if (StringUtils.isNotEmpty(examine_status)) {
                newdataApplyEntity.setExamineStatus(Integer.valueOf(examine_status));
            }
            if (StringUtils.isNotEmpty(examine_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                newdataApplyEntity.setExamineTime(date);
            }
            if (StringUtils.isNotEmpty(examiner)) {
                newdataApplyEntity.setExaminer(examiner);
            }
            if (StringUtils.isNotEmpty(freusefield)) {
                newdataApplyEntity.setFreuseField(freusefield);
            }
            if (StringUtils.isNotEmpty(ispublish)) {
                newdataApplyEntity.setIsPublish(Integer.valueOf(ispublish));
            }
            newdataApplyEntity.setLogicId(logic_id);
            if (StringUtils.isNotEmpty(memo)) {
                newdataApplyEntity.setMemo(memo);
            }
            if (StringUtils.isNotEmpty(remark)) {
                newdataApplyEntity.setRemark(remark);
            }
            newdataApplyEntity.setTableName(table_name);
            newdataApplyEntity.setUserId(user_id);
            newdataApplyEntity.setId(UUID.randomUUID().toString().replaceAll("-",""));
            newdataApplyEntity = newdataApplyDao.saveNotNull(newdataApplyEntity);

            //新增资料字段
            sql = "select * from dmin_newdata_table_field where apply_id='" + apply_id + "'";
            List<Map> fieldList = CodeDOM.getList(sql);
            for (Map<String, Object> fieldMap : fieldList) {
                String c_element_code = toString(fieldMap.get("C_ELEMENT_CODE"));
                String ele_name = toString(fieldMap.get("ELE_NAME"));
                String type = toString(fieldMap.get("TYPE"));
                String accuracy = toString(fieldMap.get("ACCURACY"));
                String unit = toString(fieldMap.get("UNIT"));
                String is_null = toString(fieldMap.get("IS_NULL"));
                String is_premary_key = toString(fieldMap.get("IS_PREMARY_KEY"));
                String serial_number = toString(fieldMap.get("SERIAL_NUMBER"));

                NewdataTableColumnEntity newdataTableColumnEntity = new NewdataTableColumnEntity();
                newdataTableColumnEntity.setApplyId(newdataApplyEntity.getId());
                newdataTableColumnEntity.setCElementCode(c_element_code);
                newdataTableColumnEntity.setEleName(ele_name);
                newdataTableColumnEntity.setType(type);
                newdataTableColumnEntity.setAccuracy(accuracy);
                if (StringUtils.isNotEmpty(unit)) {
                    newdataTableColumnEntity.setUnit(unit);
                }
                if ("1".equals(is_null)) {
                    newdataTableColumnEntity.setIsNull(true);
                } else {
                    newdataTableColumnEntity.setIsNull(false);
                }
                if ("1".equals(is_premary_key)) {
                    newdataTableColumnEntity.setIsPrimaryKey(true);
                } else {
                    newdataTableColumnEntity.setIsPrimaryKey(false);
                }
                newdataTableColumnEntity.setSerialNumber(Integer.valueOf(serial_number));
                newdataTableColumnDao.saveNotNull(newdataTableColumnEntity);
            }


            //保存存储策略
            /*if(StringUtils.isNotEmpty(data_class_id)){
                sql = "select * from dmin_storage_configuration where data_class_id='"+data_class_id+"'";
                List<Map> storageList = CodeDOM.getList(sql);
                for (Map<String, Object> storageMap : storageList) {
                    String logic_id1 = toString(storageMap.get("LOGIC_ID"));
                    String database_id1 = toString(storageMap.get("DATABASE_ID"));
                    String storage_define_identifier = toString(storageMap.get("STORAGE_DEFINE_IDENTIFIER"));
                    String data_sync_identifier = toString(storageMap.get("DATA_SYNC_IDENTIFIER"));
                    String data_moveclean_identifier = toString(storageMap.get("DATA_MOVECLEAN_IDENTIFIER"));
                    String data_backup_identifier = toString(storageMap.get("DATA_BACKUP_IDENTIFIER"));
                    String data_archiving_identifier = toString(storageMap.get("DATA_ARCHIVING_IDENTIFIER"));
                    String sync_id = toString(storageMap.get("SYNC_ID"));
                    String clear_id = toString(storageMap.get("CLEAR_ID"));
                    String backup_id = toString(storageMap.get("BACKUP_ID"));
                    //查出来data_logic_id
                    String data_logic_id = "";
                    //database_id1对应新库的id
                    String database_id_new = "";
                    sql = "select * from DMIN_DB_PHYSICS_DEFINE where database_id='"+database_id1+"'";
                    List<Map> databaseList = CodeDOM.getList(sql);
                    if(databaseList != null && databaseList.size() > 0){
                        String database_classify = toString(databaseList.get(0).get("DATABASE_CLASSIFY"));
                        String  special_database_name = toString(databaseList.get(0).get("SPECIAL_DATABASE_NAME"));
                        String  database_schema_name = toString(databaseList.get(0).get("DATABASE_SCHEMA_NAME"));
                        String  parent_id = toString(databaseList.get(0).get("PARENT_ID"));
                        if(database_classify.equals("物理库")){
                            List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", database_id1);
                            database_id_new = databaseEntity1.get(0).getId();
                        }else{
                            List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                            database_id_new = defineId.get(0).getId();
                        }
                    }

                    List<DataLogicEntity> dataLogicEntities = dataLogicDao.findByDataClassIdAndDatabaseIdAndLogicFlag(data_class_id, database_id_new, logic_id1);
                    if(dataLogicEntities != null && dataLogicEntities.size()>0){
                        data_logic_id = dataLogicEntities.get(0).getId();
                    }

                    StorageConfigurationEntity storageConfigurationEntity = new StorageConfigurationEntity();
                    if(StringUtils.isNotEmpty(data_logic_id)){
                        storageConfigurationEntity.setClassLogicId(data_logic_id);
                    }
                    if(StringUtils.isNotEmpty(storage_define_identifier)){
                        storageConfigurationEntity.setStorageDefineIdentifier(Integer.valueOf(storage_define_identifier));
                    }
                    if(StringUtils.isNotEmpty(data_sync_identifier)){
                        storageConfigurationEntity.setSyncIdentifier(Integer.valueOf(data_sync_identifier));
                    }
                    if(StringUtils.isNotEmpty(data_moveclean_identifier)){
                        storageConfigurationEntity.setMoveIdentifier(Integer.valueOf(data_moveclean_identifier));
                        storageConfigurationEntity.setCleanIdentifier(Integer.valueOf(data_moveclean_identifier));
                    }
                    if(StringUtils.isNotEmpty(data_backup_identifier)){
                        storageConfigurationEntity.setBackupIdentifier(Integer.valueOf(data_backup_identifier));
                    }
                    if(StringUtils.isNotEmpty(data_archiving_identifier)){
                        storageConfigurationEntity.setArchivingIdentifier(Integer.valueOf(data_archiving_identifier));
                    }
                    storageConfigurationDao.saveNotNull(storageConfigurationEntity);
                }
            }*/
        }
    }

    //数据访问权限审核
    public void importDataAuthority() {
        String sql = "select * from dmin_db_dataauthority_apply";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String apply_id = toString(m.get("APPLY_ID"));
            String user_id = toString(m.get("USER_ID"));
            String apply_time = toString(m.get("APPLY_TIME"));
            String audit_status = toString(m.get("AUDIT_STATUS"));
            String examiner = toString(m.get("EXAMINER"));
            String examine_time = toString(m.get("EXAMINE_TIME"));

            DataAuthorityApplyEntity dataAuthorityApplyEntity = new DataAuthorityApplyEntity();

            dataAuthorityApplyEntity.setAuditStatus(audit_status);
            if (StringUtils.isNotEmpty(examine_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, examine_time);
                dataAuthorityApplyEntity.setExamineTime(date);
            }
            if (StringUtils.isNotEmpty(examiner)) {
                dataAuthorityApplyEntity.setExaminer(examiner);
            }
            dataAuthorityApplyEntity.setUserId(user_id);
            //dataAuthorityApplyEntity = dataAuthorityApplyDao.saveNotNull(dataAuthorityApplyEntity);

            sql = "select * from dmin_db_dataauthority where apply_id='" + apply_id + "'";
            List<Map> recordList = CodeDOM.getList(sql);
            if (recordList != null && recordList.size() > 0) {
                for (Map<String, Object> recordMap : recordList) {
                    String data_class_id = toString(recordMap.get("DATA_CLASS_ID"));
                    String database_id = toString(recordMap.get("DATABASE_ID"));
                    String apply_authority = toString(recordMap.get("APPLY_AUTHORITY"));
                    String authorize = toString(recordMap.get("AUTHORIZE"));
                    String cause = toString(recordMap.get("CAUSE"));
                    String qtdb_id = toString(recordMap.get("QTDB_ID"));

                    DataAuthorityRecordEntity dataAuthorityRecordEntity = new DataAuthorityRecordEntity();
                    if (StringUtils.isNotEmpty(apply_authority)) {
                        dataAuthorityRecordEntity.setApplyAuthority(Integer.valueOf(apply_authority));
                    }
                    if (StringUtils.isNotEmpty(authorize)) {
                        dataAuthorityRecordEntity.setAuthorize(Integer.valueOf(authorize));
                    }
                    if (StringUtils.isNotEmpty(cause)) {
                        dataAuthorityRecordEntity.setCause(cause);
                    }
                    dataAuthorityRecordEntity.setDataClassId(data_class_id);
                    if (StringUtils.isNotEmpty(database_id)) {
                        String database_id_new = "";
                        sql = "select * from DMIN_DB_PHYSICS_DEFINE where database_id='" + database_id + "'";
                        List<Map> databaseList = CodeDOM.getList(sql);
                        if (databaseList != null && databaseList.size() > 0) {
                            String database_classify = toString(databaseList.get(0).get("DATABASE_CLASSIFY"));
                            String special_database_name = toString(databaseList.get(0).get("SPECIAL_DATABASE_NAME"));
                            String database_schema_name = toString(databaseList.get(0).get("DATABASE_SCHEMA_NAME"));
                            String parent_id = toString(databaseList.get(0).get("PARENT_ID"));
                            if (database_classify.equals("物理库")) {
                                List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", database_id);
                                database_id_new = databaseEntity1.get(0).getId();
                            } else {
                                List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                                database_id_new = defineId.get(0).getId();
                                dataAuthorityRecordEntity.setQtdbId(defineId.get(0).getTdbId());
                            }
                        }
                        if (StringUtils.isNotEmpty(database_id_new)) {
                            dataAuthorityRecordEntity.setDatabaseId(database_id_new);
                        }
                    }
                    dataAuthorityApplyEntity.getDataAuthorityRecordList().add(dataAuthorityRecordEntity);
                    //dataAuthorityRecordDao.saveNotNull(dataAuthorityRecordEntity);
                }
            }
            dataAuthorityApplyEntity = dataAuthorityApplyDao.saveNotNull(dataAuthorityApplyEntity);
        }
    }

    //在线时间检索
    public void importOnLineTime() {
        String sql = "select * from DMIN_DATA_TABLE_COLLECT_INFO  where statistic_time>'2020-05-01'";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String database_id = toString(m.get("DATABASE_ID"));
            String table_id = toString(m.get("TABLE_ID"));
            String statistic_date = toString(m.get("STATISTIC_DATE"));
            String begin_time = toString(m.get("BEGIN_TIME"));
            String end_time = toString(m.get("END_TIME"));
            String record_count = toString(m.get("RECORD_COUNT"));
            String day_total = toString(m.get("DAY_TOTAL"));
            String statistic_time = toString(m.get("STATISTIC_TIME"));

            TableDataStatisticsEntity tableDataStatisticsEntity = new TableDataStatisticsEntity();
            if (StringUtils.isNotEmpty(begin_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, begin_time);
                tableDataStatisticsEntity.setBeginTime(date);
            }
            String database_id_new = getNewDatabaseId(database_id);
            tableDataStatisticsEntity.setDatabaseId(database_id_new);
            if (StringUtils.isNotEmpty(day_total)) {
                tableDataStatisticsEntity.setDayTotal(Integer.valueOf(day_total));
            }
            if (StringUtils.isNotEmpty(end_time)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, end_time);
                tableDataStatisticsEntity.setEndTime(date);
            }
            if (StringUtils.isNotEmpty(record_count)) {
                tableDataStatisticsEntity.setRecordCount(Double.valueOf(record_count));
            }
            if (StringUtils.isNotEmpty(statistic_date)) {
                Date date = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, statistic_date);
                tableDataStatisticsEntity.setStatisticDate(date);
            }
            if (StringUtils.isNotEmpty(statistic_time)) {
                tableDataStatisticsEntity.setStatisticTime(statistic_time);
            }
            sql = "select * from DMIN_DATA_ID_TABLE where table_id='" + table_id + "'";
            List<Map> tableList = CodeDOM.getList(sql);
            if (tableList != null && tableList.size() > 0) {
                String data_class_id = toString(tableList.get(0).get("DATA_CLASS_ID"));
                String table_name = toString(tableList.get(0).get("TABLE_NAME"));
                List<Map<String, Object>> tableName = dataTableService.getByDatabaseIdAndTableName(database_id_new, table_name);
                if (tableName != null && tableName.size() > 0) {
                    for (Map<String, Object> map : tableName) {
                        tableDataStatisticsEntity.setId(null);
                        tableDataStatisticsEntity.setTableId(toString(map.get("ID")));
                        tableDataStatisticsDao.saveNotNull(tableDataStatisticsEntity);
                    }
                }
            }
        }
    }

    public String getNewDatabaseId(String database_id_old) {
        String database_id_new = "";
        String sql = "select * from DMIN_DB_PHYSICS_DEFINE where database_id='" + database_id_old + "'";
        List<Map> databaseList = CodeDOM.getList(sql);
        if (databaseList != null && databaseList.size() > 0) {
            String database_classify = toString(databaseList.get(0).get("DATABASE_CLASSIFY"));
            String special_database_name = toString(databaseList.get(0).get("SPECIAL_DATABASE_NAME"));
            String database_schema_name = toString(databaseList.get(0).get("DATABASE_SCHEMA_NAME"));
            String parent_id = toString(databaseList.get(0).get("PARENT_ID"));
            if (database_classify.equals("物理库")) {
                List<DatabaseEntity> databaseEntity1 = databaseDao.findByDatabaseClassifyAndDatabaseDefineId("物理库", database_id_old);
                database_id_new = databaseEntity1.get(0).getId();
            } else {
                List<DatabaseEntity> defineId = databaseDao.findByDatabaseClassifyAndDatabaseNameAndSchemaNameAndDatabaseDefineId(database_classify, special_database_name, database_schema_name, parent_id);
                database_id_new = defineId.get(0).getId();
            }
        }
        return database_id_new;
    }

    //公共元数据同步
    public void importDsync() {
        String sql = "select * from dmin_dsync_commetadata_configure";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String task_name = toString(m.get("TASK_NAME"));
            String table_name = toString(m.get("TABLE_NAME"));
            String interface_url = toString(m.get("INTERFACE_URL"));
            String interface_type = toString(m.get("INTERFACE_TYPE"));
            String start_time = toString(m.get("START_TIME"));
            String interface_datakey = toString(m.get("INTERFACE_DATAKEY"));
            String start_time_unit = toString(m.get("START_TIME_UNIT"));
            String primary_key = toString(m.get("PRIMARY_KEY"));

            ComMetadataSyncCfgEntity comMetadataSyncCfgEntity = new ComMetadataSyncCfgEntity();
            comMetadataSyncCfgEntity.setApiDataKey(interface_datakey);
            comMetadataSyncCfgEntity.setApiType(interface_type);
            comMetadataSyncCfgEntity.setApiUrl(interface_url);
            if (StringUtils.isNotEmpty(primary_key)) {
                comMetadataSyncCfgEntity.setPrimaryKey(primary_key);
            }
            comMetadataSyncCfgEntity.setStartTime(start_time);
            if (StringUtils.isNotEmpty(start_time_unit)) {
                comMetadataSyncCfgEntity.setStartTimeUnit(start_time_unit);
            }
            comMetadataSyncCfgEntity.setTaskName(task_name);
            comMetadataSyncCfgEntity.setTableName(table_name);

            comMetadataSyncCfgEntity.setJobCron(start_time);
            comMetadataSyncCfgDao.saveNotNull(comMetadataSyncCfgEntity);
        }
    }

    //系统元数据备份
    public void importMetadataBackUp() {
        String sql = "select * from DMIN_METADATA_BACKUP";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String task_name = toString(m.get("TASK_NAME"));
            String storage_directory = toString(m.get("STORAGE_DIRECTORY"));
            String is_structure = toString(m.get("IS_STRUCTURE"));
            String create_time = toString(m.get("CREATE_TIME"));
            String update_time = toString(m.get("UPDATE_TIME"));
            String cron = toString(m.get("CRON"));
            String cron_status = toString(m.get("CRON_STATUS"));
            String exec_ip = toString(m.get("EXEC_IP"));
            String exec_port = toString(m.get("EXEC_PORT"));
            String database_id = toString(m.get("DATABASE_ID"));

            String back_content = "";
            Clob clob = (Clob) m.get("BACK_CONTENT");
            try {
                char[] mapperChar = new char[(int) clob.length()];
                int read = clob.getCharacterStream().read(mapperChar);
                if (read > 0) {
                    back_content = String.valueOf(mapperChar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            MetaBackupEntity metaBackupEntity = new MetaBackupEntity();
            if (StringUtils.isNotEmpty(back_content)) {
                metaBackupEntity.setBackContent(back_content);
            }
            if (StringUtils.isNotEmpty(database_id)) {
                String newDatabaseId = getNewDatabaseId(database_id);
                metaBackupEntity.setDatabaseId(newDatabaseId);
                Optional<DatabaseEntity> databaseEntity = databaseDao.findById(newDatabaseId);
                DatabaseDto databaseDto = databaseService.getDotById(newDatabaseId);
                metaBackupEntity.setDatabaseName(databaseDto.getDatabaseDefine().getDatabaseName() + "_" + databaseDto.getDatabaseName());
                metaBackupEntity.setDatabaseType(databaseDto.getDatabaseDefine().getDatabaseType());
                metaBackupEntity.setParentId(databaseDto.getDatabaseDefine().getId());
            }
            metaBackupEntity.setIsStructure(is_structure);
            metaBackupEntity.setStorageDirectory(storage_directory);
            metaBackupEntity.setTaskName(task_name);
            metaBackupEntity.setJobCron(cron);
            if (StringUtils.isNotEmpty(cron_status)) {
                metaBackupEntity.setTriggerStatus(Integer.valueOf(cron_status));
            }
            metaBackupDao.saveNotNull(metaBackupEntity);
        }
    }

    //管理字段管理
    public void importManagerFiled() {
        String sql = "select * from dmin_db_manager_fieldgroup";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String  group_id = toString(m.get("GROUP_ID"));
            String  group_name = toString(m.get("GROUP_NAME"));
            ManageGroupEntity manageGroupEntity = new ManageGroupEntity();
            manageGroupEntity.setGroupName(group_name);
            manageGroupDao.saveNotNull(manageGroupEntity);
        }

        sql = "select * from dmin_db_manager_field";
        List<Map> fieldList = CodeDOM.getList(sql);
        for (Map<String, Object> m : fieldList) {
            String  db_ele_code = toString(m.get("DB_ELE_CODE"));
            String  user_ele_code = toString(m.get("USER_ELE_CODE"));
            String  db_ele_name = toString(m.get("DB_ELE_NAME"));
            String  ele_name = toString(m.get("ELE_NAME"));
            String  type = toString(m.get("TYPE"));
            String  data_precision1 = toString(m.get("DATA_PRECISION1"));
            String  length = toString(m.get("LENGTH"));
            String  is_null = toString(m.get("IS_NULL"));
            String  is_update = toString(m.get("IS_UPDATE"));
            String  data_precision = toString(m.get("DATA_PRECISION"));
            ManageFieldEntity manageFieldEntity = new ManageFieldEntity();

            if(StringUtils.isNotEmpty(data_precision)){
                manageFieldEntity.setDataPrecision(Integer.valueOf(data_precision));
            }
            if(StringUtils.isNotEmpty(data_precision1)){
                manageFieldEntity.setDataPrecision1(Integer.valueOf(data_precision1));
            }
            manageFieldEntity.setDbEleCode(db_ele_code);
            manageFieldEntity.setDbEleName(db_ele_name);
            if(StringUtils.isNotEmpty(ele_name)){
                manageFieldEntity.setEleName(ele_name);
            }
            manageFieldEntity.setLength(Integer.valueOf(length));
            if("true".equals(is_null)){
                manageFieldEntity.setNullAble(true);
            }else{
                manageFieldEntity.setNullAble(false);
            }
            manageFieldEntity.setType(type);
            if("true".equals(is_update)){
                manageFieldEntity.setUpdateAble(true);
            }else{
                manageFieldEntity.setUpdateAble(false);
            }
            manageFieldEntity.setUserEleCode(user_ele_code);
            manageFieldDao.saveNotNull(manageFieldEntity);
        }
        ManageGroupEntity manageGroupEntity = manageGroupDao.findByGroupName("常用管理字段");
        List<ManageFieldEntity> all = manageFieldDao.findAll();
        for(ManageFieldEntity manageFieldEntity : all){
            ManageFieldGroupEntity manageFieldGroupEntity = new ManageFieldGroupEntity();
            manageFieldGroupEntity.setGroupId(manageGroupEntity.getGroupId());
            manageFieldGroupEntity.setFieldId(manageFieldEntity.getId());
            manageFieldGroupDao.saveNotNull(manageFieldGroupEntity);
        }
    }

    //服务代码管理
    public void importEleData() {
        String sql = "select * from g_api_data_ele_define";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String user_ele_code = toString(m.get("USER_ELE_CODE"));
            String db_ele_code = toString(m.get("DB_ELE_CODE"));
            String ele_name = toString(m.get("ELE_NAME"));
            String description = toString(m.get("DESCRIPTION"));
            String has_sod = toString(m.get("HAS_SOD"));
            String user_ele_code_1 = toString(m.get("USER_ELE_CODE_1"));
            String ele_unit = toString(m.get("ELE_UNIT"));
            String is_code_param = toString(m.get("IS_CODE_PARAM"));
            String code_table_id = toString(m.get("CODE_TABLE_ID"));

            ServiceCodeEntity serviceCodeEntity = new ServiceCodeEntity();
            if (StringUtils.isNotEmpty(code_table_id)) {
                serviceCodeEntity.setCodeTableId(code_table_id);
            }
            if (StringUtils.isNotEmpty(db_ele_code)) {
                serviceCodeEntity.setDbEleCode(db_ele_code);
            }
            if (StringUtils.isNotEmpty(description)) {
                serviceCodeEntity.setDescription(description);
            }
            if (StringUtils.isNotEmpty(ele_name)) {
                serviceCodeEntity.setEleName(ele_name);
            }
            if (StringUtils.isNotEmpty(ele_unit)) {
                serviceCodeEntity.setEleUnit(ele_unit);
            }
            if (StringUtils.isNotEmpty(has_sod)) {
                serviceCodeEntity.setHasSod(has_sod);
            }
            if (StringUtils.isNotEmpty(is_code_param)) {
                serviceCodeEntity.setIsCodeParam(is_code_param);
            }
            if (StringUtils.isNotEmpty(user_ele_code)) {
                serviceCodeEntity.setUserEleCode(user_ele_code);
            }
            serviceCodeDao.saveNotNull(serviceCodeEntity);
        }
    }

    //服务代码定义
    public void importEleDataDefine() {
        String sql = "select * from dmin_grid_ele_service_define";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String user_fcst_ele = toString(m.get("USER_FCST_ELE"));
            String db_fcst_ele = toString(m.get("DB_FCST_ELE"));
            String ele_property_name = toString(m.get("ELE_PROPERTY_NAME"));
            String ele_unit = toString(m.get("ELE_UNIT"));
            String ele_name = toString(m.get("ELE_NAME"));

            ServiceCodeDefineEntity serviceCodeDefineEntity = new ServiceCodeDefineEntity();
            if (StringUtils.isNotEmpty(db_fcst_ele)) {
                serviceCodeDefineEntity.setDbFcstEle(db_fcst_ele);
            }
            if (StringUtils.isNotEmpty(ele_name)) {
                serviceCodeDefineEntity.setEleName(ele_name);
            }
            if (StringUtils.isNotEmpty(ele_property_name)) {
                serviceCodeDefineEntity.setElePropertyName(ele_property_name);
            }
            if (StringUtils.isNotEmpty(ele_unit)) {
                serviceCodeDefineEntity.setEleUnit(ele_unit);
            }
            if (StringUtils.isNotEmpty(user_fcst_ele)) {
                serviceCodeDefineEntity.setUserFcstEle(user_fcst_ele);
            }
            serviceCodeDefineDao.saveNotNull(serviceCodeDefineEntity);
        }
    }

    //区域类别管理
    public void importGridAreaDefine() {
        String sql = "select * from dmin_grid_area_define_dir";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String area_id = toString(m.get("AREA_ID"));
            String start_lat = toString(m.get("START_LAT"));
            String end_lat = toString(m.get("END_LAT"));
            String start_lon = toString(m.get("START_LON"));
            String end_lon = toString(m.get("END_LON"));
            String area_desc = toString(m.get("AREA_DESC"));

            DefineEntity defineEntity = new DefineEntity();
            if (StringUtils.isNotEmpty(area_desc)) {
                defineEntity.setAreaDesc(area_desc);
            }
            if (StringUtils.isNotEmpty(area_id)) {
                defineEntity.setAreaId(area_id);
            }
            if (StringUtils.isNotEmpty(end_lat)) {
                defineEntity.setEndLat(Double.valueOf(end_lat));
            }
            if (StringUtils.isNotEmpty(end_lon)) {
                defineEntity.setEndLon(Double.valueOf(end_lon));
            }
            if (StringUtils.isNotEmpty(start_lat)) {
                defineEntity.setStartLat(Double.valueOf(start_lat));
            }
            if (StringUtils.isNotEmpty(start_lon)) {
                defineEntity.setStartLon(Double.valueOf(start_lon));
            }
            defineDao.saveNotNull(defineEntity);
        }
    }

    //grib参数定义
    public void importGridEleDecodeDefine() {
        String sql = "select * from dmin_grid_ele_decode_define";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String ele_code_short = toString(m.get("ELE_CODE_SHORT"));
            String subject_id = toString(m.get("SUBJECT_ID"));
            String classify = toString(m.get("CLASSIFY"));
            String parameter_id = toString(m.get("PARAMETER_ID"));
            String grib_version = toString(m.get("GRIB_VERSION"));
            String element_cn = toString(m.get("ELEMENT_CN"));
            String public_config = toString(m.get("PUBLIC_CONFIG"));
            String template_id = toString(m.get("TEMPLATE_ID"));
            String template_desc = toString(m.get("TEMPLATE_DESC"));

            GribParameterDefineEntity gribParameterDefineEntity = new GribParameterDefineEntity();
            if (StringUtils.isNotEmpty(classify)) {
                gribParameterDefineEntity.setClassify(Integer.valueOf(classify));
            }
            if (StringUtils.isNotEmpty(ele_code_short)) {
                gribParameterDefineEntity.setEleCodeShort(ele_code_short);
            }
            if (StringUtils.isNotEmpty(element_cn)) {
                gribParameterDefineEntity.setElementCn(element_cn);
            }
            if (StringUtils.isNotEmpty(grib_version)) {
                gribParameterDefineEntity.setGribVersion(Integer.valueOf(grib_version));
            }
            if (StringUtils.isNotEmpty(parameter_id)) {
                gribParameterDefineEntity.setParameterId(Integer.valueOf(parameter_id));
            }
            if (StringUtils.isNotEmpty(public_config)) {
                gribParameterDefineEntity.setPublicConfig(public_config);
            }
            if (StringUtils.isNotEmpty(subject_id)) {
                gribParameterDefineEntity.setSubjectId(Integer.valueOf(subject_id));
            }
            if (StringUtils.isNotEmpty(template_desc)) {
                gribParameterDefineEntity.setTemplateDesc(template_desc);
            }
            if (StringUtils.isNotEmpty(template_id)) {
                gribParameterDefineEntity.setTemplateId(template_id);
            }
            gribParameterDefineDao.saveNotNull(gribParameterDefineEntity);
        }
    }

    //层次属性管理
    public void importGridLayerLevel() {
        String sql = "select * from dmin_grid_layer_level";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String grib_version = toString(m.get("GRIB_VERSION"));
            String level_type = toString(m.get("LEVEL_TYPE"));
            String level_code = toString(m.get("LEVEL_CODE"));
            String scale_divisor = toString(m.get("SCALE_DIVISOR"));
            String level_properity = toString(m.get("LEVEL_PROPERITY"));
            String level_name = toString(m.get("LEVEL_NAME"));
            String unit = toString(m.get("UNIT"));

            LevelEntity levelEntity = new LevelEntity();
            if (StringUtils.isNotEmpty(grib_version)) {
                levelEntity.setGribVersion(Integer.valueOf(grib_version));
            }
            if (StringUtils.isNotEmpty(level_code)) {
                levelEntity.setLevelCode(level_code);
            }
            if (StringUtils.isNotEmpty(level_name)) {
                levelEntity.setLevelName(level_name);
            }
            if (StringUtils.isNotEmpty(level_properity)) {
                levelEntity.setLevelProperity(level_properity);
            }
            if (StringUtils.isNotEmpty(level_type)) {
                levelEntity.setLevelType(Integer.valueOf(level_type));
            }
            if (StringUtils.isNotEmpty(scale_divisor)) {
                levelEntity.setScaleDivisor(scale_divisor);
            }
            if (StringUtils.isNotEmpty(unit)) {
                levelEntity.setUnit(unit);
            }
            levelDao.saveNotNull(levelEntity);
        }
    }

    public void executeBackUpMove() {
        List<BackupEntity> backupEntityList = backupDao.findAll();
        List<BackUpDto> backUpDtoList = backupMapstruct.toDto(backupEntityList);
        for (BackUpDto backUpDto : backUpDtoList) {
            try {
                backupService.updateBackup(backUpDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<MoveEntity> moveEntityList = moveDao.findAll();
        List<MoveDto> moveDtoList = moveMapstruct.toDto(moveEntityList);
        for (MoveDto moveDto : moveDtoList) {
            try {
                moveService.updateMove(moveDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<ClearEntity> clearEntityList = clearDao.findAll();
        List<ClearDto> clearDtoList = clearMapstruct.toDto(clearEntityList);
        for (ClearDto clearDto : clearDtoList) {
            try {
                clearService.updateClear(clearDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void importStorageConfig() {
        List<DataLogicEntity> dataLogicEntities = dataLogicDao.findAll();
        for (DataLogicEntity dataLogicEntity : dataLogicEntities) {
            String storageType = dataLogicEntity.getStorageType();


            StorageConfigurationEntity storageConfigurationEntity = new StorageConfigurationEntity();
            storageConfigurationEntity.setClassLogicId(dataLogicEntity.getId());

            List<DataTableEntity> dataTableEntities = dataTableDao.findByClassLogic_Id(dataLogicEntity.getId());
            if (dataTableEntities != null && dataTableEntities.size() > 0) {
                //存储策略
                storageConfigurationEntity.setStorageDefineIdentifier(1);

                for (DataTableEntity dataTableEntity : dataTableEntities) {
                    String table_id = dataTableEntity.getId();
                    String dbTableType = dataTableEntity.getDbTableType();
                    if (storageType.contains("K")) {
                        if (dbTableType.equals("E")) {
                            continue;
                        }
                    }

                    //同步
                    List<SyncConfigEntity> syncConfigEntities = syncConfigDao.findByTargetTableId(table_id);
                    if (syncConfigEntities != null && syncConfigEntities.size() > 0) {
                        List<SyncMappingEntity> syncMappingEntities = syncMappingDao.findAllByTargetTableIdIn(Arrays.asList(String.valueOf(syncConfigEntities.get(0).getId()).split(",")));
                        SyncTaskEntity syncTaskEntity = syncTaskDao.findBySourceTable(String.valueOf(syncMappingEntities.get(0).getId()));
                        if (syncTaskEntity != null) {
                            storageConfigurationEntity.setSyncIdentifier(1);
                            storageConfigurationEntity.setSyncId(syncTaskEntity.getId());
                        }
                    }


                }
            }
            //清除
            List<ClearEntity> clearEntities = clearDao.findByDatabaseIdAndDataClassId(dataLogicEntity.getDatabaseId(), dataLogicEntity.getDataClassId());
            if (clearEntities != null && clearEntities.size() > 0) {
                storageConfigurationEntity.setCleanIdentifier(1);
                storageConfigurationEntity.setClearId(clearEntities.get(0).getId());
            }
            //迁移
            List<MoveEntity> moveEntities = moveDao.findByDatabaseIdAndDataClassId(dataLogicEntity.getDatabaseId(), dataLogicEntity.getDataClassId());
            if (moveEntities != null && moveEntities.size() > 0) {
                storageConfigurationEntity.setMoveIdentifier(1);
                storageConfigurationEntity.setMoveId(moveEntities.get(0).getId());
            }
            //备份
            List<BackupEntity> backupEntities = backupDao.findByDatabaseIdAndDataClassId(dataLogicEntity.getDatabaseId(), dataLogicEntity.getDataClassId());
            if (backupEntities != null && backupEntities.size() > 0) {
                storageConfigurationEntity.setBackupIdentifier(1);
                storageConfigurationEntity.setBackupId(backupEntities.get(0).getId());
            }

            storageConfigurationDao.saveNotNull(storageConfigurationEntity);
        }

    }

    public void importPortalUser() {
        String sql = "select * from ts_user";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String user_id = toString(m.get("USER_ID"));
            String user_name = toString(m.get("USER_NAME"));
            String login_name = toString(m.get("LOGIN_NAME"));
            String password = toString(m.get("PASSWORD"));
            String phone = toString(m.get("PHONE"));
            String deptunicode = toString(m.get("DEPTUNICODE"));
            String deptName = "";
            sql = "select deptname from TS_DEPARTMENT where deptunicode='" + deptunicode + "'";
            List<Map> deptnameList = CodeDOM.getList(sql);
            if (deptnameList != null && deptnameList.size() > 0) {
                deptName = toString(deptnameList.get(0).get("DEPTNAME"));
            }
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(user_id);
            userEntity.setWebUserId(user_id);
            userEntity.setUserType("11");
            userEntity.setDeptName(deptName);
            userEntity.setWebUsername(user_name);
            userEntity.setNickName(login_name);
            userEntity.setTutorPhone(phone);
            userDao.saveNotNull(userEntity);
        }
    }


    public void importDataServiceBaseInfo() {
        String sql = "select * from DMIN_DATASERVICE_BASEINFOR";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String dataCLassId = toString(m.get("DATACLASSID"));
            String eleField = toString(m.get("ELEFIELD"));
            String regionField = toString(m.get("REGIONFIELD"));
            String region = toString(m.get("REGION"));
            String gribVersion = toString(m.get("GRIBVERSION"));
            String fieldType = toString(m.get("FIELDTYPE"));
            String processType = toString(m.get("PROCESSTYPE"));
            String dataTime = toString(m.get("DATATIME"));
            String timeUnit = toString(m.get("TIMEUNIT"));
            String spatialResolution = toString(m.get("SPATIALRESOLUTION"));

            DataServerBaseInfoEntity d = new DataServerBaseInfoEntity();

            d.setDataCLassId(dataCLassId);
            d.setDataTime(dataTime);
            d.setEleField(eleField);
            d.setFieldType(fieldType);
            d.setGribVersion(gribVersion);
            d.setProcessType(processType);
            d.setRegion(region);
            d.setRegionField(regionField);
            d.setSpatialResolution(spatialResolution);
            d.setTimeUnit(timeUnit);
            d.setCreateTime(new Date());
            dataServerBaseInfoDao.saveNotNull(d);
        }
    }

    public void importGridDecoding() {
        String sql = "select * from dmin_grid_decoding_config";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String grid_decode_id = toString(m.get("GRID_DECODE_ID"));
            String data_service_id = toString(m.get("DATA_SERVICE_ID"));
            String data_dpc_id = toString(m.get("DATA_DPC_ID"));
            String ele_code_short = toString(m.get("ELE_CODE_SHORT"));
            String subject_id = toString(m.get("SUBJECT_ID"));
            String classify = toString(m.get("CLASSIFY"));
            String parameter_id = toString(m.get("PARAMETER_ID"));
            String grib_version = toString(m.get("GRIB_VERSION"));
            String element_cn = toString(m.get("ELEMENT_CN"));
            String public_config = toString(m.get("PUBLIC_CONFIG"));
            String template_id = toString(m.get("TEMPLATE_ID"));

            GridDecodingEntity g = new GridDecodingEntity();
            g.setClassify(Integer.valueOf(classify));
            g.setDataDpcId(data_dpc_id);
            g.setDataServiceId(data_service_id);
            g.setEleCodeShort(ele_code_short);
            g.setElementCn(element_cn);
            g.setGribVersion(Integer.valueOf(grib_version));
            g.setGridDecodeId(grid_decode_id);
            g.setParameterId(Integer.valueOf(parameter_id));
            g.setPublicConfig(public_config);
            g.setSubjectId(Integer.valueOf(subject_id));
            g.setTemplateId(template_id);
            g.setCreateTime(new Date());
            gridDecodingDao.saveNotNull(g);
        }
    }

    public void importAreaDefine() {

        String sql = "select * from dmin_grid_area_define";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String data_service_id = toString(m.get("DATA_SERVICE_ID"));
            String area_id = toString(m.get("AREA_ID"));
            String area_region_desc = toString(m.get("AREA_REGION_DESC"));

            GridAreaEntity g = new GridAreaEntity();
            g.setAreaId(area_id);
            g.setAreaRegionDesc(area_region_desc);
            g.setDataServiceId(data_service_id);
            gridAreaDao.saveNotNull(g);
        }
    }


    public void importGridEEle() {
        String sql = "select * from usr_sod2.dmin_grid_ele_define";
        List<Map> list = CodeDOM.getList(sql);
        for (Map<String, Object> m : list) {
            String data_service_id = toString(m.get("DATA_SERVICE_ID"));
            String area_id = toString(m.get("AREA_ID"));//区域代码
            String db_fcst_ele = toString(m.get("DB_FCST_ELE"));//格点要素存储代码
            String ele_service_id = toString(m.get("ELE_SERVICE_ID"));//要素服务代码
            String level_type = toString(m.get("LEVEL_TYPE"));//层次类型
            String grib_version = toString(m.get("GRIB_VERSION"));//grib版本
            String ele_name_cn = toString(m.get("ELE_NAME_CN"));//要素中文名
            String ele_hours = toString(m.get("ELE_HOURS"));//资料时次
            String time_unit = toString(m.get("TIME_UNIT"));//时效单位
            //String level_list = toString(m.get("LEVEL_LIST"));//层次列表

            String level_list = "";
            Clob clob = (Clob) m.get("LEVEL_LIST");
            if(clob != null){
                try {
                    char[] mapperChar = new char[(int) clob.length()];
                    int read = clob.getCharacterStream().read(mapperChar);
                    if (read > 0) {
                        level_list = String.valueOf(mapperChar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //String time_list = toString(m.get("TIME_LIST"));//预报时效列表

            String time_list = "";
            Clob clob1 = (Clob) m.get("TIME_LIST");
            if(clob1 != null){
                try {
                    char[] mapperChar = new char[(int) clob1.length()];
                    int read = clob1.getCharacterStream().read(mapperChar);
                    if (read > 0) {
                        time_list = String.valueOf(mapperChar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            String grid_pixel = toString(m.get("GRID_PIXEL"));//空间分辨率
            String insert_time = toString(m.get("INSERT_TIME"));//插入时间
            String count_time = toString(m.get("COUNT_TIME"));//统计时间
            String ele_long_name = toString(m.get("ELE_LONG_NAME"));//要素长名
            String field_type = toString(m.get("FIELD_TYPE"));//场类型
            String genprocess_type = toString(m.get("GENPROCESS_TYPE"));//加工过程类型
            String s_number = toString(m.get("s_number"));//序号
            String level_unit = toString(m.get("LEVEL_UNIT"));//层次单位
            String scale_divisor = toString(m.get("scale_divisor"));//层次转换因子
            System.out.println(scale_divisor);
            String ele_unit = toString(m.get("ELE_UNIT"));//要素单位

            DataServerConfigEntity d = new DataServerConfigEntity();
            d.setAreaId(area_id);
            d.setDataServiceId(data_service_id);
            d.setDbEleName(db_fcst_ele);
            d.setEleHours(ele_hours);
            d.setEleLongName(ele_long_name);
            d.setEleNameCn(ele_name_cn);
            d.setEleServiceId(ele_service_id);
            d.setEleUnit(ele_unit);
            d.setTimeUnit(time_unit);
            if(StringUtils.isNotEmpty(field_type)){
                d.setFieldType(Integer.valueOf(field_type));
            }
            if(StringUtils.isNotEmpty(grib_version)){
                d.setGribVersion(Integer.valueOf(grib_version));
            }
            d.setGridPixel(grid_pixel);
            d.setLevelList(level_list);
            d.setLevelType(Integer.valueOf(level_type));
            d.setLevelUnit(level_unit);
            if(StringUtils.isNotEmpty(s_number)){
                d.setNum(Integer.valueOf(s_number));
            }
            if(StringUtils.isNotEmpty(genprocess_type)){
                d.setProcessType(Integer.valueOf(genprocess_type));
            }
            d.setScaleDivisor(scale_divisor);
            d.setTimeList(time_list);
            d.setCreateTime(new Date());
            dataServerConfigDao.saveNotNull(d);
        }
    }

}
