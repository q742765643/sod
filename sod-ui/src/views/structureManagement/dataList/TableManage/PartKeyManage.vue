<template>
  <el-main class="PartKeyManage">
    <fieldset>
      <legend>{{ rowData.tableName }}</legend>
      <el-form :model="editData" label-width="120px" size="small">
        <el-col :span="5">
          <el-form-item label="分区键">
            <el-select
              v-model.trim="editData.partitions"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option value label="无"></el-option>
              <el-option
                v-for="item in columnData"
                :key="item.dbEleCode"
                :label="item.dbEleCode + '[' + item.eleName + ']'"
                :value="item.dbEleCode"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="分区纬度">
            <el-input-number v-model="editData.partDimension"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="分区单位">
            <el-select
              v-model.trim="editData.partUnit"
              :disabled="!isEdit"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in partUnitList"
                :key="item.dbEleCode"
                :label="item.dictLabel"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="5" v-if="!this.rowData.MYDISABLED">
          <el-form-item>
            <el-button
              v-if="!isEdit"
              size="small"
              type="primary"
              @click="isEdit = !isEdit"
              >编辑</el-button
            >
            <el-button v-if="isEdit" size="small" type="primary" @click="celFun"
              >取消</el-button
            >
            <el-button v-if="isEdit" size="small" type="primary" @click="addKey"
              >保存</el-button
            >
          </el-form-item>
        </el-col>
      </el-form>
    </fieldset>
  </el-main>
</template>

<script>
import {
  shardingGet,
  shardingSaves,
} from "@/api/structureManagement/materialManage/StructureManageTable";
export default {
  name: "partKeyManage",
  props: { rowData: Object, tableInfo: Object },
  data() {
    return {
      partUnitList: [],
      columnData: [],
      editData: {
        id: this.rowData.TABLE_ID,
        partDimension: "",
        partUnit: "",
        partitions: "",
      },
      isEdit: false,
    };
  },
  methods: {
    // 获取分库分表键的值
    async getShared() {
      let id =
        this.rowData.TABLE_ID == undefined
          ? this.tableInfo.id
          : this.rowData.TABLE_ID;
      await shardingGet({ id: id }).then((response) => {
        if (response.data) {
          this.editData = response.data;
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
      shardingSaves(this.editData).then((response) => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功",
          });
          this.isEdit = !this.isEdit;
        } else {
          this.$message({
            type: "error",
            message: response.msg,
          });
        }
      });
    },
  },
  async mounted() {
    await this.getDicts("part_unit").then((response) => {
      this.partUnitList = response.data;
    });
  },
  watch: {
    tableInfo(val) {
      this.tableInfo = val;
      // 下拉框是字段的信息
      this.columnData = val.columns;
      this.getShared();
    },
  },
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
