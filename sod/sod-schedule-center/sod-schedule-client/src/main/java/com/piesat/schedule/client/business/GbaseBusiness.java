package com.piesat.schedule.client.business;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.service.databse.CassandraService;
import com.piesat.schedule.client.service.databse.GbaseService;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.TableForeignKeyUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.*;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-13 16:23
 **/
@Slf4j
public class GbaseBusiness extends BaseBusiness{
    public static Semaphore semaphore= new Semaphore(50);

    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {
        try {
            semaphore.acquire();
            StringBuilder sql=new StringBuilder();
            String tempFilePath=strategyVo.getTempPtah()+"/"+backupLogEntity.getFileName()+".txt";
            sql.append("rmt:select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions());
            this.executeCmd(sql,backupLogEntity.getTableName(),backupLogEntity.getParentId(),tempFilePath,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            this.writeTxt(strategyVo.getIndexPath(),backupLogEntity.getTableName(),
                    backupLogEntity.getFileName()+".txt",resultT);
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }finally {
            semaphore.release();
        }

    }
    //gabse 的bug 关联子查询有bug 只能改成in
    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {
        StringBuilder sql=new StringBuilder();
        String fks=backupLogEntity.getForeignKey();
        String kfk="";
        String vfk="";
        List<TableForeignKeyVo> tableForeignKeyVos= JSON.parseArray(fks,TableForeignKeyVo.class);
        if(null!=tableForeignKeyVos&&tableForeignKeyVos.size()>0){
            for(TableForeignKeyVo tableForeignKeyVo:tableForeignKeyVos){
                vfk=tableForeignKeyVo.getEleColumn();
                kfk=tableForeignKeyVo.getKeyColumn();
                if(tableForeignKeyVo.getKeyColumn().toUpperCase().equals("D_RECORD_ID")){
                    break;
                }
                if(tableForeignKeyVo.getKeyColumn().toUpperCase().equals("D_FILE_ID")){
                    break;
                }
            }

        }

        String tempFilePath=strategyVo.getTempPtah()+"/"+strategyVo.getVfileName()+".txt";
        sql.append("rmt:select a.* from ").append(backupLogEntity.getVTableName()).append(" a where "+vfk+" in ");
        sql.append("(select "+kfk+" from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions()).append(") ");
        String fkconditoins=TableForeignKeyUtil.getBackupSql(backupLogEntity.getForeignKey(),resultT);
        //sql.append(" where ").append(fkconditoins);
        if(!resultT.isSuccess()){
            return;
        }

        //sql.append(" on a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());
        this.executeCmd(sql,backupLogEntity.getVTableName(),backupLogEntity.getParentId(),tempFilePath,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        this.writeTxt(strategyVo.getIndexPath(),backupLogEntity.getVTableName(),
                strategyVo.getVfileName()+".txt",resultT);
    }
    public void executeCmd(StringBuilder sql,String tableName,String parentId,String tempFilePath,ResultT<String> resultT){
        StringBuilder cmd=new StringBuilder();
        Runtime r = Runtime.getRuntime();
        StringBuilder msg=new StringBuilder();
        //BufferedReader bufrIn = null;
        //BufferedReader bufrError = null;
        int exitVal=-1;
        try {
            DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
            ConnectVo connectVo=dynamicDataSource.getConnectVo(parentId);
            if(connectVo!=null){
                cmd.append("gccli -h ").append(connectVo.getIp())
                        .append(" -u")
                        .append(connectVo.getUserName())
                        .append(" -p").append(connectVo.getPassWord())
                        .append(" -e ")
                        .append("\"").append(sql)
                        .append(" INTO OUTFILE '").append(tempFilePath).append("'")
                        .append(" WITH HEAD FIELDS TERMINATED BY ','")
                        .append("\"");
                String[] commands = new String[]{"/bin/sh", "-c", cmd.toString()};

                Process proc = r.exec(commands);
                // exitVal = proc.waitFor();
                // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
                //bufrIn = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
                //bufrError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));

                // 读取输出
             /*   String line = null;
                while ((line = bufrIn.readLine()) != null) {
                    msg.append(line).append('\n');
                }
                while ((line = bufrError.readLine()) != null) {
                    msg.append(line).append('\n');
                }*/
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                Future<String> future1 = executorService.submit(()->{return handlerProcessBlock(proc.getInputStream());});
                Future<String> future2 = executorService.submit(()->{return handlerProcessBlock(proc.getErrorStream());});
                executorService.shutdown();
                if (proc.waitFor(6, TimeUnit.HOURS)){//程序在限定时间内执行完毕
                /* 退出码为0时 属于正常退出**/
                    exitVal=proc.exitValue();
                    msg.append(future1.get());
                    if (proc.exitValue() != 0){//执行shell出错 记录错误信息
                        msg.append(future2.get());
                    }
                }else {
                    log.info("gbase exec time out!");//执行shell超时
                    msg.append("gbase exec time out!");
                    executorService.shutdownNow();//终止线程池任务执行
                    proc.destroy();//kill 子进程
                }
            }else {
                resultT.setErrorMessage("gbase备份表{}失败,connect信息错误{}",tableName,sql.toString());
                log.error("gbase备份表{}失败,connect信息错误{}",tableName,sql.toString());

            }

        } catch (Exception e) {
            resultT.setErrorMessage("gbase备份表{}失败{},connect信息错误{}",tableName,sql.toString(),OwnException.get(e));
            log.error("gbase备份表{}失败{},connect信息错误{}",tableName,sql.toString(),OwnException.get(e));
        }finally {
            if(exitVal!=0){
                resultT.setErrorMessage("gbase备份表{}失败,cmd{},错误{}",tableName,cmd.toString(),msg);
                resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_15_ERROR.getKey());
                EiSendUtil.gbaseException(parentId,cmd.toString(),resultT);
                log.error("gbase备份表{}失败,cmd{},错误{}",tableName,cmd.toString(),msg);
            }else{
               resultT.setSuccessMessage("gbase备份表{}成功,cmd{}",tableName,cmd.toString());
               log.info("gbase备份表{}成功,cmd{}",tableName,cmd.toString());
            }

        }
    }

    public void writeTxt(String indexPath,String tableName,String fileName,ResultT<String> resultT){
        StringBuilder msg=new StringBuilder();
        msg.append("---data "+tableName+"---").append("\r\n");
        msg.append(fileName).append("\r\n");
        msg.append("---end data---").append("\r\n");
        ZipUtils.writetxt(indexPath,msg.toString(),resultT);

    }
    @Override
    public void deleteKtable(ClearLogEntity clearLogEntity, ClearVo clearVo, ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(clearLogEntity.getParentId());
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DatabaseOperationService databaseOperationService=SpringUtil.getBean(DatabaseOperationService.class);
            Date minDate=databaseOperationService.selectKtableMaxTime(clearLogEntity.getTableName(),clearLogEntity.getConditions());
            long startTime=minDate.getTime();
            if(clearVo.getClearTime()==0){
                resultT.setSuccessMessage("根据条件未按清除频率进行切割");

                this.deleteGbase(clearLogEntity,clearLogEntity.getConditions(),resultT);
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
                this.deleteGbase(clearLogEntity,conditions,resultT);
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
        DataSourceContextHolder.setDataSource(parentId);
        try {
            GbaseService gbaseService=SpringUtil.getBean(GbaseService.class);
            return gbaseService.findMeta();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return null;
    }

    @Override
    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT) {
        GbaseService gbaseService= SpringUtil.getBean(GbaseService.class);
        gbaseService.metaBack(metaBackupEntity,metadataVo,resultT);
    }

    @Override
    public void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT) {
        DataSourceContextHolder.setDataSource(recoverLogEntity.getParentId());
        try {
            log.info("gbase 恢复进入");
            GbaseService gbaseService=SpringUtil.getBean(GbaseService.class);
            gbaseService.recoverMeta(recoverMetaVo,impInfo,recoverLogEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("恢复失败{}",OwnException.get(e));
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
    }

    public void deleteGbase(ClearLogEntity clearLogEntity,String conditions,ResultT<String> resultT){
        try {
            DatabaseOperationService databaseOperationService=SpringUtil.getBean(DatabaseOperationService.class);
            if(null!=clearLogEntity.getVTableName()&&StringUtils.isNotNullString(clearLogEntity.getVTableName())){
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

    @Override
    public void recoverStructedData(RecoverMetaVo recoverMetaVo, RecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        GbaseService gbaseService=SpringUtil.getBean(GbaseService.class);
        gbaseService.recoverStructedData(recoverMetaVo,recoverLogEntity,resultT);
    }

    @Override
    public List<TreeVo> findAllTableByIp(String parentId) {
        DataSourceContextHolder.setDataSource(parentId);
        try {
            GbaseService gbaseService=SpringUtil.getBean(GbaseService.class);
            return gbaseService.findAllTableByIp();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return null;
    }

    private static String handlerProcessBlock(InputStream inputStream){
        BufferedReader reader = null;
        try {
            StringBuffer sb = new StringBuffer();
            String buff;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((buff = reader.readLine()) != null) {
                sb.append(buff.trim()).append("\n");
            }
            return sb.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}

