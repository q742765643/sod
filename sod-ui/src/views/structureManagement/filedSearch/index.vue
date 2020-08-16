<template>
  <div class="app-container filedSearch">
    <!-- 存储字段检索 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="表名称:" porp="tableName">
        <el-input clearable size="small" v-model.trim="queryParams.tableName" placeholder="请输入表名称" />
      </el-form-item>
      <el-form-item label="中文简称:" porp="eleName">
        <el-input clearable size="small" v-model.trim="queryParams.eleName" placeholder="请输入中文简称" />
      </el-form-item>
      <el-form-item label="字段名称:" porp="CElementCode">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.CElementCode"
          placeholder="请输入字段名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search" size="small">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
        <el-button type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <af-table-column prop="C_ELEMENT_CODE" label="公共元数据字段">
        <template slot-scope="scope">
          <span>{{scope.row.C_ELEMENT_CODE}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="DB_ELE_CODE" label="字段名称">
        <template slot-scope="scope">
          <span>{{scope.row.DB_ELE_CODE}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="USER_ELE_CODE" label="服务名称">
        <template slot-scope="scope">
          <span>{{scope.row.USER_ELE_CODE}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="ELE_NAME" label="中文简称">
        <template slot-scope="scope">
          <span>{{scope.row.ELE_NAME}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="TYPE" label="数据类型">
        <template slot-scope="scope">
          <span>{{scope.row.TYPE}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="ACCURACY" label="数据精度">
        <template slot-scope="scope">
          <span>{{scope.row.ACCURACY}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="CLASS_NAME" label="资料名称">
        <template slot-scope="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="viewCell(scope.row)"
          >{{scope.row.CLASS_NAME}}</el-link>
        </template>
      </af-table-column>
      <af-table-column prop="TABLE_NAME" label="表名称">
        <template slot-scope="scope">
          <span>{{scope.row.TABLE_NAME}}</span>
        </template>
      </af-table-column>
      <af-table-column prop="LOGIC_NAME" label="数据用途">
        <template slot-scope="scope">
          <span>{{scope.row.LOGIC_NAME}}</span>
        </template>
      </af-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList('')"
    />
    <!-- 高级搜索 -->
    <el-dialog
      :close-on-click-modal="false"
      title="筛选"
      :visible.sync="dialogSuperSearch"
      :before-close="handleClose"
      v-dialogDrag
    >
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @searchFun="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
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
      />
    </el-dialog>
  </div>
</template>

<script>
import { storageFieldList } from "@/api/structureManagement/filedSearch";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
// 高级搜索
import SuperSearch from "@/components/superSearch";
export default {
  components: {
    SuperSearch,
    StructureManageTable,
  },
  data() {
    return {
      rowData: [],
      structureManageVisible: false,
      structureManageTitle: "",
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: "",
        eleName: "",
        CElementCode: "",
      },
      total: 0,
      tableData: [],
      // 高级搜索
      dialogSuperSearch: false,
      superObj: {},
      superMsg: {},
      currentRow: null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.superMsg = {};
      this.queryParams.pageNum = 1;
      this.getList("");
    },
    resetQuery() {
      this.superMsg = {};
      this.$refs["queryForm"].resetFields();
      this.handleQuery();
    },
    /** 查询列表 */
    getList(superMsg) {
      // 判断是否是高级搜索
      let queryObj = {};
      if (
        (superMsg && superMsg.domains) ||
        (this.superMsg && this.superMsg.domains)
      ) {
        if (superMsg && superMsg.domains) {
          this.queryParams.pageNum = 1;
          this.superMsg = superMsg;
        }
        let superList = this.superMsg.domains;
        let newSuperForm = {};
        for (let i = 0; i < superList.length; i++) {
          newSuperForm[superList[i].select] = superList[i].value;
        }
        Object.assign(queryObj, this.queryParams, newSuperForm);
      } else {
        queryObj = this.queryParams;
      }
      console.log(queryObj);
      this.loading = true;
      storageFieldList(queryObj).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.ID == this.currentRow.ID) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },
    // 关闭高级搜索
    closeSuperSearch() {
      this.dialogSuperSearch = false;
    },
    superClick() {
      this.superObj = this.queryParams;
      this.superObj.pageName = "存储字段检索";
      this.dialogSuperSearch = true;
    },
    // 弹框取消
    handleClose() {
      this.dialogSuperSearch = false;
    },
    viewCell(row) {
      this.rowData = row;
      this.structureManageTitle = row.CLASS_NAME;
      this.structureManageVisible = true;
    },
    closeStructureManage() {
      this.getList("");
      this.structureManageVisible = false;
    },
  },
};
</script>
<style lang="scss">
.filedSearch {
  .searchBox {
    margin-bottom: 20px;
  }
  .el-table {
    .cell {
      white-space: nowrap !important;
      width: fit-content !important;
    }
    td {
      text-align: left !important;
    }
  }
}
</style>
