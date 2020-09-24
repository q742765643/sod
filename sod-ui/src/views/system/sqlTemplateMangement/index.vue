<template>
  <div class="app-container sqlTemplateMangement">
    <!-- SQL模板管理 -->
    <el-row>
      <el-col :span="4" v-for="(item,index) in sqlData" :key="index" :offset="0" class="colClass">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{item.databaseName}}</span>
          </div>
          <div style="padding: 12px;">
            <el-button type="primary" size="small" round @click="editSqlTemplate(item)">编辑</el-button>
            <el-button type="danger" size="small" round @click="deleteSqlTemplate(item)">删除</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" class="colClass addTemplate">
        <el-card>
          <div class="addBox" @click="addSqlTemplate()">
            <i class="el-icon-plus"></i>
            <h4 style="text-align:center;">点击添加</h4>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      class="sqlTemDialog"
      :title="dialogTitle"
      width="70%"
      :visible.sync="handleDailyVisible"
      @close="closeDialog"
    >
      <handleSQL @closeDialog="closeDialog" v-if="handleDailyVisible" :handleObj="handleObj"></handleSQL>
    </el-dialog>
  </div>
</template>

<script>
import handleSQL from "@/views/system/sqlTemplateMangement/handleSql";
import {
  getAllSqlList, //获取模板数据
  delTemplate, //删除模板
} from "@/api/system/sqlTemplateMangement";
export default {
  components: {
    handleSQL,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_fcst_ele: "",
      },
      sqlData: [
        {
          id: 65,
          databaseServer: "xugu",
          databaseName: "虚谷数据库",
          template: "${tableName}",
        },
      ],
      // 弹窗
      dialogTitle: "新增模板",
      handleDailyVisible: false,
      handleObj: {
        databaseServer: "",
        template: "",
      },
    };
  },
  created() {
    //获取sql模板
    this.getSqlData();
  },
  methods: {
    //获取sql模板
    getSqlData() {
      getAllSqlList().then((response) => {
        this.sqlData = response.data;
      });
    },
    //编辑sql模板
    editSqlTemplate(item) {
      this.dialogTitle = "编辑模板";
      this.handleDailyVisible = true;
      this.handleObj = item;
    },
    //删除数据库
    deleteSqlTemplate(item) {
      this.$confirm("您确定要删除吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delTemplate({ ids: item.id }).then((response) => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.getSqlData();
          });
        })
        .catch(() => {});
    },
    addSqlTemplate() {
      debugger;
      this.dialogTitle = "新增模板";
      this.handleObj = {};
      this.handleDailyVisible = true;
    },

    closeDialog() {
      this.getSqlData();
      this.handleDailyVisible = false;
    },
  },
};
</script>
<style rel="stylesheet/scss" lang="scss">
.sqlTemplateMangement {
  .addBox {
    cursor: pointer;
    i {
      text-align: center;
      display: block;
      font-size: 60px;
      margin-bottom: 17px;
      color: #999;
      margin-top: 6px;
    }
  }
  .clearfix {
    position: relative;
  }
  .clearfix:before {
    content: "";
    left: -9px;
    position: absolute;
    height: 18px;
    width: 2px;
    background: #66b1ff;
  }
  .colClass {
    padding: 5px;
  }
}
.sqlTemDialog {
  .rowBoxBtn {
    padding-top: 12px;
    .el-col {
      text-align: left;
      margin-bottom: 12px;
      .funButton {
        width: 98px;
      }
    }
  }
  .textClass {
    width: 100%;
    height: 254px;
    border: 1px solid #dcdfe6;
    margin-bottom: 20px;
    padding-right: 0;
  }
}
.addTemplate .el-card__body {
  padding: 4px;
}
</style>

