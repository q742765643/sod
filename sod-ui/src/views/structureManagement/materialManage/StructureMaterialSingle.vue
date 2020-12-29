<template>
  <!-- 新增或者编辑资料 -->
  <section class="structureMaterialSingle">
    <div class="materialCon selScrollBar">
      <el-form
        :model="materialData"
        ref="materialForm"
        :rules="rules"
        :inline="true"
        :label-width="labelWidth"
      >
        <!-- 公共元数据信息 -->
        <div class="publicData" v-if="tableStructureManageContral">
          <div class="publicDataTitle">
            <i class="el-icon-price-tag"></i>公共元数据信息
          </div>
          <div class="publicInfo">
            <el-form-item>
              <label slot="label">
                <span class="red" v-if="ddataIdFlag">*</span>
                名称
              </label>
              <treeselect
                @select="getChildCheckNode"
                v-model.trim="materialData.ddataId"
                :options="publicTreeOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="请选择名称"
              />
            </el-form-item>
            <el-form-item>
              <label slot="label">
                <span class="red" v-if="ddataIdFlag">*</span>
                四级编码
              </label>
              <el-input
                clearable
                placeholder="请选择公共元数据"
                :readonly="true"
                v-model.trim="materialData.ddataId"
              ></el-input>
            </el-form-item>
          </div>
        </div>
        <!-- 存储元数据信息 -->
        <div class="storageData">
          <div class="storageDataTitle">
            <i class="el-icon-price-tag"></i>存储元数据信息
          </div>
          <div class="storageInfo">
            <el-form-item label="资料名称" prop="className">
              <el-input
                clearable
                placeholder="资料名称"
                v-model.trim="materialData.className"
              ></el-input>
            </el-form-item>
            <el-form-item label="存储编码" prop="dataClassId">
              <el-input
                clearable
                :disabled="isDisabledEdit"
                placeholder="存储编码"
                v-model.trim="materialData.dataClassId"
              ></el-input>
            </el-form-item>
            <el-form-item label="父节点" prop="parentId">
              <!--  <el-input
                placeholder="请选择存储元数据父节点"
                :readonly="true"
                v-model.trim="materialData.parentId"
                class="styleCursor"
                @click.native="showStorageTree"
              ></el-input>-->
              <treeselect
                @select="getStorageCheckNode"
                v-model.trim="materialData.parentId"
                :options="storageTree"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="请选择存储元数据父节点"
              />
            </el-form-item>
            <el-form-item label="类型" v-if="isSourceTree">
              <el-input
                v-model.trim="materialData.typeText"
                :readonly="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="访问控制">
              <el-select v-model.trim="materialData.isAccess">
                <el-option :value="1" label="公开"></el-option>
                <el-option :value="2" label="限制"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排序" prop="serialNo">
              <el-input-number
                v-model.trim="materialData.serialNo"
                :min="0"
              ></el-input-number>
              <!-- <el-input type="number" v-model.trim="materialData.serialNo" :min="0"></el-input> -->
            </el-form-item>
            <el-form-item label="是否发布" v-if="tableStructureManageContral">
              <el-select v-model.trim="materialData.ifStopUse">
                <el-option :value="true" label="发布"></el-option>
                <el-option :value="false" label="不发布"></el-option>
                <!-- <el-option :value="true" label="全部发布"></el-option>
                <el-option :value="false" label="不发布"></el-option>
                <el-option :value="3" label="部分发布"></el-option>-->
              </el-select>
            </el-form-item>
            <el-form-item
              label="基础信息配置"
              v-if="!isSourceTree && tableStructureManageContral"
            >
              <el-select v-model.trim="materialData.useBaseInfo">
                <el-option :value="1" label="启用"></el-option>
                <el-option :value="0" label="停用"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model.trim="materialData.remark"></el-input>
            </el-form-item>
          </div>
        </div>
        <div class="dictData">
          <div class="dictDataTitle">
            <i class="el-icon-price-tag"></i>数据标签
          </div>
          <div class="dictInfo">
            <el-form-item label="数据标签" prop="labelKeyFrom">
              <el-collapse accordion>
                <el-collapse-item>
                  <template slot="title">
                    <p v-show="SelectDictdataTypes.length > 0">
                      <span
                        v-for="(item, index) in SelectDictdataTypes"
                        :key="index"
                        >{{ item.dictLabel }}</span
                      >
                    </p>
                    <span v-show="SelectDictdataTypes.length == 0"
                      >请选择数据标签</span
                    >
                  </template>
                  <div>
                    <el-checkbox-group
                      v-model="materialData.labelKeyFrom"
                      @change="setSelectDictdataTypes"
                    >
                      <el-checkbox
                        :label="item.dictValue"
                        v-for="(item, index) in dictdataTypes"
                        :key="index"
                        >{{ item.dictLabel }}</el-checkbox
                      >
                    </el-checkbox-group>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </el-form-item>
          </div>
        </div>
        <div class="dictData" v-show="materialData.ifStopUse == 3">
          <div class="dictDataTitle">
            <i class="el-icon-price-tag"></i>数据权限
          </div>
          <div class="dictInfo">
            <el-form-item
              label="数据权限"
              prop="hasPowerList"
              style="width: 100%"
            >
              <el-transfer
                filterable
                :filter-method="filterMethod"
                filter-placeholder="请输入用户名称"
                :titles="['待授权用户列表', '已授权用户列表']"
                v-model="materialData.hasPowerList"
                :data="userList"
              ></el-transfer>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <!-- 关联表信息 -->
      <div class="editDataUse" v-if="!isSourceTree">
        <h4>关联表信息</h4>
        <div class="linkInfo">
          <el-form v-model="materialData.dataLogicList" label-width="90px">
            <el-row
              v-for="(item, index) in materialData.dataLogicList"
              :key="index"
            >
              <el-col :span="4">
                <el-form-item label="数据库">
                  <el-select
                    filterable
                    v-model="item.databasePid"
                    placeholder="请选择"
                    @change="handleFindSpec($event, index)"
                  >
                    <el-option
                      v-for="(citem, cindex) in DatabaseDefineList"
                      :key="cindex"
                      :label="citem.databaseName"
                      :value="citem.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="专题库">
                  <el-select
                    filterable
                    v-model="item.databaseId"
                    placeholder="请选择"
                    @change="handleFindTable($event, index)"
                  >
                    <el-option
                      v-for="(citem, cindex) in item.specialList"
                      :key="cindex"
                      :label="citem.databaseName"
                      :value="citem.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="要素表名称">
                  <el-select
                    filterable
                    v-model="item.tableId"
                    placeholder="请选择"
                    @change="handleFindTableType($event, index)"
                  >
                    <el-option
                      v-for="(citem, cindex) in item.tableOptionList"
                      :key="cindex"
                      :label="citem.NAME_CN"
                      :value="citem.ID"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4" v-show="item.subTableId">
                <el-form-item label="键表名称">
                  <el-input
                    v-model="item.subTableNameCn"
                    class="elInput"
                    readonly
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="表类型">
                  <el-input
                    v-model="item.tabaleType"
                    class="elInput"
                    readonly
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <i class="el-icon-plus" @click="addDomain"></i>
                  <i
                    class="el-icon-minus"
                    @click.prevent="removeDomain(index)"
                  ></i>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </div>
    <div class="dialog-footer" slot="footer" v-if="!DRegistrationObj">
      <el-button type="primary" @click="makeSureSave('materialForm')"
        >确认</el-button
      >
      <el-button @click="cancleSave">取消</el-button>
    </div>

    <!-- 公共元数据资料树 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="公共元数据资料树"
      :visible.sync="publicTreeVisible"
      :append-to-body="true"
    >
      <StructurePublicTree
        v-if="publicTreeVisible"
        @returnCheckedNode="getChildCheckNode"
        @closePublicDialog="closePublicDialog"
      />
    </el-dialog>
    <!-- 存储元数据资料树 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="存储元数据资料树"
      :visible.sync="storageTreeVisible"
      :append-to-body="true"
    >
      <StructureStorageTree
        v-if="storageTreeVisible"
        :typeText="materialData.typeText"
        @storageCheckedNode="getStorageCheckNode"
        @closeStorageTreeDialog="closeStorageTreeDialog"
      />
    </el-dialog>
  </section>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  getBizUserByName,
  mmdneed,
  save,
  getDetailById,
  enable,
  getNewDataClassId,
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
  findETable,
  findTables,
} from "@/api/structureManagement/materialManage/index";
import { getDictByType } from "@/api/structureManagement/materialManage/StructureManageTable";
import { dataClassAll } from "@/api/structureManagement/materialManage/StructureClassify";
import { datumTypeGetTree } from "@/api/structureManagement/materialManage/StructureClassify";
// 公共元数据资料树
import StructurePublicTree from "@/views/structureManagement/materialManage/StructurePublicTree";
// 存储元数据资料树
import StructureStorageTree from "@/views/structureManagement/materialManage/StructureStorageTree";
// 关联表信息
import EditDataUseDialog from "@/views/structureManagement/materialManage/EditDataUseDialog";

