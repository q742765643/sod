package com.piesat.sod.system.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.sod.system.entity.ServiceCodeDefineEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 14:01
 */
@Repository
public interface ServiceCodeDefineDao extends BaseDao<ServiceCodeDefineEntity> {
    List<ServiceCodeDefineEntity> findByDbFcstEle(String dbFcstEle);
}
