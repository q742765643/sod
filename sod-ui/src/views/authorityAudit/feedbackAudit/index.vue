<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">
      <el-form-item label="状态">
        <el-select
          v-model.trim="queryParams.feedbackStatus"
          placeholder="状态"
          clearable
          size="small"
          style="width: 240px">
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in auditStatus"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题">
        <el-input clearable size="small" v-model.trim="queryParams.feedbackTitle" placeholder="标题"></el-input>
      </el-form-item>
      <!--<el-form-item label="实例名称">-->
        <!--<el-input clearable size="small" v-model.trim="queryParams.displayname" placeholder="实例名称"></el-input>-->
      <!--</el-form-item>-->
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <div style="margin: 5px">
    <!--<el-radio v-model="radio" @change="" label="1">未接手</el-radio>-->
    <!--<el-radio v-model="radio" @change="" label="2">已接手</el-radio>-->
    <!--<el-radio v-model="radio" @change="" label="3">已处理</el-radio>-->
    </div>
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      @sort-change="sortChange"
      ref="singleTable"
      highlight-current-row
      @current-change="handleCurrentChange">
      <el-table-column prop="feedbackTitle" label="标题" :show-overflow-tooltip="true" width="300"></el-table-column>
      <el-table-column prop="displayname" label="实例名称" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="analysisCell(scope.row)">{{scope.row.displayname}}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="storageLogic" label="实例类型" ></el-table-column>
      <el-table-column prop="feedbackStatus" label="状态" :formatter="statusShow"></el-table-column>
      <el-table-column prop="userId" label="申请用户" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="processor" label="处理用户" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="createTime" label="申请时间" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200px">
        <template slot-scope="scope">

          <el-button
            v-if="scope.row.feedbackStatus == '1'"
            type="text"
            size="mini"
            icon="el-icon-finished"
            @click="updateStatus(scope.row)"
          >处理</el-button>
          <el-button type="text" size="mini" icon="el-icon-view" @click="lookInfo(scope.row)"  v-if="scope.row.feedbackStatus == '2'">查看</el-button>
          <el-button type="text" size="mini" style="color: #ed5565" icon="el-icon-delete" @click="deleteFeed(scope.row)">删除</el-button>

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
      title="查看"
      :visible.sync="dialogVisible"
      width="90%"
      max-width="1100px"
      top="5vh">
      <el-row :gutter="12" >
        <el-col :span="12" >
          <el-card shadow="always">
            <div slot="header" class="clearfix">
              <span>标题：</span>
              {{feedbackTitle}}
            </div>
            <div v-html="exhibition" style="overflow:auto; height: 600px">
              <!--<pre v-html="exhibition"></pre>-->
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card shadow="always">
            <div slot="header" class="clearfix">
              <span>处理结果</span>
            </div>
            <div v-html="exhibition2" style="overflow:auto; height: 600px">
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-dialog>
    <el-dialog
      title="处理"
      :visible.sync="handleIndex"
      width="90%"
      max-width="1100px"
      top="5vh"
      :destroy-on-close="true">
      <el-row :gutter="12" >
        <el-col :span="12">
          <el-card shadow="always">
            <div slot="header" class="clearfix">
            <span>标题：</span>
              {{feedbackTitle}}
            </div>
            <div v-html="exhibition"  style="overflow:auto; height: 600px">
              <!--<pre v-html="exhibition"></pre>-->
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card shadow="always">
            <div slot="header" class="clearfix">
            <span>处理结果</span>
            </div>
            <div id="editor">
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editfeed">确 定</el-button>
      </div>
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
  </div>
</template>

