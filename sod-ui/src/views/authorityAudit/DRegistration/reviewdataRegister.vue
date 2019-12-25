<template>
  <div class="reviewDataRegister">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>新增资料基本信息</span>
      </div>
      <el-form label-width="90px" :model="registerForm" ref="registerForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="四级编码">
              <el-input v-model="registerForm.d_data_id" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资料名称">
              <el-input v-model="registerForm.type_name" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途描述">
              <el-select disabled class="size-full" v-model="registerForm.logic_id">
                <el-option
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                  v-for="(item,index) in logicDBArr"
                ></el-option>
              </el-select>
              <!--  <el-input v-model="registerForm.logic_id" :disabled="true"></el-input> -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="服务权限">
              <el-select class="size-full" disabled v-model="registerForm.ispublish">
                <el-option label="公开" value="1"></el-option>
                <el-option label="限制" value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="表名称">
              <el-input v-model="registerForm.table_name" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人">
              <el-input v-model="registerForm.username" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核状态">
              <el-radio v-model="radio" label="1">通过</el-radio>
              <el-radio v-model="radio" label="2">不通过</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="radio==1">
          <el-col :span="24">
            <el-form-item label="备注信息">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 4}"
                placeholder="请输入内容"
                v-model="registerForm.data_prop"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="radio==2">
          <el-col :span="24">
            <el-form-item label="拒绝原因">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 4}"
                placeholder="请输入内容"
                v-model="registerForm.data_prop"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div class="dialog-footer">
      <el-button type="primary" v-if="radio==1" @click="checkFuc(2)">通过</el-button>
      <el-button type="warning" v-if="radio==2" @click="checkFuc(3)">拒绝</el-button>
    </div>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig";

export default {
  name: "reviewDataRegister",
  props: {
    registerForm: {
      type: Object
    }
  },
  data() {
    return {
      //registerForm: {},
      logicDBArr: [],
      radio: "1"
    };
  },
  created() {
    // this.initLogicDB();
  },
  methods: {
    //初始化数据用途
    initLogicDB() {
      this.axios
        .get(interfaceObj.getLogicDB)
        .then(({ data }) => {
          if (data.returnCode == "0") {
            this.logicDBArr = data.DS;
          } else {
            this.$message({
              type: "error",
              message: data.returnMessage
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    checkFuc(type) {
      //this.$emit("closeexamine", this.registerForm);
      const checkobj = {
        apply_id: this.registerForm.apply_id,
        d_data_id: this.registerForm.d_data_id,
        status: type,
        cause: this.registerForm.data_prop
      };
      this.axios
        .post(interfaceObj.DataRegister_powerCheck, checkobj)
        .then(res => {
          if (res.data.returnCode == "0") {
            this.$message({
              showClose: true,
              message: "操作成功",
              type: "success"
            });
            if (type == 2) {
              this.$emit("closeexamine", this.registerForm);
            } else if (type == 3) {
              this.$emit("closeexamine", type);
            }
          } else {
            this.$message.error("操作失败，请联管理员");
          }
        });
    }
  }
};
</script>

<style lang="scss">
</style>