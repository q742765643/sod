<template>
  <section class="codeUseDialog">
    <el-form :model="msgFormDialog" ref="fromRef" :rules="baseFormRules" label-width="120px">
      <el-form-item prop="logicFlag" label="数据用途ID:">
        <el-input
          size="small"
          :disabled="isDbIdDisable"
          v-model.trim="msgFormDialog.logicFlag"
          placeholder="请输入数据用途ID"
        />
      </el-form-item>
      <el-form-item prop="logicName" label="用途描述:">
        <el-input size="small" v-model.trim="msgFormDialog.logicName" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="storageType" label="表类型:">
        <el-select
          size="small"
          filterable
          v-model.trim="msgFormDialog.storageType"
          multiple
          @change="$forceUpdate()"
        >
          <el-option
            v-for="item in tableTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="databaseId" label="数据库名称:">
        <el-select
          @change="$forceUpdate()"
          size="small"
          filterable
          v-model.trim="msgFormDialog.databaseId"
          multiple
        >
          <el-option
            v-for="item in dbNamesList"
            :key="item.ID"
            :label="item.databaseName"
            :value="item.id"
          ></el-option>
        </el-select>
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
      <el-form-item prop="logicDesc" label="用途说明:">
        <el-input
          type="textarea"
          size="small"
          :disabled="isDbIdDisable"
          v-model.trim="msgFormDialog.logicDesc"
          placeholder="请输入用途说明"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog('fromRef')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getAllStorageType,
  getDatabaseName,
  saveLogic,
  editLogic
} from "@/api/dbDictMangement/dataUsageMangement";
import { english } from "@/components/commonVaildate.js";
export default {
  name: "codeUseDialog",
  components: {},
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    var nameValidate = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入数据用途ID"));
      } else if (!english(value)) {
        callback(new Error("数据用途ID只能由英文字母组成"));
      } else {
        callback();
      }
    };
    var DBIdValidate = (rule, value, callback) => {
      if (!value || this.msgFormDialog.databaseId.length == 0) {
        callback(new Error("请选择数据库名称"));
      } else {
        callback();
      }
    };
    var TypeValidate = (rule, value, callback) => {
      if (!value || this.msgFormDialog.storageType.length == 0) {
        callback(new Error("请选择表类型"));
      } else {
        callback();
      }
    };
    return {
      isDisabled: false,
      //编辑页面列
      msgFormDialog: {
        logicFlag: "",
        logicName: "",
        storageType: [],
        logicDesc: "",
        databaseId: [],
        serialNumber: ""
      },
      //判断数据库是否已存在
      flag: false,
      //控制数据库Id是否可编辑
      isDbIdDisable: false,
      //表类型
      tableTypeList: [],
      //数据库名称
      dbNamesList: [],
      baseFormRules: {
        logicFlag: [
          { required: true, validator: nameValidate, trigger: "blur" }
        ],
        logicName: [
          { required: true, message: "请输入用途描述", trigger: "blur" }
        ],

        storageType: [
          { required: true, validator: TypeValidate, trigger: "change" }
        ],
        databaseId: [
          { required: true, validator: DBIdValidate, trigger: "change" }
        ],
        serialNumber: [
          { required: true, message: "请输入序号 ", trigger: "blur" }
        ],
        logicDesc: [
          { required: true, message: "请输入用途描述 ", trigger: "blur" }
        ]
      }
    };
  },
  async created() {
    // 表类型
    await getAllStorageType().then(res => {
      this.tableTypeList = res.data;
    });
    // 数据库
    await getDatabaseName().then(res => {
      this.dbNamesList = res.data;
    });
    if (this.handleObj.id) {
      this.isDbIdDisable = true;
      this.msgFormDialog = this.handleObj;
      console.log(this.msgFormDialog);
    } else {
      this.isDbIdDisable = false;
    }
  },
  methods: {
    trueDialog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let obj = this.msgFormDialog;
          obj.logicDatabaseEntityList = [];
          obj.databaseId.forEach(element => {
            let dObj = {};
            dObj.databaseId = element;
            obj.logicDatabaseEntityList.push(dObj);
          });

          obj.logicStorageTypesEntityList = [];
          obj.storageType.forEach(element => {
            let dObj = {};
            dObj.storageType = element;
            obj.logicStorageTypesEntityList.push(dObj);
          });
          console.log(obj);

          if (this.handleObj.id) {
            editLogic(obj).then(res => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功"
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                });
              }
            });
          } else {
            saveLogic(obj).then(res => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "增加成功"
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                });
              }
            });
          }
        } else {
          this.$message.error("请正确填写基本信息列表");
          return;
        }
      });
    },

    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    }
  }
};
</script>

<style lang="scss">
.codeUseDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
}
</style>
