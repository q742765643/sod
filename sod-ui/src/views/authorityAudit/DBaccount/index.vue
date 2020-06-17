<template>
  <div class="app-container">
    <!-- 数据库访问账户 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="审核状态" prop="examineStatus">
        <el-select
          v-model.trim="queryParams.examineStatus"
          placeholder="审核状态"
          clearable
          size="small"
          style="width: 240px"
          @change="handleQuery"
        >
          <el-option label="待审核" value="0"></el-option>
          <el-option label="审核未通过" value="2"></el-option>
          <el-option label="审核通过" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账户ID" prop="databaseUpId">
        <el-input v-model.trim="queryParams.databaseUpId" size="small"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          size="small"
          type="primary"
          @click="addCell"
          icon="el-icon-plus"
          v-hasPermi="['DBaccount:role:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" @click="handleExport" icon="el-icon-download">导出</el-button>
      </el-col>
    </el-row>
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="databaseUpId" label="账户ID" width="180" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="userName" label="关联用户" width="120px"></el-table-column>
      <el-table-column prop="deptName" label="机构" width="140px"></el-table-column>
      <el-table-column prop="phonenumber" label="联系方式" width="120px"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160px" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="applyDatabaseName" label="可用数据库" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="examineStatus" label="状态" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.examineStatus=='0'">待审核</span>
          <el-link
            type="primary"
            :underline="false"
            v-if="scope.row.examineStatus=='2'"
            @click="viewReason(scope.row)"
          >审核未通过</el-link>
          <span v-if="scope.row.examineStatus=='1'">审核通过</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width" width="160">
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
      :before-close="handleDialogClose"
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
  databaseUserAll,
  deleteList,
  download
} from "@/api/authorityAudit/DBaccount";
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
        examineStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },

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
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
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
    addCell() {
      this.dialogTitle = "新增数据库账户";
      this.handleObj = {};
      this.handleDialog = true;
    },
    //查看原因
    viewReason(row) {
      this.$alert(row.failureReason, "拒绝原因", {
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
    //导出
    handleExport() {
      download(this.queryParams).then(res => {
        this.downloadfileCommon(res);
      });
    },
    handleDialogClose(value) {
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
<style scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>
