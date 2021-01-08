<template>
  <section class="linkTableInfo">
    <!-- 表格 -->
    <el-table border :data="tableData">
      <el-table-column
        label="表名称"
        prop="TABLE_NAME"
        :show-overflow-tooltip="true"
      ></el-table-column>
     
      <el-table-column label="数据库" prop="DATABASE_NAME" >
        <template slot-scope="scope">
          <span v-if="scope.row.DATABASE_NAME_F">{{
            scope.row.DATABASE_NAME_F
          }}</span>
          <span v-if="scope.row.DATABASE_NAME && scope.row.DATABASE_NAME_F"
            >-</span
          >
          <span v-if="scope.row.DATABASE_NAME">{{
            scope.row.DATABASE_NAME
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="表类型" prop="DICT_LABEL"> </el-table-column>
    </el-table>
  </section>
</template>
<script>
import { getTableInfo } from "@/api/structureManagement/materialManage/linkTableInfo";

//分页组件
export default {
  name: "linkTableInfo",
  props: { linkTableObj: Object },
  data() {
    return {
      queryParams: {
        className: "",
        ifStopUse: "",
      },
      total: 0,
      tableData: [],
    };
  },
  created() {
    this.searchFun();
  },
  methods: {
    searchFun() {
      getTableInfo({ dataclassId: this.linkTableObj.DATA_CLASS_ID }).then((res) => {
        this.tableData = res.data;
      });
    },
  },
};
</script>


