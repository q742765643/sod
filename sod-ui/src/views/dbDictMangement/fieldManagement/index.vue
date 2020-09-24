<template>
  <div class="app-container fielddManagement">
    <!-- 字段与索引类型管理 -->
    <el-container>
      <!-- 左侧树 -->
      <el-aside class="elAside">
        <div class="sourceTreeTopOp">
          <el-input
            size="mini"
            style="width:150px;"
            placeholder="输入关键字进行过滤"
            v-model.trim="filterText"
            v-if="showFilter"
          ></el-input>
          <el-button
            type="primary"
            size="mini"
            @click="back()"
            icon="el-icon-back"
            v-if="showFilter"
          ></el-button>
          <el-button-group v-if="showTreeBtn">
            <el-button type="primary" size="mini" @click="addType()" icon="el-icon-plus"></el-button>
            <el-button type="primary" size="mini" @click="editType()" icon="el-icon-edit"></el-button>
            <el-button type="primary" size="mini" @click="deleteAlert()" icon="el-icon-delete"></el-button>
            <el-button
              class="sourceBtn"
              type="primary"
              @click="showSearch()"
              size="mini"
              icon="el-icon-search"
            ></el-button>
          </el-button-group>
        </div>
        <div class="treeTitle">字典分组</div>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            :filter-node-method="filterNode"
            class="el-tree elTree"
            :data="treeData"
            :props="defaultProps"
            :highlight-current="true"
            node-key="id"
            @node-click="handleNodeClick"
            ref="elTree"
            :render-content="renderContent"
          ></el-tree>
        </el-scrollbar>
      </el-aside>
      <el-main class="elMain">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="关键字">
            <el-input size="small" v-model.trim="queryParams.keyCol" placeholder="申请用户"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addRow()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-edit" @click="editRow()">编辑</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteRow()">删除</el-button>
          </el-col>
        </el-row>
        <el-table border :data="tableData" @selection-change="handleSelectionChange">
          <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column label="关键字" prop="keyCol"></el-table-column>
          <el-table-column label="中文名" prop="nameCn"></el-table-column>
          <el-table-column label="字典类型" prop="dataType"></el-table-column>
          <el-table-column label="字典描述" prop="description"></el-table-column>
          <el-table-column label="是否可删" prop="canDelete">
            <template slot-scope="scope">
              <span v-if="scope.row.canDelete=='Y'">是</span>
              <span v-if="scope.row.canDelete=='N'">否</span>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="handleQuery"
        />
      </el-main>
    </el-container>
    <!-- 弹窗 树-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="TreeDialog"
      v-dialogDrag
    >
      <handleTree v-if="TreeDialog" :handleTreeObj="handleTreeObj" @cancelDialog="cancelDialog"></handleTree>
    </el-dialog>

    <!-- 弹窗 字典-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="DictDialog"
      v-dialogDrag
    >
      <handleDict
        v-if="DictDialog"
        :handleGroup="handleGroup"
        :handleDictObj="handleDictObj"
        @cancelDialog="cancelDialog"
      ></handleDict>
    </el-dialog>
  </div>
</template>
<script>
//接口
import {
  findMenu,
  getTableData,
  deleteByIds,
  findById,
} from "@/api/dbDictMangement/fieldManagement";

