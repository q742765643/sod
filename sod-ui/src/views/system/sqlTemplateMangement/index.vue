<template>
  <div class="app-container sqlTemplateMangement">
    <!-- SQL模板管理 -->
    <el-row>
      <el-col :span="4" v-for="(item,index) in sqlData" :key="index" :offset="0" class="colClass">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{item.databaseName}}</span>
          </div>
          <div style="padding: 14px;">
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
      class="sqlTemDialog"
      :title="dialogTitle"
      width="70%"
      :visible.sync="handleDailyVisible"
      append-to-body
      @close="closeDialog"
    >
      <el-row>
        <el-col :span="9">
          <el-form :inline="true">
            <el-form-item label="数据库厂商：">
              <el-select v-model="handleSqlTemplate.databaseServer" size="small">
                <el-option
                  v-for="item in databaseList"
                  :key="item.keyCol"
                  :label="item.nameCn"
                  :value="`${item.keyCol}`"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <el-row class="rowBoxBtn">
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${tableName}')"
                class="funButton"
              >表名称</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${databaseName}')"
                class="funButton"
              >库名称</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${columnInfo}')"
                class="funButton"
              >字段信息</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${primaryColumn}')"
                class="funButton"
              >主键字段</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${tableDescription}')"
                class="funButton"
              >索引字段</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${indexColumn}')"
                class="funButton"
              >表描描述</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${DPartitionKey}')"
                class="funButton"
              >分库键</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                size="medium"
                type="primary"
                @click="autoFillIn('${TPartitionKey}')"
                class="funButton"
              >分表键</el-button>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="15">
          <textarea v-model="handleSqlTemplate.template" class="textClass" id="addtextareaId"></textarea>
        </el-col>
      </el-row>
      <div class="dialog-footer" slot="footer">
        <el-button type="primary" class="funButton" @click="trueDialog()">确定</el-button>
        <el-button class="funButton" @click="closeDialog()">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAllSqlList, //获取模板数据
  getDBtype, //获取数据库厂商
  saveSqlTemplate, //添加模板
  isAlreadyTem, //是否已存在模板
  delTemplate, //删除模板
  editTemplate //编辑模板
} from "@/api/system/sqlTemplateMangement";
export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        user_fcst_ele: ""
      },
      sqlData: [
        {
          id: 65,
          databaseServer: "xugu",
          databaseName: "虚谷数据库",
          template: "${tableName}"
        }
      ],
      databaseList: [], //数据类型
      // 弹窗
      dialogTitle: "新增模板",
      handleDailyVisible: false,
      handleSqlTemplate: {
        databaseServer: "",
        template: ""
      }
    };
  },
  created() {
    //获取sql模板
    this.getSqlData();
    //获取数据库类型
    this.getDBList();
  },
  methods: {
    //获取sql模板
    getSqlData() {
      getAllSqlList().then(response => {
        this.sqlData = response.data;
      });
    },
    //获取数据库厂商
    getDBList() {
      getDBtype().then(response => {
        this.databaseList = response.data;
      });
    },
    //编辑sql模板
    editSqlTemplate(item) {
      this.dialogTitle = "编辑模板";
      this.handleDailyVisible = true;
      this.handleSqlTemplate = item;
    },
    //删除数据库
    deleteSqlTemplate(item) {
      this.$confirm("您确定要删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delTemplate({ ids: item.id }).then(response => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.getSqlData();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    addSqlTemplate() {
      this.dialogTitle = "新增模板";
      this.handleDailyVisible = true;
    },
    autoFillIn(value) {
      this.handleSqlTemplate.template += value;
    },
    trueDialog() {
      isAlreadyTem({
        databaseServer: this.handleSqlTemplate.databaseServer
      }).then(response => {
        //已存在不可添加
        if (response.data.length >= 1) {
          this.$message({
            type: "warning",
            message: "此数据库已经添加，不可以重复添加"
          });
        }
        //添加
        else {
          //新增
          if (this.dialogTitle == "新增模板") {
            saveSqlTemplate(this.handleSqlTemplate).then(response => {
              if (response.code === 200) {
                this.$message({
                  type: "success",
                  message: "新增成功"
                });
                this.handleDailyVisible = false;
                this.getSqlData();
              }
            });
          }
          //编辑
          else {
            editTemplate(this.handleSqlTemplate).then(response => {
              if (response.code === 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.handleDailyVisible = false;
                this.getSqlData();
              }
            });
          }
        }
      });
    },
    closeDialog() {
      this.handleDailyVisible = false;
    }
  }
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
    padding: 0px 50px;
    .el-col {
      text-align: center;
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

