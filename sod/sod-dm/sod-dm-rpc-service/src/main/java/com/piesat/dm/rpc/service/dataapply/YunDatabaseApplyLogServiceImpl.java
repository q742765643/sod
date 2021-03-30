package com.piesat.dm.rpc.service.dataapply;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.MyBeanUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataapply.YunDatabaseApplyLogDao;
import com.piesat.dm.entity.dataapply.YunDatabaseApplyLogEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataapply.YunDatabaseApplyLogService;
import com.piesat.dm.rpc.api.dataclass.DataClassService;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyLogDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.mapper.dataapply.YunDatabaseApplyLogMapper;
import com.piesat.ucenter.dao.system.UserDao;
import com.piesat.ucenter.entity.system.UserEntity;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public  class YunDatabaseApplyLogServiceImpl extends BaseService<YunDatabaseApplyLogEntity> implements YunDatabaseApplyLogService {


    @Autowired
    private YunDatabaseApplyLogDao yunDatabaseApplyLogDao;

    @Autowired
    private YunDatabaseApplyLogMapper yunDatabaseApplyLogMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;
    @Autowired
    private DataClassService dataClassService;

    @GrpcHthtClient
    private UserDao userDao;



    @Override
    public YunDatabaseApplyLogDto addLogEdit(Map<String, String[]> parameterMap) {
        YunDatabaseApplyLogEntity yunDatabaseApplyLogEntity = new YunDatabaseApplyLogEntity();
        MyBeanUtils.getObject(yunDatabaseApplyLogEntity,parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
//            if (StringUtils.isNotEmpty(data[0])) {
                JSONObject object = JSONObject.parseObject(data[0]);
                String userId = (String) object.get("userId");
                yunDatabaseApplyLogEntity.setUserId(userId);
//            }
        }
        yunDatabaseApplyLogEntity = this.saveNotNull(yunDatabaseApplyLogEntity);
        return yunDatabaseApplyLogMapper.toDto(yunDatabaseApplyLogEntity);
    }
    @Override
    public YunDatabaseApplyLogDto addLogEdit1(Map<String, String[]> parameterMap,String logId,String examineMaterial) {
        YunDatabaseApplyLogEntity yunDatabaseApplyLogEntity = new YunDatabaseApplyLogEntity();
        MyBeanUtils.getObject(yunDatabaseApplyLogEntity,parameterMap);
        String[] data = parameterMap.get("data");
        if (data != null && data.length > 0) {
//            if (StringUtils.isNotEmpty(data[0])) {
            JSONObject object = JSONObject.parseObject(data[0]);
            String userId = (String) object.get("userId");
            yunDatabaseApplyLogEntity.setUserId(userId);
//            }
        }
        yunDatabaseApplyLogEntity.setLogId(logId);
        yunDatabaseApplyLogEntity.setExamineMaterial(examineMaterial);
        yunDatabaseApplyLogEntity = this.save(yunDatabaseApplyLogEntity);
        return yunDatabaseApplyLogMapper.toDto(yunDatabaseApplyLogEntity);
    }
    @Override
    public YunDatabaseApplyLogDto saveLog(YunDatabaseApplyLogDto yunDatabaseApplyLogDto) {
        YunDatabaseApplyLogEntity yunDatabaseApplyLogEntity = yunDatabaseApplyLogMapper.toEntity(yunDatabaseApplyLogDto);
        yunDatabaseApplyLogEntity = this.save(yunDatabaseApplyLogEntity);
        return yunDatabaseApplyLogMapper.toDto(yunDatabaseApplyLogEntity);
    }
    @Override
    public  List<YunDatabaseApplyLogDto> getByLogId(String logId){
        List<YunDatabaseApplyLogEntity> yunDatabaseApplyLogEntities = yunDatabaseApplyLogDao.findByLogId(logId);
        return  yunDatabaseApplyLogMapper.toDto(yunDatabaseApplyLogEntities);
    }
    @Override
    public void deleteByLogId(String logId) {
        List<YunDatabaseApplyLogDto> yunDatabaseApplyLogDtos = this.getByLogId(logId);
        for(int i = 0;i<yunDatabaseApplyLogDtos.size();i++) {

            YunDatabaseApplyLogDto filePathN = (YunDatabaseApplyLogDto)yunDatabaseApplyLogDtos.get(i);
            String filePath = filePathN.getExamineMaterial();
            String id = filePathN.getId();
//            System.out.println(filePath);

            try {
//                String filePath = filePathAndName;
                filePath = filePath.toString();
                java.io.File myDelFile = new java.io.File(filePath);
                myDelFile.delete();
            } catch (Exception e) {
                System.out.println("删除文件操作出错");
                e.printStackTrace();
            }
            this.delete(id);
        }

    }
    @Override
    public BaseDao<YunDatabaseApplyLogEntity> getBaseDao() {
        return this.yunDatabaseApplyLogDao;
    }
}
