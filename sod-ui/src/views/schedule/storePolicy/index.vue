<template>
  <div class="app-container storePolicy">
    <!-- 资料存储策略 -->
    <el-container>
      <!-- 左侧树 -->
      <el-aside class="elAside">
        <el-input size="small" placeholder="输入关键字进行过滤" v-model="filterText" class="filterText"></el-input>
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-tree
            class="el-tree"
            :highlight-current="highlight"
            node-key="type"
            :data="treeData"
            :props="defaultProps"
            :default-expanded-keys="expandedKeys"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            ref="elTree"
          ></el-tree>
        </el-scrollbar>
      </el-aside>
      <el-main class="elMain">
        <el-row v-for="(row,rowIndex) in groupList" :key="rowIndex" :gutter="18">
          <p class="el-p">
            <i class="el-icon-s-management"></i>
            {{row.databaseName}}
          </p>
          <el-col :span="12" v-for="(item,index) in  row.mapList" :key="index">
            <el-card class="box-cards">
              <div slot="header">
                <span>{{item.name}}</span>
              </div>
              <p class="item">
                是否配置：
                <span
                  v-if="item.detailMap.ifConfig=='已配置'"
                  style="color:#67C23A;"
                >{{item.detailMap.ifConfig}}</span>
                <span
                  v-else-if="item.detailMap.ifConfig=='未配置'"
                  style="color:#F56C6C;"
                >{{item.detailMap.ifConfig}}</span>
                <span v-else-if="item.detailMap.ifConfig=='不需配置'">{{item.detailMap.ifConfig}}</span>
              </p>
              <p class="item" v-if="item.name=='同步'">源库：{{item.detailMap.sourceDatabase}}</p>
              <p class="item" v-if="item.name=='同步'">源表：{{item.detailMap.sourceTable}}</p>
              <p class="item">任务状态：{{item.detailMap.taskState}}</p>
              <p class="item" v-if="item.name=='备份'">备份观测时间：{{item.detailMap.backupWatch}}</p>
              <p
                class="item"
                v-if="item.name=='清除'||item.name=='迁移'"
              >清除观测时间：{{item.detailMap.clearWatch}}</p>
              <p class="item" v-if="item.name!='同步'">执行耗时：{{item.detailMap.excuteTime}}</p>
              <el-popover placement="bottom" trigger="hover" popper-class="cornBox">
                <div>
                  <ul class="ulInfoBox" style="color:#67C23A;">
                    <span>下五次执行时间：</span>
                    <li v-for="(item_a,index_a) in item.cronDiv" :key="index_a">
                      <p class="rowInfo">
                        <span>{{item_a}}</span>
                      </p>
                    </li>
                  </ul>
                </div>
                <el-button type="text" slot="reference" class="cornText" v-if="item.name!='同步'">
                  <span>执行策略：</span>
                  <span style="color:#409EFF;">{{item.detailMap.excutePolicy}}</span>
                </el-button>
              </el-popover>
              <p class="item">
                操作：
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-circle-plus"
                  @click="addRow(row,item.name)"
                  title="添加"
                  v-if="item.detailMap.ifConfig=='未配置'"
                ></el-button>
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-row"
                  title="修改"
                  v-if="item.detailMap.ifConfig=='已配置'"
                  @click="editRow(item.detailMap,item.name, row.dataClassId,row.dataClassName)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-delete"
                  title="删除"
                  v-if="item.detailMap.ifConfig=='已配置'"
                  @click="deleteJob(item.detailMap,item.name,row.dataClassId,row.dataClassName)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-caret-right"
                  title="启动"
                  v-if="item.detailMap.ifConfig=='已配置'&&item.detailMap.taskState=='停止'"
                  @click="startJob(item.detailMap,item.name,row.dataClassId,row.dataClassName)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-video-play"
                  title="重启"
                  v-if="item.name=='同步'&&item.detailMap.ifConfig=='已配置'&&item.detailMap.taskState=='运行中'"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-video-pause"
                  title="停止"
                  @click="stopJob(item.detailMap,item.name,row.dataClassId,row.dataClassName)"
                  v-if="item.detailMap.ifConfig=='已配置'&&item.detailMap.taskState=='运行中'"
                ></el-button>
              </p>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
    <!-- 弹窗 树-->
    <el-dialog :title="dialogTitle" :visible.sync="TreeDialog">
      <handleTree v-if="TreeDialog" :handleTreeObj="handleTreeObj" @cancelDialog="cancelDialog"></handleTree>
    </el-dialog>

    <!-- 弹窗 字典-->
    <el-dialog :title="dialogTitle" :visible.sync="DictDialog">
      <handleDict v-if="DictDialog" :handleDictObj="handleDictObj" @cancelDialog="cancelDialog"></handleDict>
    </el-dialog>
  </div>