<script>
import {
  getByFeedId,
  updateFeedbackStatus,
  feedbackAll,
  updateFeedback,
  deleteFeedId,
} from "@/api/authorityAudit/middlewareDBaudit";
// import  TinymceEditor from "@/components/tinymce/index";
import handleMonitor from "@/views/authorityAudit/middleAudit/handleMonitor";
import WangEditor from 'wangeditor'
// const editorNew = new WangEditor('#editor');
export default {

  components: {

    handleMonitor,
    // TinymceEditor
  },
  data() {
    return {

      dialogVisible:false,
      handleIndex:false,
      // radio: '',
      // activeName: 'first',
      // 遮罩层
      monitorDialog:false,
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        feedbackStatus:
          this.$route.params.status == undefined
            ? ""
            : this.$route.params.status,
        feedbackTitle: "",
        displayname: "",
        params: {
          orderBy: {
            createTime: "desc",
          },
        },
      },
      dateRange: [],
      auditStatus: [
        {
          value: "1",
          label: "未处理",
        },
        {
          value: "2",
          label: "已处理",
        },
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      currentRow: null,
      feedbackContent:"",
      exhibition:"",
      exhibition2:"",
      editorNew:"",
      editFeedbackData:"",
      feedbackTitle:"",
    };
  },
  created() {

    this.getList();
  },

  methods: {


    handleClick(tab, event) {
      console.log(tab, event);
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
      feedbackAll(
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        feedbackStatus: "",
        userId: "",
        displayname: "",
      };
      this.dateRange = [];
      this.radio = '';
      this.handleQuery();
    },
    // 状态
    statusShow: function (row) {
      if (row.feedbackStatus == "1") {
        return "未处理";
      }else if (row.feedbackStatus == "2") {
        return "已处理";
      }
    },
    // downloadTable() {},
    // //查看原因
    // viewReason(row) {
    //   this.$alert(row.failure_reason, "拒绝原因", {
    //     confirmButtonText: "确定",
    //   });
    // },

    //处理
    updateStatus(row) {
      this.handleIndex = true;
      this.editFeedbackData = row;
      debugger
      getByFeedId({ id: row.id }).then((response) => {
        this.feedbackContent = response.data.feedbackContent;
        this.exhibition = response.data.material;
        this.feedbackTitle = response.data.feedbackTitle;

      });
      this.$nextTick(function() {
        /*现在数据已经渲染完毕*/
        this.editorNew = new WangEditor('#editor');
        this.editorNew.config.uploadImgShowBase64 = true
        this.editorNew.config.height = 518;
        // this.editorNew.txt.clear();
        this.editorNew.create();
      });
    },
    //处理提交
    editfeed(){
      debugger
      if(!this.editorNew.txt.html()){
        this.$message({
          showClose: true,
          message: '填写处理结果',
          type: 'error'
        });
      }else {
        this.editFeedbackData.feedbackStatus = '2';
        this.editFeedbackData.processor = this.$store.getters.userName;
        this.editFeedbackData.handleMaterial = this.editorNew.txt.html();
        // console.log(this.editFeedbackData.handleMaterial)
        updateFeedback(this.editFeedbackData).then(res => {
          if (res.code == 200) {
            console.log("处理成功")
            this.handleIndex = false;
          }

        });
      }
    },
    //查看
    lookInfo(row) {
      this.dialogVisible = true;
      getByFeedId({ id: row.id }).then((response) => {
        this.feedbackContent = response.data.feedbackContent;
        this.exhibition = response.data.material;
        this.exhibition2 = response.data.handleMaterial;
        this.feedbackTitle = response.data.feedbackTitle;
      });
    },
    // 详情
    analysisCell(row) {
      // this.dialogTitle = "中间件详情";
      var id = row.itserviceId;
      var type = row.storageLogic;
      var userId = row.feedbackId;
      this.handleObj= {id,type,userId};
      this.monitorDialog = true;

    },
    handleDialogClose() {
      this.handleDialog = false;
      this.handleObj = {};
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
    deleteFeed(row){
      deleteFeedId({id:row.id}).then(res =>{
        if (res.code == 200) {
          this.msgSuccess("删除成功");
          this.handleQuery();
        }
      });
    },

  },
};
</script>
