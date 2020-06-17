<template>
  <div class="app-container">
    <!-- 云数据库审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="审核状态">
        <el-select
          v-model.trim="queryParams.examineStatus"
          placeholder="审核状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申请用户">
        <el-input size="small" v-model.trim="queryParams.userName" placeholder="申请用户"></el-input>
      </el-form-item>
      <el-form-item label="数据库名">
        <el-input size="small" v-model.trim="queryParams.databaseName" placeholder="数据库名"></el-input>
      </el-form-item>
      <el-form-item label="申请时间">
        <el-date-picker
          size="small"
          v-model.trim="queryParams.dateRange"
          type="datetimerange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
        >></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cloudDBaudit:role:add']"
        >申请云数据库</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="userName" label="申请用户" width="120px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="department" label="申请单位" width="120px"></el-table-column>
      <el-table-column prop="updateTime" label="申请时间" width="160px" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="examineTime" label="审核时间" width="160px">
        <template slot-scope="scope">
          <span v-if="scope.row.examineTime">{{ parseTime(scope.row.examineTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="storageLogic" label="数据库类型" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.storageLogic == 'file'">网络共享存储</span>
          <span v-else-if="scope.row.storageLogic == 'database'">XUGU</span>
          <span v-else>{{ scope.row.storageLogic }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="databaseName" label="数据库名" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="databaseUse" label="用 途" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="examineStatus" label="审核状态" width="80px" :formatter="statusShow"></el-table-column>
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.examineStatus=='01'"
            type="text"
            size="mini"
            icon="el-icon-coordinate"
            @click="viewCell(scope.row)"
          >审核</el-button>
          <el-button
            v-if="scope.row.examineStatus=='04'"
            type="text"
            size="mini"
            icon="el-icon-finished"
            @click="viewCell(scope.row)"
          >释放</el-button>
          <el-button
            v-if="scope.row.examineStatus!='01' && scope.row.examineStatus!='04'"
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="viewCell(scope.row,'view')"
          >查看</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCell(scope.row)">删除</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-marketing"
            @click="analysisCell(scope.row)"
          >资源分析</el-button>
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
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
      v-dialogDrag
    >
      <handleAccount
        v-if="handleDialog"
        :handleObj="handleObj"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  cldbApplicationAll,
  deleteExamine,
  getById
} from "@/api/authorityAudit/cloudDBaudit";

import handleAccount from "@/views/authorityAudit/cloudDBaudit/handleCloudDB";
export default {
  components: {
    handleAccount
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examineStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        userName: "",
        databaseName: "",
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      dateRange: [],
      auditStatus: [
        {
          value: "01",
          label: "待审"
        },
        {
          value: "02",
          label: "已审"
        },
        {
          value: "03",
          label: "拒绝"
        },
        {
          value: "04",
          label: "申请释放"
        },
        {
          value: "05",
          label: "已释放"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      currentRow: null
    };
  },
  created() {
    this.getList();
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
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      if (this.queryParams.dateRange) {
        this.dateRange = this.queryParams.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      console.log(this.addDateRange(this.queryParams, this.dateRange));
      cldbApplicationAll(
        this.addDateRange(this.queryParams, this.dateRange)
      ).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "",
        userName: "",
        databaseName: ""
      };
      this.dateRange = [];
      this.handleQuery();
    },
    // 状态
    statusShow: function(row) {
      if (row.examineStatus == "01") {
        return "待审";
      } else if (row.examineStatus == "02") {
        return "已审";
      } else if (row.examineStatus == "03") {
        return "拒绝";
      } else if (row.examineStatus == "04") {
        return "申请释放";
      } else if (row.examineStatus == "05") {
        return "已释放";
      } else {
        return "-";
      }
    },
    handleAdd() {
      this.dialogTitle = "新增云数据库申请";
      this.handleObj = {};
      this.handleDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    // 资源分析
    analysisCell(row) {
      this.$message({
        type: "info",
        message: "资源分析暂无数据"
      });
    },
    viewCell(row, type) {
      getById({ id: row.id }).then(response => {
        if (type == "view") {
          this.dialogTitle = "云数据库详情";
        } else {
          this.dialogTitle = "云数据库审核";
        }

        this.handleObj = response.data;
        this.handleDialog = true;
      });
    },
    deleteCell(row) {
      this.$confirm("确定要删除这条数据吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteExamine({ id: row.id }).then(response => {
            if (response.code == 200) {
              this.msgSuccess("删除成功");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    }
  }
};
</script>
