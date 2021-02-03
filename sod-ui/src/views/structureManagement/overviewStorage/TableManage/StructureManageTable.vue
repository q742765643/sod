<template>
  <div class="scrollMain">
    <!-- <el-alert
      title="物理库还未创建此表"
      type="error"
      :closable="false"
      v-show="!tipsFlag"
    >
    </el-alert> -->
    <div class="el-alert el-alert--error is-light" v-show="!tipsFlag">
      <div class="el-alert__content">物理库还未创建此表</div>
      <el-button
        size="small"
        @click="showSQLManage"
        type="primary"
        icon="el-icon-first-aid-kit"
        plain
        >创建物理表</el-button
      >
    </div>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-container class="structureManageTable">
        <el-main id="box">
          <el-form
            v-if="
              this.rowData.STORAGE_TYPE == 'ME_table' ||
              this.rowData.STORAGE_TYPE == 'MK_table' ||
              this.rowData.STORAGE_TYPE == 'NF_table'
            "
            v-model.trim="dirRule"
            class="elementDir"
            label-width="100px"
          >
            <el-row>
              <el-col :span="20">
                <el-form-item label="存储目录">
                  <el-input
                    :disabled="!isDirEdit"
                    placeholder="存储目录"
                    size="small"
                    v-model.trim="dirRule.dirNorm"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label class="buttonCon">
                  <el-button
                    v-if="!isDirEdit"
                    type="primary"
                    size="small"
                    @click="editDir"
                    >编辑</el-button
                  >
                  <el-button
                    v-if="isDirEdit"
                    type="primary"
                    size="small"
                    @click="saveDir"
                    >保存</el-button
                  >
                  <el-button
                    v-if="isDirEdit"
                    type="primary"
                    size="small"
                    @click="celDir"
                    >取消</el-button
                  >
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div class="el-main">
            <div class="elementTableTitle">
              <i id="el_table_flag" class="el-icon-postcard"></i>表信息
            </div>
            <table-info
              class="floor"
              :tableInfo="commonTableObj.tableInfo"
              :rowData="rowData"
              :tableBaseInfo="tableBaseInfo"
              tableType="E"
              v-on:reloadTableInfo="getTableInfo"
              @findTips="findTips"
            ></table-info>
          </div>
        </el-main>
      </el-container>
      <el-tabs
        v-if="tabs.table.common"
        v-model.trim="tableActive"
        @tab-click="manageTabsClick"
      >
        <el-tab-pane label="基本信息" name="db">
          <el-collapse
            class="collapseCon el-col el-col-24"
            v-model="activeBaseCol"
          >
            <el-collapse-item class="floor" name="column">
              <template slot="title">
                <i id="el_field" class="el-icon-price-tag"></i>字段
              </template>
              <div>
                <v-column
                  v-on:reloadTableInfo="getTableInfo"
                  :rowData="rowData"
                  :tableInfo="commonTableObj.tableInfo"
                ></v-column>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
        <el-tab-pane label="约束分区" name="yueArea">
          <el-collapse v-model="activeIndexCol">
            <el-collapse-item class="floor" name="1">
              <template slot="title">
                <i id="el_indexes" class="el-icon-price-tag"></i>索引
              </template>
              <div>
                <v-index :tableInfo="commonTableObj.tableInfo"></v-index>
              </div>
            </el-collapse-item>
            <el-collapse-item class="floor" name="2">
              <template slot="title">
                <i id="el_tableKey" class="el-icon-price-tag"></i>分区键
              </template>
              <div>
                <part-key
                  :tableInfo="commonTableObj.tableInfo"
                  :rowData="rowData"
                ></part-key>
              </div>
            </el-collapse-item>
          </el-collapse>
          <div
            class="elementTableTitle"
            v-if="
              commonTableObj.tableInfo.storageType == 'K_E_table' ||
              commonTableObj.tableInfo.storageType == 'MK_table' ||
              tableBaseInfo.storageType == 'K_E_table' ||
              tableBaseInfo.storageType == 'MK_table'
            "
          >
            <i class="el-icon-postcard"></i>外键关联
          </div>
          <div
            v-if="
              commonTableObj.tableInfo.storageType == 'K_E_table' ||
              commonTableObj.tableInfo.storageType == 'MK_table' ||
              tableBaseInfo.storageType == 'K_E_table' ||
              tableBaseInfo.storageType == 'MK_table'
            "
          >
            <foreign-key
              :tableInfo="commonTableObj.tableInfo"
              :rowData="rowData"
            ></foreign-key>
          </div>
        </el-tab-pane>
        <el-tab-pane label="DDL" name="ddl">
          <handle-SQl
            :tableInfo="commonTableObj.tableInfo"
            :rowData="rowData"
          ></handle-SQl>
        </el-tab-pane>
        <el-tab-pane label="资料类型统计" name="maType">
          <data-statistics
            :tableInfo="commonTableObj.tableInfo"
            :rowData="rowData"
          ></data-statistics>
        </el-tab-pane>
        <el-tab-pane label="样例数据查询" name="demoData">
          <div>
            <sample-data
              :tableInfo="commonTableObj.tableInfo"
              :rowData="rowData"
            ></sample-data>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div v-show="tabs.table.ka" class="el-main">
        <el-collapse class="collapseCon el-col el-col-24" v-model="activeKaCol">
          <el-collapse-item name="keycode" class="floor">
            <template slot="title">
              <i id="ka_key_field" class="el-icon-price-tag"></i>主键字段
            </template>
            <div>
              <v-column
                tableType="E-Kshow"
                v-on:reloadTableInfo="getTableInfo"
                :rowData="rowData"
                :tableInfo="keyObj.tableInfo"
              ></v-column>
            </div>
          </el-collapse-item>
          <el-collapse-item name="elcode" class="floor">
            <template slot="title">
              <i id="ka_arr_field" class="el-icon-price-tag"></i>属性字段
            </template>
            <div>
              <v-column
                tableType="E-Eshow"
                v-on:reloadTableInfo="getTableInfo"
                :rowData="rowData"
                :tableInfo="commonTableObj.tableInfo"
              ></v-column>
            </div>
          </el-collapse-item>
          <el-collapse-item name="index" class="floor">
            <template slot="title">
              <i id="ka_indexes" class="el-icon-price-tag"></i>索引
            </template>
            <div>
              <v-index :tableInfo="commonTableObj.tableInfo"></v-index>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-scrollbar>
    <!-- SQL建表 -->
    <el-dialog
      :close-on-click-modal="false"
      title="SQL建表"
      :visible.sync="handleSQLDialog"
      width="80%"
      v-dialogDrag
      append-to-body
    >
      <handle-SQl2
        @cancelHandle="handleSQLDialog = false"
        v-if="handleSQLDialog"
        :handleSQLObj="handleSQLObj"
      ></handle-SQl2>
    </el-dialog>
  </div>
