package com.piesat.schedule.rpc.mapstruct.move;

import com.piesat.common.jpa.BaseMapper;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

/**
 * Created by zzj on 2019/12/24.
 */
@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MoveMapstruct extends BaseMapper<MoveDto,MoveEntity>{

}
