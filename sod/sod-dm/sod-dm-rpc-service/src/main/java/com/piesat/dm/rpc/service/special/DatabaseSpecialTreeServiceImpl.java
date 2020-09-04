package com.piesat.dm.rpc.service.special;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.special.DatabaseSpecialTreeDao;
import com.piesat.dm.entity.special.DatabaseSpecialTreeEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.special.DatabaseSpecialTreeService;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialTreeDto;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialTreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 9:47
 */
@Service
public class DatabaseSpecialTreeServiceImpl extends BaseService<DatabaseSpecialTreeEntity> implements DatabaseSpecialTreeService {

    @Autowired
    private DatabaseSpecialTreeDao databaseSpecialTreeDao;
    @Autowired
    private DatabaseSpecialTreeMapper databaseSpecialTreeMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Override
    public BaseDao<DatabaseSpecialTreeEntity> getBaseDao() {
        return databaseSpecialTreeDao;
    }
    @Override
    public Map<String, Object> saveTreeData(DatabaseSpecialTreeDto databaseSpecialTreeDto) {
        Map<String, Object> map = new HashMap<>();
        try {
            /*
             * //删除专题库原有资料树 Map<String, String> paraMap=new HashMap<String, String>();
             * paraMap.put("tdb_id", tdbId);
             * dminSpecialDbTreeDao.deleteRecordByTdbId(paraMap);
             */
            // 下面通过request取得传入的JSONObject对象对应字符串。
            /*StringBuilder responseStrBuilder = new StringBuilder();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            // 下面定义DminSpecialDbAccessapply对象。
            DatabaseSpecialTreeEntity oneRecord = new DatabaseSpecialTreeEntity();
            // 下面从responseStrBuilder中获取专题库树信息。
            JSONArray dataJsonArray = JSONArray.parseArray(responseStrBuilder.toString());
            // 下面遍历dataJsonArray中的JSONObject对象。
            for (int i = 1; i <= dataJsonArray.size(); i = i + 1) {
                JSONObject oneTableInfo = dataJsonArray.getJSONObject(i - 1);
                // 下面遍历JSONObject对象。
                Iterator iterator = oneTableInfo.keySet().iterator();
                // 下面遍历JSONObject对象。
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = oneTableInfo.getString(key);

                    // 下面循环读取每个数据。
                    if (key.equals("TDB_ID") == true) {
                        oneRecord.setSdbId(value);
                    } else if (key.equals("TYPE_ID") == true) {
                        oneRecord.setTypeId(value);
                    } else if (key.equals("TYPE_NAME") == true) {
                        oneRecord.setTypeName(value);
                    } else if (key.equals("PARENT_ID") == true) {
                        oneRecord.setParentId(value);
                    } else if (key.equals("SORT") == true) {
                        oneRecord.setSort(Integer.parseInt(value));
                    }
                }
                // 下面修改资料树表中的信息。
                databaseSpecialTreeDao.save(oneRecord);
            }*/
            DatabaseSpecialTreeEntity databaseSpecialTreeEntity = databaseSpecialTreeMapper.toEntity(databaseSpecialTreeDto);
            databaseSpecialTreeDao.save(databaseSpecialTreeEntity);
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
    public Map<String, Object> updateOneRecordByTdbId(DatabaseSpecialTreeDto databaseSpecialTreeDto) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 下面通过request取得传入的JSONObject对象对应字符串。
           /* StringBuilder responseStrBuilder = new StringBuilder();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            // 下面定义DminSpecialDbAccessapply对象。
            DatabaseSpecialTreeEntity oneRecord = new DatabaseSpecialTreeEntity();
            // 下面从responseStrBuilder中获取专题库树信息。
            JSONObject oneTableInfo = JSONObject.parseObject(responseStrBuilder.toString());
            // 下面遍历JSONObject对象。
            Iterator iterator = oneTableInfo.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = oneTableInfo.getString(key);

                // 下面循环读取每个数据。
                if (key.equals("TDB_ID") == true) {
                    oneRecord.setSdbId(value);
                } else if (key.equals("TYPE_ID") == true) {
                    oneRecord.setTypeId(value);
                } else if (key.equals("TYPE_NAME") == true) {
                    oneRecord.setTypeName(value);
                } else if (key.equals("PARENT_ID") == true) {
                    oneRecord.setParentId(value);
                } else if (key.equals("SORT") == true) {
                    oneRecord.setSort(Integer.parseInt(value));
                }
            }*/
            DatabaseSpecialTreeEntity databaseSpecialTreeEntity = databaseSpecialTreeMapper.toEntity(databaseSpecialTreeDto);
            // 下面修改资料树表中的信息。
            //databaseSpecialTreeDao.save(databaseSpecialTreeEntity);
            mybatisQueryMapper.updateSpecialTreeData(databaseSpecialTreeEntity.getSdbId(),databaseSpecialTreeEntity.getParentId(),databaseSpecialTreeEntity.getTypeId(),databaseSpecialTreeEntity.getTypeName());
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
        return map ;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteRecordByTdbId(String tdbId, String typeId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 下面执行删除操作。
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("tdb_id", tdbId);
            paraMap.put("type_id", typeId);
            DatabaseSpecialTreeEntity databaseSpecialTreeEntity = databaseSpecialTreeDao.findBySdbIdAndTypeId(tdbId, typeId);
            databaseSpecialTreeDao.deleteById(databaseSpecialTreeEntity.getId());
            mybatisQueryMapper.delTreeUpdateTypeId(paraMap);
            // 下面生成返回信息。
            map.put("returnCode", 0);
            map.put("returnMessage", "删除数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 下面生成返回信息。
            map.put("returnCode", 1);
            map.put("returnMessage", "删除数据失败：" + e.getMessage());
        }

        // 下面返回值。
        return map;
    }

    @Override
    public Map<String, Object> updateTypeIdByTdbId(String tdbId, String dataClassIds, String typeId) {
        Map<String, Object> map = new HashMap<>();
        try {
            String[] dataClassIdArray = dataClassIds.split(",");
            for (String dataClassId : dataClassIdArray) {
                // 下面定义Map对象。
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("tdbId", tdbId);
                dataMap.put("dataClassId", dataClassId);
                dataMap.put("typeId", typeId);

                // 下面将一条记录存入数据库。
                mybatisQueryMapper.updateTypeIdByTdbId(dataMap);
            }
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
}
