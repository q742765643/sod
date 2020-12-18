package com.piesat.dm.core.factory;

import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.datasource.CassandraSource;
import com.piesat.dm.core.datasource.CommDataSource;
import com.piesat.dm.core.enums.DagTypeEnum;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.model.TableVo;
import com.piesat.dm.core.template.SqlTemplate;
import com.piesat.util.ResultT;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author cwh
 * @date 2020年 12月07日 14:20:24
 */
public class AuzFactory {
    private DagTypeEnum dagType;
    private Object con;
    private Actuator actuator;
    private Actuator commData;

    public AuzFactory(String pid, ConnectVo connectVo, DatabaseTypesEnum databaseType, ResultT resultT) {
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            try {
                con = CassandraSource.getConnection(pid, connectVo);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resultT.setErrorMessage(String.format(ConstantsMsg.MSG8,pid,OwnException.get(throwables)));
            }
        } else {
            try {
                con = CommDataSource.getConnection(pid, connectVo);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                resultT.setErrorMessage(String.format(ConstantsMsg.MSG8,pid,OwnException.get(throwables)));
            }
        }
        actuator = new AuzDatabase(databaseType, con);
        commData = new CommData(databaseType, con);
    }

    public Actuator getActuator(Boolean auz) {
        if (auz) {
            return actuator;
        } else {
            return commData;
        }
    }

    public static void createTableSql(TableVo tableVo, ResultT<String> resultT) {
        try {
            String template = StringUtils.isNotEmpty(tableVo.getTemplate()) ? tableVo.getTemplate() : SqlTemplate.CREATE_TABLE_DISTRIBUTED;
            String sql = TemplateUtil.rendering(template, tableVo);
            resultT.setData(sql);
        } catch (Exception e) {
            resultT.setErrorMessage("生成sql出错！");
            resultT.setData(OwnException.get(e));
        }
    }

    public static void insertTableSql(TableVo tableVo, ResultT<String> resultT) {
        try {
            String template = StringUtils.isNotEmpty(tableVo.getTemplate()) ? tableVo.getTemplate() : SqlTemplate.INSERT_TABLE_DISTRIBUTED;
            String sql = TemplateUtil.rendering(template, tableVo);
            resultT.setData(sql);
        } catch (Exception e) {
            resultT.setErrorMessage("生成sql出错！");
            resultT.setData(OwnException.get(e));
        }
    }

    public static void queryTableSql(TableVo tableVo, ResultT<String> resultT) {
        try {
            String template = StringUtils.isNotEmpty(tableVo.getTemplate()) ? tableVo.getTemplate() : SqlTemplate.QUERY_TABLE_DISTRIBUTED;
            String sql = TemplateUtil.rendering(template, tableVo);
            resultT.setData(sql);
        } catch (Exception e) {
            resultT.setErrorMessage("生成sql出错！");
            resultT.setData(OwnException.get(e));
        }
    }
}
