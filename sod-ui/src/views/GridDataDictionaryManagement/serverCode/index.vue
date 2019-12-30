<template>
  <div class="app-container">
    <!-- 服务代码定义 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="格点要素服务代码:">
          <el-input size="small" v-model="queryParams.user_fcst_ele" />
        </el-form-item>
        <el-form-item label="索引表存储代码:">
          <el-input size="small" v-model="queryParams.db_fcst_ele" />
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

    <el-table v-loading="loading" :data="tableData" row-key="id">
        <el-table-column align="center" type="selection" width="50"></el-table-column>
        <el-table-column align="center" prop="user_fcst_ele"  label="要素服务代码"></el-table-column>
        <el-table-column align="center" prop="db_fcst_ele"  label="要素存储短名"></el-table-column>
        <el-table-column align="center" prop="ele_name" label="中文名称"></el-table-column>
        <el-table-column align="center" prop="ele_property_name" label="属性名(要素长名)"></el-table-column>
        <el-table-column align="center" prop="ele_unit" label="要素单位"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

     <!-- 弹窗-->
    <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog" width="50%">
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="要素服务代码:" prop="user_fcst_ele">
          <el-input v-model="ruleForm.user_fcst_ele" placeholder="请输入要素服务代码" />
        </el-form-item>
        <el-form-item label="要素存储短名:" prop="db_fcst_ele">
          <el-input v-model="ruleForm.db_fcst_ele" placeholder="请输入要素存储短名" />
        </el-form-item>
        <el-form-item label="中文名称:" prop="ele_name">
          <el-input v-model="ruleForm.ele_name" placeholder="请输入中文名称" />
        </el-form-item>
        <el-form-item label="属性名(要素长名):" prop="ele_property_name">
          <el-input v-model="ruleForm.ele_property_name" placeholder="请输入属性名(要素长名)" />
        </el-form-item>
        <el-form-item label="要素单位:" prop="ele_unit">
          <el-input v-model="ruleForm.ele_unit" placeholder="请输入要素单位" />
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
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""],
      },
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
    
  }
};
</script>
