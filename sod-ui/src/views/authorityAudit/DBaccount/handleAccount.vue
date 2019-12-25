<template>
  <section class="handleAccountDialog">
    <el-form :rules="rules" ref="ruleForm" :model="msgFormDialog" label-width="100px">
      <el-row>
        <el-col :span="7">
          <el-form-item label="账户ID" prop="databaseup_id">
            <el-input
              size="small"
              v-model="msgFormDialog.databaseup_id"
              :disabled="userDiasbled"
              placeholder="账户ID"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-row>
            <el-col :span="22">
              <el-form-item label="密码" prop="databaseup_password">
                <el-input
                  size="small"
                  :type="passwordType"
                  v-model="msgFormDialog.databaseup_password"
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
          <el-form-item label="申请绑定IP" prop="databaseup_IP">
            <el-input
              size="small"
              v-model="msgFormDialog.databaseup_IP"
              :disabled="ipDisabled"
              :placeholder="palceholderIp"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item class="unitFormItem">
            <el-select
              size="small"
              v-model="msgFormDialog.databaseup_IP_SEGMENT"
              @change="ipChange"
              :disabled="ipDisabled"
            >
              <el-option label="指定IP" value="1"></el-option>
              <el-option label="IP段" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="数据库选择" prop="database_id">
            <el-transfer v-model="msgFormDialog.database_id" :data="dataBaseBox"></el-transfer>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="关联用户" prop="user_id">
            <el-select
              size="small"
              filterable
              v-model="msgFormDialog.user_id"
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
            <!-- <el-input v-model="msgFormDialog.user_id" :readonly="isreadonly" placeholder="关联用户"></el-input> -->
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
          <el-form-item label="用途说明" prop="databaseup_desc">
            <el-input
              size="small"
              v-model="msgFormDialog.databaseup_desc"
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
            <!-- <el-input type="file" v-model="msgFormDialog.serverdescribe" v-show="isHideAdd"></el-input> -->
            <el-input
              size="small"
              type="text"
              disabled
              v-model="msgFormDialog.apply_material"
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
    <div slot="footer" class="dialog-footer"  v-show="isHideAdd">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
    <!-- 审核 -->
    <div slot="footer" class="dialog-footer"  v-show="isHide">
      <el-button type="primary" @click="trueDialog('ruleForm')">通 过</el-button>
      <el-button @click="cancelDialog('ruleForm')">不 通 过</el-button>
    </div>
  
  </section>
</template>

