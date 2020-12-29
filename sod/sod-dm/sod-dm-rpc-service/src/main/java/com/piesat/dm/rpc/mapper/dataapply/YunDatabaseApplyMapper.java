package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyEntity;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YunDatabaseApplyMapper extends BaseMapper<YunDatabaseApplyDto, YunDatabaseApplyEntity> {
}
