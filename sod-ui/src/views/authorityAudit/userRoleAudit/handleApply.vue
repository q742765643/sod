<template>
  <section class="handleApply">
    <el-steps :active="stepNum">
      <el-step title="数据库访问账户" icon="el-icon-menu"></el-step>
      <el-step title="专题库" icon="el-icon-s-ticket" v-show="handleMsgObj.dbCreate=='1'"></el-step>
      <el-step title="资料访问权限审核" icon="el-icon-s-finance" v-show="handleMsgObj.sodData=='1'">></el-step>
      <el-step title="完成" icon="el-icon-circle-check"></el-step>
    </el-steps>
    <el-card class="box-card" shadow="never" v-if="stepNum==0">
      <handleAccount :handleObj="handleObj" ref="AccountRef" @handleDialogClose="handleClose" />
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==1">
      <handleLibrary :handleObj="handleObj" ref="LibraryRef" />
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==2">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-thumb" size="mini" @click="handlePower">授权</el-button>
          <el-button type="danger" icon="el-icon-close" size="mini" @click="handleRefused">拒绝</el-button>
        </el-col>
      </el-row>
      <el-table
        v-loading="loading"
        :data="tableData"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="TYPE_NAME" label="资料分类" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="CLASS_NAME" label="资料名称" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="TABLE_NAME" label="表名称" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="DATABASE_NAME" label="数据库" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="SPECIAL_DATABASE_NAME" label="专题名" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="IS_ACCESS" label="权限" width="80px">
          <template slot-scope="scope">
            <span v-if="scope.row.IS_ACCESS=='0'">受限</span>
            <span v-else>公开</span>
          </template>
        </el-table-column>
        <el-table-column prop="APPLY_AUTHORITY" label="申请状态" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.APPLY_AUTHORITY=='1'">读申请</span>
            <span v-else>读写申请</span>
          </template>
        </el-table-column>
        <el-table-column prop="AUTHORIZE" label="审核状态" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.AUTHORIZE=='2'">
              <i class="el-icon-circle-close" style="color:#F56C6C"></i>拒绝
            </span>
            <span v-else-if="scope.row.AUTHORIZE=='1'">
              <i class="el-icon-circle-check" style="color:#67C23A"></i>通过
            </span>
            <span v-else>
              <i class="el-icon-s-finance" style="color:#E6A23C"></i>待审核
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="CAUSE" label="拒绝原因" width="120px">
          <template slot-scope="scope">
            <el-popover
              placement="top"
              effect="dark"
              trigger="hover"
              width="200"
              popper-class="darkPopover"
              v-if="scope.row.CAUSE"
            >
              <p>{{ scope.row.CAUSE}}</p>
              <div slot="reference" class="name-wrapper">
                <el-button type="primary" size="mini" plain class="tagpointer">查看</el-button>
              </div>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==3">
      <span style="font-size: 26px;display:block;text-align:center;">
        <i class="el-icon-circle-check"></i> 完成
      </span>
    </el-card>
    <div class="dialog-footer" style="margin-top:20px;">
      <el-button type="danger" v-if="stepNum==0" @click="finishStep">拒绝</el-button>
      <el-button type="primary" v-if="!dbIds" @click="finishStep">通过</el-button>
      <el-button type="primary" v-if="stepNum!=3&&dbIds" @click="nextStep" :disabled="nextFlag">下一步</el-button>
      <el-button type="primary" v-if="stepNum==3" @click="finishStep">完成</el-button>
    </div>
  </section>
</template>

