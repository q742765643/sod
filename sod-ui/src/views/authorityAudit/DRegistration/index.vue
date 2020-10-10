<template>
  <div class="app-container">
    <!-- 数据注册审核 -->
    <el-form :model="queryParams" :inline="true" class="searchBox">
      <el-form-item label="数据分类：">
        <el-select v-model.trim="queryParams.DDataId" placeholder>
          <el-option key="index" label="全部" value></el-option>
          <el-option
            v-for="(item, index) in dbtypeselect"
            :key="index"
            :label="item.CLASS_NAME"
            :value="item.DATA_CLASS_ID"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="审核状态：">
        <el-select
          v-model.trim="queryParams.examineStatus"
          @change="handleQuery"
        >
          <!-- <el-option label="全部" value></el-option> -->
          <el-option label="待审核" :value="1"></el-option>
          <el-option label="通过" :value="2"></el-option>
          <el-option label="不通过" :value="3"></el-option>
          <el-option label="待删除" :value="4"></el-option>
          <el-option label="已失效" :value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          @click="handleQuery"
          icon="el-icon-search"
          >查询</el-button
        >
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAddSync"
          v-hasPermi="['DRegistration:role:add']"
          >增量同步元数据</el-button
        >
      </el-col>
    </el-row>
    <!-- 表格 -->
    <el-table
      border
      class="tableList"
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <af-table-column
        type="index"
        :index="table_index"
        width="45"
        label=" "
      ></af-table-column>
      <af-table-column
        prop="TYPE_PNAME"
        label="数据分类"
        width="160"
        :show-overflow-tooltip="true"
      ></af-table-column>
      <af-table-column
        prop="D_DATA_ID"
        label="四级编码"
        width="160"
      ></af-table-column>
      <af-table-column
        prop="TYPE_NAME"
        label="数据名称"
        width="240"
        :show-overflow-tooltip="true"
      ></af-table-column>
      <af-table-column
        prop="WEB_USERNAME"
        label="申请人"
        width="100"
        :show-overflow-tooltip="true"
      ></af-table-column>
      <af-table-column prop="DEPT_NAME" label="机构"></af-table-column>
      <af-table-column prop="PHONENUMBER" label="联系方式"></af-table-column>
      <af-table-column
        prop="CREATE_TIME"
        label="申请时间"
        width="160"
        :show-overflow-tooltip="true"
        sortable="custom"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.CREATE_TIME) }}</span>
        </template>
      </af-table-column>
      <af-table-column
        prop="EXAMINE_STATUS"
        label="审核状态"
        width="100"
        v-if="queryParams.examineStatus != 2"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.EXAMINE_STATUS == 1">待审核</span>
          <span v-if="scope.row.EXAMINE_STATUS == 2">审核通过</span>
          <span v-if="scope.row.EXAMINE_STATUS == 3">审核不通过</span>
          <span v-if="scope.row.EXAMINE_STATUS == 4">删除申请中</span>
          <span v-if="scope.row.EXAMINE_STATUS == 5">已失效</span>
        </template>
      </af-table-column>

      <!-- 存储结构 -->
      <af-table-column
        prop="STORAGE_DEFINE_IDENTIFIER"
        label="存储构建"
        width="140"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <el-button
            disabled
            v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 3"
            size="mini"
          >
            <i class="btnRound orangRound"></i>存储结构
          </el-button>
          <el-button v-else size="mini" @click="handledDBMethods(scope.row)">
            <i
              class="btnRound blueRound"
              v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 1"
            ></i>
            <i class="btnRound orangRound" v-else></i>存储结构
          </el-button>
        </template>
      </af-table-column>

      <!-- 数据同步 -->
      <af-table-column
        prop="SYNC_IDENTIFIER"
        label="数据同步"
        width="140"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <el-button disabled v-if="scope.row.SYNC_IDENTIFIER == 3" size="mini">
            <i class="btnRound orangRound"></i>数据同步
          </el-button>
          <el-button v-else size="mini" @click="handlSyncMethods(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.SYNC_ID"></i>
            <i class="btnRound orangRound" v-else></i>数据同步
          </el-button>
        </template>
      </af-table-column>

      <!-- 迁移 -->
      <af-table-column
        prop="MOVE_ST"
        label="迁移"
        width="120"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <!-- 迁移 -->

          <el-button
            v-if="scope.row.MOVE_ST == 1"
            size="mini"
            @click="handlMoveMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.MOVE_ID"></i>
            <i class="btnRound orangRound" v-else></i>迁移
          </el-button>
          <el-button v-else size="mini" disabled>
            <i class="btnRound orangRound"></i>迁移
          </el-button>
        </template>
      </af-table-column>

      <!-- 清除 -->
      <af-table-column
        prop="CLEAR_ST"
        label="清除"
        min-width="130"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <!-- 清除 -->
          <el-button
            v-if="scope.row.CLEAR_ST == 1"
            size="mini"
            @click="handlClearShowMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
            <i class="btnRound orangRound" v-else></i>清除
          </el-button>
          <el-button v-else size="mini" disabled>
            <i class="btnRound orangRound"></i>清除
          </el-button>
        </template>
      </af-table-column>

      <!-- 备份 -->
      <af-table-column
        prop="BACKUP_ST"
        label="备份"
        width="120"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.BACKUP_ST == 1"
            size="mini"
            @click="handleBackUpMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.BACKUP_ID"></i>
            <i class="btnRound orangRound" v-else></i>备份
          </el-button>
          <el-button v-else size="mini" disabled>
            <i class="btnRound orangRound"></i>备份
          </el-button>
        </template>
      </af-table-column>

      <!-- 恢复 -->
      <af-table-column
        prop="ARCHIVING_IDENTIFIER"
        label="恢复"
        width="120"
        v-if="queryParams.examineStatus === 2"
      >
        <template slot-scope="scope">
          <el-button
            disabled
            v-if="scope.row.ARCHIVING_IDENTIFIER == 3"
            size="mini"
            >恢复</el-button
          >
          <el-button v-else size="mini" @click="handlRecoverMethods(scope.row)"
            >恢复</el-button
          >
        </template>
      </af-table-column>

      <af-table-column
        prop="EXAMINE_STATUS"
        label="操作"
        width="80"
        v-if="queryParams.examineStatus !== 5"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            v-if="scope.row.EXAMINE_STATUS === 1"
            @click="examineData(scope.row)"
          >
            <i class="el-icon-s-management"></i>审核
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-if="
              scope.row.EXAMINE_STATUS == 2 || scope.row.EXAMINE_STATUS == 4
            "
            @click="deleteList(scope.row)"
          >
            <i class="el-icon-delete"></i>删除
          </el-button>
          <el-popover
            v-if="scope.row.EXAMINE_STATUS === 3"
            placement="top-start"
            trigger="hover"
            :content="scope.row.REMARK"
          >
            <el-button slot="reference" size="mini" type="text">
              <i class="el-icon-tickets"></i>原因
            </el-button>
          </el-popover>
        </template>
      </af-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 表结构管理 存储结构 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="handleClose"
      top="0"
    >
      <StructureManageTable
        v-if="structureManageVisible"
        v-bind:parentRowData="rowData"
      />
    </el-dialog>
    <!-- 数据同步 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="handleSyncDialog"
      width="80%"
      v-dialogDrag
    >
      <handleSync
        v-if="handleSyncDialog"
        :handleObj="handleMsgObj"
        @resetQuery="handleClose"
        ref="myHandleServer"
      />
    </el-dialog>
    <!-- 迁移清除 结构化数据清除进度 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="handleCLeadupDialog"
      width="1100px"
      v-dialogDrag
    >
      <handleClear
        v-if="handleCLeadupDialog"
        :handleObj="handleMsgObj"
        @cancelHandle="handleClose"
      />
    </el-dialog>
    <!-- 迁移配置信息 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="handleMoveDialog"
      width="1200px"
      v-dialogDrag
    >
      <handleMove
        @cancelHandle="handleClose"
        v-if="handleMoveDialog"
        :handleObj="handleMsgObj"
      ></handleMove>
    </el-dialog>
    <!-- 备份 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="handleBackupDialog"
      width="72%"
      v-dialogDrag
    >
      <handleBackUp
        v-if="handleBackupDialog"
        :handleObj="handleMsgObj"
        @cancelHandle="handleClose"
        ref="myHandleChild"
      />
    </el-dialog>

    <!-- 数据恢复 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="结构化数据恢复"
      :visible.sync="handleDataRecoveryDialog"
      width="1100px"
    >
      <DataRecovery
        v-if="handleDataRecoveryDialog"
        :handleObj="handleMsgObj"
        @handleRecover="handleClose"
      />
    </el-dialog>

    <!-- 数据注册审核-->
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="handleReDialog"
      title="存储资料审核"
      width="1100px"
      v-dialogDrag
    >
      <reviewDataRegister
        v-if="handleReDialog"
        :handleObj="handleMsgObj"
        :registerForm="registerForm"
        @cancelHandle="handleDataReg"
      />
    </el-dialog>

    <!-- 存储资料审核步骤 -->
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="reviewStep"
      fullscreen
      title="存储资料审核步骤"
    >
      <revieStepRegister
        v-if="reviewStep"
        :handleObj="handleMsgObj"
        :registerForm="registerForm"
        @closeStep="closeStep"
        :before-close="closeStep"
      />
    </el-dialog>
  </div>
