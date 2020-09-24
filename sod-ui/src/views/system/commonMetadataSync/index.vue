<template>
  <div class="app-container commonMetadataTem">
    <!-- 公共元数据同步 -->
    <el-tabs v-model.trim="activeName" @tab-click="handleClick">
      <el-tab-pane label="同步任务配置" name="first">
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button
              size="small"
              type="primary"
              v-hasPermi="['system:commonMetadataSync:add']"
              @click="showAddDialog()"
              icon="el-icon-plus"
            >新增同步任务</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              size="small"
              type="success"
              v-hasPermi="['system:commonMetadataSync:add']"
              @click="syncCommonMetadata()"
              icon="el-icon-set-up"
            >立即同步元数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              size="small"
              type="danger"
              v-hasPermi="['system:commonMetadataSync:add']"
              @click="deleteShow()"
              icon="el-icon-delete"
            >删除同步任务</el-button>
          </el-col>
        </el-row>
        <el-table
          border
          v-loading="loading"
          :data="tableData"
          row-key="id"
          highlight-current-row
          @current-change="handleCurrentChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
          <el-table-column prop="taskName" sortable label="任务名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="tableName" sortable label="表名" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="apiUrl" label="接口url" width="350px" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="apiType" label="同步类型" :formatter="statusFormat" width="100"></el-table-column>
          <el-table-column prop="apiDataKey" label="接口关键字"></el-table-column>
          <el-table-column prop="jobCron" label="执行策略"></el-table-column>
          <el-table-column prop="description" label="来源" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-edit"
                @click="showEditDialog(scope.row)"
              >修改</el-button>
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
          @pagination="getList"
        />
      </el-tab-pane>
      <el-tab-pane label="同步记录查询" name="second">
        <el-form :model="rowlogForm" ref="rowlogFormRef" :inline="true" class="searchBox">
          <el-form-item label="同步表名" prop="syncTableName">
            <el-select size="small" filterable v-model.trim="rowlogForm.syncTableName">
              <el-option
                v-for="(item,index) in tableNames"
                :key="index"
                :label="item.dictValue"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运行状态" prop="runState">
            <el-select filterable style="width:150px" v-model.trim="rowlogForm.runState">
              <el-option value="同步成功" label="同步成功"></el-option>
              <el-option value="同步失败" label="同步失败"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围" prop="dateRange">
            <el-date-picker
              style="width:300px"
              v-model="rowlogForm.dateRange"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              size="small"
              type="primary"
              @click="dataBaseExpandForm"
              icon="el-icon-search"
            >查询</el-button>
            <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              v-hasPermi="['system:commonMetadataSync:delete']"
              @click="deleteRecord"
            >删除</el-button>
          </el-col>
        </el-row>
        <el-table
          border
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stopTime" label="结束时间" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.stopTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="syncTableName" label="同步表名" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="syncRecordNum" label="同步记录数" width="100"></el-table-column>
          <el-table-column prop="syncType" label="同步类型" width="100">
            <!-- <template slot-scope="scope">
              <span v-if="scope.row.syncType == 1">全量同步</span>
              <span v-if="scope.row.syncType == 2">增量同步</span>
            </template>-->
          </el-table-column>
          <el-table-column prop="syncModel" label="同步方式" width="100"></el-table-column>
          <el-table-column prop="runState" label="运行状态" width="100"></el-table-column>
        </el-table>
        <pagination
          v-show="logDataTotal>0"
          :total="logDataTotal"
          :page.sync="rowlogForm.pageNum"
          :limit.sync="rowlogForm.pageSize"
          @pagination="dataBaseExpandForm"
        />
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="650px"
    >
      <handleSod @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleSod>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="同步详情"
      :visible.sync="syncDescDialogVisible"
      v-if="syncDescDialogVisible"
    >
      <div v-for="(item,index) in syncDescList" :key="index">
        <div v-if="item.runState=='同步成功'">
          <span>同步表名:{{item.syncTableName}}</span>
          <span>同步记录数:{{item.syncRecordNum}}</span>
        </div>
        <div v-else>
          <span>同步表名:{{item.syncTableName}}</span>
          <span>同步状态:{{item.runState}}</span>
          <span>失败原因:{{item.failReason}}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancelHandle()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  pageList,
  recordPage,
  delRecord,
  delConfig,
  syncDataNow,
} from "@/api/system/commonMetadataSync/index";
import { getDictList } from "@/api/system/commonMetadataSync/select";
//增加查看弹框
import handleSod from "@/views/system/commonMetadataSync/handleTask";
export default {
  components: {
    handleSod,
  },
  data() {
    return {
      activeName: "first",
      // 遮罩层
      loading: true,
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
      syncDescDialogVisible: false,
      syncDescList: [],
      currentRow: null,
      //同步记录查询
      dateRange: [],
      loadingHis: false,
      rowlogForm: {
        pageNum: 1,
        pageSize: 10,
        syncTableName: "",
        runState: "",
      },
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      tableNames: [],
    };
  },
  created() {
    this.getList();
    this.getTableNames();
  },
  methods: {
    getTableNames() {
      getDictList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.tableNames = res.data;
        }
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
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      pageList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    showAddDialog() {
      this.handleObj = {};
      this.handleDialog = true;
      this.dialogTitle = "新增同步任务";
    },
    syncCommonMetadata() {
      let ids = [];
      let apiType = [];
      if (!this.currentRow) {
        this.$message({ type: "info", message: "请选择一行数据" });
        return;
      }
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "执行同步任务中,请勿有其他操作！",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });

      syncDataNow({
        ids: this.currentRow.id,
        apiType: this.currentRow.apiType,
      }).then((res) => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "同步成功",
          });
          loading.close(); //关闭延迟加载
          this.syncDescList = res.data;
          this.syncDescDialogVisible = true;
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
          loading.close(); //关闭延迟加载
        }
      });
    },
    deleteShow() {
      if (!this.currentRow) {
        this.$message({ type: "info", message: "请选择一行数据" });
        return;
      }

      this.$confirm("数据删除后将无法恢复，确认删除?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delConfig({ ids: this.currentRow.id }).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.handleQuery();
            } else {
              this.$message({
                type: "error",
                message: "删除失败",
              });
            }
          });
        })
        .catch(() => {});
    },
    handleClick(tab, event) {
      if (this.activeName == "first") {
        this.handleQuery();
      }
      if (this.activeName == "second") {
        this.dataBaseExpandForm();
      }
    },

    statusFormat(row, column, value) {
      if (value == 1) {
        return "全量同步";
      } else if (value == 2) {
        return "增量同步";
      } else {
        return value;
      }
    },
    showEditDialog(row) {
      this.handleObj = row;
      this.handleDialog = true;
      this.dialogTitle = "编辑同步任务";
    },
    viewDaily(row) {
      this.activeName = "second";
      this.rowlogForm.syncTableName = row.tableName;
      this.rowlogForm.dateRange = [];
      this.dateRange = [];
      this.rowlogForm.pageNum = 1;
      this.dataBaseExpandForm();
    },
    // 删除记录
    deleteRecord() {
      let ids = [];
      if (this.currentRow.length == 0) {
        this.$message({ type: "error", message: "请选择一行数据" });
        return;
      }
      for (var row in this.currentRow) {
        ids.push(this.currentRow[row].id);
      }
      console.log(ids);
      this.$confirm("数据删除后将无法恢复，确认删除?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delRecord({ ids: ids.join(",") }).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.resetQuery();
            } else {
              this.$message({
                type: "error",
                message: "删除失败",
              });
            }
          });
        })
        .catch(() => {});
    },
    //选中行
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    handleHistorySelectionChange(val) {
      this.currentRow = val;
    },
    // 弹框取消
    cancelHandle() {
      this.handleObj = {};
      this.currentRow = null;
      this.syncDescList = [];
      this.syncDescDialogVisible = false;
      this.handleDialog = false;
      this.getList();
    },
    //
    resetQuery() {
      this.dateRange = [];
      this.rowlogForm.pageNum = 1;
      this.dataBaseExpandForm();
      this.$refs["rowlogFormRef"].resetFields();
    },
    dataBaseExpandForm() {
      this.loadingHis = true;
      if (this.rowlogForm.dateRange) {
        this.dateRange = this.rowlogForm.dateRange;
      } else {
        this.dateRange = [];
      }
      recordPage(this.addDateRange(this.rowlogForm, this.dateRange)).then(
        (response) => {
          this.logTableData = response.data.pageData;
          this.logDataTotal = response.data.totalCount;
          this.loadingHis = false;
        }
      );
    },
  },
};
</script>
<style lang="scss">
.commonMetadataTem {
  .el-tabs__content {
    padding-bottom: 20px;
  }
  #pane-first {
    .handleTableBox {
      margin: 8px 0 24px 0;
    }
  }
  #pane-second {
    .searchBox {
      margin-top: 15px;
    }
  }
}
</style>

