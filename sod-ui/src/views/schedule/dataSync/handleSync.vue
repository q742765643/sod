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
              <el-input size="medium" v-model="msgFormDialog.taskName" placeholder="任务名称"></el-input>
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
              <el-select
                size="medium"
                filterable
                v-model="msgFormDialog.dataSourceId"
                @change="dataSourceChange"
              >
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
                v-model="msgFormDialog.sourceDatabaseId"
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
                v-model="msgFormDialog.sourceTableId"
                @change="sourceTableChange"
              >
                <el-option
                  v-for="(item,index) in sourceTableArray"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
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
                v-model="msgFormDialog.sourceTableEnName"
                placeholder="源表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row class="row-bg">
          <el-form :model="stableFilterForm" label-width="150px" class="superSearchDialog">
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
                v-model="msgFormDialog.sourceTableFilter"
              >
                <el-option
                  :key="index"
                  v-for="(item,index) in columnArray"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select
                style="width:22%; margin:0px 5px;"
                size="medium"
                filterable
                v-model="msgFormDialog.columnOper"
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
                v-model="msgFormDialog.sourceTableFilterText"
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
              <el-select size="medium" v-model="msgFormDialog.syncType">
                <el-option label="定时任务同步" value="1"></el-option>
                <el-option label="数据触发同步" value="2"></el-option>
                <el-option label="数据库日志同步" value="3"></el-option>
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
              <el-select size="medium" filterable v-model="msgFormDialog.sourceTableDatecolumn">
                <el-option
                  v-for="(item,index) in columnArray"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
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
                v-model="msgFormDialog.beginTime"
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
              <el-input size="medium" v-model="msgFormDialog.syncPeriod" placeholder="请输入数字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="DI过滤规则" prop="queueName" v-if="this.msgFormDialog.syncType == 2">
              <el-input
                size="medium"
                v-model="msgFormDialog.queueName"
                placeholder="STATION_LEVEL=11,12,13;DATA_TYPE=A.0010.0001.S001"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主键拼接规则" prop="primaryCom" v-if="this.msgFormDialog.syncType == 2">
              <el-input
                size="medium"
                v-model="msgFormDialog.primaryCom"
                placeholder="prompt:'DATA_TIME;IIiii',multiline:true"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="同步任务执行节点" prop="ipAndPort">
              <el-select size="medium" filterable v-model="msgFormDialog.ipAndPort">
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
              <el-input size="medium" v-model="msgFormDialog.batchAmount" placeholder="请输入数字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="12">
            <el-form-item label="DI/EI发送" prop="diOff">
              <el-switch v-model="msgFormDialog.diOff" active-color="#13ce66" inactive-color="#ccc"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重复数据丢弃" prop="discardOnDuplicate">
              <el-switch
                v-model="msgFormDialog.discardOnDuplicate"
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
              <el-select size="medium" filterable v-model="msgFormDialog.dataFlowDirectionId">
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
              <el-select size="medium" v-model="msgFormDialog.targetType">
                <el-option label="数据库存储" value="0"></el-option>
                <el-option label="文件存储" value="1"></el-option>
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
                v-model="msgFormDialog.targetDatabaseId"
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
          class="row-bg"
          justify="center"
          v-if="this.msgFormDialog.targetType == 0"
        >
          <el-col :span="12">
            <el-form-item label="目标表" prop="targetTable">
              <i class="el-icon-plus" @click="addnewtargettable = 1"></i>
              <i class="el-icon-minus"></i>
              <el-select
                style="width:85%;float:right;"
                size="medium"
                filterable
                v-model="msgFormDialog.targetTable"
                @change="targetTableChange"
              >
                <el-option
                  v-for="(item,index) in targetTableArray"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
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
                v-model="msgFormDialog.target_table_name"
                placeholder="目标表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center" v-if="this.addnewtargettable == 1">
          <el-col :span="12">
            <el-form-item label="目标表" prop="targetTable">
              <i class="el-icon-plus"></i>
              <i class="el-icon-minus" @click="addnewtargettable = 0"></i>
              <el-select
                style="width:85%;float:right;"
                size="medium"
                filterable
                v-model="msgFormDialog.targetTable2"
                @change="targetTableChange2"
              >
                <el-option
                  v-for="(item,index) in targetTableArray"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
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
                v-model="msgFormDialog.target_table_name2"
                placeholder="目标表名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-tabs v-model="editableTabsValue" closable type="card" @tab-remove="removeTab">
        <el-tab-pane
          :key="item.name"
          v-for="(item) in editableTabs"
          :label="item.title"
          :name="item.name"
        >
          <el-table :data="item.list" border style="width: 100%">
            <el-table-column type="index" label="序号" min-width="20"></el-table-column>
            <el-table-column prop="targetColumn_" label="目标表字段" min-width="100"></el-table-column>
            <el-table-column prop="sourceColumn_" label="源表字段" min-width="100">
              <template slot-scope="scope1">
                <el-input v-model="scope1.row.sourceColumn_"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="100">
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.isdelete">删除</el-checkbox>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
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
      stableFilterForm: {
        domains: [
          {
            value: "",
            select: ""
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

      searchObj: {},
      contantTaskChose: [],
      addnewtargettable: 0,
      msgFormDialog: {
        taskId: "", //任务ID
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
        diOff: true, //DI/EI发送
        discardOnDuplicate: "false", //重复数据丢弃
        targetDatabaseId: "", //目标库
        targetTable: "", //目标表
        target_table_name: "", //目标表名
        targetTableId: "", //目标表的值表的表ID

        targetTable2: "", //目标表2
        target_table_name2: "", //目标表2名
        targetTableId2: "", //目标表的值表的表2ID

        sourceTableId: "", //源表的值表的表ID
        sourceTableEnName: "", //源表的值表的表名
        sourceTableForeignKey: "", //源表的值表的外键
        targetTableName_K: "", //目标表的值表的表名
        tableIds: "", //目标表是键值表时为键表ID，普通要素表时可以有2个
        tableNames: "", //目标表是键值表时为键表表名，普通要素表时可以有2个
        data_class_ids: "", //目标资料存储编码
        tableNameCNs: "" //目标资料名称
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
        targetTable: [{ required: true, message: "目标表", trigger: "change" }]
      }
    };
  },
  created() {
    this.searchObj = this.handleObj;
    this.initView();
    if (this.handleObj.taskId) {
      // 查详情
      this.initDetail();
    }
  },
  methods: {
    // initView
    async initView() {
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
    },
    initDetail() {
      console.log(this.handleObj);
      console.log(this.msgFormDialog);
      for (var ii in this.msgFormDialog) {
        if (this.handleObj[ii]) {
          this.msgFormDialog[ii] = this.handleObj[ii];
        }
      }
      if (this.handleObj.sourceDBId) {
        this.msgFormDialog.sourceDatabaseId = this.handleObj.sourceDBId;
      }
    },
    //源数据库事件,根据数据库ID获取源表
    sourceDBChange(selectSourceDB) {
      syncTableByDatabaseId({ databaseId: selectSourceDB }).then(res => {
        var resdata = res.data;
        var dataList = [];
        for (var i = 0; i < resdata.length; i++) {
          var obj = {};
          obj.value = resdata[i].id;
          obj.label = resdata[i].name_cn;
          obj.sourceTableEnName = resdata[i].table_name;
          obj.selectInfo =
            resdata[i].id +
            "|" +
            resdata[i].db_table_type +
            "|" +
            resdata[i].table_name +
            "|" +
            resdata[i].data_class_id;
          if (
            resdata[i].storage_type == "K_E_table" &&
            resdata[i].db_table_type == "E"
          ) {
            obj.disabled = true;
          }
          dataList.push(obj);
        }
        this.sourceTableArray = dataList;
      });
    },
    //源表事件
    sourceTableChange(selectSourceTable) {
      //显示源表名
      this.findColumnByValue(selectSourceTable);
      this.querySourceColumn(selectSourceTable);
    },
    //显示源表名
    findColumnByValue(selectSourceTable) {
      // todo
      console.log(selectSourceTable);
      let table = {};
      table = this.sourceTableArray.find(item => {
        return item.value === selectSourceTable; //筛选出匹配数据
      });
      console.log(table);
      //显示源表名
      this.msgFormDialog.sourceTableEnName = table.sourceTableEnName;
    },
    //获取源表字段信息
    querySourceColumn(table_id) {
      // todo
      console.log(table_id);
      if (table_id) {
        var sourceTableInfo = table_id.split("|");
        let searchParameter = {
          tableId: sourceTableInfo[0]
        };
        //如果选择的是键值表的键表
        if (sourceTableInfo[1] == "K") {
          this.$message({
            showClose: true,
            message: "您选择了键值表类型的键表，系统自动匹配值表"
          });
          this.msgFormDialog.KandE = "K";
          //先查询建表对应值表的字段信息
          syncColumnByTableId(searchParameter).then(res => {
            this.sourceVColumnDetail = res.data;
          });
        } else {
          this.msgFormDialog.KandE = "E";
        }

        syncColumnByTableId(searchParameter).then(res => {
          var resdata = res.data;
          //源表字段暂存，会和目标字段进行映射
          this.sourceColumnDetail = resdata;
          //初始化源表查询字段，源表过滤字段
          var dataList = [];
          for (var i = 0; i < resdata.length; i++) {
            var obj = {};
            obj.value = resdata[i].celementCode;
            obj.label =
              resdata[i].celementCode + "[" + resdata[i].eleName + "]";
            dataList.push(obj);
          }
          this.columnArray = dataList;
        });
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

    //数据来源事件
    dataSourceChange(selectDataSourceId) {
      this.dataSourceIdArray.find(item => {
        //这里的dataSourceIdArray就是上面遍历的数据源
        return item.dictValue === selectDataSourceId; //筛选出匹配数据
      });
    },

    //目标数据库事件,根据数据库ID获取表
    targetDBChange(selectTargetDB) {
      syncTableByDatabaseId({ databaseId: selectTargetDB }).then(res => {
        var resdata = res.data;
        var dataList = [];
        for (var i = 0; i < resdata.length; i++) {
          var obj = {};
          obj.value = resdata[i].id;
          obj.label = resdata[i].name_cn;
          obj.selectInfo =
            resdata[i].id +
            "|" +
            resdata[i].db_table_type +
            "|" +
            resdata[i].table_name +
            "|" +
            resdata[i].data_class_id;
          if (
            resdata[i].storage_type == "K_E_table" &&
            resdata[i].db_table_type == "E"
          ) {
            obj.disabled = true;
          }
          dataList.push(obj);
        }
        //debugger;
        this.targetTableArray = dataList;
      });
    },
    //目标表事件
    targetTableChange(selectTargetTable) {
      let obj = {};
      obj = this.targetTableArray.find(item => {
        return item.value === selectTargetTable; //筛选出匹配数据
      });
      //查找目标表字段
      this.queryTargetColumn(obj.selectInfo, "");
    },
    targetTableChange2(selectTargetTable) {
      //查找目标表字段2
      this.queryTargetColumn(selectTargetTable, "2");
    },
    //获取目标表字段信息
    queryTargetColumn(selectTargetTable, tname) {
      debugger;
      if (selectTargetTable == "") {
        return;
      }
      if (this.sourceColumnDetail.length == 0) {
        this.$message({
          showClose: true,
          message: "请先选择一个源表"
        });
        return;
      }
      var targetTableInfo = selectTargetTable.split("|");
      let sourceTableInfo = {};
      sourceTableInfo = this.sourceTableArray.find(item => {
        return item.value === this.msgFormDialog.sourceTableId; //筛选出匹配数据
      });
      if (sourceTableInfo.selectInfo.split("|")[1] != targetTableInfo[1]) {
        this.$message({
          showClose: true,
          message: "源表和目标表的类型不一致"
        });
        return;
      }
      if (this.msgFormDialog["target_table_name" + tname]) {
        let tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        var targetName = this.msgFormDialog["target_table_name" + tname];
        if (targetName.indexOf("_K_") > -1) {
          let targetName1 = targetName.replace(/_K_/, "_");
          let stid = tabs.find(tab => tab.name == targetName1);
          if (stid.KandE == "K") {
            this.editableTabs = tabs.filter(tab => {
              if (tab.name !== targetName && tab.name !== targetName1) {
                return tab;
              }
            });
          } else {
            this.editableTabs = tabs.filter(tab => tab.name !== targetName);
          }
        } else {
          this.editableTabs = tabs.filter(tab => tab.name !== targetName);
        }
      }
      if (targetTableInfo[1] == "K") {
        this.$message({
          showClose: true,
          message: "您选择了键值表类型的键表，系统自动匹配值表"
        });
        //源表值表和目标表的值表映射
        this.ETableMapping(targetTableInfo[0], selectTargetTable);
      }
      //源表键表/普通的要素表和目标表的键表/普通的要素表的映射

      this.KTableMapping(
        targetTableInfo[0],
        targetTableInfo[2],
        targetTableInfo[3],
        selectTargetTable
      );
      //显示目标表名
      this.msgFormDialog["target_table_name" + tname] = targetTableInfo[2];
    },
    //值表映射
    ETableMapping(table_id, ttid) {
      let cellObj = {};
      cellObj.tableId = table_id;
      syncColumnByTableId(cellObj).then(res => {
        if (res.data.length > 0) {
          //debugger;
          //源表的值表字段信息
          var sourceVColumnList = this.sourceVColumnDetail.data;
          var sourceLength = sourceVColumnList.length;
          var targetLength = res.data.length;
          /* //目标表的值表的表名
            var targetTableNameCN = res.data.ETable.name_cn;

            //目标表的值表的表ID
            this.msgFormDialog.targetTableId = res.data.ETable.id;
            //源表的值表的表ID
            this.msgFormDialog.sourceTableId = this.sourceVColumnDetail.ETable.id;
            //源表的值表的表名
            this.msgFormDialog.sourceTableEnName = this.sourceVColumnDetail.ETable.table_name;
            //源表的值表的外键
            this.msgFormDialog.sourceTableForeignKey = this.sourceVColumnDetail.ETable.table_foreigh_field;
            //目标表的值表的表名
            this.msgFormDialog.targetTableName_K = res.data.ETable.table_name; */

          //组织值表映射
          var dataList = [];
          for (var i = 0; i < targetLength; i++) {
            var sourceColumnName = "";
            //以目标表为基准，如果源表中有对应字段，显示字段名称；如果没有对应字段，显示空
            for (var j = 0; j < sourceLength; j++) {
              if (
                res.data[i].celementCode == sourceVColumnList[j].celementCode
              ) {
                sourceColumnName = sourceVColumnList[j].celementCode;
                break;
              }
            }
            var obj = {};
            obj.targetColumn_ = res.data[i].celementCode;
            obj.sourceColumn_ = sourceColumnName;
            obj.index = i;
            obj.isdelete = false;
            dataList.push(obj);
          }
          //组织表头
          /* var tableDataType = [
              //{ nameProp: "index", nameLable: "序号" },
              {
                nameProp: "targetColumn_K_",
                nameLable: "目标表字段"
              },
              {
                nameProp: "sourceColumn_K_",
                nameLable: "源表字段"
              }
            ]; */

          this.editableTabs.push({
            title: res.data.ETable.table_name,
            name: res.data.ETable.table_name,
            list: dataList,
            //tableDataType: tableDataType,
            tableId: res.data.ETable.id, //组装变量名用
            KandE: "K",
            content: "",
            stid: ttid,
            namecn: res.data.ETable.name_cn,
            targetTableName_K: res.data.ETable.table_name,
            sourceTableId: this.sourceVColumnDetail.ETable.id,
            sourceTableEnName: this.sourceVColumnDetail.ETable.table_name,
            sourceTableForeignKey: this.sourceVColumnDetail.ETable
              .table_foreigh_field
          });
          this.editableTabsValue = res.data.ETable.table_name;
          console.log(this.editableTabs);
          //debugger;
        } //if end
      });
    },
    //键表或普通的要素表映射
    KTableMapping(table_id, table_name, data_class_id, ttid) {
      let cellObj = {};
      cellObj.tableId = table_id;
      syncColumnByTableId(cellObj).then(res => {
        if (res.data.length > 0) {
          //debugger;
          var sourceLength = this.sourceColumnDetail.length;
          var targetLength = res.data.length;
          /*  //获取选中的目标资料名称
            var targetTableNameCN = "";
            //目标表id
            this.msgFormDialog.tableIds.push(table_id);
            //目标表名
            this.msgFormDialog.tableNames.push(table_name);
            //目标资料存储编码
            this.msgFormDialog.data_class_ids.push(data_class_id);
            //目标资料名称
            this.msgFormDialog.tableNameCNs.push(targetTableNameCN);
 */
          //组织键表映射
          var dataList = [];
          for (var i = 0; i < targetLength; i++) {
            var sourceColumnName = "";
            //以目标表为基准，如果源表中有对应字段，显示字段名称；如果没有对应字段，显示空
            for (var j = 0; j < sourceLength; j++) {
              if (
                res.data[i].celementCode ==
                this.sourceColumnDetail[j].celementCode
              ) {
                sourceColumnName = this.sourceColumnDetail[j].celementCode;
                break;
              }
            }
            var obj = {};
            obj.targetColumn_ = res.data[i].celementCode;
            obj.sourceColumn_ = sourceColumnName;
            obj.index = i;
            obj.isdelete = false;
            dataList.push(obj);
          }
          //组织表头
          /*  var tableDataType = [
              //{ nameProp: "index", nameLable: "序号", width: "180" },
              {
                nameProp: "targetColumn_",
                nameLable: "目标表字段"
              },
              {
                nameProp: "sourceColumn_",
                nameLable: "源表字段"
              }
              //{ nameProp: "", nameLable: "操作", width: "" }
            ]; */
          //console.log(this.editableTabs);
          let seleteobj = this.targetTableArray.find(item => {
            return item.value == ttid;
          });
          //console.log(this.targetTableArray);
          //console.log(table_id);
          this.editableTabs.push({
            title: table_name,
            name: table_name,
            list: dataList,
            //tableDataType: tableDataType,
            tableId: table_id, //组装变量名用
            KandE: "",
            content: "",
            stid: ttid,
            namecn: seleteobj.label,
            data_class_ids: data_class_id
          });
          this.editableTabsValue = table_name;

          //debugger;
        } //if end
      });
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
        //debugger;
        //console.log(editableTabs);
        let tableIds = "",
          tableNames = "",
          data_class_ids = "",
          tableNameCNs = "";
        for (var i = 0; i < this.editableTabs.length; i++) {
          var obj = this.editableTabs[i];
          var list = obj.list;
          var tableId = obj.tableId;
          if (obj.KandE == "K") {
            //值表映射
            this.msgFormDialog["targetColumn_K_" + tableId] = [];
            this.msgFormDialog["sourceColumn_K_" + tableId] = [];
            for (var j = 0; j < list.length; j++) {
              if (!list.isdelete) {
                this.msgFormDialog["targetColumn_K_" + tableId].push(
                  list[j]["targetColumn_K_"]
                );
                this.msgFormDialog["sourceColumn_K_" + tableId].push(
                  list[j]["sourceColumn_K_"]
                );
              }
            }
          } else {
            tableIds = tableIds + "," + tableId;
            tableNames = tableNames + "," + obj.name;
            data_class_ids = data_class_ids + "," + obj.data_class_ids;
            tableNameCNs = tableNameCNs + "," + obj.namecn;

            let tfield = "",
              sfield = "";
            for (var j = 0; j < list.length; j++) {
              if (!list.isdelete) {
                tfield = tfield + "," + list[j]["targetColumn_"];
                sfield = sfield + "," + list[j]["sourceColumn_"];
              }
            }
            //console.log(tfield);
            debugger;
            if (tfield.length > 0) {
              this.msgFormDialog["targetColumn_" + tableId] = tfield.substr(1);
              this.msgFormDialog["sourceColumn_" + tableId] = sfield.substr(1);
            }
          }
        }
        if (tableIds.length > 0) {
          this.msgFormDialog.tableIds = tableIds.substr(1);
          this.msgFormDialog.tableNames = tableNames.substr(1);
          this.msgFormDialog.data_class_ids = data_class_ids.substr(1);
          this.msgFormDialog.tableNameCNs = tableNameCNs.substr(1);
        }

        let ipAndPort = this.msgFormDialog.ipAndPort.split(":");
        this.msgFormDialog.execIp = ipAndPort[0];
        this.msgFormDialog.execPort = ipAndPort[2];
        console.log(this.msgFormDialog);
        this.$refs[formName].validate(valid => {
          if (valid) {
            if (this.handleObj.taskId) {
            } else {
              syncSaveUpdate(this.msgFormDialog).then(res => {
                if (res.code == "200") {
                  this.$message({
                    type: "success",
                    message: "新增成功"
                  });
                  this.$emit("resetQuery");
                } else {
                  this.$message({
                    type: "error",
                    message: "新增失败"
                  });
                }
              });
            }
            //this.msgFormDialog[i_i] = v;
          } else {
            // console.log("error submit!!");
            return false;
          }
        });
      }
    },
    //取消
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.$emit("resetQuery");
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
  .el-icon-plus,
  .el-icon-minus {
    width: 16px;
    height: 16px;
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
  .brotherItem {
    .el-form-item__content {
      margin-left: 1px !important;
    }
  }
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
  .el-form-item {
    margin-bottom: 5px;
  }
}
</style>