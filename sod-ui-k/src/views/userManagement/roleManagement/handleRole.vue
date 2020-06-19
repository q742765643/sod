<template>
  <section class="roleDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item label="角色名称" prop="name">
        <el-input size="medium" v-model.trim="msgFormDialog.name"></el-input>
      </el-form-item>
      <el-form-item label="角色KEY" prop="roleKey">
        <el-input size="medium" v-model.trim="msgFormDialog.roleKey"></el-input>
      </el-form-item>
      <el-form-item label="所属科室" prop="sectionId">
        <el-select size="medium" v-model.trim="msgFormDialog.sectionId">
          <el-option
            v-for="(item,index) in optionsList"
            :key="index"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否禁用" prop="enable">
        <el-radio-group v-model.trim="msgFormDialog.enable">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
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
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "roleDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        name: "",
        roleKey: "",
        sectionId: "",
        enable: 1,
        description: ""
      },
      optionsList: [],
      rules: {
        name: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
        roleKey: [
          { required: true, message: "请输入角色KEY", trigger: "blur" }
        ],
        sectionId: [
          { required: true, message: "请选择所属科室", trigger: "change" }
        ],
        enable: [
          { required: true, message: "请选择是否禁用", trigger: "change" }
        ],
        description: [
          { required: true, message: "请输入描述", trigger: "blur" }
        ]
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
.roleDialog {
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
