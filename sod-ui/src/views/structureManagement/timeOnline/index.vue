<template>
  <div class="app-container">
    <!-- 在线时间检索 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="四级编码">
        <el-input size="small" v-model.trim="queryParams.d_data_id"></el-input>
      </el-form-item>
      <el-form-item label="资料名称">
        <el-input size="small" v-model.trim="queryParams.class_name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
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
      <el-table-column prop="CLASS_NAME" label="资料名称"></el-table-column>
      <el-table-column prop="D_DATA_ID" label="四级编码" width="160"></el-table-column>
      <el-table-column prop="BEGIN_TIME" label="开始时间" width="120"></el-table-column>
      <el-table-column prop="END_TIME" label="结束时间" width="120" sortable="custom"></el-table-column>
      <el-table-column prop="RECORD_COUNT" label="数据总量" width="100"></el-table-column>
      <el-table-column prop="IF_STOP_USE" label="是否发布" width="80">
        <template slot-scope="scope">
          <div class="cell" v-if="scope.row.IF_STOP_USE==true">是</div>
          <div class="cell" v-if="scope.row.IF_STOP_USE==false" type="text">否</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
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

    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="权限配置"
      :visible.sync="handleDialog"
      width="650px"
    >
      <handleTime
        v-if="handleDialog"
        :handleObj="handleObj"
        @cancelHandle="resetQuery"
        ref="myHandleServer"
      />
    </el-dialog>
  </div>
</template>

<script>
import { onLineList } from "@/api/structureManagement/timeOnline";
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
        d_data_id: "",
        class_name: "",
        params: {
          orderBy: {
            END_TIME: "desc"
          }
        }
      },
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      handleObj: {},
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
        orderBy.END_TIME = "asc";
      } else {
        orderBy.END_TIME = "desc";
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
      this.loading = true;
      console.log(this.queryParams);
      onLineList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.DATA_CLASS_ID == this.currentRow.DATA_CLASS_ID) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    resetQuery() {
      this.handleDialog = false;

      this.getList();
    },

    handleCell(row) {
      this.handleDialog = true;
      this.handleObj = row;
    }
  }
};
</script>
<style lang="css" scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>
