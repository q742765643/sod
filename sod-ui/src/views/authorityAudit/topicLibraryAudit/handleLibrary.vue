<template>
  <section class="handleLiberyDialog">
    <!--<el-tabs type="border-card" v-model.trim="activeName">-->
      <!--<el-tab-pane label="基本信息" name="first"></el-tab-pane>-->
      <!---->
    <!--</el-tabs>-->
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="150px"
      label-position="right">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>基本信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8" >
            <el-form-item label="应用系统" prop="userId">
              <el-input
                v-model.trim="msgFormDialog.userId"
                :disabled="true"
                size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户ID" prop="userId">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.userId">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="责任人" prop="userName">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.userName"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="left">
          <el-col :span="8">
            <el-form-item label="部门" prop="department">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.department"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系方式" prop="userPhone">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.userPhone"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" >
            <el-form-item label="用途" prop="uses">
              <el-input
                v-model.trim="msgFormDialog.uses"
                :disabled="true"
                size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-top: 20px">
        <div slot="header" class="clearfix">
          <span>审核信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="审核状态" prop="examineStatus">
              <el-radio-group
                v-model.trim="msgFormDialog.examineStatus"
                @change="statusChange">
                <el-radio label="2" >允许</el-radio>
                <el-radio label="3" >拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input
                :disabled="true"
                size="small"
                v-model.trim="msgFormDialog.examiner"
                placeholder="审核人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请材料" prop="examineMaterial">
              <label slot="label">
                <i style="color:red;">*</i>&nbsp;申请材料
              </label>
              <el-button @click="handleExport" size="small" type="success">下载查看申请材料</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="24">
            <el-form-item label="意见" prop="failureReason">
              <el-input
                size="small"
                v-model.trim="msgFormDialog.failureReason"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="box-card" style="margin-top: 20px" v-if="!isReason">
        <div slot="header" class="clearfix">
          <span>资源信息</span>
        </div>
        <el-row :gutter="12">
          <el-col :span="8">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>结构化数据库(STDB)</span>
                <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                <span style="float: right; padding: 3px 0; color: #67C23A" v-if="dbsLists.STDB != 0">已开通</span>
                <span style="float: right; padding: 3px 0; color: #F56C6C" v-else>未开通</span>
              </div>
              <div v-for="" :key="" class="text item">
                {{'存储空间:   '+dbsLists.oldSTDB+'GB  >>  '+dbsLists.STDB+'GB  变更  '+dbsLists.newSTDB}}
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>文件索引库(FIDB)</span>
                <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                <span style="float: right; padding: 3px 0; color: #67C23A" v-if="dbsLists.FIDB != 0">已开通</span>
                <span style="float: right; padding: 3px 0; color: #F56C6C" v-else>未开通</span>
              </div>
              <div v-for="" :key="" class="text item">
                {{'存储空间:   '+dbsLists.oldFIDB+'GB  >>  '+dbsLists.FIDB+'GB  变更  '+dbsLists.newFIDB}}
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>历史分析库(HADB)</span>
                <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                <span style="float: right; padding: 3px 0; color: #67C23A" v-if="dbsLists.HADB != 0">已开通</span>
                <span style="float: right; padding: 3px 0; color: #F56C6C" v-else>未开通</span>
              </div>
              <div v-for="" :key="" class="text item">
                {{'存储空间:   '+dbsLists.oldHADB+'GB  >>  '+dbsLists.HADB+'GB  变更  '+dbsLists.newHADB}}
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="12" style="margin-top: 25px">
          <el-col :span="8">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>文件存储(NAS)</span>
                <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                <span style="float: right; padding: 3px 0; color: #67C23A" v-if="dbsLists.NAS != 0">已开通</span>
                <span style="float: right; padding: 3px 0; color: #F56C6C" v-else>未开通</span>
              </div>
              <div v-for="" :key="" class="text item">
                {{'存储空间:   '+dbsLists.oldNAS+'GB  >>  '+dbsLists.NAS+'GB  变更  '+dbsLists.newNAS}}
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>对象存储</span>
                <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                <span style="float: right; padding: 3px 0; color: #67C23A" v-if="dbsLists.objectSave != 0">已开通</span>
                <span style="float: right; padding: 3px 0; color: #F56C6C" v-else>未开通</span>
              </div>
              <div v-for="" :key="" class="text item">
                {{'存储空间:   '+dbsLists.oldobjectSave+'GB  >>  '+dbsLists.objectSave+'GB  变更 '+dbsLists.newobjectSave}}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
    <div slot="footer" style="margin-top: 20px" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <!--<el-button @click="cancelDialog('ruleForm')">取 消</el-button>-->
    </div>
  </section>
