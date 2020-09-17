<template>
  <section class="handleSQLDialog">
    <el-form ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据库类型">
            <el-input v-model.trim="msgFormDialog.DATABASE_NAME_F" disabled size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" class="btnColBox">
          <el-radio-group v-model="radioTable" style="margin-right:4px;" @change="changeTableType">
            <el-radio label="K">键表</el-radio>
            <el-radio label="E">要素表</el-radio>
          </el-radio-group>
          <el-button @click="createSql" size="small" type="primary" icon="el-icon-money" plain>生成SQL</el-button>
          <el-button
            @click="handleSaveSql"
            size="small"
            type="primary"
            icon="el-icon-wallet"
            plain
          >保存SQL</el-button>
          <el-button
            size="small"
            @click="createBase"
            type="primary"
            icon="el-icon-first-aid-kit"
            plain
          >创建物理表</el-button>
        </el-col>
      </el-row>

      <el-collapse v-model.trim="activeNames" @change="handleChange">
        <el-collapse-item title="CREATE" name="1">
          <el-input type="textarea" size="small" v-model.trim="msgFormDialog.createSql" readonly></el-input>
        </el-collapse-item>
        <el-collapse-item title="INSERT" name="2">
          <el-input type="textarea" size="small" v-model.trim="msgFormDialog.insertSql" readonly></el-input>
        </el-collapse-item>
        <el-collapse-item title="SELECT" name="3">
          <el-input type="textarea" size="small" v-model.trim="msgFormDialog.selectSql" readonly></el-input>
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
import { gcl } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import {
  getSql,
  saveSql,
  createTable,
  existTable,
} from "@/api/structureManagement/tableStructureManage/handleSQL";

export default {
  name: "handleSQLDialog",
  props: {
    handleSQLObj: {
      type: Object,
    },
  },
  data() {
    return {
      activeNames: ["1"],
      radioTable: "K",
      msgFormDialog: {
        createSql: "",
        insertSql: "",
        selectSql: "",
        DATABASE_NAME_F: this.handleSQLObj.DATABASE_NAME_F,
      },
      tableObj: "",
    };
  },
  created() {
    this.init();
  },
  methods: {
    changeTableType(val) {
      gcl({ classLogic: this.handleSQLObj.LOGIC_ID }).then((res) => {
        let data = res.data;
        let indext = "";
        data.forEach((element, index) => {
          if (element.dbTableType == val) {
            indext = index;
            return;
          }
        });
        this.tableObj = res.data[indext];
      });
    },
    init() {
      gcl({ classLogic: this.handleSQLObj.LOGIC_ID }).then((res) => {
        let data = res.data;
        this.radioTable = data[0].dbTableType;
        let indext = "";
        data.forEach((element, index) => {
          if (!element.dbTableType) {
            element.dbTableType = "E";
            this.radioTable = "E";
          }
          if (element.dbTableType == this.radioTable) {
            indext = index;
            return;
          }
        });

        this.tableObj = res.data[indext];
      });
    },
    handleChange() {},
    createSql() {
      if (this.tableObj && this.tableObj.id) {
        let obj = {
          databaseId: this.handleSQLObj.DATABASE_ID,
          tableId: this.tableObj.id,
        };
        console.log(obj);
        getSql(obj).then((res) => {
          this.msgFormDialog.createSql = res.data.createSql;
          this.msgFormDialog.insertSql = res.data.insertSql;
          this.msgFormDialog.selectSql = res.data.selectSql;
          this.activeNames = ["1"];
        });
      } else {
        this.$message({
          message: "请重新选择表类型",
          type: "error",
        });
      }
    },
    handleSaveSql() {
      if (this.msgFormDialog.createSql == "") {
        this.$message({
          message: "CREATE不能为空",
          type: "error",
        });
        return;
      }

      saveSql({
        databaseId: this.handleSQLObj.DATABASE_ID,
        tableName: this.tableObj.tableName,
        tableSql: this.msgFormDialog.createSql,
      }).then((res) => {
        if (res.code == 200) {
          this.$message({
            message: "保存成功",
            type: "success",
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
    },
    createBase() {
      if (this.msgFormDialog.createSql == "") {
        this.$message({
          message: "CREATE不能为空",
          type: "error",
        });
        return;
      }
      existTable({
        databaseId: this.handleSQLObj.DATABASE_ID,
        tableName: this.tableObj.tableName,
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
              databaseId: this.handleSQLObj.DATABASE_ID,
              tableName: this.tableObj.tableName,
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
    trueSave() {},

    trueDialog(formName) {},
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
