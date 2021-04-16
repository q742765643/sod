<template>
  <section class="fileHandleDict">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px">
      <el-form-item label="关键字:" prop="keyCol">
        <el-input v-model.trim="ruleForm.keyCol" placeholder="请输入字段编码"></el-input>
      </el-form-item>
      <el-form-item label="中文名:">
        <el-input v-model.trim="ruleForm.nameCn"></el-input>
      </el-form-item>
      <el-form-item label="字段类型:" prop="type">
        <el-select v-model.trim="ruleForm.type">
          <el-option
            v-for="item in dictTypes"
            :key="item.keyCol"
            :label="item.keyCol"
            :value="item.type"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字典描述:">
        <el-input v-model.trim="ruleForm.description" placeholder="请输入字典描述"></el-input>
      </el-form-item>
      <el-form-item label="是否可删:" prop="canDelete">
        <el-select v-model.trim="ruleForm.canDelete">
          <el-option label="是" value="Y"></el-option>
          <el-option label="否" value="N"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示序号:" prop="serialNumber">
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
    handleDictObj: {
      type: Object
    },
    handleGroup: {
      type: Array
    }
  },
  data() {
    return {
      ruleForm: {
        keyCol: "",
        description: "",
        nameCn: "",
        canDelete: "",
        serialNumber: "",
        type: "",
        flag: "D"
      },
      GroupNames: [],
      dictTypes: [],
      rules: {
        keyCol: [{ required: true, message: "请输入关键字", trigger: "blur" }],
        canDelete: [
          { required: true, message: "请选择是否可删", trigger: "change" }
        ],
        type: [
          { required: true, message: "请选择字段类型", trigger: "change" }
        ],
        serialNumber: [
          { required: true, message: "请输入序号", trigger: "blur" }
        ]
      }
    };
  },
  async created() {
    this.dictTypes = this.handleGroup;
    if (this.handleDictObj.id) {
      this.ruleForm = this.handleDictObj;
    }
  },
  methods: {
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    trueDialog(formName) {
      console.log(this.ruleForm);
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleDictObj.id) {
            updateById(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.$emit("cancelDialog", "Dict");
              } else {
                this.$message({
                  type: "error",
                  message: "编辑失败"
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
                this.$emit("cancelDialog", "Dict");
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
<style lang="scss">
.fileHandleDict {
  .el-select {
    width: 100%;
  }
}
</style>
