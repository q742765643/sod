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
      <el-form-item label="状态" prop="handleCode">
        <el-select
          v-model="queryParams.handleCode"
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
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['schedule:backupLog:query']"
        >查看</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:backupLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <handleExport
          v-hasPermi="['system:dict:export']"
          :handleExportObj="queryParams"
          baseUrl="SCHEDULE"
          btnText="导出"
          exportUrl="/schedule/backupLog/export"
        />
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="backupLogList"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="运行地址" prop="executorAddress" width="180" />
      <el-table-column label="状态" prop="handleCode" :formatter="statusFormat" width="80" />
      <el-table-column label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['schedule:backupLog:query']"
          >查看</el-button>

          <handleExport
            @click="handleDownload(scope.row)"
            :handleExportObj="handleExportObj"
            baseUrl="SCHEDULE"
            btnText="下载"
            exportUrl="/api/schedule/uploadDown/downFile"
          />
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:backupLog:remove']"
          >删除</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="800px" v-dialogDrag>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="资料名称" prop="profileName">
              <el-input v-model="form.profileName" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="近时备份条件" prop="conditions">
              <el-input v-model="form.conditions" placeholder="请输入近时备份条件" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="远时备份条件" prop="secondConditions">
              <el-input v-model="form.secondConditions" placeholder="请输入远时备份条件" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="存储目录" prop="storageDirectory">
              <el-input v-model="form.storageDirectory" placeholder="请输入远时备份条件" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model="form.tableName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="四级编码" prop="ddataId">
              <el-input v-model="form.ddataId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行地址" prop="executorAddress">
              <el-input v-model="form.executorAddress" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行耗时" prop="elapsedTime">
              <el-input v-model="form.elapsedTime" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应触发时间" prop="triggerTime">
              <el-input v-model="form.triggerTime" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际触发时间" prop="handleTime">
              <el-input v-model="form.handleTime" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行过程">
              <el-input v-model="form.handleMsg" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import handleExport from "@/components/export";
import {
  listBackupLog,
  getBackupLog,
  delBackupLog
} from "@/api/schedule/backup/backupLog";
import { formatDate } from "@/utils/index";

export default {
  components: {
    handleExport
  },
  data() {
    return {
      handleExportObj: {},
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
      backupLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
        handleCode: undefined,
        tableName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getDicts("job_handle_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      listBackupLog(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.backupLogList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.handleCode);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getBackupLog(id).then(response => {
        this.form = response.data;
        this.form.triggerTime = formatDate(this.form.triggerTime);
        this.form.handleTime = formatDate(this.form.handleTime);
        this.open = true;
        this.title = "查看数据备份日志信息";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除任务日志编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delBackupLog(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    handleDownload(row) {
      this.handleExportObj = {};
      this.handleExportObj.path = row.storageDirectory + "/" + row.fileName;
    }
  }
};
</script>
