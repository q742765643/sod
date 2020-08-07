<template>
  <el-main>
    <el-button-group style="padding: 8px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="addIndex">新增</el-button>
      <el-button type="primary" size="small" icon="el-icon-edit" @click="editIndex">编辑</el-button>
      <el-button type="primary" size="small" icon="el-icon-delete" @click="deleteIndex">删除</el-button>
    </el-button-group>

    <el-table
      :data="indexItem"
      border
      @selection-change="res=>indexItemSel=res"
      ref="selectionTable"
      @row-click="handleClickTableRow"
    >
      <el-table-column type="index" label="序号" width="50"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="索引名称" prop="indexName"></el-table-column>
      <el-table-column label="索引列" prop="indexColumn"></el-table-column>
      <el-table-column label="索引类型" prop="indexType"></el-table-column>
    </el-table>
    <el-dialog
      :close-on-click-modal="false"
      width="65%"
      :title="indexTitle"
      :visible.sync="dialogStatus.indexDialog"
      append-to-body
      v-dialogDrag
    >
      <div>
        <el-form :model="indexForm" label-width="120px" ref="indexFormRef" :rules="indexRules">
          <el-form-item label="索引名称" prop="indexName">
            <el-input v-model.trim="indexForm.indexName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="索引类型" prop="indexType">
            <el-select
              v-model.trim="indexForm.indexType"
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
          <el-form-item label="索引字段" prop="indexColumn">
            <el-select
              filterable
              size="small"
              v-model.trim="indexForm.indexColumn"
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
  tableIndexSave,
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  props: { tableInfo: Object },
  data() {
    const nameValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入索引名称"));
      } else {
        let flag = false;
        this.indexItem.forEach((element) => {
          if (element.indexName == value && value != this.indexForm.indexName) {
            console.log("重复");
            flag = true;
            return;
          }
        });
        if (flag) {
          callback(new Error("索引名称不能重复"));
        } else {
          callback();
        }
      }
    };
    return {
      indexForm: { indexName: this.tableInfo.tableName, indexColumn: [] },
      indexItem: [],
      indexItemSel: [],
      columnData: [],
      indexTypeOptions: [],
      dialogStatus: { indexDialog: false },
      indexTitle: "新增索引",
      indexRules: {
        indexName: [
          { required: true, validator: nameValidate, trigger: "blur" },
        ],
        indexType: [
          { required: true, message: "请选择索引类型", trigger: "change" },
        ],
        indexColumn: [
          { required: true, message: "请选择索引字段", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    // 选中就勾选
    handleClickTableRow(row, event, column) {
      this.$refs.selectionTable.toggleRowSelection(row);
    },
    cancel() {
      this.$refs["indexFormRef"].resetFields();
      this.dialogStatus.indexDialog = false;
    },
    getIndexTable() {
      findTableIndex({ tableId: this.tableInfo.id }).then((res) => {
        if (res.code == 200) {
          this.indexItem = res.data;
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
    },
    addIndex() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }
      this.indexForm = {
        indexName: this.tableInfo.tableName,
        indexColumn: [],
      };
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
          type: "warning",
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
      getDictByType({ dictType: dictType }).then((response) => {
        if (response.code == 200) {
          this.indexTypeOptions = response.data;
        }
      });
    },
    trueIndex() {
      this.$refs["indexFormRef"].validate((valid) => {
        if (valid) {
          this.indexForm.tableId = this.tableInfo.id;
          this.indexForm.indexColumn = this.indexForm.indexColumn.join(",");
          console.log(this.indexForm);
          tableIndexSave(this.indexForm).then((response) => {
            if (response.code == 200) {
              this.$message({ message: "操作成功", type: "success" });
              this.getIndexTable();
              this.$refs["indexFormRef"].resetFields();
              this.dialogStatus.indexDialog = false;
            } else {
              this.$message({
                type: "error",
                message: "操作失败",
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    deleteIndex() {
      if (this.indexItemSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning",
        });
      } else {
        this.$confirm(
          "是否删除索引名称为" + this.indexItemSel[0].indexName + "的数据?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            delTableIndex({ id: this.indexItemSel[0].id }).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.getIndexTable();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          })
          .catch(() => {});
      }
    },
  },

  mounted() {},
  watch: {
    async tableInfo(val) {
      this.indexItem = this.tableInfo.tableIndexList;
      this.columnData = this.tableInfo.columns;
    },
  },
};
</script>
