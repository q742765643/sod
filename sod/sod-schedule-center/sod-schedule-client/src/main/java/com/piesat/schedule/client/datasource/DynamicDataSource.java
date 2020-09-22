package com.piesat.schedule.client.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.dm.rpc.api.database.DatabaseService;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.client.vo.ConnectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-07 10:09
 **/
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    public static Map<Object, Object> _targetDataSources = new ConcurrentHashMap<>();
    public static Map<Object, ConnectVo> connectVoMap = new ConcurrentHashMap();
    public static Map<String, String> type = new ConcurrentHashMap();
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DataSourceContextHolder.getDataSource();
        if(null!=dataSourceName){
              this.selectDataSource(dataSourceName);
        }
        return dataSourceName;
    }
    public void selectDataSource(String dataSourceName) {
       /* if (obj != null) {
            return;
        }*/


        DataSource dataSource = this.getDataSource(dataSourceName);
        if (null != dataSource) {
            this.setDataSource(dataSourceName, dataSource);
        }

        if(null==_targetDataSources.get(dataSourceName)){
            if(!"CASSANDRA".equals(type.get(dataSourceName))){
                throw new RuntimeException("数据库连接信息不正确!");
            }
        }


    }
  /*  public  DataSource getDataSourceByMap(String dataSourceName){
        this.selectDataSource(dataSourceName);
        Object obj = _targetDataSources.get(dataSourceName);
        if (obj != null) {
            return (DataSource) obj;
        }
        return null;

    }*/

    public ConnectVo getConnectVo(String dataSourceName) {

        ConnectVo connectVo=connectVoMap.get(dataSourceName);
        if(connectVo==null){
            this.selectDataSource(dataSourceName);
            connectVo=connectVoMap.get(dataSourceName);
        }
        if(connectVo==null){
            throw new RuntimeException("数据库连接信息不存在");
        }
        return connectVo;
    }
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        //_targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }

    private synchronized void addTargetDataSource(String key, DataSource dataSource) {
        if(null!=_targetDataSources.get(key)){
            try {
                ((DruidDataSource) _targetDataSources.get(key)).close();
                _targetDataSources.remove(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _targetDataSources.put(key, dataSource);
        this.setTargetDataSources(_targetDataSources);
    }

    private  DataSource createDataSource(String driverClassName, String url,
                                        String username, String password) {
    /*    Connection connection = null;
        try {    //排除连接不上的错误
            Class.forName(driverClassName);
            connection=DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            return null;
        }finally {
            if(connection==null){
                return null;
            }
            if(null!=connection){
                try {
                    connection.close();
                } catch (Exception e) {

                }
            }
        }*/
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url.replace("recv_mode=1","recv_mode=0"));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(60000);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setFailFast(true);
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setMaxOpenPreparedStatements(0);
        //dataSource.setRemoveAbandoned(true);
        //dataSource.setRemoveAbandonedTimeout(300);
        //dataSource.setLogAbandoned(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setValidationQuery("select 1 ");
       /* dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setFailFast(true);
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setMaxOpenPreparedStatements(0);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(180);
        dataSource.setLogAbandoned(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setValidationQuery("select 1 ");*/
        return dataSource;
    }

    private void setDataSource(String dataSourceName, DataSource dataSource) {
        this.addTargetDataSource(dataSourceName, dataSource);
        //DataSourceContextHolder.setDataSource(dataSourceName);
    }
    private synchronized DataSource getDataSource(String dataSourceName) {
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
                   if(null!=connectVoMap.get(parentId)&&null!=_targetDataSources.get(parentId)){
                     ConnectVo yConnectVo=connectVoMap.get(parentId);
                     if(connectVo.getUrl().equals(yConnectVo.getUrl())
                             &&connectVo.getUserName().equals(yConnectVo.getUserName())
                             &&connectVo.getPassWord().equals(yConnectVo.getPassWord())
                             &&connectVo.getIp().equals(yConnectVo.getIp())){
                         return null;
                     }
                   }
                   type.put(parentId,databaseDto.getDatabaseDefine().getDatabaseType().toUpperCase());
                   connectVoMap.put(parentId, connectVo);
                   if(!"CASSANDRA".equals(databaseDto.getDatabaseDefine().getDatabaseType().toUpperCase())){
                       if(!"".equals(userName)&&!"".equals(password)){
                           log.info("========={}创建连接池===========",parentId);
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

