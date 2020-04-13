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
                  <span class="progress">{{item.deviceName}}</span>
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
            <el-table-column prop="name" label="进程名称"></el-table-column>
            <el-table-column prop="address" label="状态" width="180">
              <template slot-scope="scope">
                <span v-if="scope.row.address =='正常'">
                  正常
                  <i class="iconRound successIcon"></i>
                </span>
                <span v-else>
                  异常
                  <i class="iconRound errorIcon"></i>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="date" label="监测时间"></el-table-column>
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
      fileList: [
        { deviceName: "/CMADAAS", usedPct: "20", total: "100GB" },
        { deviceName: "/CMADAAS2", usedPct: "30", total: "100GB" }
      ],
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "正常"
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "正常"
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "正常"
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "正常"
        }
      ]
    };
  },
  created() {},
  mounted() {
    this.cpuLine();
    this.innerStorageChart();
    this.pieChart();
    this.networkFlowChart();
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
      width: 20%;
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
  .el-table th {
    background: #112e4f;
  }
  .el-table tr {
    background: #173c66;
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