import handleTree from "@/views/dbDictMangement/fieldManagement/handleTree";
import handleDict from "@/views/dbDictMangement/fieldManagement/handleDict";
//分类树
export default {
  components: {
    handleTree,
    handleDict,
  },
  data() {
    return {
      showFilter: false,
      showTreeBtn: true,
      filterText: "",
      treeData: [], //字典分组
      loading: true,
      menuType: "2", //字典类型
      defaultProps: {
        children: "children",
        label: "keyCol",
      },

      checkNode: {}, //选中高亮的节点
      // 弹窗 树
      dialogTitle: "",
      TreeDialog: false,
      handleTreeObj: {},
      // 表格
      tableData: [],
      total: 0,
      queryParams: {
        keyCol: "",
        pageNum: 1,
        pageSize: 10,
      },
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {},
      multipleSelection: [],
    };
  },
  async created() {
    await this.getTreeData();
  },
  watch: {
    filterText(val) {
      this.$refs.elTree.filter(val);
    },
  },
  methods: {
    // 树过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.keyCol.indexOf(value) !== -1;
    },
    //初始化字典分组数据
    async getTreeData() {
      await findMenu({ menu: this.menuType }).then((res) => {
        res.success
          ? (this.treeData = res.data)
          : this.$notify.error({
              title: "错误",
              message: res.msg,
            });
      });
      if (this.treeData.length > 0) {
        this.$refs.elTree.setCurrentKey(this.treeData[0].id);
        this.queryParams.menu = this.treeData[0].menu;
        this.queryParams.type = this.treeData[0].type;
        this.checkNode = this.treeData[0];
        this.handleQuery();
      } else {
        this.loading = false;
      }
    },
    //格式化tree
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node treeItem">
          <i class="el-icon-menu"></i>
          <span style="margin-left:5px;">{node.label}</span>
        </span>
      );
    },
    //字典树点击查询
    handleNodeClick(node) {
      this.queryParams.menu = node.menu;
      this.queryParams.type = node.type;
      this.checkNode = node;
      this.handleQuery();
    },
    //查询表格数据
    handleQuery() {
      this.loading = true;
      getTableData(this.queryParams).then((res) => {
        this.tableData = res.data.pageData;
        this.total = res.data.totalCount;
        this.loading = false;
      });
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    addType() {
      this.dialogTitle = "添加";
      this.handleTreeObj = {};
      this.TreeDialog = true;
    },
    editType() {
      if (!this.checkNode.id) {
        this.$message({ message: "请选中需要编辑的数据!", type: "error" });
        return;
      }
      this.dialogTitle = "编辑";
      this.handleTreeObj = this.checkNode;
      this.TreeDialog = true;
    },
    deleteAlert() {
      if (!this.checkNode.id) {
        this.$message({ message: "请选中需要删除的数据!", type: "warning" });
        return;
      }
      this.$confirm("数据和类型都会被删除，确认删除", "温馨提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          deleteByIds({ ids: this.checkNode.id }).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
            }
            this.getTreeData();
          });
        })
        .catch(() => {}); //一定别忘了这个
    },
    showSearch() {
      this.showFilter = true;
      this.showTreeBtn = false;
    },
    back() {
      this.showFilter = false;
      this.showTreeBtn = true;
    },
    async cancelDialog(msg) {
      if (msg === "Dict") {
        this.handleQuery();
      }
      if (msg === "Tree") {
        await this.getTreeData();
      }
      this.TreeDialog = false;
      this.DictDialog = false;
    },
    addRow() {
      this.handleDictObj = {};
      this.dialogTitle = "新增字典";
      this.handleGroup = this.treeData;
      this.DictDialog = true;
    },
    editRow() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      this.handleGroup = this.treeData;
      findById({ id: this.multipleSelection[0].id }).then((res) => {
        this.dialogTitle = "编辑字典";
        this.handleDictObj = res.data;
        this.DictDialog = true;
      });
    },
    deleteRow() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      let flag = false;
      let nameArry = [];
      this.multipleSelection.forEach((element) => {
        if (flag || element.canDelete == "N") {
          flag = true;
          nameArry.push(element.keyCol);
        }
      });
      if (flag) {
        this.$message({
          type: "error",
          message: nameArry.join(",") + "只能修改，不能删除",
        });
        return;
      }
      if (!flag) {
        this.$confirm("确认删除吗", "温馨提示", {
          cancelButtonText: "取消",
          confirmButtonText: "确定",
          type: "warning",
        })
          .then(() => {
            let ids = [];
            this.multipleSelection.forEach((element) => {
              ids.push(element.id);
            });
            deleteByIds({ ids: ids.join(",") }).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
              }
              this.handleQuery();
            });
          })
          .catch(() => {}); //一定别忘了这个
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
};
</script>
<style lang="scss">
.fielddManagement {
  padding: 20px 5px 0 5px;
  .elAside {
    max-width: 250px;
    padding: 0;
    margin: 0 10px;
    background: #fff;
    border: 1px solid #ebeef5;
    overflow: hidden;
    box-sizing: border-box;
    height: calc(100vh - 130px);
    .sourceTreeTopOp {
      width: 100%;
      padding: 10px 0;
      text-align: center;
      .el-button {
        i {
          font-weight: bold;
        }
      }
    }
    .treeTitle {
      border-bottom: 1px solid #ebeef5;
      width: 100%;
      text-align: center;
      background: #eee;
      font-size: 14px;
    }
    .el-scrollbar {
      height: calc(100% - 80px);
    }
    .el-scrollbar__wrap {
      overflow: auto;
    }
    .elTree {
      .el-tree-node__content {
        height: 33px;
        .treeItem {
          font-size: 14px;
        }
      }
    }
  }
  .elMain {
    border: 1px solid #ebeef5;
    overflow: hidden;
  }
}
</style>
