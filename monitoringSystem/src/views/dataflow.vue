<template>
  <!--监测信息 -->
  <div class="flowTemp">
    <happy-scroll hide-horizontal color="rgba(0,0,0,0.1)" size="1">
      <h4>
        <span>数据全流程</span>
        <div>
          <img src="../assets/images/header-bg-cipas.png" />
        </div>
      </h4>
      <el-row class="searchDiv">
        <el-form :inline="true" :model="formInline">
          <el-form-item label="四级编码:">
            <el-input v-model="formInline.dataType" size="mini" placeholder="请输入四级编码"></el-input>
            <!-- <el-select v-model="formInline.region" size="mini" placeholder="活动区域">
              <el-option label="区域一" value="shanghai"></el-option>
              <el-option label="区域二" value="beijing"></el-option>
            </el-select>-->
          </el-form-item>
          <el-form-item label="资料名称:">
            <el-input v-model="formInline.name" size="mini" placeholder="请输入资料名称"></el-input>
          </el-form-item>
          <el-form-item label="最新时次:">
            <el-date-picker
              v-model="formInline.ddateTime"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              size="mini"
              placeholder="选择日期时间"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="searchTable">查询</el-button>
            <el-button type="primary" size="mini" @click="clearSearch">清空条件</el-button>
          </el-form-item>
        </el-form>
      </el-row>
      <el-row class="tableDiv">
        <el-table :data="tableData" style="width: 100%;">
          <el-table-column prop="name" label="资料名称" align="center" min-width="150"></el-table-column>
          <el-table-column prop="ddateTime" label="最新时次" align="center" min-width="50"></el-table-column>
          <!-- <el-table-column label="采集" align="center">
          <el-table-column prop="name" align="center" label="站台可用率GTS实收数" min-width="80"></el-table-column>
          </el-table-column>-->
          <el-table-column label="收集" align="center">
            <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="100">
              <template slot-scope="scope2">
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope2.row.collection_receivable*1-scope2.row.collection_realIncome*1}}</el-link>
                <span>/</span>
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope2.row.collection_realIncome}}</el-link>
                <span>/</span>
                <el-link
                  type="primary"
                  class="munberText"
                  :underline="false"
                >{{scope2.row.collection_receivable}}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="及时率" align="center" min-width="70">
              <template slot-scope="scope6">
                <el-progress
                  :text-inside="true"
                  :stroke-width="15"
                  :percentage="scope6.row.collection_jishi"
                  status="success"
                ></el-progress>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="到报率" align="center" min-width="70">
              <template slot-scope="scope7">
                <el-progress
                  :text-inside="true"
                  :stroke-width="15"
                  :percentage="scope7.row.collection_daobao"
                  status="success"
                ></el-progress>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="分省到报率" align="center" min-width="70">
              <template slot-scope="scope5">
                <el-link type="primary" :underline="false">点击查看</el-link>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="分发" align="center">
            <el-table-column prop label="目的地" align="center" min-width="70"></el-table-column>
            <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="120">
              <template slot-scope="scope4">
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope4.row.distribute_receivable*1-scope4.row.distribute_realIncome*1}}</el-link>
                <span>/</span>
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope4.row.distribute_realIncome}}</el-link>
                <span>/</span>
                <el-link
                  type="primary"
                  class="munberText"
                  :underline="false"
                >{{scope4.row.distribute_receivable}}</el-link>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="处理入库" align="center">
            <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="120">
              <template slot-scope="scope3">
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope3.row.put_receivable*1-scope3.row.put_realIncome*1}}</el-link>
                <span>/</span>
                <el-link
                  type="success"
                  class="munberText"
                  :underline="false"
                >{{scope3.row.put_realIncome}}</el-link>
                <span>/</span>
                <el-link
                  type="primary"
                  class="munberText"
                  :underline="false"
                >{{scope3.row.put_receivable}}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="入库率" align="center" min-width="70">
              <template slot-scope="scope9">
                <el-progress
                  :text-inside="true"
                  :stroke-width="15"
                  :percentage="scope9.row.input_lu"
                  status="success"
                ></el-progress>
                <span>{{scope9.row.dataText}}</span>
                <div>
                  <span>{{scope9.row.timeText}}</span>
                </div>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column prop="date" label="历史详情" align="center" min-width="100">
            <template slot-scope="scope1">
              <el-link type="primary" :underline="false">历史</el-link>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </happy-scroll>
  </div>
</template>

