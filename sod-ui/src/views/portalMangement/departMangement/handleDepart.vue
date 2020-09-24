<template>
  <section class="dataDialog">
    <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
      <el-form-item prop="ddataId" label="部门类型:">
        <el-select v-model="msgFormDialog.isshow" :disabled="handleDis">
          <el-option label="国家级" value="01"></el-option>
          <el-option label="非国家级" value="02"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="dataName" label="部门编号:">
        <el-input clearable size="small" v-model="msgFormDialog.dataName" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="dataName" label="部门名称:">
        <el-input clearable size="small" v-model="msgFormDialog.dataName" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="dataName" label="上级部门:">
        <el-input clearable size="small" v-model="msgFormDialog.dataName" :disabled="true" />
      </el-form-item>
      <el-form-item prop="serialNumber" label="部门排序:">
        <el-input-number
          clearable
          size="small"
          v-model="msgFormDialog.serialNumber"
          :disabled="handleDis"
        />
      </el-form-item>
      <el-form-item prop="serialNumber" label="部门级别:">
        <el-input-number
          clearable
          size="small"
          v-model="msgFormDialog.serialNumber"
          :disabled="handleDis"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('fromRef')" v-if="handleDis">审 核</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getById,
  editById,
  typicalAppSave,
} from "@/api/portalMangement/typicalManagement";
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
        ddataId: "",
        dataName: "",
        icon: "",
        isshow: "Y",
        serialNumber: "1",
        module: "1",
      },
      baseFormRules: {
        ddataId: [
          { required: true, message: "请输入四级编码", trigger: "blur" },
        ],
        dataName: [
          { required: true, message: "请输入分类名称 ", trigger: "blur" },
        ],

        serialNumber: [
          { required: true, message: "请输入排序编号 ", trigger: "blur" },
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
                message: "编辑成功",
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
