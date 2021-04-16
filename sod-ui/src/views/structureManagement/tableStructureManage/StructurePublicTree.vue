<template>
  <!-- 公共元数据树 -->
  <section class="structurePublicTree">
    <el-scrollbar wrap-class="scrollbar-wrapper elTreeScroll">
      <el-tree
        :data="publicTree"
        :props="defaultProps"
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
    </el-scrollbar>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="makeSureSave">确认</el-button>
      <el-button @click="cancleSave">取消</el-button>
    </div>
  </section>
</template>

<script>
import { datumTypeGetTree } from "@/api/structureManagement/tableStructureManage/StructureClassify";
export default {
  name: "structurePublicTree",
  data() {
    return {
      //格式化tree数据
      defaultProps: {
        children: "children",
        label: "name"
      },
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
        await datumTypeGetTree().then(response => {
          this.publicTree = response.data || [];
          //将数据存储在本地  提高效率
          localStorage.setItem(
            "publicDatumTree",
            JSON.stringify(this.publicTree)
          );
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
        let { id, name } = nodeArr;
        this.checkNode = { id, name };
      }
    },
    mouseleave(data, $event) {
      $event.currentTarget.innerHTML = data.name;
    },
    mouseover(data, $event) {
      $event.currentTarget.innerHTML = data.name + "(" + data.id + ")";
    },
    //点击树节点
    handleNodeClick(data) {
      let { id, name } = data;
      this.checkNode = { id, name };
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
  .el-scrollbar {
    height: 290px;
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
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
