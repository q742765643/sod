<template>
  <div class="app-container">
    <!-- 专题库审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="资料状态">
        <el-select v-model.trim="queryParams.examineStatus">
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专题库名称">
        <el-input v-model.trim="queryParams.sdbName" type="text" placeholder="专题库名称"></el-input>
      </el-form-item>
      <el-form-item label="申请用户">
        <el-input v-model.trim="queryParams.userName" type="text" placeholder="申请用户"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
        <el-button size="small" type="success" @click="handleExport" icon="el-icon-download">导出</el-button>
      </el-form-item>
    </el-form>
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="sdbName" label="专题名" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="databaseSchema" label="专题库简称"></el-table-column>
      <el-table-column prop="userName" label="用户名"></el-table-column>
      <el-table-column prop="department" label="部门"></el-table-column>
      <el-table-column prop="userPhone" label="联系方式"></el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="160px" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="serverStatus" label="用途" width="100px">
        <template slot-scope="scope">
          <el-popover
            placement="top"
            effect="dark"
            trigger="hover"
            width="200"
            popper-class="darkPopover"
          >
            <p>{{ scope.row.uses}}</p>
            <div slot="reference" class="name-wrapper">
              <el-link :underline="false" type="primary">查看用途</el-link>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="examineStatus" label="资料状态" width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.examineStatus=='1'">待审核</span>
          <span v-if="scope.row.examineStatus=='2'">审核通过</span>
          <span v-if="scope.row.examineStatus=='3'">审核不通过</span>
          <span v-if="scope.row.examineStatus=='4'">再次审核</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-edit" @click="editCell(scope.row)">修改权限</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCell(scope.row)">删除</el-button>
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
      :before-close="closeeditDialog"
      title="专题库资料授权查看"
      :visible.sync="handleDialog"
      width="1200px"
      v-dialogDrag
    >
      <handleLibrary v-if="handleDialog" :handleObj="handleObj" ref="myHandleServer" />
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :before-close="closeeditDialog"
      title="专题库创建"
      :visible.sync="applyTopicDialog"
      width="1200px"
      v-dialogDrag
      top="5vh"
    >
      <applyTopic v-if="applyTopicDialog" ref="applyTopicServer" @closedialog="closeeditDialog" />
    </el-dialog>
  </div>
</template>

<script>
import {
  pageList,
  deleteList,
  exportTables
} from "@/api/authorityAudit/topicLibraryAudit";
// / 修改权限
import handleLibrary from "@/views/authorityAudit/topicLibraryAudit/handleLibrary";
// 创建专题库
import applyTopic from "@/views/authorityAudit/topicLibraryAudit/applyTopic";
export default {
  components: {
    handleLibrary,
    applyTopic
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examineStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        sdbName: "",
        userName: "",
        params: {
          orderBy: {
            createTime: "desc"
          }
        }
      },
      auditStatus: [
        {
          value: "1",
          label: "待审核"
        },
        {
          value: "2",
          label: "已审核"
        },
        {
          value: "3",
          label: "审核不通过"
        },
        {
          value: "4",
          label: "再次审核"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      applyTopicDialog: false,
      currentRow: null
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      pageList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "",
        sdbName: "",
        userName: ""
      };
      this.handleQuery();
    },

    deleteCell(row) {
      this.$confirm("确定要删除这条数据吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteList({ id: row.id }).then(res => {
            this.$message({
              type: "success",
              message: "删除成功"
            });
            this.resetQuery();
          });
        })
        .catch(() => {});
    },
    editCell(row) {
      this.handleObj = row;
      this.handleDialog = true;
    },
    closeeditDialog() {
      this.handleObj = {};
      this.handleDialog = false;
      this.applyTopicDialog = false;
      this.getList();
    },
    // 导出
    handleExport() {
      exportTables(this.queryParams).then(res => {
        this.downloadfileCommon(res);
      });
    }
  }
};
</script>
<style lang="css" scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>
