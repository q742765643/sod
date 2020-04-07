package com.piesat.schedule.rpc.service.policy;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.*;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.api.dataclass.DataLogicService;
import com.piesat.dm.rpc.api.dataclass.DatumTypeInfoService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.schedule.dao.sync.SyncMappingDao;
import com.piesat.schedule.dao.sync.SyncTaskDao;
import com.piesat.schedule.entity.sync.SyncMappingEntity;
import com.piesat.schedule.entity.sync.SyncTaskEntity;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearLogService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.move.MoveLogService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.api.sync.SyncTaskService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.clear.ClearLogDto;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.dto.move.MoveLogDto;
import com.piesat.schedule.rpc.dto.policy.PolicyDto;
import com.piesat.schedule.rpc.dto.policy.StrategyPolicyDto;
import com.piesat.schedule.rpc.service.clear.ClearLogServiceImpl;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 10:55
 **/
@Service
public class StrategyPolicyService {
    @GrpcHthtClient
    private DatumTypeInfoService datumTypeInfoService;
    @GrpcHthtClient
    private DictDataService dictDataService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;
    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private DataClassService dataClassService;
    @Autowired
    private BackupService backupService;
    @Autowired
    private BackupLogService backupLogService;
    @Autowired
    private ClearService clearService;
    @Autowired
    private ClearLogService clearLogService;
    @Autowired
    private MoveService moveService;
    @Autowired
    private MoveLogService moveLogService;
    @Autowired
    private SyncMappingDao syncMappingDao;
    @Autowired
    private SyncTaskDao syncTaskDao;
    @Autowired
    private SyncTaskService syncTaskService;
    public JSONArray strategyTree(){
       return dataClassService.getTree();
    }

