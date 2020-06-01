<template>
  <div class="reviewDataRegister">
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>新增资料基本信息</span>
      </div>
      <el-form label-width="90px" :model="registerForm" ref="registerForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="四级编码">
              <el-input v-model.trim="registerForm.D_DATA_ID" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资料名称">
              <el-input v-model.trim="registerForm.TYPE_NAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途描述">
              <el-input disabled class="size-full" v-model.trim="registerForm.LOGIC_NAME"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="服务权限">
              <el-select class="size-full" disabled v-model.trim="registerForm.IS_PUBLISH">
                <el-option label="公开" :value="1"></el-option>
                <el-option label="限制" :value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="表名称">
              <el-input v-model.trim="registerForm.TABLE_NAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人">
              <el-input v-model.trim="registerForm.WEB_USERNAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核状态">
              <el-radio-group v-model.trim="registerForm.examineStatus">
                <el-radio :label="1">通过</el-radio>
                <el-radio :label="2">不通过</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="registerForm.examineStatus==2?'拒绝原因':'备注信息'">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 4}"
                placeholder="请输入内容，不能超过500个字符"
                v-model.trim="registerForm.DATA_PROP"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div class="dialog-footer">
      <el-button type="primary" v-if="registerForm.examineStatus==1" @click="checkFuc(2)">通过</el-button>
      <el-button type="danger" v-if="registerForm.examineStatus==2" @click="checkFuc(3)">拒绝</el-button>
      <el-button @click="closeFuc">取消</el-button>
    </div>
  </div>
</template>

<script>
import { updateStatus } from "@/api/authorityAudit/DRegistration/reviewdataRegister";
export default {
  name: "reviewDataRegister",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      registerForm: {
        examineStatus: 1 //examineStatus tofo
      },
      logicDBArr: []
    };
  },
  created() {
    this.registerForm = this.handleObj;
  },
  methods: {
    checkFuc(type) {
      const checkobj = {
        id: this.registerForm.ID,
        ddataId: this.registerForm.D_DATA_ID,
        examineStatus: type,
        remark: this.registerForm.DATA_PROP
      };
      let msg = "";
      if (type == 3) {
        msg = "拒绝原因";
      } else {
        msg = "备注信息";
      }
      if (!checkobj.remark) {
        this.$message({
          message: "请输入" + msg + "并且" + msg + "不能超过500个字符",
          type: "error",
          offset: 100
        });
        return;
      }
      if (checkobj.remark && checkobj.remark.trim().length > 500) {
        this.$message({
          message: "输入" + msg + "不能超过500个字符",
          type: "error",
          offset: 100
        });
        return;
      }

      if (type == 2) {
        this.$emit("cancelHandle", this.registerForm);
      } else {
        // 拒绝
        updateStatus(checkobj).then(response => {
          this.$message({
            showClose: true,
            message: "操作成功",
            type: "success"
          });
          this.$emit("cancelHandle");
        });
        this.$emit("cancelHandle");
      }
    },
    closeFuc() {
      this.$emit("cancelHandle");
    }
  }
};
</script>

<style lang="scss">
.reviewDataRegister {
  .dialog-footer {
    margin-top: 10px;
    text-align: center;
  }
  .el-select {
    width: 100%;
  }
}
</style>
