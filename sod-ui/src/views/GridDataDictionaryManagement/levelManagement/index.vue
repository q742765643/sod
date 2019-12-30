<template>
  <div class="app-container">
    <!-- 层次属性 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
       <el-form-item size="small"  label="GRIB格式:">
          <el-input v-model="queryParams.grib_version" placeholder="请输入GRIB格式" />
        </el-form-item>
        <el-form-item size="small"  label="层次类型:">
          <el-input v-model="queryParams.level_type" placeholder="请输入层次类型" />
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
       <el-button size="small" type="warning" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" row-key="id" @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50"></el-table-column>
        <el-table-column align="center" prop="grib_version" label="GRIB版本" ></el-table-column>
        <el-table-column align="center" prop="level_type"  label="层次类型"></el-table-column>
        <el-table-column align="center" prop="level_code"  label="层次代码"></el-table-column>
        <el-table-column align="center" prop="scale_divisor" label="比例因子"></el-table-column>
        <el-table-column align="center"
          prop="level_properity"
          label="英文描述"
          width="360"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column align="center" prop="level_name" label="中文描述" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column align="center" prop="unit" label="单位"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

     <!-- 弹窗-->
    <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog" width="45%">
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="grib格式:" prop="grib_version">
          <el-input v-model.number="ruleForm.grib_version" />
        </el-form-item>
        <el-form-item label="层次类型:" prop="level_type">
          <el-input v-model.number="ruleForm.level_type" />
        </el-form-item>
        <el-form-item label="层次代码:" prop="level_code">
          <el-input v-model="ruleForm.level_code" />
        </el-form-item>
        <el-form-item label="比例因子:" prop="scale_divisor">
          <el-input v-model.number="ruleForm.scale_divisor" />
        </el-form-item>
        <el-form-item label="英文描述:" prop="level_properity">
          <el-input v-model="ruleForm.level_properity" />
        </el-form-item>
        <el-form-item label="中文描述:" prop="level_name">
          <el-input v-model="ruleForm.level_name" />
        </el-form-item>
        <el-form-item label="单位:" prop="unit">
          <el-input v-model="ruleForm.unit" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleTrue('ruleForm')">确 定</el-button>
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>
    

  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
export default {
  data() {
    return {
     // 遮罩层
      loading: true,
      queryParams: {
         pageNum: 1,
        pageSize: 10,
        user_fcst_ele:'',
      },
      // 导入
      actionUrl:'',      
      showFile: false,
      importHeaders: {
        enctype: "multipart/form-data"
      },
      choserow:[],
      // 弹窗
      dialogTitle:'',
      msgFormDialog:false,
      ruleForm:{},
    };
  },
  created() {
    this.getList();
  },
  methods: {
     // table自增定义方法
    table_index(index) {
      return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    showDialog(type){
      this.msgFormDialog = true;
    },
    handleTrue(){

    },
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
    deleteCell(){},
    tableExoprt(){},
    superClick(){}
    
  }
};
</script>
