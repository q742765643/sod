<template>
  <div class="app-container">
    <!-- 一致性检查 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="专题库名称：">
        <el-input size="small" v-model="queryParams.database_name" placeholder="输入关键字搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          size="small"
          v-hasPermi="['consistencyCheck:role:creat']"
          type="primary"
          icon="el-icon-document"
          @click="progressDownLoad()"
        >生成差异报告</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          size="small"
          v-hasPermi="['consistencyCheck:role:add']"
          type="primary"
          icon="el-icon-plus"
          @click="addReportData()"
        >添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          size="small"
          v-hasPermi="['consistencyCheck:role:delete']"
          type="danger"
          icon="el-icon-delete"
          @click="deleteData()"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @current-change="handleCurrentChange"
    >
      <el-table-column align="center" type="index" min-width="20" label=" " :index="table_index"></el-table-column>
      <el-table-column align="center" prop="database_name" label="专题库名称" sortable min-width="200"></el-table-column>
      <el-table-column align="center" prop="database_type" label="数据库类型" sortable min-width="200"></el-table-column>
      <el-table-column
        align="center"
        prop="database_instance"
        label="专题库实例"
        sortable
        min-width="100"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="database_schema_name"
        label="专题名ID"
        sortable
        min-width="100"
      ></el-table-column>
      <el-table-column align="center" prop="database_desc" label="专题库描述" sortable min-width="120"></el-table-column>
      <el-table-column align="center" prop="address" label="操作" min-width="100">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-tickets"
            type="text"
            @click="getHistoricalReport(scope.row)"
          >历史报告列表</el-button>
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
    <el-dialog title="添加" :visible.sync="addDataDialog" width="40%" v-dialogDrag>
      <el-form>
        <el-form-item label="数据库选择:">
          <el-select style="width:80%;" v-model="addOptionValue" placeholder="请选择添加数据库">
            <el-option
              v-for="item in addoptions"
              :key="item.database_name"
              :label="item.database_name"
              :value="item.database_id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addReportRecord()">确 定</el-button>
        <el-button @click="addDataDialog= false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="历史报告表" :visible.sync="historyDialog" width="80%" height="50%" v-dialogDrag>
      <el-table ref="singleTable" :data="historyData" highlight-current-row border stripe>
        <el-table-column
          align="center"
          type="index"
          min-width="20"
          label=" "
          :index="table_index_history"
        ></el-table-column>
        <el-table-column align="center" prop="database_id" label="检查ID" sortable min-width="200"></el-table-column>
        <el-table-column align="center" prop="file_name" label="报告名称" sortable min-width="300"></el-table-column>
        <el-table-column align="center" prop="create_time" label="生成时间" sortable min-width="200"></el-table-column>
        <el-table-column align="center" prop="address" label="操作" min-width="100">
          <template slot-scope="scope1">
            <el-button
              icon="el-icon-tickets"
              type="text"
              @click="downloadHistoricalReport(scope1.row)"
            >下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="historyTotal>0"
        :total="historyTotal"
        :page.sync="historyObj.pageNum"
        :limit.sync="historyObj.pageSize"
        @pagination="getHistoryData"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="historyDialog=false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  exportRole,
  dataScope,
  changeRoleStatus
} from "@/api/system/role";
export default {
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        database_name: ""
      },
      total: 0,
      tableData: [],
      addOptionValue: "",
      // 添加弹窗
      addDataDialog: false,
      addoptions: [],
      //  历史报告
      historyDialog: false,
      historyData: [],
      historyTotal: 0,
      historyObj: {
        pageNum: 1,
        pageSize: 10,
        historyID: ""
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    table_index_history(index) {
      return (
        (this.historyObj.pageNum - 1) * this.historyObj.pageSize + index + 1
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
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    /*点击历史报告列表事件 */
    getHistoricalReport(row) {
      this.historyDialog = true;
    },
    progressDownLoad() {},
    addReportData() {
      this.addDataDialog = true;
    },
    deleteData() {},
    addReportRecord() {},
    downloadHistoricalReport() {},
    getHistoryData() {}
  }
};
</script>
