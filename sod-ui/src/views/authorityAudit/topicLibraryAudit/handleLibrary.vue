<template>
  <section class="handleLiberyDialog">
    <el-tabs type="border-card">
      <el-tab-pane label="基本信息">
        <el-form ref="ruleForm" :model="msgFormDialog" label-width="100px">
          <el-form-item label="图标" v-if="!handleObj.pageName">
            <img v-if="sdbImg" :src="sdbImg" style="width:40px;" alt />
          </el-form-item>
          <el-form-item label="专题库名称">
            <el-input size="small" v-model="msgFormDialog.sdbName"></el-input>
          </el-form-item>
          <el-form-item label="用途">
            <el-input size="small" v-model="msgFormDialog.uses"></el-input>
          </el-form-item>
          <el-form-item label="创建人" v-if="!handleObj.pageName">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.userId"></el-input>
          </el-form-item>
          <el-form-item label="机构" v-if="!handleObj.pageName">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.department"></el-input>
          </el-form-item>
          <el-form-item label="联系方式" v-if="!handleObj.pageName">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.phoneNum"></el-input>
          </el-form-item>
          <el-form-item label="申请材料" v-if="!handleObj.pageName">
            <el-button
              :disabled="!msgFormDialog.applyMaterial"
              size="small"
              type="success"
              @click="handleExport"
              icon="el-icon-document"
            >下载</el-button>
          </el-form-item>
          <el-form-item label="排序">
            <el-input size="small" v-model="msgFormDialog.sortNO"></el-input>
          </el-form-item>
          <div class="dialog-footer">
            <el-button type="primary" @click="trueDialog('ruleForm')">保 存</el-button>
          </div>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="数据库授权">
        <el-form>
          <el-row class="center">
            <el-col :span="24">
              <span style="font-weight: bold">{{this.msgFormDialog.sdbName}}</span>
            </el-col>
          </el-row>
          <el-row class="center">
            <el-col :span="24">
              <span>专题库英文简称:</span>
              <span>{{this.msgFormDialog.simpleName}}</span>
            </el-col>
          </el-row>
          <el-table :data="databaseList" border stripe style="width: 100%;">
            <el-table-column prop="databaseId" label="数据库"></el-table-column>
            <el-table-column label="权限">
              <template slot-scope="scope">
                <el-checkbox-group v-model="scope.row.checkList">
                  <el-checkbox label="createTable">创建</el-checkbox>
                  <el-checkbox label="deleteTable">删除</el-checkbox>
                  <el-checkbox label="tableDataAccess">读写</el-checkbox>
                </el-checkbox-group>
              </template>
            </el-table-column>
          </el-table>
          <el-row>
            <el-col style="text-align:center;margin-top:10px;">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-thumb"
                @click="addPhysicsDefine"
              >数据库授权</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="专题库资料">
        <!-- 查询条件 -->
        <section class="searchCon">
          <el-form
            :inline="true"
            :model="searchLibraryObj"
            ref="modelForm"
            class="funLoad selSearchCon"
          >
            <el-form-item label="关键字查询">
              <el-select size="small" v-model="searchLibraryObj.key">
                <el-option label="资料分类" value="typeName"></el-option>
                <el-option label="资料名称" value="dataName"></el-option>
                <el-option label="表名称" value="tableName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label>
              <el-input size="small" v-model="searchLibraryObj.valueText" type="text"></el-input>
            </el-form-item>
            <el-form-item class="handleSearchBtn">
              <el-button
                size="small"
                type="primary"
                @click="searchLibraryFun"
                icon="el-icon-search"
              >查询</el-button>
            </el-form-item>
          </el-form>
        </section>
        <!-- 列表 -->
        <section class="loadTable">
          <el-table :data="tableLibraryData" border style="width: 100%;">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="typeName" label="资料分类" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="dataName" label="资料名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="ddataId" label="四级编码" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="tableName" label="表名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="databaseName" label="数据库" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column
              prop="applyAuthority"
              label="申请权限"
              width="80"
              :formatter="applyAuthFormatter"
            ></el-table-column>
          </el-table>
        </section>
      </el-tab-pane>
      <el-tab-pane label="基础库资料" v-if="!handleObj.pageName">
        <!-- 查询条件 -->
        <section class="searchCon">
          <el-form
            :inline="true"
            :model="searchBaseLibraryObj"
            ref="modelForm"
            class="funLoad selSearchCon"
          >
            <el-form-item label="关键字查询">
              <el-select size="small" v-model="searchBaseLibraryObj.key">
                <el-option label="资料分类" value="typeName"></el-option>
                <el-option label="资料名称" value="dataName"></el-option>
                <el-option label="表名称" value="tableName"></el-option>
                <el-option label="申请权限" value="applyAuthority"></el-option>
                <el-option label="审核状态" value="empowerAuthority"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label>
              <el-input size="small" v-model="searchBaseLibraryObj.valueText" type="text"></el-input>
            </el-form-item>

            <el-form-item class="handleSearchBtn">
              <el-button size="small" type="primary" @click="loadReadList" icon="el-icon-search">查询</el-button>
              <el-button
                type="primary"
                size="small"
                @click="batchUpdatePower('1')"
                icon="el-icon-thumb"
              >授权</el-button>
              <el-button
                type="danger"
                size="small"
                @click="batchUpdatePower('2')"
                icon="el-icon-close"
              >拒绝</el-button>
            </el-form-item>
          </el-form>
        </section>
        <!-- 列表 -->
        <section class="loadTable">
          <el-table :data="baseTableLibraryData" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45"></el-table-column>
            <el-table-column prop="typeName" label="资料分类" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="dataName" label="资料名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="ddataId" label="四级编码" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="tableName" label="表名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="databaseName" label="数据库" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column
              prop="applyAuthority"
              width="80px"
              label="申请权限"
              :formatter="applyAuthFormatter"
            ></el-table-column>

            <el-table-column prop="empowerAuthority" label="审核状态">
              <template slot-scope="scope">
                <el-link
                  :underline="false"
                  size="small"
                  v-if="scope.row.empowerAuthority == '1'"
                  type="success"
                  icon="el-icon-check"
                >已授权</el-link>
                <el-link
                  :underline="false"
                  size="small"
                  v-else
                  type="danger"
                  icon="el-icon-close"
                >已撤销</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="examine_status" label="备注">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.failureReason }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">查看</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240px">
              <template slot-scope="scope">
                <el-button
                  plain
                  type="primary"
                  size="mini"
                  icon="el-icon-thumb"
                  @click="updatePower(scope.row,'1')"
                >授权</el-button>
                <el-button
                  plain
                  type="danger"
                  size="mini"
                  icon="el-icon-close"
                  @click="updatePower(scope.row,'2')"
                >拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </section>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>

