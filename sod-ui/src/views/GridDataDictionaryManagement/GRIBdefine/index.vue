<template>
  <div class="app-container">
    <!-- grib参数定义 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
       <el-form-item label="GRIB格式:">
          <el-input size="small"  v-model="queryParams.grib_version" placeholder="请输入GRIB格式" />
        </el-form-item>
        <el-form-item label="参数编码:">
          <el-input size="small"  v-model="queryParams.parameter_id" placeholder="请输入参数编码" />
        </el-form-item>
        <el-form-item label="代码简写:">
          <el-input size="small"  v-model="queryParams.ele_code_short" placeholder="请输入代码简写" />
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button type="text" @click="superClick">
            <i class="el-icon-share"></i>高级搜索
          </el-button>
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
        <el-upload
            class="upload-demo"
            :action="actionUrl"
            multiple
            :limit="1"
            :show-file-list="showFile"
            :before-upload="beforeAvatarUpload"
            :on-success="handleAvatarSuccess"
            :header="importHeaders"
            ref="upload"
          >
            <el-button size="small" type="warning" icon="el-icon-upload2">导入</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
         <el-button size="small" type="success" @click="tableExoprt()" icon="el-icon-download">导出</el-button>
      </el-col>
      <el-col :span="1.5">
       <el-button size="small" type="warning" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" row-key="id" @selection-change="handleSelectionChange">
       <el-table-column align="center" type="selection" width="50"></el-table-column>
        <el-table-column  align="center" prop="ele_code_short"  label="要素存储短名"></el-table-column>
        <el-table-column  align="center" prop="subject_id"  label="学科"></el-table-column>
        <el-table-column  align="center" prop="classify" label="参数种类"></el-table-column>
        <el-table-column  align="center" prop="parameter_id" label="参数编码"></el-table-column>
        <el-table-column  align="center" prop="grib_version" label="GRIB版本"></el-table-column>
        <el-table-column  align="center" prop="public_config" label="是否为共有配置"></el-table-column>
        <el-table-column  align="center" prop="template_id" label="模板编号"></el-table-column>
        <el-table-column  align="center" prop="template_desc" label="模板说明"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

     <!-- 弹窗-->
    <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog" width="50%" highlight-current-row>
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="要素存储短名:" prop="ele_code_short">
          <el-input v-model="ruleForm.ele_code_short" placeholder="请输入要素存储短名" />
        </el-form-item>
        <el-form-item label="学科:" prop="subject_id">
          <el-input v-model="ruleForm.subject_id" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="参数种类:" prop="classify">
          <el-input v-model="ruleForm.classify" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="参数编码:" prop="parameter_id">
          <el-input v-model="ruleForm.parameter_id" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="GRIB版本:" prop="grib_version">
          <el-select v-model="ruleForm.grib_version">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否为共有配置:" prop="public_config">
          <el-select v-model="ruleForm.public_config">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板编号:" prop="template_id">
          <el-input v-model="ruleForm.template_id" placeholder="请输入模板ID" />
        </el-form-item>
        <el-form-item label="模板说明:" prop="template_desc">
          <el-input type="textarea" v-model="ruleForm.template_desc" />
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
