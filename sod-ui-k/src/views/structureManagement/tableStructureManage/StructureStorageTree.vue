<template>
  <!-- 存储元数据树 -->
  <section class="structureStorageTree">
    <el-scrollbar wrap-class="scrollbar-wrapper elTreeScroll">
      <el-tree
        :data="storageTree"
        :props="defaultProps"
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
    </el-scrollbar>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="makeSureSave">确认</el-button>
      <el-button @click="cancleSave">取消</el-button>
    </div>
  </section>
</template>

<script>
import { dataClassAll } from "@/api/structureManagement/tableStructureManage/StructureClassify";
//接口地址
export default {
  name: "structureStorageTree",
  props: {
    typeText: {
      type: String
    }
  },
  data() {
    return {
      //格式化tree数据
      defaultProps: {
        children: "children",
        label: "name"
      },
      storageTree: [], //公共元数据树
      expandedKeys: [], //默认展开节点
      checkNode: {} //要传输至父组件的值
    };
  },
  async created() {
    await dataClassAll().then(response => {
      this.storageTree = response.data;
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
      if (this.typeText == "资料") {
        if (this.checkNode && this.checkNode.id.split(".").length == 3) {
          console.log(this.checkNode);
          this.$emit("storageCheckedNode", this.checkNode);
        } else {
          this.$message({
            type: "error",
            message: "只能选择三级目录"
          });
        }
      } else {
        this.$emit("storageCheckedNode", this.checkNode);
      }
    },
    cancleSave() {
      this.$emit("closeStorageTreeDialog", this.checkNode);
    }
  }
};
</script>

<style lang="scss">
.structureStorageTree {
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
  .el-tree-node[aria-disabled="true"] {
    display: none;
  }
}
</style>
