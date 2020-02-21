package com.piesat.schedule.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.vo.TableForeignKeyVo;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 15:30
 **/
@Slf4j
public class TableForeignKeyUtil {
    public static String getBackupSql(String fk,ResultT<String> resultT){
        if("null".equals(fk)||null==fk|| !StringUtils.isNotNullString(fk)){
            resultT.setErrorMessage("外键信息不能为空！请检查存储管理表结构配置");
            log.error("外键信息不能为空！请检查存储管理表结构配置");
            return "";
        }
        StringBuilder sql=new StringBuilder();
        List<TableForeignKeyVo> tableForeignKeyVos= JSON.parseArray(fk,TableForeignKeyVo.class);
        int i=0;
        for(TableForeignKeyVo tableForeignKeyVo:tableForeignKeyVos){
            i++;
            sql.append(" a.").append(tableForeignKeyVo.getEleColumn().toUpperCase());
            sql.append("=b.").append(tableForeignKeyVo.getKeyColumn().toUpperCase());
            if(i!=tableForeignKeyVos.size()) {
                sql.append( " and ");
            }
        }
        return sql.toString();
    }

    public static String getMoveVsql(String fk, Map<String,Object> map,ResultT<String> resultT){
        if("null".equals(fk)||null==fk|| !StringUtils.isNotNullString(fk)){
            resultT.setErrorMessage("外键信息不能为空！请检查存储管理表结构配置");
            log.error("外键信息不能为空！请检查存储管理表结构配置");

            return "";
        }
        StringBuilder sql=new StringBuilder();
        List<TableForeignKeyVo> tableForeignKeyVos= JSON.parseArray(fk,TableForeignKeyVo.class);
        int i=0;
        for(TableForeignKeyVo tableForeignKeyVo:tableForeignKeyVos){
            i++;
            sql.append(tableForeignKeyVo.getEleColumn().toUpperCase()).append("='");
            sql.append(map.get(tableForeignKeyVo.getKeyColumn())).append("'");
            if(i!=tableForeignKeyVos.size()) {
                sql.append( " and ");
            }
        }
        return sql.toString();
    }
    public static String getMoveKsql(String primaryKey, Map<String,Object> map,ResultT<String> resultT){
        if("null".equals(primaryKey)||null==primaryKey||!StringUtils.isNotNullString(primaryKey)){
            resultT.setErrorMessage("主键信息不能为空！请检查存储管理表结构配置");
            log.error("主键信息不能为空！请检查存储管理表结构配置");

            return "";
        }
        StringBuilder sql=new StringBuilder();
        sql.append(primaryKey.toUpperCase()).append("='").append(map.get(primaryKey.toUpperCase())).append("'");
        return sql.toString();
    }
    public static void main(String[] args) {
        List<TableForeignKeyVo> tableForeignKeyVos=new ArrayList<>();
        TableForeignKeyVo tableForeignKeyVo=new TableForeignKeyVo();
        tableForeignKeyVo.setKeyColumn("TASK_ID");
        tableForeignKeyVo.setEleColumn("TASK_ID");
        tableForeignKeyVos.add(tableForeignKeyVo);
        tableForeignKeyVos.add(tableForeignKeyVo);
        System.out.println(JSON.toJSONString(tableForeignKeyVos, SerializerFeature.DisableCircularReferenceDetect));
    }

}

