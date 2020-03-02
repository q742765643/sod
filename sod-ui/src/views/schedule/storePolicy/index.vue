<template>
  <!-- 资料存储策略 -->
  <el-container class="app-container storePolicy">
    <!-- 左侧树 -->
    <el-aside class="elAside">
      <el-input size="small" placeholder="输入关键字进行过滤" v-model="filterText" class="filterText"></el-input>
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-tree
          class="el-tree"
          :highlight-current="highlight"
          node-key="id"
          :data="treeData"
          :props="defaultProps"
          :default-expanded-keys="expandedKeys"
          :filter-node-method="filterNode"
          @node-click="sourceNodeClick"
          ref="elTree"
        ></el-tree>
      </el-scrollbar>
    </el-aside>
    <el-main class="elMain" style="padding:0 0 20px 0;">
      <div class="headName">{{checkNode.name}}</div>
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-row v-for="(row,rowIndex) in groupList" :key="rowIndex" :gutter="18" class="cardBox">
          <p class="el-p">
            <i class="el-icon-s-management"></i>
            {{row.databaseName}}
          </p>
          <el-col :span="12" v-for="(item,index) in  row.policyDtos" :key="index">
            <el-card class="box-cards">
              <div slot="header">
                <span>{{item.policyName}}</span>
              </div>
              <p>
                是否配置：
                <span
                  v-if="item.isConfiguration=='已配置'"
                  style="color:#67C23A;"
                >{{item.isConfiguration}}</span>
                <span
                  v-else-if="item.isConfiguration=='未配置'"
                  style="color:#F56C6C;"
                >{{item.isConfiguration}}</span>
                <span v-else-if="item.isConfiguration=='不需配置'">{{item.isConfiguration}}</span>
              </p>
              <p v-if="item.policyName=='同步'">源库：{{item.sourceRepository}}</p>
              <p v-if="item.policyName=='同步'">源表：{{item.sourceTable}}</p>
              <p>任务状态：{{item.triggerStatus}}</p>
              <p v-if="item.policyName=='备份'">备份观测时间：{{item.ddateTime}}</p>
              <p v-if="item.policyName=='清除'||item.policyName=='迁移'">清除观测时间：{{item.ddateTime}}</p>
              <p v-if="item.policyName!='同步'">执行耗时：{{item.elapsedTime}}</p>
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
                <p type="text" slot="reference" class="cornText" v-if="item.policyName!='同步'">
                  <span>执行策略：</span>
                  <span style="color:#409EFF;">{{item.jobCron}}</span>
                </p>
              </el-popover>
              <p>
                操作：
                <el-button
                  size="mini"
                  type="success"
                  icon="el-icon-plus"
                  @click="addRow(row)"
                  title="添加"
                  v-if="item.isConfiguration=='未配置'"
                ></el-button>
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-row"
                  title="修改"
                  v-if="item.isConfiguration=='已配置'"
                  @click="editRow(row)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-delete"
                  title="删除"
                  v-if="item.isConfiguration=='已配置'"
                  @click="deleteJob(row)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-caret-right"
                  title="启动"
                  v-if="item.isConfiguration=='已配置'&&item.triggerStatus=='停止'"
                  @click="startJob(row)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-video-play"
                  title="重启"
                  v-if="item.policyName=='同步'&&item.isConfiguration=='已配置'&&item.triggerStatus=='运行中'"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-video-pause"
                  title="停止"
                  @click="stopJob(row)"
                  v-if="item.isConfiguration=='已配置'&&item.triggerStatus=='运行中'"
                ></el-button>
              </p>
            </el-card>
          </el-col>
        </el-row>
      </el-scrollbar>
    </el-main>
    <!-- 弹窗 清除-->
    <el-dialog :title="dialogTitle" :visible.sync="clearDialog" width="800px">
      <handleClear @cancelHandle="cancelHandle" v-if="clearDialog" :handleObj="handleObj"></handleClear>
    </el-dialog>
  </el-container>
