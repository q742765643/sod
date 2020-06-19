<template>
  <div class="app-container">
    <!-- 资料访问权限审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="状态">
        <el-select
          v-model.trim="queryParams.auditStatus"
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
      <el-form-item label="申请人">
        <el-input size="small" v-model.trim="queryParams.userName" placeholder="申请人"></el-input>
      </el-form-item>
      <el-form-item label="申请时间">
        <el-date-picker
          size="small"
          v-model.trim="queryParams.dateRange"
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
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['authorityAudit:materialPower:add']"
        >权限配置</el-button>
      </el-col>
    </el-row>
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="userName" label="用户名"></el-table-column>
      <el-table-column prop="department" label="机构"></el-table-column>
      <el-table-column prop="telephone" label="联系方式"></el-table-column>
      <el-table-column prop="createTime" label="申请时间" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="100"
        prop="db_name"
        label="资料个数"
        :formatter="function(row){return row.dataAuthorityRecordList.length}"
      ></el-table-column>
      <el-table-column prop="examine_status" label="状态" :formatter="statusShow" width="120"></el-table-column>
      <el-table-column label="操作" width="120px">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="viewCell(scope.row)">查看</el-button>
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
      :close-on-click-modal="false"
      title="数据授权查看"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
      v-dialogDrag
      :before-close="cancelHandle"
    >
      <handleMaterial v-if="handleDialog" :handleObj="handleObj" ref="myHandleServer" />
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="权限配置"
      :visible.sync="handlepowerDialog"
      width="650px"
      v-dialogDrag
    >
      <handlePower v-if="handlepowerDialog" @cancelHandle="cancelHandle" ref="myHandleServer" />
    </el-dialog>
  </div>
</template>

<script>
import { queryList } from "@/api/authorityAudit/materialPower/index";
import handleMaterial from "@/views/authorityAudit/materialPower/handleMaterial";
import handlePower from "@/views/authorityAudit/materialPower/handlePower";
export default {
  components: {
    handleMaterial,
    handlePower
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        auditStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        userName: "",
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      dateRange: [],
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
      handlepowerDialog: false,
      currentRow: null
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
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
      if (this.queryParams.dateRange) {
        this.dateRange = this.queryParams.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      queryList(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
          if (this.currentRow) {
            this.tableData.forEach((element, index) => {
              if (element.id == this.currentRow.id) {
                this.$refs.singleTable.setCurrentRow(this.tableData[index]);
              }
            });
          }
        }
      );
    },
    resetQuery() {
      this.dateRange = [];
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        auditStatus: "",
        userName: ""
      };
      this.handleQuery();
    },
    handleAdd() {
      this.handleObj = {};
      this.handlepowerDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    viewCell(row) {
      this.handleObj = row;
      this.handleDialog = true;
    },
    deleteCell(row) {},
    analysisCell(row) {},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    },
    //状态转换
    statusShow(row) {
      let result = row.auditStatus == "02" ? "已审核" : "待审核";
      return result;
    },
    cancelHandle() {
      this.getList();
      this.handlepowerDialog = false;
      this.handleDialog = false;
    }
  }
};
</script>
