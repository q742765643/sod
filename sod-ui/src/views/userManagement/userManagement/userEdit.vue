<template>
  <div class="userManageTemplate">
    <el-form class="add_form" ref="formClass" :model="formObject" :rules="rules" label-width="95px">
      <el-row>
        <el-col :span="12">
          <el-form-item prop="userName" label="用户名:">
            <el-input v-model.trim="formObject.userName" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="user_role_id" label="所属角色：">
            <el-select v-model.trim="formObject.user_role_id" size="small">
              <el-option
                v-for="(item,index) in roles"
                :key="index"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="userPassword" label="用户密码:">
            <el-input v-model.trim="formObject.userPassword" type="password" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="userPhone" label="电话:">
            <el-input v-model.trim="formObject.userPhone" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="userNickname" label="用户昵称:">
            <el-input v-model.trim="formObject.userNickname" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="userRealname" label="真实姓名:">
            <el-input v-model.trim="formObject.userRealname" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="userAge" label="年龄:">
            <el-input v-model.trim="formObject.userAge" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="userSex" label="性别:">
            <el-radio v-model.trim="formObject.userSex" label="男">男</el-radio>
            <el-radio v-model.trim="formObject.userSex" label="女">女</el-radio>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="userQQ" label="QQ:">
            <el-input v-model.trim="formObject.userQQ" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="userMail" label="邮箱:">
            <el-input v-model.trim="formObject.userMail" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="地址:">
            <el-row>
              <el-col :span="12">
                <el-form-item prop="provinceAndCity">
                  <el-cascader
                    size="small"
                    :options="options"
                    v-model.trim="formObject.provinceAndCity"
                    @change="handleProvinceAndCityChange"
                  ></el-cascader>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="userAddress">
                  <el-input v-model.trim="formObject.userAddress" size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item prop="mark" label="备注:">
          <el-input type="textarea" v-model.trim="formObject.mark" size="small"></el-input>
        </el-form-item>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="saveUserInfo('formClass')">确定</el-button>
      <el-button @click="handleClose('formClass')">取消</el-button>
    </div>
  </div>
</template>
<script>
// import { interfaceObj } from "@/urlConfig.js";
// 省市区联动插件
// import { regionData } from "element-china-area-data";
export default {
  name: "userAddOrEdit",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      // 省市区联动
      options: "",
      // options: regionData,
      //添加form表单初始化
      formObject: {
        userName: "",
        user_role_id: "",
        userPassword: "",
        userPhone: "",
        userNickname: "",
        userRealname: "",
        userAge: "",
        userSex: "男",
        userQQ: "",
        userMail: "",
        province: "",
        city: "",
        county: "",
        provinceAndCity: [], //省市区联动绑定的值
        userAddress: "",
        mark: ""
      },
      roles: [],
      //检查规则
      rules: {
        userName: [
          { required: true, message: "用户名必须输入", trigger: "blur" }
        ],
        user_role_id: [
          { required: true, message: "必须选择用户角色", trigger: "blur" }
        ],
        userPassword: [
          { required: true, message: "用户密码必须输入", trigger: "blur" }
        ],
        userRealname: [
          { required: true, message: "用户真实姓名必须输入", trigger: "blur" }
        ],
        userAge: [
          { required: true, message: "用户年龄必须输入", trigger: "blur" }
        ],
        userMail: [{ required: true, message: "邮箱必须输入", trigger: "blur" }]
      }
    };
  },
  created() {
    // this.userRoles();
    // this.init();
  },
  methods: {
    init() {
      if (this.handleObj.userId == undefined) {
        // 这是新增
      } else {
        this.formObject = this.handleObj;
        let provinceAndCity =
          this.handleObj.province +
          "," +
          this.handleObj.city +
          "," +
          this.handleObj.county;
        this.formObject.provinceAndCity = provinceAndCity.split(",");
      }
    },
    handleProvinceAndCityChange() {},
    //查询所有角色
    userRoles() {
      this.axios.post(interfaceObj.userManage_selectRole).then(res => {
        if (res.data.returnCode == 0) {
          this.roles = res.data.data;
        }
      });
    },

    //保存用户
    saveUserInfo(formClass) {
      //校验表单
      this.$refs[formClass].validate(valid => {
        debugger;
        if (valid) {
          if (
            this.formObject.userName != "admin" &&
            this.formObject.user_role_id == "3"
          ) {
            this.$message({
              message: "普通用户不能授予【超级管理员】角色",
              type: "warning"
            });
            return;
          }
          if (this.formObject.length > 0) {
            this.formObject.province = this.formObject.provinceAndCity[0];
            this.formObject.city = this.formObject.provinceAndCity[1];
            this.formObject.county = this.formObject.provinceAndCity[2];
          }
          if (this.formObject.userId != null && this.formObject.userId != "") {
            this.axios
              .post(interfaceObj.userManage_updateUser, this.formObject)
              .then(res => {
                if (res.data.returnCode === "0") {
                  this.$message({ message: "修改成功", type: "success" });
                  this.$emit("handleDialogClose");
                }
              });
          } else {
            //判断用户名是否存在
            this.axios
              .post(interfaceObj.userManage_getByUserName, {
                userName: this.formObject.userName
              })
              .then(res => {
                if (res.data.data.length == 0) {
                  this.axios
                    .post(interfaceObj.userManage_addUser, this.formObject)
                    .then(res => {
                      if (res.data.returnCode === "0") {
                        this.$message({ message: "添加成功", type: "success" });
                        this.$emit("handleDialogClose");
                      } else {
                        this.$message({
                          message: "用户信息保存失败！！",
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
        }
      });
    },
    //取消按钮
    handleClose(formClass) {
      this.$refs[formClass].resetFields();
      this.$emit("handleDialogClose");
    }
  }
};
</script>
<style lang="scss">
.userManageTemplate {
  .el-cascader {
    width: 86%;
  }
  .el-select {
    width: 100%;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
}
</style>