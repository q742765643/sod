<template>
  <section class="fileDialog">
    <el-form :model="msgFormDialog" ref="fromRef" :rules="baseFormRules" label-width="120px">
      <el-form-item label="文件类型：" prop="fileType">
        <el-select
          size="small"
          filterable
          v-model.trim="msgFormDialog.fileType"
          @change="fileTypeChange"
          :disabled="isDisabled"
          placeholder="请选择"
        >
          <el-option
            v-for="(item,index) in fileTypeBox"
            :key="index"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件类别：" prop="fileCategory">
        <el-select
          size="small"
          filterable
          v-model.trim="msgFormDialog.fileCategory"
          @change="fileCategoryChange"
          :disabled="isDisabled"
          placeholder="请选择"
        >
          <el-option
            v-for="(item,index) in fileCategoryBox"
            :key="index"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择文件" class="fileInput">
        <label slot="label">
          <i style="color:red;">*</i>&nbsp;选择文件：
        </label>
        <el-upload
          v-show="isHideAdd"
          class="upload-demo"
          accept="docx"
          drag
          :limit="1"
          :on-exceed="handleExceed"
          :action="upLoadUrl"
          :before-remove="beforeRemove"
          :on-success="successUpload"
          :on-error="errorUpload"
          

          :headers="myHeaders"
          name="fileName"
          multiple
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
        </el-upload>
        <el-input
          size="small"
          type="text"
          disabled
          v-model.trim="msgFormDialog.fileName"
          v-show="isHide"
          style="width:80%;"
        ></el-input>
      </el-form-item>
      <el-form-item prop="serialNumber" label="显示序号:">
        <el-input-number
          :min="0"
          class="number"
          size="small"
          v-model.trim="msgFormDialog.serialNumber"
          placeholder="请输入显示序号"
        />
      </el-form-item>
      <el-form-item prop="fileDesc" label="文件描述:">
        <el-input
          type="textarea"
          size="small"
          v-model.trim="msgFormDialog.fileDesc"
          placeholder="请输入文件描述"
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
  import { getToken, createSign } from "@/utils/auth";
  import {
    getDictDataByType,
    saveFileManage,
    editFileManage
  } from "@/api/portalMangement/fileMangement";
  var token = getToken();
  export default {
    name: "fileDialog",
    components: {},
    props: {
      handleObj: {
        type: Object
      }
    },
    data() {
      return {
        //编辑页面列
        msgFormDialog: {
          fileType: "",
          fileCategory: "",
          fileName: "",
          serialNumber: "",
          fileDesc: "",
          filePath: "",
          fileSuffix : "",
        },
        //文件类型是否可编辑
        isDisabled: false,
        isHideAdd: true,
        isHide: false,
        fileTypeBox : [],
        fileCategoryBox : [],
        upLoadUrl: baseUrl + "/portal/fileManage/upload",
        myHeaders: { Authorization: token },
        baseFormRules: {
          fileType: [
            { required: true, message: "请选择文件类型 ", trigger: "blur" }
          ],
          fileCategory: [
            { required: true, message: "请选择文件类别 ",trigger: "blur" }
          ],
          fileName: [
            { required: true, message: "请选择文件 ",trigger: "blur" }
          ],
          serialNumber: [
            { required: true, message: "请输入序号 ", trigger: "blur" }
          ],
          fileDesc: [
            { required: true, message: "请输入文件描述 ", trigger: "blur" }
          ]
        }
      };
    },
    async created() {
      // 文件分类
      await getDictDataByType("portal_file_type").then(res => {
        this.fileTypeBox = res.data;
      });

      if (this.handleObj.id) {
        this.isDisabled = true;
        this.isHideAdd = false;
        this.isHide = true;
        this.msgFormDialog = this.handleObj;
        console.log(this.msgFormDialog);
        await this.fileTypeChange(this.handleObj.fileType);
      } else {
        this.isDisabled = false;
        this.isHideAdd = true;
        this.isHide = false;
      }
    },
    methods: {
      trueDialog(formName) {
        if (this.isHideAdd && !this.msgFormDialog.fileName) {
          this.$message({
            message: "请选择文件",
            type: "error",
            offset: 100
          });
          return;
        }
        this.$refs[formName].validate(valid => {
          if (valid) {
            let obj = this.msgFormDialog;
            console.log(obj);
            if (this.handleObj.id) {
              editFileManage(obj).then(res => {
                if (res.code == 200) {
                  this.$message({
                    type: "success",
                    message: "编辑成功"
                  });
                  this.$emit("cancelDialog");
                } else {
                  this.$message({
                    type: "error",
                    message: res.msg
                  });
                }
              });
            } else {
              saveFileManage(obj).then(res => {
                if (res.code == 200) {
                  this.$message({
                    type: "success",
                    message: "增加成功"
                  });
                  this.$emit("cancelDialog");
                } else {
                  this.$message({
                    type: "error",
                    message: res.msg
                  });
                }
              });
            }
          } else {
            this.$message.error("请正确填写基本信息列表");
            return;
          }
        });
      },

      //取消按钮
      cancelDialog() {
        this.$emit("cancelDialog");
      },
      fileTypeChange(selectFileType){
        // 文件类别
        getDictDataByType(selectFileType).then(res => {
          this.fileCategoryBox = res.data;
        });
      },
      fileCategoryChange(selectFileCategory){

      },
      // 上传限制
      handleExceed() {
        this.$message.warning("当前限制选择1个文件");
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`, {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.msgFormDialog.fileName = "";
            this.msgFormDialog.filePath = "";
            this.msgFormDialog.fileSuffix = "";
          })
          .catch(() => {
            console.log("已取消删除");
          });
      },
      successUpload: function(response, file, fileList) {
        if(response.success){
          this.msgFormDialog.fileName = response.data.fileName;
          this.msgFormDialog.filePath = response.data.filePath;
          this.msgFormDialog.fileSuffix = response.data.fileSuffix;
        }
      },
      errorUpload:function (err, file, fileList) {
        this.$message.error("文件格式不正确");
      }

    }
  };
</script>

<style lang="scss">
  .fileDialog {
    .el-select,
    .el-textarea {
      width: 100%;
    }
  }
  .upload-demo {
    display: flex;
  }
</style>
