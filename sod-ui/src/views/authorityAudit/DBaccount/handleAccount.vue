<template>
  <section class="handleAccountDialog">
    <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-row>
        <el-col :span="7" v-if="!handleObj.pageName">
          <el-form-item label="账户ID" prop="databaseUpId">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.databaseUpId"
              :disabled="userDiasbled"
              placeholder="账户ID"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7" v-if="handleObj.pageName == '业务用户审核'">
          <el-form-item label="用户名">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.userName"
              :disabled="userDiasbled"
              placeholder="用户名"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="申请绑定IP" prop="databaseUpIp">
            <el-input
              size="small"
              disabled
              v-model.trim="msgFormDialog.databaseUpIp"
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
      <el-row v-if="dbFalg">
        <el-col :span="24">
          <el-form-item label="数据库选择" prop="applyDatabaseIdList">
            <el-transfer
              :titles="['未选择数据库列表', '已选择数据库列表']"
              v-model.trim="msgFormDialog.applyDatabaseIdList"
              :data="dataBaseBox"
            ></el-transfer>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="!handleObj.pageName">
        <el-col :span="7">
          <el-form-item label="关联用户" prop="userId">
            <el-select
              size="small"
              filterable
              v-model.trim="msgFormDialog.userId"
              @change="userChange"
              :disabled="isDisabled"
              placeholder="请选择"
            >
              <el-option
                v-for="(item,index) in userBox"
                :key="index"
                :label="item.webUsername"
                :value="item.userName"
                :disabled="item.disabled"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="机构">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.deptName"
              :disabled="isDisabled"
              placeholder="机构"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="联系方式">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.phonenumber"
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
              v-model.trim="msgFormDialog.databaseUpDesc"
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
            <label slot="label">
              <i style="color:red;">*</i>&nbsp;申请材料
            </label>
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
              :headers="myHeaders"
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
              class="reloaddemo"
              v-show="isHideAdd"
              size="small"
              type="success"
              @click="demoExport"
              icon="el-icon-document"
            >模板下载</el-button>
            <el-button
              class="preViewBox"
              v-show="isHideAdd"
              size="small"
              type="success"
              @click="previewDocx"
              icon="el-icon-document"
            >预览</el-button>
            <el-input
              size="small"
              type="text"
              disabled
              v-model.trim="msgFormDialog.applyMaterial"
              v-show="isHide"
              style="width:80%;"
            ></el-input>
            <el-button
              class="preViewBox2"
              v-show="isHide"
              size="small"
              type="success"
              @click="previewDocx"
              icon="el-icon-document"
            >预览</el-button>
            <el-button
              class="reloaddemo"
              v-show="isHide"
              size="small"
              type="success"
              @click="detailExport"
              icon="el-icon-document"
            >下载</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="3"></el-col>
      </el-row>
    </el-form>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer" v-show="isHideAdd" v-if="!handleObj.pageName">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
    <!-- 审核 -->
    <div slot="footer" class="dialog-footer" v-show="isHide" v-if="!handleObj.pageName">
      <el-button type="primary" @click="trueDialog('ruleForm')">通 过</el-button>
      <el-button @click="cancelDialog('ruleForm')">不 通 过</el-button>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      width="30%"
      title="IP地址"
      :visible.sync="innerVisible"
      append-to-body
      v-dialogDrag
    >
      <div class="ipDialog">
        <el-alert title="范例(指定IP:192.168.1.1;IP段：192.168.1.%)" type="info" :closable="false"></el-alert>
        <el-form :model="ipArryForm" label-width="30px" ref="ipform" style="margin-top:10px;">
          <el-form-item
            v-for="(domain,index) in ipArryForm.domains"
            :key="domain.key"
            :label="index+1+''"
          >
            <el-input v-model.trim="domain.value" class="elInput"></el-input>
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
var baseUrl = process.env.VUE_APP_DM;
import { getToken, createSign } from "@/utils/auth";
import {
  databaseList,
  addTable,
  addBzi,
  ifUPExist,
  getById,
  update,
  demoDownload,
  exportData,
  getBizDatabaseUser
} from "@/api/authorityAudit/DBaccount";
import { getUserByType } from "@/api/authorityAudit/cloudDBaudit";
import { downloadTable } from "@/api/structureManagement/exportTable";
import {
  unstartnumValidation,
  englishAndNumValidation,
  ipUrlValidation,
  ipUrlValidation2
} from "@/components/commonVaildate.js";

