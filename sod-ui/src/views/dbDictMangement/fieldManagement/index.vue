<template>
  <div class="app-container fielddManagement">
    <!-- 字段与索引类型管理 -->
    <el-container>
      <!-- 左侧树 -->
      <el-aside class="elAside">
        <div class="sourceTreeTopOp">
          <el-button-group>
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
            <el-input size="small" v-model="queryParams.keyCol" placeholder="申请用户"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addRow()">新增字典</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-edit" @click="editRow()">编辑字典</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteRow()">删除字典</el-button>
          </el-col>
        </el-row>
        <el-table :data="tableData" highlight-current-row @current-change="handleSelectionChange">
          <el-table-column type="index" width="50" :index="table_index"></el-table-column>
          <el-table-column width="50">
            <template slot-scope="scope">
              <!-- 添加一个多选框,控制选中与否 -->
              <el-checkbox v-model="scope.row.checked"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="关键字" prop="keyCol"></el-table-column>
          <el-table-column label="中文名" prop="nameCn"></el-table-column>
          <el-table-column label="字典类型" prop="dataType"></el-table-column>
          <el-table-column label="字典描述" prop="description"></el-table-column>
          <el-table-column label="是否可删" prop="canDelete"></el-table-column>
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
    <el-dialog :title="dialogTitle" :visible.sync="TreeDialog">
      <handleTree v-if="TreeDialog" :handleTreeObj="handleTreeObj" @cancelDialog="cancelDialog"></handleTree>
    </el-dialog>

    <!-- 弹窗 字典-->
    <el-dialog :title="dialogTitle" :visible.sync="DictDialog">
      <handleDict
        v-if="DictDialog"
        :dictTypes="treeData"
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
  deleteByIds
} from "@/api/dbDictMangement/fieldManagement/index";

import handleTree from "@/views/dbDictMangement/fieldManagement/handleTree";
import handleDict from "@/views/dbDictMangement/fieldManagement/handleDict";
//分类树
export default {
  components: {
    handleTree,
    handleDict
  },
  data() {
    return {
      treeData: [], //字典分组
      loading: true,
      menuType: "2", //字典类型
      defaultProps: {
        children: "children",
        label: "keyCol"
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
        pageNum: "",
        pageSize: "",
        pageNum: 1,
        pageSize: 10
      },
      indexNodeId: "",
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {}
    };
  },
  async created() {
    await this.getTreeData();
  },
  methods: {
    //初始化字典分组数据
    async getTreeData() {
      await findMenu({ menu: this.menuType }).then(res => {
        res.success
          ? (this.treeData = res.data)
          : this.$notify.error({
              title: "错误",
              message: res.msg
            });
      });
      if (this.treeData.length > 0) {
        this.$refs.elTree.setCurrentKey(this.treeData[0].id);
        this.queryParams.menu = this.treeData[0].menu;
        this.queryParams.type = this.treeData[0].type;
        this.indexNodeId = this.treeData[0].id;
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
      this.indexNodeId = node.id;
      this.handleQuery();
    },
    //查询表格数据
    handleQuery() {
      this.loading = true;
      console.log(this.queryParams);
      getTableData(this.queryParams).then(res => {
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
      this.dialogTitle = "添加类型";
      this.TreeDialog = true;
    },
    editType() {},
    deleteAlert() {
      if (!this.checkNode.id) {
        this.$message({ message: "请选中需要删除的数据!", type: "warning" });
        return;
      }
      this.$confirm("数据和类型都会被删除，确认删除", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning"
      })
        .then(() => {
          this.rowData = this.checkNode;
          this.deleteFun();
        })
        .catch(() => {}); //一定别忘了这个
    },
    showSearch() {},

    async cancelDialog(msg) {
      if (msg === "D") {
        this.handleQuery();
      }
      if (msg === "T") {
        await findMenu({ menu: this.menuType }).then(res => {
          res.success
            ? (this.treeData = res.data)
            : this.$notify.error({
                title: "错误",
                message: res.msg
              });
        });
        this.$refs.elTree.setCurrentKey(this.indexNodeId);
      }
      this.TreeDialog = false;
      this.DictDialog = false;
    },
    addRow() {
      this.dialogTitle = "新增字典";
      this.DictDialog = true;
    },
    editRow() {},
    deleteRow() {},
    handleSelectionChange() {}
  }
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
