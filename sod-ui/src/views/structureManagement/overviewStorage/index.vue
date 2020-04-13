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
          <el-button v-else size="mini" @click="handleBackUp(scope.row)">
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
export default {
  components: {
    SuperSearch,
    DataRecovery,
    chechExe
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
    databaseShow() {},
    handleCleanAndTransfer() {},
    handleBackUp() {},
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
      this.handleDataRecoveryDialog = false;
      this.dialogSetting = false;
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
