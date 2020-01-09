<template>
  <el-main class="dataServeManage">
    <fieldset>
      <legend>{{'服务基本信息'}}</legend>
      <el-form ref="form" :model="baseServe" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素存储字段">
              <el-select filterable v-model="baseServe.eleField" placeholder="请选择" size="small">
                <el-option
                  v-for="item in optionsColumnList"
                  :key="item.db_ele_code"
                  :label="item.ele_name+'('+item.db_ele_code+')'"
                  :value="item.db_ele_code"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域存储字段">
              <el-select filterable v-model="baseServe.regionField" placeholder="请选择" size="small">
                <el-option
                  v-for="item in optionsColumnList"
                  :key="item.db_ele_code"
                  :label="item.ele_name+'('+item.db_ele_code+')'"
                  :value="item.db_ele_code"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </fieldset>
    <fieldset style="margin-top:20px;margin-bottom:20px;">
      <legend>{{'默认配置设置'}}</legend>
      <el-form ref="form" :model="baseSet" label-width="120px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="区域选择">
              <el-select filterable v-model="baseSet.region" placeholder="请选择" size="small">
                <el-option
                  v-for="item in baseAreaOptions"
                  :key="item.value"
                  :label="item.area_id"
                  :value="item.area_id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="GRIB版本">
              <el-input v-model="baseSet.gribVersion" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="场类型">
              <el-input v-model="baseSet.fieldType" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="加工过程类型">
              <el-input v-model="baseSet.processType" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="资料时次">
              <el-input v-model="baseSet.dataTime" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时效单位">
              <el-input v-model="baseSet.timeUnit" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="空间分辨率">
              <el-input v-model="baseSet.spatialResolution" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label>
              <el-button type="primary" size="small" @click="addBase">保存</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </fieldset>
    <el-row class="areaTop">
      <el-button type="primary" icon="el-icon-plus" size="small" @click="add">新增</el-button>
      <el-button type="primary" icon="el-icon-edit" size="small" @click="edit">编辑</el-button>
      <el-button type="primary" icon="el-icon-delete" size="small" @click="deleteCell">删除</el-button>
      <el-button type="primary" icon="el-icon-document-copy" @click="columnCopy" size="small">复制</el-button>
      <el-button type="primary" icon="el-icon-document-add" @click="columnPaste" size="small">粘贴</el-button>
    </el-row>

    <el-table
      :data="tableData"
      stripe
      style="width: 100%;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="index" width="45" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="area_id" label="区域代码">
        <template slot-scope="scope">{{scope.row.area_id.split('[')[0]}}</template>
      </el-table-column>
      <el-table-column prop="db_fcst_ele" label="要素存储短名"></el-table-column>
      <el-table-column prop="ele_service_id" label="要素服务代码"></el-table-column>
      <el-table-column prop="ele_name_cn" label="要素中文名"></el-table-column>
      <el-table-column prop="ele_unit" label="要素单位"></el-table-column>
      <el-table-column prop="level_type" label="层次类型"></el-table-column>
      <el-table-column prop="scale_divisor" label="层次转换因子"></el-table-column>
      <el-table-column prop="ele_hours" label="资料时次"></el-table-column>
      <el-table-column prop="grid_pixel" label="空间分辨率"></el-table-column>
      <el-table-column prop="time_unit" label="预报时效单位"></el-table-column>
      <el-table-column prop="time_list" label="预报时效列表"></el-table-column>
      <el-table-column prop="level_unit" label="层次单位"></el-table-column>
      <el-table-column prop="level_list" label="层次列表"></el-table-column>
      <el-table-column prop="ele_long_name" label="要素长名"></el-table-column>
    </el-table>
    <!-- <Pagination :total="dataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination> -->
    <el-dialog
      class="dataServeDialog"
      width="80%"
      :title="dialogTitle"
      :visible.sync="dataServeDialog"
      append-to-body
    >
      <el-form size="mini" :rules="rules" ref="formName" :model="msgFormDialog" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="序号" prop="s_number">
              <el-input placeholder="序号" v-model="msgFormDialog.s_number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域代码" prop="area_id">
              <el-select
                filterable
                v-model="msgFormDialog.area_id"
                placeholder="请选择区域"
                size="small"
                class="areaSelect"
              >
                <el-option
                  v-for="(item,index) in baseAreaOptions"
                  :key="index"
                  :label="item.area_id"
                  :value="item.area_id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素存储代码" prop="db_fcst_ele">
              <el-select
                filterable
                @change="getServeByEle"
                v-model="msgFormDialog.db_fcst_ele"
                placeholder="要素存储代码"
                size="small"
              >
                <el-option
                  v-for="(item,index) in eleQueryAll"
                  :key="index"
                  :label="item.ele_code_short"
                  :value="item.ele_code_short"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素服务代码">
              <el-select
                filterable
                @change="getEleUnit"
                v-model="msgFormDialog.field_type"
                placeholder="要素服务代码"
                size="small"
              >
                <el-option
                  v-for="(item,index) in eleOptionsList"
                  :key="index"
                  :label="item.user_fcst_ele"
                  :value="item.user_fcst_ele"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层次类型" prop="level_type">
              <el-select
                filterable
                v-model="msgFormDialog.level_type"
                placeholder="层次类型"
                size="small"
              >
                <el-option
                  v-for="item in optionsLevels"
                  :key="item.value"
                  :label="item.level_type"
                  :value="item.level_type"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="层次类型转换因子" prop="scale_divisor">
              <el-input placeholder="层次类型转换因子" v-model="msgFormDialog.scale_divisor"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素中文名">
              <el-input placeholder="要素中文名" v-model="msgFormDialog.ele_name_cn"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素长名">
              <el-input placeholder="要素长名" v-model="msgFormDialog.ele_long_name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素单位">
              <el-input placeholder="要素单位" v-model="msgFormDialog.ele_unit"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="资料时次">
              <el-input placeholder="资料时次" v-model="msgFormDialog.ele_hours"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="空间分辨率">
              <el-input placeholder="空间分辨率" v-model="msgFormDialog.grid_pixel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预报时效单位">
              <el-input placeholder="预报时效单位" v-model="msgFormDialog.time_unit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层次单位">
              <el-input placeholder="层次单位" v-model="msgFormDialog.level_unit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预报时效列表">
              <el-input placeholder="预报时效列表" v-model="msgFormDialog.time_list"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancleHandle('formName')" size="small">取 消</el-button>
        <el-button type="primary" @click="trueHandle('formName')" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
