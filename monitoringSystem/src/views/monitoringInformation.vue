<template>
  <!--监测信息 -->
  <div class="infoTemp">
    <h4>监测信息</h4>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="colBox">
          <div id="cpuUsageChart" class="chartBox"></div>
          <el-scrollbar wrap-class="scrollbar-wrapper">
            <div class="filebox">
              <p class="title">磁盘可用量</p>
              <ul>
                <li v-for="(item, index) in fileList" :key="index">
                  <span class="fileTotal">{{item.total}}</span>
                  <span
                    class="progress"
                    style="width:' + item.usedPct * 100 + '%'"
                  >{{item.deviceName}}</span>
                </li>
              </ul>
            </div>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <div id="innerStorageChart" class="chartBox"></div>
          <div id="pieChart" class="chartBox"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <div id="networkFlowChart" class="chartBox"></div>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="processName" label="进程名称" min-width="150"></el-table-column>
            <el-table-column prop="processState" label="状态" min-width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.processState =='1'">
                  正常
                  <i class="iconRound successIcon"></i>
                </span>
                <span v-else>
                  异常
                  <i class="iconRound errorIcon"></i>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="timestamp" label="监测时间" min-width="150">
              <template slot-scope="scope1">
                <span>{{scope1.row.timestamp.substring(0,scope1.row.timestamp.length-4)}}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
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
      fileList: [],
      tableData: []
    };
  },
  created() {
    console.log(this.$route);
    this.axios
      .get(interfaceObj.processUsage + "?ip=" + this.$route.params.baseIp)
      .then(res => {
        if (res.status == 200) {
          this.tableData = res.data.data;
        }
        console.log(res);
      });
  },
  mounted() {
    this.cpuLine();
    this.innerStorageChart();
    this.pieChart();
    this.networkFlowChart();
  },
  methods: {
    cpuLine() {
      var myChart = echarts.init(document.getElementById("cpuUsageChart"));
      this.axios
        .get(interfaceObj.cpuUsage + "?hostname=" + this.$route.params.baseIp)
        .then(res => {
          if (res.status == 200) {
            let cpudata = [],
              xdata = [];
            if (res.data.data && res.data.data.length > 0) {
              let newA = res.data.data.reverse();
              newA.forEach((item, index) => {
                //let data1 = [item.totalPct*100];
                cpudata.push(item.totalPct * 100);
                if (index % 10 === 0 || index == res.data.data.length - 1) {
                  let fendata = item.timestamp.split(" ")[1].split(":");
                  xdata.push(fendata[0] + ":" + fendata[1] + ":" + fendata[2]);
                }
              });
              // 绘制图表
            }
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
                boundaryGap: false,
                axisLine: {
                  show: true,
                  lineStyle: {
                    color: "#fff"
                  }
                },
                data: xdata,
                axisLabel: {
                  // inside: true,
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
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
                min: 0,
                max: 100,
                interval: 25,
                //data: [0, 25, 50, 75, 100],
                axisLabel: {
                  formatter: "{value} %",
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
                  }
                }
              },
              series: [
                {
                  data: cpudata,
                  type: "line",
                  itemStyle: {
                    normal: {
                      color: "#b93633"
                    }
                  }
                }
              ]
            });
          }
          //console.log(res);
        });
    },
    innerStorageChart() {
      var myChart = echarts.init(document.getElementById("innerStorageChart"));
      this.axios
        .get(
          interfaceObj.memoryUsage + "?hostname=" + this.$route.params.baseIp
        )
        .then(res => {
          if (res.status == 200) {
            let sdata = [],
              xdata = [];
            if (res.data.data && res.data.data.length > 0) {
              let newA = res.data.data.reverse();
              newA.forEach((item, index) => {
                sdata.push(item.usedPct * 100);
                if (index % 10 === 0 || index == res.data.data.length - 1) {
                  let fendata = item.timestamp.split(" ")[1].split(":");
                  xdata.push(fendata[0] + ":" + fendata[1] + ":" + fendata[2]);
                }
              });
            }
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
                boundaryGap: false,
                axisLine: {
                  show: true,
                  lineStyle: {
                    color: "#fff"
                  }
                },
                data: xdata,
                axisLabel: {
                  // inside: true,
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
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
                min: 0,
                max: 100,
                interval: 25,
                axisLabel: {
                  formatter: "{value} %",
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
                  }
                }
              },
              series: [
                {
                  data: sdata,
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
          }
          //console.log(res);
        });
      // 绘制图表
    },
    pieChart() {
      var myChart = echarts.init(document.getElementById("pieChart"));
      // 绘制图表
      this.axios
        .get(interfaceObj.fileUsage + "?hostname=" + this.$route.params.baseIp)
        .then(res => {
          if (res.status == 200) {
            let cpdata = [],
              namedata = [];
            if (res.data.data) {
              res.data.data.forEach((item, index) => {
                let total = (item.free / 1024 / 1024 / 1024).toFixed(2) + "GB";
                this.fileList.push({
                  deviceName: item.deviceName,
                  usedPct: (item.free / item.total).toFixed(2),
                  total: total
                });
                let name =
                  item.deviceName + ":" + (item.usedPct * 100).toFixed(2) + "%";
                cpdata.push({
                  name: name,
                  value: item.usedPct * 100
                });
                namedata.push(name);
              });
            }
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
                formatter: "{b}"
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
                data: namedata
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
                  data: cpdata
                }
              ]
            });
          }
          //console.log(res);
        });
    },
    networkFlowChart() {
      var myChart = echarts.init(document.getElementById("networkFlowChart"));
      this.axios
        .get(
          interfaceObj.netWorkUsage + "?hostname=" + this.$route.params.baseIp
        )
        .then(res => {
          if (res.status == 200) {
            let indata = [],
              outdata = [],
              xdata = [];
            if (res.data.data && res.data.data.length > 0) {
              let newA = res.data.data.reverse();
              newA.forEach((item, index) => {
                indata.push(item.transferredIn);
                outdata.push(item.transferredOut);
                //if (index % 10 === 0 || index == res.data.data.length - 1) {
                let fendata = item.timestamp.split(" ")[1].split(":");
                xdata.push(fendata[0] + ":" + fendata[1] + ":" + fendata[2]);
                //}
              });
            }
            myChart.setOption({
              title: {
                text: "网络流量",
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
                boundaryGap: false,
                axisLine: {
                  show: true,
                  lineStyle: {
                    color: "#fff"
                  }
                },

                data: xdata,
                axisLabel: {
                  // inside: true,
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
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
                offset: -30,
                splitNumber: 7,
                axisLabel: {
                  formatter: "{value} kB",
                  textStyle: {
                    color: "#fff",
                    fontSize: 10
                  }
                }
              },
              series: [
                {
                  name: "网卡入口",
                  type: "line",
                  stack: "总量",
                  itemStyle: {
                    normal: {
                      color: "#2846ff"
                    }
                  },
                  data: indata
                },
                {
                  name: "网卡出口",
                  type: "line",
                  stack: "总量",
                  itemStyle: {
                    normal: {
                      color: "#45c9b1"
                    }
                  },
                  data: outdata
                }
              ]
            });
          }

          //console.log(res);
        });
    }
  }
};
</script>
<style lang="scss">
.infoTemp {
  width: 100%;
  height: 100vh;
  background: #1e4f86;
  padding: 0 12px;
  h4 {
    font-size: 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.5);
    position: relative;
    padding: 24px 20px 8px 35px;
  }
  h4:before {
    content: "";
    position: absolute;
    width: 150px;
    height: 2px;
    background: #1f99e5;
    left: 0;
    bottom: -1px;
  }
  .colBox {
    background: #183f6b;
    height: 100%;
  }
  .el-row {
    margin-top: 20px;
    height: calc(100vh - 70px);
    .el-col {
      height: 100%;
    }
    .chartBox {
      width: 100%;
      height: 50%;
    }
  }
  .title {
    font-size: 16px;
    padding-bottom: 15px;
  }
  .filebox {
    width: 96%;
    margin: auto;
    ul {
      padding-bottom: 20px;
    }
    li {
      display: flex;
      justify-items: center;
      margin-bottom: 4px;
    }
    span {
      font-size: 14px;
      font-weight: normal;
      height: 30px;
      line-height: 30px;
    }
    .fileTotal {
      width: 60px;
    }
    .progress {
      display: inline-block;
      // width: 20%;
      padding-left: 12px;
      background: #90ed7d;
    }
  }
  .iconRound {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    margin-left: 4px;
  }
  .successIcon {
    background: #24cbac;
  }
  .errorIcon {
    background: red;
  }
  .el-table th {
    background: #112e4f;
    font-size: 12px;
  }
  .el-table tr {
    background: #173c66;
    font-size: 12px;
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
}
</style>
