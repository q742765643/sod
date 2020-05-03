(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d217a9a"],{c84c:function(e,a,t){"use strict";t.r(a);var l=function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",{staticClass:"app-container"},[t("el-form",{ref:"queryForm",staticClass:"searchBox",attrs:{model:e.queryParams,inline:!0}},[t("el-form-item",{attrs:{label:"审核状态"}},[t("el-select",{staticStyle:{width:"240px"},attrs:{placeholder:"审核状态",clearable:"",size:"small"},model:{value:e.queryParams.examineStatus,callback:function(a){e.$set(e.queryParams,"examineStatus",a)},expression:"queryParams.examineStatus"}},[t("el-option",{attrs:{label:"全部",value:""}}),e._v(" "),e._l(e.auditStatus,(function(e,a){return t("el-option",{key:a,attrs:{label:e.label,value:e.value}})}))],2)],1),e._v(" "),t("el-form-item",{attrs:{label:"申请用户"}},[t("el-input",{attrs:{size:"small",placeholder:"申请用户"},model:{value:e.queryParams.userName,callback:function(a){e.$set(e.queryParams,"userName",a)},expression:"queryParams.userName"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"数据库名"}},[t("el-input",{attrs:{size:"small",placeholder:"数据库名"},model:{value:e.queryParams.databaseName,callback:function(a){e.$set(e.queryParams,"databaseName",a)},expression:"queryParams.databaseName"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"申请时间"}},[t("el-date-picker",{attrs:{size:"small",type:"datetimerange","range-separator":"~","start-placeholder":"开始日期","end-placeholder":"结束日期","value-format":"yyyy-MM-dd HH:mm:ss"},model:{value:e.dateRange,callback:function(a){e.dateRange=a},expression:"dateRange"}},[e._v(">")])],1),e._v(" "),t("el-form-item",[t("el-button",{attrs:{size:"small",type:"primary",icon:"el-icon-search"},on:{click:e.handleQuery}},[e._v("查询")]),e._v(" "),t("el-button",{attrs:{size:"small",icon:"el-icon-refresh-right"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),e._v(" "),t("el-row",{staticClass:"handleTableBox",attrs:{gutter:10}},[t("el-col",{attrs:{span:1.5}},[t("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["cloudDBaudit:role:add"],expression:"['cloudDBaudit:role:add']"}],attrs:{type:"primary",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("添加云数据库申请")])],1)],1),e._v(" "),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.tableData,"row-key":"id"},on:{"sort-change":e.sortChange}},[t("el-table-column",{attrs:{type:"index",width:"50",index:e.table_index}}),e._v(" "),t("el-table-column",{attrs:{prop:"userName",label:"申请用户",width:"120px","show-overflow-tooltip":!0}}),e._v(" "),t("el-table-column",{attrs:{prop:"department",label:"申请单位",width:"120px"}}),e._v(" "),t("el-table-column",{attrs:{prop:"updateTime",label:"申请时间",width:"160px",sortable:"custom"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("span",[e._v(e._s(e.parseTime(a.row.createTime)))])]}}])}),e._v(" "),t("el-table-column",{attrs:{prop:"examineTime",label:"审核时间",width:"160px"},scopedSlots:e._u([{key:"default",fn:function(a){return[a.row.examineTime?t("span",[e._v(e._s(e.parseTime(a.row.examineTime)))]):e._e()]}}])}),e._v(" "),t("el-table-column",{attrs:{prop:"storageLogic",label:"数据库类型",width:"120px"}}),e._v(" "),t("el-table-column",{attrs:{prop:"databaseName",label:"数据库名","show-overflow-tooltip":!0}}),e._v(" "),t("el-table-column",{attrs:{prop:"databaseUse",label:"用 途","show-overflow-tooltip":!0}}),e._v(" "),t("el-table-column",{attrs:{prop:"examineStatus",label:"审核状态",width:"80px",formatter:e.statusShow}}),e._v(" "),t("el-table-column",{attrs:{label:"操作",width:"260px"},scopedSlots:e._u([{key:"default",fn:function(a){return["01"==a.row.examineStatus?t("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-coordinate"},on:{click:function(t){return e.viewCell(a.row)}}},[e._v("审核")]):e._e(),e._v(" "),"04"==a.row.examineStatus?t("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-finished"},on:{click:function(t){return e.viewCell(a.row)}}},[e._v("释放")]):e._e(),e._v(" "),"01"!=a.row.examineStatus&&"04"!=a.row.examineStatus?t("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-view"},on:{click:function(t){return e.viewCell(a.row)}}},[e._v("查看")]):e._e(),e._v(" "),t("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-delete"},on:{click:function(t){return e.deleteCell(a.row)}}},[e._v("删除")]),e._v(" "),t("el-button",{attrs:{type:"text",size:"mini",icon:"el-icon-s-marketing"},on:{click:function(t){return e.analysisCell(a.row)}}},[e._v("资源分析")])]}}])})],1),e._v(" "),t("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(a){return e.$set(e.queryParams,"pageNum",a)},"update:limit":function(a){return e.$set(e.queryParams,"pageSize",a)},pagination:e.getList}}),e._v(" "),t("el-dialog",{directives:[{name:"dialogDrag",rawName:"v-dialogDrag"}],attrs:{title:e.dialogTitle,visible:e.handleDialog,width:"90%","max-width":"1100px",top:"5vh"},on:{"update:visible":function(a){e.handleDialog=a}}},[e.handleDialog?t("handleAccount",{ref:"myHandleServer",attrs:{handleObj:e.handleObj},on:{handleDialogClose:e.handleDialogClose}}):e._e()],1)],1)},i=[],n=t("d0d2"),s=t("d1f7"),r={components:{handleAccount:s["default"]},data:function(){return{loading:!0,queryParams:{pageNum:1,pageSize:10,examineStatus:"",userName:"",databaseName:"",params:{orderBy:{createTime:"desc"}}},dateRange:[],auditStatus:[{value:"01",label:"待审"},{value:"02",label:"已审"},{value:"03",label:"拒绝"},{value:"04",label:"申请释放"},{value:"05",label:"已释放"}],total:0,tableData:[],dialogTitle:"",handleDialog:!1}},created:function(){this.getList()},methods:{sortChange:function(e,a,t){var l={};"ascending"==e.order?l.createTime="asc":l.createTime="desc",this.queryParams.params.orderBy=l,this.handleQuery()},table_index:function(e){return(this.queryParams.pageNum-1)*this.queryParams.pageSize+e+1},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},getList:function(){var e=this;this.loading=!0,console.log(this.addDateRange(this.queryParams,this.dateRange)),Object(n["a"])(this.addDateRange(this.queryParams,this.dateRange)).then((function(a){e.tableData=a.data.pageData,e.total=a.data.totalCount,e.loading=!1}))},resetQuery:function(){this.queryParams={pageNum:1,pageSize:10,examineStatus:"",userName:"",databaseName:""},this.dateRange=[],this.handleQuery()},statusShow:function(e){return"01"==e.examineStatus?"待审":"02"==e.examineStatus?"已审":"03"==e.examineStatus?"拒绝":"04"==e.examineStatus?"申请释放":"05"==e.examineStatus?"已释放":"-"},handleAdd:function(){this.dialogTitle="新增云数据库申请",this.handleObj={},this.handleDialog=!0},downloadTable:function(){},viewReason:function(e){this.$alert(e.failure_reason,"拒绝原因",{confirmButtonText:"确定"})},analysisCell:function(e){this.$message({type:"info",message:"资源分析暂无数据"})},viewCell:function(e){var a=this;Object(n["f"])({id:e.id}).then((function(e){a.dialogTitle="云数据库申请",a.handleObj=e.data,a.handleDialog=!0}))},deleteCell:function(e){var a=this;this.$confirm("确定要删除这条数据吗?","温馨提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){Object(n["b"])({id:e.id}).then((function(e){200==e.code&&(a.msgSuccess("删除成功"),a.getList())}))})).catch((function(){}))},handleDialogClose:function(){this.handleDialog=!1,this.handleObj={},this.handleQuery()}}},o=r,u=t("2877"),c=Object(u["a"])(o,l,i,!1,null,null,null);a["default"]=c.exports}}]);