<template>
  <div class="app-container tableStructureManageBox">
    <!-- 表结构管理 -->
    <el-container>
       <!-- 左侧树 -->
      <StructureClassify
        ref="classifyTree"
        :treeIdOfDR="treeIdOfDR"
        @showMaterialSingle="showMaterialSingle"
        @getTreeUrlOfTab="getTreeUrlOfTab"
        @searchFun="searchFun"
      />
    </el-container>

  </div>
</template>

<script>
//分类树
import StructureClassify from "@/views/structureManagement/tableStructureManage/StructureClassify";
export default {
  components: {
    StructureClassify,
  },
  data() {
    return {
       treeIdOfDR: this.$route.params.treeIdOfDR,
     
    };
  },
  created() {
  },
  methods: {
      //新增，编辑资料树  或者  资料
    showMaterialSingle(operateType) {
      this.editMaterialArry = [];
      if (operateType.title === "新增资料树节点") {
        this.materialSingleTitle = operateType.title;
        this.isSourceTree = true;
        this.editNodeId = operateType.editNodeId;
        this.treeUrl = operateType.url;
      } else if (operateType.title === "编辑资料树节点") {
        this.materialSingleTitle = operateType.title;
        this.isSourceTree = true;
        this.editNodeId = operateType.editNodeId;
        this.treeUrl = operateType.url;
      } else if (operateType === "新增资料") {
        this.materialSingleTitle = operateType;
        this.editMaterial = "";
        this.editMaterialArry = [];
        this.isSourceTree = false;
      } else if (operateType === "编辑资料") {
        if (this.currentRow == null || this.currentRow.length == 0) {
          this.$message({
            type: "error",
            message: "请选择一条数据"
          });
          return false;
        } else {
          this.materialSingleTitle = operateType;
          this.editMaterial = this.currentRow[0].data_class_id;
          this.currentRow.forEach((item, index) => {
            let obj = {};
            obj.logic_id = item.logic_id;
            obj.logic_name = item.logic_name;
            obj.storage_name = item.name_cn;
            obj.storage_type = item.storage_type;
            obj.physicsName = item.database_name;
            obj.special = item.database_id;
            obj.special_database_name = item.special_database_name;
            this.editMaterialArry.push(obj);
          });
          this.isSourceTree = false;
          // 获取详情
        }
      }
      this.materialSingleVisible = true;
    },
    // 左侧tab点击时获取树的url
    getTreeUrlOfTab(url) {
      this.treeUrl = url;
      this.searchObj.data_class_id_str = "";
      this.searchObj.class_name = "";
      this.searchObj.d_data_id = "";
    },
    searchFun(){}
    
  }
};
</script>
<style lang="scss">
.tableStructureManageBox {
  padding: 20px 5px;
  .elMain {
    border: 1px solid #ebeef5;
    background-color: #fff;
    margin-right: 10px;
    box-sizing: border-box;
    padding: 0px;
    height: calc(100vh - 190px);
    overflow: hidden;
    margin-bottom: 0;
    .treeTitle {
      border-bottom: 1px solid #ebeef5;
      padding: 10px;
      background: #eee;
      font-size: 14px;

      i {
        font-size: 16px;
        color: #1e9fff;
      }
    }
    .tableBox {
      height: calc(100vh - 310px);
      overflow-y: auto;
    }

    .tableTop {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 10px;

      .sourceOperate {
        .el-button {
          margin-left: 0px;
          border-radius: 0px;
          border-left: 1px solid #9dd5f6;
        }

        .el-button:first-child {
          border-left: 1px solid #409eff;
          border-top-left-radius: 2px;
          border-bottom-left-radius: 2px;
        }

        .el-button:last-child {
          border-top-right-radius: 2px;
          border-bottom-right-radius: 2px;
        }
      }
    }

    .tableCon {
      box-sizing: border-box;
      padding: 10px;
    }

    .el-form-item {
      margin-bottom: 0px;
    }

    .el-table .cell {
      padding-left: 0px;
      padding-right: 0px;
    }
    .el-dropdown {
      margin-left: 1px;
    }
    .btnRound {
      border-radius: 50%;
      margin-top: 4px;
      margin-right: 4px;
      height: 6px;
      width: 6px;
      display: inline-block;
      vertical-align: top;
      overflow: hidden;
    }
    .blueRound {
      background: #1e9fff;
    }
    .orangRound {
      background: #ff5722;
    }
  }
}
</style>
