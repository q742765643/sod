<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import "@/assets/common/common.scss";
export default {
  name: "app"
};
</script>

<style scoped>
body,
#app {
  width: 100%;
  height: 100vh;
  min-width: 1200px;
  background: #fff;
  overflow-x: hidden;
}
</style>
