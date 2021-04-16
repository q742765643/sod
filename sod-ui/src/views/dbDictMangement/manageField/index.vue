<template>
  <div class="app-container fielddManagement">
    <!-- 管理字段管理 -->
    <el-row :gutter="40">
      <el-col :span="4" :xs="24" class="elTreeAsideBox">
        <div class="sourceTreeTopOp">
          <el-button-group>
            <el-button type="primary" size="mini" @click="addType()" icon="el-icon-plus"></el-button>
            <el-button type="primary" size="mini" @click="editType()" icon="el-icon-edit"></el-button>
            <el-button type="primary" size="mini" @click="deleteAlert()" icon="el-icon-delete"></el-button>
          </el-button-group>
        </div>
        <div class="treeTitle">管理字段分组</div>
        <el-scrollbar wrap-class="scrollbar-wrapper elTreeScroll">
          <el-tree
            class="el-tree"
            :data="treeData"
            :props="defaultProps"
            :expand-on-click-node="false"
            ref="elTree"
            node-key="groupId"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </el-scrollbar>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
          <el-form-item label="字段编码">
            <el-input
              clearable
              size="small"
              v-model.trim="queryParams.dbEleCode"
              placeholder="字段编码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-plus" @click="addRow()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="primary" icon="el-icon-edit" @click="editRow()">编辑</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteRow()">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button size="small" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
          </el-col>
        </el-row>
        <el-table
          border
          :data="tableData"
          @selection-change="handleSelectionChange"
          ref="multipleTable"
        >
          <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column label=" " prop="id" v-if="false"></el-table-column>
          <el-table-column label="字段编码" prop="dbEleCode"></el-table-column>
          <el-table-column label="服务编码" prop="userEleCode"></el-table-column>
          <el-table-column label="中文名称" prop="dbEleName"></el-table-column>
          <el-table-column label="字段类型" prop="type"></el-table-column>
          <el-table-column label="字段精度" prop="dataPrecision" width="100"></el-table-column>
          <el-table-column label="是否可为空" prop="nullAble" :formatter="formatBoolean" width="100"></el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="handleQuery"
        />
      </el-col>
    </el-row>
    <!-- 弹窗 树-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="TreeDialog"
      v-dialogDrag
    >
      <handleTree v-if="TreeDialog" :handleTreeObj="handleTreeObj" @cancelDialog="cancelDialog"></handleTree>
    </el-dialog>

    <!-- 弹窗 字典-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="DictDialog"
      v-dialogDrag
    >
      <handleDict
        v-if="DictDialog"
        :handleGroup="handleGroup"
        :handleDictObj="handleDictObj"
        @cancelDialog="cancelDialog"
      ></handleDict>
    </el-dialog>
  </div>
</template>
<script>
import handleTree from "@/views/dbDictMangement/manageField/handleTree";
import handleDict from "@/views/dbDictMangement/manageField/handleDict";
import {
  findAllManageGroup,
  findManageFieldByPk,
  delManageGroup,
  pageField,
  delManageField,
  exportTable,
} from "@/api/dbDictMangement/manageField";

