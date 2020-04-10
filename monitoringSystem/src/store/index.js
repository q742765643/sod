import Vue from "vue";
import Vuex from "vuex";
import register from "./modules/funRegisterMod";
import taskMod from "./modules/taskMod";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    register,
    taskMod
  }
});
