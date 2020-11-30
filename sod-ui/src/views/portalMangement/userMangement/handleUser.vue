<template>
  <section class="dataDialog">
    <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
      <!--<el-form-item prop="ddataId" label="用户级别:">
        <el-select v-model="msgFormDialog.isshow" :disabled="handleDis">
          <el-option label="国家级" value="01"></el-option>
          <el-option label="非国家级" value="02"></el-option>
        </el-select>
      </el-form-item>-->
      <el-form-item prop="email" label="邮箱:">
        <el-input clearable size="small" v-model="msgFormDialog.email" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="loginName" label="登录名:">
        <el-input clearable size="small" v-model="msgFormDialog.loginName" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="userName" label="姓名:">
        <el-input clearable size="small" v-model="msgFormDialog.userName" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="deptunicode" label="工作单位:">
        <el-input
          clearable
          size="small"
          v-model="msgFormDialog.deptunicode"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="post" label="职位:">
        <el-input
          clearable
          size="small"
          v-model="msgFormDialog.post"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="jobTitle" label="职称:">
        <el-input
          clearable
          size="small"
          v-model="msgFormDialog.jobTitle"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="phone" label="手机:">
        <el-input
          clearable
          size="small"
          v-model="msgFormDialog.phone"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="fixedphone" label="办公电话:">
        <el-input
          clearable
          size="small"
          v-model="msgFormDialog.fixedphone"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="serialNumber" label="用户状态:">
        <el-select v-model="msgFormDialog.ischeck" :disabled="handleDis">
          <el-option label="全部" value></el-option>
          <el-option label="未审核" value="0"></el-option>
          <el-option label="已审核" value="1"></el-option>
          <el-option label="已驳回" value="2"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <!--<el-button type="primary" @click="trueDialog('fromRef')" v-if="handleDis == false">确 定</el-button>
      <el-button type="primary" @click="trueDialog('fromRef')" v-if="handleDis">审 核</el-button>-->
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getById,
  editById,
  typicalAppSave,
} from "@/api/portalMangement/userMangement";
export default {
  name: "dataDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      imageUrl: "",
      handleDis: false,
      //编辑页面列
      msgFormDialog: {
        email: "",
        ischeck: "",
        loginName: "",
        userName: "",
        deptunicode:"",
        post:"",
        jobTitle: "",
        phone:"",
        fixedphone:"",
      },
      baseFormRules: {
        loginName: [
          { required: true, message: "请输入登录名", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    // 详情回显
    if (this.handleObj.id) {
      this.handleDis = true;
      getById({ id: this.handleObj.id }).then((res) => {
        this.msgFormDialog = res.data;
      });
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    onExceed() {
      this.$message({
        type: "error",
        message: "只能上传一个文件",
      });
    },
    JarSuccess(res, file) {
      if (res.code == 200) {
        this.msgFormDialog.sdkJarUrl = res.url;
      } else {
        this.$message({
          type: "error",
          message: res.msg,
        });
      }
    },

    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.handleObj.id) {
            editById(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "审核成功",
              });
              this.$emit("cancelDialog");
            });
          } else {
            typicalAppSave(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "增加成功",
              });
              this.$emit("cancelDialog");
            });
          }
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
.dataDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
