<template>
  <section class="handleDataRecoveryDialog">
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormDialog"
      label-width="100px"
      :inline="true"
      class="searchBox"
    >
      <el-form-item label="数据库名称" prop="databaseId">
        <el-input size="small" v-model.trim="msgFormDialog.database_name" disabled></el-input>
      </el-form-item>
      <el-form-item label="资料名称" prop="dataClassId">
        <el-input size="small" v-model.trim="msgFormDialog.class_name" disabled></el-input>
      </el-form-item>
      <el-form-item label="目录" prop="path">
        <el-select size="small" v-model.trim="msgFormDialog.path">
          <el-option
            v-for="dict in storageDirectoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getTreeList" size="small" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <p style="font-size:14px">恢复文件列表</p>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-tree
        ref="eltree"
        node-key="id"
        show-checkbox
        :data="treedata"
        :props="defaultProps"
        :load="loadNode"
        lazy
      ></el-tree>
    </el-scrollbar>

    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleRecover('ruleForm')">md5数据校验</el-button>
      <el-button @click="handleRecover('false')">取 消</el-button>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      width="90%"
      title="MD5校验"
      :visible.sync="innerCheckMD5Visible"
      append-to-body
      v-dialogDrag
    >
      <chechExe
        v-if="innerCheckMD5Visible"
        :handlecheckObj="handlecheckObj"
        @handleMd5Cancel="handleMd5Cancel"
        ref="myHandleChild"
      />
    </el-dialog>
  </section>
</template>

<script>
// 树转换
import { newTeam } from "@/components/commonVaildate";
// md5校验
import chechExe from "@/views/structureManagement/overviewStorage/handelchechExe";
// 接口
import {
  getDataFileList,
  md5Check,
  getFileChidren
} from "@/api/schedule/dataRecovery";
export default {
  name: "handleDataRecoveryDialog",
  components: {
    chechExe
  },
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      loading: true,
      treedata: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      storageDirectoryOptions: [], //目录
      // 查询
      msgFormDialog: {
        databaseId: "",
        dataClassId: "",
        path: ""
      },
      // md5
      innerCheckMD5Visible: false,
      handlecheckObj: {},
      rules: {
        databaseId: [
          { required: true, message: "请选择数据库", trigger: "change" }
        ],
        dataClassId: [
          { required: true, message: "请选择资料", trigger: "change" }
        ],
        path: [{ required: true, message: "请选择目录", trigger: "change" }]
      }
    };
  },
  created() {
    this.msgFormDialog = this.handleObj;
    // 获取目录
    this.getDicts("backup_storage_directory").then(response => {
      this.storageDirectoryOptions = response.data;
    });
  },
  methods: {
    getTreeList(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.loading = true;
          console.log(this.msgFormDialog);
          getDataFileList(this.msgFormDialog).then(res => {
            // 第一级的pid为空
            this.treedata = newTeam(res.data, "");
            console.log(this.treedata);
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // 树的懒加载
    loadNode(node, resolve) {
      if (node.level === 0 || node.data.isParent == false) {
        return resolve([]);
      } else {
        getFileChidren({ childrenPath: node.data.id }).then(res => {
          resolve(res.data);
        });
      }
    },
    handleRecover(formName) {
      if (formName == "false") {
        this.$emit("handleRecover", false);
        return;
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          let checkedArry = this.$refs.eltree.getCheckedKeys();
          if (checkedArry.length == 0) {
            this.msgError("请选择一条数据");
            return;
          }
          let obj = {};
          obj.databaseId = this.msgFormDialog.databaseId;
          obj.paths = checkedArry;
          console.log(obj);
          md5Check(obj).then(res => {
            if (res.code == 200) {
              this.handlecheckObj = {};
              this.handlecheckObj = this.msgFormDialog;
              this.handlecheckObj.pathList = res.data;
              this.innerCheckMD5Visible = true;
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // mds校验
    handleExe() {
      let checkedArry = this.$refs.eltree.getCheckedKeys();
      if (checkedArry.length == 0) {
        this.msgError("请选择一条数据");
        return;
      }
      let obj = {};
      obj.databaseId = this.msgFormDialog.databaseId;
      obj.paths = checkedArry;
      console.log(obj);
      md5Check(obj).then(res => {
        if (res.code == 200) {
          this.handlecheckObj = {};
          this.handlecheckObj = this.msgFormDialog;
          this.handlecheckObj.pathList = res.data;
          this.innerCheckMD5Visible = true;
        }
      });
    },
    // 确认 md5
    handleMd5Cancel() {
      this.innerCheckMD5Visible = false;
    }
  }
};
</script>

<style lang="scss">
.handleDataRecoveryDialog {
  .el-scrollbar {
    margin: 10px 0;
    padding-top: 10px;
    height: 300px;
    border: 1px solid #c0c4cc;
    border-radius: 2px;
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
}
</style>
