<template>
  <div class="app-container">
    <!-- 数据库开发文档 2020/4/20 和数据库运维文档合并为数据库文档-->
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
          v-model="dateRange"
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
          v-model="examineMaterial"
          class="upload-demo"
          accept=".docx, .pdf"
          :limit="1"
          :action="upLoadUrl"
          multiple
          :on-success="successUpload"
          :before-upload="handleBefore"
          :data="uploadData"
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
      @sort-change="sortChange"
    >
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="fileName" label="文档名称"></el-table-column>
      <el-table-column prop="fileSuffix" label="文档后缀" width="100px"></el-table-column>
      <el-table-column prop="filePictrue" label="文档类型" width="100px"></el-table-column>
      <el-table-column prop="updateTime" label="上传时间" width="180px" sortable="custom"></el-table-column>
      <el-table-column label="操作" width="100px">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-download"
            @click="downloadFile(scope.row)"
          >下载文档</el-button>
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
var baseUrl = process.env.VUE_APP_DM;
import { getpage, deleteByIds, upload } from "@/api/systemHelp/dataBaseDevFile";
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
        params: {
          orderBy: {
            updateTime: "desc"
          }
        },
        fileType: ""
      },
      dateRange: [],
      uoploaFile: "",
      total: 0,
      tableData: [],
      currentRow: null,
      upLoadUrl: baseUrl + "/api/dbfile/upload",
      uploadData: {},
      examineMaterial: "",
      showFile: false
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
      getpage(this.addDateRange(this.queryParams, this.dateRange)).then(
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
        fileName: "",
        fileSuffix: "",
        endDate: "",
        startDate: "",
        order: "desc"
      };
      this.dateRange = [];
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
    handleBefore(file) {
      this.uploadData.filename = file;
    },
    successUpload(res, file) {
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
      this.examineMaterial = res.msg;
      this.$refs.upload.clearFiles();
    },
    downloadFile() {}
  }
};
</script>
<style lang="scss">
.fitBtn {
  position: absolute;
  font-size: 12px;
  border-radius: 3px;
  width: 72px;
  left: 5px;
}
</style>
