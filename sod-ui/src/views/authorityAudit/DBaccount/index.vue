<template>
  <div class="app-container">
    <!-- 数据库访问账户 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="审核状态" prop="examineStatus">
        <el-select
          v-model="queryParams.examineStatus"
          placeholder="审核状态"
          clearable
          size="small"
          style="width: 240px"
          @change="handleQuery"
        >
          <el-option label="待审核" value="0"></el-option>
          <el-option label="审核未通过" value="1"></el-option>
          <el-option label="审核通过" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          @click="addCell"
          icon="el-icon-plus"
          v-hasPermi="['DBaccount:role:add']"
        >新增</el-button>
        <el-button size="small" type="success" icon="el-icon-download" @click="downloadTable">导出</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column
        align="center"
        prop="databaseUpId"
        label="账户ID"
        width="120px"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column align="center" prop="userRealname" label="关联用户" width="100px"></el-table-column>
      <el-table-column align="center" prop="department" label="机构" width="140px"></el-table-column>
      <el-table-column align="center" prop="phoneNum" label="联系方式" width="120px"></el-table-column>
      <el-table-column
        align="center"
        prop="createTime"
        label="创建时间"
        width="160px"
        :formatter="createTimeFormater"
      ></el-table-column>
      <el-table-column align="center" prop="applyDatabaseName" label="可用数据库"></el-table-column>
      <el-table-column align="center" prop="examineStatus" label="状态" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.examineStatus=='0'">待审核</span>
          <el-link v-if="scope.row.examineStatus=='1'" @click="viewReason(scope.row)">审核未通过</el-link>
          <span v-if="scope.row.examineStatus=='2'">审核通过</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.examineStatus=='0'"
            type="text"
            size="mini"
            icon="el-icon-coordinate"
            @click="viewCell(scope.row)"
          >审核</el-button>
          <el-button
            v-else
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="viewCell(scope.row)"
          >查看</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCell(scope.row)">删除</el-button>
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
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
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
import { formatTime } from "@/components/commonVaildate.js";
import { databaseUserAll, deleteList } from "@/api/authorityAudit/DBaccount";
import handleAccount from "@/views/authorityAudit/DBaccount/handleAccount";
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
        examineStatus: ""
      },

      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false
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
    /** 搜索按钮操作 */
    handleQuery() {
      console.log(this.queryParams);
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      databaseUserAll(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    createTimeFormater: function(row) {
      return formatTime(row.createTime, "Y-M-D h:m:s");
    },
    addCell() {
      this.dialogTitle = "新增数据库账户审核";
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
    viewCell(row) {
      this.dialogTitle = "数据库账户审核";
      this.handleObj = row;
      this.handleDialog = true;
    },
    deleteCell(row) {
      this.$confirm("确认删除" + row.databaseUpId + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteList({ id: row.id }).then(res => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.handleQuery();
            }
          });
        })
        .catch(() => {});
    },
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
      this.handleQuery();
    }
  }
};
</script>
