import Vue from "vue";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

import HappyScroll from 'vue-happy-scroll'
import 'vue-happy-scroll/docs/happy-scroll.css'

import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "./http";

Vue.prototype.axios = axios;
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(HappyScroll)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");