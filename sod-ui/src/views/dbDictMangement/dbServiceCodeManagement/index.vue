<template>
  <div class="app-container">
    <!-- 服务代码管理 -->
    <el-form :inline="true" :model="queryParams" ref="modelForm" class="funLoad selSearchCon">
      <el-form-item label="服务代码:">
        <el-input size="small" v-model="queryParams.user_ele_code" placeholder="请输入服务代码名称" />
      </el-form-item>
      <el-form-item label="字段编码:">
        <el-input size="small" v-model="queryParams.db_ele_code" placeholder="请输入字段编码" />
      </el-form-item>
      <el-form-item label="要素中文名称:">
        <el-input size="small" v-model="queryParams.ele_name" placeholder="请输入要素中文名称" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showAddDialog()" icon="el-icon-plus">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showEditDialog()" icon="el-icon-edit">编辑</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button size="small" type="danger" @click="deleteAlert()" icon="el-icon-delete">删除</el-button>
      </el-col>-->
    </el-row>
    <el-table
      :data="tableData"
      stripe
      highlight-current-row
      @current-change="handleSelectionChange"
      style="width: 100%;"
    >
      <el-table-column type="index" width="50" :index="table_index"></el-table-column>
      <!-- <el-table-column width="40">
        <template slot-scope="scope">
          <el-checkbox v-model="scope.row.checked"></el-checkbox>
        </template>
      </el-table-column>-->
      <el-table-column prop="user_ele_code" label="服务代码" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="db_ele_code" label="字段编码"></el-table-column>
      <el-table-column prop="eleName" label="要素中文名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="ele_unit" label="单位" width="120"></el-table-column>
      <el-table-column prop="is_code_param" label="是否有标识代码表"></el-table-column>
      <el-table-column prop="code_table_id" label="标识代码表"></el-table-column>
      <el-table-column prop="code_table_id" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-delete"
            type="text"
            size="mini"
            @click="deleteSingleCode(scope.row.user_ele_code)"
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
    <!-- 添加-->
    <el-dialog title="添加" :visible.sync="DataDialog" width="45%" class="calculate">
      <el-form :model="handleObj" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="服务代码:" prop="user_ele_code" class="labelitem">
          <el-input v-model="handleObj.user_ele_code" placeholder="请输入服务代码名称" />
        </el-form-item>
        <el-form-item label="字段编码:" prop="db_ele_code" class="labelitem">
          <el-input v-model="handleObj.db_ele_code" placeholder="请输入字段编码" />
        </el-form-item>
        <el-form-item label="要素名称:" prop="ele_name" class="labelitem">
          <el-input v-model="handleObj.ele_name" placeholder="请输入要素中文名称" />
        </el-form-item>
        <el-form-item label="单位名称:" prop="ele_unit" class="labelitem">
          <el-input v-model="handleObj.ele_unit" placeholder="请输入单位名称" />
        </el-form-item>
        <el-form-item label="是否有标识代码:" prop="is_code_param" class="labelitem">
          <el-input v-model="handleObj.is_code_param" placeholder="请输入是否标识代码" />
        </el-form-item>
        <el-form-item label="标识代码表:" prop="code_table_id" class="labelitem">
          <el-input v-model="handleObj.code_table_id" placeholder="请输入标识代码表" />
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
import {
  defineList,
  deleteCode
} from "@/api/dbDictMangement/dbServiceCodeManagement";
export default {
  name: "filedSearchDeploy",
  data() {
    return {
      //查询条件
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_ele_code: "",
        db_ele_code: "",
        ele_name: ""
      },
      editdataObj: {},
      //添加
      handleObj: {
        user_ele_code: "",
        db_ele_code: "",
        ele_name: "",
        ele_unit: "",
        is_code_param: "",
        code_table_id: ""
      },
      DataDialog: false,
      //表格
      tableData: [],
      //当前选中列
      currentRow: null,
      total: 0,
      //删除确认信息
      dialogVisible: false,
      rules: {
        user_ele_code: [
          { required: true, message: "请输入服务代码名称", trigger: "blur" }
        ],
        db_ele_code: [
          { required: true, message: "请输入字段编码", trigger: "blur" }
        ],
        ele_name: [
          { required: true, message: "请输入要素中文名称", trigger: "blur" }
        ],
        ele_unit: [
          { required: true, message: "请输入单位名称", trigger: "blur" }
        ],
        is_code_param: [
          { required: true, message: "请输入是否标识代码", trigger: "blur" }
        ],
        code_table_id: [
          { required: true, message: "请输入标识代码表", trigger: "blur" }
        ]
      }
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
      defineList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },

    //添加
    showAddDialog() {
      this.DataDialog = true;
    },
    cancelMsgHandle() {
      this.editVisible = false;
      this.handleQuery();
    },
    /**删除数据*/
    deleteSingleCode(id) {
      this.$confirm("确定要删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteCode({ id: id }).then(res => {
            res.success
              ? (this.$message({
                  type: "success",
                  message: "删除成功!"
                }),
                this.handleQuery())
              : this.$message({
                  type: "error",
                  message: "删除失败!"
                });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    deleteAlert() {
      return;
      if (this.currentRow == null) {
        this.$message.error("请选择需要删除的记录！");
      } else {
        var ids = [];
        ids.push(this.currentRow.user_ele_code);
        if (ids.length == 0) {
          this.$message.error("请选择需要删除的记录！");
          return;
        }
        this.axios
          .post(
            interfaceObj.dbServiceCodeManagement_delDataEleById,
            "ids=" + ids
          )
          .then(res => {
            this.currentRow = "";
            this.handleQuery();
            if (res.data.returnCode == 0) {
              this.$message({ message: "删除成功!", type: "success" });
            } else {
              this.$message.error("删除失败");
            }
          });
      }
    },
    //选中行
    handleSelectionChange(val) {
      this.currentRow = val;
      for (var i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].user_ele_code !== val.user_ele_code) {
          this.tableData[i].checked = false;
        } else {
          this.tableData[i].checked = true;
        }
      }
    },
    //保存
    trueReportRecord(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // this.axios
          //   .post(
          //     interfaceObj.dbServiceCodeManagement_addDataEle,
          //     this.handleObj
          //   )
          //   .then(res => {
          //     this.currentRow = "";
          //     this.handleQuery();
          //     this.DataDialog = false;
          //     this.handleObj.user_ele_code = "";
          //     this.handleObj.db_ele_code = "";
          //     this.handleObj.ele_name = "";
          //     this.handleObj.ele_unit = "";
          //     this.handleObj.is_code_param = "";
          //     this.handleObj.code_table_id = "";
          //     if (res.data.returnCode == 0) {
          // this.$refs[formName].resetFields();
          //       this.$message({ message: "新增成功!", type: "success" });
          //     } else {
          //       this.$message.error("新增失败");
          //     }
          //   });
        }
      });
    },
    //分页事件

    //编辑
    showEditDialog() {
      if (!this.currentRow) {
        this.$message({ message: "请选择需要修改的记录！!", type: "error" });
      } else {
        this.editdataObj = this.currentRow;
        // if (this.currentRow.length == 1) {
        //   for (let index = 0; index < this.currentRow.length; index++) {
        //     this.editdataObj = this.currentRow[index];
        //   }
        //   this.editDataDialog = true;
        // } else if (this.currentRow.length > 1) {
        //   this.$message.error("请选择一条需要修改的记录！");
        // } else if (this.currentRow.length == 0) {
        //   this.$message.error("请选择需要修改的记录！");
        // }
      }
    }
  }
};
</script>
