<template>
  <div
    class="app-container dataListManageBox"
    v-bind:class="{ active: queryAll }"
  >
    <el-form
      :model="searchObj"
      ref="queryForm"
      :inline="true"
      class="searchBox"
    >
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
          v-model.trim="searchObj.dataId"
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
      <el-form-item label="数据库" prop="databasePid">
        <el-select
          size="small"
          filterable
          v-model="searchObj.databasePid"
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
          v-model="searchObj.databaseId"
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
      <el-form-item label="表名称:" prop="tableName">
        <el-input
          clearable
          size="small"
          v-model.trim="searchObj.tableName"
          placeholder="请输入表名称"
        />
      </el-form-item>
      <!-- <el-form-item label="数据权限" prop="databaseId">
        <el-select
          size="small"
          filterable
          v-model="searchObj.databaseId"
          placeholder="请选择"
        >
          <el-option
            v-for="(citem, cindex) in specialList"
            :key="cindex"
            :label="citem.databaseName"
            :value="citem.id"
          ></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="展示纬度">
        <el-radio-group
          v-model="searchObj.tableDataRadio"
          @change="changeTableDataRadio"
        >
          <el-radio :label="'资料'">资料</el-radio>
          <el-radio :label="'数据表'">数据表</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-button size="mini" type="primary" @click="searchFun('search')"
          >查询</el-button
        >
        <el-button
          size="mini"
          type="primary"
          @click="showMaterialSingle('新增资料')"
          >数据注册</el-button
        >
        <el-button size="mini" type="primary" @click="handleTableMethods('add')"
          >数据表注册</el-button
        >
        <!-- 资料的时候不显示回收站 -->
        <el-button
          size="mini"
          type="primary"
          v-show="searchObj.tableDataRadio == '数据表'"
          @click="goPage"
          >回收站</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表结构管理 -->
    <el-container id="box">
      <!-- 左侧树 -->
      <StructureClassify
        ref="classifyTree"
        :treeIdOfDR="treeIdOfDR"
        :tableDataRadio="searchObj.tableDataRadio"
        @showMaterialSingle="showMaterialSingle"
        @getTreeUrlOfTab="getTreeUrlOfTab"
        @searchFun="searchFun"
      />
      <div id="resize">&lt; &gt;</div>
      <!-- 表格 -->

      <el-main class="elMain" id="right" v-show="otherMain">
        <span
          class="el-icon-s-grid listAll"
          title="查看所有"
          @click="queryListAll"
        ></span>
        <div class="treeTitle">
          <i class="el-icon-s-home"></i>
          {{ tableName + "数据集信息" }}
        </div>
        <div class="tableCon">
          <el-table
            v-if="searchObj.tableDataRadio == '资料'"
            :height="tableH"
            :data="tableData"
            border
            highlight-current-row
            @current-change="handleCurrentChange"
            ref="singleTable"
            :span-method="objectSpanMethod"
          >
            <!-- 资料纬度 start -->
            <el-table-column width="140" label="资料名称" prop="CLASS_NAME">
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  @click="showMaterialSingle('查看资料', scope.row)"
                  >{{ scope.row.CLASS_NAME }}</el-link
                >
              </template>
            </el-table-column>
            <el-table-column
              width="140"
              sortable
              label="四级编码"
              prop="D_DATA_ID"
              v-if="tableStructureManageContral"
            ></el-table-column>
            <el-table-column
              width="140"
              sortable
              label="存储编码"
              prop="DATA_CLASS_ID"
            ></el-table-column>
            <el-table-column label="关联数据表">
              <el-table-column label="数据表名称" prop="TABLE_NAME" width="160">
              </el-table-column>
              <el-table-column label="数据库" prop="DATABASE_NAME" width="140">
              </el-table-column>
              <el-table-column label="专题库" prop="SCHEMA_NAME_CN">
              </el-table-column>
            </el-table-column>
            <el-table-column label="数据权限" width="360">
              <el-table-column label="数据归属" prop="">
                <template slot-scope="scope">
                  <span v-if="scope.row.SCHEMA_NAME_CN == '基础库'"
                    >信息中心</span
                  >
                  <span v-else>{{ scope.row.NICK_NAME }}</span>
                </template>
              </el-table-column>
              <el-table-column label="权限管理" prop="">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-view"
                    @click="showPower(scope.row)"
                    >查看</el-button
                  >
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="存储结构管理" prop="" width="560">
              <template slot-scope="scope">
                <!-- 存储结构 -->
                <el-button
                  disabled
                  v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 3"
                  size="mini"
                >
                  <i class="btnRound orangRound"></i>存储结构
                </el-button>
                <el-button
                  v-else
                  size="mini"
                  @click="handledDBMethods(scope.row)"
                >
                  <i
                    class="btnRound blueRound"
                    v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 1"
                  ></i>
                  <i class="btnRound orangRound" v-else></i>存储结构
                </el-button>
                <!-- 数据同步 -->
                <el-button
                  disabled
                  v-if="scope.row.SYNC_IDENTIFIER == 3"
                  size="mini"
                >
                  <i class="btnRound orangRound"></i>数据同步
                </el-button>
                <el-button
                  v-else
                  size="mini"
                  @click="handlSyncMethods(scope.row)"
                >
                  <i class="btnRound blueRound" v-if="scope.row.SYNC_ID"></i>
                  <i class="btnRound orangRound" v-else></i>数据同步
                </el-button>
                <!-- 迁移 -->

                <el-button
                  v-if="scope.row.MOVE_ST === 1"
                  size="mini"
                  @click="handlMoveMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.MOVE_ID"></i>
                  <i class="btnRound orangRound" v-else></i>迁移
                </el-button>

                <!-- 清除 -->
                <el-button
                  v-if="scope.row.CLEAR_ST === 1"
                  size="mini"
                  @click="handlClearMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
                  <i class="btnRound orangRound" v-else></i>清除
                </el-button>

                <!-- 备份 -->
                <el-button
                  v-if="scope.row.BACKUP_ST === 1"
                  size="mini"
                  @click="handleBackUpMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.BACKUP_ID"></i>
                  <i class="btnRound orangRound" v-else></i>备份
                </el-button>

                <!-- 恢复 -->
                <el-button
                  disabled
                  v-if="scope.row.ARCHIVING_IDENTIFIER == 3"
                  size="mini"
                  >恢复</el-button
                >
                <el-button
                  v-else
                  size="mini"
                  @click="handlRecoverMethods(scope.row)"
                  >恢复</el-button
                >
              </template>
            </el-table-column>
            <el-table-column label="资料操作" width="200px">
              <template slot-scope="scope">
                <div style="text-align: left; padding-left: 10px">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-edit"
                    @click="showMaterialSingle('编辑资料', scope.row)"
                    >编辑资料</el-button
                  >
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    @click="deleteMaterialSingle(scope.row)"
                    >删除</el-button
                  >
                </div>
              </template>
            </el-table-column>
          </el-table>
          <!-- 数据表纬度 start -->
          <el-table
            v-if="searchObj.tableDataRadio == '数据表'"
            :height="tableH"
            :data="tableData"
            border
            highlight-current-row
            @current-change="handleCurrentChange"
            ref="singleTable"
            :span-method="objectSpanMethod"
          >
            <el-table-column label="数据表名称" prop="TABLE_NAME" width="160">
              <template slot-scope="scope">
                <span>{{ scope.row.TABLE_NAME }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数据库" prop="DATABASE_NAME" width="140">
            </el-table-column>
            <el-table-column label="专题库" prop="SCHEMA_NAME_CN">
            </el-table-column>
            <el-table-column label="关联资料">
              <el-table-column width="140" label="资料名称" prop="CLASS_NAME">
                <template slot-scope="scope">
                  <el-link
                    @click="showMaterialSingle('查看资料', scope.row)"
                    type="primary"
                    v-for="(item, index) in scope.row.CLASS_NAME"
                    :key="index"
                    >{{ item }}</el-link
                  >
                </template>
              </el-table-column>
              <el-table-column
                width="140"
                sortable
                label="四级编码"
                prop="D_DATA_ID"
                v-if="tableStructureManageContral"
              >
                <template slot-scope="scope">
                  <p
                    type="primary"
                    v-for="(item, index) in scope.row.D_DATA_ID"
                    :key="index"
                  >
                    {{ item }}
                  </p>
                </template>
              </el-table-column>
              <el-table-column
                width="140"
                sortable
                label="存储编码"
                prop="DATA_CLASS_ID"
              >
                <template slot-scope="scope">
                  <p
                    type="primary"
                    v-for="(item, index) in scope.row.DATA_CLASS_ID"
                    :key="index"
                  >
                    {{ item }}
                  </p>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="数据权限" width="360">
              <el-table-column label="数据归属" prop="">
                <template slot-scope="scope">
                  <span v-if="scope.row.SCHEMA_NAME_CN == '基础库'"
                    >信息中心</span
                  >
                  <span v-else>{{ scope.row.NICK_NAME }}</span>
                </template>
              </el-table-column>
              <el-table-column label="权限管理" prop="">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-view"
                    @click="showPower(scope.row)"
                    >查看</el-button
                  >
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column label="存储结构管理" prop="" width="560">
              <template slot-scope="scope">
                <!-- 存储结构 -->
                <el-button
                  disabled
                  v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 3"
                  size="mini"
                >
                  <i class="btnRound orangRound"></i>存储结构
                </el-button>
                <el-button
                  v-else
                  size="mini"
                  @click="handledDBMethods(scope.row)"
                >
                  <i
                    class="btnRound blueRound"
                    v-if="scope.row.STORAGE_DEFINE_IDENTIFIER == 1"
                  ></i>
                  <i class="btnRound orangRound" v-else></i>存储结构
                </el-button>
                <!-- 数据同步 -->
                <el-button
                  disabled
                  v-if="scope.row.SYNC_IDENTIFIER == 3"
                  size="mini"
                >
                  <i class="btnRound orangRound"></i>数据同步
                </el-button>
                <el-button
                  v-else
                  size="mini"
                  @click="handlSyncMethods(scope.row)"
                >
                  <i class="btnRound blueRound" v-if="scope.row.SYNC_ID"></i>
                  <i class="btnRound orangRound" v-else></i>数据同步
                </el-button>
                <!-- 迁移 -->

                <el-button
                  v-if="scope.row.MOVE_ST === 1"
                  size="mini"
                  @click="handlMoveMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.MOVE_ID"></i>
                  <i class="btnRound orangRound" v-else></i>迁移
                </el-button>

                <!-- 清除 -->
                <el-button
                  v-if="scope.row.CLEAR_ST === 1"
                  size="mini"
                  @click="handlClearMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.CLEAR_ID"></i>
                  <i class="btnRound orangRound" v-else></i>清除
                </el-button>

                <!-- 备份 -->
                <el-button
                  v-if="scope.row.BACKUP_ST === 1"
                  size="mini"
                  @click="handleBackUpMethods(scope.row)"
                >
                  <!-- 在这里判断颜色，在函数里判断是哪种迁移清除 -->
                  <i class="btnRound blueRound" v-if="scope.row.BACKUP_ID"></i>
                  <i class="btnRound orangRound" v-else></i>备份
                </el-button>

                <!-- 恢复 -->
                <el-button
                  disabled
                  v-if="scope.row.ARCHIVING_IDENTIFIER == 3"
                  size="mini"
                  >恢复</el-button
                >
                <el-button
                  v-else
                  size="mini"
                  @click="handlRecoverMethods(scope.row)"
                  >恢复</el-button
                >
              </template>
            </el-table-column>

            <el-table-column label="数据表操作" width="100px">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="mini"
                  icon="el-icon-delete"
                  @click="deleteDataTable(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <!-- 数据表纬度 end -->
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
        ref="StructureMaterialSingleRef"
        :isSourceTree="isSourceTree"
        :editNodeId="editNodeId"
        :editMaterial="editMaterial"
        :editTableName="editTableName"
        :isFooterShow="isFooterShow"
        @addOrEditSuccess="addOrEditSuccess"
        @closeMaterialDialog="closeMaterialDialog"
      />
    </el-dialog>
    <!-- 表新增编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogMsgTitle"
      :visible.sync="tableVisible"
      width="700px"
      v-dialogDrag
    >
      <handleTable
        v-if="tableVisible"
        :handleObj="handleMsgObj"
        @cancelHandle="handleTableDialog"
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
        :tableBaseInfo="tableBaseInfo"
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

    <!-- 查看权限 -->
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      :title="powerTitle"
      :visible.sync="showPowerDialog"
      width="80%"
      :before-close="handleClose"
    >
      <el-table :data="powerTableData" style="width: 100%">
        <el-table-column prop="DATA_CLASS_ID" label="存储编码">
        </el-table-column>
        <el-table-column prop="NICK_NAME" label="用户名"> </el-table-column>
        <el-table-column prop="AUTHORIZE" label="状态">
          <template slot-scope="scope">
            <span v-if="scope.row.AUTHORIZE == 1">通过</span>
            <span v-else>拒绝</span>
          </template>
        </el-table-column>
        <el-table-column prop="APPLY_AUTHORITY" label="权限">
          <template slot-scope="scope">
            <span v-if="scope.row.APPLY_AUTHORITY == 1">读</span>
            <span v-else>写</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
//分类树
import StructureClassify from "@/views/structureManagement/dataList/StructureClassify";
//新增、编辑资料--弹出层
import StructureMaterialSingle from "@/views/structureManagement/dataList/StructureMaterialSingle";
//表结构管理--弹出层
import StructureManageTable from "@/views/structureManagement/dataList/TableManage/StructureManageTable";
// 表结构管理-公共元数据树查询的页面
import PublicDatumPage from "@/views/structureManagement/dataList/PublicDatumPage";
// SQL建表
import handleSQL from "@/views/structureManagement/dataList/handleSQL";
//关联表信息--弹出层
import linkTableInfo from "@/views/structureManagement/dataList/linkTableInfo";
//表新增编辑
import handleTable from "@/views/structureManagement/dataList/handleTable";
import {
  getPageDataclassInfo,
  getPageTableInfo,
  delByClass,
  tombstone,
  enable,
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
  findAuthorityList,
} from "@/api/structureManagement/dataList/index";
import handleBaseMsg from "@/views/structureManagement/dataList/TableManage/handleBaseMsg";
export default {
  components: {
    StructureClassify,
    StructureMaterialSingle,
    PublicDatumPage,
    StructureManageTable,
    handleSQL,
    linkTableInfo,
    handleBaseMsg,
    handleTable,
  },
  data() {
    return {
      handleMsgObj: {},
      dialogMsgTitle: "",
      tableVisible: false,
      tableBaseInfo: {},
      powerTitle: "",
      powerTableData: [],
      showPowerDialog: false,
      tableH: null,
      DatabaseDefineList: [],
      specialList: [],
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
        classIds: "",
        className: "",
        dataId: "",
        tableDataRadio: "资料",
      },
      tableData: [],
      dataTotal: 0,
      tableName: "",
      isSourceTree: true, //资料树与资料的区分
      materialSingleVisible: false, //新增资料分类树，新增资料弹出层控制
      materialSingleTitle: "", //资料分类树  资料弹出层标题
      editNodeId: "", //资料树编辑id
      editMaterial: "", //资料编辑id
      editTableName: "", //资料编辑表名称
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
      isFooterShow: true, //资料是否显示footer
    };
  },
  async created() {
    await getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
    enable().then((res) => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
  },
  mounted() {
    // 动态设置高度
    this.$nextTick(() => {
      let qH = document.getElementsByClassName("searchBox")[0].offsetHeight;
      this.tableH = document.body.clientHeight - qH + "px";
      document.getElementsByClassName(
        "elTreeAsideBox"
      )[0].style.height = this.tableH;
    });
    // 拉宽拉窄
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
    handleTableDialog(info) {
      if (info) {
        this.structureManageTitle = "";
        this.structureManageVisible = true;
        this.rowData = info;
        this.tableBaseInfo = info;
      }
      this.tableVisible = false;
    },
    // 新增/编辑
    handleTableMethods(info) {
      if (info == "add") {
        this.handleMsgObj = {};
        this.dialogMsgTitle = "新增";
      } else {
        this.dialogMsgTitle = "编辑";
      }
      this.tableVisible = true;
    },
    //查看权限
    showPower(row) {
      findAuthorityList({
        databaseId: row.DATABASE_ID,
        dataclassId: row.DATA_CLASS_ID,
      }).then((res) => {
        this.powerTitle = row.CLASS_NAME + "权限查看";
        this.powerTableData = res.data;
        this.showPowerDialog = true;
      });
    },
    // 切换展示纬度
    changeTableDataRadio() {
      if (this.searchObj.tableDataRadio == "资料") {
        this.$refs.classifyTree.handleTabClick({ label: "资料分类树" });
      } else {
        this.$refs.classifyTree.handleTabClick({ label: "数据库分类树" });
      }
    },
    //切换数据库查询专题库
    handleFindSpec(val) {
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },
    // 查看关联表信息
    showLinkTable(row) {
      this.linkTableVisible = true;
      this.linkTableObj = row;
    },
    //查询所有
    queryListAll() {
      this.queryAll = !this.queryAll;
      let queryObj = {};
      if (this.queryAll) {
        queryObj = {
          className: this.searchObj.className,
          dataId: this.searchObj.dataId,
          tableDataRadio: this.searchObj.tableDataRadio,
          all: 1,
        };
      } else {
        queryObj = this.searchObj;
      }
      this.queryMethods(queryObj);
    },
    queryMethods(queryObj) {
      let queryUrl = getPageDataclassInfo;
      if (this.searchObj.tableDataRadio == "数据表") {
        queryUrl = getPageTableInfo;
      }
      console.log(queryObj);
      queryUrl(queryObj).then((response) => {
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
          this.rowspanMethods();
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
    },
    // 基础信息
    showBaseInfo(row) {
      this.handleId = row.DATA_CLASS_ID;
      this.baseMsgEditDialog = true;
    },
    //新增，编辑资料树  或者  资料
    showMaterialSingle(operateType, scopeRow) {
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
        this.editTableName = "";
        this.isSourceTree = false;
      } else if (operateType === "编辑资料" || operateType === "查看资料") {
        this.currentRow = [];
        this.currentRow.push(scopeRow);
        this.materialSingleTitle = operateType;
        this.editMaterial = this.currentRow[0].DATA_CLASS_ID;
        this.editTableName = this.currentRow[0].TABLE_NAME;
        this.isSourceTree = false;
        if (operateType === "查看资料") {
          this.isFooterShow = false;
        }
        // 获取详情
      }
      this.materialSingleVisible = true;
    },
    // 左侧tab点击时获取树的url
    getTreeUrlOfTab(tree) {
      this.whichTree = tree;
      this.searchObj.classIds = "";
      this.searchObj.className = "";
      this.searchObj.dataId = "";
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
          this.searchObj.classIds = checkedNodeStr;
        } else if (checkedNodeStr == "" && checkedNodeStr != "search") {
          this.searchObj.classIds = "";
        }
        console.log(this.searchObj);
        this.queryMethods(this.searchObj);
      }
    },
    deleteMaterialSingle(row) {
      this.$confirm("是否删除资料" + row.CLASS_NAME, "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
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
    },
    // 删除数据表
    deleteDataTable(row) {
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
    // 存储结构
    handledDBMethods(row) {
      this.rowData = row;
      this.structureManageTitle = row.TABLE_NAME;
      this.structureManageVisible = true;
    },
    handleClose() {
      this.showPowerDialog = false;
      this.baseMsgEditDialog = false;
    },
    //关闭新增或编辑资料弹出层
    closeMaterialDialog() {
      this.materialSingleVisible = false;
    },
    goPage() {
      this.$router.push({
        name: "回收站",
      });
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      //表格合并
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
      if (this.searchObj.tableDataRadio == "资料") {
        if (columnIndex === 1 || columnIndex === 9) {
          const _row = this.spanArr[rowIndex];
          const _col = _row > 0 ? 1 : 0;
          return {
            rowspan: _row,
            colspan: _col,
          };
        }
      }

      /*     if (this.searchObj.tableDataRadio == "数据表") {
        if (
          columnIndex === 6 ||
          columnIndex === 7 ||
          columnIndex === 8 ||
          columnIndex === 9
        ) {
          const _row = this.spanArr[rowIndex];
          const _col = _row > 0 ? 1 : 0;
          return {
            rowspan: _row,
            colspan: _col,
          };
        }
      } */
    },
    // 判断哪些需要合并
    rowspanMethods() {
      this.spanArr = [];
      this.position = 0;
      this.tableData.forEach((item, index) => {
        if (index === 0) {
          this.spanArr.push(1);
          this.position = 0;
        } else {
          if (this.searchObj.tableDataRadio == "资料") {
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
          if (this.searchObj.tableDataRadio == "数据表") {
            if (
              this.tableData[index].TABLE_NAME ===
              this.tableData[index - 1].TABLE_NAME
            ) {
              this.spanArr[this.position] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              this.position = index;
            }
          }
        }
      });
      // console.log(this.spanArr);
    },
  },
};
</script>
<style lang="scss">
.dataListManageBox {
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
    color: #fff;
    background: #1e9fff;
    left: 0;
    z-index: 1000;
    cursor: pointer;
    border: 1px solid #1e9fff;
    padding: 2px 4px;
    &:hover {
      color: #fff;
      opacity: 0.8;
    }
  }
  .searchBox {
    margin-bottom: 12px;
    .el-form-item {
      margin-bottom: 12px;
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
    // height: calc(100vh - 170px);
    overflow: hidden;
    margin-bottom: 0;

    .treeTitle {
      border-bottom: 1px solid #ebeef5;
      padding: 10px;
      background: #eee;
      font-size: 14px;
      padding-left: 34px;

      i {
        font-size: 16px;
        color: #1e9fff;
      }
    }
    .el-scrollbar {
      height: calc(100vh - 236px);
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }

    .tableCon {
      box-sizing: border-box;
      padding: 10px;
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
    padding-top: calc(50vh - 100px);
    background: rgba(223, 230, 236, 0.75);
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
