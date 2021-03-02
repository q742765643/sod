<template>
  <div class="app-container">
    <!-- 用户反馈管理查询菜单 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="searchBox">

      <el-form-item label="反馈状态：">
        <el-select v-model.trim="queryParams.status" placeholder="反馈状态" clearable size="small" style="width: 140px"  @change="handleQuery">
          <el-option label="全部" value></el-option>
          <el-option label="未回复" value="0"></el-option>
          <el-option label="已回复" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="反馈类型">
        <el-select
          size="small"
          v-model.trim="queryParams.feedbackType"
          placeholder="反馈状态" clearable>
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in feedbackTypeList"
            :key="index"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <!--<el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>-->
      </el-form-item>
    </el-form>
    <!-- 业务动态管理操作按钮 -->
    <!--<el-row :gutter="10" class="handleTableBox">-->
      <!--<el-col :span="1.5">-->
        <!--<el-button size="small" type="primary" @click="showDialog('edit')" icon="el-icon-edit">回复</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
        <!--<el-button size="small" type="danger" icon="el-icon-delete" @click="deleteCell">删除</el-button>-->
      <!--</el-col>-->
    <!--</el-row>-->
    <!-- 数据展示列表 -->
    <el-table
      border
      v-loading="loading"
      :data="tableData"
      row-key="id"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <!--<el-table-column type="selection" width="50"></el-table-column>-->
      <el-table-column prop="feedbackTitle" label="反馈标题" :show-overflow-tooltip="true" width="300"></el-table-column>
      <el-table-column prop="feedbackType" label="类型"  :formatter="typeShow"></el-table-column>
      <el-table-column prop="userName" label="反馈人" ></el-table-column>
      <el-table-column prop="createTime" label="反馈时间"  :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态"  :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.status=='0'">未处理</span>
          <span v-if="scope.row.status=='1'">已处理</span>
        </template>
      </el-table-column>
      <!--<el-table-column prop="updateBy" label="回复人" width="100"></el-table-column>-->
      <el-table-column prop="address" label="操作" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == '0'"
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="updateFeed(scope.row)"
          >处理</el-button>
          <el-button
            v-if="scope.row.status == '1'"
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="lookInfo(scope.row)"
          >查看</el-button>
          <el-button
            type="text"
            style="color: #ed5565"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCell(scope.row)">删除</el-button>
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
    <!-- 弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="msgFormDialog"
      v-dialogDrag
    >
      <feedbackEdit
        v-if="msgFormDialog"
        :handleObj="handleObj"
        @cancelDialog="cancelDialog"
        ref="myDialog"
      ></feedbackEdit>
    </el-dialog>

    <el-dialog
      title="处理"
      :visible.sync="handleIndex"
      width="90%"
      max-width="1100px"
      top="5vh"
      :destroy-on-close="true">
      <el-row type="flex" class="row-bg" justify="left">
        <el-col :span="8">
            <span>反馈归类:</span>
            <el-select v-model.trim="isShow" placeholder="反馈归类" clearable size="small" style="width: 140px">
              <el-option label="个人问题" value="N"></el-option>
              <el-option label="常见问腿" value="Y"></el-option>
            </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="12" style="margin-top: 8px">
        <el-col :span="12">
          <el-card shadow="always">
            <div slot="header" class="clearfix">
              <span>标题:</span>
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
            <div id="editor">
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editDataSave">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="查看"
      :visible.sync="dialogVisible"
      width="90%"
      max-width="1100px"
      top="5vh">
      <el-row :gutter="12" >
        <el-col :span="12">
          <el-card shadow="always">
            <div slot="header" class="clearfix">
              <span>标题:</span>
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

  </div>
</template>

