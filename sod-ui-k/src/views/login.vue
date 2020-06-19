<template>
  <div class="login">
    <el-row class="wrap">
      <el-col :span="13">
        <span style="opacity:0;">1</span>
      </el-col>
      <el-col :span="11">
        <img src="@/assets/image/title.png" class="title" />
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
          <el-form-item prop="username">
            <el-input
              v-model.trim="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="账号"
            >
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model.trim="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="密码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-input
              v-model.trim="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 66%"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCode" />
            </div>
          </el-form-item>
          <!-- <el-checkbox v-model.trim="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox> -->
          <el-form-item>
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              class="loginBtn"
              @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <!--  底部  -->
  </div>
</template>

<script>
import { getCodeImg, getTokenPortal } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "admin",
        password: "111111",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    if (navigator.userAgent.indexOf("Chrome") > -1) {
      // 即是chrome
    } else {
      this.msgSuccess("为更好体验该系统，请选择chrome浏览器");
    }
    let winHref = window.location.href;
    if (winHref.indexOf("interfaceId") > -1) {
      let urlParam = winHref.split("?")[1];
      getTokenPortal("?" + urlParam).then(response => {
        Cookies.set("username", response.data.userId, { expires: 30 });
        Cookies.set("password", encrypt(response.data.pwd), {
          expires: 30
        });
        this.$store
          .dispatch("ThirdLogin", response.data.token)
          .then(() => {
            this.loading = false;
            this.$router.push({ path: "/" });
          })
          .catch(() => {
            this.loading = false;
          });
      });
    }
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.data.img;
        this.loginForm.uuid = res.data.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), {
              expires: 30
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30
            });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.loading = false;
              this.$router.push({ path: this.redirect || "/" });
            })
            .catch(() => {
              this.loading = false;
              this.getCode();
            });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: linear-gradient(
    25deg,
    rgb(17, 139, 252) 38%,
    rgb(24, 151, 253) 38%,
    rgb(17, 139, 252) 45%,
    rgb(24, 151, 253) 45%,
    rgb(17, 139, 252) 70%,
    rgb(25, 150, 253) 70%,
    rgb(17, 139, 252) 85%,
    rgb(25, 150, 253) 85%
  );
  .wrap {
    width: 80%;
    text-align: right;
    background: url("../assets/image/login-bg.png") no-repeat;
    background-size: 100% 100%;
    .title {
      width: 60%;
      display: block;
      margin: auto;
      margin-top: 70px;
      margin-bottom: 40px;
    }
    .el-form-item {
      margin-bottom: 26px;
    }
    .el-input__inner {
      height: 40px;
      line-height: 40px;
    }
    .el-form {
      padding: 0 15%;
    }
    .loginBtn {
      padding: 12px 20px;
      width: 100%;
      margin-bottom: 80px;
    }
    .login-code {
      width: 34%;
      height: 40px;
      float: right;
      img {
        cursor: pointer;
        vertical-align: middle;
      }
    }
  }

  .el-login-footer {
    color: #99d5fb;
    position: fixed;
    width: 100%;
    bottom: 20px;
    text-align: center;
    font-size: 14px;
    line-height: 24px;
    p {
      margin: 0;
    }
  }
}
</style>
