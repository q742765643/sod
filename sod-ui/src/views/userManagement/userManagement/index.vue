<template>
  <div class="userManagement app-container">
    <el-form :inline="true" class="funLoad selSearchCon">
      <el-form-item label="用户名：">
        <el-input size="small" v-model.trim="search"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="DataBaseExpandForm('')"
        >查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button size="small" type="primary" icon="el-icon-plus" @click="addFun()">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteFun()">删除</el-button>
      </el-col>
    </el-row>
    <!-- 列表 -->
    <el-table
      ref="singleTable"
      :data="tableData"
      stripe
      :default-sort="{prop: 'database_name', order: 'ascending'}"
    >
      <el-table-column type="index" label=" " min-width="20" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userName" label="用户名"></el-table-column>
      <el-table-column prop="userRealname" label="真实姓名"></el-table-column>
      <el-table-column prop="roleName" label="所属角色"></el-table-column>
      <el-table-column prop="userSex" label="用户性别"></el-table-column>
      <el-table-column prop="userMail" label="用户邮箱"></el-table-column>
      <el-table-column prop="lastLogintime" label="最近一次登录时间"></el-table-column>
      <el-table-column prop="address" label="操作" width="180">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-tickets"
            type="text"
            size="mini"
            @click="handleRole(scope.row)"
          >分配角色</el-button>
          <el-button icon="el-icon-edit" type="text" size="mini" @click="editCell(scope.row)">编辑</el-button>
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
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="addOrEditorTitle"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <userEdit @handleDialogClose="handleDialogClose" v-if="dialogVisible" :handleObj="handleObj"></userEdit>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="分配角色"
      :visible.sync="roleDialog"
      width="60%"
    >
      <roleEdit @handleDialogClose="handleDialogClose" v-if="roleDialog" :handleObj="handleObj"></roleEdit>
    </el-dialog>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// //分页组件
// import Pagination from "@/components/Pagination";
// //下载组件
// import { testExport } from "@/components/common.js";
// // 添加
import userEdit from "@/views/userManagement/userManagement/userEdit";
// 分配角色
import roleEdit from "@/views/userManagement/userManagement/handleRole";
export default {
  name: "userManagement",
  components: {
    userEdit,
    roleEdit
  },
  data() {
    return {
      queryParams: {
        userName: "",
        sort: "lastLogintime",
        order: "desc"
      },
      pageObj: {},
      search: "",
      //表格
      tableData: [],
      dataTotal: 0,
      addOrEditorTitle: "",
      dialogVisible: false,
      roleDialog: false,
      handleObj: {}
    };
  },
  mounted() {
    // this.DataBaseExpandForm();
  },
  methods: {
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    //查询用户列表
    DataBaseExpandForm(searchMsg) {
      if (searchMsg == "") {
        this.$refs.pagination.paginateObj.page = 1;
      }
      let paginateObj = this.$refs.pagination.paginateObj;
      this.queryParams = { ...this.queryParams, ...paginateObj };
      // this.queryParams.page = 1;
      this.queryParams.userName = this.search;
      this.axios
        .post(interfaceObj.userManage_queryUser, this.queryParams)
        .then(res => {
          this.tableData = res.data.data;
          this.dataTotal = res.data.total;
        });
    },

    //添加案件
    addFun() {
      this.handleObj = {};
      this.dialogVisible = true;
      this.addOrEditorTitle = "添加";
    },
    //分页事件
    paginationChange() {
      this.DataBaseExpandForm();
    },
    handleDialogClose() {
      this.dialogVisible = false;
      this.roleDialog = false;
      this.handleObj = {};
      this.DataBaseExpandForm("");
    },
    //删除按钮
    deleteFun() {
      let chosedRows = this.$refs.singleTable.selection;
      console.log(chosedRows);
      if (chosedRows.length > 0) {
        let ids = [];
        chosedRows.forEach(item => {
          if (item.user_role_id == "3") {
            return;
          } else {
            ids.push(item.userId);
          }
        });
        if (ids.length > 0 && ids.length == chosedRows.length) {
          this.$confirm("数据删除后将无法恢复，确认删除?", "温馨提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              this.axios
                .post(interfaceObj.userManage_deleteUser, {
                  ids: ids.join(",")
                })
                .then(res => {
                  if (res.data.returnCode == 0) {
                    this.$message({
                      type: "success",
                      message: "删除成功"
                    });
                  } else {
                    this.$message({
                      type: "error",
                      message: res.data.returnMessage
                    });
                  }
                  this.DataBaseExpandForm("");
                });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消删除"
              });
            });
        } else {
          this.$message({
            type: "error",
            message: "超级管理员不能删除"
          });
        }
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    // 分配角色
    handleRole(row) {
      this.handleObj = row;
      this.roleDialog = true;
    },
    // 编辑
    editCell(row) {
      this.handleObj = row;
      this.dialogVisible = true;
      this.addOrEditorTitle = "编辑";
    }
  }
};
</script>

<style lang="scss">
.userManagement {
  .button_item {
    float: right;
  }
}
</style>