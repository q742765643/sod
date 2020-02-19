package com.piesat.schedule.client.util;

import com.alibaba.fastjson.JSON;
import com.piesat.schedule.client.vo.TableForeignKeyVo;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 15:30
 **/
public class TableForeignKeyUtil {
    public static String getBackupSql(String fk){
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

    public static String getMoveVsql(String fk, Map<String,Object> map){
        StringBuilder sql=new StringBuilder();
        List<TableForeignKeyVo> tableForeignKeyVos= JSON.parseArray(fk,TableForeignKeyVo.class);
        int i=0;
        for(TableForeignKeyVo tableForeignKeyVo:tableForeignKeyVos){
            i++;
            sql.append(tableForeignKeyVo.getEleColumn().toUpperCase()).append("'");
            sql.append(map.get(tableForeignKeyVo.getKeyColumn())).append("'");
            if(i!=tableForeignKeyVos.size()) {
                sql.append( " and ");
            }
        }
        return sql.toString();
    }
    public static String getMoveKsql(String primaryKey, Map<String,Object> map){
        StringBuilder sql=new StringBuilder();
        sql.append(primaryKey.toUpperCase()).append("'").append(map.get(primaryKey)).append("'");
        return sql.toString();
    }


}

