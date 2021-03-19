<template>
  <div class="auditHistoryTem">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in historyList"
          :key="index"
          :timestamp="item.timestamp"
        >
          <h4>{{ item.statusInfo }}</h4>
          <p>备注：{{ item.remark }}</p>
          <el-button type="success" size="small">下载申请材料</el-button>
          <p class="userBox">操作用户： {{ item.userId }}</p>
          <p class="timeBox">操作时间： {{ parseTime(item.updateTime) }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-scrollbar>
  </div>
</template>

<script>
export default {
  props: {
    handleObj: {
      type: Array,
    },
  },
  data() {
    return {
      historyList: [],
    };
  },
  created() {
    this.historyList = this.handleObj;
    console.log(this.historyList);
  },
  methods: {},
};
</script>

<style lang="scss">
.auditHistoryTem {
  width: 100%;
  height: 500px;
  max-height: 90vh;
  .el-scrollbar {
    height: 100%;
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
    .userBox {
      color: #409eff;
    }
    .timeBox {
      color: #909399;
    }
  }
  .el-timeline-item {
    margin-left: 2px;
  }
}
</style>