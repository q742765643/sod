package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.AdvancedConfigEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:31
 */
@Repository
public interface AdvancedConfigDao extends BaseDao<AdvancedConfigEntity> {
    List<AdvancedConfigEntity> findByTableId(String id);
}