<script>
export default {
  name: "handleAccountDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    const idValidate = (rule, value, callback) => {
      if (value === "" || value == undefined) {
        callback(new Error("请输入账户ID"));
      } else if (unstartnumValidation(value)) {
        callback(new Error("数据库账号不能以数字开头"));
      } else if (this.handleObj.databaseup_id == undefined) {
        this.axios
          .get(
            interfaceObj.databaseUser_ifUPExist +
              "?databaseup_id=" +
              this.msgFormDialog.databaseup_id
          )
          .then(res => {
            if (res.data.ifExist == "YES") {
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
      if (value === "") {
        callback(new Error("请输入数据库密码"));
      } else if (!englishAndNumValidation(value)) {
        callback(new Error("密码必须由英文字母和数字组成"));
      } else {
        callback();
      }
    };

    const ipValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入IP地址"));
      } else if (
        this.msgFormDialog.databaseup_IP_SEGMENT == 1 &&
        !ipUrlValidation(value)
      ) {
        callback(new Error("请输入正确IP地址"));
      } else if (
        this.msgFormDialog.databaseup_IP_SEGMENT == 2 &&
        !ipUrlValidation2(value)
      ) {
        callback(new Error("请输入正确IP段地址"));
      } else {
        callback();
      }
    };

    return {
      downUrl: "",
      upLoadUrl: '',
      isDisabled: false,
      userDiasbled: false,
      ipDisabled: false,
      isHideAdd: true,
      isHide: false,
      passwordType: "password",
      searchObj: {},
      userBox: [], //获取所有用户
      msgFormDialog: {
        databaseup_id: "USR_", //UP账户ID
        databaseup_password: "", //UP账户密码
        databaseup_IP: "", //UP账户IP
        databaseup_IP_SEGMENT: "1", //UP账户IP可选区间
        database_id: [], //物理库ID
        user_id: "", //用户ID
        databaseup_desc: "", //UP账户描述
        apply_material: ""
      },
      palceholderIp: "指定IP样例:192.168.1.1",
      dataBaseBox: [],
      rules: {
        databaseup_id: [
          { required: true, validator: idValidate, trigger: "blur" }
        ],
        databaseup_password: [
          { required: true, validator: passwordValidate, trigger: "blur" }
        ],
        databaseup_IP: [
          { required: true, validator: ipValidate, trigger: "blur" }
        ],
        database_id: [
          { required: true, message: "请选择数据库", trigger: "blur" }
        ],
        databaseup_desc: [
          { required: true, message: "请输入用途说明", trigger: "blur" }
        ],
        user_id: [
          { required: true, message: "请选择关联用户", trigger: "change" }
        ],
        apply_material: [
          { required: true, message: "请选择申请材料", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.searchObj = this.handleObj;
    // this.getDBlist();
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
    //
    ipChange() {
      if (this.msgFormDialog.databaseup_IP_SEGMENT == 1) {
        this.palceholderIp = "指定IP样例:192.168.1.1";
      } else {
        this.palceholderIp = "IP段样例:192.168.1.%";
      }
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
    getDBlist() {
      this.axios.get(interfaceObj.databaseUser_DatabaseList).then(res => {
        var resdata = res.data.data;
        var dataList = [];
        for (var i = 0; i < resdata.length; i++) {
          var obj = {};
          obj.key = resdata[i].database_id;
          obj.label = resdata[i].database_name;
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
              if (allData[i].userId == disdata[d].user_id) {
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
      if (this.searchObj.record_id == undefined) {
        // 新增
      } else {
        // 修改
        if (this.searchObj.examine_status == "0") {
          this.userDiasbled = false;
          this.ipDisabled = true;
        } else if (this.searchObj.examine_status == "2") {
          this.userDiasbled = false;
          this.ipDisabled = false;
        } else if (this.searchObj.examine_status == "1") {
          this.userDiasbled = true;
          this.ipDisabled = true;
        }
        // this.isDisabled = true;
        this.isHide = true;
        this.isHideAdd = false;

        await this.axios
          .get(
            interfaceObj.databaseUser_getById +
              "?record_id=" +
              this.searchObj.record_id
          )
          .then(res => {
            if (res.data.returnCode == 0) {
              this.msgFormDialog = res.data.data.databaseobj;
              // 回显数据库选择
              let transferDataList = res.data.data.databaseList;
              let transferarry = [];
              transferDataList.forEach(element => {
                transferarry.push(element.database_id);
              });
              this.msgFormDialog.database_id = transferarry;
              // 回显绑定IP;

              /* if (
                res.data.data.databaseobj.databaseup_IP != "" &&
                res.data.data.databaseobj.databaseup_IP != null
              ) {
                this.msgFormDialog.databaseup_IP_SEGMENT = "1";
              } else if (
                res.data.data.databaseobj.databaseup_IP_SEGMENT != "" &&
                res.data.data.databaseobj.databaseup_IP_SEGMENT != null
              ) {
                this.msgFormDialog.databaseup_IP_SEGMENT = "2";
              } */
              // 用户信息
              this.userChange(this.msgFormDialog.user_id);
              console.log(this.msgFormDialog.user_id);
              // this.msgFormDialog.databaseup_IP_SEGMENT =
              //   res.data.data.databaseobj.examine_status;
              this.downUrl =
                interfaceObj.download + res.data.data.apply_material;
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
      this.axios
        .get(
          interfaceObj.download +
            "?filepath=" +
            this.msgFormDialog.apply_material
        )
        .then(data => {
          testExport(
            interfaceObj.download +
              "?filepath=" +
              this.msgFormDialog.apply_material
          );
        })
        .catch(function(error) {
          testExport(
            interfaceObj.download +
              "?filepath=" +
              this.msgFormDialog.apply_material
          );
        });
    },

    trueDialog(formName) {
      if (this.isHide == false && !this.msgFormDialog.filepath) {
        this.$message({
          message: "请上传申请材料",
          type: "error",
          offset: 100
        });
        return;
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            if (this.searchObj.record_id != undefined) {
              this.msgFormDialog.record_id = this.searchObj.record_id;
            }
            this.$emit("handleDialogClose", this.msgFormDialog);
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    cancelDialog(formName) {
      if (this.searchObj.record_id == undefined) {
        this.$refs[formName].resetFields();
        this.$emit("handleDialogClose");
      } else {
        this.$prompt("请输入拒绝原因", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        })
          .then(({ value }) => {
            this.msgFormDialog.record_id = this.searchObj.record_id;
            this.msgFormDialog.failure_reason = value;
            this.$emit("handleDialogClose", this.msgFormDialog);
          })
          .catch(() => {});
      }
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
  }
  .el-transfer-panel__item {
    display: block;
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
</style>
