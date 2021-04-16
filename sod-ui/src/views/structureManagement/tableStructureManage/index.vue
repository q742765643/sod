<template>
  <div class="app-container tableStructureManageBox">
    <!-- 表结构管理 -->
    <el-container id="box">
      <!-- 左侧树 -->
      <StructureClassify
        ref="classifyTree"
        :treeIdOfDR="treeIdOfDR"
        @showMaterialSingle="showMaterialSingle"
        @getTreeUrlOfTab="getTreeUrlOfTab"
        @searchFun="searchFun"
      />
      <div id="resize"></div>
      <!-- 表格 -->
      <el-main class="elMain" id="right" v-show="otherMain">
        <div class="treeTitle">
          <i class="el-icon-s-home"></i>
          {{ tableName + "数据集信息" }}
        </div>
        <div class="tableCon">
          <div class="tableTop">
            <div class="tableSearch">
              <el-form :inline="true">
                <el-form-item label="资料名称">
                  <el-input
                    clearable
                    size="small"
                    v-model.trim="searchObj.className"
                    placeholder="资料名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="四级编码">
                  <el-input
                    clearable
                    size="small"
                    v-model.trim="searchObj.dDataId"
                    placeholder="四级编码"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    size="small"
                    type="primary"
                    icon="el-icon-search"
                    @click="searchFun('search')"
                    >搜索</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
            <div class="sourceOperate">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-tickets"
                @click="showMaterialList"
                v-if="tableStructureManageContral"
                >资料概览</el-button
              >
              <el-button
                type="primary"
                size="small"
                icon="el-icon-plus"
                @click="showMaterialSingle('新增资料')"
                >新增资料</el-button
              >
              <el-button
                type="primary"
                size="small"
                icon="el-icon-edit"
                @click="showMaterialSingle('编辑资料')"
                >编辑资料</el-button
              >
              <el-button
                type="danger"
                size="small"
                icon="el-icon-delete"
                @click="deleteMaterialSingle"
                >删除资料</el-button
              >
            </div>
          </div>
          <el-scrollbar wrap-class="scrollbar-wrapper">
            <el-table
              :data="tableData"
              border
              :span-method="objectSpanMethod"
              highlight-current-row
              @current-change="handleCurrentChange"
              ref="singleTable"
            >
              <el-table-column
                label="资料名称"
                prop="CLASS_NAME"
              ></el-table-column>
              <el-table-column
                label="四级编码"
                prop="D_DATA_ID"
                v-if="tableStructureManageContral"
              ></el-table-column>
              <el-table-column
                label="数据用途类型"
                prop="LOGIC_NAME"
              ></el-table-column>
              <el-table-column
                label="存储类型"
                prop="DICT_LABEL"
              ></el-table-column>
              <el-table-column
                label="数据库"
                prop="DATABASE_NAME_F"
              ></el-table-column>
              <el-table-column
                label="专题名"
                prop="DATABASE_NAME"
              ></el-table-column>
              <el-table-column
                label="描述代码"
                prop="TABLE_DESC"
                :show-overflow-tooltip="true"
                v-if="
                  this.treeRefreshData &&
                  this.treeRefreshData.id &&
                  this.treeRefreshData.id.substring(0, 1) == 'F'
                "
              ></el-table-column>
              <el-table-column label="操作" width="320px">
                <template slot-scope="scope">
                  <el-button
                    size="small"
                    @click="showStructureManage(scope.row)"
                  >
                    <i
                      class="btnRound blueRound"
                      v-if="scope.row.TABLECOUNT > scope.row.roundCount"
                    ></i>
                    <i class="btnRound orangRound" v-else></i>
                    表结构管理
                  </el-button>
                  <el-button size="small" @click="showSQLManage(scope.row)"
                    >SQL建表</el-button
                  >
                  <el-dropdown @command="handleCommand">
                    <el-button size="small">
                      复用表信息
                      <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item
                        :command="composeValue('copy', scope.row)"
                        >复制表信息</el-dropdown-item
                      >
                      <el-dropdown-item
                        :command="composeValue('paste', scope.row)"
                        >粘贴表信息</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>
        </div>
      </el-main>
      <!-- 公共元数据 -->
      <el-main class="elMain" id="right" v-show="publicMain">
        <PublicDatumPage
          ref="PublicDatumTable"
          @showMaterialSingle="showMaterialSingle"
          :handleObj="handleObj"
        />
      </el-main>
    </el-container>
    <!-- 资料概览弹出层 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="资料概览"
      :visible.sync="materialListVisible"
      width="70%"
    >
      <StructureMaterialList v-if="materialListVisible" />
    </el-dialog>
    <!-- 新增、编辑资料  资料分类树 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="materialSingleTitle"
      :visible.sync="materialSingleVisible"
      width="90%"
      top="5vh"
    >
      <StructureMaterialSingle
        v-if="materialSingleVisible"
        :isSourceTree="isSourceTree"
        :editNodeId="editNodeId"
        :editMaterial="editMaterial"
        @addOrEditSuccess="addOrEditSuccess"
        @closeMaterialDialog="closeMaterialDialog"
        ref="myHandleChildren"
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

    <!-- SQL建表 -->
    <el-dialog
      :close-on-click-modal="false"
      title="SQL建表"
      :visible.sync="handleSQLDialog"
      width="80%"
      v-dialogDrag
    >
      <handleSQL
        @cancelHandle="cancelHandle"
        v-if="handleSQLDialog"
        :handleSQLObj="handleSQLObj"
      ></handleSQL>
    </el-dialog>
  </div>
