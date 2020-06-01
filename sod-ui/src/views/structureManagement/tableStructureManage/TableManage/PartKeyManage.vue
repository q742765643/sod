<template>
  <el-main class="PartKeyManage">
    <fieldset>
      <legend>{{rowData.database_name}}</legend>
      <el-form :model="editData" label-width="120px" size="small">
        <el-col :span="9">
          <el-form-item label="分库键">
            <el-select
              v-model.trim="editData.database_key"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option value label="无"></el-option>
              <el-option
                v-for="item in columnData"
                :key="item.dbEleCode"
                :label="item.dbEleCode+'['+item.eleName+']'"
                :value="item.dbEleCode"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="分表键">
            <el-select
              v-model.trim="editData.table_key"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%;"
            >
              <el-option value label="无"></el-option>
              <el-option
                v-for="item in columnData"
                :key="item.dbEleCode"
                :label="item.dbEleCode+'['+item.eleName+']'"
                :value="item.dbEleCode"
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
import {
  getByTableId,
  findByTableId,
  shardingSaves
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
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
    // 获取分库分表键的值
    getShared() {
      getByTableId({ id: this.tableInfo.id }).then(response => {
        if (response.code == 200) {
          let data = response.data;
          for (let i = 0; i < data.length; i++) {
            if (data[i].shardingType === 0)
              //分库键
              this.editData.database_key = data[i].columnName;
            else this.editData.table_key = data[i].columnName;
          }
        }
      });
    },
    // 取消
    celFun() {
      this.getShared();
      this.isEdit = !this.isEdit;
    },
    // 保存
    addKey() {
      let arry = [
        // 分库
        {
          columnName: this.editData.database_key,
          shardingType: "0",
          tableId: this.tableInfo.id
        },
        // 分表
        {
          columnName: this.editData.table_key,
          shardingType: "1",
          tableId: this.tableInfo.id
        }
      ];
      console.log(arry);
      shardingSaves({ shardingList: arry }).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功"
          });
          this.isEdit = !this.isEdit;
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    }
  },
  mounted() {},
  watch: {
    tableInfo(val) {
      this.tableInfo = val;
      // 下拉框是字段的信息
      this.columnData = val.columns;
      this.getShared();
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
