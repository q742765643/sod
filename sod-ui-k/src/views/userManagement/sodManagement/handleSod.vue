<template>
  <section class="handleSodDialog">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="100px">
      <el-form-item label="上级功能" prop="parentId">
        <el-select size="medium" v-model.trim="msgFormDialog.parentId">
          <el-option
            v-for="(item,index) in queryParentNames"
            :key="index"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="功能名称" prop="name">
        <el-input size="medium" v-model.trim="msgFormDialog.name"></el-input>
      </el-form-item>
      <el-form-item label="功能KEY" prop="resKey">
        <el-input size="medium" v-model.trim="msgFormDialog.resKey"></el-input>
      </el-form-item>
      <el-form-item label="功能URL" prop="resUrl">
        <el-input size="medium" v-model.trim="msgFormDialog.resUrl"></el-input>
      </el-form-item>
      <el-form-item label="功能类型" prop="type">
        <el-radio-group v-model.trim="msgFormDialog.type">
          <el-radio :label="0">目录</el-radio>
          <el-radio :label="1">菜单</el-radio>
          <el-radio :label="2">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="优先级" prop="level">
        <el-input size="medium" v-model.trim="msgFormDialog.level"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" size="medium" v-model.trim="msgFormDialog.description"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "handleSodDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        parentId: "",
        name: "",
        resKey: "",
        resUrl: "",
        type: 0,
        level: "",
        description: ""
      },
      queryParentNames: [],
      rules: {
        parentId: [
          { required: true, message: "请选择上级功能", trigger: "change" }
        ],
        name: [{ required: true, message: "请输入功能名称", trigger: "blur" }],
        resKey: [{ required: true, message: "请输入功能KEY", trigger: "blur" }],
        resUrl: [{ required: true, message: "请输入功能URL", trigger: "blur" }],
        type: [
          { required: true, message: "请选择功能类型", trigger: "change" }
        ],
        level: [{ required: true, message: "请输入优先级", trigger: "blur" }]
      }
    };
  },
  created() {
    // this.getParentNames();
    // this.initDetail();
  },
  methods: {
    // 获取详情
    initDetail() {
      if (this.handleObj.id == undefined) {
        // 是新增
      } else {
        // 是详情
        this.msgFormDialog = this.handleObj;
        this.msgFormDialog.type = Number(this.handleObj.type);
      }
    },
    getParentNames() {
      this.axios
        .post(interfaceObj.resourcesManage_queryParentName)
        .then(res => {
          this.queryParentNames = res.data.data;
        });
    },

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id == undefined) {
            this.axios
              .post(
                interfaceObj.resourcesManage_addResources,
                this.msgFormDialog
              )
              .then(res => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "新增成功"
                  });
                } else {
                  this.$message({
                    type: "error",
                    message: "新增失败"
                  });
                }
                this.$emit("cancelHandle");
              });
          } else {
            this.axios
              .post(
                interfaceObj.resourcesManage_updateResources1,
                this.msgFormDialog
              )
              .then(res => {
                if (res.data.returnCode == 0) {
                  this.$message({
                    type: "success",
                    message: "编辑成功"
                  });
                } else {
                  this.$message({
                    type: "error",
                    message: "编辑失败"
                  });
                }
                this.$emit("cancelHandle");
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    }
  }
};
</script>

<style lang="scss">
.handleSodDialog {
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
  .el-textarea__inner {
    width: 98%;
  }
  .el-select {
    width: 100%;
  }
}
</style>
