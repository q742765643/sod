<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
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

    <el-row :gutter="10" class="mb8">
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
          type="success"
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
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="backupList"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="资料名称"
        align="center"
        prop="profileName"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="执行策略" align="center" prop="jobCron" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="triggerStatus" :formatter="statusFormat" />
      <el-table-column label="任务描述" align="center" prop="jobDesc" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center" class-name="small-padding fixed-width">
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
    <el-dialog :title="title" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="物理库" prop="databaseId">
              <el-select
                v-model="form.databaseId"
                filterable
                @change="selectByDatabaseIds($event,'')"
                placeholder="请选择物理库"
                style="width:100%"
              >
                <el-option
                  v-for="database in databaseOptions"
                  :key="database.KEY"
                  :label="database.VALUE"
                  :value="database.KEY"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资料名称" prop="dataClassId">
              <el-select
                v-model="form.dataClassId"
                filterable
                @change="selectTable"
                placeholder="请选择资料"
                style="width:100%"
              >
                <el-option
                  v-for="dataClass in dataClassIdOptions"
                  :key="dataClass.KEY"
                  :label="dataClass.VALUE"
                  :value="dataClass.KEY"
                ></el-option>
              </el-select>
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
              <el-select
                v-model="form.storageDirectory"
                placeholder="请选择"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="dict in storageDirectoryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model="form.tableName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="四级编码" prop="ddataId">
              <el-input v-model="form.ddataId" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略" prop="jobCron">
              <el-input v-model="form.jobCron" placeholder="请输入执行策略" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否告警" prop="isAlarm">
              <el-radio-group v-model="form.isAlarm">
                <el-radio
                  v-for="dict in alarmOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间" prop="executorTimeout">
              <el-input v-model="form.executorTimeout" placeholder="请输入超时时间单位为分钟" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失败重试次数" prop="executorFailRetryCount">
              <el-input v-model="form.executorFailRetryCount" placeholder="请输入失败重试次数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重试间隔时间" prop="retryInterval">
              <el-input v-model="form.retryInterval" placeholder="请输入重试间隔时间单位为分钟" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.jobDesc" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
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
  executeBackup
} from "@/api/schedule/backup/backup";

export default {
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
      open: false,
      // 状态数据字典
      statusOptions: [],

      alarmOptions: [],

      storageDirectoryOptions: [],

      databaseOptions: [],

      dataClassIdOptions: [],

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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        databaseId: [
          { required: true, message: "物理库不能为空", trigger: "blur" }
        ],
        dataClassId: [
          { required: true, message: "资料名称不能为空", trigger: "blur" }
        ],
        storageDirectory: [
          { required: true, message: "存储目录不能为空", trigger: "blur" }
        ],
        jobCron: [
          { required: true, message: "执行策略不能为空", trigger: "blur" }
        ],
        executorTimeout: [
          { required: true, message: "超时时间不能为空", trigger: "blur" }
        ],
        executorFailRetryCount: [
          { required: true, message: "重试次数不能为空", trigger: "blur" }
        ],
        retryInterval: [
          { required: true, message: "重试间隔时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("job_trigger_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("job_is_alarm").then(response => {
      this.alarmOptions = response.data;
    });
    this.getDicts("backup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
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
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        profileName: undefined,
        dataClassId: undefined,
        triggerStatus: undefined,
        isAlarm: "1",
        triggerStatus: 1,
        tableName: undefined,
        vtableName: undefined,
        ddataId: undefined
      };
      this.dataClassIdOptions = [];
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      findAllDataBase().then(response => {
        this.databaseOptions = response.data;
      });
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
      this.reset();
      findAllDataBase().then(response => {
        this.databaseOptions = response.data;
      });
      const id = row.id || this.ids;
      getBackup(id).then(response => {
        this.selectByDatabaseIds(
          response.data.databaseId,
          response.data.dataClassId
        );
        this.form = response.data;
        this.open = true;
        this.title = "修改数据备份配置信息";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateBackup(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addBackup(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
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
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有类型数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return exportType(queryParams);
        })
        .then(response => {
          this.download(response.msg);
        })
        .catch(function() {});
    },
    selectByDatabaseIds(databaseId,dataClassId) {
      getByDatabaseId(databaseId,dataClassId).then(response => {
        this.dataClassIdOptions = response.data;
        this.form.dataClassId = dataClassId;
      });
    },
    selectTable(dataClassId) {
      this.form.tableName = "";
      this.findTable( this.form.databaseId, this.form.dataClassId);
    },
    findTable(databaseId, dataClassId) {
      getByDatabaseIdAndClassId(databaseId, dataClassId).then(response => {
        this.form.ddataId=response.data.ddataId;
        this.form.tableName = response.data.tableName;
      });
    }
  }
};
</script>
