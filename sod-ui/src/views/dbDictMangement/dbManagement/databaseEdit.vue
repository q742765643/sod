<template>
  <section class="filedSearchDeploy">
    <el-tabs class="pageTas" v-model="activeName" @click="pageTasChange">
      <el-tab-pane label="基本信息" name="first">
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-form
            :model="editRow"
            ref="editRow"
            :rules="baseFormRules"
            class="baseForm"
            label-width="140px"
          >
            <el-form-item prop="databaseId" label="数据库ID:">
              <el-input
                size="small"
                :disabled="isDbIdDisable"
                v-model="editRow.databaseId"
                placeholder="请输入数据库ID"
              />
            </el-form-item>
            <el-form-item prop="databaseName" label="数据库名称:">
              <el-input size="small" v-model="editRow.databaseName" placeholder="请输入数据库名称" />
            </el-form-item>
            <el-form-item prop="serialNumber" label="显示序号:">
              <el-input-number
                class="number"
                size="small"
                v-model="editRow.serialNumber"
                placeholder="请输入显示序号"
              />
            </el-form-item>
            <el-form-item prop="databaseInstance" label="数据库实例:">
              <el-input size="small" v-model="editRow.databaseInstance" placeholder="请输入数据库实例" />
            </el-form-item>
            <el-form-item prop="databaseType" label="数据库类型:">
              <el-input size="small" v-model="editRow.databaseType" placeholder="请输入数据库类型" />
            </el-form-item>
            <el-form-item prop="driverClassName" label="数据库驱动:">
              <el-input size="small" v-model="editRow.driverClassName" placeholder="请输入数据库驱动" />
            </el-form-item>
            <el-form-item prop="databaseIp" label="数据库IP:">
              <el-input size="small" v-model="editRow.databaseIp" placeholder="请输入数据库IP" />
            </el-form-item>
            <el-form-item prop="databasePort" label="数据库端口:">
              <el-input-number
                class="number"
                size="small"
                v-model="editRow.databasePort"
                placeholder="请输入数据库端口"
              />
            </el-form-item>
            <el-form-item prop="databaseUrl" label="数据库访问地址:">
              <el-row>
                <el-col :span="20">
                  <el-input size="small" v-model="editRow.databaseUrl" placeholder="请输入数据库访问地址" />
                </el-col>
                <el-col :span="4" class="unitFormItem">
                  <el-button size="small" @click="checkParam()" type="primary" class="childBtn">测试连接</el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item prop="upUrl" label="UP层访问地址:">
              <el-input size="small" v-model="editRow.upUrl" placeholder="请输入UP层访问地址" />
            </el-form-item>
            <el-form-item prop="userDisplayControl" label="显示控制:">
              <el-select v-model="editRow.userDisplayControl" size="small">
                <el-option
                  v-for="item in displayControl"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="主备类型:">
              <el-select v-model="editRow.mainBakType" size="small">
                <el-option
                  v-for="item in mainbakType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="应用场景:">
              <el-input size="small" v-model="editRow.databaseDesc" placeholder="请输入应用场景" />
            </el-form-item>
            <el-form-item label="总容量:" prop="databaseCapacity">
              <el-input size="small" v-model="editRow.databaseCapacity" placeholder="请输入总容量">
                <template slot="append">TB</template>
              </el-input>
            </el-form-item>
          </el-form>
        </el-scrollbar>
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
              @click="getPhysicsStorage(editRow.databaseId)"
              icon="el-icon-refresh"
            >恢复</el-button>
          </el-col>
        </el-row>
        <el-table
          :data="physicsStorgeList"
          border
          stripe
          highlight-current-row
          @current-change="handleSelectionChange"
          style="width: 100%;"
        >
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="databaseNode" label="节点列表">
            <template slot-scope="scope">
              <el-input v-model="scope.row.databaseNode" placeholder="请输入节点IP"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="nodeRole" label="节点角色">
            <template slot-scope="scope">
              <el-select v-model="scope.row.nodeRole" placeholder="请选择">
                <el-option
                  v-for="item in nodeRoles"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="nodeState" label="状态">
            <template slot-scope="scope">
              <el-select v-model="scope.row.nodeState" placeholder="请选择">
                <el-option
                  v-for="item in nodeStates"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                ></el-option>
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
            <el-button
              type="primary"
              size="small"
              @click="getPhysicsUser(editRow.databaseId)"
              icon="el-icon-refresh"
            >恢复</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="physicsUserList"
          border
          stripe
          highlight-current-row
          @current-change="handleSelectionChange"
          style="width: 100%;"
        >
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="userName" label="登录用户">
            <template slot-scope="scope">
              <el-input v-model="scope.row.userName" placeholder="请输入用户名"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="2" label="登录密码">
            <template slot-scope="scope">
              <el-input show-password placeholder="请输入密码" v-model="scope.row.passWord"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="3" label="开户目的">
            <template slot-scope="scope">
              <el-input v-model="scope.row.introduction"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="专题列表" name="four">
        <el-table :data="physicsSpecialList" border stripe style="width: 100%;">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="special_database_name" label="专题名"></el-table-column>
          <el-table-column prop="database_schema_name" label="专题标识"></el-table-column>
          <el-table-column prop="userName" label="关联web用户"></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer1" class="dialog-footer">
      <el-button @click="cancelDialog()">取 消</el-button>
      <el-button type="primary" @click="addDataBasePhysicsStructure('editRow')">确 定</el-button>
    </div>
  </section>
