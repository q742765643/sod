<template>
  <div class="DB-container">
   数据库访问账户
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, exportUser, resetUserPwd, changeUserStatus } from "@/api/system/user";
import { treeselect } from "@/api/system/dept";
import { listPost } from "@/api/system/post";
import { optionselect } from "@/api/system/role";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: { Treeselect },
  data() {
    return {
     
    };
  },
  watch: {
    
  },
  created() {
    
  },
  methods: {
   
  }
};
</script>
