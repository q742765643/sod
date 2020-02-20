package com.piesat.schedule.rpc.service.policy;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.api.DatumTypeInfoService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.rpc.api.backup.BackupLogService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearLogService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.move.MoveLogService;
import com.piesat.schedule.rpc.api.move.MoveService;
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
import org.springframework.stereotype.Service;

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
    public JSONArray strategyTree(){
       return datumTypeInfoService.getTree();
    }
    public void  findData(String dataClassId){

        List<DataLogicDto> dataLogicDtos = dataLogicService.findByDataClassId(dataClassId);
        for(DataLogicDto dataLogicDto:dataLogicDtos){
            StrategyPolicyDto strategyPolicyDto=new StrategyPolicyDto();
            List<PolicyDto> policyDtos=new ArrayList<>();
            DatabaseDto databaseDto= databaseService.getDotById(dataLogicDto.getDatabaseId());
            String parentId=databaseDto.getDatabaseDefine().getId();
            String databaseName=databaseDto.getDatabaseDefine().getDatabaseName()+"_"+databaseDto.getDatabaseName()
            strategyPolicyDto.setDatabaseName(databaseName);
        }
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
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        BackUpDto backup=backupService.selectBackupByParam(databaseId,dataClassId);
        if(null!=backup){
            policyDto.setIsConfiguration(1);
            policyDto.setJobCron(backup.getJobCron());
            if(backup.getTriggerStatus()==1){
                policyDto.setTriggerStatus(1);
            }
            BackupLogDto backupLogDto=backupLogService.selectBackupLoByJobId(backup.getId());

            if(null!=backupLogDto){
                policyDto.setDdateTime(format.format(new Date(backupLogDto.getBackupTime())));
                policyDto.setElapsedTime(backupLogDto.getElapsedTime()+"s");
            }

        }
        policyDtos.add(policyDto);

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
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        ClearDto clearDto=clearService.selectClearByParam(databaseId,dataClassId);
        if(null!=clearDto){
            policyDto.setIsConfiguration(1);
            policyDto.setJobCron(clearDto.getJobCron());
            if(clearDto.getTriggerStatus()==1){
                policyDto.setTriggerStatus(1);
            }
            ClearLogDto clearLogDto=clearLogService.selectClearLoByJobId(clearDto.getId());

            if(null!=clearLogDto){
                policyDto.setDdateTime(format.format(new Date(clearLogDto.getClearTime())));
                policyDto.setElapsedTime(clearLogDto.getElapsedTime()+"s");
            }

        }
        policyDtos.add(policyDto);

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
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        MoveDto moveDto=moveService.selectmoveByParam(databaseId,dataClassId);
        if(null!=moveDto){
            policyDto.setIsConfiguration(1);
            policyDto.setJobCron(moveDto.getJobCron());
            if(moveDto.getTriggerStatus()==1){
                policyDto.setTriggerStatus(1);
            }
            MoveLogDto moveLogDto=moveLogService.selectMoveLoByJobId(moveDto.getId());

            if(null!=moveLogDto){
                policyDto.setDdateTime(format.format(new Date(moveLogDto.getMoveTime())));
                policyDto.setElapsedTime(moveLogDto.getElapsedTime()+"s");
            }

        }
        policyDtos.add(policyDto);

    }

}

