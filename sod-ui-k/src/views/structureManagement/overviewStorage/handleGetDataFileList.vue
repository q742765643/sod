<template>
  <section class="handleGetDataFileList">
    <el-tree
      :data="data"
      show-checkbox
      default-expand-all
      node-key="id"
      ref="tree"
      highlight-current
      :props="defaultProps"
    ></el-tree>
    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog">确 认</el-button>
      <el-button @click="cancelDialog">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "handleGetDataFileList",
  props: {
    handleInnerObj: {
      type: Array
    }
  },
  data() {
    return {
      data: [],
      innerDialog: "",
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },
  created() {
    this.data = this.handleInnerObj;
  },
  methods: {
    trueDialog() {
      let patharry = this.$refs.tree.getCheckedNodes();
      let newArry = [];
      for (var i = 0; i < patharry.length; i++) {
        if (patharry[i].isParent == false) {
          newArry.push(patharry[i]);
        }
      }
      if (newArry.length > 0) {
        this.$emit("handleInnerTrue", newArry);
      } else {
        this.$message({
          type: "warning",
          message: "请选择至少一个文件"
        });
      }
    },
    cancelDialog() {
      this.$emit("handleInnerCancel");
    }
  }
};
</script>

<style lang="scss">
</style>
