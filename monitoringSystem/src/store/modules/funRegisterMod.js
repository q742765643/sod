import axios from "axios";
const state = {
  //算法分类
  algoCla: [],
  //所有分类
  allClassify: {
    id: "",
    classifyCnName: "所有分类"
  },
  //算法标签
  AlgoTag: [],
  //开发语言
  devLangArr: [
    { id: 1, name: "C" },
    { id: 1, name: "C++" },
    { id: 1, name: "C#" },
    { id: 1, name: "Java" },
    { id: 1, name: "Python" },
    { id: 1, name: "Fortran" },
    { id: 1, name: "NCL" },
    { id: 1, name: "其他" }
  ],
  //责任单位
  organNameArr: [
    { id: 1, name: "信息中心" },
    { id: 2, name: "气象中心" },
    { id: 3, name: "气候中心" },
    { id: 4, name: "卫星中心" },
    { id: 5, name: "探测中心" },
    { id: 6, name: "公服中心" },
    { id: 7, name: "气科院" },
    { id: 8, name: "其他" }
  ],
  //函数运行操作系统
  osTypeArr: [
    { id: 1, name: "CentOS" },
    { id: 2, name: "Windows" },
    { id: 3, name: "RedHat" },
    { id: 4, name: "Ubuntu" },
    { id: 5, name: "SUSE" }
  ],
  //计算框架
  frameTypeArr: [
    { id: 1, name: "虚拟机" },
    { id: 2, name: "Docker" },
    { id: 3, name: "Spark集群" },
    { id: 4, name: "Storm集群" },
    { id: 5, name: "物理机集群" }
  ],
  //输入输出资料大类
  sourceClassify: [
    { id: "1", name: "地面气象资料", code: "A" },
    { id: "2", name: "高空气象资料", code: "B" },
    { id: "3", name: "海洋气象资料", code: "C" },
    { id: "4", name: "气象辐射资料", code: "D" },
    { id: "5", name: "农业气象和生态气象资料", code: "E" },
    { id: "6", name: "数值分析预报产品", code: "F" },
    { id: "7", name: "大气成分气象资料", code: "G" },
    { id: "8", name: "历史气候代用数据", code: "H" },
    { id: "9", name: "气象灾害数据", code: "P" },
    { id: "10", name: "雷达数据", code: "J" },
    { id: "11", name: "卫星数据", code: "K" },
    { id: "12", name: "科学考察和实验", code: "L" },
    { id: "13", name: "气象服务产品", code: "M" },
    { id: "14", name: "其他", code: "Z" }
  ],
  //根据输入输出资料大类查询出的资料集合
  sourceData: [],
  //参数值来源
  paramSource: [
    { id: 0, name: "手动输入" },
    { id: 1, name: "自动输入" },
    { id: 2, name: "上一环节函数输出" }
  ],
  //是否启用
  isInvalid: [{ id: 0, name: "启用" }, { id: 1, name: "禁用" }],
  //任务重做时是否可修改
  isParamModify: [{ id: 0, name: "可修改" }, { id: 1, name: "不可修改" }],
  //函数注册，修改，升级信息存储
  funForm: {
    funName: "", //函数名称
    version: "", //函数版本
    devLang: "", //开发语言
    dieiFlag: 0, //是否发送DI
    content: "", //函数简介描述
    classifyId: "", //分类id
    classifyName: "", //分类名称
    inputType: 1, //输入或自定义输入
    input: "", //输入
    inputDesc: "", //输入描述
    outputType: 1, //输出或自定义输出
    output: "", //输出
    outputDesc: "", //输出描述
    contributor: "", //贡献者
    organName: "信息中心", //责任单位名称
    organId: 1, //责任单位id
    phoneNum: "", //联系电话
    osType: 1, //操作系统类型1,2,3,4,5
    osName: "CentOs", //操作系统名称
    osVersion: "", //操作系统版本
    osVersionName: "", //操作系统版本
    frameType: 1, //计算框架1,2,3,4,5
    frameName: "虚拟机", //计算框架名称
    cpuCores: "", //最大CPU核数
    memoryGb: "", //最大内存资源
    execTimeSecond: "", //最大执行时长
    depEnv: "", //依赖环境说明
    runCommand: "", //运行命令
    codeLocation: "", //算法源码包位置
    execLocation: "", //服务端执行包
    clientLocation: "", //客户端执行包
    docLocation: "", //说明文档
    libLocation: "", //第三方依赖库
    //注册的时候默认字符
    releaseFlag: 0, //算法是否加载
    approveFlag: 0, //算法是否审核
    deleteFlag: 0, //算法启不启用
    loginUserId: "", //登录人的账号
    loginUsername: "", //登录人的名称（昵称）
    funTags: [], //算法标签
    tags: []
  },
  //函数详情信息
  methodDetail: {}
};

const getters = {
  //获取开发语言
  getDevLang: state => state.devLangArr,
  //获取算法分类
  getAlgoCla: state => state.algoCla,
  //获取算法标签
  getAlgoTag: state => state.AlgoTag,
  //获取责任单位
  getOrganNameArr: state => state.organNameArr,
  //获取函数运行操作系统
  getOsTypeArr: state => state.osTypeArr,
  //获取计算框架
  getFrameTypeArr: state => state.frameTypeArr,
  //获取输入输出资料大类
  getSourceClassify: state => state.sourceClassify,
  //获取资料数据
  getSourceData: state => state.sourceData,
  //获取参数来源
  getParamSource: state => state.paramSource,
  //是否启用
  getIsInvalid: state => state.isInvalid,
  //任务重做时是否可以修改
  getIsParamModify: state => state.isParamModify,
  //获取所有分类
  getAllClassify: state => state.allClassify,
  //获取函数注册，修改，升级信息存储
  getFunInfo: state => state.funForm,
  //获取函数的基本信息
  getMethodDetail: state => state.methodDetail
};

const mutations = {
  //设置算法分类
  setAlgoCla(state, newAlgoCla) {
    state.algoCla = newAlgoCla;
  },
  //设置算法分类
  setAlgoTag(state, newAlgoTag) {
    state.AlgoTag = newAlgoTag;
  },
  //设置查询出的资料数据
  setSourceDate(state, newSourceData) {
    state.sourceData = newSourceData;
  },
  //设置函数的基本信息
  setMethodDetail(state, methodDetailObj) {
    state.methodDetail = methodDetailObj;
  }
};

const actions = {
  //通过接口请求算法分类
  async getIntfaceAlgoCla({ commit }, classify) {
    const response = await axios.get("/api/function/list/algoCla");
    let resData = response.data;
    if (classify) {
      resData = [classify, ...resData];
    }
    commit("setAlgoCla", resData);
  },
  //通过接口请求算法标签
  async getIntfaceAlgoTag({ commit }) {
    const response = await axios.get("/api/function/list/algoTag");
    commit("setAlgoTag", response.data);
  },
  //通过接口查询资料数据
  async getIntSourceData({ commit }, params) {
    const response = await axios.get(
      `/api/function/list/mgtDatum?datumName=${params.datumName}&datumCode=${
        params.datumCode
      }&currentPage=${params.currentPage}&pageSize=${params.pageSize}`
    );
    commit("setSourceDate", response.data);
  },
  async getIntMethodDetail({ commit }, methodId) {
    await axios.get(`/api/function/detail/${methodId}`).then(res => {
      commit("setMethodDetail", res.data);
    });
  }
};

export default {
  state,
  getters,
  mutations,
  actions
};
