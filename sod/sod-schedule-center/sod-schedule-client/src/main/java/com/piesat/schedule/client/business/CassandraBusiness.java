package com.piesat.schedule.client.business;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.service.databse.CassandraService;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.util.ResultT;

import java.util.List;

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
}

