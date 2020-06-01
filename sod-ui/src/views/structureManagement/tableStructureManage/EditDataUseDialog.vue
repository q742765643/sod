<template>
  <section class="editDataUseDialog">
    <div class="materialCon selScrollBar">
      <el-table border :data="tableData" class="selScrollBar">
        <el-table-column label="数据用途选择" prop="logicName">
          <template slot-scope="scope">
            <el-checkbox
              :checked="scope.row.logicIschecked"
              :label="scope.row.logicName"
              @change="clickLogicName(scope.row)"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="存储类型" prop="logicStorageTypesEntityList">
          <template slot-scope="scope">
            <el-radio-group v-model.trim="scope.row.typeIschecked">
              <el-radio
                @change="tableTypeChange(scope.row,item.storageName)"
                v-for="(item,index) in scope.row.logicStorageTypesEntityList"
                :key="index"
                :label="item.storageName"
                :disabled="!scope.row.checkType"
              ></el-radio>
            </el-radio-group>
          </template>
        </el-table-column>
        <el-table-column label="数据库名称" prop="logicDatabaseEntityList">
          <template slot-scope="scope">
            <el-radio-group v-model.trim="scope.row.physicsNameIschecked">
              <el-radio
                v-for="(item,index) in scope.row.logicDatabaseEntityList"
                :key="index"
                :label="item.databaseName"
                :disabled="!scope.row.checkType"
                @change="getSpecialLib(scope.row,item.databaseId,item.databaseName)"
              ></el-radio>
            </el-radio-group>
          </template>
        </el-table-column>
        <el-table-column label="专题库" prop="specialArr">
          <template slot-scope="scope">
            <el-select
              size="small"
              v-model.trim="scope.row.selectModel"
              :disabled="!scope.row.checkType"
              @change="tableSpecialArrChange(scope.row,scope.row.selectModel)"
            >
              <template v-if="scope.row.specialArr.length===0?false:true">
                <el-option
                  v-for="(item,index) in scope.row.specialArr"
                  :key="index"
                  :label="item.databaseName"
                  :value="item.id"
                ></el-option>
              </template>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="dialog-footer" slot="footer">
      <el-button type="primary" @click="makeSureSave('sourceForm')">确认</el-button>
      <el-button @click="cancleSave">取消</el-button>
    </div>
  </section>
</template>

