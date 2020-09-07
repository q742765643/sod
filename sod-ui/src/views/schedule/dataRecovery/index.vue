<template>
  <div class="app-container dataRecoveryTem">
    <!-- 数据恢复 -->
    <el-tabs v-model.trim="activeName" @tab-click="handleClick">
      <el-tab-pane label="结构化数据恢复" name="first">
        <el-form :model="queryParams" :inline="true" class="searchBox">
          <el-form-item label="数据库名称">
            <el-select
              v-model.trim="queryParams.databaseId"
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
            <el-select v-model.trim="queryParams.dataClassId" filterable placeholder="请选择资料">
              <el-option
                v-for="dataClass in dataClassIdOptions"
                :key="dataClass.DATA_CLASS_ID"
                :label="dataClass.CLASS_NAME"
                :value="dataClass.DATA_CLASS_ID"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目录">
            <el-select size="small" filterable v-model.trim="queryParams.path">
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
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true" class="searchBox">
          <el-form-item label="资料名称" prop="profileName">
            <el-input clearable size="small" v-model.trim="rowlogForm.profileName"></el-input>
          </el-form-item>
          <el-form-item label="运行状态" prop="handleCode">
            <el-select style="width:200px" filterable v-model.trim="rowlogForm.handleCode">
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
            <el-button size="small" @click="resetQueryLog" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          border
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @sort-change="sortChange"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index_log"></el-table-column>

          <el-table-column prop="profileName" label="资料名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="handleTime" label="运行开始时间" width="160" sortable="custom">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.handleTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="elapsedTime" label="耗时（秒）" width="120"></el-table-column>
          <el-table-column prop="handleCode" label="状态" :formatter="statusFormat" width="80"></el-table-column>
          <el-table-column label="操作" width="200">
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
    <el-dialog
      :close-on-click-modal="false"
      width="90%"
      title="MD5校验"
      :visible.sync="innerCheckMD5Visible"
      append-to-body
      v-dialogDrag
    >
      <chechExe
        v-if="innerCheckMD5Visible"
        :handlecheckObj="handlecheckObj"
        @handleMd5Cancel="handleMd5Cancel"
        ref="myHandleChild"
      />
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="详情"
      :visible.sync="logDetailDialog"
      v-if="logDetailDialog"
      width="1000px"
      v-dialogDrag
    >
      <el-form ref="ruleForm" :model="logFormDialog" label-width="120px" class="logDetailBoxRe">
        <el-form-item label="数据库名称">
          <el-select size="small" filterable v-model.trim="logFormDialog.databaseId">
            <el-option
              v-for="database in databaseOptions"
              :key="database.KEY"
              :label="database.VALUE"
              :value="database.KEY"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资料名称">
          <el-select v-model.trim="logFormDialog.dataClassId" filterable placeholder="请选择资料">
            <el-option
              v-for="dataClass in dataClassIdOptions"
              :key="dataClass.DATA_CLASS_ID"
              :label="dataClass.CLASS_NAME"
              :value="dataClass.DATA_CLASS_ID"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="表名称">
          <el-input size="small" v-model.trim="logFormDialog.tableName"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input size="small" v-model.trim="logFormDialog.handleCode"></el-input>
        </el-form-item>
        <el-form-item label="所有执行过程">
          <el-input size="small" v-model.trim="logFormDialog.handleMsg" type="textarea"></el-input>
          <el-button type="primary" size="small" @click="showAllDetail('所有执行过程')">显示全部</el-button>
        </el-form-item>
        <el-form-item label="恢复文件路径">
          <el-input size="small" v-model.trim="logFormDialog.storageDirectory" type="textarea"></el-input>
          <el-button type="primary" size="small" @click="showAllDetail('恢复文件路径')">显示全部</el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="logDetailDialog = false">取 消</el-button>
      </span>

      <el-dialog
        :close-on-click-modal="false"
        :title="allDetailTitle"
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
    </el-dialog>
  </div>
</template>

