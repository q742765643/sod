<template>
  <el-main class="columnTemplate">
    <el-button-group style="padding: 8px;">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-c-scale-to-original"
        @click="getColumnTables"
      >复用字段</el-button>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="columnAdd">新增</el-button>
      <el-button type="primary" size="small" icon="el-icon-edit" @click="columnEdit">编辑</el-button>
      <el-button type="primary" size="small" icon="el-icon-delete" @click="columnDelete">删除</el-button>
      <el-button type="primary" size="small" icon="el-icon-document-copy" @click="columnCopy">复制</el-button>
      <el-button type="primary" size="small" icon="el-icon-document-add" @click="columnPaste">粘贴</el-button>
      <el-button type="primary" size="small" icon="el-icon-s-order" @click="columnControl">质控字段</el-button>
    </el-button-group>
    <el-button-group style="float: right;padding: 8px;">
      <el-button type="primary" size="small" icon="el-icon-sort" @click="handleSort">排序</el-button>
      <el-button type="primary" size="small" icon="el-icon-download" @click="exportCode('code')">导出</el-button>
      <el-button type="primary" size="small" icon="el-icon-upload2" @click="importTelplate">导入</el-button>
      <el-button type="primary" size="small" icon="el-icon-s-operation" @click="syncServe">同步服务名称</el-button>
    </el-button-group>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-table :data="columnData" border @selection-change="res=>selColumnData=res">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" class="demo-table-expand">
              <el-form-item label="是否可空">
                <span>{{ props.row.isNull }}</span>
              </el-form-item>
              <el-form-item label="是否可改">
                <span>{{ props.row.isUpdate }}</span>
              </el-form-item>
              <el-form-item label="是否显示">
                <span>{{ props.row.isShow }}</span>
              </el-form-item>
              <el-form-item label="是否主键">
                <span>{{ props.row.isPrimaryKey }}</span>
              </el-form-item>
              <el-form-item label="中文描述">
                <span>{{ props.row.nameCn }}</span>
              </el-form-item>
              <el-form-item label="是否管理字段">
                <span>{{ props.row.isManager }}</span>
              </el-form-item>
              <el-form-item label="默认值">
                <span>{{ props.row.defaultValue }}</span>
              </el-form-item>
              <el-form-item label="序号">
                <span>{{ props.row.serialNumber }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column type="index" width="70" align="center"></el-table-column>
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column
          label="公共元数据字段"
          prop="dbEleCode"
          width="200px"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="字段编码"
          prop="celementCode"
          width="200px"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="服务代码"
          prop="userEleCode"
          width="200px"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="中文简称" prop="eleName" align="center" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          label="数据类型"
          prop="type"
          width="100px"
          align="center"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column label="数据精度" prop="accuracy" width="100px" align="center"></el-table-column>
        <el-table-column label="要素单位" prop="unitCn" width="100px" align="center"></el-table-column>
      </el-table>
    </el-scrollbar>
    <el-dialog
      width="80%"
      title="公共元数据"
      :visible.sync="dialogStatus.publicMatedataDialog"
      append-to-body
    >
      <div>
        <el-tabs v-model="activePublicName" type="border-card">
          <el-tab-pane label="公共元数据字段" name="first">
            <div class="tableSearch">
              <el-form :inline="true">
                <el-form-item label="数据元代码">
                  <el-input v-model="searchMatedata.c_datatype" placeholder="数据源代码" size="small"></el-input>
                </el-form-item>
                <el-form-item label="中文名称">
                  <el-input v-model="searchMatedata.c_datum_level" placeholder="中文名称" size="small"></el-input>
                </el-form-item>
                <el-form-item label="服务名称">
                  <el-input v-model="searchMatedata.c_datumtype" placeholder="服务名称" size="small"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="el-icon-search"
                    size="small"
                    @click="searchFun('matedata')"
                  >搜索</el-button>
                </el-form-item>
              </el-form>
            </div>
            <el-table
              :data="pubData.matedata"
              border
              height="320"
              @selection-change="handleSelectionMatedata"
            >
              <el-table-column type="index" width="50"></el-table-column>
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column label="数据元代码" prop="celementCode"></el-table-column>
              <el-table-column label="中文名称" prop="c_element_namech"></el-table-column>
              <el-table-column label="英文名称" prop="c_element_name"></el-table-column>
              <el-table-column label="数据类型" prop="c_datatype" width="70px"></el-table-column>
              <el-table-column label="计量单位" prop="c_element_unit" width="80px"></el-table-column>
              <el-table-column label="数据精度" prop="c_element_pre" width="70px"></el-table-column>
              <el-table-column label="特征值" prop="c_charactervalue"></el-table-column>
              <el-table-column label="状态" prop="c_status" width="60px"></el-table-column>
              <el-table-column label="要素说明" prop="c_notes" width="70px"></el-table-column>
              <el-table-column label="服务名称" prop="c_short_name"></el-table-column>
              <el-table-column label="是否质控" prop="c_is_control" width="80px">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.switch"
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
                      <el-select v-model="searchMmdata.group_id" size="small">
                        <el-option label="全部" value></el-option>
                        <el-option label="常用管理字段" value="6C92BF6901A014125C4ED2B377384572"></el-option>
                        <el-option label="站点数据" value="6C92BF6901A014125C4ED55C773845DB"></el-option>
                        <el-option label="文件产品" value="6C92BF6901A014125C87A02075BD8BF4"></el-option>
                        <el-option label="待分组字段" value="6C92BF6901A014125CDA344C5F2E2372"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="字段名称">
                      <el-input v-model="searchMmdata.dbEleCode" placeholder="字段名称" size="small"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="中文名称">
                      <el-input v-model="searchMmdata.db_ele_name" placeholder="中文名称" size="small"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="服务名称">
                      <el-input v-model="searchMmdata.userEleCode" placeholder="服务名称" size="small"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="2">
                    <el-button
                      class="btnmm"
                      type="primary"
                      icon="el-icon-search"
                      size="small"
                      @click="searchFun('mmdata')"
                    >搜索</el-button>
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
              <el-table-column label="字段名称" prop="dbEleCode"></el-table-column>
              <el-table-column label="服务名称" prop="userEleCode"></el-table-column>
              <el-table-column label="中文名称" prop="db_ele_name"></el-table-column>
              <el-table-column label="字段短名" prop="eleName"></el-table-column>
              <el-table-column label="类型" prop="type"></el-table-column>
              <el-table-column label="字段精度" prop="data_precision" width="90px"></el-table-column>
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
        <el-button type="primary" @click="truePublic" size="small">确 定</el-button>
        <el-button @click="dialogStatus.publicMatedataDialog = false" size="small">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
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
            <el-input placeholder="公共元数据字段" v-model="columnEditData.dbEleCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="字段名称" prop="celementCode">
            <el-input placeholder="字段名称" v-model="columnEditData.celementCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务名称" prop="userEleCode">
            <el-input placeholder="服务名称" v-model="columnEditData.userEleCode"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="中文简称" prop="eleName">
            <el-input placeholder="中文简称" v-model="columnEditData.eleName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类型" prop="type">
            <el-select v-model="columnEditData.type" placeholder="请选择数据类型" style="width: 100%;">
              <el-option
                v-for="(item,index) in dataTypes"
                :key="index"
                :label="item.key_col"
                :value="item.key_col"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="要素单位" prop="unitCn">
            <el-input placeholder="要素单位" v-model="columnEditData.unitCn"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数据精度">
            <el-input placeholder="数据精度" v-model="columnEditData.accuracy"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否可空">
            <el-switch
              v-model="columnEditData.isNull"
              active-color="#13ce66"
              inactive-color="#909399"
              active-value="true"
              inactive-value="false"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否可改">
            <el-switch
              v-model="columnEditData.isUpdate"
              active-color="#13ce66"
              inactive-color="#909399"
              active-value="true"
              inactive-value="false"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否显示">
            <el-switch
              v-model="columnEditData.isShow"
              active-color="#13ce66"
              inactive-color="#909399"
              active-value="true"
              inactive-value="false"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否主键">
            <el-switch
              v-model="columnEditData.isPrimaryKey"
              active-color="#13ce66"
              inactive-color="#909399"
              active-value="true"
              inactive-value="false"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否管理字段">
            <el-switch
              v-model="columnEditData.isManager"
              active-color="#13ce66"
              inactive-color="#909399"
              active-value="true"
              inactive-value="false"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="默认值">
            <el-input placeholder="默认值" v-model="columnEditData.defaultValue"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="中文描述">
            <el-input placeholder="中文描述" v-model="columnEditData.nameCn"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序号" prop="serialNumber">
            <el-input placeholder="序号" v-model="columnEditData.serialNumber"></el-input>
          </el-form-item>
        </el-col>
        <div class="clear"></div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="trueCode('formCodeName')" size="small">确 定</el-button>
        <el-button @click="cancleCode('formCodeName')" size="small">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog width="80%" title="字段排序" :visible.sync="dialogStatus.codeSortDialog" append-to-body>
      <div class="dragBox">
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-alert title="请拖动字段实现排序" type="success" :closable="false"></el-alert>
          <draggable
            class="list-group"
            tag="ul"
            v-model="dragList"
            v-bind="dragOptions"
            @start="isDragging = true"
            @end="isDragging = false"
          >
            <transition-group type="transition" name="flip-list">
              <li class="list-group-item" v-for="(element,index) in dragList" :key="index">
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
                    <span>{{'字段名称:'}}{{ element.celementCode }}</span>
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
      class="exportCodeDialog"
      width="30%"
      title="操作"
      :visible.sync="exportInnerVisible"
      append-to-body
    >
      <el-row :v-if="exportInnerVisible">
        <el-col :span="12">
          <el-upload
            v-model="filepath"
            class="upload-demo"
            :limit="1"
            :on-exceed="handleExceed"
            :action="upLoadUrl"
            :on-remove="handleRemove"
            :on-success="successUpload"
          >
            <el-button size="small" type="primary">导入数据</el-button>
          </el-upload>
        </el-col>
        <el-col :span="12">
          <el-button type="success" size="small" @click="exportCode('demo')">下载模板</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </el-main>
</template>

<script>
// 拖拽组件
import draggable from "vuedraggable";
import {
  findByTableId,
  tableColumnDel,
  tableColumnSave,
  tableColumnSaveList
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  props: { tableInfo: Object, tableType: String },
  components: {
    draggable
  },
  data() {
    return {
      codeTitle: "新增字段",
      dialogStatus: {
        columnDialog: false,
        publicMatedataDialog: false,
        codeSortDialog: false
      },
      exportInnerVisible: false,
      filepath: "",
      upLoadUrl: "",
      columnEditData: { unitCn: "N" },
      columnData: [],
      selColumnData: [],
      pubData: { matedata: [], mmdata: [] }, //公共元数据字段，管理字段
      searchMatedata: {
        c_datatype: "",
        c_datum_level: "",
        c_datumtype: ""
      },
      searchMmdata: {
        group_id: "",
        dbEleCode: "",
        db_ele_name: "",
        userEleCode: ""
      },
      dataTypes: [],
      dragList: [],
      matedataSelection: [],
      mmdataSelection: [],
      activePublicName: "first",
      repeatIndex: 0,
      rules: {
        dbEleCode: [
          { required: true, message: "请输入公共元数据字段", trigger: "blur" }
        ],
        celementCode: [
          { required: true, message: "请输入字段名称", trigger: "blur" }
        ],
        userEleCode: [
          { required: true, message: "请输入服务名称", trigger: "blur" }
        ],
        eleName: [
          { required: true, message: "请输入中文简称", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请选择数据类型", trigger: "change" }
        ],
        unitCn: [
          { required: true, message: "请输入要素单位", trigger: "blur" }
        ],
        serialNumber: [
          { required: true, message: "请输入序号", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    async getCodeTable() {
      let flag = undefined;
      if (this.tableType == "E-show") {
        flag = "true";
      }
      await findByTableId({ tableId: this.tableInfo.id }).then(response => {
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
          message: "表不存在"
        });
        return;
      }
      this.columnEditData = {};
      this.codeTitle = "新增字段";
      this.dialogStatus.columnDialog = true;
    },

    columnEdit() {
      if (this.selColumnData.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        this.codeTitle = "编辑字段";
        this.columnEditData = JSON.parse(JSON.stringify(this.selColumnData[0]));
        this.dialogStatus.columnDialog = true;
      }
    },
    // 删除字段
    columnDelete() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        let ids = [];
        this.selColumnData.forEach(element => {
          ids.push(element.id);
        });
        tableColumnDel({ ids: ids.join(",") }).then(response => {
          if (response.code == 200) {
            this.$message({ message: "删除成功", type: "success" });
            this.getCodeTable();
          } else {
            this.$message({
              message: "删除失败",
              type: "warning"
            });
            return;
          }
        });
      }
    },
    getOptionsData() {
      //获取下拉选项数据
      this.axios
        .get(interfaceObj.TableStructure_getOptionsByType, {
          params: { type: 2 }
        })
        .then(res => {
          this.dataTypes = res.data.data;
        })
        .catch(error => {});
    },
    getColumnTables() {
      this.getmatedata();
      this.getmmdata();
      this.matedataSelection = [];
      this.mmdataSelection = [];
      this.dialogStatus.publicMatedataDialog = true;
    },
    getmatedata() {
      this.searchMatedata.c_datum_code = this.tableInfo.data_class_id;
      this.axios
        .get(interfaceObj.TableStructure_publicDatumColumn, {
          params: this.searchMatedata
        })
        .then(res => {
          if (res.data.returnCode == "0") {
            res.data.data.forEach(element => {
              element.switch = false;
            });
            this.pubData.matedata = res.data.data;
          }
        })
        .catch(error => {});
    },
    getmmdata() {
      this.axios
        .get(interfaceObj.TableStructure_manageColumn, {
          params: this.searchMmdata
        })
        .then(res => {
          this.pubData.mmdata = res.data.data;
        })
        .catch(error => {});
    },
    searchFun(type) {
      if (type == "matedata") {
        this.getmatedata();
      } else if (type == "mmdata") {
        this.getmmdata();
      }
    },
    cancleCode(formName) {
      this.$refs[formName].resetFields();
      this.dialogStatus.columnDialog = false;
    },
    // 字段新增编辑
    trueCode(formName) {
      //校验表单
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.columnEditData.id = this.tableInfo.id;
          if (this.selColumnData.length > 0) {
            this.columnEditData.column_id = this.selColumnData[0].column_id;
          }
          if (this.tableType == "E-show") {
            this.columnEditData.is_kv_k = "true";
          }
          dataTableSavle(this.columnEditData).then(response => {
            if (response.code == 200) {
              this.$message({ message: "操作成功", type: "success" });
              this.$refs[formName].resetFields();
              this.dialogStatus.columnDialog = false;
              this.getCodeTable();
              this.columnEditData = [];
            } else {
              this.$message({
                type: "error",
                message: "操作失败"
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
          message: "当前没有字段，无法排序"
        });
      }
      this.dragList = this.columnData;
      this.dialogStatus.codeSortDialog = true;
    },
    trueSort() {
      this.dragList.forEach((item, index) => {
        item.serialNumber = index;
      });
      tableColumnSaveList({ tableColumnList: this.dragList }).then(res => {
        if (res.code == 200) {
          this.$message({
            message: "排序成功！",
            type: "success"
          });
          this.getCodeTable();
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
      this.dialogStatus.codeSortDialog = false;
    },
    exportCode(type) {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在"
        });
        return;
      }
      let url;
      if (type == "code") {
        url =
          interfaceObj.TableStructure_columnExport + "?id=" + this.tableInfo.id;
      } else {
        url = interfaceObj.TableStructure_importTelplate;
      }
      this.axios
        .get(url)
        .then(data => {
          testExport(url);
        })
        .catch(function(error) {
          testExport(url);
        });
    },
    // 上传限制
    handleExceed() {
      this.$message.warning("当前限制选择1个文件");
    },
    handleRemove(file, fileList) {},
    successUpload: function(response, file, fileList) {
      if (response.returnCode == 0) {
        this.$message({
          type: "success",
          message: "导入成功，已同步服务名称,请修改对应的数据同步配置！"
        });
        this.getCodeTable();
        // 导入成功，已同步服务名称,请修改对应的数据同步配置！
        this.syncServe();
        this.exportInnerVisible = false;
      }
      // this.filepath = response.filepath;
    },
    importTelplate() {
      if (!this.tableInfo.id) {
        this.$message({
          type: "error",
          message: "表不存在"
        });
        return;
      }
      this.handleRemove();
      this.upLoadUrl =
        interfaceObj.TableStructure_importColumn + "?id=" + this.tableInfo.id;

      this.exportInnerVisible = true;
    },

    columnCopy() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        let copyArry = [];
        this.selColumnData.forEach(element => {
          let obj = {};
          element.id = "";
          element.tableId = "";
          obj = element;
          copyArry.push(obj);
        });
        sessionStorage.setItem("copyColumn", JSON.stringify(copyArry));
        this.$message({
          message: "复制成功",
          type: "success"
        });
      }
    },
    columnPaste() {
      this.repeatIndex = 0;
      let pasteArry = JSON.parse(sessionStorage.getItem("copyColumn"));
      pasteArry = this.array_diff(pasteArry, this.columnData, "paste");
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
            pasteArry.forEach(element => {
              element.id = "";
              element.tableId = this.tableInfo.id;
            });
            tableColumnSaveList({ tableColumnList: pasteArry }).then(res => {
              if (res.code == 200) {
                this.$message({
                  message: "插入成功！",
                  type: "success"
                });
                this.getCodeTable();
              } else {
                this.$message({
                  message: res.msg,
                  type: "error"
                });
              }
            });
          }
        });
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
            this.columnData,
            "first"
          );
          publicRows.forEach(element => {
            if (element.switch) {
              var switchObj = Object.assign({}, element);
              var firstChar = element.celementCode.substr(0, 1);
              let newCode = element.celementCode.replace(firstChar, "Q");
              switchObj.eleName = element.eleName + "质量标志";
              switchObj.celementCode = newCode;
              switchObj.dbEleCode = newCode;
              publicRows.push(switchObj);
            }
          });
        } else {
          this.$message({
            message: msg + "无剩余可添加数据!",
            type: "info"
          });
        }
      } else if (this.activePublicName == "second") {
        if (this.mmdataSelection.length > 0) {
          publicRows = this.array_diff(
            this.mmdataSelection,
            this.columnData,
            "second"
          );
        } else {
          this.$message({
            message: msg + "无剩余可添加数据!",
            type: "info"
          });
        }
      }

      let msg = "";
      if (this.repeatIndex > 0) msg = "去重(" + this.repeatIndex + ")条,";
      if (publicRows.length > 0) {
        if (this.tableType == "E-show") {
          publicRows.forEach(element => {
            element.is_kv_k = "true";
          });
        }
        this.axios
          .post(interfaceObj.TableStructure_addManageColumn, {
            manageColumnList: publicRows
          })
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                message: msg + "添加成功！",
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
      } else {
        this.$message({
          message: msg + "无剩余可添加数据!",
          type: "success"
        });
      }
      this.dialogStatus.publicMatedataDialog = false;
    },
    // 质控字段
    columnControl() {
      if (this.selColumnData.length == 0) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
        });
      } else {
        this.$alert(
          "是否插入" + this.selColumnData.length + "条质控字段",
          "温馨提示",
          {
            confirmButtonText: "确定",
            callback: action => {
              let publicRows = this.selColumnData;
              publicRows.forEach(element => {
                var switchObj = Object.assign({}, element);
                var firstChar = element.celementCode.substr(0, 1);
                let newCode = element.celementCode.replace(firstChar, "Q");
                switchObj.eleName = element.eleName + "质量标志";
                switchObj.celementCode = newCode;
                switchObj.dbEleCode = newCode;
                publicRows.push(switchObj);
              });
              this.axios
                .post(interfaceObj.TableStructure_addManageColumn, {
                  manageColumnList: publicRows
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
          }
        );
      }
    },
    // 同步服务
    async syncServe() {
      await this.axios
        .post(interfaceObj.TableStructure_syncServiceName, {
          columnList: this.columnData
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              message: "同步成功！",
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
    },
    array_diff(a, b, tabActive) {
      for (var i = 0; i < b.length; i++) {
        for (var j = 0; j < a.length; j++) {
          if (a[j].celementCode == b[i].celementCode) {
            a.splice(j, 1);
            j = j - 1;
            this.repeatIndex++;
          }
        }
      }
      let newRows = [];
      a.forEach(element => {
        let obj = {};
        if (tabActive == "paste") {
          obj = element;
          obj.id = this.tableInfo.id;
          newRows.push(obj);
        } else {
          if (tabActive == "first") {
            obj.eleName = element.c_element_namech;
            obj.dbEleCode = element.celementCode;
            obj.celementCode = element.celementCode;
            obj.type = element.c_datatype;
            obj.accuracy = element.c_element_pre;
            obj.isNull = false;
            obj.isUpdate = false;
          } else if (tabActive == "second") {
            obj.eleName = element.db_ele_name;
            obj.dbEleCode = element.dbEleCode;
            obj.celementCode = element.dbEleCode;
            obj.type = element.type;
            obj.accuracy = element.data_precision;
            obj.isNull = element.isNull;
            obj.isUpdate = element.isUpdate;
          }
          obj.switch = element.switch;
          obj.id = this.tableInfo.id;
          obj.isManager = false;
          obj.isPrimaryKey = false;
          obj.isShow = false;
          obj.nameCn = element.c_notes;
          obj.serialNumber = 0;
          obj.unit = element.c_element_unit;
          obj.unitCn = element.c_element_unit;
          obj.userEleCode = element.c_short_name;
          newRows.push(obj);
        }
      });
      return newRows;
    }
  },
  mounted() {
    // this.getOptionsData();
  },
  computed: {
    dragOptions() {
      return {
        animation: 0,
        group: "description",
        disabled: false,
        ghostClass: "ghost"
      };
    }
  },
  watch: {
    tableInfo(val) {
      let flag = undefined;
      if (this.tableType == "E-show") {
        flag = "true";
      }
      this.columnData = this.tableInfo.columns;
    }
  }
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
