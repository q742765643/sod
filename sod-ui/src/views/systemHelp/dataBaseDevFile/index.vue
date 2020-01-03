<template>
  <div class="app-container">
    <!-- 数据库开发文档 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="文档名称">
        <el-input size="small" v-model="queryParams.nameUser"></el-input>
      </el-form-item>
      <el-form-item label="文档后缀">
        <el-input size="small" v-model="queryParams.nameUser"></el-input>
      </el-form-item>
      <el-form-item label="上传时间">
        <el-date-picker
          size="small"
          v-model="queryParams.time"
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
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/">
          <el-button size="small" type="primary" icon="el-icon-upload">上传</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column align="center" prop="application_unit" label="文档名称"></el-table-column>
      <el-table-column align="center" prop="db_name" label="文档后缀"></el-table-column>
      <el-table-column align="center" prop="db_name" label="文档类型"></el-table-column>
      <el-table-column align="center" prop="application_time" label="上传时间">
        <!-- <template slot-scope="scope">{{scope.row.application_time.split('.')[0]}}</template> -->
      </el-table-column>
      <el-table-column align="center" label="操作" width="260px">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-download"
            @click="downloadFile(scope.row)"
          >下载文档</el-button>
          <el-button type="text" size="mini" icon="el-icon-view" @click="viewFile(scope.row)">预览</el-button>
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
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""]
      },
      total: 0,
      tableData: []
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
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""]
      };
      this.handleQuery();
    },

    deleteCell(row) {},
    downloadFile() {},
    viewFile() {}
  }
};
</script>
