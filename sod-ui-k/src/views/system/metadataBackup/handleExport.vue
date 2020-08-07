<template>
  <section class="handleExport">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="140px">
      <el-row>
        <el-col :span="10">
          <el-form-item label="任务名称" prop="taskName">
            <el-input size="small" v-model.trim="msgFormDialog.taskName"></el-input>
          </el-form-item>
          <el-form-item label="数据库IP">
            <el-select
              size="small"
              filterable
              v-model.trim="msgFormDialog.databaseId"
              @change="findTree"
            >
              <el-option
                v-for="(item,index) in ipList"
                :key="index"
                :label="item.VAULE"
                :value="item.KEY"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="类型" prop="checked">
            <el-checkbox-group v-model.trim="msgFormDialog.checked">
              <el-checkbox label="结构">结构</el-checkbox>
              <el-checkbox label="数据">数据</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="执行策略" prop="jobCron">
            <el-popover v-model.trim="cronPopover">
              <vueCron @change="changeCron" @close="closeCronPopover" i18n="cn"></vueCron>
              <el-input
                slot="reference"
                @click="cronPopover=true"
                v-model.trim="msgFormDialog.jobCron"
                placeholder="请输入定时策略"
              ></el-input>
            </el-popover>
          </el-form-item>
          <el-form-item label="存储目录">
            <el-select size="small" filterable v-model.trim="msgFormDialog.storageDirectory">
              <el-option
                v-for="dict in storageDirectoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="数据库对象">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <el-tree
                ref="eltree"
                node-key="nodeKey"
                show-checkbox
                :data="treedata"
                :props="defaultProps"
                :default-checked-keys="defaultChecked"
              ></el-tree>
            </el-scrollbar>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="where条件" v-show="msgFormDialog.conditions">
        <el-input size="small" v-model.trim="msgFormDialog.conditions" :disabled="true"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm','handExe')">手工执行元数据备份</el-button>
      <el-button type="primary" @click="trueDialog('ruleForm','handtrue')">确 定</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { newTeam } from "@/components/commonVaildate";