    public List<StrategyPolicyDto>  findData(String dataClassId){
        List<StrategyPolicyDto> strategyPolicyDtos=new ArrayList<>();
        List<DataLogicDto> dataLogicDtos = dataLogicService.findByDataClassId(dataClassId);
        for(DataLogicDto dataLogicDto:dataLogicDtos){
            try {
                StrategyPolicyDto strategyPolicyDto=new StrategyPolicyDto();
                strategyPolicyDto.setDataClassId(dataClassId);
                List<PolicyDto> policyDtos=new ArrayList<>();
                DatabaseDto databaseDto= databaseService.getDotById(dataLogicDto.getDatabaseId());
                strategyPolicyDto.setDatabaseId(databaseDto.getId());
                String parentId=databaseDto.getDatabaseDefine().getId();
                String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName();
                strategyPolicyDto.setDatabaseName(databaseName);
                this.findBackup(databaseDto.getId(),dataClassId,parentId,policyDtos);
                this.findClear(databaseDto.getId(),dataClassId,parentId,policyDtos);
                this.findMove(databaseDto.getId(),dataClassId,parentId,policyDtos);
                this.findSync(databaseDto.getId(),dataClassId,parentId,policyDtos);
                strategyPolicyDto.setPolicyDtos(policyDtos);
                strategyPolicyDtos.add(strategyPolicyDto);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return strategyPolicyDtos;
    }

    public void findBackup(String databaseId,String dataClassId,String parentId,List<PolicyDto> policyDtos){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("backup_database");
        boolean flag=false;
        for(DictDataDto dictDataDto:dictDataDtos){
            if(dictDataDto.getDictValue().toUpperCase().equals(parentId.toUpperCase())){
                flag=true;
                break;
            }
        }
        if(!flag){
            return;
        }
        PolicyDto policyDto=new PolicyDto();
        policyDto.setPolicyName("备份");
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            BackUpDto backup=backupService.selectBackupByParam(databaseId,dataClassId);
            if(null!=backup){
                policyDto.setTaskId(backup.getId());
                policyDto.setIsConfiguration("已配置");
                policyDto.setJobCron(backup.getJobCron());
                policyDto.setNextTime(this.getNextTime(backup.getJobCron()));
                if(backup.getTriggerStatus()==1){
                    policyDto.setTriggerStatus("已启动");
                }else{
                    policyDto.setTriggerStatus("未启动");
                }
                BackupLogDto backupLogDto=backupLogService.selectBackupLoByJobId(backup.getId());

                if(null!=backupLogDto){
                    policyDto.setDdateTime(format.format(new Date(backupLogDto.getBackupTime())));
                    policyDto.setElapsedTime(backupLogDto.getElapsedTime()+"s");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            policyDtos.add(policyDto);
        }

    }

    public void findClear(String databaseId,String dataClassId,String parentId,List<PolicyDto> policyDtos){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("clear_database");
        boolean flag=false;
        for(DictDataDto dictDataDto:dictDataDtos){
            if(dictDataDto.getDictValue().toUpperCase().equals(parentId.toUpperCase())){
                flag=true;
                break;
            }
        }
        if(!flag){
            return;
        }
        PolicyDto policyDto=new PolicyDto();
        policyDto.setPolicyName("清除");
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            ClearDto clearDto=clearService.selectClearByParam(databaseId,dataClassId);
            if(null!=clearDto){
                policyDto.setTaskId(clearDto.getId());
                policyDto.setIsConfiguration("已配置");
                policyDto.setJobCron(clearDto.getJobCron());
                policyDto.setNextTime(this.getNextTime(clearDto.getJobCron()));
                if(clearDto.getTriggerStatus()==1){
                    policyDto.setTriggerStatus("已启动");
                }else{
                    policyDto.setTriggerStatus("未启动");
                }
                ClearLogDto clearLogDto=clearLogService.selectClearLoByJobId(clearDto.getId());

                if(null!=clearLogDto){
                    policyDto.setDdateTime(format.format(new Date(clearLogDto.getClearTime())));
                    policyDto.setElapsedTime(clearLogDto.getElapsedTime()+"s");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            policyDtos.add(policyDto);

        }

    }
    public void findMove(String databaseId,String dataClassId,String parentId,List<PolicyDto> policyDtos){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("move_database");
        boolean flag=false;
        for(DictDataDto dictDataDto:dictDataDtos){
            if(dictDataDto.getDictValue().toUpperCase().equals(parentId.toUpperCase())){
                flag=true;
                break;
            }
        }
        if(!flag){
            return;
        }
        PolicyDto policyDto=new PolicyDto();
        policyDto.setPolicyName("迁移");
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            MoveDto moveDto=moveService.selectmoveByParam(databaseId,dataClassId);
            if(null!=moveDto){
                policyDto.setTaskId(moveDto.getId());
                policyDto.setIsConfiguration("已配置");
                policyDto.setJobCron(moveDto.getJobCron());
                policyDto.setNextTime(this.getNextTime(moveDto.getJobCron()));
                if(moveDto.getTriggerStatus()==1){
                    policyDto.setTriggerStatus("已启动");
                }else{
                    policyDto.setTriggerStatus("未启动");
                }
                MoveLogDto moveLogDto=moveLogService.selectMoveLoByJobId(moveDto.getId());

                if(null!=moveLogDto){
                    policyDto.setDdateTime(format.format(new Date(moveLogDto.getMoveTime())));
                    policyDto.setElapsedTime(moveLogDto.getElapsedTime()+"s");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            policyDtos.add(policyDto);

        }

    }

    public void findSync(String databaseId,String dataClassId,String parentId,List<PolicyDto> policyDtos){
        List<DictDataDto> dictDataDtos=dictDataService.selectDictDataByType("sync_database");
        boolean flag=false;
        for(DictDataDto dictDataDto:dictDataDtos){
            if(dictDataDto.getDictValue().toUpperCase().equals(parentId.toUpperCase())){
                flag=true;
                break;
            }
        }
        if(!flag){
            return;
        }
        PolicyDto policyDto=new PolicyDto();
        policyDto.setPolicyName("同步");
        try {
            List<DataTableDto>  dataTableDtos=dataTableService.getByDatabaseIdAndClassId(databaseId,dataClassId);
            if(null==dataTableDtos&&dataTableDtos.isEmpty()){
                return;
            }
            List<String> targetTableIds=new ArrayList<>();
            for(DataTableDto dataTableDto:dataTableDtos){
                targetTableIds.add(dataTableDto.getId());
            }
            List<SyncMappingEntity> syncMappingEntities=syncMappingDao.findAllByTargetTableIdIn(targetTableIds);
            if(null==syncMappingEntities&&syncMappingEntities.isEmpty()){
                return;
            }
            List<SyncTaskEntity> syncTaskEntities=new ArrayList<>();
            String soourceTableName="";
            for(SyncMappingEntity syncMappingEntity:syncMappingEntities){
                List<SyncTaskEntity> syncTasks=syncTaskDao.findAllByTargetDatabaseIdAndSlaveTablesIsLike(databaseId,String.valueOf(syncMappingEntity.getId()));
                if(null!=syncTasks&&!syncTasks.isEmpty()){
                    syncTaskEntities.addAll(syncTasks);
                    soourceTableName=syncMappingEntity.getSourceTableName();
                    break;
                }
            }
            if(syncTaskEntities.isEmpty()){
                return;
            }
            SyncTaskEntity syncTaskEntity=syncTaskEntities.get(0);
            DatabaseDto databaseDto= databaseService.getDotById(syncTaskEntity.getSourceDatabaseId());
            policyDto.setSourceTable(soourceTableName);
            policyDto.setSourceRepository(databaseDto.getDatabaseDefine().getDatabaseName());
            String result=this.getStatusById(syncTaskEntity,"getallstatus");
            if("true".equals(result)){
                policyDto.setTriggerStatus("已启动");
            }else{
                policyDto.setTriggerStatus("未启动");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            policyDtos.add(policyDto);
        }


    }

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
            String line=restTemplate.getForObject(strURL,String.class);
            if(line==null){
                line="";
            }
            buffer.append(line);
        }  catch (Exception e) {
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

    public List<String> getNextTime(String cronExpression){
        List<String> cronTimeList = new ArrayList<>();
        try {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpression);
            Date nextTimePoint = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < 5; i++) {
                nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
                cronTimeList.add(sdf.format(nextTimePoint));
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return cronTimeList;
    }
}

