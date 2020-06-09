/*
package com.piesat.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.piesat.common.annotation.MybatisAnnotation;
import com.piesat.common.jpa.entity.BaseEntity;
import com.piesat.common.utils.reflect.ReflectUtils;
import com.piesat.util.BaseDto;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

*/
/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-10 13:12
 **//*


@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        List<ResultMap> resultMaps=mappedStatement.getResultMaps();
        if (StringUtils.isBlank(boundSql.getSql())) {
            return null;
        }
        String originalSql = boundSql.getSql().trim();
        String add="";
        if (parameter instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) parameter;
            CCJSqlParserManager pm = new CCJSqlParserManager();
            Statement parse = pm.parse(new StringReader(originalSql));
            Select noOrderSelect = (Select)parse;
            SelectBody selectBody = noOrderSelect.getSelectBody();
            if (selectBody instanceof SetOperationList){
                return invocation.proceed();
            }
            add=this.resetSql(entity.getParams(),selectBody,resultMaps);
            if(com.piesat.common.utils.StringUtils.isNotNullString(add)) {
                try {

                    PlainSelect setOperationList = (PlainSelect)selectBody;
                    originalSql = noOrderSelect.toString();
                    if (null!=setOperationList.getOrderByElements()){
                        setOperationList.setOrderByElements(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = originalSql+" order by  " + add;
                originalSql = sql;
            }
        }  if (parameter instanceof BaseDto) {
            BaseDto entity = (BaseDto) parameter;
            CCJSqlParserManager pm = new CCJSqlParserManager();
            Statement parse = pm.parse(new StringReader(originalSql));
            Select noOrderSelect = (Select)parse;
            SelectBody selectBody = noOrderSelect.getSelectBody();
            if (selectBody instanceof SetOperationList){
                return invocation.proceed();
            }
            add=this.resetSql(entity.getParams(),selectBody,resultMaps);

            if(com.piesat.common.utils.StringUtils.isNotNullString(add)) {
                try {

                    PlainSelect setOperationList = (PlainSelect)selectBody;
                    originalSql = noOrderSelect.toString();
                    if (null!=setOperationList.getOrderByElements()){
                        setOperationList.setOrderByElements(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = originalSql+" order by  " + add;
                originalSql = sql;
            }
        }else if(parameter instanceof Map){
            Map map = (Map) parameter;
            String pp= null;
            try {
                pp = (String) map.get("params");
            } catch (Exception e) {
               // e.printStackTrace();
            }
            CCJSqlParserManager pm = new CCJSqlParserManager();
            Statement parse = pm.parse(new StringReader(originalSql));
            Select noOrderSelect = (Select)parse;
            SelectBody selectBody = noOrderSelect.getSelectBody();
            if (selectBody instanceof SetOperationList){
                return invocation.proceed();
            }
            add=this.resetSql(pp,selectBody,resultMaps);
            if(com.piesat.common.utils.StringUtils.isNotNullString(add)){
                try {
                    PlainSelect setOperationList = (PlainSelect)selectBody;
                    originalSql = noOrderSelect.toString();
                    if (null!=setOperationList.getOrderByElements()){
                        setOperationList.setOrderByElements(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = originalSql+" order by  " + add;
                originalSql = sql;
            }

        }
       if(!com.piesat.common.utils.StringUtils.isNotNullString(add)){
            return invocation.proceed();
        }
        //String mid = mappedStatement.getId();
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), originalSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        if (ReflectUtils.getFieldValue(boundSql, "metaParameters") != null) {
            MetaObject mo = (MetaObject) ReflectUtils.getFieldValue(boundSql, "metaParameters");
            ReflectUtils.setFieldValue(newBoundSql, "metaParameters", mo);
        }
        if (ReflectUtils.getFieldValue(boundSql, "additionalParameters") != null) {
            Map<String, Object> additionalParameters =  ReflectUtils.getFieldValue(boundSql, "additionalParameters");
            ReflectUtils.setFieldValue(newBoundSql, "additionalParameters", additionalParameters);
        }
        MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

        invocation.getArgs()[0] = newMs;

        return invocation.proceed();
    }



    public static String replace(String string, String toReplace, String replacement) {
//        int pos = string.lastIndexOf(toReplace);
        int pos = string.indexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }



    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public String resetSql(String param,SelectBody selectBody,List<ResultMap> resultMaps) {
        List<SelectItem> selectItems =((PlainSelect) selectBody).getSelectItems();
        StringBuilder addSql=new StringBuilder();
        List<ResultMapping> resultMappings= resultMaps.get(0).getResultMappings();
        try {
            if (null == param || !com.piesat.common.utils.StringUtils.isNotNullString(param)) {
                return "";
            }
            Map<String, Object> map = JSON.parseObject(param, Map.class);

           Map<String, String> mapList = (Map<String, String>) map.get("orderBy");
            if (null == mapList || mapList.isEmpty()) {
                return "";

            }
            mapList.forEach((k,v)-> {
                String name =k.trim();
                if(name.indexOf(".")!=-1){
                    name=name.substring(name.lastIndexOf(".")+1);
                }
                if(null != resultMappings && !resultMappings.isEmpty()){
                    for (ResultMapping resultMapping : resultMappings) {
                        if (name.toUpperCase().equals(resultMapping.getProperty().toUpperCase())) {
                            name = resultMapping.getColumn();
                            break;
                        }
                    }
                }
                if(null!=selectItems&&!selectItems.isEmpty()){
                    for(SelectItem selectItem:selectItems){
                        SelectExpressionItem selectExpressionItem= (SelectExpressionItem) selectItem;
                        String sitem=selectExpressionItem.getExpression().toString().toUpperCase();
                        sitem=sitem.replaceAll("_","");
                        String vname=name.toUpperCase().replaceAll("_","");
                        if(sitem.indexOf(vname)!=-1){
                            name=selectExpressionItem.getExpression().toString();
                            break;
                        }
                    }
                }
                addSql.append(name).append(" ").append(v).append(",");
            });
            String sql=addSql.toString();
            return sql.substring(0,sql.length()-1);
        } catch (Exception e) {
        }
        return "";

    }

}
*/
