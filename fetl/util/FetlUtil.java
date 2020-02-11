package com.xugu.fetl.util;

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
	public static Connection get_conn(String url) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
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
