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
          <el-form-item label="清除条件" prop="conditions">
            <el-select
              v-model.trim="msgFormDialog.conditions"
              placeholder="请选择清除条件"
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
        <el-col :span="12">
          <el-form-item label="清除限制频率" prop="clearLimit">
            <el-input v-model.trim="msgFormDialog.clearLimit" placeholder="请输入清除限制频率单位为秒" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
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
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model.trim="msgFormDialog.jobDesc" type="textarea" placeholder="请输入内容"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getByDatabaseId,
  getByDatabaseIdAndClassId,
  updateClear,
  addClear,
  findAllDataBase,
  getClear
} from "@/api/schedule/clear/clear";
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
      conditionsOptions: [],
      msgFormDialog: {
        databaseId: "",
        dataClassId: "",
        conditions: "",
        tableName: "",
        ddataId: "",
        clearLimit: "",
        jobCron: "0 0 2 * * ?",
        isAlarm: "",
        executorTimeout: "",
        jobDesc: ""
      },
      rules: {
        databaseId: [
          { required: true, message: "物理库不能为空", trigger: "blur" }
        ],
        dataClassId: [
          { required: true, message: "资料名称不能为空", trigger: "blur" }
        ],
        jobCron: [
          // { required: true, validator: handleCronValidate, trigger: "blur" }
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ],
        executorTimeout: [
          { required: true, message: "超时时间不能为空", trigger: "blur" }
        ],
        clearLimit: [
          { required: true, message: "清除限制频率不能为空", trigger: "blur" }
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
    await this.getDicts("database_clear_where").then(response => {
      this.conditionsOptions = response.data;
    });
    // 匹配数据库和资料名称
    if (
      this.handleObj.pageName == "资料存储策略" ||
      this.handleObj.pageName == "存储结构概览"
    ) {
      this.msgFormDialog.databaseId = this.handleObj.databaseId;
      await this.selectByDatabaseIds(
        this.handleObj.databaseId,
        this.handleObj.dataClassId
      );
      await this.selectTable(this.handleObj.dataClassId);
    }
    // 查看详情
    if (this.handleObj.id) {
      await getClear(this.handleObj.id).then(response => {
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
        this.msgFormDialog.vTableName = response.data.vTableName;
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
            updateClear(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.$emit("cancelHandle");
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addClear(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.$emit("cancelHandle");
              } else {
                this.msgError(response.msg);
              }
            });
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
