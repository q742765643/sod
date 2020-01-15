package com.piesat.dm.dao;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.DataClassEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DataClassDao extends BaseDao<DataClassEntity> {

    @Query(value = "select A.data_class_id,A.class_name,A.parent_id,A.type,A.d_data_id,A.meta_data_name,A.is_all_line,A.use_base_info,B.id logic_id,B.logic_flag,B.storage_type,B.database_id,B.is_complete,C.database_name,C.schema_name,C.database_classify,C.stop_use,C.database_define_id,D.database_name database_name_f,D.database_instance,D.database_type,E.logic_name" +
            " from T_SOD_DATA_CLASS A left join T_SOD_DATA_LOGIC B on A.data_class_id = B.data_class_id left join T_SOD_LOGIC_DEFINE E on B.logic_flag=E.logic_flag  left join T_SOD_DATABASE C on B.database_id=C.id left join T_SOD_DATABASE_DEFINE D on C.database_define_id = D.id  where A.data_class_id in (?1) and (?3 is null or A.d_data_id like %?3%) and (?2 is null or class_name like %?2%) ", nativeQuery = true)
    List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId);

    void deleteByDataClassId(String dataClassId);

    DataClassEntity findByDataClassId(String dataClassId);
}
