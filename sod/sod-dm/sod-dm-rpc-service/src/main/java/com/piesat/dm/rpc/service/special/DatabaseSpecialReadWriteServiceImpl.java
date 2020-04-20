package com.piesat.dm.rpc.service.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.special.DatabaseSpecialReadWriteDao;
import com.piesat.dm.entity.special.DatabaseSpecialReadWriteEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.special.DatabaseSpecialReadWriteService;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.mapper.special.DatabaseSpecialReadWriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try{
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("sdbId",databaseSpecialReadWriteDto.getSdbId());
            paramMap.put("dataType",databaseSpecialReadWriteDto.getDataType().toString());
            paramMap.put("typeName",databaseSpecialReadWriteDto.getTypeName());
            paramMap.put("dataName",databaseSpecialReadWriteDto.getDataName());
            paramMap.put("tableName",databaseSpecialReadWriteDto.getTableName());
            paramMap.put("applyAuthority",databaseSpecialReadWriteDto.getApplyAuthority());
            paramMap.put("examineStatus",databaseSpecialReadWriteDto.getExamineStatus());

            List<Map<String,Object>> dataList = mybatisQueryMapper.getDatabaseSpecialReadWriteList(paramMap);
            if(dataList!=null&&dataList.size()>0){
                for(Map<String,Object> map : dataList){
                    DatabaseSpecialReadWriteDto dto = new DatabaseSpecialReadWriteDto();
                    dto.setTypeName(map.get("TYPE_NAME")+"");
                    dto.setDataName(map.get("CLASS_NAME")+"");
                    dto.setDataClassId(map.get("DATA_CLASS_ID")+"");
                    dto.setDDataId(map.get("D_DATA_ID")+"");
                    dto.setTableName(map.get("TABLE_NAME")+"");
                    dto.setDatabaseName(map.get("DATABASE_NAME")+"");
                    dto.setApplyAuthority(Integer.parseInt(map.getOrDefault("APPLY_AUTHORITY",1).toString()));
                    dto.setEmpowerAuthority(Integer.parseInt(map.getOrDefault("EMPOWER_AUTHORITY",1).toString()));
                    dto.setExamineStatus(Integer.parseInt(map.getOrDefault("EXAMINE_STATUS",2).toString()));
                    resultList.add(dto);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<DatabaseSpecialReadWriteDto> findBySdbIdAndDataClassId(String sdbId, String dataClassId) {
        List<DatabaseSpecialReadWriteEntity> readWriteEntities = databaseSpecialReadWriteDao.findBySdbIdAndDataClassId(sdbId, dataClassId);
        return this.databaseSpecialReadWriteMapper.toDto(readWriteEntities);
    }

    @Override
    public DatabaseSpecialReadWriteDto saveDto(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto) {
        DatabaseSpecialReadWriteEntity databaseSpecialReadWriteEntity=databaseSpecialReadWriteMapper.toEntity(databaseSpecialReadWriteDto);
        databaseSpecialReadWriteEntity = this.saveNotNull(databaseSpecialReadWriteEntity);
        return databaseSpecialReadWriteMapper.toDto(databaseSpecialReadWriteEntity);
    }

    @Override
    public List<Map<String, Object>> getRecordSpecialByTdbId(String sdbId, String userId) {
        return mybatisQueryMapper.getRecordSpecialByTdbId(sdbId,userId);
    }

}
