package com.piesat.portal.mapper;

import com.piesat.portal.entity.DepartManageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DepartManageMapper {
    List<DepartManageEntity> findSupDepart(String deptunicode);
}
