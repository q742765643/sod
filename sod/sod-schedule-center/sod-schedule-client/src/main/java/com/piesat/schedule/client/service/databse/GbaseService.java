package com.piesat.schedule.client.service.databse;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.util.CmdUtil;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.mapper.database.GbaseOperationMapper;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 17:21
 **/
@Slf4j
@Service
public class GbaseService {
    @Autowired
    private GbaseOperationMapper gbaseOperationMapper;
    public List<TreeVo> findMeta(){
        List<TreeVo> treeVos=new ArrayList<>();
        TreeVo pUserTreeVo=new TreeVo("","用户","用户",true);
        treeVos.add(pUserTreeVo);
        List<Map<String,Object>> users=gbaseOperationMapper.findGbaseUsers();
        if(!users.isEmpty()){
            for(Map<String,Object> user:users){
                TreeVo treeUser=new TreeVo();
                treeUser.setId(user.get("USER")+"--"+user.get("UUID"));
                treeUser.setPId("用户");
                treeUser.setName(user.get("USER")+"--"+user.get("UUID"));
                treeVos.add(treeUser);
            }
        }

        TreeVo pInstanceTreeVo=new TreeVo("","数据库","数据库",true);
        treeVos.add(pInstanceTreeVo);
        List<String> instances=gbaseOperationMapper.findGbaseInstance();
        if(!instances.isEmpty()){
            for(String instance:instances){
                TreeVo treeInstance=new TreeVo();
                treeInstance.setId(instance);
                treeInstance.setPId("数据库");
                treeInstance.setName(instance);
                treeVos.add(treeInstance);
                this.getInstanceMeta(instance,treeVos);
            }
        }

        return treeVos;
    }

    public void getInstanceMeta(String instance,List<TreeVo> treeVos){
        TreeVo pTableTreeVo=new TreeVo(instance,"表"+instance,"表",true);
        treeVos.add(pTableTreeVo);
        List<String> tables=gbaseOperationMapper.findGbaseTables(instance);
        if(!tables.isEmpty()){
            for(String table:tables){
                TreeVo treeTable=new TreeVo();
                treeTable.setId(instance+"."+table);
                treeTable.setPId("表"+instance);
                treeTable.setName(instance+"."+table);
                treeVos.add(treeTable);
            }
        }


    }


    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT){
        if(!metadataVo.getUsers().isEmpty()){
           for(String user:metadataVo.getUsers()){
               this.expUser(user,metadataVo,metaBackupEntity,resultT);
           }
        }
        if(!metadataVo.getTable().isEmpty()){
            for(String table:metadataVo.getTable()){
                if(metaBackupEntity.getIsStructure().indexOf("0")!=-1){
                   this.expTable(table,metadataVo,metaBackupEntity,resultT);
                }
                if(metadataVo.isExpData()){
                    this.expData(table,metadataVo,metaBackupEntity,resultT);
                }
            }


        }
    }
    public void expUser(String userAndUuid, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity,ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        DruidDataSource dataSource = (DruidDataSource) dynamicDataSource.getDataSourceByMap(metaBackupEntity.getParentId());
        if (dataSource != null) {
            try {
                log.info("开始备份用户{}",userAndUuid);
                String user=userAndUuid.split("--")[0];
                String path=metadataVo.getParentPath()+"/USER_"+userAndUuid+".sql";
                URL url=new URL(dataSource.getUrl());
                StringBuilder sql = new StringBuilder();
                sql.append("gccli -h ").append(url.getHost())
                        .append(" -u").append(dataSource.getUsername()).append(" -p").append(dataSource.getPassword())
                        .append(" -N -s ").append(" -e ").append("\"");
                sql.append("show grants for " + user + "").append("\"").append(">>" + path);
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit=CmdUtil.expCmd(commands,resultT);
                if(exit==0){
                    StringBuilder writePath=new StringBuilder();
                    writePath.append("---user ").append("USER_"+userAndUuid+".sql").append("---\r\n");
                    writePath.append("USER_"+userAndUuid+".sql").append("\r\n");
                    writePath.append("---end user---\r\n");
                    ZipUtils.writetxt(metadataVo.getIndexPath(),writePath.toString(),resultT);
                }else{
                    resultT.setSuccessMessage("用户{}备份失败",userAndUuid);
                    log.error("用户{}备份失败",userAndUuid);

                }
            } catch (MalformedURLException e) {
                log.error(OwnException.get(e));
            }

        }
    }
    public void expTable(String tableInfo, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity,ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        DruidDataSource dataSource = (DruidDataSource) dynamicDataSource.getDataSourceByMap(metaBackupEntity.getParentId());
        if (dataSource != null) {
            try {
                log.info("开始备份表{}结构",tableInfo);
                String path=metadataVo.getParentPath()+"/TABLE_"+tableInfo+".sql";
                String instance=tableInfo.split("\\.")[0];
                String tableName=tableInfo.split("\\.")[1];
                URL url=new URL(dataSource.getUrl());
                StringBuilder sql = new StringBuilder();
                sql.append("gcdump  -h ").append(url.getHost())
                        .append(" -u").append(dataSource.getUsername()).append(" -p").append(dataSource.getPassword())
                        .append(" "+instance + " ").append(tableName);
                sql.append(">>" + path + "");
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit=CmdUtil.expCmd(commands,resultT);
                if(exit==0){
                    StringBuilder write=new StringBuilder();
                    ZipUtils.readFile(path,write,resultT);
                    ZipUtils.writeFile(path,write,resultT);
                    StringBuilder writePath=new StringBuilder();
                    writePath.append("---table ").append(instance).append(".").append(tableName).append("---\r\n");
                    writePath.append("TABLE_"+tableInfo+".sql").append("\r\n");
                    writePath.append("---end table---\r\n");
                    ZipUtils.writetxt(metadataVo.getIndexPath(),writePath.toString(),resultT);
                }else{
                    resultT.setSuccessMessage("表结构{}备份失败",tableInfo);
                    log.error("表结构{}备份失败",tableInfo);
                }
            } catch (MalformedURLException e) {
                log.error(OwnException.get(e));
            }

        }
    }
    public void expData(String table, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity,ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        DruidDataSource dataSource = (DruidDataSource) dynamicDataSource.getDataSourceByMap(metaBackupEntity.getParentId());
        if (dataSource != null) {
            try {
                URL url=new URL(dataSource.getUrl());
                String path=metadataVo.getParentPath()+"/DATA_"+table+".txt";
                StringBuilder sql = new StringBuilder();
                sql.append("gccli -h ").append(url.getHost())
                        .append(" -u")
                        .append(dataSource.getUsername())
                        .append(" -p").append(dataSource.getPassword())
                        .append(" -e ")
                        .append("\"").append("rmt:select * from ").append(table).append(" where ").append(metaBackupEntity.getConditions())
                        .append(" INTO OUTFILE '").append(path).append("'")
                        .append(" WITH HEAD FIELDS TERMINATED BY ','")
                        .append("\"");
                String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
                int exit= CmdUtil.expCmd(commands,resultT);
                if(exit==0){
                    StringBuilder writePath=new StringBuilder();
                    writePath.append("---data "+table+"---").append("\r\n");
                    writePath.append("DATA_"+table+".txt");
                    writePath.append("---end data---");
                    ZipUtils.writetxt(metadataVo.getIndexPath(),writePath.toString(),resultT);
                }else{
                    resultT.setSuccessMessage("表数据{}备份失败",table);
                    log.error("表数据{}备份失败",table);
                }
            } catch (MalformedURLException e) {
                log.error(OwnException.get(e));
            }

        }
    }

}

