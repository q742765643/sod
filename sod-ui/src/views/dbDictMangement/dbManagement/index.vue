<template>
  <div class="app-container">
    <!-- 数据库管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据库ID:" prop="id">
        <el-input clearable size="small" v-model.trim="queryParams.id" placeholder="请输入数据库ID" />
      </el-form-item>
      <el-form-item label="数据库名称:" prop="databaseName">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.databaseName"
          placeholder="请输入数据库名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
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
      @selection-change="handleSelectionChange"
      ref="multipleTable"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="id" label="数据库ID" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="databaseName" label="数据库名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="databaseInstance" label="数据库实例"></el-table-column>
      <el-table-column prop="databaseType" label="数据库类型"></el-table-column>
      <el-table-column prop="databaseIp" label="IP地址"></el-table-column>
      <el-table-column prop="mainBakType" label="主备类型" :formatter="getMainBak"></el-table-column>
      <el-table-column prop="databaseCapacity" label="存储容量" width="100"></el-table-column>
      <el-table-column prop="display_control" label="显示控制" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.userDisplayControl == 1">显示</span>
          <span v-if="scope.row.userDisplayControl == 2">不显示</span>
          <span v-if="scope.row.userDisplayControl == 3">前端不显示</span>
        </template>
      </el-table-column>
      <el-table-column prop="checkConn" label="运行状态" width="100">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.checkConn=='1'?true:false"
            type="text"
            size="mini"
            icon="fa fa-chain"
          >连接正常</el-button>
          <el-button
            v-if="scope.row.checkConn!='1'?true:false"
            @click="checkError(scope.row)"
            type="text"
            size="mini"
            icon="fa fa fa-chain-broken"
          >连接异常</el-button>
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
      <handleDataBase
        v-if="msgFormDialog"
        :handleMsgObj="handleMsgObj"
        @cancelDialog="cancelDialog"
      ></handleDataBase>
    </el-dialog>
  </div>
</template>

<script>
import {
  defineList,
  delList,
  delByIds,
  conStatus,
  exportTable,
} from "@/api/dbDictMangement/dbManagement";
import handleDataBase from "@/views/dbDictMangement/dbManagement/databaseEdit";
import "font-awesome/css/font-awesome.css";

export default {
  components: {
    handleDataBase,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: "",
        databaseName: "",
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
      handleMsgObj: {},
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
    // 导出
    handleExport() {
      exportTable(this.queryParams).then((res) => {
        this.downloadfileCommon(res);
      });
    },
    cancelDialog() {
      this.msgFormDialog = false;
      if (this.dialogTitle && this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
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
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      defineList(this.queryParams).then((response) => {
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
        this.handleMsgObj = {};
        this.msgFormDialog = true;
      } else {
        if (this.multipleSelection.length != 1) {
          this.$message.error("请选择一条数据!");
          return;
        }
        this.dialogTitle = "编辑";
        this.handleMsgObj = this.multipleSelection[0];
        this.msgFormDialog = true;
      }
    },
    beforeAvatarUpload(file) {
      const isJSON = file.type === "application/json";
      if (!isJSON) {
        this.$message.error("导入文件只能是 JSON 格式!");
      }
    },
    handleAvatarSuccess(res, file) {
      if (res.returnCode == 0) {
        this.$message({
          type: "success",
          message: "导入成功",
        });
        this.getList();
      } else {
        this.$message({
          type: "error",
          message: "导入失败",
        });
      }
      this.$refs.upload.clearFiles();
    },

    deleteCell() {
      if (this.multipleSelection.length > 0) {
        this.$confirm("数据删除后将无法恢复,确认删除?", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let ids = [];
            this.multipleSelection.forEach((element) => {
              ids.push(element.id);
            });
            delByIds({ ids: ids.join(",") }).then((response) => {
              if (response.code == 200) {
                this.$message({ message: "删除成功", type: "success" });
                this.handleQuery();
              }
            });
          })
          .catch(() => {});
      } else {
        this.$message.error("请选择需要删除的数据库！");
      }
    },

    checkError(row) {
      conStatus({ id: row.id }).then((res) => {
        if (res.data.checkConn == 1) {
          this.$message({ message: "连接正常", type: "success" });
        } else {
          this.$message({ message: "连接异常", type: "error" });
        }
      });
    },
    //状态转换
    getMainBak(row) {
      let result = row.mainBakType == "1" ? "主库" : "备份库";
      return result;
    },

    // 表格选择
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
  },
};
</script>

