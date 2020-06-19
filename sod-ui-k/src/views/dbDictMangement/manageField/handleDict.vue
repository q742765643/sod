<template>
  <section class="fileHandleDict">
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="100px">
      <el-form-item label="字段编码:" prop="dbEleCode">
        <el-input v-model.trim="ruleForm.dbEleCode" placeholder="请输入字段编码"></el-input>
      </el-form-item>
      <el-form-item label="服务代码:" prop="userEleCode">
        <el-input v-model.trim="ruleForm.userEleCode" placeholder="请输入服务代码"></el-input>
      </el-form-item>
      <el-form-item label="中文名:" prop="dbEleName">
        <el-input v-model.trim="ruleForm.dbEleName"></el-input>
      </el-form-item>
      <el-form-item label="字段类型:" prop="type">
        <el-select v-model.trim="ruleForm.type">
          <el-option
            v-for="item in dictTypes"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="组名称:" prop="groupId">
        <el-select v-model.trim="ruleForm.groupId">
          <el-option
            v-for="(item,index) in GroupNames"
            :key="index"
            :label="item.groupName"
            :value="item.groupId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字段精度:">
        <el-input v-model.trim="ruleForm.dataPrecision"></el-input>
      </el-form-item>
      <el-form-item label="是否可为空:" prop="nullAble">
        <el-select v-model.trim="ruleForm.nullAble">
          <el-option label="是" value="true"></el-option>
          <el-option label="否" value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否可更新:" prop="updateAble">
        <el-select v-model.trim="ruleForm.updateAble">
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
import { codeVer, chinese, num } from "@/components/commonVaildate";
import {
  addManageField,
  findByType,
  editManageField
} from "@/api/dbDictMangement/manageField";
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
    var dbEleCodeValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入字段编码"));
      } else if (!codeVer(value)) {
        callback(
          new Error("字段编码不允许输入小写字母和中文，且需以大写字母开头")
        );
      } else {
        callback();
      }
    };
    var userEleCodeValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入服务代码"));
      } else if (!codeVer(value)) {
        callback(
          new Error("服务代码不允许输入小写字母和中文，且需以大写字母开头")
        );
      } else {
        callback();
      }
    };
    var dbEleNameValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入中文名"));
      } else if (!chinese(value)) {
        callback(new Error("中文名称只能输入中文"));
      } else {
        callback();
      }
    };

    return {
      ruleForm: {
        dbEleCode: "",
        userEleCode: "",
        dbEleName: "",
        nullAble: "",
        updateAble: "",
        groupId: "",
        type: ""
      },
      GroupNames: [],
      dictTypes: [],
      rules: {
        dbEleCode: [
          { required: true, validator: dbEleCodeValidate, trigger: "blur" }
        ],
        userEleCode: [
          { required: true, validator: userEleCodeValidate, trigger: "blur" }
        ],
        dbEleName: [
          { required: true, validator: dbEleNameValidate, trigger: "blur" }
        ],
        groupId: [
          { required: true, message: "请选择组名称", trigger: "change" }
        ],
        nullAble: [
          { required: true, message: "请选择是否可为空", trigger: "change" }
        ],
        updateAble: [
          { required: true, message: "请选择是否可更新", trigger: "change" }
        ],
        type: [{ required: true, message: "请选择字段类型", trigger: "change" }]
      }
    };
  },
  async created() {
    this.GroupNames = this.handleGroup;
    await this.getDict();
    if (this.handleDictObj && this.handleDictObj.id) {
      this.handleDictObj.nullAble = this.handleDictObj.nullAble.toString();
      this.handleDictObj.updateAble = this.handleDictObj.updateAble.toString();
      this.ruleForm = this.handleDictObj;
    }
  },
  methods: {
    cancelDialog() {
      this.$emit("cancelDialog", false);
    },
    async getDict() {
      await findByType().then(res => {
        if (res.code == "200") {
          this.dictTypes = res.data;
        }
      });
    },
    trueDialog(formName) {
      console.log(this.ruleForm);
      if (
        this.ruleForm.type == "varchar" ||
        this.ruleForm.type == "char" ||
        this.ruleForm.type == "numberic" ||
        this.ruleForm.type == "number" ||
        this.ruleForm.type == "int" ||
        this.ruleForm.type == "float" ||
        this.ruleForm.type == "decimal"
      ) {
        if (this.ruleForm.dataPrecision == "") {
          this.$message({
            type: "error",
            message: "该类型字段必须填写数据精度"
          });
          return;
        }
        if (!num(this.ruleForm.dataPrecision)) {
          this.$message({
            type: "error",
            message: "字段精度只能输入大于0"
          });
          return;
        }
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleDictObj.id) {
            editManageField(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.$emit("cancelDialog", "Field");
              } else {
                this.$message({
                  type: "error",
                  message: "编辑失败"
                });
              }
            });
          } else {
            addManageField(this.ruleForm).then(res => {
              if (res.code == "200") {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
                this.$emit("cancelDialog", "Field");
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
