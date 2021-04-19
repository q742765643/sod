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
        <!--  <div class="publicData" v-if="tableStructureManageContral">
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
        </div> -->
        <!-- 存储元数据信息 -->
        <div class="storageData">
          <div class="storageDataTitle">
            <i class="el-icon-price-tag"></i>存储元数据信息
          </div>
          <div class="storageInfo">
            <el-form-item label="资料名称" prop="dataClassId">
              <treeselect
                @select="getStorageCheckNode"
                v-model.trim="materialData.dataClassId"
                :options="storageTree"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="请选择资料名称"
              />
            </el-form-item>
            <el-form-item label="存储编码" prop="dataClassId">
              <el-input
                clearable
                disabled
                placeholder="存储编码"
                v-model.trim="materialData.dataClassId"
              ></el-input>
            </el-form-item>
            <!--   <el-form-item label="父节点" prop="parentId">
              <treeselect
                v-if="isSourceTree"
                @select="getStorageCheckNode"
                v-model.trim="materialData.parentId"
                :options="storageTree"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="请选择存储元数据父节点"
              />
            </el-form-item> -->
            <el-form-item label="类型" v-if="isSourceTree">
              <el-input
                v-model.trim="materialData.typeText"
                :readonly="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="数据分级" v-if="!isSourceTree">
              <el-select v-model.trim="materialData.dataLevel">
                <el-option :value="1" label="L1"></el-option>
                <el-option :value="2" label="L2"></el-option>
                <el-option :value="3" label="L3"></el-option>
                <el-option :value="4" label="L4"></el-option>
                <el-option :value="5" label="L5"></el-option>
                <el-option :value="6" label="L6"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否归档" v-if="!isSourceTree">
              <el-select v-model.trim="materialData.isArchive">
                <el-option :value="1" label="是"></el-option>
                <el-option :value="0" label="否"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排序" prop="sn">
              <el-input-number
                v-model.trim="materialData.sn"
                :min="0"
              ></el-input-number>
              <!-- <el-input type="number" v-model.trim="materialData.sn" :min="0"></el-input> -->
            </el-form-item>
            <el-form-item
              label="是否发布"
              v-if="tableStructureManageContral && !isSourceTree"
            >
              <el-select v-model.trim="materialData.published">
                <el-option :value="1" label="发布"></el-option>
                <el-option :value="0" label="不发布"></el-option>
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
            <el-form-item label="备注" v-if="!isSourceTree">
              <el-input v-model.trim="materialData.dataDesc"></el-input>
            </el-form-item>
          </div>
        </div>
        <div class="dictData" v-if="!isSourceTree">
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
                      >{{ item.dictLabel }}
                        <span v-show="index != SelectDictdataTypes.length - 1"
                        >,</span
                        >
                      </span>
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
                      >{{ item.dictLabel }}
                      </el-checkbox
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
          <div class="leftBox">
            <el-select
              v-model="linTableName"
              filterable
              placeholder="请选择"
              @change="getLinkTable"
            >
              <el-option
                v-for="(item, index) in linkOptions"
                :key="index"
                :label="item.TABLE_NAME"
                :value="item.TABLE_NAME"
              >
              </el-option>
            </el-select>
            <el-table
              ref="multipleTable"
              :data="tableDataLink"
              tooltip-effect="dark"
              style="width: 100%"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="DATABASE_NAME" label="数据库">
              </el-table-column>
              <el-table-column prop="SCHEMA_NAME" label="专题库">
              </el-table-column>
              <el-table-column prop="DICT_LABEL" label="表类型">
              </el-table-column>
            </el-table>
          </div>
          <div class="rightBox">
            <p v-for="(item, index) in multipleSelection" :key="index">
              {{ item.DATABASE_NAME }}-{{ item.SCHEMA_NAME }}-{{
                item.TABLE_NAME
              }}-{{ item.DICT_LABEL }}
            </p>
          </div>
        </div>
      </div>
      <!-- 服务编码 -->
      <div class="editDataUse" v-show="multipleSelectionTabs.length > 0">
        <h4>服务编码</h4>
      </div>
      <el-tabs v-model="activeTabsName" @tab-click="handleTabsClick">
        <el-tab-pane
          :label="item.DATABASE_NAME"
          :name="item.DATABASE_NAME"
          v-for="(item, index) in multipleSelectionTabs"
          :key="index"
        >
          <!-- 字段表格 -->
          <el-table
            :data="item.columnData"
            border
            stripe
            @selection-change="(res) => (selColumnData = res)"
            ref="selectionTable"
            @row-click="handleClickTableRow"
            class="columnTable"
          >
            <el-table-column
              :label="column.label"
              v-for="(column, field) of tableTem1"
              :key="field"
            >
              <template slot-scope="scope">
                <span>{{ scope.row[field] }}</span>
              </template>
            </el-table-column>
            <el-table-column label="服务名称" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.userEleCode"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="要素单位" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.unit"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="要素单位(中文名)" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.unitCn"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              width="120"
              :label="column.label"
              v-for="(column, field) of tableTem3"
              :key="field"
            >
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row[field]"></el-checkbox>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer" slot="footer" v-if="isFooterShow">
      <el-button type="primary" @click="makeSureSave('materialForm')"
      >确认
      </el-button
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
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  getBizUserByName,
  mmdneed,
  save,
  getDetailById,
  enable,
  getNewDataClassId,
  getCanShowDatabaseDefineList,
  findTablesByTableName,
  findAllETables,
  getServiceCode
} from '@/api/structureManagement/dataList/index'
import { getDictByType } from '@/api/structureManagement/dataList/StructureManageTable'
import { dataClassAll } from '@/api/structureManagement/dataList/StructureClassify'
import { datumTypeGetTree } from '@/api/structureManagement/dataList/StructureClassify'
import { dataApplyClassAll } from '@/api/structureManagement/dataList/StructureClassify'
// 公共元数据资料树
import StructurePublicTree from '@/views/structureManagement/dataList/StructurePublicTree'
// 存储元数据资料树
import StructureStorageTree from '@/views/structureManagement/dataList/StructureStorageTree'
// 关联表信息
import EditDataUseDialog from '@/views/structureManagement/dataList/EditDataUseDialog'

