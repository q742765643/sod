<template>
  <section class="handleDataRecoveryDialog">
    <el-form
      :model="msgFormDialog"
      :rules="drrules"
      ref="ruleForm"
      label-width="120px"
      class="demo-ruleForm"
    >
      <!-- <el-card class="box-card"> -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="数据库名称" prop="databaseId">
            <el-select v-model="msgFormDialog.databaseId" size="small" @change="changeDataBaseName">
              <el-option
                v-for="(item,index) in databaseNameSelect"
                :key="index"
                :label="item.TEXT"
                :value="item.VALUE"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料名称" prop="dataClassId">
            <el-select size="small" v-model="msgFormDialog.dataClassId">
              <el-option
                v-for="(item,index) in dataNameSelect"
                :value="item.VALUE"
                :label="item.TEXT"
                :key="index"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="目录" prop="storageDirectory">
            <el-select size="small" v-model="msgFormDialog.storageDirectory">
              <el-option
                v-for="(item,index) in rootList"
                :key="index"
                :label="item.TEXT"
                :value="item.VALUE"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行端口和IP" prop="execIp">
            <el-select size="small" v-model="msgFormDialog.execIp">
              <el-option
                v-for="(item,index) in execIpList"
                :key="index"
                :label="item.TEXT"
                :value="item.VALUE"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="选择时间">
            <el-date-picker
              size="small"
              v-model="msgFormDialog.timeQuantum"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label>
            <el-button
              type="primary"
              @click="searchFun('ruleForm')"
              size="small"
              icon="el-icon-search"
            >查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="恢复文件列表">
            <el-input size="small" type="textarea" readonly v-model="msgFormDialog.filePath"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!--  </el-card> -->
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">md5数据校验</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
    <el-dialog
      width="70%"
      :title="innerTreeTitle"
      :visible.sync="innerTreeVisible"
      append-to-body
      v-dialogDrag
    >
      <InnerTree
        v-if="innerTreeVisible"
        :handleInnerObj="handleInnerObj"
        @handleInnerTrue="handleInnerTrue"
        @handleInnerCancel="handleInnerCancel"
        ref="myHandleChild"
      />
    </el-dialog>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// import { englishAndNumValidation } from "@/components/common.js";
// import InnerTree from "@/views/structureManagement/overviewStorage/handleGetDataFileList";
export default {
  name: "handleDataRecoveryDialog",
  components: {
    InnerTree
  },
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      rootList: [],
      execIpList: [],
      databaseNameSelect: [],
      dataNameSelect: [],
      searchObj: {},
      innerTreeTitle: "",
      innerTreeVisible: false,
      handleInnerObj: [],
      msgFormDialog: {
        databaseId: "",
        dataClassId: "",
        timeQuantum: "",
        execIp: "",
        filePath: ""
      },
      drrules: {
        databaseId: [
          { required: true, message: "请选择数据库", trigger: "change" }
        ],
        dataClassId: [
          { required: true, message: "请选择资料", trigger: "change" }
        ],
        storageDirectory: [
          { required: true, message: "请选择目录", trigger: "change" }
        ],
        execIp: [
          { required: true, message: "请选择执行IP和端口", trigger: "change" }
        ]
      }
    };
  },
  created() {
    // this.searchObj = this.handleObj;
    // this.getFindDataBase("");
    // this.axios
    //   .get(interfaceObj.databaseBackup_DataBaseFromDir, {
    //     params: { type: 38 }
    //   })
    //   .then(res => {
    //     this.rootList = res.data.data;
    //   });
    // this.axios.get(interfaceObj.databaseRecover_getIpList).then(res => {
    //   this.execIpList = res.data.data;
    // });
    // this.initServerDetail();
  },
  methods: {
    // 获取详情
    initServerDetail() {
      //this.msgFormDialog = this.searchObj;
      if (this.searchObj.databaseId) {
        this.msgFormDialog.databaseId = this.searchObj.databaseId;
      }
      if (this.searchObj.dataClassId) {
        this.msgFormDialog.dataClassId = this.searchObj.dataClassId;
      }
    },
    getFindDataBase(proname) {
      if (!proname) {
        proname = "backup";
      }
      this.axios
        .get(interfaceObj.getDataRecoveryFindDataBase + "&proName=" + proname)
        .then(res => {
          this.databaseNameSelect = res.data.data;
          if (this.msgFormDialog.databaseId) {
            this.getDataBaseName(this.msgFormDialog.databaseId);
          }
        });
    },
    changeDataBaseName(dvalue) {
      this.getDataBaseName(dvalue);
    },

    getDataBaseName(dId) {
      if (!dId) {
        return;
      }
      this.axios
        .get(
          interfaceObj.getDataRecoveryFindTableMapByDBIdLike +
            "&databaseId=" +
            dId
        )
        .then(res => {
          if (res.data.returnCode == "0") {
            this.msgFormDialog.dataClassId = "";
            this.dataNameSelect = res.data.data;
          }
        });
    },
    searchFun(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let searchFileObj = {};
          searchFileObj = this.msgFormDialog;
          searchFileObj.storageDirectory = this.msgFormDialog.storageDirectory;
          if (this.msgFormDialog.length > 0) {
            searchFileObj.beginTime = this.msgFormDialog.timeQuantum[0];
            searchFileObj.endTime = this.msgFormDialog.timeQuantum[1];
          }

          this.axios
            .post(interfaceObj.databaseRecover_getDataFileList, searchFileObj)
            .then(res => {
              this.innerTreeTitle =
                "物理库:" +
                this.msgFormDialog.databaseId +
                " | 存储编码:" +
                this.msgFormDialog.dataClassId;
              this.handleInnerObj = res.data.data;
              this.innerTreeVisible = true;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleInnerTrue(newArry) {
      var ids = "";
      for (var i = 0; i < newArry.length; i++) {
        if (i == newArry.length - 1) {
          ids += newArry[i].id;
        } else {
          ids += newArry[i].id + ",";
        }
      }
      this.msgFormDialog.filePath = ids;
      this.innerTreeVisible = false;
    },
    handleInnerCancel() {
      this.innerTreeVisible = false;
    },
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (!this.msgFormDialog.filePath) {
            this.$message({
              type: "error",
              message: "请选择恢复文件列表"
            });
            return false;
          }
          this.$emit("trueRecover", this.msgFormDialog);
        } else {
          //console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("handleCancel");
    }
  }
};
</script>

<style lang="scss">
.handleDataRecoveryDialog {
  .box-card {
    margin-bottom: 20px;
  }
  .el-form-item__content {
    display: flex;
  }
  .el-card__header {
    position: relative;
    padding-left: 40px;
  }
  .el-card__header:before {
    content: "";
    position: absolute;
    width: 4px;
    left: 20px;
    top: 12px;
    height: 55%;
    background-color: #409eff;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
  }
  .switchBox .el-form-item__content {
    margin-top: 10px;
  }
}
</style>
