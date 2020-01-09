<template>
  <el-main class="gridEreaDefine">
    <!-- <legend>{{rowData.database_name}}</legend> -->
    <el-row class="areaTop">
      <el-button type="primary" size="small" @click="add">新增</el-button>
      <el-button type="primary" size="small" @click="deleteArea">删除</el-button>
    </el-row>
    <el-row v-show="isEdit" class="areaTop">
      <el-col :span="20">
        <el-select v-model="selectArea" placeholder="请选择区域" size="small" class="areaSelect">
          <el-option
            v-for="item in optionsArea"
            :key="item.value"
            :label="item.area_id"
            :value="item.area_id"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" @click="save">确认</el-button>
        <el-button type="primary" size="small" @click="cel">取消</el-button>
      </el-col>
    </el-row>

    <el-table
      :data="tableData"
      border
      stripe
      style="width: 100%;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="area_id" label="区域代码">
        <template slot-scope="scope">{{scope.row.area_id.split('[')[0]}}</template>
      </el-table-column>
      <el-table-column prop="area_region_desc" label="区域描述"></el-table-column>
    </el-table>
  </el-main>
</template>

<script>
export default {
  name: "gridEreaDefine",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      tableData: [],
      optionsArea: [],
      isEdit: false,
      multipleSelection: [],
      selectArea: ""
    };
  },

  methods: {
    searchFun() {
      this.axios
        .get(interfaceObj.TableStructure_getGridArea, {
          params: {
            data_class_id: this.rowData.data_class_id
          }
        })
        .then(res => {
          this.tableData = res.data.data;
        });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getoptionsArea() {
      this.axios
        .get(interfaceObj.TableStructure_getGridAreaDefineDirList)
        .then(res => {
          this.optionsArea = res.data.data;
        });
    },
    add() {
      this.isEdit = !this.isEdit;
    },
    cel() {
      this.isEdit = !this.isEdit;
    },
    save() {
      this.isEdit = !this.isEdit;
      let id = this.selectArea;
      let flagbegin = id.indexOf("[");
      let flagend = id.indexOf("]");
      let area_id = id.substring(0, flagbegin);
      let latdesc = "起止纬度：" + id.substring(flagbegin, flagend + 1);
      let londesc = "起止经度：" + id.substring(flagend + 1);
      let desc = latdesc + ";" + londesc;
      this.axios
        .post(interfaceObj.TableStructure_addGridArea, {
          data_class_id: this.rowData.data_class_id,
          area_id: this.selectArea,
          area_region_desc: desc
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.searchFun();
            this.$message({
              type: "success",
              message: "新增成功"
            });
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        });
    },
    deleteArea() {
      let ids = [];
      this.multipleSelection.forEach(element => {
        ids.push(element.record_id);
      });
      this.axios
        .post(interfaceObj.TableStructure_deleteGridArea, {
          record_ids: ids.join(",")
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.searchFun();
            this.$message({
              type: "success",
              message: "删除成功"
            });
          } else {
            this.$message({
              type: "error",
              message: res.data.returnMessage
            });
          }
        });
    },
    forParent() {
      this.searchFun();
      this.getoptionsArea();
    }
  },
  mounted() {}
};
</script>

<style lang="scss">
.gridEreaDefine {
  .areaSelect,
  .areaTop {
    margin-bottom: 10px;
  }
}
</style>
