<template>
  <section class="structureMaterialList">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="materialForm" :inline="true">
      <el-form-item label="资料名称">
        <el-input v-model.trim="queryParams.className" size="small" placeholder="资料名称"></el-input>
      </el-form-item>
      <el-form-item label="是否发布">
        <el-select v-model.trim="queryParams.ifStopUse" size="small">
          <el-option value label="全部"></el-option>
          <el-option :value="true" label="是"></el-option>
          <el-option :value="false" label="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table border :data="tableData">
      <el-table-column label="资料名称" prop="className" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="存储编码" prop="dataClassId" width="200"></el-table-column>
      <el-table-column label="四级编码" prop="ddataId" width="200"></el-table-column>
      <el-table-column label="是否发布" prop="ifStopUse" width="80">
        <template slot-scope="scope">{{scope.row.ifStopUse==true?'是':'否'}}</template>
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
import {
  getBaseData,
  exportTable
} from "@/api/structureManagement/tableStructureManage/StructureMaterialList";

//分页组件
export default {
  name: "structureMaterialList",
  data() {
    return {
      queryParams: {
        className: "",
        ifStopUse: "",
        pageNum: 1,
        pageSize: 10,
        type: 2
      },
      total: 0,
      tableData: []
    };
  },
  created() {
    this.searchFun();
  },
  methods: {
    handleExport() {
      exportTable(this.queryParams).then(res => {
        this.downloadfileCommon(res);
      });
    },
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
