<template>
  <section class="fileHandleDict">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px">
      <el-form-item label="关键字:" prop="key_col">
        <el-input v-model="ruleForm.key_col" placeholder="请输入关键字"></el-input>
      </el-form-item>
      <el-form-item label="中文名:">
        <el-input v-model="ruleForm.name_cn"></el-input>
      </el-form-item>
      <el-form-item label="字典类型:">
        <el-select v-model="ruleForm.type">
          <el-option
            v-for="item in dictTypes"
            :key="item.type"
            :label="item.key_col"
            :value="item.type"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字典描述:">
        <el-input v-model="ruleForm.description"></el-input>
      </el-form-item>
      <el-form-item label="是否可删:" prop="can_delete">
        <el-select v-model="ruleForm.can_delete">
          <el-option
            v-for="item in canDeletes"
            :key="item.key"
            :label="item.label"
            :value="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示序号:" prop="serial_number">
        <el-input v-model="ruleForm.serial_number"></el-input>
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
    handleDictObj: {
      type: Object
    }
  },
  data() {
    return {
      ruleForm: {
        key_col: "",
        name_cn: "",
        type: "",
        description: "",
        can_delete: "",
        serial_number: ""
      },
      dictTypes: [],
      rules: {
        key_col: [
          { required: true, message: "关键字为必输项", trigger: "blur" }
        ],
        can_delete: [
          { required: true, message: "是否可删为必输项", trigger: "blur" }
        ],
        serial_number: [
          { required: true, message: "显示序号为必输项", trigger: "blur" }
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
<style lang="scss">
.fileHandleDict {
  .el-select {
    width: 100%;
  }
}
</style>
