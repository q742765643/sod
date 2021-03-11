package com.piesat.dm.core.action.exc.abs;

import com.piesat.dm.core.action.exc.Exc;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.CountEnum;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ColumnVo;
import com.piesat.dm.core.model.SelectVo;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.dm.core.parser.ManagerUser;
import com.piesat.dm.core.template.SqlTemplateComm;
import com.piesat.util.ResultT;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 17:05:07
 */
public abstract class ExcAbs implements Exc {

    public String[] sysUsers = ManagerUser.sysUser.split(Constants.COMMA);

    public SqlTemplateComm T = new SqlTemplateComm();

    public BaseAbs ba;

}
