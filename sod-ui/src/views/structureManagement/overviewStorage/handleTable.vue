<template>
  <section class="handleTableDialog">
    <el-form
      :rules="rules"
      ref="ruleForm"
      :model="msgFormObj"
      label-width="80px"
    >
      <el-row>
        <el-col :span="8">
          <el-form-item label="数据库" prop="databasePid">
            <el-select
              filterable
              v-model="msgFormObj.databasePid"
              placeholder="请选择"
              @change="handleFindSpec"
            >
              <el-option
                v-for="(citem, cindex) in DatabaseDefineList"
                :key="cindex"
                :label="citem.databaseName"
                :value="citem.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="专题库" prop="databaseId">
            <el-select
              filterable
              v-model="msgFormObj.databaseId"
              placeholder="请选择"
            >
              <el-option
                v-for="(citem, cindex) in specialList"
                :key="cindex"
                :label="citem.databaseName"
                :value="citem.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="存储类型" prop="storageType">
            <el-select
              filterable
              v-model="msgFormObj.storageType"
              placeholder="请选择"
            >
              <el-option
                v-for="(citem, cindex) in storageTypeList"
                :key="cindex"
                :label="citem.dictLabel"
                :value="citem.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div></div>
    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog()">确 认</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import {
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
} from "@/api/structureManagement/materialManage/index";
export default {
  name: "handleMD5Dialog",
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      storageTypeList: [], //表类型list
      specialList: [], //专题库list
      DatabaseDefineList: [], //专题库list
      msgFormObj: {},
      rules: {
        databasePid: [
          { required: true, message: "请选择数据库", trigger: "change" },
        ],
        databaseId: [
          { required: true, message: "请选择专题库", trigger: "change" },
        ],
        storageType: [
          { required: true, message: "请选择表类型", trigger: "change" },
        ],
      },
    };
  },
  async created() {
    await this.getDicts("sys_storage_type").then((response) => {
      this.storageTypeList = response.data;
    });
    await getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
  },
  methods: {
    //切换数据库查询专题库
    handleFindSpec(val) {
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },
    trueDialog() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.$emit("cancelHandle", this.msgFormObj);
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
.handleTableDialog {
  .el-select {
    width: 100%;
  }
}
</style>