<script>
// 数据库访问账户
import handleAccount from "@/views/authorityAudit/DBaccount/handleAccount";
// / 专题库
import handleLibrary from "@/views/authorityAudit/topicLibraryAudit/handleLibrary";
// 资料访问权限审核
import handleMaterial from "@/views/authorityAudit/materialPower/handleMaterial";
import { findByUserId } from "@/api/authorityAudit/userRoleAudit";
import { getRecordByApplyId } from "@/api/authorityAudit/materialPower/index";
import {
  getRecordByByUserId,
  editBase,
  databaseUserExiget
} from "@/api/authorityAudit/userRoleAudit";
import {
  updateRecordCheck,
  updateRecordCheckCancel
} from "@/api/authorityAudit/materialPower/index";
export default {
  name: "handleApply",
  props: {
    handleMsgObj: {
      type: Object
    }
  },
  components: { handleAccount, handleLibrary, handleMaterial },
  data() {
    return {
      forId: this.handleMsgObj.userName,
      loading: false,
      tableData: [],
      multipleSelection: [],
      stepNum: 0,
      msgFormDialog: {},
      handleObj: { pageName: "业务用户审核" },
      nextFlag: false,
      dbIds: ""
    };
  },
  async created() {
    this.handleObj = Object.assign(this.handleMsgObj, this.handleObj);
    this.dbIds = this.handleObj.dbIds;
    if (this.handleObj.dbIds) {
      databaseUserExiget({
        databaseUpId: this.handleObj.userName,
        applyDatabaseId: this.handleObj.dbIds
      }).then(response => {
        this.nextFlag = response.data;
      });
    } else {
      this.nextFlag = true;
    }
  },

  methods: {
    async initDatail() {
      debugger;
      await findByUserId({ userId: this.handleMsgObj.userName }).then(res => {
        if (res.data && res.data.length > 0) {
          this.handleObj = res.data[0];
        } else {
          this.handleObj = {};
        }

        this.handleObj.pageName = "业务用户审核";
        this.stepNum = 1;
      });
    },
    handleClose(flag) {
      if (flag === false) {
        return;
      }
      if (this.handleMsgObj.dbCreate == "1") {
        // 到专题库
        this.initDatail();
        return;
      } else if (this.handleMsgObj.sodData == "1") {
        this.getList();
        return;
      } else {
        this.stepNum = 3;
        this.finishStep();
        return;
      }
    },
    nextStep() {
      debugger;
      // 数据库访问账户 新增
      if (this.stepNum == 0) {
        this.$refs.AccountRef.trueAdd();
        return;
      }
      if (this.stepNum == 1) {
        // 到资料访问权限审核
        this.getList();
        return;
      }
      if (this.stepNum == 2) {
        // 到完成
        this.stepNum = 3;
        return;
      }
    },
    getList() {
      let obj = {
        dataType: 2,
        sdbId: this.forId
      };
      this.loading = true;
      getRecordByByUserId({ bizUserId: this.forId }).then(res => {
        if (res.code == 200) {
          this.loading = false;
          this.tableData = res.data;
          this.stepNum = 2;
        } else {
          this.msgError(res.msg);
        }
      });
    },
    handlePower() {
      if (this.multipleSelection.length == 0) {
        this.msgError("请选择一条数据");
        return;
      } else {
        this.powerMethods();
      }
    },
    handleRefused() {
      if (this.multipleSelection.length == 0) {
        this.msgError("请选择一条数据");
        return;
      } else {
        this.$prompt("请输入拒绝原因", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputValidator: value => {
            if (value.trim().length < 1 || value.trim().length > 500) {
              return "拒绝原因长度限制1-500个字符";
            }
          }
        })
          .then(({ value }) => {
            this.powerMethods(value);
          })
          .catch(() => {});
      }
    },
    powerMethods(value) {
      this.loading = true;
      let obj = {};
      obj.userId = this.forId;
      obj.dataAuthorityRecordList = [];
      this.multipleSelection.forEach(element => {
        let cobj = {};
        cobj.id = element.ID;
        cobj.applyId = element.APPLY_ID;
        cobj.databaseId = element.DATABASE_ID;
        cobj.applyAuthority = element.APPLY_AUTHORITY;
        if (value) {
          cobj.authorize = 2;
        } else {
          cobj.authorize = 1;
        }

        cobj.dataClassId = element.DATA_CLASS_ID;
        cobj.cause = value;
        cobj.tableName = element.TABLE_NAME;
        obj.dataAuthorityRecordList.push(cobj);
      });
      console.log(obj);
      if (value) {
        updateRecordCheckCancel(JSON.parse(JSON.stringify(obj))).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg
            });
            this.getList();
          } else {
            this.loading = false;
            this.$alert(res.msg, "提示", {
              dangerouslyUseHTMLString: true
            });
            this.getList();
          }
        });
      } else {
        updateRecordCheck(JSON.parse(JSON.stringify(obj))).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg
            });
            this.getList();
          } else {
            this.loading = false;
            this.$alert(res.msg, "提示", {
              dangerouslyUseHTMLString: true
            });
            this.getList();
          }
        });
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    finishStep() {
      let status;
      if (this.stepNum == 0 && this.dbIds) {
        status = 2;
        this.$prompt("请输入拒绝原因", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputValidator: value => {
            if (value.trim().length < 1 || value.trim().length > 500) {
              return "拒绝原因长度限制1-500个字符";
            }
          }
        })
          .then(({ value }) => {
            editBase({
              bizUserid: this.forId,
              checked: status,
              reason: value
            }).then(res => {
              if (res.code == 200) {
                this.msgSuccess("操作成功");
                this.$emit("closeStep");
              } else {
                this.msgError(res.msg);
              }
            });
          })
          .catch(() => {});
      } else {
        status = 1;
        editBase({ bizUserid: this.forId, checked: status }).then(res => {
          if (res.code == 200) {
            this.msgSuccess("操作成功");
            this.$emit("closeStep");
          } else {
            this.msgError(res.msg);
          }
        });
      }
    }
  }
};
</script>

