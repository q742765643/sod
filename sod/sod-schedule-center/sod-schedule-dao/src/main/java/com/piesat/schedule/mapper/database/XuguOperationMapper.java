package com.piesat.schedule.mapper.database;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 14:21
 **/
@Component
public interface XuguOperationMapper {
    public List<String> findXuguUsers();

    public List<String> findXuguRoles();

    public List<String> findXuguInstance();

    public List<String> findXuguTables(String schemaName);

    public List<String> findXuguViews(String schemaName);

    public List<String> findXuguSeqs(String schemaName);

    public List<String> findXuguTrigs(String schemaName);

    public List<String> findXuguProcs(String schemaName);


}

