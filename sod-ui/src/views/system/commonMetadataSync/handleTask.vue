<template>
  <section class="handleTaskCommonDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item prop="task_name" label="任务名称:">
        <el-input size="small" v-model="msgFormDialog.task_name" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="table_name" label="表名:">
        <el-select size="small" filterable v-model="msgFormDialog.table_name">
          <el-option v-for="item in tableNames" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="interface_url" label="接口url:">
        <el-input size="small" v-model="msgFormDialog.interface_url" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="interface_type" label="同步类型:">
        <el-select size="small" filterable v-model="msgFormDialog.interface_type">
          <el-option
            v-for="item in interfaceTypes"
            :key="item.key"
            :label="item.value"
            :value="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="interface_dataKey" label="接口关键字:">
        <el-input size="small" v-model="msgFormDialog.interface_dataKey" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="primary_key" label="同步主键:">
        <el-input size="small" v-model="msgFormDialog.primary_key" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="start_time" label="执行策略:">
        <el-row>
          <el-col :span="18">
            <el-input size="small" v-model="msgFormDialog.start_time" placeholder="请输入" />
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
// import { interfaceObj } from "@/urlConfig.js";
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
      interfaceTypes: [
        {
          key: "1",
          value: "全量同步"
        },
        {
          key: "2",
          value: "增量同步"
        }
      ],
      msgFormDialog: {
        // 这里定义弹框内的参数
        task_name: "",
        table_name: "",
        interface_url: "",
        interface_type: "",
        interface_dataKey: "",
        primary_key: "",
        start_time: ""
      },
      tableNames: [],
      rules: {
        task_name: [
          { required: true, message: "请输入任务名称", trigger: "blur" }
        ],
        table_name: [
          { required: true, message: "选择表名", trigger: "change" }
        ],
        interface_url: [
          { required: true, message: "请输入接口url", trigger: "blur" }
        ],
        interface_type: [
          { required: true, message: "请选择同步类型", trigger: "change" }
        ],
        interface_dataKey: [
          { required: true, message: "请输入接口关键字", trigger: "blur" }
        ],
        start_time: [
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // this.selectAllTaskName();
    // this.initDetail();
  },
  methods: {
    // 获取详情
    initDetail() {
      if (this.handleObj.id == undefined) {
        // 是新增
      } else {
        // 是详情
        this.msgFormDialog = this.handleObj;
      }
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id == undefined) {
            this.axios
              .post(
                interfaceObj.commonMetadata_insertMetadata,
                this.msgFormDialog
              )
              .then(res => {
                if (res.data.returnCode == 0) {
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
            this.axios
              .post(
                interfaceObj.commonMetadata_updateMetadata,
                this.msgFormDialog
              )
              .then(res => {
                if (res.data.returnCode == 0) {
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
      let obj = { type: 41 };
      this.axios
        .post(interfaceObj.dictionaryManage_getByType, obj)
        .then(res => {
          if (res.data.returnCode == 0) {
            // this.database_name = dataBaseNames;
            for (var i = 0; i < res.data.data.length; i++) {
              this.tableNames.push(res.data.data[i].key_col);
            }
          }
        });
    },
    //配置策略按钮点击事件
    showStrategyDialog() {
      this.cronDialogVisible = true;
    },
    //设置cron表达式
    setCron(cronExpression) {
      this.msgFormDialog.start_time = cronExpression;
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
