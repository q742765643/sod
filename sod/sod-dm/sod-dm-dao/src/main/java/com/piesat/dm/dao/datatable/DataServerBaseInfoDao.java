package com.piesat.dm.dao.datatable;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.datatable.DataServerBaseInfoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 解码配置
 *
 * @author cwh
 * @date 2020年 02月12日 11:38:13
 */
@Repository
public interface DataServerBaseInfoDao extends BaseDao<DataServerBaseInfoEntity> {
    List<DataServerBaseInfoEntity> findByDataCLassId(String dataCLassId);
}
