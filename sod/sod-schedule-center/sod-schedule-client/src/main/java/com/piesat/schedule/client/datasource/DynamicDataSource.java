package com.piesat.schedule.client.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.client.vo.ConnectVo;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 10:09
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    public static Map<Object, Object> _targetDataSources = new HashMap();
    public static Map<Object, ConnectVo> connectVoMap = new HashMap();

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DataSourceContextHolder.getDataSource();
        if(null!=dataSourceName){
              this.selectDataSource(dataSourceName);
        }
        return dataSourceName;
    }
    public void selectDataSource(String dataSourceName) {
        Object obj = _targetDataSources.get(dataSourceName);
       /* if (obj != null) {
            return;
        }*/

        try {
            DataSource dataSource = this.getDataSource(dataSourceName);
            if (null != dataSource) {
                this.setDataSource(dataSourceName, dataSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public  DataSource getDataSourceByMap(String dataSourceName){
        this.selectDataSource(dataSourceName);
        Object obj = _targetDataSources.get(dataSourceName);
        if (obj != null) {
            return (DataSource) obj;
        }
        return null;
    }

    public ConnectVo getConnectVo(String dataSourceName) {
        this.selectDataSource(dataSourceName);
        ConnectVo connectVo=connectVoMap.get(dataSourceName);
        if(connectVo==null){
            throw new RuntimeException("数据库连接信息不存在");
        }
        return connectVo;
    }
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        _targetDataSources = targetDataSources;
        super.setTargetDataSources(_targetDataSources);
        afterPropertiesSet();
    }

    private void addTargetDataSource(String key, DataSource dataSource) {
        _targetDataSources.put(key, dataSource);
        this.setTargetDataSources(_targetDataSources);
    }

    private DataSource createDataSource(String driverClassName, String url,
                                        String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url.replace("recv_mode=1","recv_mode=0"));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(3);
        dataSource.setMaxActive(20);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setMaxOpenPreparedStatements(0);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        if(url.indexOf("xugu")!=-1){
            dataSource.setValidationQuery("select 1 ");
        }
        return dataSource;
    }

    private void setDataSource(String dataSourceName, DataSource dataSource) {
        this.addTargetDataSource(dataSourceName, dataSource);
        //DataSourceContextHolder.setDataSource(dataSourceName);
    }
    private DataSource getDataSource(String dataSourceName) {
        DataSource dataSource=null;
        DatabaseService databaseService= SpringUtil.getBean(DatabaseService.class);
        List<DatabaseDto> databaseDtos=databaseService.findByLevel(1);
        boolean flag=false;
        for(DatabaseDto databaseDto:databaseDtos){
               String parentId=databaseDto.getDatabaseDefine().getId();
               if(parentId.toUpperCase().equals(dataSourceName.toUpperCase())){
                   flag=true;
                   ConnectVo connectVo =new ConnectVo();
                   Set<DatabaseAdministratorDto> databaseAdministratorDtos=databaseDto.getDatabaseDefine().getDatabaseAdministratorList();
                   String userName="";
                   String password="";
                   try {
                       for(DatabaseAdministratorDto administratorDto:databaseAdministratorDtos){
                           if(administratorDto.getIsManager()) {
                               userName = administratorDto.getUserName();
                               password = administratorDto.getPassWord();
                               break;
                           }
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   connectVo.setIp(databaseDto.getDatabaseDefine().getDatabaseIp());
                   connectVo.setPort(Integer.parseInt(databaseDto.getDatabaseDefine().getDatabasePort()));
                   connectVo.setUserName(userName);
                   connectVo.setPassWord(password);
                   connectVo.setUrl(databaseDto.getDatabaseDefine().getDatabaseUrl());
                   if(null!=connectVoMap.get(parentId)){
                     ConnectVo yConnectVo=connectVoMap.get(parentId);
                     if(connectVo.getUrl().equals(yConnectVo.getUrl())
                             &&connectVo.getUserName().equals(yConnectVo.getUserName())
                             &&connectVo.getPassWord().equals(yConnectVo.getPassWord())
                             &&connectVo.getIp().equals(yConnectVo.getIp())){
                         return null;
                     }
                   }
                   connectVoMap.put(parentId, connectVo);
                   if(!"CASSANDRA".equals(databaseDto.getDatabaseDefine().getDatabaseType().toUpperCase())){
                       if(!"".equals(userName)&&!"".equals(password)){
                           String url=databaseDto.getDatabaseDefine().getDatabaseUrl();
                           String driverClassName=databaseDto.getDatabaseDefine().getDriverClassName();
                           dataSource = this.createDataSource(
                                   driverClassName, url, userName, password);
                       }

                   }
               }

        }
        if(!flag){
            throw new RuntimeException("切换数据源失败");
        }

        return dataSource;
    }
}

