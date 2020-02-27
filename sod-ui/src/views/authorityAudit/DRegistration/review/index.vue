<template>
  <div class="review_Step_Register">
    <section class="loadTable">
      <el-steps :active="stepNum" finish-status="success">
        <el-step title="储存元数据注册"></el-step>
        <el-step title="表结构管理"></el-step>
        <el-step title="数据迁移"></el-step>
        <el-step title="数据备份"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <el-card class="box-card" v-if="stepNum==0">
        <StructureMaterialSingle
          v-if="materialSingleVisible"
          :editMaterialObj="editMaterialObj"
          @addOrEditSuccess="addOrEditSuccess"
          ref="myHandleMaterial"
        />
      </el-card>
      <el-card class="box-card" v-if="stepNum==1">
        <StructureManageTable v-if="structureManageVisible" :rowMaterialData="rowMaterialData" />
      </el-card>
      <el-card class="box-card" v-if="stepNum==2">
        <HandleMsg
          v-if="handleMsgVisible"
          v-bind:msgFormDialogs="editMaterialObj"
          @handleClose="handleClose"
          ref="myMsgVisibleHandleChild"
        />
      </el-card>
      <el-card class="box-card" v-if="stepNum==3">
        <Backup
          v-if="handleBackupDialog"
          :handleObj="handleBackupObj"
          @trueBackup="trueBackup"
          @cancelHandle="cancelHandle"
          ref="myBackupHandleChild"
        />
      </el-card>
      <el-card class="box-card" v-if="stepNum==4">
        <span style="font-size: 26px;">完成</span>
      </el-card>
    </section>
    <div class="dialog-footer">
      <!-- <el-button type="primary" v-if="stepNum!=0" @click="backStep">上一步</el-button> -->
      <el-button type="primary" v-if="stepNum!=4" @click="nextStep">下一步</el-button>
      <el-button type="primary" v-if="stepNum==4" @click="$emit('closeStep')">完成</el-button>
    </div>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
import StructureMaterialSingle from "@/views/authorityAudit/DRegistration/review/StructureMaterialSingleDR";
import StructureManageTable from "@/views/authorityAudit/DRegistration/review/tableManagement";
import HandleMsg from "@/views/authorityAudit/DRegistration/review/handleMsgs";
import Backup from "@/views/authorityAudit/DRegistration/review/vhandlebackup";

export default {
  name: "revieStepRegister",
  components: {
    StructureMaterialSingle,
    StructureManageTable,
    HandleMsg,
    Backup
  },
  props: {
    materialObj: {
      type: Object
    }
  },
  data() {
    return {
      stepNum: 0,
      rowMaterialData: {},
      materialSingleVisible: true,
      structureManageVisible: false,
      isSourceTree: false,
      handleMsgVisible: false,
      handleBackupDialog: false,
      editNodeId: "",
      editMaterialObj: {},
      handleBackupObj: {}
    };
  },
  created() {
    this.editMaterialObj = this.materialObj;
    console.log(this.editMaterialObj);
  },
  methods: {
    nextStep() {
      this.showCardStep();
      //this.gotonext();
    },
    gotonext() {
      this.stepNum = this.stepNum + 1;
    },
    backStep() {
      if (this.stepNum != 0) {
        this.stepNum = this.stepNum - 1;
      }
    },
    showCardStep() {
      if (this.stepNum == 0) {
        this.$refs["myHandleMaterial"].makeSureSave();
      } else if (this.stepNum == 1) {
        this.handleMsgVisible = true;
        this.editMaterialObj.initServerDetail = 1;
        this.structureManageVisible = false;
        this.materialSingleVisible = false;
        this.gotonext();
      } else if (this.stepNum == 2) {
        this.$refs["myMsgVisibleHandleChild"].trueDialog("ruleForm");
      } else if (this.stepNum == 3) {
        this.$refs["myBackupHandleChild"].onSubmit("backupForm");
      } else {
        this.materialSingleVisible = false;
        this.structureManageVisible = false;
        this.handleMsgVisible = false;
        this.handleBackupDialog = false;
      }
    },
    /**数据迁移关闭 */
    handleClose() {
      debugger;
      this.handleMsgVisible = false;
      this.gotonext();
      this.handleBackUp(this.materialObj);
    },
    /**
     *
     * 存储结构
     */
    //新增、编辑后刷新数据
    addOrEditSuccess(materialData) {
      //debugger;
      this.materialSingleVisible = false;
      this.structureManageVisible = true;
      this.rowMaterialData = materialData;
      //console.log(this.rowMaterialData);
      this.gotonext();
    },
    //关闭新增或编辑资料弹出层
    closeMaterialDialog() {
      this.materialSingleVisible = false;
    },
    /**
     *
     * 备份
     */
    handleBackUp(row) {
      let arry = [];

      if (row.data_class_id) {
        arry.push(row.data_class_id);
      }
      this.handleBackupObj.databaseId = row.database_id;
      this.handleBackupObj.dataclass_value = arry;
      this.handleBackupObj.showqueryBtn = 2;
      this.handleBackupObj.pageName = "存储结构概览备份";
      this.handleBackupDialog = true;
      // openBackDialog 新曾
    },
    trueBackup(msgFormDialog) {
      delete msgFormDialog.pageName;
      msgFormDialog.dataServiceName = JSON.stringify(
        msgFormDialog.dataclass_value
      );
      msgFormDialog.fromFlag = "NEWDATA";
      this.axios
        .post(interfaceObj.databaseBackup_add, msgFormDialog)
        .then(res => {
          if (res.data.returnCode == "0") {
            this.msgSuccess("添加成功");
            this.handleBackupDialog = false;
            this.gotonext();
          } else {
            this.$message.error("添加出错。。。");
          }
        });
    },
    cancelHandle() {
      this.handleBackupDialog = false;
    }
  }
};
</script>

<style lang="scss">
</style>
