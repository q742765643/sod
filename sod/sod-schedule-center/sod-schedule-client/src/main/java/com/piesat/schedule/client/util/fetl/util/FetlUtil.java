package com.piesat.schedule.client.util.fetl.util;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.vo.ConnectVo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FetlUtil
{
	public static Connection get_conn(String parentId) {
		Connection conn = null;
		try {
			Class.forName("com.xugu.cloudjdbc.Driver");
			DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
			ConnectVo connectVo=dynamicDataSource.getConnectVo(parentId);
			if(connectVo.getUrl().indexOf("?")!=-1){
				conn = DriverManager.getConnection(connectVo.getUrl()+"&char_set=utf8",connectVo.getUserName(),connectVo.getPassWord());
			}else{
				conn = DriverManager.getConnection(connectVo.getUrl()+"?char_set=utf8",connectVo.getUserName(),connectVo.getPassWord());
			}

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Connection get_connN(String parentId) {
		Connection conn = null;
		try {
			Class.forName("com.xugu.cloudjdbc.Driver");
			DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
			ConnectVo connectVo=dynamicDataSource.getConnectVo(parentId);
			String url="";

			if(connectVo.getUrl().indexOf("?")!=-1){
				if(connectVo.getUrl().indexOf("recv_mode")!=-1){
					url=connectVo.getUrl().replace("recv_mode=0","recv_mode=1")+"&char_set=utf8";
				}else {
					url=connectVo.getUrl()+"&recv_mode=1&char_set=utf8";
				}
			}else{
				url=connectVo.getUrl()+"?recv_mode=1&char_set=utf8";
			}

			conn = DriverManager.getConnection(url,connectVo.getUserName(),connectVo.getPassWord());

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void closeConn(Connection con){
		try{
			if(con !=null){
				con.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void closeSt(Statement st){
		try{
			if(st !=null){
				st.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void closeRs(ResultSet rs){
		try{
			if(rs !=null){
				rs.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void closePs(PreparedStatement ps){
		try{
			if(ps !=null){
				ps.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void closeWriter(Writer writer){
		try{
			if(writer !=null){
				writer.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void closeOutput(OutputStream os){
		try{
			if(os !=null){
				os.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void closeReader(Reader reader){
		try{
			if(reader !=null){
				reader.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void closeInputStream(InputStream inputStream){
		try{
			if(inputStream !=null){
				inputStream.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String replace(String objName, String schemaName, String sql){
		String returnString = sql;
		objName = objName.toUpperCase();
		if(sql.contains(objName)){
			returnString = sql.replaceFirst(objName, schemaName+"."+objName);
		}else if(sql.contains("\""+objName+"\"")){
			returnString = sql.replaceFirst("\""+objName+"\"", schemaName+"."+objName);
		}
		return returnString;
	}
}
