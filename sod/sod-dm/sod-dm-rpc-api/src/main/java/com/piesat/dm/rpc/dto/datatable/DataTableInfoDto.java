package com.piesat.dm.rpc.dto.datatable;

import com.alibaba.fastjson.JSON;
import com.piesat.common.MapUtil;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.entity.datatable.TableColumnEntity;
import com.piesat.dm.entity.datatable.TableIndexEntity;
import com.piesat.util.BaseDto;
import lombok.Data;
import org.apache.commons.collections.MapUtils;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cwh
 * @date 2020年 12月01日 15:19:19
 */
@Data
public class DataTableInfoDto extends BaseDto {

    private final String COLUMN_NAME = "column_name";
    private final String COLUMN_TYPE = "column_type";
    private final String IS_NULLABLE = "is_nullable";
    private final String COLUMN_COMMENT = "column_comment";
    private final String NON_UNQIUE = "non_unqiue";
    private final String INDEX_NAME = "index_name";
    private final String INDEX_COLUMN = "index_column";
    private final String UK = "UK";
    private final String IDX = "IDX";


    /**
     * 数据库id
     */
    private String databaseId;

    /**
     * 表存储类型
     */
    private String storageType;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表子类型（键表、要素表）
     */
    private String tableType;

    /**
     * 表描述
     */
    private String tableDesc;

    /**
     * 中文名
     */
    private String nameCn;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 申请人
     */
    private String userId;


    private LinkedHashSet<TableColumnDto> columns;

    private LinkedHashSet<TableIndexDto> tableIndexList;

    private List<TableColumnDto> realColumns;

    private List<TableIndexDto> realTableIndex;

    public void contrast(List<Map<String, Object>> columnMap, List<Map<String, Object>> indexMap, DatabaseTypesEnum databaseType){
        this.realColumns = columnMap == null ? null : MapUtil.transformMapList(columnMap).stream().map(e->{
            TableColumnDto tc = new TableColumnDto();
            tc.setEleName(String.valueOf(e.get(COLUMN_NAME)));
            //NUMERIC(10,4)
            String type = String.valueOf(e.get(COLUMN_TYPE));
            if (type.contains(Constants.LEFT_BRACKET)){
                String c_type = type.substring(0, type.indexOf(Constants.LEFT_BRACKET));
                String la = type.substring(type.indexOf(Constants.LEFT_BRACKET)+1,type.length()-1);
                if (la.contains(Constants.COMMA)){
                    String length = la.substring(0, la.indexOf(Constants.COMMA));
                    String accuracy = la.substring(la.indexOf(Constants.COMMA) + 1);
                    tc.setLength(Integer.valueOf(length));
                    tc.setAccuracy(accuracy);
                }else {
                    tc.setLength(Integer.valueOf(la));
                }
                tc.setType(c_type);
            }else {
                tc.setType(type);
            }
            return tc;
        }).collect(Collectors.toList());

        this.realTableIndex = indexMap == null ? null : MapUtil.transformMapList(indexMap).stream().map(e->{
            TableIndexDto ti = new TableIndexDto();
            if (e.get(NON_UNQIUE).equals(0)){
                ti.setIndexType(IDX);
            }else {
                ti.setIndexType(UK);
            }
            ti.setIndexName(String.valueOf(e.get(INDEX_NAME)));
            ti.setIndexColumn(String.valueOf(e.get(INDEX_COLUMN)));
            return ti;
        }).collect(Collectors.toList());
    }

}
