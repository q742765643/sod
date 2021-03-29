<template>
  <div class="RecycleBinTem app-container">
    <el-form
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      class="searchBox"
      style="margin-bottom: 20px"
    >
      <el-form-item label="表名称" prop="tableName">
        <el-input
          clearable
          size="small"
          v-model.trim="queryParams.tableName"
          placeholder="请输入表名称"
        />
      </el-form-item>
      <el-form-item label="数据库" prop="databasePid">
        <el-select
          size="small"
          filterable
          v-model="queryParams.databasePid"
          placeholder="请选择"
          @change="handleFindSpec"
        >
          <el-option
            v-for="(citem, cindex) in DatabaseDefineList"
            :key="cindex"
            :label="citem.databaseName"
            :value="citem.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专题库" prop="databaseId">
        <el-select
          size="small"
          filterable
          v-model="queryParams.databaseId"
          placeholder="请选择"
        >
          <el-option
            v-for="(citem, cindex) in specialList"
            :key="cindex"
            :label="citem.schemaNameCn"
            :value="citem.id"
          ></el-option>
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
        <el-button
          size="small"
          @click="resetQuery('queryFormRef')"
          icon="el-icon-refresh-right"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      border
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
        prop="TABLE_NAME"
        label="数据表"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column prop="DATABASE_NAME" label="数据库"></el-table-column>
      <el-table-column prop="SCHEMA_NAME" label="模式名"></el-table-column>
      <el-table-column prop="SCHEMA_NAME_CN" label="专题名"> </el-table-column>
      <el-table-column prop="databaseName" label="存储结构">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="viewCell(scope.row)"
            >查看</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-finished"
            @click="refreshCell(scope.row)"
            >还原</el-button
          >
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCell(scope.row)"
            >删除</el-button
          >
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
  getRecycle,
  findByDatabaseDefineId,
  tombstone,
} from "@/api/structureManagement/dataList/index";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/dataList/TableManage/StructureManageTable";
export default {
  components: {
    StructureManageTable,
  },
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      tableData: [],
      DatabaseDefineList: [],
      specialList: [],
      total: 0,

      structureManageVisible: false, //表结构管理弹出层
      structureManageTitle: "", //表结构管理弹出层title
      rowData: {},
      tableBaseInfo: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    viewCell(row) {
      this.rowData = row;
      this.rowData.MYDISABLED = true; //查看去掉按钮
      console.log(this.rowData);
      this.structureManageTitle = row.TABLE_NAME;
      this.structureManageVisible = true;
    },

    deleteCell(row) {
      this.$confirm("是否删除数据表" + row.TABLE_NAME, "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          tombstone({ tableId: row.TABLE_ID, delFlag: 2 }).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.searchFun("search");
            }
          });
        })
        .catch(() => {});
    },
    //还原
    refreshCell() {
      this.$confirm("是否还原数据表" + row.TABLE_NAME, "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          tombstone({ tableId: row.TABLE_ID, delFlag: 0 }).then((response) => {
            if (response.code == 200) {
              this.$message({
                type: "success",
                message: "还原成功",
              });
              this.searchFun("search");
            }
          });
        })
        .catch(() => {});
    },
    //切换数据库查询专题库
    handleFindSpec(val) {
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery(formName) {
      this.$refs[formName].resetFields();
      this.handleQuery();
    },
    getList() {
      getRecycle(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
      });
    },
    // 关闭
    closeStructureManage() {
      this.structureManageVisible = false;
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
  },
};
</script>

<style>
</style>