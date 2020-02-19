<template>
  <el-aside class="asideTreeStructure">
    <el-tabs :tab-position="tabPosition" @tab-click="handleTabClick">
      <el-tab-pane label="资料分类树"></el-tab-pane>
      <el-tab-pane label="数据用途分类树"></el-tab-pane>
      <el-tab-pane label="数据库分类树"></el-tab-pane>
      <el-tab-pane label="公共元数据结构树"></el-tab-pane>
    </el-tabs>
    <div class="classifyTree">
      <!-- 资料分类树操作 -->
      <el-button-group class="sourceTreeTopOp" v-show="sourceTreeOp">
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="showSourceDialog()"></el-button>
        <el-button type="primary" size="mini" icon="el-icon-edit" @click="showSourceDialog('edit')"></el-button>
        <el-button type="primary" size="mini" icon="el-icon-delete" @click="deleteSourceNode"></el-button>
        <el-button
          class="sourceBtn"
          type="primary"
          size="mini"
          icon="el-icon-search"
          @click="sourceTopSearch"
        ></el-button>
      </el-button-group>
      <!-- 资料分类树/数据库用途分类树/数据库分类树/公共元数据库结构树 查询 -->
      <div class="usedTreeOp">
        <el-input
          placeholder="请输入"
          size="small"
          v-model="sourceTreeText"
          v-show="publicTreeTextActive"
        ></el-input>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-back"
          v-show="backBtn"
          @click="backTop"
        ></el-button>
      </div>

      <!-- 公共元数据结构 -->
      <div class="publicTreeOp" v-show="publicActive">
        <el-button-group>
          <el-button :type="showAll" size="small" @click="handleShowTree('all')">显示全部</el-button>
          <el-button :type="showNoCreat" size="small" @click="handleShowTree('noCreat')">显示未创建</el-button>
          <el-button type="primary" icon="el-icon-search" size="small" @click="sourceTopSearch"></el-button>
        </el-button-group>
      </div>

      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-tree
          class="el-tree"
          :data="treeData"
          :props="defaultProps"
          node-key="id"
          :highlight-current="highlight"
          :default-expanded-keys="expandedKeys"
          :filter-node-method="filterNode"
          @node-click="sourceNodeClick"
          ref="elTree"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span class="el-tree-node__label">
              <i :class="data.icon"></i>
              {{ node.label }}
            </span>
          </span>
        </el-tree>
      </el-scrollbar>
    </div>
  </el-aside>
</template>

