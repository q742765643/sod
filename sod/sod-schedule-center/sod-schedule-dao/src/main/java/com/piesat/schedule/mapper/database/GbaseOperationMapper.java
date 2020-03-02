package com.piesat.schedule.mapper.database;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 14:21
 **/
@Component
public interface GbaseOperationMapper {
    public List<Map<String,Object>> findGbaseUsers();

    public List<String> findGbaseInstance();

    public List<String> findGbaseTables(String schemaName);


    public  void createGbaseSchema(String schemaName);


    public  void createGbaseUser(String sql);



}

