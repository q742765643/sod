import axios from "axios";
import { interfaceObj } from "@/urlConfig";
const state = {
  //业务树
  busTree: [],
  //环节数据  包括每页条数 以及总条数
  cellDataObj: {
    cellData: [],
    dataTotal: 0
  },
  //环节状态
  cellZt: [
    { ztId: 0, ztName: "启动" },
    { ztId: 1, ztName: "未启动" },
    { ztId: 2, ztName: "运行中" }
  ],
  //模型弹出层显示隐藏控制
  modelDialogVisable: false,
  //前置环节弹出层显示隐藏控制
  dependDialogVisable: false,
  taskAddDialog: false,
  //模型列表数据 包括每页条数 以及总条数
  modelListObj: {
    modelData: [],
    dataTotal: 0
  },
  //环节详细信息
  cellDetailObj: {
    id: "",
    modelName: "",
    postProcessFlag: 0,
    messageFlag: 0
  },
  viewSelectId: "",
  sysParentId: 14,
  taskBus: []
};

const getters = {
  //获取业务树
  getBusTree: state => state.busTree,
  //获取环节数据  包括每页条数 以及总条数
  getCellDataObj: state => state.cellDataObj,
  //获取环节运行状态
  getCellZt: state => state.cellZt,
  // 获取函数模型弹出层显示隐藏状态
  getModelDialogVisable: state => state.modelDialogVisable,
  // 获取前置任务弹出层显示隐藏状态
  getDependDialogVisable: state => state.dependDialogVisable,
  //获取任务新增编辑的弹出层
  getTaskAddDialog: state => state.taskAddDialog,
  //获取模型数据  包括每页条数 以及总条数
  getModelListObj: state => state.modelListObj,
  //获取环节详细信息
  getCellDetailObj: state => state.cellDetailObj,
  //获取当前选中的业务id
  getViewSelectId: state => state.viewSelectId,
  //获取数据库中默认的业务树的pId
  getSysParentId: state => state.sysParentId,
  //获取新增任务所需的业务数据
  getTaskBus: state => state.taskBus
};
const mutations = {
  //设置业务树
  setBusTree(state, newBusTree) {
    state.busTree = newBusTree;
  },
  //设置环节数据  包括每页条数 以及总条数
  setCellDataObj(state, newCellDataObj) {
    state.cellDataObj = newCellDataObj;
  },
  //设置函数模型弹出层的状态
  setModelDialogVisable(state, newVisable) {
    state.modelDialogVisable = newVisable;
  },
  //设置函数模型弹出层的状态
  setDependDialogVisable(state, newVisable) {
    state.dependDialogVisable = newVisable;
  },
  //设置模型数据  包括每页条数 以及总条数
  setModelList(state, newModelObj) {
    state.modelListObj = newModelObj;
  },
  //设置环节详情
  setCellDetailObj(state, newCellDetail) {
    state.cellDetailObj = { ...state.cellDetailObj, ...newCellDetail };
  },
  resetCellDetail(state, newCellDetail) {
    state.cellDetailObj = newCellDetail;
  },
  //设置当前选中的业务id
  setViewSelectId(state, newViewSelected) {
    state.viewSelectId = newViewSelected;
  },
  //设置新增任务所需的业务树数据
  setTaskBus(state, newData) {
    state.taskBus = newData;
  },
  //设置任务新增、编辑弹出的隐藏显示
  setTaskAddDialog(state, newType) {
    state.taskAddDialog = newType;
  }
};
const actions = {
  //通过接口获取业务树数据
  async getIntBusTree({ commit }) {
    await axios
      .get("/api/taskSeg/listBus")
      .then(res => {
        const resData = res.data;
        const parentObj = resData.filter(
          singleRes => singleRes.checked == 1
        )[0];
        const parentChildren = parentObj.childs;
        const selectedView = parentChildren.filter(
          singleChild => singleChild.checked === 1
        )[0];
        commit("setViewSelectId", selectedView.id);
        commit("setBusTree", res.data);
      })
      .catch(error => {
        console.log("出错了，这是错误信息：" + error);
      });
  },
  //通过接口数据  获取环节列表数据
  async getIntCellData({ commit }, searchObj) {
    await axios
      .post(interfaceObj.taskList, searchObj)
      .then(res => {
        const newCellObj = {};
        newCellObj.cellData = res.data.list;
        newCellObj.dataTotal = res.data.all;
        commit("setCellDataObj", newCellObj);
      })
      .catch(error => {
        console.log("出错了，这是错误信息：" + error);
      });
  },
  //设置函数模型弹出层的状态
  getIntModelDialogVisable({ commit }, newVisable) {
    commit("setModelDialogVisable", newVisable);
  },
  //设置前置环节弹出层的状态
  getIntDependDialogVisable({ commit }, newVisable) {
    commit("setDependDialogVisable", newVisable);
  },
  //通过接口设置
  async getIntModelList({ commit }, searchObj) {
    await axios
      .post(interfaceObj.taskMdlList, searchObj)
      .then(res => {
        const newModelObj = {};
        newModelObj.modelData = res.data.list;
        newModelObj.dataTotal = res.data.all;
        commit("setModelList", newModelObj);
      })
      .catch(error => {
        console.log("出错了，这是错误信息：" + error);
      });
  },
  //环节的详情
  getIntCellDetail({ commit }, newCellDetail) {
    if (newCellDetail === "reset") {
      commit("resetCellDetail", { postProcessFlag: 0, messageFlag: 0 });
    } else {
      commit("setCellDetailObj", newCellDetail);
    }
  },
  //treeId
  getIntViewSelectId({ commit }, newSelectedId) {
    commit("setViewSelectId", newSelectedId);
  },
  //通过接口请求数据
  async getIntTaskBus({ commit }) {
    await axios
      .get(interfaceObj.taskBus)
      .then(res => {
        commit("setTaskBus", res.data);
      })
      .catch(error => {
        console.log(" 出错了，这是错误信息：" + error);
      });
  }
};

export default {
  state,
  getters,
  mutations,
  actions
};
