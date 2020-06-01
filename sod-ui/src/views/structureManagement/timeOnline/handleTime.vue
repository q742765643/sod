<template>
  <section class="timeOnlineDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="140px">
      <el-form-item label="开始日期" prop="beginTime">
        <el-date-picker v-model.trim="msgFormDialog.beginTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="endTime">
        <el-date-picker v-model.trim="msgFormDialog.endTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期（当天）" prop="sectionId">
        <el-checkbox v-model.trim="msgFormDialog.checkFlag"></el-checkbox>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { update } from "@/api/structureManagement/timeOnline";
export default {
  name: "timeOnlineDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        beginTime: "",
        endTime: "",
        checkFlag: false,
        endTimeFlag: "",
        dataClassId: ""
      },
      rules: {
        beginTime: [{ required: true, message: "请选择时间", trigger: "blur" }],
        endTime: [{ required: true, message: "请选择时间", trigger: "blur" }]
      }
    };
  },
  created() {
    this.msgFormDialog.beginTime = this.handleObj.BEGIN_TIME;
    this.msgFormDialog.endTime = this.handleObj.END_TIME;
    this.msgFormDialog.dataClassId = this.handleObj.DATA_CLASS_ID;
    if (this.handleObj.obj && this.handleObj.obj.endTimeFlag == "today") {
      this.msgFormDialog.checkFlag = true;
    }
  },
  methods: {
    // 获取详情
    initDetail() {},

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.msgFormDialog.checkFlag) {
            this.msgFormDialog.endTimeFlag = "today";
          }
          console.log(this.msgFormDialog);
          update(this.msgFormDialog).then(res => {
            if (res.code == 200) {
              this.$message({
                message: "操作成功",
                type: "success"
              });
              this.$emit("cancelHandle");
            } else {
              this.$message({
                message: res.msg,
                type: "error"
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    }
  }
};
</script>
<style lang="scss">
.timeOnlineDialog {
  .el-date-editor.el-input {
    width: 100%;
  }
}
</style>
