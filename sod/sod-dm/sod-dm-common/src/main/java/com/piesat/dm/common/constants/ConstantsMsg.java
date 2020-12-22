package com.piesat.dm.common.constants;

/**
 * @author cwh
 * @date 2020年 12月14日 12:00:29
 */
public class ConstantsMsg {

    public static final String MSG1 = "【%s】为数据库内部用户或模式，禁止操作！";
    public static final String MSG2 = "数据库账户不存在或账户未被授权！";
    public static final String MSG2_1 = "数据库【%s】管理账户缺失，或存管数据库未配置！";
    public static final String MSG3 = "数据库【%s】未对本账户授权！";

    public static final String MSG4 = "新增数据库【%s】-用户【%s】-%s;\n";
    public static final String MSG5 = "删除数据库【%s】-用户【%s】-%s;\n";
    public static final String MSG6 = "修改数据库【%s】-用户【%s】密码-%s;\n";
    public static final String MSG7 = "修改数据库【%s】-用户【%s】绑定IP-%s;\n";

    public static final String MSG8 = "获取数据库【%s】连接出错，ERROR:[%s];\n";

    public static final String SUCCESS = "【成功】";
    public static final String FAIL = "【失败】[ERROR:%s]";


    public static final String OPE_CREATE = "新增用户";
    public static final String OPE_DROP = "删除用户";
    public static final String OPE_ALERT_PWD = "修改密码";
    public static final String OPE_ALERT_WHITELIST = "修改白名单";

}
