<template>
  <section class="handeleRecover">
    <el-form ref="ruleForm" :model="msgFormDialog" label-width="80px">
      <el-form-item label="类型" prop="checked">
        <el-checkbox-group v-model.trim="msgFormDialog.checked">
          <el-checkbox label="结构">结构</el-checkbox>
          <el-checkbox label="数据">数据</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="树" prop="taskName">
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
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="trueDialog('ruleForm')">恢 复</el-button>
      <el-button @click="cancelDialog('ruleForm')">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { newTeam } from "@/components/commonVaildate";
import { parsingPath, recover } from "@/api/system/metadataRecover";
export default {
  name: "handeleRecover",
  props: {
    handleObj: {
      type: Object
    }
  },

  data() {
    return {
      treeJson: [],
      storageDirectoryOptions: [],
      cronDialogVisible: false,
      defaultChecked: [],
      msgFormDialog: { isStructure: "", checked: [] },
      ipList: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      treedata: []
    };
  },
  created() {
    this.msgFormDialog.databaseId = this.handleObj.databaseId;
    this.msgFormDialog.storageDirectory = this.handleObj.storageDirectory;
    this.msgFormDialog.taskName = this.handleObj.taskName;
    parsingPath({ path: this.handleObj.storageDirectory }).then(res => {
      this.treeJson = res.data.tree;
      if (!this.treeJson) {
        this.$emit("cancelHandle");
        this.msgError("当前树结构为空，请重新选择");
        return;
      }
      this.treeJson.forEach(element => {
        element.nodeKey = element.pid + "+" + element.id;
      });
      // 第一级的pid为空
      this.treedata = newTeam(this.treeJson, "");
      this.msgFormDialog.checked = [];
      let checkedArry = res.data.isStructure.split(",");
      checkedArry.forEach(element => {
        if (element == "0") {
          this.msgFormDialog.checked.push("结构");
        }
        if (element == "1") {
          this.msgFormDialog.checked.push("数据");
        }
      });
    });
  },
  methods: {
    trueDialog(formName) {
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      let halfcheckedArry = this.$refs.eltree.getHalfCheckedKeys();
      if (checkedArry.length == 0) {
        this.msgError("请选择数据库对象");
        return;
      }
      let newArry = [];
      checkedArry.forEach(element => {
        this.treeJson.forEach(t => {
          if (element == t.nodeKey) {
            let obj = {};
            obj = t;
            newArry.push(obj);
          }
        });
      });
      halfcheckedArry.forEach(element => {
        this.treeJson.forEach(t => {
          if (element == t.nodeKey) {
            let obj = {};
            obj = t;
            newArry.push(obj);
          }
        });
      });
      this.msgFormDialog.recoverContent = JSON.stringify(newArry);
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
          recover(this.msgFormDialog).then(response => {
            if (response.code === 200) {
              this.msgSuccess("修改成功");
              this.$emit("cancelHandle");
            } else {
              this.msgError(response.msg);
            }
          });
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
.handeleRecover {
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
