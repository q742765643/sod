<template>
  <section class="fileHandleDict">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px">
      <el-form-item label="关键字:" prop="keyCol">
        <el-input v-model="ruleForm.keyCol" placeholder="请输入关键字"></el-input>
      </el-form-item>
      <el-form-item label="中文名:">
        <el-input v-model="ruleForm.nameCn"></el-input>
      </el-form-item>
      <el-form-item label="字典类型:">
        <el-select v-model="ruleForm.type" @change="setMenu">
          <el-option
            v-for="item in dictTypes"
            :key="item.type"
            :label="item.keyCol"
            :value="item.type"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字典描述:">
        <el-input v-model="ruleForm.description"></el-input>
      </el-form-item>
      <el-form-item label="是否可删:" prop="canDelete">
        <el-select v-model="ruleForm.canDelete">
          <el-option label="Y" value="Y"></el-option>
          <el-option label="N" value="N"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示序号:" prop="serialNumber">
        <el-input v-model="ruleForm.serialNumber"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
    </div>
  </section>
</template>

<script>
import { addTableData } from "@/api/dbDictMangement/fieldManagement/handleDict";

export default {
  name: "filedSearchDeploy",
  components: {},
  props: {
    handleDictObj: {
      type: Object
    },
    dictTypes: {
      type: Array
    }
  },
  data() {
    return {
      ruleForm: {
        keyCol: "",
        nameCn: "",
        type: "",
        description: "",
        canDelete: "",
        serialNumber: "",
        menu: "",
        flag: "D"
      },
      rules: {
        keyCol: [
          { required: true, message: "关键字为必输项", trigger: "blur" }
        ],
        canDelete: [
          { required: true, message: "是否可删为必输项", trigger: "blur" }
        ],
        serialNumber: [
          { required: true, message: "显示序号为必输项", trigger: "blur" }
        ]
      }
    };
  },
  created() {},
  methods: {
    setMenu() {
      let indexType = this.ruleForm.type;
      this.dictTypes.forEach(item => {
        if (item.type == indexType) {
          this.ruleForm.menu = item.menu;
        }
      });
    },
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          addTableData(this.ruleForm).then(res => {
            this.$emit("cancelDialog", "D");
          });
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
