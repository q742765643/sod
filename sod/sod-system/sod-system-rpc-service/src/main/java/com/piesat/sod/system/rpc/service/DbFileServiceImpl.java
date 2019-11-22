package com.piesat.sod.system.rpc.service;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.page.PageBean;
import com.piesat.common.jpa.page.PageForm;
import com.piesat.sod.system.dao.DbFileDao;
import com.piesat.sod.system.entity.DbFileEntity;
import com.piesat.sod.system.rpc.api.DbFileService;


/** 数据库文件 业务处理
*@description
*@author wlg
*@date 2019年11月20日下午1:56:46
*
*/
@Service("dbFileService")
public class DbFileServiceImpl extends BaseService<DbFileEntity> implements DbFileService {
	
	@Autowired
	private DbFileDao dbFileDao;
	
//	@Autowired
//	private DbFileMapper dbFileMapper;
	
	@Value("${serverfile.dbfile}")
	private String fileArr;

	/**
	 *  获取分页数据
	 * @description 
	 * @author wlg
	 * @date 2019-11-20 13:58
	 * @param params
	 * @param pageForm
	 * @return
	 */
	@Override
	public PageBean findPageData(Map<String, Object> params, PageForm pageForm) throws Exception{
	
		String sql = " SELECT "
				+ " record_id,file_type,file_name,file_stor_name,file_stor_path, "
				+ " update_time,file_picture,file_suffix "
				+ " FROM dmin_db_file "
				+ " where 1=1 ";
		if(null != params.get("fileType") && !StringUtils.isEmpty(params.get("fileType")+"")) {
			sql += " and file_type = '"+params.get("fileType")+"' ";
		}
		if(null != params.get("fileName") && !StringUtils.isEmpty(params.get("fileName")+"")) {
			sql += " and file_name like '%"+params.get("fileName")+"%' ";
		}
		if(null != params.get("fileSuffix") && !StringUtils.isEmpty(params.get("fileSuffix")+"")) {
			sql += " and file_suffix like '%"+params.get("fileSuffix")+"%' ";
		}
		if(null != params.get("fileStartDate") && !StringUtils.isEmpty(params.get("fileStartDate")+"")) {
			sql += " and  update_time >= '"+params.get("fileStartDate")+" 00:00:00' ";
		}
		if(null != params.get("fileEndDate") && !StringUtils.isEmpty(params.get("fileEndDate")+"")) {
			sql += " and  update_time <= '"+params.get("fileEndDate")+" 59:59:59' ";
		}
		if(null != params.get("field") && !StringUtils.isEmpty(params.get("field")+"")
				&& null != params.get("order") && !StringUtils.isEmpty(params.get("order")+"")) {
			sql += " order by "+params.get("field") + params.get("order");
		}else {
			sql += "order by update_time desc";
		}
		PageBean page = this.queryByNativeSQLPageList(sql, DbFileEntity.class, null, pageForm);
		return page;
	}

	/**
	 *  获取dao
	 * @description 
	 * @author wlg
	 * @date 2019-11-20 13:58
	 * @return
	 */
	@Override
	public BaseDao<DbFileEntity> getBaseDao() {
		return dbFileDao;
	}

	/**
	 * 保存上传文件
	 * @description 
	 * @author wlg
	 * @date 2019-11-21 10:05
	 * @param request
	 * @throws Exception
	 */
	@Override
	public void save(MultipartHttpServletRequest request) throws Exception {
		// 获取文件存储路径 , 如果没有 , 创建
		if(!Paths.get(fileArr).toFile().exists())
			Paths.get(fileArr).toFile().mkdirs();
		
		List<MultipartFile> fileList = request.getFiles("file");
		Date now = new Date();
		for(MultipartFile mf:fileList) {
			DbFileEntity dfe = new DbFileEntity();
			//文件路径
			String filePath = fileArr + mf.getOriginalFilename();
			String fileSuffix = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf('.'));
			String fileType = request.getParameter("fileType");
			dfe.setFileName(mf.getOriginalFilename());
			dfe.setFileSuffix(fileSuffix);
			dfe.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
			dfe.setFileType(fileType);
			dfe.setFileStorName(mf.getOriginalFilename());
			dfe.setFileStorPath(filePath);
			dfe.setFilePictrue("文档");
			//上传文件到服务器指定路径
			FileCopyUtils.copy(mf.getBytes(), new File(filePath));
			dbFileDao.save(dfe);
		}
	}

}
