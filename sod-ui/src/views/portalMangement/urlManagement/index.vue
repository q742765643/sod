<template>
  <div class="app-container">
    <!-- 接口管理 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="接口名称:" prop="apiName">
        <el-input clearable size="small" v-model.trim="queryParams.apiName" placeholder="请输入接口名称" />
      </el-form-item>
      <el-form-item label="接口描述:" prop="apiDesc">
        <el-input clearable size="small" v-model.trim="queryParams.apiDesc" placeholder="请输入接口描述" />
      </el-form-item>
      <el-form-item clearable label="接口分类:" prop="apiSys">
        <el-select v-model="queryParams.apiSys">
          <el-option label="全部" value></el-option>
          <el-option
            :label="item.dictLabel"
            :value="item.dictValue"
            v-for="(item,index) in urlClassify"
            :key="index"
          ></el-option>
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
        <el-button size="small" type="primary" @click="handleExport" icon="el-icon-upload2">批量导入</el-button>
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
      <el-table-column prop="apiName" label="接口名称"></el-table-column>
      <el-table-column prop="apiDesc" label="接口描述" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="apiSys" label="接口分类" :formatter="getapiSys"></el-table-column>
      <el-table-column prop="apiHttptype" label="请求类型" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.apiHttptype=='A'">GET/POST</span>
          <span v-if="scope.row.apiHttptype=='G'">GET</span>
          <span v-if="scope.row.apiHttptype=='P'">POST</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="address" label="操作" width="150" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" size="mini" type="text" @click="showDialog(scope.row)">编辑</el-button>
          <el-button icon="el-icon-delete" size="mini" type="text" @click="deleteRow(scope.row)">删除</el-button>
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
      top="5vh"
    >
      <urlEdit
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></urlEdit>
    </el-dialog>

    <!-- 批量导入弹窗-->
    <el-dialog
      width="600px"
      :close-on-click-modal="false"
      title="批量导入"
      :visible.sync="exportFormDialog"
      v-dialogDrag
      top="5vh"
    >
      <urlExport v-if="exportFormDialog" @cancelDialog="cancelDialog" ref="myDialog"></urlExport>
    </el-dialog>
  </div>
</template>

<script>
import { queryDataPage, delById } from "@/api/portalMangement/urlManagement";
import urlEdit from "@/views/portalMangement/urlManagement/urlEdit";
import urlExport from "@/views/portalMangement/urlManagement/urlExport";

export default {
  components: {
    urlEdit,
    urlExport,
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        apiName: "",
        apiDesc: "",
        apiSys: "",
      },
      tableData: [],
      total: 0,
      urlClassify: [],
      //已勾选记录
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      handleObj: {},
      exportFormDialog: false,
    };
  },
  /** 方法调用 */
  created() {
    this.getDicts("portal_api_mng").then((response) => {
      this.urlClassify = response.data;
    });
    this.getList();
  },
  methods: {
    getapiSys(row) {
      let dictLabel;
      this.urlClassify.forEach((element) => {
        if (element.dictValue == row.apiSys) {
          dictLabel = element.dictLabel;
        }
      });
      return dictLabel;
    },
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

    handleExport() {
      this.exportFormDialog = true;
    },
    deleteRow(row) {
      this.$confirm("确认要删除" + row.apiName + "吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delById({ id: row.id }).then((response) => {
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
          });
        })
        .catch(() => {});
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
      this.exportFormDialog = false;
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
  },
};
</script>


