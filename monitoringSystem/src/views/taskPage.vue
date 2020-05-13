<template>
  <!--监测信息 -->
  <div class="taskTemp">
    <happy-scroll hide-horizontal color="rgba(0,0,0,0.1)" size="1">
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
            <div class="firstLineClass">
              <el-table :data="tableData" stripe style="width: 100%">
                <el-table-column prop="taskState" label="统计任务" align="center" min-width="80"></el-table-column>
                <el-table-column prop="backup" label="备份任务" align="center" min-width="80"></el-table-column>
                <el-table-column prop="move" label="迁移任务" align="center" min-width="80"></el-table-column>
                <el-table-column prop="clear" label="清除任务" align="center" min-width="80"></el-table-column>
              </el-table>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="colBox">
            <h6>集群各节点状态</h6>
            <el-row>
              <el-form :inline="true" :model="formInline" size="mini" class="demo-form-inline">
                <el-form-item label="更新时间：">
                  <span>{{formInline.time}}</span>
                </el-form-item>
                <el-form-item label="IP网段：">
                  <span>{{formInline.IP}}</span>
                </el-form-item>
                <el-form-item>
                  <span>
                    <i class="iconRound successIcon"></i>
                    正常({{formInline.normalNum}}个)
                  </span>
                  <span>
                    <i class="iconRound errorIcon"></i>
                    异常({{formInline.abnormalNum}}个)
                  </span>
                </el-form-item>
              </el-form>
            </el-row>
            <el-row class="contentClass">
              <span v-for="(item,index) in contentList" :key="index">
                <i v-if="item.isNormal==1" class="iconRound successIcon"></i>
                <i v-else class="iconRound errorIcon"></i>
                {{item.ip}}
              </span>
            </el-row>
            <el-row class="contentClass">
              <el-col :span="5">集群使用情况 :</el-col>
              <el-col :span="19">
                <el-progress :stroke-width="10" :percentage="formInline.diskPct"></el-progress>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="colBox">
            <h6>kafka消息积压情况</h6>
            <el-row class="firstLineClass">
              <el-col :span="12">
                <el-table :data="tableData1" stripe style="width: 100%">
                  <el-table-column prop="topic" label="列队名称" align="center" min-width="100"></el-table-column>
                  <el-table-column prop="consumerLag" label="挤压数" align="center" min-width="40"></el-table-column>
                </el-table>
              </el-col>
              <el-col :span="12">
                <el-table :data="tableData2" stripe style="width: 100%">
                  <el-table-column prop="topic" label="列队名称" align="center" min-width="100"></el-table-column>
                  <el-table-column prop="consumerLag" label="挤压数" align="center" min-width="40"></el-table-column>
                </el-table>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="8" class="secondRow">
        <el-col :span="16">
          <div class="colBox">
            <h6>基础资源</h6>
            <div class="baseContant">
              <el-row :gutter="15">
                <el-col :span="6" v-for="(item,index) in baseResourcesList" :key="index">
                  <div class="itemBox" @click="gotoNext(item)">
                    <div class="left">
                      <img src="../assets//images/base.png" alt />
                      <p>{{item.baseIp}}</p>
                    </div>
                    <div class="right">
                      <p v-for="(ele,eindex) in item.info" :key="eindex">
                        <span class="name">{{ele.name}}：</span>
                        <span>{{ele.value}}</span>
                      </p>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="colBox">
            <h6>任务执行基础资源</h6>
            <div class="baseContant smallbaseContant">
              <el-row :gutter="10">
                <el-col :span="12" v-for="(item,index) in baseResourcesList" :key="index">
                  <div class="itemBox" @click="gotoNext(item)">
                    <div class="left">
                      <img src="../assets//images/base.png" alt />
                      <p>{{item.baseIp}}</p>
                    </div>
                    <div class="right">
                      <p v-for="(ele,eindex) in item.info" :key="eindex">
                        <span class="name">{{ele.name}}：</span>
                        <span>{{ele.value}}</span>
                      </p>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="8" class="thridRow">
        <el-col :span="24">
          <div class="colBox">
            <h6>RabbitMq监控</h6>
            <el-row class="firstLineClass">
              <el-tabs v-model="editableTabsValue" type="card" @tab-click="getRabbitMonitorLag">
                <el-tab-pane
                  v-for="(item) in editableTabs"
                  :key="item.name"
                  :label="item.title"
                  :name="item.name"
                >
                  <el-table :data="item.content" stripe style="width: 100%">
                    <el-table-column
                      prop="queueName"
                      label="queueName"
                      align="center"
                      min-width="150"
                    ></el-table-column>
                    <el-table-column
                      prop="readyCount"
                      label="ReadyCount"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="readyDetailsRate"
                      label="ReadyDetail"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="totalCount"
                      label="TotalCount"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="totalDetailsRate"
                      label="TotalDetail"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="unacknowledgedCount"
                      label="UnackedCount"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="unacknowledgedDetailsRate"
                      label="UnackedDetail"
                      align="center"
                      min-width="100"
                    ></el-table-column>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </el-row>
          </div>
        </el-col>
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
      formInline: {},
      tableData: [
        {
          taskState: "失败",
          backup: "1",
          move: "0",
          clear: "0"
        },
        {
          taskState: "成功",
          backup: "1",
          move: "0",
          clear: "0"
        },
        {
          taskState: "未执行",
          backup: "1",
          move: "0",
          clear: "0"
        },
        {
          taskState: "应执行",
          backup: "1",
          move: "0",
          clear: "0"
        }
      ],
      tableData1: [
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        },
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        },
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        }
      ],
      tableData2: [
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        },
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        },
        {
          topic: "/zk437092645/article/details/8641486",
          consumerLag: "394"
        }
      ],
      contentList: [],
      baseResourcesList: [],
      editableTabsValue: "",
      editableTabs: [],
      tabIndex: 2
    };
  },
  created() {
    this.getTaskPerformance();
    this.getTaskKafkaMonitorLag();
    this.getRabbitAlias();
    this.getTaskClusterNode();
  },
  mounted() {},
  methods: {
    gotoNext(item) {
      let iii = JSON.stringify(item);
      this.$router.push({
        name: "监测信息",
        params: item
      });
      console.log(item);
    },
    /**获取任务执行情况*/
    getTaskPerformance() {
      this.axios.get(interfaceObj.groupbyTaskDuty).then(res => {
        if (res.status == 200) {
          this.tableData = res.data.data;
        }
      });
    },
    /**获取集群各节点状态*/
    getTaskClusterNode() {
      this.axios.get(interfaceObj.clusterMonitor).then(res => {
        if (res.status == 200) {
          this.formInline = res.data.data;
          this.formInline.diskPct =
            (this.formInline.diskPct * 100).toFixed(2) * 1;
          this.formInline.time = this.formatDateTime(new Date());
          if (res.data.data.cluserNode[0]) {
            let ipa = res.data.data.cluserNode[0].ip.split(".");
            this.formInline.IP = ipa[0] + ".XX.XX.XX";
          } else {
            this.formInline.IP = "XX.XX.XX.XX";
          }
          this.contentList = res.data.data.cluserNode;
          this.baseResourcesList = [];
          res.data.data.cluserNode.forEach(item => {
            let indexobj = {};
            indexobj.baseIp = item.ip;
            indexobj.info = [];
            indexobj.info.push({
              name: "CPU",
              value: (item.cpuPct * 100).toFixed(2) + "%"
            });
            indexobj.info.push({
              name: "内存",
              value: (item.memoryPct * 100).toFixed(2) + "%"
            });
            indexobj.info.push({
              name: "网络",
              value: this.getNormalText(item.netWorkIsNormal)
            });
            indexobj.info.push({
              name: "进程",
              value: this.getNormalText(item.processIsNormal)
            });
            indexobj.info.push({
              name: "磁盘",
              value: this.getNormalText(item.diskIsNormal)
            });
            this.baseResourcesList.push(indexobj);
          });
        }
      });
    },
    /**文本格式修改函数*/
    getNormalText(type) {
      if (type == 1) {
        return "正常";
      } else {
        return "异常";
      }
    },
    /**获取kafka积压情况*/
    getTaskKafkaMonitorLag() {
      this.axios.get(interfaceObj.kafkaAlias).then(res => {
        if (res.status == 200) {
          let alias = res.data.data[0];
          this.axios
            .get(interfaceObj.kafkaMonitorLag + "?alias=" + alias)
            .then(res => {
              //console.log(res);
              if (res.status == 200) {
                if (res.data.data.length < 4) {
                  if (res.data.data.length > 0) {
                    this.tableData1 = res.data.data;
                    this.tableData2 = [];
                  }
                } else if (res.data.data.length < 6) {
                  this.tableData1 = res.data.data.slice(0, 3);
                  this.tableData2 = res.data.data.slice(4);
                }
              }
            });
        }
      });
    },
    /**获取Rabbit监控表格*/
    getRabbitMonitorLag() {
        let urlobj = new FormData();
        urlobj.append("alias",this.editableTabsValue)
        //let urlobj = { alias: this.editableTabsValue };
      this.axios.post(interfaceObj.rabbitMonitorLag, urlobj).then(res => {
        if (res.status == 200) {
          this.editableTabs.forEach(item => {
            if (item.name == this.editableTabsValue) {
              item.content = res.data.data;
            }
          });
        }
        console.log(res);
      });
    },
    /**获取Rabbit监控*/
    getRabbitAlias() {
      this.axios.get(interfaceObj.rabbitAlias).then(res => {
        if (res.status == 200) {
          res.data.data.forEach(item => {
            let indexobj = { title: item, name: item, content: [] };
            this.editableTabs = [];
            this.editableTabs.push(indexobj);
          });
          this.editableTabsValue = res.data.data[0];
          this.getRabbitMonitorLag();
        }
      });
    },
    /**日期格式转化*/
    formatDateTime(date) {
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      var d = date.getDate();
      d = d < 10 ? "0" + d : d;
      var h = date.getHours();
      h = h < 10 ? "0" + h : h;
      var minute = date.getMinutes();
      minute = minute < 10 ? "0" + minute : minute;
      var second = date.getSeconds();
      second = second < 10 ? "0" + second : second;
      return y + "-" + m + "-" + d + " " + h + ":" + minute + ":" + second;
    }
  }
};
</script>
<style lang="scss">
.taskTemp {
  width: 100%;
  height: 100vh;
  background: #193154;
  padding: 0 0px;
  text-align: center;
  overflow: hidden;
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
    text-align: center;
  }
  .colBox {
    background: url("../assets/images/bg-task.png") no-repeat;
    height: 100%;
    position: relative;
    padding-bottom: 10px;
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
  .contentClass {
    padding: 5px 15px;
    font-size: 10px;
    text-align: left;
    height: 80px;
    color: #0ea9d6;
    font-weight: 600;
    .el-progress-bar__outer {
      background-color: #115ea7;
    }
    .el-progress-bar__inner {
      background-color: #03b8a1;
    }
    .el-progress__text {
      font-size: 10px !important;
      color: #0ea9d6;
      font-weight: 600;
    }
  }
  .fristRow {
    width: 99%;
    padding: 5px 8px;
    //margin-top: 5px;
    height: 185px;
    .el-col {
      height: 100%;
    }
  }
  .secondRow {
    width: 99%;
    padding: 5px 8px;
    margin-top: 10px;
    height: auto;
    .el-col {
      height: 100%;
    }
  }
  .thridRow {
    width: 99%;
    padding: 5px 8px;
    margin-bottom: 25px;
    height: auto;
    .el-col {
      height: 100%;
    }
  }

  /* .el-table th {
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
  } */
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
  .demo-form-inline {
    font-size: 12px;
    color: #0ea9d6;
    .el-form-item__label {
      padding: 0;
      color: #0ea9d6;
    }
    .el-form-item--mini.el-form-item,
    .el-form-item--small.el-form-item {
      margin: 5px 5px;
    }
    .el-form-item__label {
      font-size: 10px;
    }
    .el-form-item--mini .el-form-item__content,
    .el-form-item--mini .el-form-item__label {
      font-size: 10px;
      line-height: 14px;
    }
  }
  .firstLineClass {
    width: 92%;
    padding: 5px 0px;
    margin: 0 auto;
    .el-table,
    .el-table__expanded-cell {
      background-color: transparent;
    }
    /* .el-table--border,
    .el-table--group {
      border: none;
    }
    
    .el-table--border::after,
    .el-table--group::after {
      display: none;
    } */
    .el-table::before {
      //display: none;
      background-color: rgba(0, 0, 0, 0.2);
    }
    .el-table th,
    .el-table tr {
      background: transparent;
      background-color: transparent;
      padding: 4px 0;
      color: #008ec4;
      font-size: 10px;
      pointer-events: none;
    }
    .el-table__header th {
      box-shadow: 0px 0px 5px #13717c inset;
      border-bottom: 2px solid rgba(0, 0, 0, 0.2);
    }
    .el-table__header tr {
    }
    .el-table__body th {
      box-shadow: 0px 0px 5px #13717c inset;
    }
    .el-table__body td,
    .el-table__body th.is-leaf {
      border-bottom: none;
      border-right: 1px solid rgba(0, 0, 0, 0.2);
      border-left: 1px solid rgba(0, 0, 0, 0.2);
    }
    .el-table--striped .el-table__body tr.el-table__row--striped td {
      background: rgba(0, 0, 0, 0.2);
    }
    .el-table td {
      padding: 6px 0;
      color: #0ea9d6;
      font-weight: 600;
      //background: rgba(0, 0, 0, 0.2);
      box-shadow: none;
    }
    .el-table .cell {
      line-height: 13px;
    }
    /* .el-table--border td,
    .el-table--border th,
    .el-table__body-wrapper
      .el-table--border.is-scrolling-left
      ~ .el-table__fixed {
      border-right: 2px solid rgba(0, 0, 0, 0.2);
    } */
    .el-tabs--card > .el-tabs__header {
      border-bottom: none;
    }
    .el-tabs--card > .el-tabs__header .el-tabs__nav {
      border: none;
    }
    .el-tabs--card > .el-tabs__header .el-tabs__item:first-child {
      border-left: 1px solid #0e689f;
    }
    .el-tabs__item {
      border: 1px solid #0e689f;
      padding: 0px 10px !important;
      height: 20px;
      line-height: 20px;
      border-radius: 2px;
      font-size: 12px;
      font-weight: 500;
      color: #0e689f;
      margin: 5px 5px;
    }
    .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
      box-shadow: 0px 0px 5px #13717c;
      color: #13717c;
      font-weight: 600;
      border-bottom-color: #0e689f;
    }
  }
  .baseContant {
    width: 92%;
    padding: 2px 20px 10px 8px;
    margin: 0 auto;
    //border-right: 1px solid #115ea7;
    background: rgba(0, 0, 0, 0.2);
    .itemBox {
      display: flex;
      background: url("../assets/images/basebk.png") no-repeat top left / 15vw
        60px;
      width: 15vw;
      height: 60px;
      background-size: 100% 100%;
      padding: 5px 0px;
      margin-top: 5px;
      font-size: 10px;
      img {
        width: 32%;
        margin-bottom: 4px;
      }
      div {
        flex: 1;
        color: #5ccbe7;
        span.name {
          opacity: 0.75;
        }
      }
      .right {
        padding-left: 25px;
        p {
          text-align: left;
          padding-bottom: 4px;
          font-size: 9px;
        }
      }
    }
  }
  .smallbaseContant {
    padding: 5px 10px 10px 5px;
  }
}
</style>
