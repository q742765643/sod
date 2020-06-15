<template>
  <section class="handleSyncDialog">
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="150px"
      label-position="right"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>任务名称</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item label="任务名称" prop="taskName">
              <el-input size="medium" v-model.trim="msgFormDialog.taskName" placeholder="任务名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>源库表信息</span>
        </div>

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="数据来源" prop="dataSourceId">
              <el-select size="medium" filterable v-model.trim="msgFormDialog.dataSourceId">
                <el-option
                  v-for="(item,index) in dataSourceIdArray"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源库" prop="sourceDatabaseId">
              <el-select
                size="medium"
                filterable
                v-model.trim="msgFormDialog.sourceDatabaseId"
                @change="sourceDBChange"
              >
                <el-option
                  v-for="(item,index) in databaseDetailArray"
                  :key="index"
                  :label="item.DATABASE_NAME"
                  :value="item.ID"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="源表" prop="sourceTableId">
              <el-select
                size="medium"
                filterable
                v-model.trim="msgFormDialog.sourceTableId"
                @change="sourceTableChange"
              >
                <el-option
                  v-for="(item,index) in sourceTableArray"
                  :key="index"
                  :label="item.name_cn"
                  :value="item.id"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源表名" prop="sourceTableEnName">
              <el-input
                :disabled="true"
                size="medium"
                v-model.trim="msgFormDialog.sourceTableEnName"
                placeholder="源表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row class="row-bg">
          <el-form :model="stableFilterForm" label-width="150px" class="spacliClass">
            <el-form-item
              v-for="domain in stableFilterForm.domains"
              :key="domain.key"
              label="源表过滤字段"
            >
              <i class="el-icon-plus" @click="addStableFilter"></i>
              <i class="el-icon-minus" @click="deleteStableFilter(domain)"></i>
              <el-select
                style="width:35%; margin-left:5px;"
                size="medium"
                v-model.trim="domain.sourceTableFilter"
              >
                <el-option
                  :key="index"
                  v-for="(item,index) in columnArray"
                  :label="item.celementCode+ '[' + item.eleName + ']'"
                  :value="item.celementCode"
                ></el-option>
              </el-select>
              <el-select
                style="width:22%; margin:0px 5px;"
                size="medium"
                filterable
                v-model.trim="domain.columnOper"
              >
                <el-option
                  v-for="(itemo,indexo) in columnOperArray"
                  :key="indexo"
                  :label="itemo.lable"
                  :value="itemo.value"
                ></el-option>
              </el-select>
              <el-input
                size="medium"
                style="width:35%;float:right;"
                v-model.trim="domain.sourceTableFilterText"
                type="text"
              ></el-input>
            </el-form-item>
          </el-form>
        </el-row>
      </el-card>
      <el-card class="box-card" v-if="true">
        <div slot="header" class="clearfix">
          <span>同步任务参数</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item label="同步类型" prop="syncType">
              <el-select size="medium" v-model.trim="msgFormDialog.syncType">
                <el-option label="定时任务同步" :value="1"></el-option>
                <el-option label="数据触发同步" :value="2"></el-option>
                <el-option label="数据库日志同步" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item
              label="源表查询字段"
              prop="sourceTableDatecolumn"
              v-if="this.msgFormDialog.syncType == 1"
            >
              <el-select
                size="medium"
                filterable
                v-model.trim="msgFormDialog.sourceTableDatecolumn"
              >
                <el-option
                  v-for="(item,index) in columnArray"
                  :key="index"
                  :label="item.celementCode+ '[' + item.eleName + ']'"
                  :value="item.celementCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="同步开始时间" prop="beginTime" v-if="this.msgFormDialog.syncType == 1">
              <el-date-picker
                type="datetime"
                placeholder="选择同步开始时间"
                v-model.trim="msgFormDialog.beginTime"
                size="small"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="同步频率(毫秒)"
              prop="syncPeriod"
              v-if="this.msgFormDialog.syncType == 1"
            >
              <el-input-number size="medium" v-model.trim="msgFormDialog.syncPeriod" :min="0"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="DI过滤规则" prop="queueName" v-if="this.msgFormDialog.syncType == 2">
              <el-input
                size="medium"
                v-model.trim="msgFormDialog.queueName"
                placeholder="STATION_LEVEL=11,12,13;DATA_TYPE=A.0010.0001.S001"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主键拼接规则" prop="primaryCom" v-if="this.msgFormDialog.syncType == 2">
              <el-input
                size="medium"
                v-model.trim="msgFormDialog.primaryCom"
                placeholder="prompt:'DATA_TIME;IIiii',multiline:true"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="同步任务执行节点" prop="ipAndPort">
              <el-select size="medium" filterable v-model.trim="msgFormDialog.ipAndPort">
                <el-option
                  v-for="(item,index) in ipAndPortArray"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批处理条数(条)" prop="batchAmount">
              <el-input-number size="medium" v-model.trim="msgFormDialog.batchAmount" :min="0"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="DI/EI发送" prop="diOff">
              <el-switch
                v-model.trim="msgFormDialog.diOff"
                active-value="1"
                inactive-value="0"
                active-color="#13ce66"
                inactive-color="#ccc"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重复数据丢弃" prop="discardOnDuplicate">
              <el-switch
                v-model.trim="msgFormDialog.discardOnDuplicate"
                active-value="1"
                inactive-value="0"
                active-color="#13ce66"
                inactive-color="#ccc"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>目标信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="数据流向" prop="dataFlowDirectionId">
              <el-select size="medium" filterable v-model.trim="msgFormDialog.dataFlowDirectionId">
                <el-option
                  v-for="(item,index) in flowDirArray"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标存储类型" prop="targetType">
              <el-select size="medium" v-model.trim="msgFormDialog.targetType">
                <el-option label="数据库存储" :value="0"></el-option>
                <el-option label="文件存储" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item
              label="目标库"
              prop="targetDatabaseId"
              v-if="this.msgFormDialog.targetType == 0"
            >
              <el-select
                size="medium"
                filterable
                v-model.trim="msgFormDialog.targetDatabaseId"
                @change="targetDBChange"
              >
                <el-option
                  v-for="(item,index) in databaseDetailArray"
                  :key="index"
                  :label="item.DATABASE_NAME"
                  :value="item.ID"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row
          type="flex"
          class="spacliClass"
          justify="center"
          v-if="this.msgFormDialog.targetType == 0"
        >
          <el-col :span="12">
            <el-form-item label="目标表" prop="targetTable">
              <i class="el-icon-plus" @click="addnewtargetMehods"></i>
              <i class="el-icon-minus"></i>
              <el-select
                style="width:85%;float:right;"
                size="medium"
                filterable
                v-model.trim="msgFormDialog.targetTable"
                @change="targetTableChange"
              >
                <el-option
                  v-for="(item,index) in targetTableArray"
                  :key="index"
                  :label="item.name_cn"
                  :value="item.id"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标表名" prop="target_table_name">
              <el-input
                :disabled="true"
                size="medium"
                v-model.trim="msgFormDialog.target_table_name"
                placeholder="目标表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="spacliClass" justify="center" v-if="this.addnewtargettable == 1">
          <el-col :span="12">
            <el-form-item label="目标表2" prop="targetTable">
              <i class="el-icon-plus"></i>
              <i class="el-icon-minus" @click="addnewtargettable = 0"></i>
              <el-select
                style="width:85%;float:right;"
                size="medium"
                filterable
                v-model.trim="msgFormDialog.targetTable2"
                @change="targetTableChange2"
              >
                <el-option
                  v-for="(item,index) in targetTableArray"
                  :key="index"
                  :label="item.name_cn"
                  :value="item.id"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标表名" prop="target_table_name">
              <el-input
                :disabled="true"
                size="medium"
                v-model.trim="msgFormDialog.target_table_name2"
                placeholder="目标表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card" v-if="doubleTableFlag">
        <div slot="header" class="clearfix">
          <span>要素告警</span>
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检查间隔（小时）" prop="checkInterval">
              <el-input
                size="medium"
                v-model.trim="msgFormDialog.checkInterval"
                placeholder="请输入检查间隔"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="时间范围（小时）" prop="timeLimit">
              <el-input size="medium" v-model.trim="msgFormDialog.timeLimit" placeholder="请输入时间范围"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大差异条数" prop="biggestDifference">
              <el-input
                size="medium"
                v-model.trim="msgFormDialog.biggestDifference"
                placeholder="请输入最大差异条数"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-button
        type="primary"
        :icon="conIcon"
        @click="handHideOr"
        size="small"
        style="position: absolute;right: 16px;z-index:9999"
      ></el-button>
      <el-tabs v-model.trim="editableTabsValue" closable type="card" @tab-remove="removeTab">
        <el-tab-pane
          :key="index"
          v-for="(item,index) in editableTabs"
          :label="item.title"
          :name="item.name"
        >
          <el-table border :data="item.list" style="width: 100%">
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="targetColumn_" label="目标表字段" min-width="100"></el-table-column>
            <el-table-column prop="sourceColumn_" label="源表字段" min-width="100">
              <template slot-scope="scope1">
                <el-input v-model.trim="scope1.row.sourceColumn_"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="tabColStatus_" label="状态" min-width="100">
              <template slot-scope="scope">
                <el-button plain type="success" v-if="scope.row.tabColStatus == '通过'">通过</el-button>
                <el-button
                  type="danger"
                  plain
                  v-else-if="scope.row.tabColStatus == '不通过'"
                  @click="showStatusMsg(scope.row.msg)"
                >不通过</el-button>
                <el-button plain type="warning" v-else @click="showStatusMsg(scope.row.msg)">警告</el-button>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100">
              <template slot-scope="scope">
                <el-checkbox v-model.trim="scope.row.isdelete">删除</el-checkbox>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer" v-if="handleObj.pageName != '数据注册审核'">
      <el-button
        type="primary"
        @click="trueDialog('ruleForm')"
        v-if="this.handleObj.handleType!='detail'"
      >确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>
