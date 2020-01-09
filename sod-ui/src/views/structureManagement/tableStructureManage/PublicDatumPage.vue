<template>
  <section class="PublicDatumPage">
    <div class="treeTitle">
      <i class="el-icon-s-home"></i>
      {{this.handleObj.name+'（四级编码:'+this.handleObj.id+'）'}}
    </div>
    <div class="tableCon" v-show="tableShow">
      <p>没有与此公共元数据对应的表结构信息</p>
      <el-button type="primary" plain @click="addTableMsg">定义表结构信息</el-button>
    </div>
    <div class="tableCon" v-show="!tableShow">
      <el-radio-group
        size="small"
        v-model="tabPosition"
        v-for="(item,index) in publicTopList"
        :key="index"
        style="margin-bottom:10px;"
        @change="changeRadioBtn"
      >
        <el-radio-button :label="item.logic_id" style="margin-right:10px;">{{item.logic_name}}</el-radio-button>
      </el-radio-group>
      <el-table
        :data="tableDataParent"
        highlight-current-row
        @current-change="handleCurrentChange"
        border
      >
        <el-table-column prop="storage_name" label="存储名称"></el-table-column>
        <el-table-column prop="storage_tablename" label="存储表名"></el-table-column>
        <el-table-column prop="storage_code" label="存储编码"></el-table-column>
        <el-table-column prop="storage_metadatacode" label="公共元数据编码"></el-table-column>
        <el-table-column label="表描述">{{'查看'}}</el-table-column>
      </el-table>
      <div class="childTableBox" v-show="childShow">
        <el-table :data="tableDataChild" border>
          <el-table-column prop="db_ele_code" label="公共元数据字段"></el-table-column>
          <el-table-column prop="c_element_code" label="字段名称"></el-table-column>
          <el-table-column prop="user_ele_code" label="服务名称"></el-table-column>
          <el-table-column prop="ele_name" label="中文简称"></el-table-column>
          <el-table-column prop="type" label="数据类型"></el-table-column>
          <el-table-column prop="accuracy" label="数据精度"></el-table-column>
          <el-table-column prop="unit" label="要素单位(英文)"></el-table-column>
          <el-table-column prop="unit_cn" label="要素单位(中文)"></el-table-column>
          <el-table-column prop="is_null" label="是否可空"></el-table-column>
        </el-table>
        <el-table :data="tableIndexChild" border>
          <el-table-column prop="index_name" label="索引名称"></el-table-column>
          <el-table-column prop="index_column" label="索引列集合"></el-table-column>
          <el-table-column prop="index_type" label="索引类型"></el-table-column>
        </el-table>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "PublicDatumPage",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      publicTopList: ["Analysis", "Caching", "Observation", "Product"],
      tabPosition: "",
      tableDataParent: [],
      tableDataChild: [],
      tableIndexChild: [],
      childShow: false,
      tableShow: true
    };
  },
  created() {
    // this.initRadioGroup();
    // this.initTable();
  },
  methods: {
    initRadioGroup(paramsObj) {
      this.axios
        .get(interfaceObj.TableStructure_getLogicList, {
          params: {
            c_datum_code: paramsObj.id
          }
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            if (res.data.data.length > 0) {
              this.publicTopList = res.data.data;
              this.tabPosition = res.data.data[0].logic_id;
              this.initTable();
              this.tableShow = false;
            } else {
              this.tableShow = true;
            }
          }
        })
        .catch(error => {
          console.log("出错了，出错信息：" + error);
        });
    },
    initTable() {
      this.axios
        .get(interfaceObj.TableStructure_toCreateTable, {
          params: {
            base_type: this.tabPosition,
            c_datum_code: this.handleObj.id
          }
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.tableDataParent = res.data.rows;
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
    handleCurrentChange(val) {
      // 获取字段表格
      this.axios
        .get(interfaceObj.TableStructure_getColumnInfo, {
          params: { table_id: val.table_id }
        })
        .then(res => {
          this.tableDataChild = res.data.data;
        })
        .catch(error => {});
      // 获取索引表格
      this.axios
        .get(interfaceObj.TableStructure_getTableIndex, {
          params: { table_id: val.table_id }
        })
        .then(res => {
          this.tableIndexChild = res.data.data;
        })
        .catch(error => {});
      this.childShow = true;
    },
    changeRadioBtn(val) {
      this.initTable();
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
