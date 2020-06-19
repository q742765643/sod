<template>
  <div class="app-container">
    <!-- 区域类别管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="区域标识:">
        <el-input size="small" v-model.trim="queryParams.areaId" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <input-excel @getResult="getMyExcelData" btnText="导入"></input-excel>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" @click="tableExoprt()" icon="el-icon-download">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" @click="deleteCell" icon="el-icon-delete">删除</el-button>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleSelectionChange"
      ref="multipleTable"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="areaId" label="区域标识"></el-table-column>
      <el-table-column prop="startLat" label="开始纬度"></el-table-column>
      <el-table-column prop="endLat" label="结束纬度"></el-table-column>
      <el-table-column prop="startLon" label="开始经度"></el-table-column>
      <el-table-column prop="endLon" label="结束经度"></el-table-column>
      <el-table-column prop="areaDesc" label="备注"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      width="50%"
      highlight-current-row
      v-dialogDrag
    >
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="区域标识:" prop="areaId">
          <el-input v-model.trim="ruleForm.areaId" placeholder="请输入区域标识" />
        </el-form-item>
        <el-form-item label="开始纬度:" prop="startLat">
          <el-input v-model.trim.number="ruleForm.startLat" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="结束纬度:" prop="endLat">
          <el-input v-model.trim.number="ruleForm.endLat" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="开始经度:" prop="startLon">
          <el-input v-model.trim.number="ruleForm.startLon" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="结束经度:" prop="endLon">
          <el-input v-model.trim.number="ruleForm.endLon" type="number" placeholder="请输入结束经度" />
        </el-form-item>
        <el-form-item label="备注:" prop="areaDesc">
          <el-input type="textarea" v-model.trim="ruleForm.areaDesc" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleTrue('ruleForm')">确 定</el-button>
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 上传
import inputExcel from "@/components/excelXlsx/upload";
import {
  defineList,
  defineSave,
  defineEdit,
  defineDelete,
  detailById,
  exportTable
} from "@/api/GridDataDictionaryManagement/areaType";
export default {
  components: {
    inputExcel
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        areaId: ""
      },
      tableData: [],
      total: 0,

      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      ruleForm: {},
      rules: {
        areaId: [
          { required: true, message: "请输入区域标识", trigger: "blur" }
        ],
        startLat: [
          {
            required: true,
            message: "请输入数字格式的开始纬度",
            trigger: "blur"
          }
        ],
        endLat: [
          {
            required: true,
            message: "请输入数字格式的结束纬度",
            trigger: "blur"
          }
        ],
        startLon: [
          {
            required: true,
            message: "请输入数字格式的开始经度",
            trigger: "blur"
          }
        ],
        endLon: [
          {
            required: true,
            message: "请输入数字格式的结束经度",
            trigger: "blur"
          }
        ],
        areaDesc: [{ required: true, message: "请输入备注", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.tableData.forEach((element, index) => {
            if (element.id == row.id) {
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index]
              );
            }
          });
        });
      }
    },
    getMyExcelData(data) {
      // data 为读取的excel数据，在这里进行处理该数据
      console.log(data);
      data.forEach(element => {
        element.id = "";
      });
      defineSave(data).then(response => {
        if (response.code == 200) {
          this.msgSuccess("导入成功");
          this.handleQuery();
        } else {
          this.$message({
            type: "error",
            message: "操作失败"
          });
        }
      });
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      defineList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function() {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.ruleForm = {};
        this.dialogTitle = "新增";
      } else {
        if (this.multipleSelection.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据"
          });
          return;
        } else {
          detailById(this.multipleSelection[0].id).then(response => {
            this.ruleForm = response.data;
          });
        }
        this.dialogTitle = "编辑";
      }
      this.msgFormDialog = true;
    },
    handleTrue(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.dialogTitle == "新增") {
            defineSave(this.ruleForm).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.resetForm(formName);
                this.handleQuery();
              } else {
                this.msgError(response.msg);
              }
            });
          } else if (this.dialogTitle == "编辑") {
            defineEdit(this.ruleForm).then(response => {
              if (response.code === 200) {
                this.msgSuccess("编辑成功");
                this.resetForm(formName);
                this.handleQuery();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.msgFormDialog = false;
      this.getList();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteCell() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
        return;
      }
      let ids = [];
      let areaIds = [];
      this.multipleSelection.forEach(element => {
        ids.push(element.id);
        areaIds.push(element.areaId);
      });
      this.$confirm("是否删除" + areaIds.join(","), "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          defineDelete(ids.join(",")).then(response => {
            if (response.code === 200) {
              this.msgSuccess("删除成功");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    tableExoprt() {
      exportTable(this.queryParams).then(res => {
        this.downloadfileCommon(res);
      });
    }
  }
};
</script>
<style lang="scss">
.handleTableBox {
  .el-button.el-button--mini {
    padding: 9px 15px;
  }
}
</style>
