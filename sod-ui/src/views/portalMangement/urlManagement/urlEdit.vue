<template>
  <section class="urlEditDialog">
    <el-form :model="msgFormDialog" ref="fromRef" :rules="baseFormRules" label-width="120px">
      <el-tabs type="border-card">
        <el-tab-pane label="接口基本信息">
          <el-form-item prop="userName" label="接口名称:">
            <el-input
              size="small"
              v-model.trim="msgFormDialog.createBy"
              placeholder="请输入接口名称 , 例如 : getElementById"
            />
          </el-form-item>
          <el-form-item prop="userName" label="接口描述:">
            <el-input size="small" v-model.trim="msgFormDialog.createBy" />
          </el-form-item>
          <el-form-item prop="userName" label="接口分类:">
            <el-input size="small" v-model.trim="msgFormDialog.createBy" />
          </el-form-item>
          <el-form-item prop="userName" label="请求方式:">
            <el-input size="small" v-model.trim="msgFormDialog.createBy" />
          </el-form-item>
          <el-form-item prop="userName" label="调用样例:">
            <el-input size="small" v-model.trim="msgFormDialog.createBy" />
          </el-form-item>
          <el-form-item prop="userName" label="排序:">
            <el-input-number v-model.trim="msgFormDialog.sortNo" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item prop="userName" label="备注:">
            <el-input size="small" v-model.trim="msgFormDialog.createBy" type="textarea" />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="接口参数信息">
          <el-card shadow="never">
            <div slot="header" class="clearfix">
              <span>公共参数</span>
            </div>
            <el-table border :data="publictableData" row-key="id">
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="createTime" label="参数名称"></el-table-column>
              <el-table-column prop="content" label="参数描述"></el-table-column>
              <el-table-column prop="status" label="参数类型">
                <template slot-scope="scope">
                  <span v-if="scope.row.status=='0'">未回复</span>
                  <span v-if="scope.row.status=='1'">已回复</span>
                </template>
              </el-table-column>
              <el-table-column prop="content" label="是否必填"></el-table-column>
              <el-table-column prop="content" label="排序编号"></el-table-column>
            </el-table>
          </el-card>

          <el-card shadow="never">
            <div slot="header" class="clearfix">
              <span>其他参数</span>
              <el-button
                size="small"
                type="danger"
                @click="deleteParameter"
                style="float: right;"
                icon="el-icon-delete"
              >删除</el-button>
              <el-button
                size="small"
                type="primary"
                @click="handleParameter('add')"
                style="float: right;margin-right:4px;"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="othertableData"
              row-key="id"
              @selection-change="otherParSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="parName" label="参数名称">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parName" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parName}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parDes" label="参数描述">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parDes" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parDes}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parClass" label="参数类型">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parClass" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parClass}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="isCheck" label="是否必填">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.isCheck" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.isCheck}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parnum" label="排序编号">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parnum" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parnum}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    icon="el-icon-edit"
                    size="mini"
                    type="text"
                    @click="handleParameter(scope.row)"
                  >编辑</el-button>
                  <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="text"
                    @click="deleteParameter(scope.row,index)"
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
                type="danger"
                @click="deleteReturnMsg"
                style="float: right;"
                icon="el-icon-delete"
              >删除</el-button>
              <el-button
                size="small"
                type="primary"
                @click="handleReturnMsg('add')"
                style="float: right;margin-right:4px;"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="returntableData"
              row-key="id"
              @selection-change="returnSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="parName" label="返回值名称">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parName" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parName}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parDes" label="返回值描述">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parDes" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parDes}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parClass" label="返回值类型">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parClass" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parClass}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="parnum" label="排序编号">
                <template slot-scope="scope">
                  <el-input v-show="scope.row.handleRow" v-model="scope.row.parnum" size="small"></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.parnum}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    icon="el-icon-edit"
                    size="mini"
                    type="text"
                    @click="handleReturnMsg(scope.row)"
                  >编辑</el-button>
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
                type="danger"
                @click="deleteDemoMsg"
                style="float: right;"
                icon="el-icon-delete"
              >删除</el-button>
              <el-button
                size="small"
                type="primary"
                @click="handleDemoMsg('add')"
                style="float: right;margin-right:4px;"
                icon="el-icon-plus"
              >新增</el-button>
            </div>
            <el-table
              border
              :data="demotableData"
              row-key="id"
              @selection-change="demoSelectionChange"
            >
              <el-table-column type="selection" width="50"></el-table-column>
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column prop="codeLang" label="编程语言" width="150">
                <template slot-scope="scope">
                  <el-select v-show="scope.row.handleRow" v-model="scope.row.codeLang" size="small">
                    <el-option value="Java" label="Java"></el-option>
                    <el-option value="Python" label="Python"></el-option>
                    <el-option value="C" label="C"></el-option>
                    <el-option value="C++" label="C++"></el-option>
                    <el-option value="Fortran" label="Fortran"></el-option>
                    <el-option value="0" label="其他"></el-option>
                  </el-select>
                  <span v-show="scope.row.handleRow== false">{{scope.row.codeLang}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="useType" label="调用方式" width="150">
                <template slot-scope="scope">
                  <el-select v-show="scope.row.handleRow" v-model="scope.row.useType" size="small">
                    <el-option value="client" label="客户端调用"></el-option>
                    <el-option value="ws" label="Web Service"></el-option>
                    <el-option value="rest" label="REST"></el-option>
                    <el-option value="0" label="其他"></el-option>
                  </el-select>
                  <span v-show="scope.row.handleRow== false">{{scope.row.useType}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="resType" label="返回值格式" width="100">
                <template slot-scope="scope">
                  <el-select v-show="scope.row.handleRow" v-model="scope.row.resType" size="small">
                    <el-option value="json" label="json"></el-option>
                    <el-option value="html" label="html"></el-option>
                    <el-option value="0" label="其他"></el-option>
                  </el-select>
                  <span v-show="scope.row.handleRow== false">{{scope.row.resType}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="orders" label="排序编号" width="150">
                <template slot-scope="scope">
                  <el-input-number
                    v-show="scope.row.handleRow"
                    v-model="scope.row.orders"
                    size="small"
                    :min="0"
                  ></el-input-number>
                  <span v-show="scope.row.handleRow== false">{{scope.row.orders}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="paramCode" label="参数示例" width="200">
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.handleRow"
                    v-model="scope.row.paramCode"
                    size="small"
                    type="textarea"
                  ></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.paramCode}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="useCode" label="调用示例" width="200">
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.handleRow"
                    v-model="scope.row.useCode"
                    size="small"
                    type="textarea"
                  ></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.useCode}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="resCode" label="返回值示例" width="200">
                <template slot-scope="scope">
                  <el-input
                    v-show="scope.row.handleRow"
                    v-model="scope.row.resCode"
                    size="small"
                    type="textarea"
                  ></el-input>
                  <span v-show="scope.row.handleRow== false">{{scope.row.resCode}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="address" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    icon="el-icon-edit"
                    size="mini"
                    type="text"
                    @click="handleDemoMsg(scope.row)"
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
  </section>
</template>

<script>
import {
  saveDynManage,
  editDynManage,
} from "@/api/portalMangement/dynMangement";
export default {
  name: "urlEditDialog",
  components: {},
  props: {
    handleObj: {
      type: Object,
    },
  },
  data() {
    var titleValidate = (rule, value, callback) => {
      if (value === "" || value.length > 100) {
        callback(new Error("请输入1-100字标题"));
      } else {
        callback();
      }
    };
    return {
      //编辑页面列
      msgFormDialog: {
        title: "",
        content: "",
        serialNumber: "",
        ispublished: "0",
      },
      /* 接口参数信息 */
      publictableData: [],
      othertableData: [],
      otherSelection: [],

      /* 返回值 */
      returntableData: [],
      returnSelection: [],
      /* 样例代码 */
      demotableData: [],
      demoleSelection: [],
      //控制标题是否可编辑
      isDbIdDisable: false,
      baseFormRules: {
        title: [{ required: true, validator: titleValidate, trigger: "blur" }],
        ispublished: [{ required: true, message: "是否发布", trigger: "blur" }],
        serialNumber: [
          { required: true, message: "请输入序号 ", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请输入正文内容 ", trigger: "blur" },
        ],
      },
    };
  },
  async created() {
    if (this.handleObj.id) {
      this.isDbIdDisable = true;
      this.msgFormDialog = this.handleObj;
      this.msgFormDialog.createTime = this.parseTime(
        this.msgFormDialog.createTime
      );
      console.log(this.msgFormDialog);
    } else {
      this.isDbIdDisable = false;
    }
  },
  methods: {
    otherParSelectionChange(val) {
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
          handleRow: true,
          parName: "",
          parDes: "",
          parClass: "",
          parnum: "",
        });
      } else {
        let row = type;
        row.handleRow = true;
      }
    },

    deleteParameter(row, index) {
      // 判断多个删除或者单个删除
      if (index) {
        this.othertableData.splice(index, 1);
      } else {
        this.othertableData.forEach((pitem, pindex) => {
          this.otherSelection.forEach((citem) => {
            if (pitem.parName == citem.parName) {
              this.othertableData.splice(pindex, 1);
            }
          });
        });
      }
    },
    /* 返回值 */
    handleReturnMsg(type) {
      if (type == "add") {
        this.returntableData.push({
          handleRow: true,
          parName: "",
          parDes: "",
          parClass: "",
          parnum: "",
        });
      } else {
        let row = type;
        row.handleRow = true;
      }
    },
    deleteReturnMsg(row, index) {
      // 判断多个删除或者单个删除
      if (index) {
        this.returntableData.splice(index, 1);
      } else {
        this.returntableData.forEach((pitem, pindex) => {
          this.returnSelection.forEach((citem) => {
            if (pitem.parName == citem.parName) {
              this.returntableData.splice(pindex, 1);
            }
          });
        });
      }
    },
    /* 样例代码 */
    handleDemoMsg(type) {
      if (type == "add") {
        this.demotableData.push({
          handleRow: true,
          codeLang: "Java",
          useType: "client",
          resType: "json",
          orders: "",
          paramCode: "",
          useCode: "",
          resCode: "",
        });
      } else {
        let row = type;
        row.handleRow = true;
      }
    },
    deleteDemoMsg(row, index) {
      // 判断多个删除或者单个删除
      if (index) {
        this.demotableData.splice(index, 1);
      } else {
        this.demotableData.forEach((pitem, pindex) => {
          this.demoSelection.forEach((citem) => {
            if (pitem.orders == citem.orders) {
              this.demotableData.splice(pindex, 1);
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
          if (this.handleObj.id) {
            editDynManage(obj).then((res) => {
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
            saveDynManage(obj).then((res) => {
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
