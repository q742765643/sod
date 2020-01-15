package com.piesat.dm.common.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 01月06日 15:39:36
 */
public class LevelTreeParser implements ITreeParser<TreeNode>{

    private Map<Object,TreeNode> treeNodeMap = new HashMap<Object, TreeNode>();
    TreeNode root = new TreeNode();

    public List<TreeNode> parser(Object data){
        //将数据data强转为List<ILevelTreeNode>格式
        List<ILevelTreeNode> list = (List<ILevelTreeNode>) data;
        //List<TreeNode> nodes = new ArrayList<TreeNode>();

        //list不为空且大小不为0
        if (list != null && list.size()>0){

            //加强for循环list
            for (ILevelTreeNode levelTreeNode:list){
                TreeNode item = new TreeNode();
                //获取每个树节点的属性
                item.setAttributes(levelTreeNode.getTreeAttributesMap().getAttributes());
                //封装每个树节点
                treeNodeMap.put(levelTreeNode.getId(),item);
            }
            //再次遍历所有的树节点
            for (ILevelTreeNode levelTreeNode:list){
                TreeNode parent = treeNodeMap.get(levelTreeNode.getParentId());
                TreeNode treeNode = treeNodeMap.get(levelTreeNode.getId());
                if (parent == null){
                    root.addChild(treeNode);
                }else{
                    parent.addChild(treeNode);
                }
            }
        }
        return root.getChild();
    }
}