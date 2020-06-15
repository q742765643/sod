<template>
  <section class="stepDreg">
    <div class="loadTable">
      <el-steps :active="stepNum" finish-status="success">
        <el-step title="存储元数据注册"></el-step>
        <el-step title="表结构管理"></el-step>
        <el-step title="数据同步"></el-step>
        <el-step title="数据迁移"></el-step>
        <el-step title="数据备份"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <el-card class="box-card" shadow="never" v-if="stepNum==0">
        <!-- 新增/编辑资料 -->
        <StructureMaterialSingle
          :isSourceTree="isSourceTree"
          :DRegistrationObj="handleMsgObj"
          @addOrEditSuccess="addOrEditSuccess"
          @cancelHandle="handleClose"
          ref="materialRef"
        />
      </el-card>
      <el-card class="box-card" shadow="never" v-if="stepNum==1">
        <!-- 表结构管理 -->
        <span>资料名称：{{returnMaterialInfo.className}}</span>
        <el-divider></el-divider>
        <span>四级编码：{{returnMaterialInfo.ddataId}}</span>
        <el-table
          :data="tableData"
          border
          highlight-current-row
          @current-change="handleCurrentChange"
          ref="singleTable"
        >
          <el-table-column label="数据用途" prop="LOGIC_NAME" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="存储类型" prop="DICT_LABEL" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="数据库" prop="DATABASE_NAME_F" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" width="320px">
            <template slot-scope="scope">
              <el-button size="small" @click="showStructureManage(scope.row)">
                <i class="btnRound blueRound" v-if="scope.row.TABLECOUNT > scope.row.roundCount"></i>
                <i class="btnRound orangRound" v-else></i>
                表结构管理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card class="box-card" shadow="never" v-if="stepNum==2">
        <!-- 同步 -->
        <handleSync
          :handleObj="handleMsgObj"
          ref="syncRef"
          :formPage="formPage"
          @getStepFlag="getStepFlag"
        />
      </el-card>
      <el-card class="box-card" shadow="never" v-if="stepNum==3">
        <!-- 迁移 -->
        <handleMove
          :handleObj="handleMsgObj"
          ref="moveRef"
          @getStepFlag="getStepFlag"
          :formPage="formPage"
        ></handleMove>
      </el-card>
      <el-card class="box-card" shadow="never" v-if="stepNum==4">
        <!-- 备份 -->
        <handleBackUp
          :handleObj="handleMsgObj"
          ref="backupRef"
          @getStepFlag="getStepFlag"
          :formPage="formPage"
        />
      </el-card>
      <el-card class="box-card" shadow="never" v-if="stepNum==5">
        <span
          style="font-size: 26px;display:block;text-align:center;padding: 20px 0;color: #13ce66;"
        >
          <i class="el-icon-circle-check"></i> 审核通过
        </span>
      </el-card>
    </div>
    <div class="dialog-footer">
      <el-button type="primary" v-if="stepNum!=5" @click="nextStep">下一步</el-button>
      <el-button type="primary" v-if="stepNum==5" @click="trueStep">审核通过</el-button>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="closeStructureManage"
      top="0"
      class="scrollDialog"
      :append-to-body="true"
    >
      <StructureManageTable
        ref="scrollDiv"
        v-if="structureManageVisible"
        v-bind:parentRowData="rowData"
      />
    </el-dialog>
  </section>
</template>

