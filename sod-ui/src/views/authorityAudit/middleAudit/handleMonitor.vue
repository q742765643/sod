<template>
  <section class="handleMonitorDialog">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>实例详情</span>
        </div>
        <el-form
          ref="ruleForm"
          :model="msgFormDialog"
          label-width="150px"
          label-position="right"
        >
          <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8" >
            <el-form-item label="实例ID" prop="itserviceId">
              <el-input
                v-model.trim="msgFormDialog.itserviceId"
                :disabled="true"
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实例类型" prop="storageLogic">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.storageLogic">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间" prop="createTime">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.createShow">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="8" >
              <el-form-item label="实例服务名称" prop="name">
                <el-input
                  v-model.trim="msgFormDialog.name"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="状态" prop="examineStatus">
                <el-input
                  :disabled="true"
                  size="small"
                  v-model.trim="msgFormDialog.examineStatus">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="更新时间" prop="updateTime">
                <el-input
                  :disabled="true"
                  size="small"
                  v-model.trim="msgFormDialog.updateShow">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    <el-card class="box-card" >
    <el-tabs type="border-card" v-model.trim="activeName" @tab-click="handleLoad">
      <el-tab-pane label="节点信息" name="first">
        <el-table
          border
          :data="tableData"
          row-key="id"
          ref="singleTable"
          highlight-current-row
        >
          <el-table-column prop="uuid" label="节点ID"  :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="serviceStatus" label="节点状态":show-overflow-tooltip="true" >
            <template slot-scope="scope" :style="note">
              <div style="background: #13ce66;
              display: inline-block;
               padding: 5px;
               text-decoration: none;
               border-radius: 5px;
               -moz-border-radius: 5px;
               -webkit-border-radius: 5px;
               -moz-box-shadow: 0 1px 2px rgba(0,0,0,0.5);
               -webkit-box-shadow: 0 1px 2px rgba(0,0,0,0.5);
               text-shadow: 0 -1px 1px rgba(0,0,0,0.25);
               border-bottom: 1px solid rgba(0,0,0,0.25);
               position: relative;"></div>
              <span>{{ scope.row.serviceStatus }}</span>
            </template>
          </el-table-column>
          <!--<el-table-column prop="ip" label="节点IP" :show-overflow-tooltip="true"></el-table-column>-->
          <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="lastModifyTime" label="更新时间" :show-overflow-tooltip="true"></el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="配置参数" name="second">
        <el-form
          ref="editForm"
          :model="msgFormDialog"
          label-width="150px"
          label-position="right"
        >
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="8" >
              <el-form-item label="名称" prop="displayname">
                <el-input
                  v-model.trim="msgFormDialog.displayname"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="8" >
              <el-form-item label="服务端口" prop="port">
                <el-input
                  v-model.trim="msgFormDialog.port"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.password !=''&&msgFormDialog.password !=null">
            <el-col :span="8" >
              <el-form-item label="配置密码" prop="password">
                <el-input
                  v-model.trim="msgFormDialog.password"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.externalPort !='0'&&msgFormDialog.externalPort !=null">
            <el-col :span="8" >
              <el-form-item label="客户端连接端口" prop="externalPort">
                <el-input
                  v-model.trim="msgFormDialog.externalPort"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.managerPort !='0'&&msgFormDialog.managerPort !=null">
            <el-col :span="8" >
              <el-form-item label="管理服务端口" prop="managerPort">
                <el-input
                  v-model.trim="msgFormDialog.managerPort"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.restApiport !='0'&&msgFormDialog.restApiport !=null">
            <el-col :span="8" >
              <el-form-item label="API端口" prop="restApiport">
                <el-input
                  v-model.trim="msgFormDialog.restApiport"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.database !=''&&msgFormDialog.database !=null">
            <el-col :span="8" >
              <el-form-item label="初始化数据库名称" prop="database">
                <el-input
                  v-model.trim="msgFormDialog.database"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.username !=''&&msgFormDialog.username !=null">
            <el-col :span="8" >
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model.trim="msgFormDialog.username"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.adminPassword !=''&&msgFormDialog.adminPassword !=null">
            <el-col :span="8" >
              <el-form-item label="管理员密码" prop="adminPassword">
                <el-input
                  v-model.trim="msgFormDialog.adminPassword"
                  :disabled="true"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="8" >
              <el-form-item label="CPU" prop="cpu">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.cpu"
                >
                  <el-option
                    v-for="(item,index) in cpuList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="8" >
              <el-form-item label="内存" prop="memory">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.memory"
                >
                  <el-option
                    v-for="(item,index) in memoryList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.storage !=''&& msgFormDialog.storage != null">
            <el-col :span="8" >
              <el-form-item label="持久化存储空间大小" prop="storage">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.storage"
                >
                  <el-option
                    v-for="(item,index) in storageSizeList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center" v-if="msgFormDialog.slaveCount !=''&&msgFormDialog.slaveCount !=null">
            <el-col :span="8" >
              <el-form-item label="集群规模" prop="slaveCount">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.slaveCount"
                >
                  <el-option
                    v-for="(item,index) in clusterList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div  style="float:right">
          <el-button size="small" type="primary" @click="editSod('editForm')" >确 定</el-button>
        </div>
      </el-tab-pane>

      <el-tab-pane label="监控数据" name="third" >
        <el-form :model="monitorParams" ref="queryForm" :inline="true" class="searchBox" >
          <el-form-item label="节点ID">
            <el-select
              v-model.trim="monitorParams.nodeId"
              clearable
              size="small"
              style="width: 240px"
            >
              <!--<el-option label="全部" value></el-option>-->
              <el-option
                v-for="(item,index) in nodeIdList"
                :key="index"
                :label="item.uuid"
                :value="item.uuid"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="analysisCell" icon="el-icon-search">修改监控配置</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="displayLine" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <div style="overflow-y:scroll; height: 350px">
          <el-tabs type="border-card" v-model.trim="activeNameSec" @tab-click="handleLoadTwo">
            <el-tab-pane label="性能指标" name="first">
              <div id ="oneDiv">
              <div
                v-for="(item,index) in lineNameList"
                :key="index"
                :id ="getChartsId(index)"
                style="width: 100%;height:300px"></div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="业务指标" name="second">
              <div id ="twoDiv">
                <div
                  v-for="(item,index) in lineNameList"
                  :key="index"
                  :id ="getChartsIdTwo(index)"
                  style="width: 100%;height:300px"></div>
              </div>
            </el-tab-pane>
          </el-tabs>
        <!--<div id ="charts0" style="width: 100%;height:300px"></div>-->
        <!--<div id ="charts1" style="width: 100%;height:300px"></div>-->
        <!--<div id ="charts2" style="width: 100%;height:300px"></div>-->
        <!--<div id ="charts3" style="width: 100%;height:300px"></div>-->
        <!--<div id ="charts4" style="width: 100%;height:300px"></div>-->
        <!--<div id ="charts5" style="width: 100%;height:300px"></div>-->
        </div>
      </el-tab-pane>

      <el-tab-pane label="运行日志" name="fourth">
        <el-form :model="monitorParams" ref="queryForm" :inline="true" class="searchBox" >
          <el-form-item label="节点ID">
            <el-select
              v-model.trim="monitorParams.nodeId"
              clearable
              size="small"
              style="width: 240px">
              <el-option
                v-for="(item,index) in nodeIdList"
                :key="index"
                :label="item.uuid"
                :value="item.uuid"/>
            </el-select>
          </el-form-item>
          <el-form-item label="容器">
            <el-select
              v-model.trim="monitorParams.container"
              clearable
              size="small"
              style="width: 240px">
              <el-option
                v-for="item,index in containerList"
                :key="index"
                :label="item"
                :value="item"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="getContainer" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <div style="overflow:scroll; height: 350px">
          <pre v-html="journalData"  ></pre>
        </div>
      </el-tab-pane>
      <el-tab-pane label="告警记录" name="fifth">
        <el-form :model="monitorParams" ref="queryForm" :inline="true" class="searchBox" >
          <el-form-item label="节点ID">
            <el-select
              v-model.trim="monitorParams.nodeId"
              clearable
              size="small"
              style="width: 240px">
              <el-option
                v-for="(item,index) in nodeIdList"
                :key="index"
                :label="item.uuid"
                :value="item.uuid"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary"  icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    </el-card>

    <el-dialog
      :close-on-click-modal="false"
      title="修改监控配置"
      :visible.sync="monitorDialog"
      customClass="customHeight"
      width="60%"
      max-width="600px"
      top="5vh"
      v-dialogDrag
      append-to-body
    >
      <monitorConfig
        v-if="monitorDialog"
        :handleObj="handleObj1"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>
  </section>

