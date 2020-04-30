package com.piesat.dm.rpc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.AESUtil;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.special.DatabaseSpecialDao;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.entity.datatable.DataTableEntity;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.dm.entity.special.DatabaseSpecialEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.DataAuthorityApplyService;
import com.piesat.dm.rpc.api.database.DatabaseDefineService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.api.system.UserService;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

/**
 * 数据库可视化接口服务类
 *
 * @author cwh
 * @date 2020年 04月27日 13:27:57
 */
@Service
public class DatabaseVisibleService {
    @GrpcHthtClient
    private UserService userService;

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private DataAuthorityApplyService dataAuthorityApplyService;
    @Autowired
    private DataLogicDao dataLogicDao;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private DataClassService dataClassService;
    @Autowired
    private DataTableDao dataTableDao;
    @Autowired
    private DatabaseSpecialDao databaseSpecialDao;
    @Autowired
    private DatabaseDefineService databaseDefineService;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    public ResultT getDatabaseByBizUserId(String bizUserId) {
        UserDto userDto = this.userService.selectUserByUserName(bizUserId);
        String dbIds = userDto.getDbIds();
        String[] split = dbIds.split(",");
        List<DatabaseDto> list = new ArrayList<>();
        JSONArray arr = new JSONArray();
        for (String id : split) {
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(id);
            if (dotById != null) {
                JSONObject jo = new JSONObject();
                jo.put("DATABASE_IP", dotById.getDatabaseIp());
                jo.put("MAINBAK_TYPE", dotById.getMainBakType());
                jo.put("DATABASE_URL", dotById.getDatabaseUrl());
                JSONArray aaa = new JSONArray();
                JSONObject dd = new JSONObject();
                dd.put("DATABASE_USER", userDto.getUserName());
                dd.put("DATABASE_PASS", AESUtil.aesDecrypt(userDto.getPassword()).trim());
                jo.put("DATABASE_ACCOUNT", dd);
                jo.put("DATABASE_PORT", dotById.getDatabasePort());
                jo.put("DATABASE_DESC", dotById.getDatabaseDesc());
                jo.put("DATABASE_ID", dotById.getId());
                jo.put("DATABASE_TYPE", dotById.getDatabaseType());
                jo.put("DATABASE_NAME", dotById.getDatabaseName());
                jo.put("DATABASE_INSTANCE", dotById.getDatabaseInstance());
                arr.add(jo);
            }
        }
        return ResultT.success(arr);
    }


    public ResultT getDatabaseInfo(String databaseId, String bizUserId) {
        UserDto userDto = this.userService.selectUserByUserName(bizUserId);
        String dbIds = userDto.getDbIds();
        String[] split = dbIds.split(",");
        JSONObject jo = new JSONObject();
        for (String id : split) {
            if (!databaseId.equals(id)) continue;
            DatabaseDefineDto dotById = this.databaseDefineService.getDotById(id);
            if (dotById != null) {
                jo.put("DATABASE_IP", dotById.getDatabaseIp());
                jo.put("MAINBAK_TYPE", dotById.getMainBakType());
                jo.put("DATABASE_URL", dotById.getDatabaseUrl());
                jo.put("UP_URL", dotById.getUpUrl());
                JSONArray aaa = new JSONArray();
                JSONObject dd = new JSONObject();
                dd.put("DATABASE_USER", userDto.getUserName());
                dd.put("DATABASE_PASS", AESUtil.aesDecrypt(userDto.getPassword()).trim());
                aaa.add(dd);
                jo.put("DATABASE_ACCOUNT", aaa);
                jo.put("DATABASE_PORT", dotById.getDatabasePort());
                jo.put("DATABASE_DESC", dotById.getDatabaseDesc());
                jo.put("DATABASE_ID", dotById.getId());
                jo.put("DATABASE_TYPE", dotById.getDatabaseType());
                jo.put("DATABASE_NAME", dotById.getDatabaseName());
                jo.put("DATABASE_INSTANCE", dotById.getDatabaseInstance());
            }
        }
        return ResultT.success(jo);
    }

