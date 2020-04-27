<template>
  <section class="handleApply">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="数据库访问账户" name="first">
        <handleAccount :handleObj="handleObj" ref="AccountRef" />
      </el-tab-pane>
      <el-tab-pane label="专题库" name="second">
        <handleLibrary :handleObj="handleObj" ref="LibraryRef" />
      </el-tab-pane>
      <el-tab-pane label="资料访问权限审核" name="third">
        <handleMaterial :handleObj="handleObj" ref="MaterialRef" />
      </el-tab-pane>
    </el-tabs>
  </section>
</template>

<script>
// 数据库访问账户
import handleAccount from "@/views/authorityAudit/DBaccount/handleAccount";
// / 专题库
import handleLibrary from "@/views/authorityAudit/topicLibraryAudit/handleLibrary";
// 资料访问权限审核
import handleMaterial from "@/views/authorityAudit/materialPower/handleMaterial";
export default {
  name: "handleApply",
  props: {
    handleMsgObj: {
      type: Object
    }
  },
  components: { handleAccount, handleLibrary, handleMaterial },
  data() {
    return {
      activeName: "first",
      msgFormDialog: {},
      handleObj: { pageName: "业务用户审核" }
    };
  },
  created() {},
  methods: {
    handleClose() {},
    handleClick() {}
  }
};
</script>

