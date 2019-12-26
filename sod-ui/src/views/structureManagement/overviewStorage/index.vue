<template>
  <div class="app-container">
    <!-- 存储结构概览 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
       <el-form-item label="数据用途:">
          <el-input size="small" v-model="queryParams.logic_name" placeholder="请输入数据用途" />
        </el-form-item>
        <el-form-item label="数据库:">
          <el-input size="small" v-model="queryParams.database_name" placeholder="请输入数据库" />
        </el-form-item>
        <el-form-item label="资料名称:">
          <el-input size="small" v-model="queryParams.class_name" placeholder="请输入资料名称" />
        </el-form-item>
        <el-form-item label="表名称:">
          <el-input size="small" v-model="queryParams.table_name" placeholder="请输入表名称" />
        </el-form-item>

        <el-form-item>
          <el-button size="small"  type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button size="small"  type="text" @click="superClick">
            <i class="el-icon-share"></i>高级搜索
          </el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column
          prop="class_name"
          label="资料名称"
          width="180"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="table_name"
          label="表名称"
          width="200"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column prop="data_class_id" label="存储编码" width="140" align="center"></el-table-column>
        <el-table-column prop="d_data_id" label="四级编码" width="140" align="center"></el-table-column>
        <el-table-column prop="logic_name" label="数据用途" width="120" align="center"></el-table-column>
        <el-table-column prop="database_name1" label="数据库" width="130" align="center"></el-table-column>
        <el-table-column prop="p_special_database_name" label="专题名" width="100" align="center"></el-table-column>
        <el-table-column label="参数配置" width="340" align="center">
          <template slot-scope="scope">
            <!-- 存储结构 -->
            <el-button
              v-if="scope.row.storage_define_identifier!='3'"
              size="mini"
              @click="databaseShow(scope.row)"
            >
              <i class="btnRound blueRound" v-if="scope.row.storage_define_identifier==1"></i>
              <i class="btnRound orangRound" v-else></i>存储结构
            </el-button>

            <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>存储结构
            </el-button>

            <!-- 迁移清除 -->
            <el-button
              v-if="scope.row.data_moveclean_identifier!='3'"
              size="mini"
              @click="handleCleanAndTransfer(scope.row)"
            >
              <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
              <i class="btnRound blueRound" v-if="null!=scope.row.clear_id&&scope.row.clear_id!=''"></i>
              <i class="btnRound orangRound" v-else></i>迁移清除
            </el-button>

            <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>迁移清除
            </el-button>

            <!-- 备份 -->
            <el-button
              v-if="scope.row.data_backup_identifier!='3'"
              size="mini"
              @click="handleBackUp(scope.row)"
            >
              <i
                class="btnRound blueRound"
                v-if="scope.row.data_backup_identifier=='1'&&scope.row.backup_id!=null&&scope.row.backup_id!=''"
              ></i>
              <i class="btnRound orangRound" v-else></i>备份
            </el-button>

            <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>备份
            </el-button>

            <!-- 恢复 -->
            <el-button
              v-if="scope.row.data_archiving_identifier!='3'"
              size="mini"
              @click="openRecoverDialog(scope.row)"
            >恢复</el-button>
            <el-button disabled v-else size="mini">恢复</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130px">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-setting"
              @click="settingCell(scope.row)"
            >配置</el-button>
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
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
     <!-- 高级搜索 -->
    <el-dialog title="筛选" :visible.sync="dialogSuperSearch" :before-close="closeSuperSearch">
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @searchFun="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
     <!-- 配置 -->
    <el-dialog
      title="选择需要配置的选项"
      :visible.sync="dialogSetting"
      width="500px"
      :before-close="handleClose"
    >
      <el-checkbox
        @change="changeSetting"
        v-model="checked3"
        false-label="data_moveclean_identifier*"
        true-label="data_moveclean_identifier"
        label="迁移清除"
        border
      ></el-checkbox>
      <el-checkbox
        @change="changeSetting"
        v-model="checked4"
        false-label="data_backup_identifier*"
        true-label="data_backup_identifier"
        label="备份"
        border
      ></el-checkbox>
      <el-checkbox
        @change="changeSetting"
        v-model="checked5"
        false-label="data_archiving_identifier*"
        true-label="data_archiving_identifier"
        label="恢复"
        border
      ></el-checkbox>
    </el-dialog>

     <!-- 数据恢复 -->
    <el-dialog
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
      <el-dialog width="90%" title="MD5校验" :visible.sync="innerCheckMD5Visible" append-to-body>
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
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
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
        logic_name: "",
        database_name: "",
        table_name: "",
        class_name: ""
      },
      total: 0,
      tableData: [],
      // 高级搜索
      dialogSuperSearch:false,
      superObj:{},

      // 设置
      dialogSetting: false,
      checked3: "",
      checked4: "",
      checked5: "",
      // 数据恢复
      handleDataRecoveryDialog:false,
      innerCheckMD5Visible:false,//md5校验
      handlecheckObj:{},//md5校验
    };
  },
  created() {
    this.getList();
  },
  methods: {
     // table自增定义方法
    table_index(index) {
      return (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    superClick(){
      this.superObj = this.queryParams;
      this.superObj.pageName = "存储结构概览";
      this.dialogSuperSearch = true;
    },
    databaseShow(){},
    handleCleanAndTransfer(){},
    handleBackUp(){},
    openRecoverDialog(){
      // handleObj
      this.handleDataRecoveryDialog = true;
    },
    settingCell(row){
      this.dialogSetting = true;
    },
    deleteCell(){},
    // 关闭高级搜索
    closeSuperSearch() {
      this.dialogSuperSearch = false;
    },
    // 关闭弹窗
    handleClose(){
      this.handleDataRecoveryDialog = false;
      this.dialogSetting = false;
      this.getList();
    },
    changeSetting(event) {
      let settingObj = {};
      // settingObj.id = this.rowId;

      if (event.indexOf("*") == -1) {
        // 可用
        settingObj.value = 2;
        settingObj.column = event;
      } else {
        // 不可用
        settingObj.value = 3;
        settingObj.column = event.split("*")[0];
      }
      // this.axios
      //   .post(interfaceObj.StructureView_config, settingObj, {
      //     headers: {
      //       "Content-Type": "application/json;charset=UTF-8"
      //     }
      //   })
      //   .then(res => {
      //     if (res.data.returnCode == 0) {
      //       this.$message({
      //         type: "success",
      //         message: "配置成功"
      //       });
      //       this.searchFun();
      //     }
      //   });
      console.log(settingObj);
    },
    // 数据恢复
    handleRecover(msgFormDialog){
      if(msgFormDialog){
         
      }
     this.handleDataRecoveryDialog = false
    },
    // md5
    handleMd5(){}
    
  }
};
</script>
