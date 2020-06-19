<template>
  <section class="handleMD5Dialog">
    <el-checkbox-group v-model.trim="checkList">
      <div v-for="(item,index) in pathList" :key="index">
        <el-checkbox :label="item.path"></el-checkbox>
      </div>
    </el-checkbox-group>
    <!-- 确定取消 -->
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="trueDialog()">执 行</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
  </section>
</template>

<script>
import { recoverStructedData } from "@/api/schedule/dataRecovery";
export default {
  name: "handleMD5Dialog",
  props: {
    handlecheckObj: {
      type: Object
    }
  },
  data() {
    return {
      msgFormObj: {},
      pathList: [],
      checkList: []
    };
  },
  created() {
    this.pathList = this.handlecheckObj.pathList;
    this.msgFormObj = this.handlecheckObj;
    for (var i = 0; i < this.pathList.length; i++) {
      if (this.pathList[i].check == true) {
        this.checkList.push(this.pathList[i].path);
      }
    }
  },
  methods: {
    trueDialog() {
      if (this.checkList.length > 0) {
        var files = "";
        for (var i = 0; i < this.checkList.length; i++) {
          if (i == this.checkList.length - 1) {
            files += this.checkList[i];
          } else {
            files += this.checkList[i] + ",";
          }
        }
        this.msgFormObj.storageDirectory = files;
        console.log(this.msgFormObj);
        recoverStructedData(this.msgFormObj).then(res => {
          if (res.code == 200) {
            this.$message({
              type: "success",
              message: "执行成功"
            });
            this.$emit("handleMd5Cancel");
          }
        });
      } else {
        this.$message({
          type: "error",
          message: "请选择一条数据"
        });
      }
    },
    cancelDialog(formName) {
      this.$emit("handleMd5Cancel");
    }
  }
};
</script>

<style lang="scss">
.handleMD5Dialog {
  .el-checkbox-group {
    overflow-x: auto;
    margin-bottom: 20px;
  }
}
</style>
