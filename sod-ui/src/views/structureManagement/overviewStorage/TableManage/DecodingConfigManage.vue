<template>
  <el-main>
    <el-button-group style="padding: 8px;">
      <el-button
        type="primary"
        icon="el-icon-s-claim"
        size="small"
        @click="dialogStatus.optConfig = true"
      >选择</el-button>
      <el-button type="primary" icon="el-icon-folder-add" size="small" @click="addConfig">新增</el-button>
      <el-button type="primary" icon="el-icon-edit" size="small" @click="editConfig">编辑</el-button>
      <el-button type="primary" icon="el-icon-delete" size="small" @click="deleteConfig">删除</el-button>
    </el-button-group>
    <el-table border :data="tableData" stripe @selection-change="res=>selData=res">
      <el-table-column type="index" label="序号" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="处理编码" prop="dataDpcId"></el-table-column>
      <el-table-column label="要素存储短名" prop="eleCodeShort"></el-table-column>
      <el-table-column label="学科" prop="subjectId"></el-table-column>
      <el-table-column label="参数种类" prop="classify"></el-table-column>
      <el-table-column label="参数编码" prop="parameterId"></el-table-column>
      <el-table-column label="GRIB版本" prop="gribVersion"></el-table-column>
      <el-table-column label="是否为共有配置" prop="publicConfig"></el-table-column>
      <el-table-column label="模板编号" prop="templateId"></el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="searchFun"
    />

    <el-dialog
      :close-on-click-modal="false"
      width="80%"
      title="选择"
      :visible.sync="dialogStatus.optConfig"
      append-to-body
      v-dialogDrag
    >
      <div>
        <el-form :model="optSearchObj" :inline="true" size="small" label-width="120px">
          <el-form-item label="短名">
            <el-input v-model.trim="optSearchObj.eleCodeShort"></el-input>
          </el-form-item>
          <el-form-item label="模板ID">
            <el-input v-model.trim="optSearchObj.templateId"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="searchOptConfig(1)">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table border :data="optData" @selection-change="handleSelectionChange" ref="choseTable">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="短名" prop="eleCodeShort"></el-table-column>
          <el-table-column label="科目" prop="subjectId"></el-table-column>
          <el-table-column label="分类" prop="classify"></el-table-column>
          <el-table-column label="GRIB版本" prop="gribVersion"></el-table-column>
          <el-table-column label="参数ID" prop="parameterId"></el-table-column>
          <el-table-column label="共有配置" prop="publicConfig"></el-table-column>
          <el-table-column label="要素描述" prop="element_cn"></el-table-column>
          <el-table-column label="模板ID" prop="templateId"></el-table-column>
        </el-table>
        <!-- <Pagination :total="optDataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination> -->
        <pagination
          v-show="optDataTotal>0"
          :total="optDataTotal"
          :page.sync="optSearchObj.pageNum"
          :limit.sync="optSearchObj.pageSize"
          @pagination="searchOptConfig"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="canalc()" size="small">取 消</el-button>
        <el-button type="primary" @click="trueHandle('table')" size="small">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      width="80%"
      :title="dialogTitle"
      :visible.sync="dialogStatus.config"
      append-to-body
      v-dialogDrag
    >
      <el-form size="mini" :model="formData" label-width="120px" ref="formData" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="处理编码" prop="dataDpcId">
              <el-input size="small" placeholder="处理编码" v-model.trim="formData.dataDpcId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="格点要素短名" prop="eleCodeShort">
              <el-input size="small" placeholder="格点要素短名" v-model.trim="formData.eleCodeShort"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学科" prop="subjectId">
              <el-input
                size="small"
                placeholder="学科"
                v-model.trim="formData.subjectId"
                type="number"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="classify">
              <el-input
                size="small"
                placeholder="分类"
                v-model.trim="formData.classify"
                type="number"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="参数ID" prop="parameterId">
              <el-input size="small" placeholder="参数ID" v-model.trim="formData.parameterId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="GRIB版本" prop="gribVersion">
              <el-input
                size="small"
                placeholder="GRIB版本"
                v-model.trim="formData.gribVersion"
                type="number"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="模板编号" prop="templateId">
              <el-input size="small" placeholder="模板编号" v-model.trim="formData.templateId"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="是否共有配置">
              <el-switch
                v-model.trim="formData.publicConfig"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="Y"
                inactive-value="N"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueHandle('formData')">确 定</el-button>
        <el-button @click="canalc()">取 消</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
