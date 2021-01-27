<template>
  <div class="app-container overviewStorage">
    <!-- 存储结构概览 -->
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
          filterable
          v-model="queryParams.databaseId"
          placeholder="请选择"
        >
          <el-option
            v-for="(citem, cindex) in specialList"
            :key="cindex"
            :label="citem.databaseName"
            :value="citem.id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="关联资料:" prop="className">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.className"
          placeholder="请输入关联资料"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          size="small"
          type="primary"
          @click="handleQuery"
          icon="el-icon-search"
          >查询</el-button
        >
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right"
          >重置</el-button
        >
        <!--   <el-button size="small" type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button> -->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          size="small"
          type="primary"
          icon="el-icon-plus"
          @click="handleTable('add')"
          >新增</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          size="small"
          type="success"
          icon="el-icon-download"
          @click="handleExport"
          >导出</el-button
        >
      </el-col> -->
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
      class="afTable"
      :span-method="objectSpanMethod"
    >
      <af-table-column prop="TABLE_NAME" label="表名称">
        <template slot-scope="scope">
          <span>{{ scope.row.TABLE_NAME }}</span>
        </template>
      </af-table-column>
      <af-table-column prop="DATABASE_NAME" label="数据库">
        <template slot-scope="scope">
          <span>{{ scope.row.DATABASE_NAME }}</span>
        </template>
      </af-table-column>
      <af-table-column prop="DATABASE_P_NAME" label="专题名">
        <template slot-scope="scope">
          <span>{{ scope.row.DATABASE_P_NAME }}</span>
        </template>
      </af-table-column>
      <af-table-column prop="DICT_LABEL" label="表类型">
        <template slot-scope="scope">
          <span>{{ scope.row.DICT_LABEL }}</span>
        </template>
      </af-table-column>
      <af-table-column prop="D_DATA_ID" label="关联资料">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="showLinkMateria(scope.row)"
            >查看</el-button
          >
        </template>
      </af-table-column>

      <af-table-column label="参数配置">
        <template slot-scope="scope">
          <!-- 存储结构 -->
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
          <!-- 数据同步 -->
          <el-button disabled v-if="scope.row.SYNC_IDENTIFIER == 3" size="mini">
            <i class="btnRound orangRound"></i>数据同步
          </el-button>
          <el-button v-else size="mini" @click="handlSyncMethods(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.SYNC_ID"></i>
            <i class="btnRound orangRound" v-else></i>数据同步
          </el-button>
          <!-- 迁移 -->

          <el-button
            v-if="scope.row.MOVE_ST === 1"
            size="mini"
            @click="handlMoveMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.MOVE_ID"></i>
            <i class="btnRound orangRound" v-else></i>迁移
          </el-button>

          <!-- 清除 -->
          <el-button
            v-if="scope.row.CLEAR_ST === 1"
            size="mini"
            @click="handlClearMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
            <i class="btnRound orangRound" v-else></i>清除
          </el-button>

          <!-- 备份 -->
          <el-button
            v-if="scope.row.BACKUP_ST === 1"
            size="mini"
            @click="handleBackUpMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.BACKUP_ID"></i>
            <i class="btnRound orangRound" v-else></i>备份
          </el-button>

          <!-- 恢复 -->
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
      <af-table-column label="操作" width="140px">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-setting"
            @click="settingCell(scope.row)"
            >配置</el-button
          >
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCell(scope.row)"
            >删除</el-button
          >
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
    <!-- 高级搜索 -->
    <el-dialog
      :close-on-click-modal="false"
      title="筛选"
      :visible.sync="dialogSuperSearch"
      :before-close="closeSuperSearch"
      v-dialogDrag
    >
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @searchFun="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
    <!-- 配置 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="选择需要配置的选项"
      :visible.sync="dialogSetting"
      width="650px"
      :before-close="handleClose"
    >
      <el-checkbox-group v-model.trim="checkSetList">
        <el-checkbox
          label="数据同步"
          border
          @change="changeSetting($event, 'SYNC_IDENTIFIER', '数据同步')"
        ></el-checkbox>
        <el-checkbox
          v-for="(item, index) in checkBoxList"
          :key="index"
          :label="item.label"
          border
          @change="changeSetting($event, item.value, item.label)"
        ></el-checkbox>
        <el-checkbox
          label="恢复"
          border
          @change="changeSetting($event, 'ARCHIVING_IDENTIFIER', '恢复')"
        ></el-checkbox>
      </el-checkbox-group>
    </el-dialog>

    <!-- 数据恢复 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="结构化数据恢复"
      :visible.sync="handleDataRecoveryDialog"
      :before-close="handleClose"
      width="1100px"
    >
      <DataRecovery
        v-if="handleDataRecoveryDialog"
        :handleObj="handleMsgObj"
        @handleRecover="handleClose"
      />
    </el-dialog>

    <!-- 迁移清除 结构化数据清除进度 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="handleCLeadupDialog"
      :before-close="handleClose"
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
      :before-close="handleClose"
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
      :before-close="handleClose"
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

    <!-- 表新增编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="tableVisible"
      width="700px"
      v-dialogDrag
    >
      <handleTable
        v-if="tableVisible"
        :handleObj="handleMsgObj"
        @cancelHandle="handleTableDialog"
      />
    </el-dialog>
    <!-- 表结构管理 存储结构 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="`表结构管理(${structureManageTitle + dialogMsgTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="handleClose"
      top="0"
      class="scrollDialog"
    >
      <StructureManageTable
        ref="StructureManageTableRef"
        v-if="structureManageVisible"
        v-bind:parentRowData="rowData"
        :tableBaseInfo="tableBaseInfo"
      />
    </el-dialog>
    <!-- 关联资料查看 -->
    <el-dialog
      :close-on-click-modal="false"
      title="关联资料查看"
      :visible.sync="linkMateriaVisible"
      width="72%"
      v-dialogDrag
    >
      <el-table :data="linkMateriaTableData" style="width: 100%">
        <el-table-column prop="CLASS_NAME" label="资料名" align="center">
        </el-table-column>
        <el-table-column prop="D_DATA_ID" label="四级编码" align="center">
        </el-table-column>
        <el-table-column prop="DATA_CLASS_ID" label="存储编码" align="center">
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {
  storageConfigurationList,
  updateColumnValue,
  deleteColumnValue,
  exportTable,
  getClassByTableId,
} from "@/api/structureManagement/overviewStorage";
import {
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
} from "@/api/structureManagement/materialManage/index";
import { getSyncInfo } from "@/api/schedule/dataSync";
// 高级搜索
import SuperSearch from "@/components/superSearch";
// 数据恢复
import DataRecovery from "@/views/structureManagement/overviewStorage/handleDataRecovery";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/overviewStorage/TableManage/StructureManageTable";
// 迁移配置信息
import handleMove from "@/views/schedule/move/handleMove";
// 迁移清除 结构化数据清除配置
import handleClear from "@/views/schedule/clear/handleClear";
// 结构化数据备份配置弹窗
import handleBackUp from "@/views/schedule/backup/handleBackUp";
// 数据同步
import handleSync from "@/views/schedule/dataSync/handleSync";
//表新增编辑
import handleTable from "@/views/structureManagement/overviewStorage/handleTable";

