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
          clearable
          v-model.trim="queryParams.profileName"
          placeholder="请输入物理库资料名称"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存储编码" prop="dataClassId">
        <el-input
          clearable
          v-model.trim="queryParams.dataClassId"
          placeholder="请输入存储编码或者四级编码"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
          clearable
          v-model.trim="queryParams.tableName"
          placeholder="请输入表名"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="triggerStatus">
        <el-select
          v-model.trim="queryParams.triggerStatus"
          placeholder="运行状态"
          moveable
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
          v-hasPermi="['schedule:move:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['schedule:move:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:move:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="moveList"
      row-key="id"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="执行策略" prop="jobCron" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="状态" prop="triggerStatus" width="80" :formatter="statusFormat" />
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
            v-hasPermi="['schedule:move:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:move:remove']"
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

    <!-- 添加或修改迁移配置对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openDialog"
      width="800px"
      v-dialogDrag
    >
      <handleMove @cancelHandle="cancelHandle" v-if="openDialog" :handleObj="handleObj"></handleMove>
    </el-dialog>
  </div>
</template>

<script>
import handleMove from "@/views/schedule/move/handleMove";
import {
  listMove,
  delMove,
  startMove,
  stopMove,
  executeMove,
  exportTable,
} from "@/api/schedule/move/move";

export default {
  components: {
    handleMove,
  },
  data() {
    return {
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
      // 迁移表格数据
      moveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openDialog: false,
      // 状态数据字典
      statusOptions: [],

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
        tableName: undefined,
        params: {
          orderBy: {
            createTime: "desc",
          },
        },
      },
      handleObj: {},
      currentRow: null,
    };
  },
  created() {
    this.getList();
    this.getDicts("job_trigger_status").then((response) => {
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
      exportTable(this.queryParams).then((res) => {
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
      listMove(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.moveList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.moveList.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.moveList[index]);
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
      if (this.title.indexOf("添加") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
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
      this.handleObj = {};
      this.openDialog = true;
      this.title = "添加数据迁移配置信息";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.profileNames = selection.map((item) => item.profileName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids;
      this.handleObj.id = id;
      this.openDialog = true;
      this.title = "修改数据迁移配置信息";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const profileNames = row.profileName || this.profileNames;
      this.$confirm(
        '是否确认删除任务编号为"' + profileNames + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delMove(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    handleStop(row) {
      const id = row.id;
      this.$confirm('是否确认停止"' + row.profileName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return stopMove(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("停止成功");
        })
        .catch(function () {});
    },
    handleStart(row) {
      const id = row.id;
      this.$confirm('是否确认启动"' + row.profileName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return startMove(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("启动成功");
        })
        .catch(function () {});
    },
    handleExecute(row) {
      const id = row.id;
      this.$confirm(
        '是否立即执行任务编号为"' + row.profileName + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return executeMove(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("数据任务提交成功");
        })
        .catch(function () {});
    },
    // 查询日志
    queryDaily(row) {
      this.$router.push({
        name: "数据迁移日志",
        params: { profileName: row.profileName },
      });
    },
  },
};
</script>
