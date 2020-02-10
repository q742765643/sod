package com.piesat.schedule.client.handler.backup;

import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.constant.HandleConstant;
import com.piesat.schedule.mapper.test.TestMapper;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.backup.BackupLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:17
 */
@Slf4j
@Service("backupHandler")
public class BackupHandler implements BaseHandler {
    @Autowired
    private BackupLogMapper backupLogMapper;
    @Autowired
    private TestMapper testMapper;
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        BackupEntity backupEntity= (BackupEntity) jobInfoEntity;
        this.preParam(backupEntity);
        log.info("备份调用成功");
    }
    public void preParam(BackupEntity backupEntity){
        Map map = ExtractMessage.getIndexOf(backupEntity.getConditions(),backupEntity.getDatabaseId(),
                backupEntity.getDataClassId(),backupEntity.getTriggerLastTime());
        HashSet<String> set= (HashSet) map.get("set");
        BackupLogEntity backupLogEntity=backupLogMapper.findMaxBackupTime(backupEntity.getId());
        DataSourceContextHolder.setDataSource("1111");
        testMapper.findList();
        DataSourceContextHolder.setDataSource(null);
        BackupLogEntity backupLogEntity1=backupLogMapper.findMaxBackupTime(backupEntity.getId());

        String backupTime="";
        long mistiming=0;
        if(set.size()==2){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMddHHmm");
            List<String> timeList=new ArrayList<>();
            for(String time:set){
                timeList.add(time);
            }
            try {
                long beginTime=simpleDateFormat.parse(timeList.get(0)).getTime();
                long endTime=simpleDateFormat.parse(timeList.get(1)).getTime();
                mistiming=Math.abs(endTime-beginTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        if(backupLogEntity!=null&&mistiming>0){
            if("2"!=backupLogEntity.getHandleCode()){


            }



        }



    }

}
