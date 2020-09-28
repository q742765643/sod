<template>
  <div class="app-container">
    <!-- SDK管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item clearable label="SDK类型:" prop="sdkType">
        <el-select v-model="queryParams.sdkType">
          <el-option label="全部" value></el-option>
          <el-option
            :label="item.dictLabel"
            :value="item.dictValue"
            v-for="(item,index) in SDKClassify"
            :key="index"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          size="small"
          type="primary"
          @click="showDialog('add')"
          icon="el-icon-upload2"
        >上传SDK</el-button>
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
      <el-table-column prop="sdkType" label="SDK类型" :formatter="getapiSys"></el-table-column>
      <el-table-column prop="sdkLang" label="开发语言" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="sdkSys" label="操作系统"></el-table-column>
      <el-table-column prop="sdkJarName" label="SDK包" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="sdkDocName" label="SDK文档" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="sdkDesc" label="SDK描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="address" label="操作" width="150" :show-overflow-tooltip="true">
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
      :visible.sync="msgFormDialog"
      v-dialogDrag
      top="5vh"
    >
      <handleSDK
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></handleSDK>
    </el-dialog>
  </div>
</template>

<script>
import { queryDataPage, delById } from "@/api/portalMangement/SDKManagement";
import handleSDK from "@/views/portalMangement/SDKManagement/handleSDK";

export default {
  components: {
    handleSDK,
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
        this.handleObj = row;
      } else {
        this.dialogTitle = "新增";
        this.handleObj = {};
      }
      this.msgFormDialog = true;
    },
    deleteRow(row) {
      this.$confirm("确认要删除" + row.sdkType + "吗?", "温馨提示", {
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


