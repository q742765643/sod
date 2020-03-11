package com.piesat.schedule.client.business;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 14:09
 **/
public abstract class BaseBusiness {

    public abstract void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT);

    public abstract void backUpVtable(BackupLogEntity backupLogEntity,StrategyVo strategyVo,ResultT<String> resultT);


    public abstract void deleteKtable(ClearLogEntity clearLogEntity, ClearVo clearVo, ResultT<String> resultT);


    public abstract long selectTableCount(String parentId,String ktable, String conditions, ResultT<String> resultT);

    public abstract List<TreeVo> findMeta(String parentId);

    public abstract void metaBack(MetaBackupEntity metaBackupEntity, MetadataVo metadataVo, ResultT<String> resultT);

    public abstract void recoverMeta(RecoverMetaVo recoverMetaVo, Map<Type, Set<String>> impInfo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT);

    public abstract void recoverStructedData(RecoverMetaVo recoverMetaVo, RecoverLogEntity recoverLogEntity, ResultT<String> resultT);

    public abstract List<TreeVo> findAllTableByIp(String parentId);

}

