package com.piesat.schedule.client.util.fetl.exp;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.xugu.cloudjdbc.Types;
import com.piesat.schedule.client.util.fetl.permission.LoadPermission;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.util.fetl.util.FetlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpMetadata {

	static{
		try {
			Class.forName("com.xugu.cloudjdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public StringBuffer get_ckcons(Connection conn, String tabid, String schename,String tabname) throws Exception {
		StringBuffer cons_str = new StringBuffer();
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql_str = "SELECT CONS_NAME,DEFINE,b.table_name FROM DBA_CONSTRAINTS a inner join dba_tables b on a.REF_TABLE_ID= B.TABLE_ID WHERE a.TABLE_ID="
					+ tabid + " AND CONS_TYPE='C' ";
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			if (rs != null) {
				while (rs.next()) {
					String define = rs.getString(2);
					String cons_name = rs.getString(1);
					cons_str.append("ALTER TABLE ").append(schename).append(".").append(tabname).append(" ADD CONSTRAINT ")
					.append(cons_name).append(" CHECK(").append(define).append(");");
				}
			}
			return cons_str;
		} catch (Exception e) {
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void get_db_fk_cons(Connection conn, BufferedWriter writer) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql_str = "SELECT CONS_NAME,DEFINE,UPDATE_ACTION,DELETE_ACTION,b.table_name as pk_tab,c.table_name as fk_tab FROM CONSTRAINTS  a inner join dba_tables b on a.REF_TABLE_ID= B.TABLE_ID  inner join DBA_tables c on a.table_Id=c.table_id WHERE  CONS_TYPE='F' ";
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			if (rs != null) {
				while (rs.next()) {
					String ptab = rs.getString(5);
					String ftab = rs.getString(6);
					String define = rs.getString(2);
					String fk = define.substring(1, define.indexOf(")"));
					String pk = define.substring(define.indexOf(")") + 2).replace(")", "");
					String d_action = "";
					String u_action = "";
					if (rs.getString(3).equalsIgnoreCase("c")) {
						u_action = " ON UPDATE CASCADE ";
					} else if (rs.getString(3).equalsIgnoreCase("d")) {
						u_action = " ON UPDATE SET DEFAULT ";
					} else if (rs.getString(3).equalsIgnoreCase("u")) {
						u_action = " ON UPDATE SET NULL ";
					}
					if (rs.getString(4).equalsIgnoreCase("c")) {
						d_action = " ON DELETE CASCADE ";
					} else if (rs.getString(4).equalsIgnoreCase("d")) {
						d_action = " ON DELETE SET DEFAULT ";
					} else if (rs.getString(4).equalsIgnoreCase("u")) {
						d_action = " ON DELETE SET NULL ";
					}
					String fk_sql = "ALTER TABLE " + ftab + " ADD CONSTRAINT "
							+ rs.getString(1) + " FOREIGN KEY(" + fk
							+ ") REFERENCES " + ptab + "(" + pk + ") "
							+ u_action + " " + d_action + ";";
					writer.write("---foreign key "+ ftab+"---\r\n");
					writer.write(fk_sql+"\r\n");
					writer.write("---end foreign key---\r\n");
					writer.flush();

				}
			}
		} catch (Exception e) {
			throw e;
		} finally{
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public String get_constraint(Connection conn, String tabid, String schename,String tabname) throws Exception {
		String cons_str = "";
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql_str = "SELECT CONS_NAME,DEFINE,UPDATE_ACTION,DELETE_ACTION,b.table_name FROM DBA_CONSTRAINTS a inner join dba_tables b on a.REF_TABLE_ID= B.TABLE_ID WHERE a.TABLE_ID="
					+ tabid + " AND CONS_TYPE='F' ";
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			if (rs != null) {
				while (rs.next()) {
					String define = rs.getString(2);
					String fk = define.substring(1, define.indexOf(")"));
					String pk = define.substring(define.indexOf(")") + 2)
							.replace(")", "");
					String d_action = "";
					String u_action = "";
					if (rs.getString(3).equalsIgnoreCase("c")) {
						u_action = " ON UPDATE CASCADE ";
					} else if (rs.getString(3).equalsIgnoreCase("d")) {
						u_action = " ON UPDATE SET DEFAULT ";
					} else if (rs.getString(3).equalsIgnoreCase("u")) {
						u_action = " ON UPDATE SET NULL ";
					}
					if (rs.getString(4).equalsIgnoreCase("c")) {
						d_action = " ON DELETE CASCADE ";
					} else if (rs.getString(4).equalsIgnoreCase("d")) {
						d_action = " ON DELETE SET DEFAULT ";
					} else if (rs.getString(4).equalsIgnoreCase("u")) {
						d_action = " ON DELETE SET NULL ";
					}
					//锟斤拷模式锟斤拷
					cons_str += "ALTER TABLE " + schename + "." + tabname
							+ " ADD CONSTRAINT " + rs.getString(1)
							+ " FOREIGN KEY(" + fk + ") REFERENCES "
							+ schename + "." + rs.getString(5) + "(" + pk + ") " + u_action
							+ " " + d_action + ";";
//					cons_str += "ALTER TABLE "  + tabname
//					+ " ADD CONSTRAINT " + rs.getString(1)
//					+ " FOREIGN KEY(" + fk + ") REFERENCES "
//					+ rs.getString(5) + "(" + pk + ") " + u_action
//					+ " " + d_action + ";";
				}
			}
			return cons_str;
		} catch (Exception e) {
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public String get_index(Connection conn, String tabid, String schename, String tabname) throws Exception {
		String index_str = "";
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql_str = "SELECT INDEX_NAME,INDEX_TYPE,IS_PRIMARY,IS_UNIQUE,IS_LOCAL,KEYS FROM DBA_INDEXES WHERE TABLE_ID="
					+ tabid + " order by index_name";
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			if (rs != null) {
				while (rs.next()) {
					String index_name = rs.getString(1);
					String index_type = rs.getString(2);
					String is_pri = rs.getString(3);
					String is_unique = rs.getString(4);
					String is_local = rs.getString(5);
					String idx_cols = rs.getString(6);
					String ddl_sql = "";
					if (is_pri.equalsIgnoreCase("T")) {
						ddl_sql = "ALTER TABLE " + schename + "." + tabname
								+ " ADD CONSTRAINT " + tabname
								+ "_PK PRIMARY KEY(" + idx_cols + ") ";
//						ddl_sql = "ALTER TABLE " + tabname
//								+ " ADD CONSTRAINT " + tabname
//								+ "_PK PRIMARY KEY(" + idx_cols + ") ";
					} else if (is_unique.equalsIgnoreCase("T")
							&& is_pri.equalsIgnoreCase("F")) {
						ddl_sql = "CREATE UNIQUE INDEX " + index_name + " ON "
								+ schename + "." + tabname + "(" + idx_cols
								+ ") ";
//						ddl_sql = "CREATE UNIQUE INDEX " + index_name + " ON "
//								+ tabname + "(" + idx_cols
//								+ ") ";
					} else {
						ddl_sql = "CREATE INDEX " + index_name + " ON "
								+ schename + "." + tabname + "(" + idx_cols
								+ ") ";
//						ddl_sql = "CREATE INDEX " + index_name + " ON "
//								+ tabname + "(" + idx_cols
//								+ ") ";
					}
					if (index_type.equals("3")) {
						ddl_sql += " INDEXTYPE IS BITMAP ";
					}
					if (is_local.equalsIgnoreCase("F")
							&& is_pri.equalsIgnoreCase("F")) {
						ddl_sql += " GLOBAL ";
					}
					index_str += ddl_sql + ";\r\n";
				}
			}
			return index_str;
		} catch (Exception e) {
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public String get_parti(Connection conn,String parti_type, String parti_k,
			String auto_partit, String auto_partis, String parti_num,String tabid) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			String parti_str = "\r\nPARTITION BY ";
			if (parti_type.equals("1")) {
				parti_str += " RANGE(" + parti_k.replace("\"", "") + ") ";
			} else if (parti_type.equals("2")) {
				parti_str += " LIST(" + parti_k.replace("\"", "") + ") ";
			} else if (parti_type.equals("3")) {
				parti_str += " HASH(" + parti_k.replace("\"", "")
						+ ") PARTITIONS " + parti_num;
				return parti_str;
			}

			String get_parti = "select PARTI_NAME,PARTI_VAL FROM DBA_PARTIS WHERE TABLE_ID="
					+ tabid;
			if (conn == null) {
				return null;
			}
			st = conn.createStatement();
			rs = st.executeQuery(get_parti);
			if (rs != null) {
				if (auto_partit.equals("4")) {
					parti_str += "\r\n INTERVAL " + auto_partis
							+ " HOUR PARTITIONS(\r\n";
					rs.next();
					String fir_parti = rs.getString(2);
					parti_str += "(" + fir_parti + "));\r\n";
					rs.close();
				} else if (auto_partit.equals("3")) {
					parti_str += "\r\n INTERVAL " + auto_partis
							+ " DAY PARTITIONS(\r\n";
					rs.next();
					String fir_parti = rs.getString(2);
					parti_str += "(" + fir_parti + "));\r\n";
					rs.close();
				} else if (auto_partit.equals("2")) {
					parti_str += "\r\n INTERVAL " + auto_partis
							+ " MONTH PARTITIONS(\r\n";
					rs.next();
					String fir_parti = rs.getString(2);
					parti_str += "(" + fir_parti + "));\r\n";
					rs.close();
				} else if (auto_partit.equals("1")) {
					parti_str += "\r\n INTERVAL " + auto_partis
							+ " YEAR PARTITIONS(\r\n";
					rs.next();
					String fir_parti = rs.getString(2);
					parti_str += "(" + fir_parti + "));\r\n";
					rs.close();
				} else {
					parti_str += " PARTITIONS(\r\n";
					while (rs.next()) {
						String parti_val = rs.getString(2);
						parti_str += "(" + parti_val + "),\r\n";
					}
					parti_str = parti_str.substring(0, parti_str.length() - 3)
							+ ");";
					rs.close();
				}
			}
			return parti_str;
		} catch (Exception e) {
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expSchema(BufferedWriter writer,Connection conn,String schemaName){
		String sql_str = null;
		Statement st = null;
		ResultSet rs = null;
		sql_str = "SELECT USER_NAME FROM DBA_USERS WHERE USER_ID =(SELECT USER_ID FROM DBA_SCHEMAS WHERE SCHEMA_NAME = '" + schemaName +"')";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			while (rs.next()) {
				try {
					writer.write("---schema " + schemaName + "---" + "\r\n");
					writer.write("create schema " + schemaName +" authorization " + rs.getString(1)+"\r\n");
					writer.write("---end schema---\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void expTable(BufferedWriter writer,Connection conn,String tableName,String schemaName) throws Exception{
		String sql_str = null;
		if (tableName == null) {
			sql_str = "SELECT table_name,SCHEMA_NAME,PARTI_TYPE,PARTI_KEY,AUTO_PARTI_TYPE,AUTO_PARTI_SPAN,PARTI_NUM,COMMENTS,TABLE_ID from dba_tables a inner join dba_schemas b on a.db_id=b.db_id and a.schema_id=b.schema_id and a.user_id=b.user_id order by table_name";
		}else{
			sql_str = "SELECT table_name,SCHEMA_NAME,PARTI_TYPE,PARTI_KEY,AUTO_PARTI_TYPE,AUTO_PARTI_SPAN,PARTI_NUM,a.COMMENTS,TABLE_ID from dba_tables a,dba_schemas b where a.schema_id = b.schema_id and schema_name='"+schemaName+"' and table_name='"
					+ tableName + "'";
		}
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql_str);
			Vector<String> tabs = new Vector<String>();
			if (rs != null) {
				StringBuffer commstr = new StringBuffer();
				while (rs.next()) {
					String table_nameStr = rs.getString(1);
					String sche_namestr = rs.getString(2);
					String parti_typestr = rs.getString(3) == null ? "null"
							: rs.getString(3);
					String parti_keystr = rs.getString(4) == null ? "null"
							: rs.getString(4);
					String auto_partitystr = rs.getString(5) == null ? "null"
							: rs.getString(5);
					String auto_partispstr = rs.getString(6) == null ? "null"
							: rs.getString(6);
					String parti_numstr = rs.getString(7) == null ? "null"
							: rs.getString(7);
					String tab_idstr = rs.getString(9) == null ? "null"
							: rs.getString(9);
					String tabinfos = table_nameStr + "|"
							+ sche_namestr + "|" + parti_typestr + "|"
							+ parti_keystr + "|" + auto_partitystr
							+ "|" + auto_partispstr + "|"
							+ parti_numstr + "|" + tab_idstr;
					String tabcomm = rs.getString(8);
					if (tabcomm != null) {
						commstr.append("---comment "+schemaName+"."+rs.getString(1)+"---\r\n");
						commstr.append("COMMENT ON TABLE ")
						       .append(rs.getString(2)).append(".")
						       .append(rs.getString(1)).append(" IS '")
			                   .append(rs.getString(8)).append("';\r\n");
						commstr.append("---end comment---\r\n");
					}
					tabs.add(tabinfos);
				}

				sql_str = "select col_name,type_name,\"VARYING\",NOT_NULL,IS_SERIAL,TIMESTAMP_T,DEF_VAL,COMMENTS,SCALE,COL_NO FROM DBA_COLUMNS A  WHERE table_id=? ORDER BY COL_NO";
				ps = conn.prepareStatement(sql_str);
				for (int i = 0; i < tabs.size(); i++) {
					String[] strs = tabs.get(i).split("\\|", -1);
					String otab = strs[0];
					String osche = strs[1];
					String oparti_t = strs[2];
					String oparti_k = strs[3];
					String oparti_au = strs[4];
					String oparti_asp = strs[5];
					String oparti_num = strs[6];
					String otabid = strs[7];
					ps.clearParameters();
					ps.setString(1, otabid);
					rs = ps.executeQuery();
					writer.write("---table "+schemaName+"."+otab.toUpperCase()+"---"+"\r\n");
					//锟斤拷模式锟斤拷
				    String outinfo = "CREATE TABLE " + osche + "." + otab + "(\r\n";
//					String outinfo = "CREATE TABLE " + otab + "(\r\n";
					while (rs.next()) {
						String colname = "\""+rs.getString(1).toUpperCase()+"\"";
						String coltype = rs.getString(2);
						String vary = rs.getString(3);
						String notnull = rs.getString(4);
						String is_serial = rs.getString(5);
						String times = rs.getString(6);
						String defval = rs.getString(7);
						String comms = rs.getString(8);
						String scale = rs.getString(9);
						outinfo += colname + " ";
						if (coltype.equalsIgnoreCase("CHAR")
								&& vary.equalsIgnoreCase("F")) {
							outinfo += "CHAR(" + scale + ") ";
						} else if (coltype.equalsIgnoreCase("CHAR")
								&& vary.equalsIgnoreCase("T")) {
							outinfo += "VARCHAR";
							if (!scale.equals("-1")) {
								outinfo += "(" + scale + ") ";
							}
						} else if (coltype.equalsIgnoreCase("NUMERIC")) {
							int iscal = Integer.parseInt(scale);
							int pre = iscal / 65536;
							int sc = iscal % 65536;
							outinfo += "NUMERIC(" + pre + "," + sc
									+ ") ";
						} else if (coltype.equalsIgnoreCase("DATETIME")
								&& times.equalsIgnoreCase("i")) {
							outinfo += "TIMESTAMP ";
						} else if (coltype.equalsIgnoreCase("DATETIME")
								&& times.equalsIgnoreCase("u")) {
							outinfo += "TIMESTAMP AUTO UPDATE ";
						} else if (coltype.equalsIgnoreCase("INTEGER")
								|| coltype.equalsIgnoreCase("BIGINT")) {
							outinfo += coltype + " ";
							if (is_serial.equalsIgnoreCase("T")) {
								outinfo += " IDENTITY(1,1) ";
							}
						} else {
							outinfo += coltype + " ";
						}
						if (notnull.equalsIgnoreCase("T")) {
							outinfo += " NOT NULL ";
						}
						if (defval != null) {
							outinfo += " DEFAULT " + defval + " ";
						}
						if (comms != null) {
							outinfo += " COMMENT '" + comms + "' ";
						}
						outinfo += ",\r\n";
					}
					outinfo = outinfo
							.substring(0, outinfo.length() - 3) + ")";
					if (oparti_num != null
							&& !oparti_num.equals("null")) {
						String partis = get_parti(conn, oparti_t, oparti_k,
								oparti_au, oparti_asp, oparti_num,
								otabid);
						outinfo += partis;
					} else {
						outinfo += ";";
					}


					writer.write(outinfo + "\r\n");
					writer.flush();
					String idxstr = get_index(conn, otabid, osche, otab);
					if (idxstr.length() > 0) {
						writer.write(idxstr + "\r\n");
						writer.flush();
					}
					// String consstr = get_constraint(otabid, osche,
					// otab);
					// if (consstr.length() > 0) {
					// writer.write(consstr + "\r\n\r\n");
					// writer.flush();
					// }

					StringBuffer ck_cons = get_ckcons(conn, otabid, osche, otab);
					if (ck_cons != null) {
						writer.write(ck_cons.toString() + "\r\n");
						writer.flush();
					}
					writer.write("---end table---\r\n");
					if(tableName != null){
						String consstr = get_constraint(conn, otabid, osche, otab);
						if (consstr.length() > 0) {
							writer.write("---foreign key "+schemaName+"."+tableName+"---\r\n");
							writer.write(consstr + "\r\n");
							writer.write("---end foreign key---\r\n");
							writer.flush();
						}
					}
				}
				writer.write(commstr.toString() + "\r\n");
				writer.flush();
				if(tableName == null)
					get_db_fk_cons(conn, writer);
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeSt(st);
			FetlUtil.closeRs(rs);
			FetlUtil.closePs(ps);
		}
	}

	public void expView(BufferedWriter writer, Connection conn,String viewName,String schemaName) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		try{
			String sql = "select view_name,define from dba_views a,dba_schemas b where a.schema_id = b.schema_id and b.schema_name='"+schemaName+"'";
			if(viewName != null)
			    sql += " and view_name = '" + viewName+"'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				writer.write("---view "+schemaName+"."+rs.getString(1).toUpperCase()+"---"+"\r\n");
				String view = rs.getString(2).toUpperCase();
				viewName = viewName.toUpperCase();
				if(!view.contains(".") || view.indexOf(".",0) > view.indexOf(viewName, 0)){
					view = FetlUtil.replace(viewName, schemaName, view);
				}
				writer.write(view+"\r\n");
				writer.write("---end view---\r\n");
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expSequence(BufferedWriter writer, Connection conn,String sequenceName,String schemaName) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		try{
			String sql = "select seq_name, curr_val, min_val, max_val, step_val, is_cycle, cache_val from dba_sequences a, dba_schemas b where a.schema_id = b.schema_id and schema_name = '"+schemaName+"'";
			if(sequenceName != null)
				sql += " and seq_name = '" + sequenceName+"'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				StringBuffer createSequence = new StringBuffer();
				createSequence.append("create sequence ").append(schemaName).append(".").append(rs.getString(1)).append(" start with ").append(rs.getString(2))
				              .append(" minvalue ").append(rs.getString(3)).append(" maxvalue ").append(rs.getString(4))
				              .append(" increment by ").append(rs.getString(5));
				if(rs.getString(6).equals("T")){
					createSequence.append(" cycle");
				}
				if(rs.getInt(7) != 1){
					createSequence.append(" cache ").append(rs.getInt(7));
				}
				writer.write("---sequence "+schemaName+"."+rs.getString(1).toUpperCase()+"---"+"\r\n");
				writer.write(createSequence.toString()+"\r\n");
				writer.write("---end sequence---\r\n");
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expTrigger(BufferedWriter writer, Connection conn,String triggerName, String schemaName) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		try{
			String sql = "select trig_name,define from dba_triggers a, dba_schemas b where a.schema_id = b.schema_id and b.schema_name = '"+schemaName+"'";
			if(triggerName != null)
			    sql += " and trig_name = '" + triggerName+"'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				writer.write("---trigger "+schemaName+"."+rs.getString(1).toUpperCase()+"---"+"\r\n");
				String tri = rs.getString(2).toUpperCase();
				triggerName = triggerName.toUpperCase();
				if(!tri.contains(".") || tri.indexOf(".",0) > tri.indexOf(triggerName, 0)){
					tri = FetlUtil.replace(triggerName, schemaName, tri);
				}
				writer.write(tri+"\r\n");
				writer.write("---end trigger---\r\n");
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expProcedure(BufferedWriter writer, Connection conn,String procedureName,String schemaName) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		try{
			String sql = "select proc_name,define from dba_procedures a,dba_schemas b where a.schema_id=b.schema_id and b.schema_name='"+schemaName+"'";
			if(procedureName != null)
			    sql += " and proc_name = '" + procedureName+"'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				writer.write("---procedure "+schemaName+"."+rs.getString(1).toUpperCase()+"---"+"\r\n");
				String proc = rs.getString(2).toUpperCase();
				procedureName = procedureName.toUpperCase();
				if(!proc.contains(".") || proc.indexOf(".",0) > proc.indexOf(procedureName, 0)){
						proc = FetlUtil.replace(procedureName, schemaName, proc);
				}
				writer.write(proc+"\r\n");
				writer.write("---end procedure---\r\n");
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expPackage(BufferedWriter writer, Connection conn,String packName,String schemaName) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		try{
			String sql = "select pack_name,spec,body from dba_packages a,dba_schemas b where a.schema_id=b.schema_id and b.schema_name='"+schemaName+"' and A.is_sys='false'";
			if(packName != null)
			    sql += " and pack_name = '" + packName + "'";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				writer.write("---package "+schemaName+"."+rs.getString(1).toUpperCase()+"---"+"\r\n");
				String packHead = rs.getString(2).toUpperCase();
				packName = packName.toUpperCase();
				if(!packHead.contains(".") || packHead.indexOf(".",0) > packHead.indexOf(packName, 0)){
					packHead = FetlUtil.replace(packName, schemaName, packHead);
				}
				writer.write(packHead+";\r\n");
				String packBody = rs.getString(3).toUpperCase();
				if(!packBody.contains(".") || packBody.indexOf(".",0) > packBody.indexOf(packName, 0)){
					packBody = FetlUtil.replace(packName, schemaName, packBody);
				}
				writer.write(packBody+"\r\n");
				writer.write("---end package---\r\n");
			}
		} catch (Exception e){
			throw e;
		} finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
		}
	}

	public void expUser(BufferedWriter writer, Connection conn,String userName) throws Exception{
		LoadPermission loadPermission= new LoadPermission();
		try {
			writer.write("---user "+ userName +"---"+"\r\n");
			writer.write((loadPermission.loadPermissionSql(conn, userName).toString())+"\r\n");
			writer.write("---end user---" + "\r\n");
		} catch (Exception e) {
			throw e;
		}
	}

	public void expRole(BufferedWriter writer, Connection conn,String roleName) throws Exception{
		LoadPermission loadPermission= new LoadPermission();
		try {
			writer.write("---role "+ roleName +"---"+"\r\n");
			writer.write(loadPermission.loadPermissionSql(conn, roleName) + "\r\n");
			writer.write("---end role---"+ "\r\n");
		} catch (Exception e) {
			throw e;
		}
	}

	public void expData(BufferedWriter writer, Connection conn, List<String> tableNames, File file,StringBuffer sb) throws Exception{
		String path = file.getParentFile().getPath();
		if(tableNames == null){
			Statement st = null;
			ResultSet rs = null;
			try{
				String sql = "select table_name from dba_tables";
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				while(rs.next()){
					expData(writer,conn,rs.getString(1),path);
				}
			} finally{
				FetlUtil.closeRs(rs);
				FetlUtil.closeSt(st);
			}
		} else{
			for(String tableName:tableNames){
				log.info("开始备份表{}数据",tableName);
				try {
					expData(writer,conn,tableName,path);
				} catch (Exception e) {
					sb.append(e.getMessage()+"\n");
				}
			}
		}
	}

	public void expData(BufferedWriter writer,Connection conn, String tableName,String path) throws Exception{

		File expFile = new File(path +"/DATA_"+tableName+".exp");
		String columns = expData(conn, tableName, "select * from "+ tableName, expFile);
		writer.write("---data "+tableName.toUpperCase()+"---\r\n");
		writer.write(columns+"\r\n");
		writer.write(expFile.getName()+"\r\n");
		writer.write("---end data---\r\n");
	}

	public String expData(String file,String tableName,List<String> cols,String where,Connection conn) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			StringBuffer sb = new StringBuffer();
			if(cols == null || cols.size() == 0){
                sb.append("*");
            }else{
                for(int i = 0;i < cols.size();i++){
                    if(i == 0){
                        sb.append(cols.get(i));
                    }else{
                        sb.append(",").append(cols.get(i));
                    }
                }
            }
			sql.append("select ").append(sb.toString()).append(" from ").append(tableName);
			if(where != null && where.length()>0){
                sql.append(" where ").append(where);
            }
			File file1= new File(file);
			StringBuffer returnString = new StringBuffer();
			String columns = expData(conn, tableName, sql.toString(), file1);
			returnString.append("---data ").append(tableName.toUpperCase()).append("---\r\n")
                        .append(columns).append("\r\n")
                        .append(file1.getName()).append("\r\n")
                        .append("---end data---\r\n");
			return returnString.toString();
		} catch (Exception e){
			log.error(OwnException.get(e));
			throw e;

		}
	}

	public String expData(String fileName,String tableName,String sql,String parentId) throws SQLException{
		StringBuffer returnString = new StringBuffer();
		sql += " limit 1";
		Connection conn = null;
		Statement st = null;
		ResultSetMetaData rsmd = null;
		StringBuffer columns =  new StringBuffer();
		DataSourceContextHolder.setDataSource(parentId);
		DynamicDataSource dynamicDataSource= SpringUtil.getBean(DynamicDataSource.class);
		try {
			//conn = FetlUtil.get_conn(url+"&char_set=utf8");
			conn=dynamicDataSource.getConnection();
			st = conn.createStatement();
			rsmd = st.executeQuery(sql).getMetaData();
			for(int i = 1;i <= rsmd.getColumnCount();i++ ){
			   if(i!=1){
				   columns.append("\t");
			   }
			   columns.append(rsmd.getColumnName(i)).append("|").append(rsmd.getColumnType(i));
			}
			returnString.append("---data ").append(tableName.toUpperCase()).append("---\r\n")
            			.append(columns).append("\r\n")
            			.append(fileName).append("\r\n")
            			.append("---end data---\r\n");
			return returnString.toString();
		} catch (SQLException e) {
			throw e;
		} finally {
			DataSourceContextHolder.clearDataSource();
			if(conn!=null){
				conn.close();
			}
			FetlUtil.closeSt(st);
			FetlUtil.closeConn(conn);
		}
	}

	public String expData(Connection conn, String tableName, String sql, File file) throws Exception{
		DataOutputStream dos = null;
		ByteArrayOutputStream tempData = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		StringBuffer columns =  new StringBuffer();
		try{
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file),64 * 1024));
			tempData = new ByteArrayOutputStream(64 * 1024);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rsmd = rs.getMetaData();
			int fnum = rsmd.getColumnCount();
		    for(int i = 1;i <= rsmd.getColumnCount();i++ ){
			   if(i!=1){
				   columns.append("\t");
			   }
			   columns.append(rsmd.getColumnName(i)).append("|").append(rsmd.getColumnType(i));
		    }
		    byte[] signbytes = new byte[(fnum + 3) / 4];
			int rowLen = 0;
			int idx = 0, off = 0;
			while(rs.next()){
				initBytes(signbytes);
				rowLen = signbytes.length;
				for (int i = 1; i <= fnum; i++){
					byte[] fdata = null;
					idx = (i - 1) / 4;
					off = (i - 1) % 4;
					if (rsmd.getColumnType(i) == Types.CLOB || rsmd.getColumnType(i) == Types.NCLOB
							|| rsmd.getColumnType(i) == Types.BLOB || rsmd.getColumnType(i) == Types.BINARY){
						fdata = rs.getBytes(i);
					} else{
						fdata = rs.getString(i) == null ? null : rs.getString(i).getBytes("utf-8");
					}
					if (fdata == null){
						signbytes[idx] = (byte) (signbytes[idx] | SignByte.getNul(off));
					} else if(fdata.length == 0){
						signbytes[idx] = (byte) (signbytes[idx] | SignByte.getZero(off));
					} else if (fdata.length > Short.MAX_VALUE){
						signbytes[idx] = (byte) (signbytes[idx] | SignByte.getLen(off));
						tempData.write(Int2Bytes(fdata.length));
						tempData.write(fdata);
						rowLen += 4 + fdata.length;
					} else if (fdata.length > 0){
						tempData.write(Short2Bytes(fdata.length));
						tempData.write(fdata);
						rowLen += 2 + fdata.length;
					}
				}
				dos.writeInt(rowLen);// 琛岄暱
				dos.write(signbytes);// 闀垮害鍙婄┖鏍囪瘑
				tempData.writeTo(dos);
				tempData.reset();
			}
		} catch (Exception e){
			throw e;
		}  finally {
			FetlUtil.closeRs(rs);
			FetlUtil.closeSt(st);
			FetlUtil.closeOutput(tempData);
			FetlUtil.closeOutput(dos);
		}
		return columns.toString();
	}

	public String expMetaData(Connection conn, Map<Type, List<String>> expInfo, String filepath) {
		BufferedWriter writer = null;
		StringBuffer sb = new StringBuffer();
		try {

			File file = new File(filepath);
			//if (file.isDirectory()) {
				//file=new File(file+"/index.sql");
				/*String db = null;
				int lastIndex = url.indexOf("?");
				if(lastIndex != -1){
					db = url.substring(url.lastIndexOf("/"), lastIndex);
				}else{
					db = url.substring(url.lastIndexOf("/"));
				}
				file = new File(filepath+ System.getProperty("file.separator") + db + "_metadata.sql");*/
			//}

			//conn = FetlUtil.get_conn(url+"&char_set=utf8");
			writer = new BufferedWriter(new FileWriter(file, false));
			if(expInfo == null){
//				expSequence(writer, conn, null);
//				expTable(writer, conn, null);
//				expView(writer, conn, null);
//				expProcedure(writer, conn, null);
//				expTrigger(writer, conn, null);
//				expPackage(writer, conn, null);
//				expData(writer, conn, null ,file);
			}else{
				if (expInfo.containsKey(Type.ROLE)) {
					List<String> roleNames = expInfo.get(Type.ROLE);
					for(String roleName:roleNames){
						try {
							expRole(writer, conn, roleName);
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					}
				}
				if (expInfo.containsKey(Type.USER)) {
					List<String> userNames = expInfo.get(Type.USER);
					for(String userName:userNames){
						try {
							expUser(writer, conn, userName);
						} catch (Exception e) {
							sb.append(e.getMessage()).append("\n");
						}
					}
				}
				if (expInfo.containsKey(Type.SCHEMA)) {
					List<String> schemas = expInfo.get(Type.SCHEMA);
					if(schemas == null){
//						expSequence(writer, conn, null);
					}else{
						for(String schema:schemas){
							try {
								expSchema(writer, conn, schema);
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.SEQUENCE)){
					List<String> sequenceNames = expInfo.get(Type.SEQUENCE);
					if(sequenceNames == null){
//						expSequence(writer, conn, null);
					}else{
						for(String sequenceName:sequenceNames){
							String[] s = sequenceName.split("\\.");
							try {
								expSequence(writer, conn, s[1],s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.TABLE)){
					List<String> tableNames = expInfo.get(Type.TABLE);
					if(tableNames == null){
//						expTable(writer, conn, null);
					}else{
						for(String tableName:tableNames){
							String[] s = tableName.split("\\.");
							try {
								expTable(writer, conn, s[1], s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.VIEW)){
					List<String> viewNames = expInfo.get(Type.VIEW);
					if(viewNames == null){
//						expView(writer, conn, null);
					}else{
						for(String viewName:viewNames){
							String[] s = viewName.split("\\.");
							try {
								expView(writer, conn, s[1], s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.PROCEDURE)){
					List<String> procedureNames = expInfo.get(Type.PROCEDURE);
					if(procedureNames == null){
//						expProcedure(writer, conn, null);
					}else{
						for(String procedureName:procedureNames){
							String[] s = procedureName.split("\\.");
							try {
								expProcedure(writer, conn, s[1], s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.TRIGGER)){
					List<String> triggerNames = expInfo.get(Type.TRIGGER);
					if(triggerNames == null){
//						expTrigger(writer, conn, null);
					}else{
						for(String triggerName:triggerNames){
							String[] s = triggerName.split("\\.");
							try {
								expTrigger(writer, conn, s[1], s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}
				if(expInfo.containsKey(Type.PACKAGE)){
					List<String> packageNames = expInfo.get(Type.PACKAGE);
					if(packageNames == null){
//						expPackage(writer, conn, null);
					}else{
						for(String packageName:packageNames){
							String[] s = packageName.split("\\.");
							try {
								expPackage(writer, conn, s[1], s[0].toUpperCase());
							} catch (Exception e) {
								sb.append(e.getMessage()).append("\n");
							}
						}
					}
				}

	            if(expInfo.containsKey(Type.DATA)){
	            	List<String> tableNames = expInfo.get(Type.DATA);
	            	try {
	            		expData(writer, conn, tableNames, file,sb);
	            	} catch (Exception e) {
						sb.append(e.getMessage()).append("\n");
					}
	            }
			}
		} catch (Exception e) {
			sb.append(e.getMessage()).append("\n");
			log.error(OwnException.get(e));
		} finally {
			FetlUtil.closeWriter(writer);
		}
		return sb.toString();
	}


	static byte[] initBytes(byte[] bbs){
		for (int i = 0; i < bbs.length; i++){
			bbs[i] = 0;
		}
		return bbs;
	}

	static byte[] Int2Bytes(int x){
		byte[] bb = new byte[4];
		bb[0] = (byte) (x >> 24);
		bb[1] = (byte) (x >> 16);
		bb[2] = (byte) (x >> 8);
		bb[3] = (byte) (x >> 0);
		return bb;
	}

	static byte[] Short2Bytes(int x){
		byte[] bb = new byte[2];
		bb[0] = (byte) (x >> 8);
		bb[1] = (byte) (x >> 0);
		return bb;
	}

	static int Bytes2Int(byte[] bb){
		return ((int) bb[0] << 24) | ((int) bb[0] << 16) | ((int) bb[0] << 8) | ((int) bb[0] << 0);
	}

	public enum SignByte{//null鏄┖zero鏄┖涓�
		b0nul(0x01), b1nul(0x04), b2nul(0x10), b3nul(0x40), b0len(0x02), b1len(0x08), b2len(0x20), b3len(0x80),
		b0zero(0x03),b1zero(0x0c),b2zero(0x30),b3zero(0xc0);
		private int val;

		private SignByte(int n)
		{
			this.val = n;
		}

		public static byte getLen(int i){
			byte ret = 0x00;
			switch (i)
			{
			case 0:
				ret = (byte) b0len.val;
				break;
			case 1:
				ret = (byte) b1len.val;
				break;
			case 2:
				ret = (byte) b2len.val;
				break;
			case 3:
				ret = (byte) b3len.val;
				break;

			}
			return ret;
		}

		public static byte getZero(int i){
			byte ret = 0x00;
			switch (i)
			{
			case 0:
				ret = (byte) b0zero.val;
				break;
			case 1:
				ret = (byte) b1zero.val;
				break;
			case 2:
				ret = (byte) b2zero.val;
				break;
			case 3:
				ret = (byte) b3zero.val;
				break;
			}
			return ret;
		}

		public static byte getNul(int i){
			byte ret = 0x00;
			switch (i){
			case 0:
				ret = (byte) b0nul.val;
				break;
			case 1:
				ret = (byte) b1nul.val;
				break;
			case 2:
				ret = (byte) b2nul.val;
				break;
			case 3:
				ret = (byte) b3nul.val;
				break;
			}
			return ret;
		}
	}
}
