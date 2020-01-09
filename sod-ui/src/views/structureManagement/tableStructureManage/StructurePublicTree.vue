<template>
  <!-- 公共元数据树 -->
  <section class="structurePublicTree">
    <el-tree
      class="selScrollBar"
      :data="publicTree"
      node-key="id"
      :default-expanded-keys="expandedKeys"
      :highlight-current="true"
      @node-click="handleNodeClick"
      ref="publicTree"
    >
      <span
        class="custom-tree-node"
        slot-scope="{ node, data }"
        @mouseleave="mouseleave(data,$event)"
        @mouseover="mouseover(data,$event)"
      >
        <span class="el-tree-node__label">{{ node.label }}</span>
      </span>
    </el-tree>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="makeSureSave">确认</el-button>
      <el-button @click="cancleSave">取消</el-button>
    </div>
  </section>
</template>

<script>
export default {
  name: "structurePublicTree",
  data() {
    return {
      publicTree: [], //公共元数据树
      expandedKeys: [], //默认展开节点
      checkNode: {} //要传输至父组件的值
    };
  },
  async created() {
    await this.getPublicTree();
    if (this.publicTree.length !== 0) {
      this.getDefaultNode(this.publicTree[0]);
    }
    this.$refs.publicTree.setCurrentKey(this.checkNode.id);
  },
  methods: {
    async getPublicTree() {
      if (!localStorage.getItem("publicDatumTree")) {
        await this.axios
          .get(interfaceObj.TableStructure_publicDatumTree)
          .then(res => {
            this.publicTree = res.data.data || [];
            //将数据存储在本地  提高效率
            localStorage.setItem(
              "publicDatumTree",
              JSON.stringify(this.publicTree)
            );
          })
          .catch(error => {
            console.log("出错了，错误信息：" + error);
          });
      } else {
        this.publicTree = JSON.parse(localStorage.getItem("publicDatumTree"));
      }
    },
    //获取默认选中的节点
    getDefaultNode(nodeArr) {
      if (nodeArr.children) {
        let newNodeArr = nodeArr.children[0];
        this.getDefaultNode(newNodeArr);
        this.expandedKeys.push(nodeArr.id);
      } else {
        let { id, label } = nodeArr;
        this.checkNode = { id, label };
      }
    },
    mouseleave(data, $event) {
      $event.currentTarget.innerHTML = data.label;
    },
    mouseover(data, $event) {
      $event.currentTarget.innerHTML = data.label + "(" + data.id + ")";
    },
    //点击树节点
    handleNodeClick(data) {
      let { id, label } = data;
      this.checkNode = { id, label };
    },
    makeSureSave() {
      this.$emit("returnCheckedNode", this.checkNode);
    },
    cancleSave() {
      this.$emit("closePublicDialog");
    }
  }
};
</script>

<style lang="scss">
.structurePublicTree {
  .el-tree {
    overflow-y: auto;
    max-height: 290px;
  }
  .el-tree--highlight-current
    .el-tree-node.is-current
    > .el-tree-node__content {
    background-color: #c6e2ff;
  }
  .custom-tree-node {
    width: 100%;
    padding: 6px;
  }
}
</style>
