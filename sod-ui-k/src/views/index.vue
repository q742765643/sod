<template>
  <div class="home">
    <section>
      <!-- <div style="height:420px;"></div> -->
      <el-carousel height="420px" trigger="click" :interval="100000">
        <el-carousel-item :key="index" v-for="(item, index) in bannerList">
          <img :src="item.src" @click="bannerGoPage(index)" />
        </el-carousel-item>
      </el-carousel>
    </section>
    <section>
      <el-row class="row-bg" justify="space-between" type="flex" :gutter="20">
        <el-col :span="16">
          <div class="colBox">
            <span class="homeTitle">资料分类统计</span>
            <div class="handleChart">
              <el-button :type="classType" @click="classBarInit" size="small">按资料分类</el-button>
              <el-button :type="DBType" @click="DBBarInit" size="small">按数据库分类</el-button>
            </div>
            <div class="chartsBox" id="dataCLassfication"></div>
          </div>
        </el-col>
        <el-col :span="8" id="unCheckBox">
          <div class="colBox">
            <span class="homeTitle">待办提醒</span>
            <div
              class="eventBox"
              v-if="userType === '11'"
              style="text-align: center;padding-top: 52px;"
            >暂无待办提醒</div>
            <div class="eventBox" v-else>
              <div :key="index" class="eventList" v-for="(item, index) in eventList">
                <i class="el-icon-price-tag"></i>
                <span class="name">{{ item.name }}</span>，待办
                <span
                  class="todoNum"
                  @click="goPageUrl(item.menuName,1)"
                >{{ item.uncheck>99? '99+':item.uncheck}}</span>条，已办理
                <span
                  class="haveTodoNum"
                  @click="goPageUrl(item.menuName,2)"
                >{{ item.checked>99? '99+':item.checked }}</span>条
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>
    <section style="background:#fff;padding-bottom:20px;">
      <span class="homeTitle">存储管理</span>
      <!-- v-for="r in 5" :key="r" -->
      <el-row :gutter="20" class="storageRow">
        <el-col :key="index" :span="8" v-for="(item, index) in pieBox">
          <el-card class="box-card">
            <div class="clearfix" slot="header">
              <span>{{ item.LOGIC_NAME }}</span>
            </div>
            <div class="cardBody">
              <div :id="pieId(index)" class="pieBox"></div>
              <div class="progressBox">
                <div v-for="(itemc, indexc) in item.DB_INFO" :key="indexc">
                  <span class="name">{{ itemc.DATABASE_NAME }}</span>
                  <el-progress
                    :percentage="Number((indexc/10*100).toFixed(2))"
                    :stroke-width="16"
                    :text-inside="true"
                    color="#ffc739"
                  ></el-progress>
                </div>
              </div>
            </div>
            <div class="cardFooter">
              <div class="storageInfo">
                <i class="el-icon-receiving"></i>
                <p class="name">总容量</p>
                <span class="num">
                  {{
                  item.TOTAL_CAPACITY == undefined
                  ? "0"
                  : item.TOTAL_CAPACITY
                  }}TB
                </span>
              </div>
              <div class="storageInfo">
                <i class="el-icon-bangzhu"></i>
                <p class="name">已使用</p>
                <span class="num">
                  {{
                  item.USED_CAPACITY == undefined ? "0" : item.USED_CAPACITY
                  }}TB
                </span>
              </div>
              <div class="storageInfo">
                <i class="el-icon-odometer"></i>
                <p class="name">剩余</p>
                <span class="num">
                  {{
                  item.TOTAL_CAPACITY == undefined
                  ? "0"
                  : item.TOTAL_CAPACITY - item.USED_CAPACITY
                  }}TB
                </span>
              </div>
              <div class="storageInfo">
                <i class="el-icon-tickets"></i>
                <p class="name">存储资料</p>
                <span class="num">{{ item.NUM == undefined ? "0" : item.NUM }}种</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </section>
    <section style="background:#fff;">
      <span class="homeTitle">专题库</span>

      <el-row :gutter="20" class="projiectRow">
        <el-col :key="index" :span="6" v-for="(item, index) in projectLibraryList">
          <div :class="'project' + index">
            <span>{{ "信息中心" }}</span>
            <p>{{ item.SDB_NAME }}</p>
          </div>
        </el-col>
      </el-row>
    </section>

    <section>
      <el-row class="row-bg" :gutter="20">
        <el-col :span="16">
          <div class="colBox">
            <span class="homeTitle">资料种类统计</span>

            <div class="chartsBox" id="lineChart"></div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="colBox" style="height:390px;">
            <span class="homeTitle">帮助文档</span>
            <div class="documentBox">
              <div class="documentList" v-for="(item,index) in documentList" :key="index">
                <span class="round"></span>
                <span
                  class="name"
                  :title="item.FILE_NAME"
                >{{item.FILE_TYPE == 'dev'?'[开发文档] ':'[运维文档] '}}{{item.FILE_NAME}}</span>
                <el-link type="primary" @click="downloadWord(item.FILE_STOR_PATH)">[下载]</el-link>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!--  <div class="footer">
      <p>
        <span style="font-weight: bold;">相关链接：</span>
        <a href="http://www.cma.gov.cn/" target="_blank">中国气象局政府网</a> |
        <a href="http://10.1.65.66/pub/cmahead/nmsy/index.html" target="_blank">综合管理信息系统</a>
        |
        <a href="http://data.cma.cn/" target="_blank">中国气象数据网</a> |
        <a href="http://www.weather.com.cn/" target="_blank">中国天气网</a> |
        <a href="http://www.cmastd.cn/" target="_blank">中国气象标准化网</a>
      </p>
      <p>版权所有 © 2018-2020 国家气象局信息中心</p>
    </div>-->
    <!-- <el-backtop target=".page-component__scroll .el-scrollbar__wrap"></el-backtop> -->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { downloadTable } from "@/api/structureManagement/exportTable";
