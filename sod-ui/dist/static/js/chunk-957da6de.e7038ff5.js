(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-957da6de","chunk-3013ce12","chunk-1f30b0f9"],{"1c5f":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",{staticClass:"handlePowerDialog"},[a("el-alert",{attrs:{title:"您可以选择多个权限分配给当前选定的用户",type:"info",closable:!1}}),e._v(" "),a("div",{staticClass:"treeBox selScrollBar"},[a("el-tree",{ref:"elTree",attrs:{data:e.treeData,"show-checkbox":"","node-key":"id","default-expand-all":"","default-checked-keys":e.checkedTree,props:e.defaultProps}})],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.trueDialog("ruleForm")}}},[e._v("确 定")]),e._v(" "),a("el-button",{on:{click:function(t){return e.cancelDialog("ruleForm")}}},[e._v("取 消")])],1)],1)},n=[],o=(a("ac6a"),{name:"handlePowerDialog",props:{handleObj:{type:Object}},data:function(){return{treeData:[],checkedTree:[],defaultProps:{children:"children",label:"label"}}},created:function(){},methods:{initDetail:function(){var e=this;this.axios.post(interfaceObj.resourcesManage_permissioRole,{roleId:this.handleObj.id}).then((function(t){e.treeData=t.data.resources,e.checkedTree=t.data.checkIds}))},trueDialog:function(e){var t=this,a=this.$refs.elTree.getCheckedNodes().concat(this.$refs.elTree.getHalfCheckedNodes());if(a.length>0){var l=[];a.forEach((function(e){l.push(e.id)})),this.axios.post(interfaceObj.resourcesManage_saveRoleRescours,{rescId:l.join(","),roleId:this.handleObj.id}).then((function(e){0==e.data.returnCode?t.$message({type:"success",message:"分配成功，必须重新登录才生效"}):t.$message({type:"error",message:e.data.returnMessage}),t.$emit("cancelHandle")}))}else this.$message({type:"error",message:"请选择一条数据"})},cancelDialog:function(e){this.$emit("cancelHandle")}}}),i=o,r=(a("b2ab"),a("2877")),s=Object(r["a"])(i,l,n,!1,null,null,null);t["default"]=s.exports},2454:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",{staticClass:"roleDialog"},[a("el-form",{ref:"ruleForm",attrs:{rules:e.rules,model:e.msgFormDialog,"label-width":"100px"}},[a("el-form-item",{attrs:{label:"角色名称",prop:"name"}},[a("el-input",{attrs:{size:"medium"},model:{value:e.msgFormDialog.name,callback:function(t){e.$set(e.msgFormDialog,"name",t)},expression:"msgFormDialog.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"角色KEY",prop:"roleKey"}},[a("el-input",{attrs:{size:"medium"},model:{value:e.msgFormDialog.roleKey,callback:function(t){e.$set(e.msgFormDialog,"roleKey",t)},expression:"msgFormDialog.roleKey"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"所属科室",prop:"sectionId"}},[a("el-select",{attrs:{size:"medium"},model:{value:e.msgFormDialog.sectionId,callback:function(t){e.$set(e.msgFormDialog,"sectionId",t)},expression:"msgFormDialog.sectionId"}},e._l(e.optionsList,(function(e,t){return a("el-option",{key:t,attrs:{label:e.name,value:e.id}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"是否禁用",prop:"enable"}},[a("el-radio-group",{model:{value:e.msgFormDialog.enable,callback:function(t){e.$set(e.msgFormDialog,"enable",t)},expression:"msgFormDialog.enable"}},[a("el-radio",{attrs:{label:0}},[e._v("禁用")]),e._v(" "),a("el-radio",{attrs:{label:1}},[e._v("正常")])],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"描述",prop:"description"}},[a("el-input",{attrs:{type:"textarea",size:"medium"},model:{value:e.msgFormDialog.description,callback:function(t){e.$set(e.msgFormDialog,"description",t)},expression:"msgFormDialog.description"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.trueDialog("ruleForm")}}},[e._v("确 定")]),e._v(" "),a("el-button",{on:{click:function(t){return e.cancelDialog("ruleForm")}}},[e._v("取 消")])],1)],1)},n=[],o=(a("7f7f"),{name:"roleDialog",props:{handleObj:{type:Object}},data:function(){return{msgFormDialog:{name:"",roleKey:"",sectionId:"",enable:1,description:""},optionsList:[],rules:{name:[{required:!0,message:"请输入角色名称",trigger:"blur"}],roleKey:[{required:!0,message:"请输入角色KEY",trigger:"blur"}],sectionId:[{required:!0,message:"请选择所属科室",trigger:"change"}],enable:[{required:!0,message:"请选择是否禁用",trigger:"change"}],description:[{required:!0,message:"请输入描述",trigger:"blur"}]}}},created:function(){},methods:{initDetail:function(){void 0==this.handleObj.id||(this.msgFormDialog=this.handleObj)},getSectionList:function(){var e=this;this.axios.post(interfaceObj.roleManageApi_querySectionName).then((function(t){e.optionsList=t.data.data}))},trueDialog:function(e){var t=this;this.$refs[e].validate((function(e){if(!e)return console.log("error submit!!"),!1;void 0==t.handleObj.id?t.axios.post(interfaceObj.roleManageApi_queryRoleByRoleName,{roleName:t.msgFormDialog.name}).then((function(e){0==e.data.data.length?t.axios.post(interfaceObj.roleManageApi_addRole,t.msgFormDialog).then((function(e){"0"===e.data.returnCode?(t.$message({message:"添加成功",type:"success"}),t.$emit("cancelHandle")):t.$message({message:"添加失败！",type:"error"})})):t.$message({message:"用户名不能重复！！",type:"warning"})})):t.axios.post(interfaceObj.roleManageApi_updateRole,t.msgFormDialog).then((function(e){"0"===e.data.returnCode?(t.$message({message:"编辑成功",type:"success"}),t.$emit("cancelHandle")):t.$message({message:"编辑失败！",type:"error"})}))}))},cancelDialog:function(e){this.$emit("cancelHandle")}}}),i=o,r=(a("a222"),a("2877")),s=Object(r["a"])(i,l,n,!1,null,null,null);t["default"]=s.exports},"57ee":function(e,t,a){},a222:function(e,t,a){"use strict";var l=a("ef56"),n=a.n(l);n.a},aa9e:function(e,t,a){"use strict";var l=a("f794a"),n=a.n(l);n.a},b2ab:function(e,t,a){"use strict";var l=a("57ee"),n=a.n(l);n.a},dd6e:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container sodDeploy"},[a("el-form",{ref:"modelForm",staticClass:"searchBox",attrs:{inline:!0,model:e.queryParams}},[a("el-form-item",{attrs:{label:"角色名称"}},[a("el-input",{attrs:{size:"small"},model:{value:e.queryParams.name,callback:function(t){e.$set(e.queryParams,"name",t)},expression:"queryParams.name"}})],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{size:"small",type:"primary",icon:"el-icon-search"},on:{click:e.searchFun}},[e._v("查询")])],1)],1),e._v(" "),a("el-row",{staticClass:"handleTableBox",attrs:{gutter:10}},[a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{size:"small",type:"primary",icon:"el-icon-plus"},on:{click:e.addCell}},[e._v("新增")])],1),e._v(" "),a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{size:"small",type:"danger",icon:"el-icon-delete"},on:{click:e.deleteCell}},[e._v("删除")])],1)],1),e._v(" "),a("el-table",{ref:"singleTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:""}},[a("el-table-column",{attrs:{type:"index",width:"50",index:e.table_index}}),e._v(" "),a("el-table-column",{attrs:{type:"selection",width:"55"}}),e._v(" "),a("el-table-column",{attrs:{prop:"name",label:"角色名称","show-overflow-tooltip":!0}}),e._v(" "),a("el-table-column",{attrs:{prop:"roleKey",label:"角色KEY"}}),e._v(" "),a("el-table-column",{attrs:{prop:"sectionName",label:"所属科室"}}),e._v(" "),a("el-table-column",{attrs:{prop:"userPhone",label:"是否禁用"},scopedSlots:e._u([{key:"default",fn:function(t){return["1"==t.row.enable?a("el-link",{attrs:{type:"primary",underline:!1}},[e._v("正常")]):a("el-link",{attrs:{type:"danger",underline:!1}},[e._v("禁用")])]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"description",label:"描述"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作",width:"200px"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-s-check"},on:{click:function(a){return e.powerCell(t.row)}}},[e._v("分配权限")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-edit"},on:{click:function(a){return e.editCell(t.row)}}},[e._v("编辑")])]}}])})],1),e._v(" "),a("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.searchFun}}),e._v(" "),a("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:e.dialogTitle,visible:e.handleRoleDialog,width:"650px"},on:{"update:visible":function(t){e.handleRoleDialog=t}}},[e.handleRoleDialog?a("handleRole",{ref:"myHandleServer",attrs:{handleObj:e.handleObj},on:{cancelHandle:e.cancelHandle}}):e._e()],1),e._v(" "),a("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:"分配角色",visible:e.handlePowerDialog,width:"650px"},on:{"update:visible":function(t){e.handlePowerDialog=t}}},[e.handlePowerDialog?a("handlePower",{attrs:{handleObj:e.handleObj},on:{cancelHandle:e.cancelHandle}}):e._e()],1)],1)},n=[],o=(a("7f7f"),a("ac6a"),a("db72")),i=a("2454"),r=a("1c5f"),s={name:"sodDeploy",components:{handleRole:i["default"],handlePower:r["default"]},data:function(){return{queryParams:{name:"",sort:"name",order:"asc"},tableData:[],total:0,handleRoleDialog:!1,dialogTitle:"新增",handleObj:{},handlePowerDialog:!1}},created:function(){},mounted:function(){},methods:{table_index:function(e){return(this.queryParams.pageNum-1)*this.queryParams.pageSize+e+1},statusChange:function(){this.searchFun("1")},searchFun:function(e){var t=this;e&&(this.$refs.pagination.paginateObj.page=1);var a=this.$refs.pagination.paginateObj;this.queryParams=Object(o["a"])({},this.queryParams,{},a),this.axios.post(interfaceObj.roleManageApi_queryRole,this.queryParams).then((function(e){t.tableData=e.data.data,t.total=e.data.total}))},addCell:function(){this.handleObj={},this.dialogTitle="新增",this.handleRoleDialog=!0},powerCell:function(e){this.dialogTitle="分配权限",this.handleObj=e,this.handlePowerDialog=!0},editCell:function(e){this.dialogTitle="编辑",this.handleObj=e,this.handleRoleDialog=!0},deleteCell:function(){var e=this,t=this.$refs.singleTable.selection;if(console.log(t),t.length>0){var a=[];t.forEach((function(e){"超级管理员"!=e.name&&a.push(e.id)})),a.length>0&&a.length==t.length?this.$confirm("数据删除后将无法恢复，确认删除?","温馨提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){e.axios.post(interfaceObj.roleManageApi_deleteById,{roleId:a.join(",")}).then((function(t){0==t.data.returnCode?e.$message({type:"success",message:"删除成功"}):e.$message({type:"error",message:t.data.returnMessage}),e.searchFun("1")}))})).catch((function(){e.$message({type:"info",message:"已取消删除"})})):this.$message({type:"error",message:"超级管理员不能删除"})}else this.$message({type:"error",message:"请选择一条数据"})},cancelHandle:function(e){this.handleObj={},this.handleRoleDialog=!1,this.handlePowerDialog=!1,this.searchFun("1")}}},c=s,u=(a("aa9e"),a("2877")),d=Object(u["a"])(c,l,n,!1,null,null,null);t["default"]=d.exports},ef56:function(e,t,a){},f794a:function(e,t,a){}}]);