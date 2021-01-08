<template>
  <div class="app-container">
    <el-tabs  v-model.trim="activeName" @tab-click="changeTabs">
      <el-tab-pane label="同步配置" name="first">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
      class="searchBox"
    >
      <el-form-item label="资料名称" prop="profileName">
        <el-input
          v-model.trim="queryParams.profileName"
          placeholder="请输入物理库资料名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存储编码" prop="dataClassId">
        <el-input
          v-model.trim="queryParams.dataClassId"
          placeholder="请输入存储编码或者四级编码"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
          v-model.trim="queryParams.tableName"
          placeholder="请输入表名"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="triggerStatus">
        <el-select
          v-model.trim="queryParams.triggerStatus"
          placeholder="运行状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model.trim="queryParams.dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['schedule:backup:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['schedule:backup:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:backup:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="synctofileList"
      row-key="id"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="执行策略" prop="jobCron" :show-overflow-tooltip="true" width="160" />
      <el-table-column label="状态" prop="triggerStatus" :formatter="statusFormat" width="80" />
      <el-table-column label="创建时间" prop="createTime" width="160" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['schedule:backup:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:backup:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.triggerStatus==1"
            icon="el-icon-video-pause"
            @click="handleStop(scope.row)"
            v-hasPermi="['schedule:job:stop']"
          >停止</el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.triggerStatus==0"
            icon="el-icon-video-play"
            @click="handleStart(scope.row)"
            v-hasPermi="['schedule:job:start']"
          >启动</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleExecute(scope.row)"
            v-hasPermi="['schedule:job:execute']"
          >立即执行</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-tickets"
            @click="queryDaily(scope.row)"
          >查询日志</el-button>
        </template>
      </el-table-column>
    </el-table>
      </el-tab-pane>

      <el-tab-pane label="同步日志" name="second">
        <el-form
          :model="queryParamsLog"
          ref="queryFormLog"
          :inline="true"
          label-width="68px"
          class="searchBox"
        >
          <el-form-item label="资料名称" prop="profileName">
            <el-input
              v-model.trim="queryParamsLog.profileName"
              placeholder="请输入物理库资料名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQueryLog"
            />
          </el-form-item>
          <el-form-item label="存储编码" prop="dataClassId">
            <el-input
              v-model.trim="queryParamsLog.dataClassId"
              placeholder="请输入存储编码或者四级编码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQueryLog"
            />
          </el-form-item>
          <el-form-item label="表名" prop="tableName">
            <el-input
              v-model.trim="queryParamsLog.tableName"
              placeholder="请输入表名"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQueryLog"
            />
          </el-form-item>
          <el-form-item label="状态" prop="handleCode">
            <el-select
              v-model.trim="queryParamsLog.handleCode"
              placeholder="运行状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptionsLog"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model.trim="queryParamsLog.dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryLog">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQueryLog">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDeleteLog"
              v-hasPermi="['schedule:backupLog:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="success" icon="el-icon-download" @click="handleExportLog">导出</el-button>
          </el-col>
        </el-row>

        <el-table
          border
          v-loading="loading"
          :data="synctofileLogList"
          row-key="id"
          @sort-change="sortChangeLog"
          @selection-change="handleSelectionChangeLog"
          ref="singleTable"
          highlight-current-row
          @current-change="handleCurrentChangeLog"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
          <el-table-column label="运行地址" prop="executorAddress" width="180" />
          <el-table-column label="状态" prop="handleCode" :formatter="statusFormatLog" width="80" />
          <el-table-column label="创建时间" prop="createTime" width="160" sortable="custom">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdateLog(scope.row)"
                v-hasPermi="['schedule:backupLog:query']"
              >查看</el-button>

              <el-button
                size="mini"
                type="text"
                icon="el-icon-download"
                @click="handleDownloadLog(scope.row)"
              >导出</el-button>

              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDeleteLog(scope.row)"
                v-hasPermi="['schedule:backupLog:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>


    <pagination
      v-show="pagination1>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <pagination
      v-show="pagination2>0"
      :total="totalLog"
      :page.sync="queryParamsLog.pageNum"
      :limit.sync="queryParamsLog.pageSize"
      @pagination="getListLog"
    />
    <!--<pagination-->
      <!--v-show="total>0"-->
      <!--:total="total"-->
      <!--:page.sync="queryParamsLog.pageNum"-->
      <!--:limit.sync="queryParamsLog.pageSize"-->
      <!--@pagination="getListLog"-->
    <!--/>-->

    <!-- 添加或修改同步配置对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openDialog"
      width="800px"
      top="5vh"
    >
      <handleDialog @cancelHandle="cancelHandle" v-if="openDialog" :handleObj="handleObj"></handleDialog>
    </el-dialog>
    <!-- 添加或修改同步配置对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      width="820px"
      v-dialogDrag
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="logDetailBox">
        <el-row>
          <el-col :span="24">
            <el-form-item label="资料名称" prop="profileName">
              <el-input v-model.trim="form.profileName" />
            </el-form-item>
          </el-col>
          <!--<el-col :span="24">-->
            <!--<el-form-item label="近时备份条件" prop="conditions">-->
              <!--<el-input v-model.trim="form.conditions" placeholder="请输入近时备份条件" />-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <!--<el-col :span="24">-->
            <!--<el-form-item label="远时备份条件" prop="secondConditions">-->
              <!--<el-input v-model.trim="form.secondConditions" placeholder="请输入远时备份条件" />-->
            <!--</el-form-item>-->
          <!--</el-col>-->
          <el-col :span="24">
            <el-form-item label="存储目录" prop="storageDirectory">
              <el-input v-model.trim="form.storageDirectory" placeholder="请输入远时备份条件" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model.trim="form.tableName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="四级编码" prop="ddataId">
              <el-input v-model.trim="form.ddataId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行地址" prop="executorAddress">
              <el-input v-model.trim="form.executorAddress" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行耗时" prop="elapsedTime" class="unitInput">
              <el-input v-model.trim="form.elapsedTime" />s
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应触发时间" prop="triggerTime">
              <el-input v-model.trim="form.triggerTime" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际触发时间" prop="handleTime">
              <el-input v-model.trim="form.handleTime" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行过程">
              <el-input v-model.trim="form.handleMsg" type="textarea"></el-input>
              <el-button type="primary" size="small" @click="showAllDetailLog">显示全部</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelLog">取 消</el-button>
      </div>
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
import {
  listSynctofile,
  getSynctofile,
  addSynctofile,
  updateSynctofile,
  delSynctofile,
  findAllDataBase,
  getByDatabaseId,
  getByDatabaseIdAndClassId,
  startSynctofile,
  stopSynctofile,
  executeBackup,
  exportSynctofile
} from "@/api/schedule/syncAll/syncToFile/syncToFile";
import {
  listSynctofileLog,
  getSynctofileLog,
  delSynctofileLog,
  exportTableLog,
  downFile
} from "@/api/schedule/syncAll/syncToFile/syncToFileLog";
import { formatDate } from "@/utils/index";
import handleDialog from "@/views/schedule/syncAll/syncToFile/handleDialog";

export default {
  components: {
    handleDialog
  },
  data() {
    return {
      activeName:"first",
      pagination1:true,
      pagination2:false,
      allDetailDialog: false,
      allDetailMsg: "",
      handleExportObj: {},
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      profileNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      totalLog:0,
      // 同步表格数据
      synctofileList: [],
      // 同步日志表格数据
      synctofileLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDialog: false,
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 状态数据字典
      statusOptionsLog: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        profileName: undefined,
        dataClassId: undefined,
        triggerStatus: undefined,
        tableName: undefined,
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      // 查询参数
      queryParamsLog:{
        pageNum: 1,
        pageSize: 10,
        profileName: undefined,
        dataClassId: undefined,
        handleCode: undefined,
        tableName: undefined,
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      handleObj: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      currentRow: null
    };
  },
  created() {
    this.getList();
    this.getListLog();
    this.getDicts("job_handle_status").then(response => {
      this.statusOptionsLog = response.data;
    });
    this.getDicts("job_trigger_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {

    changeTabs(data){
      // console.log(data)
      let name = data.name
      if(name == 'first'){
        this.pagination1 = true;
        this.pagination2 = false;
      }else  if(name == 'second'){
        this.pagination1 = false;
        this.pagination2 = true;
      }
    },

    handleCurrentChangeLog(val) {
      this.currentRow = val;
    },
    showAllDetailLog() {
      this.allDetailMsg = this.form.handleMsg;
      this.allDetailDialog = true;
    },
    sortChangeLog(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.queryParamsLog.params.orderBy = orderBy;
      this.handleQueryLog();
    },
    /** 查询字典类型列表 */
    getListLog() {
      if (this.queryParamsLog.dateRange) {
        this.dateRange = this.queryParamsLog.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      listSynctofileLog(this.addDateRange(this.queryParamsLog, this.dateRange)).then(
        response => {
          this.synctofileLogList = response.data.pageData;
          this.totalLog = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.synctofileLogList.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.synctofileLogList[index]);
              }
            });
          }
        }
      );
    },
    // 字典状态字典翻译
    statusFormatLog(row, column) {
      return this.selectDictLabel(this.statusOptionsLog, row.handleCode);
    },
    // 取消按钮
    cancelLog() {
      this.open = false;
      this.resetLog();
    },
    // 表单重置
    resetLog() {
      this.form = {
        id: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQueryLog() {
      this.queryParamsLog.pageNum = 1;
      this.getListLog();
    },
    /** 重置按钮操作 */
    resetQueryLog() {
      this.dateRange = [];
      this.resetForm("queryFormLog");
      this.handleQueryLog();
    },
    // 多选框选中数据
    handleSelectionChangeLog(selection) {
      this.ids = selection.map(item => item.id);
      this.profileNames = selection.map(item => item.profileName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdateLog(row) {
      this.resetLog();
      const id = row.id || this.ids;
      getSynctofileLog(id).then(response => {
        this.form = response.data;
        this.form.triggerTime = formatDate(this.form.triggerTime);
        this.form.handleTime = formatDate(this.form.handleTime);
        this.open = true;
        this.title = "查看数据同步日志信息";
      });
    },
    /** 删除按钮操作 */
    handleDeleteLog(row) {
      const ids = row.id || this.ids;
      const profileNames = row.profileName || this.profileNames;
      this.$confirm(
        '是否确认删除任务日志编号为"' + profileNames + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delSynctofileLog(ids);
        })
        .then(() => {
          this.getListLog();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    // 单个下载
    handleDownloadLog(row) {
      let obj = {};
      obj.path = row.storageDirectory + "/" + row.fileName;
      downFile(obj).then(res => {
        this.downloadfileCommon(res);
      });
    },
    //导出
    handleExportLog() {
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "下载中，请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      exportTableLog(this.queryParamsLog).then(res => {
        this.downloadfileCommon(res);
        loading.close();
      });
    },

    handleCurrentChange(val) {
      this.currentRow = val;
    },
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
    handleExport() {
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "下载中，请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      exportSynctofile(this.queryParams).then(response => {
        this.downloadfileCommon(response);
        loading.close();
      });
    },
    /** 查询字典类型列表 */
    getList() {
      if (this.queryParams.dateRange) {
        this.dateRange = this.queryParams.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      listSynctofile(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.synctofileList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.synctofileList.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.synctofileList[index]);
              }
            });
          }
        }
      );
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.triggerStatus);
    },
    // 取消按钮
    cancelHandle() {
      this.openDialog = false;
      this.getList();
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.handleObj = {};
      this.openDialog = true;
      this.title = "添加数据同步配置信息";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.profileNames = selection.map(item => item.profileName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids;
      this.handleObj.id = id;
      this.openDialog = true;
      this.title = "修改数据同步配置信息";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const profileNames = row.profileName || this.profileNames;
      this.$confirm('是否确认删除"' + profileNames + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delSynctofile(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    handleStop(row) {
      const id = row.id;
      this.$confirm('是否确认停止"' + row.profileName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return stopSynctofile(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("停止成功");
        })
        .catch(function() {});
    },
    handleStart(row) {
      const id = row.id;
      this.$confirm('是否确认启动"' + row.profileName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return startSynctofile(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("启动成功");
        })
        .catch(function() {});
    },
    handleExecute(row) {
      const id = row.id;
      this.$confirm(
        '是否立即执行任务编号为"' + row.profileName + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return startSynctofile(id);
        })
        .then(response => {
          console.log(response)
          this.getList();
          if(undefined!=response&&null!=response.msg){
            this.msgSuccess(response.msg);
          }else {
            this.msgSuccess("数据任务提交成功");
          }

        })
        .catch(function() {});
    },
    // 查询日志
    queryDaily(row) {
      this.activeName = 'second';
      this.queryParamsLog.profileName = row.profileName;
      this.handleQueryLog();
    }
  }
};
</script>
<style lang="scss">
  .allDetailMsg {
    .el-textarea__inner {
      min-height: 300px !important;
    }
  }
  .logDetailBox {
    .el-textarea {
      width: 80%;
    }
  }
</style>
