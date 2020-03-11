package com.piesat.schedule.client.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.schedule.client.vo.ConnectVo;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
        if (obj != null) {
            return;
        }

        DataSource dataSource = this.getDataSource(dataSourceName);
        if (null != dataSource) {
            this.setDataSource(dataSourceName, dataSource);
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

    public ConnectVo getConnectVo(String dataSourceName){
        this.selectDataSource(dataSourceName);
        ConnectVo connectVo=connectVoMap.get(dataSourceName);
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
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private void setDataSource(String dataSourceName, DataSource dataSource) {
        this.addTargetDataSource(dataSourceName, dataSource);
        //DataSourceContextHolder.setDataSource(dataSourceName);
    }
    private DataSource getDataSource(String dataSourceName) {
        ConnectVo connectVo =new ConnectVo();
        connectVo.setIp("10.211.55.7");
        connectVo.setPort(9042);
        connectVo.setUserName("cassandra");
        connectVo.setPassWord("cassandra");
        connectVoMap.put("RADB", connectVo);

        DataSource dataSource=null;
        if(!dataSourceName.equals("RADB")){
            String driverClassName="com.xugu.cloudjdbc.Driver";
            String userName="USR_SOD";
            String url="jdbc:xugu://10.1.6.117:5138/BABJ_SMDB?char_set=utf8&compatibleoracle=false";
            String password="Pnmic_qwe123";
            dataSource = this.createDataSource(
                    driverClassName, url, userName, password);
        }


        return dataSource;
    }
}

