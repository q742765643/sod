package com.piesat.dm.rpc.service.datatable;

import com.piesat.dm.core.parser.ColumnSet;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.ShardingDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.datatable.TableIndexDto;
import com.piesat.sod.system.rpc.dto.SqlTemplateDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * sql生成
 *
 * @author cwh
 * @date 2020年 02月05日 20:49:19
 */
@Service
public class DatabaseSqlService {
    @Autowired
    private ColumnSet columnSet;
    private final String[] indexTypes = {"BTREE", "INDEX", "RTREE", "IDX", "TREE"};
    private final String[] uniqueType = {"UNIQUE", "UK", "PK", "唯一索引"};

    public String getXuGuCreateSql(SqlTemplateDto sqlTemplate, DataTableDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<ShardingDto> sharding, String schema) throws Exception {

        StringBuffer columns = new StringBuffer();
        List<String> eleCodes = new ArrayList<>();
        for (int i = 0; i < dataStructures.size(); i++) {
            TableColumnDto ds = dataStructures.get(i);
            eleCodes.add(ds.getDbEleCode());
            StringBuffer column = new StringBuffer();
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getXugu().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("虚谷不支持字段：" + type);
            }
            String accuracy = ds.getAccuracy();
            if (StringUtils.isNotBlank(accuracy)) {
                accuracy = "(" + accuracy.replace(".", ",") + ")";
            } else {
                accuracy = "";
            }
            String isNull = ds.getIsNull() ? " NULL" : " NOT NULL";
            column.append(ds.getDbEleCode()).append(" ").append(dataType.toUpperCase()).append(accuracy).append(isNull);
            if (StringUtils.isNotBlank(ds.getDefaultValue())) {
                column.append(" DEFAULT '").append(ds.getDefaultValue()).append("'");
            }
            column.append(" COMMENT '").append(ds.getEleName()).append("'");
            if (i < dataStructures.size() - 1) column.append(",\n");
            columns.append(column);
        }
        String dp_shard = "";
        for (ShardingDto s : sharding) {
            if ("D".equals(s.getShardingType())) {
                dp_shard = s.getColumnName();
            }
        }
        String template = sqlTemplate.getTemplate();
        template = template.replace("${tableName}", schema + "." + dataTable.getTableName());
        template = template.replace("${columnInfo}", columns);
        if (StringUtils.isNotBlank(dp_shard) && !eleCodes.contains(dp_shard)) {
            throw new Exception("分库分表键[" + dp_shard + "]不存在！");
        }
        if (StringUtils.isBlank(dp_shard)) {
            template = template.replace(template.substring(template.indexOf("%^"), template.indexOf("^%") + 2), "");
        } else {
            template = template.replace("${DPartitionKey}", dp_shard);
            template = template.replace("%^", "").replace("^%", "");
        }

