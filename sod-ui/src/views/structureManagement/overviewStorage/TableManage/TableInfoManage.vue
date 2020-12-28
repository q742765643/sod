<template>
  <el-main class="TableInfoManage">
    <el-form
      v-model="tableBaseInfo"
      label-width="100px"
      class="elementTableCon"
      style="border-bottom: none"
    >
      <div class="el-row">
        <div class="el-col el-col-5">
          <el-form-item label="数据库">
            <el-select
              size="small"
              disabled
              v-model="tableBaseInfo.databasePid"
              @change="handleFindSpec"
            >
              <el-option
                v-for="(citem, cindex) in DatabaseDefineList"
                :key="cindex"
                :label="citem.databaseName"
                :value="citem.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div class="el-col el-col-5">
          <el-form-item label="专题库">
            <el-select
              size="small"
              disabled
              v-model="tableBaseInfo.databaseId"
              placeholder="请选择"
            >
              <el-option
                v-for="(citem, cindex) in specialList"
                :key="cindex"
                :label="citem.databaseName"
                :value="citem.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div class="el-col el-col-5">
          <el-form-item label="存储类型">
            <el-select
              size="small"
              disabled
              v-model="tableBaseInfo.storageType"
              placeholder="请选择"
            >
              <el-option
                v-for="(citem, cindex) in storageTypeList"
                :key="cindex"
                :label="citem.dictLabel"
                :value="citem.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
        </div>
      </div>
    </el-form>
    <el-form v-model.trim="Info" class="elementTableCon" label-width="100px">
      <div class="el-row">
        <div class="el-col el-col-5">
          <el-form-item label="表类型">
            <el-select
              :disabled="!isEdit"
              v-model="Info.tableType"
              placeholder="请选择"
            >
              <el-option label="要素表" value="E"></el-option>
              <el-option label="键表" value="K"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div :class="tableClass">
          <el-form-item label="表名称">
            <el-input
              clearable
              :disabled="!isEdit"
              placeholder="表名称"
              size="small"
              v-model.trim="Info.tableName"
            ></el-input>
          </el-form-item>
        </div>
        <div class="el-col el-col-2" v-if="isTableName">
          <el-form-item class="buttonCon">
            <el-button type="primary" size="small" @click="help"
              >帮助</el-button
            >
          </el-form-item>
        </div>
        <div class="el-col el-col-5">
          <el-form-item label="中文名称">
            <el-input
              clearable
              :disabled="!isEdit"
              placeholder="中文名称"
              size="small"
              v-model.trim="Info.nameCn"
            ></el-input>
          </el-form-item>
        </div>
        <div class="el-col el-col-5">
          <el-form-item label="备注">
            <el-input
              clearable
              :disabled="!isEdit"
              placeholder="备注"
              size="small"
              v-model.trim="Info.tableDesc"
            ></el-input>
          </el-form-item>
        </div>
        <div class="el-col el-col-4">
          <el-form-item class="buttonCon">
            <el-button v-if="!isEdit" type="primary" size="small" @click="edit"
              >编辑</el-button
            >
            <el-button v-if="isEdit" type="primary" size="small" @click="save"
              >保存</el-button
            >
            <el-button v-if="isEdit" type="primary" size="small" @click="cel"
              >取消</el-button
            >
          </el-form-item>
        </div>
        <div class="el-col el-col-24" v-if="linkMateriaTableData.length > 0">
          <el-form-item label="关联资料">
            <el-table :data="linkMateriaTableData" style="width: 100%">
              <el-table-column prop="CLASS_NAME" label="资料名" align="center">
              </el-table-column>
              <el-table-column
                prop="DATA_CLASS_ID"
                label="四级编码"
                align="center"
              >
              </el-table-column>
              <el-table-column prop="D_DATA_ID" label="存储编码" align="center">
              </el-table-column>
            </el-table>
          </el-form-item>
        </div>
      </div>
    </el-form>
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="帮助"
      :visible.sync="dialogVisible"
      width="60%"
      :before-close="handleClose"
      append-to-body
    >
      <div class="helpbox">
        <h4 style="color: #f56c6c">
          表名称如：nafp_for_ftm_[资料标识1]_[精度2]_[区域3]_[场类型4]_[加工过程类型5]
        </h4>
        <p class="title">1.资料标识:</p>
        <span>资料标识表示资料的类别，应能方便的辨别资料的类型；</span>
        <p class="title">2.精度:</p>
        <span>精度表示高分或低分，高分用high表示，低分用low表示；</span>
        <p class="title">3.区域:</p>
        <span>区域表示资料的数据场标识的空间范围，如：GLB，CHN；</span>
        <p class="title">4.场类型:</p>
        <span
          >数值模式产品按场类型（FIELD_TYPE）分为多种不同的产品，场类型的取值和对应含义如下表所示：</span
        >
        <el-table border :data="tableData1" style="width: 100%">
          <el-table-column prop="code" label="代码值"></el-table-column>
          <el-table-column prop="means" label="含义"></el-table-column>
          <el-table-column prop="code2" label="代码值"></el-table-column>
          <el-table-column prop="means2" label="含义"></el-table-column>
        </el-table>
        <span
          >当场类型为1时是预报产品，为其它时不是正常的预报产品。比如，EC低分的场类型为2的气温、位势高度等要素产品，其实是标准差产品，不是预报产品。</span
        >
        <p class="title">5.加工过程类型:</p>
        <span>模式产品加工类型（GENPROCESS_TYPE字段）的含义如下：</span>
        <el-table border :data="tableData2" style="width: 100%">
          <el-table-column prop="code" label="代码值"></el-table-column>
          <el-table-column prop="means" label="含义"></el-table-column>
          <el-table-column prop="code2" label="代码值"></el-table-column>
          <el-table-column prop="means2" label="含义"></el-table-column>
          <el-table-column prop="code3" label="代码值"></el-table-column>
          <el-table-column prop="means3" label="含义"></el-table-column>
        </el-table>
        <span
          >比如，当检索到GENPROCESS_TYPE=2时，是预报产品；GENPROCESS_TYPE=4时，是集合预报产品。</span
        >
        <div
          slot="footer"
          class="dialog-footer"
          style="text-align: right; padding-bottom: 20px"
        >
          <el-button type="primary" @click="dialogVisible = false" size="small"
            >确 定</el-button
          >
        </div>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