<script>
import {
  logicDefineAll,
  findByDatabaseDefineId
} from "@/api/structureManagement/tableStructureManage/index";
export default {
  props: {
    handleArry: {
      type: Array
    }
  },
  data() {
    return {
      tableData: [],
      choseRowsObj: {},
      aboutHandleSpecial: []
    };
  },
  created() {
    console.log(this.handleArry);
    this.getTableData();
  },
  methods: {
    //获取编辑数据用途数据
    async getTableData() {
      await logicDefineAll().then(response => {
        if (response.code == 200) {
          this.tableData = response.data;
          this.tableData.forEach((single, index) => {
            this.$set(single, "selectModel", "");
            this.$set(single, "specialArr", []);
            this.$set(single, "checkType", false);
            this.$set(single, "typeIschecked", "");
            if (this.handleArry && this.handleArry.length > 0) {
              this.handleArry.forEach(element => {
                let obj = {};
                if (single.logicFlag == element.logicFlag) {
                  this.$set(single, "logicIschecked", true);
                  obj.logicFlag = single.logicFlag;
                  this.clickLogicName(single, element);
                  single.logicStorageTypesEntityList.forEach(item => {
                    if (item.storageType == element.storageType) {
                      this.$set(single, "typeIschecked", item.storageName);
                      this.tableTypeChange(single, item.storageName);
                    }
                  });
                  single.logicDatabaseEntityList.forEach(async item => {
                    if (item.databaseName == element.databasePName) {
                      this.$set(
                        single,
                        "physicsNameIschecked",
                        item.databaseName
                      );
                      await this.getSpecialLib(
                        single,
                        item.databaseId,
                        item.databaseName
                      );
                      this.$set(single, "selectModel", element.databaseName);
                      this.aboutHandleSpecial.forEach(s => {
                        if (s.databaseName == element.databaseName) {
                          obj.specialArr = [s];

                          this.tableSpecialArrChange(obj, s.id);
                        }
                      });
                    }
                  });
                }
              });
            }
          });
          console.log(this.tableData);
          let checkedArry = [];
          this.tableData.forEach((element, index) => {
            if (element.checkType) {
              checkedArry.push(element);
              this.tableData.splice(index, 1);
            }
          });
          checkedArry.forEach((element, index) => {
            this.tableData.unshift(element);
          });
        } else {
          this.$message({
            type: "error",
            message: response.msg
          });
        }
      });
    },

    //数据用途可用不可用控制
    clickLogicName(row, editElement) {
      if (row.checkType) {
        this.tableData.find((single, index) => {
          if (single.logicFlag === row.logicFlag) {
            let chosekey = single.logicFlag;
            single.checkType = false;
            single.typeIschecked = false;
            single.physicsNameIschecked = false;
            single.selectModel = "";
            delete this.choseRowsObj[chosekey];
          }
        });
      } else {
        this.tableData.find((single, index) => {
          if (single.logicFlag === row.logicFlag) {
            let chosekey = single.logicFlag;
            single.checkType = true;
            this.choseRowsObj[chosekey] = {};
            if (editElement && editElement.id) {
              console.log(editElement.id);
              this.choseRowsObj[chosekey].id = editElement.id;
              this.choseRowsObj[chosekey].version = editElement.version;
              this.choseRowsObj[chosekey].createTime = editElement.createTime;
            }
            this.choseRowsObj[chosekey].logicFlag = row.logicFlag;
            this.choseRowsObj[chosekey].logicName = row.logicName;
          }
        });
      }
    },
    //获取专题库数据
    async getSpecialLib(item, id, name) {
      let chosekey = item.logicFlag;
      this.choseRowsObj[chosekey].databasePName = name;
      await findByDatabaseDefineId({ id: id })
        .then(res => {
          if (res.code == 200) {
            this.aboutHandleSpecial = res.data;
            item.specialArr = res.data;
            // console.log(this.aboutHandleSpecial);
          } else {
            this.$message({
              type: "error",
              message: res.msg
            });
          }
        })
        .catch(error => {
          console.log("出错了，错误信息：" + error);
        });
    },
    // 存储类型change事件
    tableTypeChange(row, value) {
      let obj = {};
      obj = row.logicStorageTypesEntityList.find(item => {
        return item.storageName === value;
      });
      let getType = "";
      getType = obj.storageType;

      let chosekey = row.logicFlag;
      this.choseRowsObj[chosekey].storageName = value;
      this.choseRowsObj[chosekey].storageType = getType;
    },
    // 专题库change事件
    tableSpecialArrChange(row, value) {
      let obj = {};
      obj = row.specialArr.find(item => {
        return item.id === value;
      });
      let getSpecialName = "";
      getSpecialName = obj.databaseName;

      let chosekey = row.logicFlag;
      this.choseRowsObj[chosekey].databaseId = value;
      this.choseRowsObj[chosekey].databaseName = getSpecialName;
    },
    // 确认
    makeSureSave() {
      let choseKeyArry = [];
      let choseTableArry = [];
      for (var key in this.choseRowsObj) {
        choseKeyArry.push(key);
      }
      if (choseKeyArry.length < 0) {
        this.$message.error("请选择一条数据");
      } else {
        for (var i = 0; i < choseKeyArry.length; i++) {
          if (this.choseRowsObj[choseKeyArry[i]].storageType == undefined) {
            this.$message.error(
              "请选择" +
                this.choseRowsObj[choseKeyArry[i]].logicName +
                "的存储类型"
            );
            return false;
          } else if (
            this.choseRowsObj[choseKeyArry[i]].databasePName == undefined
          ) {
            this.$message.error(
              "请选择" +
                this.choseRowsObj[choseKeyArry[i]].logicName +
                "的数据库"
            );
            return false;
          } else if (
            this.choseRowsObj[choseKeyArry[i]].databaseId == undefined
          ) {
            this.$message.error(
              "请选择" +
                this.choseRowsObj[choseKeyArry[i]].logicName +
                "的专题库"
            );
            return false;
          } else {
            let obj = {};
            // 表格所需数据
            obj = this.choseRowsObj[choseKeyArry[i]];
            choseTableArry.push(obj);
          }
        }
        this.$emit("trueEditDataUseDialog", choseTableArry);
      }
    },
    //取消
    cancleSave() {
      this.$emit("closeEditUseDialog");
    }
  }
};
</script>

<style lang="scss">
.editDataUseDialog {
  .materialCon {
    max-height: calc(100vh - 230px);
    overflow-y: auto;
    margin-bottom: 10px;
  }
  .el-table .cell {
    text-align: left;
  }
  .el-radio {
    line-height: 22px;
  }
}
</style>
