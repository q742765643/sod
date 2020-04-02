package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.CloudDatabaseApplyEntity;
import com.piesat.dm.rpc.dto.dataapply.CloudDatabaseApplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:10
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CloudDatabaseApplyMapper extends BaseMapper<CloudDatabaseApplyDto, CloudDatabaseApplyEntity> {
}
