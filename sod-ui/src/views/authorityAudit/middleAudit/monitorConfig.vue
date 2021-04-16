<template>
  <section class="handleMonitorConfig">
    <el-tabs type="border-card" v-model.trim="activeName">
      <el-tab-pane label="性能指标" name="first">
        <el-checkbox-group v-model="checkList">
          <el-tooltip
            v-for="(item,index) in performance"
            :key="index"
            :content="item.desc"
            placement="top">
          <el-checkbox
            @change="getcheck(item)"
            style="width: 300px"
            :label="item.name"
            :value="item.name"></el-checkbox>
          </el-tooltip>
        </el-checkbox-group>
      </el-tab-pane>
      <el-tab-pane label="业务指标" name="second">
          <el-checkbox-group v-model="checkList">
            <el-tooltip
              v-for="(item,index) in business"
              :key="index"
              :content="item.desc"
              placement="top">
            <el-checkbox
              @change="getcheck(item)"
              style="width: 300px"
              :label="item.name"
              :value="item.name"></el-checkbox>
            </el-tooltip>
          </el-checkbox-group>
      </el-tab-pane>
    </el-tabs>

    <div slot="footer" class="dialog-footer" style="margin-top: 20px">
      <el-button type="primary" @click="edit" >确 定</el-button>
      <el-button @click="cancelDialog">取 消</el-button>
    </div>
  </section>
</template>

<script>


  import {
    getConfigNew,
    editConfigNew
  } from "@/api/authorityAudit/middlewareDBaudit";
import axios from 'axios';


export default {

  name: "handleMonitorConfig",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      dialogVisible: false,
      activeName:'first',
      msgDialog:"",
      checkList:[],
      performance:[],
      business:[],
      allCheckedData:[],
      configData:{
        type:"",
        id:"",
        data:[]
      },
      rules: {
      }
    };
  },
  created(){
    this.msgDialog = this.handleObj

    getConfigNew(this.msgDialog).then((res)=>{
      var a= JSON.parse(res.data)
      if(res.code == '200'){
        var data = a.content;
        this.allCheckedData = a.content;
        for(var i=0;i<data.length;i++){
          if(data[i].type == '性能'){
            this.performance.push(data[i]);
          }else{
            this.business.push(data[i]);
          }
          if(data[i].checked == '1'){
            this.checkList.push(data[i].name)
          }
        };
        for(var i=0;i<this.allCheckedData.length;i++){
          if(this.allCheckedData[i].checked == '1'){
            this.allCheckedData[i]['show'] = true;
          }else{
            this.allCheckedData[i]['show'] = false;
          }
        };
        // console.log(this.allCheckedData)
        // debugger
      }

    });

  },
  methods: {


    getcheck(data){

      if (event.target.checked == true) {
        for(var i=0;i<this.allCheckedData.length;i++){
          if(this.allCheckedData[i].name == data.name){
            this.allCheckedData[i].checked = '1';
            this.allCheckedData[i].show = true;
          }
        };

      } else {
        for(var i=0;i<this.allCheckedData.length;i++){
          if(this.allCheckedData[i].name == data.name){
            this.allCheckedData[i].checked = '0';
            this.allCheckedData[i].show = false;
          }
        };

      }
      // console.log(this.allCheckedData)
      // debugger
    },
    edit(){
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      this.configData.type = this.msgDialog.type;
      this.configData.id = this.msgDialog.id;
      this.configData.data = this.allCheckedData
      editConfigNew(this.configData).then((res)=>{
        // console.log(this.allCheckedData)
        // console.log(this.configData.data)
        if(res.code == '200'){
          loading.close();
          console.log('修改监控配置成功');
          this.$emit("handleDialogClose", false);
        }
      });

    },

    cancelDialog() {
      this.$emit("handleDialogClose", false);
    }
  }
};
</script>

<style lang="scss">
.handleMonitorConfig{
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
