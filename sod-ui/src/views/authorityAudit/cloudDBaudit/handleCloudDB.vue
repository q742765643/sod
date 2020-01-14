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
            <el-form-item label="申请用户" prop="app_user">
              <el-select
                v-if="this.searchObj.application_id == undefined"
                size="small"
                filterable
                v-model="msgFormDialog.app_user"
                @change="userChange"
              >
                <el-option
                  v-for="(item,index) in userBox"
                  :key="index"
                  :label="item.username"
                  :value="item.userId"
                ></el-option>
              </el-select>
              <el-input v-else :disabled="true" size="small" v-model="msgFormDialog.app_user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请单位" prop="application_unit">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.application_unit"
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
            <el-form-item label="数据库类型" prop="storage_logic">
              <el-select
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.storage_logic"
                @change="storageChange"
              >
                <el-option
                  v-for="(item,index) in storageLogic"
                  :key="index"
                  :label="item.key_col"
                  :value="item.key_col"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数据库名" prop="db_name">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.db_name"
                placeholder="数据库名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应用系统" prop="application_system">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.application_system"
                placeholder="应用系统"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="申请存储空间大小" prop="storage_space">
              <el-select :disabled="isDetail" size="small" v-model="msgFormDialog.storage_space">
                <el-option
                  v-for="(item,index) in storageSpace"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途" prop="db_use">
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.db_use"
                placeholder="用途"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="申请CPU/内存"
              prop="cpu_memory"
              v-if="msgFormDialog.storage_logic !='Redis'&& msgFormDialog.storage_logic !='网络共享存储'"
            >
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.cpu_memory"
                placeholder="申请CPU/内存"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>审核信息</span>
        </div>
        <el-row
          type="flex"
          class="row-bg"
          justify="center"
          v-if="this.searchObj.application_id == undefined"
        >
          <el-col :span="8">
            <el-form-item label="审核人" prop="examiner">
              <el-input size="small" v-model="msgFormDialog.examiner" placeholder="审核人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="单位审核材料" prop="filepath">
              <el-upload
                v-model="msgFormDialog.filepath"
                class="upload-demo"
                accept=".docx"
                :limit="1"
                :on-exceed="handleExceed"
                :action="upLoadUrl"
                :before-remove="beforeRemove"
                :on-success="successUpload"
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
            <el-form-item label="审核状态" prop="examine_status">
              <el-radio
                @change="statusChange"
                :disabled="isDetailStatus"
                v-model="msgFormDialog.examine_status"
                label="02"
              >允许</el-radio>
              <el-radio
                @change="statusChange"
                :disabled="isDetailStatus"
                v-model="msgFormDialog.examine_status"
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
            <el-form-item label="单位审核材料" prop="examine_material">
              <el-button
                size="small"
                v-model="msgFormDialog.examine_material"
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
      <el-card class="box-card" v-if="examineStatus">
        <div slot="header" class="clearfix">
          <span>分配信息</span>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item
              label="挂载服务器"
              prop="mount_server"
              v-if="msgFormDialog.storage_logic !='Redis'&& msgFormDialog.storage_logic !='XUGU'"
            >
              <el-input
                :disabled="isDetail"
                size="small"
                v-model="msgFormDialog.mount_server"
                placeholder="挂载服务器"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-row>
              <el-col :span="20">
                <el-form-item
                  label="挂载目录"
                  prop="mount_directory"
                  v-if="msgFormDialog.storage_logic !='Redis'&& msgFormDialog.storage_logic !='XUGU'"
                >
                  <el-input
                    :disabled="isDetail"
                    size="small"
                    v-model="msgFormDialog.mount_directory"
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
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="数据库IP" prop="db_ip">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.db_ip"
                placeholder="数据库IP"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数据库端口号" prop="db_portnum">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.db_portnum"
                placeholder="数据库端口号"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <!-- <el-button
              v-if="this.searchObj.application_id == undefined"
              type="primary"
              size="small"
              icon="el-icon-paperclip"
              @click="demoDownload()"
            >验证链接</el-button>-->
            <el-form-item
              v-if="this.searchObj.application_id != undefined"
              label="实际分配存储空间"
              prop="new_storage_space"
            >
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.new_storage_space"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="8">
            <el-form-item label="数据库用户名" prop="db_username">
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.db_username"
                placeholder="数据库用户名"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-row :gutter="4">
              <el-col :span="18">
                <el-form-item label="数据库密码" prop="db_password">
                  <el-input
                    :disabled="isDetailStatus"
                    :type="passwordType"
                    size="small"
                    v-model="msgFormDialog.db_password"
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
            <el-form-item
              v-if="this.searchObj.new_cpu_memory!=''&&this.searchObj.new_cpu_memory!=null&&this.searchObj.new_cpu_memory!=undefined "
              label="实际分配CPU/内存"
              prop="new_cpu_memory"
            >
              <el-input
                :disabled="isDetailStatus"
                size="small"
                v-model="msgFormDialog.new_cpu_memory"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- <el-select v-model="msgFormDialog.ipChose" class="ipChose">
          <el-option label="指定IP" value="1"></el-option>
          <el-option label="IP段" value="2"></el-option>
      </el-select>-->
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { addInfo, getYunDbLogic } from "@/api/authorityAudit/cloudDBaudit";
import {
  testExport,
  chineseLengthTenValidation,
  telphoneNumValidation,
  portNumValidation,
  ipUrlValidation
} from "@/components/commonVaildate.js";
import {
  listBackup,
  getBackup,
  addBackup,
  updateBackup,
  delBackup,
  findAllDataBase,
  getByDatabaseId,
  getByDatabaseIdAndClassId
} from "@/api/schedule/backup/backup";
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
      upLoadUrl: "",
      isDisabled: false,
      examineStatus: true,
      userBox: [], //获取所有用户
      storageSpace: [],
      storageLogic: [],

      searchObj: {},
      contantTaskChose: [],
      msgFormDialog: {
        app_user: "",
        application_unit: "",
        telephone: "",
        storage_logic: "",
        db_name: "",
        application_system: "",
        storage_space: "",
        db_use: "",
        cpu_memory: "",
        mount_server: "",
        mount_directory: "",
        examiner: "",
        filepath: "",
        db_ip: "",
        db_portnum: "",
        db_username: "",
        db_password: ""
      },
      rules: {
        app_user: [
          { required: true, message: "请选择申请用户", trigger: "change" }
        ],
        application_unit: [
          { required: true, validator: nameValidate, trigger: "blur" }
        ],
        telephone: [
          { required: true, validator: phoneValidate, trigger: "blur" }
        ],
        storage_logic: [
          { required: true, message: "请选择数据库类型", trigger: "change" }
        ],
        db_name: [
          { required: true, message: "请输入数据库名", trigger: "blur" }
        ],
        application_system: [
          { required: true, message: "请输入应用系统", trigger: "blur" }
        ],
        storage_space: [
          { required: true, message: "请选择申请存储空间大小", trigger: "blur" }
        ],
        db_use: [
          { required: true, message: "请输入数据库用途", trigger: "blur" }
        ],
        cpu_memory: [
          { required: true, message: "请输入申请CPU/内存", trigger: "blur" }
        ],
        mount_server: [
          { required: true, message: "请输入挂载服务器", trigger: "blur" }
        ],
        mount_directory: [
          { required: true, message: "请输入挂载目录", trigger: "blur" }
        ],
        examiner: [
          { required: true, message: "请输入审核人", trigger: "blur" }
        ],
        filepath: [
          { required: true, message: "请选择审核材料", trigger: "change" }
        ],
        db_ip: [{ required: true, validator: ipValidate, trigger: "blur" }],
        db_portnum: [
          { required: true, validator: portValidate, trigger: "blur" }
        ],
        db_username: [
          { required: true, message: "请输入数据库用户名", trigger: "blur" }
        ],
        db_password: [
          { required: true, message: "请输入数据库密码", trigger: "blur" }
        ],
        failure_reason: [
          { required: true, message: "请输入拒绝原因", trigger: "blur" }
        ],
        new_cpu_memory: [
          { required: true, message: "请输入实际分配CPU/内存", trigger: "blur" }
        ],
        examine_status: [
          { required: true, message: "请选择审核状态", trigger: "change" }
        ],
        new_storage_space: [
          { required: true, message: "请输入实际分配存储空间", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.searchObj = this.handleObj;
    getYunDbLogic().then(response => {
      this.databaseOptions = response;
    });
    findAllDataBase().then(response => {
      console.log(response.data);
    });
    // this.getUserAll();
    // this.initServerDetail();
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
        .get(interfaceObj.download + "?filepath=" + url)
        .then(data => {
          testExport(interfaceObj.download + "?filepath=" + url);
        })
        .catch(function(error) {
          testExport(interfaceObj.download + "?filepath=" + url);
        });
    },
    // 查看详情时的下载
    downloadWord() {
      this.axios
        .get(
          interfaceObj.download +
            "?filepath=" +
            this.msgFormDialog.examine_material
        )
        .then(data => {
          testExport(
            interfaceObj.download +
              "?filepath=" +
              this.msgFormDialog.examine_material
          );
        })
        .catch(function(error) {
          testExport(
            interfaceObj.download +
              "?filepath=" +
              this.msgFormDialog.examine_material
          );
        });
    },
    // 上传限制
    handleExceed() {
      this.$message.warning("当前限制选择1个文件");
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.msgFormDialog.filepath = "";
        })
        .catch(() => {
          console.log("已取消删除");
        });
    },
    successUpload: function(response, file, fileList) {
      this.msgFormDialog.filepath = response.filepath;
    },
    // 申请用户
    userChange(selectUserId) {
      let obj = {};
      obj = this.userBox.find(item => {
        //这里的userBox就是上面遍历的数据源
        return item.userId === selectUserId; //筛选出匹配数据
      });
      this.msgFormDialog.telephone = obj.phone;
      this.msgFormDialog.application_unit = obj.deptName;
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
      this.msgFormDialog.storage_space = "";
      if (selectStorage == "Redis") {
        this.storageSpace = [
          { label: "2GB", value: "2GB" },
          { label: "8GB", value: "8GB" },
          { label: "16GB", value: "16GB" },
          { label: "32GB", value: "32GB" }
        ];
      } else if (selectStorage == "网络共享存储") {
        this.storageSpace = [
          { label: "500GB", value: "500GB" },
          { label: "2TB", value: "2TB" },
          { label: "4TB", value: "4TB" },
          { label: "8TB", value: "8TB" }
        ];
      } else if (selectStorage == "XUGU") {
        this.storageSpace = [
          { label: "5GB", value: "5GB" },
          { label: "10GB", value: "10GB" },
          { label: "50GB", value: "50GB" },
          { label: "100GB", value: "100GB" },
          { label: "200GB", value: "200GB" }
        ];
      }
    },
    // 获取服务器详情
    initServerDetail() {
      if (this.searchObj.application_id == undefined) {
        // 新增
      } else {
        // 修改
        this.msgFormDialog = this.searchObj;
        this.msgFormDialog.app_user = this.searchObj.application_user;
        this.isDetail = true;
        if (this.msgFormDialog.examine_status == "02") {
          this.examineStatus = true;
          this.isDetailStatus = true;
        } else if (this.msgFormDialog.examine_status == "03") {
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
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$emit("trueHandle", this.msgFormDialog);
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    cancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.$emit("cancelHandle");
    }
  }
};
</script>

<style lang="scss">
.handleCloudDialog {
  .el-card {
    margin-bottom: 20px;
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
