<template>
  <div class="app-container">
    <!-- 在线时间检索 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="四级编码">
        <el-input size="small" v-model="queryParams.nameUser"></el-input>
      </el-form-item>
      <el-form-item label="资料名称">
        <el-input size="small" v-model="queryParams.nameUser"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column align="center" prop="application_unit" label="资料名称"></el-table-column>
      <el-table-column align="center" prop="db_name" label="四级编码"></el-table-column>
      <el-table-column align="center" prop="db_name" label="开始时间"></el-table-column>
      <el-table-column align="center" prop="application_time" label="结束时间">
        <!-- <template slot-scope="scope">{{scope.row.application_time.split('.')[0]}}</template> -->
      </el-table-column>
      <el-table-column align="center" prop="db_name" label="数据总量"></el-table-column>
      <el-table-column align="center" prop="examine_status" label="是否发布"></el-table-column>
      <el-table-column align="center" label="操作" width="260px">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-edit" @click="handleCell(scope.row)">编辑</el-button>
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

    <el-dialog title="权限配置" :visible.sync="handleDialog" width="650px">
      <handleTime v-if="handleDialog" @cancelHandle="cancelHandle" ref="myHandleServer" />
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  exportRole,
  dataScope,
  changeRoleStatus
} from "@/api/system/role";
import handleTime from "@/views/structureManagement/timeOnline/handleTime";
export default {
  components: {
    handleTime
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
        time: ["", ""]
      },
      examineStatus: [
        {
          value: "01",
          label: "待审核"
        },
        {
          value: "02",
          label: "已审核"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      handleDialog: false
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
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""]
      };
      this.handleQuery();
    },
    handleAdd() {
      this.handleDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    handleCell(row) {
      this.handleObj = {};
      this.handleDialog = true;
    },
    deleteCell(row) {},
    analysisCell(row) {},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
  }
};
</script>