import {
  griddecodingList,
  griddecodingSave,
  getById,
  saveList,
  deleteList
} from "@/api/structureManagement/tableStructureManage/DecodingConfigManage";
import { gridEleDecodeDefineAll } from "@/api/GridDataDictionaryManagement/GRIBdefine";
export default {
  name: "DecodingConfigManage",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      tableData: [],
      total: 0,
      dialogStatus: { optConfig: false, config: false },
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      optData: [],
      optSearchObj: {
        pageNum: 1,
        pageSize: 10,
        eleCodeShort: "",
        templateId: ""
      },
      optDataTotal: 0,
      selData: [],
      formData: {},
      multipleSelection: [],
      dialogTitle: "新增解码配置信息",
      rules: {
        dataDpcId: [
          { required: true, message: "请输入处理编码", trigger: "blur" }
        ],
        eleCodeShort: [
          { required: true, message: "请输入格点要素短名", trigger: "blur" }
        ],
        subjectId: [{ required: true, message: "请输入学科", trigger: "blur" }],
        classify: [{ required: true, message: "分类", trigger: "blur" }],
        parameterId: [
          { required: true, message: "请输入参数ID", trigger: "blur" }
        ],
        templateId: [
          { required: true, message: "请输入模板编号", trigger: "blur" }
        ],
        gribVersion: [
          { required: true, message: "请输入GRIB版本", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    searchFun() {
      this.queryParams.dataServiceId = this.rowData.DATA_CLASS_ID;
      console.log(this.queryParams);
      griddecodingList(this.queryParams).then(res => {
        this.tableData = res.data.pageData;
        this.total = res.data.totalCount;
      });
    },
    searchOptConfig(page) {
      if (page && page == 1) this.optSearchObj.pageNum = 1;
      gridEleDecodeDefineAll(this.optSearchObj).then(response => {
        this.optData = response.data.pageData;
        this.optDataTotal = response.data.totalCount;
        this.loading = false;
      });
    },
    addConfig() {
      this.formData = {};
      this.dialogTitle = "新增解码配置信息";
      this.dialogStatus.config = true;
    },
    editConfig() {
      if (this.selData.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
        return;
      } else {
        this.dialogTitle = "编辑解码配置信息";
        getById({ id: this.selData[0].id }).then(res => {
          this.formData = res.data;
          this.dialogStatus.config = true;
        });
      }
    },
    deleteConfig() {
      if (this.selData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
        return;
      } else {
        let ids = [];
        this.selData.forEach(element => {
          ids.push(element.id);
        });
        deleteList({
          ids: ids.join(",")
        }).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "删除成功"
            });
            this.canalc();
          } else {
            this.$message({
              type: "error",
              message: res.msg
            });
          }
        });
      }
    },
    canalc() {
      this.formData = {};
      this.multipleSelection = [];
      this.dialogStatus.optConfig = false;
      this.dialogStatus.config = false;
      this.searchFun();
    },
    trueHandle(formData) {
      if (formData == "formData") {
        this.$refs[formData].validate(valid => {
          if (valid) {
            this.formData.dataServiceId = this.rowData.DATA_CLASS_ID;
            griddecodingSave(this.formData).then(res => {
              if (res.code == 200) {
                this.$message({
                  message: "操作成功！",
                  type: "success"
                });
                this.canalc();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      } else {
        if (this.multipleSelection.length == 0) {
          this.$message({
            type: "info",
            message: "请选择一条数据"
          });
        } else {
          this.multipleSelection.forEach(element => {
            element.dataServiceId = this.rowData.DATA_CLASS_ID;
            element.dataDpcId = this.rowData.DATA_CLASS_ID;
          });
          this.add(this.multipleSelection);
        }
      }
    },
    add(addParams) {
      saveList({
        gridDecodingList: addParams
      }).then(res => {
        if (res.code == 200) {
          this.$message({
            message: "操作成功！",
            type: "success"
          });
          this.canalc();
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    }
  },
  mounted() {
    this.searchFun();
    this.searchOptConfig(1);
  }
};
</script>

<style scoped>
</style>