<script>
import {
  dataClassAll,
  databaseClass,
  logicClass,
  datumTypeGetTree
} from "@/api/structureManagement/tableStructureManage/StructureClassify";
import { delByClass } from "@/api/structureManagement/tableStructureManage/index";
export default {
  props: { treeIdOfDR: String },
  data() {
    return {
      loading: true,
      //格式化tree数据
      defaultProps: {
        children: "children",
        label: "name"
      },
      highlight: true,
      tabPosition: "left",
      treeData: [], //tree数据
      checkNode: {}, //选中高亮的节点id
      expandedKeys: [], //展开的节点
      sourceActive: true, //资料树选中状态
      sourceTreeOp: true, //资料树头部显示与否状态
      sourceTreeText: "",
      publicTreeTextActive: false,
      backBtn: false,
      publicActive: false,
      /***              资料树操作数据                     */
      operateNodeObj: {},
      checkedNodeArr: {}, //选中的资料树id集合
      editNodeId: "", //编辑资料树的id
      showNoCreat: "primary",
      showAll: "",
      myTreedata: [],
      whichTree: "资料分类树" //区分是哪颗树
    };
  },

  watch: {
    // 监控资料分类树
    sourceTreeText(val) {
      this.$refs.elTree.filter(val);
    }
  },
  mounted() {
    //初始化资料分类树
    this.initMethodsTree("资料分类树");
  },
  methods: {
    // 树的搜索方法
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 初始化树 同步
    async initMethodsTree(whichTree) {
      this.loading = true;
      let classBox = document.getElementsByClassName("classifyTree");
      classBox[0].classList.remove("disActive");
      if (whichTree == "资料分类树") {
        this.sourceTreeOp = true;
        await dataClassAll().then(response => {
          this.treeData = response.data;
        });
      } else if (whichTree == "数据用途分类树") {
        this.publicTreeTextActive = true;
        await logicClass().then(response => {
          this.treeData = response.data;
        });
      } else if (whichTree == "数据库分类树") {
        this.publicTreeTextActive = true;
        await databaseClass().then(response => {
          this.treeData = response.data;
        });
      } else if (whichTree == "公共元数据结构树") {
        classBox[0].classList.add("disActive");
        this.publicActive = true;
        await datumTypeGetTree().then(response => {
          this.treeData = response.data;
        });
      }
      this.defaultStyle();
    },

    //左侧分类点击
    handleTabClick(tab, event) {
      this.sourceTreeText = "";
      this.sourceTreeOp = false;
      this.publicTreeTextActive = false;
      this.backBtn = false;
      this.publicActive = false;
      this.whichTree = tab.label;
      this.$emit("getTreeUrlOfTab", this.whichTree);
      this.initMethodsTree(tab.label);
    },
    // 节点默认展开
    defaultStyle() {
      if (this.treeData.length != 0) {
        this.getDefaultNode(this.treeData[0]);
      }
      this.$refs.elTree.setCurrentKey(this.checkNode.id);
      this.sourceNodeClick(this.checkNode);
    },
    //获取默认选中的节点
    getDefaultNode(nodeArr) {
      if (this.publicActive == true) {
        if (nodeArr.children) {
          let newNodeArr = nodeArr.children[0];
          this.getDefaultNode(newNodeArr);
        } else {
          this.checkNode = nodeArr;
        }
      } else {
        this.checkNode = nodeArr;
      }
      if (this.treeIdOfDR) {
        // 默认展开从数据注册审核页面过来的节点
        this.expandedKeys.push(this.treeIdOfDR);
      } else {
        this.expandedKeys.push(nodeArr.id);
      }
    },

    //节点点击
    sourceNodeClick(data) {
      this.myTreedata = [];
      this.checkedNodeArr = {};
      this.resetData(data);
      this.editNodeId = data.id;

      let treeIds = [];
      this.myTreedata.forEach(single => {
        if (single.disabled) {
          treeIds.push(single.id);
        }
      });
      if (this.publicActive == true) {
        this.checkedNodeArr.style = "showPublicMain";
        this.checkedNodeArr.id = data.id;
        this.checkedNodeArr.name = data.label;
        this.checkedNodeArr.icon = data.icon;
        this.$emit("searchFun", this.checkedNodeArr, data);
      } else {
        this.checkedNodeArr.style = undefined;
        this.$emit("searchFun", treeIds.join(","), data);
      }
    },
    //获取父节点下的子节点
    resetData(dataArr) {
      var that = this;
      that.myTreedata.push({
        label: dataArr.label,
        id: dataArr.id,
        disabled: dataArr.disabled
      });
      if (dataArr.children) {
        var child = dataArr.children;
        for (var i = 0; i < child.length; i++) {
          that.resetData(child[i]);
        }
      }
    },
    //资料树
    showSourceDialog(operateType) {
      this.operateNodeObj.tree = this.whichTree;
      if (!operateType) {
        this.operateNodeObj.title = "新增资料树节点";
        this.operateNodeObj.editNodeId = "";
        this.$emit("showMaterialSingle", this.operateNodeObj);
      } else {
        this.operateNodeObj.title = "编辑资料树节点";
        this.operateNodeObj.editNodeId = this.editNodeId;
        this.$emit("showMaterialSingle", this.operateNodeObj);
      }
    },
    // 删除资料分类
    deleteSourceNode() {
      console.log(this.editNodeId);
      delByClass({ dataClassId: this.editNodeId }).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
          //刷新资料树
          this.initMethodsTree(this.whichTree);
        }
      });
    },
    // 显示未创建、显示全部
    handleShowTree(type) {
      //初始化资料分类树
      if (this.showAll == "primary" && type == "all") {
        this.showNoCreat = "primary";
        this.showAll = "";
      } else if (this.showNoCreat == "primary" && type == "noCreat") {
        this.showNoCreat = "";
        this.showAll = "primary";
      }
      let classBox = document.getElementsByClassName("classifyTree");
      classBox[0].classList.remove("disActive");
    },
    //查询分类树
    sourceTopSearch() {
      this.sourceTreeOp = false;
      this.publicActive = false;
      this.publicTreeTextActive = true;
      this.backBtn = true;
    },
    // 返回
    backTop() {
      this.sourceTreeOp = true;
      this.publicActive = false;
      this.publicTreeTextActive = false;
      this.backBtn = false;
    }
  }
};
</script>

<style scope lang='scss'>
.asideTreeStructure {
  padding: 0;
  max-width: 250px;
  background: #fff;
  margin: 0 10px;
  border: 1px solid #ebeef5;
  overflow: hidden;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  height: calc(100vh - 130px);
  //分类
  .el-tabs__item {
    white-space: normal;
    width: 32px;
    padding: 8px 10px;
    line-height: 16px;
    font-size: 14px;
    height: auto;
  }
  .el-scrollbar {
    margin-top: 10px;
    height: calc(100% - 80px);
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content {
    background-color: #c6e2ff;
  }
  //树
  .classifyTree {
    width: 216px;
    overflow: hidden;
    // 资料分类操作
    .sourceTreeTopOp {
      width: 100%;
      text-align: center;
      box-sizing: border-box;
      padding: 10px 0;
      padding-bottom: 0;
      margin-left: 10px;
      cursor: pointer;
      .el-button {
        i {
          font-weight: bold;
        }
      }
    }
    //逻辑库分类树
    .usedTreeOp {
      width: 100%;
      text-align: center;
      box-sizing: border-box;
      .el-input {
        width: 140px;
        margin-right: 2px;
        margin-top: 10px;
      }
    }
    // 公共元数据结构
    .publicTreeOp {
      width: 98%;
      text-align: center;
      box-sizing: border-box;
      padding: 10px 0;
      margin: auto;
      .el-button--small,
      .el-button--small.is-round {
        padding: 9px 10px;
      }
    }
    .el-tree {
      min-width: 100%;
      display: inline-block !important;
      i {
        color: #409eff;
      }
    }

    .el-tree-node[aria-disabled="true"] {
      display: none;
    }
    .el-tree-node[aria-disabled="false"] {
      color: #409eff;
    }
  }
  .disActive {
    .el-tree-node[aria-disabled="true"] {
      display: inline-block;
    }
  }
}
</style>
