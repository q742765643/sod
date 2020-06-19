<template>
  <div class="dataRegister">
    <el-form :label-width="labelWidth" :model="registerForm" :rules="rule" ref="registerForm">
      <el-row>
        <el-col :span="8">
          <el-form-item label="表名" prop="tableName">
            <el-input size="small" plcaeholder="请输入表名" v-model.trim="registerForm.tableName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="数据用途" prop="logicId">
            <el-select
              size="small"
              @change="logicIdChange"
              class="size-full"
              v-model.trim="registerForm.logicId"
            >
              <el-option
                :key="index"
                :label="item.name"
                :value="item.id"
                v-for="(item,index) in logicDBArr"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="数据库" prop="physicId">
            <el-select
              size="small"
              @change="schemaIdChange"
              class="size-full"
              v-model.trim="registerForm.physicId"
            >
              <el-option
                :key="index"
                :label="item.name"
                :value="item.id"
                v-for="(item,index) in PhysicDBByLogicArr"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="专题名" prop="schemaId">
            <el-select size="small" class="size-full" v-model.trim="registerForm.schemaId">
              <el-option
                :key="index"
                :label="item.dbname"
                :value="item.id"
                v-for="(item,index) in schemaInfoArr"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="数据频次">
            <el-select size="small" class="size-full" v-model.trim="registerForm.dataFreq">
              <el-option value="不定">不定</el-option>
              <el-option value="连续">连续</el-option>
              <el-option value="1分钟">1分钟</el-option>
              <el-option value="5分钟">5分钟</el-option>
              <el-option value="6分钟">6分钟</el-option>
              <el-option value="10分钟">10分钟</el-option>
              <el-option value="15分钟">15分钟</el-option>
              <el-option value="30分钟">30分钟</el-option>
              <el-option value="每小时">每小时</el-option>
              <el-option value="3小时">3小时</el-option>
              <el-option value="6小时">6小时</el-option>
              <el-option value="8小时">8小时</el-option>
              <el-option value="12小时">12小时</el-option>
              <el-option value="每天">每天</el-option>
              <el-option value="每候">每候</el-option>
              <el-option value="每周">每周</el-option>
              <el-option value="每8日">每8日</el-option>
              <el-option value="每旬">每旬</el-option>
              <el-option value="每两周">每两周</el-option>
              <el-option value="每16日">每16日</el-option>
              <el-option value="每月">每月</el-option>
              <el-option value="3个月">3个月</el-option>
              <el-option value="6个月">6个月</el-option>
              <el-option value="每年">每年</el-option>
              <el-option value="每10年">每10年</el-option>
              <el-option value="逐轨">逐轨</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="数据权限">
            <el-select size="small" class="size-full" v-model.trim="registerForm.published">
              <el-option label="公开" value="1"></el-option>
              <el-option label="限制" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="其他说明">
            <el-input size="small" placeholder="请输入相关说明，例如数据名称等信息" v-model.trim="registerForm.memo"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="属性资料">
            <el-radio-group @change="dataPropChange" v-model.trim="registerForm.dataProp">
              <el-radio label="站点要素写入" v-if="this.registerForm.physicId==='STDB'" value="1"></el-radio>
              <el-radio label="产品文件写入" v-if="this.registerForm.physicId==='FIDB'" value="2"></el-radio>
              <el-radio label="数据块写入" v-if="this.registerForm.physicId==='RADB'" value="3"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" v-if="this.registerForm.physicId==='RADB'">
          <el-form-item label="数据显示">
            <el-radio-group @change="datashowChange" v-model.trim="datashow">
              <el-radio label="文件块" value="1"></el-radio>
              <el-radio label="站级文件块" value="2"></el-radio>
              <el-radio label="格点数据块" value="3"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="常用字段">
            <el-input
              size="small"
              :disabled="true"
              placeholder="请选择基本字段或自定义字段表中数据"
              v-model.trim="registerForm.freuseField"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="基本字段"></el-form-item>
        </el-col>
      </el-row>
      <el-table border :data="tableData" @selection-change="tableDataChange" stripe>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="字段名称" prop="c_element_code"></el-table-column>
        <el-table-column label="中文名" prop="c_element_namech"></el-table-column>
        <el-table-column label="字段类型" prop="c_datatype"></el-table-column>
        <el-table-column label="单位" prop="c_element_unit"></el-table-column>
        <el-table-column label="字段长度" prop="fieldLength"></el-table-column>
        <el-table-column label="字段精度" prop="filedPer"></el-table-column>
        <el-table-column label="字段排序" prop="orders"></el-table-column>
        <el-table-column :formatter="formatterQc" label="主键" prop="c_isqc"></el-table-column>
        <el-table-column :formatter="formatterNo" label="是否可空" prop="c_no"></el-table-column>
      </el-table>
      <el-row class="rowSelDefined">
        <el-col :span="24">
          <el-form-item label="自定义字段">
            <div class="selDefined">
              <el-button @click="addTableData" class="el-icon-plus" size="small" type="primary">新增</el-button>
              <el-upload
                :auto-upload="false"
                :file-list="excelFileList"
                :on-change="excelRemoveFileList"
                action="sdasd"
              >
                <el-button size="small" slot="trigger" type="primary">Excle模板文件</el-button>
              </el-upload>
              <el-button icon="el-icon-download" size="small" @click="downFile">模板下载</el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-table
        :data="definedTableData"
        @cell-click="definedTableEdit"
        @selection-change="definedTableDataChange"
        border
        stripe
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="字段名称" prop="c_element_code">
          <template slot-scope="scope">
            <el-input
              @blur="fileNameBlur(scope.row)"
              ref="c_element_code"
              style="width: 100%"
              v-if="scope.row.c_element_code.edit"
              v-model.trim="scope.row.c_element_code.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.c_element_code.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="中文名" prop="c_element_namech">
          <template slot-scope="scope">
            <el-input
              size="small"
              @blur="scope.row.c_element_namech.edit = false"
              ref="c_element_namech"
              style="width: 100%"
              v-if="scope.row.c_element_namech.edit"
              v-model.trim="scope.row.c_element_namech.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.c_element_namech.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段类型" prop="c_datatype">
          <template slot-scope="scope">
            <el-select
              size="small"
              @change="scope.row.c_datatype.edit = false"
              ref="c_datatype"
              style="width: 100%"
              v-if="scope.row.c_datatype.edit"
              v-model.trim="scope.row.c_datatype.value"
            >
              <el-option
                :key="item.fieldtype"
                :label="item.fieldtype"
                :value="item.fieldtype"
                v-for="item in fieldTypeArr"
              ></el-option>
            </el-select>
            <span class="editStyle" v-else>{{ scope.row.c_datatype.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单位" prop="c_element_unit">
          <template slot-scope="scope">
            <el-input
              size="small"
              @blur="scope.row.c_element_unit.edit = false"
              ref="c_element_unit"
              style="width: 100%"
              v-if="scope.row.c_element_unit.edit"
              v-model.trim="scope.row.c_element_unit.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.c_element_unit.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段长度" prop="fieldLength">
          <template slot-scope="scope">
            <el-input
              size="small"
              @blur="scope.row.fieldLength.edit = false"
              ref="fieldLength"
              style="width: 100%"
              v-if="scope.row.fieldLength.edit"
              v-model.trim="scope.row.fieldLength.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.fieldLength.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段精度" prop="filedPer">
          <template slot-scope="scope">
            <el-input
              size="small"
              @blur="scope.row.filedPer.edit = false"
              ref="filedPer"
              style="width: 100%"
              v-if="scope.row.filedPer.edit"
              v-model.trim="scope.row.filedPer.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.filedPer.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段排序" prop="orders">
          <template slot-scope="scope">
            <el-input
              size="small"
              @blur="scope.row.orders.edit = false"
              ref="orders"
              style="width: 100%"
              v-if="scope.row.orders.edit"
              v-model.trim="scope.row.orders.value"
            ></el-input>
            <span class="editStyle" v-else>{{ scope.row.orders.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="主键" prop="c_isqc">
          <template slot-scope="scope">
            <el-select
              size="small"
              @change="scope.row.c_isqc.edit = false"
              ref="c_isqc"
              style="width: 100%"
              v-if="scope.row.c_isqc.edit"
              v-model.trim="scope.row.c_isqc.value"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
            <span class="editStyle" v-else>{{ scope.row.c_isqc.value==1?'是':'否' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="是否可空" prop="c_no">
          <template slot-scope="scope">
            <el-select
              size="small"
              @change="scope.row.c_no.edit = false"
              ref="c_no"
              style="width: 100%"
              v-if="scope.row.c_no.edit"
              v-model.trim="scope.row.c_no.value"
            >
              <el-option label="是" value="1"></el-option>
              <el-option label="否" value="0"></el-option>
            </el-select>
            <span class="editStyle" v-else>{{ scope.row.c_no.value==1?'是':'否' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <i
              @click="addTableData"
              class="el-icon-circle-plus"
              style="color:#409EFF;font-size:18px;cursor:pointer;"
            ></i>
            <i
              @click="deleteTableData(scope)"
              class="el-icon-remove"
              style="color:#F56C6C;font-size:18px;cursor:pointer;"
            ></i>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <div class="dialog-footer" slot="footer">
      <el-button @click="makeSure(true)" type="primary">立即提交</el-button>
      <el-button @click="makeSure(false)">关闭</el-button>
    </div>
  </div>
</template>

<script>
// import { interfaceObj } from "@/urlConfig";
// import { testExport } from "@/components/common.js";
// import XLSX from "xlsx";
// import Qs from "qs";
export default {
  name: "dataRegister",
  data() {
    return {
      labelWidth: "90px",
      registerForm: {
        dDataId: "Z.9999.9999", //四级编码固定值
        dataOrigin: "MUSIC接口注册", //区分申请注册数据来源固定值
        tableName: "", //表名
        logicId: "", //数据用途
        dataFreq: "不定", //数据频次
        published: "1", //服务权限 0:公开,1:限制
        memo: "", //其他说明,如果有资料名称填写:建议资料中文名称:全球地面逐小时数据
        freuseField: "", //常用字段 , 多个字段用逗号连接
        userId: "", //申请用户id
        applyId: "", //申请UUID
        dataProp: "", //资料属性
        physicId: "", //数据库
        schemaId: "" //专题名
      },

      logicDBArr: [], //数据用途集合
      PhysicDBByLogicArr: [], //数据库
      schemaInfoArr: [], //专题名
      datashow: "", //数据显示
      freuseFieldArr: [], //常用字段集合
      tableData: [], //基本字段
      tableDataMul: [], //多选的基本字段
      definedTableDataMul: [], //多选的自定义字段
      //站点
      stateData: [
        {
          c_element_code: "D_DATETIME",
          c_element_namech: "资料时间",
          c_datatype: "bigint",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ],
      //产品
      profileData: [
        {
          c_element_code: "D_DATETIME",
          c_element_namech: "资料时间",
          c_datatype: "bigint",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "V_FILE_NAME",
          c_element_namech: "文件名",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "V_FILE_FORMAT",
          c_element_namech: "文件格式",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "5",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_FILE_SIZE",
          c_element_namech: "文件大小",
          c_datatype: "number",
          c_element_unit: "byte",
          fieldLength: "12",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ],
      //块
      blockData1: [
        {
          c_element_code: "D_PRODCODE",
          c_element_namech: "资料代码",
          c_datatype: "text",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATETIME",
          c_element_namech: "资料时间",
          c_datatype: "bigint",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "V_FILE_NAME",
          c_element_namech: "文件名",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATASTATICS",
          c_element_namech: "资料数据",
          c_datatype: "map",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATAID",
          c_element_namech: "四级编码",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "50",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ],
      blockData2: [
        {
          c_element_code: "D_PRODCODE",
          c_element_namech: "资料代码",
          c_datatype: "text",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATETIME",
          c_element_namech: "资料时间",
          c_datatype: "bigint",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATASTATICS",
          c_element_namech: "资料数据",
          c_datatype: "map",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATAID",
          c_element_namech: "四级编码",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "50",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ],
      blockData3: [
        {
          c_element_code: "D_PRODCODE",
          c_element_namech: "资料代码",
          c_datatype: "text",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATETIME",
          c_element_namech: "资料时间",
          c_datatype: "bigint",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATASTATICS",
          c_element_namech: "资料数据",
          c_datatype: "map",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "STARTX",
          c_element_namech: "西经",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "150",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "ENDX",
          c_element_namech: "东经",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "STARTY",
          c_element_namech: "南纬",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "ENDY",
          c_element_namech: "北纬",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "XSTEP",
          c_element_namech: "经向格距",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "YSTEP",
          c_element_namech: "纬向格距",
          c_datatype: "float",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "XCOUNT",
          c_element_namech: "经向格数",
          c_datatype: "int",
          c_element_unit: null,
          fieldLength: "14",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "YCOUNT",
          c_element_namech: "纬向格数",
          c_datatype: "int",
          c_element_unit: null,
          fieldLength: "50",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        },
        {
          c_element_code: "D_DATAID",
          c_element_namech: "四级编码",
          c_datatype: "varchar",
          c_element_unit: null,
          fieldLength: "50",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ],
      excelFileList: [], //上传的excle
      definedTableData: [], //自定义的表格数据
      fieldTypeArr: [], //字段类型
      flage: false, //提交接口的
      rule: {
        tableName: [
          { required: true, message: "表名不能为空", trigger: "blur" }
        ],
        logicId: [
          { required: true, message: "数据用途不能为空", trigger: "change" }
        ],
        physicId: [
          { required: true, message: "数据库不能为空", trigger: "change" }
        ],
        schemaId: [
          { required: true, message: "专题名不能为空", trigger: "change" }
        ]
      }
    };
  },
  mounted() {
    // this.initLogicDB(); //初始化数据用途
    // this.getFieldTypeVal(); //获取数据字段类型
  },
  methods: {
    //获取字段类型
    getFieldTypeVal() {
      this.axios.get(interfaceObj.getFieldType).then(({ data }) => {
        if (data.returnCode == "0") {
          this.fieldTypeArr = data.DS;
        } else {
          this.$message({
            type: "error",
            message: data.returnMessage
          });
        }
      });
    },
    //初始化数据用途
    initLogicDB() {
      this.axios
        .get(interfaceObj.getLogicDB)
        .then(({ data }) => {
          if (data.returnCode == "0") {
            this.logicDBArr = data.DS;
          } else {
            this.$message({
              type: "error",
              message: data.returnMessage
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    // 数据用途改变
    logicIdChange() {
      if (this.registerForm.logicId !== "") {
        this.axios
          .get(interfaceObj.getPhysicDBByLogic, {
            params: { logicId: this.registerForm.logicId, userId: "" }
          })
          .then(({ data }) => {
            if (data.returnCode == "0") {
              this.registerForm.physicId = "";
              const dataDS = data.DS;
              this.PhysicDBByLogicArr = dataDS.map(item => {
                item.name = item.name + "【" + item.id + "】";
                return item;
              });
            } else {
              this.$message({
                type: "error",
                message: data.returnMessage
              });
            }
          })
          .catch(error => {
            console.log(error);
          });
      }
    },
    //数据库改变
    schemaIdChange() {
      if (this.registerForm.physicId !== "") {
        this.axios
          .get(interfaceObj.getSchemaInfo, {
            params: { physicId: this.registerForm.physicId, userId: "" }
          })
          .then(({ data }) => {
            if (data.returnCode == "0") {
              this.registerForm.schemaId = "";
              const dataDS = data.DS;
              this.schemaInfoArr = dataDS.map(item => {
                item.dbname = item.dbname + "【" + item.physicid + "】";
                return item;
              });
            } else {
              this.$message({
                type: "error",
                message: data.returnMessage
              });
            }
          })
          .catch(error => {
            console.log(error);
          });
      }
    },
    //资料属性选中
    dataPropChange() {
      if (this.registerForm.dataProp === "数据块写入") {
        this.datashow = "文件块";
        this.tableData = this.blockData1;
      } else if (this.registerForm.dataProp === "产品文件写入") {
        this.tableData = this.profileData;
      } else if (this.registerForm.dataProp === "站点要素写入") {
        this.tableData = this.stateData;
      }
    },
    //资料显示
    datashowChange() {
      if (this.datashow === "文件块") {
        this.tableData = this.blockData1;
      } else if (this.datashow === "站级文件块") {
        this.tableData = this.blockData2;
      } else if (this.datashow === "格点数据块") {
        this.tableData = this.blockData3;
      }
    },
    //excle上传
    excelRemoveFileList(file, fileList) {
      const _this = this;
      let excelData;
      this.excelFileList = fileList.slice(-1);
      let reader = new FileReader();
      reader.onload = function(e) {
        let data = e.target.result;
        const wb = XLSX.read(data, {
          type: "binary"
        });
        let rowNum = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]])
          .length;
        excelData = wb.Sheets[wb.SheetNames[0]];
        //数据载入
        try {
          let rows = [];
          for (let i = 0; i < rowNum; i++) {
            let er = {};
            er.c_element_code = excelData["A" + (i + 2)].v;
            er.c_element_namech = excelData["B" + (i + 2)].v;
            er.c_datatype = excelData["C" + (i + 2)].v;
            er.c_element_unit = excelData["D" + (i + 2)].v;
            er.fieldLength = excelData["E" + (i + 2)].v;
            er.filedPer = excelData["F" + (i + 2)].v;
            er.orders = excelData["G" + (i + 2)].v;
            if ("是" == excelData["H" + (i + 2)].v) {
              er.c_isqc = 1;
            } else {
              er.c_isqc = 0;
            }
            if ("是" == excelData["I" + (i + 2)].v) {
              er.c_no = 1;
            } else {
              er.c_no = 0;
            }
            rows.push(er);
          }
          rows.map(item => {
            for (let key in item) {
              item[key] = {
                value: item[key],
                edit: false
              };
            }
          });
          _this.definedTableData = _this.definedTableData.concat(rows);
        } catch (e) {
          console.log(e);
        }
      };
      reader.readAsBinaryString(file.raw);
    },
    //新增数据
    addTableData() {
      let addCellData = [
        {
          c_element_code: "字段名",
          c_element_namech: "中文名",
          c_datatype: "text",
          c_element_unit: "单位",
          fieldLength: "32",
          filedPer: "0",
          orders: "0",
          c_isqc: "1",
          c_no: "0"
        }
      ];
      addCellData.map(item => {
        for (let key in item) {
          item[key] = {
            value: item[key],
            edit: false
          };
        }
      });
      this.definedTableData = this.definedTableData.concat(addCellData);
      console.log(this.definedTableData);
    },
    //删除单元格数据
    deleteTableData(row) {
      this.definedTableData.splice(row.$index, 1);
    },
    //编辑单元格
    definedTableEdit(row, column, cell, event) {
      if (row[column.property]) {
        row[column.property].edit = true;
        setTimeout(() => {
          this.$refs[column.property].focus();
        }, 20);
      }
    },
    //字段名称是去焦点   判断字段名称
    fileNameBlur(row) {
      row.c_element_code.edit = false;
      if (row.c_element_code.value.length > 30) {
        this.$message({
          type: "error",
          message: "输入长度不能大于30"
        });
        return;
      }
      if (
        !/(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)/.test(
          row.c_element_code.value
        )
      ) {
        this.$message({
          type: "error",
          message:
            "表名首位允许是字母以及下划线；首位之后可以是字母，数字以及下划线；下划线后不能接下划线"
        });
      }
    },
    //格式化主键
    formatterQc(row) {
      return row.c_isqc == 1 ? "是" : "否";
    },
    // 格式化是否为空
    formatterNo(row) {
      return row.c_no == 1 ? "是" : "否";
    },
    //生成UUID
    genUUID() {
      let s = [];
      const hexDigits = "0123456789abcdef";
      for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
      }
      s[14] = "4";
      s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);

      s[8] = s[13] = s[18] = s[23] = "-";

      let uuid = s.join("");
      return uuid;
    },
    //选择基本字段
    tableDataChange(val) {
      this.tableDataMul = val;
      this.freuseFieldArr = [];
      this.definedTableDataMul.forEach(item => {
        this.freuseFieldArr.push(item.c_element_code.value);
      });
      this.tableDataMul.forEach(item => {
        this.freuseFieldArr.push(item.c_element_code);
      });

      this.registerForm.freuseField = this.freuseFieldArr.join(",");
    },
    //选择自定义字段
    definedTableDataChange(val) {
      this.freuseFieldArr = [];
      this.definedTableDataMul = val;
      this.tableDataMul.forEach(item => {
        this.freuseFieldArr.push(item.c_element_code);
      });
      this.definedTableDataMul.forEach(item => {
        this.freuseFieldArr.push(item.c_element_code.value);
      });

      this.registerForm.freuseField = this.freuseFieldArr.join(",");
    },
    //保存数据
    makeSure(subType) {
      if (subType) {
        let type = true;
        this.$refs["registerForm"].validate(async valid => {
          if (valid) {
            //applyId
            this.registerForm.applyId = this.genUUID();
            //用户id
            this.registerForm.userId = localStorage.getItem("loginUserId");
            //表格中的数据是否正确
            for (let i = 0; i < this.definedTableData.length; i++) {
              if (this.definedTableData[i].c_element_code.value.length > 30) {
                this.$message({
                  type: "error",
                  message: "输入长度不能大于30"
                });
                type = false;
                break;
              }
              if (
                !/(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)/.test(
                  this.definedTableData[i].c_element_code.value
                )
              ) {
                this.$message({
                  type: "error",
                  message:
                    "【自定义字段】表名首位需是字母以及下划线；首位之后可以是字母，数字以及下划线；下划线后不能接下划线"
                });
                type = false;
                break;
              }
            }
            if (type) {
              await this.saveApplyInfo();
              await this.saveTableData();
              if (this.flage) {
                this.$message({
                  type: "success",
                  message: "操作成功，请等待存储管理系统管理员审核"
                });
                this.$emit("operateData", subType);
              }
              //如果有一个删除失败   则删除已提交的
              else {
                this.axios
                  .get(interfaceObj.delNewData, {
                    params: {
                      applyId: this.registerForm.applyId,
                      dDataId: "Z.9999.9999"
                    }
                  })
                  .then(({ data }) => {
                    console.log(data);
                  })
                  .catch(error => {
                    console.log(error);
                  });
              }
            }
          }
        });
      } else {
        this.$emit("operateData", subType);
      }
    },
    //申请信息保存
    async saveApplyInfo() {
      await this.axios({
        headers: {
          "Content-Type": "application/json"
        },
        method: "post",
        url: interfaceObj.addNewData,
        data: JSON.stringify(this.registerForm)
      })
        .then(({ data }) => {
          if (data.returnCode == "0") {
            this.flage = true;
          } else {
            this.flage = false;
            this.$message({
              type: "error",
              message: data.returnMessage
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //表格数据保存
    async saveTableData() {
      const definedTableData = JSON.parse(
        JSON.stringify(this.definedTableData)
      );
      definedTableData.map(item => {
        for (let key in item) {
          item[key] = item[key].value;
        }
      });
      //合并基本字段与自定义字段
      const contatAllData = definedTableData.concat(this.tableData);
      contatAllData.forEach(item => {
        item.applyId = this.registerForm.applyId;
        item.dDataId = this.registerForm.dDataId;
        item.accuracy = item.fieldLength + "." + item.filedPer;
      });
      await this.axios({
        headers: {
          "Content-Type": "application/json"
        },
        method: "post",
        url: interfaceObj.addNewDataTableField,
        data: JSON.stringify(contatAllData)
      })
        .then(({ data }) => {
          if (data.returnCode == 0) {
            this.flage = true;
          } else {
            this.flage = false;
            this.$message({
              type: "error",
              message: data.returnMessage
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    downFile() {
      let url = interfaceObj.demoWordUrl + "个人数据注册上传模板.xlsx";
      this.axios
        .get(interfaceObj.download + "?filepath=" + url)
        .then(data => {
          testExport(interfaceObj.download + "?filepath=" + url);
        })
        .catch(function(error) {
          testExport(interfaceObj.download + "?filepath=" + url);
        });
    }
  }
};
</script>

<style lang="scss">
.dataRegister {
  .rowSelDefined {
    margin-top: 10px;
    .selDefined {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      & > .el-button {
        margin: 0 10px;
      }
      & > div {
        display: flex;
      }
    }
    .el-input-group {
      width: 30%;
    }
  }
  .editStyle {
    display: inline-block;
    padding: 1px;
    border-bottom: 1px dashed #409eff;
  }
}
</style>