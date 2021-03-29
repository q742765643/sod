<template>
  <div class="handleMaterialDialog">
    <div class="publicTitle">基本信息</div>
    <el-form :model="formBaseInfo" ref="baseParamsRef" label-width="80px">
      <el-row :gutter="10">
        <el-col :span="6">
          <el-form-item label="流水单号:" prop="FLOW_ID">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.FLOW_ID"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="应用名称:" prop="APP_NAME">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.APP_NAME"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="责任人:" prop="TUTOR_NAME">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.TUTOR_NAME"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="单位:" prop="LEGAL_UNITS">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.LEGAL_UNITS"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="部门:" prop="DEPT_NAME">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.DEPT_NAME"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联系方式:" prop="PHONENUMBER">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.PHONENUMBER"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="申请理由:" prop="tableName">
            <el-input
              clearable
              size="small"
              v-model.trim="formBaseInfo.tableName"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="publicTitle">数据申请</div>
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="关键字查询">
        <el-select
          size="small"
          v-model.trim="queryParams.searchSelect"
          @change="changeSearch"
        >
          <el-option label="数据表" value="tableName"></el-option>
          <el-option label="数据库" value="databaseName"></el-option>
          <el-option label="专题名" value="specialDatabaseName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label>
        <el-input
          size="small"
          v-model.trim="queryParams.searchInput"
          type="text"
          clearable
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button
          size="small"
          type="primary"
          @click="handleQuery"
          icon="el-icon-search"
          >查询</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-thumb"
          size="mini"
          @click="handlePower('')"
          >授权</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-close"
          size="mini"
          @click="handleRefused('')"
          >拒绝</el-button
        >
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="index" label="序号" width="50"></el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column
        prop="TABLE_NAME"
        label="数据表"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        prop="DATABASE_NAME"
        label="数据库"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        prop="SPECIAL_DATABASE_NAME"
        label="专题名"
        :show-overflow-tooltip="true"
      ></el-table-column>

      <el-table-column prop="" label="存储结构">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="handledDBMethods(scope.row)"
            >查看</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-thumb"
            @click="handlePower(scope.row)"
            >通过</el-button
          >
          <el-button
            type="text"
            size="mini"
            icon="el-icon-close"
            @click="handleRefused(scope.row)"
            >驳回</el-button
          >
        </template>
      </el-table-column>
    </el-table>
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
      append-to-body
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
  getRecordByApplyId,
  updateRecordCheck,
  updateRecordCheckCancel,
} from "@/api/authorityAudit/materialPower/index";
import { formatDate } from "@/utils/index";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/dataList/TableManage/StructureManageTable";
export default {
  name: "handleMaterialDialog",
  props: {
    handleObj: {
      type: Object,
    },
  },
  components: {
    StructureManageTable,
  },
  data() {
    return {
      passwordType: "password",
      formBaseInfo: {},
      queryParams: {},
      searchKey: "",
      loading: false,
      tableData: [],
      total: 0,
      AUTHORIZE_NUM: 0,
      AUTHORIZEF_NUM: 0,
      multipleSelection: [],
      rowData: null,
      structureManageTitle: "",
      structureManageVisible: false,
      tableBaseInfo: {},
    };
  },
  async created() {
    this.formBaseInfo = this.handleObj;
    this.queryParams.id = this.handleObj.ID;
    this.handleQuery();
  },
  methods: {
    // 关闭
    closeStructureManage() {
      this.structureManageVisible = false;
    },
    // 存储结构
    handledDBMethods(row) {
      this.rowData = row;
      this.rowData.MYDISABLED = true;
      this.structureManageTitle = row.TABLE_NAME;
      this.structureManageVisible = true;
    },
    // 眼睛开关
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 关键字更改
    changeSearch(val) {
      this.searchKey = val;
    },
    getList(obj) {
      console.log(obj);
      getRecordByApplyId(obj).then((res) => {
        if (res.code == 200) {
          this.loading = false;
          this.tableData = res.data;
          this.AUTHORIZEF_NUM = 0;
          this.AUTHORIZE_NUM = 0;
          this.AUTHORIZEF_NUM0 = 0;
          this.tableData.forEach((element) => {
            if (element.AUTHORIZE == 1) {
              this.AUTHORIZE_NUM++;
            } else if (element.AUTHORIZE == 2) {
              this.AUTHORIZEF_NUM++;
            }
          });
        } else {
          this.msgError(res.msg);
        }
      });
    },

    handleQuery() {
      let searchValue = this.queryParams.searchInput;
      let obj = {};
      obj.applyId = this.queryParams.id;
      obj[this.queryParams.searchSelect] = searchValue;
      this.getList(obj);
    },

    handlePower(row) {
      if (row) {
        this.multipleSelection = [];
        this.multipleSelection.push(row);
      }
      if (this.multipleSelection.length == 0) {
        this.msgError("请选择一条数据");
        return;
      }
      console.log(this.multipleSelection);
      this.powerMethods();
    },
    handleRefused(row) {
      if (row) {
        this.multipleSelection = [];
        this.multipleSelection.push(row);
      }
      if (this.multipleSelection.length == 0) {
        this.msgError("请选择一条数据");
        return;
      }
      this.$prompt("请输入拒绝原因", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator: (value) => {
          if (value.trim().length < 1 || value.trim().length > 50) {
            return "拒绝原因长度限制1-50个字符";
          }
        },
      })
        .then(({ value }) => {
          this.powerMethods(value);
        })
        .catch(() => {});
    },
    powerMethods(value) {
      this.loading = true;
      let obj = {};
      obj.userId = this.formBaseInfo.USER_ID;
      obj.dataAuthorityRecordList = [];
      this.multipleSelection.forEach((element) => {
        let cobj = {};
        cobj.id = element.ID;
        cobj.applyId = element.APPLY_ID;
        cobj.databaseId = element.DATABASE_ID;
        cobj.applyAuthority = element.APPLY_AUTHORITY;
        cobj.dataClassId = element.DATA_CLASS_ID;
        cobj.cause = value;
        cobj.tableName = element.TABLE_NAME;
        obj.dataAuthorityRecordList.push(cobj);
      });
      console.log(obj);
      if (value) {
        updateRecordCheckCancel(JSON.parse(JSON.stringify(obj))).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg,
            });
            this.handleQuery();
          } else {
            debugger;
            this.loading = false;
            this.$alert(res.msg, "温馨提示", {
              dangerouslyUseHTMLString: true,
            });
            this.handleQuery();
          }
        });
      } else {
        updateRecordCheck(JSON.parse(JSON.stringify(obj))).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              dangerouslyUseHTMLString: true,
              message: res.msg,
            });
            this.handleQuery();
          } else {
            this.loading = false;
            this.$alert(res.msg, "温馨提示", {
              dangerouslyUseHTMLString: true,
            });
            this.handleQuery();
          }
        });
      }
    },
  },
};
</script>
<style lang="scss">
.handleMaterialDialog {
  .publicTitle {
    box-sizing: border-box;
    background: #75bbea;
    color: #fff;
    padding: 10px;
    margin-bottom: 22px;
  }
  .el-alert {
    margin-bottom: 12px;
  }
  .el-loading-spinner {
    top: 120px !important;
  }
}
</style>

