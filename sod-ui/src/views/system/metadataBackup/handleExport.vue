<template>
  <section class="handleExport">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input size="small" v-model="msgFormDialog.taskName"></el-input>
          </el-form-item>
          <el-form-item label="数据库IP">
            <el-select size="small" filterable v-model="msgFormDialog.databaseId">
              <el-option v-for="(item,index) in ipList" :key="index" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="类型">
            <el-checkbox-group v-model="msgFormDialog.checked">
              <el-checkbox label="结构" key="结构">结构</el-checkbox>
              <el-checkbox label="数据" key="数据">数据</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-row v-show="msgFormDialog.handleType=='add'">
            <el-col :span="20">
              <el-form-item label="执行策略" prop="taskName">
                <el-input size="small" v-model="msgFormDialog.taskName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item class="unitFormItem">
                <el-button size="small" type="primary">配置策略</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="执行主机和端口">
            <el-select size="small" filterable v-model="msgFormDialog.databaseId">
              <el-option
                v-for="(item,index) in ipandPortList"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="存储目录">
            <el-select size="small" filterable v-model="msgFormDialog.databaseId">
              <el-option
                v-for="(item,index) in ipandPortList"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库对象" prop="taskName">
            <el-tree :data="treedata" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="where条件" prop="taskName" v-show="msgFormDialog.handleType=='server'">
        <el-input size="small" v-model="msgFormDialog.taskName"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "powerDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormDialog: {
        // 这里定义弹框内的参数
        name: "是否默认通过所有申请资料的读权限",
        roleKey: "读权限",
        sectionId: "",
        enable: 1,
        description: "",
        checked: [],
        handelType: "server"
      },
      ipList: ["10.20.64.41", "10.40.17.34", "10.20.64.29", "10.40.128.25"],
      ipandPortList: ["10.40.17.35:8080", "10.40.17.36:8080", "127.0.0.1:8081"],
      defaultProps: {
        children: "children",
        label: "label"
      },
      treedata: [],
      rules: {
        time: [{ required: true, message: "请选择时间", trigger: "blur" }]
      }
    };
  },
  created() {
    this.msgFormDialog.handleType = this.handleObj.handleType;
    // this.getSectionList();
    // this.initDetail();
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },
    // 获取详情
    initDetail() {
      if (this.handleObj.id == undefined) {
        // 是新增
      } else {
        // 是详情
        this.msgFormDialog = this.handleObj;
      }
    },
    getSectionList() {
      this.axios.post(interfaceObj.roleManageApi_querySectionName).then(res => {
        this.optionsList = res.data.data;
      });
    },

    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id == undefined) {
            //判断用户名是否存在
            this.axios
              .post(interfaceObj.roleManageApi_queryRoleByRoleName, {
                roleName: this.msgFormDialog.name
              })
              .then(res => {
                if (res.data.data.length == 0) {
                  this.axios
                    .post(
                      interfaceObj.roleManageApi_addRole,
                      this.msgFormDialog
                    )
                    .then(res => {
                      if (res.data.returnCode === "0") {
                        this.$message({ message: "添加成功", type: "success" });
                        this.$emit("cancelHandle");
                      } else {
                        this.$message({
                          message: "添加失败！",
                          type: "error"
                        });
                      }
                    });
                } else {
                  this.$message({
                    message: "用户名不能重复！！",
                    type: "warning"
                  });
                  return;
                }
              });
          } else {
            // 编辑
            this.axios
              .post(interfaceObj.roleManageApi_updateRole, this.msgFormDialog)
              .then(res => {
                if (res.data.returnCode === "0") {
                  this.$message({ message: "编辑成功", type: "success" });
                  this.$emit("cancelHandle");
                } else {
                  this.$message({
                    message: "编辑失败！",
                    type: "error"
                  });
                }
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    }
  }
};
</script>
<style lang="scss">
.handleExport {
  .el-select {
    width: 100%;
  }
}
</style>
