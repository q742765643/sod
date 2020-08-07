package com.piesat.schedule.rpc.service.sync;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.common.utils.poi.ExcelUtil;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.ShardingService;
import com.piesat.dm.rpc.api.datatable.TableIndexService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.ShardingDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.schedule.dao.sync.*;
import com.piesat.schedule.entity.sync.*;
import com.piesat.schedule.mapper.sync.SyncTaskMapper;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskLogDto;
import com.piesat.schedule.rpc.mapstruct.sync.SyncTaskLogMapstruct;
import com.piesat.schedule.rpc.mapstruct.sync.SyncTaskMapstruct;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 11:47
 */
@Service
public class SyncTaskServiceImpl extends BaseService<SyncTaskEntity> implements SyncTaskService {
    @Autowired
    private SyncTaskDao syncTaskDao;
    @Autowired
    private SyncConfigDao syncConfigDao;
    @Autowired
    private SyncFilterDao syncFilterDao;
    @Autowired
    private SyncMappingDao syncMappingDao;
    @Autowired
    private SyncTaskMapstruct syncTaskMapstruct;
    @Autowired
    private SyncTaskLogMapstruct syncTaskLogMapstruct;
    @Autowired
    private SyncTaskMapper syncTaskMapper;
    @Autowired
    private SyncEleWarningDao syncEleWarningDao;
    @Autowired
    private SyncDiMessageDao syncDiMessageDao;

    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private TableIndexService tableIndexService;
    @GrpcHthtClient
    private ShardingService shardingService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;
    @GrpcHthtClient
    private StorageConfigurationService storageConfigurationService;
    @GrpcHthtClient
    private DatabaseService databaseService;


    @Override
    public BaseDao<SyncTaskEntity> getBaseDao() {
        return syncTaskDao;
    }

