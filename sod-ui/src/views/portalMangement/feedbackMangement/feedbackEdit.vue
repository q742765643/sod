<template>
  <section class="dynDialog">
    <el-form :model="msgFormDialog" ref="fromRef" :rules="baseFormRules" label-width="120px">
      <el-form-item prop="userName" label="反馈人:">
        <el-input size="small" :disabled="isDbIdDisable" v-model.trim="msgFormDialog.createBy" />
      </el-form-item>
      <el-form-item label="反馈时间" prop="ispublished">
        <el-input size="small" :disabled="isDbIdDisable" v-model="msgFormDialog.createTime" />
      </el-form-item>
      <el-form-item prop="content" label="反馈内容:">
        <el-input
          :disabled="isDbIdDisable"
          type="textarea"
          size="small"
          v-model.trim="msgFormDialog.content"
        />
      </el-form-item>
      <el-form-item prop="content" label="回复:">
        <el-input
          type="textarea"
          size="small"
          v-model.trim="msgFormDialog.content"
          placeholder="请输入回复内容"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('fromRef')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  saveDynManage,
  editDynManage,
} from "@/api/portalMangement/dynMangement";
export default {
  name: "dynDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    var titleValidate = (rule, value, callback) => {
      if (value === "" || value.length > 100) {
        callback(new Error("请输入1-100字标题"));
      } else {
        callback();
      }
    };
    return {
      //编辑页面列
      msgFormDialog: {
        title: "",
        content: "",
        serialNumber: "",
        ispublished: "0",
      },
      //控制标题是否可编辑
      isDbIdDisable: false,
      baseFormRules: {
        title: [{ required: true, validator: titleValidate, trigger: "blur" }],
        ispublished: [{ required: true, message: "是否发布", trigger: "blur" }],
        serialNumber: [
          { required: true, message: "请输入序号 ", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请输入正文内容 ", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    if (this.handleObj.id) {
      this.isDbIdDisable = true;
      this.msgFormDialog = this.handleObj;
      this.msgFormDialog.createTime = this.parseTime(
        this.msgFormDialog.createTime
      );
      console.log(this.msgFormDialog);
    } else {
      this.isDbIdDisable = false;
    }
  },
  methods: {
    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let obj = this.msgFormDialog;
          console.log(obj);
          if (this.handleObj.id) {
            editDynManage(obj).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
              }
            });
          } else {
            saveDynManage(obj).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "增加成功",
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
              }
            });
          }
        } else {
          this.$message.error("请正确填写基本信息列表");
          return;
        }
      });
    },

    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    },
  },
};
</script>

<style lang="scss">
.dynDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