</template>
<script>
import handleTree from "@/views/dbDictMangement/fieldManagement/handleTree";
import handleDict from "@/views/dbDictMangement/fieldManagement/handleDict";
//分类树
export default {
  components: {
    handleTree,
    handleDict
  },
  data() {
    return {
      filterText: "",
      highlight: true,
      treeData: [],
      groupList: [],
      // defaultProps: {
      //   children: "children",
      //   label: "key_col"
      // },
      defaultProps: {
        children: "children",
        label: "label"
      },
      checkNode: {}, //选中高亮的节点
      expandedKeys: [], //展开的节点
      // 弹窗 树
      dialogTitle: "",
      TreeDialog: false,
      handleTreeObj: {},
      // 表格
      tableData: [],
      total: 0,
      queryParams: {
        key_col: "",
        pageNum: 1,
        pageSize: 10
      },
      // 弹窗 字典
      DictDialog: false,
      handleDictObj: {}
    };
  },
  created() {},
  watch: {
    filterText(val) {
      this.$refs.elTree.filter(val);
    }
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    addType() {
      this.dialogTitle = "添加管理字段分组";
      this.TreeDialog = true;
    },
    rowType() {},
    deleteAlert() {
      if (!this.checkNode.id) {
        this.$message({ message: "请选中需要删除的数据!", type: "warning" });
        return;
      }
      this.$confirm("数据和类型都会被删除，确认删除", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning"
      })
        .then(() => {
          this.rowData = this.checkNode;
          this.deleteFun();
        })
        .catch(() => {}); //一定别忘了这个
    },
    showSearch() {},
    handleNodeClick() {},
    cancelDialog(msg) {
      if (!msg) {
        this.TreeDialog = false;
        this.DictDialog = false;
      }
    },
    handleQuery() {},
    addRow() {
      this.dialogTitle = "新增字典";
      this.DictDialog = true;
    },
    editRow() {},
    rowRow() {},
    startJob() {},
    deleteJob() {},
    handleSelectionChange() {},
    getList() {},
    exportData() {
      // let url =
      //   interfaceObj.manageFieldApi_export +
      //   "?group_id=" +
      //   this.searchObj.group_id;
      // testExport(url);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    formatBoolean(row, column, cellValue) {
      var ret = ""; //你想在页面展示的值
      if (cellValue) {
        ret = "是"; //根据自己的需求设定
      } else {
        ret = "否";
      }
      return ret;
    }
  }
};
</script>
<style lang="scss">
.storePolicy {
  padding: 20px 5px 0 5px;
  .elAside {
    max-width: 250px;
    padding: 0;
    margin: 0 10px;
    background: #fff;
    border: 1px solid #ebeef5;
    overflow: hidden;
    box-sizing: border-box;
    height: calc(100vh - 130px);
    .filterText {
      display: block;
      width: 90%;
      margin: 12px auto;
    }
    .el-scrollbar {
      height: calc(100% - 80px);
    }
    .el-scrollbar__wrap {
      overflow: auto;
    }
  }
  .elMain {
    border: 1px solid #ebeef5;
    overflow: hidden;
  }
}
</style>