</template>

<script>
/*字段管理*/
import ColumnManage from "@/views/structureManagement/overviewStorage/TableManage/ColumnManage";
/*索引*/
import indexManage from "@/views/structureManagement/overviewStorage/TableManage/IndexManage";
/*分库分表键*/
import partKeyManage from "@/views/structureManagement/overviewStorage/TableManage/PartKeyManage";
/*资料类型统计*/
import DataStatistics from "@/views/structureManagement/overviewStorage/TableManage/DataStatistics";
/*样例数据查询*/
import SampleData from "@/views/structureManagement/overviewStorage/TableManage/SampleData";
/*表信息*/
import TableInfoManage from "@/views/structureManagement/overviewStorage/TableManage/TableInfoManage";
/*外键关联*/
import ForeignKeyManage from "@/views/structureManagement/overviewStorage/TableManage/ForeignKeyManage";
// DDl
import handleSQL from "@/views/structureManagement/overviewStorage/TableManage/handleSQL";
import handleSQL2 from "@/views/structureManagement/overviewStorage/TableManage/handleSQL2";
import { getNorm, saveNorm } from "@/api/structureManagement/overviewStorage";
import { dataTableGet } from "@/api/structureManagement/materialManage/index";
export default {
  components: {
    "v-column": ColumnManage,
    "v-index": indexManage,
    "part-key": partKeyManage,
    "data-statistics": DataStatistics,
    "sample-data": SampleData,
    "table-info": TableInfoManage,
    "foreign-key": ForeignKeyManage,
    "handle-SQl": handleSQL,
    "handle-SQl2": handleSQL2,
  },
  props: { parentRowData: Object, tableBaseInfo: Object },
  data() {
    return {
      tipsFlag: true,
      rowData: this.parentRowData,
      tableActive: "db",
      activeKaCol: ["keycode", "elcode"],
      activeBaseCol: ["column"],
      activeIndexCol: ["1", "2"],

      dialogStatus: { publicMatedataDialog: false, columnDialog: false },
      keyObj: { tableInfo: {}, keyColumnData: [] },
      commonTableObj: { tableInfo: {}, ColumnData: [] },
      dirRule: { dirNorm: "" },
      isDirEdit: false,
      tabs: {
        table: { common: true, ka: false },
      },
      handleSQLDialog: false,
      handleSQLObj: {},
    };
  },
  methods: {
    showSQLManage() {
      console.log(this.commonTableObj.tableInfo);
      if (this.commonTableObj.tableInfo.id) {
        this.handleSQLDialog = true;
        this.handleSQLObj = this.commonTableObj.tableInfo;
      } else {
        this.$message({
          message: "请先完善表信息",
          type: "error",
        });
      }
    },
    findTips(val) {
      console.log(val);
      this.tipsFlag = val;
    },
    async getTableInfo(val) {
      if ((this.rowData && this.rowData.ID) || (val && val != "init")) {
        let id = "";
        if (this.rowData && this.rowData.ID) {
          id = this.rowData.ID;
        } else {
          id = val;
        }
        await dataTableGet({ id: id }).then((res) => {
          if (
            this.rowData.STORAGE_TYPE == "F_table" ||
            this.rowData.STORAGE_TYPE == "G_table" ||
            this.rowData.STORAGE_TYPE == "S_table"
          ) {
            this.tabs = {
              table: { common: false, ka: true },
            };
            let data = res.data;
            // isKvK为true的时候，是主键字段，false的时候是属性字段
            let columnArry = data.columns;
            let newarryK = [];
            let newarryE = [];
            columnArry.forEach((element, index) => {
              if (element.isKvK === true) {
                newarryK.push(element);
              } else if (element.isKvK === false) {
                newarryE.push(element);
              }
            });

            this.keyObj.tableInfo = {
              columns: newarryK,
              columnVo: data.columnVo,
              column_COMMENT: data.column_COMMENT,
              column_NAME: data.column_NAME,
              column_TYPE: data.column_TYPE,
              createBy: data.createBy,
              createTime: data.createTime,
              creator: data.creator,
              databaseId: data.databaseId,
              databaseName: data.databaseName,
              databasePid: data.databasePid,
              databaseType: data.databaseType,
              delFlag: data.delFlag,
              id: data.id,
              idx: data.idx,
              indexVo: data.indexVo,
              index_COLUMN: data.index_COLUMN,
              index_NAME: data.index_NAME,
              is_NULLABLE: data.is_NULLABLE,
              nameCn: data.nameCn,
              non_UNQIUE: data.non_UNQIUE,
              params: data.params,
              realColumns: data.realColumns,
              realTableIndex: data.realTableIndex,
              storageType: data.storageType,
              tableDesc: data.tableDesc,
              tableIndexList: data.tableIndexList,
              tableName: data.tableName,
              tableType: data.tableType,
              uk: data.uk,
              updateBy: data.updateBy,
              updateTime: data.updateTime,
              userId: data.userId,
            };
            this.commonTableObj.tableInfo = {
              columns: newarryE,
              columnVo: data.columnVo,
              column_COMMENT: data.column_COMMENT,
              column_NAME: data.column_NAME,
              column_TYPE: data.column_TYPE,
              createBy: data.createBy,
              createTime: data.createTime,
              creator: data.creator,
              databaseId: data.databaseId,
              databaseName: data.databaseName,
              databasePid: data.databasePid,
              databaseType: data.databaseType,
              delFlag: data.delFlag,
              id: data.id,
              idx: data.idx,
              indexVo: data.indexVo,
              index_COLUMN: data.index_COLUMN,
              index_NAME: data.index_NAME,
              is_NULLABLE: data.is_NULLABLE,
              nameCn: data.nameCn,
              non_UNQIUE: data.non_UNQIUE,
              params: data.params,
              realColumns: data.realColumns,
              realTableIndex: data.realTableIndex,
              storageType: data.storageType,
              tableDesc: data.tableDesc,
              tableIndexList: data.tableIndexList,
              tableName: data.tableName,
              tableType: data.tableType,
              uk: data.uk,
              updateBy: data.updateBy,
              updateTime: data.updateTime,
              userId: data.userId,
            };
          } else {
            this.tabs = {
              table: { common: true, ka: false },
            };
            this.commonTableObj.tableInfo = res.data;
            //this.commonTableObj.ColumnData = res.data.columns;
          }
        });
      } else {
        //新增
        if (
          this.tableBaseInfo.storageType == "F_table" ||
          this.tableBaseInfo.storageType == "G_table" ||
          this.tableBaseInfo.storageType == "S_table"
        ) {
          this.tabs = {
            table: { common: false, ka: true },
          };
        }
        return;
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
    manageTabsClick(val) {},
  },
  async created() {
    await this.getTableInfo("init");
  },
  mounted() {
    this.getDir();
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
    height: calc(100% - 38px);
    overflow-x: hidden;
    .el-scrollbar__wrap {
      overflow-x: hidden;
    }
  }
  .el-main {
    padding: 0 10px;
  }
  .structureManageTable {
    .el-main {
      padding: 0;
    }
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
  .el-alert {
    margin-bottom: 3px;
  }
}
</style>
