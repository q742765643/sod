<template>
  <section class="handlePowerDialog">
    <el-alert title="您可以选择多个权限分配给当前选定的用户" type="info" :closable="false"></el-alert>
    <div class="treeBox selScrollBar">
      <el-tree
        :data="treeData"
        show-checkbox
        node-key="id"
        default-expand-all
        :default-checked-keys="checkedTree"
        :props="defaultProps"
        ref="elTree"
      ></el-tree>
    </div>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "handlePowerDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      treeData: [],
      checkedTree: [],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  created() {
    // this.initDetail();
  },
  methods: {
    // 获取详情
    initDetail() {
      debugger;
      this.axios
        .post(interfaceObj.resourcesManage_permissioRole, {
          roleId: this.handleObj.id
        })
        .then(res => {
          this.treeData = res.data.resources;
          this.checkedTree = res.data.checkIds;
        });
    },

    trueDialog(formName) {
      debugger;
      let checkNodes = this.$refs.elTree
        .getCheckedNodes()
        .concat(this.$refs.elTree.getHalfCheckedNodes());
      if (checkNodes.length > 0) {
        let ids = [];
        checkNodes.forEach(item => {
          ids.push(item.id);
        });
        this.axios
          .post(interfaceObj.resourcesManage_saveRoleRescours, {
            rescId: ids.join(","),
            roleId: this.handleObj.id
          })
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                type: "success",
                message: "分配成功，必须重新登录才生效"
              });
            } else {
              this.$message({
                type: "error",
                message: res.data.returnMessage
              });
            }
            this.$emit("cancelHandle");
          });
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    }
  }
};
</script>

<style lang="scss">
.handlePowerDialog {
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
  .treeBox {
    max-height: 400px;
    overflow-y: auto;
  }
  .el-tree {
    margin-top: 12px;
  }
}
</style>
