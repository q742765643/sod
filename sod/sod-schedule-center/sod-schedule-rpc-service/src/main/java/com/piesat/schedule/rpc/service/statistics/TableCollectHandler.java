package com.piesat.schedule.rpc.service.statistics;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.utils.DateUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.dao.database.DatabaseDao;
import com.piesat.dm.dao.database.SchemaDao;
import com.piesat.dm.dao.dataclass.DataOnlineTimeDao;
import com.piesat.dm.dao.datatable.DataTableDao;
import com.piesat.dm.dao.datatable.TableDataStatisticsDao;
import com.piesat.dm.entity.database.DatabaseAdministratorEntity;
import com.piesat.dm.entity.database.DatabaseEntity;
import com.piesat.dm.entity.database.SchemaEntity;
import com.piesat.dm.entity.dataclass.DataOnlineTimeEntity;
import com.piesat.dm.entity.datatable.TableDataStatisticsEntity;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.api.datatable.TableDataStatisticsService;
import com.piesat.dm.rpc.dto.datatable.TableDataStatisticsDto;
import com.piesat.schedule.client.api.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.mapstruct.JobInfoMapstruct;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SchemaDao schemaDao;
    private DatabaseDao databaseDao;
    @Autowired
    private DataOnlineTimeDao dataOnlineTimeDao;

    @Autowired
    private DataTableDao dataTableDao;

    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private TableDataStatisticsDao tableDataStatisticsDao;
    @GrpcHthtClient
    private TableDataStatisticsService tableDataStatisticsService;

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
        String newBoundEndTimeFlag = "";
        Date newBoundEndTime = null;
        Date newBoundBeginTime = null;
        List<Object> onlineTimeList;
        getTimeRange();
        StringBuffer msg = new StringBuffer();
        List<SchemaEntity> databaseEntities = schemaDao.findAll();
        List<DataOnlineTimeEntity> dataOnlineTimeEntitys = dataOnlineTimeDao.findAll();
        if(dataOnlineTimeEntitys != null && dataOnlineTimeEntitys.size()>0){
            for (DataOnlineTimeEntity dataOnlineTimeEntity : dataOnlineTimeEntitys){
                String dataClassId = dataOnlineTimeEntity.getDataClassId();
                List<Map<String, Object>> dataTableEntities = dataTableDao.getByClassId(dataClassId);
                if(dataTableEntities != null && dataTableEntities.size()>0){
                    for(int i=0;i<dataTableEntities.size();i++){
                        Map<String, Object> dataTableEntity = dataTableEntities.get(i);
                        String tableName = (String) dataTableEntity.get("TABLE_NAME");
                        String databaseId = (String) dataTableEntity.get("DATABASE_ID");
                    }
                }
            }
        }
        if(databaseEntities != null && databaseEntities.size()>0){
            for(SchemaEntity schemaEntity : databaseEntities) {
                DatabaseDcl databaseDcl = null;
                try {
                    /*if (schemaEntity.getDatabase().getUserDisplayControl().intValue() != 1) {
                        continue;
                    }*/
                    String databaseType = schemaEntity.getDatabase().getDatabaseType();
                    String driverClassName = schemaEntity.getDatabase().getDriverClassName();
                    String databaseUrl = schemaEntity.getDatabase().getDatabaseUrl();
                    String databasePort = schemaEntity.getDatabase().getDatabasePort();
                    String databaseInstance = schemaEntity.getDatabase().getDatabaseInstance();
                    String schemaName = schemaEntity.getSchemaName();

                    List<Map<String, Object>> dataTableList = dataTableService.getByDatabaseId(schemaEntity.getId());
                    if (dataTableList == null || dataTableList.size() == 0) {
                        continue;
                    }

                    //获取数据库管理账户
                    DatabaseAdministratorEntity databaseAdministratorEntity = null;
                    Set<DatabaseAdministratorEntity> databaseAdministratorList = schemaEntity.getDatabase().getDatabaseAdministratorList();
                    for (DatabaseAdministratorEntity databaseAdministratorEntity1 : databaseAdministratorList) {
                        if (databaseAdministratorEntity1.getIsManager()) {
                            databaseAdministratorEntity = databaseAdministratorEntity1;
                            break;
                        }
                    }

                    //获取链接
                    if ("xugu".equalsIgnoreCase(databaseType)) {
                        Xugu xugu = new Xugu(databaseUrl, databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                        databaseDcl = xugu;
                    } else if ("gbase8a".equalsIgnoreCase(databaseType)) {
                        Gbase8a gbase8a = new Gbase8a(databaseUrl, databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                        databaseDcl = gbase8a;
                    }

                    Map<String, String> tableCollectInfo = new HashMap<String, String>();
                    for (int i = 0; i < dataTableList.size(); i++) {
                        String begin_time = "";
                        String end_time = "";
                        String record_count = "";
                        String day_total = "";
                        String sql = "";
                        Map<String, Object> tableInfo = dataTableList.get(i);
                        String table_name = String.valueOf(tableInfo.get("table_name"));
                        String data_class_id = String.valueOf(tableInfo.get("data_class_id"));
                        msg.append("定时统计：").append(schemaEntity.getDatabase().getDatabaseName() + "_" + schemaEntity.getDatabaseName() + "[" + dataTableList.size() + "/" + i + "]" + ":" + table_name);

                        //判断昨天数据是否已经统计入库
                        //List<TableDataStatisticsEntity> tableDataStatisticsEntities = tableDataStatisticsDao.findByDatabaseIdAndTableIdAndStatisticDate(schemaEntity.getId(), String.valueOf(tableInfo.get("id")), yesterdayZeroDate);
                        TableDataStatisticsDto tableDataStatisticsDto = new TableDataStatisticsDto();
                        tableDataStatisticsDto.setDatabaseId(schemaEntity.getId());
                        tableDataStatisticsDto.setTableId(String.valueOf(tableInfo.get("id")));
                        tableDataStatisticsDto.setStatisticDate(yesterdayZeroDate);
                        List<TableDataStatisticsDto> tableDataStatisticsDtos = tableDataStatisticsService.findByParam(tableDataStatisticsDto);
                        if (tableDataStatisticsDtos != null && tableDataStatisticsDtos.size() > 0) {
                            continue;
                        }

                        //不统计值表数据
                        if ("E".equals(String.valueOf(tableInfo.get("db_table_type"))) && String.valueOf(tableInfo.get("storage_type")).contains("K")) {
                            continue;
                        }
                        String value = tableCollectInfo.get("table_name");

                        //日候旬月年  手动赋值
                        if (String.valueOf(table_name).contains("1981_2010")) {
                            //tableCollectInfo.put(String.valueOf(tableInfo.get("TABLE_NAME")),"1981-01-01 00:00:00"+","+"2010-01-01 00:00:00"+","+"10000"+","+"10000");
                            begin_time = "1981-01-01 00:00:00";
                            end_time = "2010-01-01 00:00:00";
                            record_count = "10000";
                            day_total = "10000";
                        } else if (value != null) {
                            begin_time = value.split(",")[0];
                            end_time = value.split(",")[1];
                            record_count = value.split(",")[2];
                            day_total = value.split(",")[3];
                        } else {
                            try {
                                //获取总记录数
                                record_count = databaseDcl.queryRecordNum(schemaEntity.getSchemaName(), table_name);
                                //最早记录的时间
                                begin_time = databaseDcl.queryMinTime(schemaEntity.getSchemaName(), table_name, newBoundBeginTime,"D_DATETIME");
                                //最近记录的时间
                                end_time = databaseDcl.queryMaxTime(schemaEntity.getSchemaName(), table_name, newBoundEndTime,newBoundEndTimeFlag,"D_DATETIME");
                                //获取日增量
                                day_total = databaseDcl.queryIncreCount(schemaEntity.getSchemaName(), table_name, "D_DATETIME", yesterdayZero, todayZero);
                                tableCollectInfo.put(table_name, begin_time + "," + end_time + "," + record_count + "," + day_total);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        //入库
                        TableDataStatisticsEntity tableDataStatisticsEntity = new TableDataStatisticsEntity();
                        tableDataStatisticsEntity.setTableId(String.valueOf(tableInfo.get("id")));
                        tableDataStatisticsEntity.setDatabaseId(schemaEntity.getId());
                        tableDataStatisticsEntity.setStatisticTime(sdf.format(new Date()));
                        tableDataStatisticsEntity.setStatisticDate(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", yesterdayZero));
                        //tableDataStatisticsEntity.setStatisticDate(yesterdayZero);
                        if (StringUtils.isNotNullString(begin_time)) {
                            tableDataStatisticsEntity.setBeginTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", begin_time));
                        }
                        if (StringUtils.isNotNullString(end_time)) {
                            tableDataStatisticsEntity.setEndTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", end_time));
                        }
                        if (StringUtils.isNotNullString(record_count)) {
                            tableDataStatisticsEntity.setRecordCount(Double.valueOf(record_count));
                        }
                        if (StringUtils.isNotNullString(day_total)) {
                            tableDataStatisticsEntity.setDayTotal(Integer.valueOf(day_total));
                        }
                        tableDataStatisticsDao.saveNotNull(tableDataStatisticsEntity);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (databaseDcl != null) {
                        databaseDcl.closeConnect();
                    }
                }
            }
        }

    }

    @Override
    public void executeNew(String newTableName,String newDatabaseId,Date newBoundEndTime,Date newBoundBeginTime,String newBoundEndTimeFlag) {
        getTimeRange();
        StringBuffer msg = new StringBuffer();
        List<SchemaEntity> schemaEntities = schemaDao.findAll();
        if(schemaEntities != null && schemaEntities.size()>0){
            for(SchemaEntity schemaEntity : schemaEntities) {
                DatabaseDcl databaseDcl = null;
                if(!schemaEntity.getId().equalsIgnoreCase(newDatabaseId)){
                    continue;
                }
                try {
                    /*if (schemaEntity.getDatabaseDefine().getUserDisplayControl().intValue() != 1) {
                        continue;
                    }*/
                    String databaseType = schemaEntity.getDatabase().getDatabaseType();
                    String driverClassName = schemaEntity.getDatabase().getDriverClassName();
                    String databaseUrl = schemaEntity.getDatabase().getDatabaseUrl();
                    String databasePort = schemaEntity.getDatabase().getDatabasePort();
                    String databaseInstance = schemaEntity.getDatabase().getDatabaseInstance();
                    String schemaName = schemaEntity.getSchemaName();

                    List<Map<String, Object>> dataTableList = dataTableService.getByDatabaseId(schemaEntity.getId());
                    if (dataTableList == null || dataTableList.size() == 0) {
                        continue;
                    }

                    //获取数据库管理账户
                    DatabaseAdministratorEntity databaseAdministratorEntity = null;
                    Set<DatabaseAdministratorEntity> databaseAdministratorList = schemaEntity.getDatabase().getDatabaseAdministratorList();
                    for (DatabaseAdministratorEntity databaseAdministratorEntity1 : databaseAdministratorList) {
                        if (databaseAdministratorEntity1.getIsManager()) {
                            databaseAdministratorEntity = databaseAdministratorEntity1;
                            break;
                        }
                    }

                    //获取链接
                    if ("xugu".equalsIgnoreCase(databaseType)) {
                        Xugu xugu = new Xugu(databaseUrl, databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                        databaseDcl = xugu;
                    } else if ("gbase8a".equalsIgnoreCase(databaseType)) {
                        Gbase8a gbase8a = new Gbase8a(databaseUrl, databaseAdministratorEntity.getUserName(), databaseAdministratorEntity.getPassWord());
                        databaseDcl = gbase8a;
                    }

                    List<String> arr = databaseDcl.queryTableName(schemaName);

                    Map<String, String> tableCollectInfo = new HashMap<String, String>();
                    for (int i = 0; i < dataTableList.size(); i++) {
                        String begin_time = "";
                        String end_time = "";
                        String record_count = "";
                        String day_total = "";
                        String sql = "";
                        Map<String, Object> tableInfo = dataTableList.get(i);
                        String table_name = String.valueOf(tableInfo.get("table_name"));

                        if(!Arrays.asList(arr).contains(table_name)){
                            continue;
                        }
                        if(!table_name.equalsIgnoreCase(newTableName)){
                            continue;
                        }
//                        String data_class_id = String.valueOf(tableInfo.get("data_class_id"));
                        msg.append("定时统计：").append(schemaEntity.getDatabase().getDatabaseName() + "_" + schemaEntity.getDatabaseName() + "[" + dataTableList.size() + "/" + i + "]" + ":" + table_name);

                        //判断昨天数据是否已经统计入库
                        /*List<TableDataStatisticsEntity> tableDataStatisticsEntities = tableDataStatisticsDao.findByDatabaseIdAndTableIdAndStatisticDate(schemaEntity.getId(), String.valueOf(tableInfo.get("id")), yesterdayZeroDate);
                        TableDataStatisticsDto tableDataStatisticsDto = new TableDataStatisticsDto();
                        tableDataStatisticsDto.setDatabaseId(schemaEntity.getId());
                        tableDataStatisticsDto.setTableId(String.valueOf(tableInfo.get("id")));
                        tableDataStatisticsDto.setStatisticDate(yesterdayZeroDate);
                        List<TableDataStatisticsDto> tableDataStatisticsDtos = tableDataStatisticsService.findByParam(tableDataStatisticsDto);
                        if (tableDataStatisticsDtos != null && tableDataStatisticsDtos.size() > 0) {
                            continue;
                        }*/

                        //不统计值表数据
                        if ("E".equals(String.valueOf(tableInfo.get("db_table_type"))) && String.valueOf(tableInfo.get("storage_type")).contains("K")) {
                            continue;
                        }
                        String value = tableCollectInfo.get("table_name");

                        //日候旬月年  手动赋值
                        if (String.valueOf(table_name).contains("1981_2010")) {
                            //tableCollectInfo.put(String.valueOf(tableInfo.get("TABLE_NAME")),"1981-01-01 00:00:00"+","+"2010-01-01 00:00:00"+","+"10000"+","+"10000");
                            begin_time = "1981-01-01 00:00:00";
                            end_time = "2010-01-01 00:00:00";
                            record_count = "10000";
                            day_total = "10000";
                        } else if (value != null) {
                            begin_time = value.split(",")[0];
                            end_time = value.split(",")[1];
                            record_count = value.split(",")[2];
                            day_total = value.split(",")[3];
                        } else {
                            try {
                                //获取总记录数
                                record_count = databaseDcl.queryRecordNum(schemaEntity.getSchemaName(), table_name);
                                //最早记录的时间
                                begin_time = databaseDcl.queryMinTime(schemaEntity.getSchemaName(), table_name, newBoundBeginTime, "D_DATETIME");
                                //最近记录的时间
                                end_time = databaseDcl.queryMaxTime(schemaEntity.getSchemaName(), table_name, newBoundEndTime,newBoundEndTimeFlag, "D_DATETIME");
                                //获取日增量
                                day_total = databaseDcl.queryIncreCount(schemaEntity.getSchemaName(), table_name, "D_DATETIME", yesterdayZero, todayZero);
                                tableCollectInfo.put(table_name, begin_time + "," + end_time + "," + record_count + "," + day_total);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        //入库
                        TableDataStatisticsEntity tableDataStatisticsEntity = new TableDataStatisticsEntity();
                        tableDataStatisticsEntity.setTableId(String.valueOf(tableInfo.get("id")));
                        tableDataStatisticsEntity.setDatabaseId(schemaEntity.getId());
                        tableDataStatisticsEntity.setStatisticTime(sdf.format(new Date()));
                        tableDataStatisticsEntity.setStatisticDate(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", yesterdayZero));
                        //tableDataStatisticsEntity.setStatisticDate(yesterdayZero);
                        if (StringUtils.isNotNullString(begin_time)) {
                            tableDataStatisticsEntity.setBeginTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", begin_time));
                        }
                        if (StringUtils.isNotNullString(end_time)) {
                            tableDataStatisticsEntity.setEndTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", end_time));
                        }
                        if (StringUtils.isNotNullString(record_count)) {
                            tableDataStatisticsEntity.setRecordCount(Double.valueOf(record_count));
                        }
                        if (StringUtils.isNotNullString(day_total)) {
                            tableDataStatisticsEntity.setDayTotal(Integer.valueOf(day_total));
                        }
                        tableDataStatisticsDao.saveNotNull(tableDataStatisticsEntity);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (databaseDcl != null) {
                        databaseDcl.closeConnect();
                    }
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
