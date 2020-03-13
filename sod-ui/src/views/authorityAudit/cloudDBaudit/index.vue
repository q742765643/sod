<template>
  <div class="app-container">
    <!-- 云数据库审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="审核状态">
        <el-select
          v-model="queryParams.examine_status"
          placeholder="审核状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申请用户">
        <el-input size="small" v-model="queryParams.nameUser" placeholder="申请用户"></el-input>
      </el-form-item>
      <el-form-item label="数据库名">
        <el-input size="small" v-model="queryParams.nameSourceDB" placeholder="数据库名"></el-input>
      </el-form-item>
      <el-form-item label="申请时间">
        <el-date-picker
          size="small"
          v-model="queryParams.time"
          type="datetimerange"
          range-separator="~"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
        >></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cloudDBaudit:role:add']"
        >添加云数据库申请</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column
        align="center"
        prop="userName"
        label="申请用户"
        width="120px"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column align="center" prop="department" label="申请单位" width="120px"></el-table-column>
      <el-table-column align="center" prop="application_time" label="申请时间" width="160px"></el-table-column>
      <el-table-column align="center" prop="examine_time" label="审核时间" width="160px"></el-table-column>
      <el-table-column align="center" prop="storage_logic" label="数据库类型" width="120px"></el-table-column>
      <el-table-column align="center" prop="db_name" label="数据库名" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" prop="db_use" label="用 途" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column
        align="center"
        prop="examine_status"
        label="审核状态"
        width="80px"
        :formatter="statusShow"
      ></el-table-column>
      <el-table-column align="center" label="操作" width="260px">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.examine_status=='01'"
            type="text"
            size="mini"
            icon="el-icon-coordinate"
            @click="viewCell(scope.row)"
          >审核</el-button>
          <el-button
            v-if="scope.row.examine_status=='04'"
            type="text"
            size="mini"
            icon="el-icon-finished"
            @click="viewCell(scope.row)"
          >释放</el-button>
          <el-button
            v-if="scope.row.examine_status!='01' && scope.row.examine_status!='04'"
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="viewCell(scope.row)"
          >查看</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCell(scope.row)">删除</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-marketing"
            @click="analysisCell(scope.row)"
          >资源分析</el-button>
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
    <el-dialog
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
    >
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
import {
  cldbApplicationAll,
  deleteExamine
} from "@/api/authorityAudit/cloudDBaudit";
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
        beginTime: "",
        endTime: ""
      },
      auditStatus: [
        {
          value: "01",
          label: "待审"
        },
        {
          value: "02",
          label: "已审"
        },
        {
          value: "03",
          label: "拒绝"
        },
        {
          value: "04",
          label: "申请释放"
        },
        {
          value: "05",
          label: "已释放"
        }
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
      if (this.queryParams.time && this.queryParams.time.length > 0) {
        this.queryParams.beginTime = this.queryParams.time[0];
        this.queryParams.endTime = this.queryParams.time[1];
      }

      console.log(this.queryParams);
      cldbApplicationAll(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
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
    // 状态
    statusShow: function(row) {
      if (row.examine_status == "01") {
        return "待审";
      } else if (row.examine_status == "02") {
        return "已审";
      } else if (row.examine_status == "03") {
        return "拒绝";
      } else if (row.examine_status == "04") {
        return "申请释放";
      } else if (row.examine_status == "05") {
        return "已释放";
      } else {
        return "-";
      }
    },
    handleAdd() {
      this.dialogTitle = "新增数据库账户审核";
      this.handleObj = {};
      this.handleDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    // 资源分析
    analysisCell(row) {
      this.$message({
        type: "info",
        message: "资源分析暂无数据"
      });
    },
    viewCell(row) {
      this.dialogTitle = "数据库账户审核";
      this.handleObj = row;
      this.handleDialog = true;
    },
    deleteCell(row) {
      this.$confirm(
        "确定要删除数据库名为" + row.db_name + "的这条数据吗?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          let cellObj = {};
          cellObj.application_id = row.application_id;
          cellObj.db_name = row.db_name;
          console.log(cellObj);
          deleteExamine(cellObj).then(response => {
            if (response.code == 200) {
              this.msgSuccess("删除成功");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    analysisCell(row) {},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
  }
};
</script>
