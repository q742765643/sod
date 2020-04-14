<template>
  <!--监测信息 -->
  <div class="flowTemp">
    <h4>
      <span>数据全流程</span>
      <div>
        <img src="../assets/images/header-bg-cipas.png" />
      </div>
    </h4>
    <el-row class="searchDiv">
      <el-form :inline="true" :model="formInline">
        <el-form-item label="四级编码:">
          <el-select v-model="formInline.region" size="mini" placeholder="活动区域">
            <el-option label="区域一" value="shanghai"></el-option>
            <el-option label="区域二" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资料名称:">
          <el-input v-model="formInline.name" size="mini" placeholder="请输入资料名称"></el-input>
        </el-form-item>
        <el-form-item label="最新时次:">
          <el-date-picker
            v-model="formInline.time"
            type="datetime"
            size="mini"
            placeholder="选择日期时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini">清空条件</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-row class="tableDiv">
      <el-table :data="tableData" style="width: 100%;">
        <el-table-column prop="date" label="资料名称" align="center" min-width="150"></el-table-column>
        <el-table-column prop="date" label="最新时次" align="center" min-width="70"></el-table-column>
        <el-table-column label="采集" align="center">
          <el-table-column prop="name" align="center" label="站台可用率GTS实收数" min-width="80"></el-table-column>
        </el-table-column>
        <el-table-column label="收集" align="center">
          <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="100">
            <template slot-scope="scope2">
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="primary" class="munberText" :underline="false">1</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="及时率" align="center" min-width="70">
            <template slot-scope="scope6">
              <el-progress :text-inside="true" :stroke-width="15" :percentage="0" status="success"></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="到报率" align="center" min-width="70">
            <template slot-scope="scope6">
              <el-progress
                :text-inside="true"
                :stroke-width="15"
                :percentage="100"
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
          <el-table-column prop="name" label="目的地" align="center" min-width="70"></el-table-column>
          <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="120">
            <template slot-scope="scope4">
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="primary" class="munberText" :underline="false">1</el-link>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="处理入库" align="center">
          <el-table-column prop="name" label="收缺/实收/应收" align="center" min-width="120">
            <template slot-scope="scope3">
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="success" class="munberText" :underline="false">1</el-link>
              <span>/</span>
              <el-link type="primary" class="munberText" :underline="false">1</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="入库率" align="center" min-width="70">
            <template slot-scope="scope6">
              <el-progress :text-inside="true" :stroke-width="15" :percentage="30" status="success"></el-progress>
              <span>2020-04-14</span>
              <div>
                <span>17:23:44</span>
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
  </div>
</template>

<script>
var echarts = require("echarts");
import { interfaceObj } from "@/urlConfig.js";
//分页组件
export default {
  data() {
    return {
      formInline: {},
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
  created() {},
  mounted() {
    /* this.cpuLine();
    this.innerStorageChart();
    this.pieChart();
    this.networkFlowChart(); */
  },
  methods: {
    cpuLine() {
      var myChart = echarts.init(document.getElementById("cpuUsageChart"));
      // 绘制图表
      myChart.setOption({
        title: {
          text: "cpu使用率情况",
          left: "left",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#fff",
            top: 10
          }
        },
        tooltip: {
          trigger: "axis"
        },
        color: ["#058ae5"],
        calculable: true,
        xAxis: {
          type: "category",
          axisLine: {
            show: true,
            lineStyle: {
              color: "#fff"
            }
          },

          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#fff"
            }
          }
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            textStyle: {
              color: "#fff"
            }
          }
        },
        series: [
          {
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: "line",
            itemStyle: {
              normal: {
                color: "#b93633"
              }
            }
          }
        ]
      });
    },
    innerStorageChart() {
      var myChart = echarts.init(document.getElementById("innerStorageChart"));
      // 绘制图表
      myChart.setOption({
        title: {
          text: "内存使用率",
          left: "left",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#fff",
            top: 10
          }
        },
        tooltip: {
          trigger: "axis"
        },
        color: ["#058ae5"],
        calculable: true,
        xAxis: {
          type: "category",
          axisLine: {
            show: true,
            lineStyle: {
              color: "#fff"
            }
          },

          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#fff"
            }
          }
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            textStyle: {
              color: "#fff"
            }
          }
        },
        series: [
          {
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: "line",
            itemStyle: {
              normal: {
                color: "#2344d3"
              }
            },
            areaStyle: {}
          }
        ]
      });
    },
    pieChart() {
      var myChart = echarts.init(document.getElementById("pieChart"));
      // 绘制图表
      myChart.setOption({
        title: {
          text: "磁盘使用率",
          left: "left",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#fff",
            top: 10
          }
        },
        tooltip: {
          trigger: "axis"
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: 10,
          top: 30,
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#fff"
          },
          data: ["直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎"]
        },
        series: [
          {
            name: "访问来源",
            type: "pie",
            radius: ["50%", "70%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center"
            },
            emphasis: {
              label: {
                show: true,
                fontSize: "30",
                fontWeight: "bold"
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 335, name: "直接访问" },
              { value: 310, name: "邮件营销" },
              { value: 234, name: "联盟广告" },
              { value: 135, name: "视频广告" },
              { value: 1548, name: "搜索引擎" }
            ]
          }
        ]
      });
    },
    networkFlowChart() {
      var myChart = echarts.init(document.getElementById("networkFlowChart"));
      myChart.setOption({
        title: {
          text: "cpu使用率情况",
          left: "left",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#fff",
            top: 10
          }
        },
        tooltip: {
          trigger: "axis"
        },
        color: ["#058ae5"],
        calculable: true,
        xAxis: {
          type: "category",
          axisLine: {
            show: true,
            lineStyle: {
              color: "#fff"
            }
          },

          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          axisLabel: {
            // inside: true,
            textStyle: {
              color: "#fff"
            }
          }
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            textStyle: {
              color: "#fff"
            }
          }
        },
        series: [
          {
            name: "邮件营销",
            type: "line",
            stack: "总量",
            itemStyle: {
              normal: {
                color: "#2846ff"
              }
            },
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: "联盟广告",
            type: "line",
            stack: "总量",
            itemStyle: {
              normal: {
                color: "#45c9b1"
              }
            },
            data: [220, 182, 191, 234, 290, 330, 310]
          }
        ]
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
    width: 99%;
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