<script>
import {
  getById,
  getAuthorityBySdbId,
  getSpecialDataList,
  empowerDataBatch,
  empowerDataOne,
  empowerDatabaseSpecial,
  saveBase,
  exportTable
} from "@/api/authorityAudit/topicLibraryAudit";
import { findByUserId } from "@/api/authorityAudit/userRoleAudit";
export default {
  name: "handleLiberyDialog",

  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {},
      searchLibraryObj: {
        key: "typeName",
        valueText: ""
      },
      searchBaseLibraryObj: {
        key: "typeName",
        valueText: ""
      },
      baseTableLibraryData: [],
      tableLibraryData: [],
      databaseList: [],
      multipleSelection: [],
      paramList: [], //临时变量，拒绝时附带信息
      refuseReasonDialog: false,
      refusereason: "",
      single: false,
      sdbImg: "",
      dialogImageUrlData: [
        require("@/assets/image/icon/h1.png"),
        require("@/assets/image/icon/h2.png"),
        require("@/assets/image/icon/h3.png"),
        require("@/assets/image/icon/h4.png"),
        require("@/assets/image/icon/h5.png"),
        require("@/assets/image/icon/h6.png"),
        require("@/assets/image/icon/h7.png"),
        require("@/assets/image/icon/h8.png"),
        require("@/assets/image/icon/h9.png"),
        require("@/assets/image/icon/h10.png"),
        require("@/assets/image/icon/h11.png"),
        require("@/assets/image/icon/h12.png"),
        require("@/assets/image/icon/h13.png"),
        require("@/assets/image/icon/h14.png"),
        require("@/assets/image/icon/h15.png")
      ]
    };
  },
  created() {
    /* if (this.handleObj.pageName == "业务用户审核") {
       findByUserId({ userId: this.handleObj.userName }).then(res => {
         let data = res.data[0];
         this.msgFormDialog.id=data.id;
         this.msgFormDialog.simpleName = this.handleObj.userName; //申请材料
      this.msgFormDialog.uses = this.handleObj.remark; //用途
      this.msgFormDialog.sdbName = this.handleObj.remark; //用途
      });
     
    } else {
      if (this.handleObj.id) {
        this.initDetail();
      }
    } */
    if (this.handleObj.id) {
      console.log(this.handleObj);
      this.initDetail();
    }

    this.searchLibraryFun();
    this.loadReadList();
  },
  methods: {
    applyAuthFormatter(row, column) {
      if (row.applyAuthority == "1") {
        return "只读";
      } else if (row.applyAuthority == "2") {
        return "读写";
      }
    },
    // 下载
    handleExport() {
      exportTable({ filePath: this.msgFormDialog.applyMaterial }).then(res => {
        if (res.code) {
          this.$message({ type: "warning", message: res.msg });
        } else {
          this.downloadfileCommon(res);
        }
      });
    },
    // 获取专题库基本信息|专题库授权
    initDetail() {
      // 基本信息
      getById({ id: this.handleObj.id }).then(res => {
        if (res.code == 200) {
          this.msgFormDialog = res.data;
          if (!this.msgFormDialog.applyMaterial && !this.handleObj.pageName) {
            this.$message({ type: "danger", message: "文件不存在" });
          }
        }
      });
      // 数据库授权
      getAuthorityBySdbId({ sdbId: this.handleObj.id }).then(res => {
        if (res.code == 200) {
          if (res.data && res.data.length > 0) {
            this.databaseList = res.data;
            this.databaseList.forEach((element, index) => {
              this.$set(element, "checkList", []);
              if (element.createTable == "2") {
                element.checkList.push("createTable");
              }
              if (element.deleteTable == "2") {
                element.checkList.push("deleteTable");
              }
              if (element.tableDataAccess == "2") {
                element.checkList.push("tableDataAccess");
              }
            });
          }
        }
      });
    },
    trueDialog() {
      saveBase(this.msgFormDialog).then(res => {
        if (res.code == 200) {
          this.$emit("closedialog");
          this.$message({
            type: "success",
            message: "保存成功"
          });
        } else {
          this.$message({
            type: "error",
            message: res.msg
          });
        }
      });
    },
    // 获取专题库资料列表
    searchLibraryFun() {
      let obj = {
        dataType: 1,
        sdbId: this.handleObj.id
      };
      if (this.searchLibraryObj.valueText) {
        obj[this.searchLibraryObj.key] = this.searchLibraryObj.valueText;
      }
      console.log(obj);
      getSpecialDataList(obj).then(res => {
        this.tableLibraryData = res.data;
      });
    },
    //加载读权限审核列表
    loadReadList() {
      let obj = {
        dataType: 2,
        sdbId: this.handleObj.id
      };
      if (this.searchBaseLibraryObj.valueText) {
        obj[
          this.searchBaseLibraryObj.key
        ] = this.searchBaseLibraryObj.valueText;
      }
      console.log(obj);
      getSpecialDataList(obj).then(res => {
        this.baseTableLibraryData = res.data;
      });
    },
    //数据库授权
    addPhysicsDefine() {
      let obj = {};
      let listArry = this.databaseList;
      listArry.forEach(listele => {
        let checkArry = listele.checkList;
        checkArry.forEach(cele => {
          if (listele[cele]) {
            listele[cele] = 2;
          }
        });
      });
      obj.databaseSpecialAuthorityList = listArry;
      obj.databaseId = this.msgFormDialog.databaseId;
      obj.sdbId = this.msgFormDialog.id;
      obj.sdbName = this.msgFormDialog.sdbName;
      obj.simpleName = this.msgFormDialog.simpleName;
      obj.userId = this.msgFormDialog.userId;
      console.log(obj);
      empowerDatabaseSpecial(obj).then(res => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.databaseList = [];
          this.initDetail();
          this.searchLibraryFun();
          this.loadReadList();
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //单个库的授权或拒绝
    updatePower(row, status) {
      row.examineStatus = status;
      if (status == "2") {
        this.$prompt("请输入拒绝原因", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        })
          .then(({ value }) => {
            row.failureReason = value;
            this.editPowerOne(row);
          })
          .catch(() => {});
      } else {
        this.editPowerOne(row);
      }
    },
    editPowerOne(row) {
      console.log(row);
      empowerDataOne(row).then(res => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.loadReadList();
        } else {
          this.$message({ type: "error", message: "授权失败" });
        }
      });
    },
    //批量授权或拒绝
    batchUpdatePower(status) {
      //判断是否勾选需要授权的资料
      if (this.multipleSelection.length > 0) {
        if (status == "2") {
          this.$prompt("请输入拒绝原因", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消"
          })
            .then(({ value }) => {
              this.multipleSelection.forEach(element => {
                element.failureReason = value;
                element.examineStatus = 2;
              });
              this.editPowerBath();
            })
            .catch(() => {});
        } else {
          this.multipleSelection.forEach(element => {
            element.examineStatus = 1;
          });
          this.editPowerBath();
        }
      } else {
        this.$message({ type: "warning", message: "请勾选需要操作的资料" });
        return;
      }
    },
    editPowerBath() {
      console.log(this.multipleSelection);
      empowerDataBatch(this.multipleSelection).then(res => {
        if (res.code == 200) {
          this.$message({ type: "success", message: "授权成功" });
          this.loadReadList();
        } else {
          this.$message({ type: "error", message: "授权失败" });
        }
      });
    }
  }
};
</script>

<style lang="scss">
.handleLiberyDialog {
  a {
    color: #409eff;
  }
  .el-link--inner {
    font-size: 12px;
  }
  .loadTable {
    border: none;
  }
  .center {
    margin-bottom: 20px;
    text-align: center;
  }
}
</style>
