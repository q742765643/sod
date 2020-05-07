<template>
  <!-- docx 文件预览 -->
  <!-- https://github.com/mwilliamson/mammoth.js -->
  <!-- https://jstool.gitlab.io/zh-cn/demo/preview-ms-word-docx-document-in-browser/ -->
  <div class="previewDocxBox">
    <el-button
      class="tableDownload"
      type="success"
      @click="previewDocx()"
      icon="el-icon-document"
      size="small"
    >预览</el-button>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      append-to-body
      title="文件预览"
      :visible.sync="handleDialog"
      width="80%"
      top="2vh"
    >
      <div class="resultDocxBox"></div>
    </el-dialog>
  </div>
</template>

<script>
var mammoth = require("mammoth");
export default {
  props: {
    handleDocxObj: {
      type: Object
    }
  },
  data() {
    return {
      handleDialog: false
    };
  },
  created() {},
  methods: {
    previewDocx() {
      if (this.handleDocxObj && this.handleDocxObj.file) {
        this.handleDialog = true;
        let file = this.handleDocxObj.file;
        console.time();
        var reader = new FileReader();
        reader.onloadend = function(event) {
          var arrayBuffer = reader.result;
          // debugger

          mammoth
            .convertToHtml({ arrayBuffer: arrayBuffer })
            .then(function(resultObject) {
              let resultDocx = document.getElementsByClassName("resultDocxBox");
              resultDocx[0].innerHTML = resultObject.value;
            });
          console.timeEnd();

          /* 
        //  text
          mammoth
            .extractRawText({ arrayBuffer: arrayBuffer })
            .then(function(resultObject) {
              let resultDocx = document.getElementsByClassName("resultDocxBox");
              resultDocx[0].innerHTML = resultObject.value;
            });

        // Markdown
          mammoth
            .convertToMarkdown({ arrayBuffer: arrayBuffer })
            .then(function(resultObject) {
              result3.innerHTML = resultObject.value;
              console.log(resultObject.value);
            }); */
        };
        reader.readAsArrayBuffer(file);
      }
    },
    cancelHandle() {}
  }
};
</script>

<style>
.previewDocxBox {
  display: inline;
}
</style>