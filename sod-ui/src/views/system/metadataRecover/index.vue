<template>
  <div class="app-container metaRecTem">
    <!-- 系统元数据恢复 -->
    <el-tabs v-model.trim="activeName" @tab-click="handleClick">
      <el-tab-pane label="元数据恢复" name="first">
        <el-form :model="queryParams" :inline="true" class="searchBox">
          <el-form-item label="任务名称">
            <el-input size="small" v-model.trim="queryParams.taskName"></el-input>
          </el-form-item>

          <el-form-item label="数据库IP">
            <el-select size="small" filterable v-model.trim="queryParams.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="目录">
            <el-select size="small" filterable v-model.trim="queryParams.storageDirectory">
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
            <el-upload
              class="upload-demo"
              :action="upLoadUrl"
              :on-success="successUpload"
              :before-upload="handleBefore"
              :data="uploadData"
            >
              <el-button icon="el-icon-upload2" size="small" type="success">上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <p style="font-size:14px">恢复文件</p>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            ref="eltreeS"
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
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true" class="searchBox">
          <el-form-item label="数据库ip">
            <el-select size="small" filterable v-model.trim="rowlogForm.databaseId">
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="任务名称">
            <el-input size="small" v-model.trim="rowlogForm.taskName"></el-input>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              style="width:300px"
              v-model.trim="rowlogForm.dateRange"
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
          border
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="taskName" label="任务名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="storageDirectory" label="恢复文件" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="databaseName" label="数据库"></el-table-column>
          <el-table-column prop="handleTime" label="运行开始时间" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.handleTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="handleCode" label="状态" :formatter="statusFormat" width="80"></el-table-column>
          <el-table-column label="操作" width="180">
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
                @click="deleteLog(scope.row)"
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
    <!-- 恢复 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="dialogTitle"
      :visible.sync="handeleRecoverDialog"
      width="50%"
    >
      <handeleRecover
        @cancelHandle="cancelHandle"
        v-if="handeleRecoverDialog"
        :handleObj="handleObj"
      ></handeleRecover>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="详情"
      :visible.sync="logDetailDialog"
      v-if="logDetailDialog"
      width="1000px"
    >
      <el-form ref="ruleForm" :model="logFormDialog" label-width="120px" class="logDetailBox">
        <el-row>
          <el-col :span="12" style="padding-right:12px;">
            <el-form-item label="任务名称">
              <el-input size="small" v-model.trim="logFormDialog.taskName"></el-input>
            </el-form-item>
            <el-form-item label="数据库IP">
              <el-select size="small" filterable v-model.trim="logFormDialog.databaseId">
                <el-option
                  v-for="(item,index) in ipList"
                  :key="index"
                  :label="item.VAULE"
                  :value="item.KEY"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="类型">
              <el-checkbox-group v-model.trim="logFormDialog.checked">
                <el-checkbox label="结构">结构</el-checkbox>
                <el-checkbox label="数据">数据</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="恢复文件">
              <el-input size="small" v-model.trim="logFormDialog.storageDirectory"></el-input>
            </el-form-item>
            <el-form-item label="数据库">
              <el-input size="small" v-model.trim="logFormDialog.databaseName"></el-input>
            </el-form-item>
            <el-form-item label="运行开始时间">
              <el-input size="small" v-model.trim="logFormDialog.handleTime"></el-input>
            </el-form-item>
            <el-form-item label="状态">
              <el-input size="small" v-model.trim="logFormDialog.handleCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <el-tree ref="eltree" node-key="nodeKey" :data="TreeDetaildata" :props="defaultProps"></el-tree>
            </el-scrollbar>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="所有执行过程">
              <el-input size="small" v-model.trim="logFormDialog.handleMsg" type="textarea"></el-input>
              <el-button type="primary" size="small" @click="showAllDetail('所有执行过程')">显示全部</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="logDetailDialog = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="执行过程"
      :visible.sync="allDetailDialog"
      v-if="allDetailDialog"
      width="1000px"
      append-to-body
      v-dialogDrag
    >
      <el-input size="small" v-model.trim="allDetailMsg" type="textarea" class="allDetailMsg"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allDetailDialog = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
var baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API;
import { newTeam } from "@/components/commonVaildate";
import { formatDate } from "@/utils/index";
import {
  findDataBase,
  getFileList,
  getFileChidren,
  listLog,
  logDetail,
  deleteLogById
} from "@/api/system/metadataRecover";
//增加查看弹框
import handeleRecover from "@/views/system/metadataRecover/handeleRecover";

export default {
  components: {
    handeleRecover
  },
  data() {
    return {
      allDetailDialog: false,
      allDetailMsg: "",
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
      handeleRecoverDialog: false,
      // 详情
      logDetailDialog: false,
      logFormDialog: {
        checked: []
      },
      TreeDetaildata: [],
      statusOptions: [],
      uploadData: {},
      upLoadUrl: baseUrl + "/api/schedule/uploadDown/uploadFile"
    };
  },
  created() {
    findDataBase().then(response => {
      this.ipList = response.data;
    });
    this.getDicts("job_handle_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("metabackup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
  },
  methods: {
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.handleCode);
    },
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
      // this.$refs.eltree.setCheckedNyodes([data]);
    },
    handleExe() {
      if (!this.queryParams.taskName) {
        this.msgError("请输入任务名称");
        return;
      }
      let checkedArry = this.$refs.eltreeS.getCheckedNodes();
      let childArry = [];
      checkedArry.forEach(element => {
        if (element.parent == false) {
          childArry.push(element.id);
        }
      });
      if (childArry.length != 1) {
        this.msgError("请选择一条子节点的数据");
        return;
      }
      this.dialogTitle = "恢复";
      this.handleObj = this.queryParams;
      this.handleObj.storageDirectory = childArry[0];
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
      if (this.activeName == "first") {
        this.queryParams = {
          taskName: "",
          databaseId: "",
          storageDirectory: ""
        };
        this.treedata = [];
      } else {
        this.queryListLog();
      }
    },
    //选中行
    handleHistorySelectionChange() {
      this.currentRow = val;
    },
    deleteLog(row) {
      this.$confirm('是否确认删除"' + row.taskName + '"?', "温馨提示", {
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
      logDetail(row.id).then(res => {
        this.logFormDialog = res.data;
        this.logDetailDialog = true;
        this.logFormDialog.checked = [];
        let checkedArry = this.logFormDialog.isStructure.split(",");
        checkedArry.forEach(element => {
          if (element == "0") {
            this.logFormDialog.checked.push("结构");
          }
          if (element == "1") {
            this.logFormDialog.checked.push("数据");
          }
        });
        this.logFormDialog.handleTime = formatDate(
          this.logFormDialog.handleTime
        );
        this.logFormDialog.handleCode = this.selectDictLabel(
          this.statusOptions,
          this.logFormDialog.handleCode
        );
        if (res.data.recoverContent) {
          let checkedTree = JSON.parse(res.data.recoverContent);
          this.TreeDetaildata = newTeam(checkedTree, "");
        }
      });
    },
    showAllDetail(title) {
      this.allDetailMsg = this.logFormDialog.handleMsg;
      this.allDetailDialog = true;
    },
    // 上传
    handleBefore() {
      if (!this.queryParams.databaseId) {
        this.msgError("请选择数据库IP");
        return;
      }
      if (!this.queryParams.storageDirectory) {
        this.msgError("请选择目录");
        return;
      }
      let pid = "";
      this.ipList.forEach(element => {
        if (element.KEY == this.queryParams.databaseId) {
          pid = element.parentId;
        }
      });
      this.uploadData.path =
        this.queryParams.storageDirectory +
        "/" +
        pid +
        "/" +
        this.queryParams.taskName;
    },
    successUpload(res) {
      if (res.code == 200) {
        this.msgSuccess("上传成功");
      } else {
        this.msgError(res.msg);
      }
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
  .footer {
    text-align: center;
    margin: 10px;
  }
  #pane-second {
    .searchBox {
      margin-bottom: 24px;
    }
  }
}
.logDetailBox {
  .el-select {
    width: 100%;
  }
  .el-textarea {
    width: 90%;
  }
}
.allDetailMsg {
  .el-textarea__inner {
    min-height: 300px !important;
  }
}
</style>
