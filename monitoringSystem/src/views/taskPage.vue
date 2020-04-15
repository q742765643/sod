<template>
  <!--监测信息 -->
  <div class="taskTemp">
    <h4>
      <span>基础资源监控</span>
      <div>
        <img src="../assets/images/header-bg-cipas.png" />
      </div>
    </h4>
    <el-row :gutter="8" class="fristRow">
      <el-col :span="8">
        <div class="colBox">
          <h6>任务执行情况</h6>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <h6>集群各节点状态</h6>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <h6>kafka消息积压情况</h6>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="8" class="fristRow">
      <el-col :span="16">
        <div class="colBox">
          <h6>基础资源</h6>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <h6>任务执行基础资源</h6>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="8" class="fristRow">
      <el-col :span="24">
        <div class="colBox">
          <h6>RabbitMq监控</h6>
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
.taskTemp {
  width: 100%;
  height: 120vh;
  background: #193154;
  padding: 0 12px;
  width: 100%;
  height: 100vh;
  padding: 0;
  text-align: center;
  overflow: hidden;
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
  .colBox {
    background: url("../assets/images/bg-task.png") no-repeat;
    height: 100%;
    position: relative;
    h6 {
      font-size: 14px;
      color: #2a6897;
      font-weight: 700;
      padding: 7px 0px;
    }
    h6::before {
      content: "";
      background: url("../assets/images/dd1.png") no-repeat top left / 70px 10px; /*兼容没测*/
      position: absolute;
      top: 9px;
      right: calc(50% + 70px);
      z-index: 2;
      width: 70px;
      height: 10px;
    }
    h6::after {
      content: "";
      background: url("../assets/images/dd2.png") no-repeat top left / 70px 10px; /*兼容没测*/
      position: absolute;
      top: 9px;
      left: calc(50% + 70px);
      z-index: 2;
      width: 70px;
      height: 10px;
    }
  }
  .colBox::before {
    content: "";
    background: url("../assets/images/task1.png") no-repeat top left / 32px 32px; /*兼容没测*/
    position: absolute;
    top: 1px;
    right: 1px;
    z-index: 2;
    width: 32px;
    height: 32px;
  }

  .fristRow {
    padding: 5px 8px;
    height: calc((100vh) / 3);
    .el-col {
      height: 100%;
    }
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
