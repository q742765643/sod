<template>
  <div class="app-container">
    <!-- 数据库运维文档 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="文档名称">
        <el-input size="small" v-model="queryParams.fileName"></el-input>
      </el-form-item>
      <el-form-item label="文档后缀">
        <el-input size="small" v-model="queryParams.fileSuffix"></el-input>
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
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          multiple
          :limit="1"
          :show-file-list="showFile"
          :on-success="handleAvatarSuccess"
          ref="upload"
        >
          <el-button size="small" type="primary" icon="el-icon-upload">上传</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column align="center" prop="fileName" label="文档名称"></el-table-column>
      <el-table-column align="center" prop="fileSuffix" label="文档后缀" width="100px"></el-table-column>
      <el-table-column align="center" prop="filePictrue" label="文档类型" width="100px"></el-table-column>
      <el-table-column align="center" prop="updateTime" label="上传时间" width="180px"></el-table-column>
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
import { getpage, deleteByIds } from "@/api/systemHelp/dataBaseDevFile";
export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fileName: "",
        fileSuffix: "",
        endDate: "",
        startDate: "",
        time: ["", ""],
        order: "desc",
        fileType: "mor"
      },
      total: 0,
      tableData: [],
      currentRow: null,
      uploadUrl: "",
      showFile: false
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
      if (this.queryParams.time && this.queryParams.time.length > 0) {
        this.queryParams.startDate = this.queryParams.time[0];
        this.queryParams.endDate = this.queryParams.time[1];
      }
      getpage(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        fileName: "",
        fileSuffix: "",
        endDate: "",
        startDate: "",
        time: ["", ""],
        order: "desc"
      };
      this.handleQuery();
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    deleteCell() {
      if (this.currentRow) {
        this.$confirm("数据删除后将无法恢复，确认删除?", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            deleteByIds(this.currentRow.recordId).then(response => {
              if (response.code == 200) {
                this.$message({
                  type: "success",
                  message: "删除成功"
                });
                this.getList();
              } else {
                this.$message({
                  type: "error",
                  message: response.msg
                });
              }
            });
          })
          .catch(() => {});
      } else {
        this.$message({
          type: "danger",
          message: "请选择一条数据"
        });
      }
    },
    handleAvatarSuccess(res, file) {
      if (res.returnCode == 0) {
        this.$message({
          type: "success",
          message: "导入成功"
        });
        this.getList();
      } else {
        this.$message({
          type: "error",
          message: "导入失败"
        });
      }
      this.$refs.upload.clearFiles();
    },
    downloadFile() {},
    viewFile() {}
  }
};
</script>
