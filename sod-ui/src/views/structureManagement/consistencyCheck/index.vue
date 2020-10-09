<template>
  <div class="app-container">
    <!-- 一致性检查 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="专题库名称：" prop="database_name">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.database_name"
          placeholder="输入关键字搜索"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="success" :icon="exportIcon" @click="handleExport">生成差异报告</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="warning" icon="el-icon-refresh" @click="updateEleInfo">同步字段信息</el-button>
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
          @click="deleteData"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleCurrentChange"
      ref="singleTable"
      highlight-current-row
    >
      <el-table-column type="index" label="序号" :index="table_index" width="50"></el-table-column>
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="databaseName" label="专题库名称"></el-table-column>
      <el-table-column prop="databaseDto.databaseDefine.databaseType" label="数据库类型" width="120"></el-table-column>
      <el-table-column prop="databaseDto.databaseDefine.databaseInstance" label="专题库实例"></el-table-column>
      <el-table-column prop="databaseDto.schemaName" label="专题库模式"></el-table-column>
      <!--<el-table-column prop="database_desc" label="专题库描述"></el-table-column>-->
      <el-table-column prop="address" label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-tickets"
            size="small"
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
    <el-dialog
      :close-on-click-modal="false"
      title="添加"
      :visible.sync="addDataDialog"
      width="40%"
      v-dialogDrag
    >
      <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog">
        <el-form-item label="数据库选择:" prop="databaseId">
          <el-select
            style="width:80%;"
            v-model.trim="msgFormDialog.databaseId"
            placeholder="请选择添加数据库"
            filterable
            @change="databaseChange"
          >
            <el-option
              v-for="item in databaseNameOptions"
              :key="item.ID"
              :label="item.DATABASE_NAME"
              :value="item.ID"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addReportRecord('ruleForm')">确 定</el-button>
        <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      border
      :close-on-click-modal="false"
      title="历史报告表"
      :visible.sync="historyDialog"
      width="80%"
      height="50%"
      v-dialogDrag
    >
      <el-table
        ref="singleTable"
        :data="historyData"
        highlight-current-row
        border
        stripe
        @sort-change="sortChange"
      >
        <el-table-column type="index" label="序号" width="50" :index="table_index_history"></el-table-column>
        <!--<el-table-column prop="database_id" label="检查ID"></el-table-column>-->
        <el-table-column prop="fileName" label="报告名称" min-width="300"></el-table-column>
        <el-table-column prop="createTime" label="生成时间" sortable="custom">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="操作">
          <template slot-scope="scope1">
            <el-button
              icon="el-icon-tickets"
              size="small"
              type="text"
              @click="downloadHischeckFile(scope1.row)"
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
  consistencyCheckList,
  deleteById,
  consistencyCheckSave,
  getDatabaseName,
  historyList,
  deleteByIds,
  exportTable,
  downHistoryDfcheckFile,
  updateEleInfo
} from "@/api/structureManagement/consistencyCheck";
import { downloadTable } from "@/api/structureManagement/exportTable";
export default {
  data() {
    return {
      exportIcon: "el-icon-download",
      currentRows: [],
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        database_name: "",
      },
      total: 0,
      tableData: [],
      msgFormDialog: {
        databaseId: "",
        databaseName: "",
      },
      // 添加弹窗
      addDataDialog: false,
      databaseNameOptions: [],
      //  历史报告
      historyDialog: false,
      historyData: [],
      historyTotal: 0,
      historyObj: {
        pageNum: 1,
        pageSize: 10,
        id: "",
        params: {
          orderBy: {
            createTime: "desc",
          },
        },
      },
      rules: {
        databaseId: [
          { required: true, message: "请选择数据库", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getOptions();
  },
  methods: {
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.historyObj.params.orderBy = orderBy;
      historyList(this.historyObj).then((response) => {
        this.historyData = response.data.pageData;
        this.historyTotal = response.data.totalCount;
        this.loading = false;
      });
    },
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
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      consistencyCheckList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    handleCurrentChange(val) {
      this.currentRows = val;
    },
    handleExport() {
      if (this.currentRows.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      this.exportIcon = "el-icon-loading";
      this.$alert("生成差异报告中,请稍后", "温馨提示", {
        confirmButtonText: "确定",
        callback: (action) => {},
      });
      let obj = {};
      obj.id = this.currentRows[0].databaseId;
      exportTable(obj).then((res) => {
        this.downloadfileCommon(res);
        this.exportIcon = "el-icon-download";
      });
    },
    updateEleInfo() {
      if (this.currentRows.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      let obj = {};
      obj.id = this.currentRows[0].databaseId;

      this.$confirm(
        "确认要根据物理库同步元数据字段可空/精度信息吗?",
        "温馨提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          updateEleInfo(obj).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "同步成功",
              });
              this.handleQuery();
            } else {
              this.$message({
                type: "error",
                message: "同步失败",
              });
            }
          });
        })
        .catch(() => {});
    },
    getOptions() {
      getDatabaseName().then((response) => {
        var resdata = response.data;
        resdata.forEach((element) => {
          // if (
          //element.DATABASE_TYPE.indexOf("Gbase") != -1 ||
          // element.DATABASE_TYPE.indexOf("xugu") != -1
          // ) {
          this.databaseNameOptions.push(element);
          //}
        });
      });
    },
    /*点击历史报告列表事件 */
    getHistoricalReport(row) {
      this.historyDialog = true;
      this.historyObj.id = row.id;
      this.historyObj.databaseId = row.databaseId;
      historyList(this.historyObj).then((response) => {
        this.historyData = response.data.pageData;
        this.historyTotal = response.data.totalCount;
        this.loading = false;
      });
    },
    downloadHischeckFile(row) {
      let obj = {};
      obj.file_directory = row.file_directory;
      obj.filename = row.filename;
      /*downHistoryDfcheckFile(obj).then(res => {
        this.downloadfileCommon(res);
      });*/
      downloadTable({ filePath: row.fileDirectory + "/" + row.fileName }).then(
        (res) => {
          this.downloadfileCommon(res);
        }
      );
    },
    addReportData() {
      this.addDataDialog = true;
    },
    deleteData() {
      if (this.currentRows.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      let ids = [];
      let databaseNames = [];
      this.currentRows.forEach((element) => {
        ids.push(element.id);
        databaseNames.push(element.databaseName);
      });
      this.$confirm(
        "确认要删除专题库" + databaseNames.join(",") + "吗?",
        "温馨提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          deleteByIds(ids.join(",")).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.handleQuery();
            } else {
              this.$message({
                type: "error",
                message: "删除失败",
              });
            }
          });
        })
        .catch(() => {});
    },
    cancelDialog(formName) {
      this.msgFormDialog = false;
      this.addDataDialog = false;
      this.$refs[formName].resetFields();
    },
    addReportRecord(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          consistencyCheckSave(this.msgFormDialog).then((res) => {
            if (res.code == "200") {
              this.$message({
                type: "success",
                message: "添加成功",
              });
              this.$emit("resetQuery");
              this.handleQuery();
              this.addDataDialog = false;
              this.$refs[formName].resetFields();
            } else {
              this.$message({
                type: "error",
                message: "添加失败",
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    downloadHistoricalReport() {},
    getHistoryData() {},
    databaseChange(databaseId) {
      let databaseInfo = {};
      databaseInfo = this.databaseNameOptions.find((item) => {
        return item.ID === databaseId;
      });
      this.msgFormDialog.databaseName = databaseInfo.DATABASE_NAME;
    },
  },
};
</script>
