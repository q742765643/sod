<template>
  <div class="app-container dataRecoveryTem">
    <!-- 数据恢复 -->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="结构化数据恢复" name="first">
        <el-form :model="queryParams" :inline="true">
          <el-form-item label="数据库名称">
            <el-select
              v-model="queryParams.databaseId"
              filterable
              @change="selectByDatabaseIds"
              placeholder="请选择物理库"
              style="width:100%"
            >
              <el-option
                v-for="database in databaseOptions"
                :key="database.KEY"
                :label="database.VALUE"
                :value="database.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="资料名称">
            <el-select v-model="queryParams.dataClassId" filterable placeholder="请选择资料">
              <el-option
                v-for="dataClass in dataClassIdOptions"
                :key="dataClass.DATA_CLASS_ID"
                :label="dataClass.CLASS_NAME"
                :value="dataClass.DATA_CLASS_ID"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目录">
            <el-select size="small" filterable v-model="queryParams.path">
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
            <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
        <p style="font-size:14px">恢复文件列表</p>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            ref="eltree"
            node-key="id"
            show-checkbox
            :data="treedata"
            :props="defaultProps"
            :load="loadNode"
            lazy
          ></el-tree>
        </el-scrollbar>
        <div class="footer">
          <el-button size="small" type="primary" @click="handleExe">md5校验</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="恢复日志" name="second">
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true">
          <el-form-item label="资料名称">
            <el-input size="small" v-model="queryParams.profileName"></el-input>
          </el-form-item>
          <el-form-item label="运行状态">
            <el-select style="width:200px" filterable v-model="rowlogForm.handleCode">
              <el-option
                v-for="(item,index) in statusOptions"
                :key="index"
                :label="item.dictLabel"
                :value="item.dictValue"
              ></el-option>
            </el-select>
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
        >
          <el-table-column align="center" type="index" width="40" :index="table_index_log"></el-table-column>

          <el-table-column
            align="center"
            prop="profileName"
            label="资料名称"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column align="center" prop="handleTime" label="运行开始时间">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.handleTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="elapsedTime" label="耗时（秒）"></el-table-column>
          <el-table-column align="center" prop="handleCode" label="状态" :formatter="statusFormat"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="el-icon-reading"
                @click="viewDetail(scope.row)"
              >查看详情</el-button>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-delete"
                @click="deleteCell(scope.row)"
              >删除</el-button>
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
    <el-dialog width="90%" title="MD5校验" :visible.sync="innerCheckMD5Visible" append-to-body>
      <chechExe
        v-if="innerCheckMD5Visible"
        :handlecheckObj="handlecheckObj"
        @handleMd5Cancel="handleMd5Cancel"
        ref="myHandleChild"
      />
    </el-dialog>
  </div>
</template>

<script>
// 树转换
import { newTeam } from "@/components/commonVaildate";
// md5校验
import chechExe from "@/views/structureManagement/overviewStorage/handelchechExe";
// 接口
import { findAllDataBase } from "@/api/schedule/backup/backup";
import {
  getByDatabaseId,
  getDataFileList,
  getFileChidren,
  md5Check,
  recoverStructedData,
  recoverLogList,
  deleteLogById,
  detailLogById
} from "@/api/schedule/dataRecovery";
//增加查看弹框
export default {
  components: {
    chechExe
  },
  data() {
    return {
      activeName: "first",
      loading: true,
      queryParams: {
        dataClassId: "",
        databaseId: "",
        path: ""
      },
      databaseOptions: [],
      dataClassIdOptions: [],
      storageDirectoryOptions: [],
      // 树
      treedata: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      // md5
      innerCheckMD5Visible: false,
      handlecheckObj: {},
      // 新增/编辑同步任务
      dialogTitle: "",
      handleDialog: false,
      handleObj: {},
      currentRow: [],
      //同步记录查询
      loadingHis: false,
      rowlogForm: {
        profileName: "",
        handleCode: ""
      },
      logTableData: [],
      logDataTotal: 0,
      tableNames: [],
      statusOptions: []
    };
  },
  created() {
    findAllDataBase().then(response => {
      this.databaseOptions = response.data;
    });
    this.getDicts("backup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
    this.getDicts("job_handle_status").then(response => {
      this.statusOptions = response.data;
      console.log(this.statusOptions);
    });
  },
  methods: {
    //数据库名称change事件
    selectByDatabaseIds(val) {
      getByDatabaseId(val).then(response => {
        this.dataClassIdOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    getTreeList() {
      this.loading = true;
      console.log(this.queryParams);
      getDataFileList(this.queryParams).then(res => {
        this.treeJson = res.data;
        // 第一级的pid为空
        this.treedata = newTeam(res.data, "");
        console.log(this.treedata);
      });
    },
    // 重置
    resetQuery() {
      this.queryParams = {
        dataClassId: "",
        databaseId: "",
        path: ""
      };
      this.treedata = [];
    },
    // 树的懒加载
    loadNode(node, resolve) {
      if (node.level === 0 || node.data.isParent == false) {
        return resolve([]);
      } else {
        getFileChidren({ childrenPath: node.data.id }).then(res => {
          resolve(res.data);
        });
      }
    },

    // mds校验
    handleExe() {
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      if (checkedArry.length == 0) {
        this.msgError("请选择一条数据");
        return;
      }
      let obj = {};
      obj.databaseId = this.queryParams.databaseId;
      obj.paths = checkedArry;
      console.log(obj);
      md5Check(obj).then(res => {
        if (res.code == 200) {
          this.handlecheckObj = {};
          this.handlecheckObj = this.queryParams;
          this.handlecheckObj.pathList = res.data;
          this.innerCheckMD5Visible = true;
        }
      });
    },
    // 确认 md5
    handleMd5Cancel() {
      this.innerCheckMD5Visible = false;
    },
    // tab点击
    handleClick() {
      if (this.activeName == "first") {
        this.resetQuery();
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
    // table自增定义方法
    table_index_log(index) {
      return (
        (this.rowlogForm.pageNum - 1) * this.rowlogForm.pageSize + index + 1
      );
    },
    //日志列表
    getListLog() {
      this.loading = true;
      console.log(this.rowlogForm);
      recoverLogList(this.rowlogForm).then(response => {
        this.logTableData = response.data.pageData;
        this.logDataTotal = response.data.totalCount;
        this.loading = false;
      });
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.handleCode);
    },

    deleteCell(row) {
      this.$confirm("是否确认删除该数据?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return deleteLogById(row.id);
        })
        .then(() => {
          this.handleClick();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },

    viewDetail(row) {
      detailLogById(row.id).then(res => {});
    },
    //选中行
    handleHistorySelectionChange() {
      this.currentRow = val;
    },
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.currentRow = [];
      this.handleDialog = false;
      this.handleQuery();
    }
  }
};
</script>
<style lang="scss">
.dataRecoveryTem {
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
