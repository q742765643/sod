<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model.trim="queryParams.ipaddr"
          placeholder="请输入登录地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model.trim="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table border v-loading="loading" :data="tableData" style="width: 100%;">
      <el-table-column label="登录名称" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="部门名称" prop="deptName" />
      <el-table-column label="主机" prop="ipaddr" :show-overflow-tooltip="true" />
      <el-table-column label="登录地点" prop="loginLocation" />
      <el-table-column label="浏览器" prop="browser" />
      <el-table-column label="操作系统" prop="os" />
      <el-table-column label="登录时间" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
            v-hasPermi="['monitor:online:forceLogout']"
          >强退</el-button>
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
  </div>
</template>

<script>
import { list, forceLogout } from "@/api/monitor/online";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      tableData: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$confirm(
        '是否确认强退名称为"' + row.userName + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return forceLogout(row.tokenId);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("强退成功");
        })
        .catch(function() {});
    }
  }
};
</script>
<style lang="css" scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>
