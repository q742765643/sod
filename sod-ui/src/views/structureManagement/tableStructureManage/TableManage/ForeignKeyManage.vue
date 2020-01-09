<template>
  <el-main>
    <el-form :model="formData" label-width="130px" size="small" style="border: 1px;padding: 5px;">
      <el-col :span="7">
        <el-form-item label="键表字段">
          <el-select filterable v-model="formData.K" placeholder="请选择键表字段" style="width: 100%;">
            <el-option
              v-for="item in keyColumnData"
              :key="item.db_ele_code"
              :label="item.ele_name"
              :value="item.db_ele_code"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="7">
        <el-form-item label="要素表字段">
          <el-select filterable v-model="formData.E" placeholder="请选择要素表字段" style="width: 100%;">
            <el-option
              v-for="item in elColumnData"
              :key="item.db_ele_code"
              :label="item.ele_name"
              :value="item.db_ele_code"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="是否存在真实外键">
          <el-select v-model="formData.isExist" placeholder="请选择" style="width: 100%;">
            <el-option label="存在" value="Y"></el-option>
            <el-option label="不存在" value="N"></el-option>
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
      <el-table-column prop="K" label="键表字段"></el-table-column>
      <el-table-column prop="E" label="要素表字段"></el-table-column>
      <el-table-column prop="isExist" label="是否存在真实外键">
        <template slot-scope="scope">
          <span v-if="scope.row.isExist=='Y'">是</span>
          <span v-if="scope.row.isExist=='N'">否</span>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>

<script>
export default {
  name: "ForeignKeyManage",
  props: { rowData: Object, keyTableInfo: Object, elTableInfo: Object },
  data() {
    return {
      tableData: [],
      formData: { isExist: "Y" },
      keyColumnData: [],
      elColumnData: [],
      multipleSelection: []
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getKeyColumnData() {
      this.axios
        .get(interfaceObj.TableStructure_getColumnInfo, {
          params: { table_id: this.keyTableInfo.table_id }
        })
        .then(res => {
          this.keyColumnData = res.data.data;
        })
        .catch(error => {});
    },
    getElColumnData() {
      this.axios
        .get(interfaceObj.TableStructure_getColumnInfo, {
          params: { table_id: this.elTableInfo.table_id }
        })
        .then(res => {
          this.elColumnData = res.data.data;
        })
        .catch(error => {});
    },
    add() {
      let obj = {};
      obj.table_id = this.keyTableInfo.table_id;
      obj.db_table_type = "K";
      obj.table_foreigh_field = this.formData.K + "&" + this.formData.isExist;
      this.axios
        .post(interfaceObj.TableStructure_addForeignKey, obj)
        .then(res => {
          obj.table_id = this.elTableInfo.table_id;
          obj.db_table_type = "E";
          obj.table_foreigh_field = this.formData.E;
          this.axios
            .post(interfaceObj.TableStructure_addForeignKey, obj)
            .then(res => {
              if (res.data.returnCode == 0) {
                this.$message({
                  message: "操作成功！",
                  type: "success"
                });
                this.getForeignKeyData();
              } else {
                this.$message({
                  message: "操作失败！",
                  type: "error"
                });
              }
            })
            .catch(error => {});
        })
        .catch(error => {});
    },
    deleteKeyInfo() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          message: "请选择一条数据",
          type: "info"
        });
        return;
      }
      debugger;
      let obj = {};
      obj.table_id =
        this.keyTableInfo.table_id + "," + this.elTableInfo.table_id;
      obj.table_foreigh_field =
        this.multipleSelection[0].K +
        "&" +
        this.multipleSelection[0].isExist +
        "," +
        this.multipleSelection[0].E;
      this.axios
        .post(interfaceObj.delForeignKey, obj)
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              message: "删除成功！",
              type: "success"
            });
            this.getForeignKeyData();
          } else {
            this.$message({
              message: "删除失败！",
              type: "error"
            });
          }
        })
        .catch(error => {});
    },
    getForeignKeyData() {
      this.axios
        .get(interfaceObj.TableStructure_getForeignKey, {
          params: {
            data_class_id: this.rowData.data_class_id,
            storage_type: this.rowData.storage_type,
            database_logic: this.rowData.logic_id
          }
        })
        .then(res => {
          this.tableData = [];
          let data = res.data.date.rows;
          for (let i = 0; i < data.length; i++) {
            let arr = data[i].K.split("&");
            if (arr.length < 2) continue;
            let obj = {};
            obj.E = data[i].E;
            obj.K = arr[0];
            obj.isExist = arr[1];
            this.tableData.push(obj);
          }
        })
        .catch(error => {});
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
