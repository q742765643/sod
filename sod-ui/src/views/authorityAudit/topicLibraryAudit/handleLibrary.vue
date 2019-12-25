<template>
  <section class="handleLiberyDialog">
    <el-tabs type="border-card">
      <el-tab-pane label="基本信息">
        <el-form ref="ruleForm" :model="msgFormDialog" label-width="100px">
          <el-form-item label="图标">
            <img v-if="imageurl" :src="imageurl" style="width:40px;" alt />
          </el-form-item>
          <el-form-item label="专题库名称">
            <!--  <el-input size="small" v-model="msgFormDialog.TDB_NAME"></el-input> -->
            <el-input size="small" v-model="msgFormDialog.tdb_name"></el-input>
          </el-form-item>
          <el-form-item label="用途">
            <!-- <el-input size="small" v-model="msgFormDialog.USES"></el-input> -->
            <el-input size="small" v-model="msgFormDialog.uses"></el-input>
          </el-form-item>
          <el-form-item label="创建人">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.userRealname"></el-input>
          </el-form-item>
          <el-form-item label="机构">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.department"></el-input>
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input size="small" :disabled="true" v-model="msgFormDialog.userPhone"></el-input>
          </el-form-item>
          <el-form-item label="申请材料">
            <el-button size="small" type="primary" @click="downloadfile()">点击下载</el-button>
          </el-form-item>
          <el-form-item label="排序">
            <!-- <el-input size="small" v-model="msgFormDialog.SORT_NO"></el-input> -->
            <el-input size="small" v-model="msgFormDialog.sort_no"></el-input>
          </el-form-item>
          <div class="dialog-footer">
            <el-button type="primary" @click="trueDialog('ruleForm')">保 存</el-button>
          </div>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="数据库授权">
        <el-form>
          <el-row style="margin-bottom: 20px;">
            <el-col :span="8" :offset="8">
              <span style="font-weight: bold">{{this.msgFormDialog.tdb_name_title}}</span>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 20px;">
            <el-col :span="8" :offset="8">
              <span style="text-align: right">专题库英文简称:</span>
              <span style="text-align: left;">{{this.msgFormDialog.database_schema_id}}</span>
            </el-col>
          </el-row>
          <el-table :data="databaseList" border stripe style="width: 100%;">
            <el-table-column prop="value" label="数据库"></el-table-column>
            <el-table-column label="权限">
              <template slot-scope="scope">
                <el-checkbox-group v-model="scope.row.checkList">
                  <el-checkbox label="crate">创建</el-checkbox>
                  <el-checkbox label="delete">删除</el-checkbox>
                  <el-checkbox label="readwrite">读写</el-checkbox>
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
              <el-select size="small" v-model="searchLibraryObj.data_status">
                <el-option label="资料分类" value="dataSourceId"></el-option>
                <el-option label="资料名称" value="meansName"></el-option>
                <el-option label="表名称" value="tableName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label>
              <el-input size="small" v-model="searchLibraryObj.tdb_name" type="text"></el-input>
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
          <el-table :data="tableLibraryData" border stripe style="width: 100%;">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="type_name" label="资料分类" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="class_name" label="资料名称"></el-table-column>
            <el-table-column prop="d_data_id" label="四级编码"></el-table-column>
            <el-table-column prop="table_name" label="表名称"></el-table-column>
            <el-table-column prop="database_name" label="数据库"></el-table-column>
            <el-table-column prop="special_database_name" label="专题名"></el-table-column>
            <el-table-column prop="apply_authority" label="申请权限" :formatter="applyAuthFormatter"></el-table-column>
          </el-table>
        </section>
      </el-tab-pane>
      <el-tab-pane label="基础库资料">
        <!-- 查询条件 -->
        <section class="searchCon">
          <el-form
            :inline="true"
            :model="searchLibraryObj"
            ref="modelForm"
            class="funLoad selSearchCon"
          >
            <el-form-item label="关键字查询">
              <el-select size="small" v-model="searchBaseLibraryObj.searchType">
                <el-option label="资料分类" value="dataSourceId"></el-option>
                <el-option label="资料名称" value="meansName"></el-option>
                <el-option label="表名称" value="tableName"></el-option>
                <el-option label="申请权限" value="targetDBId"></el-option>
                <el-option label="审核状态" value="flowDir"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label>
              <el-input size="small" v-model="searchBaseLibraryObj.searchValue" type="text"></el-input>
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
          <el-table
            ref="multipleTable"
            :data="baseTableLibraryData"
            border
            stripe
            style="width: 100%;"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column prop="type_name" label="资料分类" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="class_name" label="资料名称"></el-table-column>
            <el-table-column prop="d_data_id" label="四级编码"></el-table-column>
            <el-table-column prop="table_name" label="表名称"></el-table-column>
            <el-table-column prop="database_name" label="数据库"></el-table-column>
            <el-table-column prop="special_database_name" label="专题名"></el-table-column>
            <el-table-column
              prop="apply_authority"
              width="80px"
              label="申请权限"
              :formatter="applyAuthFormatter"
            ></el-table-column>
            <el-table-column fixed="right" label="操作" width="140px">
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
            <el-table-column prop="authorize" label="审核状态">
              <template slot-scope="scope">
                <el-link
                  :underline="false"
                  size="small"
                  v-if="scope.row.authorize == '1'"
                  type="success"
                  icon="el-icon-check"
                >已授权</el-link>
                <el-link
                  :underline="false"
                  size="small"
                  v-else
                  type="danger"
                  icon="el-icon-close"
                >已拒绝</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="examine_status" label="备注">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.cause }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">查看</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
          </el-table>
        </section>
      </el-tab-pane>
    </el-tabs>
    <div class="dialog-footer"></div>
    <el-dialog title="填写拒绝原因" width="30%" :visible.sync="refuseReasonDialog" append-to-body>
      <el-input type="textarea" placeholder="请填写拒绝原因" v-model="refusereason"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="refuseCommit">确 定</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// import { testExport } from "@/components/common.js";
