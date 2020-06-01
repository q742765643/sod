<template>
  <div class="app-container sodDeploy">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="queryParams" ref="modelForm" class="searchBox">
      <el-form-item label="角色名称">
        <el-input size="small" v-model.trim="queryParams.name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="searchFun" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" icon="el-icon-plus" @click="addCell">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>
    <!-- 列表 -->
    <el-table border ref="singleTable" :data="tableData" stripe style="width: 100%;">
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="角色名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="roleKey" label="角色KEY"></el-table-column>
      <el-table-column prop="sectionName" label="所属科室"></el-table-column>
      <el-table-column prop="userPhone" label="是否禁用">
        <template slot-scope="scope">
          <el-link type="primary" :underline="false" v-if="scope.row.enable=='1'">正常</el-link>
          <el-link type="danger" :underline="false" v-else>禁用</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="200px">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-check"
            @click="powerCell(scope.row)"
          >分配权限</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit" @click="editCell(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="searchFun"
    />
    <!-- 添加、编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="dialogTitle"
      :visible.sync="handleRoleDialog"
      width="650px"
    >
      <handleRole
        v-if="handleRoleDialog"
        :handleObj="handleObj"
        @cancelHandle="cancelHandle"
        ref="myHandleServer"
      />
    </el-dialog>
    <!-- 分配角色 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="分配角色"
      :visible.sync="handlePowerDialog"
      width="650px"
    >
      <handlePower @cancelHandle="cancelHandle" v-if="handlePowerDialog" :handleObj="handleObj"></handlePower>
    </el-dialog>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// //分页组件
// import Pagination from "@/components/Pagination";
// 增加查看弹框
import handleRole from "@/views/userManagement/roleManagement/handleRole";
// 分配角色弹窗
import handlePower from "@/views/userManagement/roleManagement/handlePower";

export default {
  name: "sodDeploy",
  components: {
    handleRole,
    handlePower
  },
  data() {
    return {
      //查询条件
      queryParams: {
        name: "",
        sort: "name",
        order: "asc"
      },
      //表格
      tableData: [],
      total: 0,
      // 弹框
      handleRoleDialog: false,
      dialogTitle: "新增",
      handleObj: {},
      handlePowerDialog: false
    };
  },
  created() {},
  mounted() {
    // this.searchFun();
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    // 审核状态发生改变
    statusChange() {
      this.searchFun("1");
    },
    //根据条件查询数据
    searchFun(searchPageNum) {
      if (searchPageNum) {
        this.$refs.pagination.paginateObj.page = 1;
      }
      let paginateObj = this.$refs.pagination.paginateObj;
      this.queryParams = { ...this.queryParams, ...paginateObj };
      this.axios
        .post(interfaceObj.roleManageApi_queryRole, this.queryParams)
        .then(res => {
          this.tableData = res.data.data;
          this.total = res.data.total;
        });
    },
    // 新增
    addCell() {
      this.handleObj = {};
      this.dialogTitle = "新增";
      this.handleRoleDialog = true;
    },
    // 分配权限
    powerCell(row) {
      this.dialogTitle = "分配权限";
      this.handleObj = row;
      this.handlePowerDialog = true;
    },
    // 查看
    editCell(row) {
      this.dialogTitle = "编辑";
      this.handleObj = row;
      this.handleRoleDialog = true;
    },

    // 删除
    deleteCell() {
      let chosedRows = this.$refs.singleTable.selection;
      console.log(chosedRows);
      if (chosedRows.length > 0) {
        let ids = [];
        chosedRows.forEach(item => {
          if (item.name == "超级管理员") {
            return;
          } else {
            ids.push(item.id);
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
                .post(interfaceObj.roleManageApi_deleteById, {
                  roleId: ids.join(",")
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
                  this.searchFun("1");
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
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.handleRoleDialog = false;
      this.handlePowerDialog = false;
      this.searchFun();
    }
  }
};
</script>

<style lang="scss">
.sodDeploy {
  .el-link.el-link--default {
    font-size: 12px;
    color: #409eff;
  }
  .handleSearchBtn {
    float: right;
  }
  .el-link {
    font-size: 12px;
  }
}
</style>
