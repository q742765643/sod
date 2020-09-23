<template>
  <section class="SDKDialog">
    <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
      <el-form-item prop="sdkType" label="SDK类型:">
        <el-select v-model="msgFormDialog.sdkType" :disabled="handleDis">
          <el-option
            :label="item.dictLabel"
            :value="item.dictValue"
            v-for="(item,index) in SDKClassify"
            :key="index"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="sdkLang" label="开发语言:">
        <el-select v-model="msgFormDialog.sdkLang" :disabled="handleDis">
          <el-option label="Java" value="Java"></el-option>
          <el-option label="Python" value="Python"></el-option>
          <el-option label="Fortran" value="Fortran"></el-option>
          <el-option label="C/C++" value="C/C++"></el-option>
          <el-option label="C#" value="C#"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="sdkSys" label="操作系统:">
        <el-select v-model="msgFormDialog.sdkSys" :disabled="handleDis">
          <el-option label="windows" value="windows"></el-option>
          <el-option label="Linux" value="Linux"></el-option>
          <el-option label="Linux/windows" value="Linux/windows"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="SDK包" prop="sdkJarUrl">
        <el-input
          :disabled="handleDis"
          v-show="handleObj.id"
          clearable
          size="small"
          v-model="msgFormDialog.sdkJarUrl"
        />
        <el-upload
          v-show="!handleObj.id"
          class="upload-demo"
          :action="upLoadUrl"
          ref="upload"
          :limit="1"
          :on-exceed="onExceed"
          :on-success="JarSuccess"
        >
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="SDK文档" prop="sdkDocUrl">
        <el-input
          :disabled="handleDis"
          v-show="handleObj.id"
          clearable
          size="small"
          v-model="msgFormDialog.sdkDocUrl"
        />
        <el-upload
          v-show="!handleObj.id"
          class="upload-demo"
          :action="upLoadUrl"
          ref="upload"
          :limit="1"
          :on-exceed="onExceed"
          :on-success="DocSuccess"
        >
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="sdkDesc" label="SDK描述:">
        <el-input clearable size="small" v-model="msgFormDialog.sdkDesc" />
      </el-form-item>
      <el-form-item prop="serialNumber" label="排序:">
        <el-input-number
          :disabled="handleDis"
          clearable
          size="small"
          v-model="msgFormDialog.serialNumber"
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
var baseUrl = process.env.VUE_APP_PORTAL;
import {
  getById,
  editById,
  saveSdkManage,
} from "@/api/portalMangement/feedbackMangement";
export default {
  name: "SDKDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      handleDis: false,
      SDKClassify: [],
      upLoadUrl: baseUrl + "/cmadaas/sod/portal/sdkManage/upload",
      //编辑页面列
      msgFormDialog: {
        sdkType: "",
        sdkLang: "",
        sdkSys: "",
        sdkJarUrl: "",
        sdkDocUrl: "",
        sdkDesc: "",
        serialNumber: "",
        id: "1",
      },
      baseFormRules: {
        sdkType: [
          { required: true, message: "请选择SDK类型", trigger: "change" },
        ],
        sdkLang: [
          { required: true, message: "请选择开发语言 ", trigger: "change" },
        ],
        sdkSys: [
          { required: true, message: "请选择操作系统 ", trigger: "change" },
        ],
        sdkJarUrl: [
          { required: true, message: "请上传SDK包", trigger: "change" },
        ],
        sdkDocUrl: [
          { required: true, message: "请上传SDK文档 ", trigger: "change" },
        ],
        sdkDesc: [
          { required: true, message: "请输入SDK描述 ", trigger: "blur" },
        ],
        serialNumber: [
          { required: true, message: "请输入排序编号 ", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    this.getDicts("portal_sdk_mng").then((response) => {
      this.SDKClassify = response.data;
    });
    // 详情回显
    if (this.handleObj.id) {
      this.handleDis = true;
      getById({ id: this.handleObj.id }).then((res) => {
        this.msgFormDialog = res.data;
      });
    }
  },
  methods: {
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
    DocSuccess(res, file) {
      if (res.code == 200) {
        this.msgFormDialog.sdkDocUrl = res.url;
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
                message: "编辑成功",
              });
              this.$emit("cancelDialog");
            });
          } else {
            saveSdkManage(this.msgFormDialog).then((res) => {
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
.SDKDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