</template>

<script>
// / 数据恢复
import DataRecovery from "@/views/structureManagement/overviewStorage/handleDataRecovery";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
// 迁移配置信息
import handleMove from "@/views/schedule/move/handleMove";
// 迁移清除 结构化数据清除配置
import handleClear from "@/views/schedule/clear/handleClear";
// 结构化数据备份配置弹窗
import handleBackUp from "@/views/schedule/backup/handleBackUp";
// 数据同步
import handleSync from "@/views/schedule/dataSync/handleSync";
//数据注册审核
import reviewDataRegister from "@/views/authorityAudit/DRegistration/reviewdataRegister";
//存储资料审核步骤
import revieStepRegister from "@/views/authorityAudit/DRegistration/review/index";
import {
  getDataClassify,
  getDataTable,
  databaseGet,
  deleteById,
} from "@/api/authorityAudit/DRegistration/index";
export default {
  components: {
    DataRecovery,
    StructureManageTable,
    handleMove,
    handleClear,
    handleBackUp,
    handleSync,
    reviewDataRegister,
    revieStepRegister,
  },
  data() {
    return {
      registerForm: null,
      reviewStep: false,
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        DDataId: "",
        examineStatus:
          this.$route.params.status == undefined
            ? 1
            : this.$route.params.status,
        params: {
          orderBy: {
            CREATE_TIME: "desc",
          },
        },
      }, //查询
      dbtypeselect: [], //数据分类下拉框
      tableData: [], //表格
      total: 0, //表格数
      /* 弹出层 */
      // 表结构管理
      structureManageTitle: "",
      structureManageVisible: false,
      rowData: {}, //当前行，注：一定要和表结构管理列表的当前行一样
      // 数据同步/迁移清除/配置
      dialogMsgTitle: "",
      handleMsgObj: {},

      handleMoveDialog: false, //迁移
      handleBackupDialog: false, //备份
      handleDataRecoveryDialog: false, //恢复
      handleSyncDialog: false, //同步
      handleCLeadupDialog: false, //清除
      /* 数据注册审核 */
      handleReDialog: false,
      currentRow: {}, //当前行
      currentRows: null,
    };
  },
  created() {
    console.log(this.$route.params.status);
    // 查询数据分类
    getDataClassify().then((response) => {
      this.dbtypeselect = response.data;
    });
    this.getList();
  },
  methods: {
    handleCurrentChange(val) {
      this.currentRows = val;
    },
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.CREATE_TIME = "asc";
      } else {
        orderBy.CREATE_TIME = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 查询列表
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      getDataTable(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRows) {
          this.tableData.forEach((element, index) => {
            if (element.SYNC_ID == this.currentRows.SYNC_ID) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    // 重置
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        DDataId: "",
        examineStatus: 1,
      };
      this.getList();
    },
    /* 增量同步元数据 */
    handleAddSync() {},
    /* 表格操作 */
    // 存储结构
    handledDBMethods(row) {
      this.rowData = row;
      this.structureManageTitle = row.CLASS_NAME;
      this.structureManageVisible = true;
    },
    // 数据同步
    handlSyncMethods(row) {
      this.handleMsgObj = {};
      if (row.SYNC_ID) {
        this.handleMsgObj.id = row.SYNC_ID;
        this.dialogMsgTitle = "编辑";
      } else {
        this.handleMsgObj.databaseId = row.DATABASE_ID;
        this.handleMsgObj.dataClassId = row.DATA_CLASS_ID;
        this.handleMsgObj.pageName = "存储结构概览";
        this.dialogMsgTitle = "新增";
      }
      this.handleSyncDialog = true;
    },
    // 数据迁移
    handlMoveMethods(row) {
      this.handleMsgObj = {};
      if (row.MOVE_ID) {
        this.handleMsgObj.id = row.MOVE_ID;
        this.dialogMsgTitle = "编辑";
      } else {
        this.handleMsgObj.databaseId = row.DATABASE_ID;
        this.handleMsgObj.dataClassId = row.DATA_CLASS_ID;
        this.handleMsgObj.pageName = "存储结构概览";
        this.dialogMsgTitle = "新增";
      }
      this.handleMoveDialog = true;
    },
    // 数据清除
    handlClearMethods(row) {
      this.handleMsgObj = {};
      if (row.CLEAR_ID) {
        this.handleMsgObj.id = row.CLEAR_ID;
        this.dialogMsgTitle = "编辑";
      } else {
        this.handleMsgObj.databaseId = row.DATABASE_ID;
        this.handleMsgObj.dataClassId = row.DATA_CLASS_ID;
        this.handleMsgObj.pageName = "存储结构概览";
        this.dialogMsgTitle = "新增";
      }
      this.handleCLeadupDialog = true;
    },
    // 数据备份
    handleBackUpMethods(row) {
      this.handleMsgObj = {};
      if (row.BACKUP_ID) {
        this.handleMsgObj.id = row.BACKUP_ID;
        this.dialogMsgTitle = "编辑";
      } else {
        this.handleMsgObj.databaseId = row.DATABASE_ID;
        this.handleMsgObj.dataClassId = row.DATA_CLASS_ID;
        this.handleMsgObj.pageName = "存储结构概览";
        this.dialogMsgTitle = "新增";
      }
      this.handleBackupDialog = true;
    },
    // 数据恢复
    handlRecoverMethods(row) {
      this.handleMsgObj = {};
      this.handleMsgObj.databaseId = row.DATABASE_ID;
      this.handleMsgObj.dataClassId = row.DATA_CLASS_ID;
      this.handleMsgObj.class_name = row.CLASS_NAME;
      this.handleMsgObj.database_name = row.DATABASE_NAME;
      this.handleMsgObj.pageName = "存储结构概览";
      this.dialogMsgTitle = "数据恢复";
      this.handleDataRecoveryDialog = true;
    },
    // 审核
    examineData(row) {
      // todo
      this.currentRow = row;
      this.handleMsgObj = {};
      this.handleMsgObj = row;
      this.handleReDialog = true;
    },
    // 删除
    deleteList(row) {
      this.$confirm("是否删除" + row.TYPE_NAME + "?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteById({ id: row.ID }).then((response) => {
            if (response.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.handleQuery();
            }
          });
        })
        .catch(() => {});
    },
    // 关闭数据注册弹窗
    async handleDataReg(info) {
      if (info) {
        // 传到资料弹窗的数据
        this.handleMsgObj = {};
        this.registerForm = null;
        this.registerForm = info;
        this.handleMsgObj = info;
        this.handleMsgObj.applyId = this.currentRow.ID;
        // 根据专题库id查寻
        await databaseGet({ id: this.currentRow.DATABASE_ID }).then(
          (response) => {
            this.handleMsgObj.dataLogicList = [
              {
                logicFlag: this.currentRow.LOGIC_FLAG, //数据用途
                logicName: this.currentRow.LOGIC_NAME, //数据用途
                databaseId: this.currentRow.DATABASE_ID, //专题库id
                databaseName: response.data.databaseName, //专题库名称
                databasePName: response.data.databaseDefine.databaseName, //数据库名称
              },
            ];
            this.reviewStep = true;
          }
        );
      } else {
        // 取消/拒绝
        this.getList();
      }
      this.handleReDialog = false;
    },
    // 关闭步骤
    closeStep() {
      this.reviewStep = false;
      this.handleQuery();
    },
    // 关闭弹窗
    handleClose() {
      this.handleMsgObj = {};
      this.dialogSetting = false;
      this.handleDSDialog = false;
      this.handleCLeadupDialog = false;
      this.handleBackupDialog = false;
      this.handleDataRecoveryDialog = false;
      this.handleMoveDialog = false;
      this.structureManageVisible = false;
      this.handleSyncDialog = false;
      this.getList();
    },
  },
};
</script>

<style>
</style>
