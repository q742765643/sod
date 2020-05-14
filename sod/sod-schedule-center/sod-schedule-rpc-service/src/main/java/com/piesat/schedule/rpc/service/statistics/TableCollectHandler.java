package com.piesat.schedule.rpc.service.statistics;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.datatable.TableDataStatisticsDao;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.datatable.TableDataStatisticsEntity;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.schedule.client.api.client.handler.base.BaseHandler;
import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.mapstruct.JobInfoMapstruct;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yaya
 * @description TODO
 * @date 2020/5/4 10:16
 */

@Service("tableCollectHandler")
public class TableCollectHandler  implements BaseHandler {

    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobInfoMapstruct jobInfoMapstruct;
    @Autowired
    private DatabaseDao databaseDao;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private TableDataStatisticsDao tableDataStatisticsDao;

    private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void init(){
        String id = "e10a1bbed9caea3540b4c3d2d7bf1331";
        JobInfoEntity jobInfoEntity = (JobInfoEntity)jobInfoService.getJobById(id);
        if(jobInfoEntity == null){
            jobInfoEntity = new JobInfoEntity();
            jobInfoEntity.setId(id);
            jobInfoEntity.setType("JOB");
            jobInfoEntity.setExecutorHandler("tableCollectHandler");
            jobInfoEntity.setJobCron("0 0 1 * * ?");
            jobInfoEntity.setTaskName("日增量统计任务");
            jobInfoService.saveDto(jobInfoMapstruct.toDto(jobInfoEntity));
        }
    }

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        getTimeRange();
        StringBuffer msg = new StringBuffer();
        List<DatabaseEntity> databaseEntities = databaseDao.findAll();
        if(databaseEntities != null && databaseEntities.size()>0){
            for(DatabaseEntity databaseEntity : databaseEntities){
                try {
                    if(databaseEntity.getDatabaseDefine().getUserDisplayControl().intValue() != 1){
                        continue;
                    }
                    String databaseType = databaseEntity.getDatabaseDefine().getDatabaseType();
                    String driverClassName = databaseEntity.getDatabaseDefine().getDriverClassName();
                    String databaseUrl = databaseEntity.getDatabaseDefine().getDatabaseUrl();
                    String databasePort = databaseEntity.getDatabaseDefine().getDatabasePort();
                    String databaseInstance = databaseEntity.getDatabaseDefine().getDatabaseInstance();
                    String schemaName = databaseEntity.getSchemaName();

                    List<Map<String, Object>> dataTableList = dataTableService.getByDatabaseId(databaseEntity.getId());
                    if(dataTableList == null || dataTableList.size() == 0){
                        continue;
                    }

                    //获取数据库管理账户
                    DatabaseAdministratorEntity databaseAdministratorEntity = null;
                    Set<DatabaseAdministratorEntity> databaseAdministratorList = databaseEntity.getDatabaseDefine().getDatabaseAdministratorList();
                    for(DatabaseAdministratorEntity databaseAdministratorEntity1 : databaseAdministratorList){
                        if(databaseAdministratorEntity1.getIsManager()){
                            databaseAdministratorEntity = databaseAdministratorEntity1;
                            break;
                        }
                    }

                    //获取链接
                    DatabaseDcl databaseDcl = null;
                    if("xugu".equalsIgnoreCase(databaseType)){
                        Xugu xugu = new Xugu(databaseUrl, databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                        databaseDcl = xugu;
                    }else if("gbase8a".equalsIgnoreCase(databaseType)){
                        Gbase8a gbase8a = new Gbase8a(databaseUrl,databaseAdministratorEntity.getUserName(),databaseAdministratorEntity.getPassWord());
                        databaseDcl = gbase8a;
                    }

                    Map<String,String>  tableCollectInfo = new HashMap<String,String>();
                    for(int i=0;i<dataTableList.size();i++){
                        String begin_time = "";
                        String end_time = "";
                        String record_count = "";
                        String day_total = "";
                        String sql = "";
                        Map<String,Object> tableInfo = dataTableList.get(i);
                        String table_name = String.valueOf(tableInfo.get("table_name"));
                        msg.append("定时统计：").append(databaseEntity.getDatabaseDefine().getDatabaseName()+"_"+databaseEntity.getDatabaseName()+"["+dataTableList.size()+"/"+i+"]"+":"+table_name);

                        //判断昨天数据是否已经统计入库
                        List<TableDataStatisticsEntity> tableDataStatisticsEntities = tableDataStatisticsDao.findByDatabaseIdAndTableIdAndStatisticDate(databaseEntity.getId(), String.valueOf(tableInfo.get("id")), yesterdayZero);
                        if(tableDataStatisticsEntities != null && tableDataStatisticsEntities.size()>0){
                            continue;
                        }

                        //不统计值表数据
                        if("E".equals(String.valueOf(tableInfo.get("db_table_type"))) && String.valueOf(tableInfo.get("storage_type")).contains("K")){
                            continue;
                        }
                        String value = tableCollectInfo.get("table_name");

                        //日候旬月年  手动赋值
                        if(String.valueOf(table_name).contains("1981_2010")){
                            //tableCollectInfo.put(String.valueOf(tableInfo.get("TABLE_NAME")),"1981-01-01 00:00:00"+","+"2010-01-01 00:00:00"+","+"10000"+","+"10000");
                            begin_time = "1981-01-01 00:00:00";
                            end_time = "2010-01-01 00:00:00";
                            record_count = "10000";
                            day_total = "10000";
                        }else if(value !=null){
                            begin_time = value.split(",")[0];
                            end_time = value.split(",")[1];
                            record_count = value.split(",")[2];
                            day_total = value.split(",")[3];
                        }else {
                            try {
                                //获取总记录数
                                record_count = databaseDcl.queryRecordNum(databaseEntity.getSchemaName(),table_name);
                                //最早记录的时间
                                begin_time = databaseDcl.queryMinTime(databaseEntity.getSchemaName(),table_name,"D_DATETIME");
                                //最近记录的时间
                                end_time = databaseDcl.queryMaxTime(databaseEntity.getSchemaName(),table_name,"D_DATETIME");
                                //获取日增量
                                day_total = databaseDcl.queryIncreCount(databaseEntity.getSchemaName(),table_name,"D_DATETIME",yesterdayZero,todayZero);
                                tableCollectInfo.put(table_name,begin_time+","+end_time+","+record_count+","+day_total);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        //入库
                        TableDataStatisticsEntity tableDataStatisticsEntity = new TableDataStatisticsEntity();
                        tableDataStatisticsEntity.setTableId(String.valueOf(tableInfo.get("id")));
                        tableDataStatisticsEntity.setDatabaseId(databaseEntity.getId());
                        tableDataStatisticsEntity.setStatisticTime(sdf.format(new Date()));
                        tableDataStatisticsEntity.setStatisticDate(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",yesterdayZero));
                        //tableDataStatisticsEntity.setStatisticDate(yesterdayZero);
                        if(StringUtils.isNotNullString(begin_time)){
                            tableDataStatisticsEntity.setBeginTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",begin_time));
                        }
                        if(StringUtils.isNotNullString(end_time)){
                            tableDataStatisticsEntity.setEndTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",end_time));
                        }
                        if(StringUtils.isNotNullString(record_count)){
                            tableDataStatisticsEntity.setRecordCount(Double.valueOf(record_count));
                        }
                        if(StringUtils.isNotNullString(day_total)){
                            tableDataStatisticsEntity.setDayTotal(Integer.valueOf(day_total));
                        }
                        tableDataStatisticsDao.saveNotNull(tableDataStatisticsEntity);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }


    //昨天0点
    String yesterdayZero = "";
    Date  yesterdayZeroDate = null;
    //今天0点
    String todayZero = "";



    public void getTimeRange() {
        Date date =  new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Long oneday = Long.parseLong(String.valueOf(1000 * 60 * 60 * 24));
        yesterdayZeroDate = new Date(date.getTime()-oneday);
        yesterdayZero = sdf.format(yesterdayZeroDate);
        todayZero = sdf.format(date);
    }
}
