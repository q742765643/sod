package com.piesat.dm.common.tree;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 树类 （根据自身需求对数据进行解析和转换）
 *
 * @author cwh
 * @date 2020年 01月06日 15:37:58
 */
public class Tree {
    private ITreeParser<TreeNode> treeParser;
    private int treeType;                        //转换标志（可以拓展）
    private Object data;                         //数据源

    /**
     * 有参构造函数
     *
     * @param data
     * @param treeType
     */
    public Tree(Object data, int treeType) {
        this.data = data;
        this.treeType = treeType;
    }

    /**
     * 数据转换 data ---> json格式
     *
     * @return json格式的String
     */
    public String toJsonTree() {
        String json = "";
        if (treeType == 1) {
            treeParser = new LevelTreeParser();
            json = toLevelTree();
        } else if (treeType == 2) {
            //XXX树型结构
        } else if (treeType == 3) {
            //XXX树型结构
        } else {
            //other树型结构
        }
        return json;
    }

    private String toLevelTree() {
        String jsonString = "";
        List<TreeNode> nodes = treeParser.parser(data);
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        parserLevelNodeToJson(sb, nodes);
        sb.append("]");
        jsonString = sb.toString();
        return jsonString;
    }

    private void parserLevelNodeToJson(StringBuffer sb, List<TreeNode> nodes) {
        Iterator<TreeNode> it = nodes.iterator();
        while (it.hasNext()) {
            TreeNode node = it.next();
            boolean isLeaf = node.hasChildren();
            sb.append("{");
            Map<String, Object> attributes = node.getAttributes();
            for (String key : attributes.keySet()) {
                Object obj = attributes.get(key);
                if (obj != null )
                    sb.append(key + ":'" + obj.toString() + "',");
            }
            if (!node.isShow()) {
                sb.append("disabled: true,");
            } else {
                sb.append("disabled: false,");
            }

            if (isLeaf) {
                sb.append("children: [");
                parserLevelNodeToJson(sb, node.getChild());
                if (it.hasNext()) {
                    sb.append("]},");
                } else {
                    sb.append("]}");
                }
            } else {
                if (it.hasNext()) {
                    sb.append("},");
                } else {
                    sb.append("}");
                }
            }
        }
    }
}
