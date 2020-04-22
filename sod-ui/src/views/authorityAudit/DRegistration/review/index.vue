<template>
  <section class="stepDreg">
    <div class="loadTable">
      <el-steps :active="stepNum" finish-status="success">
        <el-step title="储存元数据注册"></el-step>
        <el-step title="表结构管理"></el-step>
        <el-step title="数据同步"></el-step>
        <el-step title="数据迁移"></el-step>
        <el-step title="数据备份"></el-step>
        <el-step title="完成"></el-step>
      </el-steps>
      <el-card class="box-card" v-if="stepNum==0"></el-card>
      <el-card class="box-card" v-if="stepNum==1"></el-card>
      <el-card class="box-card" v-if="stepNum==2"></el-card>
      <el-card class="box-card" v-if="stepNum==3"></el-card>
      <el-card class="box-card" v-if="stepNum==4"></el-card>
      <el-card class="box-card" v-if="stepNum==5">
        <span style="font-size: 26px;">完成</span>
      </el-card>
    </div>
    <div class="dialog-footer">
      <el-button type="primary" v-if="stepNum!=5" @click="nextStep">下一步</el-button>
      <el-button type="primary" v-if="stepNum==5" @click="$emit('closeStep')">完成</el-button>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      stepNum: 0
    };
  },
  created() {},
  methods: {
    nextStep() {}
  }
};
</script>

<style  lang="scss">
.stepDreg {
}
</style>