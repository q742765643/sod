<template>
  <el-main>
    <el-button-group style="padding: 8px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="addIndex">新增</el-button>
      <el-button type="primary" size="small" icon="el-icon-edit" @click="editIndex">编辑</el-button>
      <el-button type="primary" size="small" icon="el-icon-delete" @click="deleteIndex">删除</el-button>
    </el-button-group>

    <el-table :data="indexItem" border @selection-change="res=>indexItemSel=res">
      <el-table-column type="index" align="center"></el-table-column>
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column label="索引名称" prop="indexName" align="center"></el-table-column>
      <el-table-column label="索引列" prop="indexColumn" align="center"></el-table-column>
      <el-table-column label="索引类型" prop="indexType" align="center"></el-table-column>
    </el-table>
    <el-dialog
      width="65%"
      :title="indexTitle"
      :visible.sync="dialogStatus.indexDialog"
      append-to-body
      v-dialogDrag
    >
      <div>
        <el-form :model="indexForm" label-width="120px" ref="indexForm">
          <el-form-item label="索引名称">
            <el-input v-model="indexForm.indexName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="索引类型">
            <el-select
              v-model="indexForm.indexType"
              size="small"
              placeholder="请选择索引类型"
              style="width: 100%;"
            >
              <el-option
                v-for="item in indexTypeOptions"
                :key="item.dictValue"
                :label="item.dictValue+'['+item.dictLabel+']'"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="索引字段">
            <el-select
              size="small"
              v-model="indexForm.indexColumn"
              multiple
              placeholder="请选择索引字段"
              style="width: 100%;"
            >
              <el-option
                v-for="item in columnData"
                :key="item.dbEleCode"
                :label="item.dbEleCode+'['+item.eleName+']'"
                :value="item.dbEleCode"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueIndex">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
import {
  findTableIndex,
  delTableIndex,
  getDictByType,
  tableIndexSave
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  props: { tableInfo: Object },
  data() {
    return {
      indexForm: { indexName: "TRAF_WEA_CHN_REP_TAB_UK", indexColumn: [] },
      indexItem: [],
      indexItemSel: [],
      columnData: [],
      indexTypeOptions: [],
      dialogStatus: { indexDialog: false },
      indexTitle: "新增索引"
    };
  },
  methods: {
    cancel() {
      this.$refs["indexForm"].resetFields();
      this.dialogStatus.indexDialog = false;
    },
    getIndexTable() {
      findTableIndex({ tableId: this.tableInfo.id }).then(res => {
        if (res.code == 200) {
          this.indexItem = res.data;
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    },
    addIndex() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在"
        });
        return;
      }
      this.indexTitle = "新增索引";
      this.indexForm.indexColumn = [];
      this.getDictByTypeMethods("table_index_type");
      // this.indexForm = {};
      this.dialogStatus.indexDialog = true;
    },
    editIndex() {
      if (this.indexItemSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        this.getDictByTypeMethods("table_index_type");
        this.indexTitle = "编辑索引";
        this.indexForm = JSON.parse(JSON.stringify(this.indexItemSel[0]));
        this.indexForm.indexColumn = this.indexForm.indexColumn.split(",");
        this.dialogStatus.indexDialog = true;
      }
    },
    // 根据类型查询字典信息
    getDictByTypeMethods(dictType) {
      getDictByType({ dictType: dictType }).then(response => {
        if (response.code == 200) {
          this.indexType = response.data;
        }
      });
    },
    trueIndex() {
      this.indexForm.id = this.tableInfo.id;
      this.indexForm.indexColumn = this.indexForm.indexColumn.join(",");
      console.log(this.indexForm);
      tableIndexSave(this.indexForm).then(response => {
        if (response.code == 200) {
          this.$message({ message: "操作成功", type: "success" });
          this.getIndexTable();
          this.$refs["indexForm"].resetFields();
          this.dialogStatus.indexDialog = false;
        } else {
          this.$message({
            type: "error",
            message: "操作失败"
          });
        }
      });
    },

    deleteIndex() {
      if (this.indexItemSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        delTableIndex({ id: this.indexItemSel[0].id }).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功"
            });
            this.getIndexTable();
          } else {
            this.$message({
              message: res.msg,
              type: "error"
            });
          }
        });
      }
    }
  },

  mounted() {},
  watch: {
    async tableInfo(val) {
      this.indexItem = this.tableInfo.tableIndexList;
      this.columnData = this.tableInfo.columns;
    }
  }
};
</script>