</template>

<script>
import {
  getById,
  getAuthorityBySdbId,
  getSpecialDataList,
  empowerDataBatch,
  empowerDataOne,
  empowerDatabaseSpecial,
  saveBase,
  exportTable,
  updateExamineStatus,
} from "@/api/authorityAudit/topicLibraryAudit";
import { findByUserId } from "@/api/authorityAudit/userRoleAudit";
export default {
  name: "handleLiberyDialog",

  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      // activeName: "first",
      isReason:false,
      flagBase: false,
      flagPower: false,
      msgFormDialog: {},
      searchLibraryObj: {
        key: "typeName",
        valueText: "",
      },
      searchBaseLibraryObj: {
        key: "typeName",
        valueText: "",
      },
      dbsLists:{
        STDB:0,
        FIDB:0,
        HADB:0,
        NAS:0,
        objectSave:0,
        newSTDB:0,
        newFIDB:0,
        newHADB:0,
        newNAS:0,
        newobjectSave:0,
        oldSTDB:0,
        oldFIDB:0,
        oldHADB:0,
        oldNAS:0,
        oldobjectSave:0,
      },
      baseTableLibraryData: [],
      tableLibraryData: [],
      databaseList: [],
      multipleSelection: [],
      paramList: [], //临时变量，拒绝时附带信息
      refuseReasonDialog: false,
      refusereason: "",
      single: false,
      sdbImg: "",
      dialogImageUrlData: [
        require("@/assets/image/icon/h1.png"),
        require("@/assets/image/icon/h2.png"),
        require("@/assets/image/icon/h3.png"),
        require("@/assets/image/icon/h4.png"),
        require("@/assets/image/icon/h5.png"),
        require("@/assets/image/icon/h6.png"),
        require("@/assets/image/icon/h7.png"),
        require("@/assets/image/icon/h8.png"),
        require("@/assets/image/icon/h9.png"),
        require("@/assets/image/icon/h10.png"),
        require("@/assets/image/icon/h11.png"),
        require("@/assets/image/icon/h12.png"),
        require("@/assets/image/icon/h13.png"),
        require("@/assets/image/icon/h14.png"),
        require("@/assets/image/icon/h15.png"),
      ],
      rules: {
        sdbName: [
          { required: true, message: "请输入专题库名称", trigger: "blur" },
        ],
        uses: [{ required: true, message: "请输入用途", trigger: "blur" }],
      },
    };
  },
  created() {
    console.log(this.handleObj);
    if (this.handleObj.id) {
      this.initDetail();
      if (this.handleObj.examineStatus == "1") {
        this.flagBase = false;
        this.flagPower = false;
      } else {
        this.flagBase = true;
        this.flagPower = true;
      }
    }

    this.searchLibraryFun();
    this.loadReadList();

  },
  methods: {
    applyAuthFormatter(row, column) {
      if (row.applyAuthority == "1") {
        return "只读";
      } else if (row.applyAuthority == "2") {
        return "读写";
      }
    },
    statusChange(val) {
      if (val == "2") {
        this.isReason = false;
        this.msgFormDialog.failureReason = "同意";
      } else {
        this.isReason = true;
        this.msgFormDialog.failureReason = "拒绝";
      }
    },
    // 下载
    handleExport() {
      if (!this.msgFormDialog.applyMaterial) {
        this.$message({ type: "error", message: "文件不存在" });
        return;
      }
      exportTable({ filePath: this.msgFormDialog.applyMaterial }).then(
        (res) => {
          if (res.code) {
            this.$message({ type: "warning", message: res.msg });
          } else {
            this.downloadfileCommon(res);
          }
        }
      );
    },
    // 获取专题库基本信息|专题库授权
    initDetail() {
      // 基本信息
      getById({ id: this.handleObj.id }).then((res) => {
        if (res.code == 200) {
          debugger
          this.msgFormDialog = res.data;
          this.msgFormDialog.examiner = this.$store.getters.name;
          this.msgFormDialog.examineStatus = "2";
          this.msgFormDialog.failureReason = "同意";
          var list = this.msgFormDialog.dbsList
          for(var i=0;i<list.length;i++){

            if(list[i].databaseId == "STDB"){
              this.dbsLists.oldSTDB = list[i].sizeOfSpace;
              if(list[i].applyOfSpace != null){
                this.dbsLists.newSTDB = list[i].applyOfSpace;
                this.dbsLists.STDB = this.dbsLists.oldSTDB + this.dbsLists.newSTDB;
              }else{
                this.dbsLists.STDB = this.dbsLists.oldSTDB
              }
            }
            if(list[i].databaseId == "FIDB"){
              this.dbsLists.oldFIDB = list[i].sizeOfSpace;
              if(list[i].applyOfSpace != null){
                this.dbsLists.newFIDB = list[i].applyOfSpace;
                this.dbsLists.FIDB = this.dbsLists.oldFIDB + this.dbsLists.newFIDB;
              }else{
                this.dbsLists.FIDB = this.dbsLists.oldFIDB
              }
            }
            if(list[i].databaseId == "HADB"){
              this.dbsLists.oldHADB = list[i].sizeOfSpace;
              if(list[i].applyOfSpace != null){
                this.dbsLists.newHADB = list[i].applyOfSpace;
                this.dbsLists.HADB = this.dbsLists.oldHADB + this.dbsLists.newHADB;
              }else{
                this.dbsLists.HADB = this.dbsLists.oldHADB
              }
            }
            if(list[i].databaseId == "NAS"){
              this.dbsLists.oldNAS = list[i].sizeOfSpace;
              if(list[i].applyOfSpace != null){
                this.dbsLists.newNAS = list[i].applyOfSpace;
                this.dbsLists.NAS = this.dbsLists.oldNAS + this.dbsLists.newNAS;
              }else{
                this.dbsLists.NAS = this.dbsLists.oldNAS
              }
            }
            if(list[i].databaseId == "objectSave"){
              this.dbsLists.oldobjectSave = list[i].sizeOfSpace;
              if(list[i].applyOfSpace != null){
                this.dbsLists.newobjectSave = list[i].applyOfSpace;
                this.dbsLists.objectSave = this.dbsLists.oldobjectSave + this.dbsLists.newobjectSave;
              }else{
                this.dbsLists.objectSave = this.dbsLists.oldobjectSave
              }
            }
          }
        }
      });
      // 数据库授权
      getAuthorityBySdbId({ sdbId: this.handleObj.id }).then((res) => {
        if (res.code == 200) {
          if (res.data && res.data.length > 0) {
            this.databaseList = res.data;
            this.databaseList.forEach((element, index) => {
              this.$set(element, "checkList", []);
              if (element.createTable == "2") {
                element.checkList.push("createTable");
              }
              if (element.deleteTable == "2") {
                element.checkList.push("deleteTable");
              }
              if (element.tableDataAccess == "2") {
                element.checkList.push("tableDataAccess");
              }
            });
          }
        }
      });
    },
    trueDialog() {
      saveBase(this.msgFormDialog).then((res) => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "保存成功",
          });
          this.flagBase = true;
          this.activeName = "second";
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
        }
      });
    },
    // 拒绝
    cancleDialog() {
      let obj = {
        examineStatus: "3",
        sdbId: this.handleObj.id,
      };
      updateExamineStatus(obj).then((res) => {
        if (res.code == 200) {
          this.$emit("closedialog");
          this.$message({
            type: "success",
            message: "拒绝成功",
          });
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
        }
      });
    },
    // 获取专题库资料列表
    searchLibraryFun() {
      let obj = {
        dataType: 1,
        sdbId: this.handleObj.id,
      };
      if (this.searchLibraryObj.valueText) {
        obj[this.searchLibraryObj.key] = this.searchLibraryObj.valueText;
      }
      console.log(obj);
      getSpecialDataList(obj).then((res) => {
        this.tableLibraryData = res.data;
      });
    },
    //加载读权限审核列表
    loadReadList() {
      let obj = {
        dataType: 2,
        sdbId: this.handleObj.id,
      };
      if (this.searchBaseLibraryObj.valueText) {
        obj[
          this.searchBaseLibraryObj.key
        ] = this.searchBaseLibraryObj.valueText;
      }
      console.log(obj);
      getSpecialDataList(obj).then((res) => {
        this.baseTableLibraryData = res.data;
      });
    },
    //数据库授权
    addPhysicsDefine() {
      let obj = {};
      let listArry = this.databaseList;
      /* listArry.forEach(listele => {
        let checkArry = listele.checkList;
        checkArry.forEach(cele => {
          if (listele[cele]) {
            listele[cele] = 2;
          }
        });
      }); */
      let keyJson = ["createTable", "deleteTable", "tableDataAccess"];
      listArry.forEach((element) => {
        let checkArry = element.checkList;
        keyJson.forEach((kitem) => {
          element[kitem] = 1;
        });
        checkArry.forEach((item) => {
          element[item] = 2;
        });
      });
      // console.log(listArry);
      obj.databaseSpecialAuthorityList = listArry;
      obj.databaseId = this.msgFormDialog.databaseId;
      obj.sdbId = this.msgFormDialog.id;
      obj.sdbName = this.msgFormDialog.sdbName;
      obj.simpleName = this.msgFormDialog.databaseSchema;
      obj.userId = this.msgFormDialog.userId;
      console.log(obj);
      empowerDatabaseSpecial(obj).then((res) => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.flagPower = true;
          this.activeName = "third";
          this.databaseList = [];
          this.initDetail();
          this.searchLibraryFun();
          this.loadReadList();
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //单个库的授权或拒绝
    updatePower(row, status) {
      row.examineStatus = status;
      if (status == "2") {
        this.$prompt("请输入拒绝原因", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        })
          .then(({ value }) => {
            row.failureReason = value;
            this.editPowerOne(row);
          })
          .catch(() => {});
      } else {
        this.editPowerOne(row);
      }
    },
    editPowerOne(row) {
      console.log(row);
      empowerDataOne(row).then((res) => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.loadReadList();
        } else {
          this.$message({ type: "error", message: "授权失败" });
        }
      });
    },
    //批量授权或拒绝
    batchUpdatePower(status) {
      //判断是否勾选需要授权的资料
      if (this.multipleSelection.length > 0) {
        if (status == "2") {
          this.$prompt("请输入拒绝原因", "温馨提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
          })
            .then(({ value }) => {
              this.multipleSelection.forEach((element) => {
                element.failureReason = value;
                element.examineStatus = 2;
              });
              this.editPowerBath();
            })
            .catch(() => {});
        } else {
          this.multipleSelection.forEach((element) => {
            element.examineStatus = 1;
          });
          this.editPowerBath();
        }
      } else {
        this.$message({ type: "warning", message: "请勾选需要操作的资料" });
        return;
      }
    },
    editPowerBath() {
      console.log(this.multipleSelection);
      empowerDataBatch(this.multipleSelection).then((res) => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.loadReadList();
        } else {
          this.$message({ type: "error", message: "授权失败" });
        }
      });
    },


  },
};
</script>

<style lang="scss">
.handleLiberyDialog {
  a {
    color: #409eff;
  }
  .el-link--inner {
    font-size: 12px;
  }
  .loadTable {
    border: none;
  }
  .center {
    margin-bottom: 20px;
    text-align: center;
  }
}
</style>
