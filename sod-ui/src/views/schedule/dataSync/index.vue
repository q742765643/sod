<template>
  <div class="app-container">
    <!-- 数据同步 -->
    <el-form :model="queryParams" ref="queryParams" :inline="true">
      <el-form-item label="关键字查询">
        <el-select size="small" v-model="queryParams.selectInfo">
          <el-option
            :key="index"
            v-for="(item,index) in selectInfoSelect"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="运行状态">
        <el-select size="small" v-model="queryParams.runState" style="width:120px;">
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in runStateSelect"
            :key="index"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="addSync">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-edit" size="small">批量启停</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" size="small">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-refresh" size="small">刷新</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column align="center" type="index" min-width="15" label=" "></el-table-column>
      <el-table-column align="center" type="selection" min-width="15"></el-table-column>
      <el-table-column align="center" prop="taskName" label="任务名称"></el-table-column>
      <el-table-column align="center" prop="dataSourceId" label="数据来源标识"></el-table-column>
      <el-table-column align="center" prop="flowDir" label="数据流向标识"></el-table-column>
      <el-table-column align="center" prop="sourceDBId" label="源库"></el-table-column>
      <el-table-column align="center" prop="execIp" label="执行主机"></el-table-column>
      <el-table-column align="center" prop="execPort" label="执行端口"></el-table-column>
      <el-table-column align="center" prop="updateTime" label="更新时间"></el-table-column>
      <el-table-column align="center" prop="status" label="运行状态" :formatter="getStatus"></el-table-column>
      <el-table-column align="center" label="操作" min-width="240">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-marketing"
            v-if="scope.row.status=='error'"
          >启动</el-button>
          <el-button type="text" size="mini" icon="el-icon-video-pause">停止</el-button>
          <el-button type="text" size="mini" icon="el-icon-s-marketing">重启</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit">编辑</el-button>
          <el-button type="text" size="mini" icon="el-icon-view">查看</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-reading"
            @click="viewDaiy(scope.row)"
          >查看日志</el-button>
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

    <el-dialog title="日志列表" :visible.sync="dailyDataDialog" width="90%">
      <dailySync v-if="dailyDataDialog" :handletaskId="handletaskId" />
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="80%">
      <handleSync
        v-if="handleDialog"
        :handleObj="handleObj"
        @cancelHandle="cancelHandle"
        ref="myHandleServer"
      />
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
// 日志查看
import dailySync from "@/views/schedule/dataSync/dailySync";
// 增加查看
import handleSync from "@/views/schedule/dataSync/handleSync";
export default {
  components: {
    dailySync,
    handleSync
  },
  data() {
    return {
      dailyDataDialog: false,
      handletaskId: "",
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
    },
    viewDaiy(row) {
      this.dailyDataDialog = true;
    },
    addSync() {
      this.dialogTitle = "新增";
      this.handleDialog = true;
    }
  }
};
</script>
