<template>
  <!-- 存储元数据树 -->
  <section class="structureStorageTree">
    <el-tree
      class="selScrollBar"
      :data="storageTree"
      node-key="id"
      :default-expanded-keys="expandedKeys"
      @node-click="handleNodeClick"
      :highlight-current="true"
      ref="storageTree"
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
//接口地址
export default {
  name: "structureStorageTree",
  data() {
    return {
      storageTree: [], //公共元数据树
      expandedKeys: [], //默认展开节点
      checkNode: {} //要传输至父组件的值
    };
  },
  async created() {
    await this.axios
      .get(interfaceObj.TableStructure_dataTypeTree_add)
      .then(res => {
        if (res.data.returnCode == 0) {
          this.storageTree = res.data.data;
        }
      })
      .catch(error => {
        console.log("出错了，错误信息" + error);
      });
    if (this.storageTree.length !== 0) {
      this.getDefaultNode(this.storageTree[0]);
    }
    this.$refs.storageTree.setCurrentKey(this.checkNode.id);
  },
  methods: {
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
      this.$emit("storageCheckedNode", this.checkNode);
    },
    cancleSave() {
      this.$emit("closeStorageTreeDialog", this.checkNode);
    }
  }
};
</script>

<style lang="scss">
.structureStorageTree {
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
