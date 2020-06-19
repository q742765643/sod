<template>
  <div class="app-container sodDeploy">
    <!-- 查询条件 -->
    <el-form :inline="true" :model="queryParams" ref="modelForm" class="searchBox">
      <el-form-item label="功能名称">
        <el-input size="small" v-model.trim="queryParams.name"></el-input>
      </el-form-item>
      <el-form-item label="功能类型">
        <el-select size="small" v-model.trim="queryParams.type" @change="statusChange">
          <el-option label="全部" value="全部"></el-option>
          <el-option label="目录" value="0"></el-option>
          <el-option label="菜单" value="1"></el-option>
          <el-option label="按钮" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="searchFun" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" icon="el-icon-plus" @click="addCell">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>
    <!-- 列表 -->
    <el-table border :data="tableData" stripe style="width: 100%;" ref="sodTable">
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="功能名称" width="120px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="parentName" label="上级功能"></el-table-column>
      <el-table-column prop="resKey" label="功能KEY" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="resUrl" label="功能URL" width="300px" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="type" label="功能类型" width="100px">
        <template slot-scope="scope">
          <span v-if="scope.row.type=='0'">目录</span>
          <span v-if="scope.row.type=='1'">菜单</span>
          <span v-if="scope.row.type=='2'">按钮</span>
        </template>
      </el-table-column>
      <el-table-column prop="level" label="优先级" width="90px"></el-table-column>
      <el-table-column prop="description" label="描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="操作" width="80px">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-edit" @click="editCell(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="650px"
    >
      <handleSod @cancelHandle="cancelHandle" v-if="handleDialog" :handleObj="handleObj"></handleSod>
    </el-dialog>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// //分页组件
// import Pagination from "@/components/Pagination";
// 增加查看弹框
import handleSod from "@/views/userManagement/sodManagement/handleSod";

export default {
  name: "sodDeploy",
  components: {
    handleSod
  },
  data() {
    return {
      //查询条件
      queryParams: {
        name: "",
        type: "",
        sort: "name",
        order: "asc"
      },
      //表格
      tableData: [],
      dataTotal: 0,
      // 弹框
      handleDialog: false,
      dialogTitle: "新增",
      handleObj: {}
    };
  },
  created() {},
  mounted() {
    // this.searchFun();
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    // 审核状态发生改变
    statusChange() {
      this.searchFun("1");
    },
    //根据条件查询数据
    searchFun(searchPageNum) {
      if (searchPageNum) {
        this.$refs.pagination.paginateObj.page = 1;
      }
      let paginateObj = this.$refs.pagination.paginateObj;
      this.queryParams = { ...this.queryParams, ...paginateObj };
      this.axios
        .post(interfaceObj.resourcesManage_queryResources, this.queryParams)
        .then(res => {
          this.tableData = res.data.data;
          this.dataTotal = res.data.total;
        });
    },
    //分页事件
    paginationChange() {
      this.searchFun();
    },
    // 新增
    addCell() {
      this.handleObj = {};
      this.dialogTitle = "新增";
      this.handleDialog = true;
    },

    // 查看
    editCell(row) {
      this.dialogTitle = "编辑";
      this.handleObj = row;
      this.handleDialog = true;
    },

    // 删除
    deleteCell(row) {
      let chosedRows = this.$refs.sodTable.selection;
      console.log(chosedRows);
      if (chosedRows.length > 0) {
        let ids = [];
        chosedRows.forEach(item => {
          ids.push(item.id);
        });
        if (ids.length > 0 && ids.length == chosedRows.length) {
          this.$confirm("数据删除后将无法恢复，确认删除?", "温馨提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              this.axios
                .post(interfaceObj.resourcesManage_deleteResources, {
                  ids: ids.join(",")
                })
                .then(res => {
                  if (res.data.returnCode == 0) {
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
                  this.searchFun("1");
                });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消删除"
              });
            });
        } else {
          this.$message({
            type: "error",
            message: "超级管理员不能删除"
          });
        }
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    // 弹框取消
    cancelHandle(msgFormDialog) {
      this.handleObj = {};
      this.handleDialog = false;
      this.searchFun();
    }
  }
};
</script>

<style lang="scss">
.sodDeploy {
  .el-link.el-link--default {
    font-size: 12px;
    color: #409eff;
  }
}
</style>
