<template>
  <el-main class="DataStatistics">
    <fieldset>
      <legend>{{rowData.DATABASE_NAME}}</legend>
      <el-form ref="ruleForm" :model="queryParams" label-width="100px">
        <el-form-item label="数据库类型:">
          <el-select
            v-model="queryParams.databaseId"
            filterable
            placeholder="请选择"
            style="width:100%"
          >
            <el-option
              v-for="database in DBtypeOptions"
              :key="database.KEY"
              :label="database.VALUE"
              :value="database.KEY"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="queryParams.radio">
            <el-radio :label="3">全部在线</el-radio>
            <el-radio :label="6">近线服务</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width: 100%;">
        <el-table-column type="index" width="50" :index="table_index"></el-table-column>
        <el-table-column prop="statisticDate" label="统计日期">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.statisticDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="beginTime" label="开始时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.beginTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.endTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="recordCount" label="总量"></el-table-column>
        <el-table-column prop="dayTotal" label="日增量"></el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="searchFun"
      />
    </fieldset>
  </el-main>
</template>

<script>
import { datastatisticsList } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  name: "DataStatistics",
  props: { rowData: Object, tableInfo: Object },
  components: {},
  data() {
    return {
      DBtypeOptions: [],
      queryParams: { pageNum: 1, pageSize: 10 },
      queryParams: {},
      tableData: [],
      total: 0
    };
  },
  /*  mounted() {
    this.searchFun();
  }, */
  methods: {
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.searchFun();
    },
    // table自增定义方法
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    searchFun() {
      datastatisticsList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    }
  },
  watch: {
    tableInfo(val) {
      this.tableInfo = val;
      this.searchFun();
    }
  }
};
</script>

<style lang="scss">
.DataStatistics {
  fieldset {
    border: 1px solid #ebeef5;
    padding: 20px;
    margin-top: 12px;
  }
}
</style>
