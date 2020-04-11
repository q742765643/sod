import Vue from "vue";
import Router from "vue-router";
import NoFound from "./views/404";
import Index from "./views/Index";
import Home from "./views/monitoringInformation";

Vue.use(Router);

export default new Router({
  // mode: "history",
  base: process.env.BASE_URL,
  routes: [{
      path: "*",
      name: "/404",
      component: NoFound
    },
    {
      path: "/",
      redirect: "/monitoringInformation"
    },
    {
      path: "/index",
      name: "index",
      component: Index,
      children: [{
          path: "",
          component: Home
        }, {
          path: "/monitoringInformation",
          name: "监测信息",
          component: () => import("./views/monitoringInformation.vue")
        }, {
          path: "/variantManage",
          name: "变量监控",
          component: () => import("./views/variantManage/Index.vue")
        },

      ]
    }
  ]
});