package com.piesat.dm.mapper;

import com.piesat.dm.entity.StorageConfigurationEntity;
import org.springframework.stereotype.Component;

/**
 * mybatis修改Mapper
 *
 * @author cwh
 * @date 2020年 02月12日 09:26:56
 */
@Component
public interface MybatisModifyMapper {
    void updateDataAuthorityConfig(StorageConfigurationEntity sce);
}
