package com.piesat.schedule.client.util.fetl.permission;


/**
 * 权限
 * @author hp
 */
public class Popedom {
	
	/**
	 * 库级权限
	 * @author hp
	 */
	public enum DBPopedom{
		
		DBA					("DBA", 			"DBA",					ObjectType.NONE,				DBAcls.ACL_DBA),
		//SSO					("SSO", 			"SSO",					ObjectType.NONE,				DBAcls.ACL_SSO),
		//AUDITOR					("AUDITOR", 			"AUDITOR",				ObjectType.NONE,				DBAcls.ACL_AUDITOR),
		//BACKUP				("可备份", 			"BACKUP",				ObjectType.NONE,				DBAcls.ACL_BACKUP_ANY),
		//RESTORE				("可恢复", 			"RESTORE",				ObjectType.NONE,				DBAcls.ACL_RESTORE_ANY),
		//TRACE					("可连接", 			"TRACE",				ObjectType.SESSION,				DBAcls.ACL_TRACE),       //2014-03-31 add by wsy
		CREATE_ANY_DATABASE			("可创建任何数据库", 			"CREATE ANY DATABASE",			ObjectType.DATABASE,			        DBAcls.ACL_CRE_ANY),
		ALTER_ANY_DATABASE			("可修改任何数据库", 			"ALTER ANY DATABASE",			ObjectType.DATABASE,			        DBAcls.ACL_ALT_ANY),
		DROP_ANY_DATABASE			("可删除任何数据库", 			"DROP ANY DATABASE",			ObjectType.DATABASE,			        DBAcls.ACL_DROP_ANY),
		CREATE_ANY_SCHEMA			("可创建任何模式", 			"CREATE ANY SCHEMA",			ObjectType.SCHEMA,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_SCHEMA			("可修改任何模式", 			"ALTER ANY SCHEMA",			ObjectType.SCHEMA,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_SCHEMA				("可删除任何模式", 			"DROP ANY SCHEMA",			ObjectType.SCHEMA,				DBAcls.ACL_DROP_ANY),		
		CREATE_ANY_TABLE			("可创建任何表", 			"CREATE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_TABLE				("可修改任何表结构", 			"ALTER ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_TABLE				("可删除任何表", 			"DROP ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_DROP_ANY),
		REFERENCES_ANY_TABLE		        ("可引用任何表", 			"REFERENCES ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_REF_ANY),
		SELECT_ANY_TABLE			("可查询任何表", 			"SELECT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_READ_ANY),
		INSERT_ANY_TABLE			("可插入记录在任何表", 		"INSERT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_INSERT_ANY),
		DELETE_ANY_TABLE			("可删除记录在任何表", 		"DELETE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_DELETE_ANY),
		UPDATE_ANY_TABLE			("可更新记录在任何表", 		"UPDATE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_UPDATE_ANY),
		//VACUUM_ANY_TABLE			("可整理任何表", 			"VACUUM ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_VACUUM_ANY),
		//ENCRYPT_ANY_TABLE			("可加密任何表", 			"ENCRYPT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_ENCRYPT_ANY),
		CREATE_ANY_VIEW				("可创建任何视图", 			"CREATE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_VIEW				("可修改任何视图结构", 		"ALTER ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_VIEW				("可删除任何视图", 			"DROP ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_DROP_ANY),
		SELECT_ANY_VIEW				("可查询任何视图", 			"SELECT ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_READ_ANY),
		INSERT_ANY_VIEW				("可插入记录在任何视图", 		"INSERT ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_INSERT_ANY),
		DELETE_ANY_VIEW				("可删除记录在任何视图", 		"DELETE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_DELETE_ANY),
		UPDATE_ANY_VIEW				("可更新记录在任何视图", 		"UPDATE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_UPDATE_ANY),
		CREATE_ANY_SEQUENCE			("可创建任何序列值", 			"CREATE ANY SEQUENCE",			ObjectType.SEQUENCE,		        	DBAcls.ACL_CRE_ANY),
		ALTER_ANY_SEQUENCE			("可修改任何序列值", 			"ALTER ANY SEQUENCE",			ObjectType.SEQUENCE,			        DBAcls.ACL_ALT_ANY),
		DROP_ANY_SEQUENCE			("可删除任何序列值", 			"DROP ANY SEQUENCE",			ObjectType.SEQUENCE,			        DBAcls.ACL_DROP_ANY),
		SELECT_ANY_SEQUENCE			("可读任何序列值", 			"SELECT ANY SEQUENCE",			ObjectType.SEQUENCE,			        DBAcls.ACL_READ_ANY),
		UPDATE_ANY_SEQUENCE			("可更新何序列值", 			"UPDATE ANY SEQUENCE",			ObjectType.SEQUENCE,			        DBAcls.ACL_UPDATE_ANY),
		REFERENCES_ANY_SEQUENCE		        ("可引用任何序列值", 			"REFERENCES ANY SEQUENCE",		ObjectType.SEQUENCE,			        DBAcls.ACL_REF_ANY),
		CREATE_ANY_PACKAGE			("可创建任何包", 			"CREATE ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_PACKAGE			("可修改任何包", 			"ALTER ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_PACKAGE			("可删除任何包", 			"DROP ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_DROP_ANY),
		EXECUTE_ANY_PACKAGE			("可执行任何包", 			"EXECUTE ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_EXECUTE_ANY),
		CREATE_ANY_PROCEDURE		        ("可创建任何存储过程或函数", 	        "CREATE ANY PROCEDURE",			ObjectType.PROCEDURE,		        	DBAcls.ACL_CRE_ANY),
		ALTER_ANY_PROCEDURE			("可修改任何存储过程或函数", 	        "ALTER ANY PROCEDURE",			ObjectType.PROCEDURE,			        DBAcls.ACL_ALT_ANY),
		DROP_ANY_PROCEDURE			("可删除任何存储过程或函数", 	        "DROP ANY PROCEDURE",			ObjectType.PROCEDURE,			        DBAcls.ACL_DROP_ANY),
		EXECUTE_ANY_PROCEDURE		        ("可执行任何存储过程或函数", 	        "EXECUTE ANY PROCEDURE",		ObjectType.PROCEDURE,			        DBAcls.ACL_EXECUTE_ANY),
		CREATE_ANY_TRIGGER			("可创建任何触发器", 			"CREATE ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_TRIGGER			("可修改任何触发器", 			"ALTER ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_TRIGGER			("可删除任何触发器", 			"DROP ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_DROP_ANY),
		CREATE_ANY_INDEX			("可创建任何索引", 			"CREATE ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_INDEX				("可修改任何索引", 			"ALTER ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_INDEX				("可删除任何索引", 			"DROP ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_TABLESPACE		        ("可创建任何表空间", 			"CREATE ANY TABLESPACE",		ObjectType.TABLESPACE,		 	        DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_TABLESPACE		        ("可修改任何表空间",			"ALTER ANY TABLESPACE",			ObjectType.TABLESPACE,			        DBAcls.ACL_ALT_ANY),
		//DROP_ANY_TABLESPACE			("可删除任何表空间", 			"DROP ANY TABLESPACE",			ObjectType.TABLESPACE,			        DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_UNDO_SEGMENT		("可创建任何回滚段", 			"CREATE ANY UNDO SEGMENT",		ObjectType.UNDO_SEGMENT,		        DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_UNDO_SEGMENT		("可修改任何回滚段", 			"ALTER ANY UNDO SEGMENT",		ObjectType.UNDO_SEGMENT,		        DBAcls.ACL_ALT_ANY),
		//DROP_ANY_UNDO_SEGMENT		        ("可删除任何回滚段", 			"DROP ANY UNDO SEGMENT",		ObjectType.UNDO_SEGMENT,		        DBAcls.ACL_DROP_ANY),
		CREATE_ANY_DATABASE_LINK	        ("可创建任何数据库间连接", 		"CREATE ANY DATABASE LINK",		ObjectType.DATABASE_LINK,		        DBAcls.ACL_CRE_ANY),
		ALTER_ANY_DATABASE_LINK		        ("可修改任何数据库间连接", 		"ALTER ANY DATABASE LINK",		ObjectType.DATABASE_LINK,		        DBAcls.ACL_ALT_ANY),
		DROP_ANY_DATABASE_LINK		        ("可删除任何数据库间连接", 		"DROP ANY DATABASE LINK",		ObjectType.DATABASE_LINK,		        DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_REPLICATION		("可创建任何数据复制", 		"CREATE ANY REPLICATION",		ObjectType.REPLICATION,			        DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_REPLICATION		        ("可修改任何数据复制", 		"ALTER ANY REPLICATION",		ObjectType.REPLICATION,			        DBAcls.ACL_ALT_ANY),
		//DROP_ANY_REPLICATION		        ("可删除任何数据复制", 		"DROP ANY REPLICATION",			ObjectType.REPLICATION,			        DBAcls.ACL_DROP_ANY),
		CREATE_ANY_SYNONYM			("可创建任何同义词", 			"CREATE ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_SYNONYM			("可修改任何同义词", 			"ALTER ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_SYNONYM			("可删除任何同义词", 			"DROP ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_SNAPSHOT			("可创建任何快照", 			"CREATE ANY SNAPSHOT",			ObjectType.SNAPSHOT,			        DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_SNAPSHOT			("可修改任何快照", 			"ALTER ANY SNAPSHOT",			ObjectType.SNAPSHOT,			        DBAcls.ACL_ALT_ANY),
		//DROP_ANY_SNAPSHOT			("可删除任何快照", 			"DROP ANY SNAPSHOT",			ObjectType.SNAPSHOT,			        DBAcls.ACL_DROP_ANY),
		CREATE_ANY_USER				("可创建任何用户", 			"CREATE ANY USER",			ObjectType.USER,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_USER				("可修改任何用户", 			"ALTER ANY USER",			ObjectType.USER,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_USER				("可删除任何用户",			"DROP ANY USER",			ObjectType.USER,				DBAcls.ACL_DROP_ANY),
		CREATE_ANY_JOB				("可创建任何定时作业", 		"CREATE ANY JOB",			ObjectType.JOB,					DBAcls.ACL_CRE_ANY),
		ALTER_ANY_JOB				("可修改任何定时作业", 		"ALTER ANY JOB",			ObjectType.JOB,					DBAcls.ACL_ALT_ANY),
		DROP_ANY_JOB				("可删除任何定时作业", 		"DROP ANY JOB",				ObjectType.JOB,					DBAcls.ACL_DROP_ANY),
		CREATE_ANY_ROLE				("可创建任何角色", 			"CREATE ANY ROLE",			ObjectType.ROLE,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_ROLE				("可修改任何角色", 			"ALTER ANY ROLE",			ObjectType.ROLE,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_ROLE				("可删除任何角色", 			"DROP ANY ROLE",			ObjectType.ROLE,				DBAcls.ACL_DROP_ANY),
//		CREATE_ANY_CLUSTER			("可创建任何簇集", 			"CREATE ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_CRE_ANY),
//		ALTER_ANY_CLUSTER			("可修改任何簇集", 			"ALTER ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_ALT_ANY),
//		DROP_ANY_CLUSTER			("可删除任何簇集", 			"DROP ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_DIR			("可创建任何数据库路径", 		"CREATE ANY DIR",			ObjectType.DIR,					DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_DIR				("可修改任何数据库路径", 		"ALTER ANY DIR",			ObjectType.DIR,					DBAcls.ACL_ALT_ANY),
		//DROP_ANY_DIR				("可删除任何数据库路径", 		"DROP ANY DIR",				ObjectType.DIR,					DBAcls.ACL_DROP_ANY),
		CREATE_ANY_OBJECT			("可创建任何UDT", 			"CREATE ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_CRE_ANY),
		ALTER_ANY_OBJECT			("可修改任何UDT", 			"ALTER ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_ALT_ANY),
		DROP_ANY_OBJECT				("可删除任何UDT", 			"DROP ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_DROP_ANY),
		CREATE_TABLE			        ("可创建表", 			"CREATE TABLE",			        ObjectType.TABLE,				DBAcls.ACL_CREATE),
		CREATE_VIEW			        ("可创建视图", 			"CREATE VIEW",			        ObjectType.VIEW,				DBAcls.ACL_CREATE),
		CREATE_SEQUENCE			        ("可创建序列值", 			"CREATE SEQUENCE",			ObjectType.SEQUENCE,				DBAcls.ACL_CREATE),
		CREATE_PACKAGE			        ("可创建包", 			"CREATE PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_CREATE),
		CREATE_PROCEDURE			("可创建存储过程或函数", 		"CREATE PROCEDURE",			ObjectType.PROCEDURE,				DBAcls.ACL_CREATE),
		CREATE_TRIGGER			        ("可创建触发器", 			"CREATE TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_CREATE),
		CREATE_INDEX			        ("可创建索引", 			"CREATE INDEX",			        ObjectType.INDEX,				DBAcls.ACL_CREATE),
		CREATE_SYNONYM			        ("可创建同义词", 			"CREATE SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_CREATE),
		CREATE_OBJECT			        ("可创建UDT", 			"CREATE OBJECT",			ObjectType.UDT,				        DBAcls.ACL_CREATE);
		
		private String key;
		
		private String value;
		
		private ObjectType objectType;
		
		private DBAcls dbAcl;
		
		private DBPopedom(String key,String value,ObjectType objectType,DBAcls dbAcl){
			this.key = key;
			this.value = value;
			this.objectType = objectType;
			this.dbAcl = dbAcl;
		}
		
		public String getKey(){
			return key;
		}
		
		public String getValue(){
			return value;
		}
		
		public ObjectType getObjectType(){
			return objectType;
		}
		
		public DBAcls getDbAcl(){
			return dbAcl;
		}
		
	}
	
	/**
	 * 模式级权限
	 * @author hp
	 */
	public enum SCHPopedom{
		
		CREATE_ANY_TABLE			("可创建任何表在模式", 				"CREATE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_TABLE				("可修改任何表结构在模式", 				"ALTER ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_TABLE				("可删除任何表在模式", 				"DROP ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_DROP_ANY),
		REFERENCES_ANY_TABLE		        ("可引用任何表在模式", 				"REFERENCES ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_REF_ANY),
		SELECT_ANY_TABLE			("可查询任何表在模式", 				"SELECT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_READ_ANY),
		INSERT_ANY_TABLE			("可插入记录在任何表在模式", 			"INSERT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_INSERT_ANY),
		DELETE_ANY_TABLE			("可删除记录在任何表在模式", 			"DELETE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_DELETE_ANY),
		UPDATE_ANY_TABLE			("可更新记录在任何表在模式", 			"UPDATE ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_UPDATE_ANY),
		//VACUUM_ANY_TABLE			("可整理任何表", 				"VACUUM ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_VACUUM_ANY),
		//ENCRYPT_ANY_TABLE			("可加密任何表", 				"ENCRYPT ANY TABLE",			ObjectType.TABLE,				DBAcls.ACL_ENCRYPT_ANY),
		CREATE_ANY_VIEW				("可创建任何视图在模式", 				"CREATE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_VIEW				("可修改何视图结构在模式", 				"ALTER ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_VIEW				("可删除任何视图在模式", 				"DROP ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_DROP_ANY),
		SELECT_ANY_VIEW				("可查询任何视图在模式", 				"SELECT ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_READ_ANY),
		INSERT_ANY_VIEW				("可插入记录在任何视图在模式", 			"INSERT ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_INSERT_ANY),
		DELETE_ANY_VIEW				("可删除记录在任何视图在模式", 			"DELETE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_DELETE_ANY),
		UPDATE_ANY_VIEW				("可更新记录在任何视图在模式", 			"UPDATE ANY VIEW",			ObjectType.VIEW,				DBAcls.ACL_UPDATE_ANY),
		CREATE_ANY_SEQUENCE			("可创建任何序列值在模式", 				"CREATE ANY SEQUENCE",			ObjectType.SEQUENCE,			DBAcls.ACL_CRE_ANY),
		ALTER_ANY_SEQUENCE			("可修改任何序列值在模式", 				"ALTER ANY SEQUENCE",			ObjectType.SEQUENCE,			DBAcls.ACL_ALT_ANY),
		DROP_ANY_SEQUENCE			("可删除任何序列值在模式", 				"DROP ANY SEQUENCE",			ObjectType.SEQUENCE,			DBAcls.ACL_DROP_ANY),
		SELECT_ANY_SEQUENCE			("可读任何序列值在模式", 				"SELECT ANY SEQUENCE",			ObjectType.SEQUENCE,			DBAcls.ACL_READ_ANY),
		UPDATE_ANY_SEQUENCE			("可更新何序列值在模式", 				"UPDATE ANY SEQUENCE",			ObjectType.SEQUENCE,			DBAcls.ACL_UPDATE_ANY),
		REFERENCES_ANY_SEQUENCE		        ("可引用任何序列值在模式", 				"REFERENCES ANY SEQUENCE",		ObjectType.SEQUENCE,			DBAcls.ACL_REF_ANY),
		CREATE_ANY_PACKAGE			("可创建任何包在模式", 				"CREATE ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_PACKAGE			("可修改任何包在模式", 				"ALTER ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_PACKAGE			("可删除任何包在模式", 				"DROP ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_DROP_ANY),
		EXECUTE_ANY_PACKAGE			("可执行任何包在模式", 				"EXECUTE ANY PACKAGE",			ObjectType.PACKAGE,				DBAcls.ACL_EXECUTE_ANY),
		CREATE_ANY_PROCEDURE		        ("可创建任何存储过程或函数在模式", 		        "CREATE ANY PROCEDURE",			ObjectType.PROCEDURE,			DBAcls.ACL_CRE_ANY),
		ALTER_ANY_PROCEDURE			("可修改任何存储过程或函数在模式", 		        "ALTER ANY PROCEDURE",			ObjectType.PROCEDURE,			DBAcls.ACL_ALT_ANY),
		DROP_ANY_PROCEDURE			("可删除任何存储过程或函数在模式", 		        "DROP ANY PROCEDURE",			ObjectType.PROCEDURE,			DBAcls.ACL_DROP_ANY),
		EXECUTE_ANY_PROCEDURE		        ("可执行任何存储过程或函数在模式", 		        "EXECUTE ANY PROCEDURE",		ObjectType.PROCEDURE,			DBAcls.ACL_EXECUTE_ANY),
		CREATE_ANY_TRIGGER			("可创建任何触发器在模式", 				"CREATE ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_TRIGGER			("可修改任何触发器在模式", 				"ALTER ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_TRIGGER			("可删除任何触发器在模式", 				"DROP ANY TRIGGER",			ObjectType.TRIGGER,				DBAcls.ACL_DROP_ANY),
		CREATE_ANY_INDEX			("可创建任何索引在模式", 				"CREATE ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_INDEX				("可修改任何索引在模式", 				"ALTER ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_INDEX				("可删除任何索引在模式", 				"DROP ANY INDEX",			ObjectType.INDEX,				DBAcls.ACL_DROP_ANY),
		CREATE_ANY_SYNONYM			("可创建任何同义词在模式", 				"CREATE ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_CRE_ANY),
		ALTER_ANY_SYNONYM			("可修改任何同义词在模式", 				"ALTER ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_ALT_ANY),
		DROP_ANY_SYNONYM			("可删除任何同义词在模式", 				"DROP ANY SYNONYM",			ObjectType.SYNONYM,				DBAcls.ACL_DROP_ANY),
		//CREATE_ANY_SNAPSHOT			("可创建任何快照", 				"CREATE ANY SNAPSHOT",			ObjectType.SNAPSHOT,			DBAcls.ACL_CRE_ANY),
		//ALTER_ANY_SNAPSHOT			("可修改任何快照", 				"ALTER ANY SNAPSHOT",			ObjectType.SNAPSHOT,			DBAcls.ACL_ALT_ANY),
		//DROP_ANY_SNAPSHOT			("可删除任何快照", 				"DROP ANY SNAPSHOT",			ObjectType.SNAPSHOT,			DBAcls.ACL_DROP_ANY),
//		CREATE_ANY_CLUSTER			("可创建任何簇集", 				"CREATE ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_CRE_ANY),
//		ALTER_ANY_CLUSTER			("可修改任何簇集", 				"ALTER ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_ALT_ANY),
//		DROP_ANY_CLUSTER			("可删除任何簇集", 				"DROP ANY CLUSTER",			ObjectType.CLUSTER,				DBAcls.ACL_DROP_ANY),
		CREATE_ANY_OBJECT			("可创建任何UDT在模式", 				"CREATE ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_CRE_ANY),
		ALTER_ANY_OBJECT			("可修改任何UDT在模式", 				"ALTER ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_ALT_ANY),
		DROP_ANY_OBJECT				("可删除任何UDT在模式", 				"DROP ANY OBJECT",			ObjectType.UDT,					DBAcls.ACL_DROP_ANY);
		
		private String key;
		
		private String value;
		
		private ObjectType objectType;
		
		private DBAcls dbAcl;
		
		private SCHPopedom(String key,String value,ObjectType objectType,DBAcls dbAcl){
			this.key = key;
			this.value = value;
			this.objectType = objectType;
			this.dbAcl = dbAcl;
		}
		
		public String getKey(){
			return key;
		}
		
		public String getValue(){
			return value;
		}	
		
		public ObjectType getObjectType(){
			return objectType;
		}
		
		public DBAcls getDbAcl(){
			return dbAcl;
		}
		
	}
	
	/**
	 * 对象级权限
	 * @author hp
	 */
	public enum OBJPopedom{
		
		ALTER_TABLE				("可修改表结构",			"ALTER",			ObjectType.TABLE,			OBJAcls.ACL_ALTER),
		DROP_TABLE				("可删除表",			"DROP",				ObjectType.TABLE,			OBJAcls.ACL_DROP),
		REFERENCES_TABLE			("可引用表",			"REFERENCES",		        ObjectType.TABLE,			OBJAcls.ACL_REF),
		SELECT_TABLE				("可查询表",			"SELECT",			ObjectType.TABLE,			OBJAcls.ACL_READ),
		INSERT_TABLE				("可插入记录在表",			"INSERT",			ObjectType.TABLE,			OBJAcls.ACL_INSERT),
		DELETE_TABLE				("可删除记录在表",			"DELETE",			ObjectType.TABLE,			OBJAcls.ACL_DELETE),
		UPDATE_TABLE				("可更新记录在表",			"UPDATE",			ObjectType.TABLE,			OBJAcls.ACL_UPDATE),
//		SELECT_TABLE				("SELECT",			"SELECT",			ObjectType.TABLE,			OBJAcls.ACL_READ),
//		INSERT_TABLE				("INSERT",			"INSERT",			ObjectType.TABLE,			OBJAcls.ACL_INSERT),
//		DELETE_TABLE				("DELETE",			"DELETE",			ObjectType.TABLE,			OBJAcls.ACL_DELETE),
//		UPDATE_TABLE				("UPDATE",			"UPDATE",			ObjectType.TABLE,			OBJAcls.ACL_UPDATE),

		//VACUUM_TABLE				("可整理表",			"VACUUM",			ObjectType.TABLE,			OBJAcls.ACL_VACUUM),
		TRIGGER_TABLE				("可创建触发器在表",		"TRIGGER",			ObjectType.TABLE,			OBJAcls.ACL_TRIG),
		INDXE_TABLE				("可创建索引在表",			"INDEX",			ObjectType.TABLE,			OBJAcls.ACL_INDEX),
		ALTER_VIEW				("可修改视图结构",			"ALTER",			ObjectType.VIEW,			OBJAcls.ACL_ALTER),
		DROP_VIEW				("可删除视图",			"DROP",				ObjectType.VIEW,			OBJAcls.ACL_DROP),
		SELECT_VIEW				("可查询视图",			"SELECT",			ObjectType.VIEW,			OBJAcls.ACL_READ),
		INSERT_VIEW				("可插入记录在视图",		"INSERT",			ObjectType.VIEW,			OBJAcls.ACL_INSERT),
		DELETE_VIEW				("可删除记录在视图",		"DELETE",			ObjectType.VIEW,			OBJAcls.ACL_DELETE),
		UPDATE_VIEW				("可更新记录在视图",		"UPDATE",			ObjectType.VIEW,			OBJAcls.ACL_UPDATE),
		SELECT_SEQUENCE				("可读序列值",			"SELECT",			ObjectType.SEQUENCE,		        OBJAcls.ACL_READ),
		UPDATE_SEQUENCE				("可更新序列值",			"UPDATE",			ObjectType.SEQUENCE,		        OBJAcls.ACL_UPDATE),
		ALTER_SEQUENCE				("可修改序列值",			"ALTER",			ObjectType.SEQUENCE,		        OBJAcls.ACL_ALTER),
		DROP_SEQUENCE				("可删除序列值",			"DROP",				ObjectType.SEQUENCE,		        OBJAcls.ACL_DROP),
		REFERENCES_SEQUENCE			("可引用序列值",			"REFERENCES",		        ObjectType.SEQUENCE,		        OBJAcls.ACL_REF),
		EXECUTE_PACKAGE				("可执行包",			"EXECUTE",			ObjectType.PACKAGE,			OBJAcls.ACL_EXECUTE),
		ALTER_PACKAGE				("可修改包",			"ALTER",			ObjectType.PACKAGE,			OBJAcls.ACL_ALTER),
		DROP_PACKAGE				("可删除包",			"DROP",				ObjectType.PACKAGE,			OBJAcls.ACL_DROP),
		EXECUTE_PROCEDURE			("可执行存储过程或函数",		"EXECUTE",			ObjectType.PROCEDURE,		        OBJAcls.ACL_EXECUTE),
		ALTER_PROCEDURE				("可修改存储过程或函数",		"ALTER",			ObjectType.PROCEDURE,		        OBJAcls.ACL_ALTER),
		DROP_PROCEDURE				("可删除存储过程或函数",		"DROP",				ObjectType.PROCEDURE,		        OBJAcls.ACL_DROP),
		DROP_TRIGGER				("可删除触发器",			"DROP",				ObjectType.TRIGGER,			OBJAcls.ACL_DROP),
		ALTER_TRIGGER				("可修改触发器",			"ALTER",			ObjectType.TRIGGER,			OBJAcls.ACL_ALTER),
		//ALTER_CLUSTER				("可修改簇集",			"ALTER",			ObjectType.CLUSTER,			OBJAcls.ACL_ALTER),
		//DROP_CLUSTER				("可删除簇集",			"DROP",				ObjectType.CLUSTER,			OBJAcls.ACL_DROP),
		EXECUTE_UDT				("可执行UDT",			"EXECUTE",			ObjectType.UDT,				OBJAcls.ACL_EXECUTE),
		ALTER_UDT				("可修改UDT",			"ALTER",			ObjectType.UDT,				OBJAcls.ACL_ALTER),
		DROP_UDT				("可删除UDT",			"DROP",				ObjectType.UDT,				OBJAcls.ACL_DROP);
		
		private String key;
		
		private String value;
		
		private ObjectType objectType;
		
		private OBJAcls objAcl;
		
		private OBJPopedom(String key,String value,ObjectType objectType,OBJAcls objAcl){
			this.key = key;
			this.value = value;
			this.objectType = objectType;
			this.objAcl = objAcl;
		}
		
		public String getKey(){
			return key;
		}
		
		public String getValue(){
			return value;
		}	
		
		public ObjectType getObjectType(){
			return objectType;
		}
		
		public OBJAcls getObjAcl(){
			return objAcl;
		}
		
	}
	
	/**
	 * 列级权限
	 * @author hp
	 */
	public enum COLPopedom{
		
		SELECT_COLUMN("可读列",		"SELECT",		ObjectType.COLUMN,		OBJAcls.ACL_READ),
		UPDATE_COLUMN("可更新列",		"UPDATE",		ObjectType.COLUMN,		OBJAcls.ACL_UPDATE);
		
		private String key;
		
		private String value;
		
		private ObjectType objectType;
		
		private OBJAcls objAcl;
		
		private COLPopedom(String key,String value,ObjectType objectType,OBJAcls objAcl){
			this.key = key;
			this.value = value;
			this.objectType = objectType;
			this.objAcl = objAcl;
		}
		
		public String getKey(){
			return key;
		}
		
		public String getValue(){
			return value;
		}
		
		public ObjectType getObjectType(){
			return objectType;
		}
		
		public OBJAcls getObjAcl(){
			return objAcl;
		}
		
	}
	
	/**
	 * 对象类型
	 * @author hp
	 */
	public enum ObjectType{
		
		NONE			("",				0),
		DATABASE 		("数据库", 			1),
		TABLESPACE		("表空间",			2),
		UNDO_SEGMENT	        ("回滚段",			3),
		SCHEMA			("模式",				4),
		TABLE			("表",				5),
		COLUMN			("列",				6),
		PROCEDURE		("存储过程或函数",	                7),
		SEQUENCE		("序列值",			8),
		VIEW			("视图",				9),
		INDEX			("索引",				10),
		TRIGGER			("触发器",			11),
		DATABASE_LINK	        ("数据库间连接",		        12),
		REPLICATION		("数据复制",			13),
		SNAPSHOT		("快照",				14),
		SYNONYM			("同义词",			15),
		USER			("用户",				16),
		ROLE			("角色",				17),
		PACKAGE			("包",				18),
		UDT			("UDT",				19),
		DIR			("文件目录",			20),
		CLUSTER			("簇集",				21),
		JOB			("定时作业",			22),
		POLICY			("策略",				23),
		USER_POLICY		("用户策略",			24),
		TABLE_POLICY	        ("表策略",			25),
		AUDIT			("审计",				26),
		SESSION			("连接",				27),    //2014-03-31 add by wsy
		VIEW_COLUMN		("列",				28),	//设计之初并未料到“表”下的列和“视图”下的列object_type值不同，故在此追加。
		DOMAIN		        ("域",				29);	//以区别 COLUMN("列", 6)
		
		private String typeName;
		
		private int typeId;
		
		private ObjectType(String typeName,int typeId){
			this.typeName = typeName;
			this.typeId = typeId;
		}
		
		public String getTypeName(){
			return typeName;
		}
		
		public int getTypeId(){
			return typeId;
		}
		
	}
	
	/**
	 * 库级权限掩码表
	 * @author hp
	 */
	public enum DBAcls{
		
		ACL_READ_ANY			(0x1),				/*	可读任何	*/
		ACL_UPDATE_ANY			(0x2),				/*	可更新，在任何  */
		ACL_INSERT_ANY			(0x4),				/*	可插入，在任何  */
		ACL_DELETE_ANY			(0x8),				/*	可删除，在任何  */
		ACL_REF_ANY			(0x10),				/*	可引用任何  */
		ACL_EXECUTE_ANY			(0x20),				/*	可执行任何  */
		ACL_INDEX_ANY			(0x40),				/*	可创建任何索引  */
		ACL_CREATE			(0x80),				
		ACL_CRE_ANY			(0x100),			/*	可创建任何  */
		ACL_ALT_ANY 			(0x200),			/*	可修改任何  */
		ACL_DROP_ANY 			(0x400),			/*	可删除任何  */
		ACL_BACKUP_ANY 			(0x800),			/*	可备份任何  */
		ACL_RESTORE_ANY 		(0x1000),			/*	可恢复任何  */
		ACL_VACUUM_ANY 			(0x2000),			/*	可整理任何  */
		ACL_REPLICATION_ANY 	        (0x4000),			/*	可同步任何  */
		ACL_REFRESH_ANY 		(0x8000),			/*	可刷新任何数据  */
		ACL_GRANT_ANY 			(0x10000),			/*	可授权任何  */
		ACL_ENCRYPT_ANY 		(0x20000),			/*	可加密任何  */
		ACL_CRE_JOB 			(0x40000),			/*	可创建任何定时作业  */
		ACL_TRACE 			(0x80000),			/*	可调试任何连接  */    //2014-03-31 add by wsy
		ACL_AUDITOR 			(0x10000000),		        /*	AUDITOR  */
		ACL_AUDIT_ADMIN 		(0x30000000),		        /*	审计管理权  */
		ACL_SSO 			(0x40000000),		        /*	SSO  */
		ACL_SS_ADMIN 			(0xc0000000),		        /*	安全管理权  */
		ACL_DBA 			(0x7fffff),			/*	DBA  */
		ACL_DBO 			(0xffffff);			/*	DBO  */
		
		private int code;
		
		private DBAcls(int code){
			this.code = code;
		}
		
		public int getCode(){
			return code;
		}
		
	}
	
	/**
	 * 对象级权限掩码表
	 * @author hp
	 */
	public enum OBJAcls{
		
		ACL_READ		(0x1),			/*  可读  */
		ACL_UPDATE		(0x2),			/*  可更新，在  */
		ACL_INSERT		(0x4),			/*  可插入，在  */
		ACL_DELETE		(0x8),			/*  可删除，在  */
		ACL_REF			(0x10),			/*  可引用  */
		ACL_EXECUTE		(0x20),			/*  可执行  */
		ACL_INDEX		(0x40),			/*  可创建索引  */
		ACL_ALTER		(0x80),			/*  可修改  */
		ACL_DROP		(0x100),		/*  可删除  */
		ACL_TRIG		(0x200),		/*  可创建触发器  */
		ACL_VACUUM		(0x400),		/*  可整理  */
		ACL_TAB_ALL		(0x7df),
		ACL_PROC_ALL	        (0x1a0),
		ACL_VIEW_ALL            (0x18f),
		ACL_PACK_ALL            (0x1a0),
		ACL_SEQ_ALL		(0x183),
		ACL_UDT_ALL             (0x1a0);
		
		private int code;
		
		private OBJAcls(int code){
			this.code = code;
		}
		
		public int getCode(){
			return code;
		}
		
	}

}
