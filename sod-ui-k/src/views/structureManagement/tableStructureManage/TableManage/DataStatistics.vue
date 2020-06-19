<template>
  <el-main class="DataStatistics">
    <fieldset>
      <legend>{{rowData.DATABASE_NAME}}</legend>
      <el-form ref="ruleForm" :model="searchParams" label-width="100px" :inline="true">
        <el-form-item>
          <el-radio-group v-model.trim="searchParams.isAllLine" @change="handleQuery">
            <el-radio :label="1">全部在线</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="归档开始时间" v-show="searchParams.isAllLine==2">
          <el-input v-model.trim="searchParams.beginTime" size="small" readonly></el-input>
        </el-form-item>
        <el-form-item label="归档结束时间" v-show="searchParams.isAllLine==2">
          <el-input v-model.trim="searchParams.endTime" size="small" readonly></el-input>
        </el-form-item>
      </el-form>
      <el-table border :data="tableData" stripe style="width: 100%;" row-key="id">
        <el-table-column type="index" label="序号" :index="table_index" width="50"></el-table-column>
        <el-table-column prop="statisticDate" label="统计日期">
          <template slot-scope="scope">
            <span v-if="scope.row.statisticDate">{{ parseTime(scope.row.statisticDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="beginTime" label="开始时间">
          <template slot-scope="scope">
            <span v-if="scope.row.beginTime">{{ parseTime(scope.row.beginTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间">
          <template slot-scope="scope">
            <span v-if="scope.row.endTime">{{ parseTime(scope.row.endTime) }}</span>
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
import {
  datastatisticsList,
  updateIsAllLine,
  getArchive
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { parseTime } from "@/utils/ruoyi";
export default {
  name: "DataStatistics",
  props: { rowData: Object, tableInfo: Object },
  components: {},
  data() {
    return {
      loading: false,
      queryParams: {
        databaseId: this.rowData.DATABASE_ID,
        tableId: this.tableInfo.id == undefined ? "NULL" : this.tableInfo.id,
        pageNum: 1,
        pageSize: 10
      },
      searchParams: {
        isAllLine: 1,
        beginTime: "",
        endTime: ""
      },
      tableData: [],
      total: 0
    };
  },
  /*  mounted() {
    this.searchFun();
  }, */
  methods: {
    handleQuery(value) {
      if (value == 2) {
        getArchive({ ddataid: this.rowData.D_DATA_ID }).then(res => {
          this.searchParams.endTime = parseTime(res.data.endTime);
          this.searchParams.beginTime = parseTime(res.data.beginTime);
        });
        //return;
      }
      let obj = {};
      obj.dataClassId = this.rowData.DATA_CLASS_ID;
      obj.isAllLine = value;
      updateIsAllLine(obj).then(response => {});
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    searchFun() {
      if (this.tableInfo.id) {
        this.queryParams.tableId = this.tableInfo.id;
      }
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
