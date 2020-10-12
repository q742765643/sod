<template>
  <el-main class="TableInfoManage">
    <el-form v-model.trim="Info" class="elementTableCon" label-width="100px">
      <div class="el-row">
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
        <div class="el-col el-col-3" v-if="isTableName">
          <el-form-item class="buttonCon">
            <el-button type="primary" size="small" @click="help"
              >帮助</el-button
            >
          </el-form-item>
        </div>
        <div class="el-col el-col-10">
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
        <div class="el-col el-col-4">
          <el-form-item class="buttonCon">
            <el-button v-if="!isEdit" type="primary" size="small" @click="edit"
              >编辑</el-button
            >
            <el-popover
              v-if="!isEdit"
              placement="bottom"
              width="240"
              trigger="hover"
              popper-class="tableMsgPop"
            >
              <div>
                <ul>
                  <li>
                    <p>
                      <span>{{ "四级编码:" }}</span>
                      <span>{{ this.rowData.D_DATA_ID }}</span>
                    </p>
                  </li>
                  <li>
                    <p>
                      <span>{{ "用途描述:" }}</span>
                      <span>{{ this.rowData.LOGIC_NAME }}</span>
                    </p>
                  </li>
                  <li>
                    <p>
                      <span>{{ "数据库名称:" }}</span>
                      <span>{{ this.rowData.DATABASE_NAME_F }}</span>
                    </p>
                  </li>
                  <li>
                    <p>
                      <span>{{ "表类型:" }}</span>
                      <span>{{ this.rowData.DICT_LABEL }}</span>
                    </p>
                  </li>
                </ul>
              </div>
              <el-button
                class="tipsBtn"
                type="primary"
                size="small"
                slot="reference"
                icon="el-icon-tickets"
              ></el-button>
            </el-popover>
            <el-button v-if="isEdit" type="primary" size="small" @click="save"
              >保存</el-button
            >
            <el-button v-if="isEdit" type="primary" size="small" @click="cel"
              >取消</el-button
            >
          </el-form-item>
        </div>
      </div>
      <div class="el-row">
        <div class="el-col el-col-10">
          <el-form-item label="服务编码">
            <el-input
              :disabled="true"
              placeholder="服务编码"
              size="small"
              v-model.trim="Info.dataServiceId"
            ></el-input>
          </el-form-item>
        </div>
        <div class="el-col el-col-10">
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
            <el-button
              type="primary"
              size="small"
              @click="baseMsgEdit"
              v-if="tableStructureManageContral"
              >基础信息编辑</el-button
            >
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
    <el-dialog
      :close-on-click-modal="false"
      v-dialogDrag
      title="基础信息编辑"
      :visible.sync="baseMsgEditDialog"
      width="80%"
      :before-close="handleClose"
      append-to-body
    >
      <handleBaseMsg
        @handleClose="handleClose"
        v-if="baseMsgEditDialog"
        :handleId="handleId"
      ></handleBaseMsg>
    </el-dialog>
  </el-main>
</template>

<script>
//接口地址
// 基础信息编辑
import { enable } from "@/api/structureManagement/tableStructureManage/index";
import handleBaseMsg from "@/views/structureManagement/tableStructureManage/TableManage/handleBaseMsg";
import { dataTableSavle } from "@/api/structureManagement/tableStructureManage/StructureManageTable";
import { tableNameVail } from "@/components/commonVaildate.js";
export default {
  name: "TableInfoManage",
  components: {
    handleBaseMsg,
  },
  props: {
    rowData: Object,
    tableInfo: Object,
    tableType: String,
  },
  data() {
    return {
      tableStructureManageContral: false,
      Info: {
        table_name: "",
        name_cn: "",
        dataServiceId: "",
        tableDesc: "",
      },
      childTableType: this.tableType,
      namehelp: "",
      isEdit: false,
      isTableName: false,
      infoList: [],
      tableClass: "el-col el-col-10",
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
  created() {
    enable().then((res) => {
      if (res.data == "true") {
        this.tableStructureManageContral = true;
      } else {
        this.tableStructureManageContral = false;
      }
    });
  },
  mounted() {
    this.Info = JSON.parse(JSON.stringify(this.tableInfo));
    this.isHelp();
  },
  methods: {
    isHelp() {
      if (!this.Info.nameCn) {
        this.Info.nameCn = this.rowData.CLASS_NAME;
        this.Info.dataServiceId = this.rowData.D_DATA_ID;
      }
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
      let saveObj = {};
      saveObj.classLogic = this.Info.classLogic;
      if (!saveObj.classLogic) {
        saveObj.classLogic = {
          id: this.rowData.LOGIC_ID,
        };
      }
      saveObj.tableName = this.Info.tableName;
      saveObj.nameCn = this.Info.nameCn;
      saveObj.dataServiceId = this.Info.dataServiceId;
      saveObj.tableDesc = this.Info.tableDesc;
      saveObj.version = this.Info.version;
      saveObj.id = this.Info.id;
      saveObj.createTime = this.Info.createTime;
      saveObj.dataServiceName = this.rowData.CLASS_NAME;
      if (this.tableType == "E-Kshow") {
        saveObj.dbTableType = this.childTableType;
      } else {
        saveObj.dbTableType = this.tableType;
      }
      dataTableSavle(saveObj).then((response) => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "操作成功",
          });
          this.$emit("reloadTableInfo");
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
    // 基础信息编辑
    baseMsgEdit() {
      this.handleId = this.Info.dataServiceId;
      this.baseMsgEditDialog = true;
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
      if (this.tableType == "E-Kshow") {
        this.childTableType = "E";
        this.namehelp = "show";
      }
      if (this.namehelp == "show") {
        this.tableClass = "el-col el-col-7";
        this.isTableName = true;
      } else {
        this.tableClass = "el-col el-col-10";
        this.isTableName = false;
      }
    },
  },
};
</script>

<style lang='scss'>
.TableInfoManage {
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
