package com.piesat.dm.rpc.service.special;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
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
            paramMap.put("applyAuthority", databaseSpecialReadWriteDto.getApplyAuthority());
            paramMap.put("examineStatus", databaseSpecialReadWriteDto.getExamineStatus());

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
                    dto.setApplyAuthority( getInteger(map,"APPLY_AUTHORITY", 1));
                    dto.setEmpowerAuthority(getInteger(map,"EMPOWER_AUTHORITY", 1));
                    dto.setExamineStatus(getInteger(map,"EXAMINE_STATUS", 1));
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
        }else {
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
    public Map<String, Object> deleteRecords(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 下面通过request取得传入的JSONArray对象对应字符串。
            StringBuilder responseStrBuilder = new StringBuilder();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            // 下面从info中获取多个专题库ID号和资料存储编码。
            JSONArray dataJsonArray = JSONArray.parseArray(responseStrBuilder.toString());
            // 下面遍历dataJsonArray中的JSONObject对象。
            for (int i = 1; i <= dataJsonArray.size(); i = i + 1) {
                // 下面定义Map对象。
                Map<String, String> dataMap = new HashMap<String, String>();
                // 下面取得一张表的信息。
                JSONObject oneTableInfo = dataJsonArray.getJSONObject(i - 1);
                // 下面遍历JSONObject对象。
                Iterator iterator = oneTableInfo.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = oneTableInfo.getString(key);

                    // 下面循环读取每个数据。
                    if (key.equals("TDB_ID") == true) {
                        dataMap.put("tdbId", value);
                    } else if (key.equals("DATA_CLASS_ID") == true) {
                        dataMap.put("dataClassId", value);
                    } else if (key.equals("LOGIC_ID") == true) {
                        dataMap.put("logicId", value);
                    }
                }
                // 下面将删除该条记录。
                mybatisQueryMapper.deleteRecordByTdbId(dataMap);
                mybatisQueryMapper.deleteDataAuthor(dataMap);
            }

            // 下面生成返回信息。
            map.put("returnCode", 0);
            map.put("returnMessage", "删除数据成功");
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            // 下面生成返回信息。
            map.put("returnCode", 1);
            map.put("returnMessage", "删除数据失败：" + e.getMessage());
        }
        // 下面返回值。
        return map;
    }
}
