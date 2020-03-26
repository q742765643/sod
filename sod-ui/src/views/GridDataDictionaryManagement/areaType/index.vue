<template>
  <div class="app-container">
    <!-- 区域类别管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="区域标识:">
        <el-input size="small" v-model="queryParams.areaId" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
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
        <el-upload
          class="upload-demo"
          :action="actionUrl"
          multiple
          :limit="1"
          :show-file-list="showFile"
          :before-upload="beforeAvatarUpload"
          :on-success="handleAvatarSuccess"
          :header="importHeaders"
          ref="upload"
        >
          <el-button size="small" type="success" icon="el-icon-upload2">导入</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="success" @click="tableExoprt()" icon="el-icon-download">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" @click="deleteCell" icon="el-icon-delete">删除</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="50"></el-table-column>
      <el-table-column align="center" prop="areaId" label="区域标识"></el-table-column>
      <el-table-column align="center" prop="startLat" label="开始纬度"></el-table-column>
      <el-table-column align="center" prop="endLat" label="结束纬度"></el-table-column>
      <el-table-column align="center" prop="startLon" label="开始经度"></el-table-column>
      <el-table-column align="center" prop="endLon" label="结束经度"></el-table-column>
      <el-table-column align="center" prop="areaDesc" label="备注"></el-table-column>
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
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      width="50%"
      highlight-current-row
      v-dialogDrag
    >
      <el-form :model="ruleForm" :rules="rules" label-width="130px" ref="ruleForm">
        <el-form-item label="区域标识:" prop="areaId">
          <el-input v-model="ruleForm.areaId" placeholder="请输入区域标识" />
        </el-form-item>
        <el-form-item label="开始纬度:" prop="startLat">
          <el-input v-model.number="ruleForm.startLat" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="结束纬度:" prop="endLat">
          <el-input v-model.number="ruleForm.endLat" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="开始经度:" prop="startLon">
          <el-input v-model.number="ruleForm.startLon" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="结束经度:" prop="endLon">
          <el-input v-model.number="ruleForm.endLon" type="number" placeholder="请输入结束经度" />
        </el-form-item>
        <el-form-item label="备注:" prop="areaDesc">
          <el-input type="textarea" v-model="ruleForm.areaDesc" />
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
  defineList,
  defineSave,
  defineEdit,
  defineDelete,
  detailById
} from "@/api/GridDataDictionaryManagement/areaType";
export default {
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
      // 导入
      actionUrl: "",
      showFile: false,
      importHeaders: {
        enctype: "multipart/form-data"
      },
      choserow: [],
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
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.ruleForm = {};
        this.dialogTitle = "添加";
      } else {
        if (this.choserow.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据"
          });
          return;
        } else {
          detailById(this.choserow[0].id).then(response => {
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
          if (this.dialogTitle == "添加") {
            defineSave(this.ruleForm).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.resetForm(formName);
              } else {
                this.msgError(response.msg);
              }
            });
          } else if (this.dialogTitle == "编辑") {
            defineEdit(this.ruleForm).then(response => {
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
          message: "导入成功"
        });
        this.getList();
      } else {
        this.$message({
          type: "error",
          message: "导入失败"
        });
      }
      this.$refs.upload.clearFiles();
    },
    handleSelectionChange(val) {
      this.choserow = val;
    },
    deleteCell() {
      if (this.choserow.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
        return;
      }
      let ids = [];
      this.choserow.forEach(element => {
        ids.push(element.id);
      });
      defineDelete(ids.join(",")).then(response => {
        if (response.code === 200) {
          this.msgSuccess("删除成功");
          this.getList();
        }
      });
    },
    tableExoprt() {}
  }
};
</script>
