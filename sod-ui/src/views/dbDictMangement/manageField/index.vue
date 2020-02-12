<template>
  <div class="app-container fielddManagement">
    <!-- 管理字段管理 -->
    <el-container>
      <!-- 左侧树 -->
      <el-aside class="elAside">
        <div class="sourceTreeTopOp">
          <el-button-group>
            <el-button type="primary" size="mini" @click="addType()" icon="el-icon-plus"></el-button>
            <el-button type="primary" size="mini" @click="editType()" icon="el-icon-edit"></el-button>
            <el-button type="primary" size="mini" @click="deleteAlert()" icon="el-icon-delete"></el-button>
          </el-button-group>
        </div>
        <div class="treeTitle">管理字段分组</div>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            class="el-tree"
            :highlight-current="highlight"
            node-key="groupId"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            ref="elTree"
            :render-content="renderContent"
          ></el-tree>
        </el-scrollbar>
      </el-aside>
      <el-main class="elMain">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="字段编码">
            <el-input size="small" v-model="queryParams.db_ele_code" placeholder="字段编码"></el-input>
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
          <el-col :span="1.5">
            <el-button size="small" type="success" icon="el-icon-download" @click="exportData()">导出</el-button>
          </el-col>
        </el-row>
        <el-table :data="tableData" highlight-current-row @current-change="handleSelectionChange">
          <el-table-column type="index" width="40" :index="table_index"></el-table-column>
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column label=" " prop="id" v-if="false"></el-table-column>
          <el-table-column label="字段编码" prop="db_ele_code"></el-table-column>
          <el-table-column label="服务编码" prop="user_ele_code"></el-table-column>
          <el-table-column label="中文名称" prop="db_ele_name"></el-table-column>
          <el-table-column label="字段类型" prop="type"></el-table-column>
          <el-table-column label="字段精度" prop="length"></el-table-column>
          <el-table-column label="是否可为空" prop="is_null" :formatter="formatBoolean"></el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-main>
    </el-container>
    <!-- 弹窗 树-->
    <el-dialog :title="dialogTitle" :visible.sync="TreeDialog">
      <handleTree v-if="TreeDialog" :handleTreeObj="handleTreeObj" @cancelDialog="cancelDialog"></handleTree>
    </el-dialog>

    <!-- 弹窗 字典-->
    <el-dialog :title="dialogTitle" :visible.sync="DictDialog">
      <handleDict v-if="DictDialog" :handleDictObj="handleDictObj" @cancelDialog="cancelDialog"></handleDict>
    </el-dialog>
  </div>
</template>
<script>
import handleTree from "@/views/dbDictMangement/manageField/handleTree";
import handleDict from "@/views/dbDictMangement/manageField/handleDict";

import {
  findAllManageGroup,
  findManageFieldByPk,
  delManageGroup
} from "@/api/dbDictMangement/manageField";

//分类树
export default {
  components: {
    handleTree,
    handleDict
  },
  data() {
    return {
      highlight: true,
      treeData: [],
      defaultProps: {
        children: "children",
        label: "groupName"
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
        id: "",
        pageNum: 1,
        pageSize: 10
      },
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {}
    };
  },
  async created() {
    await this.getTreeData();
  },
  methods: {
    // 查询字段分组
    async getTreeData() {
      await findAllManageGroup().then(res => {
        res.success
          ? (this.treeData = res.data)
          : this.$message({
              type: "error",
              message: res.msg
            });
      });
      this.$refs.elTree.setCurrentKey(this.treeData[0].groupId);
      this.checkNode = this.treeData[0];
      this.queryParams.id = this.treeData[0].groupId;
      // this.handleQuery();
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
    //树点击查询
    handleNodeClick(node) {
      this.queryParams.id = node.groupId;
      this.checkNode = node;
      this.handleQuery();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    addType() {
      this.dialogTitle = "添加管理字段分组";
      this.TreeDialog = true;
    },
    editType() {
      if (!this.checkNode.groupId) {
        this.$message({ message: "请选中需要编辑的数据!", type: "error" });
        return;
      }
      this.dialogTitle = "编辑管理字段分组";
      this.handleTreeObj = this.checkNode;
      this.TreeDialog = true;
    },
    deleteAlert() {
      if (!this.checkNode.groupId) {
        this.$message({ message: "请选中需要删除的数据!", type: "error" });
        return;
      }
      this.$confirm("数据和类型都会被删除，确认删除", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning"
      })
        .then(() => {
          delManageGroup({ groupId: this.queryParams.id }).then(res => {
            this.getTreeData();
          });
        })
        .catch(() => {}); //一定别忘了这个
    },
    showSearch() {},
    async cancelDialog(msg) {
      if (msg == "Tree") {
        await this.getTreeData();
      } else if (msg == "File") {
      }
      this.TreeDialog = false;
      this.DictDialog = false;
    },
    handleQuery() {
      this.loading = true;
      console.log(this.queryParams);
      findManageFieldByPk(this.queryParams).then(res => {
        this.tableData = res.data.pageData;
        this.total = res.data.totalCount;
        this.loading = false;
      });
    },
    addRow() {
      this.dialogTitle = "新增字典";
      this.DictDialog = true;
    },
    editRow() {},
    deleteRow() {},
    handleSelectionChange() {},
    getList() {},
    exportData() {
      // let url =
      //   interfaceObj.manageFieldApi_export +
      //   "?group_id=" +
      //   this.searchObj.group_id;
      // testExport(url);
    },
    formatBoolean(row, column, cellValue) {
      var ret = ""; //你想在页面展示的值
      if (cellValue) {
        ret = "是"; //根据自己的需求设定
      } else {
        ret = "否";
      }
      return ret;
    }
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
  }
  .elMain {
    border: 1px solid #ebeef5;
    overflow: hidden;
  }
}
</style>
