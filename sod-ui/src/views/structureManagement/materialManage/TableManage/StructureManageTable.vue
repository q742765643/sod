<template>
  <div class="scrollMain">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-tabs v-model.trim="tableActive" @tab-click="manageTabsClick">
        <el-tab-pane v-if="tabs.dc" label="解码配置" name="dc">
          <decoding-config :rowData="rowData"></decoding-config>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.rg" label="区域信息" name="rg">
          <area-config ref="areaRef" :rowData="rowData"></area-config>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.ds || tabs.pl" label="数据服务" name="ds">
          <serve-config ref="serveRef" :rowData="rowData"></serve-config>
        </el-tab-pane>
        <!-- <el-tab-pane v-if="tabs.pl" label="产品列表" name="pl">
          <product-config ref="productRef" :rowData="rowData"></product-config>
        </el-tab-pane>-->
      </el-tabs>
      <el-backtop target=".el-dialog.is-fullscreen"></el-backtop>
    </el-scrollbar>
  </div>
</template>

<script>
/*字段管理*/
import ColumnManage from "@/views/structureManagement/materialManage/TableManage/ColumnManage";
/*索引*/
import indexManage from "@/views/structureManagement/materialManage/TableManage/IndexManage";
/*分库分表键*/
import partKeyManage from "@/views/structureManagement/materialManage/TableManage/PartKeyManage";
/*资料类型统计*/
import DataStatistics from "@/views/structureManagement/materialManage/TableManage/DataStatistics";
/*样例数据查询*/
import SampleData from "@/views/structureManagement/materialManage/TableManage/SampleData";
/*表信息*/
import TableInfoManage from "@/views/structureManagement/materialManage/TableManage/TableInfoManage";
/*外键关联*/
import ForeignKeyManage from "@/views/structureManagement/materialManage/TableManage/ForeignKeyManage";
/*解码配置*/
import DecodingConfigManage from "@/views/structureManagement/materialManage/TableManage/DecodingConfigManage";
/* 区域信息 */
import areaConfigManage from "@/views/structureManagement/materialManage/TableManage/gridAreaDefine";
/* 产品列表 */
import productConfigManage from "@/views/structureManagement/materialManage/TableManage/productList";
// 数据服务
import serveConfigManage from "@/views/structureManagement/materialManage/TableManage/dataServeManage";
import {
  gcl,
  getNorm,
  saveNorm,
} from "@/api/structureManagement/materialManage/StructureManageTable";
export default {
  components: {
    "v-column": ColumnManage,
    "v-index": indexManage,
    "part-key": partKeyManage,
    "data-statistics": DataStatistics,
    "sample-data": SampleData,
    "table-info": TableInfoManage,
    "foreign-key": ForeignKeyManage,
    "decoding-config": DecodingConfigManage,
    "area-config": areaConfigManage,
    "product-config": productConfigManage,
    "serve-config": serveConfigManage,
  },
  props: { parentRowData: Object },
  data() {
    return {
      elActive: "",
      keyActive: "",
      GActive: "",
      storage: {
        //表类型展示配置
        E_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
        F_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        ME_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false,
        },
        G_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        K_E_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
        MK_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false,
        },
        NF_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true,
        },
        S_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          ds: false,
          pl: true,
          rg: true,
        },
        L_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false,
        },
      },
      dialogStatus: { publicMatedataDialog: false, columnDialog: false },
      rowData: this.parentRowData,
      tableActive: "db",
      tabs: {
        table: { el: true, key: false, ka: false },
        db: true,
        dc: false,
        rg: false,
        ds: false,
        pl: false,
      },
      keyObj: { tableInfo: {}, keyColumnData: [] },
      elObj: { tableInfo: {}, elColumnData: [] },
      pubOptions: {},
      dirRule: { dirNorm: "" },
      isDirEdit: false,
    };
  },
  methods: {
    elColumnEdit() {
      //点击要素字典编辑
      if (this.elObj.elColumnSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning",
        });
      } else {
        this.columnEditData = JSON.parse(
          JSON.stringify(this.elObj.elColumnSel[0])
        );
        this.dialogStatus.columnDialog = true;
      }
    },
    keyColumnEdit() {
      //点击键表字段编辑
      if (this.keyObj.keyColumnSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning",
        });
      } else {
        this.columnEditData = JSON.parse(
          JSON.stringify(this.keyObj.keyColumnSel[0])
        );
        this.dialogStatus.columnDialog = true;
      }
    },
    editDir() {
      this.isDirEdit = !this.isDirEdit;
    },
    saveDir() {
      this.isDirEdit = !this.isDirEdit;
      this.dirRule.id = this.rowData.DATA_CLASS_ID;
      saveNorm(this.dirRule).then((response) => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功",
          });
        }
      });
    },
    celDir() {
      this.isDirEdit = !this.isDirEdit;
    },
    getDir() {
      if (
        this.rowData.STORAGE_TYPE == "ME_table" ||
        this.rowData.STORAGE_TYPE == "MK_table" ||
        this.rowData.STORAGE_TYPE == "NF_table"
      ) {
        getNorm({ id: this.rowData.DATA_CLASS_ID }).then((response) => {
          if (response.code == 200) {
            if (response.data == null) {
              this.dirRule.dirNorm = "";
            } else {
              this.dirRule = response.data;
            }
          }
        });
      }
    },
    manageTabsClick(val) {
      if (val.label == "区域信息") {
        this.$refs["areaRef"].forParent();
      } else if (val.label == "产品列表") {
        this.$refs["productRef"].forParent();
      } else if (val.label == "数据服务") {
        this.$refs["serveRef"].forParent();
      }
    },
  },
  mounted() {
    //获取表信息
    this.tabs = this.storage[this.rowData.STORAGE_TYPE];
    this.getDir();
    this.$nextTick(function () {
      /*现在数据已经渲染完毕*/
      if (this.tabs.dc) {
        this.tableActive = "dc";
      } else if (this.tabs.rg) {
        this.tableActive = "rg";
        this.$refs["areaRef"].forParent();
      } else if (this.tabs.ds || this.tabs.pl) {
        this.tableActive = "ds";
        this.$refs["serveRef"].forParent();
      }
    });
  },
};
</script>

