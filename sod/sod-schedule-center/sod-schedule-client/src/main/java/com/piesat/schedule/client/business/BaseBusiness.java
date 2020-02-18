package com.piesat.schedule.client.business;

import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.util.ResultT;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-11 14:09
 **/
public abstract class BaseBusiness {

    public abstract void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT);

    public abstract void backUpVtable(BackupLogEntity backupLogEntity,StrategyVo strategyVo,ResultT<String> resultT);

}

