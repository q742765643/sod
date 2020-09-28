<template>
  <section class="dynDialog">
    <el-form :model="msgFormDialog" ref="fromRef" label-width="120px">
      <el-form-item prop="apiSys" label="接口分类:">
        <el-select v-model="msgFormDialog.apiSys">
          <el-option
            :label="item.dictLabel"
            :value="item.dictValue"
            v-for="(item,index) in urlClassify"
            :key="index"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择文件" prop="ispublished">
        <el-upload
          class="upload-demo"
          :action="upLoadUrl"
          :auto-upload="false"
          ref="upload"
          :data="msgFormDialog"
          :limit="1"
          :on-exceed="onExceed"
          accept=".xlsx"
        >
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="模板下载:">
        <el-button @click="demoDownload" size="small" type="success">模板下载</el-button>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog()">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
var baseUrl = process.env.VUE_APP_PORTAL;
import { editById } from "@/api/portalMangement/feedbackMangement";
export default {
  name: "dynDialog",
  components: {},

  data() {
    return {
      urlClassify: [],
      upLoadUrl: baseUrl + "/cmadaas/sod/portal/apiManage/upload",
      //编辑页面列
      msgFormDialog: {
        apiSys: "",
      },
    };
  },
  async created() {
    this.getDicts("portal_api_mng").then((response) => {
      this.urlClassify = response.data;
      this.msgFormDialog.apiSys = this.urlClassify[0].dictValue;
    });
  },
  methods: {
    // 模板下载
    demoDownload() {
      /*  exportDemo({
        examineMaterial: this.msgFormDialog.examineMaterial
      }).then(res => {
        this.downloadfileCommon(res);
      }); */
    },
    onExceed() {
      this.$message({
        type: "error",
        message: "只能上传一个文件",
      });
    },
    trueDialog(formName) {
      this.$refs.upload.submit();
      if (res.code == 200) {
        this.$message({
          type: "success",
          message: "批量导入成功成功",
        });
        this.$emit("cancelDialog");
      }
    },

    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    },
  },
};
</script>

<style lang="scss">
.dynDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
