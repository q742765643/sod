package com.piesat.dm.rpc.service.datatable;

import com.piesat.dm.core.enums.ColumnEnum;
import com.piesat.dm.core.parser.ColumnSet;
import com.piesat.dm.rpc.dto.datatable.*;
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

    public String getXuGuCreateSql(SqlTemplateDto sqlTemplate, DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<PartingDto> sharding, String schema) throws Exception {

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

            int length = ColumnEnum.getLength(type.toUpperCase());
            if (length == -1) {
                accuracy = "";
            } else if (StringUtils.isNotBlank(accuracy) && length == 0 && accuracy.contains(".")) {
                accuracy = accuracy.split("\\.")[0];
            }

            if (StringUtils.isNotBlank(accuracy) && !"0".equals(accuracy)) {
                accuracy = "(" + accuracy.replace(".", ",") + ")";
            } else {
                accuracy = "";
            }
            String isNull = (ds.getIsNull() == null || ds.getIsNull()) ? " " : " NOT NULL";
            column.append(ds.getDbEleCode()).append(" ").append(dataType.toUpperCase()).append(accuracy).append(isNull);
            if (StringUtils.isNotBlank(ds.getDefaultValue())) {
                column.append(" DEFAULT '").append(ds.getDefaultValue()).append("'");
            }
            column.append(" COMMENT '").append(ds.getEleName()).append("'");
            if (i < dataStructures.size() - 1) {
                column.append(",\n");
            }
            columns.append(column);
        }
        String dp_shard = "";

        for (PartingDto s : sharding) {
                dp_shard = s.getPartitions();
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

    public String getXuGuQuerySql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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

    public String getXuGuInsertSql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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


    public String getGbaseCreateSql(SqlTemplateDto sqlTemplate, DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<PartingDto> sharding, String schema) throws Exception {

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

            int length = ColumnEnum.getLength(type.toUpperCase());
            if (length == -1) {
                accuracy = "";
            } else if (length == 0 && accuracy.contains(".")) {
                accuracy = accuracy.split("\\.")[0];
            }

            if (StringUtils.isNotBlank(accuracy) && !"0".equals(accuracy)) {
                accuracy = "(" + accuracy.replace(".", ",") + ")";
            } else {
                accuracy = "";
            }
            String isNull = (ds.getIsNull() == null || ds.getIsNull()) ? " " : " NOT NULL";
            column.append(ds.getDbEleCode()).append(" ").append(dataType.toUpperCase()).append(accuracy).append(isNull);
            if (StringUtils.isNotBlank(ds.getDefaultValue())) {
                column.append(" DEFAULT '").append(ds.getDefaultValue()).append("'");
            }
            column.append(" COMMENT '").append(ds.getEleName()).append("'");
            if (i < dataStructures.size() - 1) {
                column.append(",\n");
            }
            columns.append(column);
        }
        String dp_shard = "";
        for (PartingDto s : sharding) {
                dp_shard = s.getPartitions();
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

    public String getGbaseQuerySql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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

    public String getGbaseInsertSql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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

    public String getCassandraCreateSql(SqlTemplateDto sqlTemplate, DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<PartingDto> sharding, String schema) throws Exception {
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

    public String getCassandraQuerySql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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

    public String getCassandraInsertSql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
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

    public String getPostgreSqlCreateSql(SqlTemplateDto sqlTemplate, DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, List<TableIndexDto> tableIndex, List<PartingDto> sharding, String schema) throws Exception {
        if (schema.equals(schema.toUpperCase())) {
            schema = "\"" + schema + "\"";
        }
        StringBuffer columns = new StringBuffer();
        List<String> eleCodes = new ArrayList<>();
        StringBuffer comments = new StringBuffer();
        for (int i = 0; i < dataStructures.size(); i++) {
            TableColumnDto ds = dataStructures.get(i);
            eleCodes.add(ds.getDbEleCode());
            StringBuffer column = new StringBuffer();
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getPostgresql().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("postgresql不支持字段：" + type);
            }
            String accuracy = ds.getAccuracy();
            if (StringUtils.isNotBlank(accuracy) && !"0".equals(accuracy) && type.toUpperCase().indexOf("INT") == -1) {
                accuracy = "(" + accuracy.replace(".", ",") + ")";
            } else {
                accuracy = "";
            }
            String isNull = ds.getIsNull() ? " NULL" : " NOT NULL";
            column.append(ds.getDbEleCode()).append(" ").append(dataType.toUpperCase()).append(accuracy).append(isNull);
            if (StringUtils.isNotBlank(ds.getDefaultValue())) {
                column.append(" DEFAULT '").append(ds.getDefaultValue()).append("'");
            }
            comments.append(" COMMENT ON COLUMN ").append(schema + "." + dataTable.getTableName() + "." + ds.getDbEleCode() + " IS '").append(ds.getEleName()).append("'").append(";\n");
            if (i < dataStructures.size() - 1) {
                column.append(",\n");
            }
            columns.append(column);
        }
        String dp_shard = "";
        for (PartingDto s : sharding) {
                dp_shard = s.getPartitions();
        }
        String template = sqlTemplate.getTemplate();
        template = template.replace("${tableName}", schema + "." + dataTable.getTableName());
        template = template.replace("${columnInfo}", columns);
        if (StringUtils.isNotBlank(dp_shard) && !eleCodes.contains(dp_shard)) {
            throw new Exception("分库分表键[" + dp_shard + "]不存在！");
        }
        if (StringUtils.isBlank(dp_shard)) {
            //template = template.replace(template.substring(template.indexOf("%^"), template.indexOf("^%") + 2), "");
        } else {
            //template = template.replace("${DPartitionKey}", dp_shard);
            //template = template.replace("%^", "").replace("^%", "");
        }
        template = template.replace("${comments}", comments.toString());

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
            template = template.replace("${tableDescription}", "COMMENT ON TABLE " + schema + "." + dataTable.getTableName() + " IS '" + dataTable.getNameCn() + "';\n");
        }
        return template;
    }

    public String getPostgreSqlQuerySql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        if (schema.equals(schema.toUpperCase())) {
            schema = "\"" + schema + "\"";
        }
        List<String> columns = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getPostgresql().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("Postgresql不支持字段：" + type);
            }
            columns.add(ds.getDbEleCode());
        }
        StringBuffer sql = new StringBuffer();
        String join = StringUtils.join(columns, ",");
        sql.append("SELECT ").append(join).append(" FROM ").append(schema + "." + dataTable.getTableName());
        return sql.toString();
    }

    public String getPostgreSqlInsertSql(DataTableInfoDto dataTable, List<TableColumnDto> dataStructures, String schema) throws Exception {
        if (schema.equals(schema.toUpperCase())) {
            schema = "\"" + schema + "\"";
        }
        List<String> columns = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        for (TableColumnDto ds : dataStructures) {
            String type = ds.getType().toLowerCase();
            String dataType = columnSet.getPostgresql().get(type);
            if (StringUtils.isBlank(dataType)) {
                throw new Exception("Postgresql不支持字段：" + type);
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
}
