<template>
  <div class="app-container">
    <!-- 业务动态管理查询菜单 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="标题:" prop="title">
        <el-input clearable size="small" v-model.trim="queryParams.title" placeholder="请输入查询标题" />
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 业务动态管理操作按钮 -->
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('add')" icon="el-icon-plus">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">编辑</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>
      </el-col>
    </el-row>
    <!-- 数据展示列表 -->
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="createBy" label="创建人"></el-table-column>
      <el-table-column prop="title" label="标题" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="360" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="ispublished" label="发布状态" width="360" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.ispublished=='0'">未发布</span>
          <span v-if="scope.row.ispublished=='1'">已发布</span>
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
    <!-- 弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      v-dialogDrag
    >
      <dynEdit
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></dynEdit>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryDataPage,
  getById,
  delSyncManage,
} from "@/api/portalMangement/dynMangement";
import dynEdit from "@/views/portalMangement/dynMangement/dynEdit";
export default {
  components: {
    dynEdit,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: "",
      },
      tableData: [],
      total: 0,
      //已勾选记录
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      handleObj: {},
    };
  },
  /** 方法调用 */
  created() {
    this.getList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    showDialog(type) {
      if (type == "add") {
        this.dialogTitle = "新增";
        this.handleObj = {};
        this.msgFormDialog = true;
      } else {
        if (this.multipleSelection.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return;
        }
        this.dialogTitle = "编辑";
        getById({ id: this.multipleSelection[0].id }).then((res) => {
          this.handleObj = res.data;
          this.msgFormDialog = true;
        });
      }
    },
    deleteCell() {
      if (this.multipleSelection.length != 1) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
        return;
      } else {
        this.$confirm(
          "确认要删除" + this.multipleSelection[0].title + "吗?",
          "温馨提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            delSyncManage({ id: this.multipleSelection[0].id }).then(
              (response) => {
                if (response.code == 200) {
                  this.$message({
                    type: "success",
                    message: "删除成功",
                  });
                  this.handleQuery();
                } else {
                  this.$message({
                    type: "error",
                    message: "删除失败",
                  });
                }
              }
            );
          })
          .catch(() => {});
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /** 查询列表 */

    getList() {
      this.loading = true;
      queryDataPage(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        //自动选中之前已选行
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    //设置表单选中指定行
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.tableData.forEach((element, index) => {
            if (element.id == row.id) {
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index]
              );
            }
          });
        });
      }
    },
    cancelDialog() {
      this.msgFormDialog = false;
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
  },
};
</script>