//接口地址
export default {
  name: "structureMaterialSingle",
  components: {
    Treeselect,
    EditDataUseDialog,
    StructurePublicTree,
    StructureStorageTree,
  },
  props: {
    isSourceTree: {
      type: Boolean,
    },
    editNodeId: {
      type: String,
    },
    editMaterial: {
      type: String,
    },
    DRegistrationObj: {
      type: Object,
    },
  },
  data() {
    return {
      DatabaseDefineList: [], //数据库列表
      ddataIdFlag: false, //公共元数据信息是否必填
      dictshowflag: false,
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1;
      }, //用户列表查询
      userList: [], //用户列表
      dictdataTypes: [], //标签
      SelectDictdataTypes: [],
      publicTreeOptions: [], //资料名称
      storageTree: [], //父节点
      normalizer(node) {
        return {
          id: node.id,
          label: node.name,
          children: node.children,
        };
      },
      tableStructureManageContral: false,
      labelWidth: "100px",

      isDisabledEdit: false,
      materialData: {
        ddataId: "",
        ddataId: null,
        dataClassId: "",
        className: "",
        parentId: null,
        typeText: "资料",
        ifStopUse: true,
        isAccess: 1,
        useBaseInfo: 0,
        serialNo: 0,
        dataLogicList: [
          {
            tableId: "",
            dataLogicList: "",
            databasePid: "",
            databaseId: "",
            tabaleType: "",
            subTableId: "",
            specialList: [], //专题库列表
            tableOptionList: [], //表名称列表
          },
        ], //数据用途回显的树
        dataLogicListTable: [], //数据用途回显的树
        labelKeyFrom: [],
        dataClassLabelList: [],
        hasPowerList: [],
      },
      publicTreeVisible: false, //公共元数据资料树弹出层
      storageTreeVisible: false, //存储元数据资料树弹出层
      rules: {
        ddataId: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "change",
          },
        ],
        ddataId: [
          {
            required: true,
            message: "四级编码不能为空",
            trigger: "change",
          },
        ],
        parentId: [
          {
            required: true,
            message: "父节点不能为空",
            trigger: "change",
          },
        ],
        className: [
          {
            required: true,
            message: "资料名称不能为空",
            trigger: "change",
          },
        ],
        dataClassId: [
          {
            required: true,
            message: "存储编码不能为空",
            trigger: "change",
          },
        ],
        serialNo: [
          {
            required: true,
            message: "排序不能为空",
            trigger: "change",
          },
        ],
      },
    };
  },
  async created() {
    await getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
    await mmdneed().then((res) => {
      this.ddataIdFlag = res.data;
    });
    await enable().then((res) => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
    // 名称树
    await datumTypeGetTree().then((response) => {
      this.resetData(response.data);
      this.publicTreeOptions = response.data;
      console.log(this.publicTreeOptions);
    });
    // 父节点树
    await dataClassAll().then((response) => {
      const menu = { id: 0, name: "主类目", children: [] };
      this.resetData(response.data);
      menu.children = response.data;
      this.storageTree.push(menu);
    });
    // 标签
    await getDictByType({ dictType: "zt_label" }).then((response) => {
      this.dictdataTypes = response.data;
    });
    // 用户列表 todo
    /*   await getBizUserByName().then((response) => {
      var resdata = response.data;
      var dataList = [];
      for (var i = 0; i < resdata.length; i++) {
        var obj = {};
        obj.key = resdata[i].userName;
        obj.label = resdata[i].userName + "(" + resdata[i].nickName + ")";
        dataList.push(obj);
      }
      this.userList = dataList;
    }); */
    // 初始化
    await this.initMaterialForm();
  },
  methods: {
    forceUpdate() {
      this.$forceUpdate();
    },
    //切换数据库查询专题库
    async handleFindSpec(val, index, type) {
      this.$forceUpdate();
      if (!type) {
        this.materialData.dataLogicList[index].databaseId = "";
      }

      await findByDatabaseDefineId({ id: val }).then((res) => {
        this.materialData.dataLogicList[index].specialList = res.data;
      });
    },
    //切换专题库查询
    async handleFindTable(val, index, type) {
      this.$forceUpdate();
      if (!type) {
        this.materialData.dataLogicList[index].tableId = "";
      }
      await findETable({ databaseId: val }).then((res) => {
        this.materialData.dataLogicList[index].tableOptionList = res.data;
      });
    },
    //根据表名称查询表类型
    async handleFindTableType(val, index, type) {
      if (!type) {
        this.materialData.dataLogicList[index].tabaleType = "";
        this.materialData.dataLogicList[index].tableId = "";
        this.materialData.dataLogicList[index].subTableId = "";
      }
      await findTables({ tableId: val }).then((res) => {
        let resData = res.data;
        if (resData.length == 1) {
          this.materialData.dataLogicList[index].tableId = resData[0].ID;
        } else {
          resData.forEach((element) => {
            if (element.TABLE_TYPE == "E") {
              this.materialData.dataLogicList[index].tableId = element.ID;
            } else {
              //双表的话键表是subTableId
              this.materialData.dataLogicList[index].subTableNameCn =
                element.NAME_CN;
              this.materialData.dataLogicList[index].subTableId = element.ID;
            }
          });
        }
        this.materialData.dataLogicList[index].tabaleType =
          resData[0].DICT_LABEL;
      });
      this.$forceUpdate();
    },
    //添加移除关联表信息
    addDomain() {
      this.materialData.dataLogicList.push({
        tableId: "",
        subTableId: "",
        dataLogicList: "",
        databasePid: "",
        databaseId: "",
        tabaleType: "",
        specialList: [], //专题库列表
        tableOptionList: [], //表名称列表
      });
    },
    removeDomain(index) {
      //  var index = this.superSearchForm.domains.indexOf(item);
      if (index !== 0) {
        this.materialData.dataLogicList.splice(index, 1);
      }
    },
    setSelectDictdataTypes(val) {
      this.SelectDictdataTypes = [];
      val.forEach((element) => {
        this.dictdataTypes.forEach((item) => {
          if (element == item.dictValue) {
            this.SelectDictdataTypes.push(item);
          }
        });
      });
    },
    resetData(dataArr) {
      for (var i in dataArr) {
        dataArr[i].name = dataArr[i].name + "(" + dataArr[i].id + ")";
        this.resetData(dataArr[i].children);
      }
    },
    //初始化表单
    initMaterialForm() {
      if (this.isSourceTree) {
        this.materialData.typeText = "目录";
      }
      if (!this.isSourceTree) {
        this.materialData.typeText = "资料";
      }
      //资料树  初始化
      if (this.isSourceTree && this.editNodeId) {
        this.isDisabledEdit = true;
        this.getMaterialForm(this.editNodeId);
      }
      //资料  初始化
      if (!this.isSourceTree && this.editMaterial) {
        this.isDisabledEdit = true;
        this.getMaterialForm(this.editMaterial);
      }

      // 从数据注册审核来的资料
      if (this.DRegistrationObj) {
        this.materialData.applyId = this.DRegistrationObj.applyId;
        // this.materialData.metaDataName = this.DRegistrationObj.TYPE_NAME;
        this.materialData.ddataId = this.DRegistrationObj.D_DATA_ID;
        this.materialData.dataLogicListTable = this.DRegistrationObj.dataLogicList;
        getNewDataClassId({ parentId: this.materialData.ddataId }).then(
          (res) => {
            if (res.code == 200) {
              this.materialData.dataClassId = res.data;
              if (res.data == "") {
                let code = checkNode.id.split(".");
                if (code.length == 4) {
                  let fCode = code[3].substring(0, 1);
                  code[3] = code[3].replace(fCode, "M");
                  this.materialData.dataClassId = code.join(".");
                } else {
                  this.materialData.dataClassId = checkNode.id;
                }
              }
            }
          }
        );
        console.log(this.materialData);
      }
    },
    //获取资料树，资料的数据
    async getMaterialForm(id) {
      console.log(id);
      await getDetailById({ id: id }).then((response) => {
        if (response.code == "200") {
          if (!response.data.dataClassLabelList) {
            response.data.labelKeyFrom = [];
          } else {
            response.data.labelKeyFrom = [];
            response.data.dataClassLabelList.forEach((element) => {
              response.data.labelKeyFrom.push(element.labelKey);
            });
            this.SelectDictdataTypes = [];
            this.dictdataTypes.forEach((element) => {
              response.data.dataClassLabelList.forEach((item) => {
                if (item.labelKey == element.dictValue) {
                  let obj = {
                    dictLabel: element.dictLabel,
                  };
                  this.SelectDictdataTypes.push(obj);
                }
              });
            });
          }

          if (!response.data.dataClassUserList) {
            response.data.dataClassUserList = [];
          } else {
            response.data.hasPowerList = [];
            response.data.dataClassUserList.forEach((element) => {
              response.data.hasPowerList.push(element.userName);
            });
          }
          if (
            response.data.dataLogicList &&
            response.data.dataLogicList.length > 0
          ) {
            let logcList = response.data.dataLogicList;
            logcList.forEach(async (item, index) => {
              await this.handleFindSpec(item.databasePid, index, "edit");
              await this.handleFindTable(item.databaseId, index, "edit");
              await this.handleFindTableType(item.tableId, index, "edit");
            });
          }
          this.materialData = response.data;
          if (this.materialData.type == 1) {
            this.materialData.typeText = "目录";
          } else {
            this.materialData.typeText = "资料";
          }

          console.log(this.materialData.dataLogicList);
        } else {
          console.log("出错了，出错信息：" + response.msg);
        }
      });
    },
    // 显示公共元数据资料树
    showPublicTree() {
      this.publicTreeVisible = true;
    },
    //从公共元数据获取数据
    getChildCheckNode(checkNode) {
      console.log(checkNode);
      if (!this.isDisabledEdit) {
        if (this.materialData.typeText == "目录") {
          this.materialData.dataClassId = checkNode.id;
        } else if (this.materialData.typeText == "资料") {
          getNewDataClassId({ parentId: checkNode.id }).then((res) => {
            if (res.code == 200) {
              this.materialData.dataClassId = res.data;
              if (res.data == "") {
                let code = checkNode.id.split(".");
                if (code.length == 4) {
                  let fCode = code[3].substring(0, 1);
                  code[3] = code[3].replace(fCode, "M");
                  this.materialData.dataClassId = code.join(".");
                } else {
                  this.materialData.dataClassId = checkNode.id;
                }
              }
            }
          });
        }

        this.materialData.className = checkNode.name;
      }

      this.materialData.ddataId = checkNode.id;
      // this.materialData.metaDataName = checkNode.name;
      // this.publicTreeVisible = false;
    },
    //关闭公共元数据弹出层
    closePublicDialog() {
      this.publicTreeVisible = false;
    },
    // 显示存储元数据资料树
    showStorageTree() {
      this.storageTreeVisible = true;
    },
    //从存储元数据获取数据
    getStorageCheckNode(checkNode) {
      if (this.isSourceTree) {
        this.materialData.parentId = checkNode.id;
      } else {
        if (checkNode.id === 0 || checkNode.id.split(".").length == 3) {
          this.materialData.parentId = checkNode.id;
        } else {
          this.$message({
            type: "error",
            message: "只能选择三级目录或者主类目",
          });
          this.$nextTick(function () {
            this.materialData.parentId = null;
          });
        }
      }
    },
    //关闭存储元数据弹出层
    closeStorageTreeDialog() {
      this.storageTreeVisible = false;
    },
    // 显示关联表信息弹出层
    async showEditDataUse() {
      await getCanShowDatabaseDefineList().then((res) => {
        this.DatabaseDefineList = res.data;
      });
      if (this.materialData.dataLogicList.length == 0) {
        this.materialData.dataLogicList = [
          {
            tableId: "",
            dataLogicList: "",
            databasePid: "",
            databaseId: "",
            tabaleType: "",
            subTableId: "",
            specialList: [], //专题库列表
            tableOptionList: [], //表名称列表
          },
        ];
      }
      this.editDataUseVisible = true;
    },

    // 保存数据
    makeSureSave(resForm) {
      if (this.ddataIdFlag && !this.materialData.ddataId) {
        this.$message({
          type: "error",
          message: "请选择名称",
        });
      }
      this.$refs["materialForm"].validate((valid) => {
        if (valid) {
          // 1为目录   2为资料
          if (this.materialData.typeText == "目录") {
            this.materialData.type = 1;
          } else if (this.materialData.typeText == "资料") {
            this.materialData.type = 2;
          }
          let handleMessage = "";
          let refreshWhich = "";
          //资料分类树新增或编辑
          if (this.isSourceTree) {
            refreshWhich = "tree";
            // 编辑
            if (this.editNodeId !== "") {
              handleMessage = "编辑资料树节点成功";
            }
            //新增
            else {
              handleMessage = "新增资料树节点成功";
            }
          }
          //资料的新增或编辑
          else {
            // 编辑
            if (this.editMaterial !== "") {
              handleMessage = "编辑资料成功";
            }
            //新增
            else {
              handleMessage = "新增资料成功";
            }
            this.materialData.dataClassLabelList = [];
            this.dictdataTypes.forEach((element) => {
              this.materialData.labelKeyFrom.forEach((item) => {
                if (element.dictValue == item) {
                  let obj = {
                    labelKey: item,
                  };
                  this.materialData.dataClassLabelList.push(obj);
                }
              });
            });

            this.materialData.dataClassUserList = [];
            this.materialData.hasPowerList.forEach((element) => {
              let obj = {
                userName: element,
              };
              this.materialData.dataClassUserList.push(obj);
            });
            console.log(this.materialData);
            this.materialData.dataLogicList.forEach((element) => {
              element.dataClassId = this.materialData.dataClassId;
            });
          }

          console.log(this.materialData);
          //数据注册审核的数据用途只能选一条
          if (!resForm) {
            let dataLogicListTable = this.materialData.dataLogicListTable;
            if (dataLogicListTable.length != 1) {
              this.$message({
                type: "error",
                message: "只能选择一条数据用途",
              });
            } else if (
              !dataLogicListTable[0].storageType ||
              dataLogicListTable[0].storageType == null ||
              dataLogicListTable[0].storageType == ""
            ) {
              this.$message({
                type: "error",
                message: "请选择存储类型",
              });
            } else {
              this.saveData(handleMessage, refreshWhich);
            }
          } else {
            this.saveData(handleMessage, refreshWhich);
          }
        }
      });
    },
    saveData(handleMessage, refreshWhich) {
      save(this.materialData).then((response) => {
        if (response.success === true) {
          this.$message({
            type: "success",
            message: handleMessage,
          });
          if (refreshWhich == "tree") {
            //资料树节点 新增  编辑  isSourceTree:true   isSourceTree:false
            this.$emit("addOrEditSuccess", "handleTree");
          } else {
            if (this.DRegistrationObj) {
              this.$emit("addOrEditSuccess", response.data);
            } else {
              this.$emit("addOrEditSuccess", "handleTable");
            }
          }
        } else {
          this.$message({
            type: "error",
            message: response.msg,
          });
        }
      });
    },

    cancleSave() {
      this.$emit("closeMaterialDialog");
    },
  },
};
</script>

