package com.piesat.schedule.rpc.service.mmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.http.HttpUtils;
import com.piesat.schedule.dao.mmd.ComMetadataSyncCfgDao;
import com.piesat.schedule.dao.mmd.ComMetadataSyncRecordDao;
import com.piesat.schedule.entity.mmd.ComMetadataSyncCfgEntity;
import com.piesat.schedule.entity.mmd.ComMetadataSyncRecordEntity;
import com.piesat.schedule.mapper.mmd.ComMetadataSyncMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.mmd.ComMetadataSyncService;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncCfgDto;
import com.piesat.schedule.rpc.dto.mmd.ComMetadataSyncRecordDto;
import com.piesat.schedule.rpc.mapstruct.mmd.ComMetaDataSyncRecordMapstruct;
import com.piesat.schedule.rpc.mapstruct.mmd.ComMetadataSyncCfgMapstruct;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * 公共元数据同步配置
 *
 * @author wlg
 * @description
 * @date 2020年2月6日下午5:19:19
 */
@Service
public class ComMetadataSyncServiceImpl extends BaseService<ComMetadataSyncCfgEntity> implements ComMetadataSyncService {

    @Autowired
    private ComMetadataSyncCfgDao comMetadataSyncCfgDao;

    @Autowired
    private ComMetadataSyncRecordDao comMetadataSyncRecordDao;

    @Autowired
    private ComMetadataSyncCfgMapstruct comMetadataSyncCfgMapstruct;

    @Autowired
    private ComMetaDataSyncRecordMapstruct comMetaDataSyncRecordMapstruct;

    @Autowired
    private ComMetadataSyncMapper comMetadataSyncMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private JobInfoService jobInfoService;

    @GrpcHthtClient
    private DictDataService dictDataService;

    @Value("${mmd.userName}")
    private String userName;

    @Value("${mmd.password}")
    private String password;


    @Value("${are.userName}")
    private String areUserName;

    @Value("${are.password}")
    private String arePassword;

    @Override
    public BaseDao<ComMetadataSyncCfgEntity> getBaseDao() {
        return comMetadataSyncCfgDao;
    }

    /**
     * 分页查询
     *
     * @param pageForm
     * @return
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 15:48
     */
    @Override
    public PageBean findPageData(PageForm<ComMetadataSyncCfgDto> pageForm) throws Exception {

        ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder specificationBuilder = new SimpleSpecificationBuilder();
        if (StringUtils.isNotNullString(ce.getTableName())) {
            specificationBuilder.add("tableName", SpecificationOperator.Operator.likeAll.name(), ce.getTableName());
        }

        if (StringUtils.isNotNullString(ce.getTaskName())) {
            specificationBuilder.add("taskName", SpecificationOperator.Operator.likeAll.name(), ce.getTaskName());
        }

        Specification specification = specificationBuilder.generateSpecification();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageBean pageBean = this.getPage(specification, pageForm, sort);
        List<ComMetadataSyncCfgEntity> ces = (List<ComMetadataSyncCfgEntity>) pageBean.getPageData();
        List<DictDataDto> dictData = this.dictDataService.selectDictDataByType("sys_mmd_sync_table");
        for (ComMetadataSyncCfgEntity c : ces) {
            for (DictDataDto d : dictData) {
                if (c.getTableName().equals(d.getDictValue())) {
                    c.setDescription(d.getRemark());
                }
            }
        }
        pageBean.setPageData(comMetadataSyncCfgMapstruct.toDto(ces));
        return pageBean;
    }