var token = getToken();
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
        callback(new Error("账户ID不能以数字开头"));
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
      userBox: [], //获取所有用户
      msgFormDialog: {
        databaseUpId: "USR_", //UP账户ID
        databaseUpIp: "", //UP账户IP
        databaseUpIpSegment: "1", //UP账户IP可选区间
        applyDatabaseIdList: [], //物理库ID
        applyDatabaseId: "",
        userId: "", //用户ID
        databaseUpDesc: "", //UP账户描述
        applyMaterial: "",
        pdfPath: ""
      },
      palceholderIp: "指定IP样例:192.168.1.1",
      dataBaseBox: [],
      handleDocxObj: {}, //预览
      myHeaders: { Authorization: token },
      rules: {
        databaseUpId: [
          { required: true, validator: idValidate, trigger: "blur" },
          {
            min: 1,
            max: 16,
            message: "账户ID长度不能超过16个字符",
            trigger: "blur"
          }
        ],

        databaseUpIp: [
          { required: true, message: "请输入IP地址", trigger: "blur" }
        ],
        applyDatabaseIdList: [
          { required: true, message: "请选择数据库", trigger: "change" }
        ],
        databaseUpDesc: [
          { required: true, message: "请输入用途说明", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "用途说明长度不能超过50个字符",
            trigger: "blur"
          }
        ],
        userId: [
          { required: true, message: "请选择关联用户", trigger: "change" }
        ],
        applyMaterial: [
          { required: true, message: "请选择申请材料", trigger: "blur" }
        ]
      },
      dbFalg: true //是否显示穿梭框
    };
  },
  async created() {
    await this.getUserAll();
    await this.getDBlist();
    if (this.handleObj.pageName == "业务用户审核") {
      getBizDatabaseUser({
        userId: this.handleObj.userName,
        databaseUpId: this.handleObj.userName
      }).then(res => {
        if (res.data && this.handleObj.checked != "0") {
          this.msgFormDialog = res.data;
          if (res.data.examineDatabaseId) {
            this.msgFormDialog.applyDatabaseIdList = res.data.examineDatabaseId.split(
              ","
            );
            this.msgFormDialog.applyDatabaseId = res.data.examineDatabaseId;
          }
          if (res.data.databaseUpId) {
            this.msgFormDialog.userName = res.data.databaseUpId;
          }
        } else {
          this.msgFormDialog.userName = this.handleObj.userName;
          this.msgFormDialog.userId = this.handleObj.userName;
          this.msgFormDialog.databaseUpId = this.handleObj.userName;
          this.msgFormDialog.databaseUpIp = this.handleObj.bizIp;
          this.msgFormDialog.databaseUpDesc = this.handleObj.remark;
          this.msgFormDialog.applyMaterial = this.handleObj.applyPaper;
          if (this.handleObj.dbIds) {
            this.msgFormDialog.applyDatabaseIdList = this.handleObj.dbIds.split(
              ","
            );
            // /dm/databaseUser/databaseUserExi
            // get接口查询，如果返回true,不能进行下一步操作，只能拒绝
          } else {
            this.dbFalg = false;
          }

          this.msgFormDialog.applyDatabaseId = this.handleObj.dbIds;
          this.msgFormDialog.pdfPath = this.handleObj.pdfPath;
          this.msgFormDialog.databaseUpPassword = this.handleObj.password;
        }
      });
      this.userDiasbled = true;
      this.isHideAdd = false;
      this.isHide = true;
    } else {
      this.initServerDetail();
    }

    console.log(this.handleObj);
  },
  methods: {
    // 模板下载
    demoExport() {
      demoDownload().then(res => {
        this.downloadfileCommon(res);
      });
    },
    // 下载
    detailExport() {
      downloadTable({ filePath: this.msgFormDialog.applyMaterial }).then(
        res => {
          this.downloadfileCommon(res);
        }
      );
    },
    // 预览
    previewDocx() {
      if (this.msgFormDialog.pdfPath) {
        window.open(baseUrl + this.msgFormDialog.pdfPath);
      } else {
        this.$message({
          type: "error",
          message: "当前没有可预览文件"
        });
      }
    },
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
      for (let i = 0; i < ips.length; i++) {
        if (ips[i].trim() != "%") {
          if (!ipUrlValidation(ips[i]) && !ipUrlValidation2(ips[i])) {
            this.$message({
              type: "error",
              message: "请输入正确IP地址"
            });
            return;
          }
        }
      }

      this.msgFormDialog.databaseUpIp = ips.join(",");
      this.$refs[formName].resetFields();
      this.innerVisible = false;
    },
    showEditIp() {
      this.innerVisible = true;
      /* ipArryForm: {
          domains: [
            {
              value: "",
              select: ""
            }
          ]
        } */
      if (this.msgFormDialog.databaseUpIp) {
        let ipsArry = this.msgFormDialog.databaseUpIp.split(",");
        this.ipArryForm.domains = [];
        ipsArry.forEach((element, index) => {
          let obj = {};
          obj.value = element;
          obj.select = index;
          this.ipArryForm.domains.push(obj);
        });
      }
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
      this.msgFormDialog.pdfPath = response.data.pdfPath;
      this.msgFormDialog.applyMaterial = response.data.filePath;
    },

    // 关联用户
    userChange(selectUserId) {
      let obj = {};
      obj = this.userBox.find(item => {
        //这里的userBox就是上面遍历的数据源
        return item.userName === selectUserId; //筛选出匹配数据
      });
      this.msgFormDialog.phonenumber = obj.phonenumber;
      this.msgFormDialog.deptName = obj.deptName;
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
      await getUserByType({
        userType: 11
      }).then(res => {
        this.userBox = res.data;
      });
    },
    // 获取服务器详情
    async initServerDetail() {
      if (this.handleObj.id) {
        // 修改
        if (this.handleObj.examine_status == "0") {
          this.userDiasbled = false;
        } else if (this.handleObj.examine_status == "2") {
          this.userDiasbled = false;
        } else if (this.handleObj.examine_status == "1") {
          this.userDiasbled = true;
        }
        // this.isDisabled = true;
        this.isHide = true;
        this.isHideAdd = false;
        await getById({ id: this.handleObj.id }).then(res => {
          if (res.code == 200) {
            let data = res.data;
            data.applyDatabaseIdList = data.applyDatabaseId.split(",");
            this.msgFormDialog = data;
            this.handleExportObj = {};
            this.handleExportObj.filePath = this.msgFormDialog.applyMaterial;
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
              this.trueAdd();
            } else {
              // 数据库账户审核 通过
              this.auditMethods("");
            }
            /*  if (this.handleObj.record_id != undefined) {
                this.msgFormDialog.record_id = this.handleObj.record_id;
              } */
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    trueAdd() {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          this.msgFormDialog.applyDatabaseId = this.msgFormDialog.applyDatabaseIdList.join(
            ","
          );
          // 新增
          if (this.handleObj.pageName) {
            addBzi(this.msgFormDialog).then(res => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "增加成功"
                });
                this.$emit("handleDialogClose", true);
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                });
              }
            });
          } else {
            addTable(this.msgFormDialog).then(res => {
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
          this.$emit("handleDialogClose", false);
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      if (!this.handleObj.id) {
        this.$refs[formName].resetFields();
        this.$emit("handleDialogClose", false);
      } else {
        // 审核不通过
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$prompt("请输入拒绝原因", "提示", {
              inputValidator: value => {
                if (value.trim().length < 1 || value.trim().length > 15) {
                  return "拒绝原因长度限制1-15个字符";
                }
              },
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
        this.msgFormDialog.examineStatus = 2;
      } else {
        this.msgFormDialog.examineStatus = 1;
      }
      this.msgFormDialog.failureReason = value;
      this.msgFormDialog.applyDatabaseId = this.msgFormDialog.applyDatabaseIdList.join(
        ","
      );
      // 审核
      update(this.msgFormDialog).then(res => {
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

  .preViewBox {
    position: absolute;
    right: 100px;
    top: 0px;
  }

  .preViewBox2 {
    position: absolute;
    right: 80px;
    top: 0px;
  }

  .el-checkbox + .el-checkbox {
    margin-left: 0;
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
