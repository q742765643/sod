<template>
  <div class="app-container export-container">
    <!-- 表结构导出 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
     <el-form-item label="数据库用途：">
          <el-select v-model="queryParams.logic_id" size="small" @change="handleQuery">
            <el-option
              v-for="(item,index) in logicList"
              :key="index"
              :label="item.logic_name"
              :value="item.logic_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库：">
          <el-select v-model="queryParams.databaseId" size="small" @change="handleQuery">
            <el-option
              v-for="(item,index) in databaseList"
              :key="index"
              :label="item.database_name"
              :value="item.database_id"
            ></el-option>
          </el-select>
        </el-form-item>
    </el-form>
    <el-row :gutter="20">
      <el-col :span="16">
        <tree-transfer :title="treeTitle" :from_data='fromTreeData' :to_data='toTreeData' :defaultProps="{label:'label'}" @addBtn='addTreeInfo' @removeBtn='removeTreeInfo' :mode='modeTree' height='540px' filter openAll>
        </tree-transfer>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>表信息导出</span>
          </div>
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="表结构导出" name="first">
              <p>表结构</p>
              <el-row>
                <el-col :span="12"><span>序号</span></el-col>
                <el-col :span="12"><span>要素名称</span></el-col>
                <el-col :span="12"><span>字段编码</span></el-col>
                <el-col :span="12"><span>数据类型</span></el-col>
                <el-col :span="12"><span>约束</span></el-col>
                <el-col :span="12"><span>说明</span></el-col>
              </el-row>
              <el-divider></el-divider>
              <p>索引</p>
              <el-divider></el-divider>
              <p>分库分表</p>
              <div style="text-align:right;">
                <el-button icon="el-icon-bottom">导出</el-button>
              </div>
              
            </el-tab-pane>
            <el-tab-pane label="sql导出" name="second">
                <p>导出方式</p>
                <el-radio v-model="exportTyoe" label="1">导出到同一个文件</el-radio>
                <el-radio v-model="exportTyoe" label="2">导出到多个文件</el-radio>
                <div style="text-align:right;">
                  <el-button icon="el-icon-bottom">导出</el-button>
                </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    
  </div>
</template>
<script>
import treeTransfer from 'el-tree-transfer' // 引入
export default {
  components: {
    treeTransfer
  },
  data() {
    return {
     // 遮罩层
      loading: true,
      queryParams: {
         pageNum: 1,
        pageSize: 10,
        databaseId: "",
        logic_id: "",
      },
      logicList:[],
      databaseList:[],
      // 穿梭框
      fromTreeData:[{
            id: "1",
            pid: 0,
            label: "一级 1",
            children: [
              {
                id: "1-1",
                pid: "1",
                label: "二级 1-1",
                children: []
              },
              {
                id: "1-2",
                pid: "1",
                label: "二级 1-2",
                children: [
                  {
                    id: "1-2-1",
                    pid: "1-2",
                    children: [],
                    label: "二级 1-2-1"
                  },
                  {
                    id: "1-2-2",
                    pid: "1-2",
                    children: [],
                    label: "二级 1-2-2"
                  }
                ]
              }
            ]
          },],
      toTreeData:[],
      modeTree:"transfer", 
      treeTitle:['数据库资料信息','选择后的资料'],
      // 表信息导出
      activeName:'first',
      exportTyoe:'1'
    };
  },
  created() {
  },
  methods: {
     handleQuery(){},
      // 监听穿梭框组件添加
      addTreeInfo(fromData,toData,obj){
        // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的        {keys,nodes,halfKeys,halfNodes}对象
        // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
        console.log("fromData:", fromData);
        console.log("toData:", toData);
        console.log("obj:", obj);
      },
      // 监听穿梭框组件移除
      removeTreeInfo(fromData,toData,obj){
        // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
        // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
        console.log("fromData:", fromData);},
      handleClick(){

      }
    
  }
};
</script>
<style lang="scss">
.export-container{

.transfer-left,.transfer-right{
  width:45% !important;
}
.el-card{
  height:540px;
  .el-card__header{
      background-color: #f5f7fa;
}
p{
  font-weight: bold
}
.el-tab-pane{
  font-size: 14px;
  .el-col{
    padding-bottom:4px;
  }
  .el-radio{
    display: block;
    margin: 12px 0 0 100px;
  }
}
}
}

</style>

