<template>
  <el-main class="PartKeyManage">
    <fieldset>
      <legend>{{rowData.database_name}}</legend>
      <el-form :model="editData" label-width="120px" size="small">
        <el-col :span="9">
          <el-form-item label="分库键">
            <el-select
              v-model="editData.database_key"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option value label="无"></el-option>
              <el-option
                v-for="item in columnData"
                :key="item.db_ele_code"
                :label="item.db_ele_code+'['+item.ele_name+']'"
                :value="item.db_ele_code"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="分表键">
            <el-select
              v-model="editData.table_key"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option value label="无"></el-option>
              <el-option
                v-for="item in columnData"
                :key="item.db_ele_code"
                :label="item.db_ele_code+'['+item.ele_name+']'"
                :value="item.db_ele_code"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button v-if="!isEdit" size="small" type="primary" @click="isEdit=!isEdit">编辑</el-button>
            <el-button v-if="isEdit" size="small" type="primary" @click="celFun">取消</el-button>
            <el-button v-if="isEdit" size="small" type="primary" @click="addKey">保存</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </fieldset>
  </el-main>
</template>

<script>
export default {
  name: "partKeyManage",
  props: { rowData: Object, tableInfo: Object },
  data() {
    return {
      columnData: [],
      editData: { database_key: "", table_key: "" },
      isEdit: false
    };
  },
  methods: {
    getColumnData() {
      this.axios
        .get(interfaceObj.TableStructure_getColumnInfo, {
          params: { table_id: this.tableInfo.table_id }
        })
        .then(res => {
          this.columnData = res.data.data;
        })
        .catch(error => {});
    },
    getShared() {
      this.axios
        .get(interfaceObj.TableStructure_getShareding, {
          params: { table_id: this.tableInfo.table_id }
        })
        .then(res => {
          let data = res.data.data;
          for (let i = 0; i < data.length; i++) {
            if (data[i].sharding_type === "D")
              this.editData.database_key = data[i].field;
            else this.editData.table_key = data[i].field;
          }
        })
        .catch(error => {});
    },
    celFun() {
      this.getShared();
      this.isEdit = !this.isEdit;
    },
    addKey() {
      let arry = [
        {
          database_id: this.rowData.database_id,
          field: this.editData.database_key,
          sharding_type: "D",
          table_id: this.tableInfo.table_id
        },
        {
          database_id: this.rowData.database_id,
          field: this.editData.table_key,
          sharding_type: "T",
          table_id: this.tableInfo.table_id
        }
      ];
      this.axios
        .post(interfaceObj.TableStructure_addShareding, { shardInfo: arry })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "操作成功"
            });
            this.isEdit = !this.isEdit;
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        })
        .catch(error => {});
    }
  },
  mounted() {},
  watch: {
    tableInfo(val) {
      // this.tableInfo = val;
      // this.getColumnData();
      // this.getShared();
    }
  }
};
</script>

<style lang="scss">
.PartKeyManage {
  fieldset {
    border: 1px solid #ebeef5;
    padding: 20px;
    margin-top: 12px;
  }
}
</style>
