<template>
  <el-main>
    <el-form
      :model="formData"
      label-width="130px"
      size="small"
      style="border: 1px; padding: 5px"
    >
      <el-col :span="8">
        <el-form-item label="关联表">
          <el-select
            filterable
            v-model.trim="formData.subTableId"
            placeholder="请选择外键关联表"
            style="width: 100%"
            @change="getKeyColumnData"
          >
            <el-option
              v-for="(item, index) in linkTableList"
              :key="index"
              :label="item.TABLE_NAME + '(' + item.NAME_CN + ')'"
              :value="item.ID"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="键表字段">
          <el-select
            filterable
            v-model.trim="formData.keyColumn"
            placeholder="请选择键表字段"
            style="width: 100%"
          >
            <el-option
              v-for="item in keyColumnData"
              :key="item.dbEleCode"
              :label="item.dbEleCode"
              :value="item.dbEleCode"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="4">
        <el-form-item label="要素表字段">
          <el-select
            filterable
            v-model.trim="formData.eleColumn"
            placeholder="请选择要素表字段"
            style="width: 100%"
          >
            <el-option
              v-for="item in elColumnData"
              :key="item.dbEleCode"
              :label="item.dbEleCode + '[' + item.eleName + ']'"
              :value="item.dbEleCode"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="3">
        <el-form-item label="是否存在真实外键">
          <el-select
            v-model.trim="formData.isReal"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option label="存在" value="true"></el-option>
            <el-option label="不存在" value="false"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="3">
        <el-button
          type="primary"
          size="small"
          style="margin-left: 20px"
          @click="add"
          >添加</el-button
        >
        <el-button type="danger" size="small" @click="deleteKeyInfo"
          >删除</el-button
        >
      </el-col>
    </el-form>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="KEY_COLUMN" label="键表字段"></el-table-column>
      <el-table-column prop="ELE_COLUMN" label="要素表字段"></el-table-column>
      <el-table-column prop="IS_REAL" label="是否存在真实外键">
        <template slot-scope="scope">
          <span v-if="scope.row.IS_REAL == true">是</span>
          <span v-if="scope.row.IS_REAL == false">否</span>
        </template>
      </el-table-column>
    </el-table>
  </el-main>
</template>

<script>
import {
  findByTableId,
  foreignKeySave,
  delByIdsKey,
  foreignKeyByTableId,
} from "@/api/structureManagement/materialManage/StructureManageTable";

import { findBySubType } from "@/api/structureManagement/overviewStorage";
export default {
  name: "ForeignKeyManage",
  props: { rowData: Object, tableInfo: Object },
  data() {
    return {
      tableData: [],
      formData: {
        tableId: this.rowData.ID,
        keyColumn: "",
        eleColumn: "",
        isReal: "true",
      },
      keyColumnData: [],
      elColumnData: this.tableInfo.columns,
      multipleSelection: [],
      linkTableList: [],
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 根据键表ID查询键表字段
    getKeyColumnData(val) {
      findByTableId({ tableId: this.formData.subTableId }).then((response) => {
        if (response.code == 200) {
          this.keyColumnData = response.data;
        }
      });
    },
    add() {
      foreignKeySave(this.formData).then((response) => {
        if (response.code == 200) {
          this.$message({
            message: "外键关联新增成功！",
            type: "success",
          });
          this.getForeignKeyData();
        } else {
          this.$message({
            message: response.msg,
            type: "error",
          });
        }
      });
    },
    deleteKeyInfo() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选择一条数据",
          type: "info",
        });
        return;
      } else {
        let ids = [];
        let keyColumns = [];
        this.multipleSelection.forEach((element) => {
          ids.push(element.ID);
          keyColumns.push(element.KEY_COLUMN);
        });
        this.$confirm("确认删除" + keyColumns.join(",") + "吗?", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            delByIdsKey({ ids: ids.join(",") }).then((response) => {
              if (response.code == 200) {
                this.$message({
                  message: "外键关联删除成功！",
                  type: "success",
                });
                this.getForeignKeyData();
              } else {
                this.$message({
                  message: response.msg,
                  type: "error",
                });
              }
            });
          })
          .catch(() => {});
      }
    },
    //外键关联表格
    getForeignKeyData() {
      foreignKeyByTableId({ tableId: this.rowData.ID }).then((response) => {
        if (response.code == 200) {
          this.tableData = response.data;
        }
      });
    },
  },
  mounted() {
    console.log(this.tableInfo);
    //关联表下拉框
    findBySubType({
      storageType: this.rowData.STORAGE_TYPE,
      tableType: this.rowData.TABLE_TYPE,
    }).then((response) => {
      if (response.code == 200) {
        this.linkTableList = response.data;
      }
    });
    //外键关联表格
    this.getForeignKeyData();
  },
  watch: {
    tableInfo(val) {
      this.elColumnData = val.columns;
    },
  },
};
</script>

<style scoped>
</style>
