<template>
  <section class="handleRoleDialog">
    <el-alert title="您可以选择角色分配给当前选定的用户" type="info" :closable="false"></el-alert>
    <el-row>
      <el-col :span="4">用户名:</el-col>
      <el-col :span="20">{{this.handleObj.userName}}</el-col>
    </el-row>
    <el-row>
      <el-col :span="4">所属角色:</el-col>
      <el-col :span="20">
        <el-link type="primary" :underline="false">{{this.handleObj.roleName}}</el-link>
      </el-col>
    </el-row>
    <el-table
      :data="tableData"
      @current-change="handleSelectionChange"
      @select="selectSingleTable"
      ref="singleTable"
      highlight-current-row
      border
      stripe
      style="width: 100%;"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="角色名"></el-table-column>
      <el-table-column prop="enable" label="是否禁用">
        <template slot-scope="scope">
          <el-link type="primary" :underline="false" v-if="scope.row.enable=='1'">正常</el-link>
          <el-link type="danger" :underline="false" v-else>禁用</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
    </el-table>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog()">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "handleRoleDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      //表格
      tableData: [],
      choserow: []
    };
  },
  created() {
    // this.initDetail();
  },
  methods: {
    initDetail() {
      this.axios
        .post(interfaceObj.userManage_userRole, {
          userId: this.handleObj.userId
        })
        .then(res => {
          let resdata = res.data.roles;
          // 后台数据返回后,手动添加一个checked属性控制选中与否
          resdata.forEach(item => {
            item.checked = false;
          });
          this.tableData = resdata;
        });
    },
    // 表格单选
    // 单选
    handleSelectionChange(val) {
      this.choserow = val;
      this.$refs.singleTable.clearSelection();
      this.$refs.singleTable.toggleRowSelection(val, true);
    },
    selectSingleTable(selection, row) {
      if (selection.length > 0) {
        this.$refs.singleTable.setCurrentRow(row);
        this.$refs.singleTable.clearSelection();
        this.$refs.singleTable.toggleRowSelection(row, true);
        this.choserow = row;
      } else {
        this.choserow = null;
        this.$refs.singleTable.setCurrentRow([]);
        this.$refs.singleTable.clearSelection();
      }
    },

    trueDialog() {
      if (this.choserow != "") {
        debugger;
        if (this.handleObj.userName != "admin" && this.choserow.id == "3") {
          this.$message({
            message: "普通用户不能授予【超级管理员】角色",
            type: "warning"
          });
          return;
        }
        this.axios
          .post(interfaceObj.userManage_allocation, {
            userId: this.handleObj.userId,
            roleId: this.choserow.id
          })
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({ message: "分配成功", type: "success" });
              this.$emit("handleDialogClose");
            } else {
              this.$message({
                type: "error",
                message: res.data.returnMessage
              });
            }
          });
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    cancelDialog() {
      this.$emit("handleDialogClose");
    }
  }
};
</script>

<style lang="scss">
.handleRoleDialog {
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
  .el-textarea__inner {
    width: 98%;
  }
  .el-select {
    width: 100%;
  }
  .el-row {
    line-height: 30px;
    border: 1px solid #dcdfe6;
    border-bottom: none;
    .el-col-4 {
      text-align: right;
      padding-right: 6px;
      border-right: 1px solid #dcdfe6;
    }
    .el-col-20 {
      padding-left: 6px;
    }
  }
}
</style>
