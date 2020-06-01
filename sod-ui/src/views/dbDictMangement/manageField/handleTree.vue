<template>
  <section class="fileHandleTree">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item prop="groupName" label="分组名:">
        <el-input size="small" v-model.trim="ruleForm.groupName" placeholder="请输入分组名"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  addManageGroup,
  editManageGroup
} from "@/api/dbDictMangement/manageField";
export default {
  name: "filedSearchDeploy",
  components: {},
  props: {
    handleTreeObj: {
      type: Object
    }
  },
  data() {
    return {
      ruleForm: {
        groupId: "",
        groupName: ""
      },
      rules: {
        groupName: [
          { required: true, message: "分组名称为必输项", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.ruleForm = this.handleTreeObj;
  },
  methods: {
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.ruleForm.groupId) {
            //编辑
            editManageGroup(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.$emit("cancelDialog", "Tree");
              } else {
                this.$message({
                  type: "error",
                  message: "编辑失败"
                });
              }
            });
          } else {
            addManageGroup(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
                this.$emit("cancelDialog", "Tree");
              } else {
                this.$message({
                  type: "error",
                  message: "新增失败"
                });
              }
            });
          }
        }
      });
    }
  }
};
</script>

