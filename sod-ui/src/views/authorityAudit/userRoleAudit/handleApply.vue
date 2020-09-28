<template>
  <section class="handleApply">
    <el-steps :active="stepNum">
      <el-step title="数据库访问账户" icon="el-icon-menu"></el-step>
      <el-step title="专题库" icon="el-icon-s-ticket" v-show="handleMsgObj.dbCreate=='1'"></el-step>
      <el-step title="资料访问权限审核" icon="el-icon-s-finance" v-show="handleMsgObj.sodData=='1'">></el-step>
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

    <div class="dialog-footer" style="margin-top:20px;">
      <el-button type="danger" @click="finishStep(2)">拒绝</el-button>
      <el-button
        type="primary"
        v-if="stepNum==1||stepNum==2 || (handleMsgObj.dbCreate!=1 &&handleMsgObj.sodData !=1)"
        @click="finishStep(1)"
      >通过</el-button>
      <el-button
        type="primary"
        v-if="(stepNum==0 ||stepNum==1)&&(handleMsgObj.dbCreate==1 || handleMsgObj.sodData ==1)"
        @click="nextStep"
      >下一步</el-button>
    </div>
  </section>
</template>

<script>
import { Loading } from "element-ui";
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
  databaseUserExiget,
} from "@/api/authorityAudit/userRoleAudit";
import {
  updateRecordCheck,
  updateRecordCheckCancel,
} from "@/api/authorityAudit/materialPower/index";
export default {
  name: "handleApply",
  props: {
    handleMsgObj: {
      type: Object,
    },
  },
  components: { handleAccount, handleLibrary, handleMaterial },
  watch: {
    handleObj: function () {
      this.$nextTick(function () {
        /*现在数据已经渲染完毕*/
        if (
          this.handleObj.dbCreate != 1 &&
          this.handleObj.sodData != 1 &&
          this.stepNum == 0
        ) {
          document.getElementsByClassName("el-step__line")[0].style.display =
            "none";
        }
      });
    },
  },
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
      dbIds: "",
    };
  },
  async created() {
    this.handleObj = Object.assign(this.handleMsgObj, this.handleObj);
    this.dbIds = this.handleObj.dbIds;
    console.log(this.dbIds);
    /*  if (this.handleObj.dbIds) {
      await databaseUserExiget({
        databaseUpId: this.handleObj.userName,
        applyDatabaseId: this.handleObj.dbIds,
      }).then((response) => {
        this.nextFlag = response.data;
      });
    } else {
      this.nextFlag = true;
    } */
  },

  methods: {
    async initDatail() {
      debugger;
      await findByUserId({ userId: this.handleMsgObj.userName }).then((res) => {
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
        sdbId: this.forId,
      };
      this.loading = true;
      getRecordByByUserId({ bizUserId: this.forId }).then((res) => {
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
        this.$prompt("请输入拒绝原因", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputValidator: (value) => {
            if (value.trim().length < 1 || value.trim().length > 500) {
              return "拒绝原因长度限制1-500个字符";
            }
          },
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
      this.multipleSelection.forEach((element) => {
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
        updateRecordCheckCancel(JSON.parse(JSON.stringify(obj))).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg,
            });
            this.getList();
          } else {
            this.loading = false;
            this.$alert(res.msg, "温馨提示", {
              dangerouslyUseHTMLString: true,
            });
            this.getList();
          }
        });
      } else {
        updateRecordCheck(JSON.parse(JSON.stringify(obj))).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg,
            });
            this.getList();
          } else {
            this.loading = false;
            this.$alert(res.msg, "温馨提示", {
              dangerouslyUseHTMLString: true,
            });
            this.getList();
          }
        });
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async finishStep(handleType) {
      let status;
      if (this.stepNum == 0 && handleType == 2) {
        status = 2;
        this.$prompt("请输入拒绝原因", "温馨提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputValidator: (value) => {
            if (value.trim().length < 1 || value.trim().length > 500) {
              return "拒绝原因长度限制1-500个字符";
            }
          },
        })
          .then(({ value }) => {
            editBase({
              bizUserid: this.forId,
              checked: status,
              reason: value,
            }).then((res) => {
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
        var loading = Loading.service({
          lock: true,
          text: "正在审核中...",
          background: "rgba(0, 0, 0, 0.7)",
        });
        if (this.handleMsgObj.dbCreate != 1 && this.handleMsgObj.sodData != 1) {
          await this.$refs.AccountRef.trueAdd();
        }
        await editBase({ bizUserid: this.forId, checked: status }).then(
          (res) => {
            if (res.code == 200) {
              this.msgSuccess("操作成功");
              this.$emit("closeStep");
              loading.close();
            } else {
              this.msgError(res.msg);
            }
          }
        );
      }
    },
  },
};
</script>

