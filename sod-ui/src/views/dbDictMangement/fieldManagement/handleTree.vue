<template>
  <section class="fileHandleTree">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item prop="keyCol" label="分组名:">
        <el-input size="small" v-model.trim="ruleForm.keyCol" placeholder="请输入分组名"></el-input>
      </el-form-item>
      <el-form-item prop="description" label="分组描述:">
        <el-input size="small" v-model.trim="ruleForm.description"></el-input>
      </el-form-item>
      <el-form-item prop="serialNumber" label="显示序号:">
        <el-input-number
          :min="0"
          class="number"
          size="small"
          v-model.trim="ruleForm.serialNumber"
          placeholder="请输入显示序号"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
    </div>
  </section>
</template>

<script>
import { addType, updateById } from "@/api/dbDictMangement/fieldManagement";
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
        keyCol: "",
        description: "",
        serialNumber: "",
        flag: "T",
        menu: 2
      },
      rules: {
        keyCol: [
          { required: true, message: "分组名称为必输项", trigger: "blur" }
        ],
        description: [
          { required: true, message: "分组描述为必输项", trigger: "blur" }
        ],
        serialNumber: [
          { required: true, message: "分组序号为必输项", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.ruleForm = this.handleTreeObj;
  },
  methods: {
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleTreeObj.id) {
            updateById(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.$emit("cancelDialog", "Tree");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                });
              }
            });
          } else {
            addType(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
                this.$emit("cancelDialog", "Tree");
              } else {
                this.$message({
                  type: "error",
                  message: "新增失败"
                });
              }
            });
          }
        }
      });
    }
  }
};
</script>

