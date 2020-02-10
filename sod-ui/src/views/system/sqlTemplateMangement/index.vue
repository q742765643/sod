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
        <el-col :span="4" class="colClass">
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
              <el-select
                v-model="handleSqlTemplate.databaseName"
                v-on:change="indexSelect()"
                size="small"
              >
                <el-option
                  v-for="item in databaseList"
                  :key="item.key_col"
                  :label="item.name_cn"
                  :value="`${item.key_col}`"
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
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/api/system/role";
import { initSqlList,getDBType,addSqlTem,delSqlTem,editSqlTem,getSqlTemById } from "@/api/system/sqlTemplateMangement";

export default {
  data() {
    return {
     // 遮罩层
      loading: true,
      queryParams: {
         pageNum: 1,
        pageSize: 10,
        user_fcst_ele:'',
      },
      sqlData:[],
      databaseList:[],
      // 弹窗
      dialogTitle:'新增模板',
      handleDailyVisible:false,
      handleSqlTemplate:{
        databaseName:'',
        template:'',
      }
    };
  },
  created() {
    this.getSqlList();
  },
  methods: {
    getSqlList(){
      initSqlList().then(
        response => {
          this.sqlData = response.data
        }
      );
    },
    editSqlTemplate(item){
      this.handleDailyVisible = true;
      this.handleSqlTemplate.template = item.template;
      this.handleSqlTemplate.databaseName = item.databaseName;
      this.dialogTitle = '编辑模板';
    },
    deleteSqlTemplate(item){
      this.$confirm('数据删除后将无法恢复，确认删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delSqlTem({ids:item.id}).then(response => {
            console.log(response);
        });
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
    },
    addSqlTemplate(){
      this.handleDailyVisible = true;
      this.dialogTitle = '新增模板';
    },
    autoFillIn(value){
      this.handleSqlTemplate.template+=value;
    },
    trueDialog(){
      //新增
      if(this.dialogTitle==='新增模板'){
        addSqlTem().then(response => {
          this.sqlData = response.data
        });
      }
      //编辑
      else{

      }
    },

    closeDialog(){
       this.handleDailyVisible = false;
    },
  }
};
</script>
<style rel="stylesheet/scss" lang="scss">
.sqlTemplateMangement {
  .addBox{
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
</style>

