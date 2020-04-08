<template>
  <el-main class="DataStatistics">
    <fieldset>
      <legend>{{rowData.database_name}}</legend>
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
        :page.sync="searchObj.pageNum"
        :limit.sync="searchObj.pageSize"
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
      searchObj: { pageNum: 1, pageSize: 10 },
      tableData: [],
      total: 0
    };
  },
  /*  mounted() {
    this.searchFun();
  }, */
  methods: {
    // table自增定义方法
    // table自增定义方法
    table_index(index) {
      return (this.searchObj.pageNum - 1) * this.searchObj.pageSize + index + 1;
    },
    searchFun() {
      datastatisticsList(this.searchObj).then(response => {
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
