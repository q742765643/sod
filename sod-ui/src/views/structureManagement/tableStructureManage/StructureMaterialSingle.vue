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
            <el-form-item label="名称" prop="metaDataName">
              <el-input
                placeholder="名称"
                :readonly="true"
                class="styleCursor"
                v-model="materialData.metaDataName"
                @click.native="showPublicTree"
              ></el-input>
            </el-form-item>
            <el-form-item label="四级编码" prop="ddataId">
              <el-input placeholder="请选择公共元数据" :readonly="true" v-model="materialData.ddataId"></el-input>
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
              <el-input placeholder="资料名称" v-model="materialData.className"></el-input>
            </el-form-item>
            <el-form-item label="存储编码" prop="dataClassId">
              <el-input
                :disabled="isDisabledEdit"
                placeholder="存储编码"
                v-model="materialData.dataClassId"
              ></el-input>
            </el-form-item>
            <el-form-item label="父节点" prop="parentId">
              <el-input
                placeholder="请选择存储元数据父节点"
                :readonly="true"
                v-model="materialData.parentId"
                class="styleCursor"
                @click.native="showStorageTree"
              ></el-input>
            </el-form-item>
            <el-form-item label="类型" v-if="isSourceTree">
              <el-input v-model="materialData.typeText" :readonly="true"></el-input>
            </el-form-item>
            <el-form-item label="访问控制">
              <el-select v-model="materialData.isAccess">
                <el-option :value="1" label="公开"></el-option>
                <el-option :value="2" label="限制"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排序" prop="serialNo">
              <el-input type="number" v-model="materialData.serialNo"></el-input>
            </el-form-item>
            <el-form-item label="是否发布" v-if="tableStructureManageContral">
              <el-select v-model="materialData.ifStopUse">
                <el-option :value="true" label="启用"></el-option>
                <el-option :value="false" label="停用"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="基础信息配置" v-if="!isSourceTree&&tableStructureManageContral">
              <el-select v-model="materialData.useBaseInfo">
                <el-option :value="1" label="启用"></el-option>
                <el-option :value="0" label="停用"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <!-- 编辑数据用途 -->
      <div class="editDataUse" v-if="!isSourceTree">
        <h4>编辑数据用途</h4>
        <div class="editUseDiv" @click="showEditDataUse">点击编辑</div>
        <el-table border v-if="editUseShow" :data="materialData.dataclasslogic" style="width: 98%">
          <el-table-column prop="logic_name" label="用途描述"></el-table-column>
          <el-table-column prop="storage_name" label="存储类型"></el-table-column>
          <el-table-column prop="physicsName" label="数据库名称"></el-table-column>
          <el-table-column prop="special_database_name" label="专题名"></el-table-column>
        </el-table>
      </div>
    </div>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="makeSureSave('materialForm')">确认</el-button>
      <el-button @click="cancleSave">取消</el-button>
    </div>

    <!-- 公共元数据资料树 -->
    <el-dialog
      title="公共元数据资料树"
      :visible.sync="publicTreeVisible"
      :close-on-click-modal="false"
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
      title="存储元数据资料树"
      :visible.sync="storageTreeVisible"
      :close-on-click-modal="false"
      :append-to-body="true"
    >
      <StructureStorageTree
        v-if="storageTreeVisible"
        @storageCheckedNode="getStorageCheckNode"
        @closeStorageTreeDialog="closeStorageTreeDialog"
      />
    </el-dialog>

    <!-- 编辑数据用途弹出层 -->
    <el-dialog
      title="数据用途选择"
      :visible.sync="editDataUseVisible"
      append-to-body
      width="70%"
      top="5vh"
    >
      <EditDataUseDialog
        v-if="editDataUseVisible"
        :handleArry="handleArry"
        @trueEditDataUseDialog="trueEditDataUseDialog"
        @closeEditUseDialog="closeEditUseDialog"
      />
    </el-dialog>
  </section>
</template>

<script>
import {
  dataClassAll,
  databaseClass,
  logicClass
} from "@/api/structureManagement/tableStructureManage/StructureClassify";

