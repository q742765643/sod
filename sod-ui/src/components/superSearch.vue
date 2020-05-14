
<template>
  <el-form :model="superSearchForm" label-width="100px" class="superSearchDialog">
    <el-form-item v-for="domain in superSearchForm.domains" :key="domain.key">
      <el-select v-model="domain.select" placeholder="请选择">
        <el-option
          v-for="(item,index) in superChose"
          :key="index"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-input v-model="domain.value" class="elInput"></el-input>
      <i class="el-icon-plus" @click="addDomain"></i>
      <i class="el-icon-minus" @click.prevent="removeDomain(domain)"></i>
    </el-form-item>
    <el-form-item>
      <el-button class="submit" type="primary" @click="submitSuperSearch('superSearchForm')">查询</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "superSearchDialog",
  props: {
    superObj: {
      type: Object
    }
  },
  data() {
    return {
      superDialogObj: {},
      superChose: [],
      superSearchForm: {
        domains: [
          {
            value: "",
            select: ""
          }
        ]
      }
    };
  },
  created() {
    this.superDialogObj = this.superObj;
    this.getSuperChose();
  },
  methods: {
    getSuperChose() {
      if (this.superDialogObj.pageName == "存储结构概览") {
        this.superChose = [
          {
            label: "数据用途",
            value: "logicName"
          },
          {
            label: "数据库",
            value: "databaseName"
          },
          {
            label: "资料名称",
            value: "className"
          },
          {
            label: "表名称",
            value: "tableName"
          },
          {
            label: "存储编码",
            value: "dataClassId"
          },
          {
            label: "四级编码",
            value: "DDataId"
          },
          {
            label: "专题名",
            value: "specialDatabaseName"
          }
        ];
      } else if (this.superDialogObj.pageName == "存储字段检索") {
        this.superChose = [
          {
            label: "公共元数据字段",
            value: "dbEleCode"
          },
          {
            label: "字段名称",
            value: "CElementCode"
          },
          {
            label: "服务名称",
            value: "userEleCode"
          },
          {
            label: "中文简称",
            value: "eleName"
          },
          {
            label: "数据类型",
            value: "type"
          },
          {
            label: "数据精度",
            value: "accuracy"
          },
          {
            label: "资料名称",
            value: "className"
          },
          {
            label: "表名称",
            value: "tableName"
          },
          {
            label: "数据用途",
            value: "logicName"
          }
        ];
      } else if (this.superDialogObj.pageName == "非结构化数据迁移配置") {
        this.superChose = [
          {
            label: "用途描述",
            value: "logic_name"
          },
          {
            label: "数据库名",
            value: "physics_dbname"
          },
          {
            label: "专题名",
            value: "special_database_name"
          },
          {
            label: "资料名称",
            value: "data_service_name"
          },
          {
            label: "IP",
            value: "exec_ip"
          },
          {
            label: "端口号",
            value: "exec_port"
          },
          {
            label: "执行策略",
            value: "cron"
          }
        ];
      } else if (this.superDialogObj.pageName == "数据备份") {
        this.superChose = [
          {
            label: "数据用途",
            value: "logicName"
          },
          {
            label: "数据库名称",
            value: "databaseName"
          },
          {
            label: "专题名",
            value: "specialDatabaseName"
          },
          {
            label: "资料名称",
            value: "dataServiceName"
          },
          {
            label: "IP",
            value: "execIp"
          },
          {
            label: "端口",
            value: "execPort"
          }
        ];
      } else if (this.superDialogObj.pageName == "GRIB参数定义") {
        this.superChose = [
          {
            label: "要素存储短名",
            value: "eleCodeShort"
          },
          {
            label: "学科",
            value: "subjectId"
          },
          {
            label: "参数种类",
            value: "classify"
          },
          {
            label: "参数编码",
            value: "parameterId"
          },
          {
            label: "GRIB版本",
            value: "gribVersion"
          },
          /* {
            label: "中文描述",
            value: "elementCn"
          }, */
          {
            label: "是否为共有配置",
            value: "publicConfig"
          },
          {
            label: "模板编号",
            value: "templateId"
          },
          {
            label: "模板说明",
            value: "templateDesc"
          }
        ];
      } else if (this.superDialogObj.pageName == "数据同步") {
        this.superChose = [
          {
            value: "taskName",
            label: "任务名称"
          },

          {
            value: "dataSourceId",
            label: "数据来源标识"
          },
          {
            value: "dataFlowDirectionId",
            label: "数据流向标识"
          },
          {
            value: "sourceDatabaseName",
            label: "源库"
          }
        ];
      }
    },

    removeDomain(item) {
      var index = this.superSearchForm.domains.indexOf(item);
      if (index !== 0) {
        this.superSearchForm.domains.splice(index, 1);
      }
    },
    addDomain() {
      this.superSearchForm.domains.push({
        value: "",
        key: Date.now()
      });
    },
    submitSuperSearch(formName) {
      this.$emit("searchFun", this.superSearchForm);
    }
  }
};
</script>
<style lang="scss">
.superSearchDialog {
  .el-form-item__content {
    display: flex;
    margin-left: 0 !important;
    align-items: center;
  }
  .elInput {
    margin: 0 12px;
  }
  .el-icon-plus,
  .el-icon-minus {
    width: 16px;
    height: 16px;
    color: #fff;
    border-radius: 4px;
    cursor: pointer;
  }
  .el-icon-plus {
    background: #409eff;
  }
  .el-icon-minus {
    background: #f56c6c;
    margin-left: 8px;
  }
  .submit {
    margin: auto;
    margin-bottom: 20px;
  }
}
</style>

