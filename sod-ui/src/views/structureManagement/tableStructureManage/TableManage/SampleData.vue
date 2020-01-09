<template>
  <el-main>
    <el-table class="tb-edit" highlight-current-row :data="tableData" border style="width: 100%">
      <el-table-column
        v-for="(item, index) in cols"
        :key="index"
        :prop="item.prop"
        :label="item.label"
      ></el-table-column>
    </el-table>
  </el-main>
</template>

<script>
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
        arr.push(this.columnData[i].db_ele_code.toLowerCase());
        let obj = {
          label: this.columnData[i].ele_name,
          prop: this.columnData[i].db_ele_code.toLowerCase()
        };
        this.cols.push(obj);
      }
      let params = {};
      params.rows = arr.join(",");
      params.database_id = this.rowData.database_id;
      params.tableName = this.tableInfo.table_name;
      this.axios
        .get(interfaceObj.TableStructure_getDemoData, { params: params })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.tableData = res.data.data;
          } else {
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  watch: {
    tableInfo(val) {
      this.axios
        .get(interfaceObj.TableStructure_getColumnInfo, {
          params: { table_id: val.table_id }
        })
        .then(res => {
          this.columnData = res.data.data;
          this.getSampleData();
        })
        .catch(error => {});
    }
  }
};
</script>

<style scoped>
</style>
