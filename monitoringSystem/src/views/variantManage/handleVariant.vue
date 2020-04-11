<template>
  <section class="varDialog">
    <el-form label-width="80px" :model="formObject" :rules="rule" ref="formObject">
      <el-form-item label="变量名" prop="name">
        <el-input placeholder="请输入变量名" size="small" v-model="formObject.name"></el-input>
      </el-form-item>
      <el-form-item label="变量值" prop="value">
        <el-input placeholder="请输入变量值" size="small" v-model="formObject.value"></el-input>
      </el-form-item>
      <el-form-item label="描述信息">
        <el-input placeholder="请输入描述信息" size="small" v-model="formObject.description"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialogFooter">
      <el-button @click="makeSure(true)" size="small" type="primary">确定</el-button>
      <el-button @click="makeSure(false)" size="small">取消</el-button>
    </div>
  </section>
</template>

<script>
import { interfaceObj } from "@/urlConfig";
//弹出层
export default {
  name: "varDialog",
  props: {
    handleObj: Object
  },
  data() {
    return {
      userOrganVisible: false, //用户部门弹出层
      formObject: {
        name: "",
        value: "",
        description: ""
      },
      oldOrganForm: "",
      rule: {
        name: [{ required: true, message: "请输入变量名", trigger: "blur" }],
        value: [{ required: true, message: "请输入变量值", trigger: "blur" }]
      }
    };
  },
  mounted() {},
  methods: {
    //确定
    makeSure(type) {
      if (type) {
        let requestUrl, resMessage;
        //修改
        if (this.organId) {
          requestUrl = interfaceObj.updateSystemCode;
          resMessage = "修改";
        }
        //新增
        else {
          requestUrl = interfaceObj.systemCode;
          resMessage = "新增";
        }
        this.$refs["formObject"].validate(valid => {
          if (valid) {
            this.$axios
              .post(requestUrl, this.formObject)
              .then(({ data }) => {
                if (data.DS) {
                  this.$notify({
                    title: "提示",
                    type: "success",
                    message: resMessage + "成功"
                  });
                  this.$emit("handleDialogClose", resMessage);
                } else {
                  this.$notify({
                    title: "提示",
                    type: "error",
                    message: resMessage + "失败"
                  });
                }
              })
              .catch(error => {
                console.log(error);
              });
          }
        });
      } else {
        this.$emit("handleDialogClose", type);
      }
    }
  }
};
</script>

<style lang="scss">
.varDialog {
  .el-form-item {
    margin-bottom: 14px;
  }
  .el-form-item__error {
    padding-top: 0px;
  }
  .smallInput {
    width: 76%;
    margin-right: 10px;
  }
}
</style>