<script>
var baseUrl = process.env.VUE_APP_SCHEDULE_CENTER_API;
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
  detailLogById,
} from "@/api/schedule/dataRecovery";
//增加查看弹框
export default {
  components: {
    chechExe,
  },
  data() {
    return {
      activeName: "first",
      loading: true,
      queryParams: {
        dataClassId: "",
        databaseId: "",
        path: "",
      },
      databaseOptions: [],
      dataClassIdOptions: [],
      storageDirectoryOptions: [],
      // 树
      treedata: [],
      defaultProps: {
        children: "children",
        label: "name",
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
        handleCode: "",
        params: {
          orderBy: {
            handleTime: "desc",
          },
        },
      },
      logTableData: [],
      logDataTotal: 0,
      tableNames: [],
      statusOptions: [],
      logFormDialog: {},
      logDetailDialog: false,
      allDetailDialog: false,
      allDetailTitle: "",
      allDetailMsg: "",
      TreeDetaildata: [],
      statusOptions: [],
      uploadData: {},
      upLoadUrl: baseUrl + "/api/schedule/uploadDown/uploadFile",
    };
  },
  created() {
    findAllDataBase().then((response) => {
      this.databaseOptions = response.data;
    });
    this.getDicts("backup_storage_directory").then((response) => {
      this.storageDirectoryOptions = response.data;
    });
    this.getDicts("job_handle_status").then((response) => {
      this.statusOptions = response.data;
      console.log(this.statusOptions);
    });
  },
  methods: {
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.handleTime = "asc";
      } else {
        orderBy.handleTime = "desc";
      }
      this.rowlogForm.params.orderBy = orderBy;
      this.getListLog();
    },
    //数据库名称change事件
    selectByDatabaseIds(val) {
      getByDatabaseId(val).then((response) => {
        this.dataClassIdOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    getTreeList() {
      this.loading = true;
      console.log(this.queryParams);
      if (!this.queryParams.dataClassId) {
        this.msgError("请选择资料名称");
        return;
      }
      if (!this.queryParams.databaseId) {
        this.msgError("请选择数据库名称");
        return;
      }
      getDataFileList(this.queryParams).then((res) => {
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
        path: "",
      };
      this.treedata = [];
    },
    // 树的懒加载
    loadNode(node, resolve) {
      if (node.level === 0 || node.data.isParent == false) {
        return resolve([]);
      } else {
        getFileChidren({ childrenPath: node.data.id }).then((res) => {
          resolve(res.data);
        });
      }
    },

    // mds校验
    handleExe() {
      let checkedArry = this.$refs.eltree.getCheckedNodes();
      let childArry = [];
      checkedArry.forEach((element) => {
        if (element.parent == false) {
          childArry.push(element.id);
        }
      });
      if (checkedArry.length == 0) {
        this.msgError("请选择一条子节点的数据");
        return;
      }
      let obj = {};
      obj.databaseId = this.queryParams.databaseId;
      obj.paths = childArry;
      console.log(obj);
      md5Check(obj).then((res) => {
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
          profileName: "",
          databaseId: "",
          params: {
            orderBy: {
              handleTime: "desc",
            },
          },
        };
        this.queryListLog();
      }
    },
    queryListLog() {
      this.rowlogForm.pageNum = 1;
      this.getListLog();
    },
    resetQueryLog() {
      this.rowlogForm = {
        pageNum: 1,
        pageSize: 10,
        databaseId: "",
        profileName: "",
        params: {
          orderBy: {
            handleTime: "desc",
          },
        },
      };
      this.queryListLog();
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
      recoverLogList(this.rowlogForm).then((response) => {
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
        type: "warning",
      })
        .then(function () {
          return deleteLogById(row.id);
        })
        .then(() => {
          this.handleClick();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },

    viewDetail(row) {
      console.log(row.id);
      detailLogById(row.id).then((res) => {
        if (res.code == 200) {
          this.logDetailDialog = true;
          this.selectByDatabaseIds(res.data.databaseId);
          this.logFormDialog = res.data;
          this.logFormDialog.handleCode = this.selectDictLabel(
            this.statusOptions,
            this.logFormDialog.handleCode
          );
        }

        // let checkedTree = JSON.parse(res.data.backContent);
      });
    },
    //选中行
    handleHistorySelectionChange() {
      this.currentRow = val;
    },
    showAllDetail(title) {
      this.allDetailTitle = title;

      if (title == "所有执行过程") {
        this.allDetailMsg = this.logFormDialog.handleMsg;
      } else {
        this.allDetailMsg = this.logFormDialog.storageDirectory;
      }
      this.allDetailDialog = true;
    },
    // 上传
    handleBefore() {
      if (!this.queryParams.databaseId) {
        this.msgError("请选择数据库名称");
        return;
      }
      if (!this.queryParams.dataClassId) {
        this.msgError("请选择资料名称");
        return;
      }
      if (!this.queryParams.path) {
        this.msgError("请选择目录");
        return;
      }
      let pid = "";
      this.databaseOptions.forEach((element) => {
        if (element.KEY == this.queryParams.databaseId) {
          pid = element.parentId;
        }
      });
      this.uploadData.path =
        this.queryParams.path + "/" + pid + "/" + this.queryParams.dataClassId;
    },
    successUpload(res) {
      if (res.code == 200) {
        this.msgSuccess("上传成功");
      } else {
        this.msgError(res.msg);
      }
    },
  },
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
.logDetailBoxRe {
  .el-select {
    width: 100%;
  }
  .el-textarea {
    width: 90%;
  }
}
.allDetailMsg {
  .el-textarea__inner {
    min-height: 420px !important;
  }
}
</style>
