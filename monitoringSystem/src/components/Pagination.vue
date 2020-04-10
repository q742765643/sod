<template>
  <el-pagination
    :current-page="realPage"
    :page-size="paginateObj.pageSize"
    :page-sizes="[10, 20, 30, 40]"
    :total="total"
    @current-change="handleCurrentChange"
    @size-change="handleSizeChange"
    layout="total, sizes, prev, pager, next, jumper"
  ></el-pagination>
</template>

<script>
export default {
  name: "pagination",
  props: {
    total: {
      type: Number
    }
  },
  data() {
    return {
      paginateObj: {
        currentPage: 1,
        pageSize: 10
      },
      realPage: 1
    };
  },
  created() {},
  methods: {
    handleSizeChange(val) {
      this.paginateObj.pageSize = val;
      this.$emit("paginationChange");
    },
    handleCurrentChange(val) {
      this.realPage = val;
      this.paginateObj.currentPage = val;
      this.$emit("paginationChange");
    },
    setPageSize(pageSize) {
      this.paginateObj.pageSize = pageSize;
    },
    setCurrentPage(currentPage) {
      this.paginateObj.currentPage = currentPage;
      this.realPage = currentPage + 1;
    }
  }
};
</script>

<style>
.el-pagination {
  text-align: right;
  padding-top: 10px;
}
</style>
