<template>
  <div class="navbar">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <!-- <el-tooltip content="下载源码" effect="dark" placement="bottom">
          <ruo-yi id="ruoyi" class="right-menu-item hover-effect" />
        </el-tooltip>-->

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <!--  <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>-->
        <el-popover placement="bottom" width="360" trigger="hover" popper-class="headerpop">
          <div class="divInfo">
            <ul class="ulInfoBox">
              <li v-for="(item,index) in infoList" :key="index">
                <p class="editMsg">{{item.title}}</p>
                <p class="editInfo">
                  <span>{{item.operName}}</span>
                  <span>{{ parseTime(item.operTime) }}</span>
                </p>
              </li>
            </ul>
            <div class="viewAll" @click="viewAll()">查看全部通知</div>
          </div>
          <el-button type="text" slot="reference" class="right-menu-item hover-effect">
            <i class="el-icon-chat-line-square weightIcon"></i>
          </el-button>
        </el-popover>
      </template>
      <div class="right-menu-item hover-effect" style="padding-top:2px;" @click="goUncheck">
        <el-badge :value="uncheckNum">
          <i class="el-icon-bell weightIcon"></i>
        </el-badge>
      </div>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <span>{{$store.getters.name}}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile" v-show="userType ==='00'">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item>
            <span @click="setting = true">布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span @click="logout">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import RuoYi from "@/components/RuoYi";
import { findUndoCount } from "@/api/index";
import { list } from "@/api/monitor/operlog";
export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYi
  },
  data() {
    return {
      userType: "00",
      uncheckNum: 0,
      infoList: []
    };
  },
  created() {
    this.getEventList();
    this.getChangeEditList();
    this.userType = localStorage.getItem("userType");
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "device"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
      set(val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val
        });
      }
    }
  },
  methods: {
    goUncheck() {
      if (this.$route.path == "/index") {
        this.$router.push({
          path: "/redirect",
          query: "/unCheckBox"
        });
      } else {
        this.$router.push({
          name: "首页",
          params: { pageFlag: "unCheckBox" }
        });
      }
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    // 获取库表修改通知
    getChangeEditList() {
      let obj = {
        pageNum: 1,
        pageSize: 3,
        title: "表",
        params: { orderBy: { operTime: "desc" } }
      };
      list(obj).then(response => {
        this.infoList = response.data.pageData;
      });
    },
    getEventList() {
      if (localStorage.getItem("userType") === "00") {
        findUndoCount().then(res => {
          let data = res.data;
          this.uncheckNum =
            data.xzzl.uncheck +
            data.sjsq.uncheck +
            data.sjkzh.uncheck +
            data.ywztk.uncheck +
            data.ysjk.uncheck;
        });
      }
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          //location.reload();
          localStorage.clear();
          this.$store.state.tagsView.visitedViews = [];
          this.$router.push("/login");
        });
      });
    },
    viewAll() {
      this.$router.push({
        name: "操作日志",
        params: { title: "表" }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  background: #fff;
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #97a8be;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          color: #fff;
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.headerpop {
  background-color: #245fa3;
  border: none;
  padding: 0;
  margin-top: 0px !important;
  .popper__arrow::after {
    border-bottom-color: #245fa3 !important;
  }
  .divInfo {
    width: 100%;
    background-color: #245fa3;
    cursor: pointer;
    .ulInfoBox li {
      color: #fff;
      font-size: 13px;
      padding: 4px 12px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.2);
    }
    .ulInfoBox li:hover {
      background: #5dbfff;
    }
    .editInfo {
      display: flex;
      justify-content: space-between;
      color: #5d99dd;
    }
    .viewAll {
      text-align: center;
      height: 50px;
      width: 100%;
      line-height: 50px;
      font-size: 14px;
      color: #0e9ce0;
    }
    .viewAll:hover {
      background: #5dbfff;
    }
  }
}
.el-badge__content.is-fixed {
  top: 16px;
  right: 16px;
}
.weightIcon {
  font-weight: bold;
  font-size: 20px;
}
</style>
