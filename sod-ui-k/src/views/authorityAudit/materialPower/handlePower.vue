<template>
  <section class="powerDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item label="配置名称" prop="name">
        <el-input size="medium" v-model.trim="msgFormDialog.name" readonly></el-input>
      </el-form-item>
      <el-form-item label="权限" prop="roleKey">
        <el-input size="medium" v-model.trim="msgFormDialog.roleKey" readonly></el-input>
      </el-form-item>
      <el-form-item label="默认通过">
        <el-radio-group v-model.trim="msgFormDialog.value">
          <el-radio label="1">通过</el-radio>
          <el-radio label="0">不通过</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" size="medium" v-model.trim="msgFormDialog.description"></el-input>
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
  getReadAuthority,
  updateReadAuthority
} from "@/api/authorityAudit/materialPower/index";
export default {
  name: "powerDialog",
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        name: "是否默认通过所有申请资料的读权限",
        roleKey: "读权限",
        value: "0",
        description: ""
      },
      rules: {
        name: [{ required: true, message: "请输入角色名称", trigger: "blur" }],

        description: [
          { required: true, message: "请输入描述", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    getReadAuthority().then(res => {
      if (res.data && res.data.length > 0) {
        this.msgFormDialog = res.data[0];
      }
      this.msgFormDialog.roleKey = "读权限";
    });
  },
  methods: {
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          updateReadAuthority(this.msgFormDialog).then(res => {
            if (res.code == 200) {
              this.$message({ type: "success", message: "操作成功" });
              this.$emit("cancelHandle");
            } else {
              this.$message({ type: "danger", message: "操作失败" });
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

