<template>
  <el-tabs
    v-model.trim="activeName"
    @tab-click="handleClick"
    class="handleMaterialDialog"
  >
    <el-tab-pane label="基本信息" name="first">
      <el-form
        :model="formBaseInfo"
        class="demo-form-inline"
        label-width="120px"
      >
        <el-form-item label="用户名">
          <el-input
            v-model.trim="formBaseInfo.USERREALNAME"
            readonly
          ></el-input>
        </el-form-item>
        <el-form-item label="机构">
          <el-input v-model.trim="formBaseInfo.DEPARTMENT" readonly></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model.trim="formBaseInfo.USERPHONE" readonly></el-input>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-input v-model.trim="formBaseInfo.CREATE_TIME" readonly></el-input>
        </el-form-item>
        <el-form-item label="数据库账户名">
          <el-input
            v-model.trim="formBaseInfo.DATABASE_UP_ID"
            readonly
          ></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="22">
            <el-form-item label="数据库账户密码">
              <el-input
                v-model.trim="formBaseInfo.DATABASE_UP_PASSWORD"
                readonly
                @blur="capsTooltip = false"
                :type="passwordType"
                name="password"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-form-item class="unitFormItem">
              <i
                :class="
                  passwordType === 'password'
                    ? 'eye isEye'
                    : 'el-icon-view isEye'
                "
                @click="showPwd"
              ></i>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="数据读写权限" name="second">
      <el-alert
        :title="
          '申请状态：已申请' +
          tableData.length +
          '种资料,涉及' +
          tableData.length +
          '张表的读写权限， 审核通过' +
          AUTHORIZE_NUM +
          '张表, 审核不通过' +
          AUTHORIZEF_NUM +
          '张表'
        "
        type="success"
        :closable="false"
      ></el-alert>
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="关键字查询">
          <el-select
            size="small"
            v-model.trim="queryParams.searchSelect"
            @change="changeSearch"
          >
            <el-option label="资料分类" value="typeName"></el-option>
            <el-option label="资料名称" value="className"></el-option>
            <el-option label="表名称" value="tableName"></el-option>
            <el-option label="数据库" value="databaseName"></el-option>
            <el-option label="申请状态" value="applyAuthority"></el-option>
            <el-option label="审核状态" value="authorize"></el-option>
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
            @click="handlePower"
            >授权</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-close"
            size="mini"
            @click="handleRefused"
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
          prop="TYPE_NAME"
          label="资料分类"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="CLASS_NAME"
          label="资料名称"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          prop="TABLE_NAME"
          label="表名称"
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
        <el-table-column prop="IS_ACCESS" label="权限" width="80px">
          <template slot-scope="scope">
            <span v-if="scope.row.IS_ACCESS == '0'">受限</span>
            <span v-else>公开</span>
          </template>
        </el-table-column>
        <el-table-column prop="APPLY_AUTHORITY" label="申请状态" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.APPLY_AUTHORITY == '1'">读申请</span>
            <span v-else>读写申请</span>
          </template>
        </el-table-column>
        <el-table-column prop="AUTHORIZE" label="审核状态" width="120px">
          <template slot-scope="scope">
            <span v-if="scope.row.AUTHORIZE == '2'">
              <i class="el-icon-circle-close" style="color: #f56c6c"></i>拒绝
            </span>
            <span v-else-if="scope.row.AUTHORIZE == '1'">
              <i class="el-icon-circle-check" style="color: #67c23a"></i>通过
            </span>
            <span v-else>
              <i class="el-icon-s-finance" style="color: #e6a23c"></i>待审核
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="CAUSE" label="拒绝原因" width="120px">
          <template slot-scope="scope">
            <el-popover
              placement="top"
              effect="dark"
              trigger="hover"
              width="200"
              popper-class="darkPopover"
              v-if="scope.row.CAUSE"
            >
              <p>{{ scope.row.CAUSE }}</p>
              <div slot="reference" class="name-wrapper">
                <el-button type="primary" size="mini" plain class="tagpointer"
                  >查看</el-button
                >
              </div>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import {
  getApplyInfoById,
  getRecordByApplyId,
  updateRecordCheck,
  updateRecordCheckCancel,
} from "@/api/authorityAudit/materialPower/index";
import { formatDate } from "@/utils/index";
export default {
  name: "handleMaterialDialog",
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      passwordType: "password",
      activeName: "first",
      formBaseInfo: {},
      queryParams: {},
      searchKey: "",
      loading: false,
      tableData: [],
      total: 0,
      AUTHORIZE_NUM: 0,
      AUTHORIZEF_NUM: 0,
      multipleSelection: [],
    };
  },
  async created() {
    this.queryParams.id = this.handleObj.id;
    if (this.handleObj.id) {
      await getApplyInfoById({ id: this.handleObj.id }).then((res) => {
        if (res.code == 200) {
          this.formBaseInfo = res.data;
          this.formBaseInfo.CREATE_TIME = formatDate(
            this.formBaseInfo.CREATE_TIME
          );
        } else {
          this.msgError(res.msg);
        }
      });
    }
  },
  methods: {
    // 眼睛开关
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
    },
    handleClick() {
      if (this.activeName == "second") {
        this.handleQuery();
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
      if (this.searchKey == "applyAuthority" || this.searchKey == "authorize") {
        if (searchValue == "读申请" || searchValue == "通过") {
          searchValue = "1";
        } else if (searchValue == "读写申请" || searchValue == "拒绝") {
          searchValue = "2";
        } else if (searchValue != "") {
          searchValue = "0";
        }
      }
      let obj = {};
      obj.applyId = this.queryParams.id;
      obj[this.queryParams.searchSelect] = searchValue;
      this.getList(obj);
    },

    handlePower() {
      if (this.multipleSelection.length == 0) {
        this.msgError("请选择一条数据");
        return;
      }
      console.log(this.multipleSelection);
      this.powerMethods();
    },
    handleRefused() {
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
        if (value) {
          cobj.authorize = 2;
        } else {
          cobj.authorize = 1;
        }

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
  .el-alert {
    margin-bottom: 12px;
  }
  .el-loading-spinner {
    top: 120px !important;
  }
}
</style>

