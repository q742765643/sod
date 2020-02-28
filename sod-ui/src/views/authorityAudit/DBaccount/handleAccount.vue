<template>
  <section class="handleAccountDialog">
    <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-row>
        <el-col :span="7">
          <el-form-item label="账户ID" prop="databaseUpId">
            <el-input
              size="small"
              v-model="msgFormDialog.databaseUpId"
              :disabled="userDiasbled"
              placeholder="账户ID"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-row>
            <el-col :span="22">
              <el-form-item label="密码" prop="databaseUpPassword">
                <el-input
                  size="small"
                  :type="passwordType"
                  v-model="msgFormDialog.databaseUpPassword"
                  :disabled="userDiasbled"
                  placeholder="密码"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <i :class="passwordType === 'password' ? 'eye' : 'el-icon-view'" @click="showPwd"></i>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="7">
          <el-form-item label="申请绑定IP" prop="databaseUpIp">
            <el-input
              size="small"
              disabled
              v-model="msgFormDialog.databaseUpIp"
              :placeholder="palceholderIp"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item class="unitFormItem">
            <el-button size="small" type="primary" @click="showEditIp">编辑</el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="数据库选择" prop="applyDatabaseId">
            <el-transfer v-model="msgFormDialog.applyDatabaseId" :data="dataBaseBox"></el-transfer>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="关联用户" prop="userId">
            <el-select
              size="small"
              filterable
              v-model="msgFormDialog.userId"
              @change="userChange"
              :disabled="isDisabled"
              placeholder="请选择"
            >
              <el-option
                v-for="item in userBox"
                :key="item.value"
                :label="item.username"
                :value="item.userId"
                :disabled="item.disabled"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="机构">
            <el-input
              size="small"
              v-model="msgFormDialog.department"
              :disabled="isDisabled"
              placeholder="机构"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="联系方式">
            <el-input
              size="small"
              v-model="msgFormDialog.userPhone"
              :disabled="isDisabled"
              placeholder="联系方式"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
      <el-row>
        <el-col :span="21">
          <el-form-item label="用途说明" prop="databaseUpDesc">
            <el-input
              size="small"
              v-model="msgFormDialog.databaseUpDesc"
              :disabled="isDisabled"
              type="textarea"
              placeholder="用途说明"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="21">
          <el-form-item label="申请材料" class="fileInput">
            <el-upload
              v-show="isHideAdd"
              class="upload-demo"
              accept=".docx"
              drag
              :limit="1"
              :on-exceed="handleExceed"
              :action="upLoadUrl"
              :before-remove="beforeRemove"
              :on-success="successUpload"
              name="filename"
              multiple
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                将文件拖到此处，或
                <em>点击上传</em>（只能上传docx文件）
              </div>
            </el-upload>
            <el-button
              type="primary"
              size="small"
              icon="el-icon-download"
              class="reloaddemo"
              @click="demoClick"
              v-show="isHideAdd"
            >模板下载</el-button>
            <el-input
              size="small"
              type="text"
              disabled
              v-model="msgFormDialog.applyMaterial"
              v-show="isHide"
              style="width:92%;"
            ></el-input>
            <el-button
              type="success"
              class="methodReload"
              size="small"
              icon="el-icon-download"
              v-show="isHide"
              @click="downloadWord"
            >下载</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer" v-show="isHideAdd">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
    <!-- 审核 -->
    <div slot="footer" class="dialog-footer" v-show="isHide">
      <el-button type="primary" @click="trueDialog('ruleForm')">通 过</el-button>
      <el-button @click="cancelDialog('ruleForm')">不 通 过</el-button>
    </div>
    <el-dialog width="30%" title="信息" :visible.sync="innerVisible" append-to-body>
      <div class="ipDialog">
        <el-alert title="范例(指定IP:192.168.1.1;IP段：192.168.1.%)" type="info" :closable="false"></el-alert>
        <el-form :model="ipArryForm" label-width="30px" ref="ipform" style="margin-top:10px;">
          <el-form-item
            v-for="(domain,index) in ipArryForm.domains"
            :key="domain.key"
            :label="index+1+''"
          >
            <el-input v-model="domain.value" class="elInput"></el-input>
            <i class="el-icon-plus" @click="addDomain"></i>
            <i class="el-icon-minus" @click.prevent="removeDomain(domain)"></i>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="iptrueDialog('ipform')">确 定</el-button>
          <el-button @click="ipcancelDialog('ipform')">取 消</el-button>
        </div>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import { Encrypt } from "@/utils/htencrypt";
