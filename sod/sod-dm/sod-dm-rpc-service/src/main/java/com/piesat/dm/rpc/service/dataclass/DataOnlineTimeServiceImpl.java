package com.piesat.dm.rpc.service.dataclass;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.dao.dataclass.DataOnlineTimeDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.entity.dataclass.DataOnlineTimeEntity;
import com.piesat.dm.mapper.MybatisQueryMapper;
import com.piesat.dm.rpc.api.dataclass.DataOnlineTimeService;
import com.piesat.dm.rpc.dto.dataclass.DataOnlineTimeDto;
import com.piesat.dm.rpc.mapper.dataclass.DataOnlineTimeMapper;
import com.piesat.schedule.rpc.service.statistics.TableCollectHandler;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:22
 */
@Service
public class DataOnlineTimeServiceImpl extends BaseService<DataOnlineTimeEntity> implements DataOnlineTimeService {

    @Autowired
    private DataOnlineTimeDao dataOnlineTimeDao;

    @Autowired
    private DataOnlineTimeMapper dataOnlineTimeMapper;

    @Autowired
    private MybatisQueryMapper mybatisQueryMapper;

    @Autowired
    private DataTableDao dataTableDao;

    @Autowired
    private TableCollectHandler tableCollectHandler;



    @Override
    public BaseDao<DataOnlineTimeEntity> getBaseDao() {
        return this.dataOnlineTimeDao;
    }


    @Override
    public DataOnlineTimeDto saveDto(DataOnlineTimeDto dataOnlineTimeDto) {
        DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeMapper.toEntity(dataOnlineTimeDto);
        dataOnlineTimeEntity = this.saveNotNull(dataOnlineTimeEntity);
        return this.dataOnlineTimeMapper.toDto(dataOnlineTimeEntity);
    }

    @Override
    public void deleteByDataClassId(String dataClassId) {
        this.dataOnlineTimeDao.deleteByDataClassId(dataClassId);
    }

    @Override
    public PageBean onLineList(PageForm<Map<String,String>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(),pageForm.getPageSize());

        List<DataOnlineTimeEntity> dataOnlineTimeEntities = this.getAll();
        List<Map<String,Object>> lists = mybatisQueryMapper.onLineList(pageForm.getT());
        for(int i=0; i<lists.size();i++){
            Map<String, Object> stringObjectMap = lists.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(stringObjectMap.get("BEGIN_TIME") != null){
                stringObjectMap.put("BEGIN_TIME",sdf.format(stringObjectMap.get("BEGIN_TIME")));
            }
            if(stringObjectMap.get("END_TIME") != null){
                stringObjectMap.put("END_TIME", sdf.format(stringObjectMap.get("END_TIME")));
            }
            for (int j = 0; j < dataOnlineTimeEntities.size(); j++) {
                DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeEntities.get(j);
                if (dataOnlineTimeEntity.getDataClassId().equals(stringObjectMap.get("DATA_CLASS_ID"))) {
                    stringObjectMap.put("obj",dataOnlineTimeEntity);
                    if ("0".equals(String.valueOf(dataOnlineTimeEntity.getIsUse()))) continue;
                    if ("today".equals(dataOnlineTimeEntity.getEndTimeFlag())) {
                        Date date1 = new Date();
                        String format = sdf.format(date1);
                        stringObjectMap.put("END_TIME",format);
                    }else if (dataOnlineTimeEntity.getEndTime()!=null){
                        stringObjectMap.put("END_TIME", sdf.format(dataOnlineTimeEntity.getEndTime()));
                    }
                    if (dataOnlineTimeEntity.getBeginTime()!=null){
                        stringObjectMap.put("BEGIN_TIME",sdf.format(dataOnlineTimeEntity.getBeginTime()));
                    }
                }
            }

        }

        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean=new PageBean(pageInfo.getTotal(),pageInfo.getPages(),lists);
        return  pageBean;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DataOnlineTimeDto dataOnlineTimeDto) {
        this.deleteByDataClassId(dataOnlineTimeDto.getDataClassId());
        this.saveDto(dataOnlineTimeDto);
    }

    @Override
    public DataOnlineTimeDto updateTime(String dataClassId) {
        DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeDao.findByDataClassId(dataClassId);
//        dataOnlineTimeEntity.setExamineStatus(examineStatus);
        dataOnlineTimeEntity.setExecuteTime(new Date());
        dataOnlineTimeEntity = this.saveNotNull(dataOnlineTimeEntity);
        return dataOnlineTimeMapper.toDto(dataOnlineTimeEntity);
    }
    @Override
    public void executeNew(DataOnlineTimeDto dataOnlineTimeDto){
        String dataClassId = dataOnlineTimeDto.getDataClassId();
//        SimpleDateFormat formater = new SimpleDateFormat();
//        System.out.println("+++++++++++++++++"+dataOnlineTimeDto.getBoundEndTime());
//        System.out.println("+++++++++++++++++"+dataOnlineTimeDto.getBoundBeginTime());
//        System.out.println("+++++++++++++++++"+dataOnlineTimeDto.getBoundEndTimeFlag());
        List<Map<String, Object>> dataTableEntities = dataTableDao.getByClassId(dataClassId);
        if(dataTableEntities != null && dataTableEntities.size()>0){
            for(int i=0;i<dataTableEntities.size();i++){
                Map<String, Object> dataTableEntity = dataTableEntities.get(i);
                String tableName = (String) dataTableEntity.get("TABLE_NAME");
                String databaseId = (String) dataTableEntity.get("DATABASE_ID");
                tableCollectHandler.executeNew(tableName,databaseId,dataOnlineTimeDto.getBoundEndTime(),dataOnlineTimeDto.getBoundBeginTime(),dataOnlineTimeDto.getBoundEndTimeFlag());
            }
        }
    }

    @Override
    public DataOnlineTimeDto findByDataClassId(String dataClassId) {
        DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeDao.findByDataClassId(dataClassId);
        return this.dataOnlineTimeMapper.toDto(dataOnlineTimeEntity);
    }

    @Override
    public Map<String, Object> getByClassId(String dataClassId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String,Object>> onLineTimeByClassId = mybatisQueryMapper.onLineTimeByClassId(dataClassId);
        DataOnlineTimeEntity dataOnlineTimeEntity = dataOnlineTimeDao.findByDataClassId(dataClassId);
        result.put("distinctDataBaseInfo",onLineTimeByClassId);
        result.put("dataOnlineTime",dataOnlineTimeEntity);
        return result;
    }
}
