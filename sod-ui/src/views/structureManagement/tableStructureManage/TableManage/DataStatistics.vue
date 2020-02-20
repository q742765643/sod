<template>
  <el-main class="DataStatistics">
    <fieldset>
      <legend>{{rowData.database_name}}</legend>
      <el-table :data="tableData" stripe style="width: 100%;">
        <el-table-column type="index" width="50" :index="table_index"></el-table-column>
        <el-table-column prop="statistic_date" label="统计日期"></el-table-column>
        <el-table-column prop="begin_time" label="开始时间">
          <template slot-scope="scope">{{scope.row.begin_time.split('.')[0]}}</template>
        </el-table-column>
        <el-table-column prop="end_time" label="结束时间">
          <template slot-scope="scope">{{scope.row.end_time.split('.')[0]}}</template>
        </el-table-column>
        <el-table-column prop="record_count" label="总量" :formatter="countShow"></el-table-column>
        <el-table-column prop="day_total" label="日增量"></el-table-column>
      </el-table>
      <!-- <Pagination :total="dataTotals" @paginationChange="paginationChange" ref="pagination"></Pagination> -->
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
      dataTotals: 0
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
    },
    countShow: function(row) {
      var str = parseFloat(row.record_count).toString();
      return str;
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
