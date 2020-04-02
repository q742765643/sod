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
                  v-for="(item,index) in optionsColumnList"
                  :key="index"
                  :label="item.eleName+'('+item.dbEleCode+')'"
                  :value="item.dbEleCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域存储字段">
              <el-select filterable v-model="baseServe.regionField" placeholder="请选择" size="small">
                <el-option
                  v-for="(item,index) in optionsColumnList"
                  :key="index"
                  :label="item.eleName+'('+item.dbEleCode+')'"
                  :value="item.dbEleCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </fieldset>
    <fieldset style="margin-top:20px;margin-bottom:20px;">
      <legend>{{'默认配置设置'}}</legend>
      <el-form ref="form" :model="baseSet" label-width="120px" :rules="baseRules">
        <el-row>
          <el-col :span="6">
            <el-form-item label="区域选择">
              <el-select
                filterable
                v-model="baseSet.region"
                placeholder="请选择"
                size="small"
                prop="region"
              >
                <el-option
                  v-for="item in baseAreaOptions"
                  :key="item.value"
                  :label="item.areaId"
                  :value="item.areaId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="GRIB版本" prop="gribVersion">
              <el-input v-model="baseSet.gribVersion" size="small" type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="场类型" prop="fieldType">
              <el-input v-model="baseSet.fieldType" size="small" type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="加工过程类型" prop="processType">
              <el-input v-model="baseSet.processType" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="资料时次" prop="dataTime">
              <el-input v-model="baseSet.dataTime" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时效单位" prop="timeUnit">
              <el-input v-model="baseSet.timeUnit" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="空间分辨率" prop="spatialResolution">
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
      <el-table-column type="selection" width="45"></el-table-column>
      <el-table-column prop="areaId" label="区域代码"></el-table-column>
      <el-table-column prop="dbEleName" label="要素存储短名"></el-table-column>
      <el-table-column prop="eleServiceId" label="要素服务代码"></el-table-column>
      <el-table-column prop="eleLongName" label="要素中文名"></el-table-column>
      <el-table-column prop="eleUnit" label="要素单位"></el-table-column>
      <el-table-column prop="levelType" label="层次类型"></el-table-column>
      <el-table-column prop="scaleDivisor" label="层次转换因子"></el-table-column>
      <el-table-column prop="eleHours" label="资料时次"></el-table-column>
      <el-table-column prop="gridPixel" label="空间分辨率"></el-table-column>
      <el-table-column prop="timeUnit" label="预报时效单位"></el-table-column>
      <el-table-column prop="timeList" label="预报时效列表"></el-table-column>
      <el-table-column prop="levelUnit" label="层次单位"></el-table-column>
      <el-table-column prop="levelList" label="层次列表"></el-table-column>
      <el-table-column prop="eleLongName" label="要素长名"></el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="searchObj.pageNum"
      :limit.sync="searchObj.pageSize"
      @pagination="searchFun"
    />

    <el-dialog
      class="dataServeDialog"
      width="80%"
      :title="dialogTitle"
      :visible.sync="dataServeDialog"
      append-to-body
      v-dialogDrag
    >
      <el-form size="mini" :rules="rules" ref="formName" :model="msgFormDialog" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="序号" prop="num">
              <el-input placeholder="序号" v-model="msgFormDialog.num"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域代码" prop="areaId">
              <el-select
                filterable
                v-model="msgFormDialog.areaId"
                placeholder="请选择区域"
                size="small"
                class="areaSelect"
              >
                <el-option
                  v-for="(item,index) in baseAreaOptions"
                  :key="index"
                  :label="item.areaId"
                  :value="item.areaId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素存储代码" prop="dbEleName">
              <el-select
                filterable
                @change="getServeByEle"
                v-model="msgFormDialog.dbEleName"
                placeholder="要素存储代码"
                size="small"
              >
                <el-option
                  v-for="(item,index) in eleQueryAll"
                  :key="index"
                  :label="item.eleCodeShort"
                  :value="item.eleCodeShort"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素服务代码">
              <el-select
                filterable
                @change="getEleUnit"
                v-model="msgFormDialog.eleServiceId"
                placeholder="要素服务代码"
                size="small"
              >
                <el-option
                  v-for="(item,index) in eleOptionsList"
                  :key="index"
                  :label="item.userFcstEle"
                  :value="item.userFcstEle"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层次类型" prop="levelType">
              <el-select
                filterable
                v-model="msgFormDialog.levelType"
                placeholder="层次类型"
                size="small"
              >
                <el-option
                  v-for="item in optionsLevels"
                  :key="item.value"
                  :label="item.levelType"
                  :value="item.levelType"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="层次类型转换因子" prop="scaleDivisor">
              <el-input placeholder="层次类型转换因子" v-model="msgFormDialog.scaleDivisor"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素中文名">
              <el-input placeholder="要素中文名" v-model="msgFormDialog.eleNameCn"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素长名">
              <el-input placeholder="要素长名" v-model="msgFormDialog.eleLongName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要素单位">
              <el-input placeholder="要素单位" v-model="msgFormDialog.eleUnit"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="资料时次">
              <el-input placeholder="资料时次" v-model="msgFormDialog.eleHours"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="空间分辨率">
              <el-input placeholder="空间分辨率" v-model="msgFormDialog.gridPixel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预报时效单位">
              <el-input placeholder="预报时效单位" v-model="msgFormDialog.timeUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层次单位">
              <el-input placeholder="层次单位" v-model="msgFormDialog.levelUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预报时效列表">
              <el-input placeholder="预报时效列表" v-model="msgFormDialog.timeList"></el-input>
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
import {
  gcl,
  findByDataServiceId,
  dataserverconfigSave,
  serviceCodeQueryAll,
  findByDbFcstEle,
  findByDataCLassId,
  dataserverconfiglist,
  saveBase,
  dataserverconfigget,
  addList,
  delByIds
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { getAllLevel } from "@/api/GridDataDictionaryManagement/levelManagement";
export default {
  name: "dataServeManage",
  props: { rowData: Object },
  data() {
    return {
      repeatIndex: 0,
      total: 0,
      tableData: [],
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
        dataServiceId: this.rowData.DATA_CLASS_ID,
        pageNum: 1,
        pageSize: 10
      },
      dialogTitle: "新增数据服务信息",
      dataServeDialog: false,
      msgFormDialog: {
        dataServiceId: this.rowData.DATA_CLASS_ID
      },
      baseRules: {
        region: [
          { required: true, message: "请选择区域代码", trigger: "change" }
        ],
        gribVersion: [
          { required: true, message: "请输入GRIB版本", trigger: "blur" }
        ],
        fieldType: [
          { required: true, message: "请输入场类型", trigger: "blur" }
        ],
        processType: [
          { required: true, message: "请输入加工过程类型", trigger: "blur" }
        ],
        dataTime: [
          { required: true, message: "请输入资料时次", trigger: "blur" }
        ],
        timeUnit: [
          { required: true, message: "请输入时效单位", trigger: "blur" }
        ],
        spatialResolution: [
          { required: true, message: "请输入空间分辨率", trigger: "blur" }
        ]
      },
      rules: {
        num: [{ required: true, message: "请输入序号", trigger: "blur" }],
        areaId: [
          { required: true, message: "请选择区域代码", trigger: "change" }
        ],
        dbEleName: [
          {
            required: true,
            message: "请选择格点要素存储代码",
            trigger: "change"
          }
        ],
        levelType: [
          { required: true, message: "请输入层次类型", trigger: "blur" }
        ],
        scaleDivisor: [
          { required: true, message: "请输入层次转换因子", trigger: "blur" }
        ]
      }
    };
  },

  methods: {
    // table自增定义方法
    table_index(index) {
      return (this.searchObj.pageNum - 1) * this.searchObj.pageSize + index + 1;
    },
    searchFun() {
      dataserverconfiglist(this.searchObj).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    async getFormDetail() {
      await findByDataCLassId({ dataCLassId: this.rowData.DATA_CLASS_ID }).then(
        response => {
          if (response.code == 200 && response.data.length > 0) {
            console.log(response);
            let data = response.data[0];
            this.baseServe.eleField = data.eleField;
            this.baseServe.regionField = data.regionField;
            this.baseSet = data;
          }
        }
      );
    },

    async getAllLevelMethods() {
      await getAllLevel().then(response => {
        this.optionsLevels = response.data;
      });
    },
    modeEleQueryAll() {
      serviceCodeQueryAll().then(response => {
        if (response.code == 200) {
          this.eleQueryAll = response.data;
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    },
    getServeByEle(val) {
      findByDbFcstEle({ dbFcstEle: val }).then(response => {
        if (response.code == 200) {
          this.eleOptionsList = response.data;
          console.log(this.eleOptionsList);
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    },
    getEleUnit(val) {
      this.eleOptionsList.forEach(element => {
        if (element.userFcstEle == val) {
          this.msgFormDialog.eleUnit = element.eleUnit;
          this.msgFormDialog.eleNameCn = element.eleName;
          this.msgFormDialog.eleLongName = element.elePropertyName;
        }
      });
    },
    addBase() {
      let obj = Object.assign(this.baseSet, this.baseServe);
      obj.dataCLassId = this.rowData.DATA_CLASS_ID;
      console.log(obj);
      saveBase(obj).then(response => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "保存成功"
          });
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    },
    add() {
      if (this.baseSet.region) {
        this.msgFormDialog = {};
        this.msgFormDialog = this.baseSet;
        this.msgFormDialog.dataServiceId = this.rowData.DATA_CLASS_ID;
        this.dialogTitle = "新增数据服务信息";
        this.getAllLevelMethods();
        this.modeEleQueryAll();
        this.dataServeDialog = true;
      } else {
        this.$message({
          type: "error",
          message: "请先设置默认配置"
        });
      }
    },
    edit() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据"
        });
      } else {
        dataserverconfigget({ id: this.multipleSelection[0].id }).then(
          response => {
            if (response.code == 200) {
              this.getAllLevelMethods();
              this.modeEleQueryAll();
              this.dialogTitle = "编辑数据服务信息";
              this.msgFormDialog = response.data;
              this.dataServeDialog = true;
            } else {
              this.$message({
                type: "error",
                message: response.msg
              });
            }
          }
        );
      }
    },
    deleteCell() {
      let ids = [];
      this.multipleSelection.forEach(element => {
        ids.push(element.mode_data_def_id);
      });
      delByIds({ ids: ids.join(",") }).then(res => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
          this.searchFun();
        } else {
          this.$message({
            type: "error",
            message: res.msg
          });
        }
      });
    },
    columnCopy() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        sessionStorage.setItem(
          "copyServeColumn",
          JSON.stringify(this.multipleSelection)
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
      if (pasteArry[0].dataServiceId == this.rowData.DATA_CLASS_ID) {
        this.$message({
          message: "不能在同一个表内插入数据!",
          type: "info"
        });
      } else {
        pasteArry.forEach(element => {
          element.dataServiceId = this.rowData.DATA_CLASS_ID;
          element.id = "";
        });
        addList(pasteArry).then(response => {
          if (response.code == 200) {
            this.$message({
              message: "插入成功！",
              type: "success"
            });
            this.searchFun();
          } else {
            this.$message({
              type: "error",
              message: response.msg
            });
          }
        });
      }
    },
    cancleHandle(formName) {
      this.$refs[formName].resetFields();
      this.dataServeDialog = false;
    },
    trueHandle(formName) {
      let obj = Object.assign(this.msgFormDialog, this.baseServe);
      console.log(obj);
      this.$refs[formName].validate(valid => {
        if (valid) {
          let msg = "";
          if (this.dialogTitle == "新增数据服务信息") {
            obj.id = "";
            msg = "新增成功";
          } else {
            msg = "编辑成功";
          }
          dataserverconfigSave(obj).then(response => {
            if (response.code == 200) {
              this.searchFun();
              this.$refs[formName].resetFields();
              this.dataServeDialog = false;
              this.$message({
                type: "success",
                message: msg
              });
            } else {
              this.$message({
                type: "error",
                message: response.msg
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    async gettableInfo() {
      await gcl({ classLogic: this.rowData.LOGIC_ID }).then(response => {
        if (response.code == 200) {
          let data = response.data;
          let tableInfo_k = [];
          let tableInfo_e = [];
          for (let i = 0; i < data.length; i++) {
            if (data[i].dbTableType == "K") {
              let colArry = data[i].columns;
              colArry.forEach(element => {
                if (element.dbEleCode.slice(0, 1) == "V") {
                  tableInfo_k.push(element);
                }
              });
            } else if (data[i].dbTableType == "E") {
              let colArry = data[i].columns;
              colArry.forEach(element => {
                if (element.dbEleCode.slice(0, 1) == "V") {
                  tableInfo_e.push(element);
                }
              });
            }
          }
          this.optionsColumnList = tableInfo_k.concat(tableInfo_e);
          if (data.length == 0) {
            this.optionsColumnList = [];
          }
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    },

    async getBaseAreaOptions() {
      await findByDataServiceId({
        dataServiceId: this.rowData.DATA_CLASS_ID
      }).then(response => {
        console.log(response);
        this.baseAreaOptions = response.data;
      });
    },
    async forParent() {
      await this.gettableInfo(); //获取基本服务信息下拉框
      await this.getBaseAreaOptions(); //获取区域下拉框
      this.searchFun();
      await this.getFormDetail();
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
