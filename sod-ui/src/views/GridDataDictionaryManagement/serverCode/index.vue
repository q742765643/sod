<template>
  <div class="app-container">
    <!-- 服务代码定义 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="格点要素服务代码:" prop="userFcstEle">
        <el-input clearable size="small" v-model.trim="queryParams.userFcstEle" />
      </el-form-item>
      <el-form-item label="要素存储短名:" prop="dbFcstEle">
        <el-input clearable size="small" v-model.trim="queryParams.dbFcstEle" />
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
      <el-table-column prop="userFcstEle" label="要素服务代码"></el-table-column>
      <el-table-column prop="dbFcstEle" label="要素存储短名"></el-table-column>
      <el-table-column prop="eleName" label="中文名称"></el-table-column>
      <el-table-column prop="elePropertyName" label="属性名(要素长名)" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="eleUnit" label="要素单位"></el-table-column>
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
      v-dialogDrag
    >
      <el-form :model="ruleForm" :rules="rules" label-width="150px" ref="ruleForm">
        <el-form-item label="要素服务代码:" prop="userFcstEle">
          <el-input v-model.trim="ruleForm.userFcstEle" placeholder="请输入要素服务代码" />
        </el-form-item>
        <el-form-item label="要素存储短名:" prop="dbFcstEle">
          <el-input v-model.trim="ruleForm.dbFcstEle" placeholder="请输入要素存储短名" />
        </el-form-item>
        <el-form-item label="中文名称:" prop="eleName">
          <el-input v-model.trim="ruleForm.eleName" placeholder="请输入中文名称" />
        </el-form-item>
        <el-form-item label="属性名(要素长名):" prop="elePropertyName">
          <el-input v-model.trim="ruleForm.elePropertyName" placeholder="请输入属性名(要素长名)" />
        </el-form-item>
        <el-form-item label="要素单位:" prop="eleUnit">
          <el-input v-model.trim="ruleForm.eleUnit" placeholder="请输入要素单位" />
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
  gridEleServiceDefineAll,
  gridEleServiceDefineAdd,
  gridEleServiceDefineEdit,
  gridEleServiceDefineDelete,
} from "@/api/GridDataDictionaryManagement/serverCode";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""],
      },
      tableData: [],
      total: 0,
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      ruleForm: {},
      rules: {
        userFcstEle: [
          { required: true, message: "请输入要素服务代码", trigger: "blur" },
        ],
        dbFcstEle: [
          { required: true, message: "请输入要素存储短名", trigger: "blur" },
        ],
        eleName: [
          { required: true, message: "请输入中文名称", trigger: "blur" },
        ],
        elePropertyName: [
          {
            required: true,
            message: "请输入属性名(要素长名)",
            trigger: "blur",
          },
        ],
        eleUnit: [
          { required: true, message: "请输入要素单位", trigger: "blur" },
        ],
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
      gridEleServiceDefineAll(this.queryParams).then((response) => {
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
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    showDialog(type) {
      if (type == "add") {
        this.ruleForm = {};
        this.dialogTitle = "添加";
        this.msgFormDialog = true;
      } else {
        if (this.multipleSelection.length == 1) {
          this.dialogTitle = "编辑";
          this.msgFormDialog = true;
          console.log(this.multipleSelection);
          this.ruleForm = this.multipleSelection[0];
        } else {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return;
        }
      }
    },
    handleTrue(formName) {
      console.log(this.ruleForm);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogTitle == "添加") {
            gridEleServiceDefineAdd(this.ruleForm).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.$refs[formName].resetFields();
                this.msgFormDialog = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else if (this.dialogTitle == "编辑") {
            gridEleServiceDefineEdit(this.ruleForm).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("编辑成功");
                this.$refs[formName].resetFields();
                this.msgFormDialog = false;
                this.getList();
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
    deleteCell() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      let ids = [];
      let userFcstEles = [];
      this.multipleSelection.forEach((element) => {
        ids.push(element.id);
        userFcstEles.push(element.userFcstEle);
      });
      this.$confirm("是否删除" + userFcstEles.join(","), "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          gridEleServiceDefineDelete(ids.join(",")).then((response) => {
            if (response.code == 200) {
              this.msgSuccess("删除成功");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.msgFormDialog = false;
    },
  },
};
</script>
