package com.piesat.dm.dao.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataapply.NewdataApplyEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 17:06
 */
@Repository
public interface NewdataApplyDao extends BaseDao<NewdataApplyEntity> {
    /*@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update t_sod_newdata_apply p set p.examine_status =?1, p.remark=?2, p.examiner=?3  where p.id = ?4 and p.d_data_id =?5 and p.table_name = ?6",nativeQuery = true)
    int updateStatus(int examine_status,  String remark, String examiner, String id, String d_data_id, String table_name);*/

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update t_sod_newdata_apply p set p.d_data_id =?1, p.data_class_id=?2  where p.id = ?3", nativeQuery = true)
    int updateDDateIdAndDataClassId(String d_data_id, String data_class_id, String apply_id);

    List<NewdataApplyEntity> findByDataClassIdAndUserId(String dataClassId, String userId);

}
