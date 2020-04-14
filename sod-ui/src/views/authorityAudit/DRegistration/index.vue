<template>
  <div class="app-container">
    <!-- 数据注册审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="数据分类：">
        <el-select v-model="queryParams.DDataId" placeholder>
          <el-option key="index" label="全部" value></el-option>
          <el-option
            v-for="(item,index) in dbtypeselect"
            :key="index"
            :label="item.class_name"
            :value="item.data_class_id"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="审核状态：">
        <el-select v-model="queryParams.examineStatus" placeholder>
          <el-option label="待审核" value="1"></el-option>
          <el-option label="通过" value="2"></el-option>
          <el-option label="不通过" value="3"></el-option>
          <el-option label="待删除" value="4"></el-option>
          <el-option label="已失效" value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['DRegistration:role:add']"
        >增量同步元数据</el-button>
      </el-col>
    </el-row>

    <el-table class="tableList" v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" :index="table_index" min-width="10" label=" "></el-table-column>
      <el-table-column prop="D_DATA_ID" label="四级编码" min-width="100"></el-table-column>
      <el-table-column prop="TYPE_NAME" label="数据名称" min-width="90" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="username" label="申请人" min-width="80"></el-table-column>
      <el-table-column prop="deptName" label="机构" min-width="60"></el-table-column>
      <el-table-column prop="phone" label="联系方式" min-width="60"></el-table-column>
      <el-table-column
        prop="CREATE_TIME"
        label="申请时间"
        min-width="100"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.CREATE_TIME) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="EXAMINE_STATUS" label="审核状态" min-width="60" v-if="datacell !==2 ">
        <template slot-scope="scope">
          <span v-if="scope.row.EXAMINE_STATUS==1">待审核</span>
          <span v-if="scope.row.EXAMINE_STATUS==3">审核不通过</span>
          <span v-if="scope.row.EXAMINE_STATUS==4">删除申请中</span>
          <span v-if="scope.row.EXAMINE_STATUS==5">已失效</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="storageDefineIdentifier"
        label="存储构建"
        min-width="70"
        v-if="datacell===2"
      >
        <template slot-scope="scope2">
          <el-button class="opBtn" size="mini" @click="showMaterialSingle(scope2.row)">
            <i class="btnRound blueRound" v-if="scope2.row.storageDefineIdentifier==1"></i>
            <i class="btnRound orangRound" v-else></i>
            存储结构
          </el-button>
          <!-- <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>存储结构
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column
        prop="storageDefineIdentifier"
        label="迁移清除"
        min-width="70"
        v-if="datacell===2"
      >
        <template slot-scope="scope4">
          <el-button class="opBtn" size="mini" @click="handleCleanAndTransfer(scope4.row)">
            <i
              class="btnRound blueRound"
              v-if="null!=scope4.row.movecleanIdentifier&&scope4.row.movecleanIdentifier!=''"
            ></i>
            <i class="btnRound orangRound" v-else></i>
            迁移清除
          </el-button>
          <!-- <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>迁移清除
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column prop="storageDefineIdentifier" label="备份" min-width="55" v-if="datacell===2">
        <template slot-scope="scope5">
          <el-button class="opBtn" size="mini" @click="handleBackUp(scope5.row)">
            <i
              class="btnRound blueRound"
              v-if="scope5.row.storageDefineIdentifier=='1'&&scope5.row.backupIdentifier!=null&&scope5.row.backupIdentifier!=''"
            ></i>
            <i class="btnRound orangRound" v-else></i>
            备份
          </el-button>

          <!--  <el-button disabled v-else size="mini">
              <i class="btnRound orangRound"></i>备份
          </el-button>-->
        </template>
      </el-table-column>
      <el-table-column prop="storageDefineIdentifier" label="恢复" min-width="55" v-if="datacell===2">
        <template slot-scope="scope6">
          <el-button
            v-if="scope6.row.storageDefineIdentifier!='3'"
            size="mini"
            @click="openRecoverDialog(scope6.row)"
          >恢复</el-button>
          <el-button disabled v-else size="mini">恢复</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="EXAMINE_STATUS" label="操作" min-width="50" v-if="datacell!==5">
        <template slot-scope="scope1">
          <el-button
            type="text"
            plain
            size="mini"
            v-if="scope1.row.EXAMINE_STATUS===1"
            @click="examineData(scope1.row)"
          >
            <i class="el-icon-s-management"></i>审核
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.EXAMINE_STATUS==2||scope1.row.EXAMINE_STATUS==4"
            @click="deleteList(scope1.row)"
          >
            <i class="el-icon-delete"></i>删除
          </el-button>
          <el-button
            plain
            size="mini"
            type="text"
            v-if="scope1.row.EXAMINE_STATUS===3"
            @click="showReason(scope1.row)"
          >
            <i class="el-icon-tickets"></i>原因
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog
      :title="dialogTitle"
      :visible.sync="handleDialog"
      width="90%"
      max-width="1100px"
      top="5vh"
      v-dialogDrag
    >
      <handleAccount
        v-if="handleDialog"
        :handleObj="handleObj"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>
    <!-- 数据注册审核-->
    <el-dialog :visible.sync="reviewdataRegisterVisible" fullscreen title="存储资料审核" v-dialogDrag>
      <reviewDataRegister
        v-if="reviewdataRegisterVisible"
        :registerForm="reviewFormData"
        @closeexamine="closeexamine"
      />
    </el-dialog>
    <el-dialog :visible.sync="reviewStep" fullscreen title="存储资料审核步骤" v-dialogDrag>
      <revieStepRegister v-if="reviewStep" :materialObj="materialObj" @closeStep="closeStep" />
    </el-dialog>

    <!-- 存储结构 与表结构管理的 新增、编辑资料一致 -->
    <el-dialog
      title="存储资料配置"
      :visible.sync="materialSingleVisible"
      width="90%"
      :close-on-click-modal="false"
      top="5vh"
      v-dialogDrag
    >
      <StructureMaterialSingle
        v-if="materialSingleVisible"
        :editMaterial="editMaterial"
        :editMaterialObj="editMaterialObj"
        :editMaterialArry="editMaterialArry"
        @addOrEditSuccess="addOrEditSuccess"
        ref="myHandleMaterial"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  getDataClassify,
  getDataTable
} from "@/api/authorityAudit/DRegistration/index";
import handleAccount from "@/views/authorityAudit/cloudDBaudit/handleCloudDB";
//数据注册审核
import reviewDataRegister from "@/views/authorityAudit/DRegistration/reviewdataRegister";
//存储资料审核步骤
import revieStepRegister from "@/views/authorityAudit/DRegistration/review/index";
//存储结构
import StructureMaterialSingle from "@/views/authorityAudit/DRegistration/review/StructureMaterialSingleDR";
export default {
  components: {
    handleAccount,
    reviewDataRegister,
    revieStepRegister,
    StructureMaterialSingle
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "1",
        DDataId: ""
      },
      datacell: 1, //当前审核状态
      examineStatus: [
        {
          value: "0",
          label: "待审核"
        },
        {
          value: "2",
          label: "审核未通过"
        },
        {
          value: "1",
          label: "审核通过"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      dbtypeselect: [], //审核状态
      reviewFormData: {},
      reviewdataRegisterVisible: false,
      materialObj: {}, //新增资料需要回显公共元数据信息
      reviewStep: false,
      materialSingleVisible: false //存储构建
    };
  },
  created() {
    //查询条件 -> 获取数据分类
    getDataClassify().then(response => {
      this.dbtypeselect = response.data;
    });
    //查询数据列表
    this.getList();
  },
  methods: {
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 重置
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "1",
        DDataId: ""
      };
      this.handleQuery();
    },
    // 查询列表
    getList() {
      this.loading = true;
      this.datacell = this.queryParams.examineStatus * 1;
      getDataTable(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        //通过状态显示
        if (this.queryParams.examineStatus == 2) {
          this.formatterTableData();
        }
      });
    },
    //通过的数据进行预处理  根据storageConfigurations将数据处理成多条
    formatterTableData() {
      //1 如果是审核通过的数据
      let newTableData = [];
      this.tableData.forEach(item => {
        if ("storageConfigurations" in item) {
          let storageConfigurations = item.storageConfigurations;
          //根据storageConfigurations长度进行条数增加\
          if (storageConfigurations.length >= 1) {
            storageConfigurations.forEach(scItem => {
              let newItem = {};
              newItem = { ...item, ...scItem };
              newTableData.push(newItem);
            });
          } else {
            let newItem = {};
            let notCreate = {
              storageDefineIdentifier: 2,
              syncIdentifier: 2,
              movecleanIdentifier: 2,
              backupIdentifier: 2,
              archivingIdentifier: 2
            };
            newItem = { ...item, ...notCreate };
            newTableData.push(newItem);
          }
        } else {
          let newItem = {};
          let notCreate = {
            storageDefineIdentifier: 2,
            syncIdentifier: 2,
            movecleanIdentifier: 2,
            backupIdentifier: 2,
            archivingIdentifier: 2
          };
          newItem = { ...item, ...notCreate };
          newTableData.push(newItem);
        }
      });
      this.tableData = newTableData;
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },

    //审核操作
    examineData(row) {
      this.reviewFormData = row;
      this.reviewdataRegisterVisible = true;
    },
    // 审核通过  审核取消
    closeexamine(registerForm) {
      this.reviewdataRegisterVisible = false;
      if (registerForm == 3) {
        this.getList();
      }
      //审核步骤
      else {
        if (registerForm != undefined) {
          console.log(registerForm);
          this.materialObj = registerForm;
        } else {
          this.materialObj = {};
        }
        this.reviewStep = true;
      }
    },
    // 存储构建
    showMaterialSingle(row) {
      console.log(row);
      this.editMaterialObj = row;
      this.editMaterial = row.D_DATA_ID;
      this.editMaterialArry = [];
      let obj = {};
      obj.logic_id = row.D_DATA_ID;
      obj.logic_name = row.class_name;
      obj.storage_name = row.TYPE_NAME;
      obj.storage_type = row.D_DATA_ID;
      obj.physicsName = "";
      obj.special = row.ID;
      obj.special_database_name = row.special_database_name;
      this.editMaterialArry.push(obj);
      this.materialSingleVisible = true;
    },
    //新增、编辑后刷新数据
    addOrEditSuccess(operateType) {
      if (operateType.type) {
        console.log(operateType);
        this.getDataBasePage("1");
        // this.$refs.classifyTree.initMethodsTree(this.treeUrl);
        this.$router.push({
          name: "表结构管理",
          params: { treeIdOfDR: operateType.parent_class_id }
        });
      }
      this.materialSingleVisible = false;
    },
    closeStep() {
      this.reviewStep = false;
      this.getList();
    },
    //审核不通过  原因
    showReason(row) {
      this.$message({
        type: "info",
        center: "true",
        message: row.REMARK
      });
    },
    handleAdd() {
      // this.dialogTitle = "新增数据库账户审核";
      // this.handleObj = {};
      // this.handleDialog = true;
    },
    downloadTable() {},
    //查看原因
    viewReason(row) {
      this.$alert(row.failure_reason, "拒绝原因", {
        confirmButtonText: "确定"
      });
    },
    viewCell(row) {
      this.dialogTitle = "数据库账户审核";
      this.handleObj = row;
      this.handleDialog = true;
    },
    deleteCell(row) {},
    analysisCell(row) {},
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
    }
  }
};
</script>
<style lang="scss">
.tableList {
  .cell {
    padding-left: 0px;
    padding-right: 0px;
  }
}
.opBtn {
  & > span {
    display: flex;
    align-items: center;
  }
  .orangRound {
    background: #ff5722;
    width: 6px;
    height: 6px;
    display: inline-block;
    border-radius: 4px;
    margin-right: 4px;
  }
  .blueRound {
    background: #057afe;
    width: 6px;
    height: 6px;
    display: inline-block;
    border-radius: 4px;
    margin-right: 4px;
  }
}
</style>
