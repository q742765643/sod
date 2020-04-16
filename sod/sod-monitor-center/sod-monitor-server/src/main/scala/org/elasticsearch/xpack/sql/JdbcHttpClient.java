//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.elasticsearch.xpack.sql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.xpack.sql.client.HttpClient;
import org.elasticsearch.xpack.sql.client.Version;
import org.elasticsearch.xpack.sql.proto.ColumnInfo;
import org.elasticsearch.xpack.sql.proto.MainResponse;
import org.elasticsearch.xpack.sql.proto.Mode;
import org.elasticsearch.xpack.sql.proto.RequestInfo;
import org.elasticsearch.xpack.sql.proto.SqlQueryRequest;
import org.elasticsearch.xpack.sql.proto.SqlQueryResponse;
import org.elasticsearch.xpack.sql.proto.SqlTypedParamValue;

class JdbcHttpClient {
    private final HttpClient httpClient;
    private final JdbcConfiguration conCfg;
    private InfoResponse serverInfo;

    JdbcHttpClient(JdbcConfiguration conCfg) throws SQLException {
        this(conCfg, true);
    }

    JdbcHttpClient(JdbcConfiguration conCfg, boolean checkServer) throws SQLException {
        this.httpClient = new HttpClient(conCfg);
        this.conCfg = conCfg;
        if(checkServer) {
            this.serverInfo = this.fetchServerInfo();
            this.checkServerVersion();
        }

    }

    boolean ping(long timeoutInMs) throws SQLException {
        return this.httpClient.ping(timeoutInMs);
    }

    Cursor query(String sql, List<SqlTypedParamValue> params, RequestMeta meta) throws SQLException {
        int fetch = meta.fetchSize() > 0?meta.fetchSize():this.conCfg.pageSize();
        SqlQueryRequest sqlRequest = new SqlQueryRequest(sql, params, this.conCfg.zoneId(), fetch, TimeValue.timeValueMillis(meta.timeoutInMs()), TimeValue.timeValueMillis(meta.queryTimeoutInMs()), (ToXContent)null, (String)null, new RequestInfo(Mode.JDBC), this.conCfg.fieldMultiValueLeniency());
        SqlQueryResponse response = this.httpClient.query(sqlRequest);
        return new DefaultCursor(this, response.cursor(), this.toJdbcColumnInfo(response.columns()), response.rows(), meta);
    }

    Tuple<String, List<List<Object>>> nextPage(String cursor, RequestMeta meta) throws SQLException {
        SqlQueryRequest sqlRequest = new SqlQueryRequest(cursor, TimeValue.timeValueMillis(meta.timeoutInMs()), TimeValue.timeValueMillis(meta.queryTimeoutInMs()), new RequestInfo(Mode.JDBC));
        SqlQueryResponse response = this.httpClient.query(sqlRequest);
        return new Tuple(response.cursor(), response.rows());
    }

    boolean queryClose(String cursor) throws SQLException {
        return this.httpClient.queryClose(cursor, Mode.JDBC);
    }

    InfoResponse serverInfo() throws SQLException {
        if(this.serverInfo == null) {
            this.serverInfo = this.fetchServerInfo();
        }

        return this.serverInfo;
    }

    private InfoResponse fetchServerInfo() throws SQLException {
        MainResponse mainResponse = this.httpClient.serverInfo();
        Version version = Version.fromString(mainResponse.getVersion());
        return new InfoResponse(mainResponse.getClusterName(), version.major, version.minor, version.revision);
    }

    private void checkServerVersion() throws SQLException {

    }

    private List<JdbcColumnInfo> toJdbcColumnInfo(List<ColumnInfo> columns) throws SQLException {
        List<JdbcColumnInfo> cols = new ArrayList(columns.size());
        Iterator var3 = columns.iterator();

        while(var3.hasNext()) {
            ColumnInfo info = (ColumnInfo)var3.next();
            cols.add(new JdbcColumnInfo(info.name(), TypeUtils.of(info.esType()), "", "", "", "", info.displaySize()));
        }

        return cols;
    }
}
