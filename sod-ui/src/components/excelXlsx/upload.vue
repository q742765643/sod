<template>
  <span>
    <input
      class="input-file"
      type="file"
      @change="exportData"
      accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
    />
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-upload"
      @click="btnClick"
    >{{btnText == undefined?'上传Excel表格数据':btnText}}</el-button>
  </span>
</template>
 
<script>
import XLSX from "xlsx";
export default {
  props: {
    btnText: {
      type: String,
    },
    uploadTableId: {
      type: String,
    },
    tableColumn: {
      type: Array,
    },
    tableType: {
      type: String,
    },
  },
  data() {
    return {};
  },
  methods: {
    btnClick() {
      console.log("upload==============>", this.tableType);
      if (this.uploadTableId) {
        document.querySelector(".input-file").id = this.uploadTableId;
        document.querySelector(".input-file").name = this.tableType;
        document.querySelector(".input-file").column = JSON.stringify(
          this.tableColumn
        );
      }
      document.querySelector(".input-file").click();
    },
    exportData(event) {
      if (!event.currentTarget.files.length) {
        return;
      }
      let fileValue = event.currentTarget.value;
      let fileArry = fileValue.split(".");
      if (fileArry.length > 0) {
        let flieType = fileArry[fileArry.length - 1];
        if (flieType != "xlsx" && flieType != "xls" && flieType != "csv") {
          this.$message({
            type: "error",
            message: "请选择Excel表格文件！",
          });
          return;
        }
      } else {
        this.$message({
          type: "error",
          message: "请选择Excel表格文件！",
        });
        return;
      }

      const that = this;
      // 拿取文件对象
      var f = event.currentTarget.files[0];
      // 用FileReader来读取
      var reader = new FileReader();
      // 重写FileReader上的readAsBinaryString方法
      FileReader.prototype.readAsBinaryString = function (f) {
        var binary = "";
        var wb; // 读取完成的数据
        var outdata; // 你需要的数据
        var reader = new FileReader();
        reader.onload = function (e) {
          // 读取成Uint8Array，再转换为Unicode编码（Unicode占两个字节）
          var bytes = new Uint8Array(reader.result);
          var length = bytes.byteLength;
          for (var i = 0; i < length; i++) {
            binary += String.fromCharCode(bytes[i]);
          }
          // 接下来就是xlsx了，具体可看api
          wb = XLSX.read(binary, {
            type: "binary",
          });
          outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]);
          // 自定义方法向父组件传递数据
          that.$emit(
            "getResult",
            outdata,
            document.querySelector(".input-file").id,
            document.querySelector(".input-file").name,
            document.querySelector(".input-file").column
          );
        };
        reader.readAsArrayBuffer(f);
      };
      reader.readAsBinaryString(f);
    },
  },
};
</script>
 
<style>
.input-file {
  display: none;
}
</style>