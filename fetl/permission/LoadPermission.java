package com.xugu.fetl.permission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Vector;



public class LoadPermission {
	
	private String sql_user; 
	
	private String sql_role; 

	private Vector<Object> rowdata; 
	
	private Statement stmt_user;
	
	private ResultSet rs_user;
	
	private Statement stmt_role;
	
	private ResultSet rs_role;
	
	private String sql;
	
	private Statement stmt;
	
	private ResultSet rs;
	
	/**
	 * 当前用户id
	 */
	private int userId;

	public Vector<Object> loadPermission(Connection currentConn,String userName, int type){	//type = 0 中文; type = 1 英文	
		Object[] parameters;
		MessageFormat messageFormat;

		/* ************************************************************ */

		parameters = new String[]{"'"+userName+"'"};
		messageFormat= new MessageFormat("select user_id from dba_users where is_role=false and user_name={0} and db_id=current_db_id");
		sql_user = messageFormat.format(parameters);
		messageFormat= new MessageFormat("select user_id from dba_roles where is_role=true and user_name={0} and db_id=current_db_id");
		sql_role = messageFormat.format(parameters);
		
		try {
			stmt_user = currentConn.createStatement();
			rs_user = stmt_user.executeQuery(sql_user);
			stmt_role = currentConn.createStatement();
			rs_role = stmt_role.executeQuery(sql_role);
			if(rs_user.next()){
				userId = rs_user.getInt(1);
			}else{
				rs_role.next();
				userId = rs_role.getInt(1);
			}
			rs_user.close();
			rs_role.close();
			stmt_role.close();
			stmt_user.close();
		} catch (SQLException e) {
			e.getMessage();
			}
			
		/* ************************** 库级权限 ************************** */
		rowdata = new Vector<Object>();
		parameters = new String[]{Integer.toString(userId)};
		messageFormat= new MessageFormat("select u.user_name from dba_roles u,dba_role_members m where m.user_id={0} and m.role_id=u.user_id and m.db_id=u.db_id and u.db_id=current_db_id");
		sql = messageFormat.format(parameters);
		
		try {
			stmt = currentConn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				for(Popedom.DBPopedom dbpopedom : Popedom.DBPopedom.values()){
					rowdata.add(dbpopedom);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
			}
		
		/* ************************** 库级权限 ************************** */
		rowdata = new Vector<Object>();
		parameters = new String[]{Integer.toString(userId)};
		messageFormat= new MessageFormat("select object_type,authority,regrant from dba_acls where grantee_id={0} and object_id=0 and db_id=current_db_id");
		sql = messageFormat.format(parameters);
		
		try {
			stmt = currentConn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				for(Popedom.DBPopedom dbpopedom : Popedom.DBPopedom.values()){
					if(dbpopedom.getObjectType().getTypeId() == rs.getInt(1)
							&& (rs.getInt(2) & dbpopedom.getDbAcl().getCode()) == dbpopedom.getDbAcl().getCode()){
//						rowdata = new Vector<Object>();
						rowdata.add(dbpopedom.getKey());
						//rowdata.add(currentConn.getRs().getInt(3)==0?false:true);
//						data.add(rowdata);
					}
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
			}

		/* ************************* 对象级权限 ************************* */	
		
		parameters = new String[]{Popedom.ObjectType.SCHEMA.getTypeName(),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name,a.regrant "
				+ "from all_acls a join dba_schemas s on(a.object_id<0 and abs(a.object_id)=s.schema_id) where a.grantee_id={1} order by s.schema_id asc)");
		sql = messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.TABLE.getTypeName(),Integer.toString(Popedom.ObjectType.TABLE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.table_name,a.regrant "
				+ "from dba_acls a join (all_tables t join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=t.table_id) "
				+ "where a.grantee_id={2} order by s.schema_id,t.table_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.VIEW.getTypeName(),Integer.toString(Popedom.ObjectType.VIEW.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||v.view_name,a.regrant "
				+ "from dba_acls a join (all_views v join all_schemas s on(v.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=v.view_id) "
				+ "where a.grantee_id={2} order by s.schema_id,v.view_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.SEQUENCE.getTypeName(),Integer.toString(Popedom.ObjectType.SEQUENCE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||q.seq_name,a.regrant "
				+ "from dba_acls a join (all_sequences q join all_schemas s on(q.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=q.seq_id) "
				+ "where a.grantee_id={2} order by s.schema_id,q.seq_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.PACKAGE.getTypeName(),Integer.toString(Popedom.ObjectType.PACKAGE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||p.pack_name,a.regrant "
				+ "from dba_acls a join (all_packages p join all_schemas s on(p.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=p.pack_id) "
				+ "where a.grantee_id={2} order by s.schema_id,p.pack_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.PROCEDURE.getTypeName(),Integer.toString(Popedom.ObjectType.PROCEDURE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||p.proc_name,a.regrant "
				+ "from dba_acls a join (all_procedures p join all_schemas s on(p.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=p.proc_id) "
				+ "where a.grantee_id={2} order by s.schema_id,p.proc_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.TRIGGER.getTypeName(),Integer.toString(Popedom.ObjectType.TRIGGER.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.trig_name,a.regrant "
				+ "from dba_acls a join (all_triggers t join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=t.trig_id) "
				+ "where a.grantee_id={2} order by s.schema_id,t.trig_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.COLUMN.getTypeName(),Integer.toString(Popedom.ObjectType.COLUMN.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.table_name||''.''||+c.col_name,a.regrant "
				+ "from dba_acls a join ((all_columns c join all_tables t on(c.table_id=t.table_id)) join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} "
				+ "and t.table_id=shr(a.object_id,10) and c.col_no=bit_and(a.object_id,1023)) where a.grantee_id={2} order by s.schema_id,t.table_id,c.col_no asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.VIEW_COLUMN.getTypeName(),Integer.toString(Popedom.ObjectType.VIEW_COLUMN.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||v.view_name||''.''||c.col_name,a.regrant "
				+ "from dba_acls a join ((all_view_columns c join all_views v on(c.view_id=v.view_id)) join all_schemas s on(v.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} "
				+ "and v.view_id=shr(a.object_id,10) and c.col_no=bit_and(a.object_id,1023)) where a.grantee_id={2} order by s.schema_id,v.view_id,c.col_no asc)");
		sql += messageFormat.format(parameters);

		try {
			stmt = currentConn.createStatement();getClass();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if(rs.getString(3).equals(Popedom.ObjectType.SCHEMA.getTypeName())){
					for(Popedom.SCHPopedom schpopedom : Popedom.SCHPopedom.values()){
						if(rs.getInt(1) == schpopedom.getObjectType().getTypeId()
								&& (rs.getInt(2) & schpopedom.getDbAcl().getCode()) == schpopedom.getDbAcl().getCode()){
							if (type==0) {
								rowdata.add(schpopedom.getKey() + ":" + "\"" + this.rs.getString(4) + "\"");						
							}else {
								rowdata.add(schpopedom.getValue()+ ":" +
										"\""+rs.getString(4)+"\"" 
										);								
							}							
						}
					}
				}
				else if(rs.getString(3).equals(Popedom.ObjectType.COLUMN.getTypeName())){
					for(Popedom.COLPopedom colpopedom : Popedom.COLPopedom.values()){
						if((rs.getInt(1) == Popedom.ObjectType.COLUMN.getTypeId()
								|| rs.getInt(1) == Popedom.ObjectType.VIEW_COLUMN.getTypeId())
								&& (rs.getInt(2) & colpopedom.getObjAcl().getCode()) == colpopedom.getObjAcl().getCode()){
							if (type==0) {
								rowdata.add(colpopedom.getKey() + ":" + "\""
										+ this.rs.getString(4).substring(0, this.rs.getString(4).indexOf(".")) + "\""
										+ "." + "\""
										+ this.rs.getString(4).substring(this.rs.getString(4).indexOf(".") + 1,
												this.rs.getString(4).lastIndexOf("."))
										+ "\"" + "." + "\""
										+ this.rs.getString(4).substring(this.rs.getString(4).lastIndexOf(".") + 1,
												this.rs.getString(4).length())
										+ "\"");			
							} else {
								rowdata.add(colpopedom.getValue() + ":" +
										"\""+rs.getString(4).substring(0, rs.getString(4).indexOf("."))+"\""
										+"."
										+"\""+rs.getString(4).substring(rs.getString(4).indexOf(".")+1, rs.getString(4).lastIndexOf("."))+"\""
										+"."
										+"\""+rs.getString(4).substring(rs.getString(4).lastIndexOf(".")+1, rs.getString(4).length())+"\""
										);
							}
						}
					}
				}
				else{
					for(Popedom.OBJPopedom objpopedom : Popedom.OBJPopedom.values()){
						if(rs.getInt(1) == objpopedom.getObjectType().getTypeId()
								&& (rs.getInt(2) & objpopedom.getObjAcl().getCode()) == objpopedom.getObjAcl().getCode()){
							if (type == 0) {
								rowdata.add(objpopedom.getKey() + ":" + "\""
										+ this.rs.getString(4).substring(0, this.rs.getString(4).indexOf(".")) + "\""
										+ "." + "\""
										+ this.rs.getString(4).substring(this.rs.getString(4).indexOf(".") + 1,
												this.rs.getString(4).length())
										+ "\"");				
							} else {
								rowdata.add(objpopedom.getValue() + ":" +
										"\""+rs.getString(4).substring(0, rs.getString(4).indexOf("."))+"\""
										+"."
										+"\""+rs.getString(4).substring(rs.getString(4).indexOf(".")+1, rs.getString(4).length())+"\""
										);
							}
						}
					}
				}
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.getMessage();				
			}
		return rowdata;
		}			
	
	public String loadPermissionSql(Connection currentConn,String userName){
		Object[] parameters;
		MessageFormat messageFormat;		
		StringBuffer permissionSql = new StringBuffer();
		/* ************************************************************ */

		parameters = new String[]{"'"+userName+"'"};
		messageFormat= new MessageFormat("select user_id from dba_users where is_role=false and user_name={0} and db_id=current_db_id");
		sql_user = messageFormat.format(parameters);
		messageFormat= new MessageFormat("select user_id from dba_roles where is_role=true and user_name={0} and db_id=current_db_id");
		sql_role = messageFormat.format(parameters);
		
		try {
			stmt_user = currentConn.createStatement();
			rs_user = stmt_user.executeQuery(sql_user);
			stmt_role = currentConn.createStatement();
			rs_role = stmt_role.executeQuery(sql_role);
			if(rs_user.next()){
				userId = rs_user.getInt(1);
				permissionSql.append("create user " + userName + " identified by '123456';" +"\r\n");
			}else{
				rs_role.next();
				userId = rs_role.getInt(1);
				permissionSql.append("create role " + userName + ";\r\n");
			}
			rs_user.close();
			rs_role.close();
			stmt_role.close();
			stmt_user.close();
		} catch (SQLException e) {
			e.getMessage();
			}
			
		/* ************************** 库级权限 ************************** */
		rowdata = new Vector<Object>();
		parameters = new String[]{Integer.toString(userId)};
		messageFormat= new MessageFormat("select u.user_name from dba_roles u,dba_role_members m where m.user_id={0} and m.role_id=u.user_id and m.db_id=u.db_id and u.db_id=current_db_id");
		sql = messageFormat.format(parameters);
		
		try {
			stmt = currentConn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){				
				for(Popedom.DBPopedom dbpopedom : Popedom.DBPopedom.values()){
					if(dbpopedom.getObjectType().getTypeId() == rs.getInt(1)
							&& (rs.getInt(2) & dbpopedom.getDbAcl().getCode()) == dbpopedom.getDbAcl().getCode()){
					permissionSql.append("grant " + dbpopedom + " to " + userName+ ";\r\n");
					}
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
			}
		
		/* ************************** 库级权限 ************************** */
		rowdata = new Vector<Object>();
		parameters = new String[]{Integer.toString(userId)};
		messageFormat= new MessageFormat("select object_type,authority,regrant from dba_acls where grantee_id={0} and object_id=0 and db_id=current_db_id");
		sql = messageFormat.format(parameters);
		
		try {
			stmt = currentConn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				for(Popedom.DBPopedom dbpopedom : Popedom.DBPopedom.values()){
					if(dbpopedom.getObjectType().getTypeId() == rs.getInt(1)
							&& (rs.getInt(2) & dbpopedom.getDbAcl().getCode()) == dbpopedom.getDbAcl().getCode()){
//						rowdata = new Vector<Object>();
						permissionSql.append("grant " + dbpopedom + " to " + userName + ";\r\n");
						//rowdata.add(currentConn.getRs().getInt(3)==0?false:true);
//						data.add(rowdata);
					}
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
			}

		/* ************************* 对象级权限 ************************* */	
		
		parameters = new String[]{Popedom.ObjectType.SCHEMA.getTypeName(),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name,a.regrant "
				+ "from all_acls a join dba_schemas s on(a.object_id<0 and abs(a.object_id)=s.schema_id) where a.grantee_id={1} order by s.schema_id asc)");
		sql = messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.TABLE.getTypeName(),Integer.toString(Popedom.ObjectType.TABLE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.table_name,a.regrant "
				+ "from dba_acls a join (all_tables t join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=t.table_id) "
				+ "where a.grantee_id={2} order by s.schema_id,t.table_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.VIEW.getTypeName(),Integer.toString(Popedom.ObjectType.VIEW.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||v.view_name,a.regrant "
				+ "from dba_acls a join (all_views v join all_schemas s on(v.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=v.view_id) "
				+ "where a.grantee_id={2} order by s.schema_id,v.view_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.SEQUENCE.getTypeName(),Integer.toString(Popedom.ObjectType.SEQUENCE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||q.seq_name,a.regrant "
				+ "from dba_acls a join (all_sequences q join all_schemas s on(q.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=q.seq_id) "
				+ "where a.grantee_id={2} order by s.schema_id,q.seq_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.PACKAGE.getTypeName(),Integer.toString(Popedom.ObjectType.PACKAGE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||p.pack_name,a.regrant "
				+ "from dba_acls a join (all_packages p join all_schemas s on(p.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=p.pack_id) "
				+ "where a.grantee_id={2} order by s.schema_id,p.pack_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.PROCEDURE.getTypeName(),Integer.toString(Popedom.ObjectType.PROCEDURE.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||p.proc_name,a.regrant "
				+ "from dba_acls a join (all_procedures p join all_schemas s on(p.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=p.proc_id) "
				+ "where a.grantee_id={2} order by s.schema_id,p.proc_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.TRIGGER.getTypeName(),Integer.toString(Popedom.ObjectType.TRIGGER.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.trig_name,a.regrant "
				+ "from dba_acls a join (all_triggers t join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} and abs(a.object_id)=t.trig_id) "
				+ "where a.grantee_id={2} order by s.schema_id,t.trig_id asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.COLUMN.getTypeName(),Integer.toString(Popedom.ObjectType.COLUMN.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||t.table_name||''.''||+c.col_name,a.regrant "
				+ "from dba_acls a join ((all_columns c join all_tables t on(c.table_id=t.table_id)) join all_schemas s on(t.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} "
				+ "and t.table_id=shr(a.object_id,10) and c.col_no=bit_and(a.object_id,1023)) where a.grantee_id={2} order by s.schema_id,t.table_id,c.col_no asc)");
		sql += messageFormat.format(parameters);
		
		sql +=" union ";
		
		parameters = new String[]{Popedom.ObjectType.VIEW_COLUMN.getTypeName(),Integer.toString(Popedom.ObjectType.VIEW_COLUMN.getTypeId()),Integer.toString(userId)};
		messageFormat = new MessageFormat("(select a.object_type,a.authority,''{0}'',s.schema_name||''.''||v.view_name||''.''||c.col_name,a.regrant "
				+ "from dba_acls a join ((all_view_columns c join all_views v on(c.view_id=v.view_id)) join all_schemas s on(v.schema_id=s.schema_id)) on(a.object_id>0 and a.object_type={1} "
				+ "and v.view_id=shr(a.object_id,10) and c.col_no=bit_and(a.object_id,1023)) where a.grantee_id={2} order by s.schema_id,v.view_id,c.col_no asc)");
		sql += messageFormat.format(parameters);

		try {
			stmt = currentConn.createStatement();getClass();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if(rs.getString(3).equals(Popedom.ObjectType.SCHEMA.getTypeName())){
					for(Popedom.SCHPopedom schpopedom : Popedom.SCHPopedom.values()){
						if(rs.getInt(1) == schpopedom.getObjectType().getTypeId()
								&& (rs.getInt(2) & schpopedom.getDbAcl().getCode()) == schpopedom.getDbAcl().getCode()){
							permissionSql.append("grant " + schpopedom.getValue()+" in schema "
									+"\"" + this.rs.getString(4) + "\""
									+" to \""+userName+"\""+";\r\n" );
						}
					}
				}
				else if(rs.getString(3).equals(Popedom.ObjectType.COLUMN.getTypeName())){
					for(Popedom.COLPopedom colpopedom : Popedom.COLPopedom.values()){
						if((rs.getInt(1) == Popedom.ObjectType.COLUMN.getTypeId()
								|| rs.getInt(1) == Popedom.ObjectType.VIEW_COLUMN.getTypeId())
								&& (rs.getInt(2) & colpopedom.getObjAcl().getCode()) == colpopedom.getObjAcl().getCode()){
							permissionSql.append("grant " + colpopedom.getValue()+"("
									+this.rs.getString(4).substring(this.rs.getString(4).lastIndexOf(".") + 1,
									this.rs.getString(4).length()) + ")" + " on "
									+ "\"" + this.rs.getString(4).substring(0, this.rs.getString(4).indexOf(".")) + "\""
									+ "." + "\""
									+ this.rs.getString(4).substring(this.rs.getString(4).indexOf(".") + 1,
									this.rs.getString(4).lastIndexOf("."))+"\"" 
									+ " to " + userName+";\r\n");
						}
					}
				}
				else{
					for(Popedom.OBJPopedom objpopedom : Popedom.OBJPopedom.values()){
						if(rs.getInt(1) == objpopedom.getObjectType().getTypeId()
								&& (rs.getInt(2) & objpopedom.getObjAcl().getCode()) == objpopedom.getObjAcl().getCode()){
							permissionSql.append("grant " + objpopedom.getValue() + " on "
									+ "\"" + this.rs.getString(4).substring(0, this.rs.getString(4).indexOf(".")) + "\""
									+ "." + "\""
									+ this.rs.getString(4).substring(this.rs.getString(4).indexOf(".") + 1,
											this.rs.getString(4).length())
									+ "\""
									+ " to " +userName +";"+"\r\n");
						}
					}
				}
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.getMessage();				
			}
		return permissionSql.toString();
	}
}
	
