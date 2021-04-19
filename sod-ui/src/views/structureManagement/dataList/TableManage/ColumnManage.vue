<template>
  <el-main class="columnTemplate">
    <el-button-group style="padding: 8px" v-if="!this.rowData.MYDISABLED">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-c-scale-to-original"
        @click="getColumnTables"
        v-if="tableStructureManageContral"
        >复用字段</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="columnAdd"
        >新增</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-edit"
        @click="columnEdit"
        >编辑</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-delete"
        @click="columnDelete"
        >删除</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-document-copy"
        @click="columnCopy"
        >复制</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-receiving"
        @click="columnSaveFun('columnForm')"
        >保存</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-refresh-left"
        @click="columnRefresh"
        >刷新</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-document-add"
        @click="columnPaste"
        >粘贴</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-s-order"
        @click="columnControl"
        v-if="tableStructureManageContral"
        >质控字段</el-button
      >
    </el-button-group>
    <el-button-group
      style="float: right; padding: 8px"
      v-if="!this.rowData.MYDISABLED"
    >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-sort"
        @click="handleSort"
        >排序</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-download"
        @click="exportCode('code')"
        >导出</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-upload2"
        @click="importTelplate"
        >导入</el-button
      >
      <el-button
        type="primary"
        size="small"
        icon="el-icon-s-operation"
        @click="syncServe"
        v-if="tableStructureManageContral"
        >同步服务名称</el-button
      >
    </el-button-group>
    <!-- 字段表格 -->
    <el-form
      :model="columnForm"
      ref="columnForm"
      label-width="100px"
      class="ruleForm"
    >
      <el-table
        :data="columnForm.columnData"
        border
        stripe
        @selection-change="(res) => (selColumnData = res)"
        height="550"
        ref="selectionTable"
        @row-click="handleClickTableRow"
        class="columnTable"
      >
        <el-table-column type="selection" width="55"> </el-table-column>

        <el-table-column label="排序">
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.serialNumber`"
              :rules="tableRules.serialNumber"
              v-show="scope.row.isEdit"
            >
              <el-input
                type="number"
                v-model="scope.row.serialNumber"
              ></el-input>
            </el-form-item>
            <span v-show="!scope.row.isEdit">{{ scope.row.serialNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column
          width="120"
          :label="column.label"
          v-for="(column, field) of tableTem1"
          :key="field"
        >
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.${field}`"
              :rules="tableRules[field]"
              v-show="scope.row.isEdit"
            >
              <el-input v-model="scope.row[field]"></el-input>
            </el-form-item>
            <span v-show="!scope.row.isEdit">{{ scope.row[field] }}</span>
          </template>
        </el-table-column>

        <el-table-column width="120" label="数据类型">
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.type`"
              :rules="tableRules.type"
              v-if="scope.row.isEdit"
            >
              <el-select v-model="scope.row.type">
                <el-option
                  v-for="(item, index) in dataTypes"
                  :key="index"
                  :label="item.text"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <span v-else>{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="column.label"
          v-for="(column, field) of tableTem2"
          :key="field"
        >
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.${field}`"
              :rules="tableRules[field]"
              v-if="scope.row.isEdit"
            >
              <el-input type="number" v-model="scope.row[field]"></el-input>
            </el-form-item>
            <span v-else>{{ scope.row[field] }}</span>
          </template>
        </el-table-column>

        <el-table-column
          width="60"
          :label="column.label"
          v-for="(column, field) of tableTem3"
          :key="field"
        >
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.${field}`"
              :rules="tableRules[field]"
              v-if="scope.row.isEdit"
            >
              <!-- <el-radio-group v-model="scope.row[field]">
                <el-radio :label="item.value" v-for="(item,index) in column.options" :key="index">{{item.text}}</el-radio>
              </el-radio-group> -->
              <el-checkbox v-model="scope.row[field]"></el-checkbox>
            </el-form-item>
            <span v-else
              ><el-checkbox v-model="scope.row[field]" disabled></el-checkbox
            ></span>
          </template>
        </el-table-column>

        <el-table-column label="默认值">
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.defaultValue`"
              :rules="tableRules.defaultValue"
              v-if="scope.row.isEdit"
            >
              <el-input v-model="scope.row.defaultValue"></el-input>
            </el-form-item>
            <span v-else>{{ scope.row.defaultValue }}</span>
          </template>
        </el-table-column>
        <el-table-column label="中文描述">
          <template slot-scope="scope">
            <el-form-item
              label-width="0px"
              :prop="`columnData.${scope.$index}.nameCn`"
              :rules="tableRules.nameCn"
              v-if="scope.row.isEdit"
            >
              <el-input v-model="scope.row.nameCn"></el-input>
            </el-form-item>
            <span v-else>{{ scope.row.nameCn }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      width="80%"
      title="公共元数据"
      :visible.sync="dialogStatus.publicMatedataDialog"
      append-to-body
    >
      <div>
        <el-tabs v-model.trim="activePublicName" type="border-card">
          <el-tab-pane label="公共元数据字段" name="first">
            <div class="tableSearch">
              <el-form :inline="true">
                <el-form-item label="数据源代码">
                  <el-input
                    clearable
                    v-model.trim="searchMatedata.c_datatype"
                    placeholder="数据源代码"
                    size="small"
                  ></el-input>
                </el-form-item>
                <el-form-item label="中文名称">
                  <el-input
                    clearable
                    v-model.trim="searchMatedata.c_datum_level"
                    placeholder="中文名称"
                    size="small"
                  ></el-input>
                </el-form-item>
                <el-form-item label="服务代码">
                  <el-input
                    clearable
                    v-model.trim="searchMatedata.c_datumtype"
                    placeholder="服务代码"
                    size="small"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="el-icon-search"
                    size="small"
                    @click="searchFun('matedata')"
                    >搜索</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
            <el-table
              :data="pubData.matedata"
              border
              height="320"
              @selection-change="handleSelectionMatedata"
            >
              <el-table-column
                label="序号"
                type="index"
                width="50"
              ></el-table-column>
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                label="数据源代码"
                prop="c_element_code"
              ></el-table-column>
              <el-table-column
                label="中文名称"
                prop="c_element_namech"
              ></el-table-column>
              <el-table-column
                label="英文名称"
                prop="c_element_name"
              ></el-table-column>
              <el-table-column
                label="数据类型"
                prop="c_datatype"
                width="70px"
              ></el-table-column>
              <el-table-column
                label="计量单位"
                prop="c_element_unit"
                width="80px"
              ></el-table-column>
              <el-table-column
                label="数据精度"
                prop="c_element_pre"
                width="70px"
              ></el-table-column>
              <el-table-column
                label="特征值"
                prop="c_charactervalue"
              ></el-table-column>
              <el-table-column
                label="状态"
                prop="c_status"
                width="60px"
              ></el-table-column>
              <el-table-column
                label="要素说明"
                prop="c_notes"
                width="70px"
              ></el-table-column>
              <el-table-column
                label="服务代码"
                prop="c_short_name"
              ></el-table-column>
              <el-table-column
                label="是否质控"
                prop="c_is_control"
                width="80px"
              >
                <template slot-scope="scope">
                  <el-switch
                    v-model.trim="scope.row.switch"
                    active-color="#13ce66"
                    inactive-color="#909399"
                  ></el-switch>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="管理字段" name="second">
            <div class="tableSearch">
              <el-form label-width="80px">
                <el-row>
                  <el-col :span="4">
                    <el-form-item label="字段分组">
                      <el-select
                        v-model.trim="searchMmdata.groupId"
                        size="small"
                      >
                        <el-option label="全部" value></el-option>
                        <el-option
                          v-for="item in AllManageGroup"
                          :key="item.value"
                          :label="item.groupId"
                          :value="item.groupId"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="字段编码">
                      <el-input
                        clearable
                        v-model.trim="searchMmdata.dbEleCode"
                        placeholder="字段编码"
                        size="small"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="中文名称">
                      <el-input
                        clearable
                        v-model.trim="searchMmdata.dbEleName"
                        placeholder="中文名称"
                        size="small"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="服务代码">
                      <el-input
                        clearable
                        v-model.trim="searchMmdata.userEleCode"
                        placeholder="服务代码"
                        size="small"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="2">
                    <el-button
                      class="btnmm"
                      type="primary"
                      icon="el-icon-search"
                      size="small"
                      @click="searchFun('mmdata')"
                      >搜索</el-button
                    >
                    <!-- <el-form-item label>

                    </el-form-item>-->
                  </el-col>
                </el-row>
              </el-form>
            </div>
            <el-table
              :data="pubData.mmdata"
              border
              height="320"
              @selection-change="handleSelectionMmdata"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                label="字段编码"
                prop="dbEleCode"
              ></el-table-column>
              <el-table-column
                label="服务代码"
                prop="userEleCode"
              ></el-table-column>
              <el-table-column
                label="中文名称"
                prop="dbEleName"
              ></el-table-column>
              <el-table-column
                label="字段短名"
                prop="eleName"
              ></el-table-column>
              <el-table-column label="类型" prop="type"></el-table-column>
              <el-table-column
                label="字段精度"
                prop="data_precision"
                width="90px"
              ></el-table-column>
              <el-table-column label="是否可为空" prop="isNull" width="90px">
                <template slot-scope="scope">
                  <span v-if="scope.row.isNull == true">是</span>
                  <span v-else>否</span>
                </template>
              </el-table-column>
              <el-table-column label="是否可更新" prop="isUpdate" width="90px">
                <template slot-scope="scope">
                  <span v-if="scope.row.isUpdate == true">是</span>
                  <span v-else>否</span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="truePublic" size="small"
          >确 定</el-button
        >
        <el-button
          @click="dialogStatus.publicMatedataDialog = false"
          size="small"
          >取 消</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      width="80%"
      title="字段排序"
      :visible.sync="dialogStatus.codeSortDialog"
      append-to-body
      v-dialogDrag
    >
      <div class="dragBox">
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-alert
            title="请拖动字段实现排序"
            type="success"
            :closable="false"
          ></el-alert>
          <draggable
            class="list-group"
            tag="ul"
            v-model.trim="dragList"
            v-bind="dragOptions"
            @start="isDragging = true"
            @end="isDragging = false"
          >
            <transition-group type="transition" name="flip-list">
              <li
                class="list-group-item"
                v-for="element in dragList"
                :key="element.celementCode"
              >
                <i
                  :class="
                    element.fixed
                      ? 'fa fa-anchor'
                      : 'glyphicon glyphicon-pushpin'
                  "
                  @click="element.fixed = !element.fixed"
                  aria-hidden="true"
                ></i>
                <el-row class="sortRow">
                  <el-col :span="4">
                    <span>{{ "序号:" }}{{ element.serialNumber }}</span>
                  </el-col>
                  <el-col :span="10">
                    <span>{{ "字段编码:" }}{{ element.celementCode }}</span>
                  </el-col>
                  <el-col :span="10">
                    <span>{{ "中文简称:" }}{{ element.eleName }}</span>
                  </el-col>
                </el-row>
              </li>
            </transition-group>
          </draggable>
        </el-scrollbar>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueSort" size="small"
          >确 定</el-button
        >
        <el-button @click="dialogStatus.codeSortDialog = false" size="small"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      class="exportCodeDialog"
      width="30%"
      title="操作"
      :visible.sync="exportInnerVisible"
      append-to-body
      v-dialogDrag
    >
      <el-row>
        <el-col :span="12">
          <input-excel
            :uploadTableId="uploadTableId"
            :tableType="tableType"
            :tableColumn="uploadColumnData"
            @getResult="getMyExcelData"
          ></input-excel>
        </el-col>
        <el-col :span="12">
          <el-button
            size="small"
            type="success"
            icon="el-icon-download"
            @click="handleExport"
            >模板下载</el-button
          >
        </el-col>
      </el-row>
    </el-dialog>
  </el-main>
</template>

<script>
// 上传
import inputExcel from "@/components/excelXlsx/upload";
// 下载
import { exportExcel } from "@/components/excelXlsx/js/download";
// 拖拽组件
import draggable from "vuedraggable";
import {
  findByTableId,
  tableColumnDel,
  tableColumnSave,
  tableColumnSaveList,
  getDictByType,
  getColumDatailById,
  syncSCode,
  queryCmccElements,
  managefieldPage,
  saveTableCloumn,
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import {
  enable,
  exportTable,
} from "@/api/structureManagement/tableStructureManage/index";
import { findAllManageGroup } from "@/api/dbDictMangement/manageField";
import { codeVer } from "@/components/commonVaildate.js";
export default {
  props: { tableInfo: Object, tableType: String, rowData: Object },
  components: {
    draggable,
    inputExcel,
  },
  data() {
    var nameValidate = (rule, value, callback) => {
      let msg = "";
      if (rule.field == "celementCode" || rule.field.indexOf("celementCode") > -1) {
        msg = "公共元数据字段";
      } else if (
        rule.field == "dbEleCode" ||
        rule.field.indexOf("dbEleCode") > -1
      ) {
        msg = "字段编码";
      }
      if (value === "") {
        callback(new Error("请输入" + msg));
      } else if (!codeVer(value)) {
        callback(
          new Error(msg + "不允许输入小写字母和中文，且需以大写字母开头")
        );
      } else {
        callback();
      }
    };

    const tableNumValidate = (rule, value, callback) => {
      if (value === "" || value === null || value === undefined) {
        callback(new Error("请输入序号"));
      } else {
        let flag = 0;
        this.columnForm.columnData.forEach((element) => {
          if (element.serialNumber == value) {
            console.log("重复");
            flag++;
            return;
          }
        });
        if (flag > 1) {
          callback(new Error("序号不能重复"));
        } else {
          callback();
        }
      }
    };

    const accuracyValidate = (rule, value, callback) => {
      if (value === null || value === "") {
        callback();
      } else {
        if (value > 0) {
          callback();
        } else {
          callback(new Error("不是正整数"));
        }
      }
    };
    const lengthValidate = (rule, value, callback) => {
      if (value === null || value === "") {
        callback();
      } else {
        if (value > 0) {
          callback();
        } else {
          callback(new Error("不是正整数"));
        }
      }
    };
    return {
      columnForm: {
        //字段表格
        columnData: [],
      },
      selColumnData: [], //选中的表格值
      tableTem1: {}, //新增表格模板1
      tableTem2: {}, //新增表格模板2
      tableTem3: {}, //新增表格模板3
      oldColumnData: [], //存储当前的字段数据
      uploadTableId: "",
      uploadTableType: "",
      uploadColumnData: [],
      tableStructureManageContral: false,
      codeTitle: "新增字段",
      dialogStatus: {
        publicMatedataDialog: false,
        codeSortDialog: false,
      },
      exportInnerVisible: false,
      filepath: "",

      pubData: { matedata: [], mmdata: [] }, //公共元数据字段，管理字段
      searchMatedata: {
        c_datatype: "",
        c_datum_level: "",
        c_datumtype: "",
      },
      searchMmdata: {
        groupId: "",
        dbEleCode: "",
        dbEleName: "",
        userEleCode: "",
      },
      dataTypes: [],
      dragList: [],
      matedataSelection: [],
      mmdataSelection: [],
      activePublicName: "first",
      repeatIndex: 0,

      tableRules: {
        serialNumber: [
          { required: true, validator: tableNumValidate, trigger: "blur" },
        ],
        dbEleCode: [
          { required: true, validator: nameValidate, trigger: "blur" },
        ],
        celementCode: [
          { required: true, validator: nameValidate, trigger: "blur" },
        ],
        userEleCode: [
          { required: true, message: "请输入服务名称", trigger: "blur" },
        ],
        eleName: [
          { required: true, message: "请输入中文简称", trigger: "blur" },
        ],
        type: [
          { required: true, message: "请选择数据类型", trigger: "change" },
        ],
        accuracy: [
          { required: true, validator: accuracyValidate, trigger: "change" },
        ],
        length: [
          { required: true, validator: lengthValidate, trigger: "change" },
        ],
      },
      // 字段导入
      fileTemp: null,
      fileListUpload: [],
      AllManageGroup: [],
    };
  },
  async created() {
    await this.getDictByTypeMethods("table_column_type");
    this.tableTem1 = {
      dbEleCode: {
        label: "字段编码",
        type: "input",
        rules: {
          required: true,
          message: "字段编码不能为空",
        },
      },
      eleName: {
        label: "中文简称",
        type: "input",
        rules: {
          required: true,
          message: "中文简称不能为空",
        },
      },
    };
    this.tableTem2 = {
      length: {
        label: "数据长度",
        type: "number",
      },
      accuracy: {
        label: "数据精度",
        type: "number",
      },
    };
    this.tableTem3 = {
      isNull: {
        label: "是否可空",
        type: "radio",
        options: [
          { text: "是", value: true },
          { text: "否", value: false },
        ],
      },
      isPrimaryKey: {
        label: "是否主键",
        type: "radio",
        options: [
          { text: "是", value: true },
          { text: "否", value: false },
        ],
      },
    };
    if (this.rowData.formPage == "数据注册审核") {
      let resData = this.tableInfo.columns;
      resData.forEach((item, index) => {
        item.isEdit = false;
        item.isAdd = false;
        item.indexNum = index;
      });
      this.columnForm.columnData = resData;
      this.oldColumnData = JSON.parse(JSON.stringify(resData)); //用于是否修改的数据对比
    }
  },
  methods: {
    handleChange() {},
    handleExport() {
      exportTable().then((res) => {
        this.downloadfileCommon(res);
      });
    },
    // 选中就勾选
    handleClickTableRow(row, event, column) {
      this.$refs.selectionTable.toggleRowSelection(row);
    },
    /* 复用字段 */
    getColumnTables() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }

      this.matedataSelection = [];
      this.mmdataSelection = [];
      this.searchMatedata = {};
      this.searchMmdata = {};
      this.getmatedata();
      this.getmmdata();
      this.activePublicName = "first";
      this.dialogStatus.publicMatedataDialog = true;
    },
    // 公共元数据
    getmatedata() {
      this.searchMatedata.c_datum_code = this.rowData.D_DATA_ID;
      console.log(this.searchMatedata);
      queryCmccElements(this.searchMatedata).then((res) => {
        if (res.code == 200) {
          res.data.forEach((element) => {
            element.switch = false;
          });
          this.pubData.matedata = res.data;
        }
      });
    },
    // 管理字段
    getmmdata() {
      managefieldPage(this.searchMmdata)
        .then((res) => {
          this.pubData.mmdata = res.data;
        })
        .catch((error) => {});
    },
    // 管理字段分组
    getAllGroup() {
      findAllManageGroup().then((res) => {
        res.success
          ? (this.AllManageGroup = res.data)
          : this.$message({
              type: "error",
              message: res.msg,
            });
      });
    },

    async getCodeTable() {
      let flag = undefined;
      if (this.tableType == "E-Kshow") {
        flag = "true";
      }
      await findByTableId({ tableId: this.tableInfo.id }).then((response) => {
        if (response.code == 200) {
          let resData = response.data;
          resData.forEach((item, index) => {
            item.isEdit = false;
            item.isAdd = false;
            item.indexNum = index;
          });
          this.columnForm.columnData = resData;

          this.oldColumnData = JSON.parse(JSON.stringify(resData)); //用于是否修改的数据对比
          console.log(this.oldColumnData);
          this.$emit("reloadTableInfo");
        }
      });
    },
    //新增字段-行
    columnAdd() {
      let serialNumber = 0;
      let indexNum = 0;
      let columnData = this.columnForm.columnData;
      if (columnData.length != 0) {
        let numArr = [],
          indexArr = [];
        columnData.forEach((item) => {
          numArr.push(item.serialNumber);
          indexArr.push(item.indexNum);
        });
        serialNumber = Math.max(...numArr);
        indexNum = Math.max(...indexArr);
      }
      let obj = {
        indexNum: indexNum + 1, //排序号为了删除
        serialNumber: serialNumber + 1, //数据的序号
        isEdit: true, //用于标识是否可编辑
        isAdd: true, //用于标识添加
        dbEleCode: "", //公共元数据
        celementCode: "", //字段编码
        userEleCode: "", //服务名称
        eleName: "", //中文简称
        accuracy: null, //数据进度
        length: null, //数据长度
        type: "", //数据类型
        unit: "N", //要素单位
        isManager: 0, //是否管理字段
        isPrimaryKey: false, //是否主键
        isShow: false, //是否显示
        isUpdate: false, //是否可改
        isNull: false, //是否可空
        tableId: this.tableInfo.id,
      };
      if (this.tableType == "E-Kshow") {
        obj.isKvK = true;
      } else if (this.tableType == "E-Eshow") {
        obj.isKvK = false;
      }
      this.columnForm.columnData.push(obj);
    },
    //编辑字段-所有
    columnEdit() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择要编辑的数据！",
          type: "error",
        });
      } else {
        this.selColumnData.forEach((item) => {
          item.isEdit = true;
        });
      }
    },
    //删除字段-选中
    columnDelete() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        this.$confirm("确认要删除选中字段吗?", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            // 选中要删除的数据
            let selectArr = this.selColumnData;
            // 未提交数据
            let indexNotArr = [];
            // 已提交的数据
            let isNotAdd = selectArr.filter((item) => {
              if (item.isAdd == true) {
                indexNotArr.push(item.indexNum);
              } else {
                return item;
              }
            });
            // 存在已提交数据删除
            if (isNotAdd.length > 0) {
              let ids = [];
              isNotAdd.forEach((element) => {
                ids.push(element.id);
              });
              tableColumnDel({ ids: ids.join(",") }).then((response) => {
                if (response.code == 200) {
                  this.$message({ message: "删除成功", type: "success" });
                  this.getCodeTable();
                } else {
                  this.$message({
                    message: "删除失败",
                    type: "error",
                  });
                  return;
                }
              });
            }
            // 选中的数据只有未提交数据
            else {
              // 获得所有数据的index
              let indexAllArr = [],
                newColumnData = [];
              this.columnForm.columnData.forEach((item) => {
                indexAllArr.push(item.indexNum);
              });

              indexNotArr.forEach((item) => {
                if (indexAllArr.includes(item)) {
                  this.columnForm.columnData.forEach((columnItem) => {
                    if (columnItem.indexNum !== item) {
                      newColumnData.push(columnItem);
                    }
                  });
                }
              });

              this.columnForm.columnData = newColumnData;
            }
          })
          .catch(() => {});
      }
    },
    // 根据类型查询字典信息
    async getDictByTypeMethods(dictType) {
      await getDictByType({ dictType: dictType }).then((response) => {
        if (response.code == 200) {
          this.dataTypes = [];
          response.data.forEach((element) => {
            let obj = {
              text: element.dictLabel,
              value: element.dictValue,
            };
            this.dataTypes.push(obj);
          });
        }
      });
    },

    searchFun(type) {
      if (type == "matedata") {
        this.getmatedata();
      } else if (type == "mmdata") {
        this.getmmdata();
      }
    },

    handleSort() {
      if (this.columnForm.columnData.length == 0) {
        this.$message({
          type: "error",
          message: "当前没有字段，无法排序",
        });
      }
      this.dragList = this.columnForm.columnData;
      this.dialogStatus.codeSortDialog = true;
    },
    trueSort() {
      this.dragList.forEach((item, index) => {
        item.serialNumber = index;
      });
      tableColumnSaveList({ tableColumnList: this.dragList }).then((res) => {
        if (res.code == 200) {
          this.$message({
            message: "排序成功！",
            type: "success",
          });
          this.getCodeTable();
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
      this.dialogStatus.codeSortDialog = false;
    },
    exportCode(type) {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }
      if (this.columnForm.columnData.length == 0) {
        this.$message({
          type: "error",
          message: "不能导出空表",
        });
        return;
      }

      let exportData = JSON.parse(JSON.stringify(this.columnForm.columnData));
      if (type == "code") {
        let theader = [
          "公共元数据字段",
          "字段编码",
          "服务代码",
          "中文简称",
          "数据类型",
          "数据精度",
          "要素单位",
          "是否可空",
          "是否可改",
          "是否显示",
          "是否主键",
          "中文描述",
          "是否管理字段",
          "默认值",
          "序号",
        ];
        let tableProp = [
          "dbEleCode",
          "celementCode",
          "userEleCode",
          "eleName",
          "type",
          "accuracy",
          "unit",
          "isNull",
          "isUpdate",
          "isShow",
          "isPrimaryKey",
          "nameCn",
          "isManager",
          "defaultValue",
          "serialNumber",
        ];
        let newArry = [];
        exportData.forEach((element) => {
          tableProp.forEach((item) => {
            if (element[item] === false) {
              element[item] = "N";
            } else if (element[item] === true) {
              element[item] = "Y";
            }
          });
          newArry.push(element);
        });
        exportExcel(
          theader,
          tableProp,
          newArry,
          this.tableInfo.tableName + "-" + this.tableInfo.databaseName
        );
      } else {
        window.location.href = "";
      }
    },

    // 上传限制
    handleExceed() {
      this.$message.warning("当前限制选择1个文件");
    },

    importTelplate() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }
      this.uploadTableType = this.tableType;
      this.uploadColumnData = this.tableInfo.columns;
      this.uploadTableId = this.tableInfo.id;
      this.exportInnerVisible = true;
    },
    //批量保存
    columnSaveFun(formName) {
      debugger
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.columnForm.columnData);
          let columnData = this.columnForm.columnData;
          let oldColumnData = this.oldColumnData;
          let editColumnArr = [];
          //1 对比id一样，其他key是否一样，取出有变化的行
          columnData.forEach((item) => {
            oldColumnData.forEach((oldItem) => {
              if (item.id === oldItem.id) {
                if (JSON.stringify(item) !== JSON.stringify(oldItem)) {
                  editColumnArr.push(item);
                }
              }
            });
          });
          //2 获取新增的数据
          columnData.forEach((item) => {
            if (item.isAdd === true) {
              editColumnArr.push(item);
            }
          });
          if (editColumnArr.length > 0) {
            saveTableCloumn({ tableColumnList: editColumnArr }).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                //刷新表格
                this.getCodeTable();
              }
            });
          } else {
            this.$message({
              type: "info",
              message: "没有需要保存的数据",
            });
          }
          console.log(editColumnArr);
        } else {
          this.$message({
            type: "error",
            message: "待保存数据中存在，不符合规则的值",
          });
          return false;
        }
      });
    },
    //刷新
    columnRefresh() {
      this.getCodeTable();
    },
    columnCopy() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        //获取选中的已保存的
        let isSaveColumnArr = this.selColumnData.filter((item) => {
          if ("id" in item) {
            return item;
          }
        });
        if (isSaveColumnArr.length == 0) {
          this.$message({
            message: "请选择一条已保存数据！",
            type: "error",
          });
        } else {
          sessionStorage.setItem("copyColumn", JSON.stringify(isSaveColumnArr));
          this.$message({
            message: `复制成功，已复制${isSaveColumnArr.length}条数据`,
            type: "success",
          });
        }
      }
    },
    columnPaste() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }
      this.repeatIndex = 0;
      let pasteArry = JSON.parse(sessionStorage.getItem("copyColumn"));
      pasteArry = this.array_diff(
        pasteArry,
        this.columnForm.columnData,
        "paste"
      );
      let msg = "";
      if (this.repeatIndex > 0) msg = "去重(" + this.repeatIndex + ")条，";
      if (pasteArry.length == 0) {
        this.$message({
          message: msg + "已无可插入数据！",
          type: "info",
        });
      } else {
        this.$confirm(
          "是否插入" + pasteArry.length + "条复制字段",
          "温馨提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            pasteArry.forEach((element) => {
              element.id = "";
              element.tableId = this.tableInfo.id;
            });
            tableColumnSaveList({ tableColumnList: pasteArry }).then((res) => {
              if (res.code == 200) {
                this.$message({
                  message: "插入成功！",
                  type: "success",
                });
                this.getCodeTable();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          })
          .catch(() => {});
      }
    },
    // 公共元数据字段选择
    handleSelectionMatedata(val) {
      this.matedataSelection = val;
    },
    // 管理字段选择
    handleSelectionMmdata(val) {
      this.mmdataSelection = val;
    },
    // 复用字段确定
    truePublic() {
      let publicRows = [];
      this.repeatIndex = 0;
      if (this.activePublicName == "first") {
        if (this.matedataSelection.length > 0) {
          publicRows = this.array_diff(
            this.matedataSelection,
            this.columnForm.columnData,
            "first"
          );
          publicRows.forEach((element) => {
            if (element.switch) {
              var switchObj = Object.assign({}, element);
              var firstChar = element.celementCode.substr(0, 1);
              let newCode = element.celementCode.replace(firstChar, "Q");
              switchObj.eleName = element.eleName + "质量标志";
              switchObj.cElementCode = newCode;
              switchObj.dbEleCode = newCode;
              publicRows.push(switchObj);
            }
          });
        } else {
          this.$message({
            message: msg + "无剩余可添加数据!",
            type: "info",
          });
        }
      } else if (this.activePublicName == "second") {
        if (this.mmdataSelection.length > 0) {
          publicRows = this.array_diff(
            this.mmdataSelection,
            this.columnForm.columnData,
            "second"
          );
        } else {
          this.$message({
            message: msg + "无剩余可添加数据!",
            type: "info",
          });
        }
      }

      let msg = "";
      if (this.repeatIndex > 0) msg = "去重(" + this.repeatIndex + ")条,";
      if (publicRows.length > 0) {
        if (this.tableType == "E-Kshow") {
          publicRows.forEach((element) => {
            element.isKvK = true;
            element.id = "";
            element.tableId = this.tableInfo.id;
          });
        } else if (this.tableType == "E-Eshow") {
          publicRows.forEach((element) => {
            element.isKvK = false;
            element.id = "";
            element.tableId = this.tableInfo.id;
          });
        }
        publicRows.forEach((element) => {
          element.id = "";
          element.tableId = this.tableInfo.id;
        });
        tableColumnSaveList({ tableColumnList: publicRows }).then(
          (response) => {
            if (response.code == 200) {
              this.$message({ message: "操作成功", type: "success" });

              this.getCodeTable();
            } else {
              this.$message({
                type: "error",
                message: "操作失败",
              });
            }
          }
        );
      } else {
        this.$message({
          message: msg + "无剩余可添加数据!",
          type: "success",
        });
      }
      this.dialogStatus.publicMatedataDialog = false;
    },
    // 质控字段
    columnControl() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        let flag = false;
        let length = this.selColumnData.length;
        let publicRows = this.selColumnData;
        //获取选中的已保存的
        let isSaveColumnArr = this.selColumnData.filter((item) => {
          if ("id" in item) {
            return item;
          }
        });
        if (isSaveColumnArr.length == 0) {
          this.$message({
            message: "请选择一条已保存数据！",
            type: "error",
          });
        } else {
          isSaveColumnArr.forEach((element) => {
            var switchObj = Object.assign({}, element);
            var firstChar = element.celementCode.substr(0, 1);
            let newCode = element.celementCode.replace(firstChar, "Q");
            switchObj.eleName = element.eleName + "质量标志";
            switchObj.celementCode = newCode;
            switchObj.dbEleCode = newCode;
            switchObj.id = "";
            publicRows.push(switchObj);
            this.columnForm.columnData.forEach((c) => {
              if (c.celementCode == switchObj.celementCode) {
                flag = true;
              }
            });
          });
          if (flag) {
            this.$message({
              message: "不能重复插入质控字段",
              type: "error",
            });
            return;
          }
          this.$confirm("是否插入" + length + "条质控字段", "温馨提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              tableColumnSaveList({ tableColumnList: publicRows }).then(
                (res) => {
                  if (res.code == 200) {
                    this.$message({
                      message: "操作成功！",
                      type: "success",
                    });
                    this.getCodeTable();
                  } else {
                    this.$message({
                      message: res.msg,
                      type: "error",
                    });
                  }
                }
              );
            })
            .catch(() => {});
        }
      }
    },
    // 同步服务
    async syncServe() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据",
          type: "error",
        });
        return;
      }
      //获取选中的已保存的
      let isSaveColumnArr = this.selColumnData.filter((item) => {
        if ("id" in item) {
          return item;
        }
      });
      if (isSaveColumnArr.length == 0) {
        this.$message({
          message: "请选择一条已保存数据！",
          type: "error",
        });
        return;
      }
      syncSCode({
        tableColumnList: isSaveColumnArr,
      }).then((res) => {
        if (res.code == 200) {
          this.$message({
            message: "同步成功！",
            type: "success",
          });
          this.getCodeTable();
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
    },
    getMyExcelData(data, getTableId, gettableType, gettableColumn) {
      let tableJson = [
        { name: "字段编码", value: "celementCode" },
        { name: "中文简称", value: "eleName" },
        { name: "数据类型", value: "type" },
        { name: "数据精度", value: "accuracy" },
        { name: "是否可空", value: "isNull" },
        { name: "是否主键", value: "isPrimaryKey" },
        { name: "中文描述", value: "nameCn" },
        { name: "序号", value: "serialNumber" },
      ];
      let dataJson = [];
      // data 为读取的excel数据，在这里进行处理该数据
      debugger;
      data.forEach((element) => {
        var obj = {};
        let itemKeys = Object.keys(element);
        itemKeys.forEach((ikey) => {
          tableJson.forEach((tkey) => {
            if (ikey == tkey.name) {
              obj[tkey.value] = element[ikey];
            }
          });
        });
        dataJson.push(obj);
      });
      console.log(dataJson);
      if (dataJson.length == 0) {
        this.$message({
          type: "error",
          message: "不能导入空表",
        });
        return;
      }
      let flag = false;
      let flagmsg = "";
      dataJson.forEach((element) => {
        element.id = "";
        element.tableId = getTableId;
        if (!element.dbEleCode) {
          flag = true;
          flagmsg = "字段编码不能为空";
          return;
        }
        if (!codeVer(element.dbEleCode)) {
          flag = true;
          flagmsg = "字段编码不允许输入小写字母和中文，且需以大写字母开头";
          return;
        }
        let tableArry = JSON.parse(gettableColumn);
        tableArry.forEach((c) => {
          if (c.dbEleCode == element.dbEleCode) {
            flag = true;
            flagmsg = "字段编码不能重复";
            return;
          }
        });

        if (!element.eleName) {
          flag = true;
          flagmsg = "中文简称不能为空";
          return;
        }
        if (!element.type) {
          flag = true;
          flagmsg = "数据类型不能为空";
          return;
        }

        if (
          element.serialNumber == "" ||
          element.serialNumber == null ||
          element.serialNumber == undefined
        ) {
          flag = true;
          flagmsg = "序号不能为空";
          return;
        }
        if (element.serialNumber < 0) {
          flag = true;
          flagmsg = "序号不能小于0";
          return;
        }
        if (element.isNull === "是" || element.isNull === "Y") {
          element.isNull = "true";
        } else if (element.isNull === "否" || element.isNull === "N") {
          element.isNull = "false";
        }
        if (element.isPrimaryKey === "是" || element.isPrimaryKey === "Y") {
          element.isPrimaryKey = "true";
        } else if (
          element.isPrimaryKey === "否" ||
          element.isPrimaryKey === "N"
        ) {
          element.isPrimaryKey = "false";
        }
        if (gettableType == "E-Kshow") {
          element.isKvK = true;
        } else if (gettableType == "E-Eshow") {
          element.isKvK = false;
        }
        element.isPrimaryKey = element.isPrimaryKey.toLowerCase();
      });
      if (flag) {
        this.$message({
          type: "error",
          message: flagmsg,
        });
        return;
      }

      tableColumnSaveList({ tableColumnList: dataJson }).then((response) => {
        if (response.code == 200) {
          this.$message({ message: "导入成功", type: "success" });
          this.exportInnerVisible = false;
          this.getCodeTable();
        } else {
          this.$message({
            type: "error",
            message: "导入失败",
          });
        }
      });
    },

    array_diff(a, b, tabActive) {
      for (var i = 0; i < b.length; i++) {
        for (var j = 0; j < a.length; j++) {
          if (
            a[j].celementCode == b[i].celementCode ||
            a[j].c_element_code == b[i].celementCode
          ) {
            a.splice(j, 1);
            j = j - 1;
            this.repeatIndex++;
          }
        }
      }
      let newRows = [];
      a.forEach((element) => {
        let obj = {};
        if (tabActive == "paste") {
          obj = element;
          obj.id = this.tableInfo.id;
          newRows.push(obj);
        } else {
          if (tabActive == "first") {
            obj.eleName = element.c_element_namech;
            obj.dbEleCode = element.c_element_code;
            obj.celementCode = element.c_element_code;
            obj.type = element.c_datatype;
            obj.accuracy = element.c_element_pre;
            obj.isNull = false;
            obj.isUpdate = false;
            obj.nameCn = element.c_notes;
            obj.unit = element.c_element_unit;
            obj.unit = element.c_element_unit;
            obj.userEleCode = element.c_short_name;
          } else if (tabActive == "second") {
            obj.eleName = element.dbEleName;
            obj.dbEleCode = element.dbEleCode;
            obj.celementCode = element.dbEleCode;
            obj.type = element.type;
            obj.isNull = element.isNull;
            obj.isUpdate = element.isUpdate;
            obj.accuracy = element.data_precision;
            obj.nameCn = element.c_notes;
            obj.unit = element.c_element_unit;
            obj.unit = element.c_element_unit;
            obj.userEleCode = element.c_short_name;
          }
          obj.switch = element.switch;
          obj.id = this.tableInfo.id;
          obj.isPrimaryKey = false;
          obj.serialNumber = 0;

          newRows.push(obj);
        }
      });
      return newRows;
    },
  },
  mounted() {
    enable().then((res) => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
  },
  computed: {
    dragOptions() {
      return {
        animation: 0,
        group: "description",
        disabled: false,
        ghostClass: "ghost",
      };
    },
  },
  watch: {
    tableInfo(val) {
      let flag = undefined;
      if (this.tableType == "E-Kshow") {
        flag = "true";
      }
      let resData = this.tableInfo.columns;
      resData.forEach((item, index) => {
        item.isEdit = false;
        item.isAdd = false;
        item.indexNum = index;
      });
      this.columnForm.columnData = resData;
      this.oldColumnData = JSON.parse(JSON.stringify(resData)); //用于是否修改的数据对比
    },
  },
};
</script>
<style lang="scss">
.columnTemplate {
  height: 600px;
  overflow: hidden;
  .el-scrollbar {
    margin-top: 10px;
    height: 500px !important;
  }
  .columnTable {
    .cell {
      padding-left: 0px;
      padding-right: 0px;
    }
    .el-radio {
      margin-right: 5px;
    }
    .el-radio__label {
      padding-left: 5px;
    }
    .el-form-item {
      margin-bottom: 15px;
    }
  }
  .demo-table-expand {
    padding-left: 122px;
    .el-form-item {
      width: 20%;
    }
  }
}
.dragBox {
  max-height: 400px;
  overflow: hidden;
  .el-scrollbar {
    margin-top: 10px;
    height: 400px;
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .sortRow {
    border-bottom: 1px solid #d3dce6;
    padding-bottom: 8px;
    span {
      background: #d3dce6;
      display: inline-block;
      width: 100%;
      text-align: center;
      padding: 8px 2px;
    }
  }
  .flip-list-move {
    transition: transform 0.5s;
  }
  .no-move {
    transition: transform 0s;
  }
  .ghost {
    opacity: 0.5;
    background: #c8ebfb;
  }
  .list-group {
    margin-top: 10px;
    min-height: 20px;
  }
  .list-group-item {
    cursor: move;
    border: none;
  }
  .list-group-item i {
    cursor: pointer;
  }
}
.btnmm {
  margin-top: 5px;
  margin-left: 4px;
}
.exportCodeDialog {
  .el-row {
    padding-bottom: 20px;
    .el-col {
      text-align: center;
    }
  }
}
</style>
