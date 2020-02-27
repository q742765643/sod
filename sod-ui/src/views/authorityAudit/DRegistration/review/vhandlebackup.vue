<template>
  <section class="vhandlebackupDialog">
    <el-form ref="backupForm" :model="backupForm" label-width="120px" :rules="vrules" size="mini">
      <el-form-item label="数据库名称" v-if="!isEdit" prop="databaseId">
        <el-select
          v-model="backupForm.databaseId"
          @change="selDatabase"
          placeholder="请选择数据库"
          size="small"
        >
          <el-option
            v-for="(item,index) in databases"
            :key="index"
            :label="item.TEXT"
            :value="item.VALUE"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="资料名称" v-if="!isEdit" prop="dataclass_value">
        <template>
          <el-transfer
            class="transfer-class"
            style="width: 100%;"
            :props="{ key: 'value',label: 'text'}"
            :titles="['可选资料', '已选资料']"
            v-model="backupForm.dataclass_value"
            :data="dataclass_data"
          ></el-transfer>
        </template>
      </el-form-item>
      <el-row>
        <el-col :span="12" v-if="isEdit">
          <el-form-item label="数据库名称">
            <el-input size="small" v-model="backupForm.specialDatabaseName" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="isEdit">
          <el-form-item label="资料名称">
            <el-input size="small" v-model="backupForm.dataServiceName" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-row>
            <el-col :span="14">
              <el-form-item label="近时备份间隔" prop="backupTimeUnit">
                <el-input-number
                  size="small"
                  v-model="backupForm.firstTimePeriod"
                  controls-position="right"
                  :min="1"
                  :max="1000"
                ></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item class="unitFormItem">
                <el-select size="small" v-model="backupForm.backupTimeUnit" placeholder="请选择">
                  <el-option
                    v-for="item in backupTimeUnit_data"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最终备份间隔" prop="lastTimePeriod">
            <el-input-number
              size="small"
              v-model="backupForm.lastTimePeriod"
              controls-position="right"
              :min="1"
              :max="1000"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行IP和端口" prop="execIp">
            <el-select size="small" v-model="backupForm.execIp" placeholder="请选择">
              <el-option
                v-for="item in execIps"
                :key="item.value"
                :label="item.TEXT"
                :value="item.VALUE"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-row>
            <el-col :span="20">
              <el-form-item label="执行策略" prop="cron">
                <el-input size="small" v-model="backupForm.cron" placeholder="请选择执行策略"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item class="unitFormItem">
                <el-button size="small" type="primary" plain @click="cronDialogVisible = true">CRON</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否告警" prop="alarm">
            <el-switch
              v-model="backupForm.alarm"
              active-text="启用"
              active-value="on"
              off-color="#F04134"
              inactive-text="禁用"
              inactive-value="off"
              active-color="#13ce66"
              inactive-color="#ff4949"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="超时时间" prop="timeout">
            <el-input-number size="small" v-model="backupForm.timeout" :min="1" :max="10"></el-input-number>
            <template>
              <el-link type="info">分钟</el-link>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重试次数" prop="retryCount">
            <el-input-number size="small" v-model="backupForm.retryCount" :min="1" :max="18"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重试间隔时间" prop="betweenRetries">
            <el-input-number size="small" v-model="backupForm.betweenRetries" :min="1" :max="10"></el-input-number>
            <template>
              <el-link type="info">分钟</el-link>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="存储目录" prop="fileStoragedir">
            <el-select size="small" v-model="backupForm.fileStoragedir" placeholder="请选择存储目录">
              <el-option
                v-for="(item,index) in dataBaseFromDir"
                :key="index"
                :label="item.TEXT"
                :value="item.VALUE"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onSubmit('backupForm')">确 定</el-button>
      <el-button v-if="showqueryBtn==1" @click="cancelDialog('backupForm')">取 消</el-button>
      <!--<el-button type="primary" @click="resetForm('backupForm')">重 置</el-button>-->
    </div>
    <Cron
      append-to-body
      v-if="cronDialogVisible"
      :cronDialogVisible="cronDialogVisible"
      @closeCron="closeCron"
      @setCron="setCron"
    />
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// //cron表达式
import Cron from "@/components/cron/Cron";
// import {
//   testExport,
//   chineseLengthTenValidation,
//   telphoneNumValidation,
//   portNumValidation,
//   ipUrlValidation
// } from "@/components/common.js";
export default {
  name: "vhandlebackupDialog",
  components: {
    Cron
  },
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      isEdit: false,
      backupForm: {
        firstTimePeriod: 1,
        timeout: 1,
        retryCount: 1,
        betweenRetries: 1,
        databaseId: "",
        dataclass_value: "",
        dataclass_data: ""
      },
      backupTimeUnit_data: [
        { label: "日", value: "日" },
        { label: "月", value: "月" },
        { label: "年", value: "年" }
      ],
      cronDialogVisible: false,
      execIps: [],
      dataBaseFromDir: [],
      dataclass_data: [],
      databases: [],
      showqueryBtn: 1,
      vrules: {
        databaseId: [
          { required: true, message: "请选择数据库", trigger: "change" }
        ],
        dataclass_value: [
          { required: true, message: "请选择资料", trigger: "change" }
        ],
        backupTimeUnit: [
          { required: true, message: "请输入近时备份间隔", trigger: "change" }
        ],
        lastTimePeriod: [
          { required: true, message: "请输入最终备份间隔", trigger: "change" }
        ],
        execIp: [
          { required: true, message: "请选择执行主机和端口", trigger: "change" }
        ],
        cron: [
          { required: true, message: "请选择执行策略", trigger: "change" }
        ],
        timeout: [
          { required: true, message: "请输入超时时间", trigger: "change" }
        ],
        retryCount: [
          { required: true, message: "请输入重试次数", trigger: "change" }
        ],
        betweenRetries: [
          { required: true, message: "请输入重试间隔时间", trigger: "change" }
        ],
        fileStoragedir: [
          { required: true, message: "请选择存储目录", trigger: "change" }
        ]
      }
    };
  },
  created() {
    // debugger;
    // this.axios.get(interfaceObj.databaseBackup_getIpList).then(res => {
    //   this.execIps = res.data.data;
    // });
    // this.axios
    //   .get(interfaceObj.databaseBackup_databases, {
    //     params: { proName: "backup" }
    //   })
    //   .then(res => {
    //     this.databases = res.data.data;
    //   });
    // this.axios
    //   .get(interfaceObj.databaseBackup_DataBaseFromDir, {
    //     params: { type: 38 }
    //   })
    //   .then(res => {
    //     this.dataBaseFromDir = res.data.data;
    //   });
    // this.init();
  },
  methods: {
    async init() {
      if (this.handleObj.taskID == undefined) {
        this.isEdit = false;
        if (this.handleObj.pageName == "存储结构概览备份") {
          this.selDatabase(this.handleObj.databaseId);
          if (this.handleObj.databaseId) {
            this.backupForm.databaseId = this.handleObj.databaseId;
          }
          this.backupForm.dataclass_value = this.handleObj.dataclass_value;
          if (this.handleObj.showqueryBtn) {
            this.showqueryBtn = this.handleObj.showqueryBtn;
          }
        } else if (this.handleObj.pageName == "资料存储策略") {
          await this.selDatabase(this.handleObj.databaseId);
          this.backupForm = this.handleObj;
        }
      } else {
        this.isEdit = true;
        this.backupForm = this.handleObj;
        this.backupForm.execIp =
          this.handleObj.execIp + ":" + this.handleObj.execPort;
      }
      //
    },
    selDatabase(ev) {
      this.axios
        .get(interfaceObj.databaseBackup_findTableMap, {
          params: { databaseId: ev }
        })
        .then(res => {
          this.dataclass_data = res.data.data;
        });
    },
    //设置cron表达式
    setCron(cronExpression) {
      this.backupForm.cron = cronExpression;
      this.cronDialogVisible = false;
    },
    closeCron() {
      this.cronDialogVisible = false;
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("trueBackup", this.backupForm);
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
.vhandlebackupDialog {
  .el-transfer-panel {
    width: 320px;
  }
}
</style>
