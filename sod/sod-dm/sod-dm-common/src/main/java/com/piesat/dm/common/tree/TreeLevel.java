package com.piesat.dm.common.tree;

/**
 * @author cwh
 * @date 2020年 01月06日 15:41:18
 */
public class TreeLevel implements ILevelTreeNode {

    private String id;
    private String parentId;
    private String name;
    private String type;
    private String icon;
    private String have = "1";


    public String getHave() {
        return have;
    }

    public void setHave(String have) {
        this.have = have;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public TreeAttributesMap getTreeAttributesMap() {
        TreeAttributesMap treeMap = new TreeAttributesMap(this.id, this.name, this.type);
        treeMap.putAttributes("parentId", this.parentId);
        treeMap.putAttributes("icon", this.icon);
        return treeMap;
    }
}
