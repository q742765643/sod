<template>
  <div class="app-container">
    <!-- 数据注册审核 -->
    <el-form :model="queryParams" :inline="true" class="searchBox">
      <el-form-item label="数据分类：">
        <el-select v-model="queryParams.DDataId" placeholder>
          <el-option key="index" label="全部" value></el-option>
          <el-option
            v-for="(item,index) in dbtypeselect"
            :key="index"
            :label="item.class_name"
            :value="item.data_class_id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="审核状态：">
        <el-select v-model="queryParams.examineStatus">
          <el-option label="待审核" :value="1"></el-option>
          <el-option label="通过" :value="2"></el-option>
          <el-option label="不通过" :value="3"></el-option>
          <el-option label="待删除" :value="4"></el-option>
          <el-option label="已失效" :value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAddSync"
          v-hasPermi="['DRegistration:role:add']"
        >增量同步元数据</el-button>
      </el-col>
    </el-row>
    <!-- 表格 -->
    <el-table class="tableList" v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" :index="table_index" width="45" label=" "></el-table-column>
      <el-table-column prop="TYPE_NAME" label="数据分类" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="D_DATA_ID" label="四级编码"></el-table-column>
      <el-table-column prop="TYPE_NAME" label="数据名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="username" label="申请人"></el-table-column>
      <el-table-column prop="deptName" label="机构"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="CREATE_TIME" label="申请时间" width="160" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.CREATE_TIME) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="EXAMINE_STATUS"
        label="审核状态"
        width="60"
        v-if="queryParams.examineStatus !==2 "
      >
        <template slot-scope="scope">
          <span v-if="scope.row.EXAMINE_STATUS==1">待审核</span>
          <span v-if="scope.row.EXAMINE_STATUS==3">审核不通过</span>
          <span v-if="scope.row.EXAMINE_STATUS==4">删除申请中</span>
          <span v-if="scope.row.EXAMINE_STATUS==5">已失效</span>
        </template>
      </el-table-column>

      <!-- 存储结构 -->
      <el-table-column
        prop="STORAGE_DEFINE_IDENTIFIER"
        label="存储构建"
        min-width="70"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope">
          <el-button disabled v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 3" size="mini">
            <i class="btnRound orangRound"></i>存储结构
          </el-button>
          <el-button v-else size="mini" @click="handledDBShowMethods(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.STORAGE_DEFINE_IDENTIFIER==1"></i>
            <i class="btnRound orangRound" v-else></i>存储结构
          </el-button>
        </template>
      </el-table-column>

      <!-- 数据同步 -->
      <el-table-column
        prop="SYNC_IDENTIFIER"
        label="存储构建"
        min-width="70"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope">
          <el-button disabled v-if="scope.row.SYNC_IDENTIFIER == 3" size="mini">
            <i class="btnRound orangRound"></i>数据同步
          </el-button>
          <el-button v-else size="mini" @click="handlSyncShowMethods(scope.row)">
            <i class="btnRound blueRound" v-if="scope.row.SYNC_ID"></i>
            <i class="btnRound orangRound" v-else></i>数据同步
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="storageDefineIdentifier"
        label="迁移"
        min-width="70"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope">
          <!-- 迁移 -->

          <el-button
            v-if="scope.row.MOVE_ST==1"
            size="mini"
            @click="handlMoveShowMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.MOVE_ID"></i>
            <i class="btnRound orangRound" v-else></i>迁移
          </el-button>
        </template>
      </el-table-column>

      <el-table-column
        prop="storageDefineIdentifier"
        label="迁移"
        min-width="70"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope">
          <!-- 清除 -->
          <el-button
            v-if="scope.row.CLEAR_ST==1"
            size="mini"
            @click="handlClearShowMethods(scope.row)"
          >
            <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
            <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
            <i class="btnRound orangRound" v-else></i>清除
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="storageDefineIdentifier"
        label="备份"
        min-width="55"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope5">
          <el-button class="opBtn" size="mini" @click="handleBackUp(scope.row)">
            <i
              class="btnRound blueRound"
              v-if="scope5.row.storageDefineIdentifier=='1'&&scope5.row.backupIdentifier!=null&&scope5.row.backupIdentifier!=''"
            ></i>
            <i class="btnRound orangRound" v-else></i>
            备份
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="storageDefineIdentifier"
        label="恢复"
        min-width="55"
        v-if="queryParams.examineStatus===2"
      >
        <template slot-scope="scope6">
          <el-button
            v-if="scope6.row.storageDefineIdentifier!='3'"
            size="mini"
            @click="openRecoverDialog(scope6.row)"
          >恢复</el-button>
          <el-button disabled v-else size="mini">恢复</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="EXAMINE_STATUS"
        label="操作"
        min-width="50"
        v-if="queryParams.examineStatus!==5"
      >
        <template slot-scope="scope1">
          <el-button
            type="text"
            plain
            size="mini"
            v-if="scope1.row.EXAMINE_STATUS===1"
            @click="examineData(scope1.row)"
          >
            <i class="el-icon-s-management"></i>审核
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.EXAMINE_STATUS==2||scope1.row.EXAMINE_STATUS==4"
            @click="deleteList(scope1.row)"
          >
            <i class="el-icon-delete"></i>删除
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.EXAMINE_STATUS===3"
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
  </div>
</template>

<script>
import {
  getDataClassify,
  getDataTable
} from "@/api/authorityAudit/DRegistration/index";
export default {
  components: {},
  data() {
    return {
      queryParams: { pageNum: 1, pageSize: 10, DDataId: "", examineStatus: "" }, //查询
      dbtypeselect: [], //数据分类下拉框
      tableData: [], //表格
      total: 0 //表格数
    };
  },
  created() {
    // 查询数据分类
    getDataClassify().then(response => {
      this.dbtypeselect = response.data;
    });
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 查询列表
    getList() {
      this.loading = true;
      console.log(this.queryParams);
      getDataTable(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    // 重置
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        DDataId: "",
        examineStatus: ""
      };
      this.getList();
    },
    /* 增量同步元数据 */
    handleAddSync() {}
  }
};
</script>

<style>
</style>