//分类树
export default {
  components: {
    handleTree,
    handleDict,
  },
  data() {
    return {
      highlight: true,
      treeData: [],
      defaultProps: {
        children: "children",
        label: "groupName",
      },
      checkNode: {}, //选中高亮的节点
      // 弹窗 树
      dialogTitle: "",
      TreeDialog: false,
      handleTreeObj: {},
      handleGroup: [],
      // 表格
      tableData: [],
      total: 0,
      queryParams: {
        dbEleCode: "", //字段编码
        groupId: "", //分组ID
        pageNum: 1,
        pageSize: 10,
      },
      // 选中的行
      multipleSelection: [],
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {},
    };
  },
  async created() {
    await this.getTreeData();
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.tableData.forEach((element, index) => {
            if (element.id == row.id) {
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index]
              );
            }
          });
        });
      }
    },
    // 导出
    handleExport() {
      exportTable(this.queryParams).then((res) => {
        this.downloadfileCommon(res);
      });
    },
    // 查询字段分组
    async getTreeData() {
      await findAllManageGroup().then((res) => {
        res.success
          ? (this.treeData = res.data)
          : this.$message({
              type: "error",
              message: res.msg,
            });
      });
      this.handleGroup = this.treeData;
      if (this.checkNode.groupId) {
        this.treeData.forEach((element) => {
          if (element.groupId == this.checkNode.groupId) {
            this.$refs.elTree.setCurrentKey(this.checkNode.groupId);
          }
        });
      }
      if (this.treeData.length > 0) {
        this.$refs.elTree.setCurrentKey(this.treeData[0].groupId);
        this.checkNode = this.treeData[0];
        this.queryParams.groupId = this.treeData[0].groupId;
        this.handleQuery();
      }
    },
    //格式化tree
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node treeItem">
          <i class="el-icon-menu"></i>
          <span style="margin-left:5px;">{node.label}</span>
        </span>
      );
    },
    //树点击查询
    handleNodeClick(node) {
      this.queryParams.groupId = node.groupId;
      this.checkNode = node;
      this.handleQuery();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    addType() {
      this.dialogTitle = "添加管理字段分组";
      this.handleTreeObj = {};
      this.TreeDialog = true;
    },
    editType() {
      if (!this.checkNode.groupId) {
        this.$message({ message: "请选中需要编辑的数据!", type: "error" });
        return;
      }
      this.dialogTitle = "编辑管理字段分组";
      this.handleTreeObj = this.checkNode;
      this.TreeDialog = true;
    },
    deleteAlert() {
      if (!this.checkNode.groupId) {
        this.$message({ message: "请选中需要删除的数据!", type: "error" });
        return;
      }
      this.$confirm("数据和类型都会被删除，确认删除", "温馨提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          delManageGroup({ groupId: this.queryParams.groupId }).then((res) => {
            this.$message({
              type: "success",
              message: "删除成功",
            });
            this.checkNode = {};
            this.getTreeData();
          });
        })
        .catch(() => {}); //一定别忘了这个
    },
    async cancelDialog(msg) {
      if (msg == "Tree") {
        await this.getTreeData();
      } else if (msg == "Field") {
        this.handleQuery();
      }
      this.TreeDialog = false;
      this.DictDialog = false;
    },
    handleQuery() {
      this.loading = true;
      console.log(this.queryParams);
      pageField(this.queryParams).then((res) => {
        this.tableData = res.data.pageData;
        this.total = res.data.totalCount;
        this.loading = false;
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    addRow() {
      this.handleDictObj = {};
      this.dialogTitle = "新增字典";
      this.DictDialog = true;
    },
    editRow() {
      if (this.multipleSelection.length != 1) {
        this.$message({ message: "请选中一条需要编辑的数据!", type: "error" });
        return;
      }
      findManageFieldByPk({ id: this.multipleSelection[0].id }).then((res) => {
        this.dialogTitle = "编辑管理字段";
        this.handleDictObj = res.data;
        this.handleDictObj.groupId = this.checkNode.groupId;
        this.DictDialog = true;
      });
    },
    deleteRow() {
      if (this.multipleSelection.length == 0) {
        this.$message({ message: "请选中需要删除的数据!", type: "error" });
        return;
      }
      this.$confirm("确认删除吗", "温馨提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          let ids = [];
          this.multipleSelection.forEach((element) => {
            ids.push(element.id);
          });
          delManageField({ ids: ids.join(",") }).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
            }
            this.handleQuery();
          });
        })
        .catch(() => {}); //一定别忘了这个
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    formatBoolean(row, column, cellValue) {
      var ret = ""; //你想在页面展示的值
      if (row.nullAble) {
        ret = "是"; //根据自己的需求设定
      } else {
        ret = "否";
      }
      return ret;
    },
  },
};
</script>
<style lang="scss">
.fielddManagement {
  .sourceTreeTopOp {
    width: 100%;
    padding: 10px 0;
    text-align: center;
    .el-button {
      i {
        font-weight: bold;
      }
    }
  }
  .treeTitle {
    padding: 8px 0;
    width: 100%;
    text-align: center;
    background: #eee;
    font-size: 14px;
  }
  .el-tree-node.is-current > .el-tree-node__content {
    background-color: #dfeffd;
  }
}
</style>
