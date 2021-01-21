package com.piesat.dm.core.factory;

import com.datastax.driver.core.Session;
import com.piesat.common.constant.Constants;
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
    private Object con = null;
    private Actuator actuator;
    private Actuator commData;

    public AuzFactory(String pid, ConnectVo connectVo, DatabaseTypesEnum databaseType, ResultT resultT) {
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            Session connection = null;
            try {
                connection = CassandraSource.getConnection(pid, connectVo);
                con = connection;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                if (connection != null) {
                    connection.close();
                }
                resultT.setErrorMessage(String.format(ConstantsMsg.MSG8, pid));
            }
        } else {
            Connection connection = null;
            try {
                connection = CommDataSource.getConnection(pid, connectVo);
                con = connection;
            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                resultT.setErrorMessage(String.format(ConstantsMsg.MSG8, pid));
            }
        }
        if (con == null) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG11, pid));
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
            resultT.setErrorMessage(ConstantsMsg.MSG12);
            resultT.setData(OwnException.get(e));
        }
    }

    public static void insertTableSql(TableVo tableVo, ResultT<String> resultT) {
        try {
            String template = SqlTemplate.INSERT_TABLE_DISTRIBUTED;
            String sql = TemplateUtil.rendering(template, tableVo);
            resultT.setData(sql);
        } catch (Exception e) {
            resultT.setErrorMessage(ConstantsMsg.MSG12);
            resultT.setData(OwnException.get(e));
        }
    }

    public static void queryTableSql(TableVo tableVo, ResultT<String> resultT) {
        try {
            String template = SqlTemplate.QUERY_TABLE_DISTRIBUTED;
            String sql = TemplateUtil.rendering(template, tableVo);
            resultT.setData(sql);
        } catch (Exception e) {
            resultT.setErrorMessage(ConstantsMsg.MSG12);
            resultT.setData(OwnException.get(e));
        }
    }
}
