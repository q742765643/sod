<template>
  <section class="PublicDatumPage">
    <div class="treeTitle">
      <i class="el-icon-s-home"></i>
      {{this.handleObj.name+'（四级编码:'+this.handleObj.id+'）'}}
    </div>
    <div class="tableCon" v-show="!tableShow">
      <p>没有与此公共元数据对应的表结构信息</p>
      <el-button type="primary" plain @click="addTableMsg">定义表结构信息</el-button>
    </div>
    <div class="tableCon" v-show="tableShow">
      <el-radio-group
        size="small"
        v-model.trim="tabPosition"
        v-for="(item,index) in publicTopList"
        :key="index"
        style="margin-bottom:10px;"
        @change="changeRadioBtn(item.ID)"
      >
        <el-radio-button :label="item.LOGIC_NAME" :value="item.ID" style="margin-right:10px;"></el-radio-button>
      </el-radio-group>
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-table
          :data="tableDataParent"
          highlight-current-row
          @current-change="handleCurrentChange"
          border
        >
          <el-table-column prop="nameCn" label="存储名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="tableName" label="存储表名"></el-table-column>
          <el-table-column prop="dataServiceId" label="存储编码"></el-table-column>
          <el-table-column prop="storage_metadatacode" label="公共元数据编码">{{this.handleObj.id}}</el-table-column>
          <el-table-column label="表描述" width="100">
            <el-link type="primary" :underline="false">查看</el-link>
          </el-table-column>
        </el-table>
        <div class="childTableBox" v-show="childShow">
          <el-table border :data="tableDataChild">
            <el-table-column
              label="公共元数据字段"
              prop="dbEleCode"
              width="200px"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              label="字段编码"
              prop="celementCode"
              width="200px"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              label="服务代码"
              prop="userEleCode"
              width="200px"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column label="中文简称" prop="eleName" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="数据类型" prop="type" width="100px" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="数据精度" prop="accuracy" width="100px"></el-table-column>
            <el-table-column label="要素单位" prop="unitCn" width="100px"></el-table-column>
          </el-table>
          <el-table border :data="tableIndexChild">
            <el-table-column label="索引名称" prop="indexName"></el-table-column>
            <el-table-column label="索引列" prop="indexColumn"></el-table-column>
            <el-table-column label="索引类型" prop="indexType"></el-table-column>
          </el-table>
        </div>
      </el-scrollbar>
    </div>
  </section>
</template>

<script>
import {
  getLogicByDdataId,
  dataTableGcl
} from "@/api/structureManagement/tableStructureManage/index";
export default {
  name: "PublicDatumPage",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      publicTopList: [],
      tabPosition: "",
      tableDataParent: [],
      tableDataChild: [],
      tableIndexChild: [],
      childShow: false,
      tableShow: true
    };
  },
  created() {
    // this.initTable();
  },
  methods: {
    initRadioGroup(paramsObj) {
      console.log(paramsObj);
      getLogicByDdataId({ dDataId: paramsObj.id }).then(res => {
        if (res.code == 200) {
          if (res.data.length > 0) {
            this.publicTopList = res.data;
            this.tabPosition = res.data[0].LOGIC_NAME;
            console.log(this.tabPosition);
            this.changeRadioBtn(res.data[0].ID);
            this.tableShow = true;
            this.childShow = false;
          } else {
            this.tableShow = false;
          }
        }
      });
    },
    initTable(val) {
      console.log(val);
      dataTableGcl({ classLogic: val })
        .then(res => {
          if (res.code == 200) {
            this.tableDataParent = res.data;
          }
        })
        .catch(error => {
          console.log("出错了，出错信息：" + error);
        });
    },
    addTableMsg() {
      this.$emit("showMaterialSingle", "新增资料");
    },
    // 点击行，查看表格
    handleCurrentChange(row) {
      // 获取字段表row格
      this.tableDataChild = row.columns;
      this.tableIndexChild = row.tableIndexList;
      this.childShow = true;
    },
    changeRadioBtn(val) {
      this.initTable(val);
    }
  }
};
</script>

<style lang="scss">
.PublicDatumPage {
  .tableCon {
    text-align: center;
    p {
      padding: 10px;
    }
  }
  .el-table {
    margin-bottom: 10px;
  }
}
</style>
