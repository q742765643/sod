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
          v-hasPermi="['schedule:clear:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['schedule:clear:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:clear:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="clearList"
      row-key="id"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="执行策略" prop="jobCron" :show-overflow-tooltip="true" width="180" />
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
            v-hasPermi="['schedule:clear:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:clear:remove']"
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
          <el-button
            size="mini"
            type="text"
            icon="el-icon-tickets"
            @click="queryDaily(scope.row)"
          >查询日志</el-button>
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

    <!-- 添加或修改清除配置对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openDialog"
      width="800px"
      v-dialogDrag
    >
      <handleClear @cancelHandle="cancelHandle" v-if="openDialog" :handleObj="handleObj"></handleClear>
    </el-dialog>
  </div>
</template>

<script>
import {
  listClear,
  getClear,
  addClear,
  updateClear,
  delClear,
  findAllDataBase,
  getByDatabaseId,
  getByDatabaseIdAndClassId,
  startClear,
  stopClear,
  executeClear,
  exportTable
} from "@/api/schedule/clear/clear";
import handleClear from "@/views/schedule/clear/handleClear";

export default {
  components: {
    handleClear
  },
  data() {
    return {
      handleObj: {},
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
      // 清除表格数据
      clearList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDialog: false,
      // 状态数据字典
      statusOptions: [],

      alarmOptions: [],

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
        tableName: undefined,
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("job_trigger_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
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
    // 导出
    handleExport() {
      exportTable(this.queryParams).then(res => {
        this.downloadfileCommon(res);
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
      listClear(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.clearList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.clearList.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.clearList[index]);
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
      this.getList();
      this.openDialog = false;
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
      this.openDialog = true;
      this.handleObj = {};
      this.title = "添加数据清除配置信息";
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
      this.title = "修改数据清除配置信息";
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
          return delClear(ids);
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
          return stopClear(id);
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
          return startClear(id);
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
          return executeClear(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("数据任务提交成功");
        })
        .catch(function() {});
    },
    // 查询日志
    queryDaily(row) {
      this.$router.push({
        name: "数据清除日志",
        params: { profileName: row.profileName }
      });
    }
  }
};
</script>
