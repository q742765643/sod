<template>
  <div class="scrollMain" @scroll="scrollEvent">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-tabs v-model="tableActive" @tab-click="manageTabsClick">
        <el-tab-pane v-if="tabs.db" label="存储结构" name="db">
          <el-container class="structureManageTable">
            <el-main id="box">
              <el-form
                v-if="this.rowData.STORAGE_TYPE=='ME_table'||this.rowData.STORAGE_TYPE=='MK_table'||this.rowData.STORAGE_TYPE=='NF_table'"
                v-model="dirRule"
                class="elementDir"
                label-width="100px"
              >
                <el-row>
                  <el-col :span="20">
                    <el-form-item label="目录规范">
                      <el-input
                        :disabled="!isDirEdit"
                        placeholder="目录规范"
                        size="small"
                        v-model="dirRule.catalog"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-form-item label class="buttonCon">
                      <el-button v-if="!isDirEdit" type="primary" size="small" @click="editDir">编辑</el-button>
                      <el-button v-if="isDirEdit" type="primary" size="small" @click="saveDir">保存</el-button>
                      <el-button v-if="isDirEdit" type="primary" size="small" @click="celDir">取消</el-button>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
              <div v-if="tabs.table.key" class="el-main">
                <div class="elementTableTitle">
                  <i id="key_table_flag" class="el-icon-postcard"></i>键表
                </div>
                <table-info
                  class="floor"
                  v-if="tabs.table.key"
                  :tableInfo="keyObj.tableInfo"
                  :rowData="rowData"
                  tableType="K"
                  v-on:reloadTableInfo="getTableInfo"
                ></table-info>
                <el-collapse class="collapseCon el-col el-col-24">
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="key_field" class="el-icon-price-tag"></i>字段
                    </template>
                    <div>
                      <v-column
                        v-on:reloadTableInfo="getTableInfo"
                        v-if="tabs.table.key"
                        :tableInfo="keyObj.tableInfo"
                      ></v-column>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="key_indexes" class="el-icon-price-tag"></i>索引
                    </template>
                    <div>
                      <v-index v-if="tabs.table.key" :tableInfo="keyObj.tableInfo"></v-index>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="key_tableKey" class="el-icon-price-tag"></i>分库分表键
                    </template>
                    <div>
                      <part-key
                        v-if="tabs.table.key"
                        :tableInfo="keyObj.tableInfo"
                        :rowData="rowData"
                      ></part-key>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="key_materialStatics" class="el-icon-price-tag"></i>资料类型统计
                    </template>
                    <div>
                      <data-statistics
                        v-if="tabs.table.key"
                        :tableInfo="keyObj.tableInfo"
                        :rowData="rowData"
                      ></data-statistics>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="key_exampleSearch" class="el-icon-price-tag"></i>样例数据查询
                    </template>
                    <div>
                      <sample-data
                        v-if="tabs.table.key"
                        :tableInfo="keyObj.tableInfo"
                        :rowData="rowData"
                      ></sample-data>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
              <div v-if="tabs.table.el" class="el-main">
                <div class="elementTableTitle">
                  <i id="el_table_flag" class="el-icon-postcard"></i>要素表
                </div>
                <table-info
                  class="floor"
                  v-if="tabs.table.el"
                  :tableInfo="elObj.tableInfo"
                  :rowData="rowData"
                  tableType="E"
                  v-on:reloadTableInfo="getTableInfo"
                ></table-info>
                <el-collapse class="collapseCon el-col el-col-24">
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="el_field" class="el-icon-price-tag"></i>字段
                    </template>
                    <div>
                      <v-column
                        v-on:reloadTableInfo="getTableInfo"
                        v-if="tabs.table.el"
                        :tableInfo="elObj.tableInfo"
                      ></v-column>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="el_indexes" class="el-icon-price-tag"></i>索引
                    </template>
                    <div>
                      <v-index v-if="tabs.table.el" :tableInfo="elObj.tableInfo"></v-index>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="el_tableKey" class="el-icon-price-tag"></i>分库分表键
                    </template>
                    <div>
                      <part-key
                        v-if="tabs.table.el"
                        :tableInfo="elObj.tableInfo"
                        :rowData="rowData"
                      ></part-key>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="el_materialStatics" class="el-icon-price-tag"></i>资料类型统计
                    </template>
                    <div>
                      <data-statistics
                        v-if="tabs.table.el"
                        :tableInfo="elObj.tableInfo"
                        :rowData="rowData"
                      ></data-statistics>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item class="floor">
                    <template slot="title">
                      <i id="el_exampleSearch" class="el-icon-price-tag"></i>样例数据查询
                    </template>
                    <div>
                      <sample-data
                        v-if="tabs.table.el"
                        :tableInfo="elObj.tableInfo"
                        :rowData="rowData"
                      ></sample-data>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
              <div v-if="tabs.table.key_el" class="el-main">
                <div class="elementTableTitle">
                  <i class="el-icon-postcard"></i>外键关联
                </div>
                <div>
                  <foreign-key
                    v-if="tabs.table.key_el"
                    :keyTableInfo="keyObj.tableInfo"
                    :elTableInfo="elObj.tableInfo"
                    :rowData="rowData"
                  ></foreign-key>
                </div>
              </div>
              <div v-if="tabs.table.ka" class="el-main">
                <table-info
                  v-if="tabs.table.ka"
                  :tableInfo="elObj.tableInfo"
                  :rowData="rowData"
                  tableType="E-show"
                  v-on:reloadTableInfo="getTableInfo"
                ></table-info>
                <el-collapse class="collapseCon el-col el-col-24">
                  <el-collapse-item name="keycode">
                    <template slot="title">
                      <i id="ka_key_field" class="el-icon-price-tag"></i>主键字段
                    </template>
                    <div>
                      <v-column
                        v-on:reloadTableInfo="getTableInfo"
                        v-if="tabs.table.ka"
                        tableType="E-show"
                        :tableInfo="elObj.tableInfo"
                      ></v-column>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item name="elcode">
                    <template slot="title">
                      <i id="ka_arr_field" class="el-icon-price-tag"></i>属性字段
                    </template>
                    <div>
                      <v-column
                        v-on:reloadTableInfo="getTableInfo"
                        v-if="tabs.table.ka"
                        :tableInfo="elObj.tableInfo"
                      ></v-column>
                    </div>
                  </el-collapse-item>
                  <el-collapse-item name="index">
                    <template slot="title">
                      <i id="ka_indexes" class="el-icon-price-tag"></i>索引
                    </template>
                    <div>
                      <v-index v-if="tabs.table.ka" :tableInfo="elObj.tableInfo"></v-index>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-main>
            <el-aside>
              <ul class="rightNav" id="rightNav">
                <li v-if="tabs.table.key" :class="keyActive">
                  <a href="#key_table_flag">键表</a>
                </li>
                <li v-if="tabs.table.key">
                  <a href="#key_field">字段</a>
                </li>
                <li v-if="tabs.table.key">
                  <a href="#key_indexes">索引</a>
                </li>
                <li v-if="tabs.table.key">
                  <a href="#key_tableKey">分库分表键</a>
                </li>
                <li v-if="tabs.table.key">
                  <a href="#key_materialStatics">资料类型统计</a>
                </li>
                <li v-if="tabs.table.key">
                  <a href="#key_exampleSearch">样例数据查询</a>
                </li>
                <li v-if="tabs.table.el" :class="elActive">
                  <a href="#el_table_flag">要素表</a>
                </li>
                <li v-if="tabs.table.el">
                  <a href="#el_field">字段</a>
                </li>
                <li v-if="tabs.table.el">
                  <a href="#el_indexes">索引</a>
                </li>
                <li v-if="tabs.table.el">
                  <a href="#el_tableKey">分库分表键</a>
                </li>
                <li v-if="tabs.table.el">
                  <a href="#el_materialStatics">资料类型统计</a>
                </li>
                <li v-if="tabs.table.el">
                  <a href="#el_exampleSearch">样例数据查询</a>
                </li>
              </ul>
            </el-aside>
          </el-container>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.dc" label="解码配置" name="dc">
          <decoding-config :rowData="rowData"></decoding-config>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.rg" label="区域信息" name="rg">
          <area-config ref="areaRef" :rowData="rowData"></area-config>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.ds" label="数据服务" name="ds">
          <serve-config ref="serveRef" :rowData="rowData"></serve-config>
        </el-tab-pane>
        <el-tab-pane v-if="tabs.pl" label="产品列表" name="pl">
          <product-config ref="productRef" :rowData="rowData"></product-config>
        </el-tab-pane>
      </el-tabs>
      <el-backtop target=".el-dialog.is-fullscreen"></el-backtop>
    </el-scrollbar>
  </div>
