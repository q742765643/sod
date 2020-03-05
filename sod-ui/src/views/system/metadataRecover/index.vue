<template>
  <div class="app-container metaRecTem">
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
            <el-button size="small" type="primary" @click="getTreeList" icon="el-icon-search">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-upload class="upload-demo" action=" ">
              <el-button icon="el-icon-upload2" size="small" type="primary">上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <p style="font-size:14px">恢复文件</p>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            ref="eltree"
            node-key="id"
            show-checkbox
            :data="treedata"
            :props="defaultProps"
            :load="loadNode"
            @check="setSelectedNode"
            lazy
          ></el-tree>
        </el-scrollbar>
        <div class="footer">
          <el-button size="small" type="primary" @click="handleExe">执行</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="元数据恢复日志" name="second">
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true">
          <el-form-item label="数据库ip">
            <el-select size="small" filterable v-model="rowlogForm.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input size="small" v-model="rowlogForm.taskName"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              style="width:300px"
              v-model="rowlogForm.dateRange"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="queryListLog" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column align="center" type="index" width="40" :index="table_index_log"></el-table-column>
          <el-table-column align="center" type="selection" width="55"></el-table-column>
          <el-table-column
            align="center"
            prop="taskName"
            label="任务名称"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            align="center"
            prop="storageDirectory"
            label="恢复文件"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column align="center" prop="databaseName" label="数据库"></el-table-column>
          <el-table-column align="center" prop="handleTime" label="运行开始时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.handleTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="triggerCode" label="状态">
            <template slot-scope="scope">
              <el-link
                icon="el-icon-circle-close"
                type="danger"
                :underline="false"
                v-if="scope.row.triggerStatus==0"
              >停止</el-link>
              <el-link
                icon="el-icon-circle-check"
                type="success"
                :underline="false"
                v-if="scope.row.triggerStatus==1"
              >启动</el-link>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-reading"
                @click="viewDetail(scope.row)"
              >查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="logDataTotal>0"
          :total="logDataTotal"
          :page.sync="rowlogForm.pageNum"
          :limit.sync="rowlogForm.pageSize"
          @pagination="getListLog"
        />
      </el-tab-pane>
    </el-tabs>
    <!-- 恢复 -->
    <el-dialog :title="dialogTitle" :visible.sync="handeleRecoverDialog" width="50%">
      <handeleRecover
        @cancelHandle="cancelHandle"
        v-if="handeleRecoverDialog"
        :handleObj="handleObj"
      ></handeleRecover>
    </el-dialog>
  </div>
</template>

<script>
import { newTeam } from "@/components/commonVaildate";
import {
  findDataBase,
  getFileList,
  getFileChidren,
  parsingPath,
  listLog,
  logDetail
} from "@/api/system/metadataRecover";
//增加查看弹框
import handeleRecover from "@/views/system/metadataRecover/handeleRecover";

export default {
  components: {
    handeleRecover
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
      handleObj: {},
      currentRow: [],
      loadingHis: false,
      rowlogForm: {
        dateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      defaultProps: {
        children: "children",
        label: "name"
      },
      treedata: [],
      treeJson: [],
      handeleRecoverDialog: false
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
      return (
        (this.rowlogForm.pageNum - 1) * this.rowlogForm.pageSize + index + 1
      );
    },
    /** 查询列表 */
    getTreeList() {
      if (!this.queryParams.databaseId) {
        this.msgError("请选择数据库IP");
        return;
      }
      if (!this.queryParams.storageDirectory) {
        this.msgError("请选择目录");
        return;
      }
      this.loading = true;
      getFileList(this.queryParams).then(res => {
        this.treeJson = res.data;
        // 第一级的pid为空
        this.treedata = newTeam(res.data, "");
        console.log(this.treedata);
      });
    },

    loadNode(node, resolve) {
      if (node.level === 0 || node.data.isParent == false) {
        return resolve([]);
      } else {
        getFileChidren({ childrenPath: node.data.id }).then(res => {
          resolve(res.data);
        });
      }
    },
    setSelectedNode(data, obj) {
      this.$refs.eltree.setCheckedNodes([data]);
    },
    handleExe() {
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      if (checkedArry.length != 1) {
        this.msgError("请选择一条数据");
        return;
      }

      this.dialogTitle = "恢复";
      this.handleObj = this.queryParams;
      this.handleObj.storageDirectory = checkedArry[0];

      this.handeleRecoverDialog = true;
    },

    handleClick() {
      if (this.activeName == "first") {
        this.queryParams = {
          taskName: "",
          databaseId: "",
          storageDirectory: ""
        };
        this.treedata = [];
      } else {
        this.rowlogForm = {
          pageNum: 1,
          pageSize: 10,
          taskName: "",
          databaseId: ""
        };
        this.queryListLog();
      }
    },
    queryListLog() {
      this.rowlogForm.pageNum = 1;
      this.getListLog();
    },
    //日志列表
    getListLog() {
      this.loading = true;
      console.log(this.rowlogForm);
      listLog(
        this.addDateRange(this.rowlogForm, this.rowlogForm.dateRange)
      ).then(response => {
        this.logTableData = response.data.pageData;
        this.logDataTotal = response.data.totalCount;
        this.loading = false;
      });
    },
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.currentRow = [];
      this.handeleRecoverDialog = false;
      this.handleClick();
    },
    //选中行
    handleHistorySelectionChange() {
      this.currentRow = val;
    },

    viewDetail(row) {
      logDetail(row.id).then(res => {
        console.log(res);
      });
    }
  }
};
</script>
<style scope lang='scss'>
.metaRecTem {
  .el-scrollbar {
    margin-top: 10px;
    padding-top: 10px;
    height: 300px;
    border: 1px solid #c0c4cc;
    border-radius: 2px;
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .el-tree {
    min-width: 100%;
    display: inline-block !important;
  }
  .footer {
    text-align: center;
    margin: 10px;
  }
}
</style>
