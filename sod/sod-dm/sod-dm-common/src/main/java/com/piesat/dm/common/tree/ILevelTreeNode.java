package com.piesat.dm.common.tree;

/**
 * 定义层级树节点的接口
 *
 * @author cwh
 * @date 2020年 01月06日 15:35:28
 */
public interface ILevelTreeNode extends ISimpleTreeNode{

    public String getParentId();
    public String getId();
}