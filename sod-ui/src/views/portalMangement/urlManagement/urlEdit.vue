<template>
  <section class="urlEditDialog">
    <el-form :model="msgFormDialog" ref="fromRef" :rules="baseFormRules" label-width="120px">
      <el-tabs type="border-card">
        <el-tab-pane label="接口基本信息">
          <el-form-item prop="apiName" label="接口名称:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.apiName"
              placeholder="请输入接口名称 , 例如 : getElementById"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
          <el-form-item prop="apiDesc" label="接口描述:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.apiDesc"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
          <el-form-item prop="apiSys" label="接口分类:">
            <el-select v-model="msgFormDialog.apiSys">
              <el-option
                :label="item.dictLabel"
                :value="item.dictValue"
                v-for="(item,index) in urlClassify"
                :key="index"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="apiHttptype" label="请求方式:">
            <el-select v-model="msgFormDialog.apiHttptype">
              <el-option label="GET/POST" value="A"></el-option>
              <el-option label="GET" value="G"></el-option>
              <el-option label="POST" value="P"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="apiExample" label="调用样例:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.apiExample"
              placeholder="例如 : http://10.20.67.179:8888/MMD/api/rest/intf/fetch/normal?userId=xxx&pwd=xxx&interfaceId=getElementById"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
          <el-form-item prop="serialNumber" label="排序:">
            <el-input-number v-model.trim="msgFormDialog.serialNumber" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item prop="remark" label="备注:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.remark"
              type="textarea"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="接口参数信息">
          <el-card shadow="never">
            <div slot="header" class="clearfix">
              <span>公共参数</span>
            </div>
            <el-table border :data="publictableData" row-key="id">
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="paramName" label="参数名称"></el-table-column>
              <el-table-column prop="desc" label="参数描述"></el-table-column>
              <el-table-column prop="paramType" label="参数类型"></el-table-column>
              <el-table-column prop="need" label="是否必填">
                <template slot-scope="scope">
                  <span v-if="scope.row.need=='Y'">是</span>
                  <span v-else>否</span>
                </template>
              </el-table-column>
              <el-table-column prop="orders" label="排序编号"></el-table-column>
            </el-table>
          </el-card>

          <el-card shadow="never" style="margin-top:12px;">
            <div slot="header" class="clearfix">
              <span>其他参数</span>
              <el-button
                size="small"
                type="primary"
                style="float:right;"
                @click="handleParameter('add')"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="othertableData"
              row-key="id"
              @selection-change="paramDSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="paramName" label="参数名称">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.paramName" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="paramDesc" label="参数描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.paramDesc" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="paramType" label="参数类型">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.paramType" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="isneed" label="是否必填">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.isneed">
                    <el-option label="否" value="N"></el-option>
                    <el-option label="是" value="Y"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="serialNumber" label="排序编号">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.serialNumber" size="small"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleParameter('add')"
                    icon="el-icon-plus"
                  >新增</el-button>
                  <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="text"
                    @click="deleteParameter(scope.row)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="接口返回值信息">
          <el-card shadow="never">
            <div slot="header" class="clearfix">
              <span>接口返回值信息</span>
              <el-button
                size="small"
                type="primary"
                style="float:right;"
                @click="handleReturnMsg('add')"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="apiResDtos"
              row-key="id"
              @selection-change="returnSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="resName" label="返回值名称">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.resName" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="resDesc" label="返回值描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.resDesc" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="resType" label="返回值类型">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.resType" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="serialNumber" label="排序编号">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.serialNumber" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleReturnMsg('add')"
                    icon="el-icon-plus"
                  >新增</el-button>
                  <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="text"
                    @click="deleteReturnMsg(scope.row,index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="样例代码">
          <el-card shadow="never">
            <div slot="header" class="clearfix">
              <span>样例代码</span>
              <el-button
                size="small"
                type="primary"
                style="float:right;"
                @click="handleDemoMsg('add')"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="apiCodeDtos"
              row-key="id"
              @selection-change="demoSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="codeLang" label="编程语言" width="150"></el-table-column>
              <el-table-column prop="useType" label="调用方式" width="150"></el-table-column>
              <el-table-column prop="resType" label="返回值格式" width="100"></el-table-column>
              <el-table-column prop="serialNumber" label="排序编号" width="150"></el-table-column>
              <el-table-column prop="paramCode" label="参数示例" width="200"></el-table-column>
              <el-table-column prop="useCode" label="调用示例" width="200"></el-table-column>
              <el-table-column prop="resCode" label="返回值示例" width="200"></el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    @click="handleDemoMsg(scope.row)"
                    icon="el-icon-edit"
                  >编辑</el-button>
                  <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="text"
                    @click="deleteDemoMsg(scope.row,index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <div slot="footer" class="dialog-footer" style="margin-top:20px;">
      <el-button type="primary" @click="trueDialog('fromRef')">确 定</el-button>
      <el-button @click="cancelDialog()">取 消</el-button>
    </div>
    <!-- 弹窗-->
    <el-dialog
      width="600px"
      append-to-body
      :close-on-click-modal="false"
      title="样例代码信息"
      :visible.sync="exportDialog"
      v-dialogDrag
      top="5vh"
    >
      <el-form :model="exportFormDialog" ref="exportfromRef" label-width="120px">
        <el-form-item label="编程语言">
          <el-select v-model="exportFormDialog.codeLang" size="small">
            <el-option value="Java" label="Java"></el-option>
            <el-option value="Python" label="Python"></el-option>
            <el-option value="C" label="C"></el-option>
            <el-option value="C++" label="C++"></el-option>
            <el-option value="Fortran" label="Fortran"></el-option>
            <el-option value="0" label="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="调用方式">
          <el-select v-model="exportFormDialog.useType" size="small">
            <el-option value="client" label="客户端调用"></el-option>
            <el-option value="ws" label="Web Service"></el-option>
            <el-option value="rest" label="REST"></el-option>
            <el-option value="0" label="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="返回值格式">
          <el-select v-model="exportFormDialog.resType" size="small">
            <el-option value="json" label="json"></el-option>
            <el-option value="html" label="html"></el-option>
            <el-option value="0" label="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="exportFormDialog.serialNumber" size="small" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="参数示例">
          <el-input v-model="exportFormDialog.paramCode" size="small" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="调用示例">
          <el-input v-model="exportFormDialog.useCode" size="small" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="返回值示例">
          <el-input v-model="exportFormDialog.resCode" size="small" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-top:20px;">
        <el-button type="primary" @click="trueExportDialog('fromRef')">确 定</el-button>
        <el-button @click="exportDialog=false">取 消</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import {
  apiManageSave,
  getById,
  editById,
} from "@/api/portalMangement/urlManagement";
export default {
  name: "urlEditDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    return {
      exportDialog: false,
      exportFormDialog: {
        serialNumber: 0,
      },
      //编辑页面列
      msgFormDialog: {},
      urlClassify: [],
      /* 接口参数信息 */
      publictableData: [
        {
          paramType: "String",
          paramName: "interfaceId",
          desc: "接口名称",
          need: "Y",
          orders: "1",
        },
        {
          paramType: "String",
          paramName: "userId",
          desc: "接口账户id",
          need: "Y",
          orders: "1",
        },
      ],
      othertableData: [],
      otherSelection: [],

      /* 返回值 */
      apiResDtos: [],
      returnSelection: [],
      /* 样例代码 */
      apiCodeDtos: [],
      demoleSelection: [],
      baseFormRules: {
        apiName: [
          { required: true, message: "请输入接口名称", trigger: "blur" },
        ],
        apiDesc: [
          { required: true, message: "请输入接口描述 ", trigger: "blur" },
        ],
        apiSys: [
          { required: true, message: "请选择接口分类 ", trigger: "change" },
        ],
        apiHttptype: [
          { required: true, message: "请选择请求方式 ", trigger: "change" },
        ],
        apiExample: [
          { required: true, message: "请输入调用样例 ", trigger: "blur" },
        ],
        serialNumber: [
          { required: true, message: "请输入排序 ", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    this.getDicts("portal_api_mng").then((response) => {
      this.urlClassify = response.data;
    });
    if (this.handleObj.id) {
      getById({ id: this.handleObj.id }).then((response) => {
        this.msgFormDialog = response.data.api;
        response.data.param.forEach((element) => {
          if (
            element.paramName != "interfaceId" &&
            element.paramName != "userId"
          ) {
            this.othertableData.push(element);
          }
        });

        this.apiResDtos = response.data.res;
        this.apiCodeDtos = response.data.code;
      });
    }
  },
  methods: {
    paramDSelectionChange(val) {
      this.otherSelection = val;
    },
    returnSelectionChange(val) {
      this.returnSelection = val;
    },
    demoSelectionChange(val) {
      this.demoleSelection = val;
    },
    /* 接口信息 新增 删除 */
    handleParameter(type) {
      if (type == "add") {
        this.othertableData.push({
          paramName: "",
          paramDesc: "",
          paramType: "String",
          isneed: "N",
          serialNumber: 0,
        });
      } else {
        let row = type;
        row.handleRow = true;
      }
    },

    deleteParameter(row) {
      this.othertableData.forEach((pitem, pindex) => {
        if (pitem.paramName == row.paramName) {
          this.othertableData.splice(pindex, 1);
        }
      });
    },
    /* 返回值 */
    handleReturnMsg(type) {
      if (type == "add") {
        this.apiResDtos.push({
          resName: "",
          resDesc: "",
          resType: "String",
          serialNumber: 0,
        });
      } else {
        let row = type;
        row.handleRow = true;
      }
    },
    deleteReturnMsg(row, index) {
      // 判断多个删除或者单个删除
      if (index) {
        this.apiResDtos.splice(index, 1);
      } else {
        this.apiResDtos.forEach((pitem, pindex) => {
          this.returnSelection.forEach((citem) => {
            if (pitem.resName == citem.resName) {
              this.apiResDtos.splice(pindex, 1);
            }
          });
        });
      }
    },
    /* 样例代码 */
    handleDemoMsg(row) {
      this.exportDialog = true;
      if (row.codeLang) {
        this.exportFormDialog = row;
      } else {
        this.exportFormDialog = {
          serialNumber: 0,
        };
      }
      this.exportDialog = true;
    },
    deleteDemoMsg(row, index) {
      // 判断多个删除或者单个删除
      if (index) {
        this.apiCodeDtos.splice(index, 1);
      } else {
        this.apiCodeDtos.forEach((pitem, pindex) => {
          this.demoSelection.forEach((citem) => {
            if (pitem.orders == citem.orders) {
              this.apiCodeDtos.splice(pindex, 1);
            }
          });
        });
      }
    },
    trueDialog(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let obj = this.msgFormDialog;
          console.log(obj);
          obj.apiCodeDtos = this.apiCodeDtos;
          obj.apiParamDtos = this.othertableData;
          this.publictableData.forEach((element) => {
            obj.apiParamDtos.push(element);
          });
          obj.apiResDtos = this.apiResDtos;
          console.log(obj);
          if (this.handleObj.id) {
            editById(obj).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
              }
            });
          } else {
            apiManageSave(obj).then((res) => {
              if (res.code == 200) {
                this.$message({
                  type: "success",
                  message: "增加成功",
                });
                this.$emit("cancelDialog");
              } else {
                this.$message({
                  type: "error",
                  message: res.msg,
                });
              }
            });
          }
        } else {
          this.$message.error("请正确填写基本信息列表");
          return;
        }
      });
    },

    //取消按钮
    cancelDialog() {
      this.$emit("cancelDialog");
    },
  },
};
</script>

<style lang="scss">
.urlEditDialog {
  .el-select,
  .el-textarea {
    width: 100%;
  }
  textarea.el-textarea__inner {
    height: 32px;
  }
}
</style>
