<template>
  <div :class="{ 'has-logo': showLogo }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="rgba(0,0,0,0)"
        :text-color="variables.menuText"
        :unique-opened="true"
        active-text-color="#fff"
        :collapse-transition="false"
        :mode="aboutMenuPosition ? 'vertical' : 'horizontal'"
      >
        <sidebar-item
          v-for="route in permission_routes"
          v-show="route.name != 'portal后台管理'"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
    <div class v-if="!aboutMenuPosition"></div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters(["permission_routes", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      // console.log(this.$store.state.settings.sidebarLogo);
      return this.$store.state.settings.sidebarLogo;
    },
    aboutMenuPosition() {
      // console.log(this.$store.state.settings.menuPosition);
      return this.$store.state.settings.menuPosition;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
  },
};
</script>