<script>
import {
  getDictDataByType,
  queryDataPage,
  getById,
  editById,
  deleteId,
} from "@/api/portalMangement/feedbackMangement";
import feedbackEdit from "@/views/portalMangement/feedbackMangement/feedbackEdit";
import WangEditor from 'wangeditor'
export default {
  components: {
    feedbackEdit,
  },
  data() {
    return {
      handleIndex:false,
      dialogVisible:false,
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: "",
        // feedbackType:"",
      },
      tableData: [],
      total: 0,
      //已勾选记录
      multipleSelection: [],
      // 弹窗
      dialogTitle: "",
      msgFormDialog: false,
      handleObj: {},
      editorNew:"",
      exhibition:"",
      exhibition2:"",
      editFeedbackData:"",
      isShow:"",
      feedbackTitle:"",
      feedbackTypeList:[],
    };
  },
  /** 方法调用 */
  created() {
    this.getList();

    getDictDataByType("portal_feedback_type").then(
      response => {
        this.feedbackTypeList = response.data;
      }
    );
  },
  methods: {
    // 类型
    typeShow: function (row) {
      if (row.feedbackType == "support") {
        return "服务与支持";
      }else if (row.feedbackType == "middleware_apply") {
        return "中间件申请";
      }else if (row.feedbackType == "data_manage") {
        return "数据管理";
      }else if (row.feedbackType == "assembly_line_manage") {
        return "流水线管理";
      }else if (row.feedbackType == "interface_manage") {
        return "接口管理";
      }else if (row.feedbackType == "user_info_manage") {
        return "用户信息管理";
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    showDialog(type) {
      this.dialogTitle = "编辑";
      if (type == "edit") {
        if (this.multipleSelection.length != 1) {
          this.$message({
            type: "error",
            message: "请选择一条数据",
          });
          return;
        }
        getById({ id: this.multipleSelection[0].id }).then((res) => {
          this.handleObj = res.data;
          this.msgFormDialog = true;
        });
      } else {
        getById({ id: type.id }).then((res) => {
          this.handleObj = res.data;
          this.msgFormDialog = true;
        });
      }
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /** 查询列表 */

    getList() {
      this.loading = true;
      queryDataPage(this.queryParams).then((response) => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
        //自动选中之前已选行
        if (this.multipleSelection.length == 1) {
          let newArry = this.multipleSelection;
          this.$nextTick(function () {
            this.toggleSelection(newArry);
          });
        }
      });
    },
    //设置表单选中指定行
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.tableData.forEach((element, index) => {
            if (element.id == row.id) {
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index]
              );
            }
          });
        });
      }
    },
    cancelDialog() {
      this.msgFormDialog = false;
      if (this.dialogTitle.indexOf("新增") != -1) {
        this.handleQuery();
      } else {
        this.getList();
      }
    },
    //处理
    updateFeed(row) {
      this.handleIndex = true;
      this.isShow = "";
      this.editFeedbackData = row;
      debugger
      getById({ id: row.id }).then((response) => {
        // this.feedbackContent = response.data.feedbackContent;
        this.exhibition = response.data.content;
        this.feedbackTitle = response.data.feedbackTitle;

      });
      this.$nextTick(function() {
        /*现在数据已经渲染完毕*/
        this.editorNew = new WangEditor('#editor');
        this.editorNew.config.uploadImgShowBase64 = true;
        this.editorNew.config.height = 518;
        this.editorNew.create();
      });
    },
    editDataSave(){
      debugger
      if(!this.editorNew.txt.html()){
        this.$message({
          showClose: true,
          message: '填写处理结果',
          type: 'error'
        });
      }else{
      this.editFeedbackData.checker = this.$store.getters.userName;
      this.editFeedbackData.reply = this.editorNew.txt.html();
      this.editFeedbackData.isShow = this.isShow;
      this.editFeedbackData.status = "1";
      editById(this.editFeedbackData).then((res) => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "回复成功",
          });
          this.handleIndex = false;
        } else {
          this.$message({
            type: "error",
            message: res.msg,
          });
        }
      });
      }
    },

    lookInfo(row) {
      this.dialogVisible = true;
      getById({ id: row.id }).then((response) => {
        // this.feedbackContent = response.data.feedbackContent;
        this.exhibition = response.data.content;
        this.exhibition2 = response.data.reply;
        this.feedbackTitle = response.data.feedbackTitle;
      });
    },

     deleteCell(row){
       this.$confirm("确定要删除这条数据吗?", "温馨提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",

      }).then(()=>{
         deleteId({id:row.id}).then((response) =>{
           if(response.code == 200){
             console.log("删除成功");
             this.handleQuery()
           }else{
             console.log("删除失败");
           }
         });
       }).catch(() => {});

    },

  },

};
</script>


