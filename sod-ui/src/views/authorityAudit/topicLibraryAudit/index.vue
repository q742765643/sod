<template>
  <div class="app-container">
    <!-- 专题库审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="资料状态">
        <el-select v-model="queryParams.examineStatus">
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
        <el-input v-model="queryParams.sdbName" type="text" placeholder="专题库名称"></el-input>
      </el-form-item>
      <el-form-item label="申请用户">
        <el-input v-model="queryParams.userId" type="text" placeholder="申请用户"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column align="center" type="index" width="50" :index="table_index"></el-table-column>
      <el-table-column align="center" prop="sdbName" label="专题名" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" prop="simpleName" label="专题库简称"></el-table-column>
      <el-table-column align="center" prop="userId" label="用户名"></el-table-column>
      <el-table-column align="center" prop="department" label="部门"></el-table-column>
      <el-table-column align="center" prop="phoneNum" label="联系方式"></el-table-column>
      <el-table-column
        align="center"
        prop="createTime"
        label="申请时间"
        width="160px"
        :formatter="createTimeFormater"
      ></el-table-column>
      <el-table-column align="center" prop="serverStatus" label="用途" width="100px">
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
              <el-button type="primary" size="mini" plain class="tagpointer">查看用途</el-button>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="examineStatus" label="资料状态" width="120px">
        <template slot-scope="scope">
          <span v-if="scope.row.examineStatus=='1'">待审核</span>
          <span v-if="scope.row.examineStatus=='2'">审核通过</span>
          <span v-if="scope.row.examineStatus=='3'">审核不通过</span>
          <span v-if="scope.row.examineStatus=='4'">再次审核</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="160px">
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
    <el-dialog title="专题库资料授权查看" :visible.sync="handleDialog" width="1200px">
      <handleLibrary
        v-if="handleDialog"
        :handleObj="handleObj"
        @closedialog="closeeditDialog"
        ref="myHandleServer"
      />
    </el-dialog>
    <el-dialog title="专题库创建" :visible.sync="applyTopicDialog" width="1200px">
      <applyTopic v-if="applyTopicDialog" ref="applyTopicServer" @closedialog="closeeditDialog" />
    </el-dialog>
  </div>
</template>

<script>
import { formatTime } from "@/components/commonVaildate.js";
import {
  specialList,
  deleteList
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
        examineStatus: "",
        sdbName: "",
        userId: ""
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
      applyTopicDialog: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    createTimeFormater: function(row) {
      return formatTime(row.createTime, "Y-M-D h:m:s");
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
      specialList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "",
        sdbName: "",
        userId: ""
      };
      this.handleQuery();
    },

    deleteCell(row) {
      deleteList({ id: row.id }).then(res => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          });
          this.resetQuery();
        } else {
          this.$message({
            type: "error",
            message: res.msg
          });
        }
      });
    },
    editCell(row) {
      this.handleObj = row;
      this.handleDialog = true;
    },
    closeeditDialog() {
      this.handleObj = {};
      this.handleDialog = false;
      this.applyTopicDialog = false;
      this.handleQuery();
    }
  }
};
</script>
