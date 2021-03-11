package com.piesat.dm.core.action.build;

import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.action.Action;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.CountEnum;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.model.SelectVo;
import com.piesat.dm.core.model.TableVo;
import com.piesat.dm.core.template.SqlTemplate;
import com.piesat.util.ResultT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 02月02日 13:30:43
 */
public class DataBuild implements Action {
    private BaseAbs ba;
    private DatabaseTypesEnum databaseType;

    @Override
    public DataBuild init(ConnectVo c, ResultT r) {
        this.databaseType = c.getDatabaseType();
        this.ba = databaseType.getConn(c, r);
        return this;
    }

    @Override
    public void close() {
        this.ba.close();
    }

    public DataBuild countData(SelectVo selectVo, Boolean onlyAll, ResultT<Map<CountEnum, String>> resultT) {
        String today = DateUtils.getToday();
        String yesterday = DateUtils.getYesterday();
        selectVo.setBeginTime(yesterday);
        selectVo.setTimeColumn(Constants.D_DATETIME);
        selectVo.setEndTime(today);
        selectVo.setColumnStr(Constants.COUNT + Constants.RT);
        Map<CountEnum, String> m = new HashMap<>();
        resultT.setData(m);
        String sql = TemplateUtil.rendering(SqlTemplate.QUERY, selectVo);
        Map<String, Object> all = this.ba.exeQueryOne(sql, resultT);
        String all_ct = all == null ? Constants.ZERO : all.get(Constants.RT).toString();
        m.put(CountEnum.ALL_COUNT, all_ct);
        if (onlyAll){
            return this;
        }
        sql = TemplateUtil.rendering(SqlTemplate.QUERY_BEGIN_END, selectVo);
        Map<String, Object> map = this.ba.exeQueryOne(sql, resultT);
        String ct = map == null ? Constants.ZERO : map.get(Constants.RT).toString();
        m.put(CountEnum.WHERE_COUNT, ct);
        this.getPartiInfo(selectVo, resultT, true);
        String min = String.format(Constants.MIN, Constants.D_DATETIME);
        selectVo.setColumnStr(min + Constants.RT);
        sql = TemplateUtil.rendering(SqlTemplate.QUERY, selectVo);
        map = this.ba.exeQueryOne(sql, resultT);
        String min_time = map == null ? null : map.get(Constants.RT).toString();
        m.put(CountEnum.MIN_TIME, min_time);
        this.getPartiInfo(selectVo, resultT, false);
        String max = String.format(Constants.MAX, Constants.D_DATETIME);
        selectVo.setColumnStr(max + Constants.RT);
        sql = TemplateUtil.rendering(SqlTemplate.QUERY, selectVo);
        map = this.ba.exeQueryOne(sql, resultT);
        String max_time = map == null ? null : map.get(Constants.RT).toString();
        m.put(CountEnum.MAX_TIME, max_time);
        return this;
    }


    public void getPartiInfo(SelectVo selectVo, ResultT resultT, Boolean isAsc) {
        selectVo.setWhere(null);
        if (this.databaseType.equals(DatabaseTypesEnum.XUGU)) {
            String query_parti = TemplateUtil.rendering(SqlTemplate.QUERY_PARTI, selectVo);
            if (!isAsc) {
                query_parti += Constants.DESC;
            }
            List<Map<String, Object>> parti = this.ba.exeQuery(query_parti, resultT);
            if (parti == null) {
                selectVo.setTimeColumn(null);
            } else {
                String where = Constants.D_DATETIME +
                        (isAsc ? Constants.LE : Constants.GT) +
                        (parti.size() > 1 ? parti.get(1).get(Constants.RT) : parti.get(0).get(Constants.RT));
                selectVo.setWhere(where);
            }
        } else {
            selectVo.setTimeColumn(null);
        }
    }

    public DataBuild sampleData(SelectVo selectVo, ResultT<List<Map<String, Object>>> resultT) {
        String sql = TemplateUtil.rendering(SqlTemplate.SAMPLE_DATA, selectVo);
        List<Map<String, Object>> maps = this.ba.exeQuery(sql, resultT);
        resultT.setData(maps);
        return this;
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
