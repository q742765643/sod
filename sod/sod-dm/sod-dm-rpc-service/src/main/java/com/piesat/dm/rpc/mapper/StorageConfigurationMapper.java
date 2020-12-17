package com.piesat.dm.rpc.mapper;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.AdvancedConfigEntity;
import com.piesat.dm.rpc.dto.AdvancedConfigDto;
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
public interface StorageConfigurationMapper extends BaseMapper<AdvancedConfigDto, AdvancedConfigEntity> {
}
