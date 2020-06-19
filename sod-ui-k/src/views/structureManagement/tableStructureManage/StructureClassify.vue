<template>
  <el-aside class="asideTreeStructure elTreeAsideBox" id="left" style="width:250px;">
    <el-tabs :tab-position="tabPosition" @tab-click="handleTabClick">
      <el-tab-pane label="资料分类树"></el-tab-pane>
      <el-tab-pane label="数据用途分类树" v-if="tableStructureManageContral"></el-tab-pane>
      <el-tab-pane label="数据库分类树" v-if="tableStructureManageContral"></el-tab-pane>
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
          v-model.trim="sourceTreeText"
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
          <span
            :class="data.haveClass?'hasClass custom-tree-node':'custom-tree-node'"
            slot-scope="{ node, data }"
          >
            <span class="el-tree-node__label" v-if="whichTree == '数据用途分类树'||whichTree == '数据库分类树'">
              <i :class="data.icon"></i>
              <span>{{ node.label }}</span>
            </span>
            <span class="el-tree-node__label" v-else>
              <i :class="data.icon"></i>
              <span>{{node.label+'('+data.id+')'}}</span>
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
import {
  delByClass,
  enable
} from "@/api/structureManagement/tableStructureManage/index";
export default {
  props: { treeIdOfDR: String },
  data() {
    return {
      tableStructureManageContral: false,
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
      showNoCreat: "info",
      showAll: "primary",
      myTreedata: [],
      whichTree: "资料分类树", //区分是哪颗树
      defaultExpandedKeys: null
    };
  },

  watch: {
    // 监控资料分类树
    sourceTreeText(val) {
      this.$refs.elTree.filter(val);
    }
  },
  mounted() {
    enable().then(res => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
    //初始化资料分类树
    this.initMethodsTree("资料分类树");
  },
  methods: {
    // 树的搜索方法
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1 && data.disabled != true;
    },
    // 初始化树 同步
    async initMethodsTree(whichTree) {
      this.loading = true;
      this.treeData = [];
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
      if (this.defaultExpandedKeys) {
        this.$refs.elTree.setCurrentKey(this.defaultExpandedKeys.id);
        this.expandedKeys = [];
        this.expandedKeys.push(this.defaultExpandedKeys.id);
      } else {
        this.defaultStyle();
      }
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
      console.log(this.checkNode);
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
      this.defaultExpandedKeys = data;
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
        this.checkedNodeArr.name = data.name;
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
      this.$confirm("确认删除该节点吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
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
        })
        .catch(() => {});
    },
    // 显示未创建、显示全部
    handleShowTree(type) {
      let classBox = document.getElementsByClassName("classifyTree");
      //初始化资料分类树
      if (type == "all") {
        this.showNoCreat = "info";
        this.showAll = "primary";
        classBox[0].classList.add("disActive");
      } else if (type == "noCreat") {
        this.showNoCreat = "primary";
        this.showAll = "info";
        classBox[0].classList.remove("disActive");
      }
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
      this.sourceTreeOp = false;
      this.publicActive = false;
      this.publicTreeTextActive = false;
      this.backBtn = false;
      if (this.whichTree == "公共元数据结构树") {
        this.publicActive = true;
      } else {
        this.sourceTreeOp = true;
      }
    }
  }
};
</script>

<style scope lang='scss'>
.asideTreeStructure {
  padding: 0;
  // max-width: 250px;
  background: #fff;
  margin: 0 0 0 10px;
  border: 1px solid #ebeef5;
  overflow: hidden;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  float: left;
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

  //树
  .classifyTree {
    width: calc(100% - 42px);
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
      .el-button--info {
        color: #909399;
        background: #f4f4f5;
        border-color: #d3d4d6;
      }
    }
    .el-tree {
      i {
        color: #409eff;
      }
      .el-button--text {
        color: #606266;
      }
    }
    /* .el-tree-node__children{
      
    } */
    .el-tree-node[aria-disabled="true"] {
      display: none;
    }
    .el-tree-node[aria-disabled="false"] {
      color: #409eff;
    }
  }
  .disActive {
    .el-tree-node[aria-disabled="true"] {
      display: block;
    }
  }
}
</style>
