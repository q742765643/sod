<template>
  <section class="filedSearchDeploy">
    <el-tabs class="pageTas" v-model.trim="activeName" @click="pageTasChange">
      <el-tab-pane label="基本信息" name="first">
        <el-form
          :model="msgFormDialog"
          ref="msgFormDialog"
          :rules="baseFormRules"
          class="baseForm"
          label-width="140px"
        >
          <el-form-item prop="schemaName" label="模式名称:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.databaseDto.schemaName"
              placeholder="请输入数据库实例"
            />
          </el-form-item>

          <el-form-item label="数据库分类:">
            <el-input
              size="small"
              disabled="disabled"
              v-model.trim="msgFormDialog.databaseDto.databaseClassify"
            />
          </el-form-item>
          <!--  <el-form-item label="数据库名称:">
            <el-input
              size="small"
              disabled="disabled"
              v-model.trim="msgFormDialog.databaseDto.databaseName"
              placeholder="请输入数据库实例"
            />
          </el-form-item>-->
          <el-form-item prop="id" label="数据库ID:">
            <el-input
              size="small"
              :disabled="isDbIdDisable"
              v-model.trim="msgFormDialog.id"
              placeholder="请输入数据库ID"
            />
          </el-form-item>
          <el-form-item prop="databaseName" label="数据库名称:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.databaseName"
              placeholder="请输入数据库名称"
            />
          </el-form-item>
          <el-form-item prop="serialNumber" label="显示序号:">
            <el-input-number
              :min="0"
              class="number"
              size="small"
              v-model.trim="msgFormDialog.serialNumber"
              placeholder="请输入显示序号"
            />
          </el-form-item>
          <el-form-item prop="databaseInstance" label="数据库实例:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.databaseInstance"
              placeholder="请输入数据库实例"
            />
          </el-form-item>
          <el-form-item prop="databaseType" label="数据库类型:">
            <el-select v-model.trim="msgFormDialog.databaseType">
              <el-option
                v-for="item in dictsList"
                :key="item.dictValue"
                :label="item.dictLabel"
                :value="item.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="driverClassName" label="数据库驱动:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.driverClassName"
              placeholder="请输入数据库驱动"
            />
          </el-form-item>
          <el-form-item prop="databaseIp" label="数据库IP:">
            <el-input size="small" v-model.trim="msgFormDialog.databaseIp" placeholder="请输入数据库IP" />
          </el-form-item>
          <el-form-item prop="databasePort" label="数据库端口:">
            <el-input-number
              :min="0"
              class="number"
              size="small"
              v-model.trim="msgFormDialog.databasePort"
              placeholder="请输入数据库端口"
            />
          </el-form-item>
          <el-form-item prop="databaseUrl" label="数据库访问地址:">
            <el-row>
              <el-col :span="20">
                <el-input
                  size="small"
                  v-model.trim="msgFormDialog.databaseUrl"
                  placeholder="请输入数据库访问地址"
                />
              </el-col>
              <el-col :span="4" class="unitFormItem">
                <el-button size="small" @click="checkParam()" type="primary" class="childBtn">测试连接</el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="upUrl" label="UP层访问地址:">
            <el-input size="small" v-model.trim="msgFormDialog.upUrl" placeholder="请输入UP层访问地址" />
          </el-form-item>
          <el-form-item prop="userDisplayControl" label="显示控制:">
            <el-select v-model.trim="msgFormDialog.userDisplayControl" size="small">
              <el-option label="显示" :value="1"></el-option>
              <el-option label="不显示" :value="2"></el-option>
              <el-option label="前端不显示" :value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="主备类型:">
            <el-select v-model.trim="msgFormDialog.mainBakType" size="small">
              <el-option label="主库" :value="1"></el-option>
              <el-option label="备份库" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="应用场景:">
            <el-input size="small" v-model.trim="msgFormDialog.databaseDesc" placeholder="请输入应用场景" />
          </el-form-item>
          <el-form-item label="总容量:" prop="databaseCapacity">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.databaseCapacity"
              placeholder="请输入总容量"
            >
              <template slot="append">TB</template>
            </el-input>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="存储信息" name="second">
        <el-row :gutter="10" class="handleTableBox">
          <el-col :span="1.5">
            <el-button type="primary" size="small" @click="addPhysicsStorage" icon="el-icon-plus">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              size="small"
              @click="deletePhysicsStorage"
              icon="el-icon-delete"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              size="small"
              @click="getPhysicsStorage"
              icon="el-icon-refresh"
            >恢复</el-button>
          </el-col>
        </el-row>
        <el-table
          :data="msgFormDialog.databaseNodesList"
          border
          stripe
          highlight-current-row
          @current-change="handleSelectionChange"
          style="width: 100%;"
        >
          <el-table-column type="index" label="序号" width="50"></el-table-column>
          <el-table-column prop="databaseNode" label="节点列表">
            <template slot-scope="scope">
              <el-input v-model.trim="scope.row.databaseNode" placeholder="请输入节点IP"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="nodeRole" label="节点角色">
            <template slot-scope="scope">
              <el-select v-model.trim="scope.row.nodeRole" placeholder="请选择">
                <el-option label="存储节点" value="存储节点"></el-option>
                <el-option label="管理节点" value="管理节点"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="nodeState" label="状态">
            <template slot-scope="scope">
              <el-select v-model.trim="scope.row.nodeState" placeholder="请选择">
                <el-option label="使用中" value="使用中"></el-option>
                <el-option label="停止使用" value="停止使用"></el-option>
                <el-option label="已废弃" value="已废弃"></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="账户列表" name="three">
        <el-form>
          <el-form-item class="manageBtn" style="float:right;">
            <el-button type="primary" size="small" @click="addPhysicsUser" icon="el-icon-plus">添加</el-button>
            <el-button
              type="warning"
              size="small"
              @click="deletePhysicsUser"
              icon="el-icon-delete"
            >删除</el-button>
            <el-button type="primary" size="small" @click="getPhysicsUser" icon="el-icon-refresh">恢复</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="msgFormDialog.databaseAdministratorList"
          border
          stripe
          highlight-current-row
          @current-change="handleSelectionChange"
          style="width: 100%;"
        >
          <el-table-column type="index" label="序号" width="50"></el-table-column>
          <el-table-column prop="userName" label="登录用户">
            <template slot-scope="scope">
              <el-input v-model.trim="scope.row.userName" placeholder="请输入用户名"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="2" label="登录密码">
            <template slot-scope="scope">
              <el-input show-password placeholder="请输入密码" v-model.trim="scope.row.passWord"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="3" label="开户目的">
            <template slot-scope="scope">
              <el-input v-model.trim="scope.row.introduction"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="3" label="是否管理账户">
            <template slot-scope="scope">
              <el-select v-model.trim="scope.row.isManager">
                <el-option label="是" :value="true"></el-option>
                <el-option label="否" :value="false"></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="专题列表" name="four" v-if="handleMsgObj.id">
        <el-table border :data="physicsSpecialList" stripe style="width: 100%;">
          <el-table-column type="index" label="序号" width="50"></el-table-column>
          <el-table-column prop="databaseName" label="专题名"></el-table-column>
          <el-table-column prop="schemaName" label="专题标识"></el-table-column>
          <el-table-column prop="userName" label="关联web用户"></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer1" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="trueDialog('msgFormDialog')">确 定</el-button>
    </div>
  </section>
