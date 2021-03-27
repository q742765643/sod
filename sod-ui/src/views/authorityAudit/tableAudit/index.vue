<template>
  <!-- 数据表审核 -->
  <div class="tableAuditTme app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      class="searchBox"
    >
      <el-form-item label="表名称:" prop="tableName">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.tableName"
          placeholder="请输入表名称"
        />
      </el-form-item>
      <el-form-item label="数据库" prop="databasePid">
        <el-select
          size="small"
          filterable
          v-model="queryParams.databasePid"
          placeholder="请选择"
          @change="handleFindSpec"
        >
          <el-option
            v-for="(citem, cindex) in DatabaseDefineList"
            :key="cindex"
            :label="citem.databaseName"
            :value="citem.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专题库" prop="databaseId">
        <el-select
          size="small"
          filterable
          v-model="queryParams.databaseId"
          placeholder="请选择"
        >
          <el-option
            v-for="(citem, cindex) in specialList"
            :key="cindex"
            :label="citem.schemaNameCn"
            :value="citem.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="资料名称">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.className"
          placeholder="资料名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model.trim="queryParams.status" size="small">
          <el-option label="待审核" :value="1"></el-option>
          <el-option label="通过" :value="2"></el-option>
          <el-option label="不通过" :value="3"></el-option>
          <el-option label="待删除" :value="4"></el-option>
          <el-option label="已失效" :value="5"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="应用">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.className"
          placeholder="资料名称"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button size="mini" type="primary" @click="handleQuery('search')"
          >查询</el-button
        >
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right"
          >重置</el-button
        >
      </el-form-item>
      <el-table
        border
        v-loading="loading"
        :data="tableData"
        ref="singleTable"
        highlight-current-row
        @current-change="handleCurrentChange"
      >
        <el-table-column
          prop="TABLE_NAME"
          label="表名称"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="DATABASE_NAME" label="数据库"></el-table-column>
        <el-table-column prop="SCHEMA_NAME_CN" label="专题中文名（英文名）">
          <template slot-scope="scope">
            <span
              >{{ scope.row.SCHEMA_NAME_CN }}({{ scope.row.SCHEMA_NAME }})</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="APP_NAME" label="应用名称">
          <template slot-scope="scope">
            <el-link
              :underline="false"
              type="primary"
              @click="showAppNameDetail(scope.row)"
              >{{ scope.row.APP_NAME }}</el-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="UPDATE_TIME" label="创建时间" width="160px">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.UPDATE_TIME) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="流程追溯">
          <template slot-scope="scope">
            <el-link
              :underline="false"
              type="primary"
              @click="showHistory(scope.row.TABLE_ID)"
              >查看</el-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="STATUS" label="审核状态">
          <template slot-scope="scope">
            <span v-if="scope.row.STATUS == 1">待审核</span>
            <span v-if="scope.row.STATUS == 2">审核通过</span>
            <span v-if="scope.row.STATUS == 3">审核不通过</span>
            <span v-if="scope.row.STATUS == 4">删除申请中</span>
            <span v-if="scope.row.STATUS == 5">已失效</span>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="操作">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              v-if="scope.row.STATUS == 1"
              @click="examineData(scope.row)"
            >
              <i class="el-icon-s-management"></i>审核
            </el-button>
            <!--  <el-button
              size="mini"
              type="text"
              v-if="scope.row.STATUS == 2 || scope.row.STATUS == 4"
              @click="deleteList(scope.row)"
            >
              <i class="el-icon-delete"></i>删除
            </el-button> -->
            <el-popover
              v-if="scope.row.STATUS == 3"
              placement="top-start"
              trigger="hover"
              :content="scope.row.REMARK"
            >
              <el-button slot="reference" size="mini" type="text">
                <i class="el-icon-tickets"></i>原因
              </el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-form>
    <!-- 应用详情 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="appNameDetailVis"
      width="500px"
      top="5vh"
      v-dialogDrag
    >
      <appNameDetail
        v-if="appNameDetailVis"
        :handleObj="handleObj"
        @handleDialogClose="closeStructureManage"
        ref="appNameDetailRef"
      />
    </el-dialog>
    <!-- 追溯 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="auditHistoryVis"
      width="500px"
      top="5vh"
      v-dialogDrag
    >
      <auditHistory
        v-if="auditHistoryVis"
        :handleObj="handleObj"
        @handleDialogClose="closeStructureManage"
        ref="auditHistoryRef"
      />
    </el-dialog>
    <!-- 表结构管理 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="closeStructureManage"
      top="0"
      class="scrollDialog tableAuditDialog"
    >
      <el-row :gutter="10">
        <el-col :span="20" class="tableAuditDialogRight">
          <div class="elementTableTitle">
            <i class="el-icon-s-home"></i>基本信息
          </div>
          <el-form :model="baseInfoForm" label-width="80px" class="baseFormBox">
            <el-row>
              <el-col :span="5">
                <el-form-item label="应用名称">
                  <el-input
                    v-model="baseInfoForm.appName"
                    :disabled="true"
                    size="small"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="责任人">
                  <el-input
                    v-model="baseInfoForm.nickName"
                    :disabled="true"
                    size="small"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="单位">
                  <el-input
                    v-model="baseInfoForm.legalUnits"
                    :disabled="true"
                    size="small"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="部门">
                  <el-input
                    v-model="baseInfoForm.deptName"
                    :disabled="true"
                    size="small"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="联系方式">
                  <el-input
                    v-model="baseInfoForm.phonenumber"
                    :disabled="true"
                    size="small"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <StructureManageTable
            ref="scrollDiv"
            v-if="structureManageVisible"
            v-bind:parentRowData="rowData"
            :tableBaseInfo="tableBaseInfo"
          />
        </el-col>
        <el-col :span="4">
          <div class="elementTableTitle">
            <i class="el-icon-s-management"></i>数据表审核
          </div>
          <el-radio-group v-model="auditDialog.status" class="statusRaioBox">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">驳回</el-radio>
          </el-radio-group>
          <el-input
            type="textarea"
            v-model="auditDialog.remark"
            placeholder="审批意见"
          ></el-input>
          <h4>审批流程</h4>
          <auditHistory
            v-if="auditHistoryVis2"
            :handleObj="handleObj"
            ref="auditHistoryRef"
          />
          <!-- 确定取消 -->
          <div class="dialog-footer">
            <el-button type="primary" @click="trueAudit">确 定</el-button>
            <el-button @click="closeStructureManage()">取 消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
} from "@/api/structureManagement/dataList/index";
import {
  tableapplyList,
  reviewlogenable,
  getBizUserByName,
  tableapplyReview,
} from "@/api/authorityAudit/tableAudit";
import appNameDetail from "@/views/authorityAudit/tableAudit/appNameDetail";
import auditHistory from "@/views/authorityAudit/tableAudit/auditHistory";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/dataList/TableManage/StructureManageTable";
export default {
  components: {
    appNameDetail,
    auditHistory,
    StructureManageTable,
  },
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: 1,
      },
      DatabaseDefineList: [],
      specialList: [],
      loading: false,
      tableData: [],
      total: 0,
      currentRow: null,

      appNameDetailVis: false,
      dialogTitle: "",
      handleObj: null,
      auditHistoryVis: false,

      structureManageTitle: "", //表结构管理弹出层title
      structureManageVisible: false, //表结构管理弹出层
      rowData: {},
      tableBaseInfo: {},

      auditDialog: {
        status: 2,
      },
      baseInfoForm: {},
      auditHistoryVis2: false,
    };
  },
  created() {
    //数据库列表
    getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
    this.handleQuery();
  },
  methods: {
    //确认审核
    trueAudit() {
      tableapplyReview(this.auditDialog).then((response) => {
        this.$message.success("审核成功");
        this.structureManageVisible = false;
        this.handleQuery();
      });
    },
    //应用名称
    showAppNameDetail(row) {
      this.handleObj = row;
      this.dialogTitle = "应用详情";
      this.appNameDetailVis = true;
    },
    //追溯
    showHistory(val) {
      reviewlogenable({ bindId: val }).then((response) => {
        this.handleObj = response.data;
        console.log(this.handleObj);
        this.dialogTitle = "申请和变更记录";
        this.auditHistoryVis = true;
      });
    },
    getList() {
      tableapplyList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 审核
    examineData(row) {
      //基本信息（应用详情）
      getBizUserByName({ userName: row.USER_ID }).then((response) => {
        this.baseInfoForm = response.data[0];
      });
      //审核流程 追溯
      reviewlogenable({ bindId: row.TABLE_ID }).then((response) => {
        this.handleObj = response.data;
        this.auditHistoryVis2 = true;
        console.log(this.handleObj);
      });
      (this.auditDialog = {
        status: 2,
        remark: "",
        id: row.TABLE_ID,
      }),
        //存储结构概览
        (this.rowData = row);
      this.rowData.MYDISABLED = true; //查看去掉按钮
      this.structureManageTitle = row.TABLE_NAME;
      this.structureManageVisible = true;
    },
    //切换数据库查询专题库
    handleFindSpec(val) {
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    closeStructureManage() {
      this.appNameDetailVis = false;
      this.auditHistoryVis = false;
      this.structureManageVisible = false;
    },
    // 重置
    resetQuery() {
      this.$refs["queryForm"].resetFields();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
  },
};
</script>

<style lang="scss">
.tableAuditTme {
  .elementTableTitle {
    box-sizing: border-box;
    background: #75bbea;
    color: #fff;
    padding: 10px;
    i {
      font-size: 16px;
      margin-right: 4px;
    }
  }
}
.tableAuditDialog {
  .tableAuditDialogRight {
    height: calc(100vh - 110px);
    position: relative;
    .scrollMain {
      top: 115px;
      bottom: 0;
    }
  }
  .statusRaioBox {
    margin: 12px 0;
  }
  .baseFormBox {
    margin-top: 20px;
  }
  .auditHistoryTem {
    height: 300px;
  }
  .dialog-footer {
    text-align: center;
    margin-top: 20px;
  }
}
</style>