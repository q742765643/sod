<template>
  <section class="handleClearDialog">
    <el-form ref="ruleForm" :model="msgFormDialog" :rules="rules" label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="物理库" prop="databaseId">
            <el-select
              v-model.trim="msgFormDialog.databaseId"
              filterable
              @change="selectByDatabaseIds($event,'')"
              placeholder="请选择物理库"
              style="width:100%"
            >
              <el-option
                v-for="database in databaseOptions"
                :key="database.KEY"
                :label="database.VALUE"
                :value="database.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料名称" prop="dataClassId">
            <el-select
              v-model.trim="msgFormDialog.dataClassId"
              filterable
              @change="selectTable"
              placeholder="请选择资料"
              style="width:100%"
            >
              <el-option
                v-for="dataClass in dataClassIdOptions"
                :key="dataClass.KEY"
                :label="dataClass.VALUE"
                :value="dataClass.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="近时备份条件" prop="conditions">
            <el-select
              v-model.trim="msgFormDialog.conditions"
              placeholder="请选择近时备份条件"
              filterable
              style="width: 100%"
            >
              <el-option
                v-for="dict in conditionsOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="远时备份条件" prop="secondConditions">
            <el-select
              v-model.trim="msgFormDialog.secondConditions"
              placeholder="请选择远时备份条件"
              filterable
              style="width: 100%"
            >
              <el-option
                v-for="dict in secondConditionsOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="存储目录" prop="storageDirectory">
            <el-select
              v-model.trim="msgFormDialog.storageDirectory"
              placeholder="请选择"
              filterable
              style="width: 100%"
            >
              <el-option
                v-for="dict in storageDirectoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表名" prop="tableName">
            <el-input v-model.trim="msgFormDialog.tableName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="四级编码" prop="ddataId">
            <el-input v-model.trim="msgFormDialog.ddataId" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="执行策略" prop="jobCron">
            <el-popover v-model.trim="cronPopover">
              <vueCron @change="changeCron" @close="cronPopover=false" i18n="cn"></vueCron>
              <el-input
                slot="reference"
                @click="cronPopover=true"
                v-model.trim="msgFormDialog.jobCron"
                placeholder="请输入定时策略"
              ></el-input>
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否告警" prop="isAlarm">
            <el-radio-group v-model.trim="msgFormDialog.isAlarm">
              <el-radio
                v-for="dict in alarmOptions"
                :key="dict.dictValue"
                :label="dict.dictValue"
              >{{dict.dictLabel}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="超时时间" prop="executorTimeout">
            <el-input v-model.trim="msgFormDialog.executorTimeout" placeholder="请输入超时时间单位为分钟" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失败重试次数" prop="executorFailRetryCount">
            <el-input v-model.trim="msgFormDialog.executorFailRetryCount" placeholder="请输入失败重试次数" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重试间隔时间" prop="retryInterval">
            <el-input v-model.trim="msgFormDialog.retryInterval" placeholder="请输入重试间隔时间单位为分钟" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model.trim="msgFormDialog.jobDesc" type="textarea" placeholder="请输入内容"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer" v-if="handleObj.pageName != '数据注册审核'">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getBackup,
  addBackup,
  updateBackup,
  findAllDataBase,
  getByDatabaseId,
  getByDatabaseIdAndClassId
} from "@/api/schedule/backup/backup";
export default {
  name: "handleClearDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    //校验是否为cron表达式
    var handleCronValidate = (rule, value, callback) => {
      if (!!value) {
        let parser = require("cron-parser");
        try {
          let interval = parser.parseExpression(value);
          console.log("cronDate:", interval.next().toDate());
        } catch (e) {
          callback("执行策略非Cron表达式格式，请检查！");
        }
      } else {
        callback("请输入执行策略!");
      }
      callback();
    };
    return {
      cronPopover: false,
      alarmOptions: [],
      databaseOptions: [],
      dataClassIdOptions: [],
      storageDirectoryOptions: [],
      conditionsOptions: [],
      secondConditionsOptions: [],
      msgFormDialog: {
        ddataId: "",
        dataClassId: "",
        conditions: "",
        secondConditions: "",
        storageDirectory: "",
        tableName: "",
        ddataId: "",
        jobCron: "0 0 2 * * ?",
        isAlarm: "",
        executorTimeout: "",
        executorFailRetryCount: "",
        retryInterval: "",
        jobDesc: ""
      },
      // 表单校验
      rules: {
        databaseId: [
          { required: true, message: "物理库不能为空", trigger: "change" }
        ],
        dataClassId: [
          { required: true, message: "资料名称不能为空", trigger: "change" }
        ],
        storageDirectory: [
          { required: true, message: "存储目录不能为空", trigger: "change" }
        ],
        jobCron: [
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ],
        executorTimeout: [
          { required: true, message: "超时时间不能为空", trigger: "blur" }
        ],
        executorFailRetryCount: [
          { required: true, message: "重试次数不能为空", trigger: "blur" }
        ],
        retryInterval: [
          { required: true, message: "重试间隔时间不能为空", trigger: "blur" }
        ]
      }
    };
  },

  async created() {
    this.getDicts("job_is_alarm").then(response => {
      this.alarmOptions = response.data;
    });
    await findAllDataBase().then(response => {
      this.databaseOptions = response.data;
    });
    await this.getDicts("backup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
    await this.getDicts("database_backup_near_where").then(response => {
      this.conditionsOptions = response.data;
    });
    await this.getDicts("database_backup_far_where").then(response => {
      this.secondConditionsOptions = response.data;
    });
    // 匹配数据库和资料名称
    if (
      this.handleObj.pageName == "资料存储策略" ||
      this.handleObj.pageName == "存储结构概览" ||
      this.handleObj.pageName == "数据注册审核"
    ) {
      this.msgFormDialog = this.handleObj;
      await this.selectByDatabaseIds(
        this.handleObj.databaseId,
        this.handleObj.dataClassId
      );
      await this.selectTable(this.handleObj.dataClassId);
    }

    // 查看详情
    if (this.handleObj.id) {
      await getBackup(this.handleObj.id).then(response => {
        this.selectByDatabaseIds(
          response.data.databaseId,
          response.data.dataClassId
        );
        this.msgFormDialog = response.data;
      });
    }
  },
  methods: {
    changeCron(val) {
      this.msgFormDialog.jobCron = val;
    },
    selectTable(dataClassId) {
      this.msgFormDialog.tableName = "";
      this.findTable(
        this.msgFormDialog.databaseId,
        this.msgFormDialog.dataClassId
      );
    },
    findTable(databaseId, dataClassId) {
      getByDatabaseIdAndClassId(databaseId, dataClassId).then(response => {
        this.msgFormDialog.ddataId = response.data.ddataId;
        this.msgFormDialog.tableName = response.data.tableName;
      });
    },
    async selectByDatabaseIds(databaseId, dataClassId) {
      await getByDatabaseId(databaseId, dataClassId).then(response => {
        this.dataClassIdOptions = response.data;
        this.msgFormDialog.dataClassId = dataClassId;
      });
    },
    /** 提交按钮 */
    trueDialog(formName) {
      this.msgFormDialog.triggerStatus = 1;
      console.log(this.msgFormDialog);
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.msgFormDialog.id != undefined) {
            updateBackup(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                if (this.handleObj.pageName == "数据注册审核") {
                  this.$emit("getStepFlag", true);
                } else {
                  this.$emit("cancelHandle");
                }
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addBackup(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                if (this.handleObj.pageName == "数据注册审核") {
                  this.$emit("getStepFlag", true);
                } else {
                  this.$emit("cancelHandle");
                }
              } else {
                this.msgError(response.msg);
              }
            });
          }
        } else {
          if (this.handleObj.pageName == "数据注册审核") {
            this.$emit("getStepFlag", false);
          }
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
.handleClearDialog {
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
  .treeBox {
    max-height: 400px;
    overflow-y: auto;
  }
  .el-tree {
    margin-top: 12px;
  }
}
</style>