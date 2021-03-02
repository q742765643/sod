<template>
  <div class="app-container">
    <!-- 中间件审核 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="审核状态">
        <el-select
          v-model.trim="queryParams.examineStatus"
          placeholder="审核状态"
          clearable
          size="small"
          style="width: 140px"
        >
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="实例类型">
        <el-select
          v-model.trim="queryParams.storageLogic"
          placeholder="实例类型"
          clearable
          size="small"
          style="width: 140px"
        >
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditType"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!--<el-form-item label="申请用户">-->
        <!--<el-input clearable size="small" v-model.trim="queryParams.userId" placeholder="申请用户"></el-input>-->
      <!--</el-form-item>-->
      <el-form-item label="名称">
        <el-input clearable size="small" v-model.trim="queryParams.displayname" placeholder="名称"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>

      </el-form-item>
      <el-form-item style="float: right">
        <el-button size="small"  type="primary" @click="applyNewExample" icon="el-icon-plus">新增实例</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">

    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <!--<el-table-column type="index" label="序号" width="50" :index="table_index"></el-table-column>-->
      <el-table-column prop="displayname" label="名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="name" label="实例服务名称" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            @click="getLink(scope.row)">
            {{scope.row.name}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="storageLogic" label="实例类型":show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column prop="examineStatus" label="审核状态" width="110px" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            @click="getLogData(scope.row.id)">
            {{statusShow(scope.row.examineStatus)}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="examineStatus" label="运行状态" width="80px" :formatter="statusShow1"></el-table-column>
      <el-table-column prop="userId" label="申请用户" width="120px" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            @click="getBizUser(scope.row.userId)">
            {{scope.row.userId}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="申请时间" width="160px" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="160px">
        <template slot-scope="scope">
          <span v-if="scope.row.updateTime">{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="310">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.examineStatus == '01'"
            type="text"
            size="mini"
            icon="el-icon-coordinate"
            @click="viewCell(scope.row)"
          >审核</el-button>
          <!--<el-button-->
            <!--v-if="scope.row.examineStatus == '01'&&scope.row.itserviceId != null"-->
            <!--type="text"-->
            <!--size="mini"-->
            <!--icon="el-icon-coordinate"-->
            <!--@click="viewCell(scope.row)"-->
          <!--&gt;变更审核</el-button>-->
          <el-button
            v-if="scope.row.itserviceId"
            type="text"
            size="mini"
            icon="el-icon-s-marketing"
            @click="analysisCell(scope.row)"
          >详情</el-button>
          <el-button
            v-if="scope.row.itserviceId != null"
            type="text"
            style="color: #ed5565"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCell(scope.row)">删除</el-button>
          <el-button
            v-if="scope.row.itserviceId == null"
            type="text"
            style="color: #ed5565"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCe(scope.row)">删除</el-button>
          <el-button
            v-if="scope.row.itserviceId != null"
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="getLink(scope.row)">链接信息</el-button>
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
    :close-on-click-modal="false"
    :title="dialogTitle"
    :visible.sync="handleDialog"
    width="90%"
    max-width="1100px"
    top="5vh"
    v-dialogDrag
  >
    <handleAccount
      style="overflow-y:scroll; height: 700px"
      v-if="handleDialog"
      :handleObj="handleObj"
      @handleDialogClose="handleDialogClose"
      ref="myHandleServer"
    />
  </el-dialog>
    <el-dialog
      title="业务用户信息"
      :visible.sync="dialogVisible"
      width="25%"
      >
      <el-form :model="bizUserInfo"  ref="ruleForm" label-width="200px" class="demo-ruleForm">
        <div align="center">
        <table style="border-color: black;font-size: 20px;"
               border="2px">
          <tr>
            <td style="padding: 3px">业务用户：</td>
            <td style="padding: 3px"><span>{{bizUserInfo.userName}}</span></td>
          </tr>
          <tr>
            <td style="padding: 3px">责任人：</td>
            <td style="padding: 3px"><span>{{bizUserInfo.nickName}}</span></td>
          </tr>
          <tr>
            <td style="padding: 3px">部门：</td>
            <td style="padding: 3px"><span>{{bizUserInfo.deptName}}</span></td>
          </tr>
          <tr>
            <td style="padding: 3px">联系方式：</td>
            <td style="padding: 3px"><span>{{bizUserInfo.phonenumber}}</span></td>
          </tr>
        </table>
        </div>
      </el-form>
      <!--<span slot="footer" class="dialog-footer">-->
    <!--&lt;!&ndash;<el-button @click="dialogVisible = false">取 消</el-button>&ndash;&gt;-->
    <!--<el-button type="primary" @click="dialogVisible = false">关闭</el-button>-->
  <!--</span>-->
    </el-dialog>
    <el-dialog
      title="实例链接信息"
      :visible.sync="dialogLink"
      width="25%"
    >
      <el-form :model="linkInfo"  ref="ruleForm" label-width="200px" class="demo-ruleForm">
        <div align="center">
          <table style="border-color: black;font-size: 20px;"
                 border="2px">
            <tr v-if="linkInfo.url != null">
              <td style="padding: 3px;">url：</td>
              <td style="padding: 3px"><span>{{linkInfo.url}}</span></td>
            </tr>
            <tr v-if="linkInfo.user != null&&linkInfo.user != ''">
              <td style="padding: 3px">user：</td>
              <td style="padding: 3px"><span>{{linkInfo.user}}</span></td>
            </tr>
            <tr v-if="linkInfo.password != null&&linkInfo.password != ''">
              <td style="padding: 3px">password：</td>
              <td style="padding: 3px"><el-input style="width: 140px;margin: 0px;" :type="passwordType" :disabled="true" v-model="linkInfo.password"/>
                <i
                  style="margin: auto;"
                  :class="iconType"
                  @click="showPwd"
                ></i></td>
            </tr>
            <tr v-if="linkInfo.webUrl != null&&linkInfo.webUrl != ''">
              <td style="padding: 3px">webUrl：</td>
              <td style="padding: 3px"><span>{{linkInfo.webUrl}}</span></td>
            </tr>
            <tr v-if="dialogLinkXG">
              <td style="padding: 3px">user：</td>
              <td style="padding: 3px"><span>SYSDBA</span></td>
            </tr>
            <tr v-if="dialogLinkXG">
              <td style="padding: 3px">password：</td>
              <td style="padding: 3px">
                <el-input style="width: 140px;margin: 0px;" :type="passwordType" :disabled="true" value="SYSDBA"/>
                  <i
                    style="margin: auto;"
                    :class="iconType"
                    @click="showPwd"
                  ></i>
              </td>
            </tr>
            <tr v-if="dialogLinkXG">
              <td style="padding: 3px">数据库：</td>
              <td style="padding: 3px"><span>SYSTEM</span></td>
            </tr>
          </table>
        </div>
      </el-form>
      <!--<span slot="footer" class="dialog-footer">-->
      <!--&lt;!&ndash;<el-button @click="dialogVisible = false">取 消</el-button>&ndash;&gt;-->
      <!--<el-button type="primary" @click="dialogVisible = false">关闭</el-button>-->
      <!--</span>-->
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      title="中间件详情"
      :visible.sync="monitorDialog"
      customClass="customHeight"
      width="90%"
      max-width="1100px"
      top="5vh"
      v-dialogDrag
    >
      <handleMonitor

        v-if="monitorDialog"
        :handleObj="handleObj"
        @handleDialogClose="handleDialogClose"
        ref="myHandleServer"
      />
    </el-dialog>

    <el-dialog title="申请和变更记录" :visible.sync="outerVisible"
               width="30%"
               max-width="400px">

      <div  class="block" style="overflow-y:scroll; height: 550px">
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="'操作时间：'+parseTime(activity.createTime)">
            <h3>{{logStatus(activity.viewStatus)}}</h3>
            <p v-if="activity.viewStatus != '4'&&activity.viewStatus != '5'&&activity.viewStatus != '2'&&activity.viewStatus != '6'">申请理由：{{activity.databaseUse}}</p>
            <p v-if="activity.viewStatus == '4'|| activity.viewStatus == '5' || activity.viewStatus == '2'">审核意见：{{activity.rejectReason}}<br></p>
            <el-button v-if="activity.viewStatus == '1'||activity.viewStatus == '3'" @click="handleExport(activity.examineMaterial)" size="small" type="success">下载申请材料</el-button>
            <br v-if="activity.viewStatus == '1'||activity.viewStatus == '3'"><el-button
              type="text"
              size="mini"
              @click="getBizUser(activity.userId)">
              操作用户：{{activity.userId}}
            </el-button><br>
          </el-timeline-item>
        </el-timeline>

      </div>
    </el-dialog>
    <el-dialog title="新增中间件实例"
               :visible.sync="applyVisible"
               width="35%"
               max-width="500px"
               v-dialogDrag>
      <div  class="block" style="overflow-y:scroll; height: 550px" align="left">
        <el-form
          :rules="rules"
          ref="ruleForm"
          :model="msgFormDialog"
          label-width="150px"
          label-position="right">
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item label="实例类型" prop="storageLogic">
                <!--<el-input-->
                <!--size="small"-->
                <!--v-model.trim="msgFormDialog.storageLogic">-->
                <!--</el-input>-->
                <el-select
                  v-model.trim="msgFormDialog.storageLogic"
                  size="small"
                  @change="getTempInfo">
                  <!--<el-option label="全部" value></el-option>-->
                  <el-option
                    v-for="(item,index) in auditType"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item label="实例名" prop="displayname">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.displayname"
                  placeholder="实例名"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item label="服务端口" prop="port">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.port"
                  placeholder="服务端口"
                  :disabled="true"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'kafka'">
              <el-form-item label="客户端连接端口" prop="externalPort">
                <el-input
                  size="small"
                  :disabled="true"
                  v-model.trim="msgFormDialog.externalPort">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'tensorflow'">
              <el-form-item label="API端口" prop="restApiport">
                <el-input
                  size="small"
                  :disabled="true"
                  v-model.trim="msgFormDialog.restApiport"
                  placeholder="API端口"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'rabbitmq'">
              <el-form-item label="管理服务端口" prop="managerPort">
                <el-input
                  size="small"
                  :disabled="true"
                  v-model.trim="msgFormDialog.managerPort"
                  placeholder="管理服务端口"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item
                label="CPU"
                prop="cpu">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.cpu">
                  <el-option
                    v-for="(item,index) in cpuList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item
                label="内存"
                prop="memory">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.memory">
                  <el-option
                    v-for="(item,index) in memoryList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic != 'tensorflow'">
              <el-form-item label="存储空间大小" prop="storage">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.storage">
                  <el-option
                    v-for="(item,index) in storageSizeList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic != 'xuguserver' && msgFormDialog.storageLogic != 'rabbitmq'">
              <el-form-item
                label="集群规模"
                prop="slaveCount">
                <el-select
                  size="small"
                  v-model.trim="msgFormDialog.slaveCount">
                  <el-option
                    v-for="(item,index) in clusterList"
                    :key="index"
                    :label="item.dictValue"
                    :value="item.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'rabbitmq' || msgFormDialog.storageLogic == 'postgresql'" >
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model.trim="msgFormDialog.username"
                  placeholder="用户名"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18"  v-if="msgFormDialog.storageLogic == 'redis' || msgFormDialog.storageLogic == 'rabbitmq' || msgFormDialog.storageLogic == 'postgresql'" >
              <el-form-item label="配置密码" prop="password">
                <el-input
                  v-model.trim="msgFormDialog.password"
                  placeholder="配置密码"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'postgresql'">
              <el-form-item label="初始化数据库名称" prop="database">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.database"
                  placeholder="初始化数据库名称"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18" v-if="msgFormDialog.storageLogic == 'postgresql'">
              <el-form-item label="管理员密码" prop="adminPassword">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.adminPassword"
                  placeholder="管理员密码"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="18">
              <el-form-item label="用途" prop="databaseUse">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.databaseUse"
                  placeholder="用途"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="trueDialog('ruleForm')">确 定</el-button>
        <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  var baseUrl = process.env.VUE_APP_DM;
import {
  getDictDataByType,
  cldbApplicationAll,
  deleteExamine,
  deleteNew,
  getById,
  getUserId,
  zjCldb,
  saveCldb,
  saveLog,
  deployNew,
  gatBiz,
  getLogId,
  deleteLogData,
  getTemp,
  deleteFeedbackId,
  getLinkInfo,
  gethostsNew,
} from "@/api/authorityAudit/middlewareDBaudit";
  import { downloadTable } from "@/api/structureManagement/exportTable";
import axios from 'axios'
import handleAccount from "@/views/authorityAudit/middleAudit/handleMiddle";
import handleMonitor from "@/views/authorityAudit/middleAudit/handleMonitor";
export default {
  components: {
    handleAccount,
    handleMonitor,

  },
  data() {


    return {
      upLoadUrl: baseUrl + "/dm/yunDatabaseApply/upload",
      passwordType:"",
      iconType:"el-icon-view",
      sysdba:"SYSDBA",
      uploadData: {},
      dialogLink:false,
      dialogLinkXG:false,
      applyVisible:false,
      outerVisible: false,
      innerVisible: false,
      dialogVisible:false,
      bizUserInfo:{},
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        storageLogic:"",
        examineStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        userId: "",
        displayname: "",
        params: {
          orderBy: {
            createTime: "desc",
          },
        },
      },
      activities:[],
      dateRange: [],

      auditStatus: [
        {value: "01",
          label: "待审核",},
        {value: "02",
          label: "审核通过",},
        {value: "03",
          label: "审核拒绝",},
        {value: "04",
          label: "待删除",},
      ],
      auditType: [
        {value: "redis",
          label: "redis",},
        {value: "kafka",
          label: "kafka",},
        {value: "rabbitmq",
          label: "rabbitmq",},
        // {value: "tensorflow",
        //   label: "tensorflow",},
        // {value: "postgresql",
        //   label: "postgresql",},
        {value: "xuguserver",
          label: "xuguserver",},
      ],
      msgFormDialog: {

        userId: "",
        slaveCount:"",
        name:"",
        port:"",
        password:"",
        externalPort:"",
        restApiport:"",
        managerPort:"",
        database:"",
        adminPassword:"",
        department: "",
        itserviceId:"",
        cluster:"",
        cpu:"",
        memory:"",
        storage:"",
        storageLogic: "redis",
        displayname: "",
        applicationSystem: "",
        storageSize: "",
        databaseUse: "",
        clusterSize:"",
        cpuSize: "",
        memorySize:"",
        mountServer: "",
        mountDirectory: "",
        examiner: "",
        examineMaterial: "",
        token:"",
        remarks:[]
      },
      rules: {
        displayname: [
          { required: true,pattern:/^[\u4e00-\u9fa5A-Za-z][\u4e00-\u9fa5A-Za-z0-9_]{0,}[\u4e00-\u9fa5A-Za-z0-9]$/, message: '支持输入中英文、数字及特殊字符"_"，不允许以"_"和数字开头，不允许以"_"结尾，长度不少于2个字符',trigger: "blur" }
        ],
        cpu: [
          { required: true, message: "请选择CPU", trigger: "blur" }
        ],
        memory:[
          { required: true, message: "请选择配内存", trigger: "blur" }
        ],
        slaveCount:[
          { required: true, message: "请选择集群规模", trigger: "blur" }
        ],
        storage: [
          { required: true, message: "请选择存储空间", trigger: "blur" }
        ],
        username: [
          { required: true, pattern:/(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)/, message: "用户名允许是字母、数字以及下划线且不能以数字开头，不能输入连续下划线", trigger: "blur" }
        ],
        password: [
          { required: true, pattern:/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)[A-Za-z\d_-~;,.'":#&!@$%^*]{8,16}$/, message: "密码必须包含大小写字母和数字，长度8-16位", trigger: "blur" }
        ],
        database: [
          { required: true, pattern:/(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)/, message: "用户名允许是字母、数字以及下划线且不能以数字开头，不能输入连续下划线", trigger: "blur" }
        ],
        adminPassword: [
          { required: true,pattern:/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)[A-Za-z\d_-~;,.'":#&!@$%^*]{8,16}$/, message: "密码必须包含大小写字母和数字，长度8-16位", message: "请输入管理员密码", trigger: "blur" },
        ],
        databaseUse: [
          { required: true, message: "请输入用途", trigger: "blur" }
        ],
      },
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      monitorDialog:false,
      currentRow: null,
      newToken:"",
      configData:{
        type:"",
        id:""
      },
      linkInfo:{},
      storageSizeList:[],
      cpuList:[],
      memoryList:[],
      clusterList:[],
      hostsList:[],
    };
  },
  created() {
    this.getList();
    getDictDataByType("yun_storage_size").then(
      response => {
        this.storageSizeList = response.data;
      }
    );
    getDictDataByType("yun_cpu_type").then(
      response => {
        this.cpuList = response.data;
      }
    );
    getDictDataByType("yun_memory_type").then(
      response => {
        this.memoryList = response.data;
      }
    );
    getDictDataByType("yun_cluster_size").then(
      response => {
        this.clusterList = response.data;
      }
    );
  },

  methods: {

    // ceshi(row){
    //   deleteLogData({logId:row.id})
    // },
    // 文件下载
    handleExport(data) {
      if (data) {
        downloadTable({ filePath: data }).then(
          res => {
            this.downloadfileCommon(res);
          }
        );
      } else {
        this.$message({
          type: "error",
          message: "当前没有文件可以下载"
        });
      }
    },

    handleCurrentChange(val) {
      this.currentRow = val;
    },
    sortChange(column, prop, order) {
      var orderBy = {};
      if (column.order == "ascending") {
        orderBy.createTime = "asc";
      } else {
        orderBy.createTime = "desc";
      }
      this.queryParams.params.orderBy = orderBy;
      this.handleQuery();
    },
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {

      if (this.queryParams.dateRange) {
        this.dateRange = this.queryParams.dateRange;
      } else {
        this.dateRange = [];
      }
      this.loading = true;
      console.log(this.addDateRange(this.queryParams, this.dateRange));
      cldbApplicationAll(
        this.addDateRange(this.queryParams, this.dateRange)
      ).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        if (this.currentRow) {
          this.tableData.forEach((element, index) => {
            if (element.id == this.currentRow.id) {
              this.$refs.singleTable.setCurrentRow(this.tableData[index]);
            }
          });
        }
      });
    },

    resetQuery() {
      this.superMsg = {};
      this.$refs["queryForm"].resetFields();
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        examineStatus: "",
        userId: "",
        displayname: "",
      };
      this.dateRange = [];
      this.handleQuery();
    },

    // 状态
    statusShow(row) {
      if (row == "01") {
        return "待审核";
      } else if (row == "02") {
        return "审核通过";
      } else if (row == "03") {
        return "审核拒绝";
      } else if (row == "04") {
        return "待删除";
      } else {
        return "-";
      }
    },
    // 状态
    statusShow1: function (row) {
      if (row.examineStatus == "01") {
        if (row.itserviceId) {
          return "活跃";
        }
        return "--";
      } else if (row.examineStatus == "02") {
        return "活跃";
      } else if (row.examineStatus == "03") {
        if (row.itserviceId) {
          return "活跃";
        }
        return "--";
      } else if (row.examineStatus == "04") {
        return "活跃";
      } else {
        return "-";
      }
    },

    downloadTable() {},

    // 详情
    analysisCell(row) {
      // this.dialogTitle = "中间件详情";
      var id = row.itserviceId;
      var type = row.storageLogic;
      var userId = row.id;
      this.handleObj= {id,type,userId};
      this.monitorDialog = true;

    },
    viewCell(row, type) {

      getById({ id: row.id }).then((response) => {
        if(response.code == 202 || response.data.examineStatus == "02"){
          this.msgError("实例已删除或者状态已改变");
          this.handleQuery();
        }else{
        this.handleObj = response.data;
        this.handleObj.editCpu = this.handleObj.cpu;
        this.handleObj.editMmemory = this.handleObj.memory;
        this.handleObj.editSlaveCount = this.handleObj.slaveCount;
        this.handleObj.editStorage = this.handleObj.storage;
        this.handleDialog = true;
        }
      }).catch(()=>{
        this.handleQuery();
      });
    },

    async deleteCell(row) {
      await this.$confirm("确定要删除这条数据吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      });
//显示延迟加载
      const loading = this.$loading({
        lock: true,
        text: "请稍候",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      setTimeout(() => {
        loading.close();
        this.handleQuery();
      }, 2000);
      this.configData.type = row.storageLogic;
      this.configData.id = row.itserviceId;
      var a ;
      debugger
      await getById({ id: row.id }).then((response) => {
        if(response.code == 1){
          a = false;
        }
      }).catch(()=>{
        this.msgError("未找到该实例");
      });
      // if(!a){
      await deleteNew(this.configData).then((response) =>{
        if(response.code == 200){
          console.log("中间件删除成功");
        }else{
          console.log("中间件删除失败");
        }
      });
      await deleteExamine({ id: row.id }).then((response) => {
        if (response.code == 200) {
          console.log("实例信息删除成功")
        }
      });
      await deleteFeedbackId({ itserviceId: row.itserviceId }).then((response) => {
        if (response.code == 200) {
          console.log("反馈信息删除成功")
        }
      });
      await deleteLogData({ logId: row.id }).then((response) => {
        if (response.code == 200) {

        }
      });
      // }else{
      //   this.msgError("未找到该实例");
      // }
    },
    deleteCe(row) {
      this.$confirm("确定要删除这条数据吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //显示延迟加载
          const loading = this.$loading({
            lock: true,
            text: "请稍候",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          setTimeout(() => {
            loading.close();
            this.handleQuery();
          }, 2000);
            getById({ id: row.id }).then((response) => {
              if(response.data.itserviceId == null) {
                deleteExamine({ id: row.id }).then((response) => {
                  if (response.code == 200) {
                    // loading.close();
                    deleteLogData({ logId: row.id }).then((response) => {
                      if (response.code == 200) {
                        // this.msgSuccess("删除成功");
                        // this.handleQuery();
                      }
                    });
                  }
                });
              }else{
                    this.msgError("实例已部署");
              }
            });
        })
        .catch(() => {});
    },
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
      this.handleQuery();

    },
    getBizUser(userId){
      gatBiz({bizUserid:userId}).then((res)=>{
        if(res.code == 200){this.bizUserInfo = res.data;}
      });
      this.dialogVisible = true;
    },
    logStatus(data){
      if(data == '1'){
        return "实例申请"
      }
      if(data == '2'){
        return "审核通过"
      }
      if(data == '3'){
        return "变更申请"
      }
      if(data == '4'){
        return "审核拒绝"
      }
      if(data == '5'){
        return "变更拒绝"
      }
      if(data == '6'){
        return "管理员修改"
      }
      if(data == '7'){
        return "修改申请"
      }
      if(data == '8'){
        return "实例申请（管理员）"
      }
    },
    getLogData(id){
      getLogId({logId:id}).then((res)=>{
        if(res.code == 200){this.activities = res.data;}
      });
      this.outerVisible = true;
    },
    applyNewExample(){
      this.hostsList = [];
      this.applyVisible = true;
      // this.msgFormDialog.storageLogic = 'tensorflow';
      this.getTempInfo(this.msgFormDialog.storageLogic);
      gethostsNew({}).then(response => {
        response = JSON.parse(response.data)
        var host = response.content.content;
        for(var i=0;i<host.length;i++){
          this.hostsList.push(host[i].address);
        }
        // var index = Math.floor((Math.random()*this.hostsList.length));
        // this.msgFormDialog.externalIP = this.hostsList[index];
        // debugger
        });
    },
    getTempInfo(type){
      getTemp({type:type}).then((result)=>{
        result = JSON.parse(result.data)
        for(var i=0;i<result.content.length;i++){
          var obj = result.content[i];
          for(var j=0;j<obj.config.length;j++){
            var obj1 = obj.config[j];
            if(obj1.name == "port" ){
              this.msgFormDialog.port = obj1.defaultValue;
            }
            if(obj1.name == "restApiport" ){
              this.msgFormDialog.restApiport = obj1.defaultValue;
            }
            if(obj1.name == "managerPort" ){
              this.msgFormDialog.managerPort = obj1.defaultValue;
            }
            if(obj1.name == "externalPort" ){
              this.msgFormDialog.externalPort = obj1.defaultValue;
            }
          }
        }
      });
    },
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.applyVisible = false;
    },
    trueDialog(formName) {
      //显示延迟加载

      // console.log(this.msgFormDialog);
      if(this.msgFormDialog.slaveCount !="1"){
        this.msgFormDialog.cluster = true
      } else{
        this.msgFormDialog.cluster = false
      }
      if(this.msgFormDialog.storageLogic =="kafka"){
        debugger
        var index = Math.floor((Math.random()*this.hostsList.length));
        this.msgFormDialog.externalIP = this.hostsList[index];
        if(this.msgFormDialog.slaveCount !="1" ){
          this.msgFormDialog.port = "";
          var a = parseInt(this.msgFormDialog.slaveCount);
          for(var i=0;i<a;i++){
            if(this.msgFormDialog.port != ""){
              this.msgFormDialog.port = this.msgFormDialog.port+","
            }
            var b = Math.floor((Math.random()+3)*10000);
            var c = b.toString();
            this.msgFormDialog.port = this.msgFormDialog.port + c;
          }
        }
      }

      this.msgFormDialog.userId = this.$store.getters.userName;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: "请稍候",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          deployNew(this.msgFormDialog).then(res => {
            debugger
            if (res.code == 200) {
              var  a = JSON.parse(res.data);
              this.msgFormDialog.itserviceId = a.content.id;
              this.msgFormDialog.name = a.content.name;
              saveCldb(this.msgFormDialog).then(res =>{
                this.msgFormDialog.logId = res.data.id;
                this.msgFormDialog.id = '';
                this.msgFormDialog.userId = this.$store.getters.userName;
                this.msgFormDialog.viewStatus = '8';
                // this.msgFormDialog.databaseUse = '管理员申请'
                saveLog(this.msgFormDialog).then(res =>{
                  this.msgFormDialog.viewStatus = '2';
                  this.msgFormDialog.rejectReason = '同意'
                  saveLog(this.msgFormDialog);
                  loading.close();
                  this.$message({
                    type: "success",
                    message: "操作成功"
                  });
                  this.$refs[formName].resetFields();
                  this.applyVisible = false;
                  this.handleQuery();
                });

              });
              // this.msgFormDialog.viewStatus = '2';


            } else {
              this.$message({
                type: "error",
                message: res.message
              });
            }
          });
        }
      });
    },
    getLink(row){
      this.dialogLink = true;
      this.passwordType = "password";
      debugger
      if(row.storageLogic == "xuguserver"){
        this.dialogLinkXG = true;
      }else{
        this.dialogLinkXG = false;
      };
      getLinkInfo({type:row.storageLogic,id:row.itserviceId}).then(res =>{
        res = JSON.parse(res.data);
        this.linkInfo = res.content;
      });
    },
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
        this.iconType = "el-icon-view"
      } else {
        this.passwordType = "password";
        this.iconType = "el-icon-view"
      }
    },
  },
};
</script>
<style>
  .el-card {
    margin-bottom: 20px;
  }
  .el-select {
    width: 100%;
  }
</style>
