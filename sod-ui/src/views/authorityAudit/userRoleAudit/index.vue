<template>
  <div class="app-container">
    <!-- 业务用户审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="用户名称" prop="userName">
        <el-input clearable size="small" v-model.trim="queryParams.userName" placeholder="请输入用户名称"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="nickName">
        <el-input clearable size="small" v-model.trim="queryParams.nickName" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item label="机构" prop="deptName">
        <el-input clearable size="small" v-model.trim="queryParams.deptName" placeholder="请输入机构名称"></el-input>
      </el-form-item>
      <el-form-item label="用户状态" prop="checked">
        <el-select v-model="queryParams.checked" size="small" style="width: 140px">
          <el-option label="全部" value></el-option>
          <el-option label="待审核" value="0"></el-option>
          <el-option label="审核通过" value="1"></el-option>
          <el-option label="驳回" value="2"></el-option>
          <el-option label="激活" value="3"></el-option>
          <el-option label="注销" value="4"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      border
      highlight-current-row
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      @current-change="handleCurrentChange"
      ref="singleTable"
    >
      <el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>
      <el-table-column prop="userName" label="用户名称"></el-table-column>
      <el-table-column prop="nickName" label="用户名"></el-table-column>
      <el-table-column prop="deptName" label="机构"></el-table-column>
      <el-table-column prop="phonenumber" label="联系方式" width="120"></el-table-column>
      <el-table-column prop="updateTime" label="创建时间" sortable="custom" width="160">
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="审核状态" width="100">
        <template slot-scope="scope">
          <el-link v-if="scope.row.checked == '0'" :underline="false" type="primary">待审核</el-link>
          <el-link v-else-if="scope.row.checked == '1'" :underline="false" type="success">审核通过</el-link>
          <el-link v-else-if="scope.row.checked == '2'" :underline="false" type="danger">驳回</el-link>
          <el-link v-else-if="scope.row.checked == '3'" :underline="false" type="warning">激活</el-link>
          <el-link v-else-if="scope.row.checked == '4'" :underline="false" type="info">注销</el-link>
          <el-link v-else :underline="false" type="info">未知</el-link>
        </template>
      </el-table-column>
      <el-table-column label="角色授权" width="120">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-custom"
            @click="handleRole(scope.row)"
          >角色授权</el-button>
        </template>
      </el-table-column>
      <el-table-column label="申请审核" width="100">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-s-check"
            @click="handleApply(scope.row)"
          >审核</el-button>
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
    <!-- 新增/编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      v-dialogDrag
    >
      <handleWork v-if="dialogVisible" :handleMsgObj="handleMsgObj" @closeStep="closeStep" />
    </el-dialog>
    <!-- 角色授权 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="dialogRole"
      width="500px"
      v-dialogDrag
    >
      <div>
        <el-checkbox-group v-model="roleIds" style="display:block;margin-bottom:20px;">
          <el-checkbox
            v-for="item in roleOptions"
            :key="item.roleId"
            :label="item.roleName"
            :disabled="item.status == 1"
          ></el-checkbox>
        </el-checkbox-group>
        <div class="dialog-footer" slot="footer">
          <el-button type="primary" @click="trueRole">确 定</el-button>
          <el-button @click="dialogRole = false">取 消</el-button>
        </div>
      </div>
    </el-dialog>
    <!-- 申请审核 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="dialogApply"
      width="1100px"
      v-dialogDrag
      top="4vh"
    >
      <handleApply v-if="dialogApply" :handleMsgObj="handleMsgObj" @closeStep="closeStep" />
    </el-dialog>
  </div>
</template>

<script>
import { gatAllBiz } from "@/api/authorityAudit/userRoleAudit";
import { optionselect } from "@/api/system/role";
import { getUser, updateUser } from "@/api/system/user";
import handleWork from "@/views/authorityAudit/userRoleAudit/handleWork";
import handleApply from "@/views/authorityAudit/userRoleAudit/handleApply";
export default {
  components: {
    handleWork,
    handleApply,
  },
  data() {
    return {
      roleIds: [],
      roleOptions: [],
      dialogApply: false,
      dialogRole: false,
      dialogVisible: false,
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: "",
        username: "",
        nameSourceDB: "",
        params: {
          orderBy: {
            updateTime: "desc",
          },
        },
      },

      total: 0,
      tableData: [],
      dialogTitle: "",
      currentRow: {},
      currentId: "",
    };
  },
  created() {
    this.getList();
    this.getRoles();
  },
  methods: {
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.updateTime = "asc";
      } else {
        orderBy.updateTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
    handleCurrentChange(val) {
      this.currentRow = val;
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
    resetQuery() {
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      gatAllBiz(this.queryParams).then((response) => {
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

    /** 查询角色列表 */
    getRoles() {
      optionselect().then((response) => {
        this.roleOptions = response.data;
      });
    },
    // 新增
    handleAdd() {
      this.dialogTitle = "新增";
      this.handleMsgObj = {};
      this.dialogVisible = true;
    },
    // 修改
    handleEdit() {
      if (this.currentRow) {
        this.handleMsgObj = this.currentRow;
        this.dialogVisible = true;
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
      }
    },
    // 删除
    handleDelete() {},
    // 角色授权
    handleRole(row) {
      this.dialogTitle = "角色授权";
      this.currentId = row.id;
      getUser(row.id).then((response) => {
        this.roleOptions.forEach((element) => {
          response.data.roleIds.forEach((item) => {
            if (element.id == item) {
              this.roleIds.push(element.roleName);
            }
          });
        });
        this.dialogRole = true;
      });
    },
    // 确认授权
    trueRole() {
      let obj = {};
      obj.id = this.currentId;
      obj.roleIds = [];
      this.roleOptions.forEach((element) => {
        this.roleIds.forEach((item) => {
          if (element.roleName == item) {
            obj.roleIds.push(element.id);
          }
        });
      });

      updateUser(obj).then((response) => {
        if (response.code === 200) {
          this.msgSuccess("修改成功");
          this.dialogRole = false;
          this.getList();
        } else {
          this.msgError(response.msg);
        }
      });
    },
    // 申请审核
    handleApply(row) {
      this.dialogTitle = "申请审核";
      this.handleMsgObj = row;
      this.dialogApply = true;
    },
    closeStep() {
      this.handleMsgObj = {};
      this.dialogApply = false;
      this.getList();
    },
  },
};
</script>
<style scoped>
.searchBox {
  margin-bottom: 24px;
}
</style>
