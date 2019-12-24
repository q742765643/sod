<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="资料名称" prop="profileName">
        <el-input
          v-model="queryParams.profileName"
          placeholder="请输入物理库资料名称"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="存储编码" prop="dataClassId">
        <el-input
          v-model="queryParams.dataClassId"
          placeholder="请输入存储编码或者四级编码"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表名" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名"
          moveable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="triggerStatus">
        <el-select
          v-model="queryParams.triggerStatus"
          placeholder="运行状态"
          moveable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['schedule:move:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['schedule:move:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['schedule:move:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="moveList" row-key="id" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资料名称" align="center" prop="profileName" :show-overflow-tooltip="true" />
      <el-table-column label="执行策略" align="center" prop="jobCron" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="triggerStatus" :formatter="statusFormat" />
      <el-table-column label="任务描述" align="center" prop="jobDesc" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['schedule:move:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['schedule:move:remove']"
          >删除</el-button>
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

    <!-- 添加或修改迁移配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="物理库" prop="databaseId">
              <el-input v-model="form.databaseId" placeholder="请选择物理库" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资料名称" prop="dataClassId">
              <el-input v-model="form.dataClassId" placeholder="请选择资料名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="迁移条件" prop="conditions">
              <el-input v-model="form.conditions" placeholder="请输入迁移条件" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="迁移源目录" prop="sourceDirectory">
              <el-select v-model="form.sourceDirectory" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in sourceDirectoryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="迁移目标目录" prop="targetDirectory">
              <el-select v-model="form.targetDirectory" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="dict in targetDirectoryOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model="form.tableName" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="四级编码" prop="ddataId">
              <el-input v-model="form.ddataId" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="迁移限制条数" prop="moveLimit">
              <el-input v-model="form.moveLimit" placeholder="请输入限制条数单位为万"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略" prop="jobCron">
              <el-input v-model="form.jobCron" placeholder="请输入执行策略" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否告警" prop="isAlarm">
              <el-radio-group v-model="form.isAlarm">
                <el-radio
                  v-for="dict in alarmOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时间" prop="executorTimeout">
              <el-input v-model="form.executorTimeout" placeholder="请输入超时时间单位为分钟" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否清除" prop="isClear">
              <el-radio-group v-model="form.isClear">
                <el-radio
                  v-for="dict in isClearOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
          <el-form-item v-if="form.isClear != '0'" label="二级nas清除条件"  prop="clearConditions">
            <el-input v-model="form.clearConditions" placeholder="请输入二级nas清除条件" />
          </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.isClear != '0'" label="归档清除条件"  prop="archiveConditions">
              <el-input v-model="form.archiveConditions" placeholder="请输入归档清除条件" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.jobDesc" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listMove, getMove, addMove, updateMove, delMove } from "@/api/schedule/move/move";

  export default {
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 迁移表格数据
        moveList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],

        alarmOptions: [],

        isClearOptions: [],

        sourceDirectoryOptions: [],

        targetDirectoryOptions: [],

        // 日期范围
        dateRange: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          profileName: undefined,
          dataClassId: undefined,
          triggerStatus: undefined,
          tableName: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          databaseId: [
            { required: true, message: "物理库不能为空", trigger: "blur" }
          ],
          dataClassId: [
            { required: true, message: "资料名称不能为空", trigger: "blur" }
          ],
          jobCron: [
            { required: true, message: "执行策略不能为空", trigger: "blur" }
          ],
          executorTimeout: [
            { required: true, message: "超时时间不能为空", trigger: "blur" }
          ],
          moveLimit: [
            { required: true, message: "限制条数不能为空", trigger: "blur" }
          ],
          sourceDirectory: [
            { required: true, message: "迁移源目录不能为空", trigger: "blur" }
          ],
          targetDirectory: [
            { required: true, message: "迁移目标目录不能为空", trigger: "blur" }
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("job_trigger_status").then(response => {
        this.statusOptions = response.data;

      });
      this.getDicts("job_is_alarm").then(response => {
        this.alarmOptions = response.data;
      });
      this.getDicts("move_is_clear").then(response => {
        this.isClearOptions = response.data;
      });
      this.getDicts("move_source_directory").then(response => {
        this.sourceDirectoryOptions = response.data;
      });
      this.getDicts("move_target_directory").then(response => {
        this.targetDirectoryOptions = response.data;
      });

    },
    methods: {
      /** 查询字典类型列表 */
      getList() {
        this.loading = true;
        listMove(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.moveList = response.data.pageData;
            this.total = response.data.totalCount;
            this.loading = false;
          }
        );
      },
      // 字典状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.triggerStatus);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          profileName: undefined,
          dataClassId: undefined,
          triggerStatus: undefined,
          isAlarm:"1",
          isClear:"1",

        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加数据迁移配置信息";
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getMove(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改数据迁移配置信息";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateMove(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            } else {
              addMove(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$confirm('是否确认删除任务编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMove(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有类型数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportType(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    }
  };
</script>
