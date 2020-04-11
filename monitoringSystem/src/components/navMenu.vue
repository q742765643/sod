<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-vertical-demo"
    @select="handleSelect"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409eff"
  >
    <el-menu-item index="1">
      <router-link to="/material" @click.native="jump('/material')">
        <i class="el-icon-menu"></i>
        <span>资料</span>
      </router-link>
    </el-menu-item>
    <el-menu-item index="2">
      <router-link to="/asses" @click.native="jump('/asses')">
        <i class="el-icon-s-operation"></i>
        <span>评估</span>
      </router-link>
    </el-menu-item>
    <el-menu-item index="3">
      <router-link to="/drawing" @click.native="jump('/drawing')">
        <i class="el-icon-picture-outline"></i>
        <span>制图</span>
      </router-link>
    </el-menu-item>

    <!-- <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-location"></i>
        <span>算法测试</span>
      </template>
      <el-menu-item index="2-1">
        <router-link to="/testResult">测试结果</router-link>
      </el-menu-item>
      <el-menu-item index="2-2">
        <router-link to="/assesResult">评估结果</router-link>
      </el-menu-item>
    </el-submenu>-->
  </el-menu>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "headeNav",
  data() {
    return {
      activeIndex: "1",
      indexDate: "",
      indexDay: ""
    };
  },
  created() {
    //设置时间
    this.setTodayTime();
  },
  computed: mapGetters(["getFunInfo"]),
  methods: {
    jump(url) {
      console.log(url);
      //判断是否是当前路由
      if (url == this.$route.path) {
        this.$router.push({
          path: "/redirect",
          query: url
        });
      } else {
        this.$router.push({
          path: url
        });
      }
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleSelect(key, keyPath) {
      this.activeIndex = key;
    },
    setTodayTime() {
      const date = new Date();
      const weekday = [
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
      ];
      this.indexDate =
        date.getFullYear() +
        "-" +
        (date.getMonth() * 1 + 1) +
        "-" +
        date.getDate();
      this.indexDay = weekday[date.getDay()];
    }
  }
};
</script>