</template>
<script>
import handleClear from "@/views/schedule/clear/handleClear";
import { strategyTree, findData } from "@/api/schedule/storePolicy";
export default {
  components: {
    handleClear
  },
  data() {
    return {
      filterText: "",
      highlight: true,
      treeData: [],
      groupList: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      checkNode: {}, //选中高亮的节点
      expandedKeys: [], //展开的节点
      myTreedata: [],
      checkedNodeArr: [],
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
      clearDialog: false,
      handleObj: {}
    };
  },
  async created() {
    await this.getTreeData();
  },
  watch: {
    filterText(val) {
      this.$refs.elTree.filter(val);
    }
  },
  methods: {
    async getTreeData() {
      await strategyTree().then(res => {
        res.success
          ? (this.treeData = res.data)
          : this.$message({
              type: "error",
              message: res.msg
            });
      });
      if (this.treeData.length != 0) {
        this.getDefaultNode(this.treeData[0]);
      }
      this.$refs.elTree.setCurrentKey(this.checkNode.id);
      this.sourceNodeClick(this.checkNode);
    },
    //获取默认选中的节点
    getDefaultNode(nodeArr) {
      if (nodeArr.children) {
        let newNodeArr = nodeArr.children[0];
        this.getDefaultNode(newNodeArr);
      } else {
        this.checkNode = nodeArr;
      }
      this.expandedKeys.push(nodeArr.id);
    },
    //节点点击
    sourceNodeClick(data) {
      this.myTreedata = [];
      this.checkedNodeArr = {};
      this.resetData(data);
      let treeIds = [];
      this.myTreedata.forEach(single => {
        if (single.disabled) {
          treeIds.push(single.id);
        }
      });
      console.log(this.checkNode);
      this.getfindData(this.checkNode.id);
    },
    getfindData(classId) {
      findData({ classId: classId }).then(res => {
        if (res.code == "200") {
          this.groupList = res.data;
          this.groupList = [
            {
              databaseName: "历史分析库_基础库",
              databaseId: "7f9b7480f9874cca942c0e3910ab714a",
              dataClassId: "D.0004.0001.M001",
              policyDtos: [
                {
                  taskId: "",
                  policyName: "备份",
                  jobCron: "",
                  isConfiguration: "未配置",
                  triggerStatus: "",
                  elapsedTime: "",
                  ddateTime: "",
                  sourceRepository: "",
                  sourceTable: null
                },
                {
                  taskId: "",
                  policyName: "清除",
                  jobCron: "",
                  isConfiguration: "未配置",
                  triggerStatus: "",
                  elapsedTime: "",
                  ddateTime: "",
                  sourceRepository: "",
                  sourceTable: null
                },
                {
                  taskId: "",
                  policyName: "迁移",
                  jobCron: "",
                  isConfiguration: "未配置",
                  triggerStatus: "",
                  elapsedTime: "",
                  ddateTime: "",
                  sourceRepository: "",
                  sourceTable: null
                },
                {
                  taskId: "",
                  policyName: "同步",
                  jobCron: "",
                  isConfiguration: "未配置",
                  triggerStatus: "",
                  elapsedTime: "",
                  ddateTime: "",
                  sourceRepository: "",
                  sourceTable: null
                }
              ]
            },
            {
              databaseName: "结构化数据库_基础库",
              databaseId: "f499a19d5d0743899dfe74ef724be865",
              dataClassId: "D.0004.0001.M001",
              policyDtos: []
            }
          ];
        } else {
        }
      });
    },
    //获取父节点下的子节点
    resetData(dataArr) {
      var that = this;
      that.myTreedata.push({
        label: dataArr.label,
        id: dataArr.id,
        disabled: dataArr.disabled
      });
      if (dataArr.children) {
        var child = dataArr.children;
        for (var i = 0; i < child.length; i++) {
          that.resetData(child[i]);
        }
      }
    },
    cancelDialog(msg) {
      if (!msg) {
        this.TreeDialog = false;
        this.DictDialog = false;
      }
    },

    addRow(row) {
      console.log(row);
      this.handleObj = {};
      this.dialogTitle = "添加迁移清除配置信息";
      this.clearDialog = true;
    },
    editRow() {},
    startJob() {},
    deleteJob() {},

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    cancelHandle() {
      this.clearDialog = false;
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
      overflow-x: hidden;
    }
    .el-tree {
      min-width: 100%;
      display: inline-block !important;
    }
  }
  .elMain {
    border: 1px solid #ebeef5;
    overflow: hidden;
    height: calc(100vh - 130px);
    .el-scrollbar {
      height: calc(100% - 40px);
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }
    .el-scrollbar__bar.is-horizontal {
      display: none;
    }
  }
  .headName {
    width: 100%;
    height: 44px;
    line-height: 44px;
    padding-left: 20px;
    background-color: #f4f4f5;
    color: #909399;
  }
  .el-card {
    height: 308px;
    margin-top: 20px;
  }
  .cardBox {
    padding: 0 20px;
  }
  .handleIcon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
    background: #67c23a;
    color: #fff;
    cursor: pointer;
    border-radius: 2px;
  }
}
</style>
