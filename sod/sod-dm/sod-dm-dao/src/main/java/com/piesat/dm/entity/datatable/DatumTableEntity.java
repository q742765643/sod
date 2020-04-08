package com.piesat.dm.entity.datatable;

import lombok.Data;

/**
 * @author cwh
 * @date 2020年 04月03日 09:36:56
 */
@Data
public class DatumTableEntity {
    private	String	c_datum_code;			//资料（类别）编码/id/seq
    private	String	c_datumtype;			//资料类型*/
    private	String	c_datum_level;			//资料所属层级*/grade
    private	String	c_flow_id;				//流程ID
    private	String	c_datumparent_code;		//上级资料编码*/belongto
    private	String	c_coremetar_name;		//核心元数据名称/
    private	String	c_sys_code;				//所属系统*/
    private	String	c_flow;					//业务主流程*/
    private	String	c_sys_name;				//所属系统名称/
    private	String	c_opt_type;				//业务类型/操作类型add、update、abolish
    private	String	c_status;				//状态信息/
    private	String	c_coremeta_code;		//核心元数据代码
    private	String	c_msg_header;			//报告类别
    private	String	c_datatype;				//数据类型*/1报告类资料，2要素类资料，3是其他
}
