<template>
  <div class="app-container">
    <!-- 数据库管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
       <el-form-item label="数据库ID:">
          <el-input size="small"  v-model="queryParams.database_id_search" placeholder="请输入数据库ID" />
        </el-form-item>
        <el-form-item label="数据库名称:">
          <el-input size="small"  v-model="queryParams.database_name_search" placeholder="请输入数据库名称" />
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
            @click="outDataBasePhysicss('物理库定义导出')"
            icon="el-icon-download"
            size="small"
          >导出</el-button>
      </el-col>
       
    </el-row>

    <el-table v-loading="loading" :data="tableData" row-key="id" @selection-change="handleSelectionChange">
       <el-table-column  align="center" type="selection" width="50"></el-table-column>
        <el-table-column align="center" prop="database_id"  label="数据库ID"></el-table-column>
        <el-table-column align="center" prop="database_name"  label="数据库名称" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column align="center" prop="database_instance" label="数据库实例"></el-table-column>
        <el-table-column align="center" prop="database_type" label="数据库类型"></el-table-column>
        <el-table-column align="center" prop="database_ip" label="IP地址"></el-table-column>
        <el-table-column align="center" prop="mainbak_type" label="主备类型" :formatter="getMainBak"></el-table-column>
        <el-table-column align="center" prop="database_capacity" label="存储容量"></el-table-column>
        <el-table-column align="center" prop="display_control" label="显示控制" :formatter="getDisplyControl"></el-table-column>
        <el-table-column align="center" prop="check_conn" label="运行状态">
          <template slot-scope="scope">
            <el-link
              v-if="scope.row.check_conn==='1'?true:false"
              type="primary"
              style="color: #20b572;"
            >
              <span>连接正常</span>
            </el-link>
            <el-link
              v-if="scope.row.check_conn!=='1'?true:false"
              @click="checkError(scope.row)"
              style="color: red;"
            >
              <span>连接异常</span>
            </el-link>
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
     <el-dialog :title="dialogTitle" :visible.sync="msgFormDialog">
      <handleDataBase v-if="msgFormDialog" :handleMsgObj="handleMsgObj" @cancelDialog="cancelDialog"></handleDataBase>
    </el-dialog>
    

  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
import handleDataBase from "@/views/dbDictMangement/dbManagement/databaseEdit";
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
        user_fcst_ele:'',
      },
      tableData:[],
      total:0,
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
      handleMsgObj:{}
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
    outDataBasePhysicss(){},
    cancelDialog(){},
    checkError(){},
  }
};
</script>
