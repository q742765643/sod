<template>
  <section class="handleMiddleDialog">
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="150px"
      label-position="right"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>申请信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8" >
            <el-form-item label="申请用户" prop="userId">
              <el-input
                v-model.trim="msgFormDialog.userId"
                :disabled="true"
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数据库类型" prop="storageLogic">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.storageLogic">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="名称" prop="displayname">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.displayname"
                placeholder="名称"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="服务端口" prop="port">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.port"
                placeholder="服务端口"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途" prop="databaseUse">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.databaseUse"
                placeholder="用途"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="申请CPU"
              prop="cpuSize">
              <!--v-if="msgFormDialog.storageLogic !='redis'&& msgFormDialog.storageLogic !='file'"-->
              <el-input :disabled="isDetail" size="small" v-model.trim="msgFormDialog.cpuSize">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="left">
          <el-col :span="8" v-if="msgFormDialog.storageSize!= null">
            <el-form-item label="申请存储空间大小" prop="storageSize">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.storageSize">
              </el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item
              label="申请内存"
              prop="memorySize">
              <el-input :disabled="isDetail" size="small" v-model.trim="msgFormDialog.memorySize">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.clusterSize != null">
            <el-form-item
              label="申请集群规模"
              prop="clusterSize">
              <el-input :disabled="isDetail" size="small" v-model.trim="msgFormDialog.clusterSize">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="left">
          <el-col :span="8" v-if="msgFormDialog.username != null">
            <el-form-item label="用户名" prop="username">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.username"
                placeholder="用户名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.password != null" >
            <el-form-item label="配置密码" prop="password">
              <el-input
                v-model.trim="msgFormDialog.password"
                :disabled="true"
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.externalPort != null && msgFormDialog.externalPort != '0'">
            <el-form-item label="客户端连接端口" prop="externalPort">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.externalPort">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.restApiport != null && msgFormDialog.restApiport != '0'">
            <el-form-item label="API端口" prop="restApiport">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.restApiport"
                placeholder="API端口"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.managerPort != null && msgFormDialog.managerPort != '0'">
          <el-form-item label="管理服务端口" prop="managerPort">
            <el-input
              :disabled="isDetail"
              size="small"
              v-model.trim="msgFormDialog.managerPort"
              placeholder="管理服务端口"
            ></el-input>
          </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.database != null">
            <el-form-item label="初始化数据库名称" prop="database">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.database"
                placeholder="初始化数据库名称"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.adminPassword != null">
            <el-form-item label="管理员密码" prop="adminPassword">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model.trim="msgFormDialog.adminPassword"
                placeholder="管理员密码"
              ></el-input>
            </el-form-item>
          </el-col>

        </el-row>
      </el-card>
      <el-card class="box-card" v-if="msgFormDialog.itserviceId">
        <div slot="header" class="clearfix">
          <span>变更信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="left">
          <el-col :span="8" v-if="msgFormDialog.editCpu != msgFormDialog.cpuSize">
            <el-form-item label="CPU变更：" prop="editCpu">
             <span>{{msgFormDialog.editCpu}}&nbsp; >>&nbsp; {{msgFormDialog.cpuSize}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.editMmemory != msgFormDialog.memorySize">
            <el-form-item label="内存变更：" prop="editCpu">
              <span>{{msgFormDialog.editMmemory}}&nbsp; >>&nbsp; {{msgFormDialog.memorySize}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.storageSize != ''&&msgFormDialog.storageSize != null && msgFormDialog.editStorage != msgFormDialog.storageSize">
            <el-form-item label="存储空间变更：" prop="editCpu">
              <span>{{msgFormDialog.editStorage}}&nbsp; >>&nbsp; {{msgFormDialog.storageSize}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.clusterSize != ''&&msgFormDialog.clusterSize != null && msgFormDialog.editSlaveCount != msgFormDialog.clusterSize">
            <el-form-item label="集群变更：" prop="editCpu">
              <span>{{msgFormDialog.editSlaveCount}}&nbsp; >>&nbsp; {{msgFormDialog.clusterSize}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>审核信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center" v-if="this.handleObj.id == undefined">
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input size="small" v-model.trim="msgFormDialog.examiner" placeholder="审核人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="单位审核材料" prop="examineMaterial">
              <label slot="label">
                <i style="color:red;">*</i>&nbsp;单位审核材料
              </label>
              <el-upload
                v-model.trim="msgFormDialog.examineMaterial"
                class="upload-demo"
                accept=".docx"
                :limit="1"
                :on-exceed="handleExceed"
                :action="upLoadUrl"
                :on-success="successUpload"
                :before-upload="handleBefore"
                :data="uploadData"
              >
                <el-button size="small">点击上传(只能上传docx文件)</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button @click="demoDownload" size="small" type="success">模板下载</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center" v-else>
          <el-col :span="8">
            <el-form-item label="审核状态" prop="examineStatus">
              <el-radio-group
                v-model.trim="msgFormDialog.examineStatus"
                @change="statusChange"
                :disabled="isDetailStatus">
                <el-radio label="02" :disabled="exeFlag">允许</el-radio>
                <el-radio label="03" :disabled="exeFlag">拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.examiner"
                placeholder="审核人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位审核材料" prop="examineMaterial">
              <label slot="label">
                <i style="color:red;">*</i>&nbsp;单位审核材料
              </label>
              <el-button @click="handleExport" size="small" type="success">下载查看审核文件</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item label="意见" prop="rejectReason">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.rejectReason"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card" v-if="!isReason">
        <div slot="header" class="clearfix">
          <span>分配信息</span>
        </div>

        <el-row
          type="flex"
          class="row-bg"
          justify="left"
        >

          <el-col :span="8">
            <el-form-item
              label="实际分配CPU"
              prop="cpu">
              <!--v-if="msgFormDialog.storageLogic !='redis'&& msgFormDialog.storageLogic !='file'"-->
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.cpu">
                <el-option
                  v-for="(item,index) in cpuList"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="实际分配内存"
              prop="memory"
            >
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.memory">
                <!--<el-option :label="msgFormDialog.memorySize" :value="msgFormDialog.memorySize"></el-option>-->
                <el-option
                  v-for="(item,index) in memoryList"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" v-if="msgFormDialog.storageSize != null">
            <el-form-item label="实际分配存储空间" prop="storage">
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.storage">
                <!--<el-option :label="msgFormDialog.storageSize" :value="msgFormDialog.storageSize"></el-option>-->
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
        <el-row type="flex"
                class="row-bg"
                justify="left">

          <el-col :span="8" v-if="msgFormDialog.clusterSize != null">
            <el-form-item label="实际分配集群配置" prop="slaveCount">
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model.trim="msgFormDialog.slaveCount">
                <!--<el-option :label="msgFormDialog.clusterSize" :value="msgFormDialog.clusterSize"></el-option>-->
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
      </el-card>
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')" v-if="!isDetailStatus">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
var baseUrl = process.env.VUE_APP_DM;

import {
  getDictDataByType,
  editCldb,
  zjCldb,
  saveCldb,
  saveLog,
  deployNew,
  exportDemo,
  exprotFile,
  getUserByType,
  editDeployNew,
  gethostsNew
} from "@/api/authorityAudit/middlewareDBaudit";
import { downloadTable } from "@/api/structureManagement/exportTable";
import {
  chineseLengthTenValidation,
  telphoneNumValidation,
  portNumValidation,
  ipUrlValidation
} from "@/components/commonVaildate.js";
import axios from 'axios'
export default {
  name: "handleMiddleDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {

    return {
      newToken:"",
      passwordType: "",
      isReason: false,
      isDetail: false,
      isDetailStatus: false,
      exeFlag:false,
      uploadData: {},
      upLoadUrl: baseUrl + "/dm/yunDatabaseApply/upload",
      isDisabled: false,
      // userBox: [], //获取所有用户
      cpuList:[],
      storageSizeList: [],
      memoryList:[],
      clusterList:[],
      storageLogicList: [],
      contantTaskChose: [],
      hostsList:[],
      msgFormDialog: {
        // viewStatus:"",
        userId: "",
        slaveCount:"",
        name:"",
        port:"",
        password:"",
        externalPort:"",
        restApiport:"",
        managerPort:"",
        database:"",
        adminPassword:"",
        department: "",
        itserviceId:"",
        cluster:"",
        cpu:"",
        memory:"",
        storage:"",
        // telephone: "",tatus
        storageLogic: "",
        displayname: "",
        // applicationSystem: "",
        storageSize: "",
        databaseUse: "",
        clusterSize:"",
        cpuSize: "",
        memorySize:"",
        mountServer: "",
        mountDirectory: "",
        examiner: "",
        examineMaterial: "",
        // token:"",
        remarks:[],

      },

      rules: {

        // rejectReason: [
        //   { required: true, message: "请输入拒绝原因", trigger: "blur" }
        // ],
        cpu: [
          { required: true, message: "请输入实际分配CPU/内存", trigger: "blur" }
        ],
        examineStatus: [
          { required: true, message: "请选择审核状态", trigger: "change" }
        ],
        memory:[
          { required: true, message: "请输入实际分配内存", trigger: "blur" }
        ],
        slaveCount:[
          { required: true, message: "请输入实际分配存储空间", trigger: "blur" }
        ],
        storage: [
          { required: true, message: "请输入实际分配存储空间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // this.getUserAll();
    this.msgFormDialog = this.handleObj;
    this.msgFormDialog.memory = this.msgFormDialog.memorySize;
    this.msgFormDialog.cpu = this.msgFormDialog.cpuSize;
    this.msgFormDialog.storage = this.msgFormDialog.storageSize;
    this.msgFormDialog.slaveCount = this.msgFormDialog.clusterSize;
    this.msgFormDialog.rejectReason = "同意";
    //this.msgFormDialog.userId = "admin";
    this.getDicts("yun_database_type").then(response => {
      this.storageLogicList = response.data;
    });
    // this.getDicts("yun_cup_type").then(response => {
    //   this.cpuList = response.data;
    // });
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
    gethostsNew({}).then(response => {
      debugger
      response = JSON.parse(response.data)
      var host = response.content.content;
      for(var i=0;i<host.length;i++){
        this.hostsList.push(host[i].address);
      }
      });
    this.initServerDetail();
    this.msgFormDialog.examiner = this.$store.getters.name;
  },
  methods: {

    // 上传限制
    handleExceed() {
      this.$message.warning("当前限制选择1个文件");
    },

    handleBefore(file) {
      this.uploadData.filename = file;
    },
    successUpload: function(response, file, fileList) {
      this.msgFormDialog.examineMaterial = response.msg;
    },

    // 文件下载
    handleExport() {
      if (this.msgFormDialog.examineMaterial) {
        downloadTable({ filePath: this.msgFormDialog.examineMaterial }).then(
          res => {
            this.downloadfileCommon(res);
          }
        );
      } else {
        this.$message({
          type: "error",
          message: "当前没有文件可以下载"
        });
      }
    },
    // 模板下载
    demoDownload() {
      exportDemo({
        examineMaterial: this.msgFormDialog.examineMaterial
      }).then(res => {
        this.downloadfileCommon(res);
      });
    },
    // 获取服务器详情
    initServerDetail() {
      if (this.handleObj.id) {
        // 修改
        this.msgFormDialog = this.handleObj;
        this.isDetail = true;
        if (this.msgFormDialog.examineStatus == "02") {
          this.isDetailStatus = true;
        } else if (this.msgFormDialog.examineStatus == "03") {
          this.isDetailStatus = true;
          this.isReason = true;
        } else {
          this.isDetailStatus = false;
        }

        if (this.msgFormDialog.examineStatus == "01") {
          // if(this.msgFormDialog.itserviceId){
            this.msgFormDialog.examineStatus = "02";
          // }
        }
      }
    },
    //
    statusChange(val) {
      if (val == "02") {
        this.isReason = false;
        this.msgFormDialog.rejectReason = "同意";
      } else {
        this.isReason = true;
        this.msgFormDialog.rejectReason = "拒绝";
      }
    },


    async trueDialog(formName) {
      // this.msgFormDialog.remarks=this.msgFormDialog.remarks;
      // console.log(this.msgFormDialog);
      if(this.msgFormDialog.slaveCount !="1"){
        this.msgFormDialog.cluster = true
      } else{
        this.msgFormDialog.cluster = false
      }
      debugger

      if(this.msgFormDialog.storageLogic =="kafka"){
        if(this.msgFormDialog.itserviceId == null) {
          if (this.msgFormDialog.externalIP == null) {
            var index = Math.floor((Math.random() * this.hostsList.length));
            this.msgFormDialog.externalIP = this.hostsList[index];
          }
          if (this.msgFormDialog.slaveCount != "1") {
            this.msgFormDialog.port = "";
            var a = parseInt(this.msgFormDialog.slaveCount);
            for (var i = 0; i < a; i++) {
              if (this.msgFormDialog.port != "") {
                this.msgFormDialog.port = this.msgFormDialog.port + ","
              }
              var b = Math.floor((Math.random() + 3) * 10000);
              var c = b.toString();
              this.msgFormDialog.port = this.msgFormDialog.port + c;
            }
          }
        }
        if(this.msgFormDialog.editSlaveCount != null){
          var q = parseInt(this.msgFormDialog.slaveCount);
          var w = parseInt(this.msgFormDialog.editSlaveCount);
          var e = w-q;
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
      }
      // debugger
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
       this.$refs[formName].validate(valid => {
        if (valid) {

          if (this.handleObj.id) {
            if(this.msgFormDialog.itserviceId == null) {
              deployNew(this.msgFormDialog).then(res => {
                if (res.code == 200) {
                  var  a = JSON.parse(res.data);
                  this.msgFormDialog.itserviceId = a.content.id;
                  this.msgFormDialog.name = a.content.name;
                  editCldb(this.msgFormDialog).then(res =>{
                    this.msgFormDialog.logId = this.msgFormDialog.id;
                    this.msgFormDialog.id = '';
                    this.msgFormDialog.userId = this.$store.getters.userName;
                    this.msgFormDialog.rejectReason = '同意'
                    saveLog(this.msgFormDialog);
                    loading.close();
                    this.$message({
                      type: "success",
                      message: "操作成功"
                    });
                    this.$emit("handleDialogClose");
                  });
                  // this.msgFormDialog.viewStatus = '2';


                } else {
                  this.$message({
                    type: "error",
                    message: res.message
                  });
                }
              });
            }else if (this.msgFormDialog.itserviceId != null){

              editDeployNew(this.msgFormDialog, this.msgFormDialog.storageLogic,this.msgFormDialog.itserviceId).then(res => {
                if (res.code == 200) {
                  this.msgFormDialog.examineStatus = '02';
                  editCldb(this.msgFormDialog).then(res => {
                    this.msgFormDialog.logId = this.msgFormDialog.id;
                    this.msgFormDialog.id = '';
                    this.msgFormDialog.userId = this.$store.getters.userName;
                    this.msgFormDialog.rejectReason = '变更申请同意'
                    saveLog(this.msgFormDialog);
                    loading.close();
                    this.$message({
                      type: "success",
                      message: "操作成功"
                    });
                    this.$emit("handleDialogClose");
                  });
                  // this.msgFormDialog.viewStatus = '2';

                } else {
                  this.$message({
                    type: "error",
                    message: res.message
                  });
                }
              });

            } else{
              if(this.msgFormDialog.examineStatus == '03'){
                if(this.msgFormDialog.itserviceId){
                  // this.msgFormDialog.examineStatus = '02'
                  this.msgFormDialog.viewStatus = '5';
                }else{
                  this.msgFormDialog.viewStatus = '4';
                }
              }
              editCldb(this.msgFormDialog).then(res => {
                if (res.code == 200) {

                  this.msgFormDialog.logId = this.msgFormDialog.id;
                  this.msgFormDialog.id = '';
                  this.msgFormDialog.userId = this.$store.getters.userName;
                  saveLog(this.msgFormDialog);
                  loading.close();
                  this.$message({
                    type: "success",
                    message: "操作成功"
                  });
                  this.$emit("handleDialogClose");
                  // this.getList();
                } else {
                  this.$message({
                    type: "error",
                    message: res.msg
                  });
                }
              });
            }
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });

    },
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.$emit("handleDialogClose", false);
    }
  }
};
</script>

<style lang="scss">
.handleMiddleDialog {
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