<style lang="scss">
.scrollMain {
  position: absolute;
  top: 48px;
  bottom: 14px;
  left: 20px;
  right: 20px;
  overflow: auto;
  .el-scrollbar {
    height: 100%;
    overflow-x: hidden;
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
  }
}
.structureManageTable {
  .el-main {
    padding: 0 10px;
  }
  .elementTableTitle {
    box-sizing: border-box;
    background: #75bbea;
    color: #fff;
    padding: 10px;

    i {
      font-size: 16px;
      margin-right: 4px;
    }
  }

  .elementTableCon {
    box-sizing: border-box;
    padding-top: 10px;
    border: 1px solid #ebebeb;
    border-top: none;
  }

  .el-form-item {
    margin-bottom: 14px;
  }

  .collapseCon {
    margin-top: 10px;

    .el-collapse-item__header {
      border: 1px solid #ebeef5;
      border-top: none;
      text-indent: 10px;

      i {
        margin-right: 2px;
      }
    }

    .el-collapse-item__content {
      border: 1px solid #ebeef5;
      border-top: none;
      border-bottom: none;
    }
  }

  .el-aside {
    width: 100px !important;
    padding: 10px;
    background: none;
    line-height: normal;
    font-size: 14px;
    padding-left: 0;
    .rightNav {
      position: fixed;
      padding: 0;
      li {
        border-left: 1px solid #ebeef5;
        padding: 10px;
        list-style: none;

        a {
          text-decoration: none;
          color: #333;
        }
      }

      li:before {
        content: "";
        width: 12px;
        height: 12px;
        background: #ddd;
        position: absolute;
        border-radius: 6px;
        left: -6px;
      }
      li.active {
        a {
          color: #75bbea;
        }
      }
      li.active:before {
        background: #75bbea;
      }
    }
  }
  .elementDir {
    padding: 0 10px;
  }
  .buttonCon {
    .el-form-item__content {
      margin-left: 20px !important;
    }
  }
}
</style>
