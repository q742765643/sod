<template>
  <div class="app-container">
    <!-- 接口管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="接口名称:" prop="userName">
        <el-input clearable size="small" v-model.trim="queryParams.userName" placeholder="请输入接口名称" />
      </el-form-item>
      <el-form-item label="接口描述:" prop="userName">
        <el-input clearable size="small" v-model.trim="queryParams.userName" placeholder="请输入接口描述" />
      </el-form-item>
      <el-form-item clearable label="接口分类:" prop="userName">
        <el-select v-model="queryParams.userName">
          <el-option
            :label="item"
            :value="item"
            v-for="(item,index) in urlClassify"
            :key="index"
          >{{item}}</el-option>
        </el-select>
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
        <el-button
          size="small"
          type="primary"
          @click="showDialog('export')"
          icon="el-icon-upload2"
        >批量导入</el-button>
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
      <el-table-column prop="createBy" label="接口名称"></el-table-column>
      <el-table-column prop="createTime" label="接口描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="content" label="接口类别" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" label="请求类型" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.status=='0'">未回复</span>
          <span v-if="scope.row.status=='1'">已回复</span>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="备注" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="address" label="操作" width="150" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="showDialog(scope.row)"
          >回复</el-button>
          <el-button
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="showDialog(scope.row)"
          >删除</el-button>
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
      width="1100px"
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      v-dialogDrag
    >
      <urlEdit
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></urlEdit>
    </el-dialog>
  </div>
</template>

<script>
import {
  saveDynManage,
  editDynManage,
} from "@/api/portalMangement/dynMangement";
import urlEdit from "@/views/portalMangement/urlManagement/urlEdit";
export default {
  components: {
    urlEdit,
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: "",
      },
      tableData: [],
      total: 0,
      urlClassify: ["全部"],
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
    //this.getList();
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
    showDialog(row) {
      if (row.id) {
        this.dialogTitle = "编辑";
        this.handleObj = row;
      } else {
        this.dialogTitle = "新增";
        this.handleObj = {};
      }
      this.msgFormDialog = true;
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
          "提示",
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


