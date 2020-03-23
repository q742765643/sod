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
            v-for="(item,index) in optionsArea"
            :key="index"
            :label="item.areaId"
            :value="item.areaId"
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
      <el-table-column prop="areaId" label="区域代码" align="center"></el-table-column>
      <el-table-column prop="areaRegionDesc" label="区域描述" align="center"></el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="searchFun"
    />
  </el-main>
</template>

<script>
import {
  gridareaSave,
  gridareaList
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { defineList } from "@/api/GridDataDictionaryManagement/areaType";
export default {
  name: "gridEreaDefine",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: ""
      },
      total: 0,
      tableData: [],
      optionsArea: [],
      isEdit: false,
      multipleSelection: [],
      selectArea: ""
    };
  },

  methods: {
    searchFun() {
      console.log(this.queryParams);
      gridareaList(this.queryParams).then(res => {
        if (res.code == 200) {
          debugger;
          this.tableData = res.data.pageData;
          this.total = res.data.totalCount;
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getoptionsArea() {
      let obj = {
        pageNum: 1,
        pageSize: 10,
        areaId: ""
      };
      defineList(obj).then(response => {
        this.optionsArea = response.data.pageData;
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
      debugger;
      var time = new Date();
      let obj = {};
      obj.areaId = this.selectArea;
      this.optionsArea.forEach(element => {
        if (element.areaId == this.selectArea) {
          obj.areaRegionDesc = element.areaDesc;
        }
      });
      obj.dataClassId = this.rowData.DATA_CLASS_ID;
      console.log(obj);
      gridareaSave(obj).then(res => {
        if (res.code == 200) {
          this.searchFun();
          this.$message({
            type: "success",
            message: "新增成功"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
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