</template>

<script>
import {
  databaseDefineSave,
  databaseDefineGet,
  findByDatabaseDefineId,
  conStatus,
  findBaseByPId,
  connStatus
} from "@/api/dbDictMangement/dbManagement";
import { getDicts } from "@/api/system/dict/data";
import {
  EngNumLine,
  ipUrlValidation,
  ipUrlValidation2
} from "@/components/commonVaildate.js";

export default {
  name: "filedSearchDeploy",
  components: {},
  props: {
    handleMsgObj: {
      type: Object
    }
  },
  data() {
    var nameValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入数据库ID"));
      } else if (!EngNumLine(value)) {
        callback(new Error("数据库ID只能由英文字母组成"));
      } else {
        callback();
      }
    };
    var ipValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入数据库IP"));
      } else if (!ipUrlValidation(value) && !ipUrlValidation2(value)) {
        callback(
          new Error(
            "请输入正确IP地址,范例(指定IP:192.168.1.1;IP段：192.168.1.%)"
          )
        );
      } else {
        callback();
      }
    };

    var databaseCapacityValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入总容量"));
      } else if ((value + "").indexOf(".") > -1) {
        callback(new Error("不能输入小数"));
      } else {
        callback();
      }
    };

    return {
      dictsList: [],
      //专题列表
      physicsSpecialList: [],
      //当前选中列
      currentRow: null,
      //编辑页面列
      msgFormDialog: {
        databaseDto: {
          schemaName: "",
          databaseClassify: "物理库",
          databaseName: "基础库"
        },

        id: "",
        databaseName: "",
        serialNumber: "",
        databaseInstance: "",
        databaseType: "",
        driverClassName: "",
        databaseIp: "",
        databasePort: 0,
        databaseUrl: "",
        upUrl: "",
        userDisplayControl: 1,
        mainBakType: 1,
        databaseDesc: "",
        databaseCapacity: 0,
        databaseNodesList: [], //存储信息
        databaseAdministratorList: [] //账户信息
      },
      //默认site页
      activeName: "first",
      //判断数据库是否已存在
      flag: false,
      //控制数据库Id是否可编辑
      isDbIdDisable: false,

      baseFormRules: {
        id: { required: true, validator: nameValidate, trigger: "blur" },
        databaseName: [
          { required: true, message: "请输入数据库名称", trigger: "blur" }
        ],
        serialNumber: [
          { required: true, message: "请输入显示序号", trigger: "blur" },
          { type: "number", message: "显示序号必须为数字值" }
        ],
        databaseInstance: [
          { required: true, message: "数据库实例", trigger: "blur" }
        ],
        databaseType: [
          { required: true, message: "请输入数据库类型", trigger: "blur" }
        ],
        driverClassName: [
          { required: true, message: "请输入数据库驱动", trigger: "blur" }
        ],
        databaseIp: [
          { required: true, validator: ipValidate, trigger: "blur" }
        ],
        databasePort: [
          { required: true, message: "请输入数据库端口", trigger: "blur" },
          { type: "number", message: "数据库端口必须为数字值" }
        ],
        databaseUrl: [
          { required: true, message: "请输入数据库访问地址", trigger: "blur" }
        ],
        upUrl: [
          { required: true, message: "请输入UP层访问地址", trigger: "blur" }
        ],
        databaseCapacity: [
          {
            required: true,
            validator: databaseCapacityValidate,
            trigger: "blur"
          }
        ]
      }
    };
  },
  async created() {
    await this.getDictsList();
    //点击编辑时处理
    if (this.handleMsgObj.id) {
      this.isDbIdDisable = true;
      this.formDetail(this.handleMsgObj.id, "detail");
      /*  //存储信息
      this.getPhysicsStorage(this.msgFormDialog.id);
      //账户列表
      this.getPhysicsUser(this.msgFormDialog.id); */
      //专题列表
      this.getPhysicsSpecial(this.handleMsgObj.id);
    } else {
      //点击添加时处理
      this.isDbIdDisable = false;
    }
  },
  methods: {
    async getDictsList() {
      getDicts("sys_database_type").then(response => {
        if (response.code == 200) {
          this.dictsList = response.data;
        }
      });
    },
    //添加或修改数据库基本信息的保存按钮
    async trueDialog(formName) {
      //验证基本信息页的必填项是否输入
      this.$refs[formName].validate(valid => {
        if (!valid) {
          this.$message.error("请正确填写基本信息列表");
          return;
        } else {
          let flagInfo = false;
          if (this.msgFormDialog.databaseNodesList.length == 0) {
            this.$message.error("存储信息列表不能为空");
            return;
          }
          if (this.msgFormDialog.databaseAdministratorList.length == 0) {
            this.$message.error("账户信息列表不能为空");
            return;
          }
          //验证存储信息列表的必填项是否输入
          this.msgFormDialog.databaseNodesList.forEach(element => {
            if (!element.databaseNode) {
              this.$message.error("请正确填写存储信息列表");
              flagInfo = true;
              return;
            }
            this.$set(element, "databaseId", this.msgFormDialog.id);
          });
          //验证账户信息列表的必填项是否输入
          this.msgFormDialog.databaseAdministratorList.forEach(element => {
            if (!element.userName || !element.passWord) {
              this.$message.error("请正确填写账户信息列表");
              flagInfo = true;
              return;
            }
          });

          //验证账户名称是否重复
          let names = [];
          this.msgFormDialog.databaseAdministratorList.some((item, i) => {
            names.push(item.userName);
          });
          names.sort();
          for (var i = 1; i < names.length; i++) {
            if (names[i - 1] === names[i]) {
              flagInfo = true;
              this.$message.error("用户名重复！");
              return;
            }
          }
          console.log(this.msgFormDialog);
          if (flagInfo) {
            return;
          }
          if (!this.handleMsgObj) {
            // 如果新增
            //验证数据库是否已存在
            this.formDetail(this.msgFormDialog.id, "repeat");
          }
          if (this.flag) {
            this.$message({ message: "数据库ID重复", type: "error" });
            return;
          }
          databaseDefineSave(this.msgFormDialog).then(response => {
            if (response.code == 200) {
              this.$message({ message: "操作成功", type: "success" });
              this.cancelDialog();
            } else {
              this.$message({ message: response.msg, type: "error" });
              this.cancelDialog();
            }
          });
        }
      });
    },
    formDetail(id, type) {
      databaseDefineGet({ id: id }).then(response => {
        if (type == "repeat") {
          if (response.data) {
            this.flag = true;
          } else {
            this.flag = false;
          }
        } else {
          if (!response.data.databaseDto) {
            response.data.databaseDto = {
              schemaName: "",
              databaseClassify: "物理库",
              databaseName: "基础库"
            };
          }
          this.msgFormDialog = response.data;
        }
      });
    },
    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    },
    //选中行
    handleSelectionChange(val) {
      this.currentRow = val;
    },
    //测试数据库连接
    checkParam() {
      if (this.isDbIdDisable) {
        conStatus({ id: this.msgFormDialog.id }).then(res => {
          if (res.data.checkConn == 1) {
            this.$message({ message: "连接正常", type: "success" });
          } else {
            this.$message({ message: "连接异常", type: "error" });
          }
        });
      } else {
        if (!this.msgFormDialog.id) {
          this.$message({ message: "数据库ID不能为空", type: "error" });
          return;
        }
        // 如果新增
        //验证数据库是否已存在
        this.formDetail(this.msgFormDialog.id, "repeat");
        if (!this.msgFormDialog.databaseType) {
          this.$message({ message: "数据库类型不能为空", type: "error" });
          return;
        }
        if (!this.msgFormDialog.databaseUrl) {
          this.$message({ message: "数据库访问地址不能为空", type: "error" });
          return;
        }
        if (this.msgFormDialog.databaseAdministratorList.length == 0) {
          this.$message.error("账户信息列表不能为空");
          return;
        }
        let flagInfo = false;
        //验证账户信息列表的必填项是否输入
        this.msgFormDialog.databaseAdministratorList.forEach(element => {
          if (!element.userName || !element.passWord) {
            this.$message.error("请正确填写账户信息列表");
            flagInfo = true;
            return;
          }
        });
        //验证账户名称是否重复
        let names = [];
        this.msgFormDialog.databaseAdministratorList.some((item, i) => {
          names.push(item.userName);
        });
        names.sort();
        for (var i = 1; i < names.length; i++) {
          if (names[i - 1] === names[i]) {
            flagInfo = true;
            this.$message.error("用户名重复！");
            return;
          }
        }
        if (flagInfo) {
          return;
        }

        if (this.flag) {
          this.$message({ message: "数据库ID重复", type: "error" });
          return;
        }
        connStatus(this.msgFormDialog).then(response => {
          this.$message({ message: "连接正常", type: "success" });
        });
      }
    },
    //存储信息
    getPhysicsStorage() {
      this.msgFormDialog.databaseNodesList.forEach((element, index) => {
        if (!element.id) {
          this.msgFormDialog.databaseNodesList.splice(index, 1);
        }
      });
    },
    //存储信息添加一行
    addPhysicsStorage() {
      this.msgFormDialog.databaseNodesList.push({
        databaseNode: "",
        nodeRole: "存储节点",
        nodeState: "使用中"
      });
    },
    //存储信息删除一行
    deletePhysicsStorage() {
      if (this.currentRow) {
        let lineId = this.currentRow.databaseNode;
        this.msgFormDialog.databaseNodesList.some((item, i) => {
          if (item.databaseNode == lineId) {
            this.msgFormDialog.databaseNodesList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //账户列表查询页
    getPhysicsUser() {
      this.msgFormDialog.databaseAdministratorList.forEach((element, index) => {
        if (!element.id) {
          this.msgFormDialog.databaseAdministratorList.splice(index, 1);
        }
      });
    },
    //账户列表页添加一行
    addPhysicsUser() {
      this.msgFormDialog.databaseAdministratorList.push({
        userName: "",
        passWord: "",
        introduction: "",
        isManager: true
      });
    },
    //账户列表页删除一行
    deletePhysicsUser() {
      if (this.currentRow) {
        let lineId = this.currentRow.userName;
        this.msgFormDialog.databaseAdministratorList.some((item, i) => {
          if (item.userName == lineId) {
            this.msgFormDialog.databaseAdministratorList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //专题库信息查询页
    getPhysicsSpecial(id) {
      findByDatabaseDefineId({ id: id }).then(res => {
        if (res.code == 200) {
          this.physicsSpecialList = res.data;
        }
      });
    },
    //页签切换时间(将选中对象清掉)
    pageTasChange() {
      this.currentRow = null;
    }
  }
};
</script>

<style lang="scss">
.filedSearchDeploy {
  .loadTable {
    margin: 10px;
  }
  .baseForm {
    padding-right: 12px;
  }
  .number .el-input,
  .el-select {
    width: 100%;
  }
  .selSearchCon .handleSearchBtn {
    float: none;
  }
  .childBtn {
    margin-left: 2px;
  }

  .el-table {
    margin-bottom: 20px;
  }
}
</style>
