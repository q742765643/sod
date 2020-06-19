<template>
  <el-button
    class="tableDownload"
    type="success"
    @click="exportData()"
    icon="el-icon-download"
    size="small"
  >{{btnText}}</el-button>
</template>

<script>
// const uuid = require("uuid/v4");
// import { createSign } from "@/utils/auth";
import { download } from "@/api/export";
export default {
  props: {
    handleExportObj: {
      type: Object
    },
    btnText: {
      type: String
    },
    exportUrl: {
      type: String
    },
    baseUrl: {
      type: String
    }
  },
  data() {
    return {
      loading: false
    };
  },
  created() {},
  methods: {
    exportData(formObj) {
      this.loading = true;
      let obj;
      if (formObj) {
        obj = formObj;
      } else {
        obj = this.handleExportObj;
      }
      /* const param = {
        timestamp: new Date().getTime(),
        nonce: uuid(),
        data: JSON.stringify(obj)
      };
      let sign = createSign(param);
      param.sign = sign; */

      let exportBase = "";
      if (this.baseUrl == "DM") {
        exportBase = process.env.VUE_APP_DM;
      }
      if (this.baseUrl == "SCHEDULE") {
        exportBase = process.env.VUE_APP_SCHEDULE_CENTER_API;
      }
      if (this.baseUrl == "UCENTER") {
        exportBase = process.env.VUE_APP_UCENTER_API;
      }
      if (this.baseUrl == "DMWL") {
        exportBase = process.env.VUE_APP_DMWLEI;
      }

      /*  window.location.href =
        exportBase +
        this.exportUrl +
        "?data=" +
        param.data +
        "&timestamp=" +
        param.timestamp +
        "&nonce=" +
        param.nonce +
        "&sign=" +
        param.sign; */
      let url = exportBase + this.exportUrl;
      console.log(obj);
      download(url, obj).then(res => {
        // 关闭loading
        this.loading = false;
        console.log(res);
        // 此处有个坑。这里用content保存文件流，最初是content=res，但下载的test.xls里的内容如下图1，
        // 检查了下才发现，后端对文件流做了一层封装，所以将content指向res.data即可
        // 另外，流的转储属于浅拷贝，所以此处的content转储仅仅是便于理解，并没有实际作用=_=
        const content = res;
        const blob = new Blob([content]); // 构造一个blob对象来处理数据
        const fileName = "table.xlsx"; // 导出文件名
        // 对于<a>标签，只有 Firefox 和 Chrome（内核） 支持 download 属性
        // IE10以上支持blob但是依然不支持download
        if ("download" in document.createElement("a")) {
          // 支持a标签download的浏览器
          const link = document.createElement("a"); // 创建a标签
          link.download = fileName; // a标签添加属性
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click(); // 执行下载
          URL.revokeObjectURL(link.href); // 释放url
          document.body.removeChild(link); // 释放标签
        } else {
          // 其他浏览器
          navigator.msSaveBlob(blob, fileName);
        }
      });
    }
  }
};
</script>

<style>
</style>