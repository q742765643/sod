<template>
  <section class="handleMsgDialog">
    <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog" label-width="130px">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>资料信息</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库" prop="physics_database">
              <el-select
                size="small"
                v-model="msgFormDialog.physics_database"
                :disabled="dataIsDisabled"
                @change="databaseChange"
              >
                <el-option
                  v-for="(item,index) in physics_database_list"
                  :key="index"
                  :label="item.TEXT"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资料名称" prop="databasename">
              <el-select
                size="small"
                v-model="msgFormDialog.databasename"
                :disabled="dataIsDisabled"
                @change="databasenameChange"
              >
                <el-option
                  v-for="(item,index) in databasename_list"
                  :key="index"
                  :label="item.text"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="表名" prop="tablename">
              <el-input size="small" v-model="msgFormDialog.tablename" :disabled="isDisabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="四级编码" prop="data_class_id">
              <el-input size="small" v-model="msgFormDialog.data_class_id" :disabled="isDisabled"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>一级NAS迁移</span>
        </div>
        <el-row>
          <el-col :span="24">
            <el-row>
              <el-col :span="20">
                <el-form-item label="过期时间" prop="data_overduetime1">
                  <el-input
                    size="small"
                    v-model="msgFormDialog.data_overduetime1"
                    :disabled="isDisabled"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" class="brotherItem">
                <el-form-item label prop="time_unit1">
                  <el-select size="small" v-model="msgFormDialog.time_unit1" :disabled="isDisabled">
                    <el-option label="日" value="日"></el-option>
                    <el-option label="月" value="月"></el-option>
                    <el-option label="年" value="年"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="迁移源目录" prop="source_directory1">
              <el-select
                size="small"
                v-model="msgFormDialog.source_directory1"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="(item,index) in source_directory_list"
                  :key="index"
                  :label="item.VALUE"
                  :value="item.VALUE"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="迁移目标目录" prop="file_storagedir1">
              <el-select
                size="small"
                v-model="msgFormDialog.file_storagedir1"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="(item,index) in file_storagedir_list"
                  :key="index"
                  :label="item.VALUE"
                  :value="item.VALUE"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="执行IP和端口" prop="exec_ipAndPort1">
              <el-select
                size="small"
                v-model="msgFormDialog.exec_ipAndPort1"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="(item,index) in ipAndPort_list"
                  :key="index"
                  :label="item.VALUE"
                  :value="item.VALUE"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-row>
              <el-col :span="20">
                <el-form-item label="执行策略" prop="cron1">
                  <el-input size="small" v-model="msgFormDialog.cron1" :disabled="isDisabled"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" class="brotherItem">
                <el-button
                  size="small"
                  type="primary"
                  @click="cronClick1"
                  :disabled="isDisabled"
                >配置策略</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12" class="switchBox">
            <el-form-item label="是否告警" prop="alarm1">
              <el-switch
                v-model="msgFormDialog.alarm1"
                active-color="#13ce66"
                inactive-color="#ccc"
                @change="switchChange1"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间" prop="timeout1">
              <el-input
                size="small"
                v-model="msgFormDialog.timeout1"
                :disabled="isDisabled"
                placeholder="填写数字单位为分钟"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>二级NAS迁移</span>
        </div>
        <el-row>
          <el-col :span="24">
            <el-row>
              <el-col :span="8">
                <el-form-item label="过期时间" prop="data_overduetime2">
                  <el-input
                    size="small"
                    v-model="msgFormDialog.data_overduetime2"
                    :disabled="isDisabled"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" class="brotherItem">
                <el-form-item label prop="time_unit2">
                  <el-select size="small" v-model="msgFormDialog.time_unit2" :disabled="isDisabled">
                    <el-option label="日" value="日"></el-option>
                    <el-option label="月" value="月"></el-option>
                    <el-option label="年" value="年"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="归档过期时间" prop="file_storagedir2">
                  <el-input
                    size="small"
                    v-model="msgFormDialog.file_storagedir2"
                    :disabled="isDisabled"
                    placeholder="填写数字单位与过期时间相同"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="执行IP和端口" prop="exec_ipAndPort2">
              <el-select
                size="small"
                v-model="msgFormDialog.exec_ipAndPort2"
                :disabled="isDisabled"
              >
                <el-option
                  v-for="(item,index) in ipAndPort_list"
                  :key="index"
                  :label="item.VALUE"
                  :value="item.VALUE"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-row>
              <el-col :span="20">
                <el-form-item label="执行策略" prop="cron2">
                  <el-input size="small" v-model="msgFormDialog.cron2" :disabled="isDisabled"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" class="brotherItem">
                <el-button
                  size="small"
                  type="primary"
                  @click="cronClick2"
                  :disabled="isDisabled"
                >配置策略</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12" class="switchBox">
            <el-form-item label="是否告警" prop="alarm2">
              <el-switch
                v-model="msgFormDialog.alarm2"
                active-color="#13ce66"
                inactive-color="#ccc"
                @change="switchChange2"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间" prop="timeout2">
              <el-input
                size="small"
                v-model="msgFormDialog.timeout2"
                :disabled="isDisabled"
                placeholder="填写数字单位为分钟"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')" v-if="trueShow">确 定</el-button>
      <!-- <el-button @click="cancelDialog('ruleForm')">取 消</el-button> -->
    </div>
    <!-- cron表达式 -->
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
// import { englishAndNumValidation } from "@/components/common.js";
// // cron表达式
// import Cron from "@/components/cron/Cron";
export default {
  name: "handleMsgDialog",
  components: {
    Cron
  },
  props: {
    msgFormDialogs: {
      type: Object
    }
  },
  data() {
    var passwordValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入数据库密码"));
      } else if (!englishAndNumValidation(value)) {
        callback(new Error("密码必须由英文字母和数字组成"));
      } else {
        callback();
      }
    };
    return {
      trueShow: true,
      isDisabled: false,
      dataIsDisabled: false,
      cronDialogVisible: false,
      isaddorupdate: false,
      searchObj: {},
      msgFormDialog: {
        cornflage: "",
        database_type: "",

        physics_database: "",
        databasename: "",
        tablename: "",
        data_class_id: "",

        task_id1: "", //新增时任务ID为空
        data_overduetime1: "",
        time_unit1: "",
        source_directory1: "",
        file_storagedir1: "",
        exec_ipAndPort1: "",
        cron1: "",
        timeout1: "",
        alarm1: false,

        task_id2: "", //新增时任务ID为空
        data_overduetime2: "",
        time_unit2: "",
        file_storagedir2: "",
        exec_ipAndPort2: "",
        cron2: "",
        timeout2: "",
        alarm2: false
      },
      physics_database_list: [],
      source_directory_list: [],
      file_storagedir_list: [],
      ipAndPort_list: [],
      databasename: [],
      databasename_list: [],
      rules: {
        physics_database: [
          { required: true, message: "请选择数据库", trigger: "change" }
        ],
        databasename: [
          { required: true, message: "资料名称", trigger: "change" }
        ],
        tablename: [
          { required: true, message: "请输入表名称", trigger: "blur" }
        ],
        data_class_id: [
          { required: true, message: "请选择四级编码", trigger: "blur" }
        ],
        data_overduetime: [
          { required: true, message: "请选择过期时间", trigger: "blur" }
        ],
        time_unit1: [
          { required: true, message: "请选择单位", trigger: "change" }
        ],
        source_directory1: [
          { required: true, message: "请选择迁移源目录", trigger: "change" }
        ],
        file_storagedir1: [
          { required: true, message: "请选择迁移目标目录", trigger: "change" }
        ],
        exec_ipAndPort1: [
          { required: true, message: "请选择执行IP和端口", trigger: "change" }
        ],
        cron1: [{ required: true, message: "请配置执行策略", trigger: "blur" }],
        timeout1: [
          { required: true, message: "请输入超时时间", trigger: "blur" }
        ],

        data_overduetime2: [
          { required: true, message: "请选择过期时间", trigger: "blur" }
        ],
        time_unit2: [
          { required: true, message: "请选择单位", trigger: "change" }
        ],

        file_storagedir2: [
          { required: true, message: "请输入归档过期时间", trigger: "change" }
        ],
        exec_ipAndPort2: [
          { required: true, message: "请选择执行IP和端口", trigger: "change" }
        ],
        cron2: [{ required: true, message: "请配置执行策略", trigger: "blur" }],
        timeout2: [
          { required: true, message: "请输入超时时间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // debugger;
    // this.setBasicData(this.msgFormDialogs);
    // this.findDataBaseMap();
    // this.databaseChange(this.msgFormDialog.physics_database);
    // this.findTargetDictionary();
    // this.findSourceDictionary();
    // this.getIpList();
    // debugger;
    // if (this.msgFormDialogs.initServerDetail != 1) {
    //   this.initServerDetail();
    // }
  },
  methods: {
    switchChange1(val) {
      this.msgFormDialog.alarm1 = val;
      console.log(this.msgFormDialog.alarm1);
    },
    switchChange2(val) {
      this.msgFormDialog.alarm2 = val;
      console.log(this.msgFormDialog.alarm1);
    },
    // 获取详情
    initServerDetail() {
      let paramsObj = {};
      paramsObj.dataClassId = this.searchObj.data_class_id;
      paramsObj.databaseId = this.searchObj.physics_database;
      this.axios
        .post(interfaceObj.databaseTransfer_findbyDataClassId, paramsObj)
        .then(res => {
          debugger;
          let resdata = res.data.data;
          this.msgFormDialog.task_id1 = resdata.transfer.task_id;
          if (
            resdata.transfer.task_id != null &&
            resdata.transfer.task_id != null
          ) {
            this.isaddorupdate = true;
          }
          if (this.isaddorupdate) {
            this.msgFormDialogs.physics_database =
              resdata.transfer.physics_database;
            this.msgFormDialog.databasename =
              resdata.transfer.data_service_name;
            this.msgFormDialog.tablename = resdata.transfer.tablename;
            this.msgFormDialog.data_class_id = resdata.transfer.data_class_id;
            // 获取资料列表
            this.databaseChange(resdata.transfer.physics_database);
            // 获取表名
            this.databasenameChange(resdata.transfer.data_class_id);
            this.msgFormDialog.data_overduetime1 =
              resdata.transfer.data_overduetime;
            this.msgFormDialog.time_unit1 = resdata.transfer.time_unit;
            this.msgFormDialog.source_directory1 =
              resdata.transfer.source_directory;
            this.msgFormDialog.file_storagedir1 =
              resdata.transfer.file_storagedir;
            this.msgFormDialog.exec_ipAndPort1 =
              resdata.transfer.exec_ip + ":" + resdata.transfer.exec_port;
            this.msgFormDialog.cron1 = resdata.transfer.cron;
            this.msgFormDialog.timeout1 = resdata.transfer.timeout;
            if (resdata.transfer.alarm == "false") {
              resdata.transfer.alarm = false;
            } else if (resdata.transfer.alarm == "true") {
              resdata.transfer.alarm = true;
            }
            this.msgFormDialog.alarm1 = resdata.transfer.alarm;
            this.msgFormDialog.task_id2 = resdata.clear.task_id;
            this.msgFormDialog.data_overduetime2 =
              resdata.clear.data_overduetime;
            this.msgFormDialog.time_unit2 = resdata.clear.time_unit;
            this.msgFormDialog.file_storagedir2 = resdata.clear.file_storagedir;
            this.msgFormDialog.exec_ipAndPort2 =
              resdata.clear.exec_ip + ":" + resdata.clear.exec_port;
            this.msgFormDialog.cron2 = resdata.clear.cron;
            this.msgFormDialog.timeout2 = resdata.clear.timeout;
            if (resdata.clear.alarm == "false") {
              resdata.clear.alarm = false;
            } else if (resdata.clear.alarm == "true") {
              resdata.clear.alarm = true;
            }
            this.msgFormDialog.alarm2 = resdata.clear.alarm;
          }
        });
    },
    // 获取数据库
    findDataBaseMap() {
      this.axios
        .get(interfaceObj.findDataBaseMap + "?proName=backup")
        .then(res => {
          this.physics_database_list = res.data.data;
        });
    },
    // 获取资料名称
    databaseChange(selectId) {
      let operation;
      let value = "";
      let text = "";
      if (this.searchObj.task_id == undefined) {
        operation = 1;
      } else {
        operation = 2;
        value = this.msgFormDialog.data_class_id;
        text = this.msgFormDialog.databasename;
      }
      this.axios
        .get(interfaceObj.findTableMap, {
          params: {
            databaseId: selectId.split("#")[0],
            databaseType: 1,
            operation: operation,
            value: value,
            text: text
          }
        })
        .then(res => {
          this.databasename_list = res.data.data;
        });
    },
    // 获取表名、四级编码
    databasenameChange(selectId) {
      let params = {
        data_class_id: selectId
      };
      this.axios.post(interfaceObj.tableClearName, params).then(res => {
        this.msgFormDialog.tablename = res.data.data;
      });
      console.log(this.databasename_list);
      if (this.databasename_list.length > 0) {
        let obj = {};
        obj = this.databasename_list.find(item => {
          return item.value === selectId; //筛选出匹配数据
        });
        if (obj != undefined) {
          this.msgFormDialog.data_class_id = obj.value;
        }
      }
    },
    // 获取源目录和目标目录
    findSourceDictionary() {
      this.axios
        .get(interfaceObj.findDBFromDictionary + "?type=39")
        .then(res => {
          this.source_directory_list = res.data.data;
        });
    },
    findTargetDictionary() {
      this.axios
        .get(interfaceObj.findDBFromDictionary + "?type=40")
        .then(res => {
          this.file_storagedir_list = res.data.data;
        });
    },
    // 获取所有可用IP
    getIpList() {
      this.axios.get(interfaceObj.getIpList).then(res => {
        this.ipAndPort_list = res.data.data;
      });
    },
    // 执行策略
    cronClick1() {
      this.msgFormDialog.cornflage = 1;
      this.cronDialogVisible = true;
    },
    cronClick2() {
      this.msgFormDialog.cornflage = 2;
      this.cronDialogVisible = true;
    },
    //设置cron表达式
    setCron(cronExpression) {
      if (this.msgFormDialog.cornflage == 1) {
        this.msgFormDialog.cron1 = cronExpression;
      } else if (this.msgFormDialog.cornflage == 2) {
        this.msgFormDialog.cron2 = cronExpression;
      }
      this.cronDialogVisible = false;
    },
    // 关闭cron表达式
    closeCron() {
      this.cronDialogVisible = false;
    },
    setBasicData(obj) {
      this.msgFormDialog.physics_database = obj.database_id;
      this.msgFormDialog.databasename = obj.data_class_id;
      this.msgFormDialog.tablename = obj.table_name;
      this.msgFormDialog.data_class_id = obj.data_class_id;
      this.searchObj.physics_database = obj.database_id;
      this.searchObj.data_class_id = obj.d_data_id;
      this.searchObj.database_type = obj.database_type;
    },
    trueDialog(formName) {
      debugger;

      let dialogObj = {};
      dialogObj.dataBaseTransferClear = [];
      dialogObj.dataBaseTransferClear.push(
        {
          task_id: this.msgFormDialog.task_id1,
          database_type: 1,
          physics_database: this.msgFormDialog.physics_database.split("#")[0],
          data_class_id: this.msgFormDialog.data_class_id,
          alarm: this.msgFormDialog.alarm1,
          data_overduetime: this.msgFormDialog.data_overduetime1,
          time_unit: this.msgFormDialog.time_unit1,
          source_directory: this.msgFormDialog.source_directory1,
          file_storagedir: this.msgFormDialog.file_storagedir1,
          exec_ip: this.msgFormDialog.exec_ipAndPort1.split(":")[0],
          exec_port: this.msgFormDialog.exec_ipAndPort1.split(":")[1],
          cron: this.msgFormDialog.cron1,
          timeout: this.msgFormDialog.timeout1
        },
        {
          task_id: this.msgFormDialog.task_id2,
          database_type: 3,
          physics_database: this.msgFormDialog.physics_database.split("#")[0],
          data_class_id: this.msgFormDialog.data_class_id,
          alarm: this.msgFormDialog.alarm2,
          data_overduetime: this.msgFormDialog.data_overduetime2,
          time_unit: this.msgFormDialog.time_unit2,
          file_storagedir: this.msgFormDialog.file_storagedir2,
          exec_ip: this.msgFormDialog.exec_ipAndPort2.split(":")[0],
          exec_port: this.msgFormDialog.exec_ipAndPort2.split(":")[1],
          cron: this.msgFormDialog.cron2,
          timeout: this.msgFormDialog.timeout2
        }
      );
      if (!this.isaddorupdate) {
        this.axios
          .post(interfaceObj.databaseTransfer_add, dialogObj)
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                type: "success",
                message: "添加成功"
              });
              //debugger;
              this.$emit("handleClose");
            } else {
              this.$message({
                type: "error",
                message: res.data.returnMessage
              });
            }
          });
      } else {
        this.axios
          .post(interfaceObj.databaseTransfer_update, dialogObj)
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                type: "success",
                message: "编辑成功"
              });
              this.$emit("handleClose");
            } else {
              this.$message({
                type: "error",
                message: res.data.returnMessage
              });
            }
          });
      }
    },
    cancelDialog(formName) {
      this.$emit("handleClose");
    }
  }
};
</script>

<style lang="scss">
.handleMsgDialog {
  .box-card {
    margin-bottom: 20px;
  }
  .el-card__header {
    position: relative;
    padding-left: 40px;
  }
  .el-card__header:before {
    content: "";
    position: absolute;
    width: 4px;
    left: 20px;
    top: 12px;
    height: 55%;
    background-color: #409eff;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
  }
  .brotherItem {
    .el-form-item__content {
      margin-left: 2px !important;
    }
    button {
      margin-left: 4px;
      margin-top: 4px;
    }
  }
}
</style>
