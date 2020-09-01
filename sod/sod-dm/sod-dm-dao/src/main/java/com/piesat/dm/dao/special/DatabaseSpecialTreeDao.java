package com.piesat.dm.dao.special;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.entity.special.DatabaseSpecialTreeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 9:44
 */
@Repository
public interface DatabaseSpecialTreeDao extends BaseDao<DatabaseSpecialTreeEntity> {

    List<DatabaseSpecialTreeEntity> findBySdbId(String sdbId);

    DatabaseSpecialTreeEntity findBySdbIdAndTypeId(String sdbId,String typeId);
}
