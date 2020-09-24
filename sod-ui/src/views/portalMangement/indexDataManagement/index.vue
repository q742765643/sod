<template>
  <div class="app-container">
    <!-- 首页数据管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item clearable label="描述:" prop="remark">
        <el-input clearable size="small" v-model="queryParams.remark" placeholder="请输入描述信息" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
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
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="dataCode" label="数据编号"></el-table-column>
      <el-table-column prop="dataValue" label="数据值"></el-table-column>
      <el-table-column prop="remark" label="描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="address" label="操作" width="150">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" size="mini" type="text" @click="showDialog(scope.row)">编辑</el-button>
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
      :visible.sync="visibleDialog"
      v-dialogDrag
      top="5vh"
    >
      <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
        <el-form-item prop="dataCode" label="数据编号:">
          <el-input clearable size="small" v-model="msgFormDialog.dataCode" />
        </el-form-item>
        <el-form-item prop="dataValue" label="数据值:">
          <el-input-number clearable size="small" v-model="msgFormDialog.dataValue" />
        </el-form-item>
        <el-form-item prop="remark" label="备注:">
          <el-input clearable size="small" v-model="msgFormDialog.remark" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueDialog('fromRef')">确 定</el-button>
        <el-button @click="visibleDialog=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryDataPage,
  delById,
  editById,
  homeDataSave,
  getById,
} from "@/api/portalMangement/indexDataManagement";

export default {
  data() {
    return {
      // 遮罩层
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        remark: "",
      },
      tableData: [],
      total: 0,
      SDKClassify: [],
      //已勾选记录
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      visibleDialog: false,
      msgFormDialog: {
        dataCode: "",
        dataValue: "",
        remark: "",
      },
      baseFormRules: {
        dataCode: [
          { required: true, message: "请输入数据编号 ", trigger: "blur" },
        ],
        dataValue: [
          { required: true, message: "请输入数据值 ", trigger: "blur" },
        ],
        remark: [{ required: true, message: "请输入描述 ", trigger: "blur" }],
      },
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
    getapiSys(row) {
      let dictLabel;
      this.SDKClassify.forEach((element) => {
        if (element.dictValue == row.sdkType) {
          dictLabel = element.dictLabel;
        }
      });
      return dictLabel;
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
        this.msgFormDialog = row;
      } else {
        this.dialogTitle = "新增";
        this.msgFormDialog = {
          dataCode: "",
          dataValue: "",
          remark: "",
        };
      }
      this.visibleDialog = true;
    },
    deleteRow(row) {
      this.$confirm("确认要删除" + row.dataCode + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delById({ id: row.id }).then((response) => {
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.handleQuery();
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

    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogTitle == "编辑") {
            editById(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "编辑成功",
              });
              this.visibleDialog = false;
              this.getList();
            });
          } else {
            homeDataSave(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "增加成功",
              });
              this.visibleDialog = false;
              this.handleQuery();
            });
          }
        }
      });
    },
  },
};
</script>


