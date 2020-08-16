<template>
  <div class="app-container metadataBackupTem">
    <!-- 系统元数据备份 -->
    <el-tabs v-model.trim="activeName" @tab-click="handleClick">
      <el-tab-pane label="元数据备份配置" name="first">
        <el-form :model="queryParams" :inline="true" class="searchBox">
          <el-form-item label="数据库IP">
            <el-select
              size="small"
              style="width:200px"
              filterable
              v-model.trim="queryParams.databaseId"
            >
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input clearable size="small" v-model.trim="queryParams.taskName"></el-input>
          </el-form-item>
          <el-form-item label="任务状态">
            <el-select
              size="small"
              filterable
              style="width:200px"
              v-model.trim="queryParams.triggerStatus"
            >
              <el-option value=" " label="全部"></el-option>
              <el-option value="0" label="停止"></el-option>
              <el-option value="1" label="启动"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
            <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button size="small" type="primary" @click="handleAdd" icon="el-icon-plus">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="danger" @click="deleteShow()" icon="el-icon-delete">删除同步任务</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
          </el-col>
        </el-row>
        <el-table
          border
          v-loading="loading"
          :data="tableData"
          row-key="id"
          highlight-current-row
          @selection-change="handleTaskSelectionChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="taskName" label="任务名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="storageDirectory" label="存储目录" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="databaseName" label="数据库" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="jobCron" label="执行策略" width="120" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="isStructure" label="类型" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.isStructure==0">结构</span>
              <span v-if="scope.row.isStructure==1">数据</span>
              <span v-if="scope.row.isStructure=='0,1'||scope.row.isStructure=='1,0'">数据，结构</span>
            </template>
          </el-table-column>
          <el-table-column prop="triggerStatus" label="状态" width="80">
            <template slot-scope="scope">
              <el-link
                icon="el-icon-circle-close"
                type="danger"
                :underline="false"
                v-if="scope.row.triggerStatus==0"
              >停止</el-link>
              <el-link
                icon="el-icon-circle-check"
                type="success"
                :underline="false"
                v-if="scope.row.triggerStatus==1"
              >启动</el-link>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="400">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-edit"
                @click="showEditDialog(scope.row)"
              >编辑</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-video-pause"
                :disabled="scope.row.triggerStatus==0"
                @click="handleTask(scope.row,'停止')"
              >停止</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-video-pause"
                :disabled="scope.row.triggerStatus==1"
                @click="handleTask(scope.row,'启动')"
              >启动</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-thumb"
                @click="handleTask(scope.row,'立即执行')"
              >立即执行</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-reading"
                @click="viewDaily(scope.row)"
              >查询日志</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getMetaBackupList"
        />
      </el-tab-pane>
      <el-tab-pane label="元数据备份监控" name="second">
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true" class="searchBox">
          <el-form-item label="数据库IP">
            <el-select style="width:200px" filterable v-model.trim="rowlogForm.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input clearable size="small" v-model.trim="rowlogForm.taskName"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              style="width:300px"
              v-model.trim="rowlogForm.dateRange"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="queryListLog" icon="el-icon-search">查询</el-button>
            <el-button size="small" @click="resetQueryLog" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button
              size="small"
              type="success"
              icon="el-icon-download"
              @click="handleLogExport"
            >导出</el-button>
          </el-col>
        </el-row>
        <el-table
          border
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="taskName" label="任务名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="storageDirectory" label="存储目录" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="databaseName" label="数据库" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="handleTime" label="运行开始时间" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.handleTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isStructure" label="类型" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.isStructure==0">结构</span>
              <span v-if="scope.row.isStructure==1">数据</span>
              <span v-if="scope.row.isStructure=='0,1'||scope.row.isStructure=='1,0'">数据，结构</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="handleCode" :formatter="statusFormat" width="80"></el-table-column>
          <el-table-column label="操作" width="240px">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-reading"
                @click="showDetailDialog(scope.row)"
              >查看详情</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-download"
                @click="handleFileExport(scope.row)"
              >下载</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-delete"
                @click="deleteLog(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="logDataTotal>0"
          :total="logDataTotal"
          :page.sync="rowlogForm.pageNum"
          :limit.sync="rowlogForm.pageSize"
          @pagination="getListLog"
        />
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="70%"
    >
      <handleExportD @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleExportD>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="详情"
      :visible.sync="logDetailDialog"
      v-if="logDetailDialog"
      width="1100px"
    >
      <el-form ref="ruleForm" :model="logFormDialog" label-width="140px" class="logDetailBox">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称">
              <el-input size="small" v-model.trim="logFormDialog.taskName"></el-input>
            </el-form-item>
            <el-form-item label="数据库IP">
              <el-select size="small" filterable v-model.trim="logFormDialog.databaseId">
                <el-option
                  v-for="(item,index) in ipList"
                  :key="index"
                  :label="item.VAULE"
                  :value="item.KEY"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="类型">
              <el-checkbox-group v-model.trim="logFormDialog.checked">
                <el-checkbox label="结构">结构</el-checkbox>
                <el-checkbox label="数据">数据</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="存储目录">
              <el-input size="small" v-model.trim="logFormDialog.storageDirectory"></el-input>
            </el-form-item>
            <el-form-item label="执行地址">
              <el-input size="small" v-model.trim="logFormDialog.executorAddress"></el-input>
            </el-form-item>
            <el-form-item label="执行耗时" class="unitInput">
              <el-input size="small" v-model.trim="logFormDialog.elapsedTime"></el-input>s
            </el-form-item>
            <el-form-item label="应触发时间">
              <el-input size="small" v-model.trim="logFormDialog.triggerTime"></el-input>
            </el-form-item>
            <el-form-item label="实际触发时间">
              <el-input size="small" v-model.trim="logFormDialog.handleTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <el-tree ref="eltree" node-key="nodeKey" :data="treedata" :props="defaultProps"></el-tree>
            </el-scrollbar>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="执行过程">
              <el-input size="small" v-model.trim="logFormDialog.handleMsg" type="textarea"></el-input>
              <el-button type="primary" size="small" @click="showAllDetail">显示全部</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="logDetailDialog = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="执行过程"
      :visible.sync="allDetailDialog"
      v-if="allDetailDialog"
      width="1000px"
      append-to-body
      v-dialogDrag
    >
      <el-input size="small" v-model.trim="allDetailMsg" type="textarea" class="allDetailMsg"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allDetailDialog = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { newTeam } from "@/components/commonVaildate";
