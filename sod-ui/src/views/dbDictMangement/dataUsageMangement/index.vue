<template>
  <div class="app-container">
    <!-- 数据用途管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="数据用途ID:">
        <el-input size="small" v-model="queryParams.logic_id" placeholder="请输入数据用途ID" />
      </el-form-item>
      <el-form-item label="用途描述:">
        <el-input size="small" v-model="queryParams.logic_name" placeholder="请输入中文简称" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
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
          @click="outDataBasePhysicss('逻辑库定义导出')"
          icon="el-icon-download"
          size="small"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <el-table-column align="center" prop="logicFlag" label="数据用途ID"></el-table-column>
      <el-table-column align="center" prop="logicName" label="用途描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column
        align="center"
        prop="storage_type_name"
        label="表类型"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="logic_desc"
        label="用途说明"
        width="360"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="database_name"
        label="数据库名称"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column align="center" prop="create_time" label="创建时间">
        <!-- <template slot-scope="scope">
            <span>{{scope.row.create_time.split('.')[0]}}</span>
        </template>-->
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
    <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog">
      <dataUsageEdit v-if="msgFormDialog" :handleMsgObj="handleMsgObj" @cancelDialog="cancelDialog"></dataUsageEdit>
    </el-dialog>
  </div>
</template>

<script>
import { queryDataBaseLogicAll } from "@/api/dbDictMangement/dataUsageMangement";
import dataUsageEdit from "@/views/dbDictMangement/dataUsageMangement/dataUsageEdit";
export default {
  components: {
    dataUsageEdit
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_fcst_ele: ""
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
      queryDataBaseLogicAll(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    showDialog(type) {
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
    handleSelectionChange(val) {
      this.choserow = val;
    },
    deleteCell() {},
    tableExoprt() {},
    outDataBasePhysicss() {},
    cancelDialog() {}
  }
};
</script>
