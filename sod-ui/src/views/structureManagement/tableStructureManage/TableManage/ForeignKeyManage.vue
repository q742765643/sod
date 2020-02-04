<template>
  <el-main>
    <el-form :model="formData" label-width="130px" size="small" style="border: 1px;padding: 5px;">
      <el-col :span="7">
        <el-form-item label="键表字段">
          <el-select
            filterable
            v-model="formData.keyColumn"
            placeholder="请选择键表字段"
            style="width: 100%;"
          >
            <el-option
              v-for="item in keyColumnData"
              :key="item.dbEleCode"
              :label="item.eleName"
              :value="item.dbEleCode"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="7">
        <el-form-item label="要素表字段">
          <el-select
            filterable
            v-model="formData.eleColumn"
            placeholder="请选择要素表字段"
            style="width: 100%;"
          >
            <el-option
              v-for="item in elColumnData"
              :key="item.dbEleCode"
              :label="item.eleName"
              :value="item.dbEleCode"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="是否存在真实外键">
          <el-select v-model="formData.isReal" placeholder="请选择" style="width: 100%;">
            <el-option label="存在" value="true"></el-option>
            <el-option label="不存在" value="false"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" style="margin-left:20px;" @click="add">添加</el-button>
        <el-button type="danger" size="small" @click="deleteKeyInfo">删除</el-button>
      </el-col>
    </el-form>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="keyColumn" label="键表字段" align="center"></el-table-column>
      <el-table-column prop="eleColumn" label="要素表字段" align="center"></el-table-column>
      <el-table-column prop="isReal" label="是否存在真实外键" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.isReal==true">是</span>
          <span v-if="scope.row.isReal==false">否</span>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>

<script>
import {
  findByTableId,
  foreignKeyList,
  foreignKeySave
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  name: "ForeignKeyManage",
  props: { rowData: Object, keyTableInfo: Object, elTableInfo: Object },
  data() {
    return {
      tableData: [],
      formData: {
        tableId: "",
        keyColumn: "",
        eleColumn: "",
        isReal: "true"
      },
      keyColumnData: [],
      elColumnData: [],
      multipleSelection: []
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 根据键表ID查询键表字段
    getKeyColumnData() {
      findByTableId({ tableId: this.keyTableInfo.id }).then(response => {
        if (response.code == 200) {
          this.keyColumnData = response.data;
          this.formData.tableId = this.keyTableInfo.id;
        }
      });
    },
    // 根据要素表ID查询要素表字段
    getElColumnData() {
      findByTableId({ tableId: this.elTableInfo.id }).then(response => {
        if (response.code == 200) {
          this.elColumnData = response.data;
        }
      });
    },
    add() {
      console.log(this.formData);
      foreignKeySave(this.formData).then(response => {
        if (response.code == 200) {
          this.$message({
            message: "外键关联新增成功！",
            type: "success"
          });
          this.getForeignKeyData();
        } else {
          this.$message({
            message: response.msg,
            type: "error"
          });
        }
      });
    },
    deleteKeyInfo() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选择一条数据",
          type: "info"
        });
        return;
      }
      foreignKeySave().then(response => {
        if (response.code == 200) {
          this.$message({
            message: "外键关联删除成功！",
            type: "success"
          });
          this.getForeignKeyData();
        } else {
          this.$message({
            message: response.msg,
            type: "error"
          });
        }
      });
    },
    // 根据LOGIC_ID查询外键关联
    getForeignKeyData() {
      foreignKeyList({ logicId: this.rowData.LOGIC_ID }).then(response => {
        if (response.code == 200) {
          this.tableData = response.data;
        }
      });
    }
  },
  mounted() {
    this.getForeignKeyData();
  },
  watch: {
    keyTableInfo(val) {
      this.getKeyColumnData();
    },
    elTableInfo(val) {
      this.getElColumnData();
    }
  }
};
</script>

<style scoped>
</style>
