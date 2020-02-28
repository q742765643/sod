<template>
  <section class="handleTaskCommonDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item prop="taskName" label="任务名称:">
        <el-input size="small" v-model="msgFormDialog.taskName" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="tableName" label="表名:">
        <el-select size="small" filterable v-model="msgFormDialog.tableName">
          <el-option
            v-for="(item,index) in tableNames"
            :key="index"
            :label="item.dictValue"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="apiUrl" label="接口url:">
        <el-input size="small" v-model="msgFormDialog.apiUrl" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="apiType" label="同步类型:">
        <el-select size="small" filterable v-model="msgFormDialog.apiType">
          <el-option label="全量同步" value="1"></el-option>
          <el-option label="增量同步" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="apiDataKey" label="接口关键字:">
        <el-input size="small" v-model="msgFormDialog.apiDataKey" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="primaryKey" label="同步主键:">
        <el-input size="small" v-model="msgFormDialog.primaryKey" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="startTime" label="执行策略:">
        <el-row>
          <el-col :span="18">
            <el-input size="small" v-model="msgFormDialog.startTime" placeholder="请输入" />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="small" @click="showStrategyDialog()">配置策略</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
    <Cron
      append-to-body
      v-if="cronDialogVisible"
      :cronDialogVisible="cronDialogVisible"
      @closeCron="closeCron"
      @setCron="setCron"
    />
  </section>
</template>

<script>
import {
  editConfig,
  addConfig,
  findCfgByPk
} from "@/api/system/commonMetadataSync/index";
import { getDictList } from "@/api/system/commonMetadataSync/select";

// cron表达式
import Cron from "@/components/cron/Cron";
export default {
  name: "handleTaskCommonDialog",
  components: {
    Cron
  },
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      cronDialogVisible: false,

      msgFormDialog: {
        // 这里定义弹框内的参数
        taskName: "",
        tableName: "", //todo
        apiUrl: "",
        apiType: "",
        apiDataKey: "",
        primaryKey: "",
        startTime: ""
      },
      tableNames: [],
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" }
        ],
        tableName: [{ required: true, message: "选择表名", trigger: "change" }],
        apiUrl: [{ required: true, message: "请输入接口url", trigger: "blur" }],
        apiType: [
          { required: true, message: "请选择同步类型", trigger: "change" }
        ],
        apiDataKey: [
          { required: true, message: "请输入接口关键字", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ]
      }
    };
  },
  async created() {
    // this.msgFormDialog = this.handleObj;
    await this.selectAllTaskName();
    // this.initDetail();
    if (this.handleObj.id) {
      await findCfgByPk({ id: this.handleObj.id }).then(res => {
        if (res.code == 200) {
          this.msgFormDialog = res.data;
        } else {
          this.$message({
            type: "error",
            message: "查询失败"
          });
        }
      });
    }
  },
  methods: {
    trueDialog(formName) {
      console.log(this.msgFormDialog);
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (!this.handleObj.id) {
            addConfig(this.msgFormDialog).then(res => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
              } else {
                this.$message({
                  type: "error",
                  message: "新增失败"
                });
              }
              this.$emit("cancelHandle");
            });
          } else {
            editConfig(this.msgFormDialog).then(res => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
              } else {
                this.$message({
                  type: "error",
                  message: "编辑失败"
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
      getDictList(this.queryParams).then(res => {
        if (res.code == 200) {
          this.tableNames = res.data;
        }
      });
    },
    //配置策略按钮点击事件
    showStrategyDialog() {
      this.cronDialogVisible = true;
    },
    //设置cron表达式
    setCron(cronExpression) {
      this.msgFormDialog.startTime = cronExpression;
      this.cronDialogVisible = false;
    },
    // 关闭cron表达式
    closeCron() {
      this.cronDialogVisible = false;
    }
  }
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
