package com.piesat.dm.rpc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.StorageConfigurationDao;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.DataClassService;
import com.piesat.dm.rpc.api.StorageConfigurationService;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.dm.rpc.mapper.StorageConfigurationMapper;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
@Service
public class StorageConfigurationServiceImpl extends BaseService<StorageConfigurationEntity> implements StorageConfigurationService {
    @Autowired
    private StorageConfigurationDao storageConfigurationDao;
    @Autowired
    private StorageConfigurationMapper storageConfigurationMapper;
    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;
    @GrpcHthtClient
    private ClearService clearService;
    @GrpcHthtClient
    private SyncTaskService syncTaskService;
    @GrpcHthtClient
    private BackupService backupService;

    @Override
    public BaseDao<StorageConfigurationEntity> getBaseDao() {
        return storageConfigurationDao;
    }

    @Override
    public PageBean selectPageList(PageForm<Map<String,String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());
        List<Map<String,Object>> lists = mybatisQueryMapper.selectStorageConfigurationPageList(pageForm.getT());//自定义的接口
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return pageBean;
    }

    @Override
    public StorageConfigurationDto saveDto(StorageConfigurationDto storageConfigurationDto) {
        StorageConfigurationEntity storageConfigurationEntity = this.storageConfigurationMapper.toEntity(storageConfigurationDto);
        storageConfigurationEntity = this.storageConfigurationDao.save(storageConfigurationEntity);
        return this.storageConfigurationMapper.toDto(storageConfigurationEntity);
    }

    @Override
    public Map<String, Object> exportTable(Map<String, String> pMap) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String,Object>> list = mybatisQueryMapper.selectStorageConfigurationPageList(pMap);
        ArrayList<String> headList = new ArrayList<>();
        headList.add("资料名称");
        headList.add("四级编码");
        headList.add("存储编码");
        headList.add("表名称");
        headList.add("数据用途");
        headList.add("数据库");
        headList.add("专题名");
        ArrayList<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> strings = new ArrayList<>();
            Map<String, Object> map = list.get(i);
            strings.add(map.get("CLASS_NAME") == null ? "" : map.get("CLASS_NAME").toString());
            strings.add(map.get("D_DATA_ID") == null ? "" : map.get("D_DATA_ID").toString());
            strings.add(map.get("DATA_CLASS_ID") == null ? "" : map.get("DATA_CLASS_ID").toString());
            strings.add(map.get("TABLE_NAME") == null ? "" : map.get("TABLE_NAME").toString());
            strings.add(map.get("LOGIC_NAME") == null ? "" : map.get("LOGIC_NAME").toString());
            strings.add(map.get("DATABASE_NAME1") == null ? "" : map.get("DATABASE_NAME1").toString());
            strings.add(map.get("P_SPECIAL_DATABASE_NAME") == null ? "" : map.get("P_SPECIAL_DATABASE_NAME").toString());
            lists.add(strings);
        }
        resultMap.put("headList",headList);
        resultMap.put("lists",lists);
        return resultMap;
    }

    @Override
    public ResultT updateColumnValue(String id, String column, String value) {
        //删除配置
        if("3".equals(value)){
            //获取策略配置
            StorageConfigurationEntity storage = this.getById(id);
            if (column.equals("storage_define_identifier")) {
                //删除存储结构
                dataClassService.deleteByDataClassId(storage.getDataClassId());
            }else if(column.equals("sync_identifier")){
                //删除同步配置
                String task_id = storage.getSyncId();
                if (StringUtils.isNotNullString(task_id)) {
                    SyncTaskDto syncTaskDto = syncTaskService.getDtoById(task_id);
                    if (syncTaskDto != null) {
                        syncTaskService.deleteSync(task_id);
                    }
                }
            }else if(column.equals("moveclean_identifier")){
                //删除迁移清楚
                String taskId = storage.getClearId();
                if (StringUtils.isNotNullString(taskId)) {
                    clearService.deleteClearByIds(new String[]{taskId});
                }
            }else if(column.equals("backup_identifier")){
                //删除备份
                String taskId = storage.getBackupId();
                if (StringUtils.isNotNullString(taskId)) {
                    backupService.deleteBackupByIds(new String[]{taskId});
                }
            }
        }
        //更新状态
        mybatisQueryMapper.updateStorageConfigurationStatus(id,column,value);

        return  ResultT.success();
    }

    @Override
    public ResultT deleteById(String id) {
        StorageConfigurationEntity storage = this.getById(id);
        //删除存储结构
        dataClassService.deleteByDataClassId(storage.getDataClassId());
        //删除同步配置
        if (StringUtils.isNotNullString(storage.getSyncId())) {
            SyncTaskDto syncTaskDto = syncTaskService.getDtoById(storage.getSyncId());
            if (syncTaskDto != null) {
                syncTaskService.deleteSync(storage.getSyncId());
            }
        }
        //删除迁移清楚
        if (StringUtils.isNotNullString(storage.getClearId())) {
            clearService.deleteClearByIds(new String[]{storage.getClearId()});
        }
        //删除备份
        if (StringUtils.isNotNullString(storage.getBackupId())) {
            backupService.deleteBackupByIds(new String[]{storage.getBackupId()});
        }
        //删除配置信息
        storageConfigurationDao.deleteById(id);
        return ResultT.success();
    }
}
