<template>
  <el-button type="success" @click="exportData()" icon="el-icon-download" size="small">{{btnText}}</el-button>
</template>

<script>
var baseUrl = process.env.VUE_APP_DM;
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

      window.location.href =
        baseUrl +
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