<template>
  <div class="app-container export-container">
    <!-- 表结构导出 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据库：" prop="database_id">
        <el-select
          clearable
          v-model.trim="queryParams.database_id"
          size="small"
          style="width:400px"
          filterable
        >
          <el-option
            v-for="(item,index) in databaseList"
            :key="index"
            :label="'['+item.databaseClassify+']'+item.databaseDefine.databaseName+'--'+item.databaseName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="20">
      <el-col :span="16">
        <tree-transfer
          :title="treeTitle"
          :from_data="fromTreeData"
          :to_data="toTreeData"
          :defaultProps="defaultProps"
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
          <el-tabs v-model.trim="activeName" @tab-click="handleClick">
            <el-tab-pane label="表结构导出" name="first">
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
                <el-button type="success" icon="el-icon-bottom" size="small" @click="exportClick">导出</el-button>
              </div>
            </el-tab-pane>
            <!--  <el-tab-pane label="表结构导出--简版" name="second" style="display:none;">
              <p>
                <el-checkbox
                  :indeterminate="isIndeterminate"
                  v-model.trim="checkAll"
                  @change="handleCheckAllChange"
                >表结构</el-checkbox>
              </p>
              <el-checkbox-group
                v-model.trim="checkedSimple"
                @change="handleCheckedChange"
                class="simpleBox"
              >
                <el-checkbox
                  v-for="(item,index) in simples"
                  v-model.trim="item.value"
                  :label="item.value"
                  :key="index"
                >{{item.key}}</el-checkbox>
              </el-checkbox-group>
              <el-divider></el-divider>
              <p>
                <el-checkbox v-model.trim="simpleObj.index">索引</el-checkbox>
              </p>
              <el-divider></el-divider>
              <p>
                <el-checkbox v-model.trim="simpleObj.fenku">分库分表</el-checkbox>
              </p>
              <div style="text-align:right;">
                <el-button type="success" icon="el-icon-bottom" size="small" @click="exportClick">导出</el-button>
              </div>
            </el-tab-pane>-->
            <el-tab-pane label="SQL导出" name="third">
              <p>导出方式</p>
              <el-radio v-model.trim="exportTyoe" :label="1">导出到同一个文件</el-radio>
              <el-radio v-model.trim="exportTyoe" :label="2">导出到多个文件</el-radio>
              <div style="text-align:right;">
                <el-button type="success" icon="el-icon-bottom" size="small" @click="exportClick">导出</el-button>
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
import { dataClassAll } from "@/api/structureManagement/tableStructureManage/StructureClassify";
import {
  logicDefineList,
  databaseList,
  dataTree,
  exportTable,
  exportSQL,
  exportTableSimple,
  downloadTable,
} from "@/api/structureManagement/exportTable";
export default {
  components: {
    treeTransfer,
  },
  data() {
    return {
      //格式化tree数据
      defaultProps: {
        children: "children",
        label: "name",
      },
      // 遮罩层
      loading: true,
      queryParams: {
        database_id: "",
      },
      databaseList: [],
      // 穿梭框
      fromTreeData: [
        /* {
          id: "1",
          pid: 0,
          name: "一级 1",
          children: [
            {
              id: "1-1",
              pid: "1",
              name: "二级 1-1",
              children: []
            },
            {
              id: "1-2",
              pid: "1",
              name: "二级 1-2",
              children: [
                {
                  id: "1-2-1",
                  pid: "1-2",
                  children: [],
                  name: "二级 1-2-1"
                },
                {
                  id: "1-2-2",
                  pid: "1-2",
                  children: [
                    {
                      id: "1-3-1",
                      pid: "1-2-2",
                      name: "三级 1-3-1",
                      children: [
                        {
                          id: "1-4-1",
                          pid: "1-3-1",
                          name: "四级 1-4-1"
                        }
                      ]
                    }
                  ],
                  name: "二级 1-2-2"
                }
              ]
            }
          ]
        } */
      ],
      toTreeData: [],
      modeTree: "transfer",
      treeTitle: ["数据库资料信息", "选择后的资料"],
      // 表信息导出
      activeName: "first",
      exportTyoe: 1,
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
        fenku: false, //分库分表
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
        { key: "中文描述", value: "name_cn" },
      ],
      isIndeterminate: false,
      toIds: [],
    };
  },
  created() {
    this.getDatabaseList(); //获取数据库列表
  },
  methods: {
    getDatabaseList() {
      databaseList().then((response) => {
        this.databaseList = response.data;
      });
    },
    handleQuery() {
      this.toTreeData = [];
      dataTree(this.queryParams).then((response) => {
        this.resetData(response.data);
        this.fromTreeData = response.data;
      });
    },
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    resetData(dataArr) {
      for (var i in dataArr) {
        dataArr[i].pid = dataArr[i].parentId;
        this.resetData(dataArr[i].children);
      }
    },
    // 监听穿梭框组件添加
    addTreeInfo(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的        {keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      /* console.log("fromData:", fromData);
      console.log("toData:", toData);
      console.log("obj:", obj); */
      this.toTreeData = toData;
    },
    // 监听穿梭框组件移除
    removeTreeInfo(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      console.log("toData:", toData);
      this.toTreeData = toData;
    },
    handleClick() {},
    // 全选
    handleCheckAllChange(value) {
      if (value) {
        this.checkedSimple = [];
        this.simples.forEach((element) => {
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
    },
    resetDataTo(dataArr) {
      for (var i in dataArr) {
        if (dataArr[i].id.split(".").length == 4) {
          this.toIds.push(dataArr[i].id);
        }
        this.resetDataTo(dataArr[i].children);
      }
    },
    // 导出
    exportClick() {
      if (this.toTreeData.length == 0) {
        this.$message({
          showClose: true,
          message: "请选择要导出的数据",
          type: "error",
        });
        return;
      }
      this.toIds = [];
      let dataArr = this.toTreeData;
      this.resetDataTo(dataArr);
      this.handleExportObj = {};
      let obj = {};
      obj.data_class_ids = this.toIds.join(",");
      obj.database_id = this.queryParams.database_id;
      if (this.activeName == "first") {
        obj.use_id = this.queryParams.use_id;
        exportTable(obj).then((response) => {
          if (response.code == 200) {
            downloadTable({ filePath: response.data.filePath }).then((res) => {
              this.downloadfileCommon(res);
            });
          }
        });
      } else if (this.activeName == "second") {
        exportTableSimple(obj).then((response) => {
          if (response.code == 200) {
            downloadTable({ filePath: response.data.filePath }).then((res) => {
              this.downloadfileCommon(res);
            });
          }
        });
      } else if (this.activeName == "third") {
        this.$confirm("请确保已在【SQL建表】内保存待导出的SQL语句?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let obj = {};
            obj.data_class_ids = this.toIds.join(",");
            obj.database_id = this.queryParams.database_id;
            obj.exportType = this.exportTyoe;
            exportSQL(obj).then((response) => {
              if (response.code == 200) {
                downloadTable({ filePath: response.data.filePath }).then(
                  (res) => {
                    this.downloadfileCommon(res);
                  }
                );
              }
            });
          })
          .catch(() => {});
      }
    },
  },
};
</script>
<style lang="scss">
.export-container {
  .searchBox {
    margin-bottom: 24px;
  }
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
  .el-scrollbar {
    height: calc(100% - 80px);
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
}
</style>