<script>
//新增、编辑资料--弹出层
import StructureMaterialSingle from "@/views/structureManagement/tableStructureManage/StructureMaterialSingle";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
// 数据同步
import handleSync from "@/views/schedule/dataSync/handleSync";
// 迁移配置信息
import handleMove from "@/views/schedule/move/handleMove";
// 结构化数据备份配置弹窗
import handleBackUp from "@/views/schedule/backup/handleBackUp";
import { getListBYIn } from "@/api/structureManagement/tableStructureManage/index";
import {
  getDotById,
  addApply
} from "@/api/authorityAudit/DRegistration/review/index";
import { gcl } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { updateStatus } from "@/api/authorityAudit/DRegistration/reviewdataRegister";
export default {
  components: {
    StructureMaterialSingle,
    StructureManageTable,
    handleSync,
    handleMove,
    handleBackUp
  },
  props: {
    handleObj: {
      type: Object
    },
    registerForm: {
      type: Object
    }
  },
  data() {
    return {
      stepNum: 0,
      formPage: "数据注册审核",
      isSourceTree: false, //资料
      rowData: {},
      handleMsgObj: {}, //资料/同步/备份/迁移/清除
      structureManageVisible: false, //表结构管理
      structureManageTitle: "",
      tableData: [],
      tableDetail: [],
      currentRow: [],
      returnMaterialInfo: {} //StructureMaterialSingle新增资料成功后返回的数据
    };
  },
  created() {
    this.handleMsgObj = this.handleObj;
    console.log(this.registerForm);
  },

  methods: {
    nextStep() {
      // 储存元数据注册
      if (this.stepNum == 0) {
        this.$refs.materialRef.makeSureSave();
        return;
      }
      // 表结构管理
      if (this.stepNum == 1) {
        // 如果有字段，可以继续网下走
        if (
          this.tableDetail.length > 0 &&
          this.tableDetail[0].columns &&
          this.tableDetail[0].columns.length > 0
        ) {
          this.stepNum = 2;
          this.handleMsgObj = {};
          this.handleMsgObj.pageName = "数据注册审核";
          this.handleMsgObj.databaseId = this.tableData[0].DATABASE_ID; //目标库
          this.handleMsgObj.targetTable = this.tableDetail[0].id; //目标表
          this.handleMsgObj.dataClassId = this.tableData[0].DATA_CLASS_ID; //资料
        } else {
          this.$message({
            message: "表结构信息不完整",
            type: "error"
          });
          return;
        }
        return;
      }
      // 数据同步
      if (this.stepNum == 2) {
        this.$refs.syncRef.trueDialog("ruleForm");
      }
      // 数据迁移
      if (this.stepNum == 3) {
        this.$refs.moveRef.trueDialog("ruleForm");
      }
      // 数据备份
      if (this.stepNum == 4) {
        this.$refs.backupRef.trueDialog("ruleForm");
      }
    },
    getStepFlag(stepFlag) {
      if (stepFlag) {
        this.stepNum = this.stepNum + 1;
        if (this.stepNum === 5) {
          this.trueStep();
        }
        return;
      }
    },
    // 资料新增
    addOrEditSuccess(returnInfo) {
      console.log(returnInfo);
      this.returnMaterialInfo = returnInfo;
      // 表格
      getListBYIn({ stringList: returnInfo.dataClassId }).then(response => {
        this.stepNum = 1;
        let tableData = response.data;
        if (tableData.length > 0) {
          tableData.forEach((item, index) => {
            item.roundCount =
              item.STORAGE_TYPE == "K_E_table"
                ? 1
                : item.STORAGE_TYPE == "MK_table"
                ? 1
                : 0;
          });
        }
        this.tableData = tableData;
        // 传applyid 逗号隔开的id
        let ids = [];
        this.returnMaterialInfo.dataLogicList.forEach(element => {
          ids.push(element.id);
        });
        let obj = {
          applyId: this.handleObj.applyId,
          classLogicIds: ids.join(",")
        };
        addApply(obj).then(res => {});
      });
    },
    //显示表结构管理
    showStructureManage(row) {
      this.rowData = row;
      console.log(row);
      this.structureManageTitle = row.CLASS_NAME;
      this.structureManageVisible = true;
    },
    // 选中行
    handleCurrentChange(currentRow) {
      if (currentRow == null) {
        return;
      }
      this.currentRow = [];
      this.tableData.forEach((item, index) => {
        let obj = {};
        if (item.DATA_CLASS_ID != null && currentRow.DATA_CLASS_ID != null) {
          if (item.DATA_CLASS_ID == currentRow.DATA_CLASS_ID) {
            this.currentRow.push(item);
          }
        }
      });
    },
    handleClose() {},
    trueStep() {
      const checkobj = {
        id: this.registerForm.ID,
        ddataId: this.registerForm.D_DATA_ID,
        examineStatus: 2,
        remark: this.registerForm.DATA_PROP
      };
      updateStatus(checkobj).then(response => {
        this.$message({
          showClose: true,
          message: "操作成功",
          type: "success"
        });
        this.$emit("closeStep");
      });
    },
    closeStructureManage() {
      this.structureManageVisible = false;
      getListBYIn({ stringList: this.returnMaterialInfo.dataClassId }).then(
        response => {
          let tableData = response.data;
          if (tableData.length > 0) {
            tableData.forEach((item, index) => {
              item.roundCount =
                item.STORAGE_TYPE == "K_E_table"
                  ? 1
                  : item.STORAGE_TYPE == "MK_table"
                  ? 1
                  : 0;
            });
          }
          this.tableData = tableData;
          this.getTableDatail();
        }
      );
    },
    // 查询表格详情
    getTableDatail() {
      gcl({ classLogic: this.tableData[0].LOGIC_ID }).then(response => {
        this.tableDetail = response.data;
      });
    }
  }
};
</script>

<style  lang="scss">
.stepDreg {
  .is-never-shadow {
    margin: 20px auto;
  }
  .el-table {
    margin-top: 20px;
  }
}
</style>