    @Override
    public PageBean selectPageList(PageForm<SyncTaskDto> pageForm) {
        //实体转换
        SyncTaskEntity syncTaskEntity = syncTaskMapstruct.toEntity(pageForm.getT());
        //syncTaskEntity.setRunState(pageForm.getT().getRunState());

        //指定了运行状态就查询所有记录，没有指定运行状态按页码查询
        if (!StringUtils.isNotNullString(syncTaskEntity.getRunState())) {
            PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        }
        List<SyncTaskEntity> syncTaskEntities = syncTaskMapper.selectPageList(syncTaskEntity);

        //获取同步部署的服务器们
        List<DictDataDto> dictDataDtos = dictDataService.selectDictDataByType("sync_run_host");
        //获取所有任务的状态
        JSONObject jsonThree = new JSONObject();
        if (dictDataDtos != null && dictDataDtos.size() > 0) {
            for (int i = 0; i < dictDataDtos.size(); i++) {
                SyncTaskEntity sync = new SyncTaskEntity();
                sync.setExecIp(dictDataDtos.get(i).getDictValue().split(":")[0]);
                sync.setExecPort(Integer.valueOf(dictDataDtos.get(i).getDictValue().split(":")[1]));
                sync.setId("");
                String allStatus = getStatusById(sync, "getallstatus");
                if (!"error".equals(allStatus)) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(allStatus);
                        jsonThree.putAll(jsonObject);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }

                }
            }
        }

        //每条记录赋值运行状态
        if (syncTaskEntities != null && syncTaskEntities.size() > 0) {
            for (int i = syncTaskEntities.size() - 1; i >= 0; i--) {
                //运行状态  true(运行中01) false_有出错原因(运行出错02) error(未启动03)  error_没有出错原因（停止中04，程序正常关闭）
                String status = jsonThree.getString(syncTaskEntities.get(i).getId());
                if (null == status) {
                    status = "error";
                }
                String error = "";
                if ("false".equals(status)) {// 出错，调用接口查找错误原因
                    error = getStatusById(syncTaskEntities.get(i), "geterror");// 错误原因(运行出错02) 空(停止中04)
                    status = status + "|" + error;
                }
                syncTaskEntities.get(i).setRunState(status);
                // 有查询状态下，把不符合的记录踢出去
                if (StringUtils.isNotNullString(syncTaskEntity.getRunState())) {
                    if (syncTaskEntity.getRunState().equals("01")) {//运行中
                        if (!status.equals("true")) {
                            syncTaskEntities.remove(i);
                        }
                    } else if (syncTaskEntity.getRunState().equals("02")) {//运行出错
                        if (!status.contains("false") || "".equals(error)) {
                            syncTaskEntities.remove(i);
                        }
                    } else if (syncTaskEntity.getRunState().equals("03")) {//未启动
                        if (!status.equals("error")) {
                            syncTaskEntities.remove(i);
                        }
                    } else if (syncTaskEntity.getRunState().equals("04")) {//停止中
                        if (!(status.contains("false") && "".equals(error))) {
                            syncTaskEntities.remove(i);
                        }
                    }
                }
            }
        }
        //分页
        PageInfo<SyncTaskEntity> pageInfo = new PageInfo<>(syncTaskEntities);
        if (StringUtils.isNotNullString(syncTaskEntity.getRunState())) {
            int pages = syncTaskEntities.size() % pageForm.getPageSize() == 0 ? syncTaskEntities.size() / pageForm.getPageSize() : syncTaskEntities.size() / pageForm.getPageSize() + 1;
            pageInfo.setPages(pages);
            int fromIndex = (pageForm.getCurrentPage() - 1) * pageForm.getPageSize();
            if (pageForm.getCurrentPage() < pageInfo.getPages()) {
                syncTaskEntities = syncTaskEntities.subList(fromIndex, fromIndex + pageForm.getPageSize());
            } else if (Integer.valueOf(pageForm.getCurrentPage()).equals(Integer.valueOf(pageInfo.getPages()))) {
                syncTaskEntities = syncTaskEntities.subList(fromIndex, syncTaskEntities.size());
            }
        }

        //获取当前页数据
        List<SyncTaskDto> syncTaskDtos = syncTaskMapstruct.toDto(syncTaskEntities);
        //查询物理库名称
       /* for(SyncTaskDto syncTaskDto : syncTaskDtos){
            DatabaseDto databaseDto = databaseService.getDotById(syncTaskDto.getSourceDatabaseId());
            syncTaskDto.setSourceDatabaseName(databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName());
        }*/
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), syncTaskDtos);
        return pageBean;
    }

    @Override
    public PageBean selectLogPageList(PageForm<SyncTaskLogDto> pageForm) {
        SyncTaskLogEntity syncTaskLogEntity = syncTaskLogMapstruct.toEntity(pageForm.getT());
        PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        List<SyncTaskLogEntity> syncTaskLogEntities = syncTaskMapper.selectLogPageList(syncTaskLogEntity);//自定义的接口
        PageInfo<SyncTaskLogEntity> pageInfo = new PageInfo<>(syncTaskLogEntities);
        //获取当前页数据
        List<SyncTaskLogDto> syncTaskLogDtos = syncTaskLogMapstruct.toDto(pageInfo.getList());
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), syncTaskLogDtos);
        return pageBean;
    }

    @Override
    public List<SyncTaskDto> all() {
        List<SyncTaskEntity> all = this.getAll();
        return this.syncTaskMapstruct.toDto(all);
    }

    @Override
    @Transactional(readOnly = false)
    public SyncTaskDto saveDto(SyncTaskDto syncTaskDto) {
        List<String> targetTableIds = new ArrayList<String>();

        //将源表过滤字段信息存到sync_filter表中，
        String filterRecordIds = syncFilterSaveDto(syncTaskDto);

        //将目标表信息存到sync_config表中
        List<Map<String, Object>> targetRelationList = syncTaskDto.getTargetRelation();
        StringBuffer sourceIds = new StringBuffer();
        for (int i = 0; i < targetRelationList.size(); i++) {
            Map<String, Object> targetRelation = targetRelationList.get(i);
            String targetTableId = (String) targetRelation.get("targetTableId");
            List<Map<String, String>> mapping = (List<Map<String, String>>) targetRelation.get("mapping");
            targetTableIds.add(targetTableId);
            //保存
            String mappingRecordId = syncConfigAndMappingSaveDto(syncTaskDto.getSourceTableId(), targetTableId, mappingListToString(mapping), filterRecordIds, StringUtils.isNotNullString(syncTaskDto.getTargetVTableId()));
            if (sourceIds.length() > 0) {
                sourceIds.append(",").append(mappingRecordId);
            } else {
                sourceIds.append(mappingRecordId);
            }
        }

        //值表映射关系入库
        StringBuffer slaveIds = new StringBuffer();
        Map<String, Object> slaveRelation = syncTaskDto.getSlaveRelation();
        if (slaveRelation != null && !slaveRelation.isEmpty()) {
            String sourceVTableId = (String) slaveRelation.get("sourceVTableId");
            String targetVTableId = (String) slaveRelation.get("targetVTableId");
            List<Map<String, String>> mapping = (List<Map<String, String>>) slaveRelation.get("mapping");
            String linkKey = (String) slaveRelation.get("linkKey");
            syncTaskDto.setLinkKey(linkKey);

            String mappingRecordId = syncConfigAndMappingSaveDto(sourceVTableId, targetVTableId, mappingListToString(mapping), "", true);
            slaveIds.append(mappingRecordId);
        }
        syncTaskDto.setSourceTable(sourceIds.toString());
        syncTaskDto.setSlaveTables(slaveIds.toString());
        /*if(StringUtils.isNotNullString(syncTaskDto.getDiOff()) && "true".equals(syncTaskDto.getDiOff())){
            syncTaskDto.setDiOff("1");
        }else{
            syncTaskDto.setDiOff("0");
        }
        if(StringUtils.isNotNullString(syncTaskDto.getDiscardOnDuplicate()) && "true".equals(syncTaskDto.getDiscardOnDuplicate())){
            syncTaskDto.setDiscardOnDuplicate("1");
        }else{
            syncTaskDto.setDiscardOnDuplicate("0");
        }*/
        if (syncTaskDto.getBeginTime() != null) {
            syncTaskDto.setLastSuccessTime(syncTaskDto.getBeginTime());
        }
        syncTaskDto.setHasModify("0");

        UserDto loginUser = (UserDto) SecurityUtils.getSubject().getPrincipal();
        syncTaskDto.setCreateBy(loginUser.getUserName());


        SyncTaskEntity syncTaskEntity = this.syncTaskMapstruct.toEntity(syncTaskDto);
        syncTaskEntity = syncTaskDao.saveNotNull(syncTaskEntity);

        //修改存储策略配置
        updateStorageConfiguration(targetTableIds, syncTaskDto.getTargetDatabaseId(), 1, syncTaskEntity.getId());

        if (syncTaskDto.getCheckInterval() != null && syncTaskDto.getTimeLimit() != null && syncTaskDto.getBiggestDifference() != null) {
            SyncEleWarningEntity see = new SyncEleWarningEntity();
            see.setId(syncTaskEntity.getId());
            see.setTaskId(syncTaskEntity.getId());
            see.setCheckInterval(syncTaskDto.getCheckInterval());
            see.setTimeLimit(syncTaskDto.getTimeLimit());
            see.setBiggestDifference(syncTaskDto.getBiggestDifference());
            this.syncEleWarningDao.saveNotNull(see);
        }

        DataTableDto sourceTableDto = dataTableService.getDotById(syncTaskDto.getSourceTableId());

        //数据触发同步
        if(syncTaskDto.getSyncType() != null && syncTaskDto.getSyncType().intValue() == 2){
            SyncDiMessageEntity sde = new SyncDiMessageEntity();
            sde.setId(syncTaskEntity.getId());
            sde.setMessageQueueName(syncTaskDto.getQueueName());
            sde.setPrimaryComposition(syncTaskDto.getPrimaryCom());
            sde.setDDataId(sourceTableDto.getClassLogic().getDataClassId());
            this.syncDiMessageDao.saveNotNull(sde);
        }


        return this.syncTaskMapstruct.toDto(syncTaskEntity);
    }

    //修改存储策略配置
    public void updateStorageConfiguration(List<String> targetTableIds, String targetDatabaseId, Integer syncIdentifier, String taskId) {
        if (targetTableIds != null && targetTableIds.size() > 0) {
            for (String targetTableId : targetTableIds) {
                DataTableDto targetTableDto = dataTableService.getDotById(targetTableId);
                List<DataLogicDto> dataLogicDtos = dataLogicService.getDataLogic(targetTableDto.getDataServiceId(), targetDatabaseId, targetTableDto.getTableName());
                if (dataLogicDtos != null && dataLogicDtos.size() > 0) {
                    for (DataLogicDto dataLogicDto : dataLogicDtos) {
                        StorageConfigurationDto storageConfigurationDto = new StorageConfigurationDto();
                        storageConfigurationDto.setClassLogicId(dataLogicDto.getId());
                        storageConfigurationDto.setSyncIdentifier(syncIdentifier);
                        storageConfigurationDto.setSyncId(taskId);
                        storageConfigurationService.updateDataAuthorityConfig(storageConfigurationDto);
                    }
                }
            }
        }
    }


    @Override
    @Transactional(readOnly = false)
    public SyncTaskDto updateDto(SyncTaskDto syncTaskDto) {
        this.deleteConfigFilter(syncTaskDto.getId());
        this.saveDto(syncTaskDto);
        if (syncTaskDto.getCheckInterval() != null && syncTaskDto.getTimeLimit() != null && syncTaskDto.getBiggestDifference() != null) {
            SyncEleWarningEntity see = new SyncEleWarningEntity();
            see.setId(syncTaskDto.getId());
            see.setTaskId(syncTaskDto.getId());
            see.setCheckInterval(syncTaskDto.getCheckInterval());
            see.setTimeLimit(syncTaskDto.getTimeLimit());
            see.setBiggestDifference(syncTaskDto.getBiggestDifference());
            this.syncEleWarningDao.saveNotNull(see);
        }
        return syncTaskDto;
    }

    /**
     * 保存syncFilter
     *
     * @param syncTaskDto
     * @return 可能情况：空  一个  多个用逗号分隔
     */
    public String syncFilterSaveDto(SyncTaskDto syncTaskDto) {
        String[] sourceTableFilter = syncTaskDto.getSourceTableFilter();
        String filterRecordIds = "";
        if (sourceTableFilter != null && sourceTableFilter.length > 0) {
            for (int i = 0; i < sourceTableFilter.length; i++) {
                SyncFilterEntity sti = new SyncFilterEntity();
                sti.setColumnName(sourceTableFilter[i]);
                sti.setFilterValues(syncTaskDto.getSourceTableFilterText()[i]);
                sti.setColumnOper(syncTaskDto.getColumnOper()[i]);

                sti = syncFilterDao.saveNotNull(sti);
                if (!"".equals(filterRecordIds)) {
                    filterRecordIds += ",";
                }
                if (StringUtils.isNotNullString(String.valueOf(sti.getId()))) {
                    filterRecordIds += sti.getId();
                }

            }
        }
        return filterRecordIds;
    }

    /**
     * 保存syncConfig和syncMapping
     *
     * @param sourceTableId
     * @param targetTableId
     * @param mapping
     * @param filterRecordIds
     * @param isKV
     * @return
     */

    public String syncConfigAndMappingSaveDto(String sourceTableId, String targetTableId, String mapping, String filterRecordIds, boolean isKV) {
        //获取源表信息
        DataTableDto sourceTableDto = dataTableService.getDotById(sourceTableId);
        //获取目标表信息
        DataTableDto targetTableDto = dataTableService.getDotById(targetTableId);

        SyncConfigEntity tti = new SyncConfigEntity();
        tti.setTargetTableId(targetTableId);
        //获取目标表的唯一索引
        List<TableIndexDto> tableIndexDtos = tableIndexService.findByTableId(targetTableId);
        String unique_index = findUniqueIndex(tableIndexDtos);
        //目标表的唯一索引不存在的话用源表唯一索引代替
        if (!StringUtils.isNotNullString(unique_index)) {
            tableIndexDtos = tableIndexService.findByTableId(sourceTableId);
            unique_index = findUniqueIndex(tableIndexDtos);
        }
        tti.setUniqueKeys(unique_index);

        //目标表分库分表键
        List<ShardingDto> shardingDtos = shardingService.getDotByTableId(targetTableId);
        String ttkeys = getPartitionKey(shardingDtos);
        if (StringUtils.isNotNullString(ttkeys)) {
            tti.setIfpatitions("1");
            tti.setPartitionKeys(ttkeys);
        } else {
            tti.setIfpatitions("0");
        }

        //是否kv表
        if (isKV) {
            tti.setIsKv("1");
        } else {
            tti.setIsKv("0");
        }

        //目标表的存储编码
        tti.setDDataId(targetTableDto.getDataServiceId());
        //存储
        tti = syncConfigDao.saveNotNull(tti);


        //保存sync_mapping
        SyncMappingEntity syncMappingEntity = new SyncMappingEntity();
        syncMappingEntity.setSourceTableId(filterRecordIds);
        syncMappingEntity.setSourceTableName(sourceTableDto.getTableName());
        syncMappingEntity.setTargetTableId(String.valueOf(tti.getId()));
        syncMappingEntity.setTargetTableName(targetTableDto.getTableName());
        syncMappingEntity.setMapping(mapping);
        syncMappingEntity = syncMappingDao.saveNotNull(syncMappingEntity);
        return String.valueOf(syncMappingEntity.getId());
    }

    public String mappingListToString(List<Map<String, String>> mapping) {
        String mappingString = "";
        if (mapping != null && mapping.size() > 0) {
            for (int i = 0; i < mapping.size(); i++) {
                Map<String, String> map = mapping.get(i);
                String targetColumn_ = map.get("targetColumn_");
                String sourceColumn_ = map.get("sourceColumn_");
                mappingString = mappingString + "<" + targetColumn_ + ">" + sourceColumn_ + "</" + targetColumn_ + ">" + "\r\n";
            }
        }
        return mappingString;
    }

    public JSONArray mappingStringToList(String mapping) {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isNotNullString(mapping)) {
            for (int i = 0; i < mapping.split("\r\n").length; i++) {
                JSONObject jsonObject = new JSONObject();
                String oneMapping = mapping.split("\r\n")[i];
                String sourceColumn_ = oneMapping.substring(oneMapping.indexOf(">") + 1, oneMapping.indexOf("</"));
                String targetColumn_ = oneMapping.substring(oneMapping.indexOf("<") + 1, oneMapping.indexOf(">"));
                jsonObject.put("targetColumn_", targetColumn_);
                jsonObject.put("sourceColumn_", sourceColumn_);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }


    /**
     * 唯一索引/主键
     *
     * @param tableIndexList
     * @return
     */
    private String findUniqueIndex(List<TableIndexDto> tableIndexList) {
        if (tableIndexList != null && tableIndexList.size() > 0) {
            for (TableIndexDto ti : tableIndexList) {
                if (ti.getIndexType().contains("唯一") || ti.getIndexType().contains("UK") || ti.getIndexType().contains("PK")) {
                    return ti.getIndexColumn();
                }
            }
        }
        return "";
    }

    /**
     * 分库分表键
     *
     * @param shardingDtos
     * @return
     */
    public String getPartitionKey(List<ShardingDto> shardingDtos) {
        try {
            String partitionKey = "";//分库分表键，用逗号分隔
            if (shardingDtos != null && shardingDtos.size() > 0) {
                for (ShardingDto shard : shardingDtos) {
                    partitionKey += shard.getColumnName() + ",";
                }
                if (partitionKey.length() > 0) {
                    partitionKey = partitionKey.substring(0, partitionKey.length() - 1);
                }
            }
            return partitionKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * 调用同步服务接口
     *
     */
    public String getStatusById(SyncTaskEntity syncTaskEntity, String taskName) {
        String strURL = "http://" + syncTaskEntity.getExecIp() + ":" + syncTaskEntity.getExecPort() + "/sod_sync/rest/"
                + taskName + "/" + syncTaskEntity.getId();
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        StringBuffer buffer = new StringBuffer();
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(6000);// 设置超时
            requestFactory.setReadTimeout(6000);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            String line = restTemplate.getForObject(strURL, String.class);
            if (line == null) {
                line = "";
            }
            buffer.append(line);
        } catch (Exception e) {
            e.printStackTrace();
            buffer.append("error");
        } finally {
            try {
                if (reader != null) {// 关闭流
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return buffer.toString();
    }

    @Override
    public SyncTaskDto getDtoById(String id) {
        SyncTaskEntity syncTaskEntity = this.getById(id);
        return this.syncTaskMapstruct.toDto(syncTaskEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteSync(String taskId) {
        List<String> targetTableIds = deleteConfigFilter(taskId);

        //修改存储策略配置
        SyncTaskDto syncTaskDto = this.getDtoById(taskId);
        if (syncTaskDto != null) {
            updateStorageConfiguration(targetTableIds, syncTaskDto.getTargetDatabaseId(), 2, "");
        }

        //删除synctask表
        syncTaskDao.deleteById(taskId);
        boolean b = this.syncEleWarningDao.existsById(taskId);
        if (b) {
            this.syncEleWarningDao.deleteById(taskId);
        }
        boolean syncDiMessageExist = this.syncDiMessageDao.existsById(taskId);
        if(syncDiMessageExist){
            this.syncDiMessageDao.deleteById(taskId);
        }
    }

    public List<String> deleteConfigFilter(String taskId) {
        List<String> targetTableIds = new ArrayList<String>();
        SyncTaskDto syncTaskDto = this.getDtoById(taskId);
        SyncTaskEntity syncTaskEntity = this.syncTaskMapstruct.toEntity(syncTaskDto);
        // 调用服务
        getStatusById(syncTaskEntity, "drop");

        //删除filter和config表
        String so = syncTaskDto.getSourceTable();//键表对的mapper的id，可能有多个用逗号分隔
        String sl = syncTaskDto.getSlaveTables();//值表对的mapper的id
        List<Integer> mapperList = new ArrayList<Integer>();
        if (StringUtils.isNotNullString(so)) {
            for (int i = 0; i < so.split(",").length; i++) {
                mapperList.add(Integer.valueOf(so.split(",")[i]));
            }
        }
        if (StringUtils.isNotNullString(sl)) {
            mapperList.add(Integer.valueOf(sl));
        }
        //查找所有的mapper
        List<SyncMappingEntity> syncMappingLists = syncMappingDao.findAllByIdIn(mapperList);
        //查filter和config的id
        for (SyncMappingEntity syncMappingEntity : syncMappingLists) {
            //删除filter
            if (StringUtils.isNotNullString(syncMappingEntity.getSourceTableId())) {
                for (String filterId : syncMappingEntity.getSourceTableId().split(",")) {
                    if (syncFilterDao.existsById(Integer.valueOf(filterId))){
                        syncFilterDao.deleteById(Integer.valueOf(filterId));
                    }
                }
            }
            //删除config
            if (StringUtils.isNotNullString(syncMappingEntity.getTargetTableId())) {
                SyncConfigEntity syncConfigEntity = syncConfigDao.findById(Integer.valueOf(syncMappingEntity.getTargetTableId()));
                targetTableIds.add(syncConfigEntity.getTargetTableId());
                if (syncConfigDao.existsById(Integer.valueOf(syncMappingEntity.getTargetTableId()))){
                    syncConfigDao.deleteById(Integer.valueOf(syncMappingEntity.getTargetTableId()));
                }
            }
            //删除mapping表
            if (syncMappingDao.existsById(syncMappingEntity.getId())){
                syncMappingDao.deleteById(syncMappingEntity.getId());
            }

        }

        //删除dimessage表
        return targetTableIds;
    }

    @Override
    public JSONObject getSyncJsonById(String taskId) {
        SyncTaskEntity syncTaskEntity = this.getById(taskId);

        String sourceTable = syncTaskEntity.getSourceTable();
        List<Integer> mappingKIds = new ArrayList<Integer>();
        for (int i = 0; i < sourceTable.split(",").length; i++) {
            mappingKIds.add(Integer.valueOf(sourceTable.split(",")[i]));
        }
        List<SyncMappingEntity> syncMappingEntitys = syncMappingDao.findAllByIdIn(mappingKIds);

        //源表过滤字段
        String sourceTableFilter = "";
        String columnOper = "";
        String sourceTableFilterText = "";
        SyncMappingEntity syncMappingEntit = syncMappingEntitys.get(0);
        String filterIds = syncMappingEntit.getSourceTableId();
        if (StringUtils.isNotNullString(filterIds)) {
            for (int i = 0; i < filterIds.split(",").length; i++) {
                SyncFilterEntity syncFilterEntity = syncFilterDao.findById(Integer.valueOf(filterIds.split(",")[i]));
                if (StringUtils.isNotNullString(sourceTableFilter)) {
                    sourceTableFilter = "," + syncFilterEntity.getColumnName();
                    columnOper = "," + syncFilterEntity.getColumnOper();
                    sourceTableFilterText = "," + syncFilterEntity.getFilterValues();

                } else {
                    sourceTableFilter = syncFilterEntity.getColumnName();
                    columnOper = syncFilterEntity.getColumnOper();
                    sourceTableFilterText = syncFilterEntity.getFilterValues();
                }
            }
        }
        if (StringUtils.isNotNullString(sourceTableFilter)) {
            syncTaskEntity.setSourceTableFilter(sourceTableFilter.split(","));
        }
        if (StringUtils.isNotNullString(columnOper)) {
            syncTaskEntity.setColumnOper(columnOper.split(","));
        }
        if (StringUtils.isNotNullString(sourceTableFilterText)) {
            syncTaskEntity.setSourceTableFilterText(sourceTableFilterText.split(","));
        }

        SyncTaskDto syncTaskDto = syncTaskMapstruct.toDto(syncTaskEntity);


        //目标表
        for (SyncMappingEntity syncMappingEntity : syncMappingEntitys) {

            //目标表
            Map<String, Object> relation = new HashMap<>();
            //获取config的id
            String configId = syncMappingEntity.getTargetTableId();
            //目标表的编码
            SyncConfigEntity syncConfigEntity = syncConfigDao.findById(Integer.valueOf(configId));
            relation.put("targetTableId", syncConfigEntity.getTargetTableId());
            //将mapping格式化成json
            String mapping = syncMappingEntity.getMapping();
            relation.put("mapping", mappingStringToList(mapping));
            syncTaskDto.getTargetRelation().add(relation);
        }

        //值表mapping的id
        String slaveTable = syncTaskEntity.getSlaveTables();
        if (StringUtils.isNotNullString(slaveTable)) {
            Map<String, Object> relation = new HashMap<>();
            SyncMappingEntity syncMappingEntity = syncMappingDao.findById(Integer.valueOf(slaveTable));

            //获取config的id
            String configId = syncMappingEntity.getTargetTableId();
            //将mapping格式化成json
            String mapping = syncMappingEntity.getMapping();
            relation.put("mapping", mappingStringToList(mapping));
            syncTaskDto.setSlaveRelation(relation);
        }

        Optional<SyncEleWarningEntity> optionalT = this.syncEleWarningDao.findById(syncTaskDto.getId());
        SyncEleWarningEntity see = optionalT.isPresent() ? optionalT.get() : null;
        if (see != null) {
            syncTaskDto.setCheckInterval(see.getCheckInterval());
            syncTaskDto.setTimeLimit(see.getTimeLimit());
            syncTaskDto.setBiggestDifference(see.getBiggestDifference());
        }

        //数据触发同步
        if(syncTaskDto.getSyncType() != null &&  syncTaskDto.getSyncType().intValue() == 2){
            Optional<SyncDiMessageEntity> syncDiMessageOptionl = this.syncDiMessageDao.findById(syncTaskDto.getId());
            SyncDiMessageEntity syncDiMessageEntity = syncDiMessageOptionl.isPresent() ? syncDiMessageOptionl.get() : null;
            if(syncDiMessageEntity != null){
                syncTaskDto.setQueueName(syncDiMessageEntity.getMessageQueueName());
                syncTaskDto.setPrimaryCom(syncDiMessageEntity.getPrimaryComposition());
            }

        }

        String syncTaskJson = JSONObject.toJSONString(syncTaskDto);
        JSONObject jsonObject = JSONObject.parseObject(syncTaskJson);
        return jsonObject;
    }


    @Override
    public void exportExcel(SyncTaskDto syncTaskDto) {
        SyncTaskEntity syncTaskEntity = syncTaskMapstruct.toEntity(syncTaskDto);
        List<SyncTaskEntity> syncTaskEntitiesList = syncTaskMapper.selectPageList(syncTaskEntity);
        ExcelUtil<SyncTaskEntity> util = new ExcelUtil(SyncTaskEntity.class);
        util.exportExcel(syncTaskEntitiesList, "数据同步");
    }

    @Override
    public void restart(String taskId) {
        SyncTaskEntity byId = this.getById(taskId);
        getStatusById(byId, "restart");
    }

    @Override
    public void stop(String taskId) {
        SyncTaskEntity byId = this.getById(taskId);
        getStatusById(byId, "stop");
    }
}
