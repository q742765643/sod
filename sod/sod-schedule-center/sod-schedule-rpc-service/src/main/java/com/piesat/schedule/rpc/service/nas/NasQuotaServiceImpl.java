package com.piesat.schedule.rpc.service.nas;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.nas.NasQuotaDao;
import com.piesat.schedule.entity.nas.NasQuotaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName : NasQuotaServiceImpl
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:50
 */
@Service
public class NasQuotaServiceImpl extends BaseService<NasQuotaEntity> {
    @Autowired
    private  NasQuotaDao nasQuotaDao;
    @Override
    public BaseDao<NasQuotaEntity> getBaseDao() {
        return nasQuotaDao;
    }
}