export default {
  name: "dataServeManage",
  props: { rowData: Object },
  data() {
    return {
      tableData: [],
      dataTotal: 0,
      baseAreaOptions: [],
      multipleSelection: [],
      optionsColumnList: [],
      eleQueryAll: [],
      eleOptionsList: [],
      optionsLevels: [],
      baseServe: {
        eleField: "",
        regionField: ""
      },
      baseSet: {},
      searchObj: {
        data_class_id: this.rowData.data_class_id,
        page: 1,
        rows: 10
      },
      dialogTitle: "新增数据服务信息",
      dataServeDialog: false,
      msgFormDialog: {
        field_type: "",
        ele_unit: "",
        data_class_id: this.rowData.data_class_id
      },
      rules: {
        s_number: [{ required: true, message: "请输入序号", trigger: "blur" }],
        area_id: [
          { required: true, message: "请选择区域代码", trigger: "change" }
        ],
        db_fcst_ele: [
          {
            required: true,
            message: "请选择格点要素存储代码",
            trigger: "change"
          }
        ],
        level_type: [
          { required: true, message: "请输入层次类型", trigger: "blur" }
        ],
        scale_divisor: [
          { required: true, message: "请输入层次转换因子", trigger: "blur" }
        ]
      }
    };
  },

  methods: {
    // table自增定义方法
    table_index(index) {
      return (this.searchObj.page - 1) * this.searchObj.rows + index + 1;
    },
    searchFun() {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.axios
        .get(interfaceObj.TableStructure_getModeDef, {
          params: this.searchObj
        })
        .then(res => {
          this.tableData = res.data.data;
          this.dataTotal = res.data.total;
        });
    },
    //分页事件
    paginationChange() {
      this.searchFun();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    getBaseAreaOptions() {
      this.axios
        .get(interfaceObj.TableStructure_getGridArea, {
          params: {
            data_class_id: this.rowData.data_class_id
          }
        })
        .then(res => {
          this.baseAreaOptions = res.data.data;
        });
    },
    getFormDetail() {
      this.axios
        .get(interfaceObj.TableStructure_getServiceBase, {
          params: {
            data_class_id: this.rowData.data_class_id
          }
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.baseServe.eleField = res.data.data.eleField;
            this.baseServe.regionField = res.data.data.regionField;
            this.baseSet = res.data.data;
          } else {
            console.log(res.data.codeMessage);
          }
        });
    },

    getServiceColumnList() {
      this.axios
        .get(interfaceObj.TableStructure_getServiceColumnList, {
          params: {
            data_class_id: this.rowData.data_class_id
          }
        })
        .then(res => {
          this.optionsColumnList = res.data.data;
        });
    },
    getAllLevel() {
      this.axios.get(interfaceObj.TableStructure_getAllLevel).then(res => {
        this.optionsLevels = res.data.data;
      });
    },
    modeEleQueryAll() {
      this.axios.get(interfaceObj.TableStructure_modeEleQueryAll).then(res => {
        this.eleQueryAll = res.data.data;
      });
    },
    getServeByEle(val) {
      // this.msgFormDialog.field_type = "";
      this.axios
        .get(interfaceObj.TableStructure_getByEle, {
          params: {
            db_fcst_ele: val
          }
        })
        .then(res => {
          this.eleOptionsList = res.data.data;
        });
    },
    getEleUnit(val) {
      let record_id;
      this.eleOptionsList.forEach(element => {
        if (element.user_fcst_ele == val) {
          record_id = element.record_id;
        }
      });
      this.axios
        .get(interfaceObj.TableStructure_getDminById, {
          params: {
            id: record_id
          }
        })
        .then(res => {
          this.msgFormDialog.ele_unit = res.data.data.ele_unit;
        });
    },
    addBase() {
      this.baseSet.data_class_id = this.rowData.data_class_id;
      this.baseSet.regionField = this.baseServe.regionField;
      this.baseSet.eleField = this.baseServe.eleField;
      this.axios
        .post(interfaceObj.TableStructure_addServiceBase, this.baseSet)
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "保存成功"
            });
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        });
    },
    add() {
      this.msgFormDialog = {};
      this.msgFormDialog.data_class_id = this.rowData.data_class_id;
      this.dialogTitle = "新增数据服务信息";
      this.getAllLevel();
      this.modeEleQueryAll();
      this.dataServeDialog = true;
    },
    edit() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据"
        });
      } else {
        this.msgFormDialog = {};
        this.msgFormDialog.data_class_id = this.rowData.data_class_id;
        this.getAllLevel();
        this.modeEleQueryAll();
        this.dialogTitle = "编辑数据服务信息";
        this.msgFormDialog = this.multipleSelection[0];
        this.dataServeDialog = true;
      }
    },
    deleteCell() {
      let ids = [];
      this.multipleSelection.forEach(element => {
        ids.push(element.mode_data_def_id);
      });
      this.axios
        .post(interfaceObj.TableStructure_deleteModeDef, {
          mode_data_def_ids: ids.join(",")
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "删除成功"
            });
            this.searchFun();
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        });
    },
    columnCopy() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        sessionStorage.setItem(
          "copyServeColumn",
          JSON.stringify(this.selColumnData)
        );
        this.$message({
          message: "复制成功",
          type: "success"
        });
      }
    },
    columnPaste() {
      this.repeatIndex = 0;
      let pasteArry = JSON.parse(sessionStorage.getItem("copyServeColumn"));
      pasteArry = this.array_diff(pasteArry, this.columnData);
      let msg = "";
      if (this.repeatIndex > 0) msg = "去重(" + this.repeatIndex + ")条,";
      if (pasteArry.length == 0) {
        this.$message({
          message: msg + "已无可插入数据!",
          type: "info"
        });
      } else {
        this.$alert("是否插入" + pasteArry.length + "条复制字段", "温馨提示", {
          confirmButtonText: "确定",
          callback: action => {
            this.axios
              .post(interfaceObj.TableStructure_addManageColumn, {
                manageColumnList: pasteArry
              })
              .then(res => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    message: "插入成功！",
                    type: "success"
                  });
                  this.getCodeTable();
                } else {
                  this.$message({
                    message: res.data.returnMessage,
                    type: "error"
                  });
                }
              })
              .catch(error => {});
          }
        });
      }
    },
    cancleHandle(formName) {
      this.$refs[formName].resetFields();
      this.dataServeDialog = false;
    },
    trueHandle(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.dialogTitle == "新增数据服务信息") {
            this.msgFormDialog.data_class_id = this.rowData.data_class_id;
            delete this.msgFormDialog.data_service_id;
            this.axios
              .post(interfaceObj.TableStructure_addModeDef, this.msgFormDialog)
              .then(res => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "新增成功"
                  });
                  this.searchFun();
                  this.$refs[formName].resetFields();
                  this.dataServeDialog = false;
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.returnMessage
                  });
                }
              });
          } else {
            this.msgFormDialog.data_class_id = this.msgFormDialog.data_service_id;
            delete this.msgFormDialog.data_service_id;
            this.axios
              .post(
                interfaceObj.TableStructure_updateModeDef,
                this.msgFormDialog
              )
              .then(res => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "编辑成功"
                  });
                  this.searchFun();
                  this.$refs[formName].resetFields();
                  this.dataServeDialog = false;
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
    },
    array_diff(a, b, tabActive) {
      for (var i = 0; i < b.length; i++) {
        for (var j = 0; j < a.length; j++) {
          if (a[j].c_element_code == b[i].c_element_code) {
            a.splice(j, 1);
            j = j - 1;
            this.repeatIndex++;
          }
        }
      }
      return a;
    },
    forParent() {
      this.searchFun();
      this.getBaseAreaOptions();
      this.getServiceColumnList();
      this.getFormDetail();
    }
  }
};
</script>

<style lang="scss">
.dataServeManage {
  fieldset {
    border: 1px solid #ebeef5;
    padding: 20px;
  }
  .areaSelect,
  .areaTop {
    margin-bottom: 10px;
  }
  .el-select {
    width: 100%;
  }
}
.dataServeDialog {
  .el-select {
    width: 100%;
  }
}
</style>
