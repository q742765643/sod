<template>
  <section class="dataDialog">
    <el-form
      :model="msgFormDialog"
      :rules="baseFormRules"
      ref="fromRef"
      label-width="120px"
    >
      <el-form-item prop="classCode" label="应用类型:">
        <el-select v-model="msgFormDialog.classCode">
          <el-option label="国家级" value="C"></el-option>
          <el-option label="省级" value="P"></el-option>
          <el-option label="公有云" value="CC"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="province" label="省份:" v-if="this.msgFormDialog.classCode == 'P'">
        <el-input clearable size="small" v-model="msgFormDialog.province" />
      </el-form-item>
      <el-form-item prop="appName" label="应用名称:">
        <el-input clearable size="small" v-model="msgFormDialog.appName" placeholder="多个应用名称以逗号分隔"/>
      </el-form-item>
      <el-form-item prop="orgName" label="机构名称:" v-if="this.msgFormDialog.classCode != 'P'">
        <el-input clearable size="small" v-model="msgFormDialog.orgName" />
      </el-form-item>
      <el-form-item label="图标" prop="icon" v-if="this.msgFormDialog.classCode == 'C'">
        <img  v-show="handleDis" style="width:20px;height:20px;" :src=baseCode+baseIcon />
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
      <!--<el-form-item prop="num" label="应用个数:" v-if="this.msgFormDialog.classCode == 'P'">
        <el-input-number clearable size="small" v-model="msgFormDialog.num" />
      </el-form-item>-->
      <el-form-item prop="url" label="应用链接:" v-if="this.msgFormDialog.classCode == 'C'">
        <el-input clearable size="small" v-model="msgFormDialog.url" />
      </el-form-item>
      <el-form-item prop="serialNumber" label="排序:">
        <el-input-number
          clearable
          size="small"
          v-model="msgFormDialog.serialNumber"
        />
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
  typicalAppSave,
} from "@/api/portalMangement/typicalManagement";
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
      baseCode: "data:image/png;base64,",
      baseIcon : "",
      //编辑页面列
      msgFormDialog: {
        classCode: "",
        appName: "",
        orgName: "",
        url: "",
        isshow: "Y",
        serialNumber: "1",
        province:"",
        num:"0",
        icon: "",
      },
      baseFormRules: {
        classCode: [
          { required: true, message: "请选择应用级别", trigger: "blur" },
        ],
        province: [
          { required: true, message: "请输入省份 ", trigger: "blur" },
        ],
        appName: [
          { required: true, message: "请输入应用名称 ", trigger: "blur" },
        ],
        orgName: [
          { required: true, message: "请输入机构名称 ", trigger: "blur" },
        ],
        icon: [
          { required: true, message: "请选择图标 ", trigger: "blur" },
        ],
       /* num: [
          { required: true, message: "请输入应用个数 ", trigger: "blur" },
        ],*/
        url: [{ required: true, message: "请输入应用连接 ", trigger: "blur" }],
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
        this.baseIcon = this.msgFormDialog.icon;
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
