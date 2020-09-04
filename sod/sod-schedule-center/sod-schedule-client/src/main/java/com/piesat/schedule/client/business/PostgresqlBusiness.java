package com.piesat.schedule.client.business;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class PostgresqlBusiness extends BaseBusiness{
    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {

    }

    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {

    }

    @Override
    public void deleteKtable(ClearLogEntity clearLogEntity, ClearVo clearVo, ResultT<String> resultT) {
        DataSourceContextHolder.setDataSource(clearLogEntity.getParentId());
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DatabaseOperationService databaseOperationService= SpringUtil.getBean(DatabaseOperationService.class);
            Date minDate=databaseOperationService.selectKtableMaxTime(clearLogEntity.getTableName(),clearLogEntity.getConditions());
            long startTime=minDate.getTime();
            if(clearVo.getClearTime()==0){
                resultT.setSuccessMessage("根据条件未按清除频率进行切割");

                this.deletePostgresql(clearLogEntity,clearLogEntity.getConditions(),resultT);
                if(!resultT.isSuccess()){
                    return;
                }
                resultT.setSuccessMessage("清除条件为{}成功",clearLogEntity.getConditions());
                return;
            }
            String ytime=format.format(clearVo.getClearTime());
            resultT.setSuccessMessage("按清除频率进行切割{}",clearLogEntity.getClearLimit());
            long num=0;
            while (startTime<clearVo.getClearTime()){
                startTime=startTime+clearLogEntity.getClearLimit()*1000;

                if(startTime>clearVo.getClearTime()){
                    startTime=clearVo.getClearTime();
                }
                String ddateTime=format.format(startTime);
                String conditions=clearLogEntity.getConditions().replaceAll(ytime,ddateTime);
                if(num==0){
                    resultT.setSuccessMessage("开始清除条件{}",conditions);
                    log.info("开始清除条件{}",conditions);
                }
                this.deletePostgresql(clearLogEntity,conditions,resultT);
                num++;
                if(!resultT.isSuccess()){
                    resultT.setSuccessMessage("条件为{}时执行失败",conditions);
                    log.error("条件为{}时执行失败",conditions);
                    return;
                }
            }
        } catch (Exception e) {
            resultT.setErrorMessage("执行清除失败:{}", OwnException.get(e));
            log.error("执行清除失败:{}", OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
    }

    @Override
    public long selectTableCount(String parentId, String ktable, String conditions, ResultT<String> resultT) {
        DatabaseOperationService databaseOperationService=SpringUtil.getBean(DatabaseOperationService.class);
        return databaseOperationService.selectTableCount(parentId,ktable,conditions,resultT);
    }

    @Override
    public List<TreeVo> findMeta(String parentId) {
        return null;
    }

    @Override
    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT) {

    }

    @Override
    public void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT) {

    }

    @Override
    public void recoverStructedData(RecoverMetaVo recoverMetaVo, RecoverLogEntity recoverLogEntity, ResultT<String> resultT) {

    }

    @Override
    public List<TreeVo> findAllTableByIp(String parentId) {
        return null;
    }
    public void deletePostgresql(ClearLogEntity clearLogEntity,String conditions,ResultT<String> resultT){
        //clearLogEntity.setTableName(clearLogEntity.getTableName().toLowerCase());
        try {
            DatabaseOperationService databaseOperationService=SpringUtil.getBean(DatabaseOperationService.class);
            if(null!=clearLogEntity.getVTableName()&& StringUtils.isNotNullString(clearLogEntity.getVTableName())){
                //clearLogEntity.setVTableName(clearLogEntity.getVTableName());
                long vcount=databaseOperationService.deleteVtable(clearLogEntity,conditions,resultT);
                if(!resultT.isSuccess()){
                    return;
                }
            }
            long kcount =databaseOperationService.delteKtable(clearLogEntity,conditions);
        } catch (Exception e) {
            resultT.setErrorMessage("执行清除失败:{}", OwnException.get(e));
            log.error("执行清除失败:{}", OwnException.get(e));

        }
    }
}
