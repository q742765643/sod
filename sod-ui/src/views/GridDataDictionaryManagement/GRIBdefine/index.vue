<template>
  <div class="app-container">
    <!-- grib参数定义 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="GRIB格式:">
        <el-input size="small" v-model="queryParams.gribVersion" placeholder="请输入GRIB格式" />
      </el-form-item>
      <el-form-item label="参数编码:">
        <el-input size="small" v-model="queryParams.parameterId" placeholder="请输入参数编码" />
      </el-form-item>
      <el-form-item label="代码简写:">
        <el-input size="small" v-model="queryParams.eleCodeShort" placeholder="请输入代码简写" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
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
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="eleCodeShort" label="要素存储短名"></el-table-column>
      <el-table-column prop="subjectId" label="学科"></el-table-column>
      <el-table-column prop="classify" label="参数种类"></el-table-column>
      <el-table-column prop="parameterId" label="参数编码"></el-table-column>
      <el-table-column prop="gribVersion" label="GRIB版本"></el-table-column>
      <el-table-column prop="publicConfig" label="是否为共有配置"></el-table-column>
      <el-table-column prop="templateId" label="模板编号"></el-table-column>
      <el-table-column prop="templateDesc" label="模板说明"></el-table-column>
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
      <el-form
        :model="ruleForm"
        :rules="rules"
        label-width="130px"
        ref="ruleForm"
        class="gribdefineDialog"
      >
        <el-form-item label="要素存储短名:" prop="eleCodeShort">
          <el-input v-model="ruleForm.eleCodeShort" placeholder="请输入要素存储短名" />
        </el-form-item>
        <el-form-item label="学科:" prop="subjectId">
          <el-input v-model.number="ruleForm.subjectId" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="参数种类:" prop="classify">
          <el-input v-model.number="ruleForm.classify" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="参数编码:" prop="parameterId">
          <el-input v-model.number="ruleForm.parameterId" type="number" placeholder="请输入数字" />
        </el-form-item>
        <el-form-item label="GRIB版本:" prop="gribVersion">
          <el-select v-model.number="ruleForm.gribVersion">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否为共有配置:" prop="publicConfig">
          <el-select v-model="ruleForm.publicConfig">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板编号:" prop="templateId">
          <el-input v-model="ruleForm.templateId" placeholder="请输入模板ID" />
        </el-form-item>
        <el-form-item label="模板说明:" prop="templateDesc">
          <el-input type="textarea" v-model="ruleForm.templateDesc" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleTrue('ruleForm')">确 定</el-button>
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 高级搜索 -->
    <el-dialog
      title="筛选"
      :visible.sync="dialogSuperSearch"
      :before-close="closeSuperSearch"
      v-dialogDrag
    >
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @getList="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  gridEleDecodeDefineAll,
  gridEleDecodeDefineAdd,
  gridEleDecodeDefineEdit,
  gridEleDecodeDefineDelete,
  exportJSON,
  importJSON
} from "@/api/GridDataDictionaryManagement/GRIBdefine";

// 高级搜索
import SuperSearch from "@/components/superSearch";
export default {
  components: { SuperSearch },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      tableData: [],
      total: 0,
      superObj: {}, //高级搜索
      dialogSuperSearch: false,
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
        eleCodeShort: [
          { required: true, message: "请输入活动区域", trigger: "blur" }
        ],
        subjectId: [
          { required: true, message: "请输入数字格式学科", trigger: "blur" }
        ],
        classify: [
          { required: true, message: "请输入数字格式参数种类", trigger: "blur" }
        ],
        parameterId: [
          { required: true, message: "请输入数字格式参数编码", trigger: "blur" }
        ],
        gribVersion: [
          { required: true, message: "请选择GRIB版本", trigger: "change" }
        ],
        publicConfig: [
          { required: true, message: "请选择是否为共有配置", trigger: "change" }
        ],
        templateDesc: [
          { required: true, message: "请输入模板说明", trigger: "blur" }
        ]
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
      this.getList();
    },
    /** 查询列表 */
    getList(superMsg) {
      // 判断是否是高级搜索
      if (superMsg && superMsg.domains) {
        let superList = superMsg.domains;
        let newSuperForm = {};
        for (let i = 0; i < superList.length; i++) {
          newSuperForm[superList[i].select] = superList[i].value;
        }
        Object.assign(this.queryParams, newSuperForm);
      }
      this.loading = true;
      gridEleDecodeDefineAll(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    showDialog(type) {
      if (type == "add") {
        this.dialogTitle = "添加";
        this.msgFormDialog = true;
      } else {
        if (this.choserow.length == 1) {
          this.dialogTitle = "编辑";
          this.ruleForm = this.choserow[0];
          this.msgFormDialog = true;
        } else {
          this.$message({
            type: "error",
            message: "请选择一条数据"
          });
          return;
        }
      }
    },
    handleTrue(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.dialogTitle == "添加") {
            console.log(this.ruleForm);
            gridEleDecodeDefineAdd(this.ruleForm).then(response => {
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
            gridEleDecodeDefineEdit(this.ruleForm).then(response => {
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.msgFormDialog = false;
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
      gridEleDecodeDefineDelete(ids.join(",")).then(response => {
        if (response.code == 200) {
          this.msgSuccess("删除成功");
          this.getList();
        }
      });
    },
    tableExoprt() {
      exportJSON().then(response => {});
    },
    superClick() {
      this.superObj = {};
      this.superObj.pageName = "GRIB参数定义";
      this.dialogSuperSearch = true;
    },
    closeSuperSearch() {
      this.queryParams = {
        gribVersion: "",
        parameterId: "",
        eleCodeShort: ""
      };
      this.dialogSuperSearch = false;
    }
  }
};
</script>
<style lang="scss">
.gribdefineDialog {
  .el-select {
    width: 100%;
  }
}
</style>
