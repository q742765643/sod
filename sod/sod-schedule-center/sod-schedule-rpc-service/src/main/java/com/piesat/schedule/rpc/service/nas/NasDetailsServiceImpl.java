package com.piesat.schedule.rpc.service.nas;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.schedule.dao.nas.NasDetailsDao;
import com.piesat.schedule.entity.nas.NasDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName : NasDetailsServiceImpl
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 16:53
 */
@Service
public class NasDetailsServiceImpl extends BaseService<NasDetailsEntity> {
    @Autowired
    private NasDetailsDao nasDetailsDao;
    @Override
    public BaseDao<NasDetailsEntity> getBaseDao() {
        return nasDetailsDao;
    }

}