//接口地址
import { dataTableSavle } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { tableNameVail } from "@/components/commonVaildate.js";
import {
  getCanShowDatabaseDefineList,
  findByDatabaseDefineId,
} from "@/api/structureManagement/materialManage/index";
import {
  getClassByTableId,
  existTable,
} from "@/api/structureManagement/overviewStorage";
export default {
  name: "TableInfoManage",
  components: {},
  props: {
    rowData: Object,
    tableInfo: Object,
    tableType: String,
    TBInfo: Object,
  },
  data() {
    return {
      linkMateriaTableData: [],
      tableBaseInfo: {},
      storageTypeList: [],
      DatabaseDefineList: [],
      specialList: [],
      Info: {
        tableName: "",
        nameCn: "",
        tableDesc: "",
      },
      childTableType: this.tableType,
      namehelp: "",
      isEdit: false,
      isTableName: false,
      infoList: [],
      tableClass: "el-col el-col-5",
      dialogVisible: false,
      baseMsgEditDialog: false,
      handleObj: "",
      tableData1: [
        {
          code: "0",
          means: "分析产品",
          code2: "1",
          means2: "预报产品",
        },
        {
          code: "2",
          means: "分析和预报产品",
          code2: "3",
          means2: "控制预报产品",
        },
        {
          code: "4",
          means: "扰动预报产品",
          code2: "5",
          means2: "控制和扰动预报产品",
        },
        {
          code: "6",
          means: "加工的卫星观测资料",
          code2: "7",
          means2: "加工的雷达观测资料",
        },
        {
          code: "8",
          means: "事件概率",
          code2: "9-191",
          means2: "保留",
        },
        {
          code: "192—254",
          means: "保留，供本地使用",
          code2: "255",
          means2: "空缺",
        },
      ],
      tableData2: [
        {
          code: "0",
          means: "分析",
          code2: "1",
          means2: "初始化",
          code3: "2",
          means3: "预报",
        },
        {
          code: "3",
          means: "偏差订正预报",
          code2: "4",
          means2: "集合预报",
          code3: "5",
          means3: "概率预报",
        },
        {
          code: "6",
          means: "预报误差",
          code2: "7",
          means2: "分析误差",
          code3: "8",
          means3: "观测",
        },
        {
          code: "9",
          means: "气候",
          code2: "10",
          means2: "加权概率预报",
          code3: "11",
          means3: "偏差订正集合预报",
        },
        {
          code: "12—191",
          means: "保留",
          code2: "192—254",
          means2: "保留，供本地使用",
          code3: "255",
          means3: "空缺值",
        },
      ],
    };
  },
  async created() {
    await this.getDicts("sys_storage_type").then((response) => {
      this.storageTypeList = response.data;
    });
    await getCanShowDatabaseDefineList().then((res) => {
      this.DatabaseDefineList = res.data;
    });
    if (this.TBInfo) {
      this.tableBaseInfo = this.TBInfo;
      this.handleFindSpec(this.tableBaseInfo.databasePid);
    }
  },
  mounted() {
    this.Info = JSON.parse(JSON.stringify(this.tableInfo));
    console.log(this.Info);
    getClassByTableId({ tableId: this.rowData.ID }).then((res) => {
      this.linkMateriaTableData = res.data;
    });
  },
  methods: {
    //切换数据库查询专题库
    handleFindSpec(val) {
      findByDatabaseDefineId({ id: val }).then((res) => {
        this.specialList = res.data;
      });
    },

    reloadTableInfo() {
      this.$emit("reloadTableInfo");
    },
    edit() {
      this.isEdit = !this.isEdit;
    },
    save() {
      this.isEdit = !this.isEdit;
      if (!this.Info.tableName || !tableNameVail(this.Info.tableName)) {
        this.$alert(
          "表名不能为空，表名首位允许是字母以及下划线；首位之后可以是字母，数字以及下划线；下划线后不能接下划线",
          "温馨提示",
          {
            type: "warning",
            confirmButtonText: "确定",
            callback: (action) => {},
          }
        );
        return;
      }
      let saveObj = {
        databasePid: this.tableBaseInfo.databasePid,
        databaseId: this.tableBaseInfo.databaseId,
        storageType: this.tableBaseInfo.storageType,
        tableName: this.Info.tableName,
        nameCn: this.Info.nameCn,
        tableDesc: this.Info.tableDesc,
        tableType: this.Info.tableType,
      };
      console.log(saveObj);
      dataTableSavle(saveObj).then((response) => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功",
          });
          this.$emit("reloadTableInfo", response.data.id);
        } else {
          this.$message({
            type: "error",
            message: "操作失败",
          });
        }
      });
    },
    cel() {
      this.isEdit = !this.isEdit;
      this.Info = JSON.parse(JSON.stringify(this.tableInfo));
    },
    // 帮助
    help() {
      this.dialogVisible = true;
    },

    handleClose() {
      this.dialogVisible = false;
      this.baseMsgEditDialog = false;
    },
  },
  watch: {
    tableInfo(val) {
      this.Info = JSON.parse(JSON.stringify(val));
      console.log(this.Info);
      if (!this.tableBaseInfo.databasePid) {
        this.tableBaseInfo = {
          databasePid: this.Info.databasePid,
          databaseId: this.Info.databaseId,
          storageType: this.Info.storageType,
        };
        this.handleFindSpec(this.tableBaseInfo.databasePid);
        console.log(this.tableBaseInfo);
      }
      existTable({
        databaseId: this.Info.databaseId,
        tableName: this.Info.tableName,
      }).then((res) => {
        this.$emit("findTips", res.data);
      });
      if (this.tableType == "E-Kshow") {
        this.childTableType = "E";
        this.namehelp = "show";
      }
      if (this.namehelp == "show") {
        this.tableClass = "el-col el-col-3";
        this.isTableName = true;
      } else {
        this.tableClass = "el-col el-col-5";
        this.isTableName = false;
      }
    },
  },
};
</script>

<style lang='scss'>
.TableInfoManage {
  .el-select {
    width: 100%;
  }
  padding-top: 0;
  .buttonCon {
    .el-form-item__content {
      margin-left: 20px !important;
    }
  }
}
.helpbox {
  h4 {
    line-height: 30px;
    font-size: 20px;
  }
  p,
  span {
    line-height: 20px;
  }
  .title {
    font-weight: bold;
  }
}
.tableMsgPop {
  background-color: #409eff;
  border: none;
  padding: 12px;
  margin-top: 0px !important;
  color: #fff;
  .popper__arrow::after {
    border-bottom-color: #409eff !important;
  }
}
.tipsBtn {
  margin-left: 10px;
}
</style>
