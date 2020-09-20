package com.piesat.schedule.client.util.fetl.imp;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import com.alibaba.druid.pool.DruidDataSource;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.util.BlockThreadPool;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.util.fetl.util.FetlUtil;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
@Slf4j
public class ImpMetaData{
	public static Map<String, String> errorMap = new ConcurrentHashMap();
	static{
		try {
			Class.forName("com.xugu.cloudjdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List<Integer> columnTypes = new ArrayList<>();
	//private LinkedBlockingQueue<Runnable> task = null;
	//private ThreadPoolExecutor pool = null;

	private int threadNum = 60;
	private int commitCount = 1500;
	private int taskRows = 30000;
	private int taskNum = 50;

	public ImpMetaData(){}

	public ImpMetaData(int threadNum, int commitCount, int taskRows,int taskNum){
		this.threadNum = threadNum;
		this.commitCount = commitCount;
		this.taskRows = taskRows;
		this.taskNum = taskNum;
	}

	public String impMetaData(String parentId, Map<Type, Set<String>> impInfo, String filepath) {
		//Connection con = FetlUtil.get_conn(url+"&char_set=utf8");
		Connection con=null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		Statement st = null;
		BufferedReader br = null;
		String line = null;
		DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);

		try{
			DataSourceContextHolder.setDataSource(parentId);
			con=dynamicDataSource.getConnection();
			st = con.createStatement();
			File file  = new File(filepath);
			br = new BufferedReader(new FileReader(file),64*1024);
			if(impInfo == null){
				List<String> foreigns = new ArrayList<>();
				while((line = br.readLine()) != null){
				    try{
						if(line.startsWith("---data")){
							String tableName = line.substring(8, line.indexOf("-", 3));
							String[] columns = br.readLine().split("\t");
							String fileName = file.getParentFile().getPath() + File.separator +  br.readLine();
							br.readLine();//---end---
							/*if(pool == null && task == null){
								task = new LinkedBlockingQueue<Runnable>(taskNum);
								pool = new ThreadPoolExecutor(threadNum, threadNum, 10, TimeUnit.SECONDS, task);
								pool.allowCoreThreadTimeOut(true);
							}*/
//							impData(con, fileName, tableName.toUpperCase(), columns);
//							if(database != null && !"null".equalsIgnoreCase(database)) {
//								url = url.replace("SYSTEM", database);
//							}
							impDataReadFile(parentId, fileName, tableName, columns,sb);
							continue;
						}
						if(line.equalsIgnoreCase("---end foreign key---")){
							foreigns.add(new String(sql));
							sql.setLength(0);
							continue;
						}
						if(line.startsWith("---table")){
							execSql(sql,br,st,"---end table---");
//							log.info(object+" created");
							continue;
						}
						if(line.startsWith("---end")){
							st.execute(sql.toString());
//							log.info(object+" created");
							sql.setLength(0);
							continue;
						} else if(line.startsWith("---")){
							continue;
						}
						sql.append(line).append("\r\n");
					  } catch (SQLException e) {
						  sql.setLength(0);
						  if(e.getErrorCode() != 7003 && e.getErrorCode() != 12008 && e.getErrorCode() != 21060){
							  e.printStackTrace();
						  }
					  }
				  }
				for(String foreign:foreigns){
					st.execute(foreign);
				}
			}else{
				Map<Type, Set<String>> newImpInfo = newMap(impInfo);
				List<String> foreigns = new ArrayList<>();
				while((line = br.readLine()) != null){
			    	if (line.startsWith("---role") && newImpInfo.containsKey(Type.ROLE) &&
			    		(newImpInfo.get(Type.ROLE) == null || newImpInfo.get(Type.ROLE).contains(line.substring(8, line.indexOf("-", 3))))) {
			    		try {
			    			execSql(sql,br,st,"---end role---");
			    		} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
			    	} else if(line.startsWith("---user") && newImpInfo.containsKey(Type.USER) &&
						(newImpInfo.get(Type.USER) == null || newImpInfo.get(Type.USER).contains(line.substring(8, line.indexOf("-", 3))))) {
						try {
				    		execSql(sql,br,st,"---end user---");
				    	} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
			    	} else if (line.startsWith("---schema")&& newImpInfo.containsKey(Type.SCHEMA) &&
						(newImpInfo.get(Type.SCHEMA) == null || newImpInfo.get(Type.SCHEMA).contains(line.substring(10, line.indexOf("-", 3))))) {
			    		try {
			    			execSql(sql,br,st,"---end schema---");
				    	} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if (line.startsWith("---sequence") && newImpInfo.containsKey(Type.SEQUENCE) &&
						(newImpInfo.get(Type.SEQUENCE) == null || newImpInfo.get(Type.SEQUENCE).contains(line.substring(12, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end sequence---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---table") && newImpInfo.containsKey(Type.TABLE) &&
						(newImpInfo.get(Type.TABLE) == null || newImpInfo.get(Type.TABLE).contains(line.substring(9, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end table---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---foreign key") && newImpInfo.containsKey(Type.TABLE) &&
						(newImpInfo.get(Type.TABLE) == null || newImpInfo.get(Type.TABLE).contains(line.substring(15, line.indexOf("-", 3))))){
//								execSql(sql,br,st,"---end foreign key---");
						sql.append(br.readLine());
						String sqlPart = null;
						while(!(sqlPart = br.readLine()).equals("---end foreign key---")){
							sql.append("\r\n").append(sqlPart);
						}
						foreigns.add(new String(sql));
						sql.setLength(0);
					} else if(line.startsWith("---comment") && newImpInfo.containsKey(Type.TABLE) &&
							(newImpInfo.get(Type.TABLE) == null || newImpInfo.get(Type.TABLE).contains(line.substring(11, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end comment---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
				    } else if(line.startsWith("---view") && newImpInfo.containsKey(Type.VIEW) &&
						(newImpInfo.get(Type.VIEW) == null ||newImpInfo.get(Type.VIEW).contains(line.substring(8, line.indexOf("-", 3))))){
						try {
					    	execSql(sql,br,st,"---end view---");
					    } catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---procedure") && newImpInfo.containsKey(Type.PROCEDURE) &&
						(newImpInfo.get(Type.PROCEDURE) == null || newImpInfo.get(Type.PROCEDURE).contains(line.substring(13, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end procedure---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---trigger") && newImpInfo.containsKey(Type.TRIGGER) &&
						(newImpInfo.get(Type.TRIGGER) == null || newImpInfo.get(Type.TRIGGER).contains(line.substring(11, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end trigger---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---package") && newImpInfo.containsKey(Type.PACKAGE) &&
						(newImpInfo.get(Type.PACKAGE) == null || newImpInfo.get(Type.PACKAGE).contains(line.substring(11, line.indexOf("-", 3))))){
						try {
							execSql(sql,br,st,"---end package---");
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					} else if(line.startsWith("---data") && newImpInfo.containsKey(Type.DATA) &&
						(newImpInfo.get(Type.DATA) == null || newImpInfo.get(Type.DATA).contains(line.substring(8, line.indexOf("-", 3))))){
						String tableName = line.substring(8, line.indexOf("-", 3));
						String[] columns = br.readLine().split("\t");
						String fileName =  file.getParentFile().getPath() + File.separator + br.readLine();
						br.readLine();//---end---
					/*	if(pool == null && task == null){
							task = new LinkedBlockingQueue<Runnable>(taskNum);
							pool = new ThreadPoolExecutor(threadNum, threadNum, 10, TimeUnit.SECONDS, task);
							pool.allowCoreThreadTimeOut(true);
						}*/
//							impData(con, fileName, tableName, columns);
//							if(database != null && "null".equalsIgnoreCase("null")) {
//								url = url.replace("SYSTEM", database);
//							}
						log.info("开始恢复表{}数据",tableName);
						try {
							impDataReadFile(parentId, fileName, tableName, columns,sb);
						} catch (Exception e) {
							log.info(OwnException.get(e));
						}
						log.info("结束恢复表{}数据",tableName);

					}
				}
				for(String foreign : foreigns){
					try {
						st.execute(foreign);
					} catch (Exception e) {
						sb.append(e.getMessage()).append("\n");
					}
				}
			}
		/*	if(pool != null){
				pool.shutdown();
			}
			while(true){
				Thread.sleep(3*1000);
				if(pool.isTerminated()){
					break;
				}
			}*/
		} catch (Exception e){
			sb.append(e.getMessage());
		} finally {
			FetlUtil.closeSt(st);
			FetlUtil.closeConn(con);
			FetlUtil.closeReader(br);
			DataSourceContextHolder.clearDataSource();
		}
		return sb.toString();
 	}

	public void impDataReadFile(String parentId, String fileName, String tableName, String[] columns,StringBuffer sb) throws Exception{
		BlockThreadPool pool = new BlockThreadPool(threadNum);
		List<Integer> types = new ArrayList<>();
		String restorSql = connetSqlString2(columns, tableName,types);
		DataInputStream dis = null;
		File file = new File(fileName);
		String ex="";
		try {
			if(file.exists()){
                List<byte[]> oneData = new ArrayList<byte[]>();
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file),64*1024));
                while(dis.available() !=0 ){
                    byte[] data = new byte[dis.readInt()];
                    dis.read(data);
                    oneData.add(data);
                    if(oneData.size() % commitCount == 0){
                        pool.execute(
                            new AnalysisData(fileName,parentId,tableName,commitCount,oneData,restorSql,types)
                        );
                        oneData = new ArrayList<byte[]>();
                    }
                }
                if(!oneData.isEmpty()){
					pool.execute(
                            new AnalysisData(fileName,parentId,tableName,commitCount,oneData,restorSql,types)
                    );

                }
            }
		} catch (Exception e) {
			ex=e.getMessage();
			log.error("恢复最终:"+OwnException.get(e));
		} finally {
			try {
				if(null!=dis){
					dis.close();
				}
				pool.shutdown();
				while (true){
					if(pool.isTerminated()){
						log.info("所有的子线程都结束了！");
						break;
					}
				}

			} catch (Exception e) {
				log.error("恢复finally:"+OwnException.get(e));
			}
			if(null!=errorMap.get(fileName)){
				sb.append(errorMap.get(fileName));
			}
			errorMap.remove(fileName);
			if(!"".equals(ex)){
				sb.append(ex).append("\n").append("连接被关闭或者字段不匹配");
			}
		}
	}


	public String connetSqlString(String[] columns,String tableName){
		this.columnTypes.clear();
		StringBuffer column = new StringBuffer();
		StringBuffer value = new StringBuffer();
		column.append("insert into ").append(tableName).append("(");
		value.append(" values(");
		for(int i= 0; i< columns.length; i++){
			String[] temple = columns[i].split("\\|");
			this.columnTypes.add(Integer.valueOf(temple[1]));
			if(i==0){
				column.append(temple[0]);
				value.append("?");
			}else{
				column.append(",").append(temple[0]);
				value.append(",?");
			}
		}
		column.append(")");
		value.append(")");
		column.append(value.toString());
		return column.toString();
	}

	public String connetSqlString2(String[] columns,String tableName,List<Integer> arr){
		StringBuffer column = new StringBuffer();
		StringBuffer value = new StringBuffer();
		column.append("insert into ").append(tableName).append("(");
		value.append(" values(");
		for(int i= 0; i< columns.length; i++){
			String[] temple = columns[i].split("\\|");
			arr.add(Integer.valueOf(temple[1]));
			if(i==0){
				column.append("\"").append(temple[0]).append("\"");
				value.append("?");
			}else{
				column.append(",").append("\"").append(temple[0]).append("\"");
				value.append(",?");
			}
		}
		column.append(")");
		value.append(")");
		column.append(value.toString());
		return column.toString();
	}

	public void execSql(StringBuffer sql, BufferedReader br,Statement st,String end) throws Exception{
		sql.append(br.readLine());
		String sqlPart = null;
		while((sqlPart = br.readLine()) != null){
			if(sql.length() == 0){
				sql.append(sqlPart);
			}else if(sql.length() != 0 && !sqlPart.equalsIgnoreCase(end)){
				sql.append("\r\n").append(sqlPart);
			}else if(sqlPart.equalsIgnoreCase(end)){
				try{
					st.executeQuery(sql.toString());
				} catch (SQLException e){
					sql.setLength(0);
					if(e.getErrorCode() != 7003 && e.getErrorCode() != 12008 && e.getErrorCode() != 21060)
						throw e;
				}
				sql.setLength(0);
				break;
			}
		}
//		while(!(sqlPart = br.readLine()).equals(end)){
//			if(sql.length() != 0){
//				sql.append("\r\n");
//			}
//			sql.append(sqlPart);
//			if(sqlPart.endsWith(";")){
//				try{
//					st.executeQuery(sql.toString());
//				} catch (SQLException e){
//					sql.setLength(0);
//					if(e.getErrorCode() != 7003 && e.getErrorCode() != 12008 && e.getErrorCode() != 21060)
//						throw e;
//				}
//				sql.setLength(0);
//			}
//		}
	}

	public Map<Type, Set<String>> newMap(Map<Type, Set<String>> impInfo){
		Map<Type, Set<String>> newMap = new HashMap<>();
		Iterator<Entry<Type, Set<String>>> it = impInfo.entrySet().iterator();
		while(it.hasNext()){
			Set<String> set = null;
			Entry<Type, Set<String>> entry = it.next();
			if(entry.getValue() != null){
				set = new HashSet<>();
				Iterator<String> it2 = entry.getValue().iterator();
				while(it2.hasNext()){
					set.add(it2.next().toUpperCase());
				}
			}
			newMap.put(entry.getKey(), set);
		}
		return newMap;
	}

	public enum signByte{
		b0sign(0x03),b1sign(0x0c),b2sign(0x30),b3sign(0xc0);
		private int val;
		private signByte(int val){
			this.val = val;
		}
		public static byte getSign(int i){
			byte ret = 0x00;
			switch (i){
				case 0:
					ret = (byte) b0sign.val;
					break;
				case 1:
					ret = (byte) b1sign.val;
					break;
				case 2:
					ret = (byte) b2sign.val;
					break;
				case 3:
					ret = (byte) b3sign.val;
					break;
			}
			return ret;
		}
	}
}
