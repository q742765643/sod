<template>
  <section class="handleApply">
    <el-steps :active="stepNum" finish-status="success">
      <el-step title="数据库访问账户"></el-step>
      <el-step title="专题库" v-if="handleMsgObj.dbCreate=='1'"></el-step>
      <el-step title="资料访问权限审核"></el-step>
      <el-step title="完成"></el-step>
    </el-steps>
    <el-card class="box-card" shadow="never" v-if="stepNum==0">
      <handleAccount :handleObj="handleObj" ref="AccountRef" @handleDialogClose="handleClose" />
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==1">
      <handleLibrary :handleObj="handleObj" ref="LibraryRef" />
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==2">
      <handleMaterial :handleObj="handleObj" ref="MaterialRef" />
    </el-card>
    <el-card class="box-card" shadow="never" v-if="stepNum==3">
      <span style="font-size: 26px;">完成</span>
    </el-card>
    <div class="dialog-footer" style="margin-top:20px;">
      <el-button type="primary" v-if="stepNum!=5" @click="nextStep">下一步</el-button>
      <el-button type="primary" v-if="stepNum==3" @click="$emit('closeStep')">完成</el-button>
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
      stepNum: 1,
      msgFormDialog: {},
      handleObj: { pageName: "业务用户审核" }
    };
  },
  created() {
    this.handleObj = Object.assign(this.handleMsgObj, this.handleObj);
    console.log(this.handleObj);
  },
  methods: {
    handleClose() {},
    nextStep() {
      // 数据库访问账户 新增
      if (this.stepNum == 0) {
        this.$refs.AccountRef.trueAdd();
        this.stepNum = 1;
        return;
      }
      if (this.stepNum == 1) {
        this.$refs.AccountRef.trueAdd();
        this.stepNum = 1;
        return;
      }
    }
  }
};
</script>