export default {
  components: {
    SuperSearch,
    DataRecovery,
    StructureManageTable,
    handleMove,
    handleClear,
    handleBackUp,
    handleSync,
    handleTable,
  },
  data() {
    return {
      linkMateriaTableData: [],
      linkMateriaVisible: false,
      tableBaseInfo: {},
      tableVisible: false,
      dataLogicList: [],
      DatabaseDefineList: [],
      specialList: [],
      checkBoxList: [],
      checkSetList: [],
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        databaseId: "",
        databasePid: "",
        tableName: "",
        className: "",
      },
      total: 0,
      tableData: [],
      // 高级搜索
      dialogSuperSearch: false,
      superObj: {},
      // 弹窗
      rowData: {}, //当前行
      structureManageTitle: "", //存储结构标题
      dialogMsgTitle: "", //标题
      handleMsgObj: {}, //
      //存储结构
      structureManageVisible: false,

      handleDSDialog: false,
      handleCLeadupDialog: false,
      handleBackupDialog: false,
      handleDataRecoveryDialog: false,
      handleMoveDialog: false,
      handleSyncDialog: false, //数据同步

      // 设置
      dialogSetting: false,

      rowId: "",
      // 数据恢复
      handleDataRecoveryDialog: false,
      superMsg: {},
      currentRow: null,
      // 合并单元格
      spanArr: [],
      position: 0,
    };
  },
  created() {
    getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
    this.getList();
  },
  methods: {
    showLinkMateria(row) {
      getClassByTableId({ tableId: row.ID }).then((res) => {
        this.linkMateriaVisible = true;
        this.linkMateriaTableData = res.data;
      });
    },
    // 新增/编辑
    handleTable(info) {
      if (info == "add") {
        this.handleMsgObj = {};
        this.dialogMsgTitle = "新增";
      } else {
        this.dialogMsgTitle = "编辑";
      }
      this.tableVisible = true;
    },
    //切换数据库查询专题库
    handleFindSpec(val) {
      this.queryParams.databaseId = "";
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },
    handleTableDialog(info) {
      if (info) {
        this.structureManageTitle = "";
        this.structureManageVisible = true;
        this.tableBaseInfo = info;
      }
      this.tableVisible = false;
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    handleExport() {
      exportTable(this.queryParams).then((res) => {
        this.downloadfileCommon(res);
      });
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.superMsg = {};
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.superMsg = {};
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList(superMsg) {
      // 判断是否是高级搜索
      let queryObj = {};
      if (
        (superMsg && superMsg.domains) ||
        (this.superMsg && this.superMsg.domains)
      ) {
        if (superMsg && superMsg.domains) {
          this.queryParams.pageNum = 1;
          this.superMsg = superMsg;
        }
        let superList = this.superMsg.domains;
        let newSuperForm = {};
        for (let i = 0; i < superList.length; i++) {
          newSuperForm[superList[i].select] = superList[i].value;
        }
        Object.assign(queryObj, this.queryParams, newSuperForm);
      } else {
        queryObj = this.queryParams;
      }
      this.loading = true;
      storageConfigurationList(queryObj).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        if (this.tableData.length > 0) {
          this.rowspan();
        }
        this.loading = false;
        this.$refs.singleTable.setCurrentRow();
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.ID == this.currentRow.ID) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    superClick() {
      this.superObj = this.queryParams;
      this.superObj.pageName = "存储结构概览";
      this.dialogSuperSearch = true;
    },
    // 存储结构
    handledDBMethods(row) {
      this.rowData = row;
      this.structureManageTitle = row.TABLE_NAME;
      this.structureManageVisible = true;
    },
    // 数据同步
    handlSyncMethods(row) {
      this.handleMsgObj = {};
      if (row.SYNC_ID) {
        getSyncInfo(row.SYNC_ID).then((response) => {
          this.dialogMsgTitle = "编辑";
          this.handleMsgObj = response.data;
          // / 同步任务执行节点回显
          this.handleMsgObj.ipAndPort =
            this.handleMsgObj.execIp + ":" + this.handleMsgObj.execPort;
          // 目标表回显
          if (this.handleMsgObj.targetRelation.length > 1) {
            this.handleMsgObj.targetTable2 = this.handleMsgObj.targetRelation[1].targetTableId;
          }
          this.handleMsgObj.targetTable = this.handleMsgObj.targetRelation[0].targetTableId;
          this.handleMsgObj.handleType = "edit";
        });
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
    settingCell(row) {
      let setArry = [
        {
          label: "数据同步",
          value: "SYNC_IDENTIFIER",
        },
        {
          label: "恢复",
          value: "ARCHIVING_IDENTIFIER",
        },
      ];
      let setArry2 = [
        {
          label: "迁移",
          value: "MOVE_ST",
        },
        {
          label: "清除",
          value: "CLEAR_ST",
        },
        {
          label: "备份",
          value: "BACKUP_ID",
        },
      ];

      let checkedBoxArry = [
        /* {
          label: "数据同步",
          value: "SYNC_IDENTIFIER"
        }, */
        {
          label: "迁移",
          value: "MOVE_IDENTIFIER",
        },
        {
          label: "清除",
          value: "CLEAN_IDENTIFIER",
        },
        {
          label: "备份",
          value: "BACKUP_IDENTIFIER",
        },
        /* {
          label: "恢复",
          value: "ARCHIVING_IDENTIFIER"
        } */
      ];
      this.rowId = row.ID;
      this.checkSetList = [];
      this.checkBoxList = [];
      setArry.forEach((element) => {
        if (row[element.value] === 1 || row[element.value] === 2) {
          this.checkSetList.push(element.label);
        }
      });
      setArry2.forEach((element) => {
        if (row[element.value] == 1) {
          this.checkSetList.push(element.label);
        }
      });

      this.checkSetList.forEach((element) => {
        checkedBoxArry.forEach((item) => {
          if (element == item.label) {
            this.checkBoxList.push(item);
          }
        });
      });
      console.log(this.checkBoxList);
      console.log(this.checkSetList);
      this.dialogSetting = true;
    },
    deleteCell(row) {
      this.$confirm("确认删除" + row.TABLE_NAME + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteColumnValue({ id: row.ID }).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.handleQuery();
            } else {
              this.$message({
                type: "error",
                message: response.msg,
              });
            }
          });
        })
        .catch(() => {});
    },
    // 关闭高级搜索
    closeSuperSearch() {
      this.dialogSuperSearch = false;
    },
    // 关闭弹窗
    handleClose() {
      this.handleMsgObj = {};
      this.rowData = {};
      this.tableBaseInfo = {};
      this.dialogSetting = false;
      this.handleDSDialog = false;
      this.handleCLeadupDialog = false;
      this.handleBackupDialog = false;
      this.handleDataRecoveryDialog = false;
      this.handleMoveDialog = false;
      this.structureManageVisible = false;
      this.handleSyncDialog = false;
      this.tableVisible = false;
      this.getList();
    },
    changeSetting(checked, column, label) {
      let settingObj = {};
      settingObj.id = this.rowId;
      settingObj.column = column;
      if (checked === false) {
        settingObj.value = 3;
        this.$confirm(
          "取消该配置将会同步删除已配置的任务，是否确认删除?",
          "温馨提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            updateColumnValue(settingObj).then((response) => {
              this.$message({
                type: "success",
                message: "配置成功",
              });
              this.getList();
            });
          })
          .catch(() => {
            this.checkSetList.push(label);
          });
      } else {
        settingObj.value = 2;
        updateColumnValue(settingObj).then((response) => {
          this.$message({
            type: "success",
            message: "配置成功",
          });
          this.getList();
        });
      }
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      //表格合并行
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        return {
          rowspan: _row,
          colspan: 1,
        };
      }
    },
    // 判断哪些需要合并
    rowspan() {
      this.spanArr = [];
      this.position = 0;
      this.tableData.forEach((item, index) => {
        if (index === 0) {
          this.spanArr.push(1);
          this.position = 0;
        } else {
          if (
            this.tableData[index].TABLE_NAME ===
            this.tableData[index - 1].TABLE_NAME
          ) {
            this.spanArr[this.position] += 1;
            this.spanArr.push(0);
          } else {
            this.spanArr.push(1);
            this.position = index;
          }
        }
      });
    },
  },
};
</script>
<style lang="scss">
.overviewStorage {
  .searchBox {
    margin-bottom: 20px;
  }
  .el-table.afTable {
    font-size: 12px;
    .el-button--mini {
      padding: 6px;
      margin-left: 0;
    }
    .el-button--text {
      padding: 6px 2px;
    }
    .cell {
      white-space: nowrap !important;
      width: fit-content !important;
    }
    th {
      text-align: left;
    }
    th.is-center {
      text-align: center;
    }
  }
}
.scrollDialog {
  .el-dialog__header {
    padding: 10px 20px;
    padding-left: 44px;
    margin-bottom: 0;
  }
  .el-dialog__header:before {
    top: 12px;
  }
  .el-dialog__headerbtn {
    top: 12px;
  }
}
</style>
