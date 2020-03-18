<template>
  <section class="handleCloudDialog">
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="150px"
      label-position="right"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>申请信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="申请用户" prop="userId">
              <el-select
                v-if="this.searchObj.id == undefined"
                size="small"
                filterable
                v-model="msgFormDialog.userId"
                @change="userChange"
              >
                <el-option
                  v-for="(item,index) in userBox"
                  :key="index"
                  :label="item.username"
                  :value="item.userId"
                ></el-option>
              </el-select>
              <el-input v-else :disabled="true" size="small" v-model="msgFormDialog.userId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请单位" prop="department">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.department"
                placeholder="申请单位"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="telephone">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.telephone"
                placeholder="联系电话"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="数据库类型" prop="storageLogic">
              <el-select
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.storageLogic"
                @change="storageChange"
              >
                <el-option
                  v-for="(item,index) in storageLogicList"
                  :key="index"
                  :label="item.dictLabel"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数据库名" prop="databaseName">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.databaseName"
                placeholder="数据库名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应用系统" prop="applicationSystem">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.applicationSystem"
                placeholder="应用系统"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="申请存储空间大小" prop="storageSpace">
              <el-select :disabled="isDetail" size="small" v-model="msgFormDialog.storageSpace">
                <el-option
                  v-for="(item,index) in storageSpaceList"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途" prop="databaseUse">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.databaseUse"
                placeholder="用途"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="申请CPU/内存"
              prop="cpuMemory"
              v-if="msgFormDialog.storageLogic !='redis'&& msgFormDialog.storageLogic !='file'"
            >
              <el-select :disabled="isDetail" size="small" v-model="msgFormDialog.cpuMemory">
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
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>审核信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center" v-if="this.searchObj.id == undefined">
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input size="small" v-model="msgFormDialog.examiner" placeholder="审核人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="单位审核材料" prop="examineMaterial">
              <el-upload
                v-model="msgFormDialog.examineMaterial"
                class="upload-demo"
                accept=".docx"
                :limit="1"
                :on-exceed="handleExceed"
                :action="upLoadUrl"
                :on-success="successUpload"
                :before-upload="handleBefore"
                :data="uploadData"
              >
                <el-button size="small">点击上传(只能上传docx文件)</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" size="small" icon="el-icon-download" @click="demoClick">模板下载</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center" v-else>
          <el-col :span="8">
            <el-form-item label="审核状态" prop="examineStatus">
              <el-radio
                @change="statusChange"
                :disabled="isDetailStatus"
                v-model="msgFormDialog.examineStatus"
                label="02"
              >允许</el-radio>
              <el-radio
                @change="statusChange"
                :disabled="isDetailStatus"
                v-model="msgFormDialog.examineStatus"
                label="03"
              >拒绝</el-radio>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.examiner"
                placeholder="审核人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位审核材料" prop="examineMaterial">
              <el-button
                size="small"
                v-model="msgFormDialog.examineMaterial"
                @click="downloadWord"
              >下载查看审核文件</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center" v-if="isReason">
          <el-col :span="24">
            <el-form-item label="未通过审核原因" prop="failure_reason">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.failure_reason"
                placeholder="拒绝原因"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card" v-if="examineStatus || msgFormDialog.storageLogic !='database'">
        <div slot="header" class="clearfix">
          <span>分配信息</span>
        </div>
        <el-row
          type="flex"
          class="row-bg"
          justify="center"
          v-if="msgFormDialog.storageLogic !='redis'&& msgFormDialog.storageLogic !='database'"
        >
          <el-col :span="8">
            <el-form-item label="挂载服务器" prop="mountServer">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.mountServer"
                placeholder="挂载服务器"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-row>
              <el-col :span="20">
                <el-form-item label="挂载目录" prop="mountDirectory">
                  <el-input
                    :disabled="isDetail"
                    size="small"
                    v-model="msgFormDialog.mountDirectory"
                    placeholder="挂载目录"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" class="spacialBtn">
                <el-form-item>
                  <el-button type="primary" size="small" @click="helpPassword">帮助</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="8"></el-col>
        </el-row>
        <el-row
          type="flex"
          class="row-bg"
          justify="center"
          v-if="msgFormDialog.storageLogic !='file'"
        >
          <el-col :span="8">
            <el-form-item label="数据库IP" prop="databaseIp">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.databaseIp"
                placeholder="数据库IP"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数据库端口号" prop="databasePort">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.databasePort"
                placeholder="数据库端口号"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实际分配存储空间" prop="newStorageSpace">
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.newStorageSpace"
              >
                <el-option
                  v-for="(item,index) in storageSpaceList"
                  :key="index"
                  :label="item.dictValue"
                  :value="item.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row
          type="flex"
          class="row-bg"
          justify="center"
          v-if="msgFormDialog.storageLogic !='file'"
        >
          <el-col :span="8">
            <el-form-item label="数据库用户名" prop="databaseUsername">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.databaseUsername"
                placeholder="数据库用户名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-row :gutter="4">
              <el-col :span="18">
                <el-form-item label="数据库密码" prop="databasePassword">
                  <el-input
                    :disabled="isDetailStatus"
                    :type="passwordType"
                    size="small"
                    v-model="msgFormDialog.databasePassword"
                    placeholder="数据库密码"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <i
                  :class="passwordType === 'password' ? 'eye isEye' : 'el-icon-view isEye'"
                  @click="showPwd"
                ></i>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实际分配CPU/内存" prop="newCpuMemory">
              <el-select
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.newCpuMemory"
                v-if="msgFormDialog.storageLogic !='redis'&& msgFormDialog.storageLogic !='file'"
              >
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
      </el-card>
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
var baseUrl = process.env.VUE_APP_DM;
import { Encrypt } from "@/utils/htencrypt";
import {
  getDictDataByType,
  editCldb,
  saveCldb
} from "@/api/authorityAudit/cloudDBaudit";
import {
  chineseLengthTenValidation,
  telphoneNumValidation,
  portNumValidation,
  ipUrlValidation
} from "@/components/commonVaildate.js";

