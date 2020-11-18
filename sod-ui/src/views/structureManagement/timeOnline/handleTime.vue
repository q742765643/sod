<template>
  <section class="timeOnlineDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <div class="publicData">
        <div class="publicDataTitle">
          <i class="el-icon-price-tag"></i>手动配置
        </div>
        <div class="publicInfo">
          <el-form-item label="开始日期" prop="beginTime">
            <el-date-picker v-model.trim="msgFormDialog.beginTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
          <el-form-item label="结束日期" prop="endTime">
            <el-date-picker v-model.trim="msgFormDialog.endTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </div>
      </div>

      <div class="publicData" v-for="databaseInfo in distinctDataBaseInfo">
        <div class="publicDataTitle">
          <i class="el-icon-price-tag"></i>{{databaseInfo.DATABASE_NAME}}
        </div>
        <div class="publicInfo">
          <el-form-item label="开始日期">
            <el-date-picker v-model.trim="databaseInfo.BEGIN_TIME" type="date" placeholder="yy-MM-dd" disabled></el-date-picker>
          </el-form-item>
          <el-form-item label="结束日期">
            <el-date-picker v-model.trim="databaseInfo.END_TIME" type="date" placeholder="yy-MM-dd" disabled></el-date-picker>
          </el-form-item>
        </div>
      </div>

      <div class="publicData">
        <div class="publicDataTitle">
          <i class="el-icon-price-tag"></i>阈值设置
        </div>
        <div class="publicInfo">
          <el-form-item label="开始日期" prop="boundBeginTime">
            <el-date-picker v-model.trim="msgFormDialog.boundBeginTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
          <el-form-item label="结束日期" prop="boundEndTime">
            <el-date-picker v-model.trim="msgFormDialog.boundEndTime" type="date" placeholder="yy-MM-dd"></el-date-picker>
          </el-form-item>
        </div>
      </div>

      <el-form-item label="结束日期（当天）" prop="sectionId" class="lastDate">
        <el-checkbox v-model.trim="msgFormDialog.checkFlag"></el-checkbox>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { update,getByClassId } from "@/api/structureManagement/timeOnline";
export default {
  name: "timeOnlineDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        beginTime: "",
        endTime: "",
        boundBeginTime:"",
        boundEndTime:"",
        checkFlag: false,
        endTimeFlag: "",
        dataClassId: ""
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
        this.msgFormDialog.beginTime = this.dataOnlineTime.beginTime;
        this.msgFormDialog.endTime = this.dataOnlineTime.endTime;
        this.msgFormDialog.boundBeginTime = this.dataOnlineTime.boundBeginTime;
        this.msgFormDialog.boundEndTime = this.dataOnlineTime.boundEndTime;
      }
    });
  },
  methods: {
    // 获取详情
    initDetail() {},

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.msgFormDialog.checkFlag) {
            this.msgFormDialog.endTimeFlag = "today";
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
