package com.piesat.dm.core.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.dadiyang.equator.GetterBaseEquator;
import com.piesat.dm.core.model.ConnectVo;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cwh
 * @date 2020年 12月04日 10:08:56
 */
@Slf4j
public class CommDataSource {
    public static Map<Object, Object> _targetDataSources = new ConcurrentHashMap<>();
    public static Map<Object, ConnectVo> connectVoMap = new ConcurrentHashMap();

    private DataSource createDataSource(ConnectVo connectVo) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(connectVo.getClassName());
        dataSource.setUrl(connectVo.getUrl());
        dataSource.setUsername(connectVo.getUserName());
        dataSource.setPassword(connectVo.getPassWord());
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
        dataSource.init();
        return dataSource;
    }

    public static Connection getConnection(String pid, ConnectVo connectVo) throws SQLException {

        Object o = _targetDataSources.get(pid);
        if (o == null) {
            CommDataSource dataSource = new CommDataSource();
            dataSource.getDataSource(pid, connectVo, false);
            o = _targetDataSources.get(pid);
        } else {
            ConnectVo connectVoReal = connectVoMap.get(pid);
            boolean equals = new GetterBaseEquator().isEquals(connectVoReal, connectVo);
            if (!equals) {
                CommDataSource dataSource = new CommDataSource();
                dataSource.getDataSource(pid, connectVo, true);
                o = _targetDataSources.get(pid);
            }
        }
        if (o instanceof DataSource) {
            return ((DataSource) o).getConnection();
        }
        return null;
    }

    private synchronized void getDataSource(String pid, ConnectVo connectVo, Boolean reload) throws SQLException {
        ConnectVo connectVoReal = connectVoMap.get(pid);
        boolean equals = false;
        if (!reload) {
            equals = new GetterBaseEquator().isEquals(connectVoReal, connectVo);
        }
        if (!equals && _targetDataSources.get(pid) != null) {
            ((DruidDataSource) _targetDataSources.get(pid)).close();
            _targetDataSources.remove(pid);
        }

        DataSource dataSource = this.createDataSource(connectVo);
        connectVoMap.put(pid,connectVo);
        _targetDataSources.put(pid, dataSource);
    }

}
