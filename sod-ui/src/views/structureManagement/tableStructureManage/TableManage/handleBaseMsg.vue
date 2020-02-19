<template>
  <div class="handleTableBaseMsg">
    <el-form ref="formClass" :model="tableFormObject" :rules="rules" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_rpindname" label="负责人名:">
            <el-input v-model="tableFormObject.c_rpindname" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_mainfreq" label="维护和更新频率:">
            <el-input v-model="tableFormObject.c_mainfreq" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_datascal" label="空间分辨率:">
            <el-input v-model="tableFormObject.c_datascal" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_geodesc" label="地理覆盖描述:">
            <el-input v-model="tableFormObject.c_geodesc" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_eastbl" label="最东经度:">
            <el-input v-model="tableFormObject.c_eastbl" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_westbl" label="最西经度:">
            <el-input v-model="tableFormObject.c_westbl" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_northbl" label="最北纬度:">
            <el-input v-model="tableFormObject.c_northbl" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_southbl" label="最南纬度:">
            <el-input v-model="tableFormObject.c_southbl" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_nettype" label="站网:">
            <el-input v-model="tableFormObject.c_nettype" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_netname" label="站网名称:">
            <el-input v-model="tableFormObject.c_netname" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="c_creat_name" label="申请人名称:">
            <el-input v-model="tableFormObject.c_creat_name" size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="c_checker" label="审核人名称:">
            <el-input v-model="tableFormObject.c_checker" size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item prop="c_source" label="数据源:">
          <el-input type="textarea" v-model="tableFormObject.c_source" size="small"></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item prop="c_idabs" label="数据说明:">
          <el-input type="textarea" v-model="tableFormObject.c_idabs" size="small"></el-input>
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
        c_rpindname: "",
        c_mainfreq: "",
        c_datascal: "",
        c_geodesc: "",
        c_eastbl: "",
        c_westbl: "",
        c_northbl: "",
        c_southbl: "",
        c_nettype: "",
        c_netname: "",
        c_creat_name: "",
        c_source: "",
        c_idabs: ""
      },
      //检查规则
      rules: {
        c_rpindname: [
          { required: true, message: "请输入负责人名", trigger: "blur" }
        ],
        c_mainfreq: [
          { required: true, message: "请输入维护和更新频率", trigger: "blur" }
        ],
        c_datascal: [
          { required: true, message: "请输入空间分布率", trigger: "blur" }
        ],
        c_geodesc: [
          { required: true, message: "请输入地理覆盖描述", trigger: "blur" }
        ],
        c_eastbl: [
          { required: true, message: "请输入最东经度", trigger: "blur" }
        ],
        c_westbl: [
          { required: true, message: "请输入最西经度", trigger: "blur" }
        ],
        c_northbl: [
          { required: true, message: "请输入最北纬度", trigger: "blur" }
        ],
        c_southbl: [
          { required: true, message: "请输入最南纬度", trigger: "blur" }
        ],
        c_nettype: [{ required: true, message: "请输入站网", trigger: "blur" }],
        c_netname: [
          { required: true, message: "请输入站网名称", trigger: "blur" }
        ],
        c_creat_name: [
          { required: true, message: "请输入申请人名称", trigger: "blur" }
        ],
        c_checker: [
          { required: true, message: "请输入审核人名称", trigger: "blur" }
        ],
        c_source: [
          { required: true, message: "请输入数据源", trigger: "blur" }
        ],
        c_idabs: [
          { required: true, message: "请输入数据说明", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      if (this.handleId != undefined) {
        // 获取基本信息
        this.axios
          .get(
            interfaceObj.TableStructure_getBaseInfo +
              "?data_class_id=" +
              this.handleId
          )
          .then(res => {
            if (res.data.returnCode == 0) {
              this.tableFormObject = res.data.data;
            }
          });
      }
    },
    //保存用户
    saveUserInfo(formClass) {
      //校验表单
      this.$refs[formClass].validate(valid => {
        if (valid) {
          this.axios
            .post(interfaceObj.TableStructure_addBaseInfo, this.tableFormObject)
            .then(res => {
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