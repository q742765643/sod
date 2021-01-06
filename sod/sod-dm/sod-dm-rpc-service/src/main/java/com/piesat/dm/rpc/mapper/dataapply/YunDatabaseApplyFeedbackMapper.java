package com.piesat.dm.rpc.mapper.dataapply;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyFeedbackEntity;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyFeedbackDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YunDatabaseApplyFeedbackMapper extends BaseMapper<YunDatabaseApplyFeedbackDto, YunDatabaseApplyFeedbackEntity> {
}
