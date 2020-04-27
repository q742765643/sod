<template>
  <div class="app-container">
    <!-- 一致性检查 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="专题库名称：">
        <el-input size="small" v-model="queryParams.database_name" placeholder="输入关键字搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">生成差异报告</el-button>
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
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleCurrentChange"
    >
      <el-table-column type="index" label=" " :index="table_index"></el-table-column>
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="databaseName" label="专题库名称"></el-table-column>
      <el-table-column prop="databaseDto.databaseDefine.databaseType" label="数据库类型"></el-table-column>
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
    <el-dialog title="添加" :visible.sync="addDataDialog" width="40%" v-dialogDrag>
      <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog">
        <el-form-item label="数据库选择:">
          <el-select
            style="width:80%;"
            v-model="msgFormDialog.databaseId"
            placeholder="请选择添加数据库"
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
        <el-button @click="addDataDialog= false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="历史报告表" :visible.sync="historyDialog" width="80%" height="50%" v-dialogDrag>
      <el-table ref="singleTable" :data="historyData" highlight-current-row border stripe>
        <el-table-column type="index" min-width="20" label=" " :index="table_index_history"></el-table-column>
        <el-table-column prop="database_id" label="检查ID"></el-table-column>
        <el-table-column prop="file_name" label="报告名称" min-width="300"></el-table-column>
        <el-table-column prop="create_time" label="生成时间"></el-table-column>
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
  downloadDfcheckFile,
  consistencyCheckSave,
  getDatabaseName,
  historyList,
  deleteByIds,
  exportTable
} from "@/api/structureManagement/consistencyCheck";
export default {
  data() {
    return {
      currentRow: [],
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        database_name: ""
      },
      total: 0,
      tableData: [],
      msgFormDialog: {
        databaseId: "",
        databaseName: ""
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
        id: ""
      },
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getOptions();
  },
  methods: {
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
    /** 查询列表 */
    getList() {
      this.loading = true;
      consistencyCheckList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    handleExport() {
      if (this.currentRow.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
        return;
      }
      let obj = {};
      obj.databaseId = this.currentRow[0].databaseId;
      exportTable(obj).then(res => {
        this.downloadfileCommon(res);
      });
    },
    getOptions() {
      getDatabaseName().then(response => {
        this.databaseNameOptions = response.data;
      });
    },
    /*点击历史报告列表事件 */
    getHistoricalReport(row) {
      this.historyDialog = true;
      this.historyObj.id = row.id;
      historyList(this.historyObj).then(response => {
        this.historyData = response.data.pageData;
        this.historyTotal = response.data.totalCount;
        this.loading = false;
      });
    },
    downloadHischeckFile(row) {
      let obj = {};
      obj.file_directory = row.file_directory;
      obj.filename = row.filename;
      this.$refs.downloadDf.exportData(obj);
    },
    addReportData() {
      this.addDataDialog = true;
    },
    deleteData() {
      if (this.currentRow.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
        return;
      }
      let ids = [];
      this.currentRow.forEach(element => {
        ids.push(element.id);
      });
      deleteByIds(ids.join(",")).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
        } else {
          this.$message({
            type: "error",
            message: "删除失败"
          });
        }
      });
    },
    addReportRecord(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          consistencyCheckSave(this.msgFormDialog).then(res => {
            if (res.code == "200") {
              this.$message({
                type: "success",
                message: "添加成功"
              });
              this.$emit("resetQuery");
              this.handleQuery();
              this.addDataDialog = false;
            } else {
              this.$message({
                type: "error",
                message: "添加失败"
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
      databaseInfo = this.databaseNameOptions.find(item => {
        return item.ID === databaseId;
      });
      this.msgFormDialog.databaseName = databaseInfo.DATABASE_NAME;
    }
  }
};
</script>
