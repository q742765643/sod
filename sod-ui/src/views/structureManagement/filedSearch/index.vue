<template>
  <div class="app-container">
    <!-- 存储字段检索 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="表名称:">
        <el-input size="small" v-model="queryParams.table_name" placeholder="请输入表名称" />
      </el-form-item>
      <el-form-item label="中文简称:">
        <el-input size="small" v-model="queryParams.ele_name" placeholder="请输入中文简称" />
      </el-form-item>
      <el-form-item label="字段名称:">
        <el-input size="small" v-model="queryParams.c_element_code" placeholder="请输入字段名称" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search" size="small">查询</el-button>
        <el-button type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column
        prop="C_ELEMENT_CODE"
        label="公共元数据字段"
        width="140px"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="DB_ELE_CODE" label="字段名称" width="120px"></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="USER_ELE_CODE"
        label="服务名称"
        width="120px"
      ></el-table-column>
      <el-table-column prop="ELE_NAME" label="中文简称" width="170px"></el-table-column>
      <el-table-column prop="TYPE" label="数据类型" width="80px"></el-table-column>
      <el-table-column prop="ACCURACY" label="数据精度" width="80px"></el-table-column>
      <el-table-column prop="CLASS_NAME" label="资料名称" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="viewCell(scope.row)"
          >{{scope.row.CLASS_NAME}}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="TABLE_NAME" :show-overflow-tooltip="true" label="表名称" width="160px"></el-table-column>
      <el-table-column prop="LOGIC_NAME" :show-overflow-tooltip="true" label="数据用途" width="100px"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 高级搜索 -->
    <el-dialog
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
  </div>
</template>

<script>
import { storageFieldList } from "@/api/structureManagement/overviewStorage";
// 高级搜索
import SuperSearch from "@/components/superSearch";
export default {
  components: {
    SuperSearch
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        table_name: "",
        ele_name: "",
        c_element_code: ""
      },
      total: 0,
      tableData: [],
      // 高级搜索
      dialogSuperSearch: false,
      superObj: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList("");
    },
    /** 查询列表 */
    getList(superSearchForm) {
      let queryObj = {};
      if (superSearchForm) {
        this.queryParams.pageNum = 1;
        let superList = superSearchForm.domains;
        let newSuperForm = {};
        for (let i = 0; i < superList.length; i++) {
          newSuperForm[superList[i].select] = superList[i].value;
        }
        queryObj = Object.assign(newSuperForm, this.queryParams);
      } else {
        queryObj = this.queryParams;
      }
      console.log(queryObj);
      this.loading = true;
      storageFieldList(queryObj).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
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
    }
  }
};
</script>
<style lang="css" scoped>
.searchBox {
  margin-bottom: 20px;
}
</style>
