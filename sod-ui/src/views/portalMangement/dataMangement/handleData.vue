<template>
  <section class="dataDialog">
    <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
      <el-form-item prop="ddataId" label="四级编码:">
        <el-input clearable size="small" v-model="msgFormDialog.ddataId" />
      </el-form-item>
      <el-form-item prop="dataName" label="分类名称:">
        <el-input clearable size="small" v-model="msgFormDialog.dataName" />
      </el-form-item>
      <el-form-item prop="module" label="分类类别:">
        <el-select v-model="msgFormDialog.module">
          <el-option label="气象业务数据" value="1"></el-option>
          <el-option label="地球科学数据" value="2"></el-option>
          <el-option label="行业社会数据" value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-upload
          class="avatar-uploader"
          :action="upLoadUrl"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :headers="myHeaders"
          name="fileName"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item prop="serialNumber" label="排序:">
        <el-input-number clearable size="small" v-model="msgFormDialog.serialNumber" />
      </el-form-item>
      <el-form-item prop="isshow" label="是否显示:">
        <el-select v-model="msgFormDialog.isshow">
          <el-option label="是" value="Y"></el-option>
          <el-option label="否" value="N"></el-option>
        </el-select>
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
import { getToken, createSign } from "@/utils/auth";
import {
  getById,
  editById,
  homeDataSave,
} from "@/api/portalMangement/DataManagement";
var token = getToken();
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
      upLoadUrl: baseUrl + "/portal/fileManage/upload",
      myHeaders: { Authorization: token },
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
    handleAvatarSuccess(response, file, fileList) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.msgFormDialog.icon = response.data.filePath;
    },
    beforeAvatarUpload(file){
      const isPNG = file.type === 'image/png';
      if (!isPNG) {
        this.$message.error('上传图片只能是 PNG 格式!');
      }
      return isPNG;
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
            homeDataSave(this.msgFormDialog).then((res) => {
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
