<template>
  <div class="app-container">
    <!-- 业务用户审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="用户名称">
        <el-input size="small" v-model="queryParams.username" placeholder="请输入用户名称"></el-input>
      </el-form-item>
      <el-form-item label="机构">
        <el-select v-model="queryParams.status" clearable size="small" style="width: 240px">
          <el-option label="全部" value></el-option>
          <el-option label="未审核" value="0"></el-option>
          <el-option label="已审核" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态">
        <el-select v-model="queryParams.status" clearable size="small" style="width: 140px">
          <el-option label="全部" value></el-option>
          <el-option label="未审核" value="0"></el-option>
          <el-option label="已审核" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          size="small"
          v-model="dateRange"
          type="date"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd "
        >></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>

    <!--  <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>-->
    <el-table
      highlight-current-row
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" width="40" :index="table_index"></el-table-column>
      <el-table-column prop="account" label="用户名称"></el-table-column>
      <el-table-column prop="username" label="机构"></el-table-column>
      <el-table-column prop="post" label="联系方式"></el-table-column>
      <el-table-column prop="updateTime" label="创建时间" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="审核状态">
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
      <el-table-column label="角色授权" width="100">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-custom"
            @click="handleRole(scope.row)"
          >角色授权</el-button>
        </template>
      </el-table-column>
      <el-table-column label="申请审核" width="100">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-check"
            @click="handleApply(scope.row)"
          >申请审核</el-button>
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
    <!-- 新增/编辑 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" v-dialogDrag>
      <handleWork v-if="dialogVisible" :handleMsgObj="handleMsgObj" @cancleClose="cancleClose" />
    </el-dialog>
    <!-- 角色授权 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogRole" width="500px" v-dialogDrag>
      <div>
        <el-select
          v-model="queryParams.status"
          clearable
          size="small"
          style="display:block;margin-bottom:20px;"
        >
          <el-option label="管理员" value></el-option>
          <el-option label="业务用户" value="0"></el-option>
          <el-option label="游客" value="1"></el-option>
        </el-select>
        <div class="dialog-footer" slot="footer">
          <el-button type="primary" @click="trueRole">确 定</el-button>
          <el-button @click="dialogRole = false">取 消</el-button>
        </div>
      </div>
    </el-dialog>
    <!-- 申请审核 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogApply"
      width="1100px"
      v-dialogDrag
      top="5vh"
    >
      <handleApply v-if="dialogApply" :handleMsgObj="handleMsgObj" @cancleClose="cancleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { page, delByIds, update } from "@/api/authorityAudit/userRoleAudit";
import handleWork from "@/views/authorityAudit/userRoleAudit/handleWork";
import handleApply from "@/views/authorityAudit/userRoleAudit/handleApply";
export default {
  components: {
    handleWork,
    handleApply
  },
  data() {
    return {
      dialogApply: false,
      dialogRole: false,
      dialogVisible: false,
      dateRange: [],
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: "",
        username: "",
        nameSourceDB: "",
        params: {
          orderBy: {
            updateTime: "desc"
          }
        }
      },

      total: 0,
      tableData: [],
      dialogTitle: "",
      currentRow: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.updateTime = "asc";
      } else {
        orderBy.updateTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
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
        nameSourceDB: ""
      };
      this.handleQuery();
    },
    // 新增
    handleAdd() {
      this.dialogTitle = "新增";
      this.handleMsgObj = {};
      this.dialogVisible = true;
    },
    // 修改
    handleEdit() {
      if (this.currentRow) {
        this.handleMsgObj = this.currentRow;
        this.dialogVisible = true;
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    // 删除
    handleDelete() {},
    // 角色授权
    handleRole(row) {
      this.dialogTitle = "角色授权";
      this.dialogRole = true;
    },
    // 确认授权
    trueRole() {},
    // 申请审核
    handleApply(row) {
      this.dialogTitle = "申请审核";
      this.handleMsgObj = row;
      this.dialogApply = true;
    },
    cancleClose() {
      this.handleMsgObj = {};
      this.dialogApply = false;
    }
  }
};
</script>
<style scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>