import {
  findDataCount,
  findDataMonthCount,
  findUndoCount,
  findLogicInfo,
  findSpecialDbList,
  findFileList,
  findLogicCountData
} from "@/api/index";
var echarts = require("echarts");
export default {
  name: "home",
  components: {},
  data() {
    return {
      userType: "",
      pageFlag: this.$route.params.pageFlag,
      classType: "primary",
      DBType: "",
      bannerList: [
        {
          index: 1,
          src: require("../assets/home/images/banner1.gif")
        },
        {
          index: 2,
          src: require("../assets/home/images/banner2.gif")
        },
        {
          index: 3,
          src: require("../assets/home/images/banner3.gif")
        },
        {
          index: 4,
          src: require("../assets/home/images/banner4.gif")
        }
      ],
      // 代办提醒
      eventList: [
        {
          name: "新增资料审核",
          menuName: "数据注册审核",
          checked: 0,
          uncheck: 0
        },
        {
          name: "数据授权审核",
          menuName: "资料访问权限",
          checked: 0,
          uncheck: 0
        },
        {
          name: "数据库账户审核",
          menuName: "数据库访问账户",
          checked: 0,
          uncheck: 0
        },
        {
          name: "业务专题库审核",
          menuName: "专题库审核",
          checked: 0,
          uncheck: 0
        },
        {
          name: "云数据库审核",
          menuName: "云数据库审核",
          checked: 0,
          uncheck: 0
        }
      ],
      // 饼图
      pieBox: [],
      // 专题库
      projectLibraryList: [],
      // 帮助文档
      documentList: [],

      interfaceData: [], //获取到的接口数据
      menuName: ""
    };
  },
  mounted() {
    //资料分类
    this.drawBarChartClass();
    // 资料种类统计
    this.drawLineChart();
    if (this.pageFlag) {
      document.querySelector("#" + this.pageFlag).scrollIntoView(true);
    }
    console.log(this.permission_routes);
  },
  computed: {
    ...mapGetters(["permission_routes", "sidebar"])
  },
  created() {
    this.getEventList();
    this.getHelpDocument();
    this.getPieBox();
    this.queryCheckList();
    this.userType = localStorage.getItem("userType");
    console.log(this.userType);
  },
  watch: {
    pieBox: function() {
      this.$nextTick(function() {
        /*现在数据已经渲染完毕*/
        this.drawPies();
      });
    }
  },

  methods: {
    resetData(jsonObj, name) {
      // 循环所有键
      for (var v in jsonObj) {
        var element = jsonObj[v];
        // 1.判断是对象或者数组
        if (typeof element == "object") {
          this.resetData(element, name);
        } else {
          if (element == name) {
            this.menuName = name;
          }
        }
      }
    },
    // banner页面跳转
    bannerGoPage(index) {
      this.$router.push({
        name: "业务场景说明",
        params: { pageFlag: "pageFlag" + index }
      });
    },
    // 代办提醒
    getEventList() {
      findUndoCount().then(res => {
        let data = res.data;
        this.eventList[0].checked = data.xzzl.checked;
        this.eventList[0].uncheck = data.xzzl.uncheck;

        this.eventList[1].checked = data.sjsq.checked;
        this.eventList[1].uncheck = data.sjsq.uncheck;

        this.eventList[2].checked = data.sjkzh.checked;
        this.eventList[2].uncheck = data.sjkzh.uncheck;

        this.eventList[3].checked = data.ywztk.checked;
        this.eventList[3].uncheck = data.ywztk.uncheck;

        this.eventList[4].checked = data.ysjk.checked;
        this.eventList[4].uncheck = data.ysjk.uncheck;
      });
    },
    // 代办跳页
    goPageUrl(name, value) {
      this.resetData(this.permission_routes, name);
      let status = "";
      if (this.menuName == "数据注册审核") {
        status = value;
      } else if (this.menuName == "资料访问权限") {
        status = "0" + value;
      } else if (this.menuName == "数据库访问账户") {
        status = String(value - 1);
      } else if (this.menuName == "专题库审核") {
        status = String(value);
      } else if (this.menuName == "云数据库审核") {
        status = "0" + value;
      }
      this.$router.push({
        name: this.menuName,
        params: { status: status }
      });
    },
    // 按资料分类的chart
    drawBarChartClass() {
      findDataCount().then(res => {
        let data = res.data;
        let dataNumChart = [];
        let dataTypeChart = [];
        data.forEach(element => {
          dataNumChart.push(element.NUM);
          dataTypeChart.push(element.CLASS_NAME);
        });
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(
          document.getElementById("dataCLassfication")
        );
        // 绘制图表
        myChart.setOption({
          title: {
            text: "气象资料种类统计",
            left: "center",
            textStyle: {
              fontSize: 14,
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "axis"
          },
          color: ["#058ae5"],
          calculable: true,
          yAxis: [
            {
              name: "单位: 种",
              nameRotate: 90,
              nameGap: 50,
              nameLocation: "middle",
              type: "value",
              axisLabel: {
                formatter: "{value}"
              },
              boundaryGap: [0, 0.01]
            }
          ],
          xAxis: [
            {
              type: "category",
              data: dataTypeChart,
              axisLabel: {
                interval: 0,
                rotate: 20
              }
            }
          ],
          series: [
            {
              name: "总计",
              type: "bar",
              barWidth: 20,
              data: dataNumChart,
              itemStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#21bd69"
                    },
                    {
                      offset: 1,
                      color: "#1561d1"
                    }
                  ])
                }
              },
              label: {
                normal: {
                  show: false
                }
              }
            }
          ]
        });
      });
    },
    // 数据库分类
    drawDBchart() {
      findLogicCountData().then(res => {
        let data = res.data;
        let chartstype = [];
        let chartsNum = [];
        data.forEach(element => {
          chartstype.push(element.LOGICNAME);
          chartsNum.push(element.NUM);
        });
        var myChart = echarts.init(
          document.getElementById("dataCLassfication")
        );
        myChart.setOption({
          title: {
            text: "数据库资料种数统计",
            left: "center",
            textStyle: {
              fontSize: 14,
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "axis"
          },
          color: ["#1fbf86"],
          calculable: true,
          yAxis: [
            {
              name: "单位: 种",
              nameRotate: 90,
              nameLocation: "middle",
              nameGap: 50,
              type: "value",
              axisLabel: {
                formatter: "{value}"
              },
              boundaryGap: [0, 0.01]
            }
          ],
          xAxis: [
            {
              type: "category",
              data: chartstype,
              nameRotate: 90,
              axisLabel: {
                interval: 0,
                rotate: 40,
                textStyle: { fontSize: 10 }
              }
            }
          ],
          series: [
            {
              type: "bar",
              barWidth: 20,
              data: chartsNum,
              itemStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#21bd69"
                    },
                    {
                      offset: 1,
                      color: "#1561d1"
                    }
                  ])
                }
              },
              label: {
                normal: {
                  show: false
                }
              }
            }
          ]
        });
      });
    },
    // chart切换
    classBarInit() {
      if (this.classType != "primary") {
        this.classType = "primary";
        this.DBType = "";
        this.drawBarChartClass();
      }
    },
    DBBarInit() {
      if (this.DBType != "primary") {
        this.classType = "";
        this.DBType = "primary";
        this.drawDBchart();
      }
    },
    // 动态绑定ID
    pieId: function(index) {
      return "pieId" + index;
    },
    // 饼图box
    getPieBox() {
      findLogicInfo().then(res => {
        this.pieBox = res.data;
      });
    },
    // 饼图
    drawPies() {
      var option = {
        tooltip: {
          trigger: "item",
          // formatter: "{a} <br/>{b}: {c}TB ({d}%)",
          formatter: function(msg) {
            // debugger
            var data = msg;
            if ((data.name = "")) {
              return (
                "数值 <br/>非结构化数据库 : " +
                data.value +
                "TB(" +
                data.percent +
                "%)"
              );
            } else {
              return (
                "数值 <br/>结构化数据库 : " +
                data.value +
                "TB(" +
                data.percent +
                "%)"
              );
            }
          },
          position: function(point, params, dom, rect, size) {
            //其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
            var x = point[0]; //
            var y = point[1];
            var viewWidth = size.viewSize[0];
            var viewHeight = size.viewSize[1];
            var boxWidth = size.contentSize[0];
            var boxHeight = size.contentSize[1];
            var posX = 0; //x坐标位置
            var posY = 0; //y坐标位置

            if (x < boxWidth) {
              //左边放不开
              posX = 5;
            } else {
              //左边放的下
              posX = x - boxWidth;
            }

            if (y < boxHeight) {
              //上边放不开
              posY = 5;
            } else {
              //上边放得下
              posY = y - boxHeight;
            }
            return [posX, posY];
          }
        },
        grid: {
          width: "auto",
          height: "auto"
        },
        color: ["#ffc739", "#22bfcb", "#76da77"],
        series: [
          {
            name: "数值",
            type: "pie",
            center: ["40%", "50%"],
            radius: ["50%", "70%"],
            data: [
              { value: 310, name: "DRC" },
              { value: 635, name: "NAS" }
            ],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            },
            label: {
              normal: {
                show: false,
                position: "inner", // 设置标签位置，默认在饼状图外 可选值：'outer' ¦ 'inner（饼状图上）'
                // formatter: '{a} {b} : {c}个 ({d}%)'   设置标签显示内容 ，默认显示{b}
                // {a}指series.name  {b}指series.data的name
                // {c}指series.data的value  {d}%指这一部分占总数的百分比
                formatter: "{b}"
              }
            }
          }
        ]
      };

      for (let i = 0; i < this.pieBox.length; i++) {
        var pieDom = document.getElementById("pieId" + i);
        var pieChart = echarts.init(pieDom);
        var pieList = [];
        if (this.pieBox[i].database_name2) {
          pieList.push(
            {
              value: this.pieBox[i].TOTAL_CAPACITY,
              name: this.pieBox[i].LOGIC_NAME
            },
            {
              value: this.pieBox[i].total_capacity2,
              name: this.pieBox[i].logic_name2
            }
          );
        } else {
          pieList.push({
            value:
              this.pieBox[i].TOTAL_CAPACITY == undefined
                ? 0
                : this.pieBox[i].TOTAL_CAPACITY,
            name: this.pieBox[i].LOGIC_NAME
          });
        }
        option.series[0].data = pieList;
        pieChart.setOption(option, true);
      }
    },
    // 获取专题库
    queryCheckList() {
      findSpecialDbList().then(res => {
        this.projectLibraryList = res.data;
      });
    },
    // 资料种类统计
    drawLineChart() {
      findDataMonthCount().then(res => {
        let numData = res.data.numData;
        let monthData = res.data.monthData;

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById("lineChart"));
        // 绘制图表
        myChart.setOption({
          title: {
            text: "存储管理资料总数月统计",
            // subtext: date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + '-' + now.getFullYear() + '年' + (now.getMonth() + 1) + '月',
            left: "center",
            textStyle: {
              fontSize: 14,
              fontWeight: "normal"
            }
          },
          tooltip: {
            trigger: "axis"
          },
          xAxis: {
            type: "category",
            boundaryGap: false,
            //轴字体颜色
            axisLabel: {
              color: "#50697a"
            },
            //轴不显示
            axisLine: {
              show: false
            },
            //分隔线不显示
            splitLine: {
              show: false
            },
            //轴上的分隔线
            axisTick: {
              show: false
            },
            data: monthData
          },
          yAxis: {
            name: "单位: 种",
            nameRotate: 90,
            nameLocation: "middle",
            nameGap: 50,
            axisLabel: {
              color: "#50697a"
            },
            axisLine: {
              show: false
            },
            //轴上的分隔线
            axisTick: {
              show: false
            },
            type: "value"
          },
          series: [
            {
              name: "总计",
              type: "line",
              color: "#21cecf",
              data: numData,
              label: {
                normal: {
                  show: false
                }
              },
              lineStyle: {
                color: "#21cecf"
              },
              smooth: true,
              areaStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: "#80e2e2"
                    },
                    {
                      offset: 1,
                      color: "#ffe"
                    }
                  ])
                }
              }
            }
          ]
        });
      });
    },
    // 帮助文档
    getHelpDocument() {
      findFileList().then(res => {
        res.data.forEach((element, index) => {
          if (index == 8) {
            return;
          }
          this.documentList.push(element);
        });
      });
    },
    // 下载
    downloadWord(path) {
      downloadTable({ filePath: path }).then(res => {
        this.downloadfileCommon(res);
      });
    },

    //获取接口详细信息
    getInterfaceInfo(name) {
      let interfacename = "";
      this.interfaceData.find(item => {
        if (item.interfaceid == name) {
          interfacename = item.interfacename;
        }
      });
      return interfacename;
    }
  }
};
</script>
<style lang="scss" scoped>
section {
  width: 1200px;
  margin: auto;
  margin-top: 20px;
}
.home {
  min-width: 1200px;
  width: 100%;
}
.el-carousel--horizontal {
  overflow: hidden;
}
.colBox {
  width: 100%;
  height: 100%;
  background: #fff;
  position: relative;
}
.homeTitle {
  color: #373d41;
  line-height: 40px;
  font-size: 15px;
  font-weight: bold;
  border-left: 3px solid #1ac3fb;
  margin-left: 20px;
  padding-left: 20px;
}
.eventBox {
  width: 98%;
  margin: auto;
  border-top: solid 1px #ebebeb;
}
.eventList {
  text-indent: 7px;
  font-size: 14px;
  cursor: pointer;
  border-radius: 6px;
  height: 40px;
  line-height: 40px;
  background: #f5f5f5;
  margin: 20px 0;
  i {
    padding-right: 6px;
  }
}
.todoNum,
.haveTodoNum {
  font-size: 14px;
  padding: 2px;
  width: 26px;
  height: 26px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  display: inline-block;
  text-indent: 0;
}
.todoNum {
  color: #f79d29;
  background: #fee4c1;
}
.haveTodoNum {
  color: #ed7960;
  background: #f8d1c9;
}
.chartsBox {
  width: 100%;
}
#dataCLassfication,
#DBChart,
#lineChart,
#interfaceChart {
  height: 350px;
}
.handleChart {
  float: right;
  padding-top: 10px;
  padding-right: 10px;
}
.projiectRow {
  padding: 0 10px;
  .el-col {
    div {
      background: url(~@/assets/home/images/topicInfo.png) no-repeat 10px center;
      border-radius: 6px;
      height: 72px;
      background-color: #409eff;
      margin-bottom: 12px;
      padding-right: 4px;
      span,
      p {
        display: block;
        width: 100%;
        text-align: right;
        color: #fff;
        font-size: 16px;
        padding-top: 5px;
      }
      span {
        padding-top: 12px;
      }
    }
  }
}

