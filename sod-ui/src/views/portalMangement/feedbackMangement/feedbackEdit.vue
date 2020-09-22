<template>
  <section class="dynDialog">
    <el-form :model="msgFormDialog" ref="fromRef" label-width="120px">
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
      <el-form-item prop="reply" label="回复:">
        <el-input
          type="textarea"
          size="small"
          v-model.trim="msgFormDialog.reply"
          placeholder="请输入回复内容"
          maxlength="500"
          show-word-limit
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
import { editById } from "@/api/portalMangement/feedbackMangement";
export default {
  name: "dynDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      //编辑页面列
      msgFormDialog: {},
      isDbIdDisable: true,
    };
  },
  async created() {
    if (this.handleObj.id) {
      this.msgFormDialog = this.handleObj;
      this.msgFormDialog.createTime = this.parseTime(
        this.msgFormDialog.createTime
      );
    }
  },
  methods: {
    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let obj = {
            id: this.msgFormDialog.id,
            reply: this.msgFormDialog.reply,
          };
          editById(obj).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "回复成功",
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
          this.$message.error(res.msg);
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
