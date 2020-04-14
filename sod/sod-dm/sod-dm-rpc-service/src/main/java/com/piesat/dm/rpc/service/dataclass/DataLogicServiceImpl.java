package com.piesat.dm.rpc.service.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataclass.DataLogicDao;
import com.piesat.dm.entity.dataclass.DataLogicEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.mapper.dataclass.DataLogicMapper;
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
    private StorageConfigurationService storageConfigurationService;
    @Autowired
    private DataLogicMapper dataLogicMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataTableService dataTableService;

    @Override
    public BaseDao<DataLogicEntity> getBaseDao() {
        return dataLogicDao;
    }

    @Override
    public DataLogicDto saveDto(DataLogicDto dataLogicDto) {
        boolean isAdd = false;
        if (StringUtils.isEmpty(dataLogicDto.getId())) {
            isAdd = true;
        }
        dataLogicDto.setCreateTime(new Date());
        DataLogicEntity dataLogicEntity = this.dataLogicMapper.toEntity(dataLogicDto);
        dataLogicEntity = this.dataLogicDao.save(dataLogicEntity);
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
    public List<DataLogicDto> saveList(List<DataLogicDto> dataLogicList) {
        List<DataLogicEntity> dataLogicEntities = this.dataLogicMapper.toEntity(dataLogicList);
        for (DataLogicEntity d : dataLogicEntities) {
            boolean isAdd = false;
            if (StringUtils.isEmpty(d.getId())) {
                isAdd = true;
                d.setCreateTime(new Date());
            }
            d = this.dataLogicDao.save(d);
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
    public void deleteById(String id) {
        DataLogicDto dotById = this.getDotById(id);
        dataTableService.deleteByClassLogicId(dotById.getId());
        this.delete(id);
    }

}
