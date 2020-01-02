<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" class="handleMaterialDialog">
    <el-tab-pane label="基本信息" name="first">
      <el-form :model="formBaseInfo" class="demo-form-inline" label-width="120px">
        <el-form-item label="用户名">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
        <el-form-item label="机构">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
        <el-form-item label="数据库账户名">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
        <el-form-item label="数据库账户密码">
          <el-input v-model="formBaseInfo.user" readonly></el-input>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="数据读写权限" name="second">
      <el-alert title="申请状态：已申请2种资料,涉及2张表的读写权限， 审核通过2张表, 审核不通过0张表" type="success" :closable="false"></el-alert>
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="状态">
          <el-select
            v-model="queryParams.examine_status"
            placeholder="状态"
            clearable
            size="small"
            style="width: 240px"
          >
            <el-option label="全部" value></el-option>
            <el-option
              v-for="(item,index) in examineStatus"
              :key="index"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请人">
          <el-input size="small" v-model="queryParams.nameUser" placeholder="申请人"></el-input>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker
            size="small"
            v-model="queryParams.time"
            type="datetimerange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          >></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-thumb" size="mini" @click="handlePower">授权</el-button>
          <el-button type="danger" icon="el-icon-close" size="mini" @click="handleRefused">拒接</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="tableData" row-key="id">
        <el-table-column type="index" width="50" :index="table_index"></el-table-column>
        <el-table-column align="center" prop="application_unit" label="资料分类"></el-table-column>
        <el-table-column align="center" prop="db_name" label="资料名称"></el-table-column>
        <el-table-column align="center" prop="db_name" label="表名称"></el-table-column>
        <el-table-column align="center" prop="db_name" label="数据库"></el-table-column>
        <el-table-column align="center" prop="db_name" label="专题名"></el-table-column>
        <el-table-column align="center" prop="db_name" label="权限"></el-table-column>
        <el-table-column align="center" prop="db_name" label="申请状态"></el-table-column>
        <el-table-column align="center" prop="db_name" label="审核状态"></el-table-column>
        <el-table-column align="center" prop="examine_status" label="拒绝原因">
          <template slot-scope="scope">
            <el-popover
              placement="top"
              effect="dark"
              trigger="click"
              width="200"
              popper-class="darkPopover"
              v-if="scope.row.uses"
            >
              <p>{{ scope.row.uses}}</p>
              <div slot="reference" class="name-wrapper">
                <el-button type="primary" size="mini" plain class="tagpointer">查看</el-button>
              </div>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  name: "handleCloudDialog",
  props: {
    handleObj: {
      type: Object
    }
  },
  data() {
    return {
      activeName: "first",
      formBaseInfo: {},
      queryParams: {},
      loading: false,
      tableData: [],
      total: 0
    };
  },
  created() {},
  methods: {
    getList() {},
    handlePower() {},
    handleRefused() {
      this.$prompt("请输入拒绝原因", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "拒绝原因不能为空"
      })
        .then(({ value }) => {})
        .catch(() => {});
    }
  }
};
</script>
<style lang="scss">
.handleMaterialDialog {
  .el-alert {
    margin-bottom: 12px;
  }
}
</style>

