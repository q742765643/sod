<template>
  <div class="app-container">
    <!-- 公共元数据同步 -->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="同步任务配置" name="first">
         <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" v-hasPermi="['system:commonMetadataSync:add']" @click="showAddDialog()" icon="el-icon-plus">新增同步任务</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" v-hasPermi="['system:commonMetadataSync:add']" @click="syncCommonMetadata()" icon="el-icon-set-up">立即同步元数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" v-hasPermi="['system:commonMetadataSync:add']" @click="deleteShow()"  icon="el-icon-delete">删除同步任务</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="tableData" row-key="id"  highlight-current-row
            @selection-change="handleTaskSelectionChange">
            <el-table-column align="center" type="index" width="40" :index="table_index"></el-table-column>
            <el-table-column align="center" type="selection" width="55"></el-table-column>
            <el-table-column align="center" prop="task_name" sortable label="任务名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column align="center" prop="table_name" sortable label="表名" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column align="center"
              prop="interface_url"
              label="接口url"
              width="350px"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column align="center"
              prop="interface_type"
              label="同步类型"
              :formatter="statusFormat"
              width="100"
            ></el-table-column>
            <el-table-column align="center" prop="interface_dataKey" label="接口关键字"></el-table-column>
            <el-table-column align="center" prop="start_time" label="执行策略"></el-table-column>
            <el-table-column align="center" prop="description" label="来源" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column align="center"  label="操作" width="160px">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-edit"
                  @click="showEditDialog(scope.row)"
                >修改</el-button>
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-reading"
                  @click="viewDaily(scope.row)"
                >查询日志</el-button>
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
      </el-tab-pane>
      <el-tab-pane label="同步记录查询" name="second">
        <el-form :model="rowlogForm" ref="rowlogForm" :inline="true">
          <el-form-item label="同步表名">
            <el-select style="width:200px" filterable v-model="rowlogForm.name">
              <el-option
                v-for="(item,index) in tableNames"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运行状态">
            <el-select filterable style="width:150px" v-model="rowlogForm.status">
              <el-option value="同步成功" label="同步成功"></el-option>
              <el-option value="同步失败" label="同步失败"></el-option>
            </el-select>
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
            <el-button  size="small" type="primary" @click="dataBaseExpandForm" icon="el-icon-search">查询</el-button>
            <el-button  size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
          </el-form-item>
        </el-form>
         <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              v-hasPermi="['system:commonMetadataSync:delete']"
            >删除</el-button>
          </el-col>
        </el-row>
         <el-table
          v-loading="loadingHis"
          :data="logTableData"
          highlight-current-row
          ref="multipleTable"
          @selection-change="handleHistorySelectionChange"
        >
          <el-table-column type="index" width="40" :index="table_index_log"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="start_time"  label="开始时间"></el-table-column>
          <el-table-column prop="stop_time"  label="结束时间"></el-table-column>
          <el-table-column prop="dysnc_tablename" label="同步表名"></el-table-column>
          <el-table-column prop="dysnc_recordnum" label="同步记录数" ></el-table-column>
          <el-table-column prop="dysnc_type" label="同步类型" ></el-table-column>
          <el-table-column prop="dysnc_model" label="同步方式" ></el-table-column>
          <el-table-column prop="run_state" label="运行状态" ></el-table-column>
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
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="650px">
      <handleSod @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleSod>
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
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
//增加查看弹框
import handleSod from "@/views/system/commonMetadataSync/handleTask";
export default {
  components: {
    handleSod
  },
  data() {
    return {
      activeName:'first',
     // 遮罩层
      loading: true,
      queryParams: {
         pageNum: 1,
        pageSize: 10,
        
      },
      tableData:[],
      total:0,
      // 新增/编辑同步任务
      dialogTitle:'',
      handleDialog:false,
      handleObj:{},
      // 立即同步元数据
      syncDescDialogVisible:false,
      syncDescList:[],
      currentRow:[],
      //同步记录查询
      loadingHis:false,
      rowlogForm: {
        time: "",
        name: "",
        status: "同步成功"
      }, 
      logTableData: [], //同步表格数据
      logDataTotal: 0,
      tableNames:[],

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
    // table自增定义方法
    table_index_log(index) {
      return (this.rowlogForm.page - 1) * this.rowlogForm.rows + index + 1;
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
    showAddDialog(){
      this.handleObj = {};
      this.handleDialog = true;
      this.dialogTitle = "新增同步任务";
    },
    syncCommonMetadata(){
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
    deleteShow(){},
     handleClick(){},
    statusFormat(row, column, value) {
      if (value == 1) {
        return "全量同步";
      } else if (value == 2) {
        return "增量同步";
      } else {
        return value;
      }
    },
    showEditDialog(row){},
    viewDaily(row){},
   //选中行
    handleTaskSelectionChange(val) {
      this.currentRow = val;
    },
    handleHistorySelectionChange(){
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
  resetQuery(){},
  dataBaseExpandForm(){}
  }
};
</script>
