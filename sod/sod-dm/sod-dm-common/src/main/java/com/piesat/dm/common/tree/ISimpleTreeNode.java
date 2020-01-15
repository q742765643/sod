package com.piesat.dm.common.tree;

/**
 * 定义一般普通的树节点的接口
 *
 * @author cwh
 * @date 2020年 01月06日 15:34:41
 */
public interface ISimpleTreeNode {
    /**
     * 每个树节点都具备自身的一些属性
     * @return
     */
    public TreeAttributesMap getTreeAttributesMap();
}
