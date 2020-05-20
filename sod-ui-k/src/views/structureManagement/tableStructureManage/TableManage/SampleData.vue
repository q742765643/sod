<template>
  <el-main style="padding-top:10px;">
    <el-table border class="tb-edit" highlight-current-row :data="tableData" style="width: 100%">
      <af-table-column
        v-for="(item, index) in cols"
        :key="index"
        :prop="item.prop"
        :label="item.label"
      ></af-table-column>
    </el-table>
  </el-main>
</template>

<script>
import { sampleList } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  name: "SampleData",
  props: { rowData: Object, tableInfo: Object },
  data() {
    return {
      cols: [],
      tableData: [],
      columnData: []
    };
  },
  methods: {
    getSampleData() {
      let arr = [];
      this.cols = [];
      for (let i = 0; i < this.columnData.length; i++) {
        arr.push(this.columnData[i].dbEleCode.toLowerCase());
        let obj = {
          label: this.columnData[i].eleName,
          prop: this.columnData[i].dbEleCode.toLowerCase()
        };
        this.cols.push(obj);
      }
      let params = {};
      params.column = arr;
      params.databaseId = this.rowData.DATABASE_ID;
      params.tableName = this.tableInfo.tableName;
      console.log(params);
      sampleList(params).then(res => {
        if (res.code == 200) {
          this.tableData = res.data;
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    }
  },
  watch: {
    tableInfo(val) {
      this.columnData = this.tableInfo.columns;
      this.getSampleData();
    }
  }
};
</script>

<style scoped>
</style>
