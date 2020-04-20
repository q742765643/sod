<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
      class="searchBox"
    >
      <el-form-item label="资料名称" prop="profileName">
        <el-input
          v-model="queryParams.profileName"
          placeholder="请输入物理库资料名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存储编码" prop="dataClassId">
        <el-input
          v-model="queryParams.dataClassId"
          placeholder="请输入存储编码或者四级编码"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="triggerStatus">
        <el-select
          v-model="queryParams.triggerStatus"
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
          v-model="dateRange"
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
        <handleExport
          v-hasPermi="['system:dict:export']"
          :handleExportObj="queryParams"
          baseUrl="SCHEDULE"
          btnText="导出"
          exportUrl="/schedule/backup/export"
        />
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="backupList"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="执行策略" prop="jobCron" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="状态" prop="triggerStatus" :formatter="statusFormat" width="80" />
      <el-table-column label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" class-name="small-padding fixed-width">
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
            icon="el-icon-delete"
            @click="handleStop(scope.row)"
            v-hasPermi="['schedule:job:stop']"
          >停止</el-button>
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.triggerStatus==0"
            icon="el-icon-delete"
            @click="handleStart(scope.row)"
            v-hasPermi="['schedule:job:start']"
          >启动</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleExecute(scope.row)"
            v-hasPermi="['schedule:job:execute']"
          >立即执行</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改备份配置对话框 -->
    <el-dialog :title="title" :visible.sync="openDialog" width="800px" v-dialogDrag>
      <handleBackUp @cancelHandle="cancelHandle" v-if="openDialog" :handleObj="handleObj"></handleBackUp>
    </el-dialog>
  </div>
</template>

<script>
import handleExport from "@/components/export";
import {
  listBackup,
  getBackup,
  addBackup,
  updateBackup,
  delBackup,
  findAllDataBase,
  getByDatabaseId,
  getByDatabaseIdAndClassId,
  startBackup,
  stopBackup,
  executeBackup,
  exportBackup
} from "@/api/schedule/backup/backup";
import handleBackUp from "@/views/schedule/backup/handleBackUp";

export default {
  components: {
    handleBackUp,
    handleExport
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 备份表格数据
      backupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDialog: false,
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        profileName: undefined,
        dataClassId: undefined,
        triggerStatus: undefined,
        tableName: undefined
      },
      handleObj: {}
    };
  },
  created() {
    this.getList();
    this.getDicts("job_trigger_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      listBackup(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.backupList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
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
      this.title = "添加数据备份配置信息";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids;
      this.handleObj.id = id;
      this.openDialog = true;
      this.title = "修改数据备份配置信息";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除任务编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delBackup(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    handleStop(row) {
      const id = row.id;
      this.$confirm('是否确认停止任务编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return stopBackup(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("停止成功");
        })
        .catch(function() {});
    },
    handleStart(row) {
      const id = row.id;
      this.$confirm('是否确认启动任务编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return startBackup(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("启动成功");
        })
        .catch(function() {});
    },
    handleExecute(row) {
      const id = row.id;
      this.$confirm('是否立即执行任务编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return executeBackup(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("执行成功");
        })
        .catch(function() {});
    }
  }
};
</script>
