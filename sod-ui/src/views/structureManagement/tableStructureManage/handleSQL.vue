<template>
  <section class="handleSQLDialog">
    <el-form ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库类型">
            <el-input v-model="msgFormDialog.database_name" disabled size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" class="btnColBox">
          <el-button @click="createSql" size="small" type="primary" icon="el-icon-money" plain>生成SQL</el-button>
          <el-button @click="saveSql" size="small" type="primary" icon="el-icon-wallet" plain>保存SQL</el-button>
          <el-button size="small" type="primary" icon="el-icon-first-aid-kit" plain>创建物理表</el-button>
        </el-col>
      </el-row>

      <el-collapse v-model="msgFormDialog.aaa" @change="handleChange">
        <el-collapse-item title="CREATE" name="1">
          <el-input type="textarea" size="small" v-model="msgFormDialog.createSql"></el-input>
        </el-collapse-item>
        <el-collapse-item title="INSERT" name="2">
          <el-input type="textarea" size="small" v-model="msgFormDialog.insertSql"></el-input>
        </el-collapse-item>
        <el-collapse-item title="SELECT" name="3">
          <el-input type="textarea" size="small" v-model="msgFormDialog.selectSql"></el-input>
        </el-collapse-item>
      </el-collapse>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <!-- <el-button type="primary" @click="trueDialog('ruleForm')" size="small">确 定</el-button> -->
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "handleSQLDialog",
  props: {
    handleSQLObj: {
      type: Object
    }
  },
  data() {
    return {
      activeNames: ["1"],
      msgFormDialog: {
        createSql: "",
        insertSql: "",
        selectSql: "",
        database_name: this.handleSQLObj.database_name
      },
      tableObj: ""
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.axios
        .get(interfaceObj.TableStructure_getTableInfo, {
          params: {
            data_class_id: this.handleSQLObj.data_class_id,
            database_logic: this.handleSQLObj.logic_id,
            storage_type: this.handleSQLObj.storage_type
          }
        })
        .then(res => {
          this.tableObj = res.data.data[0];
        })
        .catch(error => {});
    },
    handleChange() {},
    createSql() {
      this.axios
        .get(interfaceObj.TableStructure_createSql, {
          params: {
            table_id: this.tableObj.table_id,
            table_desc: this.handleSQLObj.database_id
          }
        })
        .then(res => {
          this.msgFormDialog.createSql = res.data.data.createSql;
          this.msgFormDialog.insertSql = res.data.data.insertSql;
          this.msgFormDialog.selectSql = res.data.data.selectSql;
        })
        .catch(error => {});
    },
    saveSql() {
      if (this.msgFormDialog.createSql == "") {
        this.$message({
          message: "CREATE不能为空",
          type: "error"
        });
        return;
      } else if (this.msgFormDialog.insertSql == "") {
        this.$message({
          message: "INSERT不能为空",
          type: "error"
        });
        return;
      } else if (this.msgFormDialog.selectSql == "") {
        this.$message({
          message: "SELECT不能为空",
          type: "error"
        });
        return;
      }
      this.axios
        .get(interfaceObj.TableStructure_selectTableSql, {
          params: {
            table_name: this.tableObj.table_name,
            database_id: this.handleSQLObj.database_id
          }
        })
        .then(res => {
          debugger;
          if (res.data.data.length > 0) {
            this.$confirm(
              "此sql语句在数据库中已存在！是否删除并重新保存?",
              "温馨提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消"
                // type: "info"
              }
            )
              .then(() => {
                this.trueSave();
              })
              .catch(() => {});
          } else {
            this.trueSave();
          }
        })
        .catch(error => {});
    },
    trueSave() {
      let table_sql = {};
      table_sql.createSql = this.msgFormDialog.createSql;
      table_sql.insertSql = this.msgFormDialog.insertSql;
      table_sql.selectSql = this.msgFormDialog.selectSql;
      this.axios
        .post(interfaceObj.TableStructure_saveSql, {
          database_id: this.handleSQLObj.database_id,
          table_name: this.tableObj.table_name,
          table_sql: table_sql
        })
        .then(res => {
          if (res.data.returnCode == "0") {
            this.$message({
              message: "保存成功",
              type: "success"
            });
          } else {
            this.$message({
              message: res.data.returnMessage,
              type: "error"
            });
          }
        })
        .catch(error => {});
    },

    trueDialog(formName) {}
  }
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
}
</style>
