<template>
  <section class="handleWork">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="80px">
      <el-form-item label="用户名称" prop="conditions">
        <el-input size="small" v-model.trim="msgFormDialog.conditions"></el-input>
      </el-form-item>
      <el-form-item label="机构" prop="conditions">
        <el-input size="small" v-model.trim="msgFormDialog.conditions"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="conditions">
        <el-input size="small" v-model.trim="msgFormDialog.conditions"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
export default {
  name: "handleWork",
  props: {
    handleObj: {
      type: Object
    }
  },
  components: {},
  data() {
    return {
      msgFormDialog: {},
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" }
        ],
        databaseId: [
          { required: true, message: "请选择数据库IP", trigger: "change" }
        ],
        jobCron: [
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ],
        conditions: [
          { required: true, message: "请输入where条件", trigger: "blur" }
        ]
      }
    };
  },
  created() {},
  methods: {}
};
</script>

