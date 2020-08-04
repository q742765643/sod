<template>
  <div class="app-container">
    <!-- 数据用途管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据用途ID:" prop="logicFlag">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.logicFlag"
          placeholder="请输入数据用途ID"
        />
      </el-form-item>
      <el-form-item label="用途描述:" prop="logicName">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.logicName"
          placeholder="请输入用途描述"
        />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="logicFlag" label="数据用途ID"></el-table-column>
      <el-table-column prop="logicName" label="用途描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="storageType" label="表类型" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{storageTypeShow(scope.row.logicStorageTypesEntityList)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="databaseName" label="数据库名称" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{dbNameShow(scope.row.logicDatabaseEntityList)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="logicDesc" label="用途说明" width="360" :show-overflow-tooltip="true"></el-table-column>

      <el-table-column prop="createTime" label="创建时间" width="160">
        <template slot-scope="scope">
          <span>{{parseTime(scope.row.createTime)}}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      v-dialogDrag
    >
      <dataUsageEdit
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></dataUsageEdit>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryDataBaseLogicAll,
  delLogic,
  getById,
  exportTable,
} from "@/api/dbDictMangement/dataUsageMangement";
import dataUsageEdit from "@/views/dbDictMangement/dataUsageMangement/dataUsageEdit";
export default {
  components: {
    dataUsageEdit,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_fcst_ele: "",
      },
      tableData: [],
      total: 0,
      // 导入
      actionUrl: "",
      showFile: false,
      importHeaders: {
        enctype: "multipart/form-data",
      },
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      ruleForm: {},
      handleObj: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.tableData.forEach((element, index) => {
            if (element.id == row.id) {
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index]
              );
            }
          });
        });
      }
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    storageTypeShow(list) {
      let names = [];
      list.forEach((element) => {
        names.push(element.storageName);
      });
      return names.join(",");
    },
    dbNameShow(list) {
      let names = [];
      list.forEach((element) => {
        names.push(element.databaseName);
      });
      return names.join(",");
    },
    // 导出
    handleExport() {
      exportTable(this.queryParams).then((res) => {
        this.downloadfileCommon(res);
      });
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */

    getList() {
      this.loading = true;
      queryDataBaseLogicAll(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.dialogTitle = "新增";
        this.handleObj = {};
        this.msgFormDialog = true;
      } else {
        if (this.multipleSelection.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return;
        }
        this.dialogTitle = "编辑";
        getById({ id: this.multipleSelection[0].id }).then((res) => {
          this.handleObj = res.data;
          this.handleObj.storageType = [];
          res.data.logicStorageTypesEntityList.forEach((element) => {
            this.handleObj.storageType.push(element.storageType);
          });

          this.handleObj.databaseId = [];
          res.data.logicDatabaseEntityList.forEach((element) => {
            this.handleObj.databaseId.push(element.databaseId);
          });
          this.msgFormDialog = true;
        });
      }
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteCell() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      } else {
        this.$confirm(
          "确认要删除" + this.multipleSelection[0].logicFlag + "吗?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            delLogic({ id: this.multipleSelection[0].id }).then((response) => {
              if (response.code == 200) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.handleQuery();
              } else {
                this.$message({
                  type: "error",
                  message: "删除失败",
                });
              }
            });
          })
          .catch(() => {});
      }
    },
    cancelDialog() {
      this.msgFormDialog = false;
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
  },
};
</script>
