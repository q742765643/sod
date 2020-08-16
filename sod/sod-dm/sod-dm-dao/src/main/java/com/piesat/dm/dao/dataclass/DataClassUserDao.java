package com.piesat.dm.dao.dataclass;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.dataclass.DataClassUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资料和用户关系
 *
 * @author cwh
 * @date 2020年 07月31日 16:09:09
 */
@Repository
public interface DataClassUserDao extends BaseDao<DataClassUserEntity> {
    /**
     * 根据存储编码删除
     * @param dataclassId
     */
    void deleteByDataClassId(String dataclassId);

    /**
     * 根据存储编码查询
     * @param dataclassId
     * @return
     */
    List<DataClassUserEntity> findByDataClassId(String dataclassId);

    /**
     * 根据用户查询
     * @param userName
     * @return
     */
    List<DataClassUserEntity> findByUserName(String userName);

}
