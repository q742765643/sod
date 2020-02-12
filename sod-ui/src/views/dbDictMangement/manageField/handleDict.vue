<template>
  <section class="fileHandleDict">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px">
      <el-form-item label="字段编码:" prop="dbEleCode">
        <el-input v-model="ruleForm.dbEleCode" placeholder="请输入字段编码"></el-input>
      </el-form-item>
      <el-form-item label="服务代码:" prop="userEleCode">
        <el-input v-model="ruleForm.userEleCode" placeholder="请输入服务代码"></el-input>
      </el-form-item>
      <el-form-item label="中文名:" prop="dbEleName">
        <el-input v-model="ruleForm.dbEleName"></el-input>
      </el-form-item>
      <el-form-item label="字典类型:" prop="type">
        <el-select v-model="ruleForm.type">
          <el-option
            v-for="item in dictTypes"
            :key="item.type"
            :label="item.key_col"
            :value="item.type"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="组名称:" prop="dbEleName">
        <el-select v-model="ruleForm.type">
          <el-option
            v-for="item in dictTypes"
            :key="item.type"
            :label="item.key_col"
            :value="item.type"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字段精度:">
        <el-input v-model="ruleForm.dataPrecision"></el-input>
      </el-form-item>
      <el-form-item label="是否可为空:" prop="is_null">
        <el-select v-model="ruleForm.is_null">
          <el-option label="是" value="true"></el-option>
          <el-option label="否" value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否可更新:" prop="is_update">
        <el-select v-model="ruleForm.is_update">
          <el-option label="是" value="true"></el-option>
          <el-option label="否" value="false"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
    </div>
  </section>
</template>

<script>
import { addManageField } from "@/api/dbDictMangement/manageField";
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
        dataPrecision: ""
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
