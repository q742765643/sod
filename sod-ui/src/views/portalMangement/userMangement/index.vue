<template>
  <div class="app-container">
    <!--portal 系统管理 用户管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item prop="userName" label="用户姓名:">
        <el-input clearable size="small" v-model="queryParams.userName" placeholder="请输入用户姓名" />
      </el-form-item>
      <el-form-item prop="loginName" label="登录名:">
        <el-input clearable size="small" v-model="queryParams.loginName" placeholder="请输入登录名" />
      </el-form-item>
      <el-form-item prop="userLevel" label="用户级别:">
        <el-select v-model="queryParams.userLevel" size="small" style="width: 140px">
          <el-option label="全部" value></el-option>
          <el-option label="国家级" value="01"></el-option>
          <el-option label="非国家级" value="02"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="ischeck" label="用户状态:">
        <el-select v-model="queryParams.ischeck" size="small" style="width: 140px">
          <el-option label="全部" value></el-option>
          <el-option label="未审核" value="0"></el-option>
          <el-option label="已审核" value="1"></el-option>
          <el-option label="已驳回" value="2"></el-option>
        </el-select>
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
      <el-table-column type="index" label="序号" width="80" :index="table_index"></el-table-column>
      <el-table-column prop="loginName" label="登录名" width="120"></el-table-column>
      <el-table-column prop="userName" label="用户姓名" width="120"></el-table-column>
      <el-table-column prop="userLevel" label="用户级别" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.userLevel=='01'">国家级</span>
          <span v-if="scope.row.userLevel=='02'">非国家级</span>
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门"></el-table-column>
      <el-table-column prop="email" label="邮箱地址"></el-table-column>
      <el-table-column prop="phone" label="电话号码"></el-table-column>
     <!-- <el-table-column prop="lastLoginTime" label="最后登录时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLoginTime) }}</span>
        </template>
      </el-table-column>-->
      <el-table-column prop="ischeck" label="状态">
        <template slot-scope="scope">
          <span v-if="scope.row.ischeck=='0'">未审核</span>
          <span v-if="scope.row.ischeck=='1'">已审核</span>
          <span v-if="scope.row.ischeck=='2'">已驳回</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="showDialog(scope.row)"
          >查看</el-button>
          <el-button
            icon="el-icon-coordinate"
            size="mini"
            type="text"
            @click="auditRow(scope.row)"
          >审核</el-button>
          <el-button
            icon="el-icon-refresh"
            size="mini"
            type="text"
            @click="refreshPassword(scope.row)"
          >重置密码</el-button>
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
      <handleUser
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></handleUser>
    </el-dialog>
  </div>
</template>

<script>
import { queryDataPage, delById, editById,resetPwd } from "@/api/portalMangement/userMangement";
import handleUser from "@/views/portalMangement/userMangement/handleUser";

export default {
  components: {
    handleUser,
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: "",
        loginName:"",
        userLevel:"",
        ischeck:"",
      },
      tableData: [],
      total: 0,
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
        type: "warning",
      })
        .then(() => {
          let obj = {
            id: row.id,
            ischeck: '1',
          };
          editById(obj).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "审核通过成功",
              });
              this.handleQuery();
            } else {
              this.$message({
                type: "error",
                message: "审核通过失败",
              });
            }
          });
        })
        .catch((action) => {
          let obj = {
            id: row.id,
            ischeck: '2',
          };
          if (action === 'cancel'){
            editById(obj).then((response) => {
              if (response.code == 200) {
                this.$message({
                  type: "success",
                  message: "驳回成功",
                });
                this.handleQuery();
              } else {
                this.$message({
                  type: "error",
                  message: "驳回失败",
                });
              }
            });
          }
        });
    },
    refreshPassword(row) {
      this.$confirm("确定要继续当前操作码?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          resetPwd({ id: row.id,password:'123qweasdzxc' }).then((response) => {
            if (response.code == 200) {
              this.$alert("重置密码成功，默认密码为123qweasdzxc", "温馨提示", {
                confirmButtonText: "确定",
                callback: (action) => {},
              });
            }else{
              this.$alert("重置密码失败", "温馨提示", {
                confirmButtonText: "确定",
                callback: (action) => {},
              });
            }

          });
        })
        .catch(() => {});
    },
    deleteRow(row) {
      this.$confirm("确认要删除" + row.userName + "吗?", "温馨提示", {
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


