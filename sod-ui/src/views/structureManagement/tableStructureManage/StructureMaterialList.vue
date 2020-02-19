<template>
  <section class="structureMaterialList">
    <!-- 查询条件 -->
    <el-form :model="searchObj" ref="materialForm" :inline="true">
      <el-form-item label="资料名称">
        <el-input v-model="searchObj.class_name" size="small" placeholder="资料名称"></el-input>
      </el-form-item>
      <el-form-item label="是否发布">
        <el-select v-model="searchObj.if_stop_use" size="small">
          <el-option value label="全部"></el-option>
          <el-option :value="1" label="是"></el-option>
          <el-option :value="0" label="否"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-search" @click="searchFun">查询</el-button>
        <el-button type="primary" size="small" icon="el-icon-right">导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData">
      <el-table-column label="资料名称" prop="class_name"></el-table-column>
      <el-table-column label="存储编码" prop="data_class_id"></el-table-column>
      <el-table-column label="四级编码" prop="d_data_id"></el-table-column>
      <el-table-column label="是否发布" prop="if_stop_use" width="70">
        <template slot-scope="scope">{{scope.row.if_stop_use===1?'是':'否'}}</template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </section>
</template>

<script>
//分页组件
export default {
  name: "structureMaterialList",
  components: {},
  data() {
    return {
      searchObj: {
        class_name: "",
        if_stop_use: ""
      },
      dataTotal: 0,
      tableData: []
    };
  },
  methods: {
    searchFun() {},

    //刷新列表
    refreshTable() {
      this.searchFun();
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
