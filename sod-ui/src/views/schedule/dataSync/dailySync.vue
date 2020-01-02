<template>
  <section class="dailySyncDialog">
    <el-table :data="tableData" highlight-current-row border stripe>
      <el-table-column align="center" type="index" min-width="15" label=" "></el-table-column>
      <el-table-column align="center" prop="beginTime" label="开始时间" min-width="100"></el-table-column>
      <el-table-column align="center" prop="endTime" label="结束时间" min-width="100"></el-table-column>
      <el-table-column align="center" prop="spendTime" label="总耗时（毫秒）" min-width="80"></el-table-column>
      <el-table-column align="center" prop="counts" label="处理记录总数" min-width="80"></el-table-column>
      <el-table-column align="center" prop="insert_count" label="成功插入数" min-width="70"></el-table-column>
      <el-table-column align="center" prop="update_count" label="成功更新数" min-width="70"></el-table-column>
      <el-table-column align="center" prop="discard_count" label="舍弃数" min-width="40"></el-table-column>
      <el-table-column align="center" prop="error" label="日志信息" min-width="60"></el-table-column>
      <el-table-column align="center" prop="error" label="详情" min-width="60">
        <template slot-scope="scope">
          <el-link type="primary" @click="viewCell(scope.row)">查看详情</el-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="dataTotal>0"
      :total="dataTotal"
      :page.sync="searchObj.pageNum"
      :limit.sync="searchObj.pageSize"
      @pagination="searchFun"
    />
  </section>
</template>
<script>
export default {
  name: "dailySyncDialog",

  props: {
    handletaskId: {
      type: String
    }
  },
  data() {
    return {
      tableData: [],
      dataTotal: 0,
      searchObj: {}
    };
  },
  created() {},
  methods: {
    searchFun() {
      this.searchObj.taskId = this.handletaskId;
      //debugger;
      this.axios
        .post(interfaceObj.databaseSync_queryPageSyncLog, this.searchObj)
        .then(res => {
          this.tableData = res.data.rows;
          this.dataTotal = res.data.total;
        });
    },
    //分页事件
    getPageSize(paginateObj) {
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.searchFun();
    },
    getCurrentPage(paginateObj) {
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.searchFun();
    },
    getPaginateObj(paginateObj) {
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.searchFun();
    }
  }
};
</script>
<style lang="scss">
</style>