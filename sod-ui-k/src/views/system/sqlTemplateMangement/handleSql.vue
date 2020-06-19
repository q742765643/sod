<template>
  <section class="SQLTemplate">
    <el-form label-width="100px" v-model.trim="handleSqlTemplate">
      <el-row>
        <el-col :span="9">
          <el-form-item label="数据库厂商：">
            <el-select
              v-model.trim="handleSqlTemplate.databaseServer"
              size="small"
              :disabled="disabledFlag"
            >
              <el-option
                v-for="item in databaseList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="`${item.dictValue}`"
              ></el-option>
            </el-select>
            <el-row class="rowBoxBtn">
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${tableName}')"
                  class="funButton"
                >表名称</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${databaseName}')"
                  class="funButton"
                >库名称</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${columnInfo}')"
                  class="funButton"
                >字段信息</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${primaryColumn}')"
                  class="funButton"
                >主键字段</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${tableDescription}')"
                  class="funButton"
                >索引字段</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${indexColumn}')"
                  class="funButton"
                >表描描述</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${DPartitionKey}')"
                  class="funButton"
                >分库键</el-button>
              </el-col>
              <el-col :span="12">
                <el-button
                  size="medium"
                  type="primary"
                  @click="autoFillIn('${TPartitionKey}')"
                  class="funButton"
                >分表键</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-col>
        <el-col :span="15">
          <textarea v-model.trim="handleSqlTemplate.template" class="textClass" id="addtextareaId"></textarea>
        </el-col>
      </el-row>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog()">确 定</el-button>
      <el-button @click="closeDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  saveSqlTemplate, //添加模板
  isAlreadyTem, //是否已存在模板
  editTemplate //编辑模板
} from "@/api/system/sqlTemplateMangement";
// cron表达式
export default {
  name: "SQLTemplate",

  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      disabledFlag: false,
      databaseList: [], //数据类型
      handleSqlTemplate: {
        databaseServer: "",
        template: ""
      }
    };
  },
  created() {
    //获取数据库类型
    this.getDBList();
    console.log(this.handleObj);
  },
  methods: {
    //获取数据库厂商
    getDBList() {
      this.getDicts("sys_database_type").then(response => {
        this.databaseList = response.data;
        this.handleSqlTemplate = this.handleObj;
        if (this.handleObj.id) {
          this.disabledFlag = true;
        }
      });
    },

    autoFillIn(insertTxt) {
      // this.handleObj.template += value;
      var elInput = document.getElementById("addtextareaId");
      var startPos = elInput.selectionStart;
      var endPos = elInput.selectionEnd;
      if (startPos === undefined || endPos === undefined) return;
      var txt = elInput.value;
      var result =
        txt.substring(0, startPos) + insertTxt + txt.substring(endPos);
      elInput.value = result;
      elInput.focus();
      elInput.selectionStart = startPos + insertTxt.length;
      elInput.selectionEnd = startPos + insertTxt.length;
    },

    trueDialog() {
      //新增
      if (!this.handleObj.id) {
        isAlreadyTem({
          databaseServer: this.handleSqlTemplate.databaseServer
        }).then(response => {
          //已存在不可添加
          if (response.data.length >= 1) {
            this.$message({
              type: "error",
              message: "此数据库已经添加，不可以重复添加"
            });
          }
          //添加
          else {
            this.handleSqlTemplate.template = document.getElementById(
              "addtextareaId"
            ).value;
            saveSqlTemplate(this.handleSqlTemplate).then(response => {
              if (response.code === 200) {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
                this.$emit("closeDialog");
              }
            });
          }
        });
      }
      //编辑
      else {
        this.handleSqlTemplate.template = document.getElementById(
          "addtextareaId"
        ).value;
        editTemplate(this.handleSqlTemplate).then(response => {
          if (response.code === 200) {
            this.$message({
              type: "success",
              message: "编辑成功"
            });
            this.$emit("closeDialog");
          }
        });
      }
    },
    closeDialog(formName) {
      this.$emit("closeDialog");
    }
  }
};
</script>

<style lang="scss">
.SQLTemplate {
}
</style>
