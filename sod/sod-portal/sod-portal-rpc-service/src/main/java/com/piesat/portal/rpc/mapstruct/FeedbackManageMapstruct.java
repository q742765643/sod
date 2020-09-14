package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.FeedbackManageEntity;
import com.piesat.portal.rpc.dto.FeedbackManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("feedbackManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackManageMapstruct extends BaseMapper<FeedbackManageDto, FeedbackManageEntity> {
}
