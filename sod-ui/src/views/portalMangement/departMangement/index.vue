<template>
  <div class="app-container">
    <!--portal 系统管理 单位管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item prop="dataName" label="部门编号:">
        <el-input clearable size="small" v-model="queryParams.dataName" placeholder="请输入部门编号" />
      </el-form-item>
      <el-form-item prop="dataName" label="部门名称:">
        <el-input clearable size="small" v-model="queryParams.dataName" placeholder="请输入部门名称" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">新增</el-button>
      </el-col>
    </el-row>
    <!-- 数据展示列表 -->
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="ddataId" label="部门编号"></el-table-column>
      <el-table-column prop="dataName" label="部门名称"></el-table-column>
      <el-table-column prop="icon" label="部门类型"></el-table-column>
      <el-table-column prop="serialNumber" label="上级部门"></el-table-column>
      <el-table-column prop="isshow" label="排序"></el-table-column>
      <el-table-column prop="serialNumber" label="部门级别"></el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-coordinate"
            size="mini"
            type="text"
            @click="showDialog(scope.row)"
          >编辑</el-button>
          <el-button icon="el-icon-delete" size="mini" type="text" @click="deleteRow(scope.row)">删除</el-button>
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
      width="600px"
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      v-dialogDrag
      top="5vh"
    >
      <handleDepart
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></handleDepart>
    </el-dialog>
  </div>
</template>

<script>
//import { queryDataPage, delById } from "@/api/portalMangement/userMangement";
import handleDepart from "@/views/portalMangement/departMangement/handleDepart";

export default {
  components: {
    handleDepart,
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sdkType: "",
      },
      tableData: [],
      total: 0,
      SDKClassify: [],
      //已勾选记录
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      handleObj: {},
    };
  },
  /** 方法调用 */
  created() {
    this.getDicts("portal_sdk_mng").then((response) => {
      this.SDKClassify = response.data;
    });
    this.getList();
  },
  methods: {
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
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    showDialog(row) {
      if (row.id) {
        this.dialogTitle = "编辑";
        this.handleObj = row;
      } else {
        this.dialogTitle = "新增";
        this.handleObj = {};
      }
      this.msgFormDialog = true;
    },
    auditRow(row) {
      this.$confirm("", "用户审核", {
        distinguishCancelAndClose: true,
        confirmButtonText: "通过",
        cancelButtonText: "驳回",
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "执行了通过",
          });
          this.getList();
        })
        .catch((action) => {
          this.$message({
            type: "error",
            message: "执行了驳回",
          });
          this.getList();
        });
    },
    refreshPassword(row) {
      this.$confirm("确定要继续当前操作码?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delById({ id: row.id }).then((response) => {
            this.$alert("重置密码成功，默认密码为123qweasdzxc", "温馨提示", {
              confirmButtonText: "确定",
              callback: (action) => {},
            });
          });
        })
        .catch(() => {});
    },
    deleteRow(row) {
      this.$confirm("确认要删除" + row.ddataId + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delById({ id: row.id }).then((response) => {
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
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /** 查询列表 */

    getList() {
      this.loading = true;
      queryDataPage(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        //自动选中之前已选行
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    //设置表单选中指定行
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
    cancelDialog() {
      this.msgFormDialog = false;
      this.exportFormDialog = false;
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
  },
};
</script>


