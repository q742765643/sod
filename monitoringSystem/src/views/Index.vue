<template>
  <div class="appMain">
    <router-view></router-view>
  </div>
</template>

<script>
//页面头部
// import navMenu from "@/components/navMenu";
//页面底部
// import FooterBottom from "@/components/FooterBottom";
export default {
  name: "index",
  components: {
    // navMenu
    // FooterBottom
  }
};
</script>
<style lang="scss">
.appMain {
  width: 100%;
  height: 100vh;
  min-width: 1200px;
}
</style>
