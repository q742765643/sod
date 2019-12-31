<template>
  <section class="fileHandleTree">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item prop="key_col" label="分组名:">
        <el-input size="small" v-model="ruleForm.key_col" placeholder="请输入分组名"></el-input>
      </el-form-item>
      <el-form-item prop="description" label="分组描述:">
        <el-input size="small" v-model="ruleForm.description"></el-input>
      </el-form-item>
      <el-form-item prop="serial_number" label="显示序号:">
        <el-input size="small" v-model="ruleForm.serial_number"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
    </div>
  </section>
</template>

<script>
export default {
  name: "filedSearchDeploy",
  components: {},
  props: {
    handleTreeObj: {
      type: Object
    }
  },
  data() {
    return {
      ruleForm: {
        key_col: "",
        description: "",
        serial_number: ""
      },
      rules: {
        key_col: [
          { required: true, message: "分组名称为必输项", trigger: "blur" }
        ],
        description: [
          { required: true, message: "分组描述为必输项", trigger: "blur" }
        ],
        serial_number: [
          { required: true, message: "分组序号为必输项", trigger: "blur" }
        ]
      }
    };
  },
  created() {},
  methods: {
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("cancelDialog", false);
        }
      });
    }
  }
};
</script>

