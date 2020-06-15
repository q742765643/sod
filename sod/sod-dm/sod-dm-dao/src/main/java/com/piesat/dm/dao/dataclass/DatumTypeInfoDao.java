package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DatumTypeInfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 16:19:34
 */
@Repository
public interface DatumTypeInfoDao extends BaseDao<DatumTypeInfoEntity> {
    @Query(value = "select * from TAB_OMIN_CM_CC_DATUMTYPEINFO A left join T_SOD_DATA_CLASS B ON A.",nativeQuery = true)
    List<Map<String, Object>> getMyAll();
}
