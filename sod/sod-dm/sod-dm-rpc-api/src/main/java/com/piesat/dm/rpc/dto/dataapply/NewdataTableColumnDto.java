package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.dm.core.enums.ColumnEnum;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.util.BaseDto;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/18 21:01
 */
@Data
public class NewdataTableColumnDto extends BaseDto {
    /**
     * 申请记录id
     */
    private String applyId;

    /**
     * 字段名称
     * c_element_code
     */
    private String cElementCode;

    /**
     * 中文名称
     * ele_name
     */
    private String eleName;

    /**
     * 字段类型
     * type
     */
    private String type;

    /**
     * 字段精度
     * accuracy
     */
    private String accuracy;

    /**
     * 英文单位
     * unit
     */
    private String unit;

    /**
     * 是否可为空
     * is_null
     */
    private Boolean isNull;

    /**
     * 是否主键
     * is_primary_key
     */
    private Boolean isPrimaryKey;

    /**
     * 序号
     * serial_number
     */
    private Integer serialNumber;

    public TableColumnDto toTableColumn() {
        TableColumnDto tc = new TableColumnDto();
        tc.setCElementCode(this.cElementCode);
        tc.setDbEleCode(this.cElementCode);
        tc.setEleName(this.eleName);
        tc.setType(this.type);
        String t = this.type.toUpperCase();
        int length = ColumnEnum.getLength(t);
        if (length == -1) {
            tc.setAccuracy("");
        } else if (length == 0 && this.accuracy.contains(".")) {
            tc.setAccuracy(this.accuracy.split("\\.")[0]);
        } else {
            tc.setAccuracy(this.accuracy);
        }
        tc.setUnit(this.unit);
        tc.setIsNull(this.isNull);
        tc.setIsPrimaryKey(this.isPrimaryKey);
        tc.setSerialNumber(this.serialNumber);
        return tc;
    }
}
