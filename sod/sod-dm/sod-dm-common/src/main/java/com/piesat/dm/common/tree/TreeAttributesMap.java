package com.piesat.dm.common.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * 树型节点的属性Map（用于操作节点的各个属性，如name,id）
 *
 * @author cwh
 * @date 2020年 01月06日 15:33:51
 */
public class TreeAttributesMap {
    private Map<String,Object> attributes = new HashMap<>();     //存放属性的Map

    /**
     * 有参构造函数
     * @param id
     * @param name
     */
    public TreeAttributesMap(Object id,Object name,Object type){
        attributes.put(TreeNode.TREENODE_ATTRIBUTE_NAME_ID,id);
        attributes.put(TreeNode.TREENODE_ATTRIBUTE_NAME_NAME,name);
        attributes.put(TreeNode.TREENODE_ATTRIBUTE_TYPE_ID,type);
    }

    /**
     * 获取属性的Map
     * @return
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 往属性的Map中添加属性
     * @param name
     * @param value
     */
    public void putAttributes(String name,Object value){
        attributes.put(name,value);
    }
}
