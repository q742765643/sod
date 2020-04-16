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
          <div class="firstLineClass">
            <el-table :data="tableData" stripe style="width: 100%">
              <el-table-column prop="name" label="统计任务" align="center" min-width="80"></el-table-column>
              <el-table-column prop="back" label="备份任务" align="center" min-width="80"></el-table-column>
              <el-table-column prop="qianyi" label="迁移任务" align="center" min-width="80"></el-table-column>
              <el-table-column prop="clear" label="清楚任务" align="center" min-width="80"></el-table-column>
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
                  正常({{formInline.ok}}个)
                </span>
                <span>
                  <i class="iconRound errorIcon"></i>
                  异常({{formInline.ok}}个)
                </span>
              </el-form-item>
            </el-form>
          </el-row>
          <el-row class="contentClass">
            <span v-for="(item,index) in contentList" :key="index">
              <i v-if="item.type==1" class="iconRound successIcon"></i>
              <i v-else class="iconRound errorIcon"></i>
              正常
            </span>
          </el-row>
          <el-row>
            <el-form :inline="true" :model="formInline" size="mini" class="demo-form-inline">
              <!--  <el-form-item label="集群使用情况"> -->
              <el-progress :text-inside="true" :stroke-width="14" :percentage="70"></el-progress>
              <!--   </el-form-item> -->
            </el-form>
          </el-row>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="colBox">
          <h6>kafka消息积压情况</h6>
          <el-row class="firstLineClass">
            <el-col :span="12">
              <el-table :data="tableData1" stripe style="width: 100%">
                <el-table-column prop="name" label="列队名称" align="center" min-width="100"></el-table-column>
                <el-table-column prop="back" label="挤压数" align="center" min-width="40"></el-table-column>
              </el-table>
            </el-col>
            <el-col :span="12">
              <el-table :data="tableData1" stripe style="width: 100%">
                <el-table-column prop="name" label="列队名称" align="center" min-width="100"></el-table-column>
                <el-table-column prop="back" label="挤压数" align="center" min-width="40"></el-table-column>
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
                <div class="itemBox">
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
                <div class="itemBox">
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
      formInline: {
        time: "2020-04-15 17:49:02",
        IP: "192.168.1.2",
        ok: 5
      },
      tableData: [
        {
          name: "失败",
          back: "1",
          qianyi: "0",
          clear: "0"
        },
        {
          name: "失败",
          back: "1",
          qianyi: "0",
          clear: "0"
        },
        {
          name: "失败",
          back: "1",
          qianyi: "0",
          clear: "0"
        },
        {
          name: "失败",
          back: "1",
          qianyi: "0",
          clear: "0"
        }
      ],
      tableData1: [
        {
          name: "/zk437092645/article/details/8641486",
          back: "394"
        },
        {
          name: "/zk437092645/article/details/8641486",
          back: "394"
        },
        {
          name: "/zk437092645/article/details/8641486",
          back: "394"
        }
      ],
      contentList: [
        { index: 1, type: 1 },
        { index: 1, type: 1 },
        { index: 1, type: 1 },
        { index: 1, type: 1 },
        { index: 1, type: 1 }
      ],
      baseResourcesList: [
        {
          baseIp: "10.20.64.41",
          info: [
            { name: "CPU", value: "4.1%" },
            { name: "内存", value: "75.4%" },
            { name: "网络", value: "正常" },
            { name: "进程", value: "正常" },
            { name: "磁盘", value: "正常" }
          ]
        },
        {
          baseIp: "10.20.64.41",
          info: [
            { name: "CPU", value: "4.1%" },
            { name: "内存", value: "75.4%" },
            { name: "网络", value: "正常" },
            { name: "进程", value: "正常" },
            { name: "磁盘", value: "正常" }
          ]
        },
        {
          baseIp: "10.20.64.41",
          info: [
            { name: "CPU", value: "4.1%" },
            { name: "内存", value: "75.4%" },
            { name: "网络", value: "正常" },
            { name: "进程", value: "正常" },
            { name: "磁盘", value: "正常" }
          ]
        },
        {
          baseIp: "10.20.64.41",
          info: [
            { name: "CPU", value: "4.1%" },
            { name: "内存", value: "75.4%" },
            { name: "网络", value: "正常" },
            { name: "进程", value: "正常" },
            { name: "磁盘", value: "正常" }
          ]
        },
        {
          baseIp: "10.20.64.41",
          info: [
            { name: "CPU", value: "4.1%" },
            { name: "内存", value: "75.4%" },
            { name: "网络", value: "正常" },
            { name: "进程", value: "正常" },
            { name: "磁盘", value: "正常" }
          ]
        }
      ]
    };
  },
  created() {},
  mounted() {},
  methods: {}
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
  .contentClass {
    padding: 5px 15px;
    font-size: 10px;
    text-align: left;
    height: 80px;
  }
  .fristRow {
    padding: 5px 8px;
    height: 185px;
    .el-col {
      height: 100%;
    }
  }
  .secondRow {
    padding: 5px 8px;
    height: 290px;
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
    .el-form-item__label {
      padding: 0;
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
