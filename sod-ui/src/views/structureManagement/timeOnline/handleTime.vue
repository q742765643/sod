<template>
  <section class="timeOnlineDialog">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-tooltip class="item" effect="dark" content="蓝框标注：开始时间最早时间，结束时间最晚时间；存在手动配置，以手动配置优先级最高" placement="top-start">
        <span>资料在线时间统计规则？</span>
        </el-tooltip>
      </div>
    <el-table
      border
      :data="distinctDataBaseInfo"
      row-key="id"
      ref="singleTable"
      highlight-current-row
    >
      <el-table-column prop="DATABASE_NAME" label="数据库类型"  :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="DATA_CLASS_ID" label="存储编码"  :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="BEGIN_TIME" label="开始时间" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <!--border:5px solid #4aff38-->
          <span v-if="!scope.row.BEGIN_TIME ">{{ '---' }}</span>
          <!--<span v-if="minTimeData != scope.row.BEGIN_TIME && scope.row.BEGIN_TIME != '' && scope.row.BEGIN_TIME != null">{{ parseTime(scope.row.BEGIN_TIME) }}</span>-->
          <span v-else
                style="padding: 2px"
                v-bind:class="{
                grennBorder1:scope.row.BEGIN_TIME == minTimeData && !handStyle && !a,
                grennBorder:scope.row.BEGIN_TIME == handData.BEGIN_TIME && handStyle,
                grennBorder2:scope.row.DATABASE_NAME == '结构化数据库' && !handStyle && a}">{{parseTime(scope.row.BEGIN_TIME)}}</span>
          <!--<span v-else-if="scope.row.BEGIN_TIME && handStyle" v-bind:class="{grennBorder:handStyle && scope.row.BEGIN_TIME == handData.BEGIN_TIME}">{{parseTime(scope.row.BEGIN_TIME)}}</span>-->
        </template>
      </el-table-column>
      <el-table-column prop="END_TIME" label="结束时间" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="!scope.row.END_TIME">{{ '---' }}</span>
          <!--<span v-if="maxTimeData != scope.row.END_TIME && scope.row.END_TIME != '' && scope.row.END_TIME != null">{{ parseTime(scope.row.END_TIME) }}</span>-->
          <span v-else
                style="padding: 2px"
                v-bind:class="{
                redBorder1:scope.row.END_TIME == maxTimeData && !handStyle1 && !b,
                redBorder:scope.row.END_TIME == handData.END_TIME && handStyle1,
                redBorder2:scope.row.DATABASE_NAME == '结构化数据库' && !handStyle1 && b}">{{parseTime(scope.row.END_TIME)}}</span>
          <!--<span v-else v-bind:class="{redBorder:maxTimeData == scope.row.END_TIME}">{{maxStyle(scope.row.END_TIME)}}</span>-->
          <!--<span v-if="maxTimeData == scope.row.END_TIME && scope.row.END_TIME != '' && scope.row.END_TIME != null" style="border:5px solid #ff2b1a">{{ parseTime(scope.row.END_TIME) }}</span>-->
        </template>
      </el-table-column>
    </el-table>
      <!--<table-->
        <!--border>-->
        <!--<tr>-->
          <!--<td>手动配置</td>-->
          <!--<td></td>-->
          <!--<td></td>-->
          <!--<td></td>-->
        <!--</tr>-->
      <!--</table>-->
    </el-card>
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="150px"
      label-position="right">

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-checkbox-button size="small" @change="changeTime1" >修改</el-checkbox-button>
        <span style="margin-left: 10px">手动配置</span>
      </div>
      <el-row type="flex" class="row-bg" justify="left" >
        <el-col :span="8" >
          <el-form-item label="开始日期" prop="beginTime">
            <el-date-picker :disabled="exchangeFlag1" v-model.trim="msgFormDialog.beginTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="!endFlag1">
          <el-form-item label="结束日期" prop="endTime">
            <el-date-picker :disabled="exchangeFlag1" v-model.trim="msgFormDialog.endTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="endFlag1" style="width: 500px">
          <el-form-item label="结束日期（当天）" prop="sectionId" >
            <el-input
              size="small"
              v-model.trim="msgFormDialog.handDate"
              style="width: 100px"><i slot="suffix" style="font-style:normal;margin-right: 10px;">天</i></el-input>
            <el-button style="height:35px;margin-left: 1px;border-radius: 20px;" icon="el-icon-back" @click="cutDate1"></el-button>
            <el-button style="height:35px;margin-left: 1px;border-radius: 20px;" icon="el-icon-right" @click="addDate1"></el-button>
          </el-form-item>
        </el-col>
        <!--<el-button :disabled="exchangeFlag1" type="primary" @click="changeTimeType1" style="height: 35px;margin-left: 20px">日期类型</el-button>-->
        <el-button round :disabled="exchangeFlag1" type="primary" @click="changeTimeType1" style="height: 35px;margin-left: 20px" icon="el-icon-sort" circle></el-button>
      </el-row>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-checkbox-button size="small" @change="changeTime2" >修改</el-checkbox-button>
        <span style="margin-left: 10px">阈值配置</span>
        <!--<el-button type="primary" @click="" style="width:40px;height:20px;border-radius:10px;background: #1e9fff"></el-button>-->
      </div>
      <el-row type="flex" class="row-bg" justify="left">
        <el-col :span="8" >
          <el-form-item label="开始日期" prop="boundBeginTime">
            <el-date-picker :disabled="exchangeFlag2" v-model.trim="msgFormDialog.boundBeginTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="!endFlag2">
          <el-form-item label="结束日期" prop="boundEndTime" >
            <el-date-picker :disabled="exchangeFlag2" v-model.trim="msgFormDialog.boundEndTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8" v-if="endFlag2" style="width: 500px">
          <el-form-item label="结束日期（当天）" prop="sectionId" >
            <el-input
              size="small"
              v-model.trim="msgFormDialog.thresholdDate"
              style="width: 100px"><i slot="suffix" style="font-style:normal;margin-right: 10px;">天</i></el-input>
              <!--<span>天</span>-->
            <el-button style="height:35px;margin-left: 1px;border-radius: 20px;" icon="el-icon-back" @click="cutDate2"></el-button>
            <el-button style="height:35px;margin-left: 1px;border-radius: 20px;" icon="el-icon-right" @click="addDate2"></el-button>
          </el-form-item>

        </el-col>
        <el-button round type="primary" :disabled="exchangeFlag2" @click="changeTimeType2" style="height: 35px;margin-left: 20px" icon="el-icon-sort" circle></el-button>
      </el-row>
    </el-card>
    </el-form>
    <div class="dialog-footer" slot="footer" style="padding-top: 20px">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { update,getByClassId } from "@/api/structureManagement/timeOnline";