<style lang="scss">
.structureMaterialSingle {
  .el-collapse {
    border: 1px solid #e6ebf5;
    .el-collapse-item__header {
      padding-left: 10px;
    }
    .el-collapse-item__content {
      border-top: 1px solid #e6ebf5;
      padding-top: 25px;
    }
  }
  .publicDataTitle,
  .storageDataTitle,
  .dictDataTitle {
    box-sizing: border-box;
    padding: 10px;
    border: 1px solid #ebeef5;
    background: #f2f2f2;

    i {
      font-size: 16px;
      margin-right: 4px;
    }
  }

  .publicInfo,
  .storageInfo,
  .dictInfo {
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    flex-wrap: wrap;
    align-items: center;
    border: 1px solid #ebeef5;
    border-top: none;
    padding-top: 10px;
    margin-bottom: 10px;

    .el-form-item {
      margin-bottom: 18px;
      text-align: center;
      width: 50%;
      margin-right: 0px;
    }

    .styleCursor .el-input__inner {
      cursor: pointer;
    }

    .el-select {
      width: 100%;
    }
  }

  .el-form-item__content {
    width: 70%;
  }

  .editDataUse {
    .el-select {
      width: 100%;
    }
    .el-icon-plus,
    .el-icon-minus {
      width: 16px;
      height: 16px;
      color: #fff;
      border-radius: 4px;
      cursor: pointer;
      text-align: center;
      line-height: 16px;
    }

    .el-icon-plus {
      background: #409eff;
    }

    .el-icon-minus {
      background: #f56c6c;
      margin-left: 8px;
    }
    h4 {
      font-size: 15px;
      height: 39px;
      line-height: 39px;
      border-bottom: 3px solid rgba(155, 208, 245, 1);
      font-weight: bold;
      color: #333;
      position: relative;
      background: url("./images/editDataUse.png") no-repeat left center;
      text-indent: 28px;
    }

    h4:before {
      content: "";
      width: 90px;
      border-bottom: 3px solid rgba(5, 138, 229, 1);
      position: absolute;
      top: 36px;
      left: 0px;
    }

    .editUseDiv {
      box-sizing: border-box;
      padding: 10px;
      text-align: center;
      border: 1px solid #ebeef5;
      cursor: pointer;
      background: #ecf5ff;
      margin-bottom: 10px;
    }
  }

  .editDataUse {
    margin-bottom: 20px;
  }

  .el-input-number {
    width: 100%;
  }
  .el-transfer-panel {
    width: 400px;
    .el-checkbox-group {
      text-align: left;
      .el-checkbox {
        display: block;
      }
    }
    .el-transfer-panel__header {
      .el-checkbox {
        text-align: left;
      }
    }
  }
}
</style>
