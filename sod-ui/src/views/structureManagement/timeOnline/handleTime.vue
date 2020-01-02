<template>
  <section class="powerDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="140px">
      <el-form-item label="开始日期" prop="time">
        <el-date-picker v-model="msgFormDialog.time" type="date" placeholder="yy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="time">
        <el-date-picker v-model="msgFormDialog.time" type="date" placeholder="yy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期（当天）" prop="sectionId">
        <el-checkbox v-model="msgFormDialog.checked"></el-checkbox>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "powerDialog",
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        name: "是否默认通过所有申请资料的读权限",
        roleKey: "读权限",
        sectionId: "",
        enable: 1,
        description: ""
      },
      rules: {
        time: [{ required: true, message: "请选择时间", trigger: "blur" }]
      }
    };
  },
  created() {
    // this.getSectionList();
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
    getSectionList() {
      this.axios.post(interfaceObj.roleManageApi_querySectionName).then(res => {
        this.optionsList = res.data.data;
      });
    },

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id == undefined) {
            //判断用户名是否存在
            this.axios
              .post(interfaceObj.roleManageApi_queryRoleByRoleName, {
                roleName: this.msgFormDialog.name
              })
              .then(res => {
                if (res.data.data.length == 0) {
                  this.axios
                    .post(
                      interfaceObj.roleManageApi_addRole,
                      this.msgFormDialog
                    )
                    .then(res => {
                      if (res.data.returnCode === "0") {
                        this.$message({ message: "添加成功", type: "success" });
                        this.$emit("cancelHandle");
                      } else {
                        this.$message({
                          message: "添加失败！",
                          type: "error"
                        });
                      }
                    });
                } else {
                  this.$message({
                    message: "用户名不能重复！！",
                    type: "warning"
                  });
                  return;
                }
              });
          } else {
            // 编辑
            this.axios
              .post(interfaceObj.roleManageApi_updateRole, this.msgFormDialog)
              .then(res => {
                if (res.data.returnCode === "0") {
                  this.$message({ message: "编辑成功", type: "success" });
                  this.$emit("cancelHandle");
                } else {
                  this.$message({
                    message: "编辑失败！",
                    type: "error"
                  });
                }
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
    }
  }
};
</script>
<style lang="scss">
.powerDialog {
  .el-date-editor.el-input {
    width: 100%;
  }
}
</style>