export default {
  name: "handleCloudDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    var nameValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入申请单位"));
      } else if (!chineseLengthTenValidation(value)) {
        callback(new Error("申请单位格式不正确"));
      } else {
        callback();
      }
    };
    var phoneValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入联系电话"));
      } else if (!telphoneNumValidation(value)) {
        callback(new Error("电话号码格式不正确"));
      } else {
        callback();
      }
    };
    var portValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入端口号"));
      } else if (!portNumValidation(value)) {
        callback(new Error("端口号格式不正确"));
      } else {
        callback();
      }
    };

    var ipValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入IP地址"));
      } else if (!ipUrlValidation(value)) {
        callback(new Error("请输入正确的IP地址"));
      } else {
        callback();
      }
    };

    return {
      passwordType: "",
      isReason: false,
      isDetail: false,
      isDetailStatus: false,
      uploadData: {},
      upLoadUrl: baseUrl + "/dm/cloudDatabaseApply/upload",
      isDisabled: false,
      examineStatus: true,
      userBox: [], //获取所有用户
      storageSpaceList: [],
      storageLogicList: [],

      searchObj: {},
      contantTaskChose: [],
      msgFormDialog: {
        userId: "admin",
        department: "",
        telephone: "",
        storageLogic: "",
        databaseName: "",
        applicationSystem: "",
        storageSpace: "",
        databaseUse: "",
        cpuMemory: "",
        mountServer: "",
        mountDirectory: "",
        examiner: "",
        examineMaterial: "",
        databaseIp: "",
        databasePort: "",
        databaseUsername: "",
        databasePassword: ""
      },
      cpuList: [],
      rules: {
        userId: [
          { required: true, message: "请选择申请用户", trigger: "change" }
        ],
        department: [
          { required: true, validator: nameValidate, trigger: "blur" }
        ],
        telephone: [
          { required: true, validator: phoneValidate, trigger: "blur" }
        ],
        storageLogic: [
          { required: true, message: "请选择数据库类型", trigger: "change" }
        ],
        databaseName: [
          { required: true, message: "请输入数据库名", trigger: "blur" }
        ],
        applicationSystem: [
          { required: true, message: "请输入应用系统", trigger: "blur" }
        ],
        storageSpace: [
          { required: true, message: "请选择申请存储空间大小", trigger: "blur" }
        ],
        databaseUse: [
          { required: true, message: "请输入数据库用途", trigger: "blur" }
        ],
        cpuMemory: [
          { required: true, message: "请输入申请CPU/内存", trigger: "blur" }
        ],
        mountServer: [
          { required: true, message: "请输入挂载服务器", trigger: "blur" }
        ],
        mountDirectory: [
          { required: true, message: "请输入挂载目录", trigger: "blur" }
        ],
        examiner: [
          { required: true, message: "请输入审核人", trigger: "blur" }
        ],
        examineMaterial: [
          { required: true, message: "请选择审核材料", trigger: "change" }
        ],
        databaseIp: [
          { required: true, validator: ipValidate, trigger: "blur" }
        ],
        databasePort: [
          { required: true, validator: portValidate, trigger: "blur" }
        ],
        databaseUsername: [
          { required: true, message: "请输入数据库用户名", trigger: "blur" }
        ],
        databasePassword: [
          { required: true, message: "请输入数据库密码", trigger: "blur" }
        ],
        failure_reason: [
          { required: true, message: "请输入拒绝原因", trigger: "blur" }
        ],
        newCpuMemory: [
          { required: true, message: "请输入实际分配CPU/内存", trigger: "blur" }
        ],
        examineStatus: [
          { required: true, message: "请选择审核状态", trigger: "change" }
        ],
        newStorageSpace: [
          { required: true, message: "请输入实际分配存储空间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.searchObj = this.handleObj;
    this.msgFormDialog = this.handleObj;
    this.msgFormDialog.userId = "admin";
    this.getDicts("cloud_database_type").then(response => {
      this.storageLogicList = response.data;
    });
    this.getDicts("cloud_cup_type").then(response => {
      this.cpuList = response.data;
    });
    this.initServerDetail();
  },
  methods: {
    // 眼睛开关
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
    },
    demoClick() {
      let url = interfaceObj.demoWordUrl + "云数据库申请表模板.docx";
      this.axios
        .get(interfaceObj.download + "?examineMaterial=" + url)
        .then(data => {
          testExport(interfaceObj.download + "?examineMaterial=" + url);
        })
        .catch(function(error) {
          testExport(interfaceObj.download + "?examineMaterial=" + url);
        });
    },
    // 查看详情时的下载
    downloadWord() {
      let path = this.msgFormDialog.examineMaterial;
      let obj = {
        filepath: path
      };
      let flieData = Encrypt(JSON.stringify(obj));
      flieData = encodeURIComponent(flieData);
      window.location.href =
        baseUrl +
        "/dm/cloudDatabaseApply/download?sign=111111&data=" +
        flieData;
    },
    // 上传限制
    handleExceed() {
      this.$message.warning("当前限制选择1个文件");
    },

    handleBefore(file) {
      this.uploadData.filename = file;
    },
    successUpload: function(response, file, fileList) {
      this.msgFormDialog.examineMaterial = response.msg;
    },
    // 申请用户
    userChange(selectUserId) {
      /*  let obj = {};
      obj = this.userBox.find(item => {
        //这里的userBox就是上面遍历的数据源
        return item.userId === selectUserId; //筛选出匹配数据
      });
      this.msgFormDialog.telephone = obj.phone;
      this.msgFormDialog.department = obj.deptName; */
    },
    // 获取所有用户信息
    getUserAll() {
      this.axios.get(interfaceObj.databaseUser_allUserList).then(res => {
        this.userBox = res.data.data;
      });
    },

    // 数据库改变
    storageChange(selectStorage) {
      console.log(selectStorage);
      this.msgFormDialog.storageSpaceList = [];
      getDictDataByType("cloud_" + selectStorage + "_storage_space").then(
        response => {
          this.storageSpaceList = response.data;
        }
      );
    },
    // 获取服务器详情
    initServerDetail() {
      if (this.searchObj.id) {
        // 修改
        this.msgFormDialog = this.searchObj;
        this.msgFormDialog.userId = this.searchObj.application_user;
        this.isDetail = true;
        if (this.msgFormDialog.examineStatus == "02") {
          this.examineStatus = true;
          this.isDetailStatus = true;
        } else if (this.msgFormDialog.examineStatus == "03") {
          this.isDetailStatus = true;
          this.examineStatus = false;
          this.isReason = true;
        } else {
          this.isDetailStatus = false;
          this.examineStatus = false;
          console.log("没有状态");
        }
      }
    },
    //
    statusChange(val) {
      if (val == "02") {
        this.examineStatus = true;
        this.isReason = false;
      } else {
        this.examineStatus = false;
        this.isReason = true;
      }
    },
    // 密码帮助
    helpPassword() {
      const h = this.$createElement;
      this.$msgbox({
        title: "帮助",
        customClass: "helpMsgBox",
        message: h("p", null, [
          h(
            "span",
            { style: "font-size: 16px" },
            "磁盘挂载到服务器的操作步骤： "
          ),
          h("p", null, "(1)打开服务器中/etc/fstab文件 "),
          h(
            "p",
            null,
            "(2)增加一行：nfs03.lan.nmic.cn:/nmic-dashuju-20181226 /本地路径(提前创建好) nfs nfsvers=3,async,nolock,noatime,nodiratime,wsize=1048576,rsize=1048576,timeo=600  0 0"
          ),
          h("p", null, "(3)文件编辑完成，保存退出"),
          h("p", null, "(4)在命令行输入mount -a，进行磁盘挂载"),
          h("p", null, " (5)在命令行输入df -h，查看是否挂载成功")
        ]),
        confirmButtonText: "确定"
      })
        .then(action => {})
        .catch(() => {});
    },

    trueDialog(formName) {
      if (this.isDetailStatus == true) {
        this.$refs[formName].resetFields();
        this.$emit("cancelHandle");
      } else {
        console.log(this.msgFormDialog);
        this.$refs[formName].validate(valid => {
          if (valid) {
            if (this.handleObj.id) {
              editCldb(this.msgFormDialog).then(res => {
                if (res.code == 200) {
                  this.$message({
                    type: "success",
                    message: "编辑成功"
                  });
                  this.$emit("handleDialogClose");
                } else {
                  this.$message({
                    type: "error",
                    message: res.msg
                  });
                }
              });
            } else {
              this.msgFormDialog.examineStatus = "02";
              saveCldb(this.msgFormDialog).then(res => {
                if (res.code == 200) {
                  this.$message({
                    type: "success",
                    message: "增加成功"
                  });
                  this.$emit("handleDialogClose");
                } else {
                  this.$message({
                    type: "error",
                    message: res.msg
                  });
                }
              });
            }
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.$emit("handleDialogClose");
    }
  }
};
</script>

<style lang="scss">
.handleCloudDialog {
  .el-card {
    margin-bottom: 20px;
  }
  .el-select {
    width: 100%;
  }
  .el-checkbox + .el-checkbox {
    margin-left: 0;
  }
  .el-form-item.is-validating .el-input__inner {
    border-color: #67c23a;
  }
  .helpMsgBox {
    width: 636px;
  }
  .el-button a {
    color: #fff;
  }
  .spacialBtn {
    .el-form-item__content {
      margin-left: 70px !important;
    }
  }
}
</style>
