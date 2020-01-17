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
    <el-table :data="tableData" stripe @selection-change="res=>selData=res">
      <el-table-column type="index" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="服务编码" prop="data_dpc_id"></el-table-column>
      <el-table-column label="要素存储短名" prop="ele_code_short"></el-table-column>
      <el-table-column label="学科" prop="subject_id"></el-table-column>
      <el-table-column label="参数种类" prop="classify"></el-table-column>
      <el-table-column label="参数编码" prop="parameter_id"></el-table-column>
      <el-table-column label="GRIB版本" prop="grib_version"></el-table-column>
      <el-table-column label="是否为共有配置" prop="public_config"></el-table-column>
      <el-table-column label="模板编号" prop="template_id"></el-table-column>
    </el-table>

    <!-- <Pagination :total="dataTotal" @paginationChange="paginationChange0" ref="pagination"></Pagination> -->

    <el-dialog width="80%" title="选择" :visible.sync="dialogStatus.optConfig" append-to-body>
      <div>
        <el-form :model="optSearchObj" :inline="true" size="small" label-width="120px">
          <el-form-item label="短名">
            <el-input v-model="optSearchObj.ele_code_short"></el-input>
          </el-form-item>
          <el-form-item label="模板ID">
            <el-input v-model="optSearchObj.template_id"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="searchOptConfig(1)">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="optData" @selection-change="handleSelectionChange" ref="choseTable">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="短名" prop="ele_code_short"></el-table-column>
          <el-table-column label="科目" prop="subject_id"></el-table-column>
          <el-table-column label="分类" prop="classify"></el-table-column>
          <el-table-column label="GRIB版本" prop="grib_version"></el-table-column>
          <el-table-column label="参数ID" prop="parameter_id"></el-table-column>
          <el-table-column label="共有配置" prop="public_config"></el-table-column>
          <el-table-column label="要素描述" prop="element_cn"></el-table-column>
          <el-table-column label="模板ID" prop="template_id"></el-table-column>
        </el-table>
        <!-- <Pagination :total="optDataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination> -->
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="canalc()" size="small">取 消</el-button>
        <el-button type="primary" @click="trueHandle('table')" size="small">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog width="80%" :title="dialogTitle" :visible.sync="dialogStatus.config" append-to-body>
      <el-form size="mini" :model="formData" label-width="120px" ref="formData" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="处理编码" prop="data_dpc_id">
              <el-input placeholder="处理编码" v-model="formData.data_dpc_id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="格点要素短名" prop="ele_code_short">
              <el-input placeholder="格点要素短名" v-model="formData.ele_code_short"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学科" prop="subject_id">
              <el-input placeholder="学科" v-model="formData.subject_id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="classify">
              <el-input placeholder="分类" v-model="formData.classify"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="参数ID" prop="parameter_id">
              <el-input placeholder="参数ID" v-model="formData.parameter_id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="GRIB版本" prop="grib_version">
              <el-input placeholder="GRIB版本" v-model="formData.grib_version"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="模板编号" prop="template_id">
              <el-input placeholder="模板编号" v-model="formData.template_id"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="是否共有配置">
              <el-switch
                v-model="formData.public_config"
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
export default {
  name: "DecodingConfigManage",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      tableData: [],
      dataTotal: 0,
      dialogStatus: { optConfig: false, config: false },
      searchObj: {},
      optData: [],
      optSearchObj: { ele_code_short: "", template_id: "" },
      optDataTotal: 0,
      selData: [],
      formData: {},
      multipleSelection: [],
      dialogTitle: "新增解码配置信息",
      rules: {
        data_dpc_id: [
          { required: true, message: "请输入处理编码", trigger: "blur" }
        ],
        ele_code_short: [
          { required: true, message: "请输入格点要素短名", trigger: "blur" }
        ],
        subject_id: [
          { required: true, message: "请输入学科", trigger: "blur" }
        ],
        classify: [{ required: true, message: "分类", trigger: "blur" }],
        parameter_id: [
          { required: true, message: "请输入参数ID", trigger: "blur" }
        ],
        template_id: [
          { required: true, message: "请输入模板编号", trigger: "blur" }
        ],
        grib_version: [
          { required: true, message: "请输入GRIB版本", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (this.searchObj.page - 1) * this.searchObj.rows + index + 1;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    searchFun() {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.axios
        .get(interfaceObj.TableStructure_getModeCode, {
          params: {
            data_class_id: this.rowData.data_class_id,
            ...this.searchObj
          }
        })
        .then(res => {
          this.tableData = res.data.data;
          this.dataTotal = res.data.total;
        });
    },
    searchOptConfig(page) {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.optSearchObj = { ...this.optSearchObj, ...paginateObj };
      let sp = {};
      if (page && page == 1) sp.page = 1;
      this.axios
        .get(interfaceObj.TableStructure_getModeEle, {
          params: { ...this.optSearchObj, ...sp }
        })
        .then(res => {
          this.optData = res.data.data;
          this.optDataTotal = res.data.total;
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
        this.formData = JSON.parse(JSON.stringify(this.selData[0]));
      }
      this.dialogStatus.config = true;
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
          ids.push(element.grid_decode_id);
        });
        this.axios
          .post(interfaceObj.TableStructure_deleteModeCode, {
            grid_decode_id: ids.join(",")
          })
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                type: "success",
                message: "删除成功"
              });
              this.canalc();
            } else {
              this.$message({
                type: "error",
                message: res.data.returnMessage
              });
            }
          });
      }
    },
    //分页事件

    paginationChange() {
      this.searchOptConfig();
    },
    paginationChange0() {
      this.searchFun();
    },
    canalc() {
      this.formData = {};
      this.multipleSelection = [];
      this.dialogStatus.optConfig = false;
      this.dialogStatus.config = false;
      this.searchFun();
      // this.$refs.choseTable.clearSelection();
    },
    trueHandle(formData) {
      if (formData == "formData") {
        this.$refs[formData].validate(valid => {
          if (valid) {
            if (this.dialogTitle == "新增解码配置信息") {
              this.formData.data_class_id = this.rowData.data_class_id;
              delete this.formData.data_service_id;
              let arry = [];
              arry.push(this.formData);
              this.add(arry);
            } else {
              this.formData.data_class_id = this.formData.data_service_id;
              delete this.formData.data_service_id;
              this.axios
                .post(interfaceObj.TableStructure_updateModeCode, this.formData)
                .then(res => {
                  if (res.data.returnCode == 0) {
                    this.$message({
                      type: "success",
                      message: "编辑成功"
                    });
                    this.canalc();
                  } else {
                    this.$message({
                      type: "error",
                      message: res.data.returnMessage
                    });
                  }
                });
            }
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
            element.data_class_id = this.rowData.data_class_id;
          });
          this.add(this.multipleSelection);
        }
      }
    },
    add(addParams) {
      this.axios
        .post(interfaceObj.TableStructure_addModeCode, {
          model_code_str: addParams
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "新增成功"
            });
            this.canalc();
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        });
    }
  },
  mounted() {
    // this.searchFun();
  }
};
</script>

<style scoped>
</style>
