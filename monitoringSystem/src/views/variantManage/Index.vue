<template>
  <!-- 变量管理 -->
  <el-main class="roleManage">
    <section class="roleOperate">
      <el-button @click="addVar" class="el-icon-plus" size="small" type="primary">添加</el-button>
      <el-button
        @click="deleteVars"
        class="el-icon-delete selYellowBtn"
        size="small"
        type="warning"
      >批量删除</el-button>
    </section>
    <el-table :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column :tableData="tableData" type="selection" width="55"></el-table-column>
      <el-table-column label="id" prop="id"></el-table-column>
      <el-table-column label="变量名" prop="name"></el-table-column>
      <el-table-column label="变量值" prop="value"></el-table-column>
      <el-table-column label="变量描述" prop="description"></el-table-column>
      <el-table-column label="关联任务" prop="mapInfos"></el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            @click="deleteById(scope.row)"
            class="selYellowBtn"
            size="mini"
            type="warning"
          >删除</el-button>
          <el-button @click="editRole(scope.row)" size="mini" type="primary">修改</el-button>
          <el-button @click="showCompetence(scope.row)" size="mini">关联任务</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="dataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination>
    <!-- 增加/编辑变量 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="60%">
      <HandleVar
        @handleDialogClose="handleDialogClose"
        v-if="dialogVisible"
        :handleObj="handleObj"
      />
    </el-dialog>
  </el-main>
</template>

<script>
//分页组件
import Pagination from "@/components/Pagination";
//添加 修改角色
// import HandleVar from "@/views/systemManage/variantManage/handleVariant";
import HandleVar from "@/views/variantManage/handleVariant";
//后台功能授权
//角色
import { interfaceObj } from "@/urlConfig";
//通用的common
// import common from "@/common";
export default {
  name: "",
  data() {
    return {
      searchObj: {
        //查询条件
        roleName: null
      },
      dialogVisible: false,
      handleObj: {},
      dialogTitle: "",
      dataTotal: 0, //角色数量
      tableData: [],
      multipleSelection: [] //选中的数据
    };
  },
  components: { Pagination, HandleVar },
  mounted() {},
  methods: {
    //获取更新时间
    getUpdateTime(updateTime) {
      return common.formatTime(updateTime);
    },
    handleDialogClose() {
      this.handleObj = {};
      this.dialogVisible = false;
    },

    //选择
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    searchFun() {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.searchObj = { ...this.searchObj, ...paginateObj };
      this.$axios
        .post(interfaceObj.listVariant, this.searchObj)
        .then(({ data }) => {
          this.dataTotal = data.DS.all;
          this.tableData = data.DS.list;
        })
        .catch(error => {
          console.log(error);
        });
    },
    //分页
    paginationChange() {
      this.searchFun();
    },
    //设置
    isDisableFun(id, type) {
      if (type === 3) {
        if (id === "administrator") {
          return true;
        } else {
          return false;
        }
      }
      if (id === "administrator" || id === "8a9ce8f56744889a016744949ca50002") {
        return true;
      } else {
        return false;
      }
    },
    //添加
    addVar() {
      this.dialogTitle = "增加变量";
      this.handleObj = {};
      this.dialogVisible = true;
    },
    //修改
    editRole(row) {
      this.dialogVisible = true;
      this.handleObj = row;
      this.dialogTitle = "编辑变量";
    },

    //删除
    deleteById(row) {
      const h = this.$createElement;
      this.$msgbox({
        title: "角色删除",
        message: h("p", null, [
          h("span", null, "您确定要删除变量："),
          h("i", { style: "color: orange" }, `${row.name}`)
        ]),
        type: "warning",
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      })
        .then(() => {
          this.$axios
            .delete(interfaceObj.algClassify + `/${row.id}`)
            .then(res => {
              this.$notify({
                title: "提示",
                type: "success",
                message: "删除成功"
              });
              this.$common.changeCurrentPage(
                this.tableData.length,
                this.$refs.pagination.paginateObj,
                this.dataTotal,
                1,
                this
              );
              this.searchFun();
            })
            .catch(error => {
              console.log(error);
            });
        })
        .catch(() => {
          this.$notify({
            title: "提示",
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //批量删除
    deleteVars() {
      if (this.multipleSelection.length === 0) {
        this.$notify({
          title: "提示",
          type: "warning",
          message: "请选择要删除的变量"
        });
      } else {
        this.$msgbox({
          title: "变量删除",
          message: "您确定要删除选中的变量",
          type: "warning",
          showCancelButton: true,
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        })
          .then(() => {
            let deleteIds = [];
            this.multipleSelection.forEach(single => {
              deleteIds.push(single.id);
            });
            this.$axios
              .delete(interfaceObj.algClassify + `/${deleteIds.join(",")}`)
              .then(res => {
                this.$notify({
                  title: "提示",
                  type: "success",
                  message: "删除成功"
                });
                this.$common.changeCurrentPage(
                  this.tableData.length,
                  this.$refs.pagination.paginateObj,
                  this.dataTotal,
                  deleteIds.length,
                  this
                );
                this.searchFun();
              })
              .catch(error => {
                console.log(error);
              });
          })
          .catch(() => {
            this.$notify({
              title: "提示",
              type: "info",
              message: "已取消删除"
            });
          });
      }
    },
    //后台功能权限
    showCompetence(row) {
      this.competenceVisible = true;
      this.operateRoleId = row.id;
      this.operateRoleName = row.roleName;
    }
  }
};
</script>

<style lang="scss">
.roleManage {
  .roleOperate {
    padding: 10px;
    border: 1px solid #ebeef5;
    border-bottom: none;
  }
}
</style>