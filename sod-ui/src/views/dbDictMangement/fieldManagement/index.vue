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
            class="el-tree"
            :highlight-current="highlight"
            node-key="type"
            :data="treeData"
            :props="defaultProps"
            :default-expanded-keys="expandedKeys"
            @node-click="handleNodeClick"
            ref="elTree"
          ></el-tree>
        </el-scrollbar>
      </el-aside>
      <el-main class="elMain">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="关键字">
            <el-input size="small" v-model="queryParams.key_col" placeholder="申请用户"></el-input>
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
          <el-table-column label="关键字" prop="key_col"></el-table-column>
          <el-table-column label="中文名" prop="name_cn"></el-table-column>
          <el-table-column label="字典类型" prop="typeName"></el-table-column>
          <el-table-column label="字典描述" prop="description"></el-table-column>
          <el-table-column label="是否可删" prop="can_delete"></el-table-column>
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
      highlight: true,
      treeData: [],
      // defaultProps: {
      //   children: "children",
      //   label: "key_col"
      // },
      defaultProps: {
        children: "children",
        label: "label"
      },
      checkNode: {}, //选中高亮的节点
      expandedKeys: [], //展开的节点
      // 弹窗 树
      dialogTitle: "",
      TreeDialog: false,
      handleTreeObj: {},
      // 表格
      tableData: [],
      total: 0,
      queryParams: {
        key_col: "",
        pageNum: "",
        pageSize: "",
        pageNum: 1,
        pageSize: 10
      },
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {}
    };
  },
  created() {},
  methods: {
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
    handleNodeClick() {},
    cancelDialog(msg) {
      if (!msg) {
        this.TreeDialog = false;
        this.DictDialog = false;
      }
    },
    handleQuery() {},
    addRow() {
      this.dialogTitle = "新增字典";
      this.DictDialog = true;
    },
    editRow() {},
    deleteRow() {},
    handleSelectionChange() {},
    getList() {}
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
      height: calc(100% - 120px);
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
