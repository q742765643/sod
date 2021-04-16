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
      <el-form-item label="状态" prop="handleCode">
        <el-select
          v-model.trim="queryParams.handleCode"
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
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:backupLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="backupLogList"
      row-key="id"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="资料名称" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="运行地址" prop="executorAddress" width="180" />
      <el-table-column label="状态" prop="handleCode" :formatter="statusFormat" width="80" />
      <el-table-column label="创建时间" prop="createTime" width="160" sortable="custom">
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

          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >导出</el-button>

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
          <el-col :span="24">
            <el-form-item label="近时备份条件" prop="conditions">
              <el-input v-model.trim="form.conditions" placeholder="请输入近时备份条件" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="远时备份条件" prop="secondConditions">
              <el-input v-model.trim="form.secondConditions" placeholder="请输入远时备份条件" />
            </el-form-item>
          </el-col>
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
              <el-button type="primary" size="small" @click="showAllDetail">显示全部</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
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
  listBackupLog,
  getBackupLog,
  delBackupLog,
  exportTable,
  downFile
} from "@/api/schedule/backup/backupLog";
import { formatDate } from "@/utils/index";

export default {
  data() {
    return {
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
        profileName: this.$route.params.profileName,
        dataClassId: undefined,
        handleCode: undefined,
        tableName: undefined,
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      currentRow: null
    };
  },
  created() {
    this.getList();
    this.getDicts("job_handle_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    showAllDetail() {
      this.allDetailMsg = this.form.handleMsg;
      this.allDetailDialog = true;
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
    /** 查询字典类型列表 */
    getList() {
      if (this.queryParams.dateRange) {
        this.dateRange = this.queryParams.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      listBackupLog(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.backupLogList = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.backupLogList.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.backupLogList[index]);
              }
            });
          }
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
      this.profileNames = selection.map(item => item.profileName);
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
          return delBackupLog(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    // 单个下载
    handleDownload(row) {
      let obj = {};
      obj.path = row.storageDirectory + "/" + row.fileName;
      downFile(obj).then(res => {
        this.downloadfileCommon(res);
      });
    },
    //导出
    handleExport() {
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "下载中，请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      exportTable(this.queryParams).then(res => {
        this.downloadfileCommon(res);
        loading.close();
      });
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