</template>

<script>
// var baseUrl = process.env.VUE_APP_DM;
import {
  getById,
  editCldb,
  zjCldb,
  saveCldb,
  deployCldb,
  nodeDataNew,
  exportDemo,
  exprotFile,
  getUserByType,
  containerDataNew,
  getDictDataByType,
  editDeployNew,
  getJournalNew,
  getMonitorDataNew,
  saveLog,
  gethostsNew
} from "@/api/authorityAudit/middlewareDBaudit";
import {
  parseTime
}from '@/utils/ruoyi'
import {
  chineseLengthTenValidation,
  telphoneNumValidation,
  portNumValidation,
  ipUrlValidation
} from "@/components/commonVaildate.js";
import monitorConfig from "@/views/authorityAudit/middleAudit/monitorConfig";
import axios from 'axios';
var echarts = require("echarts");
export default {
  components: {
    monitorConfig,
  },
  name: "handleMonitorDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      url:"",

      note:{
        // backgroundImage:"url("+require('@/assets/image/status_running.png')+")",
      },
      monitorDialog:false,
      journalData:"",
      // statusImg: require('@/assets/image/status_running.png'),
      activeName:"first",
      activeNameSec:"first",
      newToken:"",
      lineName:"",
      getDataRequest:"",
      lineNameList:[],
      cpuList:[],
      storageSizeList: [],
      memoryList:[],
      clusterList:[],
      yData:[],
      xData:[],
      lineId:"",
      tableData: [],
      msgDialog: {
        id:"",
        type:"",
        userId:""
      },
      nodeDataInfo:{
        createTime: "",
        ip: "",
        lastModifyTime: "",
        port:"",
        serviceStatus: "",
        uuid: ""
      },
      msgFormDialog:{
        cluster:"",
        itserviceId:"",
        storageLogic: "",
        createTime:"",
        updateTime:"",
        createShow:"",
        updateShow:"",
        name:"",
        examineStatus:""
      },
      //监控数据
      monitorParams:{
        nodeId:"",
        container:""
      },
      containerList:[],
      nodeIdList:[],
      hostsList:[],
      configData: {
        id:"",
        type:"",
        nodeId:"",
        container:"",
        step:"",
        zType:"",
        startDate:"",
        endDate:""
      }
    };
  },
  created(){
    this.msgDialog = this.handleObj

    getDictDataByType("yun_storage_size").then(
      response => {
        this.storageSizeList = response.data;
      }
    );
    getDictDataByType("yun_cpu_type").then(
      response => {
        this.cpuList = response.data;
      }
    );
    getDictDataByType("yun_memory_type").then(
      response => {
        this.memoryList = response.data;
      }
    );
    getDictDataByType("yun_cluster_size").then(
      response => {
        this.clusterList = response.data;
      }
    );
    // gethostsNew({}).then(response => {
    //   response = JSON.parse(response.data)
    //   var host = response.content.content;
    //   for(var i=0;i<host.length;i++){
    //     this.hostsList.push(host[i].address);
    //   }
    // });

    nodeDataNew(this.msgDialog).then(respone =>{
      if(respone.code == "200"){
        //节点信息下拉
        var  a = JSON.parse(respone.data);
        for(var i=0;i<a.content.length;i++){
            var nadeData = a.content[i];
            this.nodeIdList.push(nadeData);
            this.lineId = this.nodeIdList[0].uuid;
            this.monitorParams.nodeId = this.nodeIdList[0].uuid;
        };

        this.tableData = a.content;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      }else{
        console.log("节点信息获取失败");
      }
    });
    getById({ id: this.msgDialog.userId }).then((response) => {
      this.msgFormDialog = response.data;
      this.msgFormDialog.createShow = parseTime(this.msgFormDialog.createTime);
      this.msgFormDialog.updateShow = parseTime(this.msgFormDialog.updateTime);
      if(this.msgFormDialog.storageLogic == "kafka"){
        this.msgFormDialog.clusterSize = this.msgFormDialog.slaveCount;
      }
      this.msgFormDialog.examineStatus = '活跃'
    });

    // this.getfirstLineNameList();
    // this.getContainer();
  },
  methods: {
    getChartsId(index){
        return "charts"+index;

    },
    getChartsIdTwo(index){
    return "chartsT"+index;
    },

    analysisCell() {

      var id = this.msgFormDialog.itserviceId;
      var type = this.msgFormDialog.storageLogic;
      // var userId = row.id;
      this.handleObj1= {id,type};
      this.monitorDialog = true;

    },
    handleLoad (data) {
      let name = data.name
      if(name == 'third'){
        this.getDataRequest = '性能'
        this.displayLine();
      }else  if(name == 'fourth'){
        this.getContainer();
      }
    },
    handleLoadTwo (data) {
      let name = data.name
      if(name == 'first'){
        this.getDataRequest = '性能'
        this.displayLine();
      }else  if(name == 'second'){
        this.getDataRequest = '业务'
        this.displayLine();
      }
    },
    //变更
    editSod(editForm){
      if(this.msgFormDialog.slaveCount == '1'){
        this.msgFormDialog.cluster = false
      }else{
        this.msgFormDialog.cluster = true
      }

      if(this.msgFormDialog.storageLogic == "kafka"){
      var q = parseInt(this.msgFormDialog.slaveCount);
      var w = parseInt(this.msgFormDialog.clusterSize);
      var e = q-w;
      if(e>0){
        for (var i = 0; i < e; i++) {
          if (this.msgFormDialog.port != "") {
            this.msgFormDialog.port = this.msgFormDialog.port + ","
          }
          var b = Math.floor((Math.random() + 3) * 10000);
          var c = b.toString();
          this.msgFormDialog.port = this.msgFormDialog.port + c;
        }
      }
      }
      // if(this.msgFormDialog.storageLogic =="kafka"){
      //   var index = Math.floor((Math.random()*this.hostsList.length));
      //   this.msgFormDialog.externalIP = this.hostsList[index];
      // }
      editDeployNew(this.msgFormDialog).then(res => {
        if (res.code == 200) {
          this.msgFormDialog.examineStatus = '02'
          editCldb(this.msgFormDialog).then(res =>{
            this.msgFormDialog.logId = this.msgFormDialog.id;
            this.msgFormDialog.id = '';
            this.msgFormDialog.viewStatus = '6';
            this.msgFormDialog.userId = this.$store.getters.userName;
            saveLog(this.msgFormDialog);
          });
          this.$message({
            type: "success",
            message: "操作成功"
          });
          this.$emit("handleDialogClose");
        } else {
          this.$message({
            type: "error",
            message: res.message
          });
        }
      });

    },

     async getContainer(){
        this.configData.id = this.msgFormDialog.itserviceId;
        this.configData.type = this.msgFormDialog.storageLogic;
        this.configData.nodeId = this.monitorParams.nodeId;
       await containerDataNew(this.configData).then((res)=>{
         var a = JSON.parse(res.data);
          if(res.code == 200){

            for(var i=0;i<a.content.length;i++){
              this.containerList = a.content
            }
            if(this.monitorParams.container == ''){
            this.monitorParams.container = this.containerList[0];
            }
          }
      });
       this.configData.container = this.monitorParams.container;
       await getJournalNew(this.configData).then((res)=>{
         console.log(res);
         var a = JSON.parse(res.data);
         if(res.code == 200){
           this.journalData = a.content;
           // $("#journalData").append(this.journalData);

         }
       });

    },

    //折线图
    async drawLine(chartData,num,lineName) {
      var sname = this.getHourXseries(chartData);
      var qtime = this.getHourYqtime(chartData);
      var max = 0;

      if(this.getDataRequest == '性能'){
        var num = 'charts'+num;
      }else{
        var num = 'chartsT'+num;
      }


      if (typeof (sname) != 'undefined') {
        max = sname.length > 9 ? 9 : sname.length - 1;
      }
      var charts = echarts.init(document.getElementById(num))
      await  charts.setOption({

        title: {
          text: lineName,
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
          data: sname
        },
        yAxis: {
          name: lineName,
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
            name: lineName,
            type: "line",
            color: "#21cecf",
            data: qtime,
            label: {
              normal: {
                show: false
              }
            },
            lineStyle: {
              color: "#21cecf"
            },
            smooth: true,
          }
        ]

      });
    },
    //调用折线图方法
    async displayLine(){
      var step = '378.94736842105266';
      var endDate = new Date().getTime();
      var startDate = new Date().getTime() - 1 * 60 * 60 * 1000;
      var monitoringData;
      var chartData;
      // console.log(this.url)
        this.configData.id = this.msgFormDialog.itserviceId;
        this.configData.type = this.msgFormDialog.storageLogic;
        this.configData.nodeId = this.lineId;
        this.configData.step = step;
        this.configData.startDate = startDate;
        this.configData.endDate = endDate;
        this.configData.zType = this.getDataRequest;
      await getMonitorDataNew(this.configData).then((res) => {
        //折线图类型
        this.lineNameList = [];
        var a = JSON.parse(res.data);
        for (var key in a.content) {
          this.lineNameList.push(key);
        }
        monitoringData = a.content;
      });

      await this.initStatUserForHour(monitoringData);

    },
    initStatUserForHour(monitoringData) {
      for(var i=0;i<this.lineNameList.length;i++){
        var chartData = JSON.parse(monitoringData[this.lineNameList[i]]).data.result[0].values;
        var num = i;
        var lineName = this.lineNameList[i];
        this.drawLine(chartData,num,lineName);
      }
    },
    //横坐标数据
    getHourXseries(chartData) {
      var chartData = chartData;
      var hourArr = [];
      for (var i=0;i < chartData.length;i++){
        var timeData = chartData[i][0]*1000;
        var time = parseTime(timeData);
        hourArr.push(time);
      }
      return hourArr;
    },
    //折线图数据
    getHourYqtime(chartData) {
      var chartData = chartData;
      var hourYqtime = [];
      for (var i=0;i < chartData.length;i++){
        hourYqtime.push(Math.round(chartData[i][1]*100)/100);
      }
      return hourYqtime;
    },
    handleDialogClose() {
      this.monitorDialog = false;
      // this.handleObj = {};

    },

  }
};
</script>

<style lang="scss">
.handleMonitorDialog {
  .el-card {
    margin-bottom: 20px;
  }
  .el-select {
    width: 100%;
  }
  .el-checkbox + .el-checkbox {
    margin-left: 0;
  }
  .el-form-item.is-validating .el-input__inner {
    border-color: #67c23a;
  }
  .helpMsgBox {
    width: 636px;
  }
  .el-button a {
    color: #fff;
  }
  .spacialBtn {
    .el-form-item__content {
      margin-left: 70px !important;
    }
  }
}
</style>
