package com.piesat.dm.rpc.service.special;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.special.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialReadWriteMapper;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 专题库管理
 */
@Service
public class DatabaseSpecialReadWriteServiceImpl extends BaseService<DatabaseSpecialReadWriteEntity> implements DatabaseSpecialReadWriteService {
    @Autowired
    private DatabaseSpecialReadWriteDao databaseSpecialReadWriteDao;
    @Autowired
    private DatabaseSpecialReadWriteMapper databaseSpecialReadWriteMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DatabaseSpecialServiceImpl databaseSpecialServiceImpl;

    @Override
    public BaseDao<DatabaseSpecialReadWriteEntity> getBaseDao() {
        return databaseSpecialReadWriteDao;
    }

    @Override
    public List<DatabaseSpecialReadWriteDto> getDotList(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        List<DatabaseSpecialReadWriteDto> resultList = new ArrayList();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("sdbId", databaseSpecialReadWriteDto.getSdbId());
            paramMap.put("dataType", databaseSpecialReadWriteDto.getDataType().toString());
            paramMap.put("typeName", databaseSpecialReadWriteDto.getTypeName());
            paramMap.put("dataName", databaseSpecialReadWriteDto.getDataName());
            paramMap.put("tableName", databaseSpecialReadWriteDto.getTableName());
            if (StringUtils.isNotNullString(databaseSpecialReadWriteDto.getApplyAuthorityString())) {
                if ("只读".equals(databaseSpecialReadWriteDto.getApplyAuthorityString())) {
                    paramMap.put("applyAuthority", 1);
                } else {
                    paramMap.put("applyAuthority", 2);
                }
            }
            if (StringUtils.isNotNullString(databaseSpecialReadWriteDto.getExamineStatusString())) {
                if ("拒绝".equals(databaseSpecialReadWriteDto.getExamineStatusString())) {
                    paramMap.put("examineStatus", 3);
                } else if (databaseSpecialReadWriteDto.getExamineStatusString().contains("已授权")) {
                    paramMap.put("examineStatus", 2);
                } else if (databaseSpecialReadWriteDto.getExamineStatusString().contains("待审核")) {
                    paramMap.put("examineStatus", 1);
                }
            }
            List<Map<String, Object>> dataList = mybatisQueryMapper.getDatabaseSpecialReadWriteList(paramMap);
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, Object> map : dataList) {
                    DatabaseSpecialReadWriteDto dto = new DatabaseSpecialReadWriteDto();
                    dto.setSdbId(map.get("SDB_ID") + "");
                    dto.setDatabaseId(map.get("DATABASE_ID") + "");
                    dto.setTypeName(map.get("TYPE_NAME") + "");
                    dto.setDataName(map.get("CLASS_NAME") + "");
                    dto.setDataClassId(map.get("DATA_CLASS_ID") + "");
                    dto.setDDataId(map.get("D_DATA_ID") + "");
                    dto.setTableName(map.get("TABLE_NAME") + "");
                    dto.setDatabaseName(map.get("DATABASE_NAME") + "");
                    if (map.get("FAILURE_REASON") != null && !"null".equals(map.get("FAILURE_REASON").toString())) {
                        dto.setFailureReason(map.get("FAILURE_REASON").toString());
                    } else {
                        dto.setFailureReason("");
                    }
                    dto.setApplyAuthority(getInteger(map, "APPLY_AUTHORITY", 1));
                    dto.setEmpowerAuthority(getInteger(map, "EMPOWER_AUTHORITY", 1));
                    dto.setExamineStatus(getInteger(map, "EXAMINE_STATUS", 1));
                    resultList.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Integer getInteger(Map<String, Object> map, String key, Integer def) {
        Object o = map.get(key);
        if (o != null) {
            try {
                return Integer.parseInt(o.toString());
            } catch (Exception e) {
                return def;
            }
        } else {
            return def;
        }
    }

    @Override
    public List<DatabaseSpecialReadWriteDto> findBySdbIdAndDataClassId(String sdbId, String dataClassId) {
        List<DatabaseSpecialReadWriteEntity> readWriteEntities = databaseSpecialReadWriteDao.findBySdbIdAndDataClassId(sdbId, dataClassId);
        return this.databaseSpecialReadWriteMapper.toDto(readWriteEntities);
    }

    @Override
    public DatabaseSpecialReadWriteDto saveDto(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity = databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto);
        databaseSpecialReadWriteEntity = this.saveNotNull(databaseSpecialReadWriteEntity);
        return databaseSpecialReadWriteMapper.toDto(databaseSpecialReadWriteEntity);
    }

    @Override
    public List<Map<String, Object>> getRecordSpecialByTdbId(String sdbId, String userId) {
        return mybatisQueryMapper.getRecordSpecialByTdbId(sdbId, userId);
    }

    @Override
    public Map<String, Object> changeDataStatus(String tdbId, String physical, String data_class_id) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 下面定义Map对象。
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("tdbId", tdbId);
            dataMap.put("physical", physical);
            dataMap.put("data_class_id", data_class_id);

            // 下面将一条记录存入数据库。
            mybatisQueryMapper.changeDataStatus(dataMap);
            mybatisQueryMapper.changeDataAuthorStatus(dataMap);

            // 下面生成返回信息。
            map.put("returnCode", 0);
            map.put("returnMessage", "保存数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 下面生成返回信息。
            map.put("returnCode", 1);
            map.put("returnMessage", "保存数据失败：" + e.getMessage());
        }

        // 下面返回值。
        return map;
    }

    @Override
    @Transactional
    public void deleteRecords(List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtos) {
        if (databaseSpecialReadWriteDtos != null && databaseSpecialReadWriteDtos.size() > 0) {
            for (int i = 0; i < databaseSpecialReadWriteDtos.size(); i++) {
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("tdbId", databaseSpecialReadWriteDtos.get(i).getSdbId());
                dataMap.put("dataClassId", databaseSpecialReadWriteDtos.get(i).getDataClassId());
                dataMap.put("databaseId", databaseSpecialReadWriteDtos.get(i).getDatabaseId());
                dataMap.put("userId", databaseSpecialReadWriteDtos.get(i).getUserId());
                //撤销权限
                databaseSpecialServiceImpl.cancelAuthority(dataMap.get("userId"), dataMap.get("databaseId"), dataMap.get("dataClassId"), null);
                // 下面将删除该条记录。
                mybatisQueryMapper.deleteRecordByTdbId(dataMap);
                mybatisQueryMapper.deleteDataAuthor(dataMap);
            }
        }


    }

    @Override
    public void deleteByDataClassId(String dataclassId) {
        this.databaseSpecialReadWriteDao.deleteByDataClassId(dataclassId);
    }
}
