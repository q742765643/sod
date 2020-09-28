<template>
  <div class="app-container">
    <!-- 服务代码管理 -->
    <el-form :inline="true" :model="queryParams" ref="modelForm" class="searchBox">
      <el-form-item label="服务代码:" prop="userEleCode">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.userEleCode"
          placeholder="请输入服务代码名称"
        />
      </el-form-item>
      <el-form-item label="字段编码:" prop="dbEleCode">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.dbEleCode"
          placeholder="请输入字段编码"
        />
      </el-form-item>
      <el-form-item label="要素中文名称:" prop="eleName">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.eleName"
          placeholder="请输入要素中文名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showAddDialog()" icon="el-icon-plus">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" @click="deleteAlert()" icon="el-icon-delete">删除</el-button>
      </el-col>
    </el-row>
    <el-table
      border
      :data="tableData"
      stripe
      highlight-current-row
      @selection-change="handleSelectionChange"
      style="width: 100%;"
      ref="multipleTable"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userEleCode" label="服务代码" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="dbEleCode" label="字段编码"></el-table-column>
      <el-table-column prop="eleName" label="要素中文名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="eleUnit" label="单位" width="120"></el-table-column>
      <el-table-column prop="isCodeParam" label="是否有标识代码表" width="140"></el-table-column>
      <el-table-column prop="codeTableId" label="标识代码表" width="120"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            type="text"
            size="mini"
            @click="showEditDialog(scope.row)"
          >编辑</el-button>
          <el-button
            icon="el-icon-delete"
            type="text"
            size="mini"
            @click="deleteSingleCode(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="handleQuery"
    />
    <!-- 新增-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="DataDialog"
      width="45%"
      class="calculate"
      v-dialogDrag
    >
      <el-form :model="handleObj" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="服务代码:" prop="userEleCode" class="labelitem">
          <el-input v-model.trim="handleObj.userEleCode" placeholder="请输入服务代码名称" />
        </el-form-item>
        <el-form-item label="字段编码:" prop="dbEleCode" class="labelitem">
          <el-input v-model.trim="handleObj.dbEleCode" placeholder="请输入字段编码" />
        </el-form-item>
        <el-form-item label="要素名称:" prop="eleName" class="labelitem">
          <el-input v-model.trim="handleObj.eleName" placeholder="请输入要素中文名称" />
        </el-form-item>
        <el-form-item label="单位名称:" prop="eleUnit" class="labelitem">
          <el-input v-model.trim="handleObj.eleUnit" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="是否有标识代码:" prop="isCodeParam" class="labelitem">
          <el-input v-model.trim="handleObj.isCodeParam" placeholder="请输入是否标识代码" />
        </el-form-item>
        <el-form-item label="标识代码表:" prop="codeTableId" class="labelitem">
          <el-input v-model.trim="handleObj.codeTableId" placeholder="请输入标识代码表" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueReportRecord('ruleForm')">确 定</el-button>
        <el-button @click="DataDialog= false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { EngNumLine } from "@/components/commonVaildate.js";
import {
  defineList,
  addCode,
  editCode,
  getById,
  deleteById,
  deleteByIds,
} from "@/api/dbDictMangement/dbServiceCodeManagement";
export default {
  name: "filedSearchDeploy",
  data() {
    const ENLValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入服务代码"));
      } else if (!EngNumLine(value)) {
        callback(new Error("服务代码只能以英文数字和下划线组成"));
      } else {
        callback();
      }
    };
    return {
      //查询条件
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userEleCode: "",
        dbEleCode: "",
        eleName: "",
      },
      editdataObj: {},
      //新增
      handleObj: {
        userEleCode: "",
        dbEleCode: "",
        eleName: "",
        eleUnit: "",
        isCodeParam: "",
        codeTableId: "",
      },
      DataDialog: false,
      //表格
      tableData: [],
      //当前选中列
      multipleSelection: [],
      total: 0,
      //删除确认信息
      dialogVisible: false,
      rules: {
        userEleCode: [
          // { required: true, validator: ENLValidate, trigger: "blur" }
          { required: true, message: "请输入服务代码名称", trigger: "blur" },
        ],
        dbEleCode: [
          { required: true, message: "请输入字段编码", trigger: "blur" },
        ],
        eleName: [
          { required: true, message: "请输入要素中文名称", trigger: "blur" },
        ],
        eleUnit: [
          { required: true, message: "请输入单位名称", trigger: "blur" },
        ],
        isCodeParam: [
          { required: true, message: "请输入是否标识代码", trigger: "blur" },
        ],
        codeTableId: [
          { required: true, message: "请输入标识代码表", trigger: "blur" },
        ],
      },
      currentRow: {},
    };
  },
  mounted() {
    this.handleQuery();
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    handleQuery() {
      this.loading = true;
      defineList(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.multipleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    resetQuery() {
      this.$refs["modelForm"].resetFields();
      this.handleQuery();
    },
    //新增
    showAddDialog() {
      this.dialogTitle = "新增";
      this.handleObj = {};
      this.DataDialog = true;
    },
    //编辑
    showEditDialog(row) {
      this.dialogTitle = "编辑";
      this.currentRow = row;
      // this.handleObj = row;
      getById({ id: row.id }).then((res) => {
        if (res.code == 200) {
          this.handleObj = res.data;
          this.DataDialog = true;
        } else {
          this.msgError(res.msg);
        }
      });
    },
    cancelMsgHandle() {
      this.editVisible = false;
      this.handleQuery();
    },
    /**删除数据*/
    deleteSingleCode(row) {
      this.$confirm("确定要删除吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          console.log(row.id);
          deleteById({ id: row.id }).then((res) => {
            if (res.code == 200) {
              this.msgSuccess("删除成功");
              this.handleQuery();
            } else {
              this.msgError(res.msg);
            }
          });
        })
        .catch(() => {});
    },
    deleteAlert() {
      if (this.multipleSelection.length == 0) {
        this.$message.error("请选择需要删除的记录！");
        return;
      } else {
        this.$confirm("确定要删除选中项吗?", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let ids = [];
            this.multipleSelection.forEach((element) => {
              ids.push(element.id);
            });
            return deleteByIds(ids.join(","));
          })
          .then(() => {
            this.msgSuccess("删除成功");
            this.handleQuery();
          })
          .catch(() => {});
      }
    },
    //选中行
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //保存
    trueReportRecord(formName) {
      console.log(this.handleObj);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.handleObj.id) {
            editCode(this.handleObj).then((res) => {
              if (res.code == 200) {
                this.msgSuccess("编辑成功");
                this.handleQuery();
                this.$refs[formName].resetFields();
                this.DataDialog = false;
              } else {
                this.msgError(res.msg);
              }
            });
          } else {
            addCode(this.handleObj).then((res) => {
              if (res.code == 200) {
                this.msgSuccess("新增成功");
                this.handleQuery();
                this.$refs[formName].resetFields();
                this.DataDialog = false;
              } else {
                this.msgError(res.msg);
              }
            });
          }
        }
      });
    },
  },
};
</script>