</template>

<script>
import {
  databaseNodesAll,
  databaseNodesDelete,
  databaseNodesGetById,
  databaseNodesSave
} from "@/api/dbDictMangement/dbManagement";
export default {
  name: "filedSearchDeploy",
  components: {},
  props: {
    handleMsgObj: {
      type: Object
    }
  },
  data() {
    return {
      //存储信息
      physicsStorgeList: [],
      //账户列表
      physicsUserList: [],
      //专题列表
      physicsSpecialList: [],
      //当前选中列
      currentRow: null,
      //编辑页面列
      editRow: {
        databaseId: "",
        databaseName: "",
        serialNumber: "",
        databaseInstance: "",
        databaseType: "",
        driverClassName: "",
        databaseIp: "",
        databasePort: 0,
        databaseUrl: "",
        upUrl: "",
        userDisplayControl: "1",
        mainBakType: "1",
        databaseDesc: "",
        databaseCapacity: 0,
        user_display_control: ""
      },
      //默认site页
      activeName: "first",
      //判断数据库是否已存在
      flag: false,
      //控制数据库Id是否可编辑
      isDbIdDisable: false,
      //显示控制
      displayControl: [
        {
          value: "1",
          label: "显示"
        },
        {
          value: "2",
          label: "不显示"
        },
        {
          value: "3",
          label: "前端不显示"
        }
      ],
      //主备类型
      mainbakType: [
        {
          value: "1",
          label: "主库"
        },
        {
          value: "2",
          label: "备份库"
        }
      ],
      //节点角色
      nodeRoles: [
        {
          value: "存储节点",
          text: "存储节点"
        },
        {
          value: "管理节点",
          text: "管理节点"
        }
      ],
      //存储信息状态
      nodeStates: [
        {
          value: "使用中",
          text: "使用中"
        },
        {
          value: "停止使用",
          text: "停止使用"
        },
        {
          value: "已废弃",
          text: "已废弃"
        }
      ],
      baseFormRules: {
        databaseId: [
          { required: true, message: "请输入数据库ID", trigger: "blur" }
        ],
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
          { required: true, message: "请输入数据库IP", trigger: "blur" }
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
          { required: true, message: "请输入总容量", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    //点击编辑时处理
    if (this.handleMsgObj != null) {
      this.editRow = this.handleMsgObj;
      this.isDbIdDisable = true;
      //存储信息
      this.getPhysicsStorage(this.editRow.databaseId);
      //账户列表
      this.getPhysicsUser(this.editRow.databaseId);
      //专题列表
      this.getPhysicsSpecial(this.editRow.databaseId);
    } else {
      //点击添加时处理
      this.isDbIdDisable = false;
    }
  },
  methods: {
    //添加或修改数据库基本信息的保存按钮
    async addDataBasePhysicsStructure(formName) {
      //验证基本信息页的必填项是否输入
      this.$refs[formName].validate(valid => {
        if (!valid) {
          this.$message.error("请正确填写基本信息列表");
          return;
        }
      });
      //验证存储信息列表的必填项是否输入
      for (var i = 0; i < this.physicsStorgeList.length; i++) {
        if (!this.physicsStorgeList[i].databaseNode) {
          this.$message.error("请正确填写存储信息列表");
          return;
        }
      }
      //验证账户信息列表的必填项是否输入
      for (var i = 0; i < this.physicsUserList.length; i++) {
        if (
          !this.physicsUserList[i].userName ||
          !this.physicsUserList[i].passWord
        ) {
          this.$message.error("请正确填写账户信息列表");
          return;
        }
        //设置数据库id
        this.$set(
          this.physicsUserList[i],
          "databaseId",
          this.editRow.databaseId
        );
      }
      //验证账户名称是否重复
      let names = [];
      this.physicsUserList.some((item, i) => {
        names.push(item.userName);
      });
      names.sort();
      for (var i = 1; i < names.length; i++) {
        if (names[i - 1] === names[i]) {
          this.$message.error("用户名重复！");
          return;
        }
      }
      //显示控制
      if (this.editRow.userDisplayControl == "1") {
        this.editRow.user_display_control = "1";
      } else if (this.editRow.userDisplayControl == "2") {
        this.editRow.user_display_control = "2";
      } else if (this.editRow.userDisplayControl == "3") {
        this.editRow.userDisplayControl == "1";
        this.editRow.userDisplayControl == "2";
      }
      //验证数据库是否已存在
      await this.updateOrInsert(this.editRow.databaseId);
      //保存操作
      if (this.flag) {
        //更新基本信息
        this.axios
          .post(interfaceObj.dataBaseManage_updateDataBasePhysics, this.editRow)
          .then(res => {
            if (res.data.returnCode === "0") {
              //更新存储信息列表
              for (var i = 0; i < this.physicsStorgeList.length; i++) {
                this.$set(
                  this.physicsStorgeList[i],
                  "databaseId",
                  this.editRow.databaseId
                );
              }
              this.axios
                .post(
                  interfaceObj.dataBaseManage_addPhysicsStorageList,
                  this.physicsStorgeList
                )
                .then(res => {
                  if (res.data.returnCode === "0") {
                    //更新账户信息列表
                    this.axios
                      .post(
                        interfaceObj.dataBaseManage_addPhysicsUserList,
                        this.physicsUserList
                      )
                      .then(res => {
                        this.$message({ message: "更新成功", type: "success" });
                        this.cancelDialog();
                      });
                  }
                });
            }
          });
      } else {
        //新插入数据
        //插入基本信息
        this.axios
          .post(interfaceObj.dataBaseManage_addDataBasePhysics, this.editRow)
          .then(res => {
            if (res.data.returnCode === "0") {
              //更新存储信息列表
              for (var i = 0; i < this.physicsStorgeList.length; i++) {
                this.$set(
                  this.physicsStorgeList[i],
                  "databaseId",
                  this.editRow.databaseId
                );
              }
              this.axios
                .post(
                  interfaceObj.dataBaseManage_addPhysicsStorageList,
                  this.physicsStorgeList
                )
                .then(res => {
                  if (res.data.returnCode === "0") {
                    //更新账户信息列表
                    this.axios
                      .post(
                        interfaceObj.dataBaseManage_addPhysicsUserList,
                        this.physicsUserList
                      )
                      .then(res => {
                        this.$message({ message: "更新成功", type: "success" });
                        this.cancelDialog();
                      });
                  }
                });
            }
          });
      }
    },
    //判断数据库信息是更新还是插入
    async updateOrInsert(databaseId) {
      await this.axios
        .post(interfaceObj.dataBaseManage_queryDataBasePhysicsByDbId, {
          databaseId
        })
        .then(res => {
          //如果有值返回true,否则返回false
          if (res.data.data) {
            this.flag = true;
          } else {
            this.flag = false;
          }
        });
    },
    //取消按钮
    cancelDialog() {
      this.$emit("cancelMsgHandle");
    },
    //选中行
    handleSelectionChange(val) {
      this.currentRow = val;
    },
    //测试数据库连接
    checkParam() {
      debugger;
      if (this.physicsUserList.length > 0) {
        let testConn = this.editRow;
        this.$set(testConn, "userName", this.physicsUserList[0].userName);
        this.$set(testConn, "passWord", this.physicsUserList[0].passWord);
        this.axios
          .post(interfaceObj.dataBaseManage_checkParam, testConn)
          .then(res => {
            if (res.data.returnCode == 0) {
              this.$message({
                message: res.data.returnMessage,
                type: "success"
              });
            } else {
              this.$message.error(res.data.returnMessage);
            }
          });
      } else {
        this.$message.error("未创建数据库用户");
      }
    },
    //存储信息
    getPhysicsStorage(databaseId) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsStorageById, {
          databaseId
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.physicsStorgeList = res.data.data;
          }
        });
    },
    //存储信息添加一行
    addPhysicsStorage() {
      this.physicsStorgeList.push({
        databaseNode: "",
        nodeRole: "存储节点",
        nodeState: "使用中"
      });
    },
    //存储信息删除一行
    deletePhysicsStorage() {
      if (this.currentRow) {
        let lineId = this.currentRow.databaseNode;
        this.physicsStorgeList.some((item, i) => {
          if (item.databaseNode == lineId) {
            this.physicsStorgeList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //账户列表查询页
    getPhysicsUser(databaseId) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsUserById, { databaseId })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.physicsUserList = res.data.data;
          }
        });
    },
    //账户列表页添加一行
    addPhysicsUser() {
      this.physicsUserList.push({
        userName: "",
        passWord: "",
        introduction: ""
      });
    },
    //账户列表页删除一行
    deletePhysicsUser() {
      if (this.currentRow) {
        let lineId = this.currentRow.userName;
        this.physicsUserList.some((item, i) => {
          if (item.userName == lineId) {
            this.physicsUserList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //专题库信息查询页
    getPhysicsSpecial(databaseId) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsSpecialById, {
          databaseId
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.physicsSpecialList = res.data.data;
          }
        });
    },
    //页签切换时间(将选中对象清掉)
    pageTasChange() {
      this.currentRow = null;
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          this.currentRow = null;
          this.dialogVisible = false;
          done();
        })
        .catch(_ => {});
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
  .el-tab-pane {
    height: 400px;
    overflow: hidden;
    .el-scrollbar {
      height: 100%;
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }
  }
}
</style>