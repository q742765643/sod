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
              <el-input v-model="registerForm.D_DATA_ID" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资料名称">
              <el-input v-model="registerForm.TYPE_NAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途描述">
              <el-select disabled class="size-full" v-model="registerForm.LOGIC_NAME">
                <el-option
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                  v-for="(item,index) in logicDBArr"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="服务权限">
              <el-select class="size-full" disabled v-model="registerForm.IS_PUBLISH">
                <el-option label="公开" :value="1"></el-option>
                <el-option label="限制" :value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="表名称">
              <el-input v-model="registerForm.TABLE_NAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人">
              <el-input v-model="registerForm.USER_NAME" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核状态">
              <el-radio v-model="registerForm.radio" :label="1">通过</el-radio>
              <el-radio v-model="registerForm.radio" :label="2">不通过</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="registerForm.radio==1">
          <el-col :span="24">
            <el-form-item label="备注信息">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 4}"
                placeholder="请输入内容"
                v-model="registerForm.DATA_PROP"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="registerForm.radio==2">
          <el-col :span="24">
            <el-form-item label="拒绝原因">
              <el-input
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 4}"
                placeholder="请输入内容"
                v-model="registerForm.DATA_PROP"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div class="dialog-footer">
      <el-button type="primary" v-if="registerForm.radio==1" @click="checkFuc(2)">通过</el-button>
      <el-button type="warning" v-if="registerForm.radio==2" @click="checkFuc(3)">拒绝</el-button>
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
        radio: 1
      },
      logicDBArr: []
    };
  },
  created() {},
  methods: {
    checkFuc(type) {
      const checkobj = {
        id: this.registerForm.ID,
        examineStatus: type,
        remark: this.registerForm.DATA_PROP
      };
      this.$emit("cancelHandle", this.registerForm);
      /* updateStatus(checkobj).then(response => {
        if (response.code == 200) {
          this.$message({
            showClose: true,
            message: "操作成功",
            type: "success"
          });
          if (type == 2) {
            this.$emit("cancelHandle", this.registerForm);
          } else if (type == 3) {
            this.$emit("cancelHandle", 3);
          }
        }
      }); */
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