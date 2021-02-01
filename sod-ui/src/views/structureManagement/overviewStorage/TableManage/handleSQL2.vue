<template>
  <section class="handleSQLDialog">
    <el-form ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-button
        style="margin-bottom: 20px"
        size="small"
        @click="createBase"
        type="primary"
        icon="el-icon-first-aid-kit"
        plain
        >创建物理表</el-button
      >
      <el-input
        type="textarea"
        size="small"
        v-model.trim="msgFormDialog.createSql"
        readonly
      ></el-input>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <!-- <el-button type="primary" @click="trueDialog('ruleForm')" size="small">确 定</el-button> -->
    </div>
  </section>
</template>

<script>
import {
  getSql,
  createTable,
  existTable,
} from "@/api/structureManagement/materialManage/handleSQL";

export default {
  name: "handleSQLDialog",
  props: {
    handleSQLObj: {
      type: Object,
    },
  },
  data() {
    return {
      msgFormDialog: {
        createSql: "",
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      let obj = {
        databaseId: this.handleSQLObj.databaseId,
        tableId: this.handleSQLObj.id,
      };
      getSql(obj).then((res) => {
        this.msgFormDialog.createSql =
          res.data.createSql == "null" ? "" : res.data.createSql;
      });
    },
    createBase() {
      if (!this.msgFormDialog.createSql) {
        this.$message({
          message: "SQL不能为空",
          type: "error",
        });
        return;
      }
      existTable({
        databaseId: this.handleSQLObj.databaseId,
        tableName: this.handleSQLObj.tableName,
      })
        .then((res) => {
          if (res.data) {
            this.$message({
              message: "表已存在",
              type: "error",
            });
            return;
          } else if (res.data == null) {
            this.$message({
              message: res.msg,
              type: "error",
            });
          } else {
            createTable({
              databaseId: this.handleSQLObj.databaseId,
              tableName: this.handleSQLObj.tableName,
              tableSql: this.msgFormDialog.createSql,
            }).then((res) => {
              if (res.code == 200) {
                this.$message({
                  message: "创建成功",
                  type: "success",
                });
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          }
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss">
.handleSQLDialog {
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
  .btnColBox {
    padding-top: 4px;
  }
  .el-textarea__inner {
    min-height: 300px !important;
  }
}
</style>
