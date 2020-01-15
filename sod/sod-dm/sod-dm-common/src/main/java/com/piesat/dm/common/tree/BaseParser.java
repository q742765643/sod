package com.piesat.dm.common.tree;

import java.util.List;

/**
 * 解析格式选择类 （用于接受数据源并选择解析格式）
 *
 * @author cwh
 * @date 2020年 01月06日 15:40:10
 */
public class BaseParser {
    public static String parserListToLevelTree(List list){
        Tree tree = new Tree(list,1);
        return  tree.toJsonTree();
    }
}