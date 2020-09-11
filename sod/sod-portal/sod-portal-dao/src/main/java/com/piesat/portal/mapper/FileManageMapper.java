package com.piesat.portal.mapper;

import com.piesat.portal.entity.FileManageEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileManageMapper {
    /**
     * 条件分页查询
     * @param fileManageEntity
     * @return
     */
    public List<FileManageEntity> selectPageList(@Param("entity")FileManageEntity fileManageEntity);
}
