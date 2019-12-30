<template>
  <section class="codeUseDialog">
    <el-form :model="editRow" ref="editRow" :rules="baseFormRules" label-width="120px">
      <el-form-item prop="logic_id" label="数据用途ID:">
        <el-input
          size="small"
          :disabled="isDbIdDisable"
          v-model="editRow.logic_id"
          placeholder="请输入(1内容不能含有中文字符)"
        />
      </el-form-item>
      <el-form-item prop="logic_name" label="用途描述:">
        <el-input size="small" v-model="editRow.logic_name" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="storage_type" label="表类型:">
        <el-select
          size="small"
          filterable
          v-model="editRow.storage_type"
          :disabled="isDisabled"
          multiple="multiple"
        >
          <el-option
            v-for="item in tableType"
            :key="item.key_col"
            :label="item.name_cn"
            :value="item.key_col"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="database_id" label="数据库名称:">
        <el-select
          size="small"
          filterable
          v-model="editRow.database_id"
          :disabled="isDisabled"
          multiple="multiple"
        >
          <el-option
            v-for="item in dbNames"
            :key="item.database_id"
            :label="item.database_name"
            :value="item.database_id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="serial_number" label="显示序号:">
        <el-input size="small" v-model="editRow.serial_number" placeholder="请输入" />
      </el-form-item>
      <el-form-item prop="logic_desc" label="用途说明:">
        <el-input size="small" v-model="editRow.logic_desc" type="textarea" placeholder="请输入" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="addDataBasePhysicsStructure('editRow')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
export default {
  name: "codeUseDialog",
  components: {},
  props: {
    handleMsgObj: {
      type: Object
    }
  },
  data() {
    return {
      isDisabled: false,
      //编辑页面列
      editRow: {
        logic_id: "",
        logic_name: "",
        storage_type: [],
        storage_type_name: [],
        logic_desc: "",
        database_name: [],
        database_id: [],
        serial_number: ""
      },
      //判断数据库是否已存在
      flag: false,
      //控制数据库Id是否可编辑
      isDbIdDisable: false,
      //表类型
      tableType: [],
      //数据库名称
      dbNames: [],
      baseFormRules: {
        logic_id: [{ required: true, message: " ", trigger: "blur" }],
        logic_name: [{ required: true, message: " ", trigger: "blur" }],
        logic_desc: [{ required: true, message: " ", trigger: "blur" }],
        storage_type: [{ required: true, message: " ", trigger: "change" }],
        database_id: [{ required: true, message: " ", trigger: "change" }],
        serial_number: [{ required: true, message: " ", trigger: "blur" }]
      }
    };
  },
  created() {
    // this.getAllPhysics();
    // this.getAllNotype();
    // //点击编辑时处理
    // if (this.handleMsgObj) {
    //   this.editRow = this.handleMsgObj;
    //   this.editRow.storage_type = this.editRow.storage_type.split(",");
    //   this.editRow.database_id = this.editRow.database_id.split(",");
    //   this.isDbIdDisable = true;
    // } else {
    //   //点击添加时处理
    //   this.isDbIdDisable = false;
    // }
  },
  methods: {
    //查询数据库名
    async getAllPhysics() {
      await this.axios
        .post(interfaceObj.dataUsageManage_queryAllPhysics)
        .then(res => {
          if (res.data.returnCode == 0) {
            this.dbNames = res.data.list;
          }
        });
    },
    // 获取所有表类型
    async getAllNotype() {
      await this.axios
        .post(interfaceObj.dataUsageManage_queryAllNotype)
        .then(res => {
          if (res.data.returnCode == 0) {
            this.tableType = res.data.list;
          }
        });
    },
    //添加或修改数据库基本信息的保存按钮
    async addDataBasePhysicsStructure(formName) {
      var validFlag = false;
      //验证基本信息页的必填项是否输入
      this.$refs[formName].validate(valid => {
        // alert(valid);
        if (valid) {
          //判断数据库信息是更新还是插入
          let id = this.editRow.logic_id;
          this.axios
            .post(interfaceObj.dataUsageManage_queryLogicIdIsExist, { id })
            .then(res => {
              let url = "";
              //如果有值返回true,否则返回false
              if (res.data.data) {
                //更新基本信息
                url = interfaceObj.dataUsageManage_updateLogic;
              } else {
                //新插入数据
                //插入基本信息
                url = interfaceObj.dataUsageManage_addLogic;
              }
              this.axios
                .post(interfaceObj.dataUsageManage_addLogic, this.editRow)
                .then(res => {
                  this.$emit("cancelDialog");
                });
            });
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
  .el-select {
    width: 100%;
  }
  .el-textarea {
    width: 90%;
  }
  .dialog-footer {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: end;
    -ms-flex-pack: end;
    justify-content: flex-end;
    padding: 10px 0;
  }
}
</style>