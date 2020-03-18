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
                    <li v-for="(item_a,index_a) in item.nextTime" :key="index_a">
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
                  @click="addRow(row,item.policyName)"
                  title="添加"
                  v-if="item.isConfiguration=='未配置'"
                ></el-button>
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  title="修改"
                  v-if="item.isConfiguration=='已配置'"
                  @click="editRow(item,item.policyName)"
                ></el-button>
                <el-button
                  size="mini"
                  type="danger"
                  icon="el-icon-delete"
                  title="删除"
                  v-if="item.isConfiguration=='已配置'"
                  @click="deleteJob(item,item.policyName)"
                ></el-button>
                <el-button
                  size="mini"
                  type="warning"
                  icon="el-icon-caret-right"
                  title="启动"
                  v-if="item.isConfiguration=='已配置'&&item.triggerStatus=='停止'"
                  @click="startJob(row,item.policyName)"
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
                  @click="stopJob(row,item.policyName)"
                  v-if="item.isConfiguration=='已配置'&&item.triggerStatus=='运行中'"
                ></el-button>
              </p>
            </el-card>
          </el-col>
        </el-row>
      </el-scrollbar>
    </el-main>
    <!-- 弹窗 备份 -->
    <el-dialog :title="dialogTitle" :visible.sync="backUpDialog" width="800px" v-dialogDrag>
      <handleBackUp @cancelHandle="cancelHandle" v-if="backUpDialog" :handleObj="handleObj"></handleBackUp>
    </el-dialog>
    <!-- 弹窗 清除-->
    <el-dialog :title="dialogTitle" :visible.sync="clearDialog" width="800px" v-dialogDrag>
      <handleClear @cancelHandle="cancelHandle" v-if="clearDialog" :handleObj="handleObj"></handleClear>
    </el-dialog>
    <!-- 弹窗 迁移 -->
    <el-dialog :title="dialogTitle" :visible.sync="moveDialog" width="800px" v-dialogDrag>
      <handleMove @cancelHandle="cancelHandle" v-if="moveDialog" :handleObj="handleObj"></handleMove>
    </el-dialog>
  </el-container>
</template>
<script>
import handleClear from "@/views/schedule/clear/handleClear";
import handleBackUp from "@/views/schedule/backup/handleBackUp";
import handleMove from "@/views/schedule/move/handleMove";
import { strategyTree, findData } from "@/api/schedule/storePolicy";
import { delBackup } from "@/api/schedule/backup/backup";
import { delClear } from "@/api/schedule/clear/clear";
import { delMove } from "@/api/schedule/move/move";
export default {
  components: {
    handleBackUp,
    handleClear,
    handleMove
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
      backUpDialog: false,
      moveDialog: false,
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
      console.log(data);
      this.checkNode = data;
      this.getfindData(data.id);
    },
    getfindData(classId) {
      findData({ classId: classId }).then(res => {
        if (res.code == "200") {
          this.groupList = res.data;
        } else {
          this.msgError(res.msg);
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

    addRow(row, name) {
      console.log(row);
      this.handleObj = {};
      this.handleObj = row;
      this.handleObj.pageName = "资料存储策略";
      if (name == "备份") {
        this.dialogTitle = "添加数据备份配置信息";
        this.backUpDialog = true;
      } else if (name == "清除") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.clearDialog = true;
      } else if (name == "迁移") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.moveDialog = true;
      } else if (name == "同步") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.clearDialog = true;
      }
    },
    editRow(item, name) {
      console.log(item);
      this.handleObj = {};
      this.handleObj.id = item.taskId;
      if (name == "备份") {
        this.dialogTitle = "添加数据备份配置信息";
        this.backUpDialog = true;
      } else if (name == "清除") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.clearDialog = true;
      } else if (name == "迁移") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.moveDialog = true;
      } else if (name == "同步") {
        this.dialogTitle = "添加迁移清除配置信息";
        this.clearDialog = true;
      }
    },
    startJob() {},
    deleteJob(item, name) {
      this.$confirm(
        '是否确认删除任务编号为"' + item.taskId + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          if (name == "备份") {
            return delBackup(item.taskId);
          } else if (name == "清除") {
            return delClear(item.taskId);
          } else if (name == "迁移") {
            return delMove(item.taskId);
          } else if (name == "同步") {
          }
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    cancelHandle() {
      this.backUpDialog = false;
      this.clearDialog = false;
      this.moveDialog = false;
      this.getfindData(this.checkNode.id);
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
