package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.StorageConfigurationEntity;
import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:37
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorageConfigurationMapper extends BaseMapper<StorageConfigurationDto, StorageConfigurationEntity> {
}