    public ResultT getDataclassTree(String bizUserId, String dataBaseId) {
        dataBaseId = dataBaseId == null ? "":dataBaseId;
        List<LinkedHashMap<String, Object>> dataClassByBizUserOrDatabase = this.mybatisQueryMapper.getDataClassByBizUserOrDatabase(bizUserId, dataBaseId);
        List<String> list = new ArrayList<>();
        for (LinkedHashMap<String, Object> obj : dataClassByBizUserOrDatabase) {
            list.add(obj.get("DATA_CLASS_ID").toString());
        }
        List<LinkedHashMap<String, Object>> dataclassTreeByClassIds = this.mybatisQueryMapper.getDataclassTreeByClassIds(list);
        return ResultT.success(dataclassTreeByClassIds);
    }


    public ResultT getTable(String bizUserId, String dataclassId) {
        List<DataAuthorityApplyDto> dataAuthorityApplyDto = this.dataAuthorityApplyService.findByUserId(bizUserId);
        List<DataAuthorityRecordDto> dataAuthorityRecordList = new ArrayList<>();
        for (DataAuthorityApplyDto d:dataAuthorityApplyDto ) {
            dataAuthorityRecordList.addAll(d.getDataAuthorityRecordList());
        }
        DataClassDto byDataClassId = dataClassService.findByDataClassId(dataclassId);
        JSONArray arr = new JSONArray();
        for (DataAuthorityRecordDto d : dataAuthorityRecordList) {
            if (d.getDataClassId().equals(dataclassId)) {
                String databaseId = d.getDatabaseId();
                DatabaseDto dotById = databaseService.getDotById(databaseId);
                List<DataLogicEntity> byDatabaseIdAndDataClassId = dataLogicDao.findByDatabaseIdAndDataClassId(databaseId, d.getDataClassId());
                for (DataLogicEntity dl : byDatabaseIdAndDataClassId) {
                    List<DataTableDto> byClassLogicId = dataTableService.getByClassLogicId(dl.getId());
                    for (DataTableDto dt : byClassLogicId) {
                        JSONObject jo = new JSONObject();
                        jo.put("TABLE_NAME", dt.getTableName());
                        jo.put("DATA_SERVICE_NAME", dt.getDataServiceName());
                        jo.put("D_DATA_ID", byDataClassId.getDDataId());
                        jo.put("STORAGE_TYPE", dl.getStorageType());
                        JSONArray aaa = new JSONArray();
                        JSONObject bbb = new JSONObject();
                        bbb.put("DATABASE_ID", databaseId);
                        bbb.put("DATABASE_NAME", dotById.getDatabaseDefine().getDatabaseName() + "(" + dotById.getDatabaseName() + ")");
                        aaa.add(bbb);
                        jo.put("DATABASE", aaa);
                        arr.add(jo);
                    }

                }

            }
        }
        return ResultT.success(arr);
    }

    public ResultT getTableInfo(String tableName) {
        JSONObject jo = new JSONObject();
        List<DataTableEntity> byTableName = dataTableDao.findByTableName(tableName);
        if (byTableName != null) {
            DataTableEntity dataTableEntity = byTableName.get(0);
            String dataClassId = dataTableEntity.getClassLogic().getDataClassId();
            DataClassDto dataClassDto = dataClassService.findByDataClassId(dataClassId);
            JSONObject table_info = new JSONObject();
            table_info.put("D_DATA_ID", dataClassDto.getDDataId());
            table_info.put("DATA_CLASS_ID", dataClassId);
            table_info.put("DATA_SERVICE_ID", dataTableEntity.getDataServiceId());
            table_info.put("TABLE_NAME", dataTableEntity.getTableName());
            table_info.put("DATA_SERVICE_NAME", dataTableEntity.getDataServiceName());
            jo.put("table_info", table_info);
            JSONArray table_structure = new JSONArray();
            for (TableColumnEntity c : dataTableEntity.getColumns()) {
                JSONObject column = new JSONObject();
                column.put("COLUMN_ID", c.getId());
                column.put("TABLE_ID", c.getTableId());
                column.put("DB_ELE_CODE", c.getDbEleCode());
                column.put("C_ELEMENT_CODE", c.getCElementCode());
                column.put("IS_MANAGER", c.getIsManager());
                column.put("USER_ELE_CODE", c.getUserEleCode());
                column.put("NAME_CN", c.getNameCn());
                column.put("ELE_NAME", c.getEleName());
                column.put("TYPE", c.getType());
                column.put("ACCURACY", c.getAccuracy());
                column.put("UNIT", c.getUnit());
                column.put("UNIT_CN", c.getUnitCn());
                column.put("IS_NULL", c.getIsNull());
                column.put("IS_UPDATE", c.getIsUpdate());
                column.put("IS_SHOW", c.getIsShow());
                column.put("IS_PREMARY_KEY", c.getIsPrimaryKey());
                column.put("IS_KV_K", c.getIsKvK());
                column.put("DATABASE_TYPE", c.getDatabaseType());
                column.put("SERIAL_NUMBER", c.getSerialNumber());
                table_structure.add(column);
            }
            jo.put("table_structure", table_structure);

            JSONArray table_index = new JSONArray();
            for (TableIndexEntity c : dataTableEntity.getTableIndexList()) {
                JSONObject index = new JSONObject();
                index.put("INDEX_NAME", c.getIndexName());
                index.put("INDEX_COLUMN", c.getIndexColumn());
                table_index.add(index);
            }
            jo.put("table_index", table_index);
        }
        return ResultT.success(jo);
    }

