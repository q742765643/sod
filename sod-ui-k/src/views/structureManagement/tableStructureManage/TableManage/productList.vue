<template>
  <el-main class="productList">
    <el-row class="areaTop">
      <el-button type="primary" size="small" @click="add">新增</el-button>
      <el-button type="primary" size="small" @click="edit">编辑</el-button>
      <el-button type="primary" size="small" @click="deleteProduct">删除</el-button>
    </el-row>
    <el-row class="areaTop">
      <el-col :span="20">
        <el-select v-model.trim="selectArea" placeholder="请选择区域" size="small" class="areaSelect">
          <el-option
            v-for="item in optionsArea"
            :key="item.value"
            :label="item.area_id.split('[')[0]"
            :value="item.area_id"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>

    <el-table
      :data="tableData"
      stripe
      style="width: 100%;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="area_id" label="区域代码">
        <template slot-scope="scope">{{scope.row.area_id.split('[')[0]}}</template>
      </el-table-column>
      <el-table-column prop="db_fcst_ele" label="格点要素存储代码"></el-table-column>
      <el-table-column prop="ele_service_id" label="要素服务代码"></el-table-column>
      <el-table-column prop="level_type" label="层次类型"></el-table-column>
      <el-table-column prop="grib_version" label="grib版本"></el-table-column>
      <el-table-column prop="field_type" label="场类型"></el-table-column>
      <el-table-column prop="genprocess_type" label="加工过程类型"></el-table-column>
      <el-table-column prop="ele_name_cn" label="要素中文名"></el-table-column>
      <el-table-column prop="ele_hours" label="资料时次"></el-table-column>
      <el-table-column prop="time_unit" label="时效单位"></el-table-column>
      <el-table-column prop="level_list" label="层次列表"></el-table-column>
      <el-table-column prop="time_list" label="预报时效列表"></el-table-column>
      <el-table-column prop="grid_pixel" label="空间分辨率"></el-table-column>
    </el-table>
    <!-- <Pagination :total="dataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination> -->

    <el-dialog
      :close-on-click-modal="false"
      class="productDialog"
      width="80%"
      :title="dialogTitle"
      :visible.sync="productDialog"
      append-to-body
      v-dialogDrag
    >
      <el-form size="mini" :rules="rules" ref="formName" :model="msgFormDialog" label-width="140px">
        <el-col :span="12">
          <el-form-item label="区域代码" prop="area_id">
            <el-select
              v-model.trim="msgFormDialog.area_id"
              placeholder="请选择区域"
              size="small"
              class="areaSelect"
            >
              <el-option
                v-for="item in optionsArea"
                :key="item.value"
                :label="item.area_id.split('[')[0]"
                :value="item.area_id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="格点要素存储代码" prop="db_fcst_ele">
            <el-select v-model.trim="msgFormDialog.db_fcst_ele" placeholder="格点要素存储代码" size="small">
              <el-option
                v-for="item in optionsFcst"
                :key="item.value"
                :label="item.ele_code_short"
                :value="item.ele_code_short"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="要素服务代码" prop="ele_service_id">
            <el-input placeholder="要素服务代码" v-model.trim="msgFormDialog.ele_service_id"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="层次类型" prop="level_type">
            <el-input placeholder="层次类型" v-model.trim="msgFormDialog.level_type"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="grib版本" prop="grib_version">
            <el-input placeholder="grib版本" v-model.trim="msgFormDialog.grib_version"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="场类型" prop="field_type">
            <el-input-number v-model.trim="msgFormDialog.field_type" :min="0" size="small"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="加工过程类型" prop="genprocess_type">
            <el-input-number v-model.trim="msgFormDialog.genprocess_type" :min="0" size="small"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="要素中文名" prop="ele_name_cn">
            <el-input placeholder="要素中文名" v-model.trim="msgFormDialog.ele_name_cn"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料时次" prop="ele_hours">
            <el-input placeholder="资料时次" v-model.trim="msgFormDialog.ele_hours"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时效单位" prop="time_unit">
            <el-input placeholder="时效单位" v-model.trim="msgFormDialog.time_unit"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="层次列表" prop="level_list">
            <el-input placeholder="层次列表" v-model.trim="msgFormDialog.level_list"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预报时效列表" prop="time_list">
            <el-input placeholder="预报时效列表" v-model.trim="msgFormDialog.time_list"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="空间分辨率" prop="grid_pixel">
            <el-input placeholder="空间分辨率" v-model.trim="msgFormDialog.grid_pixel"></el-input>
          </el-form-item>
        </el-col>
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
  name: "productList",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      tableData: [],
      dataTotal: 0,
      optionsArea: [],
      optionsFcst: [],
      multipleSelection: [],
      selectArea: "",
      searchObj: {
        area_id: this.selectArea,
        data_class_id: this.rowData.data_class_id,
      },
      dialogTitle: "新增数据服务信息",
      productDialog: false,
      msgFormDialog: {
        data_class_id: this.rowData.data_class_id,
      },
      rules: {
        area_id: [
          { required: true, message: "请选择区域代码", trigger: "change" },
        ],
        db_fcst_ele: [
          {
            required: true,
            message: "请选择格点要素存储代码",
            trigger: "change",
          },
        ],
        ele_service_id: [
          { required: true, message: "请输入要素服务代码", trigger: "blur" },
        ],
        level_type: [
          { required: true, message: "请输入层次类型", trigger: "blur" },
        ],
        grib_version: [
          { required: true, message: "请输入grib版本", trigger: "blur" },
        ],
        field_type: [
          { required: true, message: "请输入场类型", trigger: "blur" },
        ],
        genprocess_type: [
          { required: true, message: "请输入加工过程类型", trigger: "blur" },
        ],
        ele_name_cn: [
          { required: true, message: "请输入要素中文名", trigger: "blur" },
        ],
        ele_hours: [
          { required: true, message: "请输入资料时次", trigger: "blur" },
        ],
        time_unit: [
          { required: true, message: "请输入时效单位", trigger: "blur" },
        ],
        level_list: [
          { required: true, message: "请输入层次列表", trigger: "blur" },
        ],
        time_list: [
          { required: true, message: "请输入预报时效列表", trigger: "blur" },
        ],
        grid_pixel: [
          { required: true, message: "请输入空间分辨率", trigger: "blur" },
        ],
      },
    };
  },

  methods: {
    searchFun() {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.axios
        .get(interfaceObj.TableStructure_getProductModeDef, {
          params: this.searchObj,
        })
        .then((res) => {
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
    getoptionsArea() {
      this.axios
        .get(interfaceObj.TableStructure_getGridArea, {
          params: {
            data_class_id: this.rowData.data_class_id,
          },
        })
        .then((res) => {
          this.optionsArea = res.data.data;
        });
    },
    getoptionsFcst() {
      this.axios
        .get(interfaceObj.TableStructure_modeEleQueryAll)
        .then((res) => {
          this.optionsFcst = res.data.data;
        });
    },
    add() {
      // this.msgFormDialog = {};
      this.dialogTitle = "新增数据服务信息";
      this.getoptionsFcst();
      this.productDialog = true;
    },
    edit() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据",
        });
      } else {
        this.dialogTitle = "编辑数据服务信息";
        this.msgFormDialog = this.multipleSelection[0];
        this.productDialog = true;
      }
    },
    deleteProduct() {
      let ids = [];
      this.multipleSelection.forEach((element) => {
        ids.push(element.mode_data_def_id);
      });
      this.axios
        .get(interfaceObj.TableStructure_deleteProductList, {
          params: {
            mode_data_def_id: ids.join(","),
          },
        })
        .then((res) => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.searchFun();
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage,
            });
          }
        });
    },
    cancleHandle(formName) {
      this.$refs[formName].resetFields();
      this.productDialog = false;
    },
    trueHandle(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dialogTitle == "新增数据服务信息") {
            this.msgFormDialog.data_class_id = this.rowData.data_class_id;
            delete this.msgFormDialog.data_service_id;
            this.axios
              .post(
                interfaceObj.TableStructure_addProductList,
                this.msgFormDialog
              )
              .then((res) => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "新增成功",
                  });
                  this.searchFun();
                  this.$refs[formName].resetFields();
                  this.productDialog = false;
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.returnMessage,
                  });
                }
              });
          } else {
            this.msgFormDialog.data_class_id = this.msgFormDialog.data_service_id;
            delete this.msgFormDialog.data_service_id;
            this.axios
              .post(
                interfaceObj.TableStructure_updateProductList,
                this.msgFormDialog
              )
              .then((res) => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "编辑成功",
                  });
                  this.searchFun();
                  this.$refs[formName].resetFields();
                  this.productDialog = false;
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.returnMessage,
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
    forParent() {
      this.searchFun();
      this.getoptionsArea();
    },
  },
};
</script>

<style lang="scss">
.productList {
  .areaSelect,
  .areaTop {
    margin-bottom: 10px;
  }
  .el-select {
    width: 100%;
  }
}
.productDialog {
  .el-select {
    width: 100%;
  }
}
</style>
