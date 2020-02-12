package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.DataLogicDao;
import com.piesat.dm.entity.DataLogicEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
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
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;


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
    public DataLogicDto getDotById(String id) {
        DataLogicEntity dataLogicEntity = this.getById(id);
        return this.dataLogicMapper.toDto(dataLogicEntity);
    }

}
