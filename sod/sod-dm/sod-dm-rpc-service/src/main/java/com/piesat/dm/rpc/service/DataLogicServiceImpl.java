package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataLogicDao;
import com.piesat.dm.entity.DataLogicEntity;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.mapper.DataLogicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        String sql = "SELECT * FROM T_SOD_DATA_LOGIC A LEFT JOIN T_SOD_DATA_CLASS B ON A.DATA_CLASS_ID = B.DATA_CLASS_ID AND A.DATABASEID = '" + databaseId + "'";
        List<Map<String, Object>> list = this.queryByNativeSQL(sql);
        return list;
    }

    @Override
    public DataLogicDto getDotById(String id) {
        DataLogicEntity dataLogicEntity = this.getById(id);
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }
}
