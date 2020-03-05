<template>
  <div class="app-container">
    <!-- 用户角色审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="申请用户">
        <el-input size="small" v-model="queryParams.username" placeholder="申请用户"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option label="全部" value=" "></el-option>
          <el-option label="未审核" value="0"></el-option>
          <el-option label="已审核" value="1"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column align="center" prop="account" label="账户名称"></el-table-column>
      <el-table-column align="center" prop="username" label="用户名称"></el-table-column>
      <el-table-column align="center" prop="post" label="部门"></el-table-column>
      <el-table-column align="center" prop="updateTime" label="申请时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="status" label="审核状态">
        <template slot-scope="scope">
          <el-link
            icon="el-icon-circle-close"
            type="danger"
            :underline="false"
            v-if="scope.row.status==0"
          >未审核</el-link>
          <el-link
            icon="el-icon-circle-check"
            type="success"
            :underline="false"
            v-if="scope.row.status==1"
          >已审核</el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="260px">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status=='0'"
            type="text"
            size="mini"
            icon="el-icon-circle-check"
            @click="handleCell(scope.row,'通过',1)"
          >通过</el-button>
          <el-button
            v-if="scope.row.status == 1"
            type="text"
            size="mini"
            icon="el-icon-circle-close"
            @click="handleCell(scope.row,'拒绝',0)"
          >拒绝</el-button>
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
import { page, delByIds, update } from "@/api/authorityAudit/userRoleAudit";
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
        status: "",
        username: "",
        nameSourceDB: "",
        time: ["", ""]
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
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        status: "",
        username: "",
        nameSourceDB: "",
        time: ["", ""]
      };
      this.handleQuery();
    },
    handleAdd() {
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
    handleCell(row, type, status) {
      this.$confirm("是否确认" + type + "", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return update(row.id, status);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("操作成功");
        })
        .catch(function() {});
    },
    deleteCell(row) {
      this.$confirm("是否确认删除", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delByIds(row.id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
  }
};
</script>