import {
  findDataBase,
  findMeta,
  addMetaBackup,
  editMetaBackup,
  handExecute,
} from "@/api/system/metadataBackup";
import { getNextTime } from "@/api/schedule/backup/backup";
export default {
  name: "handleExport",
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    //校验是否为cron表达式
    var handleCronValidate = async (rule, value, callback) => {
      if (value == "") {
        callback(new Error("请输入执行策略!"));
      } else {
        let flag = true;
        await getNextTime({
          cronExpression: this.msgFormDialog.jobCron.split(" ?")[0] + " ?",
        }).then((res) => {
          flag = false;
        });
        if (flag) {
          callback(new Error("执行策略表达式错误!"));
        } else {
          callback();
        }
      }
    };
    return {
      cronExpression: "",
      treeJson: [],
      storageDirectoryOptions: [],
      cronPopover: false,
      defaultChecked: [],
      msgFormDialog: {
        taskName: "",
        databaseId: "",
        checked: [],
        jobCron: "0 0 2 * * ?",
        storageDirectory: "",
        conditions: "",
      },
      ipList: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      treedata: [],
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" },
        ],
        databaseId: [
          { required: true, message: "请选择数据库IP", trigger: "change" },
        ],
        checked: [{ required: true, message: "请选择类型", trigger: "change" }],
        jobCron: [
          { required: true, message: "请输入执行策略", trigger: "blur" },
        ],
        storageDirectory: [
          { required: true, message: "请选择存储目录", trigger: "change" },
        ],
      },
    };
  },
  async created() {
    await findDataBase().then((response) => {
      this.ipList = response.data;
    });
    await this.getDicts("metabackup_storage_directory").then((response) => {
      this.storageDirectoryOptions = response.data;
    });
    if (this.handleObj.id) {
      this.msgFormDialog = this.handleObj;
      this.msgFormDialog.checked = [];
      let checkedArry = this.msgFormDialog.isStructure.split(",");
      checkedArry.forEach((element) => {
        if (element == "0") {
          this.msgFormDialog.checked.push("结构");
        }
        if (element == "1") {
          this.msgFormDialog.checked.push("数据");
        }
      });
      await this.findTree(this.msgFormDialog.databaseId);
      // 获取树的选中节点
      let checkedTree = JSON.parse(this.msgFormDialog.backContent);
      this.defaultChecked = [];
      checkedTree.forEach((element) => {
        this.treeJson.forEach((t) => {
          if (element.nodeKey == t.nodeKey && element.isParent == false) {
            this.defaultChecked.push(element.nodeKey);
          }
        });
      });
      console.log(this.defaultChecked);
    }
  },
  methods: {
    changeCron(val) {
      this.cronExpression = val;
      if (val.substring(0, 5) == "* * *") {
        this.msgError("小时,分钟,秒必填");
      } else {
        this.msgFormDialog.jobCron = val.split(" ?")[0] + " ?";
      }
    },
    closeCronPopover() {
      if (this.cronExpression.substring(0, 5) == "* * *") {
        return;
      } else {
        getNextTime({
          cronExpression: this.cronExpression.split(" ?")[0] + " ?",
        }).then((res) => {
          let times = res.data;
          let html = "";
          times.forEach((element) => {
            html += "<p>" + element + "</p>";
          });
          this.$alert(html, "前5次执行时间", {
            dangerouslyUseHTMLString: true,
          }).then(() => {
            this.CronPopover = false;
          });
        });
      }
    },
    // 获取数据库对象
    async findTree(val) {
      await findMeta({ databaseId: val }).then((res) => {
        this.treeJson = res.data;
        this.treeJson.forEach((element) => {
          element.nodeKey = element.pid + "+" + element.id;
        });
        // 第一级的pid为空
        this.treedata = newTeam(this.treeJson, "");
        console.log(this.treedata);
      });
    },
    handExe(formName) {
      this.msgFormDialog.triggerStatus = 1;
    },
    trueDialog(formName, type) {
      this.msgFormDialog.triggerStatus = 1;
      let halfcheckedArry = this.$refs.eltree.getHalfCheckedKeys();
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      if (checkedArry.length == 0) {
        this.msgError("请选择数据库对象");
        return;
      }
      let newArry = [];
      checkedArry.forEach((element) => {
        this.treeJson.forEach((t) => {
          if (element == t.nodeKey) {
            let obj = {};
            obj = t;
            newArry.push(obj);
          }
        });
      });
      halfcheckedArry.forEach((element) => {
        this.treeJson.forEach((t) => {
          if (element == t.nodeKey) {
            let obj = {};
            obj = t;
            newArry.push(obj);
          }
        });
      });
      this.msgFormDialog.backContent = JSON.stringify(newArry);
      this.msgFormDialog.isStructure = [];
      this.msgFormDialog.checked.forEach((element) => {
        if (element == "结构") {
          this.msgFormDialog.isStructure.push("0");
        }
        if (element == "数据") {
          this.msgFormDialog.isStructure.push("1");
        }
      });
      this.msgFormDialog.isStructure = this.msgFormDialog.isStructure.join(",");
      console.log(this.msgFormDialog);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (type == "handtrue") {
            if (this.handleObj.id) {
              editMetaBackup(this.msgFormDialog).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.$emit("cancelHandle");
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addMetaBackup(this.msgFormDialog).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.$emit("cancelHandle");
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          } else {
            this.$prompt("请输入where条件", "温馨提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
            })
              .then(({ value }) => {
                this.msgFormDialog.conditions = value;
                handExecute(this.msgFormDialog).then((response) => {
                  if (response.code === 200) {
                    this.msgSuccess("新增成功");
                    this.$emit("cancelHandle");
                  } else {
                    this.msgError(response.msg);
                  }
                });
              })
              .catch(function () {});
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancelDialog(formName) {
      this.$emit("cancelHandle");
    },
  },
};
</script>
<style lang="scss">
.handleExport {
  .el-select {
    width: 100%;
  }
  .el-scrollbar {
    height: 340px;
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
    .el-button + .el-button {
      margin-left: 0;
    }
  }
  .el-tree {
    min-width: 100%;
    display: inline-block !important;
  }
}
</style>
