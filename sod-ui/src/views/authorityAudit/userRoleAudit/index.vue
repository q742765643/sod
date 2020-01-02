<template>
  <div class="app-container">
    <!-- 用户角色审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="申请用户">
        <el-input  size="small" v-model="queryParams.nameUser" placeholder="申请用户"></el-input>
      </el-form-item>
     <el-form-item label="状态">
        <el-select
          v-model="queryParams.examine_status"
          placeholder="状态"
          clearable
          size="small"
          style="width: 240px"
        >
         <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in examineStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
       
        <el-form-item>
          <el-button  size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
        <el-table-column  align="center"
          prop="application_user"
          label="账户名称"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column  align="center" prop="application_unit" label="用户名称"></el-table-column>
        <el-table-column  align="center" prop="db_name" label="部门"></el-table-column>
        <el-table-column  align="center" prop="application_time" label="申请时间">
          <!-- <template slot-scope="scope">{{scope.row.application_time.split('.')[0]}}</template> -->
        </el-table-column>
        <el-table-column  align="center" prop="examine_status" label="审核状态" :formatter="statusShow"></el-table-column>
        <el-table-column  align="center"  label="操作" width="260px">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.examine_status=='04'"
              type="text"
              size="mini"
              icon="el-icon-circle-check"
              @click="viewCell(scope.row)"
            >通过</el-button>
            <el-button
              v-if="scope.row.examine_status!='01' && scope.row.examine_status!='04'"
              type="text"
              size="mini"
              icon="el-icon-circle-close"
              @click="viewCell(scope.row)"
            >拒绝</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="90%" max-width="1100px" top="5vh">
      <handleAccount
        v-if="handleDialog"
        :handleObj="handleObj"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>

  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
import handleAccount from "@/views/authorityAudit/cloudDBaudit/handleCloudDB";
export default {
  components: {
    handleAccount
  },
  data() {
    return {
     // 遮罩层
      loading: true,
      queryParams: {
         pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""],
      },
      examineStatus: [
        {
          value: "0",
          label: "待审核"
        },
         {
          value: "1",
          label: "审核通过"
        },
        {
          value: "2",
          label: "审核未通过"
        },
       
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false
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
     resetQuery() {
      this.queryParams={
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""],
      };
      this.handleQuery();
    },
    handleAdd(){
      this.dialogTitle = "新增数据库账户审核";
      this.handleObj = {};
      this.handleDialog = true;
    },
    downloadTable(){},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    viewCell(row) {
      // this.dialogTitle = "数据库账户审核";
      // this.handleObj = row;
      // this.handleDialog = true;
    },
    deleteCell(row) {},
    analysisCell(row){},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
    
  }
};
</script>
