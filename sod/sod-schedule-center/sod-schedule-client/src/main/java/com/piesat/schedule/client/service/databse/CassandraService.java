package com.piesat.schedule.client.service.databse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.util.CmdUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.ConnectVo;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-25 10:34
 **/
@Slf4j
@Service
public class CassandraService {
    public List<TreeVo> findMeta(String parenId) {
        List<TreeVo> treeVos = new ArrayList<>();
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo = dynamicDataSource.getConnectVo(parenId);
        Cluster cluster =
                Cluster.builder().addContactPoint(connectVo.getIp())
                        .withPort(connectVo.getPort())
                        .withCredentials(connectVo.getUserName(), connectVo.getPassWord())
                        .build();

        try {
            Metadata metadata = cluster.getMetadata();
            TreeVo pInstanceTreeVo = new TreeVo("", "数据库", "数据库", true);
            treeVos.add(pInstanceTreeVo);
            List<KeyspaceMetadata> keyspaceMetadatas = metadata.getKeyspaces();
            if (!keyspaceMetadatas.isEmpty()) {
                for (KeyspaceMetadata keyspaceMetadata : keyspaceMetadatas) {
                    TreeVo treeInstance = new TreeVo();
                    treeInstance.setId(keyspaceMetadata.getName());
                    treeInstance.setPId("数据库");
                    treeInstance.setName(keyspaceMetadata.getName());
                    treeInstance.setParent(true);
                    treeVos.add(treeInstance);
                    this.getInstanceMeta(keyspaceMetadata, treeVos);
                }

            }
        } finally {
            cluster.close();
        }
        return treeVos;
    }

    public void getInstanceMeta(KeyspaceMetadata keyspaceMetadata, List<TreeVo> treeVos) {
        Collection<TableMetadata> tables = keyspaceMetadata.getTables();
        String instance = keyspaceMetadata.getName();
        TreeVo pTableTreeVo = new TreeVo(instance, "表" + instance, "表", true);
        treeVos.add(pTableTreeVo);
        if (!tables.isEmpty()) {
            for (TableMetadata table : tables) {
                TreeVo treeTable = new TreeVo();
                treeTable.setId(instance + "." + table.getName());
                treeTable.setPId("表" + instance);
                treeTable.setName(instance + "." + table.getName());
                treeVos.add(treeTable);
            }
        }


    }

    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT) {
        if(!metadataVo.getSchema().isEmpty()){
            for(String schema:metadataVo.getSchema()){
                this.expInstance(schema,metadataVo,metaBackupEntity,resultT);
            }
        }

        if (!metadataVo.getTable().isEmpty()) {
            for (String table : metadataVo.getTable()) {
                if (metaBackupEntity.getIsStructure().indexOf("0") != -1) {
                    this.expTable(table, metadataVo, metaBackupEntity, resultT);
                }
            }
        }
    }

    public void expInstance(String instance, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity, ResultT<String> resultT){
        try {
            DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
            ConnectVo connectVo =dynamicDataSource.getConnectVo(metaBackupEntity.getParentId());
            String path = metadataVo.getParentPath() + "/INSTANCE_" + instance + ".sql";
            StringBuilder sql=new StringBuilder();
            sql.append("cqlsh ").append(connectVo.getIp()).append(" ").append(connectVo.getPort());
            sql.append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord());
            sql.append(" -e ").append("'describe keyspace "+instance+"'>>"+path);
            log.info("cassandra导出表空间sql{}:",sql.toString());
            String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
            int exit = CmdUtil.expCmd(commands, resultT);
            if(exit==0){
                StringBuilder success=new StringBuilder();
                ZipUtils.readFileCansandra(path+".tmp",success,resultT);
                ZipUtils.writetxt(path,success.toString(),resultT);
                StringBuilder writePath=new StringBuilder();
                writePath.append("---instance ").append(instance).append("---\r\n");
                writePath.append("INSTANCE_" + instance + ".sql").append("\r\n");
                writePath.append("---end instance---\r\n");
                ZipUtils.writetxt(metadataVo.getIndexPath(),writePath.toString(),resultT);
            }else{
                resultT.setErrorMessage("数据库模式{}备份失败", instance);

            }
        } catch (Exception e) {
            resultT.setErrorMessage("数据库模式{}备份失败", instance);
            log.error(OwnException.get(e));
        }


    }

    public void expTable(String tableInfo, MetadataVo metadataVo, MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {
        try {
            DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
            ConnectVo connectVo = dynamicDataSource.getConnectVo(metaBackupEntity.getParentId());
            String path = metadataVo.getParentPath() + "/TABLE_" + tableInfo + ".sql";
            StringBuilder sql = new StringBuilder();
            sql.append("cqlsh ").append(connectVo.getIp()).append(" ").append(connectVo.getPort());
            sql.append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord());
            sql.append(" -e ").append("'describe table " + tableInfo + "'>>" + path);
            String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
            log.info("开始备份表{}结构", tableInfo);
            int exit = CmdUtil.expCmd(commands, resultT);
            if (exit == 0) {
                StringBuilder writePath = new StringBuilder();
                writePath.append("---table ").append(tableInfo).append("---\r\n");
                writePath.append("TABLE_" + tableInfo + ".sql").append("\r\n");
                writePath.append("---end table---\r\n");
                ZipUtils.writetxt(metadataVo.getIndexPath(), writePath.toString(), resultT);
            } else {
                resultT.setErrorMessage("表结构{}备份失败", tableInfo);
                log.error("表结构{}备份失败", tableInfo);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("表结构{}备份失败", tableInfo);
            log.error(OwnException.get(e));
        }
    }

    public void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT) {

        Map<String, String> map = ZipUtils.readFile(recoverMetaVo.getIndexPath(), resultT);
        Set<String> schemas=impInfo.get(Type.SCHEMA);
        if(null!=schemas&&!schemas.isEmpty()){
            for(String schema:schemas){
                String realPath = recoverMetaVo.getUnzipPath() + "/" + map.get("---instance " + schema + "---");
                this.recoverCassandraTable(recoverLogEntity.getParentId(),schema,realPath,resultT);
            }
        }

        Set<String> tables=impInfo.get(Type.TABLE);
        if(null!=tables&&!tables.isEmpty()){
            for(String table:tables){
                String realPath = recoverMetaVo.getUnzipPath() + "/" + map.get("---table " + table + "---");
                this.recoverCassandraTable(recoverLogEntity.getParentId(),table,realPath,resultT);
            }
        }


    }

    public void recoverCassandraTable(String parentId, String tableOrInstance, String path, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo = dynamicDataSource.getConnectVo(parentId);
        StringBuilder sql=new StringBuilder();
        sql.append("cqlsh ").append(connectVo.getIp()).append(" ").append(connectVo.getPort());
        sql.append(" -u").append(connectVo.getUserName()).append(" -p").append(connectVo.getPassWord());
        sql.append(" -f ").append(path);

        String[] commands = new String[]{"/bin/sh", "-c", sql.toString()};
        int  exit=CmdUtil.expCmd(commands,resultT);
        if(exit!=0){
            resultT.setErrorMessage("表或者模式{}恢复失败",tableOrInstance);
        }

    }

}

