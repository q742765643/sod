<template>
  <div class="app-container">
    <!-- 数据注册审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="数据分类：">
        <el-select v-model="queryParams.importName" placeholder>
          <el-option
            v-for="(item,index) in dbtypeselect"
            :key="index"
            :label="item.class_name"
            :value="item.data_class_id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="审核状态：">
        <el-select v-model="queryParams.status" placeholder>
          <el-option label="待审核" value="1"></el-option>
          <el-option label="通过" value="2"></el-option>
          <el-option label="不通过" value="3"></el-option>
          <el-option label="待删除" value="4"></el-option>
          <el-option label="已失效" value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['DRegistration:role:add']"
        >增量同步元数据</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column align="center" type="index" :index="table_index" min-width="10" label=" "></el-table-column>
      <el-table-column align="center" prop="d_data_id" label="四级编码" sortable min-width="100"></el-table-column>
      <el-table-column
        align="center"
        prop="class_name"
        label="数据名称"
        min-width="100"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column align="center" prop="username" label="申请人" min-width="80"></el-table-column>
      <el-table-column align="center" prop="deptName" label="机构" min-width="100"></el-table-column>
      <el-table-column align="center" prop="phone" label="联系方式" min-width="60"></el-table-column>
      <el-table-column align="center" prop="apply_time" label="申请时间" sortable min-width="100"></el-table-column>
      <el-table-column
        align="center"
        prop="examine_status"
        label="审核状态"
        min-width="60"
        v-if="datacell !==2 "
      >
        <template slot-scope="scope">
          <span v-if="scope.row.examine_status==1">待审核</span>
          <span v-if="scope.row.examine_status==3">审核不通过</span>
          <span v-if="scope.row.examine_status==4">删除申请中</span>
          <span v-if="scope.row.examine_status==5">已失效</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="storage_define_identifier"
        label="存储构建"
        min-width="55"
        v-if="datacell===2"
      >
        <template slot-scope="scope2">
          <el-button size="mini" @click="showMaterialSingle(scope2.row)">
            <!-- <i class="btnRound blueRound" v-if="scope2.row.storage_define_identifier==1"></i>
            <i class="btnRound orangRound" v-else></i>-->
            存储结构
          </el-button>
          <!-- <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>存储结构
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="storage_define_identifier"
        label="迁移清除"
        min-width="55"
        v-if="datacell===2"
      >
        <template slot-scope="scope4">
          <el-button size="mini" @click="handleCleanAndTransfer(scope4.row)">
            <!-- <i
                class="btnRound blueRound"
                v-if="null!=scope4.row.clear_id&&scope4.row.clear_id!=''"
              ></i>
            <i class="btnRound orangRound" v-else></i>-->
            迁移清除
          </el-button>
          <!-- <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>迁移清除
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="storage_define_identifier"
        label="备份"
        min-width="35"
        v-if="datacell===2"
      >
        <template slot-scope="scope5">
          <el-button size="mini" @click="handleBackUp(scope5.row)">
            <!--  <i
                class="btnRound blueRound"
                v-if="scope5.row.storage_define_identifier=='1'&&scope5.row.backup_id!=null&&scope5.row.backup_id!=''"
              ></i>
            <i class="btnRound orangRound" v-else></i>-->
            备份
          </el-button>

          <!--  <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>备份
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="storage_define_identifier"
        label="恢复"
        min-width="35"
        v-if="datacell===2"
      >
        <template slot-scope="scope6">
          <el-button
            v-if="scope6.row.storage_define_identifier!='3'"
            size="mini"
            @click="openRecoverDialog(scope6.row)"
          >恢复</el-button>
          <el-button disabled v-else size="mini">恢复</el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="EXAMINE_STATUS"
        label="操作"
        min-width="50"
        v-if="datacell!==5"
      >
        <template slot-scope="scope1">
          <el-button
            type="text"
            plain
            size="mini"
            v-if="scope1.row.examine_status===1"
            @click="examineData(scope1.row)"
          >
            <i class="el-icon-s-management"></i>审核
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.examine_status==2||scope1.row.examine_status==4"
            @click="deleteList(scope1.row)"
          >
            <i class="el-icon-delete"></i>删除
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.examine_status===3"
            @click="showReason(scope1.row)"
          >
            <i class="el-icon-tickets"></i>原因
          </el-button>
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
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
    >
      <handleAccount
        v-if="handleDialog"
        :handleObj="handleObj"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  exportRole,
  dataScope,
  changeRoleStatus
} from "@/api/system/role";
import handleAccount from "@/views/authorityAudit/cloudDBaudit/handleCloudDB";
export default {
  components: {
    handleAccount
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""]
      },
      examineStatus: [
        {
          value: "0",
          label: "待审核"
        },
        {
          value: "2",
          label: "审核未通过"
        },
        {
          value: "1",
          label: "审核通过"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
    /** 查询角色列表 */
    getList() {
      this.loading = true;
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.tableData = response.data.pageData;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examine_status: "",
        nameUser: "",
        nameSourceDB: "",
        time: ["", ""]
      };
      this.handleQuery();
    },
    handleAdd() {
      // this.dialogTitle = "新增数据库账户审核";
      // this.handleObj = {};
      // this.handleDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    viewCell(row) {
      this.dialogTitle = "数据库账户审核";
      this.handleObj = row;
      this.handleDialog = true;
    },
    deleteCell(row) {},
    analysisCell(row) {},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
  }
};
</script>
