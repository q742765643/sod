package com.piesat.sod.system.rpc.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.DbFileDao;
import com.piesat.sod.system.entity.DbFileEntity;
import com.piesat.sod.system.mapper.DbFileMapper;
import com.piesat.sod.system.rpc.api.DbFileService;
import com.piesat.sod.system.rpc.dto.DbFileDto;
import com.piesat.sod.system.rpc.mapstruct.DbFileMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;


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
	
	@Autowired
	private DbFileMapstruct dbFileMapstruct;
	
	@Autowired
	private DbFileMapper dbFileMapper;
	
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
	public PageBean findPageData(PageForm<DbFileDto> pageForm) throws Exception{
		DbFileEntity dfe = dbFileMapstruct.toEntity(pageForm.getT());
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		if(!StringUtil.isEmpty(dfe.getFileName())) dfe.setFileName("%"+dfe.getFileName()+"%");
		if(!StringUtil.isEmpty(dfe.getFileSuffix())) dfe.setFileSuffix("%"+dfe.getFileSuffix()+"%");
		
		List<DbFileEntity> data = dbFileMapper.selectList(dfe);
		PageInfo<DbFileEntity> pageInfo = new PageInfo<>(data);
		
		List<DbFileDto> dtoData = dbFileMapstruct.toDto(pageInfo.getList());
		PageBean page = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);
		
		/*
		 * StringBuffer sb = new StringBuffer();
		 * 
		 * sb.append( " SELECT " +
		 * " record_id,file_type,file_name,file_stor_name,file_stor_path, " +
		 * " update_time,file_picture,file_suffix " + " FROM dmin_db_file " +
		 * " where 1=1 "); if(null != dfe.getFileType() &&
		 * !StringUtils.isEmpty(dfe.getFileType())) { sb.append(
		 * " and file_type = '"+dfe.getFileType()+"' "); } if(null != dfe.getFileName()
		 * && !StringUtils.isEmpty(dfe.getFileName())) {
		 * sb.append(" and file_name like '%"+dfe.getFileName()+"%' "); } if(null !=
		 * dfe.getFileSuffix() && !StringUtils.isEmpty(dfe.getFileSuffix())) {
		 * sb.append( " and file_suffix like '%"+dfe.getFileSuffix()+"%' "); } if(null
		 * != dfe.getStartDate() && !StringUtils.isEmpty(dfe.getStartDate())) {
		 * sb.append( " and  update_time >= '"+dfe.getStartDate()+" 00:00:00' "); }
		 * if(null != dfe.getEndDate() && !StringUtils.isEmpty(dfe.getEndDate())) {
		 * sb.append(" and  update_time <= '"+dfe.getEndDate()+" 59:59:59' "); }
		 * sb.append( "order by update_time desc");
		 */
//		PageBean page = this.queryByNativeSQLPageList(sb.toString(), DbFileEntity.class, null, pageForm);
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
			dfe.setUpdateTime(now);
			dfe.setFileType(fileType);
			dfe.setFileStorName(mf.getOriginalFilename());
			dfe.setFileStorPath(filePath);
			dfe.setFilePictrue("文档");
			//上传文件到服务器指定路径
			FileCopyUtils.copy(mf.getBytes(), new File(filePath));
			dbFileDao.save(dfe);
		}
	}

	/**
	 *  根据id删除
	 * @description 
	 * @author wlg
	 * @date 2019-12-23 08:20
	 * @param recordIds
	 * @throws Exception
	 */
	@Override
	public void deleteByIds(String recordIds) throws Exception {
		String[] ids = recordIds.split(",");
		for(String id:ids) {
			DbFileEntity df = dbFileDao.findById(id).orElse(null);
			if(null != df) {
				String filePath = df.getFileStorPath();
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
				}
				dbFileDao.delete(df);
			}
		}
		
	}

}
