<template>
  <section class="structureMaterialList">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="materialForm" :inline="true">
      <el-form-item label="资料名称">
        <el-input v-model="queryParams.className" size="small" placeholder="资料名称"></el-input>
      </el-form-item>
      <el-form-item label="是否发布">
        <el-select v-model="queryParams.ifStopUse" size="small">
          <el-option value label="全部"></el-option>
          <el-option :value="1" label="是"></el-option>
          <el-option :value="0" label="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button type="success" size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData">
      <el-table-column label="资料名称" prop="className" align="center" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="存储编码" prop="dataClassId" align="center" width="200"></el-table-column>
      <el-table-column label="四级编码" prop="DDataId" align="center" width="200"></el-table-column>
      <el-table-column label="是否发布" prop="ifStopUse" width="80" align="center">
        <template slot-scope="scope">{{scope.row.ifStopUse===1?'是':'否'}}</template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="searchFun"
    />
  </section>
</template>
<script>
import { getBaseData } from "@/api/structureManagement/tableStructureManage/StructureMaterialList";
var baseUrl = process.env.VUE_APP_DM;
import { Encrypt } from "@/utils/htencrypt";
//分页组件
export default {
  name: "structureMaterialList",
  components: {},
  data() {
    return {
      queryParams: {
        className: "",
        ifStopUse: "",
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableData: []
    };
  },
  created() {
    this.searchFun();
  },
  methods: {
    handleQuery() {
      this.queryParams.pageNum = 1;

      this.searchFun();
    },
    searchFun() {
      console.log(this.queryParams);
      getBaseData(this.queryParams).then(res => {
        this.tableData = res.data.pageData;
        this.total = res.data.totalCount;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      let obj = this.queryParams;
      let flieData = Encrypt(JSON.stringify(obj)); //加密
      flieData = encodeURIComponent(flieData); //转码
      window.location.href =
        baseUrl + "/dm/dataClass/exportBaseData?sign=111111&data=" + flieData;
    }
  }
};
</script>

<style lang="scss">
.structureMaterialList {
  .el-form-item {
    margin-bottom: 10px;
  }
  .el-pagination {
    margin-right: 0px;
  }
  .el-table th.gutter {
    display: table-cell !important;
  }
}
</style>