import {parseTime} from "@/utils/ruoyi"
export default {
  name: "timeOnlineDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      a:false,
      b:false,
      handStyle:false,
      handStyle1:false,
      minStyleStatus:false,
      maxStyleStatus:false,
      minTimeData:"",
      maxTimeData:"",
      exchangeFlag1:true,
      exchangeFlag2:true,
      endFlag1:false,
      endFlag2:false,
      num1:0,
      num2:0,
      startStyle:"",
      enfStyle:"",
      msgFormDialog: {
        // 这里定义弹框内的参数
        // databaseName:"",
        handDate:'今',
        thresholdDate:'今',
        beginTime: "",
        endTime: "",
        boundBeginTime:"",
        boundEndTime:"",
        checkFlag: false,
        endTimeFlag: 0,
        boundEndTimeFlag:0,
        dataClassId: ""
      },
      handData:{
        DATABASE_NAME:"手动配置",
        DATA_CLASS_ID:"",
        BEGIN_TIME:"",
        END_TIME:"",
      },
      distinctDataBaseInfo: [],
      dataOnlineTime:{},
      rules: {
        beginTime: [{ required: false, message: "请选择时间", trigger: "blur" }],
        endTime: [{ required: false, message: "请选择时间", trigger: "blur" }],
        boundBeginTime: [{ required: false, message: "请选择时间", trigger: "blur" }],
        boundEndTime: [{ required: false, message: "请选择时间", trigger: "blur" }],
      }
    };
  },
  created() {

    this.msgFormDialog.dataClassId = this.handleObj.DATA_CLASS_ID;
    if (this.handleObj.obj && this.handleObj.obj.endTimeFlag == "today") {
      this.msgFormDialog.checkFlag = true;
    }
    getByClassId({ data_class_id: this.handleObj.DATA_CLASS_ID }).then((res) => {
      console.log(res.data);
      this.distinctDataBaseInfo = res.data.distinctDataBaseInfo;
      this.dataOnlineTime =  res.data.dataOnlineTime
      if(this.dataOnlineTime){
        this.handData.BEGIN_TIME = this.dataOnlineTime.beginTime;
        if(this.dataOnlineTime.endTimeFlag != ''){
        this.handData.END_TIME = new Date().getTime() + this.dataOnlineTime.endTimeFlag* 24 * 60 * 60 * 1000;
        }else {
          this.handData.END_TIME = this.dataOnlineTime.endTime;
        }
        if(this.handData.BEGIN_TIME){this.handStyle = true;}
        if(this.handData.END_TIME){this.handStyle1 = true;}
      }
      this.distinctDataBaseInfo.push(this.handData)
      console.log(this.distinctDataBaseInfo)

      for(var i=0;i<this.distinctDataBaseInfo.length;i++){
          var j = i+1;
          var a = [];
          for(var key in this.distinctDataBaseInfo[i]){
            a.push(key);
          }
          if(a.length > 4 ){
            if(this.minTimeData == ''){
              this.minTimeData = this.distinctDataBaseInfo[i].BEGIN_TIME
            }
            if(this.minTimeData > this.distinctDataBaseInfo[i].BEGIN_TIME){
              this.minTimeData = this.distinctDataBaseInfo[i].BEGIN_TIME
            }
          }
        if(a.length > 4 ){
          if(this.maxTimeData == ''){
            this.maxTimeData = this.distinctDataBaseInfo[i].END_TIME
          }
          if(this.maxTimeData < this.distinctDataBaseInfo[i].END_TIME){
            this.maxTimeData = this.distinctDataBaseInfo[i].END_TIME
          }
        }
      }
      var minNum = 0;
      var maxNum = 0;
      for(var i=0;i<this.distinctDataBaseInfo.length;i++){
        if(this.minTimeData == this.distinctDataBaseInfo[i].BEGIN_TIME){
          minNum = minNum+1;
        }
        if(this.maxTimeData == this.distinctDataBaseInfo[i].END_TIME){
          maxNum = maxNum+1;
        }
      }
      if(minNum > 1){
        this.a = true;
      }
      if(maxNum > 1){
        this.b = true;
      }
      // this.dataOnlineTime =  res.data.dataOnlineTime
      // if(this.dataOnlineTime != null){
      // this.handData.BEGIN_TIME = this.dataOnlineTime.beginTime;
      // this.handData.END_TIME = this.dataOnlineTime.endTime;
      // }
      // this.distinctDataBaseInfo.push(this.handData)
      if(this.dataOnlineTime){
        this.msgFormDialog.beginTime = this.dataOnlineTime.beginTime;

        this.msgFormDialog.endTime = this.dataOnlineTime.endTime;

        this.msgFormDialog.boundBeginTime = this.dataOnlineTime.boundBeginTime;
        this.msgFormDialog.boundEndTime = this.dataOnlineTime.boundEndTime;
        debugger
        if(this.dataOnlineTime.endTimeFlag != '' && this.dataOnlineTime.endTimeFlag != null){
          this.msgFormDialog.endTimeFlag = parseInt(this.dataOnlineTime.endTimeFlag);
          // this.num1 = Math.round(((this.dataOnlineTime.endTimeFlag) - (new Date().getTime()))/86400000);
          if(this.dataOnlineTime.endTimeFlag > 0){
            this.msgFormDialog.handDate = '';
            this.msgFormDialog.handDate = '后'+this.dataOnlineTime.endTimeFlag
          }else if(this.dataOnlineTime.endTimeFlag == 0){
            this.msgFormDialog.handDate = '今'
          }else{
            // this.num1 = -this.num1;
            this.msgFormDialog.handDate = '';
            this.msgFormDialog.handDate = '前'+(-this.dataOnlineTime.endTimeFlag)
          }
        }
        if(this.dataOnlineTime.boundEndTimeFlag != ''&&this.dataOnlineTime.endTimeFlag != null) {
          this.num2 = Math.round(((this.dataOnlineTime.boundEndTimeFlag) - (new Date().getTime())) / 86400000);
          if (this.num2 > 0) {
            this.msgFormDialog.thresholdDate = '';
            this.msgFormDialog.thresholdDate = '后' + this.num2
          } else if (this.num2 == 0) {
            this.msgFormDialog.thresholdDate = '今'
          } else {
            // this.num2 = -this.num2;
            this.msgFormDialog.thresholdDate = '';
            this.msgFormDialog.thresholdDate = '前' + (-this.num2)
          }
        }

      }

    });
  },
  methods: {
    // 获取详情
    initDetail() {},

    changeTimeType1(){
      this.endFlag1 = !this.endFlag1;
    },
    changeTimeType2(){
      this.endFlag2 = !this.endFlag2;
    },
    changeTime1(){
      this.exchangeFlag1 = !this.exchangeFlag1;
      this.endFlag1 = false
    },
    changeTime2(){
      this.exchangeFlag2 = !this.exchangeFlag2;
      this.endFlag2 = false
    },
    addDate1(){
      debugger
      this.msgFormDialog.endTimeFlag = this.msgFormDialog.endTimeFlag + 1;
      if(this.msgFormDialog.endTimeFlag > 0){
        this.msgFormDialog.handDate = '';
        this.msgFormDialog.handDate = '后'+this.msgFormDialog.endTimeFlag
      }else if(this.msgFormDialog.endTimeFlag == 0){
        this.msgFormDialog.handDate = '今'
      }else{
        // this.num1 = -this.num1;
        this.msgFormDialog.handDate = '';
        this.msgFormDialog.handDate = '前'+(-this.msgFormDialog.endTimeFlag)
      }
      // var today = new Date().getTime();
      // this.msgFormDialog.endTimeFlag = today + this.num1* 24 * 60 * 60 * 1000;

    },
    addDate2(){
      this.num2 = this.num2 + 1;
      if(this.num2 > 0){
        this.msgFormDialog.thresholdDate = '';
        this.msgFormDialog.thresholdDate = '后'+this.num2
      }else if(this.num2 == 0){
        this.msgFormDialog.thresholdDate = '今'
      }else{
        // this.num2 = -this.num2;
        this.msgFormDialog.thresholdDate = '';
        this.msgFormDialog.thresholdDate = '前'+(-this.num2)
      }
      var today = new Date().getTime();
      this.msgFormDialog.boundEndTimeFlag = today + this.num2* 24 * 60 * 60 * 1000;

    },
    cutDate1(){
      this.msgFormDialog.endTimeFlag = this.msgFormDialog.endTimeFlag - 1;
      if(this.msgFormDialog.endTimeFlag > 0){
        this.msgFormDialog.handDate = '';
        this.msgFormDialog.handDate = '后'+this.msgFormDialog.endTimeFlag
      }else if(this.msgFormDialog.endTimeFlag == 0){
        this.msgFormDialog.handDate = '今'
      }else{
        // this.num1 = -this.num1;
        this.msgFormDialog.handDate = '';
        this.msgFormDialog.handDate = '前'+(-this.msgFormDialog.endTimeFlag)
      }
      // var today = new Date().getTime();
      // this.msgFormDialog.endTimeFlag = today + this.num1* 24 * 60 * 60 * 1000;

    },
    cutDate2(){
      this.num2 = this.num2 - 1;
      if(this.num2 > 0){
        this.msgFormDialog.thresholdDate = '';
        this.msgFormDialog.thresholdDate = '后'+this.num2
      }else if(this.num2 == 0){
        this.msgFormDialog.thresholdDate = '今'
      }else{
        // this.num2 = -this.num2;
        this.msgFormDialog.thresholdDate = '';
        this.msgFormDialog.thresholdDate = '前'+(-this.num2)
      }
      var today = new Date().getTime();
      this.msgFormDialog.boundEndTimeFlag = today + this.num2* 24 * 60 * 60 * 1000;

    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // if (this.msgFormDialog.checkFlag) {
          //   this.msgFormDialog.endTimeFlag = "today";
          // }
          if(this.endFlag2){
            if(this.msgFormDialog.thresholdDate == '今'){
              this.msgFormDialog.boundEndTimeFlag = new Date().getTime();
            }
            this.msgFormDialog.boundEndTime = ''
          }else{
            this.msgFormDialog.boundEndTimeFlag = ''
          }
          if(this.endFlag1){
            if(this.msgFormDialog.handDate == '今'){
              this.msgFormDialog.endTimeFlag = 0;
            }
            this.msgFormDialog.endTime = ''
          }else{
            this.msgFormDialog.endTimeFlag = ''
          }


          console.log(this.msgFormDialog);
          update(this.msgFormDialog).then(res => {
            if (res.code == 200) {
              this.$message({
                message: "操作成功",
                type: "success"
              });
              this.$emit("cancelHandle");
            } else {
              this.$message({
                message: res.msg,
                type: "error"
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    }
  }
};
</script>
<style lang="scss">
.timeOnlineDialog {
  .el-date-editor.el-input {
    width: 100%;
  }
  .grennBorder{
    border:2px solid #236bff
  }
  .grennBorder1{
    border:2px solid #236bff
  }
  .grennBorder2{
    border:2px solid #236bff
  }
  .redBorder{
    border:2px solid #236bff
  }
  .redBorder1{
    border:2px solid #236bff
  }
  .redBorder2{
    border:2px solid #236bff
  }
  .el-checkbox-button{
    width: 70px;
    height: 40px;
    border-radius: 20px;
  }
  .publicDataTitle {
    box-sizing: border-box;
    padding: 10px;
    border: 1px solid #ebeef5;
    background: #f2f2f2;

    i {
      font-size: 16px;
      margin-right: 4px;
    }
  }
  .publicInfo{
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-wrap: wrap;
    align-items: center;
    border: 1px solid #ebeef5;
    border-top: none;
    padding-top: 10px;
    margin-bottom: 10px;

    .el-form-item {
      margin-bottom: 18px;
      text-align: center;
      width: 50%;
      margin-right: 0px;
    }

  }
  .lastDate label{
    width: 140px !important;
  }
}

</style>
