<template>
  <div
    class="app-container tableStructureManageBox"
    v-bind:class="{ active: queryAll }"
  >
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
      <span class="el-icon-rank listAll" title="查看所有" @click="queryListAll">
      </span>
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
                <el-form-item label="存储编码">
                  <el-input
                    clearable
                    size="small"
                    v-model.trim="searchObj.dataclassId"
                    placeholder="存储编码"
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
                icon="el-icon-plus"
                @click="showMaterialSingle('新增资料')"
                >新增资料</el-button
              >
            </div>
          </div>
          <el-scrollbar wrap-class="scrollbar-wrapper">
            <el-table
              :data="tableData"
              border
              highlight-current-row
              @current-change="handleCurrentChange"
              ref="singleTable"
            >
              <el-table-column
                label="资料名称"
                prop="CLASS_NAME"
              ></el-table-column>
              <el-table-column
                sortable
                label="四级编码"
                prop="D_DATA_ID"
                v-if="tableStructureManageContral"
              ></el-table-column>
              <el-table-column
                sortable
                label="存储编码"
                prop="DATA_CLASS_ID"
              ></el-table-column>
              <el-table-column label="是否发布" prop="STOP_USE" width="100px">
                <template slot-scope="scope">
                  <span v-if="scope.row.STOP_USE == false">不发布</span>
                  <span v-else>发布</span>
                </template>
              </el-table-column>
              <el-table-column label="关联表信息" prop="STOP_USE" width="100px">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-view"
                    @click="showLinkTable(scope.row)"
                    >查看</el-button
                  >
                </template>
              </el-table-column>

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
              <el-table-column label="操作" width="360px">
                <template slot-scope="scope">
                  <div style="text-align: left; padding-left: 10px">
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-edit"
                      @click="showMaterialSingle('编辑资料')"
                      >编辑资料</el-button
                    >
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-tickets"
                      @click="showBaseInfo(scope.row)"
                      >基础信息</el-button
                    >
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-delete"
                      @click="deleteMaterialSingle"
                      >删除</el-button
                    >
                    <!-- 解码配置，区域信息，数据服务存在任意一个，显示服务信息按钮 -->
                    <el-button
                      type="text"
                      size="mini"
                      icon="el-icon-notebook-2"
                      @click="showServeManage(scope.row)"
                      v-if="
                        scope.row.STORAGE_TYPE &&
                        (storage[scope.row.STORAGE_TYPE].dc ||
                          storage[scope.row.STORAGE_TYPE].rg ||
                          storage[scope.row.STORAGE_TYPE].ds ||
                          storage[scope.row.STORAGE_TYPE].pl)
                      "
                      >服务信息</el-button
                    >
                  </div>
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

    <!-- 关联表信息查看 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="关联表信息"
      :visible.sync="linkTableVisible"
      width="70%"
    >
      <linkTableInfo v-if="linkTableVisible" :linkTableObj="linkTableObj" />
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
    <!-- 基础信息编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="基础信息编辑"
      :visible.sync="baseMsgEditDialog"
      width="80%"
      :before-close="handleClose"
    >
      <handleBaseMsg
        @handleClose="handleClose"
        v-if="baseMsgEditDialog"
        :handleId="handleId"
      ></handleBaseMsg>
    </el-dialog>
  </div>
</template>

<script>
//分类树
import StructureClassify from "@/views/structureManagement/materialManage/StructureClassify";
//新增、编辑资料--弹出层
import StructureMaterialSingle from "@/views/structureManagement/materialManage/StructureMaterialSingle";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/materialManage/TableManage/StructureManageTable";
// 表结构管理-公共元数据树查询的页面
import PublicDatumPage from "@/views/structureManagement/materialManage/PublicDatumPage";
// SQL建表
import handleSQL from "@/views/structureManagement/materialManage/handleSQL";
//关联表信息--弹出层
import linkTableInfo from "@/views/structureManagement/materialManage/linkTableInfo";
import {
  dataClassAll,
  databaseClass,
  logicClass,
} from "@/api/structureManagement/materialManage/StructureClassify";
import {
  getListBYIn,
  delByClass,
  pasteTable,
  dataLogicGet,
  enable,
  dataTableGet,
} from "@/api/structureManagement/materialManage/index";
import { gcl } from "@/api/structureManagement/materialManage/StructureManageTable";
import handleBaseMsg from "@/views/structureManagement/materialManage/TableManage/handleBaseMsg";
export default {
  components: {
    StructureClassify,
    StructureMaterialSingle,
    PublicDatumPage,
    StructureManageTable,
    handleSQL,
    linkTableInfo,
    handleBaseMsg,
  },
  data() {
    return {
      handleId: "",
      baseMsgEditDialog: false,
      storage: {
        //表类型展示配置
        E_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
        F_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        ME_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false,
        },
        G_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        K_E_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
        MK_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false,
        },
        NF_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        S_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          ds: false,
          pl: true,
          rg: true,
        },
        L_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
      },
      linkTableVisible: false, //关联表信息
      linkTableObj: {},
      queryAll: false, //查询所有
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
    // 查看关联表信息
    showLinkTable(row) {
      this.linkTableVisible = true;
      this.linkTableObj = row;
    },
    //查询所有
    queryListAll() {
      this.queryAll = !this.queryAll;
      let queryObj = undefined;
      if (!this.queryAll) {
        queryObj = this.searchObj;
      }
      getListBYIn(queryObj).then((response) => {
        this.tableData = response.data;
      });
    },
    // 基础信息
    showBaseInfo(row) {
      this.handleId = row.DATA_CLASS_ID;
      this.baseMsgEditDialog = true;
    },
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

    cancelHandle() {
      this.handleSQLObj = {};
      this.handleSQLDialog = false;
    },
    //显示服务信息
    showServeManage(row) {
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
    handleClose() {
      this.baseMsgEditDialog = false;
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
  &.active {
    .listAll {
      left: 10px;
    }
    #resize,
    .asideTreeStructure {
      display: none;
    }
  }
  .listAll {
    position: absolute;
    font-size: 16px;
    color: #1e9fff;
    left: 276px;
    z-index: 1000;
    cursor: pointer;
    border: 1px solid #1e9fff;
    padding: 2px 4px;
    &:hover {
      color: #fff;
      background: #1e9fff;
    }
  }
  .elMain {
    position: relative;
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