//接口地址
export default {
  name: 'structureMaterialSingle',
  components: {
    Treeselect,
    EditDataUseDialog,
    StructurePublicTree,
    StructureStorageTree
  },
  props: {
    isSourceTree: {
      type: Boolean
    },
    editNodeId: {
      type: String
    },
    editMaterial: {
      type: String
    },
    DRegistrationObj: {
      type: Object
    },
    editTableName: {
      type: String
    },
    isFooterShow: {
      type: Boolean
    }
  },
  data() {
    var nameValidate = (rule, value, callback) => {
      let msg = ''
      if (rule.field == 'dbEleCode' || rule.field.indexOf('dbEleCode') > -1) {
        msg = '字段编码'
      } else if (
        rule.field == 'celementCode' ||
        rule.field.indexOf('celementCode') > -1
      ) {
        msg = '字段编码'
      }
      if (value === '') {
        callback(new Error('请输入' + msg))
      } else if (!codeVer(value)) {
        callback(
          new Error(msg + '不允许输入小写字母和中文，且需以大写字母开头')
        )
      } else {
        callback()
      }
    }

    const tableNumValidate = (rule, value, callback) => {
      if (value === '' || value === null || value === undefined) {
        callback(new Error('请输入序号'))
      }
    }

    const accuracyValidate = (rule, value, callback) => {
      if (value === null || value === '') {
        callback()
      } else {
        if (value > 0) {
          callback()
        } else {
          callback(new Error('不是正整数'))
        }
      }
    }
    const lengthValidate = (rule, value, callback) => {
      if (value === null || value === '') {
        callback()
      } else {
        if (value > 0) {
          callback()
        } else {
          callback(new Error('不是正整数'))
        }
      }
    }
    return {
      selColumnData: [], //选中的表格值
      multipleSelectionTabs: [],
      multipleSelectionTabsArry: [],
      activeTabsName: '', //服务编码tab
      tableTem1: {}, //新增表格模板1
      tableTem3: {}, //新增表格模板3
      //关联表信息-start
      tableDataLink: [],
      multipleSelection: [],
      linTableName: '',
      linkOptions: [],
      //关联表信息-end
      DatabaseDefineList: [], //数据库列表
      ddataIdFlag: false, //公共元数据信息是否必填
      dictshowflag: false,
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1
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
          children: node.children
        }
      },
      tableStructureManageContral: false,
      labelWidth: '100px',

      isDisabledEdit: false,
      materialData: {
        // ddataId: "",
        ddataId: null,
        dataClassId: null,
        dataName: null,
        parentId: null,
        typeText: '资料',
        published: 0,
        isArchive: 1,
        useBaseInfo: 0,
        sn: 0,
        dataLogicListTable: [], //数据用途回显的树
        labelKeyFrom: [],
        dataClassLabelList: [],
        hasPowerList: [],
        dataDesc: null
      },
      publicTreeVisible: false, //公共元数据资料树弹出层
      storageTreeVisible: false, //存储元数据资料树弹出层
      rules: {
        // ddataId: [
        //   {
        //     required: true,
        //     message: "名称不能为空",
        //     trigger: "change",
        //   },
        // ],
        ddataId: [
          {
            required: true,
            message: '四级编码不能为空',
            trigger: 'change'
          }
        ],
        parentId: [
          {
            required: true,
            message: '父节点不能为空',
            trigger: 'change'
          }
        ],
        dataName: [
          {
            required: true,
            message: '资料名称不能为空',
            trigger: 'change'
          }
        ],
        dataClassId: [
          {
            required: true,
            message: '存储编码不能为空',
            trigger: 'change'
          }
        ],
        sn: [
          {
            required: true,
            message: '排序不能为空',
            trigger: 'change'
          }
        ]
      },
      dataClassIdWatch: ''
    }
  },
  async created() {
    this.tableTem1 = {
      dbEleCode: {
        label: '字段编码',
        type: 'text',
        rules: {
          required: true,
          message: '字段编码不能为空'
        }
      },
      eleName: {
        label: '中文简称',
        type: 'text',
        rules: {
          required: true,
          message: '中文简称不能为空'
        }
      }
      /*   dbEleCode: {
        label: "公共元数据字段",
        type: "input",
        width: 200,
        rules: {
          required: true,
          message: "公共元数据字段不能为空",
        },
      }, */
    }

    this.tableTem3 = {
      /*  isUpdate: {
        label: "是否可改",
        type: "radio",
        options: [
          { text: "是", value: true },
          { text: "否", value: false },
        ],
      }, */
      isShow: {
        label: '是否显示',
        type: 'radio',
        options: [
          { text: '是', value: 1 },
          { text: '否', value: 0 }
        ]
      }

      /*  isManager: {
        label: "是否管理字段",
        type: "radio",
        options: [
          { text: "是", value: true },
          { text: "否", value: false },
        ],
      }, */
    }
    await getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data
    })
    await mmdneed().then((res) => {
      this.ddataIdFlag = res.data
    })
    await enable().then((res) => {
      if (res.data == 'true') {
        this.tableStructureManageContral = true
      } else {
        this.tableStructureManageContral = false
      }
    })
    // 名称树
    await dataApplyClassAll().then((response) => {
      this.resetData(response.data)
      this.publicTreeOptions = response.data
    })
    // 父节点树
    await dataClassAll().then((response) => {
      const menu = { id: 0, name: '主类目', children: [] }
      this.resetData(response.data)
      menu.children = response.data
      this.storageTree.push(menu)
    })
    // 标签
    await getDictByType({ dictType: 'zt_label' }).then((response) => {
      this.dictdataTypes = response.data
    })

    //查询所有要素表
    await findAllETables().then((response) => {
      this.linkOptions = response.data
    })

    if (this.editTableName) {
      await this.getLinkTable(this.editTableName)
    }
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
    await this.initMaterialForm()
  },
  watch: {
    dataClassIdWatch: function() {
      this.$nextTick(function() {
        /*现在数据已经渲染完毕*/
        this.multipleSelectionTabsArry = []
        this.getServiceCodeMethods()
      })
    }
  },
  methods: {
    // tabclick
    handleTabsClick(tab, event) {
      console.log(tab, event)
    },
    // 选中就勾选
    handleClickTableRow(row, event, column) {
      row.isEdit = true
    },
    getLinkTable(val) {
      this.linTableName = val
      //关联表信息
      findTablesByTableName({ tableName: this.linTableName }).then(
        (response) => {
          this.tableDataLink = response.data
        }
      )
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
      this.multipleSelectionTabsArry = []
      this.multipleSelectionTabsArry = val
      this.multipleSelectionTabsArry.forEach((element) => {
        element.columnData = []
      })
      if (this.multipleSelection.length > 0) {
        this.activeTabsName = this.multipleSelection[0].DATABASE_NAME
        this.getServiceCodeMethods()
      }
    },
    // 获取字段
    async getServiceCodeMethods() {
      for (var i = 0; i < this.multipleSelectionTabsArry.length; i++) {
        debugger
        await getServiceCode({
          dataclassId: this.materialData.dataClassId,
          tableId: this.multipleSelectionTabsArry[i].ID
        }).then((res) => {
          if (res.code == 200) {
            this.multipleSelectionTabsArry[i].columnData = res.data
          }
        })
      }
      this.multipleSelectionTabs = this.multipleSelectionTabsArry
      console.log(this.multipleSelectionTabs)
    },
    setSelectDictdataTypes(val) {
      this.SelectDictdataTypes = []
      val.forEach((element) => {
        this.dictdataTypes.forEach((item) => {
          if (element == item.dictValue) {
            this.SelectDictdataTypes.push(item)
          }
        })
      })
    },
    resetData(dataArr) {
      for (var i in dataArr) {
        dataArr[i].className = dataArr[i].name
        dataArr[i].name = dataArr[i].name + '(' + dataArr[i].id + ')'
        this.resetData(dataArr[i].children)
      }
    },
    //初始化表单
    initMaterialForm() {
      if (this.isSourceTree) {
        this.materialData.typeText = '目录'
      }
      if (!this.isSourceTree) {
        this.materialData.typeText = '资料'
      }
      //资料树  初始化
      if (this.isSourceTree && this.editNodeId) {
        this.isDisabledEdit = true
        this.getMaterialForm(this.editNodeId)
      }
      //资料  初始化
      if (!this.isSourceTree && this.editMaterial) {
        this.isDisabledEdit = true
        this.getMaterialForm(this.editMaterial)
      }

      // 从数据注册审核来的资料
      if (this.DRegistrationObj) {
        this.materialData.applyId = this.DRegistrationObj.applyId
        // this.materialData.metaDataName = this.DRegistrationObj.TYPE_NAME;
        this.materialData.ddataId = this.DRegistrationObj.D_DATA_ID
        this.materialData.dataLogicListTable = this.DRegistrationObj.dataLogicList
        // getNewDataClassId({ parentId: this.materialData.ddataId }).then(
        //   (res) => {
        //     if (res.code == 200) {
        //       this.materialData.dataClassId = res.data;
        //       if (res.data == "") {
        //         let code = checkNode.id.split(".");
        //         if (code.length == 4) {
        //           let fCode = code[3].substring(0, 1);
        //           code[3] = code[3].replace(fCode, "M");
        //           this.materialData.dataClassId = code.join(".");
        //         } else {
        //           this.materialData.dataClassId = checkNode.id;
        //         }
        //       }
        //       this.dataClassIdWatch = this.materialData.dataClassId;
        //     }
        //   }
        // );
        console.log(this.materialData)
      }
    },
    //获取资料树，资料的数据
    async getMaterialForm(id) {
      console.log(id)
      await getDetailById({ id: id }).then((response) => {
        if (response.code == '200') {
          if (!response.data.dataClassLabelList) {
            response.data.labelKeyFrom = []
          } else {
            response.data.labelKeyFrom = []
            response.data.dataClassLabelList.forEach((element) => {
              response.data.labelKeyFrom.push(element.labelKey)
            })
            this.SelectDictdataTypes = []
            this.dictdataTypes.forEach((element) => {
              response.data.dataClassLabelList.forEach((item) => {
                if (item.labelKey == element.dictValue) {
                  let obj = {
                    dictLabel: element.dictLabel
                  }
                  this.SelectDictdataTypes.push(obj)
                }
              })
            })
          }

          if (!response.data.dataClassUserList) {
            response.data.dataClassUserList = []
          } else {
            response.data.hasPowerList = []
            response.data.dataClassUserList.forEach((element) => {
              response.data.hasPowerList.push(element.userName)
            })
          }
          this.materialData = response.data
          if (
            response.data.dataLogicList &&
            response.data.dataLogicList.length > 0
          ) {
            let logcList = response.data.dataLogicList
            logcList.forEach(async(item, index) => {
              this.tableDataLink.forEach((citem, cindex) => {
                if (item.tableId == citem.ID) {
                  this.$refs.multipleTable.toggleRowSelection(
                    this.tableDataLink[cindex]
                  )
                }
              })
            })
          }

          if (this.materialData.type == 1) {
            this.materialData.typeText = '目录'
          } else {
            this.materialData.typeText = '资料'
          }

          console.log(this.materialData.dataLogicList)
        } else {
          console.log('出错了，出错信息：' + response.msg)
        }
      })
    },
    // 显示公共元数据资料树
    showPublicTree() {
      this.publicTreeVisible = true
    },
    //从公共元数据获取数据
    getChildCheckNode(checkNode) {
      console.log(checkNode)
      if (!this.isDisabledEdit) {
        if (this.materialData.typeText == '目录') {
          this.materialData.dataClassId = checkNode.id
        } else if (this.materialData.typeText == '资料') {
          getNewDataClassId({ parentId: checkNode.id }).then((res) => {
            if (res.code == 200) {
              this.materialData.dataClassId = res.data
              if (res.data == '') {
                let code = checkNode.id.split('.')
                if (code.length == 4) {
                  let fCode = code[3].substring(0, 1)
                  code[3] = code[3].replace(fCode, 'M')
                  this.materialData.dataClassId = code.join('.')
                } else {
                  this.materialData.dataClassId = checkNode.id
                }
              }
            }
          })
        }
        this.dataClassIdWatch = this.materialData.dataClassId
        this.materialData.dataName = checkNode.className
      }

      this.materialData.ddataId = checkNode.id
      // this.materialData.metaDataName = checkNode.name;
      // this.publicTreeVisible = false;
    },
    //关闭公共元数据弹出层
    closePublicDialog() {
      this.publicTreeVisible = false
    },
    // 显示存储元数据资料树
    showStorageTree() {
      this.storageTreeVisible = true
    },
    //从存储元数据获取数据
    getStorageCheckNode(checkNode) {
      if (this.isSourceTree) {
        this.materialData.parentId = checkNode.id
      } else {
        if (checkNode.id != 0 && checkNode.id.split('.').length == 4) {
          this.materialData.dataName = checkNode.name
          this.materialData.dataClassId = checkNode.id
        } else {
          this.$message({
            type: 'error',
            message: '只能选择四级资料'
          })
          this.$nextTick(function() {
            this.materialData.dataName = null
          })
        }
      }
    },
    //关闭存储元数据弹出层
    closeStorageTreeDialog() {
      this.storageTreeVisible = false
    },

    // 保存数据
    makeSureSave(resForm) {
      /*  if (this.ddataIdFlag && !this.materialData.ddataId) {
        this.$message({
          type: "error",
          message: "请选择名称",
        });
      } */
      this.$refs['materialForm'].validate((valid) => {
        if (valid) {
          // 1为目录   2为资料
          if (this.materialData.typeText == '目录') {
            this.materialData.type = 1
          } else if (this.materialData.typeText == '资料') {
            this.materialData.type = 2
          }
          let handleMessage = ''
          let refreshWhich = ''
          //资料分类树新增或编辑
          if (this.isSourceTree) {
            refreshWhich = 'tree'
            // 编辑
            if (this.editNodeId !== '') {
              handleMessage = '编辑资料树节点成功'
            }
            //新增
            else {
              handleMessage = '新增资料树节点成功'
            }
          }
          //资料的新增或编辑
          else {
            // 编辑
            if (this.editMaterial !== '') {
              handleMessage = '编辑资料成功'
            }
            //新增
            else {
              handleMessage = '新增资料成功'
            }
            this.materialData.dataClassLabelList = []
            this.dictdataTypes.forEach((element) => {
              this.materialData.labelKeyFrom.forEach((item) => {
                if (element.dictValue == item) {
                  let obj = {
                    labelKey: item
                  }
                  this.materialData.dataClassLabelList.push(obj)
                }
              })
            })

            this.materialData.dataClassUserList = []
            // this.materialData.hasPowerList.forEach((element) => {
            //   let obj = {
            //     userName: element,
            //   };
            //   this.materialData.dataClassUserList.push(obj);
            // });
            console.log(this.materialData)
            this.materialData.dataLogicList = []
            this.multipleSelection.forEach((element) => {
              let obj = {
                databaseId: element.DATABASE_ID,
                subTableId: element.SUB_TABLE_ID,
                tableId: element.ID,
                dataClassId: this.materialData.dataClassId
              }
              this.materialData.dataLogicList.push(obj)
            })
            this.multipleSelectionTabs.forEach((element) => {
              element.dataclassId = this.materialData.dataClassId
            })
            this.materialData.serviceCodeList = this.multipleSelectionTabs
          }
          console.log(this.materialData)
          //数据注册审核的数据用途只能选一条
          if (!resForm) {
            let dataLogicListTable = this.materialData.dataLogicListTable
            if (dataLogicListTable.length != 1) {
              this.$message({
                type: 'error',
                message: '只能选择一条数据用途'
              })
            } else if (
              !dataLogicListTable[0].storageType ||
              dataLogicListTable[0].storageType == null ||
              dataLogicListTable[0].storageType == ''
            ) {
              this.$message({
                type: 'error',
                message: '请选择存储类型'
              })
            } else {
              this.saveData(handleMessage, refreshWhich)
            }
          } else {
            this.saveData(handleMessage, refreshWhich)
          }
        }
      })
    },
    saveData(handleMessage, refreshWhich) {
      save(this.materialData).then((response) => {
        if (response.success === true) {
          this.$message({
            type: 'success',
            message: handleMessage
          })
          if (refreshWhich == 'tree') {
            //资料树节点 新增  编辑  isSourceTree:true   isSourceTree:false
            this.$emit('addOrEditSuccess', 'handleTree')
          } else {
            if (this.DRegistrationObj) {
              this.$emit('addOrEditSuccess', response.data)
            } else {
              this.$emit('addOrEditSuccess', 'handleTable')
            }
          }
        } else {
          this.$message({
            type: 'error',
            message: response.msg
          })
        }
      })
    },

    cancleSave() {
      this.$emit('closeMaterialDialog')
    }
  }
}
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

    .linkInfo {
      display: flex;
      justify-content: space-between;

      .leftBox,
      .rightBox {
        width: 49.5%;
      }

      .el-table {
        margin-top: 10px;
      }

      .rightBox {
        border: 1px solid #c0c4cc;
        padding: 10px;
      }
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

  .dialog-footer {
    margin-top: 20px;
  }
}
</style>
