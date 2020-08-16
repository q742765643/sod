<template>
  <el-main class="columnTemplate">
    <el-button-group style="padding: 8px;">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="columnAdd">新增</el-button>
      <el-button type="primary" size="small" icon="el-icon-edit" @click="columnEdit">编辑</el-button>
      <el-button type="primary" size="small" icon="el-icon-delete" @click="columnDelete">删除</el-button>
      <el-button type="primary" size="small" icon="el-icon-document-copy" @click="columnCopy">复制</el-button>
      <el-button type="primary" size="small" icon="el-icon-document-add" @click="columnPaste">粘贴</el-button>
    </el-button-group>
    <el-button-group style="float: right;padding: 8px;">
      <el-button type="primary" size="small" icon="el-icon-sort" @click="handleSort">排序</el-button>
      <el-button type="primary" size="small" icon="el-icon-download" @click="exportCode('code')">导出</el-button>
      <el-button type="primary" size="small" icon="el-icon-upload2" @click="importTelplate">导入</el-button>
    </el-button-group>
    <!-- <el-scrollbar wrap-class="scrollbar-wrapper"> -->
    <el-table
      :data="columnData"
      border
      @selection-change="res=>selColumnData=res"
      height="550"
      ref="selectionTable"
      @row-click="handleClickTableRow"
    >
      <el-table-column type="index" label="序号" width="70"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="字段编码" prop="celementCode" width="200px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="中文简称" prop="eleName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="数据类型" prop="type" width="100px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="数据精度" prop="accuracy" width="100px"></el-table-column>
      <el-table-column label="要素单位" prop="unit" width="100px"></el-table-column>
      <el-table-column label="是否可空" prop="isNull" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.isNull == true">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="是否可改" prop="isUpdate" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.isUpdate == true">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" prop="isShow" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.isShow == true">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="是否主键" prop="isPrimaryKey" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.isPrimaryKey == true">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="中文描述" prop="nameCn" width="100px"></el-table-column>
      <el-table-column label="是否管理字段" prop="isManager" width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.isManager == true">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="默认值" prop="defaultValue" width="100px"></el-table-column>
      <el-table-column label="序号" prop="serialNumber" width="100px"></el-table-column>
    </el-table>

    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      width="80%"
      :title="codeTitle"
      :visible.sync="dialogStatus.columnDialog"
      append-to-body
    >
      <el-form
        size="mini"
        :rules="rules"
        ref="formCodeName"
        :model="columnEditData"
        label-width="140px"
      >
        <el-col :span="12">
          <el-form-item label="公共元数据字段" prop="dbEleCode">
            <el-input placeholder="公共元数据字段" v-model.trim="columnEditData.dbEleCode" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="字段编码" prop="celementCode">
            <el-input placeholder="字段编码" v-model.trim="columnEditData.celementCode" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务代码" prop="userEleCode">
            <el-input placeholder="服务代码" v-model.trim="columnEditData.userEleCode" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="中文简称" prop="eleName">
            <el-input placeholder="中文简称" v-model.trim="columnEditData.eleName" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类型" prop="type">
            <el-select
              v-model.trim="columnEditData.type"
              placeholder="请选择数据类型"
              style="width: 100%;"
            >
              <el-option
                v-for="(item,index) in dataTypes"
                :key="index"
                :label="item.dictLabel"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="要素单位" prop="unit">
            <el-input placeholder="要素单位" v-model.trim="columnEditData.unit" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数据精度">
            <el-input-number v-model.trim="columnEditData.accuracy" :min="0" size="small"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否可空">
            <el-switch
              v-model.trim="columnEditData.isNull"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否可改">
            <el-switch
              v-model.trim="columnEditData.isUpdate"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否显示">
            <el-switch
              v-model.trim="columnEditData.isShow"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否主键">
            <el-switch
              v-model.trim="columnEditData.isPrimaryKey"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否管理字段">
            <el-switch
              v-model.trim="columnEditData.isManager"
              active-color="#13ce66"
              inactive-color="#909399"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="默认值">
            <el-input placeholder="默认值" v-model.trim="columnEditData.defaultValue" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="中文描述">
            <el-input placeholder="中文描述" v-model.trim="columnEditData.nameCn" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序号" prop="serialNumber">
            <el-input-number v-model.trim="columnEditData.serialNumber" size="small" :min="0"></el-input-number>
          </el-form-item>
        </el-col>
        <div class="clear"></div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueCode('formCodeName')" size="small">确 定</el-button>
        <el-button @click="cancleCode('formCodeName')" size="small">取 消</el-button>
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
          <el-alert title="请拖动字段实现排序" type="success" :closable="false"></el-alert>
          <draggable
            class="list-group"
            tag="ul"
            v-model.trim="dragList"
            v-bind="dragOptions"
            @start="isDragging = true"
            @end="isDragging = false"
          >
            <transition-group type="transition" name="flip-list">
              <li class="list-group-item" v-for="element in dragList" :key="element.celementCode">
                <i
                  :class="
                element.fixed ? 'fa fa-anchor' : 'glyphicon glyphicon-pushpin'
              "
                  @click="element.fixed = !element.fixed"
                  aria-hidden="true"
                ></i>
                <el-row class="sortRow">
                  <el-col :span="4">
                    <span>{{'序号:'}}{{ element.serialNumber }}</span>
                  </el-col>
                  <el-col :span="10">
                    <span>{{'字段编码:'}}{{ element.celementCode }}</span>
                  </el-col>
                  <el-col :span="10">
                    <span>{{'中文简称:'}}{{ element.eleName }}</span>
                  </el-col>
                </el-row>
              </li>
            </transition-group>
          </draggable>
        </el-scrollbar>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueSort" size="small">确 定</el-button>
        <el-button @click="dialogStatus.codeSortDialog = false" size="small">取 消</el-button>
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
      <el-row :v-if="exportInnerVisible">
        <el-col :span="12">
          <input-excel @getResult="getMyExcelData"></input-excel>
        </el-col>
        <el-col :span="12">
          <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">模板下载</el-button>
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
  queryCmccElements,
  managefieldPage,
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { exportTable } from "@/api/structureManagement/tableStructureManage/index";
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
      if (rule.field == "dbEleCode") {
        msg = "公共元数据字段";
      } else if (rule.field == "celementCode") {
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
    const numValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入序号"));
      } else {
        let flag = false;
        this.columnData.forEach((element) => {
          if (
            element.serialNumber == value &&
            value != this.columnEditData.serialNumber
          ) {
            console.log("重复");
            flag = true;
            return;
          }
        });
        if (flag) {
          callback(new Error("序号不能重复"));
        } else {
          callback();
        }
      }
    };
    return {
      codeTitle: "新增字段",
      dialogStatus: {
        columnDialog: false,
        publicMatedataDialog: false,
        codeSortDialog: false,
      },
      exportInnerVisible: false,
      filepath: "",
      columnEditData: {
        unit: "N",
        isManager: false,
        isPrimaryKey: false,
        isShow: false,
        isUpdate: false,
        isNull: false,
      },
      columnData: [],
      selColumnData: [],
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
      rules: {
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
        unit: [{ required: true, message: "请输入要素单位", trigger: "blur" }],
        serialNumber: [
          { required: true, validator: numValidate, trigger: "blur" },
        ],
      },
      // 字段导入
      fileTemp: null,
      fileListUpload: [],
      AllManageGroup: [],
    };
  },
  methods: {
    handleExport() {
      exportTable().then((res) => {
        this.downloadfileCommon(res);
      });
    },
    // 选中就勾选
    handleClickTableRow(row, event, column) {
      this.$refs.selectionTable.toggleRowSelection(row);
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
          this.columnData = response.data;
          this.$emit("reloadTableInfo");
        }
      });
    },
    columnAdd() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在",
        });
        return;
      }
      this.columnEditData = {
        unit: "N",
        isManager: false,
        isPrimaryKey: false,
        isShow: false,
        isUpdate: false,
        isNull: false,
      };
      this.getDictByTypeMethods("table_column_type");
      this.codeTitle = "新增字段";
      this.columnEditData.serialNumber = Number(
        this.columnData[this.columnData.length - 1].serialNumber + 1
      );
      this.dialogStatus.columnDialog = true;
    },

    columnEdit() {
      if (this.selColumnData.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        this.codeTitle = "编辑字段";
        this.getDictByTypeMethods("table_column_type");
        getColumDatailById({ id: this.selColumnData[0].id }).then(
          (response) => {
            if (response.code == 200) {
              this.columnEditData = response.data;
              this.dialogStatus.columnDialog = true;
            }
          }
        );
      }
    },
    // 根据类型查询字典信息
    getDictByTypeMethods(dictType) {
      getDictByType({ dictType: dictType }).then((response) => {
        if (response.code == 200) {
          this.dataTypes = response.data;
        }
      });
    },
    // 删除字段
    columnDelete() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        this.$confirm("确认要删除选中字段吗?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let ids = [];
            this.selColumnData.forEach((element) => {
              ids.push(element.id);
            });
            /* if (ids.length > 600) {
              this.$message({
                message: "不可以删除600个以上字段！",
                type: "error"
              });
              return;
            } */
            console.log(ids.join(","));
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
          })
          .catch(() => {});
      }
    },

    cancleCode(formName) {
      this.$refs[formName].resetFields();
      this.dialogStatus.columnDialog = false;
    },
    // 字段新增编辑
    trueCode(formName) {
      this.columnEditData.dbEleCode = this.columnEditData.celementCode;
      this.columnEditData.userEleCode = this.columnEditData.celementCode;
      //校验表单
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.columnEditData.tableId = this.tableInfo.id;
          if (this.tableType == "E-Kshow") {
            this.columnEditData.isKvK = true;
          } else if (this.tableType == "E-Eshow") {
            this.columnEditData.isKvK = false;
          }
          console.log(this.columnEditData);
          tableColumnSave(this.columnEditData).then((response) => {
            if (response.code == 200) {
              this.$message({ message: "操作成功", type: "success" });
              this.$refs[formName].resetFields();
              this.dialogStatus.columnDialog = false;
              this.getCodeTable();
              this.columnEditData = [];
            } else {
              this.$message({
                type: "error",
                message: "操作失败",
              });
            }
          });
        }
      });
    },

    handleSort() {
      if (this.columnData.length == 0) {
        this.$message({
          type: "error",
          message: "当前没有字段，无法排序",
        });
      }
      this.dragList = this.columnData;
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
      if (this.columnData.length == 0) {
        this.$message({
          type: "error",
          message: "不能导出空表",
        });
        return;
      }

      if (type == "code") {
        let theader = [
          "字段编码",
          "中文简称",
          "数据精度",
          "要素单位",
          "是否可空",
          "是否可改",
          "是否显示",
          "是否主键",
          "中文描述",
          "数据类型",
          "是否管理字段",
          /*"是否修改数据库",*/
          "默认值",
          "序号",
        ];
        let tableProp = [
          "celementCode",
          "eleName",
          "accuracy",
          "unit",
          "isNull",
          "isUpdate",
          "isShow",
          "isPrimaryKey",
          "nameCn",
          "type",
          "isManager",
          /*"updateDatabase",*/
          "defaultValue",
          "serialNumber",
        ];
        let newArry = [];
        this.columnData.forEach((element) => {
          tableProp.forEach((item) => {
            if (element[item] === false) {
              element[item] = "否";
            } else if (element[item] === true) {
              element[item] = "是";
            }
          });
          newArry.push(element);
        });
        exportExcel(theader, tableProp, newArry);
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
      this.exportInnerVisible = true;
    },

    columnCopy() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "error",
        });
      } else {
        sessionStorage.setItem(
          "copyColumn",
          JSON.stringify(this.selColumnData)
        );
        this.$message({
          message: "复制成功",
          type: "success",
        });
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
      pasteArry = this.array_diff(pasteArry, this.columnData, "paste");
      let msg = "";
      if (this.repeatIndex > 0) msg = "去重(" + this.repeatIndex + ")条,";
      if (pasteArry.length == 0) {
        this.$message({
          message: msg + "已无可插入数据!",
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

    getMyExcelData(data) {
      let tableJson = [
        { name: "公共元数据字段", value: "dbEleCode" },
        { name: "字段编码", value: "celementCode" },
        { name: "服务代码", value: "userEleCode" },
        { name: "中文简称", value: "eleName" },
        { name: "数据精度", value: "accuracy" },
        { name: "要素单位", value: "unit" },
        { name: "是否可空", value: "isNull" },
        { name: "是否可改", value: "isUpdate" },
        { name: "是否显示", value: "isShow" },
        { name: "是否主键", value: "isPrimaryKey" },
        { name: "中文描述", value: "nameCn" },
        { name: "是否管理字段", value: "isManager" },
        { name: "数据类型", value: "type" },
        // { name: "是否修改数据库", value: "updateDatabase" },
        { name: "默认值", value: "defaultValue" },
        { name: "序号", value: "serialNumber" },
      ];
      let dataJson = [];
      // data 为读取的excel数据，在这里进行处理该数据
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
        element.tableId = this.tableInfo.id;

        if (!element.celementCode) {
          flag = true;
          flagmsg = "字段编码不能为空";
          return;
        }
        if (!codeVer(element.celementCode)) {
          flag = true;
          flagmsg = "字段编码不允许输入小写字母和中文，且需以大写字母开头";
          return;
        }
        this.columnData.forEach((c) => {
          if (c.celementCode == element.celementCode) {
            flag = true;
            flagmsg = "字段编码不能重复";
            return;
          }
        });
        element.dbEleCode = element.celementCode;
        element.userEleCode = element.celementCode;

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
        if (!element.unit) {
          flag = true;
          flagmsg = "要素单位不能为空";
          return;
        }
        if (
          element.serialNumber === "" ||
          element.serialNumber === null ||
          element.serialNumber === undefined
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
        if (element.isNull === "是") {
          element.isNull = "true";
        } else if (element.isNull === "否") {
          element.isNull = "false";
        }
        if (element.isUpdate === "是") {
          element.isUpdate = "true";
        } else if (element.isUpdate === "否") {
          element.isUpdate = "false";
        }
        if (element.isShow === "是") {
          element.isShow = "true";
        } else if (element.isShow === "否") {
          element.isShow = "false";
        }
        if (element.isManager === "是") {
          element.isManager = "true";
        } else if (element.isManager === "否") {
          element.isManager = "false";
        }
        if (element.isPrimaryKey === "是") {
          element.isPrimaryKey = "true";
        } else if (element.isPrimaryKey === "否") {
          element.isPrimaryKey = "false";
        }
        element.isNull = element.isNull.toLowerCase();
        element.isUpdate = element.isUpdate.toLowerCase();
        element.isShow = element.isShow.toLowerCase();
        element.isManager = element.isManager.toLowerCase();
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
          obj.isManager = false;
          obj.updateDatabase = false;
          obj.isPrimaryKey = false;
          obj.isShow = false;
          obj.serialNumber = 0;

          newRows.push(obj);
        }
      });
      return newRows;
    },
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
      this.columnData = this.tableInfo.columns;
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
