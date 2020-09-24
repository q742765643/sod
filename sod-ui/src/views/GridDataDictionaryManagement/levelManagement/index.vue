<template>
  <div class="app-container">
    <!-- 层次属性 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item size="small" label="GRIB格式:" prop="gribVersion">
        <el-input clearable v-model.trim="queryParams.gribVersion" placeholder="请输入GRIB格式" />
      </el-form-item>
      <el-form-item size="small" label="层次类型:" prop="levelType">
        <el-input clearable v-model.trim="queryParams.levelType" placeholder="请输入层次类型" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">编辑</el-button>
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
      <el-table-column prop="gribVersion" label="GRIB版本"></el-table-column>
      <el-table-column prop="levelType" label="层次类型"></el-table-column>
      <el-table-column prop="levelCode" label="层次代码"></el-table-column>
      <el-table-column prop="scaleDivisor" label="比例因子"></el-table-column>
      <el-table-column prop="levelProperity" label="英文描述" width="360" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="levelName" label="中文描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="unit" label="单位"></el-table-column>
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
      width="45%"
      v-dialogDrag
    >
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="grib格式:" prop="gribVersion">
          <el-input v-model.trim.number="ruleForm.gribVersion" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="层次类型:" prop="levelType">
          <el-input v-model.trim.number="ruleForm.levelType" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="层次代码:" prop="levelCode">
          <el-input v-model.trim="ruleForm.levelCode" />
        </el-form-item>
        <el-form-item label="比例因子:" prop="scaleDivisor">
          <el-input v-model.trim.number="ruleForm.scaleDivisor" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="英文描述:" prop="levelProperity">
          <el-input v-model.trim="ruleForm.levelProperity" />
        </el-form-item>
        <el-form-item label="中文描述:" prop="levelName">
          <el-input v-model.trim="ruleForm.levelName" />
        </el-form-item>
        <el-form-item label="单位:" prop="unit">
          <el-input v-model.trim="ruleForm.unit" />
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
import {
  levelList,
  levelSave,
  levelEdit,
  levelDelete,
} from "@/api/GridDataDictionaryManagement/levelManagement";
export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_fcst_ele: "",
      },
      tableData: [],
      total: 0,
      // 导入
      actionUrl: "",
      showFile: false,
      importHeaders: {
        enctype: "multipart/form-data",
      },
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      ruleForm: {},
      rules: {
        gribVersion: [
          { required: true, message: "请输入grib格式", trigger: "blur" },
        ],
        levelType: [
          { required: true, message: "请输入层次类型", trigger: "blur" },
        ],
        levelCode: [
          { required: true, message: "请输入层次代码", trigger: "blur" },
        ],
        scaleDivisor: [
          { required: true, message: "请输入比例因子", trigger: "blur" },
        ],
        levelProperity: [
          { required: true, message: "请输入英文描述", trigger: "blur" },
        ],
        levelName: [
          { required: true, message: "请输入中文描述", trigger: "change" },
        ],
        unit: [{ required: true, message: "请输入单位", trigger: "change" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
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
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      levelList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.ruleForm = {};
        this.dialogTitle = "添加";
      } else {
        if (this.multipleSelection.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return;
        } else {
          this.ruleForm = this.multipleSelection[0];
        }
        this.dialogTitle = "编辑";
      }
      this.msgFormDialog = true;
    },
    handleTrue(formName) {
      console.log(this.ruleForm);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogTitle == "添加") {
            levelSave(this.ruleForm).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.resetForm(formName);
              } else {
                this.msgError(response.msg);
              }
            });
          } else if (this.dialogTitle == "编辑") {
            levelEdit(this.ruleForm).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("编辑成功");
                this.resetForm(formName);
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
    beforeAvatarUpload(file) {
      const isJSON = file.type === "application/json";
      if (!isJSON) {
        this.$message.error("导入文件只能是 JSON 格式!");
      }
    },
    handleAvatarSuccess(res, file) {
      if (res.returnCode == 0) {
        this.$message({
          type: "success",
          message: "导入成功",
        });
        this.getList();
      } else {
        this.$message({
          type: "error",
          message: "导入失败",
        });
      }
      this.$refs.upload.clearFiles();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteCell() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      let ids = [];
      let levelNames = [];
      this.multipleSelection.forEach((element) => {
        ids.push(element.id);
        levelNames.push(element.levelName);
      });
      this.$confirm("是否删除" + levelNames.join(","), "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          levelDelete(ids.join(",")).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("删除成功");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    tableExoprt() {},
    superClick() {},
  },
};
</script>
