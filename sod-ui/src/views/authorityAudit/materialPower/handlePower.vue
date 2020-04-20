<template>
  <section class="powerDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item label="配置名称" prop="name">
        <el-input size="medium" v-model="msgFormDialog.name" readonly></el-input>
      </el-form-item>
      <el-form-item label="权限" prop="roleKey">
        <el-input size="medium" v-model="msgFormDialog.roleKey" readonly></el-input>
      </el-form-item>
      <el-form-item label="默认通过">
        <el-radio-group v-model="msgFormDialog.enable">
          <el-radio :label="0">通过</el-radio>
          <el-radio :label="1">不通过</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" size="medium" v-model="msgFormDialog.description"></el-input>
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
        enable: 1,
        description: ""
      },
      rules: {
        name: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
        roleKey: [
          { required: true, message: "请输入角色KEY", trigger: "blur" }
        ],

        description: [
          { required: true, message: "请输入描述", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // this.getSectionList();
    if (this.handleObj.id) {
      // 是详情
      this.msgFormDialog = this.handleObj;
    }
  },
  methods: {
    getSectionList() {
      this.axios.post(interfaceObj.roleManageApi_querySectionName).then(res => {
        this.optionsList = res.data.data;
      });
    },

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id) {
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
          } else {
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