export default {
  name: "handleLiberyDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      searchObj: {},
      msgFormDialog: {},
      searchLibraryObj: {
        data_status: "dataSourceId",
        tdb_name: ""
      },
      searchBaseLibraryObj: {
        searchType: "dataSourceId",
        searchValue: ""
      },
      baseTableLibraryData: [],
      tableLibraryData: [],
      databaseList: [],
      multipleSelection: [],
      paramList: [], //临时变量，拒绝时附带信息
      refuseReasonDialog: false,
      refusereason: "",
      single: false,
      imageurl: "",
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
    this.searchObj = this.handleObj;

    // this.initServerDetail();
    // this.searchLibraryFun();
    // this.loadReadList();
  },
  methods: {
    initDBAndType() {
      this.axios.get(interfaceObj.SpecialDB_DBAndType).then(res => {
        //console.log(res.data.data);
        //this.msgFormDialog = res.data.data.baseInfo;
      });
    },
    applyAuthFormatter(row, column) {
      if (row.apply_authority == "1") {
        return "只读";
      } else if (row.apply_authority == "2") {
        return "读写";
      }
    },
    //申请材料下载
    downloadfile() {
      if (this.msgFormDialog.apply_file_path == "") {
        this.$message({ type: "warning", message: "文件不存在" });
        return;
      }
      let filepath = this.msgFormDialog.apply_file_path;
      this.axios
        .post(interfaceObj.SpecialDB_ifFileExist, { filepath })
        .then(res => {
          if (res.data.result == "yes") {
            this.axios
              .get(
                interfaceObj.download +
                  "?filepath=" +
                  this.msgFormDialog.apply_file_path
              )
              .then(data => {
                testExport(
                  interfaceObj.download +
                    "?filepath=" +
                    this.msgFormDialog.apply_file_path
                );
              })
              .catch(function(error) {
                testExport(
                  interfaceObj.download +
                    "?filepath=" +
                    this.msgFormDialog.apply_file_path
                );
              });
          } else {
            this.$message({ type: "warning", message: "文件不存在" });
          }
        });
    },
    // 获取专题库基本信息|专题库授权
    initServerDetail() {
      if (this.searchObj.tdb_id == undefined) {
        this.$message({
          type: "error",
          message: "没有获取到当前行的ID"
        });
      } else {
        //
        this.axios
          .get(
            interfaceObj.SpecialDB_baseInfo + "&tdb_id=" + this.searchObj.tdb_id
          )
          .then(res => {
            this.msgFormDialog = res.data.data.baseInfo;
            //处理图片地址
            let img_path = this.msgFormDialog.tdb_img;
            let img_path1 = img_path.split("h");
            img_path = img_path1[img_path1.length - 1];
            img_path = img_path.substring(0, img_path.length - 4);
            //this.msgFormDialog.tdb_img = img_path;
            this.imageurl = this.dialogImageUrlData[img_path * 1 - 1];
            this.msgFormDialog.tdb_name_title = this.msgFormDialog.tdb_name;
            //数据库授权数据库信息
            if (
              this.msgFormDialog.database_id != null &&
              this.msgFormDialog.database_name != null
            ) {
              console.log(this.msgFormDialog);
              let idArr = this.msgFormDialog.database_id.split(",");
              let nameArr = this.msgFormDialog.database_name.split(",");
              //查询数据库的实际权限
              let id = this.searchObj.tdb_id;
              this.axios
                .post(interfaceObj.SpecialDB_getSpecialAuthority, { id })
                .then(res => {
                  for (let i = 0; i < idArr.length; i++) {
                    let tmp = {};
                    tmp.id = idArr[i];
                    tmp.value = nameArr[i];
                    tmp.checkList = [];
                    if (res.data.data != null) {
                      let databaseObject = res.data.data.find(item => {
                        return item.database_id == idArr[i];
                      });
                      if (databaseObject) {
                        if (databaseObject.create_table != "1") {
                          tmp.checkList.push("crate");
                        }
                        if (databaseObject.delete_table != "1") {
                          tmp.checkList.push("delete");
                        }
                        if (databaseObject.table_data_access != "1") {
                          tmp.checkList.push("readwrite");
                        }
                      }
                    }
                    this.databaseList.push(tmp);
                    console.log(this.databaseList);
                  }
                });
            }
          });
      }
    },
    trueDialog(formName) {
      let editDBobj = {};
      editDBobj.tdbId = this.searchObj.tdb_id;
      editDBobj.tdbName = this.msgFormDialog.tdb_name;
      editDBobj.Uses = this.msgFormDialog.uses;
      editDBobj.sort_no = this.msgFormDialog.sort_no;
      this.axios.post(interfaceObj.SpecialDB_update, editDBobj).then(res => {
        if (res.data.returnCode == 0) {
          this.$emit("closedialog");
          this.$message({
            type: "success",
            message: "修改成功"
          });
        } else {
          this.$message({
            type: "error",
            message: "修改失败，请稍后重试"
          });
        }
      });
    },
    // 获取专题库资料列表
    searchLibraryFun() {
      if (this.handleObj.tdb_id == undefined) {
        this.$message({
          type: "error",
          message: "没有获取到当前行的ID"
        });
      } else {
        let searchObject = {};
        searchObject.tdb_id = this.handleObj.tdb_id;
        searchObject.data_Type = "1";
        //资料分类
        if (this.searchLibraryObj.data_status == "dataSourceId") {
          searchObject.dataType = this.searchLibraryObj.tdb_name;
        } else if (this.searchLibraryObj.data_status == "meansName") {
          searchObject.meansName = this.searchLibraryObj.tdb_name;
        } else if (this.searchLibraryObj.data_status == "tableName") {
          searchObject.tableName = this.searchLibraryObj.tdb_name;
        }
        this.axios
          .post(interfaceObj.SpecialDB_getPriReadList, searchObject)
          .then(res => {
            if (
              res.data.returnCode == "0" &&
              res.data.data.readList.length > 0
            ) {
              this.tableLibraryData = [];
              debugger;
              for (let i = 0; i < res.data.data.readList.length; i++) {
                var obj = res.data.data.readList[i];
                var databaseNameArray = obj["database_name"].trim().split(",");
                var special_database_name = obj["special_database_name"].split(
                  ","
                );
                var applyauthorityArray = obj["apply_authority"].split(",");
                var length =
                  databaseNameArray.length > special_database_name.length
                    ? special_database_name.length
                    : databaseNameArray.length;
                for (let j = 0; j < length; j++) {
                  obj.database_name = databaseNameArray[j];
                  obj.special_database_name = special_database_name[j];
                  obj.apply_authority = applyauthorityArray[j];
                  this.tableLibraryData.push(obj);
                }
              }
            }
          });
      }
    },
    //加载读权限审核列表
    loadReadList() {
      let searchObject = {};
      searchObject.tdb_id = this.handleObj.tdb_id;
      searchObject.data_Type = "2";
      //资料分类
      if (this.searchBaseLibraryObj.searchType == "dataSourceId") {
        searchObject.dataType = this.searchBaseLibraryObj.searchValue;
      } else if (this.searchBaseLibraryObj.searchType == "meansName") {
        searchObject.meansName = this.searchBaseLibraryObj.searchValue;
      } else if (this.searchBaseLibraryObj.searchType == "tableName") {
        searchObject.tableName = this.searchBaseLibraryObj.searchValue;
      } else if (this.searchBaseLibraryObj.searchType == "targetDBId") {
        if (this.searchBaseLibraryObj.searchValue == "只读") {
          searchObject.applyauthority = "1";
        } else if (this.searchBaseLibraryObj.searchValue == "读写") {
          searchObject.applyauthority = "2";
        } else {
          searchObject.applyauthority = "";
        }
      } else if (this.searchBaseLibraryObj.searchType == "sourceDBId") {
        if (this.searchBaseLibraryObj.searchValue == "读") {
          searchObject.applyauth = "1";
        } else if (this.searchBaseLibraryObj.searchValue == "读写") {
          searchObject.applyauth = "2";
        } else if (this.searchBaseLibraryObj.searchValue == "未授权") {
          searchObject.applyauth = "3";
        } else {
          searchObject.applyauth = "";
        }
      } else if (this.searchBaseLibraryObj.searchType == "flowDir") {
        if (this.searchBaseLibraryObj.searchValue == "已授权") {
          searchObject.authorize = "1";
        } else if (this.searchBaseLibraryObj.searchValue == "已拒绝") {
          searchObject.authorize = "2";
        } else if (this.searchBaseLibraryObj.searchValue == "待授权") {
          searchObject.authorize = "3";
        } else {
          searchObject.authorize = "";
        }
      }
      this.axios
        .post(interfaceObj.SpecialDB_getPriReadList, searchObject)
        .then(res => {
          if (res.data.returnCode == "0") {
            this.baseTableLibraryData = [];
            for (let i = 0; i < res.data.data.readList.length; i++) {
              var obj = res.data.data.readList[i];
              var databaseNameArray = obj["database_name"].trim().split(",");
              var special_database_name = obj["special_database_name"].split(
                ","
              );
              var applyauthorityArray = obj["apply_authority"].split(",");
              var length =
                databaseNameArray.length > special_database_name.length
                  ? special_database_name.length
                  : databaseNameArray.length;
              for (let j = 0; j < length; j++) {
                obj.database_name = databaseNameArray[j];
                obj.special_database_name = special_database_name[j];
                obj.apply_authority = applyauthorityArray[j];
                this.baseTableLibraryData.push(obj);
              }
            }
          }
        });
    },
    //数据库授权
    addPhysicsDefine() {
      let tdb_id = this.searchObj.tdb_id;
      let tdb_name = this.msgFormDialog.tdb_name;
      let database_schema_id = this.msgFormDialog.database_schema_id;
      if (this.msgFormDialog.database_schema_id == "") {
        this.$message({ type: "error", message: "数据不完整！" });
        return;
      }
      let specialAuthority = [];
      let checkedIds = "";
      for (let i = 0; i < this.databaseList.length; i++) {
        let sa = {};
        sa.tdb_id = tdb_id;
        sa.database_id = this.databaseList[i].id;
        let crateObject = this.databaseList[i].checkList.find(item => {
          return item == "crate";
        });
        sa.create_table = crateObject == null ? 1 : 2;
        let deleteObject = this.databaseList[i].checkList.find(item => {
          return item == "delete";
        });
        sa.delete_table = deleteObject == null ? 1 : 2;
        let readwriteObject = this.databaseList[i].checkList.find(item => {
          return item == "readwrite";
        });
        sa.table_data_access = readwriteObject == null ? 1 : 2;
        specialAuthority.push(sa);
        checkedIds += this.databaseList[i].id + ",";
      }
      if (checkedIds.length == 0) {
        this.$message({ type: "error", message: "缺少物理库!" });
        return;
      } else {
        checkedIds = checkedIds.substring(0, checkedIds.length - 1);
      }
      let database_id = checkedIds;
      let physicsDefine = {
        tdb_id: tdb_id,
        special_database_name: tdb_name,
        database_instance: database_schema_id,
        database_schema_name: database_schema_id,
        parent_id: database_id,
        database_classify: "专题库",
        user_id: this.msgFormDialog.user_id,
        specialAuthority: specialAuthority
      };
      this.axios
        .post(interfaceObj.SpecialDB_addPhysicsDefine, physicsDefine)
        .then(res => {
          if (res.data.returnCode == "0") {
            this.$message({ type: "info", message: "添加成功" });
            this.databaseList = [];
            this.initServerDetail();
            this.searchLibraryFun();
          }
        });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //单个库的授权或拒绝
    updatePower(obj, status) {
      let tdb_id = this.searchObj.tdb_id;
      let userId = this.msgFormDialog.user_id;
      let paramList = {
        userId: userId,
        tdb_id: tdb_id,
        data_class_id: obj.data_class_id,
        physical_id: obj.logic_id,
        apply_power: "1",
        status: "1",
        cause: ""
      };
      if (status == "1") {
        this.axios
          .post(interfaceObj.SpecialDB_oneUpdatePower, paramList)
          .then(res => {
            if (res.data.data.result == "success") {
              this.$message({ type: "info", message: "授权成功" });
              this.loadReadList();
            } else {
              this.$message({ type: "error", message: "授权失败" });
            }
          });
      }
      //如果是拒绝，要给出拒绝原因
      if (status == "2") {
        paramList.status = "2";
        this.paramList = paramList;
        this.refuseReasonDialog = true;
        this.single = true;
      }
    },
    //批量授权或拒绝
    batchUpdatePower(status) {
      debugger;
      //判断是否勾选需要授权的资料
      if (this.multipleSelection.length > 0) {
        let tdb_id = this.searchObj.tdb_id;
        let userId = this.msgFormDialog.user_id;
        let paramList = [];
        for (let i = 0; i < this.multipleSelection.length; i++) {
          let checkVal = this.multipleSelection[i];
          //获取存储编码和数据库ID
          let data_class_id = checkVal.data_class_id;
          let physical_id = checkVal.logic_id;
          //获取授予或拒绝的权限
          let apply_power = 1;
          paramList.push({
            userId: userId,
            tdb_id: tdb_id,
            data_class_id: data_class_id,
            physical_id: physical_id,
            apply_power: apply_power,
            status: status,
            cause: ""
          });
        }
        // paramList = JSON.stringify(paramList);
        if (status == "1") {
          paramList = JSON.stringify(paramList);
          this.axios
            .post(interfaceObj.SpecialDB_batchUpdatePower, { paramList })
            .then(res => {
              if (res.data.result) {
                this.$alert(res.data.result, "温馨提示", {
                  dangerouslyUseHTMLString: true
                });
                // this.$message({ type: "info", message: res.data.result });
                this.loadReadList();
              } else {
                this.$message({ type: "error", message: "授权失败" });
              }
            });
        }
        //如果是拒绝，要给出拒绝原因
        if (status == "2") {
          this.paramList = paramList;
          this.refusereason = "";
          this.refuseReasonDialog = true;
          this.single = false;
        }
      } else {
        this.$message({ type: "warning", message: "请勾选需要操作的资料" });
        return;
      }
    },
    refuseCommit() {
      this.refuseReasonDialog = false;
      if (this.single) {
        this.paramList.cause = this.refusereason;
        this.axios
          .post(interfaceObj.SpecialDB_oneUpdatePower, this.paramList)
          .then(res => {
            if (res.data.data.result == "success") {
              this.$message({ type: "info", message: "拒绝成功" });
              this.loadReadList();
            } else {
              this.$message({ type: "error", message: "拒绝失败" });
            }
          });
      } else {
        this.paramList.forEach(element => {
          element.cause = this.refusereason;
        });
        let obj = {};
        obj.paramList = this.paramList;
        obj.cause = this.refusereason;
        this.axios
          .post(interfaceObj.SpecialDB_batchUpdatePower, obj)
          .then(res => {
            if (res.data.result) {
              this.$alert(res.data.result, "温馨提示", {
                dangerouslyUseHTMLString: true
              });
              // this.$message({ type: "info", message: res.data.result });
              this.loadReadList();
            } else {
              this.$message({ type: "error", message: "拒绝失败" });
            }
          });
      }
    },
    // 审核状态
    statusShow() {}
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
}
</style>