<script>
import {
  getDictDataByType,
  syncDatabaseDetail,
  syncTableByDatabaseId,
  syncColumnByTableId,
  syncSaveUpdate
} from "@/api/schedule/dataSync";
export default {
  name: "handleSyncDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    // 格式校验
    var nameValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入申请单位"));
      } else if (!chineseLengthTenValidation(value)) {
        callback(new Error("申请单位格式不正确"));
      } else {
        callback();
      }
    };
    var phoneValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入联系电话"));
      } else if (!telphoneNumValidation(value)) {
        callback(new Error("电话号码格式不正确"));
      } else {
        callback();
      }
    };
    return {
      conIcon: "el-icon-arrow-up",
      targetChangeFlag: 0,
      stableFilterForm: {
        domains: [
          {
            sourceTableFilter: "",
            columnOper: ""
          }
        ]
      },
      isDetailStatus: false, //新增是false，查看是true
      dataSourceIdArray: [], //数据来源
      databaseDetailArray: [], //数据库
      sourceTableArray: [], //源表
      targetTableArray: [], //目标表
      sourceVColumnDetail: [], //源表为键值表时，值表字段信息
      sourceColumnDetail: [], //源表字段信息
      columnArray: [], //源表查询字段，源表过滤字段
      columnOperArray: [
        { value: "EQ", lable: "=" },
        { value: "NE", lable: "!=" },
        { value: "GT", lable: ">" },
        { value: "LT", lable: "<" },
        { value: "GE", lable: ">=" },
        { value: "LE", lable: "<=" },
        { value: "IN", lable: "in" },
        { value: "NOT in", lable: "not in" }
      ], //源表过滤字段操作符
      ipAndPortArray: [], //同步任务执行节点
      flowDirArray: [], //数据流向

      editableTabs: [], //源表和目标表的映射tab
      editableTabsValue: "", //当前tab名称

      contantTaskChose: [],
      addnewtargettable: 0,
      doubleTableFlag: false, //是否是建表要素表
      msgFormDialog: {
        taskName: "", //任务名
        dataSourceId: "", //数据来源
        sourceDatabaseId: "", //源库
        sourceTableEnName: "", //源表名
        KandE: "E",
        sourceTableFilter: "", //源表过滤字段
        columnOper: "", //源表过滤字段操作符
        sourceTableFilterText: "", //源表过滤字段数值
        syncType: "", //同步类型
        sourceTableDatecolumn: "", //源表查询字段
        beginTime: "", //同步开始时间
        syncPeriod: "", //同步频率
        queueName: "", //DI过滤规则
        primaryCom: "", //主键拼接规则
        ipAndPort: "", //同步任务执行节点
        batchAmount: "", //批处理条数
        diOff: "0", //DI/EI发送
        discardOnDuplicate: "0", //重复数据丢弃
        targetDatabaseId: "", //目标库
        targetTable: "", //目标表
        target_table_name: "", //目标表名
        targetTableId: "", //目标表的值表的表ID

        targetTable2: "", //目标表2
        target_table_name2: "", //目标表2名
        targetTableId2: "", //目标表的值表的表2ID

        sourceTableId: "", //源表的值表的表ID
        targetTableName_K: "", //目标表的值表的表名
        targetTableIds: "", //目标表是键值表时为键表ID，普通要素表时可以有2个
        tableNames: "", //目标表是键值表时为键表表名，普通要素表时可以有2个
        data_class_ids: "", //目标资料存储编码
        tableNameCNs: "", //目标资料名称
        targetColumnDetail: [], //目标表字段
        targetRelation: [], //源表和目标表的映射关系
        slaveRelation: {}, //源表值表和目标表值表的映射关系
        sourceVTableId: "", //源表值表id
        targetVTableId: "", //目标表值表id
        targetType: 0 //目标存储类型
      },
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" }
        ],
        dataSourceId: [
          { required: true, message: "请选择数据来源", trigger: "change" }
        ],
        sourceDatabaseId: [
          { required: true, message: "请选择源库", trigger: "change" }
        ],
        sourceTableId: [
          { required: true, message: "请选择源表", trigger: "change" }
        ],
        sourceTableEnName: [
          { required: true, message: "源表名", trigger: "blur" }
        ],
        syncType: [{ required: true, message: "同步类型", trigger: "change" }],
        sourceTableDatecolumn: [
          { required: true, message: "源表查询字段", trigger: "change" }
        ],
        beginTime: [
          { required: true, message: "同步开始时间", trigger: "blur" }
        ],
        syncPeriod: [{ required: true, message: "同步频率", trigger: "blur" }],
        queueName: [{ required: true, message: "DI过滤规则", trigger: "blur" }],
        primaryCom: [
          { required: true, message: "主键拼接规则", trigger: "blur" }
        ],
        ipAndPort: [
          { required: true, message: "任务执行节点", trigger: "change" }
        ],
        batchAmount: [
          { required: true, message: "批处理条数", trigger: "blur" }
        ],
        diOff: [{ required: true, message: "DIEI发送", trigger: "blur" }],
        discardOnDuplicate: [
          { required: true, message: "重复数据丢弃", trigger: "blur" }
        ],
        dataFlowDirectionId: [
          { required: true, message: "数据流向", trigger: "blur" }
        ],
        targetType: [
          { required: true, message: "目标存储类型", trigger: "change" }
        ],
        targetDatabaseId: [
          { required: true, message: "目标库", trigger: "change" }
        ],
        targetTable: [{ required: true, message: "目标表", trigger: "change" }],
        checkInterval: [
          { required: true, message: "请输入检查间隔", trigger: "blur" }
        ],
        timeLimit: [
          { required: true, message: "请输入时间范围", trigger: "blur" }
        ],
        biggestDifference: [
          { required: true, message: "请输入最大差异条数", trigger: "blur" }
        ]
      }
    };
  },
  async created() {
    // 数据来源
    await getDictDataByType("data_source_identify").then(res => {
      this.dataSourceIdArray = res.data;
    });
    // 源库，目标库
    await syncDatabaseDetail().then(res => {
      this.databaseDetailArray = res.data;
    });
    // 同步任务执行节点
    await getDictDataByType("sync_run_host").then(res => {
      this.ipAndPortArray = res.data;
    });
    // 数据流向
    await getDictDataByType("data_flow_identify").then(res => {
      this.flowDirArray = res.data;
    });
    if (this.handleObj.id) {
      // 查详情
      await this.initDetail();
    }
    if (
      this.handleObj.pageName == "数据注册审核" ||
      this.handleObj.pageName == "存储结构概览" ||
      this.handleObj.pageName == "资料存储策略"
    ) {
      console.log(this.handleObj);
      this.msgFormDialog.targetDatabaseId = this.handleObj.databaseId; //目标库
      if (this.handleObj.pageName == "数据注册审核") {
        this.msgFormDialog.targetTable = this.handleObj.targetTable; //目标表
      } else {
        this.msgFormDialog.targetTable = this.handleObj.dataClassId; //目标表
      }

      // 目标表下拉框
      await this.targetDBChange(this.msgFormDialog.targetDatabaseId);
      await this.targetTableChange(this.msgFormDialog.targetTable, "");
      if (this.msgFormDialog.targetTable2) {
        this.msgFormDialog.targetTable2 = this.msgFormDialog.targetTables[1].targetTableId;
        await this.targetTableChange2(this.msgFormDialog.targetTable2, "2");
      }
    }
  },
  /* mounted() {
    return (this.targetChangeFlag = 1);
  }, */
  watch: {
    editableTabs: function() {
      this.handHideOr();
      this.targetChangeFlag = 1;
    }
  },
  methods: {
    async initDetail() {
      this.msgFormDialog = this.handleObj;

      // 源表源表名回显
      await this.sourceDBChange(this.handleObj.sourceDatabaseId);
      await this.sourceTableChange(this.handleObj.sourceTableId);
      if (this.handleObj.sourceDBId) {
        this.msgFormDialog.sourceDatabaseId = this.handleObj.sourceDBId;
      }
      // 源表过滤字段回显
      this.stableFilterForm = {
        domains: []
      };
      this.msgFormDialog.sourceTableFilter.forEach((element, index) => {
        let obj = {};
        obj.sourceTableFilter = element;
        obj.sourceTableFilterText = this.msgFormDialog.sourceTableFilterText[
          index
        ];
        obj.columnOper = this.msgFormDialog.columnOper[index];
        this.stableFilterForm.domains.push(obj);
      });
      // 目标表下拉框
      await this.targetDBChange(this.msgFormDialog.targetDatabaseId);
      await this.targetTableChange(this.msgFormDialog.targetTable, "");
      // 判断是否是2个表
      if (this.msgFormDialog.targetTable2) {
        this.addnewtargettable = 1;
        await this.targetTableChange2(this.msgFormDialog.targetTable2, "2");
      }
      // 判断是否是kv表
    },
    //源数据库事件,根据数据库ID获取源表
    async sourceDBChange(selectSourceDB) {
      await syncTableByDatabaseId({ databaseId: selectSourceDB }).then(res => {
        var resdata = res.data;
        for (var i = 0; i < resdata.length; i++) {
          if (
            resdata[i].storage_type.indexOf("K") != -1 &&
            resdata[i].db_table_type == "E"
          ) {
            resdata[i].disabled = true;
          }
        }
        this.sourceTableArray = resdata;
      });
    },
    //源表事件
    async sourceTableChange(selectSourceTableID) {
      //显示源表名
      this.findColumnByValue(selectSourceTableID);
      await this.querySourceColumn(selectSourceTableID);
      if (this.handleObj.pageName == "数据注册审核") {
        await this.targetTableChange(this.msgFormDialog.targetTable, "");
      }
    },
    //显示源表名
    findColumnByValue(selectSourceTableID) {
      let tableInfo = {};
      tableInfo = this.sourceTableArray.find(item => {
        return item.id === selectSourceTableID; //筛选出匹配数据
      });
      console.log(tableInfo);
      //显示源表名
      this.msgFormDialog.sourceTableEnName = tableInfo.table_name;
    },
    //获取源表字段信息
    async querySourceColumn(selectSourceTableID) {
      if (selectSourceTableID) {
        let tableInfo = {};
        tableInfo = this.sourceTableArray.find(item => {
          return item.id === selectSourceTableID; //筛选出匹配数据
        });
        console.log(tableInfo);
        let searchParameter = {
          tableId: selectSourceTableID
        };
        //如果选择的是键值表的键表  todo
        if (tableInfo.db_table_type == "K") {
          if (!this.handleObj.id) {
            this.$message({
              showClose: true,
              message: "您选择了键值表类型的键表，系统自动匹配值表"
            });
          }
          this.msgFormDialog.KandE = "K";
          //先查询建表对应值表的字段信息
          // 过滤出e表的tableID,查出E表的字段信息
          // data_class_id,storage_type相同的
          var ETableId = "";
          this.sourceTableArray.forEach(element => {
            if (
              element.data_class_id == tableInfo.data_class_id &&
              element.storage_type == tableInfo.storage_type &&
              element.id != tableInfo.id
            ) {
              ETableId = element.id;
              this.msgFormDialog.sourceVTableId = element.id;
            }
          });
          await syncColumnByTableId({ tableId: ETableId }).then(res => {
            //源表字段暂存，会和目标字段进行映射
            this.sourceVColumnDetail = res.data;
          });
          await syncColumnByTableId(searchParameter).then(res => {
            this.sourceColumnDetail = res.data;
            this.columnArray = res.data;
          });
        } else {
          this.msgFormDialog.KandE = "E";
          await syncColumnByTableId(searchParameter).then(res => {
            //源表字段暂存，会和目标字段进行映射
            this.sourceColumnDetail = res.data;
            //初始化源表查询字段，源表过滤字段
            this.columnArray = res.data;
          });
        }
      }
    },
    deleteStableFilter(item) {
      var index = this.stableFilterForm.domains.indexOf(item);
      if (index !== 0) {
        this.stableFilterForm.domains.splice(index, 1);
      }
    },
    addStableFilter() {
      this.stableFilterForm.domains.push({
        value: "",
        key: Date.now()
      });
    },
    // 目标表增加事件
    addnewtargetMehods() {
      // 如果源表为K表，只能选一个
      var flag = false;
      this.sourceTableArray.forEach(element => {
        if (
          this.msgFormDialog.sourceTableId == element.id &&
          element.db_table_type == "K"
        ) {
          flag = true;
        }
      });
      if (flag) {
        this.$message({
          type: "error",
          message: "您选择了键值表类型的源表，目标表只能选一个"
        });
      } else {
        this.addnewtargettable = 1;
      }
    },
    //目标数据库事件,根据数据库ID获取表
    async targetDBChange(selectTargetDB) {
      await syncTableByDatabaseId({ databaseId: selectTargetDB }).then(res => {
        var resdata = res.data;
        for (var i = 0; i < resdata.length; i++) {
          if (
            resdata[i].storage_type.indexOf("K") != -1 &&
            resdata[i].db_table_type == "E"
          ) {
            resdata[i].disabled = true;
          }
        }
        this.targetTableArray = resdata;
      });
    },
    //目标表事件
    targetTableChange(selectTargetTableID) {
      if (this.targetChangeFlag !== 0) {
        this.targetChangeFlag = this.targetChangeFlag + 1;
      }

      //查找目标表字段
      this.queryTargetColumn(selectTargetTableID, "");
    },
    targetTableChange2(selectTargetTableID) {
      if (this.targetChangeFlag !== 0) {
        this.targetChangeFlag = this.targetChangeFlag + 1;
      }
      //查找目标表字段2
      this.queryTargetColumn(selectTargetTableID, "2");
    },
    //获取目标表字段信息
    async queryTargetColumn(selectTargetTableID, tname) {
      debugger;
      if (!selectTargetTableID) {
        return;
      }
      if (
        this.sourceColumnDetail.length == 0 &&
        this.handleObj.pageName != "数据注册审核"
      ) {
        this.$message({
          showClose: true,
          message: "请先选择一个源表"
        });
        return;
      }
      let targetTableInfo = {};
      targetTableInfo = this.targetTableArray.find(item => {
        return item.id === selectTargetTableID; //筛选出目标表匹配数据
      });
      let sourceTableInfo = {};
      sourceTableInfo = this.sourceTableArray.find(item => {
        return item.id === this.msgFormDialog.sourceTableId; //筛选源表出匹配数据
      });
      if (sourceTableInfo.db_table_type != targetTableInfo.db_table_type) {
        this.$message({
          showClose: true,
          message: "源表和目标表的类型不一致"
        });
        return;
      }
      // 目标表名
      this.msgFormDialog["target_table_name" + tname] =
        targetTableInfo.table_name;
      // 目标表字段
      await syncColumnByTableId({ tableId: selectTargetTableID }).then(res => {
        this.targetColumnDetail = res.data;
      });
      if (targetTableInfo.db_table_type == "K") {
        if (!this.handleObj.id) {
          this.$message({
            showClose: true,
            message: "您选择了键值表类型的键表，系统自动匹配值表"
          });
        }
        this.doubleTableFlag = true;
        var element_obj = "";
        this.targetTableArray.forEach(element => {
          if (
            element.data_class_id == targetTableInfo.data_class_id &&
            element.storage_type == targetTableInfo.storage_type &&
            element.id != targetTableInfo.id
          ) {
            element_obj = element;
            this.msgFormDialog.targetVTableId = element.id;
          }
        });
        await syncColumnByTableId({ tableId: element_obj.id }).then(res => {
          //目标表字段暂存，会和源表字段进行映射
          this.targetVColumnDetail = res.data;
        });
        //源表值表和目标表的值表映射
        await this.ETableMapping(element_obj);
      }

      //源表键表/普通的要素表和目标表的键表/普通的要素表的映射
      this.KTableMapping(
        targetTableInfo.id,
        targetTableInfo.table_name,
        targetTableInfo.data_class_id,
        selectTargetTableID
      );
    },
    // tab
    //键表或普通的要素表映射
    KTableMapping(table_id, table_name, data_class_id, ttid) {
      console.log(this.targetChangeFlag);
      if (this.targetColumnDetail.length > 0) {
        var sourceLength = this.sourceColumnDetail.length;
        var targetLength = this.targetColumnDetail.length;
        //组织键表映射
        var dataList = [];
        for (var i = 0; i < targetLength; i++) {
          var sourceColumnName = "";
          var sourceColumnObj = "";
          var tabColStatusObj = "";
          //以目标表为基准，如果源表中有对应字段，显示字段名称；如果没有对应字段，显示空
          for (var j = 0; j < sourceLength; j++) {
            if (
              this.targetColumnDetail[i].celementCode ==
              this.sourceColumnDetail[j].celementCode
            ) {
              sourceColumnName = this.sourceColumnDetail[j].celementCode;
              sourceColumnObj = this.sourceColumnDetail[j];
              tabColStatusObj = this.columnVer(
                sourceColumnObj,
                this.targetColumnDetail[i]
              );
              break;
            }
          }

          if (
            this.handleObj.targetRelation &&
            this.handleObj.targetRelation.length > 0 &&
            this.targetChangeFlag === 0
          ) {
            this.handleObj.targetRelation.forEach((te, ti) => {
              if (table_id == te.targetTableId) {
                te.mapping.forEach((mItem, mIndex) => {
                  if (
                    this.targetColumnDetail[i].celementCode ==
                    mItem.targetColumn_
                  ) {
                    debugger;
                    var sourceColumnObj = this.findStatusKTable(
                      mItem.sourceColumn_,
                      "K",
                      "source"
                    );
                    var targetColumnObj = this.findStatusKTable(
                      mItem.targetColumn_,
                      "K",
                      "target",
                      te.targetTableId
                    );
                    tabColStatusObj = this.columnVer(
                      sourceColumnObj,
                      targetColumnObj
                    );
                    var obj = {};
                    obj.index = i;
                    obj.isdelete = false;
                    obj.targetColumn_ = mItem.targetColumn_;
                    obj.sourceColumn_ = mItem.sourceColumn_;
                    obj.tabColStatus = tabColStatusObj.tabColStatus;
                    obj.msg = tabColStatusObj.msg;
                    dataList.push(obj);
                  }
                });
              }
            });
          } else {
            var obj = {};
            obj.index = i;
            obj.isdelete = false;
            obj.targetColumn_ = this.targetColumnDetail[i].celementCode;
            obj.sourceColumn_ = sourceColumnName;
            obj.tabColStatus = tabColStatusObj.tabColStatus;
            obj.msg = tabColStatusObj.msg;
            dataList.push(obj);
          }
        }

        let seleteobj = this.targetTableArray.find(item => {
          return item.id == ttid;
        });

        this.editableTabs.push({
          title: table_name,
          name: table_name,
          list: dataList,
          //tableDataType: tableDataType,
          tableId: table_id, //组装变量名用
          KandE: "",
          content: "",
          stid: ttid,
          namecn: seleteobj.name_cn,
          data_class_ids: data_class_id
        });
        this.editableTabsValue = table_name;
        console.log("1");
      } //if end
    },

    //值表映射
    ETableMapping(element_obj) {
      //源表的值表字段信息
      var sourceVColumnList = this.sourceVColumnDetail;
      var sourceLength = sourceVColumnList.length;
      // 目标表字段信息
      var targetLength = this.targetVColumnDetail.length;
      //组织值表映射
      var dataList = [];
      for (var i = 0; i < targetLength; i++) {
        var sourceColumnName = "";
        var tabColStatusObj = "";
        var sourceColumnObj = "";
        //以目标表为基准，如果源表中有对应字段，显示字段名称；如果没有对应字段，显示空
        for (var j = 0; j < sourceLength; j++) {
          if (
            this.targetVColumnDetail[i].celementCode ==
            sourceVColumnList[j].celementCode
          ) {
            sourceColumnName = sourceVColumnList[j].celementCode;
            sourceColumnObj = sourceVColumnList[j];
            if (!this.handleObj.slaveRelation || this.targetChangeFlag !== 0) {
              tabColStatusObj = this.columnVer(
                sourceColumnObj,
                this.targetVColumnDetail[i]
              );
              var obj = {};
              obj.index = i;
              obj.isdelete = false;
              obj.targetColumn_ = this.targetVColumnDetail[i].celementCode;
              obj.sourceColumn_ = sourceColumnName;
              obj.tabColStatus = tabColStatusObj.tabColStatus;
              obj.msg = tabColStatusObj.msg;
              dataList.push(obj);
            }
            break;
          }
        }
      }
      if (this.handleObj.slaveRelation && this.targetChangeFlag === 0) {
        this.handleObj.slaveRelation.mapping.forEach((mItem, i) => {
          debugger;
          //获取源表字段的信息
          var sourceColumnObj = this.findStatusKTable(
            mItem.sourceColumn_,
            "V",
            "source"
          );
          var targetColumnObj = this.findStatusKTable(
            mItem.targetColumn_,
            "V",
            "target",
            element_obj.id
          );
          tabColStatusObj = this.columnVer(sourceColumnObj, targetColumnObj);
          var obj = {};
          obj.index = i;
          obj.isdelete = false;
          obj.targetColumn_ = mItem.targetColumn_;
          obj.sourceColumn_ = mItem.sourceColumn_;
          obj.tabColStatus = tabColStatusObj.tabColStatus;
          obj.msg = tabColStatusObj.msg;
          dataList.push(obj);
        });
      }

      this.editableTabs.push({
        title: element_obj.table_name,
        name: element_obj.table_name,
        list: dataList,
        //tableDataType: tableDataType,
        tableId: element_obj.id, //组装变量名用
        KandE: "K",
        content: "",
        stid: element_obj.id,
        namecn: element_obj.name_cn,
        targetTableName_K: element_obj.table_name,
        sourceTableId: element_obj.id,
        sourceTableName: element_obj.table_name
      });
      this.editableTabsValue = element_obj.table_name;
      console.log("2");
    },

    removeTab(targetName) {
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.editableTabsValue = activeName;
      let stid = tabs.find(tab => tab.name == targetName);
      if (stid.stid == this.msgFormDialog.targetTable2) {
        this.msgFormDialog.targetTable2 = "";
        this.msgFormDialog.target_table_name2 = "";
      } else if (stid.stid == this.msgFormDialog.targetTable) {
        this.msgFormDialog.targetTable = "";
        this.msgFormDialog.target_table_name = "";
      }
      this.editableTabs = tabs.filter(tab => tab.name !== targetName);
    },
    //确认
    trueDialog(formName) {
      if (this.isDetailStatus == true) {
        this.$refs[formName].resetFields();
        this.$emit("resetQuery");
      } else {
        //组装映射
        let targetRelation = [],
          slaveRelation = {};
        for (var i = 0; i < this.editableTabs.length; i++) {
          var obj = this.editableTabs[i];
          var list = obj.list;
          var tableId = obj.tableId;
          if (obj.KandE == "K") {
            let mapping = [];
            for (var j = 0; j < list.length; j++) {
              if (!list[j].isdelete) {
                let obj = {};
                obj.targetColumn_ = list[j]["targetColumn_"];
                obj.sourceColumn_ = list[j]["sourceColumn_"];
                mapping.push(obj);
              }
            }
            slaveRelation.mapping = mapping;
          } else {
            let mapping = [];
            for (var j = 0; j < list.length; j++) {
              if (!list[j].isdelete) {
                let obj = {};
                obj.targetColumn_ = list[j]["targetColumn_"];
                obj.sourceColumn_ = list[j]["sourceColumn_"];
                mapping.push(obj);
              }
            }
            targetRelation.push({ targetTableId: tableId, mapping: mapping });
          }
        }

        this.msgFormDialog.targetRelation = targetRelation;
        this.msgFormDialog.slaveRelation = slaveRelation;

        let ipAndPort = this.msgFormDialog.ipAndPort.split(":");
        this.msgFormDialog.execIp = ipAndPort[0];
        this.msgFormDialog.execPort = ipAndPort[1];

        let arryDomains = this.stableFilterForm.domains;
        this.msgFormDialog.sourceTableFilterText = [];
        this.msgFormDialog.columnOper = [];
        this.msgFormDialog.sourceTableFilter = [];

        arryDomains.forEach(element => {
          if (element) {
            this.msgFormDialog.sourceTableFilterText.push(
              element.sourceTableFilterText
            );
            this.msgFormDialog.columnOper.push(element.columnOper);
            this.msgFormDialog.sourceTableFilter.push(
              element.sourceTableFilter
            );
          }
        });

        this.$refs[formName].validate(valid => {
          if (valid) {
            let msg = "";
            if (this.handleObj.id) {
              msg = "编辑成功";
            } else {
              msg = "新增成功";
            }
            console.log(this.msgFormDialog);
            syncSaveUpdate(this.msgFormDialog).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: msg
                });
                if (this.handleObj.pageName == "数据注册审核") {
                  this.$emit("getStepFlag", true);
                } else {
                  this.$emit("resetQuery");
                }
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                });
              }
            });
          } else {
            if (this.handleObj.pageName == "数据注册审核") {
              this.$emit("getStepFlag", false);
            }
            console.log("error submit!!");
          }
        });
      }
    },

    //取消
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.$emit("resetQuery");
    },
    handHideOr() {
      if (this.conIcon == "el-icon-arrow-up") {
        this.conIcon = "el-icon-arrow-down";
        document.getElementsByClassName("el-tabs__content")[0].style.display =
          "none";
      } else {
        this.conIcon = "el-icon-arrow-up";
        document.getElementsByClassName("el-tabs__content")[0].style.display =
          "block";
      }
    },
    columnVer(sourceColumnObj, targetColumnObj) {
      var timeType = ["timestamp", "datetime", "DATETIME", "TIMESTAMP"];
      var accuType = [
        "numeric",
        "decimal",
        "number",
        "NUMERIC",
        "DECIMAL",
        "NUMBER"
      ];
      var nullType = ["", null];
      if (typeof sourceColumnObj == "undefined") {
        sourceColumnObj = "";
      }
      if (typeof targetColumnObj == "undefined") {
        targetColumnObj = "";
      }
      var tabColStatusObj = {};
      if (
        targetColumnObj.type != sourceColumnObj.type &&
        !(
          timeType.indexOf(targetColumnObj.type) > -1 &&
          timeType.indexOf(sourceColumnObj.type) > -1
        ) &&
        !(
          accuType.indexOf(targetColumnObj.type) > -1 &&
          accuType.indexOf(sourceColumnObj.type) > -1
        )
      ) {
        debugger;
        if (sourceColumnObj.type) {
          tabColStatusObj.msg =
            "字段类型不一致【目标表：" +
            targetColumnObj.type +
            ",源表：" +
            sourceColumnObj.type +
            "】";
        } else {
          tabColStatusObj.msg =
            "字段类型不一致【目标表：" +
            targetColumnObj.type +
            ",源表：" +
            "】";
        }

        tabColStatusObj.tabColStatus = "不通过";
      } else if (
        targetColumnObj.accuracy != sourceColumnObj.accuracy &&
        !(
          nullType.indexOf(targetColumnObj.accuracy) > -1 &&
          nullType.indexOf(sourceColumnObj.accuracy) > -1
        )
      ) {
        tabColStatusObj.msg =
          "字段精度不一致【目标表：" +
          targetColumnObj.accuracy +
          ",源表：" +
          sourceColumnObj.accuracy +
          "】";
        tabColStatusObj.tabColStatus = "警告";
      } else if (targetColumnObj.isNull != sourceColumnObj.isNull) {
        //是否可空不同
        tabColStatusObj.msg =
          "字段是否可空不一致【目标表：" +
          targetColumnObj.isNull +
          ",源表：" +
          sourceColumnObj.isNull +
          "】";

        tabColStatusObj.tabColStatus = "警告";
      } else {
        tabColStatusObj.tabColStatus = "通过";
      }
      return tabColStatusObj;
    },
    showStatusMsg(msg) {
      this.$alert(msg, "消息", {
        confirmButtonText: "确定",
        callback: action => {}
      });
    },
    findStatusKTable(column_name, kAndE, SAndT, targetTableId) {
      debugger;
      if (SAndT == "source" && kAndE == "K") {
        var columnInfo = this.sourceColumnDetail.find(function(obj) {
          return obj.dbEleCode === column_name;
        });
        return columnInfo;
      }
      if (SAndT == "source" && kAndE == "V") {
        var columnInfo = this.sourceVColumnDetail.find(function(obj) {
          return obj.dbEleCode === column_name;
        });
        return columnInfo;
      }
      if (SAndT == "target" && kAndE == "V") {
        var columnInfo = this.targetVColumnDetail.find(function(obj) {
          return obj.dbEleCode === column_name;
        });
        return columnInfo;
      }
      if (SAndT == "target" && kAndE == "K") {
        var columnInfo = this.targetColumnDetail.find(function(obj) {
          return obj.dbEleCode === column_name;
        });
        return columnInfo;
      }
    }
  } //method end
};
</script>
<style lang="scss">
.handleSyncDialog {
  .el-card {
    margin-bottom: 10px;
  }
  .el-select {
    width: 100%;
  }
  .spacliClass {
    .el-icon-plus,
    .el-icon-minus {
      width: 16px;
      height: 16px;
      line-height: 16px;
      text-align: center;
      color: #fff;
      border-radius: 4px;
      cursor: pointer;
    }
    .el-icon-plus {
      background: #409eff;
    }
    .el-icon-minus {
      background: #f56c6c;
      margin-left: 8px;
    }
  }

  .brotherItem {
    .el-form-item__content {
      margin-left: 1px !important;
    }
  }
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
    margin-top: 20px;
  }
  .el-input-number {
    width: 100%;
  }
}
</style>