    public ResultT getSpecial(String bizUserId) {
        List<DatabaseSpecialEntity> specialEntityList = this.databaseSpecialDao.findByUserIdAndUseStatus(bizUserId, "2");
        JSONArray jrr = new JSONArray();
        for (DatabaseSpecialEntity ds : specialEntityList) {
            JSONObject dd = new JSONObject();
            dd.put("TDB_ID", ds.getId());
            dd.put("TDB_NAME", ds.getSdbName());
            dd.put("USER_ID", bizUserId);
            dd.put("APPLY_TIME", ds.getCreateTime());
            dd.put("USES", ds.getUses());
            dd.put("EXAMINE_STATUS", ds.getExamineStatus());
            dd.put("EXAMINE_TIME", ds.getExamineTime());
            dd.put("USE_STATUS", ds.getUseStatus());
            jrr.add(dd);
        }
        return ResultT.success(jrr);
    }


    public ResultT getSpecialInfo(String specialId) {
        JSONObject jo = new JSONObject();
        DatabaseSpecialEntity ds = this.databaseSpecialDao.getOne(specialId);
        JSONObject dd = new JSONObject();
        dd.put("TDB_ID", ds.getId());
        dd.put("TDB_NAME", ds.getSdbName());
        dd.put("USER_ID", ds.getUserId());
        dd.put("APPLY_TIME", ds.getCreateTime());
        dd.put("USES", ds.getUses());
        dd.put("EXAMINE_STATUS", ds.getExamineStatus());
        dd.put("EXAMINE_TIME", ds.getExamineTime());
        dd.put("USE_STATUS", ds.getUseStatus());
        jo.put("specialDb", dd);
        JSONArray jrr = new JSONArray();
        List<DatabaseEntity> databaseEntityList = databaseDao.findByTdbId(ds.getId());
        for (DatabaseEntity de : databaseEntityList) {
            List<DataLogicEntity> dataLogicEntityList = dataLogicDao.findByDatabaseId(de.getId());
            for (DataLogicEntity dl : dataLogicEntityList) {
                DataClassDto dataClassDto = dataClassService.findByDataClassId(dl.getDataClassId());
                List<DataTableEntity> dataTableEntityList = dataTableDao.findByClassLogicId(dl.getId());
                for (DataTableEntity dt : dataTableEntityList) {
                    JSONObject tt = new JSONObject();
                    tt.put("TABLE_NAME", dt.getTableName());
                    tt.put("DATA_SERVICE_NAME", dt.getDataServiceName());
                    tt.put("D_DATA_ID", dataClassDto.getDDataId());
                    tt.put("CLASS_NAME", dataClassDto.getClassName());
                    tt.put("PHYSICAL", de.getId());
                    tt.put("DATABASE_NAME", de.getDatabaseDefine().getDatabaseName() + "(" + de.getDatabaseName() + ")");
                    tt.put("DATA_CLASS_ID", dataClassDto.getDataClassId());
                    jrr.add(tt);
                }
            }
        }
        jo.put("dataList", jrr);
        jo.put("returnCode", 0);
        return ResultT.success(jo);
    }

    public ResultT getAccountInfo(String bizUserId) {
        UserDto userDto = this.userService.selectUserByUserName(bizUserId);
        JSONObject tt = new JSONObject();
        tt.put("databaseUP_ID", userDto.getUserName());
        tt.put("databaseUP_PASSWORD", AESUtil.aesDecrypt(userDto.getPassword()).trim());
        tt.put("returnCode",0);
        return ResultT.success(tt);
    }


}