</template>

<script>
//分类树
import StructureClassify from "@/views/structureManagement/tableStructureManage/StructureClassify";
//资料概览--弹出层
import StructureMaterialList from "@/views/structureManagement/tableStructureManage/StructureMaterialList";
//新增、编辑资料--弹出层
import StructureMaterialSingle from "@/views/structureManagement/tableStructureManage/StructureMaterialSingle";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/tableStructureManage/TableManage/StructureManageTable";
// 表结构管理-公共元数据树查询的页面
import PublicDatumPage from "@/views/structureManagement/tableStructureManage/PublicDatumPage";
// SQL建表
import handleSQL from "@/views/structureManagement/tableStructureManage/handleSQL";
import {
  dataClassAll,
  databaseClass,
  logicClass,
} from "@/api/structureManagement/tableStructureManage/StructureClassify";
import {
  getListBYIn,
  delByClass,
  pasteTable,
  dataLogicGet,
  enable,
} from "@/api/structureManagement/tableStructureManage/index";
import { gcl } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  components: {
    StructureClassify,
    StructureMaterialSingle,
    PublicDatumPage,
    StructureManageTable,
    StructureMaterialList,
    handleSQL,
  },
  data() {
    return {
      tableStructureManageContral: false,
      treeIdOfDR: this.$route.params.treeIdOfDR,
      searchObj: {
        stringList: "",
        className: "",
        dDataId: "",
      },
      tableData: [],
      dataTotal: 0,
      tableName: "",
      materialListVisible: false, //资料概览
      isSourceTree: true, //资料树与资料的区分
      materialSingleVisible: false, //新增资料分类树，新增资料弹出层控制
      materialSingleTitle: "", //资料分类树  资料弹出层标题
      editNodeId: "", //资料树编辑id
      editMaterial: "", //资料编辑id
      structureManageVisible: false, //表结构管理弹出层
      structureManageTitle: "", //表结构管理弹出层title
      rowData: {},
      otherMain: true,
      publicMain: false,
      handleObj: {},
      currentRow: [],
      // 合并单元格
      spanArr: [],
      position: 0,
      // SQL建表
      handleSQLObj: {},
      handleSQLDialog: false,
      // treeUrl: interfaceObj.TableStructure_dataTypeTree, //区分刷新那颗树
      whichTree: "资料分类树", //区分刷新那颗树
      treeRefreshData: {},
    };
  },
  created() {
    enable().then((res) => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
  },
  mounted() {
    var resize = document.getElementById("resize");
    var left = document.getElementById("left");
    var right = document.getElementById("right");
    var box = document.getElementById("box");
    resize.onmousedown = function (e) {
      var startX = e.clientX;
      resize.left = resize.offsetLeft;
      document.onmousemove = function (e) {
        var endX = e.clientX;

        var moveLen = resize.left + (endX - startX);
        var maxT = box.clientWidth - resize.offsetWidth;
        if (moveLen < 150) moveLen = 150;
        if (moveLen > maxT - 150) moveLen = maxT - 150;

        resize.style.left = moveLen;
        left.style.width = moveLen + "px";
        right.style.width = box.clientWidth - moveLen - 5 + "px";
      };
      document.onmouseup = function (evt) {
        document.onmousemove = null;
        document.onmouseup = null;
        resize.releaseCapture && resize.releaseCapture();
      };
      resize.setCapture && resize.setCapture();
      return false;
    };
  },
  methods: {
    //新增，编辑资料树  或者  资料
    showMaterialSingle(operateType) {
      console.log(operateType);
      if (
        operateType.title === "新增资料树节点" ||
        operateType.title === "编辑资料树节点"
      ) {
        this.materialSingleTitle = operateType.title;
        this.isSourceTree = true;
        this.editNodeId = operateType.editNodeId;
        this.whichTree = operateType.tree; //获取是哪棵树
      } else if (operateType === "新增资料") {
        this.materialSingleTitle = operateType;
        this.editMaterial = "";
        this.isSourceTree = false;
      } else if (operateType === "编辑资料") {
        if (this.currentRow == null || this.currentRow.length == 0) {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return false;
        } else {
          this.materialSingleTitle = operateType;
          this.editMaterial = this.currentRow[0].DATA_CLASS_ID;
          this.isSourceTree = false;
          // 获取详情
        }
      }
      this.materialSingleVisible = true;
    },
    // 左侧tab点击时获取树的url
    getTreeUrlOfTab(tree) {
      this.whichTree = tree;
      this.searchObj.stringList = "";
      this.searchObj.className = "";
      this.searchObj.dDataId = "";
    },
    searchFun(checkedNodeStr, treeRefreshData) {
      if (treeRefreshData) {
        this.tableName = treeRefreshData.name;
        this.treeRefreshData = treeRefreshData;
      }
      // this.searchObj.stringList = "";
      if (checkedNodeStr && checkedNodeStr.style) {
        this.handleObj = checkedNodeStr;
        this.otherMain = false;
        this.publicMain = true;
        this.$refs.PublicDatumTable.initRadioGroup(this.handleObj);
      } else {
        this.otherMain = true;
        this.publicMain = false;
        if (checkedNodeStr && checkedNodeStr != "search") {
          this.searchObj.stringList = checkedNodeStr;
        } else if (checkedNodeStr == "" && checkedNodeStr != "search") {
          this.searchObj.stringList = "";
        }
        console.log(this.searchObj);
        getListBYIn(this.searchObj).then((response) => {
          this.tableData = response.data;
          if (this.tableData.length > 0) {
            this.tableData.forEach((item, index) => {
              item.roundCount =
                item.STORAGE_TYPE == "K_E_table"
                  ? 1
                  : item.STORAGE_TYPE == "MK_table"
                  ? 1
                  : 0;
            });
            this.rowspan();
          }
          if (this.currentRow.length > 0) {
            this.tableData.forEach((element, index) => {
              if (element.DATA_CLASS_ID == this.currentRow[0].DATA_CLASS_ID) {
                this.$refs.singleTable.setCurrentRow(this.tableData[index]);
              }
            });
          } else {
            this.$refs.singleTable.setCurrentRow(this.tableData[0]);
          }
        });
      }
    },
    deleteMaterialSingle() {
      if (this.currentRow.length == 0) {
        this.$message({
          type: "error",
          message: "请选择一条数据",
        });
      } else {
        this.$confirm(
          "是否删除资料" + this.currentRow[0].CLASS_NAME,
          "温馨提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            delByClass({ dataClassId: this.currentRow[0].DATA_CLASS_ID }).then(
              (response) => {
                if (response.code == 200) {
                  this.$message({
                    type: "success",
                    message: "删除成功",
                  });
                  this.searchFun("search");
                }
              }
            );
          })
          .catch(() => {});
      }
    },
    composeValue(item, row) {
      return {
        button: item,
        row: row,
      };
    },
    cancelHandle() {
      this.handleSQLObj = {};
      this.handleSQLDialog = false;
    },
    async handleCommand(command) {
      if (command.button == "copy") {
        sessionStorage.setItem(
          "copyIndexTableColumn",
          JSON.stringify(command.row)
        );
        this.$message({
          message: "复制成功",
          type: "success",
        });
      } else if (command.button == "paste") {
        let copyObj = JSON.parse(
          sessionStorage.getItem("copyIndexTableColumn")
        );
        if (!copyObj) {
          this.$message({
            message: "请选择一条数据",
            type: "error",
          });
        }
        if (command.row.STORAGE_TYPE !== copyObj.STORAGE_TYPE) {
          this.$message({
            message: "复制与粘贴表存储类型不一致！",
            type: "info",
          });
          return;
        }
        let msg = "";
        let resulet = await gcl({ classLogic: command.row.LOGIC_ID }).then(
          (response) => {
            if (response.code == 200) {
              return response.data;
            } else {
              this.$message({
                message: res.msg,
                type: "error",
              });
            }
          }
        );
        if (resulet.length > 0) {
          msg = "删除已有表结构并";
        }
        let pasteObj = {};
        pasteObj.copyId = copyObj.LOGIC_ID;
        pasteObj.pasteId = command.row.LOGIC_ID;
        console.log(pasteObj);
        this.$confirm(
          "确认" + msg + "粘贴表结构(" + copyObj.CLASS_NAME + ")?",
          "温馨提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            pasteTable(pasteObj).then((res) => {
              if (res.code == 200) {
                this.$message({
                  message: "粘贴成功",
                  type: "success",
                });
                this.searchFun("search");
              } else {
                this.$message({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          })
          .catch(() => {});
      }
    },
    //显示表结构管理
    showStructureManage(row) {
      this.rowData = row;
      console.log(row);
      this.structureManageTitle = row.CLASS_NAME;
      this.structureManageVisible = true;
    },
    // 关闭
    closeStructureManage() {
      this.searchFun("search");
      this.structureManageVisible = false;
    },
    showSQLManage(row) {
      if (row.TABLECOUNT > row.roundCount) {
        this.handleSQLObj = row;
        this.handleSQLDialog = true;
      } else {
        this.$message({
          message: "表结构不存在，请先创建",
          type: "info",
        });
      }
    },
    // 选中行
    handleCurrentChange(currentRow) {
      if (currentRow == null) {
        return;
      }
      this.currentRow = [];
      this.tableData.forEach((item, index) => {
        let obj = {};
        if (item.DATA_CLASS_ID != null && currentRow.DATA_CLASS_ID != null) {
          if (item.DATA_CLASS_ID == currentRow.DATA_CLASS_ID) {
            this.currentRow.push(item);
          }
        }
      });
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      //表格合并行
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
      if (columnIndex === 1) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
    // 判断哪些需要合并
    rowspan() {
      this.spanArr = [];
      this.position = 0;
      this.tableData.forEach((item, index) => {
        if (index === 0) {
          this.spanArr.push(1);
          this.position = 0;
        } else {
          if (
            this.tableData[index].DATA_CLASS_ID ===
            this.tableData[index - 1].DATA_CLASS_ID
          ) {
            this.spanArr[this.position] += 1;
            this.spanArr.push(0);
          } else {
            this.spanArr.push(1);
            this.position = index;
          }
        }
      });
    },
    //资料概览
    showMaterialList() {
      this.materialListVisible = true;
    },
    //新增、编辑后刷新数据
    addOrEditSuccess(operateType) {
      // this.currentRow = [];
      if (operateType == "handleTree") {
        this.$refs.classifyTree.initMethodsTree(this.whichTree);
      } else {
        if (this.materialSingleTitle == "编辑资料") {
          this.searchFun("search");
        } else {
          this.$refs.classifyTree.initMethodsTree(this.whichTree);
        }
      }
      this.materialSingleVisible = false;
    },
    //关闭新增或编辑资料弹出层
    closeMaterialDialog() {
      this.materialSingleVisible = false;
    },
  },
};
</script>
<style lang="scss">
.tableStructureManageBox {
  padding: 20px 5px;
  .elMain {
    float: left;
    border: 1px solid #ebeef5;
    background-color: #fff;
    margin-right: 10px;
    box-sizing: border-box;
    padding: 0px;
    height: calc(100vh - 170px);
    overflow: hidden;
    margin-bottom: 0;
    .treeTitle {
      border-bottom: 1px solid #ebeef5;
      padding: 10px;
      background: #eee;
      font-size: 14px;

      i {
        font-size: 16px;
        color: #1e9fff;
      }
    }
    .el-scrollbar {
      height: calc(100vh - 280px);
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
      .el-button + .el-button {
        margin-left: 0;
      }
    }

    .tableTop {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      align-items: center;
      .sourceOperate {
        display: flex;
        justify-content: flex-end;
        .el-button {
          margin-left: 0px;
          border-radius: 0px;
          border-left: 1px solid #9dd5f6;
          padding: 9px 6px;
        }

        .el-button:first-child {
          border-left: 1px solid #409eff;
          border-top-left-radius: 2px;
          border-bottom-left-radius: 2px;
        }

        .el-button:last-child {
          border-top-right-radius: 2px;
          border-bottom-right-radius: 2px;
        }
      }
    }

    .tableCon {
      box-sizing: border-box;
      padding: 10px;
    }

    .el-form-item {
      margin-bottom: 0px;
    }

    .el-table {
      .cell {
        padding-left: 0px;
        padding-right: 0px;
      }
    }
    .el-dropdown {
      margin-left: 1px;
    }
    .btnRound {
      border-radius: 50%;
      margin-top: 4px;
      margin-right: 4px;
      height: 6px;
      width: 6px;
      display: inline-block;
      vertical-align: top;
      overflow: hidden;
    }
    .blueRound {
      background: #1e9fff;
    }
    .orangRound {
      background: #ff5722;
    }
  }
  #resize {
    width: 10px;
    height: calc(100vh - 170px);
    cursor: w-resize;
    float: left;
  }
}
.scrollDialog {
  .el-dialog__header {
    padding: 10px 20px;
    padding-left: 44px;
    margin-bottom: 0;
  }
  .el-dialog__header:before {
    top: 12px;
  }
  .el-dialog__headerbtn {
    top: 12px;
  }
}
</style>
