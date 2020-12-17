package com.piesat.dm.core.datasource;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.github.dadiyang.equator.GetterBaseEquator;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.model.ConnectVo;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cwh
 * @date 2020年 12月04日 16:30:52
 */
public class CassandraSource {
    public static Map<Object, Object> _targetDataSources = new ConcurrentHashMap<>();
    public static Map<Object, ConnectVo> connectVoMap = new ConcurrentHashMap();
    public static Map<Object, Session> connectSessionMap = new ConcurrentHashMap();

    public static Session getConnection(String pid, ConnectVo connectVo) throws SQLException {
        Session session = connectSessionMap.get(pid);
        if (session != null && !session.isClosed()) {
            return session;
        }
        Object o = _targetDataSources.get(pid);
        if (o == null) {
            CassandraSource dataSource = new CassandraSource();
            dataSource.getDataSource(pid, connectVo, false);
            o = _targetDataSources.get(pid);
        } else {
            ConnectVo connectVoReal = connectVoMap.get(pid);
            boolean equals = new GetterBaseEquator().isEquals(connectVoReal, connectVo);
            if (!equals) {
                CassandraSource dataSource = new CassandraSource();
                dataSource.getDataSource(pid, connectVo, true);
                o = _targetDataSources.get(pid);
            }
        }
        if (o instanceof Cluster) {
            Session connect = ((Cluster) o).connect();
            connectSessionMap.put(pid, connect);
            return connect;
        }
        return null;
    }

    private synchronized void getDataSource(String pid, ConnectVo connectVo, Boolean reload) {
        ConnectVo connectVoReal = connectVoMap.get(pid);
        boolean equals = false;
        if (!reload) {
            equals = new GetterBaseEquator().isEquals(connectVoReal, connectVo);
        }
        if (!equals && _targetDataSources.get(pid) != null) {
            ((Cluster) _targetDataSources.get(pid)).close();
            connectSessionMap.get(pid).close();
            _targetDataSources.remove(pid);
            connectSessionMap.remove(pid);
        }
        Cluster cluster = this.createDataSource(connectVo);
        connectVoMap.put(pid,connectVo);
        _targetDataSources.put(pid, cluster);
    }

    private Cluster createDataSource(ConnectVo connectVo) {
        String[] idArray = connectVo.getIp().split(Constants.COMMA);
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions
                .setConnectionsPerHost(HostDistance.LOCAL, 6, 12)
                .setMaxRequestsPerConnection(HostDistance.LOCAL, 32768);
        Cluster.Builder clusterBuilder = Cluster.builder()
                .addContactPoints(idArray)
                .withPoolingOptions(poolingOptions)
                .withLoadBalancingPolicy(new RoundRobinPolicy())
                .withReconnectionPolicy(new ConstantReconnectionPolicy(100L))
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                .withCredentials(connectVo.getUserName(), connectVo.getPassWord())
                .withPort(connectVo.getPort());
        return clusterBuilder.build();
    }

}
