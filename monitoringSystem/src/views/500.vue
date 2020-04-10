<template>
  <div class="locate">
    <el-row>
      <el-col :span="12">
        <div class="img404" />
        <div class="sorry">很抱歉！系统正在维护中，具体请联系管理员！</div>
        <div>
          <el-button type="primary" @click="goHome">返回首页</el-button>
          <el-button @click="goBack">上一步</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="bg" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  methods: {
    goHome() {
      this.$router.push({ path: '/' })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.locate {
  position: absolute;
  width: 800px;
  height: 400px;
  top: 30%;
  left: 50%;
  transform: translateX(-50%);
  .img404 {
    height: 120px;
    width: 280px;
    background: url("~@/assets/images/500.png") center/contain no-repeat;
  }
  .sorry {
    margin: 30px 0 15px 0;
    font-size: 18px;
    color: #2cb7fd;
  }
  .el-button{
    width: 120px;
    margin-right: 20px;
  }
  .bg{
    height: 224px;
    background: url("~@/assets/images/404_images/404-bg.png") center/contain no-repeat;
  }
}
</style>
