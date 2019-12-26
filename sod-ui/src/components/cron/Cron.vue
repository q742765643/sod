<template lang="html">
<el-dialog
      title="Cron表达式"
      :visible.sync="cronDialogVisible"
      :append-to-body="true"
      width="660px"
      :show-close="false"
      max-height="500px"
      top="1vh"
      :close-on-click-modal="false"
    >
  <div class="cron" :val="value_">
    <el-tabs v-model="activeName">
      <el-tab-pane label="秒" name="s">
        <second-and-minute v-model="sVal" lable="秒"></second-and-minute >
      </el-tab-pane>
      <el-tab-pane label="分" name="m">
        <second-and-minute v-model="mVal" lable="分"></second-and-minute >
      </el-tab-pane>
      <el-tab-pane label="时" name="h">
        <hour v-model="hVal" lable="时"></hour>
      </el-tab-pane>
      <el-tab-pane label="日" name="d">
        <day v-model="dVal" lable="日"></day>
      </el-tab-pane>
      <el-tab-pane label="月" name="month">
        <month v-model="monthVal" lable="月"></month>
      </el-tab-pane>
      <el-tab-pane label="周" name="week">
        <week v-model="weekVal" lable="周"></week>
      </el-tab-pane>
      <el-tab-pane label="年" name="year">
        <year v-model="yearVal" lable="年"></year>
      </el-tab-pane>
    </el-tabs>
    <!-- table -->
    <el-table
       :data="tableData"
       size="mini"
       border
       style="width: 100%;">
       <el-table-column
         prop="sVal"
         label="秒"
         width="70">
       </el-table-column>
       <el-table-column
         prop="mVal"
         label="分"
         width="70">
       </el-table-column>
       <el-table-column
         prop="hVal"
         label="时"
         width="70">
       </el-table-column>
       <el-table-column
         prop="dVal"
         label="日"
         width="70">
       </el-table-column>
       <el-table-column
         prop="monthVal"
         label="月"
         width="70">
       </el-table-column>
       <el-table-column
         prop="weekVal"
         label="周"
         width="70">
       </el-table-column>
       <el-table-column
         prop="yearVal"
         label="年">
       </el-table-column>
     </el-table>
     <el-input class="cronVal" size="small" v-model="value_" auto-complete="off"></el-input>
      <div class="dialogFooter">
        <el-button type="primary" size="small" @click="cronSure">确定</el-button>
        <el-button size="small" @click="cronCancel">取消</el-button>
      </div>
  </div>
  </el-dialog>
</template>

<script>
import SecondAndMinute from "./secondAndMinute";
import hour from "./hour";
import day from "./day";
import month from "./month";
import week from "./week";
import year from "./year";
// import { interfaceObj } from "@/urlConfig";

export default {
  props: {
    value: {
      type: String
    },
    cronDialogVisible: {
      type: Boolean
    }
  },
  data() {
    return {
      //
      activeName: "s",
      sVal: "",
      mVal: "",
      hVal: "",
      dVal: "",
      monthVal: "",
      weekVal: "",
      yearVal: "",
      cronExpression: ""
    };
  },
  watch: {
    value(a, b) {
      this.updateVal();
    }
  },
  computed: {
    tableData() {
      return [
        {
          sVal: this.sVal,
          mVal: this.mVal,
          hVal: this.hVal,
          dVal: this.dVal,
          monthVal: this.monthVal,
          weekVal: this.weekVal,
          yearVal: this.yearVal
        }
      ];
    },
    value_() {
      if (!this.dVal && !this.weekVal) {
        return "";
      }
      if (this.dVal === "?" && this.weekVal === "?") {
        this.$message.error("日期与星期不可以同时为“不指定”");
      }
      if (this.dVal !== "?" && this.weekVal !== "?") {
        this.$message.error("日期与星期必须有一个为“不指定”");
      }
      let v = `${this.sVal} ${this.mVal} ${this.hVal} ${this.dVal} ${this.monthVal} ${this.weekVal} ${this.yearVal}`;
      if (v !== this.value) {
        this.$emit("input", v);
      }
      this.cronExpression = v;
      return v;
    }
  },
  methods: {
    updateVal() {
      if (!this.value) {
        return;
      }
      let arrays = this.value.split(" ");
      this.sVal = arrays[0];
      this.mVal = arrays[1];
      this.hVal = arrays[2];
      this.dVal = arrays[3];
      this.monthVal = arrays[4];
      this.weekVal = arrays[5];
      this.yearVal = arrays[6];
    },
    // cron表达式确定
    cronSure() {
      //对特殊字符进行编码
      this.axios
        .get(interfaceObj.listNext5Val, {params: {cron: this.cronExpression}})
        .then(res => {
          if (res.data&&res.data.data.length > 0) {
            let fiveHtml = "<ul>";
            const resData = res.data.data;
            resData.forEach(single => {
              fiveHtml += "<li>" + single + "</li>";
            });
            fiveHtml += "</ul>";
            this.$confirm(fiveHtml, "cron表达式的5次执行时间", {
              dangerouslyUseHTMLString: true,
              confirmButtonText: "保存",
              cancelButtonText: "重新选择",
              closeOnClickModal: false
            })
              .then(() => {
                //  设置cron表达式的值   关闭弹出层
                this.$emit("setCron", this.cronExpression);
                this.$message({
                  type: "success",
                  message: "保存成功!"
                });
              })
              .catch(() => {
                this.$message({
                  type: "info",
                  message: "重新选择"
                });
              });
          } else {
            this.$message({
              type: "error",
              message: "选择的cron表达式不存在五次执行时间，请重新选择"
            });
          }
        })
        .catch(error => {
          console.log("出错了，这是错误信息：" + error);
        });
    },
    // cron表达式取消
    cronCancel() {
      this.$emit("closeCron");
    }
  },
  created() {
    this.updateVal();
  },
  components: {
    SecondAndMinute,
    hour,
    day,
    month,
    week,
    year
  }
};
</script>

<style lang="scss">
.cron {
  text-align: left;
  padding: 10px;
  background: #fff;
  border: 1px solid #dcdfe6;
  .el-tabs__content {
    line-height: 40px;
  }
  .el-checkbox {
    margin-right: 0px;
  }
  .el-table th {
    padding: 4px 0;
    line-height: 0px;
  }
}
</style>