.project0 {
  background-color: #fb6e52 !important;
}
.project1 {
  background-color: #ffc655 !important;
}
.project2 {
  background-color: #409eff !important;
}
.project3 {
  background-color: #67c23a !important;
}
.project4 {
  background-color: #37d6b7 !important;
}
.project5 {
  background-color: #a8da28 !important;
}
.documentBox {
  width: 89%;
  margin: auto;
  margin-top: 12px;
}
.documentList {
  font-size: 14px;
  cursor: pointer;
  list-style: disc outside none;
  padding-bottom: 20px;
  display: flex;
  align-items: center;
  .name {
    width: calc(100% - 100px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    display: inline-block;
    margin-right: 42px;
  }
}
.round {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #333;
  margin-right: 12px;
}
.footer {
  width: 100%;
  height: 120px;
  background-color: #f2f2f2;
  padding-top: 36px;
  margin-top: 20px;
  color: #333;
  p {
    font: 12px "宋体";
    text-align: center;
  }
  a {
    color: #4a4a4a;
    font: 12px "宋体";
  }
}
.storageRow {
  padding: 10px 15px 0 15px;
}
.cardBody,
.cardFooter {
  display: flex;
  position: relative;
}
.cardFooter {
  padding-top: 10px;
}
.cardFooter:before {
  content: "";
  position: absolute;
  width: calc(100% + 40px);
  height: 1px;
  background: #ebeef5;
  top: 0;
  left: -20px;
}
.pieBox {
  width: 56%;
  height: 200px;
}
.progressBox {
  width: 44%;
  .name {
    font-size: 13px;
    position: relative;
    padding-left: 18px;
  }
  .name:before {
    content: "";
    width: 12px;
    height: 12px;
    display: inline-grid;
    background: #ffc739;
    left: 0px;
    position: absolute;
    top: 2px;
  }
  .name2:before {
    background: #22bfcb;
  }
}
.storageInfo {
  width: 25%;
  text-align: center;
  border-right: 1px solid #ebeef5;
  i {
    font-size: 36px;
  }
  .name {
    color: #323232;
    font-size: 12px;
    padding: 2px 0 2px 0;
  }
  .num {
    font-size: 13px;
    font-weight: bold;
    letter-spacing: 1px;
  }
}
.cardFooter .storageInfo:nth-child(1) {
  color: #49a7f7;
}
.cardFooter .storageInfo:nth-child(2) {
  color: #fac94e;
}
.cardFooter .storageInfo:nth-child(3) {
  color: #82d983;
}
.cardFooter .storageInfo:nth-child(4) {
  color: #b3a9f8;
  border: none;
}
.el-progress-bar__innerText {
  margin-top: 1px;
  vertical-align: top;
}
.box-card {
  margin-bottom: 22px;
}
.el-carousel__item,
.colBox,
section {
  border-radius: 4px;
}
/* 系统banner */
//  background: url("~@/assets/home/images/banner.png") no-repeat top center;
</style>
