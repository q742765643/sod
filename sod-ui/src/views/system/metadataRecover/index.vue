<template>
  <div class="app-container">
    <!-- 系统元数据恢复 -->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="元数据恢复" name="first">
        <el-form :model="queryParams" :inline="true">
          <el-form-item label="任务名称">
            <el-input size="small" v-model="queryParams.taskName"></el-input>
          </el-form-item>

          <el-form-item label="数据库IP">
            <el-select size="small" filterable v-model="queryParams.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="目录">
            <el-select size="small" filterable v-model="queryParams.storageDirectory">
              <el-option
                v-for="dict in storageDirectoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="getList" icon="el-icon-search">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/">
              <el-button icon="el-icon-upload2" size="small" type="primary">上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <p style="font-size:14px">恢复文件</p>
        <div class="treeBox">
          <el-tree ref="eltree" node-key="id" show-checkbox :data="treedata" :props="defaultProps"></el-tree>
        </div>
      </el-tab-pane>
      <el-tab-pane label="元数据恢复日志" name="second">
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true">
          <el-form-item label="数据库ip">
            <el-select style="width:200px" filterable v-model="rowlogForm.name">
              <el-option
                v-for="(item,index) in tableNames"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input size="small" v-model="rowlogForm.taskName"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              style="width:300px"
              v-model="rowlogForm.time"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              size="small"
              type="primary"
              @click="dataBaseExpandForm"
              icon="el-icon-search"
            >查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" width="40" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="start_time" label="任务名称"></el-table-column>
          <el-table-column prop="stop_time" label="恢复文件"></el-table-column>
          <el-table-column prop="dysnc_tablename" label="数据库"></el-table-column>
          <el-table-column prop="dysnc_recordnum" label="运行开始时间"></el-table-column>
          <el-table-column prop="run_state" label="状态"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-reading"
                @click="showEditDialog(scope.row)"
              >查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="logDataTotal>0"
          :total="logDataTotal"
          :page.sync="rowlogForm.pageNum"
          :limit.sync="rowlogForm.pageSize"
          @pagination="dataBaseExpandForm"
        />
      </el-tab-pane>
    </el-tabs>
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="50%">
      <handleExport @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleExport>
    </el-dialog>
    <el-dialog title="同步详情" :visible.sync="syncDescDialogVisible" v-if="syncDescDialogVisible">
      <div v-for="(item,index) in syncDescList" :key="index">
        <div v-if="item.run_state=='同步成功'">
          <span>同步表名{{index}}:{{item.dysnc_tablename}}</span>
          <span>同步记录数{{index}}:{{item.dysnc_recordnum}}</span>
        </div>
        <div v-else>
          <span>同步表名{{index}}:{{item.dysnc_tablename}}</span>
          <span>同步状态:{{item.run_state}}</span>
          <span>失败原因:{{item.fail_reason}}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeSyncDesc()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { newTeam } from "@/components/commonVaildate";
import { findDataBase, getFileList } from "@/api/system/metadataRecover";
//增加查看弹框
import handleExport from "@/views/system/metadataRecover/handleExport";
export default {
  components: {
    handleExport
  },
  data() {
    return {
      storageDirectoryOptions: [],
      activeName: "first",
      // 遮罩层
      loading: true,
      queryParams: {
        taskName: "",
        databaseId: "",
        storageDirectory: ""
      },
      ipList: [],
      // 新增/编辑同步任务
      dialogTitle: "",
      handleDialog: false,
      handleObj: {},
      // 立即同步元数据
      syncDescDialogVisible: false,
      syncDescList: [],
      currentRow: [],
      //同步记录查询
      loadingHis: false,
      rowlogForm: {
        pageNum: 1,
        pageSize: 10
      },
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      tableNames: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      treedata: [],
      treeJson: []
    };
  },
  created() {
    findDataBase().then(response => {
      this.ipList = response.data;
    });
    this.getDicts("metabackup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
  },
  methods: {
    // table自增定义方法
    table_index_log(index) {
      return (this.rowlogForm.page - 1) * this.rowlogForm.rows + index + 1;
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      getFileList(this.queryParams).then(res => {
        this.treeJson = res.data;
        // 第一级的pid为空
        this.treedata = newTeam(res.data, "");
        console.log(this.treedata);
      });
    },
    handleExport(type) {
      this.handleObj = {};
      if (type == "server") {
        this.dialogTitle = "执行服务库数据备份";
        this.handleObj.handleType = "server";
      } else if (type == "add") {
        this.dialogTitle = "添加";
        this.handleObj.handleType = "add";
      } else {
        this.dialogTitle = "执行元数据备份";
        this.handleObj.handleType = "base";
      }
      this.handleDialog = true;
    },
    syncCommonMetadata() {
      let ids = [];
      if (this.currentRow.length == 0) {
        this.$message({ type: "info", message: "请选择一行数据" });
        return;
      }
      for (var row in this.currentRow) {
        ids.push(this.currentRow[row].id);
      }
      //显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "执行同步任务中,请勿有其他操作！",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      // this.axios
      //   .post(interfaceObj.commonMetadata_totalinterface, { ids })
      //   .then(res => {
      //     if (res.data.returnCode == 0) {
      //       this.axios
      //         .post(interfaceObj.commonMetadata_records, { ids })
      //         .then(res => {
      //           loading.close(); //关闭延迟加载
      //           this.syncDescList = res.data.data;
      //           this.syncDescDialogVisible = true;
      //         });
      //     }
      //   });
    },
    deleteShow() {},
    handleClick() {},
    statusFormat(row, column, value) {
      if (value == 1) {
        return "全量同步";
      } else if (value == 2) {
        return "增量同步";
      } else {
        return value;
      }
    },
    showEditDialog(row) {},
    viewDaily(row) {},
    //选中行
    handleTaskSelectionChange(val) {
      this.currentRow = val;
    },
    handleHistorySelectionChange() {
      this.currentRow = val;
    },
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.currentRow = [];
      this.handleDialog = false;
      this.handleQuery();
    },
    //
    resetQuery() {},
    dataBaseExpandForm() {}
  }
};
</script>
