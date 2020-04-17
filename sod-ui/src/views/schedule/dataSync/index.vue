<template>
  <div class="app-container">
    <!-- 数据同步 -->
    <el-form :model="queryParams" ref="queryParams" :inline="true" class="searchBox">
      <el-form-item label="关键字查询">
        <el-select size="small" v-model="queryParams.selectInfo">
          <el-option
            :key="index"
            v-for="(item,index) in selectInfoSelect"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label>
        <el-input v-model="queryParams.searchValue"></el-input>
      </el-form-item>
      <el-form-item label="运行状态">
        <el-select size="small" v-model="queryParams.runState" style="width:120px;">
          <el-option label="全部" value></el-option>
          <el-option
            v-for="(item,index) in runStateSelect"
            :key="index"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button size="small" type="text" @click="superClick">
          <i class="el-icon-share"></i>高级搜索
        </el-button>
        <el-button size="small" @click="resetQuery" icon="el-icon-refresh-right">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="handleTableBox">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="addSync">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-edit" size="small">批量启停</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" size="small">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-refresh" size="small">刷新</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData" row-key="id">
      <el-table-column type="index" min-width="15" label=" "></el-table-column>
      <el-table-column type="selection" min-width="15"></el-table-column>
      <el-table-column prop="taskName" width="200px" label="任务名称" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="dataSourceId"
        width="120px"
        label="数据来源标识"
      ></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="dataFlowDirectionId"
        width="120px"
        label="数据流向标识"
      ></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="sourceDatabaseId"
        width="120px"
        label="源库"
      ></el-table-column>
      <el-table-column prop="execIp" width="120px" label="执行主机"></el-table-column>
      <el-table-column prop="execPort" width="120px" label="执行端口"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="160px">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="runState" label="运行状态" :formatter="getStatus"></el-table-column>
      <el-table-column label="操作" min-width="400">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="mini"
            icon="el-icon-video-play"
            v-if="scope.row.runState=='error'"
          >启动</el-button>
          <el-button type="text" size="mini" icon="el-icon-video-pause">停止</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row,'edit')"
            v-hasPermi="['schedule:backup:edit']"
          >编辑</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:sync:deleteSync']"
          >删除</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-view"
            @click="handleUpdate(scope.row,'detail')"
          >查看</el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-reading"
            @click="viewDaiy(scope.row)"
          >查看日志</el-button>
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

    <el-dialog title="日志列表" :visible.sync="dailyDataDialog" width="90%" v-dialogDrag>
      <dailySync v-if="dailyDataDialog" :handletaskId="handletaskId" />
    </el-dialog>
    <!-- 新增编辑 -->
    <el-dialog :title="dialogTitle" :visible.sync="handleDialog" width="80%" v-dialogDrag>
      <handleSync
        v-if="handleDialog"
        :handleObj="handleObj"
        @resetQuery="resetQuery"
        ref="myHandleServer"
      />
    </el-dialog>
    <!-- 高级搜索 -->
    <el-dialog
      title="筛选"
      :visible.sync="dialogSuperSearch"
      :before-close="closeSuperSearch"
      v-dialogDrag
    >
      <super-search
        v-if="dialogSuperSearch"
        :superObj="superObj"
        @getList="getList"
        ref="supersearchinfo"
      />
    </el-dialog>
  </div>
</template>

<script>
import { syncList, delSync, getSyncInfo } from "@/api/schedule/dataSync";
// 日志查看
import dailySync from "@/views/schedule/dataSync/dailySync";
// 增加查看
import handleSync from "@/views/schedule/dataSync/handleSync";
// 高级搜索
import SuperSearch from "@/components/superSearch";
export default {
  components: {
    dailySync,
    handleSync,
    SuperSearch
  },
  data() {
    return {
      dailyDataDialog: false,
      handletaskId: "",
      // 遮罩层
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        selectInfo: "taskName",
        searchValue: "",
        runState: ""
      },
      selectInfoSelect: [
        {
          value: "taskName",
          label: "任务名称"
        },
        {
          value: "sourceDBId",
          label: "源库"
        },
        {
          value: "dataSourceId",
          label: "数据来源标识"
        },
        {
          value: "flowDir",
          label: "数据流向标识"
        }
      ],
      runStateSelect: [
        {
          value: "01",
          label: "运行中"
        },
        {
          value: "02",
          label: "运行出错"
        },
        {
          value: "03",
          label: "未启动"
        },
        {
          value: "04",
          label: "停止中"
        }
      ],
      total: 0,
      tableData: [],
      dialogTitle: "",
      handleDialog: false,
      dialogSuperSearch: false,
      superObj: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // table自增定义方法
    table_index(index) {
      return (
        (this.queryParams.pageNum - 1) * this.queryParams.pageSize + index + 1
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 查询列表 */
    getList(superMsg) {
      // 普通搜索
      this.selectInfoSelect.forEach(element => {
        if (element.value == this.queryParams.selectInfo) {
          this.queryParams[element.value] = this.queryParams.searchValue;
        }
      });
      // 高级搜索
      if (superMsg && superMsg.domains) {
        let superList = superMsg.domains;
        let newSuperForm = {};
        for (let i = 0; i < superList.length; i++) {
          newSuperForm[superList[i].select] = superList[i].value;
        }
        Object.assign(this.queryParams, newSuperForm);
      }
      console.log(this.queryParams);
      this.loading = true;
      syncList(this.queryParams).then(response => {
        this.tableData = response.data.pageData;
        this.total = response.data.totalCount;
        this.loading = false;
      });
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        selectInfo: "taskName",
        searchValue: "",
        runState: ""
      };
      this.handleDialog = false;
      this.dailyDataDialog = false;
      this.getList();
    },
    // 高级搜索
    superClick() {
      this.superObj = {};
      this.superObj.pageName = "数据同步";
      this.dialogSuperSearch = true;
    },
    // 状态
    getStatus: function(row) {
      var value = row.runState.split("|");
      var result = "";
      if (value[0] == "true") {
        result = "运行中";
      } else if (value[0] == "false") {
        if (value[1] == "") {
          result = "停止中";
        } else {
          result = "<span>运行出错</span>";
          value[1] = value[1].replace(/[\r\n]/g, ""); //去掉回车换行
        }
      } else if (value[0] == "error") {
        result = "未启动";
      }
      return result;
    },
    handleAdd() {
      this.handleDialog = true;
    },

    viewDaiy(row) {
      this.handletaskId = row.id;
      this.dailyDataDialog = true;
    },
    addSync() {
      this.dialogTitle = "新增";
      this.handleDialog = true;
      this.handleObj = {};
    },
    closeSuperSearch() {
      this.resetQuery();
      this.dialogSuperSearch = false;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$confirm('是否确认删除任务编号为"' + id + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delSync(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    /** 修改按钮操作 */
    handleUpdate(row, type) {
      const id = row.id;
      if (type == "edit") {
        this.dialogTitle = "修改";
      } else {
        this.dialogTitle = "查看详情";
      }
      getSyncInfo(id).then(response => {
        this.handleDialog = true;
        this.handleObj = response.data;
        // / 同步任务执行节点回显
        this.handleObj.ipAndPort =
          this.handleObj.execIp + ":" + this.handleObj.execPort;
        // 目标表回显
        this.handleObj.targetTable = this.handleObj.targetRelation[0].targetTableId;
        this.handleObj.handleType = type;
        console.log(this.handleObj);
      });
    }
  }
};
</script>
