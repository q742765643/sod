<template>
  <section class="handleExport">
    <el-form ref="ruleForm" :rules="rules" :model="msgFormDialog" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input size="small" v-model="msgFormDialog.taskName"></el-input>
          </el-form-item>
          <el-form-item label="数据库IP">
            <el-select
              size="small"
              filterable
              v-model="msgFormDialog.databaseId"
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
            <el-checkbox-group v-model="msgFormDialog.checked">
              <el-checkbox label="结构">结构</el-checkbox>
              <el-checkbox label="数据">数据</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-row v-show="msgFormDialog.handleType=='add'">
            <el-col :span="20">
              <el-form-item label="执行策略" prop="jobCron">
                <el-input size="small" v-model="msgFormDialog.jobCron"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item class="unitFormItem">
                <el-button
                  size="small"
                  type="primary"
                  @click="cronDialogVisible=true"
                  style="padding:9px 0;width: 100%;"
                >配置策略</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="存储目录">
            <el-select size="small" filterable v-model="msgFormDialog.storageDirectory">
              <el-option
                v-for="dict in storageDirectoryOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库对象" prop="taskName">
            <el-scrollbar wrap-class="scrollbar-wrapper">
              <el-tree
                ref="eltree"
                node-key="id"
                show-checkbox
                :data="treedata"
                :props="defaultProps"
                :default-checked-keys="defaultChecked"
              ></el-tree>
            </el-scrollbar>
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
    <Cron
      append-to-body
      v-if="cronDialogVisible"
      :cronDialogVisible="cronDialogVisible"
      @closeCron="closeCron"
      @setCron="setCron"
    />
  </section>
</template>

<script>
import Cron from "@/components/cron/Cron";
import {
  findDataBase,
  findMeta,
  addMetaBackup,
  editMetaBackup
} from "@/api/system/metadataBackup";
export default {
  name: "handleExport",
  props: {
    handleObj: {
      type: Object
    }
  },
  components: {
    Cron
  },
  data() {
    return {
      treeJson: [],
      storageDirectoryOptions: [],
      cronDialogVisible: false,
      defaultChecked: [],
      msgFormDialog: {
        taskName: "",
        databaseId: "",
        checked: [],
        jobCron: "",
        storageDirectory: ""
      },
      ipList: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      treedata: [],
      rules: {
        taskName: [
          { required: true, message: "请输入任务名称", trigger: "blur" }
        ],
        databaseId: [
          { required: true, message: "请选择数据库IP", trigger: "change" }
        ],
        checked: [{ required: true, message: "请选择类型", trigger: "change" }],
        jobCron: [
          { required: true, message: "请输入执行策略", trigger: "blur" }
        ],
        storageDirectory: [
          { required: true, message: "请选择存储目录", trigger: "change" }
        ]
      }
    };
  },
  async created() {
    this.msgFormDialog.handleType = this.handleObj.handleType;
    await findDataBase().then(response => {
      this.ipList = response.data;
    });
    await this.getDicts("metabackup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
    if (this.handleObj.id) {
      this.msgFormDialog = this.handleObj;
      this.msgFormDialog.checked = [];
      let checkedArry = this.msgFormDialog.isStructure.split(",");
      checkedArry.forEach(element => {
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
      checkedTree.forEach(element => {
        this.defaultChecked.push(element.id);
      });
    }
  },
  methods: {
    // 获取数据库对象
    findTree(val) {
      findMeta({ databaseId: val }).then(res => {
        var json = res.data;
        this.treeJson = res.data;
        var newjson = [];
        for (var i = 0; i < json.length; i++) {
          if (!json[i].pId) newjson.push(json[i]);
          for (var j = i + 1; j < json.length; j++) {
            if (json[i].id == json[j].pId) {
              json[i].children = json[i].children || [];
              json[i].children.push(json[j]);
            } else if (json[j].id == json[i].pId) {
              json[j].children = json[j].children || [];
              json[j].children.push(json[i]);
            }
          }
        }
        this.treedata = newjson;
      });
    },

    trueDialog(formName) {
      this.msgFormDialog.triggerStatus = 1;
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      if (checkedArry.length == 0) {
        this.msgError("请选择数据库对象");
        return;
      }
      let newArry = [];
      checkedArry.forEach(element => {
        this.treeJson.forEach(t => {
          if (element == t.id) {
            let obj = {};
            obj = t;
            newArry.push(obj);
          }
        });
      });
      this.msgFormDialog.backContent = JSON.stringify(newArry);
      this.msgFormDialog.isStructure = [];
      this.msgFormDialog.checked.forEach(element => {
        if (element == "结构") {
          this.msgFormDialog.isStructure.push("0");
        }
        if (element == "数据") {
          this.msgFormDialog.isStructure.push("1");
        }
      });
      this.msgFormDialog.isStructure = this.msgFormDialog.isStructure.join(",");
      console.log(this.msgFormDialog);
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.handleObj.id) {
            editMetaBackup(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.$emit("cancelHandle");
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addMetaBackup(this.msgFormDialog).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.$emit("cancelHandle");
              } else {
                this.msgError(response.msg);
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
    },
    //设置cron表达式
    setCron(cronExpression) {
      this.msgFormDialog.jobCron = cronExpression;
      this.cronDialogVisible = false;
    },
    closeCron() {
      this.cronDialogVisible = false;
    }
  }
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
