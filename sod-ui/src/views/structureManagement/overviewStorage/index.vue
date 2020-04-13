<template>
  <div class="app-container">
    <!-- 存储结构概览 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据用途:">
        <el-input size="small" v-model="queryParams.logicName" placeholder="请输入数据用途" />
      </el-form-item>
      <el-form-item label="数据库:">
        <el-input size="small" v-model="queryParams.databaseName" placeholder="请输入数据库" />
      </el-form-item>
      <el-form-item label="资料名称:">
        <el-input size="small" v-model="queryParams.className" placeholder="请输入资料名称" />
      </el-form-item>
      <el-form-item label="表名称:">
        <el-input size="small" v-model="queryParams.tableName" placeholder="请输入表名称" />
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column prop="CLASS_NAME" label="资料名称" width="200"></el-table-column>
      <el-table-column prop="TABLE_NAME" label="表名称" width="180"></el-table-column>
      <el-table-column prop="DATA_CLASS_ID" label="存储编码"></el-table-column>
      <el-table-column prop="D_DATA_ID" label="四级编码"></el-table-column>
      <el-table-column prop="LOGIC_NAME" label="数据用途"></el-table-column>
      <el-table-column prop="DATABASE_NAME" label="数据库"></el-table-column>
      <el-table-column prop="SPECIAL_DATABASE_NAME" label="专题名" width="100"></el-table-column>
      <el-table-column label="参数配置" width="240">
        <template slot-scope="scope">
          <!-- 存储结构 -->
          <el-button disabled v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 3" size="mini">
            <i class="btnRound orangRound"></i>存储结构
          </el-button>
          <el-button v-else size="mini" @click="databaseShow(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.STORAGE_DEFINE_IDENTIFIER==1"></i>
            <i class="btnRound orangRound" v-else></i>存储结构
          </el-button>

          <!-- 迁移清除 -->
          <el-button disabled v-if="scope.row.STORAGE_MOVECLEAN_IDENTIFIER==3" size="mini">
            <i class="btnRound orangRound"></i>迁移清除
          </el-button>
          <el-button v-else size="mini" @click="handleCleanAndTransfer(scope.row)">
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
            <i class="btnRound orangRound" v-else></i>迁移清除
          </el-button>

          <!-- 备份 -->
          <el-button disabled v-if="scope.row.STORAGE_BACKUP_IDENTIFIER==3" size="mini">
            <i class="btnRound orangRound"></i>备份
          </el-button>
          <el-button v-else size="mini" @click="handleBackUpMethods(scope.row)">
            <i
              class="btnRound blueRound"
              v-if="scope.row.STORAGE_BACKUP_IDENTIFIER==1&&scope.row.BACKUP_ID"
            ></i>
            <i class="btnRound orangRound" v-else></i>备份
          </el-button>

          <!-- 恢复 -->
          <el-button disabled v-if="scope.row.STORAGE_ARCHIVING_IDENTIFIER==3" size="mini">恢复</el-button>
          <el-button v-else size="mini" @click="openRecoverDialog(scope.row)">恢复</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140px">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-setting"
            @click="settingCell(scope.row)"
          >配置</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCell(scope.row)">删除</el-button>
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
    <!-- 高级搜索 -->
    <el-dialog
      title="筛选"
      :visible.sync="dialogSuperSearch"
      :before-close="closeSuperSearch"
      v-dialogDrag
    >
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @searchFun="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
    <!-- 配置 -->
    <el-dialog
      v-dialogDrag
      title="选择需要配置的选项"
      :visible.sync="dialogSetting"
      width="500px"
      :before-close="handleClose"
    >
      <el-checkbox
        @change="changeSetting"
        v-model="checked3"
        false-label="STORAGE_MOVECLEAN_IDENTIFIER*"
        true-label="STORAGE_MOVECLEAN_IDENTIFIER"
        label="迁移清除"
        border
      ></el-checkbox>
      <el-checkbox
        @change="changeSetting"
        v-model="checked4"
        false-label="STORAGE_BACKUP_IDENTIFIER*"
        true-label="STORAGE_BACKUP_IDENTIFIER"
        label="备份"
        border
      ></el-checkbox>
      <el-checkbox
        @change="changeSetting"
        v-model="checked5"
        false-label="STORAGE_ARCHIVING_IDENTIFIER*"
        true-label="STORAGE_ARCHIVING_IDENTIFIER"
        label="恢复"
        border
      ></el-checkbox>
    </el-dialog>

    <!-- 数据恢复 -->
    <el-dialog
      v-dialogDrag
      title="结构化数据恢复"
      :visible.sync="handleDataRecoveryDialog"
      :before-close="handleClose"
      width="1100px"
    >
      <DataRecovery
        v-if="handleDataRecoveryDialog"
        :handleObj="handleObj"
        @handleRecover="handleRecover"
      />
      <el-dialog
        width="90%"
        title="MD5校验"
        :visible.sync="innerCheckMD5Visible"
        append-to-body
        v-dialogDrag
      >
        <chechExe
          v-if="innerCheckMD5Visible"
          :handlecheckObj="handlecheckObj"
          @handleMd5="handleMd5"
        />
      </el-dialog>
    </el-dialog>

    <!-- 表结构管理 存储结构 -->
    <el-dialog
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="handleClose"
      top="0"
    >
      <StructureManageTable v-if="structureManageVisible" v-bind:parentRowData="rowData" />
    </el-dialog>
    <!-- 迁移清除 结构化数据清除进度 -->
    <el-dialog
      :title="dialogMsgTitle"
      :visible.sync="handleCLeadupDialog"
      :before-close="handleClose"
      width="1100px"
    >
      <handleClear
        v-if="handleCLeadupDialog"
        :handleObj="handleMsgObj"
        @closeClearDialog="closeClearDialog"
      />
    </el-dialog>
    <!-- 迁移配置信息 -->
    <el-dialog
      :title="dialogMsgTitle"
      :visible.sync="handleMsgDialog"
      width="1200px"
      :before-close="handleClose"
    >
      <handleMove @handleClose="handleClose" v-if="handleMsgDialog" :handleObj="handleMsgObj"></handleMove>
    </el-dialog>
    <!-- 备份 -->
    <el-dialog
      :title="dialogMsgTitle"
      :visible.sync="handleBackupDialog"
      :before-close="handleClose"
      width="72%"
    >
      <handleBackUp
        v-if="handleBackupDialog"
        :handleObj="handleMsgObj"
        @handleClose="handleClose"
        ref="myHandleChild"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  storageConfigurationList,
  updateColumnValue,
  deleteColumnValue
} from "@/api/structureManagement/overviewStorage";
// 高级搜索
import SuperSearch from "@/components/superSearch";
// 数据恢复
import DataRecovery from "@/views/structureManagement/overviewStorage/handleDataRecovery";
// md5校验
import chechExe from "@/views/structureManagement/overviewStorage/handelchechExe";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
// 迁移配置信息
import handleMove from "@/views/schedule/move/handleMove";
// 迁移清除 结构化数据清除配置
import handleClear from "@/views/schedule/clear/handleClear";
// 结构化数据备份配置弹窗
import handleBackUp from "@/views/schedule/backup/handleBackUp";
export default {
  components: {
    SuperSearch,
    DataRecovery,
    chechExe,
    StructureManageTable,
    handleMove,
    handleClear,
    handleBackUp
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        logicName: "",
        databaseName: "",
        tableName: "",
        className: ""
      },
      total: 0,
      tableData: [],
      // 高级搜索
      dialogSuperSearch: false,
      superObj: {},
      // 弹窗
      rowData: {}, //当前行
      structureManageTitle: "", //存储结构标题
      dialogMsgTitle: "", //标题
      handleMsgObj: {}, //
      //存储结构
      structureManageVisible: false,

      handleDSDialog: false,
      handleCLeadupDialog: false,
      handleBackupDialog: false,
      handleDataRecoveryDialog: false,
      handleMsgDialog: false,
      innerCheckMD5Visible: false,

      // 设置
      dialogSetting: false,
      checked3: "",
      checked4: "",
      checked5: "",
      // 数据恢复
      handleDataRecoveryDialog: false,
      innerCheckMD5Visible: false, //md5校验
      handlecheckObj: {} //md5校验
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
      console.log(this.queryParams);
      storageConfigurationList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    superClick() {
      this.superObj = this.queryParams;
      this.superObj.pageName = "存储结构概览";
      this.dialogSuperSearch = true;
    },
    databaseShow(row) {
      this.rowData = row;
      this.structureManageTitle = row.class_name;
      this.structureManageVisible = true;
    },
    handleCleanAndTransfer(row) {
      if (row.STORAGE_MOVECLEAN_IDENTIFIER != 3) {
        if (
          row.STORAGE_MOVECLEAN_IDENTIFIER == 1 &&
          (row.database_type == "cassandra" || row.database_type == "ali_ots")
        ) {
          //flag添加或修改表中，3添加，2修改
          let flag = 3;
          if (row.CLEAR_ID) {
            flag = 2;
          }
          // this.handleCLeadupDialog = true;
          // tbdataClearAdd
        } else {
          //flag添加或修改表中，3添加，2修改
          let flag = 3;
          let databasetype = 2;
          this.handleMsgObj.handle = "存储结构概览-新增";
          this.dialogMsgTitle = "添加迁移清除配置信息";
          // this.dialogCleanupTitle = '添加迁移清除配置信息'
          if (row.DATABASE_ID && row.parent_id.indexOf("FIDB") > -1) {
            databasetype = 1;
          }
          if (null != row.CLEAR_ID && row.CLEAR_ID != "") {
            flag = 2;
            this.handleMsgObj.handle = "存储结构概览-编辑";
            this.dialogMsgTitle = "编辑迁移清除配置信息";
          }
          this.handleMsgObj.physics_database = row.parent_id;
          this.handleMsgObj.data_class_id = row.data_class_id;
          this.handleMsgObj.database_type = databasetype;
          this.handleMsgObj.data_service_name = row.data_service_name;
          console.log(this.handleMsgObj);
          // 判断去哪个页面
          this.axios
            .get(interfaceObj.findDataBaseMap + "?proName=migrateandclear")
            .then(res => {
              let list = res.data.data;
              let rec = {};
              rec = list.find(item => {
                return (
                  item.TEXT ===
                  row.database_name + "_" + row.special_database_name
                );
              });
              if (rec.value.indexOf("#1") > 0) {
                // 文件
                // 跳转迁移
                this.handleMsgDialog = true;
              } else if (rec.value.indexOf("#2") > 0) {
                // 历史
                // 跳转结构化数据类型清除
                if (this.dialogMsgTitle == "编辑迁移清除配置信息") {
                  this.axios
                    .post(interfaceObj.databaseTransfer_findbyDataClassId, {
                      databaseId: row.DATABASE_ID,
                      dataClassId: row.data_class_id,
                      type: 2
                    })
                    .then(res => {
                      if (res.data.returnCode == 0) {
                        this.handleMsgObj = res.data.data.transfer;
                        console.log(this.handleMsgObj);
                        this.handleCLeadupDialog = true;
                      }
                    });
                } else {
                  this.handleMsgObj.pageName = "存储结构概览";
                  this.handleCLeadupDialog = true;
                }
              } else {
                console.log("11");
              }
            });
        }
      }
    },
    handleBackUpMethods(row) {
      if (row.data_backup_identifier == 1 && row.backup_id) {
        // openUpdateBackupDialog 修改
        this.axios
          .get(interfaceObj.getByTaskId, { params: { taskId: row.backup_id } })
          .then(res => {
            this.handleObj = res.data.data;
            this.dialogBackupTitle = "修改";
            this.handleBackupDialog = true;
          });
      } else {
        this.handleObj = {};
        let arry = [];
        arry.push(row.data_class_id);
        this.handleObj.databaseId = row.database_id;
        this.handleObj.dataclass_value = arry;
        this.handleObj.pageName = "存储结构概览备份";
        this.dialogBackupTitle = "新增";
        this.handleBackupDialog = true;
        // openBackDialog 新曾
      }
    },
    openRecoverDialog() {
      // handleObj
      this.handleDataRecoveryDialog = true;
    },
    settingCell(row) {
      this.dialogSetting = true;
    },
    deleteCell(row) {
      deleteColumnValue({ id: row.id }).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    },
    // 关闭高级搜索
    closeSuperSearch() {
      this.dialogSuperSearch = false;
    },
    // 关闭弹窗
    handleClose() {
      this.handleMsgObj = {};
      this.dialogSetting = false;
      this.handleDSDialog = false;
      this.handleCLeadupDialog = false;
      this.handleBackupDialog = false;
      this.handleDataRecoveryDialog = false;
      this.handleMsgDialog = false;
      this.structureManageVisible = false;
      this.getList();
    },
    changeSetting(event) {
      let settingObj = {};
      settingObj.id = this.rowId;

      if (event.indexOf("*") == -1) {
        // 可用
        settingObj.value = 2;
        settingObj.column = event;
      } else {
        // 不可用
        settingObj.value = 3;
        settingObj.column = event.split("*")[0];
      }
      updateColumnValue(settingObj).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
      console.log(settingObj);
    },
    // 数据恢复
    handleRecover(msgFormDialog) {
      if (msgFormDialog) {
      }
      this.handleDataRecoveryDialog = false;
    },
    // md5
    handleMd5() {}
  }
};
</script>
<style lang="scss" scoped>
.searchBox {
  margin-bottom: 20px;
}
.el-table {
  font-size: 12px;
  .el-button--mini {
    padding: 6px;
    margin-left: 0;
  }
  .el-button--text {
    padding: 6px 2px;
  }
}
</style>
