<template>
  <!-- 评估 -->
  <div class="assesTemp">
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="queryBox">
      <el-form-item label="资料名称:">
        <el-select
          size="small"
          v-model="queryParams.dataname"
          filterable
          allow-create
          default-first-option
        >
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in datanameList"
            :key="index"
            :label="item.dataname"
            :value="item.dataname"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态:">
        <el-select size="small" v-model="queryParams.zt">
          <el-option label="全部" value></el-option>
          <el-option label="执行中" :value="0"></el-option>
          <el-option label="执行完成" :value="1"></el-option>
          <el-option label="执行异常" value="-1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <div class="toolBar">
      <el-button size="small" type="primary" @click="showdialogForm">质量评估</el-button>
    </div>
    <el-table
      :data="tableData.slice((queryParams.currentPage-1)*queryParams.pageSize,queryParams.currentPage*queryParams.pageSize)"
      style="width: 100%"
      border
      :default-sort="{prop: 'date', order: 'descending'}"
    >
      <el-table-column align="center" prop="dataname" label="资料名" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" prop="startime" label="开始时间" width="180">
        <template slot-scope="scope">{{scope.row.startime.split(' ')[0]}}</template>
      </el-table-column>
      <el-table-column align="center" prop="endtime" label="结束时间" width="180">
        <template slot-scope="scope">{{scope.row.endtime.split(' ')[0]}}</template>
      </el-table-column>
      <el-table-column sortable align="center" prop="createtime" label="创建时间" width="180">
        <template slot-scope="scope">{{scope.row.createtime.split('.')[0]}}</template>
      </el-table-column>
      <el-table-column align="center" prop="finishtime" label="完成时间" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.finishtime">{{scope.row.finishtime.split('.')[0]}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="batch" label="执行批次" width="80"></el-table-column>
      <el-table-column align="center" prop="zt" label="执行状态" width="160">
        <template slot-scope="scope">
          <div class="cell" v-if="scope.row.zt==0">
            <i class="el-icon-loading" style="color:#409EFF;"></i> 执行中
          </div>
          <div class="cell" v-if="scope.row.zt==1" type="text">
            <i class="el-icon-check" style="color:#67C23A;"></i> 执行完成
          </div>
          <div class="cell" v-if="scope.row.zt=='-1'" type="text">
            <i class="el-icon-close" style="color:#F56C6C;"></i> 执行异常
          </div>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="dataTotal" @paginationChange="paginationChange" ref="pagination"></Pagination>
    <el-dialog title="质量评估" :visible.sync="dialogVisible" width="50%" class="dialogForm">
      <el-form ref="form" :model="dialogForm" label-width="170px" :rules="rules">
        <el-form-item label="资料名" prop="dataname">
          <el-select
            v-model="dialogForm.dataname"
            filterable
            allow-create
            default-first-option
            placeholder="请选择"
            @change="keyup()"
            size="small"
          >
            <el-option
              v-for="(item,index) in datanameList"
              :key="index"
              :label="item.dataname"
              :value="item.dataname"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否按站号过滤" v-if="isShow">
          <el-select size="small" v-model="dialogForm.mark">
            <el-option label="不过滤" :value="0"></el-option>
            <el-option label="过滤" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间:" prop="startime">
          <el-date-picker
            size="small"
            v-model="dialogForm.startime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间:" prop="endtime">
          <el-date-picker
            size="small"
            v-model="dialogForm.endtime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="formFooter">
          <el-button size="small" type="primary" @click="onSubmit('form')">确定</el-button>
          <el-button size="small" @click="cancleDialog">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { interfaceObj } from "@/urlConfig.js";
//分页组件
import Pagination from "@/components/Pagination";
export default {
  components: { Pagination },
  data() {
    return {
      flag: false,
      // 遮罩层
      loading: true,
      queryParams: {
        currentPage: 1,
        pageSize: 10,
        dataname: "",
        zt: ""
      },
      dataTotal: 0,
      tableData: [],
      datanameList: [],
      // 弹窗
      isShow: false,
      dialogVisible: false,
      dialogForm: {
        dataname: "",
        startime: "",
        endtime: "",
        mark: 0
      },
      rules: {
        dataname: [
          { required: true, message: "请输入资料名称", trigger: "blur" }
        ],
        startime: [
          { required: true, message: "请输入开始时间", trigger: "blur" }
        ],
        endtime: [
          { required: true, message: "请输入结束时间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getdatanameList();
    this.handleQuery();
  },

  methods: {
    keyup() {
      if (this.dialogForm.dataname == "中国地面国家站") {
        this.isShow = true;
      } else {
        this.isShow = false;
      }
      this.axios
        .get(
          interfaceObj.existDataname +
            "?dataname=" +
            encodeURIComponent(this.dialogForm.dataname)
        )
        .then(res => {
          if (res.data.status == 200) {
            this.flag = true;
            this.$message({
              type: "error",
              message: res.data.msg
            });
          } else {
            this.flag = false;
          }
        });
    },
    paginationChange() {
      let paginateObj = this.$refs.pagination.paginateObj;
      this.queryParams = { ...this.queryParams, ...paginateObj };
    },
    getdatanameList() {
      this.axios.get(interfaceObj.getDatanameResult).then(res => {
        if (res.data.status == 200) {
          this.datanameList = res.data.data;
        }
      });
    },
    handleQuery() {
      console.log(this.queryParams);
      this.axios
        .get(interfaceObj.assesList, {
          params: this.queryParams
        })
        .then(res => {
          if (res.data.status == 200) {
            this.tableData = res.data.data;
            this.dataTotal = this.tableData.length;
          }
        });
    },
    showdialogForm() {
      this.dialogForm = {
        dataname: "",
        startime: "",
        endtime: "",
        mark: 0
      };
      this.dialogVisible = true;
    },
    cancleDialog() {
      this.$refs["form"].resetFields();
      this.dialogVisible = false;
      this.getdatanameList();
    },

    onSubmit(form) {
      if (this.flag) {
        this.$message({
          type: "error",
          message: "资料名称不存在，请重新填写"
        });
        return;
      }
      this.$refs[form].validate(valid => {
        if (valid) {
          let obj = this.dialogForm;
          let starArry = obj.startime.split(" ");
          obj.startime =
            starArry[0].split("-").join("") + starArry[1].split(":").join("");
          let endArry = obj.endtime.split(" ");
          obj.endtime =
            endArry[0].split("-").join("") + endArry[1].split(":").join("");

          console.log(obj);
          this.axios
            .get(
              interfaceObj.getCheckResult +
                "?dataname=" +
                encodeURIComponent(this.dialogForm.dataname) +
                "&startime=" +
                obj.startime +
                "&endtime=" +
                obj.endtime +
                "&mark=" +
                obj.mark
            )
            .then(res => {
              if (res.data.status == 200) {
                this.handleQuery();
                this.$message({
                  type: "success",
                  message: "正在进行资料质量评估"
                });
                this.cancleDialog();
              }
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss">
.assesTemp {
  .queryBox {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}
.dialogForm {
  .el-date-editor.el-input,
  .el-select {
    width: 100%;
  }
}
</style>