    /**
     * 添加同步配置
     *
     * @param cd
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 16:16
     */
    @Override
    @Transactional
    public void addConfig(ComMetadataSyncCfgDto cd) throws Exception {
        ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(cd);
        this.saveNotNull(ce);
        try {

            jobInfoService.start(cd);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 主键查询
     *
     * @param id
     * @return
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 16:35
     */
    @Override
    public ComMetadataSyncCfgDto findByPk(String id) throws Exception {
        ComMetadataSyncCfgEntity ce = this.getById(id);
//		ComMetadataSyncCfgEntity ce = comMetadataSyncCfgDao.findById(id).orElse(null);
        return comMetadataSyncCfgMapstruct.toDto(ce);
    }

    /**
     * 编辑同步任务
     *
     * @param cd
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 17:04
     */
    @Override
    @Transactional
    public void editConfig(ComMetadataSyncCfgDto cd) throws Exception {
        ComMetadataSyncCfgEntity ce = comMetadataSyncCfgMapstruct.toEntity(cd);
        this.saveNotNull(ce);
        try {
            jobInfoService.start(cd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除公共元数据同步任务
     *
     * @param ids
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 17:13
     */
    @Override
    @Transactional
    public void delConfig(String ids) throws Exception {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            comMetadataSyncCfgDao.deleteById(id);
        }
        try {
            jobInfoService.stopByIds(Arrays.asList(idArr));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取公共元数据同步记录分页数据
     *
     * @param pageForm
     * @return
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 17:51
     */
    @Override
    public PageBean findRecordPage(PageForm<ComMetadataSyncRecordDto> pageForm) throws Exception {
        ComMetadataSyncRecordEntity ce = comMetaDataSyncRecordMapstruct.toEntity(pageForm.getT());

        PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        //获取结果集
        List<ComMetadataSyncRecordEntity> data = comMetadataSyncMapper.selectRecordList(ce);
        PageInfo<ComMetadataSyncRecordEntity> pageInfo = new PageInfo<>(data);

        List<ComMetadataSyncRecordDto> dtoData = comMetaDataSyncRecordMapstruct.toDto(pageInfo.getList());
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), dtoData);
        return pageBean;
    }

    /**
     * 删除公共元数据同步记录
     *
     * @param ids
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020-02-17 17:52
     */
    @Override
    @Transactional
    public void delRecord(String ids) throws Exception {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            comMetadataSyncRecordDao.deleteById(id);
        }
    }

    /**
     * 立即同步公共元数据
     *
     * @param ids
     * @return
     * @throws Exception
     * @description 先判断是根据同步类型 还是id 进行同步的
     * 如果是根据同步类型 根据同步类型 查找id
     * <p>
     * 逻辑 : 1.获取同步url
     * 2.发送请求,查看是否能获取到数据
     * 3.获取到数据,查看同步配置是全量同步还是增量同步
     * 4.如果是全量同步,删表,查数据
     * 5.如果是增量同步 更新数据 或者是删除指定数据
     * 6.如果没有同步类型,先删除数据再同步数据
     * @author wlg
     * @date 2020-02-18 11:38
     */
    @Override
    @Transactional
    public ResultT syncDataNow(String ids, String apiType, Integer oprType) throws Exception {
        try {
            if (StringUtil.isEmpty(ids) && StringUtil.isEmpty(apiType)) return ResultT.failed("参数不能为空");
            List<String> idList = new ArrayList<>();
            String[] idArr = null;
            //
//			if(!StringUtil.isEmpty(apiType)) {
//				List<ComMetadataSyncCfgEntity> ceList = comMetadataSyncCfgDao.findByApiType(apiType);
//				for(ComMetadataSyncCfgEntity ce:ceList) {
//					idList.add(ce.getId());
//				}
//			}else {
            idArr = ids.split(",");
            idList = Arrays.asList(idArr);
//			}
            List<ComMetadataSyncRecordEntity> strList = new ArrayList<>();
            for (String id : idList) {
                if (StringUtil.isEmpty(id)) continue;

                //获取同步配置
                ComMetadataSyncCfgEntity ce = comMetadataSyncCfgDao.findById(id).orElse(null);
                if (null == ce) return ResultT.failed("公共元数据同步任务配置不存在");

                String tableName = ce.getTableName();

                //添加同步任务记录
                ComMetadataSyncRecordEntity cre = new ComMetadataSyncRecordEntity();
                cre.setSyncTableName(tableName);
                cre.setStartTime(new Date());
                cre.setSyncType("1".equals(ce.getApiType()) ? "全量同步" : "增量同步");
                cre.setSyncModel(oprType == 2 ? "手动同步" : "自动同步");

                //获取同步接口
                String url = ce.getApiUrl();
                if (StringUtil.isEmpty(url)) return ResultT.failed("同步任务【" + ce.getTaskName() + "】的接口url为空");

                //获取同步数据
                String[] split = url.split("\\?");
//				if (split.length!=2){
//					return ResultT.failed("同步任务【"+ce.getTaskName()+"】url格式不正确");
//				}
                String interfaceId = null;
                if (split.length == 2) {
                    String s1 = split[1];
                    String[] split1 = s1.split("&");
                    for (String p : split1) {
                        if (p.contains("interfaceId")) {
                            url = split[0];
                            interfaceId = p.substring(p.lastIndexOf("=") + 1);
                        }
                    }
                }

//				String result = HttpUtils.sendGet(url,"");
                String result = "";
                if (StringUtils.isEmpty(interfaceId)) {
                    result = HttpUtils.arcGet(url, areUserName, arePassword);
                } else {
                    result = HttpUtils.mmdGet(url, interfaceId, userName, password);
                }
                if (StringUtil.isEmpty(result))
                    return ResultT.failed("同步任务【" + ce.getTaskName() + "】的接口url【" + url + "】的返回值为空");

                JSONObject obj = new JSONObject();
                try {
                    obj = JSON.parseObject(result);
                } catch (Exception e) {
                    return ResultT.failed("同步任务【" + ce.getTaskName() + "】的接口url【" + url + "】的返回值异常,返回值为:" + result);
                }

                if (StringUtil.isEmpty(ce.getApiDataKey()))
                    return ResultT.failed("请配置同步任务【" + ce.getTaskName() + "】的接口关键字");
                JSONArray arr = obj.getJSONArray(ce.getApiDataKey());
                if (null == arr)
                    return ResultT.failed("同步任务【" + ce.getTaskName() + "】的接口url【" + url + "】的返回值异常,返回值为:" + result);

                Map<String, String> fieldInfoMap = getFieldInfo(tableName);

                if (null == fieldInfoMap) {
                    cre.setSyncRecordNum(0);
                    cre.setStopTime(new Date());
                    cre.setRunState("同步失败");
                    cre.setFailReason("未获取到表结构信息");
                    comMetadataSyncRecordDao.save(cre);

                    return ResultT.failed("同步任务【" + ce.getTaskName() + "】没有从数据库中获取到表结构信息");
                }

                Boolean succeed = true;
                //查看同步类型 1:全量同步,2:增量同步
                if ("1".equals(ce.getApiType())) comMetadataSyncMapper.clearTable(tableName);

                for (int i = 0; i < arr.size(); i++) {
                    JSONObject objEntity = arr.getJSONObject(i);
                    Map<String, String> row = get1RowData(objEntity, fieldInfoMap);
                    Map<String, Object> param = new HashMap<>();
                    param.put("tableName", tableName);
                    param.put("row", row);

                    if ("1".equals(ce.getApiType())) {
                        comMetadataSyncMapper.insert1Row(param);

                    } else if ("2".equals(ce.getApiType()) &&
                            ("update".equals(getFiledValue(row, "C_FLOW_TYPE")) || "update".equals(getFiledValue(row, "C_OPT_TYPE")))) {
                        String pk = ce.getPrimaryKey();
                        if (StringUtil.isEmpty(pk)) {
                            succeed = false;
                            cre.setFailReason("主键配置缺失");
                        } else {
                            param.put("pk", pk);
                            param.put("pkValue", row.get(pk));
                            comMetadataSyncMapper.update1Row(param);
                        }

                    } else if ("2".equals(ce.getApiType())
                            && ("delete".equals(getFiledValue(row, "C_FLOW_TYPE")) || "delete".equals(getFiledValue(row, "C_OPT_TYPE")))) {
                        String pk = ce.getPrimaryKey();
                        if (StringUtil.isEmpty(pk)) {
                            succeed = false;
                            cre.setFailReason("主键配置缺失");
                        } else {
                            param.put("pk", pk);
                            param.put("pkValue", row.get(pk));
                            comMetadataSyncMapper.del1Row(param);
                        }

                    } else {
                        String pk = ce.getPrimaryKey();
                        if (StringUtil.isEmpty(pk)) {
                            succeed = false;
                            cre.setFailReason("主键配置缺失");
                        } else {
                            param.put("pk", pk);
                            param.put("pkValue", row.get(pk));
                            comMetadataSyncMapper.del1Row(param);
                            comMetadataSyncMapper.insert1Row(param);
                        }
                    }

                }

                cre.setRunState(succeed ? "同步成功" : "同步失败");
                cre.setStopTime(new Date());
                cre.setSyncRecordNum(succeed ? arr.size() : 0);
                comMetadataSyncRecordDao.save(cre);
                StringBuffer sb = new StringBuffer();
                sb.append(ce.getTaskName()).append(":").append(succeed ? "同步成功（" + arr.size() + "）条" : "同步失败");
                strList.add(cre);
            }
            return ResultT.success(strList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }

    }

    /**
     * 获取map值
     *
     * @param row
     * @param field
     * @return
     * @description
     * @author wlg
     * @date 2020年2月18日下午3:37:47
     */
    private Object getFiledValue(Map<String, String> row, String field) {
        for (String k : row.keySet()) {
            if (k.equalsIgnoreCase(field)) {
                return row.get(k);
            }
        }
        return null;
    }

    /**
     * 获取一行数据
     *
     * @param obj
     * @param filedInfoMap
     * @return
     * @description
     * @author wlg
     * @date 2020年2月18日下午3:10:13
     */
    private Map<String, String> get1RowData(JSONObject obj, Map<String, String> filedInfoMap) throws Exception {
        Map<String, String> row = new HashMap<>();
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey().toLowerCase();
            String value = entry.getValue() == null ? null : entry.getValue().toString();

            //转换时间类型
            for (Map.Entry<String, String> fieldInfo : filedInfoMap.entrySet()) {
                String fieldName = fieldInfo.getKey();
                if (key.equalsIgnoreCase(fieldName)) {
                    //判断是否是时间格式，处理时间格式
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if ("DATETIME".equalsIgnoreCase(fieldInfo.getValue())) {
                        try {
                            Date date = new Date((Long) entry.getValue());
                            String time = sdf.format(date);
                            row.put(fieldName, time);
                            sdf.setLenient(false);
                        } catch (Exception e) {
                            row.put(fieldName, value);
                        }
                    } else {
                        row.put(fieldName, value);
                    }
                }
            }
        }
        return row;
    }

    /**
     * 获取表的元数据
     *
     * @param tableName
     * @return
     * @throws Exception
     * @description
     * @author wlg
     * @date 2020年2月18日下午2:21:15
     */
    private Map<String, String> getFieldInfo(String tableName) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlSessionFactory.openSession().getConnection();
            String sql = "select * from " + tableName;

            ps = conn.prepareStatement(sql);
            ResultSetMetaData rsmd = ps.getMetaData();
            if (null != rsmd) {
                Map<String, String> map = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
                }
                return map;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			if(null != conn) conn.close();
            if (null != ps) ps.close();
        }
        return null;

    }

}
