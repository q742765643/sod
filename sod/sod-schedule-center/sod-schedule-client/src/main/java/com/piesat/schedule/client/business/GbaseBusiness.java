package com.piesat.schedule.client.business;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-13 16:23
 **/
public class GbaseBusiness extends BaseBusiness{
    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {
        StringBuilder sql=new StringBuilder();
        String tempFilePath=strategyVo.getTempPtah()+"/"+backupLogEntity.getFileName()+".txt";
        sql.append("rmt:select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions());
        this.executeCmd(sql,backupLogEntity.getVTableName(),backupLogEntity.getParentId(),tempFilePath,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        this.writeTxt(strategyVo.getIndexPath(),backupLogEntity.getTableName(),
                backupLogEntity.getFileName()+".txt",resultT);

    }

    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {
        StringBuilder sql=new StringBuilder();
        String tempFilePath=strategyVo.getTempPtah()+"/"+strategyVo.getVfileName()+".txt";
        sql.append(" select a.* from ").append(backupLogEntity.getVTableName()).append(" a inner join");
        sql.append("(select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions()).append(") b");
        sql.append(" on a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());
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
        StringBuilder msg=null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        int exitVal=-1;
        try {
            DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
            DruidDataSource dataSource = (DruidDataSource) dynamicDataSource.getDataSourceByMap(parentId);
            if(dataSource!=null){
                URL url=new URL(dataSource.getUrl());
                cmd.append("gccli -h ").append(url.getHost())
                        .append(" -u")
                        .append(dataSource.getUsername())
                        .append(" -p").append(dataSource.getPassword())
                        .append(" -e ")
                        .append("\"").append(sql)
                        .append(" INTO OUTFILE '").append(tempFilePath).append("'")
                        .append(" WITH HEAD FIELDS TERMINATED BY ','")
                        .append("\"");
                String[] commands = new String[]{"/bin/sh", "-c", cmd.toString()};

                Process proc = r.exec(commands);
                exitVal = proc.waitFor();
                // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
                bufrIn = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
                bufrError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));

                // 读取输出
                String line = null;
                while ((line = bufrIn.readLine()) != null) {
                    msg.append(line).append('\n');
                }
                while ((line = bufrError.readLine()) != null) {
                    msg.append(line).append('\n');
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(exitVal!=0){
                resultT.setErrorMessage("gbase备份表{}失败,cmd{},错误{}",tableName,cmd,msg);
                resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_15_ERROR.getKey());
                EiSendUtil.gbaseException(parentId,cmd.toString(),resultT);
            }else{
               resultT.setSuccessMessage("gbase备份表{}成功,cmd{}",tableName,cmd);
            }

        }
    }

    public void writeTxt(String indexPath,String tableName,String fileName,ResultT<String> resultT){
        StringBuilder msg=new StringBuilder();
        msg.append("---data "+tableName+"---").append("\r\n");
        msg.append("gabse").append("\r\n");
        msg.append(fileName);
        msg.append("---end data---");
        ZipUtils.writetxt(indexPath,msg.toString(),resultT);

    }
}

