<template>
  <section class="dataDialog">
    <el-form :model="msgFormDialog" :rules="baseFormRules" ref="fromRef" label-width="120px">
      <el-form-item prop="depttype" label="部门类型:">
        <el-select v-model="msgFormDialog.depttype">
          <el-option label="机构" value="01"></el-option>
          <el-option label="部门" value="02"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="deptcode" label="部门编号:">
        <el-input clearable size="small" v-model="msgFormDialog.deptcode" :disabled="handleDis" />
      </el-form-item>
      <el-form-item prop="deptname" label="部门名称:">
        <el-input clearable size="small" v-model="msgFormDialog.deptname" />
      </el-form-item>
      <el-form-item prop="parentCode" label="上级部门:" v-if="msgFormDialog.parentCode !== '0'">
        <treeselect
          v-model.trim="msgFormDialog.parentCode"
          :options="deptOptions"
          placeholder="请选择上级部门"
        />
      </el-form-item>
      <el-form-item prop="serialNumber" label="部门排序:">
        <el-input-number
          clearable
          size="small"
          v-model="msgFormDialog.serialNumber"
        />
      </el-form-item>
      <el-form-item prop="deptLevel" label="部门级别:">
        <el-input-number
          clearable
          size="small"
          v-model="msgFormDialog.deptLevel"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('fromRef')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getById,
  editById,
  saveDepartManage,
  treeselect,
} from "@/api/portalMangement/departMangement";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "dataDialog",
  components: { Treeselect },
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      imageUrl: "",
      handleDis: false,
      deptOptions: [],
      //编辑页面列
      msgFormDialog: {
        depttype: "",
        deptcode:"",
        deptname: "",
        parentCode: "",
        serialNumber: "1",
        deptLevel: "",
      },
      baseFormRules: {
        depttype: [
          { required: true, message: "请选择部门类型", trigger: "change" },
        ],
        deptcode: [
          { required: true, message: "请输入部门编号 ", trigger: "blur" },
        ],
        deptname: [
          { required: true, message: "请输入部门名称 ", trigger: "blur" },
        ],
        parentCode: [
          { required: true, message: "请选择上级部门", trigger: "change" },
        ],
        deptLevel: [
          { required: true, message: "请选择部门级别", trigger: "change" },
        ],
      },
    };
  },
  async created() {
    this.getTreeselect();
    // 详情回显
    if (this.handleObj.id) {
      this.handleDis = true;
      getById({ id: this.handleObj.id }).then((res) => {
        this.msgFormDialog = res.data;
      });
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    onExceed() {
      this.$message({
        type: "error",
        message: "只能上传一个文件",
      });
    },
    JarSuccess(res, file) {
      if (res.code == 200) {
        this.msgFormDialog.sdkJarUrl = res.url;
      } else {
        this.$message({
          type: "error",
          message: res.msg,
        });
      }
    },

    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.handleObj.id) {
            editById(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "编辑成功",
              });
              this.$emit("cancelDialog");
            });
          } else {
            saveDepartManage(this.msgFormDialog).then((res) => {
              this.$message({
                type: "success",
                message: "增加成功",
              });
              this.$emit("cancelDialog");
            });
          }
        }
      });
    },

    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    },

    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
  },
};
</script>

<style lang="scss">
.dataDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
