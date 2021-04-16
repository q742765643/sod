<template>
  <el-main class="gridEreaDefine">
    <!-- <legend>{{rowData.database_name}}</legend> -->
    <el-row class="areaTop">
      <el-button type="primary" size="small" @click="add">新增</el-button>
      <el-button type="primary" size="small" @click="deleteArea">删除</el-button>
    </el-row>
    <el-row v-show="isEdit" class="areaTop">
      <el-col :span="20">
        <el-select v-model.trim="selectArea" placeholder="请选择区域" size="small" class="areaSelect">
          <el-option
            v-for="(item,index) in optionsArea"
            :key="index"
            :label="item.areaId+'：起止纬度：['+item.startLat+','+item.endLat+'];'+'起止经度：['+item.startLon+','+item.endLon+']'"
            :value="item.areaId+'&起止纬度：['+item.startLat+','+item.endLat+'];'+'起止经度：['+item.startLon+','+item.endLon+']'"
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
      <el-table-column type="index" label="序号" width="50"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="areaId" label="区域代码"></el-table-column>
      <el-table-column prop="areaRegionDesc" label="区域描述">
        <template slot-scope="scope">
          <span v-if="scope.row.areaRegionDesc">{{scope.row.areaRegionDesc}}</span>
        </template>
      </el-table-column>
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
  gridareaList,
  findByDataServiceId,
  allArea,
  gridareaDel,
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  name: "gridEreaDefine",
  props: { rowData: Object },
  components: {},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataServiceId: this.rowData.DATA_CLASS_ID,
      },
      total: 0,
      tableData: [],
      optionsArea: [],
      isEdit: false,
      multipleSelection: [],
      selectArea: "",
    };
  },

  methods: {
    searchFun() {
      console.log(this.queryParams);
      gridareaList(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data.pageData;
          this.total = res.data.totalCount;
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getoptionsArea() {
      allArea().then((response) => {
        this.optionsArea = response.data;
      });
    },
    add() {
      this.isEdit = !this.isEdit;
    },
    cel() {
      this.isEdit = !this.isEdit;
    },
    save() {
      if (!this.selectArea) {
        this.$message({
          message: "请选择一条数据新增",
          type: "error",
        });
        return;
      }
      this.isEdit = !this.isEdit;
      var time = new Date();
      let obj = {};
      obj.areaId = this.selectArea.split("&")[0];
      obj.areaRegionDesc = this.selectArea.split("&")[1];
      obj.dataServiceId = this.rowData.DATA_CLASS_ID;
      console.log(obj);
      gridareaSave(obj).then((res) => {
        if (res.code == 200) {
          this.searchFun();
          this.$message({
            type: "success",
            message: "新增成功",
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error",
          });
        }
      });
    },
    deleteArea() {
      let ids = [];
      let areaIds = [];
      this.multipleSelection.forEach((element) => {
        ids.push(element.id);
        areaIds.push(element.areaId);
      });
      if (ids.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      }
      this.$confirm("确认删除" + areaIds.join(",") + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          gridareaDel({ ids: ids.join(",") }).then((res) => {
            if (res.code == 200) {
              this.searchFun();
              this.$message({
                type: "success",
                message: "删除成功",
              });
            } else {
              this.$message({
                message: res.msg,
                type: "error",
              });
            }
          });
        })
        .catch(() => {});
    },
    forParent() {
      this.searchFun();
      this.getoptionsArea();
    },
  },
  mounted() {},
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
