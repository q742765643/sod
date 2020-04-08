<template>
  <section class="dailySyncDialog">
    <el-table :data="tableData" highlight-current-row border stripe>
      <el-table-column type="index" min-width="15" label=" "></el-table-column>
      <el-table-column prop="beginTime" label="开始时间" min-width="100"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" min-width="100"></el-table-column>
      <el-table-column prop="spendTime" label="总耗时（毫秒）" min-width="80"></el-table-column>
      <el-table-column prop="counts" label="处理记录总数" min-width="80"></el-table-column>
      <el-table-column prop="insertCount" label="成功插入数" min-width="70"></el-table-column>
      <el-table-column prop="updateCount" label="成功更新数" min-width="70"></el-table-column>
      <el-table-column prop="discardCount" label="舍弃数" min-width="40"></el-table-column>
      <el-table-column prop="error" label="日志信息" min-width="60"></el-table-column>
      <el-table-column prop="error" label="详情" min-width="60">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.error" placement="top">
            <el-button size="small">错误信息</el-button>
          </el-tooltip>
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
import { listLog } from "@/api/schedule/dataSync";
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
      searchObj: {
        pageNum: 1,
        pageSize: 10,
        taskId: ""
      }
    };
  },
  created() {
    this.searchFun();
  },
  methods: {
    searchFun() {
      this.searchObj.taskId = this.handletaskId;
      listLog(this.searchObj).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    viewCell() {}
  }
};
</script>
<style lang="scss">
</style>