<template>
  <div class="tableManagementDR">
    <div class="headerTopMsg">
      <el-row>
        <el-col :span="2">
          <span>资料名称:</span>
        </el-col>
        <el-col :span="22">
          <span>{{this.tableMsgObj.class_name}}</span>
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <el-row>
        <el-col :span="2">
          <span>四级编码:</span>
        </el-col>
        <el-col :span="22">
          <span>{{this.tableMsgObj.data_class_id}}</span>
        </el-col>
      </el-row>
      <el-divider></el-divider>
    </div>
    <el-table
      :data="tableData"
      border
      highlight-current-row
      @current-change="handleCurrentChange"
      ref="singleTable"
    >
      <el-table-column label="数据用途" prop="logic_name"></el-table-column>
      <el-table-column label="存储类型" prop="name_cn"></el-table-column>
      <el-table-column label="数据库" prop="database_name"></el-table-column>
      <el-table-column label="操作" width="316px">
        <template slot-scope="scope">
          <el-button size="small" @click="showStructureManage(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.tablecount > scope.row.roundCount"></i>
            <i class="btnRound orangRound" v-else></i>
            表结构管理
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表结构管理 -->
    <el-dialog
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      append-to-body
      :fullscreen="true"
      :before-close="closeStructureManage"
      top="0"
      v-dialogDrag
    >
      <StructureManageTable v-if="structureManageVisible" v-bind:parentRowData="rowData" />
    </el-dialog>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// //表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
export default {
  name: "tableManagementDR",
  components: {
    StructureManageTable
  },
  props: {
    rowMaterialData: {
      type: Object
    }
  },
  data() {
    return {
      tableData: [],
      tableMsgObj: {},
      structureManageTitle: "",
      structureManageVisible: false,
      rowData: {},
      currentRow: []
    };
  },
  created() {
    this.tableMsgObj = this.rowMaterialData;
    // console.log(this.tableMsgObj);
    // this.searchFun();
  },
  methods: {
    searchFun() {
      this.axios
        .get(interfaceObj.TableStructure_getDataByTypeId, {
          params: {
            data_class_id_str: this.tableMsgObj.data_class_id
          }
        })
        .then(res => {
          this.tableData = res.data.data;
          this.tableData.forEach((item, index) => {
            item.roundCount =
              item.storage_type == "K_E_table"
                ? 1
                : item.storage_type == "MK_table"
                ? 1
                : 0;
          });

          this.rowspan();
        })
        .catch(error => {});
    },
    handleCurrentChange(currentRow) {
      if (currentRow == null) {
        return;
      }
      this.currentRow = currentRow;
    },
    //显示表结构管理
    showStructureManage(row) {
      this.rowData = row;
      this.structureManageTitle = row.class_name;
      this.structureManageVisible = true;
    },
    closeStructureManage() {
      this.searchFun();
      this.structureManageVisible = false;
    }
  }
};
</script>

<style lang="scss">
</style>
