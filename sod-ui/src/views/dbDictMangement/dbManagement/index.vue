<template>
  <div class="app-container">
    <!-- 数据库管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据库ID:">
        <el-input size="small" v-model="queryParams.id" placeholder="请输入数据库ID" />
      </el-form-item>
      <el-form-item label="数据库名称:">
        <el-input size="small" v-model="queryParams.databaseName" placeholder="请输入数据库名称" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          @click="outDataBasePhysicss('物理库定义导出')"
          icon="el-icon-download"
          size="small"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table
      class="unChoseAll"
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @current-change="handleSelectionChange"
      @select="selectSingleTable"
      ref="singleTable"
      highlight-current-row
    >
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <el-table-column align="center" prop="id" label="数据库ID"></el-table-column>
      <el-table-column
        align="center"
        prop="databaseName"
        label="数据库名称"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column align="center" prop="databaseInstance" label="数据库实例"></el-table-column>
      <el-table-column align="center" prop="databaseType" label="数据库类型"></el-table-column>
      <el-table-column align="center" prop="databaseIp" label="IP地址"></el-table-column>
      <el-table-column align="center" prop="mainBakType" label="主备类型" :formatter="getMainBak"></el-table-column>
      <el-table-column align="center" prop="databaseCapacity" label="存储容量"></el-table-column>
      <el-table-column
        align="center"
        prop="display_control"
        label="显示控制"
        :formatter="getDisplyControl"
      ></el-table-column>
      <el-table-column align="center" prop="check_conn" label="运行状态">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.check_conn==='1'?true:false"
            type="text"
            size="mini"
            icon="fa fa-chain"
          >连接正常</el-button>
          <el-button
            v-if="scope.row.check_conn!=='1'?true:false"
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
    <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog" v-dialogDrag>
      <handleDataBase
        v-if="msgFormDialog"
        :handleMsgObj="handleMsgObj"
        @cancelDialog="cancelDialog"
      ></handleDataBase>
    </el-dialog>
  </div>
</template>

<script>
import { defineList, delList } from "@/api/dbDictMangement/dbManagement";
import handleDataBase from "@/views/dbDictMangement/dbManagement/databaseEdit";
import "font-awesome/css/font-awesome.css";
export default {
  components: {
    handleDataBase
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: "",
        databaseName: ""
      },
      tableData: [],
      total: 0,
      // 导入
      actionUrl: "",
      showFile: false,
      importHeaders: {
        enctype: "multipart/form-data"
      },
      choserow: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      ruleForm: {},
      handleMsgObj: {}
    };
  },
  created() {
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
    /** 查询列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      defineList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.dialogTitle = "添加";
        this.handleMsgObj = null;
      } else {
        this.dialogTitle = "编辑";
      }

      this.msgFormDialog = true;
    },
    handleTrue() {},
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.msgFormDialog = false;
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
          message: "导入成功"
        });
        this.getList();
      } else {
        this.$message({
          type: "error",
          message: "导入失败"
        });
      }
      this.$refs.upload.clearFiles();
    },

    deleteCell() {
      if (this.choserow && this.choserow.id) {
        this.$confirm("数据删除后将无法恢复,确认删除?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            delList({ id: this.choserow.id }).then(response => {
              this.getList();
            });
          })
          .catch(() => {});
      } else {
        this.$message.error("请选择需要删除的数据库！");
      }
    },
    tableExoprt() {},
    outDataBasePhysicss() {},
    cancelDialog() {},
    checkError() {},
    //状态转换
    getMainBak(row) {
      let result = row.mainbak_type == "1" ? "主库" : "备份库";
      return result;
    },
    getDisplyControl(row) {
      // if (row.display_control == "1" && row.user_display_control == "1") {
      //   return "显示";
      // } else if (row.display_control == "2") {
      //   return "不显示";
      // } else if (
      //   row.display_control == "1" &&
      //   row.user_display_control == "2"
      // ) {
      //   return "前端不显示";
      // }没有display_control这个参数，暂时都显示
      return "显示";
    },
    // 单选
    handleSelectionChange(val) {
      this.choserow = val;
      this.$refs.singleTable.clearSelection();
      this.$refs.singleTable.toggleRowSelection(val, true);
    },
    selectSingleTable(selection, row) {
      if (selection.length > 0) {
        this.$refs.singleTable.setchoserow(row);
        this.$refs.singleTable.clearSelection();
        this.$refs.singleTable.toggleRowSelection(row, true);
        this.choserow = row;
      } else {
        this.choserow = null;
        this.$refs.singleTable.setchoserow([]);
        this.$refs.singleTable.clearSelection();
      }
    }
  }
};
</script>