</template>

<script>
/*字段管理*/
import ColumnManage from "@/views/structureManagement/tableStructureManage/TableManage/ColumnManage";
/*索引*/
import indexManage from "@/views/structureManagement/tableStructureManage/TableManage/IndexManage";
/*分库分表键*/
import partKeyManage from "@/views/structureManagement/tableStructureManage/TableManage/PartKeyManage";
/*资料类型统计*/
import DataStatistics from "@/views/structureManagement/tableStructureManage/TableManage/DataStatistics";
/*样例数据查询*/
import SampleData from "@/views/structureManagement/tableStructureManage/TableManage/SampleData";
/*表信息*/
import TableInfoManage from "@/views/structureManagement/tableStructureManage/TableManage/TableInfoManage";
/*外键关联*/
import ForeignKeyManage from "@/views/structureManagement/tableStructureManage/TableManage/ForeignKeyManage";
/*解码配置*/
import DecodingConfigManage from "@/views/structureManagement/tableStructureManage/TableManage/DecodingConfigManage";
/* 区域信息 */
import areaConfigManage from "@/views/structureManagement/tableStructureManage/TableManage/gridAreaDefine";
/* 产品列表 */
import productConfigManage from "@/views/structureManagement/tableStructureManage/TableManage/productList";
// 数据服务
import serveConfigManage from "@/views/structureManagement/tableStructureManage/TableManage/dataServeManage";
import { gcl } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
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
    "serve-config": serveConfigManage
  },
  props: { parentRowData: Object },
  data() {
    return {
      elActive: "",
      keyActive: "",
      storage: {
        //表类型展示配置
        E_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false
        },
        F_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true
        },
        ME_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false
        },
        G_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true
        },
        K_E_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: false,
          rg: false,
          ds: false,
          pl: false
        },
        MK_table: {
          table: { el: true, key: true, ka: false, key_el: true },
          db: true,
          dc: true,
          rg: true,
          ds: true,
          pl: false
        },
        NF_table: {
          table: { el: true, key: false, ka: false, key_el: false },
          db: true,
          dc: false,
          rg: true,
          ds: false,
          pl: true
        },
        S_table: {
          table: { el: false, key: false, ka: true, key_el: false },
          db: true,
          dc: false,
          ds: false,
          pl: true,
          rg: true
        }
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
        pl: false
      },
      keyObj: { tableInfo: {}, keyColumnData: [] },
      elObj: { tableInfo: {}, elColumnData: [] },
      pubOptions: {},
      dirRule: { catalog: "" },
      isDirEdit: false
    };
  },
  methods: {
    getTableInfo() {
      //获取表信息
      this.tabs = this.storage[this.rowData.STORAGE_TYPE];
      if (this.tabs.table.key) {
        this.keyActive = "active";
      } else if (this.tabs.table.el) {
        this.elActive = "active";
      }
      gcl({ classLogic: this.rowData.LOGIC_ID }).then(response => {
        if (response.code == 200) {
          console.log(response.data);
          let data = response.data;
          for (let i = 0; i < data.length; i++) {
            let row = data[i];
            if (row.dbTableType && "E" === row.dbTableType) {
              this.elObj.tableInfo = row;
            } else if (row.dbTableType && "K" === row.dbTableType) {
              this.keyObj.tableInfo = row;
            }
          }
          if (data.length == 0) {
            let obj = {
              nameCn: this.rowData.CLASS_NAME,
              dataServiceId: this.rowData.DATA_CLASS_ID
            };
            this.elObj.tableInfo = obj;
            this.keyObj.tableInfo = obj;
          }
        }
      });
    },
    getOptionsData() {
      //获取下拉选项数据
      // this.axios
      //   .get(interfaceObj.TableStructure_getOptionsByType, {
      //     params: { type: 2 }
      //   })
      //   .then(res => {
      //     this.pubOptions.dataTypes = res.data.data;
      //   })
      //   .catch(error => {});
    },
    elColumnEdit() {
      //点击要素字典编辑
      if (this.elObj.elColumnSel.length != 1) {
        this.$message({
          message: "请选择一条数据！",
          type: "warning"
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
          type: "warning"
        });
      } else {
        this.columnEditData = JSON.parse(
          JSON.stringify(this.keyObj.keyColumnSel[0])
        );
        this.dialogStatus.columnDialog = true;
      }
    },
    elColumnEditSave() {
      //保存字段信息
    },
    editDir() {
      this.isDirEdit = !this.isDirEdit;
    },
    saveDir() {
      this.isDirEdit = !this.isDirEdit;
      this.axios
        .post(interfaceObj.TableStructure_addDirRule, {
          data_class_id: this.rowData.data_class_id,
          catalog: this.dirRule.catalog
        })
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              type: "success",
              message: "操作成功"
            });
          }
        })
        .catch(error => {
          console.log("出错了，出错信息：" + error);
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
        this.axios
          .get(interfaceObj.TableStructure_getDirRule, {
            params: {
              data_class_id: this.rowData.data_class_id
            }
          })
          .then(res => {
            if (res.data.returnCode == 0) {
              this.dirRule.catalog = res.data.data.area_name;
            }
          })
          .catch(error => {
            console.log("出错了，出错信息：" + error);
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
    handleScroll() {
      var sideNav = document.getElementById("rightNav");
      var btns = sideNav.getElementsByTagName("li"); //按钮

      //2.点击按钮可以跳转到对应楼层
      for (var i = 0; i < btns.length; i++) {
        //绑定索引
        btns[i].index = i;
        btns[i].onclick = function() {
          //                      console.log(this.index);
          //排他
          for (var j = 0; j < btns.length; j++) {
            btns[j].className = "";
          }
          this.className = "active";
          // var ih = lous[this.index].offsetTop;
          // window.scrollTo(0, ih);
        };
      }
    },
    scrollEvent(e) {
      // 滚动滑轮，到达临界点的时候，按钮跟着高亮显示
      // console.log(e.srcElement.scrollTop);
      var scrollTop = e.srcElement.scrollTop; //滚动距离
      var box = document.getElementById("box");
      var lous = box.getElementsByClassName("floor"); //楼层
      var sideNav = document.getElementById("rightNav");
      var btns = sideNav.getElementsByTagName("li"); //按钮
      for (var i = 0; i < lous.length; i++) {
        if (scrollTop >= lous[i].offsetTop) {
          // console.log(i);
          //排他 设置高亮
          for (var j = 0; j < btns.length; j++) {
            btns[j].className = "";
          }
          btns[i].className = "active";
        }
      }
    }
  },
  mounted() {
    this.getTableInfo();
    // this.getOptionsData();
    // this.getDir();
    // this.handleScroll();
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.scrollEvent);
  }
};
</script>

<style lang="scss">
.scrollMain {
  position: absolute;
  top: 70px;
  bottom: 0;
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
