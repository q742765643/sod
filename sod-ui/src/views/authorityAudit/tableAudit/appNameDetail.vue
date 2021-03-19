<template>
  <div class="appNameDetailTem">
    <el-form
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="100px"
      label-position="right"
    >
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
          </div>
        </template>
        <div>
          <el-form-item label="应用名称" prop="appName">
            <el-input v-model="msgFormDialog.appName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="账号类型" prop="mytype">
            <el-input v-model="msgFormDialog.mytype" size="small"></el-input>
          </el-form-item>
          <el-form-item label="用途" prop="remark">
            <el-input
              type="textarea"
              v-model="msgFormDialog.remark"
              size="small"
            ></el-input>
          </el-form-item>
        </div>
      </el-card>
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>责任人信息</span>
          </div>
        </template>
        <div>
          <el-form-item label="责任人" prop="nickName">
            <el-input v-model="msgFormDialog.nickName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="单位" prop="legalUnits">
            <el-input
              v-model="msgFormDialog.legalUnits"
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="部门" prop="deptName">
            <el-input v-model="msgFormDialog.deptName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="phonenumber">
            <el-input
              v-model="msgFormDialog.phonenumber"
              size="small"
            ></el-input>
          </el-form-item>
        </div>
      </el-card>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      msgFormDialog: {},
    };
  },
  created() {
    this.msgFormDialog = this.handleObj;
    if (this.msgFormDialog.bizType == "biz") {
      this.msgFormDialog.mytype = "业务用户";
    } else {
      this.msgFormDialog.mytype = "开发用户";
    }
  },
  mounted() {},
  methods: {},
};
</script>

<style lang="scss">
.appNameDetailTem {
  .el-card {
    border: none;
    .el-card__header {
      font-size: 16px;
      font-weight: bold;
      color: #46a6ff;
      border-bottom: 2px solid #46a6ff;
    }
    .el-select {
      width: 100%;
    }
  }
}
</style>