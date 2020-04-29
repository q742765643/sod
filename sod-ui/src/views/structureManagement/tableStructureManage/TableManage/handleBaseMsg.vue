<template>
  <div class="handleTableBaseMsg">
    <el-form ref="formClass" :model="tableFormObject" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item prop="crpindname" label="负责人名:">
            <el-input v-model="tableFormObject.crpindname" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="cmainfreq" label="维护和更新频率:">
            <el-input v-model="tableFormObject.cmainfreq" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="cdatascal" label="空间分辨率:">
            <el-input v-model="tableFormObject.cdatascal" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="cgeodesc" label="地理覆盖描述:">
            <el-input v-model="tableFormObject.cgeodesc" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="ceastbl" label="最东经度:">
            <el-input v-model="tableFormObject.ceastbl" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="cwestbl" label="最西经度:">
            <el-input v-model="tableFormObject.cwestbl" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="cnorthbl" label="最北纬度:">
            <el-input v-model="tableFormObject.cnorthbl" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="csouthbl" label="最南纬度:">
            <el-input v-model="tableFormObject.csouthbl" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="cnettype" label="站网:">
            <el-input v-model="tableFormObject.cnettype" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="cnetname" label="站网名称:">
            <el-input v-model="tableFormObject.cnetname" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="ccreatName" label="申请人名称:">
            <el-input v-model="tableFormObject.ccreatName" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="cchecker" label="审核人名称:">
            <el-input v-model="tableFormObject.cchecker" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item prop="csource" label="数据源:">
          <el-input type="textarea" v-model="tableFormObject.csource" size="small"></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item prop="cidabs" label="数据说明:">
          <el-input type="textarea" v-model="tableFormObject.cidabs" size="small"></el-input>
        </el-form-item>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="saveUserInfo('formClass')">确定</el-button>
      <el-button @click="handleClose('formClass')">取消</el-button>
    </div>
  </div>
</template>
<script>
import {
  getDataClassBaseInfo,
  saveDataClassBaseInfo
} from "@/api/structureManagement/tableStructureManage/StructureManageTable";
export default {
  name: "handleTableBaseMsg",
  props: {
    handleId: {
      type: String
    }
  },
  data() {
    return {
      //添加form表单初始化
      tableFormObject: {
        crpindname: "",
        cmainfreq: "",
        cdatascal: "",
        cgeodesc: "",
        ceastbl: "",
        cwestbl: "",
        cnorthbl: "",
        csouthbl: "",
        cnettype: "",
        cnetname: "",
        ccreatName: "",
        csource: "",
        cidabs: ""
      },
      //检查规则
      rules: {
        crpindname: [
          { required: true, message: "请输入负责人名", trigger: "blur" }
        ],
        cmainfreq: [
          { required: true, message: "请输入维护和更新频率", trigger: "blur" }
        ],
        cdatascal: [
          { required: true, message: "请输入空间分布率", trigger: "blur" }
        ],
        cgeodesc: [
          { required: true, message: "请输入地理覆盖描述", trigger: "blur" }
        ],
        ceastbl: [
          { required: true, message: "请输入最东经度", trigger: "blur" }
        ],
        cwestbl: [
          { required: true, message: "请输入最西经度", trigger: "blur" }
        ],
        cnorthbl: [
          { required: true, message: "请输入最北纬度", trigger: "blur" }
        ],
        csouthbl: [
          { required: true, message: "请输入最南纬度", trigger: "blur" }
        ],
        cnettype: [{ required: true, message: "请输入站网", trigger: "blur" }],
        cnetname: [
          { required: true, message: "请输入站网名称", trigger: "blur" }
        ],
        ccreatName: [
          { required: true, message: "请输入申请人名称", trigger: "blur" }
        ],
        cchecker: [
          { required: true, message: "请输入审核人名称", trigger: "blur" }
        ],
        csource: [{ required: true, message: "请输入数据源", trigger: "blur" }],
        cidabs: [{ required: true, message: "请输入数据说明", trigger: "blur" }]
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.tableFormObject.id = this.handleId;
      if (this.handleId) {
        // 获取基本信息
        getDataClassBaseInfo({ id: this.handleId }).then(res => {
          if (res.code == 200) {
            this.tableFormObject = res.data;
          }
        });
      }
    },
    //保存用户
    saveUserInfo(formClass) {
      //校验表单
      this.$refs[formClass].validate(valid => {
        if (valid) {
          saveDataClassBaseInfo(this.tableFormObject).then(res => {
            if (res.data.returnCode == 0) {
              this.$message({ message: "编辑成功", type: "success" });
              this.$emit("handleClose");
            } else {
              this.$message({
                message: res.data.returnMessage,
                type: "warning"
              });
              return;
            }
          });
        }
      });
    },
    //取消按钮
    handleClose(formClass) {
      this.$refs[formClass].resetFields();
      this.$emit("handleClose");
    }
  }
};
</script>
<style lang="scss">
.handleTableBaseMsg {
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 0;
  }
}
</style>