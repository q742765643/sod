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
const uuid = require("uuid/v4");
import { createSign } from "@/utils/auth";
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
    return {};
  },
  created() {},
  methods: {
    exportData() {
      let obj = this.handleExportObj;
      const param = {
        timestamp: new Date().getTime(),
        nonce: uuid(),
        data: JSON.stringify(obj)
      };
      let sign = createSign(param);
      param.sign = sign;

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

      window.location.href =
        exportBase +
        this.exportUrl +
        "?data=" +
        param.data +
        "&timestamp=" +
        param.timestamp +
        "&nonce=" +
        param.nonce +
        "&sign=" +
        param.sign;
    }
  }
};
</script>

<style>
</style>