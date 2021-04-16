<template>
  <div class="DRegistrationTem app-container">
    <!-- 数据注册审核 -->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      class="searchBox"
    >
      <el-form-item label="资料名称">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.dataName"
          placeholder="资料名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="存储编码 ">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.dataClassId"
          placeholder="存储编码 "
        ></el-input>
      </el-form-item>
      <el-form-item label="数据类型 ">
        <el-select
          v-model.trim="queryParams.dataType"
          placeholder="数据类型 "
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option label="全部" value></el-option>
          <el-option label="业务数据" :value="0"></el-option>
          <el-option label="业务中间数据" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态：">
        <el-select v-model.trim="queryParams.status" @change="handleQuery">
          <el-option label="待审核" :value="1"></el-option>
          <el-option label="通过" :value="2"></el-option>
          <el-option label="不通过" :value="3"></el-option>
          <!-- <el-option label="待删除" :value="4"></el-option>
          <el-option label="已失效" :value="5"></el-option> -->
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          @click="handleQuery"
          icon="el-icon-search"
          >查询</el-button
        >
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="singleTable"
      highlight-current-row
    >
      <el-table-column
        type="index"
        label="序号"
        width="50"
        :index="table_index"
      ></el-table-column>
      <el-table-column
        prop="dataName"
        label="资料名称"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column prop="dataClassId" label="存储编码"></el-table-column>
      <el-table-column
        prop="updateTime"
        label="数据描述"
        :show-overflow-tooltip="true"
      >
      </el-table-column>
      <el-table-column prop="dataType" label="数据类型">
        <template slot-scope="scope">
          <span v-if="scope.row.dataType == 0">业务数据</span>
          <span v-if="scope.row.dataType == 1">业务中间数据</span>
        </template>
      </el-table-column>
      <el-table-column prop="userId" label="申请人"> </el-table-column>
      <el-table-column prop="userId" label="存储结构">
        <template slot-scope="scope">
          <div
            v-for="(item, index) in scope.row.dataTableApplyDtoList"
            :key="index"
          >
            <el-button plain size="mini" @click="viewCell(scope.row, index)"
              >存储结构</el-button
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 1"
            type="text"
            size="mini"
            icon="el-icon-check"
            @click="checkCell(scope.row, 2)"
            >通过</el-button
          >
          <el-button
            v-if="scope.row.status == 1"
            type="text"
            size="mini"
            icon="el-icon-close"
            @click="checkCell(scope.row, 3)"
            >拒绝</el-button
          >
          <el-button size="mini" type="text" @click="deleteList(scope.row)">
            <i class="el-icon-delete"></i>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 表结构管理 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="`表结构管理(${structureManageTitle})`"
      :visible.sync="structureManageVisible"
      width="100%"
      :fullscreen="true"
      :before-close="closeStructureManage"
      top="0"
      class="scrollDialog"
    >
      <StructureManageTable
        ref="scrollDiv"
        v-if="structureManageVisible"
        v-bind:parentRowData="rowData"
        :tableBaseInfo="tableBaseInfo"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  deleteById,
  getDataTable,
  review,
} from "@/api/authorityAudit/DRegistration/index";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/dataList/TableManage/StructureManageTable";
export default {
  components: {
    StructureManageTable,
  },
  data() {
    return {
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        DDataId: "",
        dataName: "",
        dataClassId: "",
        dataType: "",
        status:
          this.$route.params.status == undefined
            ? 1
            : this.$route.params.status,
      },
      total: 0,
      tableData: [],
      currentRow: null,
      structureManageTitle: "",
      structureManageVisible: false,
      rowData: {},
      tableBaseInfo: {},
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
    /** 查询列表 */
    getList() {
      getDataTable(this.queryParams).then((response) => {
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
        dataClassId: "",
        dataName: "",
        dataType: "",
        status:
          this.$route.params.status == undefined
            ? 1
            : this.$route.params.status,
      };
      this.handleQuery();
    },
    viewCell(row, index) {
      this.rowData = {
        MYDISABLED: true,
        CLASS_NAME: row.dataName,
        DATA_CLASS_ID: row.dataClassId,
        DATABASE_ID: row.dataTableApplyDtoList[index].databaseId,
        STORAGE_TYPE: row.dataTableApplyDtoList[index].storageType,
        TABLE_ID: row.dataTableApplyDtoList[index].id,
        USER_ID: row.dataTableApplyDtoList[index].userId,
        STORAGE_TYPE: row.dataTableApplyDtoList[index].storageType,
        dataTableApplyDtoList: row.dataTableApplyDtoList,
        formPage: "数据注册审核",
      };
      this.tableBaseInfo = row.dataTableApplyDtoList[index];
      this.structureManageTitle = row.dataName;
      this.structureManageVisible = true;
    },
    // 审核
    checkCell(row, type) {
      let typeText = "通过";
      if (type == 3) {
        typeText = "拒绝";
      }
      this.$confirm("是否" + typeText + row.dataName + "?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (type == 3) {
            this.$prompt("请输入拒绝原因", "温馨提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
            })
              .then(({ value }) => {
                this.checkCellMethods({
                  id: row.id,
                  reviewer: this.$store.getters.userName,
                  status: 3,
                  reviewNotes: value,
                });
              })
              .catch(() => {});
          } else {
            this.checkCellMethods({
              id: row.id,
              reviewer: this.$store.getters.userName,
              status: 2,
            });
          }
        })
        .catch(() => {});
    },
    checkCellMethods(obj) {
      review(obj).then((response) => {
        if (response.code == 200) {
          this.$message({
            message: "审核成功",
            type: "success",
          });
          this.handleQuery();
        }
      });
    },
    // 删除
    deleteList(row) {
      this.$confirm("是否删除" + row.dataName + "?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteById({ id: row.id }).then((response) => {
            if (response.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.handleQuery();
            }
          });
        })
        .catch(() => {});
    },
    // 关闭
    closeStructureManage() {
      this.getList();
      this.structureManageVisible = false;
    },
  },
};
</script>

<style lang="scss">
.DRegistrationTem {
  .searchBox {
    margin-bottom: 24px;
  }
}
</style>