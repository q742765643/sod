<template>
  <div class="app-container">
    <!-- 系统元数据备份 -->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="元数据备份配置" name="first">
        <el-form :model="queryParams" :inline="true">
          <el-form-item label="数据库IP">
            <el-select size="small" style="width:200px" filterable v-model="queryParams.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input size="small" v-model="queryParams.taskName"></el-input>
          </el-form-item>
          <el-form-item label="任务状态">
            <el-select
              size="small"
              filterable
              style="width:200px"
              v-model="queryParams.triggerStatus"
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
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              size="small"
              type="success"
              @click="handleExport('server')"
              icon="el-icon-download"
            >服务库数据导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              size="small"
              type="success"
              @click="handleExport('base')"
              icon="el-icon-download"
            >元数据数据导出</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              size="small"
              type="primary"
              @click="handleExport('add')"
              icon="el-icon-plus"
            >添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="danger" @click="deleteShow()" icon="el-icon-delete">删除同步任务</el-button>
          </el-col>
        </el-row>
        <el-table
          v-loading="loading"
          :data="tableData"
          row-key="id"
          highlight-current-row
          @selection-change="handleTaskSelectionChange"
        >
          <el-table-column align="center" type="index" width="40" :index="table_index"></el-table-column>
          <el-table-column align="center" type="selection" width="55"></el-table-column>
          <el-table-column
            align="center"
            prop="taskName"
            label="任务名称"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            align="center"
            prop="storageDirectory"
            label="存储目录"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            align="center"
            prop="databaseName"
            label="数据库"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column align="center" prop="jobCron" label="执行策略" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" prop="type" label="类型" width="100"></el-table-column>
          <el-table-column align="center" prop="triggerStatus" label="状态">
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
          <el-table-column align="center" label="操作" width="360px">
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
                icon="el-icon-video-play"
                :disabled="scope.row.triggerStatus==1"
                @click="handleTask(scope.row,'启动')"
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
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true">
          <el-form-item label="数据库IP">
            <el-select style="width:200px" filterable v-model="rowlogForm.name">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input size="small" v-model="rowlogForm.taskName"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              style="width:300px"
              v-model="dateRange"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="queryListLog" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" align="center" width="40" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" align="center" width="55"></el-table-column>
          <el-table-column
            prop="taskName"
            align="center"
            label="任务名称"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="storageDirectory"
            align="center"
            label="存储目录"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="databaseName"
            align="center"
            label="数据库"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            width="200px"
            prop="handleTime"
            align="center"
            label="运行开始时间"
            :formatter="createTimeFormater"
          ></el-table-column>
          <el-table-column prop="dysnc_type" align="center" label="类型"></el-table-column>
          <el-table-column prop="dysnc_model" align="center" label="操作类型"></el-table-column>
          <el-table-column label="状态" align="center" prop="handleCode" :formatter="statusFormat"></el-table-column>
          <el-table-column align="center" label="操作" width="240px">
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
                @click="download(scope.row)"
              >下载</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="50%">
      <handleExport @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleExport>
    </el-dialog>
    <el-dialog title="同步详情" :visible.sync="syncDescDialogVisible" v-if="syncDescDialogVisible">
      <div v-for="(item,index) in syncDescList" :key="index">
        <div v-if="item.run_state=='同步成功'">
          <span>同步表名{{index}}:{{item.dysnc_tablename}}</span>
          <span>同步记录数{{index}}:{{item.dysnc_recordnum}}</span>
        </div>
        <div v-else>
          <span>同步表名{{index}}:{{item.dysnc_tablename}}</span>
          <span>同步状态:{{item.run_state}}</span>
          <span>失败原因:{{item.fail_reason}}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeSyncDesc()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { formatTime } from "@/components/commonVaildate.js";
import {
  findDataBase,
  metaBackupList,
  metaBackupDel,
  stopTask,
  startTask,
  listLog,
  listLogDatail
} from "@/api/system/metadataBackup";
//增加查看弹框
import handleExport from "@/views/system/metadataBackup/handleExport";
export default {
  components: {
    handleExport
  },
  data() {
    return {
      activeName: "first",
      // 遮罩层
      loading: true,
      ipList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      // 新增/编辑同步任务
      dialogTitle: "",
      handleDialog: false,
      handleObj: {},
      // 立即同步元数据
      syncDescDialogVisible: false,
      syncDescList: [],
      currentRow: [],
      //同步记录查询
      loadingHis: false,
      rowlogForm: {
        pageNum: 1,
        pageSize: 10,
        taskName: "",
        databaseId: ""
      },
      dateRange: [],
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      tableNames: [],
      statusOptions: []
    };
  },
  created() {
    findDataBase().then(response => {
      this.ipList = response.data;
    });
    this.getDicts("job_handle_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getMetaBackupList();
  },
  methods: {
    createTimeFormater: function(row) {
      return formatTime(row.createTime, "Y-M-D h:m:s");
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
    /** 查询列表 */
    getMetaBackupList() {
      this.loading = true;
      metaBackupList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    handleExport(type) {
      this.handleObj = {};
      if (type == "server") {
        this.dialogTitle = "执行服务库数据备份";
        this.handleObj.handleType = "server";
      } else if (type == "add") {
        this.dialogTitle = "添加";
        this.handleObj.handleType = "add";
      } else {
        this.dialogTitle = "执行元数据备份";
        this.handleObj.handleType = "base";
      }
      this.handleDialog = true;
    },
    // 启动/停止
    handleTask(row, type) {
      this.$confirm("是否确认" + type + row.taskName + "?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          if (type == "启动") {
            return startTask(row.id);
          } else {
            return stopTask(row.id);
          }
        })
        .then(() => {
          this.getMetaBackupList();
          this.msgSuccess(type + "成功");
        })
        .catch(function() {});
    },
    // 删除配置
    deleteShow() {
      if (this.currentRow.length > 0) {
        let names = [];
        let ids = [];
        this.currentRow.forEach(element => {
          names.push(element.taskName);
          ids.push(element.id);
        });
        this.$confirm('是否确认删除"' + names.join(",") + '"?', "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return metaBackupDel(ids.join(","));
          })
          .then(() => {
            this.getMetaBackupList();
            this.msgSuccess("删除成功");
          })
          .catch(function() {});
      }
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        databaseId: "",
        taskName: "",
        triggerStatus: ""
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
      this.handleObj.handleType = "add";
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
    handleHistorySelectionChange() {
      this.currentRow = val;
    },
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.currentRow = [];
      this.handleDialog = false;
      this.handleQuery();
    },
    //日志列表
    getListLog() {
      this.loading = true;
      console.log(this.rowlogForm);
      listLog(this.addDateRange(this.rowlogForm, this.dateRange)).then(
        response => {
          this.logTableData = response.data.pageData;
          this.logDataTotal = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    // 查看详情
    showDetailDialog(row) {
      listLogDatail(row.id).then(res => {
        console.log(res);
      });
    },
    download() {},
    handleClick() {
      if (this.activeName == "first") {
        this.resetQuery();
      } else {
        this.rowlogForm = {
          pageNum: 1,
          pageSize: 10,
          taskName: "",
          databaseId: ""
        };
        this.queryListLog();
      }
    }
  }
};
</script>