        // 拼接索引 根据类型判断是哪种索引
        StringBuffer indexColumn = new StringBuffer();
        String Index_type = "INDEX";// 索引名
        for (int i = 0; i < tableIndex.size(); i++) {
            TableIndexDto el = tableIndex.get(i);
            if (Arrays.asList(indexTypes).contains(el.getIndexType())) {
                Index_type = "INDEX";
            } else if (Arrays.asList(uniqueType).contains(el.getIndexType())) {
                Index_type = "UNIQUE INDEX";
            } else {
                Index_type = "INDEX";
            }
            indexColumn.append("\nCREATE " + Index_type + "");
            indexColumn.append(" " + el.getIndexName());
            indexColumn.append(" ON " + schema + "." + dataTable.getTableName());
            indexColumn.append(" (");
            String index_column = el.getIndexColumn().toUpperCase();
//            if (index_column.contains("D_DATETIME")&&!index_column.startsWith("D_DATETIME")){
//                index_column = index_column.replace(",D_DATETIME","");
//                index_column = "D_DATETIME,"+index_column;
//            }
            String[] split = index_column.split(",");
            List<String> stringList = Arrays.asList(split);
            List arrList = new ArrayList(stringList);
            if (arrList.contains("D_DATETIME")) {
                arrList.remove("D_DATETIME");
                arrList.add(0, "D_DATETIME");
            }
            String join = String.join(",", arrList);
            indexColumn.append(join);
            indexColumn.append(");");
        }
        template = template.replace("${indexColumn}", indexColumn);
        if (!StringUtils.isEmpty(dataTable.getNameCn())) {
            template = template.replace("${tableDescription}", dataTable.getNameCn());
        }
        return template;
    }

    public String getXuGuQuerySql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getXugu().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("虚谷不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
        }
        StringBuffer sql = new StringBuffer();
        String join = StringUtils.join(columns, ",");
        sql.append("SELECT ").append(join).append(" FROM ").append(schema + "." + dataTable.getTableName());
        return sql.toString();
    }

    public String getXuGuInsertSql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getXugu().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("虚谷不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
            flags.add("?");
        }
        String join = StringUtils.join(columns, ",");
        String flag = StringUtils.join(flags, ",");
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(schema + "." + dataTable.getTableName()).append(" (").append(join).append(") VALUES (").append(flag).append(")");
        return sql.toString();
    }


    public String getGbaseCreateSql(SqlTemplateDto sqlTemplate, DataTableDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<ShardingDto> sharding, String schema) throws Exception {

        StringBuffer columns = new StringBuffer();
        List<String> eleCodes = new ArrayList<>();
        for (int i = 0; i < dataStructures.size(); i++) {
            TableColumnDto ds = dataStructures.get(i);
            eleCodes.add(ds.getDbEleCode());
            StringBuffer column = new StringBuffer();
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getGbase8a().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("gbase8a不支持字段：" + type);
            }
            String accuracy = ds.getAccuracy();
            if (StringUtils.isNotBlank(accuracy)) {
                accuracy = "(" + accuracy.replace(".", ",") + ")";
            } else {
                accuracy = "";
            }
            String isNull = ds.getIsNull() ? " NULL" : " NOT NULL";
            column.append(ds.getDbEleCode()).append(" ").append(dataType.toUpperCase()).append(accuracy).append(isNull);
            if (StringUtils.isNotBlank(ds.getDefaultValue())) {
                column.append(" DEFAULT '").append(ds.getDefaultValue()).append("'");
            }
            column.append(" COMMENT '").append(ds.getEleName()).append("'");
            if (i < dataStructures.size() - 1) column.append(",\n");
            columns.append(column);
        }
        String dp_shard = "";
        for (ShardingDto s : sharding) {
            if ("T".equals(s.getShardingType())) {
                dp_shard = s.getColumnName();
            }
        }
        String template = sqlTemplate.getTemplate();
        template = template.replace("${tableName}", schema + "." + dataTable.getTableName());
        template = template.replace("${columnInfo}", columns);
        if (StringUtils.isNotBlank(dp_shard) && !eleCodes.contains(dp_shard)) {
            throw new Exception("分库分表键[" + dp_shard + "]不存在！");
        }
        if (StringUtils.isBlank(dp_shard)) {
            template = template.replace(template.substring(template.indexOf("%^"), template.indexOf("^%") + 2), "");
        } else {
            template = template.replace("${DPartitionKey}", dp_shard);
            template = template.replace("%^", "").replace("^%", "");
        }
        if (!StringUtils.isEmpty(dataTable.getNameCn())) {
            template = template.replace("${tableDescription}", dataTable.getNameCn());
        }
        return template;
    }

    public String getGbaseQuerySql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getGbase8a().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("gbase8a不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
        }
        StringBuffer sql = new StringBuffer();
        String join = StringUtils.join(columns, ",");
        sql.append("SELECT ").append(join).append(" FROM ").append(schema + "." + dataTable.getTableName());
        return sql.toString();
    }

    public String getGbaseInsertSql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getGbase8a().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("gbase8a不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
            flags.add("?");
        }
        String join = StringUtils.join(columns, ",");
        String flag = StringUtils.join(flags, ",");
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(schema + "." + dataTable.getTableName()).append(" (").append(join).append(") VALUES (").append(flag).append(")");
        return sql.toString();
    }

    public String getCassandraCreateSql(SqlTemplateDto sqlTemplate, DataTableDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<ShardingDto> sharding, String schema) throws Exception {
        List<String> key_columns = new ArrayList<>();
        StringBuffer columns = new StringBuffer();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            Boolean is_kv_k = ds.getIsKvK();
            String dataType = columnSet.getCassandra().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("Cassandra不支持字段：" + type);
            }
            String column = ds.getDbEleCode() + " " + dataType + ",\n";
            columns.append(column);
            if (is_kv_k != null && is_kv_k) {
                key_columns.add(ds.getDbEleCode());
            }
        }
        String keys = StringUtils.join(key_columns, ",");
        String template = sqlTemplate.getTemplate();
        template = template.replace("${tableName}", dataTable.getTableName());
        template = template.replace("${columnInfo}", columns);
        template = template.replace("${primaryColumn}", keys);
        return template;
    }

    public String getCassandraQuerySql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getCassandra().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("Cassandra不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
        }
        StringBuffer sql = new StringBuffer();
        String join = StringUtils.join(columns, ",");
        sql.append("SELECT ").append(join).append(" FROM ").append(dataTable.getTableName());
        return sql.toString();
    }

    public String getCassandraInsertSql(DataTableDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        List<String> columns = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getCassandra().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("Cassandra不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
            flags.add("?");
        }
        String join = StringUtils.join(columns, ",");
        String flag = StringUtils.join(flags, ",");
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(dataTable.getTableName()).append(" (").append(join).append(") VALUES (").append(flag).append(")");
        return sql.toString();
    }
}