import {
  databaseList,
  addTable,
  ifUPExist,
  getById,
  download,
  update
} from "@/api/authorityAudit/DBaccount";
var baseUrl = process.env.VUE_APP_WLEI;
import {
  unstartnumValidation,
  englishAndNumValidation,
  ipUrlValidation,
  ipUrlValidation2
} from "@/components/commonVaildate.js";
export default {
  name: "handleAccountDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    const idValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入账户ID"));
      } else if (unstartnumValidation(value)) {
        callback(new Error("数据库账号不能以数字开头"));
      } else if (this.handleObj.databaseUpId == undefined) {
        ifUPExist({ databaseUPId: value }).then(res => {
          if (res.ifExist == "YES") {
            console.log("重复");
            callback(new Error("数据库账号不能重复"));
          } else {
            console.log("不重复");
            callback();
          }
        });
      } else {
        callback();
      }
    };
    const passwordValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入数据库密码"));
      } else if (!englishAndNumValidation(value)) {
        callback(new Error("密码必须由英文字母和数字组成"));
      } else {
        callback();
      }
    };

    const ipValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入IP地址"));
      } else if (
        this.msgFormDialog.databaseUpIpSegment == 1 &&
        !ipUrlValidation(value)
      ) {
        callback(new Error("请输入正确IP地址"));
      } else if (
        this.msgFormDialog.databaseUpIpSegment == 2 &&
        !ipUrlValidation2(value)
      ) {
        callback(new Error("请输入正确IP段地址"));
      } else {
        callback();
      }
    };

    return {
      ipArryForm: {
        domains: [
          {
            value: "",
            select: ""
          }
        ]
      },
      innerVisible: false,
      downUrl: "",
      upLoadUrl: baseUrl + "/dm/databaseUser/api/databaseUser/upload",
      isDisabled: false,
      userDiasbled: false,
      isHideAdd: true,
      isHide: false,
      passwordType: "password",
      searchObj: {},
      userBox: [], //获取所有用户
      msgFormDialog: {
        databaseUpId: "USR_", //UP账户ID
        databaseUpPassword: "", //UP账户密码
        databaseUpIp: "", //UP账户IP
        databaseUpIpSegment: "1", //UP账户IP可选区间
        applyDatabaseId: [], //物理库ID
        userId: "10", //用户ID
        databaseUpDesc: "", //UP账户描述
        applyMaterial: ""
      },
      palceholderIp: "指定IP样例:192.168.1.1",
      dataBaseBox: [],
      rules: {
        databaseUpId: [
          { required: true, validator: idValidate, trigger: "blur" }
        ],
        databaseUpPassword: [
          { required: true, validator: passwordValidate, trigger: "blur" }
        ],
        databaseUpIp: [
          { required: true, validator: ipValidate, trigger: "blur" }
        ],
        applyDatabaseId: [
          { required: true, message: "请选择数据库", trigger: "blur" }
        ],
        databaseUpDesc: [
          { required: true, message: "请输入用途说明", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "请选择关联用户", trigger: "change" }
        ],
        applyMaterial: [
          { required: true, message: "请选择申请材料", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.searchObj = this.handleObj;
    this.getDBlist();
    // this.getUserAll();
    this.initServerDetail();
  },
  methods: {
    // 申请绑定IP
    removeDomain(item) {
      var index = this.ipArryForm.domains.indexOf(item);
      if (index !== 0) {
        this.ipArryForm.domains.splice(index, 1);
      }
    },
    addDomain() {
      this.ipArryForm.domains.push({
        value: "",
        key: Date.now()
      });
    },
    ipcancelDialog(formName) {
      this.$refs[formName].resetFields();
      this.innerVisible = false;
    },
    iptrueDialog(formName) {
      console.log(this.ipArryForm.domains);
      let ips = [];
      let arrys = this.ipArryForm.domains;
      arrys.forEach(element => {
        ips.push(element.value);
      });
      this.msgFormDialog.databaseUpIp = ips.join(",");
      this.$refs[formName].resetFields();
      this.innerVisible = false;
    },
    showEditIp() {
      this.innerVisible = true;
    },
    // 眼睛开关
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
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
          this.msgFormDialog.applyMaterial = "";
        })
        .catch(() => {
          console.log("已取消删除");
        });
    },
    successUpload: function(response, file, fileList) {
      this.msgFormDialog.applyMaterial = response.msg;
    },

    // 关联用户
    userChange(selectUserId) {
      let obj = {};
      obj = this.userBox.find(item => {
        //这里的userBox就是上面遍历的数据源
        return item.userId === selectUserId; //筛选出匹配数据
      });
      this.msgFormDialog.userPhone = obj.phone;
      this.msgFormDialog.department = obj.deptName;
    },
    // 获取数据库
    async getDBlist() {
      await databaseList().then(response => {
        var resdata = response.data;
        var dataList = [];
        for (var i = 0; i < resdata.length; i++) {
          var obj = {};
          obj.key = resdata[i].id;
          obj.label = resdata[i].databaseName;
          dataList.push(obj);
        }

        this.dataBaseBox = dataList;
      });
    },
    // 获取所有用户信息
    async getUserAll() {
      await this.axios.get(interfaceObj.databaseUser_allUserList).then(res => {
        var allData = res.data.data;
        this.axios.get(interfaceObj.databaseUser_UserList).then(res => {
          var disdata = res.data.data;
          for (var i = 0; i < allData.length; i++) {
            for (var d = 0; d < disdata.length; d++) {
              if (allData[i].userId == disdata[d].userId) {
                allData[i].disabled = true;
              }
            }
          }
          this.userBox = allData;
        });
      });
    },
    // 获取服务器详情
    async initServerDetail() {
      if (!this.searchObj.id) {
        // 新增
      } else {
        // 修改
        if (this.searchObj.examine_status == "0") {
          this.userDiasbled = false;
        } else if (this.searchObj.examine_status == "2") {
          this.userDiasbled = false;
        } else if (this.searchObj.examine_status == "1") {
          this.userDiasbled = true;
        }
        // this.isDisabled = true;
        this.isHide = true;
        this.isHideAdd = false;
        await getById({ id: this.searchObj.id }).then(res => {
          if (res.code == 200) {
            let data = res.data;
            data.applyDatabaseId = data.applyDatabaseId.split(",");
            console.log(data.applyDatabaseId);
            this.msgFormDialog = data;
            /* this.msgFormDialog = res.data.data.databaseobj;
              // 回显数据库选择
              let transferDataList = res.data.data.databaseList;
              let transferarry = [];
              transferDataList.forEach(element => {
                transferarry.push(element.applyDatabaseId);
              });
              this.msgFormDialog.applyDatabaseId = transferarry;
              // 用户信息
              this.userChange(this.msgFormDialog.userId);
              console.log(this.msgFormDialog.userId);
              this.downUrl =
                interfaceObj.download + res.data.data.applyMaterial; */
          }
        });
      }
    },
    demoClick() {
      let url = interfaceObj.demoWordUrl + "大数据云平台存储账户申请模板.docx";
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
      let path = this.msgFormDialog.applyMaterial;
      let obj = {
        filepath: path
      };
      let flieData = Encrypt(JSON.stringify(obj));
      flieData = encodeURIComponent(flieData);
      console.log(
        baseUrl + "/dm/databaseUser/download?sign=111111&data=" + flieData
      );
      window.location.href =
        baseUrl + "/dm/databaseUser/download?sign=111111&data=" + flieData;
    },

    trueDialog(formName) {
      if (this.isHideAdd && !this.msgFormDialog.applyMaterial) {
        this.$message({
          message: "请上传申请材料",
          type: "error",
          offset: 100
        });
        return;
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            if (this.isHideAdd) {
              let obj = this.msgFormDialog;
              obj.applyDatabaseId = obj.applyDatabaseId.join(",");
              console.log(obj);
              // 新增
              addTable(obj).then(res => {
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
            } else {
              // 数据库账户审核 通过
              this.auditMethods("");
            }
            /*  if (this.searchObj.record_id != undefined) {
              this.msgFormDialog.record_id = this.searchObj.record_id;
            } */
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    cancelDialog(formName) {
      if (!this.searchObj.id) {
        this.$refs[formName].resetFields();
        this.$emit("handleDialogClose");
      } else {
        // 审核不通过
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$prompt("请输入拒绝原因", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消"
            })
              .then(({ value }) => {
                this.auditMethods(value);
              })
              .catch(() => {});
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    auditMethods(value) {
      if (value) {
        this.msgFormDialog.examineStatus = 1;
      } else {
        this.msgFormDialog.examineStatus = 2;
      }
      this.msgFormDialog.failureReason = value;
      let obj = this.msgFormDialog;
      obj.applyDatabaseId = obj.applyDatabaseId.join(",");
      console.log(obj);
      // 审核
      update(obj).then(res => {
        if (res.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功"
          });
          this.$emit("handleDialogClose");
        } else {
          this.$message({
            type: "error",
            message: res.msg
          });
        }
      });
      this.$emit("handleDialogClose");
    }
  }
};
</script>

<style lang="scss">
.handleAccountDialog {
  .methodReload {
    margin-left: 4px;
    padding: 9px 10px;
    a {
      color: #fff;
    }
  }
  .reloaddemo {
    position: absolute;
    right: 0;
    top: 0;
    a {
      color: #fff;
    }
  }
  .el-form-item.is-validating .el-input__inner {
    border-color: #67c23a;
  }
  .el-transfer-panel {
    width: 370px;
  }

  .el-transfer-panel__body {
    height: 166px;
    overflow: auto;
  }
  .el-transfer-panel__item {
    display: block;
  }
  .el-checkbox-group.el-transfer-panel__list {
    overflow: hidden;
  }
  .upload-demo {
    display: flex;
  }
  .eye,
  .el-icon-view {
    cursor: pointer;
    position: absolute;
    font-size: 18px;
    top: 12px;
    right: 12px;
    width: 16px;
    height: 16px;
    z-index: 10;
    background: url("../../../assets/image/eye.png") no-repeat;
    background-size: 100%;
  }
  .el-icon-view {
    background: none;
  }
}
.ipDialog {
  .el-input {
    width: 86%;
  }
  .el-icon-plus,
  .el-icon-minus {
    width: 16px;
    height: 16px;
    color: #fff;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
    line-height: 16px;
  }
  .el-icon-plus {
    background: #409eff;
  }
  .el-icon-minus {
    background: #f56c6c;
    margin-left: 8px;
  }
}
</style>
