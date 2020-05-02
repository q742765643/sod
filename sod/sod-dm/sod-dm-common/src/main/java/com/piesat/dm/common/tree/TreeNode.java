package com.piesat.dm.common.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点类（用于模拟树型结构关系中的节点）
 *
 * @author cwh
 * @date 2020年 01月06日 15:32:18
 */
public class TreeNode {
    public static final String TREENODE_ATTRIBUTE_NAME_NAME = "name";       // 字段属性name
    public static final String TREENODE_ATTRIBUTE_NAME_ID = "id";           // 字段属性id
    public static final String TREENODE_ATTRIBUTE_TYPE_ID = "type";           // 字段属性id

    private Map<String,Object> attributes = new HashMap<String, Object>();  // 字段属性的Map

    private TreeNode parent = null;                                         //当前节点的父节点
    private List<TreeNode> child = new ArrayList<TreeNode>();               //当前节点的子节点
    private boolean isCheck = false;                                        //当前节点是否被勾选标志


    /*
        无参构造函数
     */
    public  TreeNode(){
        super();
    }

    /**
     * 获取/设置 节点的属性name
     * @return
     */
    public String getName() {
        return (String) attributes.get(TREENODE_ATTRIBUTE_NAME_NAME);
    }
    public void setName(String name) {
        attributes.put(TREENODE_ATTRIBUTE_NAME_NAME,name);
    }

    /**
     * 获取/设置 节点的属性id
     * @return
     */
    public String getId() {
        return (String) attributes.get(TREENODE_ATTRIBUTE_NAME_ID);
    }
    public void setId(String id) {
        attributes.put(TREENODE_ATTRIBUTE_NAME_ID,id);
    }


    public String getType() {
        return (String) attributes.get(TREENODE_ATTRIBUTE_TYPE_ID);
    }
    public void setType(String type) {
        attributes.put(TREENODE_ATTRIBUTE_TYPE_ID,type);
    }

    /**
     * 获取/设置 节点需要在树形结构中显示的信息
     * @return
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * 获取/设置 节点是否被勾选信息
     * @return
     */
    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isShow(){return this.getType().equals("1");}

    /**
     * 获取/设置 节点父节点信息
     * @return
     */
    public TreeNode getParent() {
        return parent;
    }
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * 是否存在/获取/设置/添加 节点子节点信息
     * @return
     */
    public boolean hasChildren(){
        return child.size()>0;
    }
    public List<TreeNode> getChild() {
        return child;
    }
    public void setChild(List<TreeNode> child) {
        this.child = child;
    }
    public void addChild(TreeNode treeNode){
        treeNode.setParent(this);
        child.add(treeNode);
    }
}
