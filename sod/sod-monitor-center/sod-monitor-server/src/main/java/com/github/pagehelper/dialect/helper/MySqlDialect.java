package com.github.pagehelper.dialect.helper;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.util.MetaObjectUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-01 16:05
 **/
public class MySqlDialect extends AbstractHelperDialect {
    public MySqlDialect(){

    }

    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, Page page, BoundSql boundSql, CacheKey pageKey) {
       /* paramMap.put("First_PageHelper", Integer.valueOf(page.getStartRow()));
        paramMap.put("Second_PageHelper", Integer.valueOf(page.getPageSize()));
        pageKey.update(Integer.valueOf(page.getStartRow()));
        pageKey.update(Integer.valueOf(page.getPageSize()));
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList(boundSql.getParameterMappings());
            if (page.getStartRow() == 0) {
                newParameterMappings.add((new ParameterMapping.Builder(ms.getConfiguration(), "Second_PageHelper", Integer.class)).build());
            } else {
                newParameterMappings.add((new ParameterMapping.Builder(ms.getConfiguration(), "First_PageHelper", Integer.class)).build());
                newParameterMappings.add((new ParameterMapping.Builder(ms.getConfiguration(), "Second_PageHelper", Integer.class)).build());
            }

            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
*/
        return paramMap;
    }

    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        if (page.getStartRow() == 0) {
            sqlBuilder.append(" LIMIT "+page.getTotal());
        } else {
            sqlBuilder.append(" LIMIT "+page.getStartRow()+","+page.getPageSize());
        }

        return sqlBuilder.toString();
    }
}

