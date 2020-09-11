package com.piesat.portal.rpc.mapstruct;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.portal.entity.FileManageEntity;
import com.piesat.portal.rpc.dto.FileManageDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component("fileManageMapstruct")
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileManageMapstruct extends BaseMapper<FileManageDto, FileManageEntity> {
}
