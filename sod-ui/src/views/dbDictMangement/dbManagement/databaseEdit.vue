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
            <el-form-item prop="database_id" label="数据库ID:">
              <el-input
                size="small"
                :disabled="isDbIdDisable"
                v-model="editRow.database_id"
                placeholder="请输入数据库ID"
              />
            </el-form-item>
            <el-form-item prop="database_name" label="数据库名称:">
              <el-input size="small" v-model="editRow.database_name" placeholder="请输入数据库名称" />
            </el-form-item>
            <el-form-item prop="serial_number" label="显示序号:">
              <el-input-number
                class="number"
                size="small"
                v-model="editRow.serial_number"
                placeholder="请输入显示序号"
              />
            </el-form-item>
            <el-form-item prop="database_instance" label="数据库实例:">
              <el-input size="small" v-model="editRow.database_instance" placeholder="请输入数据库实例" />
            </el-form-item>
            <el-form-item prop="database_type" label="数据库类型:">
              <el-input size="small" v-model="editRow.database_type" placeholder="请输入数据库类型" />
            </el-form-item>
            <el-form-item prop="driver_class_name" label="数据库驱动:">
              <el-input size="small" v-model="editRow.driver_class_name" placeholder="请输入数据库驱动" />
            </el-form-item>
            <el-form-item prop="database_ip" label="数据库IP:">
              <el-input size="small" v-model="editRow.database_ip" placeholder="请输入数据库IP" />
            </el-form-item>
            <el-form-item prop="database_port" label="数据库端口:">
              <el-input-number
                class="number"
                size="small"
                v-model="editRow.database_port"
                placeholder="请输入数据库端口"
              />
            </el-form-item>
            <el-form-item prop="database_url" label="数据库访问地址:">
              <el-row>
                <el-col :span="20">
                  <el-input size="small" v-model="editRow.database_url" placeholder="请输入数据库访问地址" />
                </el-col>
                <el-col :span="4" class="unitFormItem">
                  <el-button size="small" @click="checkParam()" type="primary" class="childBtn">测试连接</el-button>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item prop="up_url" label="UP层访问地址:">
              <el-input size="small" v-model="editRow.up_url" placeholder="请输入UP层访问地址" />
            </el-form-item>
            <el-form-item prop="display_control" label="显示控制:">
              <el-select v-model="editRow.display_control" size="small">
                <el-option
                  v-for="item in displayControl"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="主备类型:">
              <el-select v-model="editRow.mainbak_type" size="small">
                <el-option
                  v-for="item in mainbakType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="应用场景:">
              <el-input size="small" v-model="editRow.database_desc" placeholder="请输入应用场景" />
            </el-form-item>
          </el-form>
        </el-scrollbar>
      </el-tab-pane>
      <el-tab-pane label="存储信息" name="second">
        <el-form :model="editRow" ref="storeForm" :rules="storeFormRules">
          <el-form-item class="totalSize" prop="database_capacity" label="总容量:">
            <el-input size="small" v-model="editRow.database_capacity" placeholder="请输入总容量">
              <template slot="append">TB</template>
            </el-input>
            <span style="float:right;margin-top:12px;">
              <el-button
                type="primary"
                size="small"
                @click="addPhysicsStorage"
                icon="el-icon-search"
              >添加</el-button>
              <el-button
                type="warning"
                size="small"
                @click="deletePhysicsStorage"
                icon="el-icon-delete"
              >删除</el-button>
              <el-button
                type="primary"
                size="small"
                @click="getPhysicsStorage(editRow.database_id)"
                icon="el-icon-refresh"
              >恢复</el-button>
            </span>
          </el-form-item>
        </el-form>
        <el-table
          :data="physicsStorgeList"
          border
          stripe
          highlight-current-row
          @current-change="handleSelectionChange"
          style="width: 100%;"
        >
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="database_node" label="节点列表">
            <template slot-scope="scope">
              <el-input v-model="scope.row.database_node" placeholder="请输入节点IP"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="node_role" label="节点角色">
            <template slot-scope="scope">
              <el-select v-model="scope.row.node_role" placeholder="请选择">
                <el-option
                  v-for="item in nodeRoles"
                  :key="item.value"
                  :label="item.text"
                  :value="item.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="node_state" label="状态">
            <template slot-scope="scope">
              <el-select v-model="scope.row.node_state" placeholder="请选择">
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
            <el-button type="primary" size="small" @click="addPhysicsUser" icon="el-icon-search">添加</el-button>
            <el-button
              type="warning"
              size="small"
              @click="deletePhysicsUser"
              icon="el-icon-delete"
            >删除</el-button>
            <el-button
              type="primary"
              size="small"
              @click="getPhysicsUser(editRow.database_id)"
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
          <el-table-column prop="database_user" label="登录用户">
            <template slot-scope="scope">
              <el-input v-model="scope.row.database_user" placeholder="请输入用户名"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="2" label="登录密码">
            <template slot-scope="scope">
              <el-input show-password placeholder="请输入密码" v-model="scope.row.database_pass"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="3" label="开户目的">
            <template slot-scope="scope">
              <el-input v-model="scope.row.user_purpose"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="专题列表" name="four">
        <el-table :data="physicsSpecialList" border stripe style="width: 100%;">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="special_database_name" label="专题名"></el-table-column>
          <el-table-column prop="database_schema_name" label="专题标识"></el-table-column>
          <el-table-column prop="database_user" label="关联web用户"></el-table-column>
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
        database_id: "",
        database_name: "",
        serial_number: "",
        database_instance: "",
        database_type: "",
        driver_class_name: "",
        database_ip: "",
        database_port: 0,
        database_url: "",
        up_url: "",
        display_control: "1",
        mainbak_type: "1",
        database_desc: "",
        database_capacity: 0,
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
        database_id: [
          { required: true, message: "请输入数据库ID", trigger: "blur" }
        ],
        database_name: [
          { required: true, message: "请输入数据库名称", trigger: "blur" }
        ],
        serial_number: [
          { required: true, message: "请输入显示序号", trigger: "blur" },
          { type: "number", message: "显示序号必须为数字值" }
        ],
        database_instance: [
          { required: true, message: "数据库实例", trigger: "blur" }
        ],
        database_type: [
          { required: true, message: "请输入数据库类型", trigger: "blur" }
        ],
        driver_class_name: [
          { required: true, message: "请输入数据库驱动", trigger: "blur" }
        ],
        database_ip: [
          { required: true, message: "请输入数据库IP", trigger: "blur" }
        ],
        database_port: [
          { required: true, message: "请输入数据库端口", trigger: "blur" },
          { type: "number", message: "数据库端口必须为数字值" }
        ],
        database_url: [
          { required: true, message: "请输入数据库访问地址", trigger: "blur" }
        ],
        up_url: [
          { required: true, message: "请输入UP层访问地址", trigger: "blur" }
        ]
      },
      storeFormRules: {
        database_capacity: [
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
      this.getPhysicsStorage(this.editRow.database_id);
      //账户列表
      this.getPhysicsUser(this.editRow.database_id);
      //专题列表
      this.getPhysicsSpecial(this.editRow.database_id);
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
      //验证总容量是否为空
      if (
        this.editRow.database_capacity == null ||
        this.editRow.database_capacity == ""
      ) {
        this.$message.error("总容量不能为空");
        return;
      }
      //验证存储信息列表的必填项是否输入
      for (var i = 0; i < this.physicsStorgeList.length; i++) {
        if (
          this.physicsStorgeList[i].database_node === "" ||
          this.physicsStorgeList[i].database_node === null
        ) {
          this.$message.error("请正确填写存储信息列表");
          return;
        }
      }
      //验证账户信息列表的必填项是否输入
      for (var i = 0; i < this.physicsUserList.length; i++) {
        if (
          this.physicsUserList[i].database_user === "" ||
          this.physicsUserList[i].database_user === null ||
          this.physicsUserList[i].database_pass === "" ||
          this.physicsUserList[i].database_pass === null
        ) {
          this.$message.error("请正确填写账户信息列表");
          return;
        }
        //设置数据库id
        this.$set(
          this.physicsUserList[i],
          "database_id",
          this.editRow.database_id
        );
      }
      //验证账户名称是否重复
      let names = [];
      this.physicsUserList.some((item, i) => {
        names.push(item.database_user);
      });
      names.sort();
      for (var i = 1; i < names.length; i++) {
        if (names[i - 1] === names[i]) {
          this.$message.error("用户名重复！");
          return;
        }
      }
      //显示控制
      if (this.editRow.display_control == "1") {
        this.editRow.user_display_control = "1";
      } else if (this.editRow.display_control == "2") {
        this.editRow.user_display_control = "2";
      } else if (this.editRow.display_control == "3") {
        this.editRow.display_control == "1";
        this.editRow.display_control == "2";
      }
      //验证数据库是否已存在
      await this.updateOrInsert(this.editRow.database_id);
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
                  "database_id",
                  this.editRow.database_id
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
                  "database_id",
                  this.editRow.database_id
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
    async updateOrInsert(database_id) {
      await this.axios
        .post(interfaceObj.dataBaseManage_queryDataBasePhysicsByDbId, {
          database_id
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
        this.$set(
          testConn,
          "database_user",
          this.physicsUserList[0].database_user
        );
        this.$set(
          testConn,
          "database_pass",
          this.physicsUserList[0].database_pass
        );
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
    getPhysicsStorage(database_id) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsStorageById, {
          database_id
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
        database_node: "",
        node_role: "存储节点",
        node_state: "使用中"
      });
    },
    //存储信息删除一行
    deletePhysicsStorage() {
      if (this.currentRow) {
        let lineId = this.currentRow.database_node;
        this.physicsStorgeList.some((item, i) => {
          if (item.database_node == lineId) {
            this.physicsStorgeList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //账户列表查询页
    getPhysicsUser(database_id) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsUserById, { database_id })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.physicsUserList = res.data.data;
          }
        });
    },
    //账户列表页添加一行
    addPhysicsUser() {
      this.physicsUserList.push({
        database_user: "",
        database_pass: "",
        user_purpose: ""
      });
    },
    //账户列表页删除一行
    deletePhysicsUser() {
      if (this.currentRow) {
        let lineId = this.currentRow.database_user;
        this.physicsUserList.some((item, i) => {
          if (item.database_user == lineId) {
            this.physicsUserList.splice(i, 1);
            return true;
          }
        });
      } else {
        this.$message.error("请选择需要删除的记录！");
      }
    },
    //专题库信息查询页
    getPhysicsSpecial(database_id) {
      this.axios
        .post(interfaceObj.dataBaseManage_queryPhysicsSpecialById, {
          database_id
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