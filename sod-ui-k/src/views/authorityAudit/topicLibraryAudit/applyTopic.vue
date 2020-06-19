<template>
  <section class="applyTopicDialog">
    <el-steps :active="stepNum" simple>
      <el-step title="1 创建专题库"></el-step>
      <el-step title="2 申请资料"></el-step>
      <el-step title="3 提交申请"></el-step>
    </el-steps>
    <el-card class="box-card" v-if="stepNum===0">
      <div slot="header" class="clearfix">
        <span style="font-size: 26px;">创建专题库</span>
        <p
          style="margin-top:10px;font-size: 14px;color: #666;"
        >填写专题库申请信息，用于创建专题库。专题库创建后不需要审核，可直接在专题库列表页面查看</p>
      </div>
      <div>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="名称" prop="name">
            <el-input v-model.trim="ruleForm.name" placeholder="请输入专题库名称，支持中文，英文，数字和下划线，不超过20个字符"></el-input>
          </el-form-item>
          <el-form-item label="简称" prop="pname">
            <el-input v-model.trim="ruleForm.pname" placeholder="请输入6个字符以内的英文大写"></el-input>
          </el-form-item>
          <el-form-item label="数据库" prop="dbSelect">
            <el-checkbox-group v-model.trim="ruleForm.dbSelect">
              <el-checkbox v-for="db in ruleForm.database" :label="db.name" :key="db.id">{{db.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="用途" prop="use">
            <el-input type="textarea" v-model.trim="ruleForm.use"></el-input>
          </el-form-item>
          <el-form-item label="图标" prop="imageURL">
            <img style="width:40px;" v-if="dialogImageUrl" :src="dialogImageUrl" alt />
            <el-popover
              v-model.trim="popoverVisibale"
              placement="right"
              width="260"
              trigger="click"
            >
              <div>
                <div
                  v-for="(url,index) in dialogImageUrlData"
                  :key="index"
                  v-bind:class="{
                    'demoImage-origin':url[1]==1,
                    'is-demoImage-active':dialogImageUrlData[index][1]==0,
                    }"
                  @click="clickImage(index)"
                >
                  <el-image style="width:40px;height:40px" :src="url[0]" fit="fill"></el-image>
                </div>
              </div>
              <el-button slot="reference" type="primary" size="small">选择图标</el-button>
            </el-popover>
          </el-form-item>
          <el-form-item label="申请材料" prop="filepath">
            <el-upload
              class="upload-demo"
              :action="upLoadUrl"
              :limit="1"
              :on-exceed="handleExceed"
              :file-list="filelist"
              :on-success="successUpload"
            >
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
            <el-button
              size="small"
              type="primary"
              style="float:right;margin-top:-35px;"
              @click="download"
            >下载模板</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card" v-if="stepNum===1">
      <div slot="header" class="clearfix">
        <span style="font-size: 26px;">申请资料</span>
        <p style="margin-top:10px;font-size: 14px;color: #666;">选择需要申请的资料及资料的访问权限</p>
      </div>
      <div>
        <el-form label-width="100px" class="demo-ruleForm">
          <el-form-item label>
            <el-checkbox-group v-model.trim="sdbSelect">
              <el-checkbox
                v-for="db in ruleForm.database"
                :label="db.id"
                :key="db.name"
                @change="getDataByLogic"
              >{{db.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="asideTreeScroll">
              <div slot="header">
                <span>待选资料</span>
              </div>
              <div class="treeBox selScrollBar">
                <el-tree
                  class="el-tree"
                  :data="treenode"
                  :props="defaultProps"
                  auto-expand-parent
                  :default-expanded-keys="expandedkeys"
                  :default-checked-keys="checkedKeys"
                  node-key="uid"
                  show-checkbox
                  ref="treeZL"
                  @check-change="changeTree"
                ></el-tree>
              </div>
            </el-card>
          </el-col>
          <el-col :span="18">
            <el-card class="asideTable">
              <div slot="header">
                <span>已选资料</span>
              </div>
              <el-button size="small" type="primary" @click="deleteMuch">批量删除</el-button>
              <div class="tableBox selScrollBar">
                <el-table
                  :data="tableData"
                  border
                  stripe
                  ref="tableZL"
                  @selection-change="selectChange"
                >
                  <el-table-column type="selection" min-width="15"></el-table-column>
                  <el-table-column type="index" label="序号" min-width="25"></el-table-column>
                  <el-table-column prop="name" label="资料名称" min-width="60"></el-table-column>
                  <el-table-column prop="d_data_id" label="四级编码" min-width="50"></el-table-column>
                  <el-table-column prop="table_name" label="表名称" min-width="60"></el-table-column>
                  <el-table-column prop="pname" label="数据库" min-width="60"></el-table-column>
                  <el-table-column prop label="权限" min-width="20">
                    <template slot-scope="scope">
                      <span v-if="scope.row.readOrWrite==''">只读</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop label="操作" min-width="20">
                    <template slot-scope="scope">
                      <el-button
                        type="text"
                        size="small"
                        @click="deleteRow(scope.$index,tableData,scope.row.uid)"
                      >删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card class="box-card" v-if="stepNum===2">
      <div slot="header" class="clearfix">
        <span style="font-size: 26px;">提交申请</span>
        <p
          style="margin-top:10px;font-size: 14px;color: #666;"
        >点击申请按钮，提交当前申请信息。点击返回按钮，放弃本次申请操作，并返回到专题库列表页。</p>
      </div>
      <div>
        <el-button type="primary" @click="addSpecialDBApply">提交申请</el-button>
      </div>
    </el-card>
    <div class="dialog-footer">
      <el-button type="primary" v-if="stepNum!=0" @click="backStep()">上一步</el-button>
      <el-button type="primary" v-if="stepNum!=2" @click="nextStep()">下一步</el-button>
    </div>
  </section>
</template>

<script>
// import { interfaceObj } from "@/urlConfig.js";
// import { testExport } from "@/components/common.js";
export default {
  name: "applyTopicDialog",

  props: {},
  data() {
    const nameValidate = (rule, value, callback) => {
      const reg = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
      if (value == "") {
        callback(new Error("请输入专题库名称"));
      } else if (!reg.test(value)) {
        callback(
          new Error(
            "专题库名称允许中文，英文，数字，下划线，且不能以下划线开始"
          )
        );
      } else {
        this.axios
          .get(interfaceObj.SpecialDB_validNameOrCode + "name=" + value)
          .then(res => {
            if (res.data && res.data.returnCode == 0) {
              callback();
            } else {
              callback(new Error("专题库名称已经存在"));
            }
          });
      }
    };
    const pnameValidate = (rule, value, callback) => {
      const reg = /^[A-Z]+$/;
      if (value == "") {
        callback(new Error("请输入专题库简称"));
      } else if (!reg.test(value)) {
        return callback(new Error("专题库简称只能是大写英文"));
      } else {
        this.axios
          .get(interfaceObj.SpecialDB_validNameOrCode + "code=" + value)
          .then(res => {
            if (res.data && res.data.returnCode == 0) {
              callback();
            } else {
              callback(new Error("专题库简称已经存在"));
            }
          });
      }
    };
    return {
      currentNodeArry: [],
      checkedKeys: [],
      expandedkeys: [],
      upLoadUrl: "",
      stepNum: 0,
      ruleForm: {
        name: "",
        pname: "",
        use: "",
        imageURL: "",
        database: [],
        dbSelect: [],
        filepath: "",
        tableData: []
      },
      filelist: [],
      sdbSelect: [],
      //dialogImageUrl: require("@/assets/images/h1.png"),
      dialogImageUrl: "",
      popoverVisibale: false,
      dialogImageUrlData: [
        [require("@/assets/image/icon/h1.png"), 1],
        [require("@/assets/image/icon/h2.png"), 1],
        [require("@/assets/image/icon/h3.png"), 1],
        [require("@/assets/image/icon/h4.png"), 1],
        [require("@/assets/image/icon/h5.png"), 1],
        [require("@/assets/image/icon/h6.png"), 1],
        [require("@/assets/image/icon/h7.png"), 1],
        [require("@/assets/image/icon/h8.png"), 1],
        [require("@/assets/image/icon/h9.png"), 1],
        [require("@/assets/image/icon/h10.png"), 1],
        [require("@/assets/image/icon/h11.png"), 1],
        [require("@/assets/image/icon/h12.png"), 1],
        [require("@/assets/image/icon/h13.png"), 1],
        [require("@/assets/image/icon/h14.png"), 1],
        [require("@/assets/image/icon/h15.png"), 1]
      ],
      treenode: [],
      tableData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      tableSelsectData: [],
      rules: {
        name: [
          { min: 1, max: 20, message: "不超过20个字符", trigger: "blur" },
          { required: true, validator: nameValidate, trigger: "blur" }
        ],
        pname: [
          {
            min: 1,
            max: 6,
            message: "输入长度不能大于6个字符",
            trigger: "blur"
          },
          { required: true, validator: pnameValidate, trigger: "blur" }
        ],
        dbSelect: [
          {
            type: "array",
            required: true,
            message: "请选择数据库",
            trigger: "change"
          }
        ],
        use: [
          {
            required: true,
            message: "请填写专题库用途",
            trigger: "blur"
          },
          {
            min: 0,
            max: 100,
            message: "输入长度不能大于100个字符",
            trigger: "blur"
          }
        ],
        imageURL: [
          {
            required: true,
            message: "请选择图标",
            trigger: "change"
          }
        ],
        filepath: [
          {
            required: true,
            message: "请上传文件",
            trigger: "change"
          }
        ]
      }
    };
  },
  created() {
    // this.getDatabase();
  },
  methods: {
    selectChange(selection) {
      this.tableSelsectData = selection;
    },
    clickImage(num) {
      //console.log(this.dialogImageUrlData[num][1]);
      this.dialogImageUrlData.forEach((item, index, array) => {
        item[1] = 1;
      });
      this.dialogImageUrlData[num][1] = 0;
      let imageurl = "/assets/images/icon/h" + (num + 1) + ".png";
      this.dialogImageUrl = this.dialogImageUrlData[num][0];
      this.popoverVisibale = false;
      this.ruleForm.imageURL = imageurl;
    },
    successUpload: function(response, file, fileList) {
      this.ruleForm.filepath = file.raw;
    },
    backStep() {
      if (this.stepNum != 0) {
        this.stepNum = this.stepNum - 1;
      }
    },
    nextStep() {
      if (this.stepNum == 0) {
        this.$refs["ruleForm"].validate(valid => {
          if (valid) {
            //alert("submit!");
            this.stepNum = this.stepNum + 1;
          } else {
            //console.log("error submit!!");
            return false;
          }
        });
      }
      if (this.stepNum == 1) {
        const selectnodes = this.$refs.treeZL.getCheckedNodes(true);
        selectnodes.forEach((item, index, array) => {
          this.ruleForm.tableData.push({
            DATA_CLASS_ID: item.id,
            LOGIC_ID: item.physicalDB.split(",")[0],
            APPLY_AUTHORITY: "1"
          });
        });
        this.stepNum = this.stepNum + 1;
      }
      //this.stepNum = this.stepNum + 1;
    },
    getDatabase() {
      this.axios.get(interfaceObj.SpecialDB_getPhysicDB).then(res => {
        //console.log(res);
        this.ruleForm.database = res.data.DS;
        console.log(this.ruleForm.database);
      });
    },
    getDataByLogic() {
      let checkedNodes = this.$refs.treeZL.getCheckedNodes(true);
      checkedNodes.forEach(element => {
        this.currentNodeArry.push(element.uid);
      });
      var getid = "";
      var that = this;
      //console.log(this.sdbSelect);
      this.sdbSelect.forEach((item, index, array) => {
        getid = getid + "&logics=" + item;
      });
      //console.log(getid);
      this.axios
        .get(interfaceObj.SpecialDB_getDataByLogics + getid)
        .then(res => {
          let allData = res.data.data;
          let etreearray = [];
          let etreeobj = {};
          allData.forEach((value, index, array) => {
            let paid = value["pId"];
            let did = value["id"];
            if (paid == "-1") {
              etreeobj[did] = [etreearray.length];
              value.children = [];
              etreearray.push(value);
            } else {
              if (etreeobj[paid]) {
                let locationarray = etreeobj[paid][0];
                value.pname = that.setPhysicalName(value.physicalDB);
                etreearray[locationarray].children.push(value);
              }
            }
          });
          this.treenode = etreearray;
          this.checkedKeys = this.currentNodeArry;
        });
    },
    setPhysicalName(pid) {
      if (pid) {
        const ppid = pid.split(",")[0];
        var callback = "";
        this.ruleForm.database.forEach((value, index, array) => {
          if (value.id == ppid) {
            callback = value.name;
          }
        });
        return callback;
      }
    },
    changeTree(data) {
      this.tableData = this.$refs.treeZL.getCheckedNodes(true);
    },
    deleteRow(index, rows, uid) {
      this.$refs.treeZL.setChecked(uid, false);
      rows.splice(index, 1);
    },
    deleteMuch() {
      if (this.tableSelsectData.length > 0) {
        this.$confirm("确定选中数据都删除？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.tableSelsectData.forEach((value, index, array) => {
            this.$refs.treeZL.setChecked(value.uid, false);
          });
          this.tableData = this.tableData.filter(
            item => !this.tableSelsectData.includes(item)
          );
          console.log(this.tableData);
        });
      } else {
        this.$message.error("请选择需要删除的文件！");
      }
    },

    download() {
      let url = interfaceObj.demoWordUrl + "专题库申请表模板.docx";
      this.axios
        .get(interfaceObj.download + "?filepath=" + url)
        .then(data => {
          testExport(interfaceObj.download + "?filepath=" + url);
        })
        .catch(function(error) {
          testExport(interfaceObj.download + "?filepath=" + url);
        });
    },
    // 上传限制
    handleExceed() {
      //this.ruleForm.file = [];
      this.$message.warning("当前限制选择1个文件");
    },
    addSpecialDBApply() {
      let formData = new FormData();
      formData.append("TDB_NAME", this.ruleForm.name);
      formData.append("SCHEMA_ID", this.ruleForm.pname);
      formData.append("USES", this.ruleForm.use);
      formData.append("TDB_IMG", this.ruleForm.imageURL);
      var getdid = [];
      debugger;

      this.ruleForm.database.forEach(p => {
        this.ruleForm.dbSelect.forEach(c => {
          if (p.name == c) {
            getdid.push(p.id);
          }
        });
      });
      formData.append("DATABASE_ID", getdid.join(","));
      formData.append("APPLY_FILE_PATH", this.ruleForm.filepath);
      formData.append("TABLE_DATA", JSON.stringify(this.ruleForm.tableData));
      const uid = this.$store.getters.getLoginUser.userId;
      this.axios
        .post(interfaceObj.SpecialDB_addSpecialDBApply + uid, formData)
        .then(res => {
          if (res.data.returnCode == 0) {
            this.$message({
              showClose: true,
              message: "保存成功，请等待管理员审核",
              type: "success"
            });
            this.$emit("closedialog");
          } else {
            this.$message.error("申请失败，请联管理员");
          }
        });
    }
  }
};
</script>

<style lang="scss">
.applyTopicDialog {
  .el-table th.gutter {
    display: table-cell !important;
  }
  .treeBox {
    margin-top: 10px;
  }
  .asideTable {
    background: #fff;
    padding: 10px;
    border: 1px solid #ebeef5;
    overflow: hidden;
    .tableBox {
      margin-top: 10px;
      overflow: auto;
      height: calc(100vh - 295px);
    }
  }
}
.demoImage-origin {
  border: 1px solid #fff;
  float: left;
  padding: 5px;
}
.demoImage-origin:hover {
  border: 1px solid #23aaf2;
}
.is-demoImage-active {
  border: 1px solid #2e9a1f;
  float: left;
  padding: 5px;
}
</style>