import { formatDate } from "@/utils/index";
import {
  findDataBase,
  metaBackupList,
  metaBackupDel,
  startTask,
  stopTask,
  execute,
  listLog,
  deleteLogById,
  listLogDatail,
  downFile,
  exportTable,
  exportTableLog,
} from "@/api/system/metadataBackup";
//增加查看弹框
import handleExportD from "@/views/system/metadataBackup/handleExport";
export default {
  components: {
    handleExportD,
  },
  data() {
    return {
      allDetailDialog: false,
      allDetailMsg: "",
      handleExportObj: {},
      activeName: "first",
      // 遮罩层
      loading: true,
      ipList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      tableData: [],
      total: 0,
      // 新增/编辑同步任务
      dialogTitle: "",
      handleDialog: false,
      handleObj: {},
      // 立即同步元数据
      logDetailDialog: false,
      syncDescList: [],
      currentRow: [],
      //同步记录查询
      loadingHis: false,
      rowlogForm: {
        pageNum: 1,
        pageSize: 10,
        taskName: "",
        databaseId: "",
      },
      dateRange: [],
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      tableNames: [],
      statusOptions: [],
      logFormDialog: {
        checked: [],
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      treedata: [],
    };
  },
  created() {
    findDataBase().then((response) => {
      this.ipList = response.data;
    });
    this.getDicts("job_handle_status").then((response) => {
      this.statusOptions = response.data;
    });
    this.getMetaBackupList();
  },
  methods: {
    showAllDetail() {
      this.allDetailMsg = this.logFormDialog.handleMsg;
      this.allDetailDialog = true;
    },
    handleExport() {
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "下载中，请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      exportTable(this.queryParams).then((res) => {
        this.downloadfileCommon(res);
        loading.close();
      });
    },
    handleFileExport(row) {
      downFile({ path: row.storageDirectory }).then((res) => {
        this.downloadfileCommon(res);
      });
    },
    handleLogExport(row) {
      exportTableLog(this.rowlogForm).then((res) => {
        this.downloadfileCommon(res);
      });
    },

    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    // table自增定义方法
    table_index_log(index) {
      return (
        (this.rowlogForm.pageNum - 1) * this.rowlogForm.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getMetaBackupList();
    },
    queryListLog() {
      this.rowlogForm.pageNum = 1;
      this.getListLog();
    },
    resetQueryLog() {
      this.rowlogForm = {
        pageNum: 1,
        pageSize: 10,
        taskName: "",
        databaseId: "",
      };
      this.getListLog();
    },
    /** 查询列表 */
    getMetaBackupList() {
      this.loading = true;
      metaBackupList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    handleAdd(type) {
      this.handleObj = {};
      this.dialogTitle = "添加";
      this.handleDialog = true;
    },
    // 启动/停止
    handleTask(row, type) {
      this.$confirm("是否确认" + type + row.taskName + "?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          if (type == "启动") {
            return startTask(row.id);
          } else if (type == "停止") {
            return stopTask(row.id);
          } else if (type == "立即执行") {
            return execute(row.id);
          }
        })
        .then(() => {
          this.getMetaBackupList();
          this.msgSuccess(type + "成功");
        })
        .catch(function () {});
    },
    // 删除配置
    deleteShow() {
      if (this.currentRow.length > 0) {
        let names = [];
        let ids = [];
        this.currentRow.forEach((element) => {
          names.push(element.taskName);
          ids.push(element.id);
        });
        this.$confirm('是否确认删除"' + names.join(",") + '"?', "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            return metaBackupDel(ids.join(","));
          })
          .then(() => {
            this.getMetaBackupList();
            this.msgSuccess("删除成功");
          })
          .catch(function () {});
      }
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        databaseId: "",
        taskName: "",
        triggerStatus: "",
      };
      this.getMetaBackupList();
    },

    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.handleCode);
    },
    // 编辑
    showEditDialog(row) {
      this.dialogTitle = "编辑";
      this.handleObj = row;
      this.handleDialog = true;
    },
    viewDaily(row) {
      this.activeName = "second";
      this.rowlogForm.taskName = row.taskName;
      this.getListLog();
    },
    //选中行
    handleTaskSelectionChange(val) {
      this.currentRow = val;
    },
    handleHistorySelectionChange(val) {
      this.currentRow = val;
    },
    // 弹框取消
    cancelHandle(logFormDialog) {
      this.handleObj = {};
      this.currentRow = [];
      this.handleDialog = false;
      if (this.dialogTitle.indexOf("添加") != -1) {
        this.handleQuery();
      } else {
        this.getMetaBackupList();
      }
    },
    //日志列表
    getListLog() {
      this.loading = true;
      if (this.rowlogForm.dateRange) {
        this.dateRange = this.rowlogForm.dateRange;
      } else {
        this.dateRange = [];
      }
      console.log(this.rowlogForm);
      listLog(this.addDateRange(this.rowlogForm, this.dateRange)).then(
        (response) => {
          this.logTableData = response.data.pageData;
          this.logDataTotal = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    // 查看详情
    showDetailDialog(row) {
      listLogDatail(row.id).then((res) => {
        this.logFormDialog = res.data;
        this.logDetailDialog = true;
        this.logFormDialog.checked = [];
        let checkedArry = this.logFormDialog.isStructure.split(",");
        checkedArry.forEach((element) => {
          if (element == "0") {
            this.logFormDialog.checked.push("结构");
          }
          if (element == "1") {
            this.logFormDialog.checked.push("数据");
          }
        });
        this.logFormDialog.triggerTime = formatDate(
          this.logFormDialog.triggerTime
        );
        this.logFormDialog.handleTime = formatDate(
          this.logFormDialog.handleTime
        );
        let checkedTree = JSON.parse(res.data.backContent);
        this.treedata = newTeam(checkedTree, "");
      });
    },
    download(row) {
      this.handleExportObj = {};
      this.handleExportObj.path = row.storageDirectory;
    },
    deleteLog(row) {
      this.$confirm('是否确认删除"' + row.taskName + '"?', "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return deleteLogById(row.id);
        })
        .then(() => {
          this.handleClick();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    handleClick() {
      if (this.activeName == "first") {
        this.resetQuery();
      } else {
        this.rowlogForm = {
          pageNum: 1,
          pageSize: 10,
          taskName: "",
          databaseId: "",
        };
        this.queryListLog();
      }
    },
  },
};
</script>
<style lang="scss">
.metadataBackupTem {
  .el-select {
    width: 100%;
  }
  .el-scrollbar {
    height: 470px;
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
  }
}
.allDetailMsg {
  .el-textarea__inner {
    min-height: 300px !important;
  }
}
.logDetailBox {
  .el-textarea {
    width: 90%;
  }
}
</style>
