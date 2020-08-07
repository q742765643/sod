<template>
  <section class="handleTaskCommonDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item prop="taskName" label="任务名称:">
        <el-input size="small" v-model.trim="msgFormDialog.taskName" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="tableName" label="表名:">
        <el-select size="small" filterable v-model.trim="msgFormDialog.tableName">
          <el-option
            v-for="(item,index) in tableNames"
            :key="index"
            :label="item.dictValue"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="apiUrl" label="接口url:">
        <el-input size="small" v-model.trim="msgFormDialog.apiUrl" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="apiType" label="同步类型:">
        <el-select size="small" filterable v-model.trim="msgFormDialog.apiType">
          <el-option label="全量同步" value="1"></el-option>
          <el-option label="增量同步" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="apiDataKey" label="接口关键字:">
        <el-input size="small" v-model.trim="msgFormDialog.apiDataKey" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="primaryKey" label="同步主键:">
        <el-input size="small" v-model.trim="msgFormDialog.primaryKey" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="jobCron" label="执行策略:">
        <el-popover v-model.trim="cronPopover">
          <vueCron @change="changeCron" @close="closeCronPopover" i18n="cn"></vueCron>
          <el-input
            slot="reference"
            @click="cronPopover=true"
            v-model.trim="msgFormDialog.jobCron"
            placeholder="请输入定时策略"
          ></el-input>
        </el-popover>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  editConfig,
  addConfig,
  findCfgByPk,
} from "@/api/system/commonMetadataSync/index";
import { getDictList } from "@/api/system/commonMetadataSync/select";
import { getNextTime } from "@/api/schedule/backup/backup";

export default {
  name: "handleTaskCommonDialog",

  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    //校验是否为cron表达式
    var handleCronValidate = async (rule, value, callback) => {
      if (value == "") {
        callback(new Error("请输入执行策略!"));
      } else {
        let flag = true;
        await getNextTime({
          cronExpression: this.msgFormDialog.jobCron.split(" ?")[0] + " ?",
        }).then((res) => {
          flag = false;
        });
        if (flag) {
          callback(new Error("执行策略表达式错误!"));
        } else {
          callback();
        }
      }
    };
    return {
      cronExpression: "",
      cronPopover: false,
      msgFormDialog: {
        // 这里定义弹框内的参数
        taskName: "",
        tableName: "", //todo
        apiUrl: "",
        apiType: "",
        apiDataKey: "",
        primaryKey: "",
        jobCron: "0 0 2 * * ?",
      },
      tableNames: [],
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" },
        ],
        tableName: [{ required: true, message: "选择表名", trigger: "change" }],
        apiUrl: [{ required: true, message: "请输入接口url", trigger: "blur" }],
        apiType: [
          { required: true, message: "请选择同步类型", trigger: "change" },
        ],
        apiDataKey: [
          { required: true, message: "请输入接口关键字", trigger: "blur" },
        ],
        jobCron: [
          { required: true, message: "请输入执行策略", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    // this.msgFormDialog = this.handleObj;
    await this.selectAllTaskName();
    // this.initDetail();
    if (this.handleObj.id) {
      await findCfgByPk({ id: this.handleObj.id }).then((res) => {
        if (res.code == 200) {
          this.msgFormDialog = res.data;
        } else {
          this.$message({
            type: "error",
            message: "查询失败",
          });
        }
      });
    }
  },
  methods: {
    changeCron(val) {
      this.cronExpression = val;
      if (val.substring(0, 5) == "* * *") {
        this.msgError("小时,分钟,秒必填");
      } else {
        this.msgFormDialog.jobCron = val.split(" ?")[0] + " ?";
      }
    },
    closeCronPopover() {
      if (this.cronExpression.substring(0, 5) == "* * *") {
        return;
      } else {
        getNextTime({
          cronExpression: this.cronExpression.split(" ?")[0] + " ?",
        }).then((res) => {
          let times = res.data;
          let html = "";
          times.forEach((element) => {
            html += "<p>" + element + "</p>";
          });
          this.$alert(html, "前5次执行时间", {
            dangerouslyUseHTMLString: true,
          }).then(() => {
            this.CronPopover = false;
          });
        });
      }
    },
    trueDialog(formName) {
      console.log(this.msgFormDialog);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.handleObj.id) {
            addConfig(this.msgFormDialog).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
              } else {
                this.$message({
                  type: "error",
                  message: "新增失败",
                });
              }
              this.$emit("cancelHandle");
            });
          } else {
            editConfig(this.msgFormDialog).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
              } else {
                this.$message({
                  type: "error",
                  message: "编辑失败",
                });
              }
              this.$emit("cancelHandle");
            });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    },
    selectAllTaskName() {
      getDictList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.tableNames = res.data;
        }
      });
    },
  },
};
</script>

<style lang="scss">
.handleTaskCommonDialog {
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
}
</style>
