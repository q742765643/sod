package com.piesat.schedule.client.business;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.Select2File;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.exp.ExpMetadata;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-10 09:13
 **/
@Slf4j
public class XuguBusiness extends BaseBusiness{
    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT)  {
        StringBuilder sql=new StringBuilder();
        sql.append("select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions());
        Select2File select2File=new Select2File();

        String tempFilePath=strategyVo.getTempPtah()+"/"+backupLogEntity.getFileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            strategyVo.setKfilePath(tempFilePath);
            ExpMetadata expMetadata=new ExpMetadata();
            File tempFile=new File(tempFilePath);
            String index=expMetadata.expData(tempFile.getName(),backupLogEntity.getTableName(),sql.toString(),backupLogEntity.getParentId());
            ZipUtils.writetxt(strategyVo.getIndexPath(),index,resultT);
            resultT.setSuccessMessage("备份K表{}成功,sql{}",backupLogEntity.getTableName(),sql);
            log.info("备份K表{}成功,sql{}",backupLogEntity.getTableName(),sql);
        } catch (Exception e) {
            resultT.setErrorMessage("备份K表{}失败,sql{},错误{}",backupLogEntity.getTableName(),sql, OwnException.get(e));
            log.error("备份K表{}失败,sql{},错误{}",backupLogEntity.getTableName(),sql, OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_14_ERROR.getKey());
            EiSendUtil.xuguException(backupLogEntity.getParentId(),resultT);
        }

    }
    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity,StrategyVo strategyVo,ResultT<String> resultT){
        StringBuilder sql=new StringBuilder();
        sql.append(" select a.* from ").append(backupLogEntity.getVTableName()).append(" a inner join");
        sql.append("(select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions()).append(") b");
        sql.append(" on a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());
        Select2File select2File=new Select2File();
        String tempFilePath=strategyVo.getTempPtah()+"/"+strategyVo.getVfileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getVTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            strategyVo.setVfilePath(tempFilePath);
            File tempFile=new File(tempFilePath);
            ExpMetadata expMetadata=new ExpMetadata();
            String index=expMetadata.expData(tempFile.getName(),backupLogEntity.getVTableName(),sql.toString(),backupLogEntity.getParentId());
            ZipUtils.writetxt(strategyVo.getIndexPath(),index,resultT);
            resultT.setSuccessMessage("备份V表{}成功,sql{}",backupLogEntity.getVTableName(),sql);

            log.info("备份V表{}成功,sql{}",backupLogEntity.getVTableName(),sql);
        } catch (Exception e) {
            resultT.setErrorMessage("备份K表{}失败,sql{},错误{}",backupLogEntity.getVTableName(),sql, OwnException.get(e));
            log.error("备份K表{}失败,sql{},错误{}",backupLogEntity.getVTableName(),sql, OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_14_ERROR.getKey());
            EiSendUtil.xuguException(backupLogEntity.getParentId(),resultT);
        }
    }

    public void deleteKtable(ClearLogEntity clearLogEntity, ClearVo clearVo, ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(clearLogEntity.getParentId());

        try {
            if(null!=clearLogEntity.getVTableName()){
                this.deleteXugu(clearLogEntity.getVTableName(),clearVo,clearLogEntity,resultT);
                if(!resultT.isSuccess()){
                    return;
                }
            }
            this.deleteXugu(clearLogEntity.getTableName(),clearVo,clearLogEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("执行虚谷删除分区失败:{}",OwnException.get(e));
            log.error("执行虚谷删除分区失败:{}",OwnException.get(e));

        }finally {
            DataSourceContextHolder.clearDataSource();

        }
    }
    public void deleteXugu(String tableName,ClearVo clearVo,ClearLogEntity clearLogEntity,ResultT<String> resultT) throws Exception {
        String schemaName=tableName.split("\\.")[0];
        String table=tableName.split("\\.")[1];
        DatabaseOperationService databaseOperationService= SpringUtil.getBean(DatabaseOperationService.class);
        List<Map<String,Object>> partList=databaseOperationService.selectXuguPartition(schemaName,table);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(partList!=null&&!partList.isEmpty()){
            for(Map<String,Object> map:partList){
                String partiName= (String) map.get("parti_name");
                String partiVal = (String) map.get("parti_val");
                long time = sdf.parse(partiVal.replace("'", "")).getTime();
                if(time>clearVo.getClearTime()){
                    break;
                }
                int loopNum=0;
                while (true){
                    loopNum++;
                    try {
                        databaseOperationService.deletePartition(tableName,partiName);
                        resultT.setSuccessMessage("删除表{},分区{}成功",tableName,partiName);
                        log.info("删除表{},分区{}成功",tableName,partiName);
                    } catch (Exception e) {
                        if(loopNum>3){
                            resultT.setErrorMessage("删除表{},分区{}异常;错误原因{}",tableName,partiName,OwnException.get(e));
                            log.error("删除表{},分区{}异常;错误原因{}",tableName,partiName,OwnException.get(e));
                            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_12_ERROR.getKey());
                            EiSendUtil.partitionException(partiName,clearLogEntity.getParentId(),resultT);
                            Thread.sleep(18000);
                            break;
                        }
                    }
                    break;
                }

            }
        }

    }
}