import {
  save,
  getDetailById,
  enable,
  getNewDataClassId
} from "@/api/structureManagement/tableStructureManage/index";
// 公共元数据资料树
import StructurePublicTree from "@/views/structureManagement/tableStructureManage/StructurePublicTree";
// 存储元数据资料树
import StructureStorageTree from "@/views/structureManagement/tableStructureManage/StructureStorageTree";
// 编辑数据用途
import EditDataUseDialog from "@/views/structureManagement/tableStructureManage/EditDataUseDialog";
//接口地址
export default {
  name: "structureMaterialSingle",
  components: { EditDataUseDialog, StructurePublicTree, StructureStorageTree },
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
    editMaterialArry: {
      type: Array
    }
  },
  data() {
    return {
      tableStructureManageContral: false,
      handleArry: [],
      labelWidth: "100px",
      editUseShow: false, //数据用途树默认不显示
      editDataUseVisible: false, //编辑数据用途弹出层控制
      isDisabledEdit: false,
      materialData: {
        ddataId: "",
        metaDataName: "",
        dataClassId: "",
        className: "",
        parentId: "",
        typeText: "资料",
        ifStopUse: true,
        isAccess: 1,
        useBaseInfo: 0,
        serialNo: 0,
        dataclasslogic: [] //数据用途回显的树
      },
      dataclasslogicOld: [],
      publicTreeVisible: false, //公共元数据资料树弹出层
      storageTreeVisible: false, //存储元数据资料树弹出层
      rules: {
        metaDataName: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "change"
          }
        ],
        ddataId: [
          {
            required: true,
            message: "四级编码不能为空",
            trigger: "change"
          }
        ],
        parentId: [
          {
            required: true,
            message: "父节点不能为空",
            trigger: "change"
          }
        ],
        className: [
          {
            required: true,
            message: "资料名称不能为空",
            trigger: "change"
          }
        ],
        dataClassId: [
          {
            required: true,
            message: "存储编码不能为空",
            trigger: "change"
          }
        ],
        serialNo: [
          {
            required: true,
            message: "排序不能为空",
            trigger: "change"
          }
        ]
      }
    };
  },
  async created() {
    await enable().then(res => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
    this.initMaterialForm();
  },
  methods: {
    //初始化表单
    initMaterialForm() {
      if (this.isSourceTree) {
        this.materialData.typeText = "目录";
      }
      if (!this.isSourceTree) {
        this.materialData.typeText = "资料";
      }
      //资料树  初始化
      if (this.isSourceTree && this.editNodeId !== "") {
        this.isDisabledEdit = true;
        this.getMaterialForm(this.editNodeId);
      }
      //资料  初始化
      if (!this.isSourceTree && this.editMaterial !== "") {
        this.isDisabledEdit = true;
        this.getMaterialForm(this.editMaterial);
      }
    },
    //获取资料树，资料的数据
    getMaterialForm(id) {
      console.log(id);
      getDetailById({ id: id }).then(response => {
        if (response.code == "200") {
          this.materialData = response.data;
          if (this.materialData.type == 1) {
            this.materialData.typeText = "目录";
          } else {
            this.materialData.typeText = "资料";
          }
          if (
            response.data.dataclasslogic &&
            response.data.dataclasslogic.length > 0
          ) {
            this.editUseShow = true;
            this.materialData.dataclasslogic = this.editMaterialArry;
          }
          console.log(this.editMaterialArry);
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
        this.materialData.dataClassId = checkNode.id;
        this.materialData.className = checkNode.name;
      }
      this.materialData.ddataId = checkNode.id;
      this.materialData.metaDataName = checkNode.name;
      this.publicTreeVisible = false;
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
      this.materialData.parentId = checkNode.id;
      if (this.tableStructureManageContral == false) {
        getNewDataClassId({ parentId: checkNode.id }).then(res => {
          if (res.code == 200) {
            this.materialData.dataClassId = res.data;
          }
        });
      }
      this.storageTreeVisible = false;
    },
    //关闭存储元数据弹出层
    closeStorageTreeDialog() {
      this.storageTreeVisible = false;
    },
    // 显示编辑数据用途弹出层
    showEditDataUse() {
      if (this.materialData.dataclasslogic.length > 0) {
        this.handleArry = this.materialData.dataclasslogic;
        console.log(this.handleArry);
      }
      this.editDataUseVisible = true;
    },
    //确认编辑数据用途弹出层
    trueEditDataUseDialog(choseTableArry) {
      this.editUseShow = true;
      this.materialData.dataclasslogic = choseTableArry;
      console.log(choseTableArry);
      this.editDataUseVisible = false;
    },
    // 关闭编辑数据用途弹出层
    closeEditUseDialog() {
      this.editDataUseVisible = false;
    },
    // 保存数据
    makeSureSave(resForm) {
      this.$refs["materialForm"].validate(valid => {
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
            if (this.materialData.dataclasslogic.length == 0) {
              this.$message({
                type: "error",
                message: "请选择数据用途"
              });
              return false;
            }
            let dataclasslogic = this.materialData.dataclasslogic;
            this.dataclasslogicOld = dataclasslogic;
            let newDataclasslogic = [];
            for (var i = 0; i < dataclasslogic.length; i++) {
              let obj = {};
              obj.class_logic_id =
                this.materialData.dataClassId +
                "_" +
                dataclasslogic[i].logic_id; //存储编码和逻辑库ID拼接窜
              obj.dataClassId = this.materialData.dataClassId; //存储编码
              obj.logic_id = dataclasslogic[i].logic_id; //逻辑库ID
              obj.storage_type = dataclasslogic[i].storage_type; //资料类型
              obj.dlphysics = []; //专题库列表
              let dlObj = {};
              dlObj.database_id = dataclasslogic[i].special; //物理库ID
              dlObj.dl_id =
                this.materialData.dataClassId +
                "_" +
                dataclasslogic[i].logic_id; ///存储编码和逻辑库ID拼接窜
              obj.dlphysics.push(dlObj);
              newDataclasslogic.push(obj);
            }
            this.materialData.dataclasslogic = newDataclasslogic;
          }
          console.log(this.materialData);
          this.saveData(handleMessage, refreshWhich);
        }
      });
    },
    saveData(handleMessage, refreshWhich) {
      save(this.materialData).then(response => {
        if (response.success === true) {
          this.$message({
            type: "success",
            message: handleMessage
          });
          if (refreshWhich == "tree") {
            //资料树节点 新增  编辑  isSourceTree:true   isSourceTree:false
            this.$emit("addOrEditSuccess", "handleTree");
          } else {
            this.$emit("addOrEditSuccess", "handleTable");
          }
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
          this.materialData.dataclasslogic = this.dataclasslogicOld;
        }
      });
    },

    cancleSave() {
      this.$emit("closeMaterialDialog");
    }
  }
};
</script>

<style lang="scss">
.structureMaterialSingle {
  .publicDataTitle,
  .storageDataTitle {
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
  .storageInfo {
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
  .materialCon {
    max-height: calc(100vh - 170px);
    overflow-y: auto;
    margin-bottom: 10px;
  }
}
</style>