<script>
var echarts = require("echarts");
import { interfaceObj } from "@/urlConfig.js";
//分页组件
export default {
  data() {
    return {
      formInline: {
        name: "",
        dataType: "",
        ddateTime: ""
      },
      tableData: [
        {
          date: "2016-05-03",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-02",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-08",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-06",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        },
        {
          date: "2016-05-07",
          name: "王小虎",
          province: "上海",
          city: "普陀区",
          address: "上海市普陀区金沙江路 1518 弄",
          zip: 200333
        }
      ]
    };
  },
  created() {
    this.getflowMonitorList("");
  },
  mounted() {},
  methods: {
    clearSearch() {
      this.formInline = {
        name: "",
        dataType: "",
        ddateTime: ""
      };
      this.getflowMonitorList("");
    },
    searchTable() {
      let searchtext = "";
      if (this.formInline.name) {
        searchtext = searchtext + "&name=" + this.formInline.name;
      }
      if (this.formInline.dataType) {
        searchtext = searchtext + "&dataType=" + this.formInline.dataType;
      }
      if (this.formInline.ddateTime) {
        let fff = this.formInline.ddateTime.split(" ")[1].split(":");
        let endtime =
          this.formInline.ddateTime.split(" ")[0] +
          " " +
          fff[0] +
          ":" +
          fff[1] +
          ":" +
          (fff[2] * 1 + 1).toString();
        searchtext =
          searchtext +
          "&startTime=" +
          this.formInline.ddateTime +
          "&endTime=" +
          endtime;
      }
      if (searchtext.length > 0) {
        searchtext = searchtext.substr(1);
        searchtext = "?" + searchtext;
      }
      //console.log(searchtext);
      this.getflowMonitorList(searchtext);
    },
    getflowMonitorList(txt) {
      this.axios.get(interfaceObj.flowMonitorList + txt).then(res => {
        this.tableData = res.data.data;
        this.tableData.forEach(item => {
          item.dataText = item.ddateTime.split(" ")[0];
          item.timeText = item.ddateTime.split(" ")[1];
          item.collection_jishi =
            ((item.timely * 100) / item.collection_receivable).toFixed(2) * 1;
          item.collection_daobao =
            (
              (item.collection_realIncome * 100) /
              item.collection_receivable
            ).toFixed(2) * 1;
          item.input_lu =
            ((item.put_realIncome * 100) / item.put_receivable).toFixed(2) * 1;
        });
      });
    }
  }
};
</script>
<style lang="scss">
.flowTemp {
  width: 100%;
  height: 100vh;
  padding: 0;
  text-align: center;
  background: #1e4f86;
  .happy-scroll {
    width: 100%;
    .happy-scroll-container {
      width: 100%;
      .happy-scroll-content {
        width: 100%;
        display: block;
      }
    }
  }
  //padding:10px;
  h4 {
    height: 50px;
    line-height: 30px;
    span {
      font-size: 26px;
      background: linear-gradient(to bottom, #0e9ce0, #fff);
      font-weight: 600;
      -webkit-background-clip: text;
      color: transparent;
    }
    div {
      line-height: 15px;
    }
    img {
      height: 15px;
    }
    position: relative;
    padding: 0px;
    text-align: center;
  }
  .searchDiv {
    height: 50px;
    width: 100%;
    padding: 5px 15px;
    color: #fff;
    text-align: left;
    .el-form-item__label {
      color: #fff;
    }
    .el-input__inner {
      background-color: transparent;
      border: 1px solid #2477b2;
    }
    .el-button--primary {
      background: transparent;
      border-color: #2477b2;
    }
    .el-button--primary:focus,
    .el-button--primary:hover {
      background: #409eff;
    }
  }
  .tableDiv {
    margin: auto;
    //width: 94%;
    height: calc(100vh - 102px);
    border: 1px solid #2477b2;
    .el-table {
      border: none;
    }
    .el-table::before {
      display: none;
    }
    .el-table--border::after,
    .el-table--group::after {
      display: none;
    }
    .el-progress-bar__outer,
    .el-progress-bar__inner {
      border-radius: 5px;
    }
    .el-progress {
      padding: 0px 5px;
    }
    .el-link {
      font-size: 10px;
    }
    .munberText {
      font-style: italic;
    }
    .munberText:before {
      content: "";
      position: absolute;
      width: 100%;
      height: 2px;
      background: #fff;
      left: 0;
      bottom: -1px;
    }
    .el-table th {
      background: #112e4f;
      font-size: 14px;
      padding: 5px 0;
    }
    .el-table th.is-leaf {
      font-size: 10px;
      padding: 5px 0;
    }
    el-table .cell,
    .el-table th div,
    .el-table--border td:first-child .cell,
    .el-table--border th:first-child .cell {
      padding-left: 0px;
    }
    .el-table .cell,
    .el-table th div,
    .el-table--border td:first-child .cell,
    .el-table--border th:first-child .cell {
      padding-left: 0px;
    }
    .el-table .cell,
    .el-table th div {
      padding-right: 0px;
    }
    .el-table tr {
      background: #173c66;
      padding: 5px 0;
    }
    .el-table td {
      font-size: 10px;
    }
    .el-table,
    .el-table thead,
    .el-table tbody {
      color: #fff;
    }
    .el-table td,
    .el-table th.is-leaf {
      border-bottom: 1px solid #2a5f9c;
    }
    .el-table--enable-row-hover .el-table__body tr:hover > td {
      background-color: #112e4f !important;
    }
    .el-table .cell {
      line-height: 15px;
    }
    .el-table--border th,
    .el-table__fixed-right-patch {
      border-bottom: 1px solid #2a5f9c;
    }
    .el-table--border td,
    .el-table--border th,
    .el-table__body-wrapper
      .el-table--border.is-scrolling-left
      ~ .el-table__fixed {
      border-right: 1px solid #2a5f9c;
    }
  }
}
</style>
