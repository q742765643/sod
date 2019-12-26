<template>
  <!-- 新增或者编辑资料 -->
  <section class="structureMaterialSingleDR">
    <div class="materialCon selScrollBar">
      <el-form
        :model="materialData"
        ref="materialForm"
        :rules="rules"
        :inline="true"
        :label-width="labelWidth"
      >
        <!-- 公共元数据信息 -->
        <div class="publicData">
          <div class="publicDataTitle">
            <i class="el-icon-price-tag"></i>公共元数据信息
          </div>
          <div class="publicInfo">
            <el-form-item label="名称" prop="meta_data_name">
              <el-input
                size="small"
                placeholder="名称"
                :readonly="true"
                class="styleCursor"
                v-model="materialData.meta_data_name"
                disabled
              ></el-input>
            </el-form-item>
            <el-form-item label="四级编码" prop="d_data_id">
              <el-input
                size="small"
                disabled
                placeholder="请选择公共元数据"
                :readonly="true"
                v-model="materialData.d_data_id"
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
            <el-form-item label="资料名称" prop="class_name">
              <el-input size="small" placeholder="资料名称" v-model="materialData.class_name"></el-input>
            </el-form-item>
            <el-form-item label="存储编码" prop="data_class_id">
              <el-input
                :disabled="isDisabledEdit"
                size="small"
                placeholder="存储编码"
                v-model="materialData.data_class_id"
              ></el-input>
            </el-form-item>
            <el-form-item label="父节点" prop="parent_class_id">
              <el-input
                size="small"
                placeholder="请选择存储元数据父节点"
                :readonly="true"
                v-model="materialData.parent_class_id"
                class="styleCursor"
                @click.native="showStorageTree"
              ></el-input>
            </el-form-item>

            <el-form-item label="访问控制">
              <el-select size="small" v-model="materialData.access_control">
                <el-option :value="1" label="公开"></el-option>
                <el-option :value="2" label="限制"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排序" prop="serial_no">
              <el-input size="small" type="number" v-model="materialData.serial_no"></el-input>
            </el-form-item>
            <el-form-item label="是否发布">
              <el-select size="small" v-model="materialData.if_stop_use">
                <el-option :value="1" label="启用"></el-option>
                <el-option :value="2" label="停用"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="基础信息配置">
              <el-select size="small" v-model="materialData.use_base_info">
                <el-option :value="1" label="启用"></el-option>
                <el-option :value="0" label="停用"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <!-- 编辑数据用途 -->
      <div class="editDataUse">
        <h4>编辑数据用途</h4>
        <div class="editUseDiv" @click="showEditDataUse">点击编辑</div>
        <el-table border :data="materialData.dataclasslogic" style="width: 98%">
          <el-table-column prop="logic_name" label="用途描述"></el-table-column>
          <el-table-column prop="storage_name" label="存储类型"></el-table-column>
          <el-table-column prop="physicsName" label="数据库名称"></el-table-column>
          <el-table-column prop="special_database_name" label="专题名"></el-table-column>
        </el-table>
      </div>
    </div>
    <div class="dialog-footer" v-if="isShow">
      <el-button type="primary" @click="makeSureSave">确认</el-button>
      <!-- <el-button @click="cancleSave">取消</el-button> -->
    </div>

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
// 存储元数据资料树
// import StructureStorageTree from "@/views/structureManagement/tableStructureManage/StructureStorageTree";
// // 编辑数据用途
// import EditDataUseDialog from "@/views/structureManagement/tableStructureManage/EditDataUseDialog";
// //接口地址
// import { interfaceObj } from "@/urlConfig";
export default {
  name: "structureMaterialSingleDR",
  components: { EditDataUseDialog, StructureStorageTree },
  props: {
    editMaterialObj: {
      type: Object
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
      isShow: true,
      detailObj: { data_class_id: "" },
      handleArry: [],
      labelWidth: "100px",
      editUseShow: false, //数据用途树默认不显示
      editDataUseVisible: false, //编辑数据用途弹出层控制
      isDisabledEdit: false,
      materialData: {
        d_data_id: "",
        meta_data_name: "",
        data_class_id: "",
        class_name: "",
        parent_class_id: "",
        meta_data_stor_type: "资料",
        if_stop_use: 1,
        access_control: 1,
        use_base_info: 0,
        if_stop_use: 1,
        serial_no: 0,
        dataclasslogic: [] //数据用途回显的树
      },
      storageTreeVisible: false, //存储元数据资料树弹出层
      rules: {
        meta_data_name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "change"
          }
        ],
        d_data_id: [
          {
            required: true,
            message: "四级编码不能为空",
            trigger: "change"
          }
        ],
        parent_class_id: [
          {
            required: true,
            message: "父节点不能为空",
            trigger: "change"
          }
        ],
        class_name: [
          {
            required: true,
            message: "资料名称不能为空",
            trigger: "change"
          }
        ],
        data_class_id: [
          {
            required: true,
            message: "存储编码不能为空",
            trigger: "change"
          }
        ],
        serial_no: [
          {
            required: true,
            message: "排序不能为空",
            trigger: "change"
          }
        ]
      }
    };
  },
  mounted() {
    // 数据用途
    // this.initDetail();
  },
  methods: {
    initDetail() {
      this.isDisabledEdit = true;
      // debugger;
      this.detailObj = this.editMaterialObj;
      //this.detailObj.data_class_id = "A.0001.0001.D002"; //测试
      // console.log(this.detailObj);
      if (!this.detailObj.data_class_id) {
        this.materialData.meta_data_name = this.detailObj.c_datumtype;
        this.materialData.d_data_id = this.detailObj.c_datum_code;
        this.materialData.class_name = this.detailObj.c_datumtype;
        this.materialData.data_class_id = this.detailObj.c_datum_code;
        // 此时只有用途描述和数据库名称，需要选择存储类型
        let obj = {};
        obj.logic_id = this.detailObj.logic_id;
        obj.physicsName = this.detailObj.database_name;
        obj.special = this.detailObj.pid;
        obj.special_database_name = this.detailObj.special_database_name;
        obj.logic_name = "";
        obj.storage_name = "";
        obj.storage_type = "";
        this.materialData.dataclasslogic.push(obj);
        //console.log(obj);
      } else {
        this.axios
          .get(interfaceObj.TableStructure_getDataById, {
            params: { data_class_id: this.detailObj.data_class_id }
          })
          .then(res => {
            this.materialData = res.data.data;
            if (res.data.data.dataclasslogic.length > 0) {
              this.editUseShow = true;
              this.materialData.dataclasslogic = this.editMaterialArry;
            }
            //console.log(this.editMaterialArry);
          })
          .catch(error => {
            console.log("出错了，出错信息：" + error);
          });
      }
    },

    // 显示存储元数据资料树
    showStorageTree() {
      this.storageTreeVisible = true;
    },
    //从存储元数据获取数据
    getStorageCheckNode(checkNode) {
      this.materialData.parent_class_id = checkNode.id;
      this.storageTreeVisible = false;
    },
    //关闭存储元数据弹出层
    closeStorageTreeDialog() {
      this.storageTreeVisible = false;
    },
    // 显示编辑数据用途弹出层
    showEditDataUse() {
      if (
        this.materialData.dataclasslogic &&
        this.materialData.dataclasslogic.length > 0
      ) {
        this.handleArry = this.materialData.dataclasslogic;
        //console.log(this.handleArry);
      }
      this.editDataUseVisible = true;
    },
    //确认编辑数据用途弹出层
    trueEditDataUseDialog(choseTableArry) {
      this.materialData.dataclasslogic = choseTableArry;
      //console.log(choseTableArry);
      this.editDataUseVisible = false;
    },
    // 关闭编辑数据用途弹出层
    closeEditUseDialog() {
      this.editDataUseVisible = false;
    },
    // 保存数据
    makeSureSave() {
      //console.log(1);
      //debugger;
      //提交的接口地址
      let requestInterface;
      //成功后的提示语
      let successMessage = "";
      //资料的新增或编辑
      // 编辑
      if (this.editMaterial) {
        requestInterface = interfaceObj.TableStructure_updateData;
        successMessage = "修改成功";
      }
      //新增
      else {
        requestInterface = interfaceObj.TableStructure_addData;
        successMessage = "新增成功";
      }
      if (this.materialData.dataclasslogic.length == 0) {
        this.$message({
          type: "error",
          message: "请选择数据用途"
        });
        return false;
      } else if (this.materialData.dataclasslogic.length > 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据用途"
        });
        return false;
      } //else if (this.materialData.dataclasslogic[0].storage_type == "") {
      //   this.$message({
      //     type: "error",
      //     message: "请选择存储类型"
      //   });
      //   return false;
      // }
      let dataclasslogic = this.materialData.dataclasslogic;
      this.materialData.dataclasslogicOld = this.materialData.dataclasslogic;
      let newDataclasslogic = [];
      for (var i = 0; i < dataclasslogic.length; i++) {
        let obj = {};
        obj.class_logic_id =
          this.materialData.data_class_id + "_" + dataclasslogic[i].logic_id; //存储编码和逻辑库ID拼接窜
        obj.data_class_id = this.materialData.data_class_id; //存储编码
        obj.logic_id = dataclasslogic[i].logic_id; //逻辑库ID
        obj.storage_type = dataclasslogic[i].storage_type; //资料类型
        obj.dlphysics = []; //专题库列表
        let dlObj = {};
        dlObj.database_id = dataclasslogic[i].special; //物理库ID
        dlObj.dl_id =
          this.materialData.data_class_id + "_" + dataclasslogic[i].logic_id; ///存储编码和逻辑库ID拼接窜
        obj.dlphysics.push(dlObj);
        newDataclasslogic.push(obj);
      }
      this.materialData.dataclasslogic = newDataclasslogic;
      this.saveData(requestInterface, successMessage);
    },

    saveData(requestInterface, successMessage) {
      this.$refs["materialForm"].validate(valid => {
        if (valid) {
          this.axios
            .post(requestInterface, this.materialData)
            .then(res => {
              if (res.data.returnCode == "0") {
                this.$message({
                  type: "success",
                  message: successMessage
                });
                if (!this.editMaterial) {
                  let obj = {};
                  obj.applyId = this.detailObj.apply_id;
                  obj.dDataId = this.materialData.d_data_id;
                  obj.dataClassId = this.materialData.data_class_id;
                  this.editNewDataApplyMethods(obj);
                }
                this.$emit("addOrEditSuccess", this.materialData);
                // 新增成功后将信息提交给表格页面，表格回显
              } else {
                this.$message({
                  type: "error",
                  message: res.data.returnMessage
                });
              }
            })
            .catch(error => {
              console.log("出错了，出错信息：" + error);
            });
        }
      });
    },
    editNewDataApplyMethods(obj) {
      this.axios
        .post(interfaceObj.editNewDataApply, obj)
        .then(res => {
          if (res.data.returnCode != "0") {
            console.log(res.data.returnMessage);
          }
        })
        .catch(error => {
          console.log("出错了，出错信息：" + error);
        });
    },
    cancleSave() {
      this.$emit("closeMaterialDialog");
    }
  }
};
</script>

<style lang="scss">
.structureMaterialSingleDR {
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
      // background: url("~@/views/structureManagement/tableStructureManage/images/editDataUse.png")
      //   no-repeat left center;
      text-indent: 28px;
    }
    h4:before {
      content: "";
      width: 90px;
      border-bottom: 3px solid rgba(5, 138, 229, 1);
      position: absolute;
      top: 39px;
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
    overflow-y: auto;
    margin-bottom: 10px;
  }
}
</style>
