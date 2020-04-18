package com.piesat.schedule.client.business;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.service.databse.CassandraService;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.*;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 13:16
 **/
public class CassandraBusiness extends BaseBusiness{
    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {

    }

    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT) {

    }

    @Override
    public void deleteKtable(ClearLogEntity clearLogEntity, ClearVo clearVo, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        ConnectVo connectVo = dynamicDataSource.getConnectVo(clearLogEntity.getParentId());
        Cluster cluster=null;
        Session session=null;
        try {
            cluster =
                    Cluster.builder().addContactPoint(connectVo.getIp())
                            .withPort(connectVo.getPort())
                            .withCredentials(connectVo.getUserName(), connectVo.getPassWord())
                            .build();
            Metadata metadata = cluster.getMetadata();
            session = cluster.connect();
            if(null!=clearLogEntity.getVTableName()&& StringUtils.isNotNullString(clearLogEntity.getVTableName())){
                this.deleteCassandra(clearLogEntity.getVTableName(),clearLogEntity.getConditions(),session,metadata,resultT);
                if(!resultT.isSuccess()){
                    return;
                }
            }
            this.deleteCassandra(clearLogEntity.getTableName(),clearLogEntity.getConditions(),session,metadata,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
        } finally {
            if(null!=session){
                session.close();
            }
            if(null!=cluster){
                cluster.close();
            }
        }


    }

    @Override
    public long selectTableCount(String parentId, String ktable, String conditions, ResultT<String> resultT) {
        return 0;
    }

    @Override
    public List<TreeVo> findMeta(String parentId) {
            CassandraService cassandraService= SpringUtil.getBean(CassandraService.class);
            return cassandraService.findMeta(parentId);
    }

    @Override
    public void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT) {
        CassandraService cassandraService= SpringUtil.getBean(CassandraService.class);
        cassandraService.metaBack(metaBackupEntity,metadataVo,resultT);
    }

    @Override
    public void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT) {
        CassandraService cassandraService=SpringUtil.getBean(CassandraService.class);
        cassandraService.recoverMeta(recoverMetaVo,impInfo,recoverLogEntity,resultT);
    }

    @Override
    public void recoverStructedData(RecoverMetaVo recoverMetaVo, RecoverLogEntity recoverLogEntity, ResultT<String> resultT) {

    }

    @Override
    public List<TreeVo> findAllTableByIp(String parentId) {
        return null;
    }

    public void deleteCassandra(String tableName,String conditions,Session session, Metadata metadata,ResultT<String> resultT){
        try {
            String[] kkeyspaceAndTable=tableName.split("\\.");
            TableMetadata kTableMetadata= metadata.getKeyspace(kkeyspaceAndTable[0]).getTable(kkeyspaceAndTable[1]);
            long defaultTime=kTableMetadata.getOptions().getDefaultTimeToLive();
            resultT.setSuccessMessage("{}修改前表级生存时间为{},单位为秒",tableName,defaultTime);
            String sql="ALTER TABLE "+tableName+" WITH "+conditions;
            resultT.setSuccessMessage("{}修改表级生存时间为sql{}",tableName,sql);
            //session.execute(sql);
        } catch (Exception e) {
            resultT.setErrorMessage("{}修改表级生存时间失败{}",tableName, OwnException.get(e));
        }
    }
}

