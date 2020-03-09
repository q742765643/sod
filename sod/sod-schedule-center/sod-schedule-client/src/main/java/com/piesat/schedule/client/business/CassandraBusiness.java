package com.piesat.schedule.client.business;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.service.databse.CassandraService;
import com.piesat.schedule.client.service.databse.XuguService;
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
}

