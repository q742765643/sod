package com.piesat.schedule.client.service.move;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.clear.ClearLogDao;
import com.piesat.schedule.dao.move.MoveLogDao;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-06 14:11
 **/
@Service
public class MoveLogService extends BaseService<MoveLogEntity>{
    @Autowired
    private MoveLogDao moveLogDao;

    @Override
    public BaseDao<MoveLogEntity> getBaseDao() {
        return moveLogDao;
    }

    

}

