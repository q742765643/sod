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
        <tree-transfer
          :title="treeTitle"
          :from_data="fromTreeData"
          :to_data="toTreeData"
          :defaultProps="{label:'label'}"
          @addBtn="addTreeInfo"
          @removeBtn="removeTreeInfo"
          :mode="modeTree"
          height="540px"
          filter
          openAll
        ></tree-transfer>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>表信息导出</span>
          </div>
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="表结构导出--完整版" name="first">
              <p>表结构</p>
              <el-row>
                <el-col :span="12">
                  <span>序号</span>
                </el-col>
                <el-col :span="12">
                  <span>要素名称</span>
                </el-col>
                <el-col :span="12">
                  <span>字段编码</span>
                </el-col>
                <el-col :span="12">
                  <span>数据类型</span>
                </el-col>
                <el-col :span="12">
                  <span>约束</span>
                </el-col>
                <el-col :span="12">
                  <span>说明</span>
                </el-col>
              </el-row>
              <el-divider></el-divider>
              <p>索引</p>
              <el-divider></el-divider>
              <p>分库分表</p>
              <div style="text-align:right;">
                <el-button icon="el-icon-bottom">导出</el-button>
              </div>
            </el-tab-pane>
            <el-tab-pane label="表结构导出--简版" name="second">
              <p>
                <el-checkbox
                  :indeterminate="isIndeterminate"
                  v-model="checkAll"
                  @change="handleCheckAllChange"
                >表结构</el-checkbox>
              </p>
              <el-checkbox-group
                v-model="checkedSimple"
                @change="handleCheckedChange"
                class="simpleBox"
              >
                <el-checkbox
                  v-for="(item,index) in simples"
                  v-model="item.value"
                  :label="item.value"
                  :key="index"
                >{{item.key}}</el-checkbox>
              </el-checkbox-group>
              <el-divider></el-divider>
              <p>
                <el-checkbox v-model="simpleObj.index">索引</el-checkbox>
              </p>
              <el-divider></el-divider>
              <p>
                <el-checkbox v-model="simpleObj.fenku">分库分表</el-checkbox>
              </p>
              <div style="text-align:right;">
                <el-button icon="el-icon-bottom">导出</el-button>
              </div>
            </el-tab-pane>
            <el-tab-pane label="sql导出" name="third">
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
import treeTransfer from "el-tree-transfer"; // 引入

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
        logic_id: ""
      },
      logicList: [],
      databaseList: [],
      // 穿梭框
      fromTreeData: [
        {
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
        }
      ],
      toTreeData: [],
      modeTree: "transfer",
      treeTitle: ["数据库资料信息", "选择后的资料"],
      // 表信息导出
      activeName: "first",
      exportTyoe: "1",
      // 简版
      simpleObj: {
        db_ele_code: false, //公共元数据字段
        c_element_code: false, //字段名称
        is_manager: false, //是否管理字段
        user_ele_code: false, // 服务名称
        ele_name: false, //中文简称
        type: false, //数据类型
        accuracy: false, //数据精度
        unit_cn: false, //要素单位（中文）
        is_update: false, //是否可改
        is_null: false, //是否可空
        is_premary_key: false, //是否主键
        is_show: false, //是否显示
        serial_number: false, //序号
        name_cn: false, //中文描述
        index: false, //索引
        fenku: false //分库分表
      },
      checkAll: false,
      checkedSimple: [],
      simples: [
        { key: "公共元数据字段", value: "db_ele_code" },
        { key: "字段名称", value: "c_element_code" },
        { key: "是否管理字段", value: "is_manager" },
        { key: "服务名称", value: "user_ele_code" },
        { key: "中文简称", value: "ele_name" },
        { key: "数据类型", value: "type" },
        { key: "数据精度", value: "accuracy" },
        { key: "要素单位（中文）", value: "unit_cn" },
        { key: "是否可改", value: "is_update" },
        { key: "是否可空", value: "is_null" },
        { key: "是否主键", value: "is_premary_key" },
        { key: "是否显示", value: "is_show" },
        { key: "序号", value: "serial_number" },
        { key: "中文描述", value: "name_cn" }
      ],
      isIndeterminate: false
    };
  },
  created() {},
  methods: {
    handleQuery() {},
    // 监听穿梭框组件添加
    addTreeInfo(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的        {keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      console.log("fromData:", fromData);
      console.log("toData:", toData);
      console.log("obj:", obj);
    },
    // 监听穿梭框组件移除
    removeTreeInfo(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      console.log("fromData:", fromData);
    },
    handleClick() {},
    // 全选
    handleCheckAllChange(value) {
      if (value) {
        this.checkedSimple = [];
        this.simples.forEach(element => {
          this.checkedSimple.push(element.value);
        });
      } else {
        this.checkedSimple = [];
      }
      // let checkedCount = value.length;
      // this.checkAll = checkedCount === this.simples.length;
      // this.isIndeterminate =
      //   checkedCount > 0 && checkedCount < this.simples.length;
    },
    // 单选
    handleCheckedChange(val) {
      if (val.length == this.simples.length) {
        this.isIndeterminate = true;
      } else {
        this.isIndeterminate = false;
      }
    }
  }
};
</script>
<style lang="scss">
.export-container {
  .transfer-left,
  .transfer-right {
    width: 45% !important;
  }
  .el-card {
    height: 540px;
    .el-card__header {
      background-color: #f5f7fa;
    }
    p {
      font-weight: bold;
    }
    .el-tab-pane {
      font-size: 14px;
      .el-col {
        padding-bottom: 4px;
      }
      .el-radio {
        display: block;
        margin: 12px 0 0 100px;
      }
      .simpleBox {
        .el-checkbox {
          width: 40%;
        }
      }
    }
  }
}
</style>

